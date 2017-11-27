using Cashew.Services;

namespace Cashew.Internal.Network
{
    internal class RequestQueue
    {
        private IRabbitMQService _rabbitService;

        internal void Open()
        {
            _rabbitService = Sdk.RabbitMQService.CreateService(NetworkConstants.AMQP_REQUEST_QUEUE_HOSTNAME, NetworkConstants.AMQP_REQUEST_QUEUE_USERNAME, NetworkConstants.AMQP_REQUEST_QUEUE_PASSWORD, NetworkConstants.AMQP_REQUEST_QUEUE_V_HOST, 60);

            _rabbitService.Open();
        }



        internal void Send(string token, string queueName, string message)
        {
            if (_rabbitService.IsOpen == false)
            {
                Open();
            }
            _rabbitService.Send(token, queueName, message, "", "rpc_queue");
        }
    }
}
