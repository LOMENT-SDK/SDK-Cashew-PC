using Cashew.Internal.Loment;
using Cashew.Model;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{
    internal class AttachmentWebService
    {
        private const string XAuthHeader = "X-Auth-Creds";
        private const string UploadEndpoint = "uploadAtt.php";
        private const string DownloadEndpoint = "downloadAtt.php";

        private readonly Dictionary<string, NetworkCredential> _credentialCache = new Dictionary<string, NetworkCredential>();
        private Uri _baseUri;

        internal AttachmentWebService()
        {
            if (Sdk.IsInitialized == false)
            {
                throw new InvalidOperationException("The SDk is not initialized. Please call Sdk.Initialize() first.");
            }

            _baseUri = new Uri(NetworkConstants.HTTP_ATTACHMENT_SERVER_URL);
        }

        private NetworkCredential GetCreds(string relativeUri)
        {
            if (relativeUri.Equals(UploadEndpoint))
            {
                return NetworkHelper.GetCredentialsForAttachmentUpload(NetworkConstants.HTTP_ATTACHMENT_SERVER_URL, relativeUri);
            }
            else if (relativeUri.Equals(DownloadEndpoint))
            {
                return NetworkHelper.GetCredentialsForAttachmentDownload(NetworkConstants.HTTP_ATTACHMENT_SERVER_URL, relativeUri);
            }
            else
            {
                throw new ArgumentException("Unknown endpoint.");
            }
        }

        internal async Task<string> UploadAttachmentAsync(byte[] data, string fileName)
        {
            MultipartFormDataContent content = new MultipartFormDataContent("---------------------------4664151417711");

            ByteArrayContent byteContent = new ByteArrayContent(data);
            byteContent.Headers.Add("Content-Type", "application/octet-stream");
            content.Add(byteContent, "file", fileName);

            string response = await PostWithHttpAsStringClient(UploadEndpoint, content);

            var responseObject = JObject.Parse(response);

            var status = (int)responseObject.GetValue("status");

            if (status != 0)
            {
                Debug.WriteLine(responseObject.GetValue("comments"));
                return null;
            }
            else
            {
                return (string)responseObject.GetValue("uploaded_file_name");
            }
        }

        internal async Task<Stream> DownloadAttachmentAsync(string fileName, CNLomentAccount lomentAccount, CNCashewAccount cashewAccount)
        {
            var values = new List<KeyValuePair<string, string>>();
            values.Add(new KeyValuePair<string, string>("file_name", fileName));
            values.Add(new KeyValuePair<string, string>("auth_key", NetworkHelper.GetAuthKey(lomentAccount, cashewAccount)));
            HttpContent content = new FormUrlEncodedContent(values);


            Stream stream = await PostWithHttpClientAsStreamArray(DownloadEndpoint, content);
            return stream;
        }

        private async Task<string> PostWithHttpAsStringClient(string uri, HttpContent content)
        {
            using (var handler = new HttpClientHandler
            {
                UseDefaultCredentials = false,
                Credentials = GetCreds(uri),
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

        private async Task<Stream> PostWithHttpClientAsStreamArray(string uri, HttpContent content)
        {
            using (var handler = new HttpClientHandler
            {
                UseDefaultCredentials = false,
                Credentials = GetCreds(uri),
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
                            if (response.StatusCode == HttpStatusCode.OK)
                            {
                                return await response.Content.ReadAsStreamAsync();
                            }
                            else
                            {
                                return null;
                            }
                        }
                        catch (Exception)
                        {
                            return null;
                        }
                    }
                }
            }
        }

    }
}
