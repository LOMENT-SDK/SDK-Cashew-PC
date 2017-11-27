using Cashew;
using Cashew.Utility.Services;
using Core.Data;
using Core.Services;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Ioc;
using Loment.CashewReference.DB.Model;
using System;
using System.Collections.ObjectModel;
using System.Linq;

namespace Core.ViewModel
{
    public class ConversationViewModel : ViewModelBase
    {
        private readonly INavigationService _navService;
        private readonly IPlatformUtility _platformUtility;
        private readonly DataService _dataService;
        private readonly MessagingService _messagingService;
        private readonly IUIDispatcher _dispatcher;

        private ComposeViewModel _compose;
        public ComposeViewModel Compose
        {
            get { return _compose; }
            set { Set(ref _compose, value); }
        }

        public string Id { get; set; }

        public bool IsGroup { get; set; }

        private string _contactName;
        public string ContactName
        {
            get { return _contactName; }
            set { Set(ref _contactName, value); }
        }

        public bool CanEdit
        {
            get
            {
                return Conversation.CashewAccountId == GroupOwner;
            }
        }

        private string _groupOwner;
        public string GroupOwner
        {
            get { return _groupOwner; }
            set { Set(ref _groupOwner, value); RaisePropertyChanged(() => CanEdit); }
        }

        private ObservableCollection<string> _members;
        public ObservableCollection<string> Members
        {
            get { return _members; }
            set { Set(ref _members, value); }
        }

        private OrderedObservableCollection<MessageViewModel> _messages;
        public OrderedObservableCollection<MessageViewModel> Messages
        {
            get { return _messages; }
            set { Set(ref _messages, value); }
        }

        private DbConversation _conversation;
        public DbConversation Conversation
        {
            get { return _conversation; }
            set { Set(ref _conversation, value); }
        }


        public ConversationViewModel(DataService dataService, DbConversation conversation, INavigationService navService, IPlatformUtility platformUtility, IUIDispatcher dispatcher)
        {
            _dataService = dataService;
            _messagingService = dataService.GetMessageingServiceByAccount(conversation.CashewAccountId);
            _navService = navService;
            _platformUtility = platformUtility;
            _dispatcher = dispatcher;

            SetConversation(conversation);

            Messages = new OrderedObservableCollection<MessageViewModel>(MessageDateComparer, true, true);
            Compose = new ComposeViewModel(_dataService, conversation);
            Compose.MessageCreated += OnComposeMessageCreated;
        }

        internal void SetConversation(DbConversation conversation)
        {
            Conversation = conversation;
            IsGroup = conversation.IsGroupConversation;
            ContactName = conversation.Name;
            Id = conversation.ConversationId;
            GroupOwner = conversation.OwnerId;
            Members = new ObservableCollection<string>(conversation.Members.Where(x => x != conversation.OwnerId));
        }

        private void OnComposeMessageCreated(object sender, DbMessage e)
        {
            Messages.Add(new MessageViewModel(e, this, _dataService, _platformUtility, _dispatcher));
        }

        public static int MessageDateComparer(MessageViewModel x, MessageViewModel y)
        {
            return DateTime.Compare(y.Message.SendTime, x.Message.SendTime);
        }


        private RelayCommand _editGroup;
        public RelayCommand EditGroup
        {
            get
            {
                return _editGroup ?? (_editGroup = new RelayCommand(ExecEditGroup, CanEditGroup));
            }
        }

        /// <summary>
        /// Checks whether the EditGroup command is executable
        /// </summary>
        private bool CanEditGroup()
        {
            return IsGroup;
        }

        /// <summary>
        /// Executes the EditGroup command 
        /// </summary>
        private void ExecEditGroup()
        {
            var lomVM = SimpleIoc.Default.GetInstance<LomentAccountViewModel>();
            if (lomVM.SelectedCashewAccount != null)
            {
                _navService.Navigate("/GroupEditPage.xaml", new GroupEditViewModel(Id, lomVM.SelectedCashewAccount.Conversations.Where(x => x.IsGroup == false).Select(x => x.ContactName).ToList(), _messagingService, _navService));
            }
        }


        private RelayCommand _leaveGroup;
        public RelayCommand LeaveGroup
        {
            get
            {
                return _leaveGroup ?? (_leaveGroup = new RelayCommand(ExecLeaveGroup, CanLeaveGroup));
            }
        }

        /// <summary>
        /// Checks whether the LeaveGroup command is executable
        /// </summary>
        private bool CanLeaveGroup()
        {
            return IsGroup;
        }

        /// <summary>
        /// Executes the LeaveGroup command 
        /// </summary>
        private void ExecLeaveGroup()
        {
            _messagingService.LeaveGroupAsync(Id);
        }
    }
}
