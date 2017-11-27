
using Loment.CashewReference.DB.Base;

namespace Loment.CashewReference.DB.Model
{

    /// <summary>
    /// Public interface to the Database representation of Loment Account table 
    /// </summary>
    public class DbLomentAccount : BaseObservableObject
    {

        private string _userId      = string.Empty;
        private string _userName    = string.Empty;
        private string _phone       = string.Empty;
        private string _email       = string.Empty;
        private string _deviceId    = string.Empty;
        private string _password    = string.Empty;

        /// <summary>
        /// Internal Primary Key of the database table
        /// </summary>
        public long Pk { get; set; }

        /// <summary>
        /// The UserId of the account.
        /// </summary>
        public string UserId { get; set; }

        /// <summary>
        /// The UserName of the account.
        /// </summary>
        public string UserName
        {
            get { return _userName; }
            set
            {
                if (value == _userName)
                    return;
                _userName = value;
                RaisePropertyChanged(() => UserName);
            }
        }
        
        /// <summary>
        /// Primary phone number.
        /// </summary>
        public string Phone
        {
            get { return _phone; }
            set
            {
                if (value == _phone)
                    return;
                _phone = value;
                RaisePropertyChanged(() => Phone);
            }
        }

        /// <summary>
        /// Primary email address.
        /// </summary>
        public string Email
        {
            get { return _email; }
            set
            {
                if (value == _email)
                    return;
                _email = value;
                RaisePropertyChanged(() => Email);
            }
        }

        /// <summary>
        /// The device Id this account is used on
        /// <para>See DeviceId property on <see cref="Device"/></para>
        /// </summary>
        public string DeviceId
        {
            get { return _deviceId; }
            set
            {
                if (value == _deviceId)
                    return;
                _deviceId = value;
                RaisePropertyChanged(() => DeviceId);
            }
        }

        /// <summary>
        /// The password for the user.
        /// </summary>
        public string Password
        {
            get { return _password; }
            set
            {
                if (Set(ref _password, value))
                {
                    RaisePropertyChanged(() => Password);
                }
            }
        }
    }
}
