
using Cashew.Internal.Network;
using Cashew.Utility.Helper;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;

namespace Cashew.Internal.Loment
{
    internal class LomentApi
    {
        private const string XAuthHeader = "X-Auth-Creds";
        private Uri _baseUri;

        private readonly Dictionary<string, NetworkCredential> _credentialCache = new Dictionary<string, NetworkCredential>();

        internal LomentApi()
        {
            if (Sdk.IsInitialized == false)
            {
                throw new InvalidOperationException("The SDk is not initialized. Please call Sdk.Initialize() first.");
            }

            _baseUri = new Uri(NetworkConstants.BASE_API_URL);
        }

        private async Task<string> PostWithHttpClient(string uri, HttpContent content)
        {
            using (var handler = new HttpClientHandler
            {
                UseDefaultCredentials = false,
                Credentials = NetworkHelper.GetCredentials(NetworkConstants.BASE_API_URL, uri),
                PreAuthenticate = true,
            })
            {
                {
                    using (var client = new HttpClient(handler))
                    {
                        client.BaseAddress = _baseUri;
                        client.DefaultRequestHeaders.Add(XAuthHeader, APIKeyManager.Key);

                        try
                        {
                            var response = await client.PostAsync(uri, content);

                            return await response.Content.ReadAsStringAsync();
                        }
                        catch (Exception e)
                        {
                            return e.Message;
                        }
                    }
                }
            }
        }

  
        private async Task<string> PostWithWebRequest(string relativeUri, HttpContent content)
        {
            var buffer = content.ReadAsByteArrayAsync().Result;

            var request = (HttpWebRequest)WebRequest.Create(string.Format("{0}/{1}", NetworkConstants.BASE_API_URL, relativeUri));

            request.Credentials = NetworkHelper.GetCredentials(NetworkConstants.BASE_API_URL, relativeUri);
            request.Method = "POST";
            request.Accept = "application/json";
            request.ContentType = "application/x-www-form-urlencoded";
            request.Headers[XAuthHeader] = APIKeyManager.Key;

            using (var requestStream = await request.GetRequestStreamAsync())
            {
                requestStream.Write(buffer, 0, buffer.Length);

                using (var response = (HttpWebResponse)await request.GetResponseAsync())
                using (var reader = new StreamReader(response.GetResponseStream()))
                {
                    return reader.ReadToEnd();
                }
            }
        }

        private async Task<string> Post(string uri, HttpContent content)
        {
            var response = string.Empty;

            if (PlatformDetector.DetectedPlatform == PlatformLibrary.Net || PlatformDetector.DetectedPlatform == PlatformLibrary.WinRT)
            {
                response = await PostWithHttpClient(uri, content);
            }
            else
            {
                response = await PostWithWebRequest(uri, content);
            }

#if DEBUG
            Debug.WriteLine("Post response: " + response);
#endif

            return response;
        }


        private async Task<string> GetWithHttpClient(string uri)
        {
            using (var handler = new HttpClientHandler
            {
                UseDefaultCredentials = false,
                Credentials = NetworkHelper.GetCredentials(NetworkConstants.BASE_API_URL, uri),
                PreAuthenticate = true
            })
            using (var client = new HttpClient(handler))
            {
                client.BaseAddress = _baseUri;

                client.DefaultRequestHeaders.Add(XAuthHeader, APIKeyManager.Key);

                try
                {
                    return await client.GetStringAsync(uri);
                }
                catch (Exception e)
                {
                    return e.Message;
                }
            }
        }

        private async Task<string> GetWithWebRequest(string relativeUri)
        {
            var request = (HttpWebRequest)WebRequest.Create(string.Format("{0}{1}", NetworkConstants.BASE_API_URL, relativeUri));

            request.Credentials = NetworkHelper.GetCredentials(NetworkConstants.BASE_API_URL, relativeUri);
            request.Method = "GET";
            request.Accept = "application/json";
            request.Headers[XAuthHeader] = APIKeyManager.Key;

            using (var response = (HttpWebResponse)await request.GetResponseAsync())
            {
                using (var reader = new StreamReader(response.GetResponseStream()))
                {
                    return reader.ReadToEnd();
                }
            }
        }

