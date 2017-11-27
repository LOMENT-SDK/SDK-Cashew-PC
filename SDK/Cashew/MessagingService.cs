using Cashew.Internal.Network;
using Cashew.Model;
using Cashew.Model.NetworkResponses;
using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;

namespace Cashew
{
    /// <summary>
    /// A service providing complete functionality to interact with Loment Cashew messaging backend.
    /// </summary>
    public class MessagingService
    {
        private AmpqManager _ampqManger;

        /// <summary>
        /// The Loment account used to communicate. See ctor parameter.
        /// </summary>
        public CNLomentAccount LomentAccount { get; private set; }

        /// <summary>
        /// The Cashew account used to communicate. See ctor parameter.
        /// </summary>
        public CNCashewAccount CashewAccount { get; private set; }

        /// <summary>
        /// The ctor for the MessaginService. Set the Loment and Cashew account properties.
        /// </summary>
        public MessagingService(CNLomentAccount lomentAccount, CNCashewAccount cashewAccount)
        {
            LomentAccount = lomentAccount;
            CashewAccount = cashewAccount;
        }

        /// <summary>
        /// Authenticates the current Loment/Cashew account combination.
        /// </summary>
        /// <param name="timeout"></param>
        /// <returns>A response indicating the success of the operation. In the case of success carries also the authentication info object.</returns>
        public Task<CashewResponse<AuthenticationResponse>> AuthenticateAsync(int timeout = NetworkConstants.QueueDefaultTimeOut)
        {
            _ampqManger = new AmpqManager();
            var ret = _ampqManger.StartAsync(LomentAccount, CashewAccount, RaiseMessageReceived, HandleGroupInfoReceived, timeout);
            return ret;
        }

        /// <summary>
        /// Triggers a sync of messages for the current Cashew account.
        /// </summary>
        /// <param name="startDateToSync">A start date form where on messages should be synced. If null then a complete sync is executed.</param>
        /// <param name="timeout"></param>
        /// <returns>A response indicating the success of the operation.</returns>
        public Task<CashewResponse> SyncAsync(DateTime? startDateToSync, int timeout = NetworkConstants.QueueDefaultTimeOut)
        {
#pragma warning disable 4014
            return _ampqManger.SyncMessagesAsync(startDateToSync, timeout);
#pragma warning restore 4014
        }

        /// <summary>
        /// Sends a direct message
        /// </summary>
        /// <param name="message">The message to be send.</param>
        /// <param name="to">The Cashew Id of the recipient.</param>
        /// <param name="options">Options for the message.</param>
        /// <param name="attachments">Optional attachments.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation. In the case of success carries also the send message.</returns>
        public Task<CashewResponse<CNMessage>> SendMessageAsync(string message, string to, CNMessageOptions options, CNAttachmentData[] attachments = null, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.SendMessageAsync(message, to, options, attachments, false, timeOut);
        }

        /// <summary>
        /// Sends a message to a group conversation.
        /// </summary>
        /// <param name="message">The message to be send.</param>
        /// <param name="groupId">The Id of the group.</param>
        /// <param name="options">Options for the message.</param>
        /// <param name="attachments">Optional attachments.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation. In the case of success carries also the send message.</returns>
        public Task<CashewResponse<CNMessage>> SendGroupMessageAsync(string message, string groupId, CNMessageOptions options, CNAttachmentData[] attachments = null, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.SendMessageAsync(message, groupId, options, attachments, true, timeOut);
        }

        /// <summary>
        /// Marks a message as deleted on the server.
        /// </summary>
        /// <param name="messageServerId">The server Id of the message.</param>
        /// <param name="senderId">The Cashew Id of the sender of the message.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation.</returns>
        public Task<CashewResponse> MarkMessageAsDeletedAsync(int messageServerId, string senderId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.MarkMessageAsDeleted(messageServerId, senderId, timeOut);
        }

        /// <summary>
        /// Accepts a friend request.
        /// </summary>
        /// <param name="friendRequestMessageId">The message id of the friend request.</param>
        /// <seealso cref="CNFriendRequest"/>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse> AcceptFriendRequestAsync(int friendRequestMessageId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.AcceptFriendRequest(friendRequestMessageId, timeOut);
        }

