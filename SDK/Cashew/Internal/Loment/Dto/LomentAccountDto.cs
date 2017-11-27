using Newtonsoft.Json;

namespace Cashew.Internal.Loment.Dto
{
    internal class LomentAccountDto : ResponseDtoBase
    {
        public int UserId { get; set; }

        public string Name { get; set; }

        [JsonProperty(PropertyName = "primary_mobile_number")]
        public string PrimaryMobileNumber { get; set; }

        [JsonProperty(PropertyName = "update_time")]
        public UpdateTimeDto UpdateTime { get; set; }
    }
}