        private async Task<string> Get(string uri)
        {
            var response = string.Empty;

            if (PlatformDetector.DetectedPlatform == PlatformLibrary.Net || PlatformDetector.DetectedPlatform == PlatformLibrary.WinRT)
            {
                response = await GetWithHttpClient(uri);
            }
            else
            {
                response = await GetWithWebRequest(uri);
            }

#if DEBUG
            Debug.WriteLine("Get response: " + response);
#endif
            return response;
        }

        internal Task<string> CreateCashewAccount(string userName, string cashewAccountName, string deviceId)
        {
            string url = string.Format("/user/{0}/product/cashewnut/device/{1}/accounts/new", userName, deviceId);

            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("cashewnut_username", cashewAccountName)
            });

            return Post(url, content);
        }

        internal Task<string> GetCashewAccounts(string userName, string deviceId)
        {
            string url = string.Format("/user/{0}/product/cashewnut/device/{1}/accounts/all", userName, deviceId);

            return Get(url);
        }

        internal async Task<string> GetUserAccount(string userName)
        {
            return await Get(string.Format("user/{0}/account", userName));
        }


        internal async Task<string> BeginUserAccountRecovery(string userName)
        {
            return await Get(string.Format("user/{0}/account/recovery", userName));
        }


        internal async Task<string> RegisterUserAccount(string fullName, string pass, string primaryEmail, string primaryMobileNumber, string countryAbbreviation)
        {
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("name", fullName),
                new KeyValuePair<string, string>("password", pass),
                new KeyValuePair<string, string>("primary_email", primaryEmail),
                new KeyValuePair<string, string>("primary_mobile_number", primaryMobileNumber),
                new KeyValuePair<string, string>("country_abbrev", countryAbbreviation),
            });

            return await Post("user/register/", content);
        }

        internal Task<string> AuthenticateUserAccount(string userName, string pass)
        {
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("password", pass),
            });

            return Post(string.Format("user/{0}/authenticate", userName), content);
        }


        internal Task<string> CreateDevice(string userName, string deviceId, string deviceTypeId, string operatingSystem, string deviceName)
        {
            var parameter = new List<KeyValuePair<string, string>>
            {
                new KeyValuePair<string, string>("device_identifier", deviceId),
                new KeyValuePair<string, string>("device_type_id", deviceTypeId),
                new KeyValuePair<string, string>("operating_system", operatingSystem),
            };

            if (!string.IsNullOrEmpty(deviceName))
            {
                parameter.Add(new KeyValuePair<string, string>("device_name", deviceName));
            }

            var content = new FormUrlEncodedContent(parameter);

            return Post(string.Format("user/{0}/device/new", userName), content);
        }


        internal async Task<string> GetAllDevicesDetails(string userName)
        {
            return await Get(string.Format("user/{0}/device/all", userName));
        }


        internal Task<string> GetFeatureDeviceSubscription(string userName, int featureId, string deviceId)
        {
            return Get(string.Format("user/{0}/subscription/{1}/device/{2}", userName, featureId, deviceId));
        }


        internal Task<string> GetFriendSuggestions(string userName, string password, List<string> phoneNumbers, List<string> emailAddresses)
        {
            string url = string.Format("user/{0}/contact/cashew/search", userName);

            var requestObject = new { phone = phoneNumbers, email = emailAddresses.Select(x => x.ToLower()) };

            var parameter = new List<KeyValuePair<string, string>>
            {
                new KeyValuePair<string, string>("password", password),
                new KeyValuePair<string, string>("contact", JsonConvert.SerializeObject(requestObject))
            };

            var content = new FormUrlEncodedContent(parameter);

            return Post(url, content);
        }
    }
}