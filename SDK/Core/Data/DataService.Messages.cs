using Cashew.Model;
using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Services;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core.Data
{
    /// <summary>
    /// Database access methods and business logic for Messages
    /// </summary>
    public partial class DataService
    {

        internal async Task<Tuple<DbMessage, DbEntityAction>> MessageCreateOrUpdate(CNFriendRequest friendRequest)
        {
            DbMessage dbMessageExisting = await _dataStore.MessageGetByServerIDAsync(friendRequest.ServerId);

            await CreateConversationIfNeeded(friendRequest, false);
            await CreateContactIfNeeded(friendRequest);

            /* Create a new DBMessage object for use in either update or create process */
            DbMessage cbMessageToCreateOrUpdate = new DbMessage()
            {
                ServerId = friendRequest.ServerId,
                CashewAccountId = friendRequest.CashewAccount.UserName,
                ConversationId = friendRequest.ConversationId,
                Content = "",
                ExpiryMinutes = -1,
                IsRead = false,
                IsPartOfGroupConversation = false,
                RequestsReadAcknowledgement = false,
                Sender = await _dataStore.ContactGetBySenderIdAsync(friendRequest.SenderId),
                SendTime = friendRequest.SendTime,
                Subject = friendRequest.Subject,
                TimeStamp = friendRequest.TimeStamp,
                IsReadAcknowledged = false,
                IsForwardRestricted = false,
                IsMarkedAsDeleted = false,
                Priority = MessagePriority.PRIORITY_0,
                Direction = (MessageDirection)friendRequest.Direction,
                IsFriendRequest = true,
                FriendRequestStatus = (FriendRequestStatus)friendRequest.Status,
            };


            if (dbMessageExisting == null)
            {
                /* No message exists with this ServerID so create a new one */
                DbMessage newDbMessage = await _dataStore.MessageCreateAsync(cbMessageToCreateOrUpdate);
                return new Tuple<DbMessage, DbEntityAction>(newDbMessage, DbEntityAction.Create);
            }
            else
            {
                /* Found a message with this ServerID so update it */
                int rowsAffected = await _dataStore.MessageUpdateAsync(cbMessageToCreateOrUpdate);
                return new Tuple<DbMessage, DbEntityAction>(cbMessageToCreateOrUpdate, DbEntityAction.Update);
            }
        }

        internal async Task<Tuple<DbMessage, DbEntityAction>> MessageCreateOrUpdate(CNMessage message)
        {
            DbMessage dbMessageExisting = await _dataStore.MessageGetByServerIDAsync(message.ServerId);

            if (await CreateConversationIfNeeded(message, message.IsPartOfGroupConversation))
            {
                await CreateContactIfNeeded(message);

                /* Create a new DBMessage object for use in either update or create process */
                DbMessage cbMessageToCreateOrUpdate = new DbMessage()
                {
                    ServerId = message.ServerId,
                    CashewAccountId = message.CashewAccount.UserName,
                    ConversationId = message.ConversationId,
                    Conversation = await _dataStore.ConversationsGetByIdAsync(message.CashewAccount.UserName, message.ConversationId),
                    Content = dbMessageExisting != null ? dbMessageExisting.Content : message.Content,
                    ExpiryMinutes = message.ExpiryMinutes,
                    IsRead = message.IsRead,
                    IsPartOfGroupConversation = message.IsPartOfGroupConversation,
                    RequestsReadAcknowledgement = message.RequestsReadAcknowledgement,
                    SenderId = message.SenderId,
                    Sender = await _dataStore.ContactGetBySenderIdAsync(message.SenderId),
                    Recipient = message.IsPartOfGroupConversation ? message.ConversationId : message.Recipients.First(),
                    SendTime = message.SendTime,
                    Subject = message.Subject,
                    TimeStamp = message.TimeStamp,
                    IsReadAcknowledged = message.IsReadAcknowledged,
                    IsForwardRestricted = message.IsForwardRestricted,
                    IsMarkedAsDeleted = message.MarkedAsDeleted,
                    Priority = (MessagePriority)message.Priority,
                    Direction = (MessageDirection)message.Direction,
                    IsFriendRequest = false,
                    FriendRequestStatus = FriendRequestStatus.None,
                };

                if (message.Attachments != null && message.Attachments.Any())
                {
                    cbMessageToCreateOrUpdate.Attachments = message.Attachments.Select(x => new DbAttachment
                    {
                        Message = cbMessageToCreateOrUpdate,
                        Name = x.Name,
                        Padding = x.Padding,
                        Size = x.Size
                    }).ToList();
                }
                else
                {
                    cbMessageToCreateOrUpdate.Attachments = new List<DbAttachment>();
                }

                if (dbMessageExisting == null)
                {
                    /* No message exists with this ServerID so create a new one */
                    DbMessage newDbMessage = await _dataStore.MessageCreateAsync(cbMessageToCreateOrUpdate);


                    return new Tuple<DbMessage, DbEntityAction>(newDbMessage, DbEntityAction.Create);
                }
                else
                {
                    /* Found a message with this ServerID so update it */
                    int rowsAffected = await _dataStore.MessageUpdateAsync(cbMessageToCreateOrUpdate);
                    return new Tuple<DbMessage, DbEntityAction>(cbMessageToCreateOrUpdate, DbEntityAction.Update);
                }
            }
            else
            {
                return null;
            }
        }

        private async Task<bool> CreateConversationIfNeeded(CNMessageBase message, bool isPartOfGroupConversation)
        {

            var conversation = await _dataStore.ConversationsGetByIdAsync(message.CashewAccount.UserName, message.ConversationId);

            if (conversation == null)
            {
                conversation = new DbConversation
                {
                    CashewAccountId = message.CashewAccount.UserName,
                    ConversationId = message.ConversationId,
                    IsGroupConversation = isPartOfGroupConversation,
                };

                if (conversation.IsGroupConversation)
                {
                    var messagingService = GetMessageingServiceByAccount(message.CashewAccount.UserName);
                    var response = await messagingService.GetGroupDetailsAsync(message.ConversationId);
                    if (response.IsSuccess)
                    {
                        conversation.Name = response.Entity.Name;
                        conversation.OwnerId = response.Entity.Owner;
                        conversation.Members = response.Entity.Member.ToList();
                        if(conversation.OwnerId != message.CashewAccount.UserName && conversation.Members.Any(x=>x==message.CashewAccount.UserName) == false)
                        {
                            // we are no longer part of the group
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    conversation.Name = message.Direction == CNMessageDirection.Incoming ? message.SenderId : message.Recipients.First();
                }

                try
                {
                    await _dataStore.ConversationCreateAsync(conversation);
                }
                catch (Exception ex)
                {
                    Debug.WriteLine(ex);
                    return false;
                }

                if (GetConversationActionByAccountId(message.CashewAccount.UserName) != null)
                {
                    GetConversationActionByAccountId(message.CashewAccount.UserName)(conversation, DbEntityAction.Create);
                }
            }

            return true;
        }


        /// <summary>
        /// Retrieve a list of all Messages for a CashewAccount
        /// </summary>
        /// <param name="dataStore"></param>
        /// <param name="cashewAccountID"></param>
        /// <returns></returns>
        internal static async Task<List<DbMessage>> MessagesForAccountGetList(DataStore dataStore, string cashewAccountID)
        {

            List<DbMessage> messages = await dataStore.MessagesGetListForCashewAccountAsync(cashewAccountID);

            return messages;

        }

    }
}
