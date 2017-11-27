using Cashew;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using Cashew.Utility.Services;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Loment.CashewReference.DB.Model;

namespace Core.ViewModel
{
    public class AttachmentViewModel : ViewModelBase
    {
        MessagingService _messagingService;
        IPlatformUtility _platformUtility;


        public DbAttachment Attachment { get; private set; }


        public AttachmentViewModel(DbAttachment attachment, MessagingService messagingService, IPlatformUtility platformUtility)
        {
            Attachment = attachment;
            _messagingService = messagingService;
            _platformUtility = platformUtility;
        }


        private bool _isDownloading = false;

        private RelayCommand _open;
        public RelayCommand Open
        {
            get
            {
                return _open ?? (_open = new RelayCommand(ExecOpen, CanOpen));
            }
        }

        /// <summary>
        /// Checks whether the Open command is executable
        /// </summary>
        private bool CanOpen()
        {
            return !_isDownloading;
        }

        /// <summary>
        /// Executes the Open command 
        /// </summary>
        private async void ExecOpen()
        {
            _isDownloading = true;
            Open.RaiseCanExecuteChanged();

            var stream = await _platformUtility.GetAttachmentSaveStream(_messagingService.CashewAccount.LomentAccountUserId, Attachment.Message.ServerId.ToString(), Attachment.Name);

            CNAttachmentDownloadInfo cdi = new CNAttachmentDownloadInfo
            {
                IsPartOfGroupConversation = Attachment.Message.IsPartOfGroupConversation,
                MessageServerId = Attachment.Message.ServerId,
                MessageSenderId = Attachment.Message.SenderId,
                MessageRecipientId = Attachment.Message.IsPartOfGroupConversation ? Attachment.Message.ConversationId : Attachment.Message.Recipient,
                Name = Attachment.Name,
                Padding = Attachment.Padding,
                Size = Attachment.Size
            };

            CashewResponse response = await _messagingService.DownloadAttachmentAsync(cdi, stream);

            await stream.FlushAsync();
            stream.Dispose();
            stream = null;

            if (response.IsSuccess)
            {

                string filePath = await _platformUtility.GetAttachmentFilePath(_messagingService.CashewAccount.LomentAccountUserId, Attachment.Message.ServerId.ToString(), Attachment.Name);
                filePath = _platformUtility.GetAbsoluteFilePath(filePath);
                _platformUtility.LaunchFileInExternalViewer(filePath);
            }

            _isDownloading = false;
            Open.RaiseCanExecuteChanged();
        }

    }
}
