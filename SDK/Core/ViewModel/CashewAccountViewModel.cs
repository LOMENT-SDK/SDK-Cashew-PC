using Cashew;
using Cashew.Model;
using Cashew.Utility.Services;
using Core.Data;
using Core.Services;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Ioc;
using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Services;
using System.Collections.ObjectModel;
using System.Linq;

namespace Core.ViewModel
{
    public class CashewAccountViewModel : ViewModelBase
    {
        private readonly MessagingService _messagingService;
        private readonly IUIDispatcher _dispatcher;
        private readonly INavigationService _navService;
        private readonly DataStore _dataStore;
        private readonly IPlatformUtility _platformUtility;
        private readonly DataService _dataService;

        public CNLomentAccount LomentAccount { get; private set; }
        public DbCashewAccount CashewAccount { get; private set; }

        private FriendsViewModel _friends;
        public FriendsViewModel Friends
        {
            get { return _friends; }
            set { Set(ref _friends, value); }
        }

        private ObservableCollection<ConversationViewModel> _conversations;
        public ObservableCollection<ConversationViewModel> Conversations
        {
            get { return _conversations; }
            set { Set(ref _conversations, value); }
        }

        private ConversationViewModel _selectedConversation;
        public ConversationViewModel SelectedConversation
        {
            get { return _selectedConversation; }
            set { Set(ref _selectedConversation, value); }
        }

        private bool _isRunning;
        public bool IsRunning
        {
            get { return _isRunning; }
            set { Set(ref _isRunning, value); }
        }

        public CashewAccountViewModel(LomentService lomentService, DataStore dataStore, DataService dataAccess, DbCashewAccount cashewAccount, IUIDispatcher dispatcher, INavigationService navService, IPlatformUtility platformUtility)
        {
            _dataService = dataAccess;
            _dataStore = dataStore;
            _messagingService = _dataService.GetMessageingServiceByAccount(cashewAccount.UserName);
            _dispatcher = dispatcher;
            _navService = navService;
            _platformUtility = platformUtility;

            CashewAccount = cashewAccount;
            Friends = new FriendsViewModel(lomentService, _messagingService);

            _dataService.RegisterConversationAction(CashewAccount, OnConverstionAction);
            _dataService.RegisterMessageAction(CashewAccount, OnMessageAppeared);

            Conversations = new ObservableCollection<ConversationViewModel>();
        }

        private void OnConverstionAction(DbConversation conversation, DbEntityAction action)
        {
            _dispatcher.BeginInvokeOnMainThread(() =>
            {
                switch (action)
                {
                    case DbEntityAction.Create:
                        ConversationViewModel cvm = new ConversationViewModel(_dataService, conversation, _navService, _platformUtility, _dispatcher);
                        Conversations.Add(cvm);
                        if (SelectedConversation == null)
                            SelectedConversation = cvm;
                        break;
                    case DbEntityAction.Update:
                        cvm = Conversations.Single(x => x.Conversation.ConversationId == conversation.ConversationId);
                        cvm.SetConversation(conversation);
                        break;
                    case DbEntityAction.Delete:
                        cvm = Conversations.Single(x => x.Conversation.ConversationId == conversation.ConversationId);
                        Conversations.Remove(cvm);
                        break;
                    default:
                        break;
                }
            });
        }

        private void OnMessageAppeared(DbMessage message, DbEntityAction action)
        {
            _dispatcher.BeginInvokeOnMainThread(() =>
            {
                var cvm = Conversations.SingleOrDefault(x => x.Conversation.ConversationId == message.ConversationId);
                if (cvm != null)
                {
                    if (action == DbEntityAction.Create)
                    {
                        cvm.Messages.Add(new MessageViewModel(message, cvm, _dataService, _platformUtility, _dispatcher));
                    }
                    else if (action == DbEntityAction.Update)
                    {
                        if (message.IsFriendRequest)
                        {
                            var messageVm = cvm.Messages.SingleOrDefault(x => x.Message.ServerId == message.ServerId);
                            if (messageVm != null)
                                cvm.Messages.Remove(messageVm);
                            cvm.Messages.Add(new MessageViewModel(message, cvm, _dataService, _platformUtility, _dispatcher));
                        }
                        else
                        {
                            var messageVm = cvm.Messages.SingleOrDefault(x => x.Message.ServerId == message.ServerId);
                            if (messageVm != null)
                                messageVm.Message = message;
                            else
                                cvm.Messages.Add(new MessageViewModel(message, cvm, _dataService, _platformUtility, _dispatcher));
                        }
                    }
                }
            });
        }


        private RelayCommand _load;
        public RelayCommand Load
        {
            get
            {
                return _load ?? (_load = new RelayCommand(ExecLoad, CanLoad));
            }
        }

        private volatile bool _isLoading;

        /// <summary>
        /// Checks whether the Load command is executable
        /// </summary>
        private bool CanLoad()
        {
            return !_isLoading;
        }

        /// <summary>
        /// Executes the Load command 
        /// </summary>
        private async void ExecLoad()
        {
            _isLoading = true;
            if (IsRunning)
                return;


            var conversations = await _dataStore.ConversationsGetByCashewAccountAsync(CashewAccount.UserName);
            Conversations = new ObservableCollection<ConversationViewModel>(conversations.Select(x => new ConversationViewModel(_dataService, x, _navService, _platformUtility, _dispatcher)));
            SelectedConversation = Conversations.FirstOrDefault();

            foreach (var item in Conversations)
            {
                var messages = await _dataStore.MessagesGetListForConversationAsync(item.Conversation.CashewAccountId, item.Conversation.ConversationId);
                foreach (var message in messages)
                {
                    item.Messages.Add(new MessageViewModel(message, item, _dataService, _platformUtility, _dispatcher));
                }
            }

            var response = await _dataService.SyncCashewAccount(CashewAccount);

            IsRunning = response.IsSuccess;
            _isLoading = false;
        }


        private RelayCommand _createGroup;

        public RelayCommand CreateGroup
        {
            get
            {
                return _createGroup ?? (_createGroup = new RelayCommand(ExecCreateGroup, CanCreateGroup));
            }
        }

        /// <summary>
        /// Checks whether the CreateGroup command is executable
        /// </summary>
        private bool CanCreateGroup()
        {
            return true;
        }

        /// <summary>
        /// Executes the CreateGroup command 
        /// </summary>
        private void ExecCreateGroup()
        {
            var lomVM = SimpleIoc.Default.GetInstance<LomentAccountViewModel>();
            if (lomVM.SelectedCashewAccount != null)
            {
                _navService.Navigate("/GroupEditPage.xaml", new GroupEditViewModel("", Conversations.Where(x => x.IsGroup == false).Select(x => x.ContactName).ToList(), _messagingService, _navService));
            }
        }
    }
}
