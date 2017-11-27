using System.Collections.Generic;

namespace Cashew.Internal.Network.MessageDto
{
    public class FriendRequestResponseDto
    {
        public string id { get; set; }
        public string from { get; set; }
        public string type { get; set; }
        public string folder { get; set; }
        public string creation_timestamp { get; set; }
        public string last_update_timestamp { get; set; }
        public List<string> to { get; set; }
        public int status { get; set; }
        public SubjectDto subject { get; set; }
    }
}

