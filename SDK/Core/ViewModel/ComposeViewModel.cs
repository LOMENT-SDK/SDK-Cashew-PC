using Cashew;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using Core.Data;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Loment.CashewReference.DB.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;

namespace Core.ViewModel
{
    public class ComposeViewModel : ViewModelBase
    {
        private readonly bool _isGroupConversation;
        private readonly string _conversationId;
        private readonly string _cashewReceiverName;
        private readonly MessagingService _messagingService;
        private readonly DataService _dataService;

        private string _sendError;
        public string SendError
        {
            get { return _sendError; }
            set { Set(ref _sendError, value); }
        }

        private int _expiry;
        public int Expiry
        {
            get { return _expiry; }
            set { Set(ref _expiry, value); }
        }

        private List<int> _expiries;
        public List<int> Expiries
        {
            get { return _expiries; }
            set { Set(ref _expiries, value); }
        }

        private bool _requireAck;
        public bool RequireAck
        {
            get { return _requireAck; }
            set { Set(ref _requireAck, value); }
        }

        private string _typedMessage;
        public string TypedMessage
        {
            get { return _typedMessage; }
            set { Set(ref _typedMessage, value); SendMessage.RaiseCanExecuteChanged(); }
        }

        private CNMessagePriority _priority;
        public CNMessagePriority Priority
        {
            get { return _priority; }
            set { Set(ref _priority, value); }
        }

        private bool _isForwardRestricted;
        public bool IsForwardRestricted
        {
            get { return _isForwardRestricted; }
            set { Set(ref _isForwardRestricted, value); }
        }

        private ObservableCollection<CNAttachmentData> _attachments = new ObservableCollection<CNAttachmentData>();
        public ObservableCollection<CNAttachmentData> Attachments
        {
            get { return _attachments; }
            set { Set(ref _attachments, value); }
        }

        private bool _isSending;
        public bool IsSending
        {
            get { return _isSending; }
            set { Set(ref _isSending, value); SendMessage.RaiseCanExecuteChanged(); }
        }

        public ComposeViewModel(DataService dataService, DbConversation conversation)
        {
            _isGroupConversation = conversation.IsGroupConversation;
            _conversationId = conversation.ConversationId;
            _cashewReceiverName = conversation.Name;
            _dataService = dataService;
            _messagingService = dataService.GetMessageingServiceByAccount(conversation.CashewAccountId);

            _expiries = new List<int>();
            _expiries.Add(-1);
            for (int i = 1; i <= 99; i++)
            {
                _expiries.Add(i);
            }

            Expiry = -1;
        }

        private RelayCommand _sendMessaeg;
        public RelayCommand SendMessage
        {
            get
            {
                return _sendMessaeg ?? (_sendMessaeg = new RelayCommand(ExecSendMessage, CanSendMessage));
            }
        }

        /// <summary>
        /// Checks whether the SendMessage command is executable
        /// </summary>
        private bool CanSendMessage()
        {
            return !string.IsNullOrEmpty(TypedMessage) && !IsSending;
        }

        /// <summary>
        /// Executes the SendMessage command 
        /// </summary>
        private async void ExecSendMessage()
        {
            IsSending = true;

            SendError = null;
            CashewResponse<CNMessage> response = null;

            var options = new CNMessageOptions
            {
                ExpiryInMinutes = Expiry,
                RequestReadAcknlowledgement = RequireAck,
                Priority = Priority,
                IsForwardResticted = IsForwardRestricted
            };

            try
            {
                int timeOut = 10000;
                if (_isGroupConversation)
                {
                    if (Attachments.Count > 0)
                    {
                        // Lets add 10 seconds per MB of attachment data to the time out
                        timeOut += ((Attachments.Sum(x => x.Data.Length) / 1024 / 1024) * 10000);
                        response = await _messagingService.SendGroupMessageAsync(TypedMessage, _conversationId, options, Attachments.ToArray(), timeOut);
                    }
                    else
                    {
                        response = await _messagingService.SendGroupMessageAsync(TypedMessage, _conversationId, options);
                    }
                }
                else
                {
                    if (Attachments.Count > 0)
                    {
                        // Lets add 10 seconds per MB of attachment data to the time out
                        timeOut += ((Attachments.Sum(x => x.Data.Length) / 1024 / 1024) * 10000);
                        response = await _messagingService.SendMessageAsync(TypedMessage, _cashewReceiverName, options, Attachments.ToArray(), timeOut);
                    }
                    else
                    {
                        response = await _messagingService.SendMessageAsync(TypedMessage, _cashewReceiverName, options);
                    }
                }
            }
            catch (Exception ex)
            {
                SendError = string.Format($"Message: {ex.Message}");
                Attachments.Clear();
                IsSending = false;
                return;
            }

            if (response != null && response.IsSuccess)
            {
                if (!_isGroupConversation)
                {
                    var dbMessageTuple = await _dataService.MessageCreateOrUpdate(response.Entity);

                    var attachments = Attachments.ToList();


                    var safeCopy = MessageCreated;
                    if (safeCopy != null)
                    {
                        MessageCreated(this, dbMessageTuple.Item1);
                    }
                }

                Attachments.Clear();
                TypedMessage = "";

            }
            else
            {
                SendError = string.Format($"ErrorCode: {response.ResponseCode} Message: {response.Comment}");
                Attachments.Clear();
            }
            IsSending = false;
        }

        public event EventHandler<DbMessage> MessageCreated;
    }
}
