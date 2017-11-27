using Loment.CashewReference.DB.Model.Dao.Tables;
using Loment.CashewReference.DB.Services;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

namespace Loment.CashewReference.DB.Model.Dao
{
    /// <summary>
    /// Extensions that map Table objects to Poco DB objects and vice-versa
    /// </summary>
    internal static class DaoMapper
    {

        #region AppConfigTable

        /// <summary>
        /// Maps an AppConfig object to an AppConfigTable row
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        internal static AppConfigTable Map(this DbAppConfig obj)
        {
            /* IMPORTANT! If you add a property, remember to add it to the tbl_AppConfig Map(this tbl_AppConfig obj) method */
            return new AppConfigTable
            {
                Pk = obj.Pk,
                LomentAccountSubscriptionIsActive = obj.LomentAccountSubscriptionIsActive,
                LomentUserName = obj.LomentUserName,
                IsRememberPassword = obj.IsRememberPassword,
                Password = obj.Password,
            };
        }

        /// <summary>
        /// Maps an AppConfigTable row object to an AppConfig object
        /// </summary>
        /// <param name="tbl"></param>
        /// <returns></returns>
        internal static DbAppConfig Map(this AppConfigTable tbl)
        {
            /* IMPORTANT! If you add a property, remember to add it to the AppConfigTable Map(this DbAppConfig obj) */
            return new DbAppConfig
            {
                Pk = tbl.Pk,
                LomentAccountSubscriptionIsActive = tbl.LomentAccountSubscriptionIsActive,
                LomentUserName = tbl.LomentUserName,
                IsRememberPassword = tbl.IsRememberPassword,
                Password = tbl.Password
            };
        }

        #endregion


        #region LomentAccountTable

        /// <summary>
        /// Maps a LomentAccountTable row object to a DbLomentAccount object
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        internal static DbLomentAccount Map(this LomentAccountTable obj)
        {
            /* IMPORTANT! If you add a property, remember to add it to the LomentAccountTable Map(this DbLomentAccount obj) */
            return new DbLomentAccount
            {
                Pk = obj.Pk,
                UserId = obj.UserId,
                UserName = obj.UserName,
                Phone = obj.Phone,
                Email = obj.Email,
                DeviceId = obj.DeviceId,
                Password = obj.Password     // TODO: This must be encrypted in a real-world implementation 
            };
        }

        /// <summary>
        /// Maps a  DbLomentAccount object to a LomentAccountTable row object
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        internal static LomentAccountTable Map(this DbLomentAccount obj)
        {
            /* IMPORTANT! If you add a property, remember to add it to the LomentAccountTable Map(this DbLomentAccount obj) */
            return new LomentAccountTable()
            {
                Pk = obj.Pk,
                UserId = obj.UserId,
                UserName = obj.UserName,
                Phone = obj.Phone,
                Email = obj.Email,
                DeviceId = obj.DeviceId,
                Password = obj.Password     // TODO: This must be encrypted in a real-world implementation 
            };
        }

        #endregion


        #region CashewAccountTable

        /// <summary>
        /// Maps a CashewAccountsTable row to a DbCashewAccount object
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        //internal static DbCashewAccount Map(this CashewAccountsTable obj)
        //{
        //    /* IMPORTANT! If you add a property, remember to add it to the CashewAccountsTable Map(this DbCashewAccount obj) */
        //    return new DbCashewAccount
        //    {
        //        LomentAccountId = obj.LomentAccountId,
        //        UserName = obj.UserName,
        //        Status = obj.Status,
        //        CreateDateUtc = obj.CreateDateUtc,
        //        LastUpdateDateUtc = obj.LastUpdateDateUtc,
        //    };
        //}