        /// <summary>
        /// Rejects a friend request.
        /// </summary>
        /// <param name="friendRequestMessageId">The message id of the friend request.</param>
        /// <seealso cref="CNFriendRequest"/>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse> RejectFriendRequestAsync(int friendRequestMessageId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.RejectFriendRequest(friendRequestMessageId, timeOut);
        }

        /// <summary>
        /// Retrieves the group info for a group.
        /// </summary>
        /// <param name="groupId">The id of the group. Also the conversation id of a CNMessage.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse<CNGroup>> GetGroupDetailsAsync(string groupId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.RequestGroupDetails(groupId, timeOut);
        }

        /// <summary>
        /// Creates a new group.
        /// </summary>
        /// <param name="groupName">The name of the group.</param>
        /// <param name="members">A list of cashew ids.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation and the group Id in case of success.</returns>
        public Task<CashewResponse<CNGroup>> CreateGroupAsync(string groupName, List<string> members, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.CreateGroup(groupName, members, timeOut);

        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="groupId">The id of the group to change.</param>
        /// <param name="membersToAdd">A list of members to be added to the group</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation.</returns>
        public Task<CashewResponse> AddMembersToGroupAsync(string groupId, List<string> membersToAdd, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.AddMembersToGroup(groupId, membersToAdd, timeOut);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="groupId">The id of the group to change.</param>
        /// <param name="membersToRemove">A list of members to be removed from the group.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation.</returns>
        public Task<CashewResponse> RemoveMembersFromGroupAsync(string groupId, List<string> membersToRemove, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.RemoveMembersFromGroup(groupId, membersToRemove, timeOut);
        }

        /// <summary>
        /// Changes the owner of the group.
        /// </summary>
        /// <param name="groupId">The id of the group to change.</param>
        /// <param name="newOwner">The cashew id of the new owner.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation.</returns>
        public Task<CashewResponse> ChangeGroupOwnerAsync(string groupId, string newOwner, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.ChangeGroupOwner(groupId, newOwner, timeOut);
        }

        /// <summary>
        /// Changes the display name of a group.
        /// </summary>
        /// <param name="groupId">The id of the group to change.</param>
        /// <param name="newGroupName">The new name for the group.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns>A response indicating the success of the operation.</returns>
        public Task<CashewResponse> EditGroupNameAsync(string groupId, string newGroupName, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.EditGroupName(groupId, newGroupName, timeOut);
        }

        /// <summary>
        /// Leave a group 
        /// </summary>
        /// <param name="groupId">The id of the group to leave.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse> LeaveGroupAsync(string groupId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.LeaveGroup(groupId, timeOut);
        }

        /// <summary>
        /// Downloads an attachment.
        /// </summary>
        /// <param name="attachmentInfo">The meta data of the attachment to download.</param>
        /// <param name="outStream">A stream to save the attachment to.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse> DownloadAttachmentAsync(CNAttachmentDownloadInfo attachmentInfo, Stream outStream, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.DownloadAttachmentAsync(attachmentInfo, outStream, timeOut);
        }

        /// <summary>
        /// Marks a message as acknowledged.
        /// </summary>
        /// <param name="messageServerId">The server Id of the message.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse> MarkMessageAsAcknowledgedAsync(string messageServerId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.MarkMessageAsAcknowledgedAsync(messageServerId, timeOut);
        }

        /// <summary>
        /// Marks a message as read.
        /// </summary>
        /// <param name="messageServerId">The server Id of the message.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse> MarkMessageAsReadAsync(string messageServerId, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.MarkMessageAsReadAsync(messageServerId, timeOut);
        }

        /// <summary>
        /// Sends a friend request.
        /// </summary>
        /// <param name="contact">A contact that represents a LomentAccoutn and all associated Cashew accounts.</param>
        /// <param name="timeOut">An optional parameter to wait for a response.</param>
        /// <returns></returns>
        public Task<CashewResponse>[] SendFriendRequestAsync(CNFriendSuggestion contact, int timeOut = NetworkConstants.QueueDefaultTimeOut)
        {
            return _ampqManger.SendFriendRequestsAsync(contact, timeOut);
        }

        /// <summary>
        /// Raises the MessageReceived event when a new message arrives.
        /// </summary>
        /// <param name="message">The message.</param>
        private void RaiseMessageReceived(CNMessageBase message)
        {
            var safeCopy = MessageReceived;
            if (safeCopy != null)
            {
                safeCopy(this, message);
            }
        }

        /// <summary>
        /// Raises the Group info event when new information about a group are populated.
        /// </summary>
        /// <param name="group">The Group.</param>
        private void HandleGroupInfoReceived(CNGroup group)
        {
            var safeCopy = GroupInfoReceived;
            if (safeCopy != null)
            {
                safeCopy(this, group);
            }
        }

        /// <summary>
        /// An event on arrival of an new message, message update or friend request.
        /// </summary>
        public event EventHandler<CNMessageBase> MessageReceived;

        /// <summary>
        /// An event on arrival of new group information.
        /// </summary>
        public event EventHandler<CNGroup> GroupInfoReceived;
    }
}
