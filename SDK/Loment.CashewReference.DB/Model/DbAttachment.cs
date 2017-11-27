using Loment.CashewReference.DB.Base;
using System;

namespace Loment.CashewReference.DB.Model
{

    /// <summary>
    /// Public interface to the Database representation of Attachments
    /// </summary>
    public class DbAttachment : BaseObservableObject
    {
        /// <summary>
        /// Internal Primary Key value of the table
        /// </summary>
        public long Pk { get; set; } = 0;

        /// <summary>
        /// The Message this attachment belongs to.
        /// </summary>
        public DbMessage Message { get; set; }

        /// <summary>
        /// Attachment Name i.e. QuarterlySales.docx
        /// </summary>
        public string Name { get; set; } = String.Empty;

        /// <summary>
        /// The padding of the attachment data.
        /// </summary>
        public int Padding { get; set; }

        /// <summary>
        /// The size of the file.
        /// </summary>
        public long Size { get; set; }
    }

}
