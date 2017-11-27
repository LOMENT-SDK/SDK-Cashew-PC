using Cashew;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using Cashew.Utility.Helper;
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
    /// Root of the partial class used for all access to the database
    /// </summary>
    public partial class DataService
    {
        private Queue<CNMessageBase> _messageProcessingQueue = new Queue<CNMessageBase>();
        private Queue<Tuple<MessagingService, CNGroup>> _groupeProcessingQueue = new Queue<Tuple<MessagingService, CNGroup>>();
        private object _messageLock = new object();
        private object _groupLock = new object();


        Dictionary<MessagingService, DbCashewAccount> _messagingServicesDictionary = new Dictionary<MessagingService, DbCashewAccount>();
        Dictionary<DbCashewAccount, Action<DbMessage, DbEntityAction>> _messageActionDictionary = new Dictionary<DbCashewAccount, Action<DbMessage, DbEntityAction>>();
        Dictionary<DbCashewAccount, Action<DbConversation, DbEntityAction>> _conversationActionDictionary = new Dictionary<DbCashewAccount, Action<DbConversation, DbEntityAction>>();

        CNLomentAccount _lomentAccount;
        LomentService _lomentService;
        DataStore _dataStore;

        internal DataService(LomentService lomentService, DataStore dataStore)
        {
            _lomentService = lomentService;
            _dataStore = dataStore;
        }

        internal async Task Start(CNLomentAccount lomentAccount)
        {
            _lomentAccount = lomentAccount;
            var accounts = await _lomentService.GetCashewAccountsAsync(lomentAccount);

            await CashewAccountsSynchronize(accounts);

            var dbAccounts = await _dataStore.CashewAccountsGetAllListAsync();
            foreach (var item in dbAccounts)
            {
                ActivateCashewAccount(item);
            }

            StartMessagProcessor();
            StartGroupProcessor();
        }

        internal void ActivateCashewAccount(DbCashewAccount account)
        {
            var messagingService = new MessagingService(_lomentAccount, new CNCashewAccount
            {
                CreateDateUtc = account.CreateDateUtc,
                LastUpdateDateUtc = account.LastUpdateDateUtc,
                LomentAccountUserId = account.LomentAccount.UserId,
                Status = account.Status,
                UserName = account.UserName
            });

            _messagingServicesDictionary.Add(messagingService, account);

            messagingService.MessageReceived += OnMessageReceived;
            messagingService.GroupInfoReceived += OnGroupInfoReceived;
        }

        private void StartMessagProcessor()
        {
            Task.Run(async () =>
            {
                while (true)
                {
                    CNMessageBase messageToProcess = null;
                    lock (_messageLock)
                    {
                        if (_messageProcessingQueue.Any())
                            messageToProcess = _messageProcessingQueue.Dequeue();
                    }
                    if (messageToProcess != null)
                    {

                        try
                        {
                            if (messageToProcess is CNMessage)
                            {
                                await ProcessMessage(messageToProcess as CNMessage);
                            }
                            if (messageToProcess is CNFriendRequest)
                            {
                                await ProcessFriendRequest(messageToProcess as CNFriendRequest);
                            }
                        }
                        catch (Exception ex)
                        {
                            Debug.WriteLine(ex);
                        }
                    }
                    else
                    {
                        await Task.Delay(1000);
                    }
                }
            });
        }

        private void StartGroupProcessor()
        {
            Task.Run(async () =>
            {
                while (true)
                {
                    Debug.WriteLine("processing group");
                    Tuple<MessagingService, CNGroup> groupMessageToProcess = null;
                    lock (_groupLock)
                    {
                        if (_groupeProcessingQueue.Any())
                            groupMessageToProcess = _groupeProcessingQueue.Dequeue();
                    }
                    if (groupMessageToProcess != null)
                    {
                        try
                        {
                            await ProcessGroup(groupMessageToProcess.Item1, groupMessageToProcess.Item2);
                        }
                        catch (Exception ex) {
                            Debug.WriteLine(ex);
                        }
                    }
                    else
                    {
                        await Task.Delay(1000);
                    }
                }
            });
        }


        internal async Task<CashewResponse> SyncCashewAccount(DbCashewAccount account)
        {
            var messagingService = _messagingServicesDictionary.Single(x => x.Value.UserName == account.UserName).Key;

            var authResp = await messagingService.AuthenticateAsync();

#if DEBUG
            if (authResp.IsSuccess) // In debug we always sync
#else
            if (authResp.IsSuccess && authResp.Entity.IsSyncRequired)
#endif
            {
                var messages = await _dataStore.MessagesGetListForCashewAccountAsync(account.UserName);
                if (messages.Any())
                {
                    return await messagingService.SyncAsync(messages.Max(x => x.SendTime));
                }
                else
                {
                    return await messagingService.SyncAsync(null);
                }
            }
            else
            {
                return authResp;
            }
        }


        internal void RegisterConversationAction(DbCashewAccount account, Action<DbConversation, DbEntityAction> callback)
        {
            _conversationActionDictionary.Add(account, callback);
        }

        internal void RegisterMessageAction(DbCashewAccount account, Action<DbMessage, DbEntityAction> callback)
        {
            _messageActionDictionary.Add(account, callback);
        }



        private void OnGroupInfoReceived(object sender, CNGroup e)
        {
            lock (_groupLock)
            {
                _groupeProcessingQueue.Enqueue(new Tuple<MessagingService, CNGroup>(sender as MessagingService, e));
            }
        }

        private async Task ProcessGroup(MessagingService messagingService, CNGroup group)
        {
            string cashewAccountId = messagingService.CashewAccount.UserName;
            var dbc = await _dataStore.ConversationsGetByIdAsync(cashewAccountId, group.ConverstaionId);

            var callback = GetConversationActionByAccountId(cashewAccountId);
            if (dbc == null)
            {
                dbc = new DbConversation
                {
                    CashewAccountId = cashewAccountId,
                    ConversationId = group.ConverstaionId,
                    IsGroupConversation = true,
                    Members = group.Member.ToList(),
                    Name = group.Name,
                    OwnerId = group.Owner
                };

                await _dataStore.ConversationCreateAsync(dbc);

                callback(dbc, DbEntityAction.Create);
            }
            else
            {
                dbc.OwnerId = group.Owner;
                dbc.Name = group.Name;
                dbc.Members = group.Member.ToList();

                if (group.Owner == cashewAccountId || group.Member.Any(x => x == cashewAccountId))
                {
                    await _dataStore.ConversationUpdateAsync(dbc);
                    callback(dbc, DbEntityAction.Update);
                }
                else
                {
                    // we are no longer part of the group
                    await _dataStore.ConversationDeleteAsync(dbc);
                    callback(dbc, DbEntityAction.Delete);
                }
            }
        }

        private void OnMessageReceived(object sender, CNMessageBase message)
        {
            lock (_messageLock)
            {
                _messageProcessingQueue.Enqueue(message);
            }
        }

        private void ProcessGroupMessage(CNMessage cNMessage)
        {
            //throw new NotImplementedException();
        }

        private async Task ProcessFriendRequest(CNFriendRequest cNFriendRequest)
        {
            var tuple = await MessageCreateOrUpdate(cNFriendRequest);
            var callback = GetMessagenActionByAccountId(cNFriendRequest.CashewAccount.UserName);
            if (callback != null)
                callback(tuple.Item1, tuple.Item2);
        }

        private async Task ProcessMessage(CNMessage cNMessage)
        {
            var tuple = await MessageCreateOrUpdate(cNMessage);
            if (tuple != null)
            {
                var callback = GetMessagenActionByAccountId(cNMessage.CashewAccount.UserName);
                if (callback != null)
                    callback(tuple.Item1, tuple.Item2);
            }
        }

        internal MessagingService GetMessageingServiceByAccount(string cashewAccountId)
        {
            if (_messagingServicesDictionary.Any(x => x.Value.UserName == cashewAccountId))
            {
                return _messagingServicesDictionary.Single(x => x.Value.UserName == cashewAccountId).Key;
            }
            else
            {
                return null;
            }
        }

        private Action<DbConversation, DbEntityAction> GetConversationActionByAccountId(string cashewAccountId)
        {
            if (_messagingServicesDictionary.Any(x => x.Value.UserName == cashewAccountId))
            {
                return _conversationActionDictionary.SingleOrDefault(x => x.Key.UserName == cashewAccountId).Value;
            }
            else
            {
                return null;
            }
        }

        private Action<DbMessage, DbEntityAction> GetMessagenActionByAccountId(string cashewAccountId)
        {
            if (_messagingServicesDictionary.Any(x => x.Value.UserName == cashewAccountId))
            {
                return _messageActionDictionary.SingleOrDefault(x => x.Key.UserName == cashewAccountId).Value;
            }
            else
            {
                return null;
            }
        }

    }

    internal enum DbEntityAction
    {
        Create,
        Update,
        Delete
    }

}
