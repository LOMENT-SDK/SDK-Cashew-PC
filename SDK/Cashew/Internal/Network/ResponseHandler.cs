using Cashew.Internal.Loment.Dto;
using Cashew.Internal.Network.MessageDto;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace Cashew.Internal.Network
{
    internal class ResponseHandler
    {
        private object _lock = new object();

        private Dictionary<string, MessageRequestBase> _responseDictionary;
        private MessageQueue _messageQueue;
        private CNLomentAccount _lomentAccount;
        private CNCashewAccount _cashewAccount;
        private AmpqManager _ampqManager;

        internal ResponseHandler(MessageQueue messageQueue, CNLomentAccount lomentAccount, CNCashewAccount cashewAccount, AmpqManager ampqManager)
        {
            _messageQueue = messageQueue;
            _lomentAccount = lomentAccount;
            _cashewAccount = cashewAccount;
            _ampqManager = ampqManager;

            _responseDictionary = new Dictionary<string, MessageRequestBase>();
        }

        internal void AddMessageRequest(string token, MessageRequest request)
        {
            lock (_lock)
            {
                _responseDictionary.Add(token, request);
            }
        }

        internal void AddMessageRequest<T>(string token, MessageRequest<T> request)
        {
            lock (_lock)
            {
                _responseDictionary.Add(token, request);
            }
        }

        internal void HandleResponse(string token, string body)
        {
            lock (_lock)
            {
                if (_responseDictionary.ContainsKey(token))
                {
                    MessageRequestBase request = _responseDictionary[token];
                    _responseDictionary.Remove(token);


                    switch (request.Type)
                    {
                        case MessageRequestType.AUTH:
                            HandleAuthResponse(request, body);
                            break;
                        case MessageRequestType.SEND_MESSAGE:
                        case MessageRequestType.SEND_GROUP_MESSAGE:
                            HandleSendMessageResponse(request, body);
                            break;
                        case MessageRequestType.UPDATE_MESSAGE:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.DELETE_MESSAGE:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.SEND_MESSAGE_SCHEDULED:
                            HandleSendMessageResponse(request, body);
                            break;
                        case MessageRequestType.CREATE_GROUP:
                            HandleCreateGroupResponse(request, body);
                            break;
                        case MessageRequestType.SYNC:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.ADD_MEMBERS_TO_GROUP:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.REMOVE_MEMBERS_FROM_GROUP:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.EDIT_GROUP_NAME:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.GROUP_CONF:
                            HandleGroupConfiguration(request, body);
                            break;
                        case MessageRequestType.CHANGE_GROUP_OWNER:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.BIND_ATTACHMENT:
                            HandleBindAttachmentResponse(request, body);
                            break;
                        case MessageRequestType.DOWNLOAD_ATTACHMENT_AMQP:
                            HandleDownloadAttachmentHttpResponse(request, body);
                            break;
                        case MessageRequestType.SEND_FRIEND_REQUEST:
                        case MessageRequestType.SEND_FRIEND_REQUEST_FINAL:
                            HandleGenericMessageResponse(request, body);
                            break;
                        case MessageRequestType.RESPOND_TO_FRIEND_REQUEST:
                            HandleRespondToFriendRequestResponse(request, body);
                            break;
                    }
                }
                else
                {
                    Debug.WriteLine("Unknown response. Token: {0}", token);
                }
            }
        }

        private void HandleCreateGroupResponse(MessageRequestBase request, string body)
        {
            var message = new { status = 0, comments = "", group_id = "" };

            message = JsonConvert.DeserializeAnonymousType(body, message);

            (request as MessageRequest<string>).SetResponse(new CashewResponse<string>(message.status, message.comments, message.status == 0, message.group_id));
        }

        private void HandleGenericMessageResponse(MessageRequestBase request, string body)
        {
            var message = new { status = 0, comments = "" };

            message = JsonConvert.DeserializeAnonymousType(body, message);

            (request as MessageRequest).SetResponse(new CashewResponse(message.status, message.comments, message.status == 0));
        }

        private void HandleRespondToFriendRequestResponse(MessageRequestBase request, string body)
        {
            JObject response = (JObject)JsonConvert.DeserializeObject(body);
            int msgId = (int)request.State;

            int status = (int)response.Descendants().Where(x => x is JProperty).Cast<JProperty>().Single(x => x.Path == string.Format("data.{0}.status", msgId)).Value;
            string comments = (string)response.Descendants().Where(x => x is JProperty).Cast<JProperty>().Single(x => x.Path == string.Format("data.{0}.comments", msgId)).Value;

            ((MessageRequest)request).SetResponse(new CashewResponse(status, comments, status == 0));
        }

        private async void HandleDownloadAttachmentHttpResponse(MessageRequestBase request, string body)
        {
            var message = new { status = 0, comments = "", file_name = "" };

            message = JsonConvert.DeserializeAnonymousType(body, message);
            var downloadAttachmentRequest = (MessageRequest)request;
            var state = request.State as Tuple<CNAttachmentDownloadInfo, Stream>;

            if (message.status == 0)
            {
                string attachmentFileName = message.file_name;
                AttachmentManager attachmentMananger = new AttachmentManager();

                try
                {
                    await attachmentMananger.DownloadAttachmentAsync(state.Item1, state.Item2, attachmentFileName, _lomentAccount, _cashewAccount);
                    downloadAttachmentRequest.SetResponse(new CashewResponse(message.status, message.comments, true));
                }
                catch (Exception ex)
                {
                    downloadAttachmentRequest.SetResponse(new CashewResponse(-1, "Attempt to download attachment failed with: " + ex.Message, false));
                }
            }
            else
            {
                downloadAttachmentRequest.SetResponse(new CashewResponse(message.status, message.comments, message.status == 0));
            }

        }

        private void HandleBindAttachmentResponse(MessageRequestBase request, string body)
        {
            var message = new { status = 0, comments = "" };

            message = JsonConvert.DeserializeAnonymousType(body, message);

            (request as MessageRequest).SetResponse(new CashewResponse(message.status, message.comments, message.status == 0));
        }



        private void HandleGroupConfiguration(MessageRequestBase request, string body)
        {
            var response = JsonConvert.DeserializeObject<GroupConfResponseDto>(body);

            CNGroup group = null;
            var sendMessageRequest = (MessageRequest<CNGroup>)request;
            if (response.status == 0)
            {
                group = new CNGroup
                {
                    ConverstaionId = response.GRP_CONF.group_id,
                    Created = DateTime.Parse(response.GRP_CONF.creation_time, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal | DateTimeStyles.AdjustToUniversal).Add(NetworkConstants.SERVER_TIME_DRIFT),
                    LastUpdate = DateTime.Parse(response.GRP_CONF.last_update_time, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal | DateTimeStyles.AdjustToUniversal).Add(NetworkConstants.SERVER_TIME_DRIFT),
                    Name = response.GRP_CONF.name,
                    Owner = response.GRP_CONF.owner,
                    Member = response.GRP_CONF.members.ToList()
                };
            }

            sendMessageRequest.SetResponse(new CashewResponse<CNGroup>(response.status, response.comments, response.status == 0, group));
        }

        private void HandleSyncResponse()
        {
            // We do nothing on purpose.
        }

        private async void HandleSendMessageResponse(MessageRequestBase request, string body)
        {
            var message = new { status = 0, queue = "", comments = "", msg_id = "" };

            message = JsonConvert.DeserializeAnonymousType(body, message);


            var sendMessageRequest = (MessageRequest<CNMessage>)request;

            CNMessage cnm = null;

            var dto = request.State as Tuple<SendMessageDto, CNAttachmentData[], string>;

            if (message.status == 0)
            {
                cnm = new CNMessage()
                {
                    ServerId = int.Parse(message.msg_id),
                    ConversationId = request.Type == MessageRequestType.SEND_GROUP_MESSAGE ? dto.Item3 : GenerateUniqueConversationId(dto.Item1.data.header.from, dto.Item1.data.header.to.To_1),
                    IsPartOfGroupConversation = request.Type == MessageRequestType.SEND_GROUP_MESSAGE,
                    Direction = CNMessageDirection.Outgoing,
                    Content = dto.Item1.data.body,
                    ExpiryMinutes = dto.Item1.data.header.control_params.expiry,
                    IsForwardRestricted = dto.Item1.data.header.control_params.restricted == 3,
                    IsRead = false,
                    IsReadAcknowledged = false,
                    Priority = (CNMessagePriority)dto.Item1.data.header.control_params.priority,
                    RequestsReadAcknowledgement = dto.Item1.data.header.control_params.ack == 1,
                    SenderId = dto.Item1.data.header.from,
                    Recipients = new List<string> { dto.Item1.data.header.to.To_1 },
                    Subject = dto.Item1.data.header.subject.content,
                    SendTime = DateTime.UtcNow,
                    TimeStamp = DateTime.UtcNow,
                    MarkedAsDeleted = false,
                    CashewAccount = _cashewAccount,
                    Attachments = new List<CNAttachment>()
                };
            }

            AttachmentManager attachmentMananger = new AttachmentManager();


            var uploadTasks = dto.Item2?.Select(x => attachmentMananger.UploadAttachmentAsync(x, cnm.ServerId.ToString(), cnm.SenderId, cnm.IsPartOfGroupConversation ? cnm.ConversationId : cnm.Recipients[0], cnm.IsPartOfGroupConversation)).ToArray();

            if (uploadTasks != null)
            {
                var uploadResults = await Task.WhenAll(uploadTasks);

                if (uploadResults.Any(x => x == false))
                {
                    sendMessageRequest.SetResponse(new CashewResponse<CNMessage>(-1, "One or more attempts to upload attachments failed", false, cnm));
                    return;
                }

                var bindTasks = dto.Item2.Select(x => _ampqManager.BindAttachmentAsync(cnm.ServerId.ToString(), x, NetworkConstants.QueueDefaultTimeOut));

                var bindResults = await Task.WhenAll(bindTasks);

                if (bindResults.Any(x => !x.IsSuccess))
                {
                    sendMessageRequest.SetResponse(new CashewResponse<CNMessage>(-1, "One or more attempts to bind attachments to messages failed", false, cnm));
                    return;
                }

                cnm.Attachments = dto.Item2.Select(x => new CNAttachment(cnm) { Name = x.Name, Padding = x.Padding, Size = x.Size }).ToList();
            }

            sendMessageRequest.SetResponse(new CashewResponse<CNMessage>(message.status, message.comments, message.status == 0, cnm));
        }

        private void HandleAuthResponse(MessageRequestBase request, string body)
        {
            var message = new { status = 0, queue = "", comments = "", sync_required = false };

            message = JsonConvert.DeserializeAnonymousType(body, message);

            int status = message.status;
            var respMeta = ResponseUtil.GetResponseMetaForCode(status);

            string queue = message.queue;
            if (message.status == 0)
            {
                if (!string.IsNullOrEmpty(queue))
                {
                    _messageQueue.SetAccounts(_lomentAccount, _cashewAccount);
                    _messageQueue.Open(queue);
                }
            }

            var authMessageRequest = (MessageRequest<AuthenticationResponse>)request;
            authMessageRequest.SetResponse(new CashewResponse<AuthenticationResponse>(message.status, message.comments, message.status == 0, new AuthenticationResponse { IsSyncRequired = message.sync_required }));
        }

        private string GenerateUniqueConversationId(string from, string to)
        {
            List<string> fromTo = new List<string> { from, to };
            string id = string.Join("/", fromTo.OrderBy(x => x));
            return id;
        }
    }
}
