
using SQLite.Net.Attributes;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{

    /// <summary>
    /// Attachments to a message
    /// </summary>
    internal class AttachmentsTable
    {
        /// <summary>
        /// Internal Primary Key value of the table
        /// </summary>
        [PrimaryKey, AutoIncrement]
        public long Pk { get; set; } = 0;

        /// <summary>
        /// ServerId of the message used for relating attachment to the message
        /// </summary>
        [Indexed]
        public int MessageId { get; set; } = -1;

        /// <summary>
        /// Name of the file as provided by the sender i.e. QuaryterlySales.docx
        /// </summary>
        public string Name { get; set; } = string.Empty;

        /// <summary>
        /// Name of the attachment as written out to file
        /// </summary>
        public string FileName { get; set; }

        public int Padding { get; set; }

        /// <summary>
        /// The size in byte of teh attachment.
        /// </summary>
        public long Size { get; set; }
    }
}