        /// <summary>
        /// Maps a DbCashewAccount object to a CashewAccountsTable
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        //internal static CashewAccountsTable Map(this DbCashewAccount obj)
        //{
        //    /* IMPORTANT! If you add a property, remember to add it to the CashewAccountsTable Map(this DbCashewAccount obj) */
        //    return new CashewAccountsTable
        //    {
        //        LomentAccountId = obj.LomentAccountId,
        //        UserName = obj.UserName,
        //        Status = obj.Status,
        //        CreateDateUtc = obj.CreateDateUtc,
        //        LastUpdateDateUtc = obj.LastUpdateDateUtc,
        //    };
        //}

        #endregion


        #region ContactsTable

        /// <summary>
        /// Maps a CashContact Object to a DbContact table row object
        /// </summary>
        /// <param name="obj"></param>
        /// <returns>ContactsTable</returns>
        internal static ContactsTable Map(this DbContact obj)
        {
            /* IMPORTANT! If you add a property, remember to add it to the Table Map(this DbContact obj) */
            return new ContactsTable
            {
                IsCashewAccount = obj.IsCashewAccount,
                SenderId = obj.SenderId,
                Name = obj.Name,
                Email = obj.Email,
                Phone = obj.Phone,
                ImagePath = obj.ImagePath
            };
        }

        internal static ContactsTable Map(this DbCashewAccount obj)
        {
            /* IMPORTANT! If you add a property, remember to add it to the Table Map(this DbContact obj) */
            return new ContactsTable
            {
                IsCashewAccount = true,
                SenderId = obj.UserName,
                CreateDateUtc = obj.CreateDateUtc,
                LastUpdateDateUtc = obj.LastUpdateDateUtc,
            };
        }

        /// <summary>
        /// Maps a Table object to a DbContact
        /// </summary>
        /// <param name="dao"></param>
        /// <returns>DbContact</returns>
        internal static DbContact Map(this ContactsTable dao)
        {
            /* IMPORTANT! If you add a property, remember to add it to the Table Map(this DbContact obj) */
            return new DbContact
            {
                IsCashewAccount = dao.IsCashewAccount,
                SenderId = dao.SenderId,
                Name = dao.Name,
                Email = dao.Email,
                Phone = dao.Phone,
                ImagePath = dao.ImagePath,
            };
        }

        internal static DbCashewAccount Map(this ContactsTable dao, DbLomentAccount lomentAccount)
        {
            /* IMPORTANT! If you add a property, remember to add it to the Table Map(this DbContact obj) */
            return new DbCashewAccount
            {
                UserName = dao.SenderId,
                LomentAccount = lomentAccount,
                CreateDateUtc = dao.CreateDateUtc,
                LastUpdateDateUtc = dao.LastUpdateDateUtc

            };
        }

        #endregion


        #region MessageTable

        /// <summary>
        /// Map a MessageTable row to a DbMessage object
        /// </summary>
        /// <param name="tbl"></param>
        /// <returns>DbMessage</returns>
        internal static DbMessage Map(this MessagesTable tbl, DbConversation conversation = null)
        {
            /* IMPORTANT! If you add a property, remember to add it to the CashewMessage Map(this MessageTable ) */
            return new DbMessage
            {
                CashewAccountId = tbl.CashewAccountId,
                ServerId = tbl.ServerId,
                ConversationId = tbl.ConversationId,
                SenderId = tbl.SenderId,
                Recipient = tbl.Recipient,
                Conversation = conversation,
                Priority = tbl.Priority,
                IsForwardRestricted = tbl.IsForwardRestricted,
                RequestsReadAcknowledgement = tbl.IsForwardRestricted,
                IsReadAcknowledged = tbl.IsReadAcknowledged,
                ExpiryMinutes = tbl.ExpiryMinutes,
                TimeStamp = tbl.TimeStamp,
                IsRead = tbl.IsRead,
                SendTime = tbl.SendTime,
                Content = tbl.Content,
                Direction = tbl.Direction,
                Subject = tbl.Subject,
                IsPartOfGroupConversation = tbl.IsPartOfGroupConversation,
                IsFriendRequest = tbl.IsFriendRequest,
                FriendRequestStatus = tbl.FriendRequestStatus,
                IsMarkedAsDeleted = tbl.IsMarkedAsDeleted
            };
        }

