using Cashew.Internal.Encryption;
using Cashew.Internal.Network.MessageDto;
using Cashew.Model;
using Cashew.Services;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Text;

namespace Cashew.Internal.Network
{
    internal class MessageQueue : AutoRecoveryQueueBase
    {
        private const string FULL_MESSAGE = "MSG_FULL";
        private const string HEADER_MESSAGE = "MSG_HEADER";
        private const string HEADER = "header";
        private const string TYPE = "type";
        private const string GROUP_CONF_MESSAGE = "GRP_CONF";

        private CNCashewAccount _cashewAccount;

        public CNLomentAccount LomentAccount { get; private set; }

        private Action<CNMessageBase> ReportMessageReceived;

        private Action<CNGroup> ReportGroup;

        internal MessageQueue(Action<CNMessageBase> reportMessageReceived, Action<CNGroup> reportGroup)
        {
            ReportMessageReceived = reportMessageReceived;
            ReportGroup = reportGroup;
        }

        internal void SetAccounts(CNLomentAccount lomentAccount, CNCashewAccount cashewAccount)
        {
            LomentAccount = lomentAccount;
            _cashewAccount = cashewAccount;
        }

        protected override IRabbitMQService CreateRabbitService()
        {
            return Sdk.RabbitMQService.CreateService(NetworkConstants.AMQP_MESSAGE_QUEUE_HOSTNAME, NetworkConstants.AMQP_MESSAGE_QUEUE_USERNAME, NetworkConstants.AMQP_MESSAGE_QUEUE_PASSWORD, NetworkConstants.AMQP_MESSAGE_QUEUE_V_HOST, 20);
        }

        protected override void OnMessageReceived(MessageReceivedEventArgs e)
        {
            ProcessMessageAsync(e.Body);
        }

        private void ProcessMessageAsync(string body)
        {
            Debug.WriteLine(body);

            CNMessageBase message = null;
            CNGroup group = null;
            switch (DetermineMessageType(body))
            {
                case MessageDtoType.Full:
                    message = ParseFullMessage(body);
                    DecryptMessage((CNMessage)message);
                    break;
                case MessageDtoType.Header:
                    message = ParseHeader(body);
                    break;
                case MessageDtoType.FriendRequest:
                    message = ParseFriendRequest(body);
                    break;
                case MessageDtoType.FriendRequestResponse:
                    message = ParseFriendRequestResponse(body);
                    break;
                case MessageDtoType.GroupConfiguration:
                    group = ParseGroupConfiguration(body);
                    break;
                default:
                    break;
            }

            if (message != null)
            {
                message.CashewAccount = _cashewAccount;
                ReportMessageReceived(message);
            }

            if (group != null)
            {
                ReportGroup(group);
            }
        }

        private CNMessageBase ParseHeader(string body)
        {
            var dto = JsonConvert.DeserializeObject<HeaderMessageDto>(body);

            CNMessage message = ParseFromHeader(dto.MSG_HEADER);

            return message;
        }

        private CNMessage ParseFullMessage(string body)
        {
            var dto = JsonConvert.DeserializeObject<FullMessageDto>(body);

            CNMessage message = ParseFromHeader(dto.MSG_FULL.header);
            message.Content = dto.MSG_FULL.body;
            if (dto.MSG_FULL.header.attachments.data != null)
                message.Attachments = dto.MSG_FULL.header.attachments.data.Select(x => new CNAttachment(message) { Name = x.name, Size = long.Parse(x.size), Padding = int.Parse(x.padding) }).ToList();
            else
                message.Attachments = new List<CNAttachment>();

            return message;
        }

        private CNMessage ParseFromHeader(MessageHeaderDto dto)
        {
            CNMessage message = new CNMessage();

            message.ServerId = int.Parse(dto.id);
            message.IsForwardRestricted = dto.control_params.restricted == "3";
            message.RequestsReadAcknowledgement = dto.control_params.ack == "1";
            message.ExpiryMinutes = int.Parse(dto.control_params.expiry);
            message.Priority = (CNMessagePriority)int.Parse(dto.control_params.priority);
            message.SenderId = dto.from;
            message.Direction = string.Compare(_cashewAccount.UserName, message.SenderId, StringComparison.OrdinalIgnoreCase) == 0 ? CNMessageDirection.Outgoing : CNMessageDirection.Incoming;
            message.SendTime = DateTime.Parse(dto.creation_timestamp, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal | DateTimeStyles.AdjustToUniversal).Add(NetworkConstants.SERVER_TIME_DRIFT);
            message.TimeStamp = DateTime.Parse(dto.creation_timestamp, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal | DateTimeStyles.AdjustToUniversal).Add(NetworkConstants.SERVER_TIME_DRIFT);
            message.ConversationId = !string.IsNullOrEmpty(dto.group_id) ? dto.group_id : GenerateUniqueConversationId(dto.from, dto.to);
            message.IsPartOfGroupConversation = !string.IsNullOrEmpty(dto.group_id);
            message.Recipients = dto.to.ToList();
            message.MarkedAsDeleted = dto.status.self_deleted == "1" || dto.status.recipient_deleted.Any(x => x != "0");
            message.IsRead = dto.status.read.Any(x => x == "1");
            message.IsReadAcknowledged = dto.status.ack.Any(x => x == "1");

            return message;
        }

