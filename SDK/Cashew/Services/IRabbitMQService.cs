using System;

namespace Cashew.Services
{
    /// <summary>
    /// A minimal interface needed to communicate through RabbiMQ. Each platform needs to provide an implementation of this interface.
    /// <see cref="Sdk.Initialize(IDeviceInformationProvider, IRabbitMQService, IDataCrypter)"/>
    /// </summary>
    public interface IRabbitMQService
    {
        /// <summary>
        /// Indicates whether the connection is open.
        /// </summary>
        bool IsOpen { get; }

        IRabbitMQService CreateService(string hostName, string userName, string password, string virtualHost, ushort hearBeat);

        /// <summary>
        /// OPens the communication.
        /// </summary>
        void Open();

        /// <summary>
        /// Opens the client and declares a queue.
        /// </summary>
        /// <returns>Teh name of the declared queue.</returns>
        string OpenAndDeclareQueue();

        /// <summary>
        /// Sends a message to the queue.
        /// </summary>
        /// <param name="token">A token to identified the message after round trips.</param>
        /// <param name="responseQueueName">The name of the response queue.</param>
        /// <param name="content">The content to send.</param>
        /// <param name="exchange">An exchange key.</param>
        /// <param name="routingKey">A routing key.</param>
        void Send(string token, string responseQueueName, string content, string exchange, string routingKey);

        /// <summary>
        /// Starts a receive loop to listen to incoming messages.
        /// </summary>
        /// <param name="queue"></param>
        void StartReceiveLoop(string queue);

        /// <summary>
        /// An event that occurs once a message is received.
        /// </summary>
        event EventHandler<MessageReceivedEventArgs> MessageReceived;

        /// <summary>
        /// Will be raised in case the connection is lost.
        /// </summary>
        event EventHandler Disconnected;

        /// <summary>
        /// Closes the connection. This does not raise the <see cref="Disconnected"/> event.
        /// </summary>
        void Close();
    }
}
