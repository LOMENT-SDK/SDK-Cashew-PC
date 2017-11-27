namespace Cashew.Model.NetworkResponses
{
    /// <summary>
    /// A response info class for authentication.
    /// </summary>
    public  class AuthenticationResponse
    {
        /// <summary>
        /// Indicates whether a sync is required on the  current device.
        /// </summary>
        public bool IsSyncRequired { get; internal set; }
    }
}
