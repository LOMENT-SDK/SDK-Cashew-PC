using Cashew.Internal.Loment.Dto;
using Newtonsoft.Json;
using System;

namespace Cashew.Internal.Loment.Extensions
{
    internal static class JsonConvertExtensions
    {
        public static T SafeDeserializeObject<T>(this string json) where T : ResponseDtoBase, new()
        {
            try
            {
                var obj = JsonConvert.DeserializeObject<T>(json);

                if (obj == null)
                    throw new JsonException("null object after JsonConvert.DeserializeObject<T>");

                return obj;
            }
            catch (Exception e)
            {
                return new T
                {
                    Comments = string.Format("Failed to Deserialize JSON '{0}' Error: {1}", json, e.Message),
                    Status = ResponseUtil.UnknownCodeKey,
                    UtcTimestamp = DateTime.UtcNow.ToString()
                };
            }
        }
    }
}
