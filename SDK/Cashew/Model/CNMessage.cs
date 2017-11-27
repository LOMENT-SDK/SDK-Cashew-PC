using System;
using System.Collections.Generic;

namespace Cashew.Model
{
    /// <summary>
    /// A class representing a message sent through the Cashew network.
    /// </summary>
    public class CNMessage : CNMessageBase
    {
        /// <summary>
        /// The internal default ctor
        /// </summary>
        internal CNMessage() { }

        /// <summary>
        /// Indicates whether the message is part of a group conversation.
        /// </summary>
        public bool IsPartOfGroupConversation { get; internal set; }

        /// <summary>
        /// The priority of the message.
        /// </summary>
        public CNMessagePriority Priority { get; internal set; }

        /// <summary>
        /// Indicates whether the message has a froward restriction.
        /// </summary>
        public bool IsForwardRestricted { get; internal set; }

        /// <summary>
        /// Indicates whether the message requests a read acknowledgment.
        /// </summary>
        public bool RequestsReadAcknowledgement { get; internal set; }

        /// <summary>
        /// Indicates whether the message has been acknowledged.
        /// </summary>
        public bool IsReadAcknowledged { get; set; }

        /// <summary>
        /// The time in minutes the message expires after start reading. -1 if the message does not expire.
        /// </summary>
        public int ExpiryMinutes { get; internal set; }


        /// <summary>
        /// Indicates whether the message is read.
        /// </summary>
        public bool IsRead { get; set; }

        /// <summary>
        /// The content of the message.
        /// </summary>
        public string Content { get; internal set; }

        /// <summary>
        /// Indicates whether the message is deleted on the server.
        /// </summary>
        public bool MarkedAsDeleted { get; internal set; }

        /// <summary>
        /// Al list of attachments
        /// </summary>
        public List<CNAttachment> Attachments { get; internal set; }
    }
}
