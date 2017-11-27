using System.Collections.Generic;

namespace Cashew.Internal.Network.MessageDto
{
    public class GroupConfResponseDto
    {
        public int status { get; set; }
        public string comments { get; set; }
        public GrpConfDto GRP_CONF { get; set; }
        public int cmd { get; set; }
    }

    public class GrpConfDto
    {
        public string group_id { get; set; }
        public string name { get; set; }
        public string grp_type { get; set; }
        public string owner { get; set; }
        public string creation_time { get; set; }
        public string last_update_time { get; set; }
        public int type { get; set; }
        public List<string> members { get; set; }
    }

}
