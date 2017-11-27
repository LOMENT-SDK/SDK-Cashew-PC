using Cashew.Services;

namespace Cashew.Internal.Network
{
    internal class ResponseQueue : AutoRecoveryQueueBase
    {
        private readonly ResponseHandler _handler;

        internal ResponseQueue(ResponseHandler handler)
        {
            _handler = handler;
        }

        protected override IRabbitMQService CreateRabbitService()
        {
            return Sdk.RabbitMQService.CreateService(NetworkConstants.AMQP_RESPONSE_QUEUE_HOSTNAME, NetworkConstants.AMQP_RESPONSE_QUEUE_USERNAME, NetworkConstants.AMQP_RESPONSE_QUEUE_PASSWORD, NetworkConstants.AMQP_RESPONSE_QUEUE_V_HOST, 65);
        }

        protected override void OnMessageReceived(MessageReceivedEventArgs e)
        {
            _handler.HandleResponse(e.Token, e.Body);
        }
    }
}