        /// <summary>
        /// Maps a Cashew Message object to a MessageTable object 
        /// The CashewMessageTabel object can then be used to insert new
        /// or update existing rows in the table
        /// </summary>
        /// <param name="obj"></param>
        /// <returns>MessagesTable</returns>
        internal static MessagesTable Map(this DbMessage obj)
        {
            /* IMPORTANT! If you add a property, remember to add it to the MessageTable Map(this CashewMessage obj) */

            return new MessagesTable
            {
                ServerId = obj.ServerId,
                CashewAccountId = obj.CashewAccountId,
                SenderId = obj.SenderId,
                Recipient = obj.Recipient,
                ConversationId = obj.ConversationId,
                Priority = obj.Priority,
                IsForwardRestricted = obj.IsForwardRestricted,
                RequestsReadAcknowledgement = obj.IsForwardRestricted,
                IsReadAcknowledged = obj.IsReadAcknowledged,
                ExpiryMinutes = obj.ExpiryMinutes,
                TimeStamp = obj.TimeStamp,
                IsRead = obj.IsRead,
                SendTime = obj.SendTime,
                Content = obj.Content,
                Direction = obj.Direction,
                Subject = obj.Subject,
                IsPartOfGroupConversation = obj.IsPartOfGroupConversation,
                IsFriendRequest = obj.IsFriendRequest,
                FriendRequestStatus = obj.FriendRequestStatus,
                IsMarkedAsDeleted = obj.IsMarkedAsDeleted
            };
        }

        #endregion


        #region ConversationsTable

        /// <summary>
        /// Maps a Conversation object to a ConversationTable row
        /// </summary>
        /// <param name="obj"></param>
        /// <returns>ConversationsTable</returns>
        internal static ConversationsTable Map(this DbConversation obj)
        {

            return new ConversationsTable()
            {
                ConversationId = obj.ConversationId,
                CashewAccountId = obj.CashewAccountId,
                Name = obj.Name,
                IsGroupConversation = obj.IsGroupConversation,
                OwnerId = obj.OwnerId
            };

        }

        internal static List<GroupMemberTable> MapToMembers(this DbConversation obj)
        {
            return obj.Members.Select(x => new GroupMemberTable { ConversationId = obj.ConversationId, ContactSenderId = x }).ToList();
        }

        /// <summary>
        /// Maps a Conversation Table row to a Conversation object
        /// </summary>
        /// <param name="tbl"></param>
        /// <returns>DbConversation</returns>
        internal static DbConversation Map(this ConversationsTable tbl)
        {
            return new DbConversation()
            {
                ConversationId = tbl.ConversationId,
                CashewAccountId = tbl.CashewAccountId,
                Name = tbl.Name,
                IsGroupConversation = tbl.IsGroupConversation,
                OwnerId = tbl.OwnerId
            };
        }


        #endregion


        #region Attachment

        /// <summary>
        /// Maps a DbAttachment to a AttachmentTable row
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        internal static AttachmentsTable Map(this DbAttachment obj)
        {
            return new AttachmentsTable
            {
                Pk = obj.Pk,
                MessageId = obj.Message.ServerId,
                Padding = obj.Padding,
                Size = obj.Size,
                Name = obj.Name,
            };
        }

        /// <summary>
        /// Maps a AttachmentTable row object to a DbAttachment object
        /// </summary>
        /// <param name="tbl"></param>
        /// <returns></returns>
        internal static DbAttachment Map(this AttachmentsTable tbl, DbMessage message)
        {
            return new DbAttachment
            {
                Pk = tbl.Pk,
                Padding = tbl.Padding,
                Size = tbl.Size,
                Name = tbl.Name,
                Message = message
            };
        }

        #endregion


    }
}