using System.Collections.Generic;
using System.Collections.Immutable;

namespace Cashew.Model
{
    /// <summary>
    /// Represents information about a contact returned by a contact search.
    /// </summary>
    public class CNFriendSuggestion
    {
        /// <summary>
        /// The email address of the  contact.
        /// </summary>
        public string Email { get; private set; } = "";

        /// <summary>
        /// THe full name of the contact.
        /// </summary>
        public string Name { get; private set; } = "";

        /// <summary>
        /// The phone number of the contact.
        /// </summary>
        public string Phone { get; private set; } = "";

        /// <summary>
        /// A copy ctor.
        /// </summary>
        /// <param name="contact"></param>
        public CNFriendSuggestion(CNFriendSuggestion contact)
           : this(contact.Name, contact.Email, contact.Email, new List<string>(contact.CashewAccountIds))
        { }

        /// <summary>
        /// A parameterized ctor to create the contact.
        /// </summary>
        public CNFriendSuggestion(string name, string email, string phone, List<string> cashewAccountIds)
        {
            Email = email;
            Name = name;
            Phone = phone;
            CashewAccountIds = ImmutableList.CreateRange(cashewAccountIds);
        }

        /// <summary>
        /// A list of associated Cashew account for this Contact.
        /// </summary>
        public ImmutableList<string> CashewAccountIds { get; private set; }
    }
}
