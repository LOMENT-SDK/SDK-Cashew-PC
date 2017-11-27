namespace Cashew.Model
{
    /// <summary>
    /// Represents an attachment of a message.
    /// </summary>
    public class CNAttachment
    {        
        /// <summary>
        /// The internal default ctor.
        /// </summary>
        internal CNAttachment(CNMessage message)
        {
            Message = message;
        }

        /// <summary>
        /// The message the attachments belongs to.
        /// </summary>
        public CNMessage Message { get; internal set; }

        /// <summary>
        /// The name of the attachment.
        /// </summary>
        public string Name { get; internal set; }

        /// <summary>
        /// The size in byte of the attachment.
        /// </summary>
        public long Size { get; internal set; }

        /// <summary>
        /// The padding of the attachment
        /// </summary>
        public int Padding { get; internal set; }
     
    }
}
