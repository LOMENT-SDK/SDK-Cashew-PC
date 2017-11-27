namespace Cashew.Internal.Network
{
    internal enum MessageRequestType
    {
        AUTH,
        SEND_MESSAGE,
        SEND_GROUP_MESSAGE,
        UPDATE_MESSAGE,
        DELETE_MESSAGE,
        SEND_MESSAGE_SCHEDULED,
        CREATE_GROUP,
        SYNC,
        ADD_MEMBERS_TO_GROUP,
        REMOVE_MEMBERS_FROM_GROUP,
        EDIT_GROUP_NAME,
        GROUP_CONF,
        CHANGE_GROUP_OWNER,
        BIND_ATTACHMENT,
        DOWNLOAD_ATTACHMENT_AMQP,
        SEND_FRIEND_REQUEST,
        SEND_FRIEND_REQUEST_FINAL,
        RESPOND_TO_FRIEND_REQUEST
    }
}
