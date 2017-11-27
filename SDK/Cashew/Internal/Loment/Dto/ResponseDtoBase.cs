using Newtonsoft.Json;

namespace Cashew.Internal.Loment.Dto
{
    internal class ResponseDtoBase
    {
        public string Comments { get; set; }

        public int Status { get; set; }

        [JsonProperty("utc_timestamp")]
        public string UtcTimestamp { get; set; }

        [JsonIgnore]
        private ResponseMeta _responseMeta;

        [JsonIgnore]
        public ResponseMeta ResponseMeta
        {
            get
            {
                return _responseMeta ?? (_responseMeta = ResponseUtil.GetResponseMetaForCode(Status));
            }   
        }
        
        public override string ToString()
        {
            return JsonConvert.SerializeObject(this);
        }
    }
}
