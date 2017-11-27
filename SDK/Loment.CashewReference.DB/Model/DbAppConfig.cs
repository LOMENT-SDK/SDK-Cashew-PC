using System;

using Loment.CashewReference.DB.Base;

namespace Loment.CashewReference.DB.Model
{
    /// <summary>
    /// Public interface to the Database representation of the DBAppConfig table 
    /// </summary>
    public class DbAppConfig : BaseObservableObject
    {
        public long Pk { get; set; } = 0;

        public string Password { get; set; } = String.Empty;

        public bool IsRememberPassword { get; set; } = false;

        public bool HasPersistedLomentUserName { get { return !string.IsNullOrEmpty(LomentUserName); } }

        public string LomentUserName { get; set; } = String.Empty;

        private bool _lomentAccountSubscriptionIsActive;

        public bool LomentAccountSubscriptionIsActive
        {
            get { return _lomentAccountSubscriptionIsActive; }
            set { Set(ref _lomentAccountSubscriptionIsActive, value); }
        }

        #region Default Email Composition

        private bool _emailIsMarkedForWalnutEncryption;
        private bool _emailWalnutForwardRestrict;
        private TimeSpan _emailWalnutExpiryTime = TimeSpan.FromMinutes(10);
        private bool _isEmailWalnutExpires;

        public bool EmailIsMarkedForWalnutEncryption
        {
            get { return _emailIsMarkedForWalnutEncryption; }
            set { Set(ref _emailIsMarkedForWalnutEncryption, value); } 
        }

        public bool EmailWalnutForwardRestrict
        {
            get { return _emailWalnutForwardRestrict; }
            set { Set(ref _emailWalnutForwardRestrict, value); }
        }

        public bool EmailWalnutExpires
        {
            get { return _isEmailWalnutExpires; }
            set { Set(ref _isEmailWalnutExpires, value); }
        }

        public TimeSpan EmailWalnutExpiryTime
        {
            get { return _emailWalnutExpiryTime; }
            set { Set(ref _emailWalnutExpiryTime, value); }
        }

        #endregion
    }
}
