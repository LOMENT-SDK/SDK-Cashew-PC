using Cashew.Internal.Encryption;
using Cashew.Internal.Network;
using Cashew.Model;
using System.IO;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{
    /// <summary>
    /// Class for managing attachment uploads and downloads.
    /// </summary>
    internal class AttachmentManager
    {
        private AttachmentWebService _attachmentService;

        internal AttachmentManager()
        {
            _attachmentService = new AttachmentWebService();
        }

        /// <summary>
        /// Method for uploading attachments after the initial message is sent; if successful, the uploaded file name will be set on the AttachmentData object.
        /// </summary>
        /// <param name="attachment">The attachment to be uploaded.</param>
        /// <param name="messageId">The sent message's ID on the server.</param>
        /// <param name="from">The sender of the message.</param>
        /// <param name="to">The recipient of the message</param>
        /// <returns>True if the upload succeeded, false if it failed.</returns>
        internal async Task<bool> UploadAttachmentAsync(CNAttachmentData attachment, string messageId, string from, string to, bool isPArtofGroupConversation)
        {
            //Temporary hack to account for the fact that block encryption and streaming encryption return different results, and
            //the streaming encryption result is what other platforms expect from attachments. Correct this as soon as the underlying
            //issue is fixed on all platforms.
            MemoryStream inStream = new MemoryStream();
            MemoryStream outStream = new MemoryStream();

            inStream.Write(attachment.Data, 0, attachment.Data.Length);

            inStream.Position = 0;

            if (isPArtofGroupConversation)
                Crypto.GetDataCrypter().EncryptGroupStream(inStream, outStream, from, to);
            else
                Crypto.GetDataCrypter().EncryptStream(inStream, outStream, from, to);

            outStream.Position = 0;

            byte[] encrypted = new byte[outStream.Length];

            outStream.Read(encrypted, 0, encrypted.Length);

            string serverName = await _attachmentService.UploadAttachmentAsync(encrypted, attachment.Name);

            if (!string.IsNullOrEmpty(serverName))
            {
                attachment.ServerName = serverName;
                return true;
            }
            else return false;
        }

        /// <summary>
        /// Method downloading attachments associated with a received message.
        /// </summary>
        /// <param name="attachment">The attachment metadata from the message.</param>
        /// <param name="outStream">A stream to write the attachment data to.</param>
        /// <param name="from">The sender of the received message.</param>
        /// <param name="to">The recipient of the received message.</param>
        /// <returns>The downloaded and unencrypted attachment.</returns>
        internal async Task DownloadAttachmentAsync(CNAttachmentDownloadInfo attachment, Stream outStream, string fileName, CNLomentAccount lomentAccount, CNCashewAccount cashewAccount)
        {
            Stream encryptedData = await _attachmentService.DownloadAttachmentAsync(fileName, lomentAccount, cashewAccount);

            if (encryptedData != null)
            {
                if (attachment.IsPartOfGroupConversation)
                    Crypto.GetDataCrypter().DecryptGroupStream(encryptedData, outStream, attachment.MessageSenderId, attachment.MessageRecipientId, encryptedData.Length, attachment.Size);
                else
                    Crypto.GetDataCrypter().DecryptStream(encryptedData, outStream, attachment.MessageSenderId, attachment.MessageRecipientId, encryptedData.Length, attachment.Size);
            }
        }
    }
}
