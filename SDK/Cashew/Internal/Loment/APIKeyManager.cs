using Cashew.Internal.Network;
using Cashew.Utiltity.Extensions;
using Newtonsoft.Json;
using System;

namespace Cashew.Internal.Loment
{
    internal static class APIKeyManager
    {
        private static string _key;

        public static string Key
        {
            get
            {
                return _key ?? (_key = GenerateKey());
            }
        }

        private static string GenerateKey()
        {
            long time = DateTime.UtcNow.JavaTimeMillis();
            string key = time + ":" + Sdk.ClientId + ":" + Sdk.PartnerId + ":" + time;

            string encryptedKey = APICrypter.Encrypt(key, NetworkConstants.PACKAGE_NAME);

            var jsonObject = new { package_name = NetworkConstants.PACKAGE_NAME, key = encryptedKey };

            var ret = JsonConvert.SerializeObject(jsonObject);

            return ret;
        }
    }
}
