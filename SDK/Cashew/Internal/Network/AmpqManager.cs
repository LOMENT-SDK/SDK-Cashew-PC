using Cashew.Internal.Encryption;
using Cashew.Internal.Loment;
using Cashew.Internal.Network.MessageDto;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using Cashew.Services;
using Cashew.Utiltity.Extensions;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{
    internal class AmpqManager
    {
        private DateTime _lastSeenMessageDateTime = new DateTime(1975, 1, 1).ToUniversalTime();

        private volatile bool _isAuthenticated = false;

        private object _callLock = new object();

        private const int CMD_AUTH = 1;
        private const int CMD_MESSAGE = 2;
        private const int CMD_ATTACHMENT = 3;
        private const int CMD_UPDATE_STATUS = 4;
        private const int CMD_SYNC = 5;
        private const int CMD_DOWNLOAD_ATTACHMENT = 6;
        private const int CMD_CREATE_GROUP = 8;
        private const int CMD_ADD_MEMBER = 9;
        private const int CMD_REMOVE_MEMBER = 10;
        private const int CMD_EDIT_NAME = 11;
        private const int CMD_CHANGE_OWNER = 12;
        private const int CMD_GROUP_CONF = 13;

        private const long MAXIMUM_ATTACHMENT_UPLOAD_SIZE = 15000000;

        private static int fakeToken = 0;
        private string lastAuthToken = null;

        private CNLomentAccount _lomentAccount;
        private CNCashewAccount _cashewAccount;
        private ResponseHandler _responseHandler;
        private RequestQueue _requestQueue;
        private ResponseQueue _responseQueue;
        private MessageQueue _messageQueue;

        public AmpqManager()
        {

        }

        public Task<CashewResponse<AuthenticationResponse>> StartAsync(CNLomentAccount lomentAccount, CNCashewAccount cashewAccount, Action<CNMessageBase> handleMessageReceived, Action<CNGroup> handleGroup, int timeOut)
        {
            _isAuthenticated = false;

            _lomentAccount = lomentAccount;
            _cashewAccount = cashewAccount;


            _requestQueue = new RequestQueue();
            _messageQueue = new MessageQueue((message) =>
            {
                _lastSeenMessageDateTime = message.SendTime > _lastSeenMessageDateTime ? message.SendTime : _lastSeenMessageDateTime;
                handleMessageReceived(message);
            }, handleGroup);
            _responseHandler = new ResponseHandler(_messageQueue, _lomentAccount, _cashewAccount, this);
            _responseQueue = new ResponseQueue(_responseHandler);

            _responseQueue.OpenAndDeclare();
            _requestQueue.Open();

            _responseQueue.Disconnected += OnResponseQueueDisconnected;
            _responseQueue.ConnectionRecovered += OnResponseQueueConnectionRecovered;

            return AuthenticateUser(timeOut);
        }


        private void OnResponseQueueDisconnected(object sender, EventArgs e)
        {
            // When a connection is lost we need to re-authenticate next time we try communicate. See OnResponseQueueConnectionRecovered()
            _isAuthenticated = false;
        }

        private async void OnResponseQueueConnectionRecovered(object sender, EventArgs e)
        {
            // After connections are recovered we need to authenticate again. Otherwise sending messages through the RequestQueue will fail.
            var response = await AuthenticateUser(NetworkConstants.QueueDefaultTimeOut);
            if (response.IsSuccess && response.Entity.IsSyncRequired)
            {
                // Now wee need to catch up on messages we missed while we were offline.
                await SyncMessagesAsync(_lastSeenMessageDateTime, NetworkConstants.QueueDefaultTimeOut);
            }
        }

        private async Task<CashewResponse<AuthenticationResponse>> AuthenticateUser(int timeOut)
        {
            var authMessage = new
            {
                cmd = CMD_AUTH,
                key = GetAuthKey(),
                api_key = APIKeyManager.Key
            };

            var auhthJson = JsonConvert.SerializeObject(authMessage);

            string token = ConstructToken("Auth@");
            lastAuthToken = token;

            var response = await CallSecuredAsync<AuthenticationResponse>(token, auhthJson, MessageRequestType.AUTH, null, timeOut);

            _isAuthenticated = response.IsSuccess;

            return response;
        }



        internal async Task<CashewResponse<CNMessage>> SendMessageAsync(string message, string to, CNMessageOptions options, CNAttachmentData[] attachments, bool sendAsGroupMessaeg, int timeOut)
        {

            SendMessageDto dto = new SendMessageDto();
            string token = ConstructToken("header-");

            dto.cmd = CMD_MESSAGE.ToString();
            dto.data.body = message;
            dto.data.header.body.size = message.Length.ToString();
            dto.data.header.client_params.e_key_v = "V1";
            dto.data.header.client_params.msg_token = token;
            dto.data.header.control_params.ack = options.RequestReadAcknlowledgement ? 1 : -1;
            dto.data.header.control_params.expiry = options.ExpiryInMinutes;
            dto.data.header.control_params.priority = (int)options.Priority;
            dto.data.header.control_params.restricted = options.IsForwardResticted ? 3 : -1;
            dto.data.header.folder = "inbox";
            dto.data.header.from = _cashewAccount.UserName;
            dto.data.header.subject.content = "  ";
            dto.data.header.subject.size = 2;
            dto.data.header.token = token;
            dto.data.header.type = 1;
            dto.data.header.id = "new";

            if (!sendAsGroupMessaeg)
            {
                dto.data.header.to.To_1 = to;
            }

            string json = JsonConvert.SerializeObject(dto);
            JObject jsonObject = (JObject)JsonConvert.DeserializeObject(json);
            JProperty headerProperty = jsonObject.Descendants().Where(x => x is JProperty).Cast<JProperty>().Single(x => x.Name == "header");

            if (sendAsGroupMessaeg)
            {
                JProperty toProperty = jsonObject.Descendants().Where(x => x is JProperty).Cast<JProperty>().Single(x => x.Name == "1");
                toProperty.Remove();
                ((JObject)headerProperty.Value).Add("group_id", to);
            }

            if (attachments != null && attachments.Length > 0)
            {
                JObject attachmentsObj = new JObject();
                ((JObject)headerProperty.Value).Add("attachments", attachmentsObj);

                JObject attachmentData = new JObject();
                attachmentsObj.Add("data", attachmentData);
                attachmentsObj.Add("count", attachments.Length.ToString());
                for (int i = 0; i < attachments.Length; i++)
                {
                    if (attachments[i].Data.Length > MAXIMUM_ATTACHMENT_UPLOAD_SIZE)
                    {
                        return new CashewResponse<CNMessage>(-1, $"One ore more attachments exceed the maximum size of {MAXIMUM_ATTACHMENT_UPLOAD_SIZE} byte.", false, null);
                    }

                    int paddingsize = (int)(16 - (attachments[i].Data.Length % 16));
                    if (paddingsize == 16)
                    {
                        paddingsize = 0;
                    }


                    JObject attachmentObj = new JObject();
                    attachmentObj.Add("padding", paddingsize.ToString());
                    attachmentObj.Add("size", attachments[i].Data.Length.ToString());
                    attachmentObj.Add("name", attachments[i].Name);

                    attachmentData.Add((i + 1).ToString(), attachmentObj);
                }
            }

            JProperty bodyProperty = jsonObject.Descendants().Where(x => x is JProperty).Cast<JProperty>().Single(x => x.Path == "data.body");
            (bodyProperty.Value as JValue).Value = EncryptMessage(message, _cashewAccount.UserName, to, sendAsGroupMessaeg);


            json = JsonConvert.SerializeObject(jsonObject);

            // the state object is a tuple that is needed in the response handler. 
            return await CallSecuredAsync<CNMessage>(token, json, sendAsGroupMessaeg ? MessageRequestType.SEND_GROUP_MESSAGE : MessageRequestType.SEND_MESSAGE, new Tuple<SendMessageDto, CNAttachmentData[], string>(dto, attachments, to), timeOut);
        }

        internal Task<CashewResponse> MarkMessageAsDeleted(int messageServerId, string messageSenderId, int timeOut)
        {
            JObject deleteObj = new JObject();

            JObject dataObj = new JObject();
            if (string.Compare(messageSenderId, _cashewAccount.UserName, StringComparison.OrdinalIgnoreCase) == 0)
            {
                deleteObj.Add("self_deleted", "1");
                dataObj.Add(messageServerId.ToString(), deleteObj);
            }
            else
            {
                deleteObj.Add("recipient_deleted", "1");
                JObject statusObj = new JObject();

                statusObj.Add("status", deleteObj);
                dataObj.Add(messageServerId.ToString(), statusObj);
            }

            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_UPDATE_STATUS);
            cmd.Add("data", dataObj);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.DELETE_MESSAGE, null, timeOut);
        }

        internal async Task<CashewResponse<CNGroup>> CreateGroup(string groupName, List<string> members, int timeOut)
        {
            if (!members.Contains(_cashewAccount.UserName))
                members.Add(_cashewAccount.UserName);

            JObject data = new JObject();
            data.Add("name", groupName);
            data.Add("type", "2");
            data.Add("members", new JArray(members.ToArray()));

            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_CREATE_GROUP);
            cmd.Add("data", data);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            var groupCreateResponse = await CallSecuredAsync<string>(token, json, MessageRequestType.CREATE_GROUP, null, timeOut);
            if (groupCreateResponse.IsSuccess)
            {
                return await RequestGroupDetails(groupCreateResponse.Entity, timeOut);
            }
            else
            {
                return new CashewResponse<CNGroup>(groupCreateResponse.ResponseCode, groupCreateResponse.Comment, false, null);
            }
        }

        internal Task<CashewResponse> RemoveMembersFromGroup(string groupId, List<string> membersToRemove, int timeOut)
        {
            JObject data = new JObject();
            data.Add("group_id", groupId);
            data.Add("members_to_remove", new JArray(membersToRemove.ToArray()));


            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_REMOVE_MEMBER);
            cmd.Add("data", data);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.REMOVE_MEMBERS_FROM_GROUP, null, timeOut);
        }

        internal Task<CashewResponse> AddMembersToGroup(string groupId, List<string> membersToAdd, int timeOut)
        {
            JObject data = new JObject();
            data.Add("group_id", groupId);
            data.Add("members_to_add", new JArray(membersToAdd.ToArray()));

            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_ADD_MEMBER);
            cmd.Add("data", data);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.ADD_MEMBERS_TO_GROUP, null, timeOut);
        }

        internal Task<CashewResponse> LeaveGroup(string groupId, int timeOut)
        {
            JObject data = new JObject();
            data.Add("group_id", groupId);
            data.Add("members_to_remove", new JArray(new string[] { _cashewAccount.UserName }));

            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_REMOVE_MEMBER);
            cmd.Add("data", data);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.REMOVE_MEMBERS_FROM_GROUP, null, timeOut);
        }

        internal Task<CashewResponse> MarkMessageAsAcknowledgedAsync(string messageServerId, int timeOut)
        {
            JObject updateObj = new JObject();

            JObject dataObj = new JObject();

            updateObj.Add("ack", "1");
            JObject statusObj = new JObject();

            statusObj.Add("status", updateObj);
            dataObj.Add(messageServerId.ToString(), statusObj);


            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_UPDATE_STATUS);
            cmd.Add("data", dataObj);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.UPDATE_MESSAGE, null, timeOut);
        }

        internal Task<CashewResponse> MarkMessageAsReadAsync(string messageServerId, int timeOut)
        {
            JObject updateObj = new JObject();

            JObject dataObj = new JObject();

            updateObj.Add("read", "1");
            JObject statusObj = new JObject();

            statusObj.Add("status", updateObj);
            dataObj.Add(messageServerId.ToString(), statusObj);


            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_UPDATE_STATUS);
            cmd.Add("data", dataObj);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.UPDATE_MESSAGE, null, timeOut);
        }

        internal Task<CashewResponse> ChangeGroupOwner(string groupId, string newOwner, int timeOut)
        {
            JObject data = new JObject();
            data.Add("group_id", groupId);
            data.Add("new_owner", newOwner);


            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_CHANGE_OWNER);
            cmd.Add("data", data);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.CHANGE_GROUP_OWNER, null, timeOut);
        }

        internal Task<CashewResponse> EditGroupName(string groupId, string groupName, int timeOut)
        {
            JObject data = new JObject();
            data.Add("group_id", groupId);
            data.Add("name", groupName);


            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_EDIT_NAME);
            cmd.Add("data", data);

            string token = ConstructToken("header-");
            var json = JsonConvert.SerializeObject(cmd);
            return CallSecuredAsync(token, json, MessageRequestType.EDIT_GROUP_NAME, null, timeOut);
        }

        private string EncryptMessage(string body, string from, string to, bool isGroupMessage)
        {
            IDataCrypter crypter = Crypto.GetDataCrypter();

            if (isGroupMessage)
            {
                byte[] encryptedBytes = crypter.EncryptGroupData(Encoding.UTF8.GetBytes(body), from, to);
                return Encoding.UTF8.GetString(encryptedBytes, 0, encryptedBytes.Length);
            }
            else
            {
                byte[] encryptedBytes = crypter.EncryptData(Encoding.UTF8.GetBytes(body), from, to);
                return Encoding.UTF8.GetString(encryptedBytes, 0, encryptedBytes.Length);
            }
        }

        internal Task<CashewResponse>[] SendFriendRequestsAsync(CNFriendSuggestion outgoingContact, int timeOut)
        {
            if (outgoingContact == null)
            {
                throw new ArgumentException("Contact for friend request can not be null.");
            }


            JObject data = new JObject();
            JObject header = new JObject();
            header.Add("id", "new");

            JObject clientParams = new JObject();

            if (!string.IsNullOrEmpty(outgoingContact.Email))
            {
                clientParams.Add("email", outgoingContact.Email);
            }

            if (!string.IsNullOrEmpty(outgoingContact.Phone))
            {
                clientParams.Add("phone", outgoingContact.Phone);
            }

            header.Add("client_params", clientParams);

            header.Add("from", _cashewAccount.UserName);
            header.Add("type", 3);

            JObject controlParams = new JObject();
            controlParams.Add("status", 0);
            header.Add("control_parameters", controlParams);

            header.Add("comments", _lomentAccount.UserName + " wants to connect");

            data.Add("header", header);
            JObject cmd = new JObject();
            cmd.Add("cmd", CMD_MESSAGE);
            cmd.Add("data", data);


            List<Task<CashewResponse>> ret = new List<Task<CashewResponse>>();
            int cashewIdCounter = outgoingContact.CashewAccountIds.Count;

            JArray to = new JArray();
            to.Add("");

            foreach (var chasewId in outgoingContact.CashewAccountIds)
            {
                to = new JArray();
                to.Add(chasewId);

                header.Remove("to");
                header.Add("to", to);

                string token = ConstructToken("friend_request" + cashewIdCounter);
                cashewIdCounter--;

                if (cashewIdCounter > 0)
                {
                    ret.Add(CallSecuredAsync(token, JsonConvert.SerializeObject(cmd), MessageRequestType.SEND_FRIEND_REQUEST, null, timeOut));
                }
                else
                {
                    ret.Add(CallSecuredAsync(token, JsonConvert.SerializeObject(cmd), MessageRequestType.SEND_FRIEND_REQUEST_FINAL, null, timeOut));
                }
            }

            return ret.ToArray();

        }

        internal Task<CashewResponse> SyncMessagesAsync(DateTime? startDate, int timeOut)
        {
            JObject data = new JObject();
            data.Add("folder", "all");
            data.Add("start_msg_time", startDate.HasValue ? startDate.Value.ToString("yyyy-MM-dd HH:mm:ss") : null);
            data.Add("start_msg_id", null);
            data.Add("end_msg_id", null);

            JObject sync = new JObject();
            sync.Add("data", data);
            sync.Add("cmd", CMD_SYNC);
            string token = ConstructToken("Sync@");


            return CallSecuredAsync(token, JsonConvert.SerializeObject(sync), MessageRequestType.SYNC, null, timeOut);
        }


        internal Task<CashewResponse> AcceptFriendRequest(int friendRequestMessageId, int timeOut)
        {
            return ReplyToFriendRequest(friendRequestMessageId, 1, timeOut);
        }

        internal Task<CashewResponse> RejectFriendRequest(int friendRequestMessageId, int timeOut)
        {
            return ReplyToFriendRequest(friendRequestMessageId, 2, timeOut);
        }

        private Task<CashewResponse> ReplyToFriendRequest(int friendRequestMessageId, int acceptReject, int timeOut)
        {
            JObject statusObj = new JObject();
            statusObj.Add("status", acceptReject);

            JObject dataObj = new JObject();
            dataObj.Add(friendRequestMessageId.ToString(), statusObj);

            JObject responseObj = new JObject();

            responseObj.Add("data", dataObj);
            responseObj.Add("cmd", CMD_UPDATE_STATUS);

            string token = ConstructToken("friend_request_response");


            return CallSecuredAsync(token, JsonConvert.SerializeObject(responseObj), MessageRequestType.RESPOND_TO_FRIEND_REQUEST, friendRequestMessageId, timeOut);
        }

        public Task<CashewResponse<CNGroup>> RequestGroupDetails(string groupId, int timeOut)
        {
            JObject dataObj = new JObject();
            dataObj.Add("group_id", groupId);

            JObject responseObj = new JObject();

            responseObj.Add("data", dataObj);
            responseObj.Add("cmd", CMD_GROUP_CONF);

            string token = ConstructToken("group-");


            return CallSecuredAsync<CNGroup>(token, JsonConvert.SerializeObject(responseObj), MessageRequestType.GROUP_CONF, null, timeOut);
        }

        public Task<CashewResponse> BindAttachmentAsync(string messageId, CNAttachmentData attachment, int timeOut)
        {
            JObject dataObj = new JObject();
            dataObj.Add("msg_id", messageId);
            dataObj.Add("attachment_name", attachment.Name);
            dataObj.Add("uploaded_file_name", attachment.ServerName);
            dataObj.Add("size", attachment.Data.Length);
            dataObj.Add("padding", attachment.Padding);

            JObject responseObj = new JObject();

            responseObj.Add("data", dataObj);
            responseObj.Add("cmd", CMD_ATTACHMENT);

            string token = ConstructToken("attach");

            return CallSecuredAsync(token, JsonConvert.SerializeObject(responseObj), MessageRequestType.BIND_ATTACHMENT, null, timeOut);
        }

        public Task<CashewResponse> DownloadAttachmentAsync(CNAttachmentDownloadInfo attachmentInfo, Stream outStream, int timeOut)
        {
            JObject dataObj = new JObject();
            dataObj.Add("msg_id", attachmentInfo.MessageServerId);
            dataObj.Add("attachment_name", attachmentInfo.Name);
            dataObj.Add("size", attachmentInfo.Size);
            dataObj.Add("padding", attachmentInfo.Padding);

            JObject responseObj = new JObject();

            responseObj.Add("data", dataObj);
            responseObj.Add("cmd", CMD_DOWNLOAD_ATTACHMENT);

            string token = ConstructToken("download");

            return CallSecuredAsync(token, JsonConvert.SerializeObject(responseObj), MessageRequestType.DOWNLOAD_ATTACHMENT_AMQP, new Tuple<CNAttachmentDownloadInfo, Stream>(attachmentInfo, outStream), timeOut);
        }

        private async Task<CashewResponse> CallSecuredAsync(string token, string messageString, MessageRequestType action, object state, int timeOut)
        {
            if (!_isAuthenticated && action != MessageRequestType.AUTH)
            {
                var response = await AuthenticateUser(timeOut);
                if (response.IsSuccess == false)
                {
                    throw new InvalidOperationException("User is not authenticated.");
                }
                else
                {
                    _isAuthenticated = true;
                }
            }

            var request = new MessageRequest(action, timeOut);
            request.State = state;
            _responseHandler.AddMessageRequest(token, request);
            _requestQueue.Send(token, _responseQueue.Name, messageString);

            return await request.Task;
        }

        private async Task<CashewResponse<T>> CallSecuredAsync<T>(string token, string messageString, MessageRequestType action, object state, int timeOut)
        {
            if (!_isAuthenticated && action != MessageRequestType.AUTH)
            {
                var response = await AuthenticateUser(timeOut);
                if (response.IsSuccess == false)
                {
                    throw new InvalidOperationException("User is not authenticated.");
                }
                else
                {
                    _isAuthenticated = true;
                }
            }

            var request = new MessageRequest<T>(action, timeOut);
            request.State = state;
            _responseHandler.AddMessageRequest<T>(token, request);
            _requestQueue.Send(token, _responseQueue.Name, messageString);
            return await request.Task;
        }

        private string GetAuthKey()
        {
            return NetworkHelper.GetAuthKey(_lomentAccount, _cashewAccount);
        }

        private string ConstructToken(string header)
        {
            string token = header;
            if (_cashewAccount != null)
            {
                token += "-" + _cashewAccount.UserName;
            }
            token += "-" + DateTime.UtcNow.JavaTimeMillis() + "-" + ++fakeToken;

            return token;
        }

    }
}
