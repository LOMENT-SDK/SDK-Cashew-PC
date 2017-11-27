using Cashew.Services;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.IO;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace RabbitMQNet
{
    public class NetRabbitMQService : IRabbitMQService
    {
#if DEBUG
        private enum OutputType
        {
            Standard,
            Receive,
            Send,
            Error
        }
        private void Output(string message, OutputType type)
        {

            switch (type)
            {
                case OutputType.Standard:
                    break;
                case OutputType.Receive:
                    Console.ForegroundColor = ConsoleColor.Green;
                    break;
                case OutputType.Send:
                    Console.ForegroundColor = ConsoleColor.Blue;
                    break;
                case OutputType.Error:
                    Console.ForegroundColor = ConsoleColor.Red;
                    break;
                default:
                    break;
            }

            Console.WriteLine(message);

            Console.ResetColor();
        }
#endif

        private readonly bool _isInitialized;

        private readonly string _hostName;
        private readonly string _userName;
        private readonly string _password;
        private readonly string _virtualHost;
        private readonly ushort _heartBeat;

        private IConnection _connection;
        private IModel _channel;

        public bool IsOpen
        {
            get
            {
                return _channel == null ? false : _channel.IsOpen;
            }
        }

        private NetRabbitMQService(string hostName, string userName, string password, string virtualHost, ushort heartBeat)
        {
            _hostName = hostName;
            _userName = userName;
            _password = password;
            _virtualHost = virtualHost;
            _heartBeat = heartBeat;

            _isInitialized = true;

#if DEBUG
            Output($"Creating queue: hostname:{hostName}", OutputType.Standard);
#endif
        }



        public NetRabbitMQService()
        {
            _isInitialized = false;
        }

        public IRabbitMQService CreateService(string hostName, string userName, string password, string virtualHost, ushort hearBeat)
        {
            return new NetRabbitMQService(hostName, userName, password, virtualHost, hearBeat);
        }

        public void Open()
        {
            OpenInternal(false);
        }

        public string OpenAndDeclareQueue()
        {
            return OpenInternal(true);
        }

        private volatile bool _wasOpen;
        private volatile bool _queueWasDeclared;

        private string OpenInternal(bool declareQueue)
        {
            if (!_isInitialized)
            {
                throw new InvalidOperationException("This instance is not initialized. Please use CreateService() to create an initialized service.");
            }

            ConnectionFactory factory = new ConnectionFactory();
            factory.RequestedHeartbeat = _heartBeat;
            factory.HostName = _hostName;
            factory.UserName = _userName;
            factory.Password = _password;
            factory.VirtualHost = _virtualHost;
            factory.AutomaticRecoveryEnabled = true;

            try
            {
                _connection = factory.CreateConnection();
                _channel = _connection.CreateModel();

                _wasOpen = true;

#if DEBUG
                Output($"Opening queue: hostname:{_hostName} declaring queue:{declareQueue}", OutputType.Standard);
#endif
                if (declareQueue)
                {
                    _queueWasDeclared = true;
                    return _channel.QueueDeclare().QueueName;
                }
                else
                {

                    return null;
                }

            }
            catch (Exception ex)
            {
#if DEBUG
                Output($"Error in receive loop; {ex.Message}", OutputType.Standard);
#endif

                if (_connection != null && _connection.IsOpen)
                {
                    _connection.Close();

                    _connection = null;
                }
                throw;
            }
        }

        public void Send(string token, string responseQueueName, string content, string exchange, string routingKey)
        {
            if (!IsOpen)
            {
                if (_wasOpen)
                {
                    OpenInternal(_queueWasDeclared);
                    //if (_queueWasDeclared)
                    //    StartReceiveLoop(_channel.QueueDeclare().QueueName);
                }
                else
                {
                    throw new InvalidOperationException("Not connected.");
                }
            }

            IBasicProperties props = _channel.CreateBasicProperties();
            props.CorrelationId = token;
            props.ReplyTo = responseQueueName;

            _channel.BasicPublish(exchange, routingKey, props, Encoding.UTF8.GetBytes(content));
#if DEBUG
            Output($"Sending: {content}", OutputType.Send);
#endif
        }

        public void StartReceiveLoop(string queueName)
        {
            var messageConsumer = new QueueingBasicConsumer(_channel);
            _channel.BasicConsume(queueName, false, messageConsumer);

            Task.Run(() =>
            {
                while (true)
                {
                    BasicDeliverEventArgs args = null;
                    try
                    {
                        args = (BasicDeliverEventArgs)messageConsumer.Queue.Dequeue();
                        _channel.BasicAck(args.DeliveryTag, false);
                    }
                    catch (Exception ex)
                    {
                        // The consumer was canceled, the model closed, or the
                        // connection went away.
#if DEBUG
            Output($"Error: {ex.Message}", OutputType.Error);
#endif
                        if (Disconnected != null)
                            Disconnected(this, EventArgs.Empty);
                        break;
                    }

                    // report the message
                    try
                    {
                        OnMessageReceived(args);
                    }
                    catch (Exception ex)
                    {
#if DEBUG
            Output($"Error: {ex.Message}", OutputType.Error);
#endif
                    }
                }
            });
        }

        private void OnMessageReceived(BasicDeliverEventArgs deliveryArgs)
        {
            string body = Encoding.UTF8.GetString(deliveryArgs.Body);
#if DEBUG
            Output($"Received: {body}", OutputType.Receive);
#endif

            MessageReceivedEventArgs messageArgs = new MessageReceivedEventArgs(body, deliveryArgs.ConsumerTag, deliveryArgs.DeliveryTag, deliveryArgs.Exchange, deliveryArgs.Redelivered, deliveryArgs.RoutingKey, deliveryArgs.BasicProperties.CorrelationId);

            var safeCopy = MessageReceived;
            if (safeCopy != null)
            {
                safeCopy(this, messageArgs);
            }
        }

        public void Close()
        {
            try
            {
                _channel.Close();
                _connection.Close();
            }
            catch { }

            try
            {
                _channel.Dispose();
                _connection.Dispose();
            }
            catch { }
        }

        public event EventHandler<MessageReceivedEventArgs> MessageReceived;

        public event EventHandler Disconnected;
    }
}
