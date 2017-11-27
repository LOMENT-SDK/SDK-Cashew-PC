using Cashew.Model;
using Org.BouncyCastle.Utilities.Encoders;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{
    internal static class NetworkHelper
    {
        private static Dictionary<string, NetworkCredential> _credentialCache = new Dictionary<string, NetworkCredential>();


        internal static string GetAuthKey(CNLomentAccount lomentAccount, CNCashewAccount cashewAccount)
        {
            string key = lomentAccount.Email + ":" + lomentAccount.Password + ":" +
                    cashewAccount.UserName + ":" + lomentAccount.DeviceId;

            return Base64.ToBase64String(Encoding.UTF8.GetBytes(key));
        }

        internal static NetworkCredential GetCredentials(string baseUrl, string relativeUri)
        {
            if (!_credentialCache.ContainsKey($"{baseUrl}/{relativeUri}"))
            {
                _credentialCache.Add($"{baseUrl}/{relativeUri}", new NetworkCredential(NetworkConstants.HTTP_DIGEST_USER, NetworkConstants.HTTP_DIGEST_PASSWORD, $"{baseUrl}/{relativeUri}"));
            }

            return _credentialCache[$"{baseUrl}/{relativeUri}"];
        }

        internal static NetworkCredential GetCredentialsForAttachmentUpload(string baseUrl, string relativeUri)
        {
            if (!_credentialCache.ContainsKey($"{baseUrl}/{relativeUri}"))
            {
                _credentialCache.Add($"{baseUrl}/{relativeUri}", new NetworkCredential(NetworkConstants.HTTP_UPLOAD_DIGEST_USER, NetworkConstants.HTTP_UPLOAD_DIGEST_PASSWORD, $"{baseUrl}/{relativeUri}"));
            }

            return _credentialCache[$"{baseUrl}/{relativeUri}"];
        }

        internal static NetworkCredential GetCredentialsForAttachmentDownload(string baseUrl, string relativeUri)
        {
            if (!_credentialCache.ContainsKey($"{baseUrl}/{relativeUri}"))
            {
                _credentialCache.Add($"{baseUrl}/{relativeUri}", new NetworkCredential(NetworkConstants.HTTP_DOWNLOAD_DIGEST_USER, NetworkConstants.HTTP_DOWNLOAD_DIGEST_PASSWORD, $"{baseUrl}/{relativeUri}"));
            }

            return _credentialCache[$"{baseUrl}/{relativeUri}"];
        }

        public static async Task<bool> IsConnectedToInternet()
        {
            try
            {
                using (var client = new HttpClient())
                {
                    var response = await client.GetAsync("http://www.google.com");
                    return response.IsSuccessStatusCode;
                }
            }
            catch
            {
                return false;
            }
        }
    }
}
