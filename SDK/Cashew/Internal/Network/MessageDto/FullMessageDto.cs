using System.Collections.Generic;

namespace Cashew.Internal.Network.MessageDto
{
    public class FullMessageDto
    {
        public MsgFullDto MSG_FULL { get; set; }

    }

    public class HeaderMessageDto
    {
        public MessageHeaderDto MSG_HEADER { get; set; }

    }

    public class MsgFullDto
    {
        public MessageHeaderDto header { get; set; }
        public string body { get; set; }
    }

    public class MessageHeaderDto
    {
        public string id { get; set; }
        public string from { get; set; }
        public string type { get; set; }
        public string folder { get; set; }
        public string creation_timestamp { get; set; }
        public string last_update_timestamp { get; set; }
        public SubjectDto subject { get; set; }
        public List<string> to { get; set; }
        public MessageControlParamsDto control_params { get; set; }
        public MessageStatusDto status { get; set; }
        public BodySizeDto body { get; set; }
        public AttachmentsDto attachments { get; set; }
        public ClientParamsDto client_params { get; set; }
        public string group_id { get; set; }
    }

    public class ClientParamsDto
    {
        public string e_key_v { get; set; }
        public string msg_token { get; set; }
    }

    public class BodySizeDto
    {
        public string size { get; set; }
    }

    public class MessageStatusDto
    {
        public List<string> folder { get; set; }
        public List<string> read { get; set; }
        public List<string> ack { get; set; }
        public string self_deleted { get; set; }
        public List<string> recipient_deleted { get; set; }
    }

    public class MessageControlParamsDto
    {
        public string priority { get; set; }
        public string restricted { get; set; }
        public string ack { get; set; }
        public string expiry { get; set; }
    }

    public class AttachmentDataDto
    {
        public string name { get; set; }
        public string size { get; set; }
        public string padding { get; set; }
    }

    public class AttachmentsDto
    {
        public int count { get; set; }
        public List<AttachmentDataDto> data { get; set; }
    }

}
