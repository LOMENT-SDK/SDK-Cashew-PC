namespace Cashew.Model
{
    /// <summary>
    /// An attachment data class used to send attachments along with message.
    /// <see cref="MessagingService.SendMessageAsync(string, string, CNMessageOptions, CNAttachmentData[], int)"/>
    /// </summary>
    public class CNAttachmentData
    {
        /// <summary>
        /// The defult ctor.
        /// </summary>
        public CNAttachmentData() { }

        /// <summary>
        /// A parametrized ctor to set name and data.
        /// </summary>
        /// <param name="name"></param>
        /// <param name="data"></param>
        public CNAttachmentData(string name, byte[] data)
        {
            Name = name;
            Data = data;
        }

        /// <summary>
        /// The name of the attachment.
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// The actual content of the attachment represented as a byte array.
        /// </summary>
        public byte[] Data { get; set; }

        private long? _size;

        internal long Size { get { return (long)(_size ?? (_size = Data.Length)); } }

        private int? _padding;
        internal int Padding { get { return (int)(_padding ?? (_padding = GetPadding())); } }

        internal string ServerName { get; set; }

        private int GetPadding()
        {
            //determine padding
            int paddingSize = (int)(16 - (Data.Length % 16));
            if (paddingSize == 16)
            {
                paddingSize = 0;
            }
            return paddingSize;
        }
    }
}
