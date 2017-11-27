using Newtonsoft.Json;

namespace Cashew.Internal.Loment.Dto
{
    internal class CashewAccountDto
    {
        [JsonProperty(PropertyName = "id")]
        public string id { get; set; }

        [JsonProperty(PropertyName = "status")]
        public string status { get; set; }

        [JsonProperty(PropertyName = "creation_date")]
        public string creation_date { get; set; }

        [JsonProperty(PropertyName = "last_update_date")]
        public string last_update_date { get; set; }

        [JsonProperty(PropertyName = "username")]
        public string username { get; set; }
    }
}