        private CNGroup ParseGroupConfiguration(string body)
        {
            var dto = JsonConvert.DeserializeObject<GroupConfResponseDto>(body);

            CNGroup group = new CNGroup
            {
                ConverstaionId = dto.GRP_CONF.group_id,
                Created = DateTime.Parse(dto.GRP_CONF.creation_time, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal),
                LastUpdate = DateTime.Parse(dto.GRP_CONF.last_update_time, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal),
                Member = dto.GRP_CONF.members.ToList(),
                Name = dto.GRP_CONF.name,
                Owner = dto.GRP_CONF.owner
            };

            return group;
        }

        private void DecryptMessage(CNMessage message)
        {
            IDataCrypter crypter = Crypto.GetDataCrypter();

            byte[] decryptedBytes = new byte[0];
            try
            {
                if (message.IsPartOfGroupConversation)
                {
                    decryptedBytes = crypter.DecryptGroupData(Encoding.UTF8.GetBytes(message.Content), message.SenderId, message.ConversationId);
                    message.Content = Encoding.UTF8.GetString(decryptedBytes, 0, decryptedBytes.Length);
                }
                else
                {
                    decryptedBytes = crypter.DecryptData(Encoding.UTF8.GetBytes(message.Content), message.SenderId, message.Recipients.First());
                    message.Content = Encoding.UTF8.GetString(decryptedBytes, 0, decryptedBytes.Length);
                }

                if (message.Content.Contains("again"))
                {

                }
            }
            catch (Exception) { }
        }

        private CNFriendRequest ParseFriendRequest(string body)
        {
            var dto = JsonConvert.DeserializeObject<IncomingFriendRequestDto>(body);

            CNFriendRequest m = new CNFriendRequest();

            m.ServerId = int.Parse(dto.header.id);
            m.SenderId = dto.header.from;
            m.Direction = string.Compare(_cashewAccount.UserName, m.SenderId, StringComparison.OrdinalIgnoreCase) == 0 ? CNMessageDirection.Outgoing : CNMessageDirection.Incoming;
            m.SendTime = DateTime.Parse(dto.header.creation_timestamp, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal);
            m.TimeStamp = DateTime.Parse(dto.header.creation_timestamp, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal);
            m.ConversationId = GenerateUniqueConversationId(dto.header.from, dto.header.to);
            m.Subject = dto.header.subject.content;
            m.Recipients = dto.header.to.ToList();
            m.Status = (CNFriendStatus)dto.header.status;

            return m;
        }

        private CNFriendRequest ParseFriendRequestResponse(string body)
        {
            var dto = JsonConvert.DeserializeObject<FriendRequestResponseDto>(body);

            CNFriendRequest m = new CNFriendRequest();

            m.ServerId = int.Parse(dto.id);
            m.SenderId = dto.from;
            m.Direction = string.Compare(_cashewAccount.UserName, m.SenderId, StringComparison.OrdinalIgnoreCase) == 0 ? CNMessageDirection.Outgoing : CNMessageDirection.Incoming;
            m.SendTime = DateTime.Parse(dto.creation_timestamp, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal);
            m.TimeStamp = DateTime.Parse(dto.creation_timestamp, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal);
            m.ConversationId = GenerateUniqueConversationId(dto.from, dto.to);
            m.Subject = dto.subject.content;
            m.Recipients = dto.to.ToList();
            m.Status = (CNFriendStatus)dto.status;

            return m;
        }



        private string GenerateUniqueConversationId(string from, List<string> to)
        {
            List<string> fromTo = new List<string> { from, to.First() };
            string id = string.Join("/", fromTo.OrderBy(x => x));
            return id;
        }

        private MessageDtoType DetermineMessageType(string body)
        {
            JObject messageObject = (JObject)JsonConvert.DeserializeObject(body);
            var properties = messageObject.Descendants().Where(x => x is JProperty).Cast<JProperty>().ToList();
            var type = properties.SingleOrDefault(x => x.Name == "type");

            if (properties.Any(x => x.Name == FULL_MESSAGE))
            {
                return MessageDtoType.Full;
            }
            else if (properties.Any(x => x.Name == HEADER_MESSAGE))
            {
                return MessageDtoType.Header;
            }
            else if (properties.Any(x => x.Name == HEADER) && type != null && type.Value.ToString() == "3")
            {
                return MessageDtoType.FriendRequest;
            }
            else if (properties.Any(x => x.Name == GROUP_CONF_MESSAGE))
            {
                return MessageDtoType.GroupConfiguration;
            }
            else if (type != null)
            {         
                if (type.Value.ToString() == "3")
                {
                    return MessageDtoType.FriendRequestResponse;
                }
                else
                {
                    return MessageDtoType.Unknown;
                }
            }
            else
            {
                return MessageDtoType.Unknown;
            }
        }
    }
}
