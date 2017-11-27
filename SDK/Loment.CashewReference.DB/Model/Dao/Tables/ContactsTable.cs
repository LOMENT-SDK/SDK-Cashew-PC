
using System;
using SQLite.Net.Attributes;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{
    internal class ContactsTable
    {
        [PrimaryKey]
        public String SenderId { get; set; }

        /// <summary>
        /// Email address of the DbContact
        /// </summary>
        [Indexed]
        public string Email { get; set; } = String.Empty;

        /// <summary>
        /// Indicates whether this Contact also represents a cashew account for this user.
        /// </summary>
        public bool IsCashewAccount { get; set; }

        /// <summary>
        /// Date and time the account was created.
        /// </summary>
        public DateTime CreateDateUtc { get; set; }

        /// <summary>
        /// Date and time the account was last updated.
        /// </summary>
        public DateTime LastUpdateDateUtc { get; set; }

        /// <summary>
        /// Name of the DbContact
        /// </summary>
        public string Name { get; set; } = String.Empty;

        ///// <summary>
        ///// Phone Number of the DbContact
        ///// </summary>
        public string Phone { get; set; } = String.Empty;

        ///// <summary>
        ///// Location on disk of the DbContact's picture
        ///// </summary>
        public string ImagePath { get; set; } = String.Empty;
        
    }
}
