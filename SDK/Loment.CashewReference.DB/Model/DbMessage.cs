using System;
using System.Linq.Expressions;
using Loment.CashewReference.DB.Base;
using System.Collections.Generic;

namespace Loment.CashewReference.DB.Model
{
    /// <summary>
    /// Public interface to the Database representation of Message table
    /// Represents the Messages exchanged between users
    /// </summary>
    public class DbMessage : BaseObservableObject
    {
        private bool _isReadAcknowledged = false;
        private bool _isRead = false;
        private FriendStatus _status;
        private MessagePriority _priority;
        private bool _requestsReadAcknowledgement = false;
        private bool _isForwardRestricted = false;
        private string _content = String.Empty;
        private string _subject = String.Empty;

        public DbMessage()
        {
        }


        /// <summary>
        /// ID of the Cashew Account for the CashewMessage
        /// This is the value assigned by the Cashew remote server
        /// </summary>
        public String CashewAccountId { get; set; } = String.Empty;

        /// <summary>
        /// Id of the Message of the Cashew Server. This value is immutable.
        /// </summary>
        public int ServerId { get; set; } = 0;

        /// <summary>
        /// The sender Id of the message.
        /// </summary>
        public string SenderId{ get; set; }

        /// <summary>
        /// The id of the sender of the message.
        /// <para> See email name property on </para><see cref="DbContact"/><para>
        /// </summary>
        public DbContact Sender { get; set; } = null;

        /// <summary>
        /// The cashew Id of the recipient.
        /// </summary>
        public string Recipient { get; set; }

        /// <summary>
        /// The identifier of the conversation this message belongs to.
        /// <para> See unique name property on </para><see cref="DbConversation"/><para>
        /// </summary>
        public string ConversationId { get; set; } = String.Empty;

        public DbConversation Conversation { get; set; }

        /// <summary>
        /// The priority of the message.
        /// </summary>
        public MessagePriority Priority
        {
            get { return _priority; }
            set { Set(ref _priority, value); }
        }

        /// <summary>
        /// Indicates whether the message has a froward restriction.
        /// </summary>
        public bool IsForwardRestricted
        {
            get { return _isForwardRestricted; }
            set { Set(ref _isForwardRestricted, value); }
        }

        /// <summary>
        /// Indicates whether the message requests a read acknowledgment.
        /// </summary>
        public bool RequestsReadAcknowledgement
        {
            get { return _requestsReadAcknowledgement; }
            set { Set(ref _requestsReadAcknowledgement, value); }
        }

        /// <summary>
        /// Indicates whether the message has been acknowledged.
        /// </summary>
        public bool IsReadAcknowledged
        {
            get { return _isReadAcknowledged; }
            set { Set(ref _isReadAcknowledged, value); }
        }

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
        public bool IsRead
        {
            get { return _isRead; }
            set { Set(ref _isRead, value); }
        }

        /// <summary>
        /// The time in UTC the message was sent.
        /// </summary>
        public DateTime SendTime { get; set; }

        /// <summary>
        /// Unencrypted message content
        /// </summary>
        public string Content
        {
            get { return _content; }
            set { Set(ref _content, value); }
        }

        /// <summary>
        /// Direction of the message: Incoming or outgoing
        /// </summary>
        public MessageDirection Direction { get; set; }

        /// <summary>
        /// Subject of the message
        /// </summary>
        public string Subject
        {
            get { return _subject; }
            set { Set(ref _subject, value); }
        }

        /// <summary>
        /// Is the message part of a group conversation
        /// </summary>
        public bool IsPartOfGroupConversation { get; set; }

        /// <summary>
        /// Indicates whether this message is a friend request.
        /// </summary>
        public bool IsFriendRequest { get; set; }

        private FriendRequestStatus _friendRequestStatus;
        /// <summary>
        /// If this messages is a friend request then this indicates the status.
        /// </summary>
        public FriendRequestStatus FriendRequestStatus
        {
            get { return _friendRequestStatus; }
            set { Set(ref _friendRequestStatus, value); }
        }

        /// <summary>
        /// A list of attachments for this message.
        /// </summary>
        public List<DbAttachment> Attachments { get; set; } = new List<DbAttachment>();

        private bool _isMarkedAsDeleted;

        /// <summary>
        /// Indicates whether a message is marked as deleted.
        /// </summary>
        public bool IsMarkedAsDeleted
        {
            get { return _isMarkedAsDeleted; }
            set { Set(ref _isMarkedAsDeleted, value); }
        }
    }
}

