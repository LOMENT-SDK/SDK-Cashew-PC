using System;

namespace Cashew.Services
{
    /// <summary>
    /// Represents a message received through a RabbitMQ queue.
    /// </summary>
    public class MessageReceivedEventArgs : EventArgs
    {
        /// <summary>
        /// The body of the message.
        /// </summary>
        public string Body { get; private set; }

        /// <summary>
        /// A tag for the consumer.
        /// </summary>
        public string ConsumerTag { get; private set; }

        /// <summary>
        /// A delivery tag.
        /// </summary>
        public ulong DeliveryTag { get; private set; }

        /// <summary>
        /// The exchange the message was received on.
        /// </summary>
        public string Exchange { get; private set; }

        /// <summary>
        /// Indicates whether this message was redelivered.
        /// </summary>
        public bool Redelivered { get; private set; }

        /// <summary>
        /// The routing key.
        /// </summary>
        public string RoutingKey { get; private set; }

        /// <summary>
        /// The token to identify messages through round trips.
        /// </summary>
        public string Token { get; private set; }

        /// <summary>
        /// The parameterized ctor to set all properties.
        /// </summary>
        public MessageReceivedEventArgs(string body, string consumerTag, ulong deliveryTag, string exchange, bool redelivered, string routingKey, string token)
        {
            Body = body;
            ConsumerTag = consumerTag;
            DeliveryTag = deliveryTag;
            Exchange = exchange;
            Redelivered = redelivered;
            RoutingKey = routingKey;
            Token = token;
        }
    }
}
