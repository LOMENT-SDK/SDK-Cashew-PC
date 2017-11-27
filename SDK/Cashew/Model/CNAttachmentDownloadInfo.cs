using Newtonsoft.Json.Linq;

namespace Cashew.Model
{
    /// <summary>
    /// Represents a info class necessary for the consumer to download attachments.
    /// </summary>
    public class CNAttachmentDownloadInfo
    {
        /// <summary>
        /// The server Id of the message the attachment belongs to.
        /// </summary>
        public int MessageServerId { get; set; }

        /// <summary>
        /// The sender Cashew Id of the attachment.
        /// </summary>
        public string MessageSenderId { get; set; }

        /// <summary>
        /// The recipient Id of the attachment. Can be either a Cashew Id or group conversation Id.
        /// </summary>
        public string MessageRecipientId { get; set; }

        /// <summary>
        /// The original name of the attachment.
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// The size of the attachment.
        /// </summary>
        public long Size { get; set; }

        /// <summary>
        /// The padding of the attachment data.
        /// </summary>
        public int Padding { get; set; }

        /// <summary>
        /// Indicates whether the message the attachment belongs to is part of a group conversation.
        /// </summary>
        public bool IsPartOfGroupConversation { get; set; }

    }
}
