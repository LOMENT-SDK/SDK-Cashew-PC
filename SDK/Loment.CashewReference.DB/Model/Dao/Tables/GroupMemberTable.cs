using SQLite.Net.Attributes;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{
    internal class GroupMemberTable
    {
        [PrimaryKey] [AutoIncrement]
        public int Id { get; set; }

        [Indexed]
        public string ConversationId { get; internal set; }

        [Indexed]
        public string ContactSenderId { get; internal set; }
    }
}
