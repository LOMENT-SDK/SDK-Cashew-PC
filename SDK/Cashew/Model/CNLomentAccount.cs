namespace Cashew.Model
{
    /// <summary>
    /// Represents a Loment Account
    /// </summary>
    public class CNLomentAccount
    {

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
        /// </summary>
        public string DeviceId { get { return Sdk.DeviceInformationProvider.GetUniqueDeviceId(); } }

        /// <summary>
        /// The password for the user.
        /// </summary>
        public string Password { get; set; }
        
    }
}