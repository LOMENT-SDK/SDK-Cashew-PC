using System;

namespace Cashew.Model
{
    /// <summary>
    /// Represents a Cashew account
    /// </summary>
    public class CNCashewAccount
    {
        /// <summary>
        /// The id of the user this account belongs to.
        /// <see cref="CNLomentAccount"/>
        /// </summary>
        public string LomentAccountUserId { get; set; }

        /// <summary>
        /// The cashew user name for this account.
        /// </summary>
        public string UserName { get; set; }

        /// <summary>
        /// The current status of the account.
        /// </summary>
        public string Status { get; set; }

        /// <summary>
        /// Date and time in UTC the account was created.
        /// </summary>
        public DateTime CreateDateUtc { get; set; }

        /// <summary>
        /// Date and time in UTC the account was las updated.
        /// </summary>
        public DateTime LastUpdateDateUtc { get; set; }

        /// <summary>
        /// The device id this account was created on.
        /// </summary>
        public string DeviceId { get { return Sdk.DeviceInformationProvider.GetUniqueDeviceId(); } }
    }
}
