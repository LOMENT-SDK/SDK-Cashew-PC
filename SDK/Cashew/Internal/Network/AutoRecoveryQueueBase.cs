using Cashew.Services;
using Cashew.Utility.Helper;
using System;
using System.Diagnostics;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{
    internal abstract class AutoRecoveryQueueBase
    {
        private bool _isOpen = false;
        private bool _declareQueue = false;
        private volatile bool _isOpening = false;
        private object _lock = new object();

        protected IRabbitMQService _rabbitService;

        internal string Name { get; private set; }

        protected abstract IRabbitMQService CreateRabbitService();

        internal void Open(string queueName)
        {
            if (_isOpening)
                return;

            _isOpening = true;

            try
            {
                if (_isOpen && queueName.Equals(Name, StringComparison.OrdinalIgnoreCase) == false)
                {
                    _rabbitService.Close();
                    Name = queueName;
                    OpenAndStartQueue();
                }
                else if (_isOpen == false)
                {
                    Name = queueName;
                    OpenAndStartQueue();
                }
            }
            catch { throw; }
            finally { _isOpen = false; }
        }

        internal void OpenAndDeclare()
        {
            if (_isOpening)
                return;

            _isOpening = true;

            try
            {
                _declareQueue = true;
                OpenAndStartQueue();
            }
            catch { throw; }
            finally { _isOpen = false; }
        }

        private void OpenAndStartQueue()
        {
            Debug.WriteLine($"Opening queue {Name}");

            if (_rabbitService != null)
            {
                _rabbitService.MessageReceived -= OnRabbitServiceMessageReceived;
                _rabbitService.Disconnected -= OnQueueDisconnected;
            }

            _rabbitService = CreateRabbitService();

            if (_declareQueue)
                Name = _rabbitService.OpenAndDeclareQueue();
            else
                _rabbitService.Open();

            _rabbitService.MessageReceived += OnRabbitServiceMessageReceived;
            _rabbitService.Disconnected += OnQueueDisconnected;
            _rabbitService.StartReceiveLoop(Name);

            _isOpen = true;


            Debug.WriteLine($"Queue {Name} opened.");
        }

        private void OnRabbitServiceMessageReceived(object sender, MessageReceivedEventArgs e)
        {
            OnMessageReceived(e);
        }

        protected virtual void OnMessageReceived(MessageReceivedEventArgs e)
        { }

        private async Task RecoverQueueConnection()
        {
            Debug.WriteLine($"trying to recover queue {Name}");
            while (await NetworkHelper.IsConnectedToInternet() == false)
            {
                Debug.WriteLine("No Internet. Keep waiting...");
                await Task.Delay(5000);
            }

            try
            {
                OpenAndStartQueue();
                EventHelper.NotifyEvent(this, ConnectionRecovered);
            }
            catch
            {
                //Lets keep trying.
                Debug.WriteLine($"Failed to re-open queue {Name}. Lets try again.");
#pragma warning disable 4014
                RecoverQueueConnection();
#pragma warning restore 4014

            }
        }

        private async void OnQueueDisconnected(object sender, EventArgs e)
        {
            _isOpen = false;
            Debug.WriteLine($"Queue {Name} disconnected.");
            _rabbitService.Disconnected -= OnQueueDisconnected;
            _rabbitService = null;

            EventHelper.NotifyEvent(this, Disconnected);
            await RecoverQueueConnection();
        }

        internal event EventHandler Disconnected;

        internal event EventHandler ConnectionRecovered;
    }
}
