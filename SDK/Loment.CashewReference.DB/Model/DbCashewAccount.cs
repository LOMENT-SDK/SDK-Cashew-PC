using Loment.CashewReference.DB.Base;
using System;

namespace Loment.CashewReference.DB.Model
{
    public class DbCashewAccount : BaseObservableObject
    {
        private string _userName = String.Empty;
        private string _status = String.Empty;
        private DateTime _lastUpdateDateUtc;

        /// <summary>
        /// The id of the user this account belongs to.
        /// This is the Loment Account ID
        /// <see cref="User"/>
        /// </summary>
        public DbLomentAccount LomentAccount { get; set; } = null;

        /// <summary>
        /// The cashew user name for this account.
        /// </summary>
        public string UserName
        {
            get { return _userName; }
            set { Set(ref _userName, value); }
        }

        /// <summary>
        /// The current status of the account.
        /// </summary>
        public string Status
        {
            get { return _status; }
            set { Set(ref _status, value); }
        }

        /// <summary>
        /// Date and time in UTC the account was created.
        /// </summary>
        public DateTime CreateDateUtc { get; set; }

        /// <summary>
        /// Date and time in UTC the account was las updated.
        /// </summary>
        public DateTime LastUpdateDateUtc
        {
            get { return _lastUpdateDateUtc; }
            set { Set(ref _lastUpdateDateUtc, value); }
        }
    }
}
