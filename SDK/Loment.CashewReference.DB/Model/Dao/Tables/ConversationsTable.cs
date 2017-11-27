using SQLite.Net.Attributes;
using System;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{
    internal class ConversationsTable
    {
        [PrimaryKey]
        [AutoIncrement]
        /// <summary>
        /// Internal Primary Key of the table
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// The cashew account Id this conversation belongs to.
        /// </summary>
        [Indexed(Name = "ConversationIdConstraint", Order = 1, Unique = true)]
        public string CashewAccountId { get; set; } = "";

        /// <summary>
        /// The server conversation Id 
        /// </summary>
        [Indexed(Name = "ConversationIdConstraint", Order = 2, Unique = true)]
        public string ConversationId { get; set; } = "";

        /// <summary>
        /// Indicates whether this conversation is a group conversation.
        /// </summary>
        public bool IsGroupConversation { get; set; } = false;
        
        /// <summary>
        /// The name of the conversation.
        /// </summary>
        [Indexed]
        public string Name { get; set; } = String.Empty;

   
        /// <summary>
        /// An identifier of the owner of the group.
        /// <para> See UserName property of <see cref="DbContact"/> </para>
        /// </summary>
        [Indexed]
        public string OwnerId { get; set; } = String.Empty;
    }
}
