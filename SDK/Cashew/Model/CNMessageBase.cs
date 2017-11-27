using System;
using System.Collections.Generic;

namespace Cashew.Model
{
    /// <summary>
    /// A base class for different types of messages that can be received form the Cashew server.
    /// </summary>
    public abstract class CNMessageBase
    {
        /// <summary>
        /// The Cashew account the message was received on.
        /// </summary>
        public CNCashewAccount CashewAccount { get; set; }

        /// <summary>
        /// The identifier assigned by the server.
        /// </summary>
        public int ServerId { get; internal set; }

        /// <summary>
        /// The identifier of the conversation this message belongs to.
        /// <para> See unique name property on </para><see cref="Conversation"/><para>
        /// </summary>
        public string ConversationId { get; set; }

        /// <summary>
        /// The id of the sender of the message.
        /// <para> See email name property on </para><see cref="Contact"/><para>
        /// </summary>
        public string SenderId { get; set; }

        /// <summary>
        /// A list of recipients of this message.
        /// </summary>
        public List<string> Recipients { get; internal set; }

        /// <summary>
        /// The subject of the message.
        /// </summary>
        public string Subject { get; internal set; }

        /// <summary>
        /// The time in UTC the message was sent.
        /// </summary>
        public DateTime SendTime { get; set; }

        /// <summary>
        /// The time in UTC the message was created.
        /// </summary>
        public DateTime TimeStamp { get; internal set; }

        /// <summary>
        /// Indicates whether this message is an incoming or outgoing message.
        /// </summary>
        public CNMessageDirection Direction { get; set; }
    }
}
