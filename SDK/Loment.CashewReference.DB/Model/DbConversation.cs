using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Loment.CashewReference.DB.Base;

namespace Loment.CashewReference.DB.Model
{
    /// <summary>
    /// Public interface to the Database representation of Conversation table
    /// </summary>
    public class DbConversation : BaseObservableObject
    {

        /// <summary>
        /// A unique Id fo rthe conversation.
        /// </summary>
        public string ConversationId { get; set; } = String.Empty;

        /// <summary>
        /// The cashew Id this conversation belongs to.
        /// </summary>
        public string CashewAccountId { get; set; }

        /// <summary>
        /// Indicates whether this is a group conversation or direct messaging.
        /// </summary>
        public bool IsGroupConversation { get; set; } = false;

        /// <summary>
        /// A display name of the conversation. In case of a group this is the name of the group. In case of direct messaging
        /// this is the recipient acahsew account.
        /// </summary>
        public string Name { get; set; } = String.Empty;


        /// <summary>
        /// An identifier of the owner of the group.
        /// <para> See UserName property of <see cref="DbContact"/> </para>
        /// </summary>
        public string OwnerId { get; set; } = String.Empty;

        /// <summary>
        /// In case of a group this is the list of members.
        /// </summary>
        public List<string> Members { get; set; } = new List<string>();
    }
}
