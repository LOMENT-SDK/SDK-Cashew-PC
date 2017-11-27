using System;

using SQLite.Net.Attributes;

namespace Loment.CashewReference.DB.Model.Dao.Tables
{
    /// <summary>
    /// Database table class for Messages
    /// </summary>
    internal class MessagesTable
    {
        /// <summary>
        /// The identifier assigned by the server.
        /// </summary>
        [PrimaryKey]
        public int ServerId { get; internal set; } = 0;

        /// <summary>
        /// ID of the Cashew Account for the CashewMessage
        /// This is the value assigned by the Cashew remote server
        /// </summary>
        [Indexed]
        public string CashewAccountId { get; set; } = String.Empty;

        /// <summary>
        /// Internal Foreign Key relationship to DbContact
        /// Relates to the Sender of the CashewMessage
        /// </summary>
        [Indexed]
        public long ContactId { get; internal set; } = 0;

        /// <summary>
        /// The id of the sender of the message.
        /// <para> See email name property on </para><see cref="DbContact"/><para>
        /// </summary>
        [Indexed]
        public string SenderId { get; set; } = String.Empty;

        /// <summary>
        /// The cashew Id of the recipient.
        /// </summary>
        public string Recipient { get; set; }

        /// <summary>
        /// The identifier of the conversation this message belongs to.
        /// </summary>
        [Indexed]
        public string ConversationId { get; set; } = String.Empty;

        ///// <summary>
        ///// The priority of the message.
        ///// MessagePriority is an enum
        ///// </summary>
        public MessagePriority Priority { get; set; }

        /// <summary>
        /// Indicates whether the message has a froward restriction.
        /// </summary>
        public bool IsForwardRestricted { get; set; } = false;

        /// <summary>
        /// Indicates whether the message requests a read acknowledgment.
        /// </summary>
        public bool RequestsReadAcknowledgement { get; set; } = false;

        /// <summary>
        /// Indicates whether the message has been acknowledged.
        /// </summary>
        public bool IsReadAcknowledged { get; set; } = false;

        /// <summary>
        /// The time in minutes the message expires after start reading. -1 if the message does not expire.
        /// </summary>
        public int ExpiryMinutes { get; set; } = 0;

        /// <summary>
        /// The time in UTC the message was created.
        /// </summary>
        public DateTime TimeStamp { get; set; }

        /// <summary>
        /// Indicates whether the message is read.
        /// </summary>
        public bool IsRead { get; set; } = false;

        /// <summary>
        /// The time in UTC the message was sent.
        /// </summary>
        public DateTime SendTime { get; set; }

        /// <summary>
        /// The content of the message.
        /// </summary>
        public string Content { get; set; } = String.Empty;

        /// <summary>
        /// Defines whether a message is outgoing or incoming
        /// </summary>
        public MessageDirection Direction { get; internal set; }

        /// <summary>
        /// Subject of the message
        /// </summary>
        public string Subject { get; internal set; }

        /// <summary>
        /// Defines whether or not the message is part of a Group Conversation
        /// </summary>
        public bool IsPartOfGroupConversation { get; internal set; }


        public bool IsFriendRequest { get; internal set; }

        public FriendRequestStatus FriendRequestStatus { get; internal set; }

        public bool IsMarkedAsDeleted { get; internal set; }
    }
}
