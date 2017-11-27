using Cashew;
using Cashew.Utility.Services;
using Core.Data;
using Core.Services;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Loment.CashewReference.DB.Model;
using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace Core.ViewModel
{
    public class MessageViewModel : ViewModelBase
    {

        private readonly MessagingService _messagingService;
        private readonly ConversationViewModel _parentViewModel;
        private readonly DataService _dataService;
        private readonly IUIDispatcher _dispatcher;

        private DbMessage _message;
        public DbMessage Message
        {
            get { return _message; }
            set
            {
                Set(ref _message, value);
                IsRead = Message.IsRead;
                RaisePropertyChanged(() => DisplayContent);
                CheckIfExpiryTimerIsNeeded();
            }
        }

        private void CheckIfExpiryTimerIsNeeded()
        {
            if (_message.ExpiryMinutes > 0 && DateTime.UtcNow < _message.SendTime.AddMinutes(_message.ExpiryMinutes))
            {
                // the message expires but is still valid
                // Lets start a loop to check 
                Task.Run(() =>
                {
                    while (true)
                    {
                        if (IsExpired())
                        {
                            break;
                        }
                        Task.Delay(1000);
                    }
                    _dispatcher.BeginInvokeOnMainThread(() =>
                    {
                        RaisePropertyChanged(() => DisplayContent);
                    });
                });
            }
        }

        public string DisplayContent
        {
            get
            {
                if (IsExpired())
                {
                    return "THIS MESSAGE IS EXPIRED.";
                }
                if (_message.RequestsReadAcknowledgement && !_message.IsReadAcknowledged)
                {
                    return "YOU NEED TO ACKNOWLEDGE THIS MESSAGE BEFORE YOU CAN READ IT.";
                }
                else
                {
                    return _message.Content;
                }
            }

        }

        private bool IsExpired()
        {
            return _message.ExpiryMinutes > 0 && DateTime.UtcNow > _message.SendTime.AddMinutes(_message.ExpiryMinutes);
        }

        private ObservableCollection<AttachmentViewModel> _attachments;
        public ObservableCollection<AttachmentViewModel> Attachments
        {
            get { return _attachments; }
            set { Set(ref _attachments, value); }
        }

        private bool _isRead;
        public bool IsRead
        {
            get { return _isRead; }
            set { Set(ref _isRead, value); }
        }

        public MessageViewModel(DbMessage message, ConversationViewModel parentViewModel, DataService dataService, IPlatformUtility platformUtility, IUIDispatcher dispatcher)
        {
            Message = message;
            IsRead = message.IsRead;
            _parentViewModel = parentViewModel;
            _dataService = dataService;
            _messagingService = dataService.GetMessageingServiceByAccount(message.CashewAccountId);
            _dispatcher = dispatcher;

            Attachments = new ObservableCollection<AttachmentViewModel>(message.Attachments.Select(x => new AttachmentViewModel(x, _messagingService, platformUtility)));
        }

        private RelayCommand _delete;
        public RelayCommand Delete
        {
            get
            {
                return _delete ?? (_delete = new RelayCommand(ExecDelete, CanDelete));
            }
        }

        /// <summary>
        /// Checks whether the Delete command is executable
        /// </summary>
        private bool CanDelete()
        {
            return true;
        }

        /// <summary>
        /// Executes the Delete command 
        /// </summary>
        private async void ExecDelete()
        {
            var response = await _messagingService.MarkMessageAsDeletedAsync(Message.ServerId, Message.Sender.SenderId);

            if (response.IsSuccess)
            {
                _parentViewModel.Messages.Remove(this);
            }
        }


        private RelayCommand _markAsRead;
        public RelayCommand MarkAsRead
        {
            get
            {
                return _markAsRead ?? (_markAsRead = new RelayCommand(ExecMarkAsRead, CanMarkAsRead));
            }
        }

        /// <summary>
        /// Checks whether the MarkAsRead command is executable
        /// </summary>
        private bool CanMarkAsRead()
        {
            return true;
        }

        /// <summary>
        /// Executes the MarkAsRead command 
        /// </summary>
        private async void ExecMarkAsRead()
        {
            var response = await _messagingService.MarkMessageAsReadAsync(Message.ServerId.ToString());
        }


        private RelayCommand _markAsAcknowledged;
        public RelayCommand MarkAsAcknowledged
        {
            get
            {
                return _markAsAcknowledged ?? (_markAsAcknowledged = new RelayCommand(ExecMarkAcknowledged, CanMarkAcknowledged));
            }
        }

        /// <summary>
        /// Checks whether the MarkAcknowledged command is executable
        /// </summary>
        private bool CanMarkAcknowledged()
        {
            return true;
        }

        /// <summary>
        /// Executes the MarkAcknowledged command 
        /// </summary>
        private async void ExecMarkAcknowledged()
        {
            var response = await _messagingService.MarkMessageAsAcknowledgedAsync(Message.ServerId.ToString());
        }

        internal void RaiseMessageChanged()
        {
            RaisePropertyChanged(() => Message);
        }

        private RelayCommand _acceptFriendRequest;
        public RelayCommand AcceptFriendRequest
        {
            get
            {
                return _acceptFriendRequest ?? (_acceptFriendRequest = new RelayCommand(ExecAcceptFriendRequest, CanAcceptFriendRequest));
            }
        }

        /// <summary>
        /// Checks whether the AcceptFriendRequest command is executable
        /// </summary>
        private bool CanAcceptFriendRequest()
        {
            return true;
        }

        /// <summary>
        /// Executes the AcceptFriendRequest command 
        /// </summary>
        private async void ExecAcceptFriendRequest()
        {
            var response = await _messagingService.AcceptFriendRequestAsync(Message.ServerId);
        }


        private RelayCommand _rejectFriendRequest;
        public RelayCommand RejectFriendRequest
        {
            get
            {
                return _rejectFriendRequest ?? (_rejectFriendRequest = new RelayCommand(ExecRejectFriendRequest, CanRejectFriendRequest));
            }
        }

        /// <summary>
        /// Checks whether the RejectFriendRequest command is executable
        /// </summary>
        private bool CanRejectFriendRequest()
        {
            return true;
        }

        /// <summary>
        /// Executes the RejectFriendRequest command 
        /// </summary>
        private async void ExecRejectFriendRequest()
        {
            var response = await _messagingService.RejectFriendRequestAsync(Message.ServerId);
        }
    }
}
