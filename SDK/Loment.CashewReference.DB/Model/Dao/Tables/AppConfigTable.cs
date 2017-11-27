using System;

using SQLite.Net.Attributes;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{
    /// <summary>
    /// Holds the app configuration information
    /// Note: Only one row in this table. To handle multiple Loment accounts
    /// Use separate databases
    /// </summary>
    internal class AppConfigTable
    {

        /// <summary>
        /// Internal primary key of the database for the AppConfig table
        /// </summary>
        [PrimaryKey, AutoIncrement]
        public long Pk { get; set; } 

        /// <summary>
        /// Holds the status of the account
        /// </summary>
        public bool LomentAccountSubscriptionIsActive { get; set; } = false;

        /// <summary>
        /// Loment Account User Name
        /// </summary>
        public string LomentUserName { get; set; } = String.Empty;

        /// <summary>
        /// Defines whether or not the password for the account is to be stored in the database
        /// </summary>
        public bool IsRememberPassword { get; set; } = false;

        /// <summary>
        /// Password of the LomentAccount
        /// </summary>
        public string Password { get; set; } = String.Empty;


    }
}
