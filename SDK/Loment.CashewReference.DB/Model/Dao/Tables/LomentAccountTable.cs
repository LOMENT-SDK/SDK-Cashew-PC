using SQLite.Net.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{
    internal class LomentAccountTable
    {

        /// <summary>
        /// Internal Primary Key of the database table
        /// </summary>
        [PrimaryKey, AutoIncrement]
        public long Pk { get; set; }

        /// <summary>
        /// The UserId of the account.
        /// </summary>
        public string UserId { get; set; }

        /// <summary>
        /// The UserName of the account.
        /// </summary>
        public string UserName { get; set; }

        /// <summary>
        /// Primary phone number.
        /// </summary>
        public string Phone { get; set; }

        /// <summary>
        /// Primary email address.
        /// </summary>
        public string Email { get; set; }

        /// <summary>
        /// The device Id this account is used on
        /// <para>See DeviceId property on <see cref="Device"/></para>
        /// </summary>
        public string DeviceId { get; set; }

        /// <summary>
        /// The password for the user.
        /// </summary>
        public string Password { get; set; }
    }
}