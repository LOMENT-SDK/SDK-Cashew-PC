using System.Collections.Generic;

namespace Cashew.Internal.Network.MessageDto
{
    internal class IncomingFriendRequestDto
    {
        public IncomingFriendRequesHeader header { get; set; }
    }

    internal class IncomingFriendRequesHeader
    {
        public string id { get; set; }
        public string from { get; set; }
        public string type { get; set; }
        public string folder { get; set; }
        public string creation_timestamp { get; set; }
        public string last_update_timestamp { get; set; }
        public SubjectDto subject { get; set; }
        public List<string> to { get; set; }
        public int status { get; set; }
        public IncomingFriendRequesClientParams client_params { get; set; }
    }
  

    internal class IncomingFriendRequesClientParams
    {
        public string email { get; set; }
    }

}
