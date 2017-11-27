using Cashew;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Microsoft.Practices.ServiceLocation;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace Core.ViewModel
{
    public class FriendsViewModel : ViewModelBase
    {
        private LomentService _lomentService;
        private MessagingService _messagingService;

        private ObservableCollection<CNFriendSuggestion> _friendSuggestions;
        public ObservableCollection<CNFriendSuggestion> FriendSuggestions
        {
            get { return _friendSuggestions; }
            set { Set(ref _friendSuggestions, value); }
        }

        public string FriendSearchTerm { get; set; }

        public FriendsViewModel(LomentService lomentService, MessagingService messagingService)
        {
            _lomentService = lomentService;
            _messagingService = messagingService;

            ServiceLocator.Current.GetInstance<LomentAccountViewModel>().PropertyChanged += FriendsViewModel_PropertyChanged;
        }

        private void FriendsViewModel_PropertyChanged(object sender, System.ComponentModel.PropertyChangedEventArgs e)
        {
            FindFriendSuggestions.RaiseCanExecuteChanged();
        }

        private RelayCommand _findFriendSuggestions;
        public RelayCommand FindFriendSuggestions
        {
            get
            {
                return _findFriendSuggestions ?? (_findFriendSuggestions = new RelayCommand(ExecFindFriendSuggestions, CanFindFriendSuggestions));
            }
        }

        /// <summary>
        /// Checks whether the FindFriendSuggestions command is executable
        /// </summary>
        private bool CanFindFriendSuggestions()
        {
            return ServiceLocator.Current.GetInstance<LomentAccountViewModel>().AuthenticatedLomentAccount != null;
        }

        /// <summary>
        /// Executes the FindFriendSuggestions command 
        /// </summary>
        private async void ExecFindFriendSuggestions()
        {
            var suggestions = await _lomentService.GetCashewFriendSuggestions(ServiceLocator.Current.GetInstance<LomentAccountViewModel>().AuthenticatedLomentAccount, new List<string> { }, new List<string> { FriendSearchTerm });

            FriendSuggestions = new ObservableCollection<CNFriendSuggestion>(suggestions);
        }


        private RelayCommand<CNFriendSuggestion> _sendFriendRequest;
        public RelayCommand<CNFriendSuggestion> SendFriendRequest
        {
            get
            {
                return _sendFriendRequest ?? (_sendFriendRequest = new RelayCommand<CNFriendSuggestion>(ExecSendFriendRequest, CanSendFriendRequest));
            }
        }

        /// <summary>
        /// Checks whether the SendFriendRequest command is executable
        /// </summary>
        private bool CanSendFriendRequest(CNFriendSuggestion val)
        {
            return val != null;
        }

        /// <summary>
        /// Executes the SendFriendRequest command 
        /// </summary>
        private async void ExecSendFriendRequest(CNFriendSuggestion val)
        {
            Task<CashewResponse>[] requests = _messagingService.SendFriendRequestAsync(val);

            await Task.WhenAll(requests);
        }

    }
}
