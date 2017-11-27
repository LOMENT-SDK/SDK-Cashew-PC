namespace Cashew.Model
{
    /// <summary>
    /// Options that can be set for a message when sent to the Cashew network.
    /// </summary>
    public class CNMessageOptions
    {
        /// <summary>
        /// The priority of the message.
        /// </summary>
        public CNMessagePriority Priority { get; set; } = CNMessagePriority.PRIORITY_3;

        /// <summary>
        /// The time in minutes the message is valid and can be viewed by the receiver.
        /// </summary>
        public int ExpiryInMinutes { get; set; } = -1;

        /// <summary>
        /// Indicates the  client whether the message is allowed to be forwarded.
        /// </summary>
        public bool IsForwardResticted { get; set; } = false;

        /// <summary>
        /// Indicates whether the message needs to be acknowledged by the receiver before it can be viewed.
        /// </summary>
        public bool RequestReadAcknlowledgement { get; set; }
    }
}
