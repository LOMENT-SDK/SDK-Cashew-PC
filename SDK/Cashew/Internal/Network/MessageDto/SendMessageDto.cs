using Newtonsoft.Json;

namespace Cashew.Internal.Network.MessageDto
{
    public class SendMessageDto
    {
        public SendMessageDataDto data { get; set; } = new SendMessageDataDto();
        public string cmd { get; set; }
    }

    public class SendMessageToDto
    {
        [JsonProperty("1")]
        public string To_1 { get; set; }
    }

    public class SendMessageBodyDto
    {
        public string size { get; set; }
    }

    public class SendMessageControlParamsDto
    {
        public int restricted { get; set; }
        public int ack { get; set; }
        public int priority { get; set; }
        public int expiry { get; set; }
    }

    public class SendMessageSubjectDto
    {
        public string content { get; set; }
        public int size { get; set; }
    }

    public class SendMessageClientParamsDto
    {
        public string msg_token { get; set; }
        public string e_key_v { get; set; }
    }

    public class SendMessageHeaderDto
    {
        public string id { get; set; }
        public SendMessageToDto to { get; set; } = new SendMessageToDto();
        public string folder { get; set; }
        public SendMessageBodyDto body { get; set; } = new SendMessageBodyDto();
        public string token { get; set; }
        public SendMessageControlParamsDto control_params { get; set; } = new SendMessageControlParamsDto();
        public SendMessageSubjectDto subject { get; set; } = new SendMessageSubjectDto();
        public SendMessageClientParamsDto client_params { get; set; } = new SendMessageClientParamsDto();
        public string from { get; set; }
        public int type { get; set; }
    }

    public class SendMessageDataDto
    {
        public string body { get; set; }
        public SendMessageHeaderDto header { get; set; } = new SendMessageHeaderDto();
    }

  
}
