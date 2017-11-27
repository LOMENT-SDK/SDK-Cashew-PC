using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Model.Dao;
using Loment.CashewReference.DB.Model.Dao.Tables;

namespace Loment.CashewReference.DB.Services
{
    /// <summary>
    /// Partial Class implementation of DataStore.Message
    /// </summary>
    public partial class DataStore
    {

        private enum MessagesColumns
        {
            ServerId,
            CashewAccountId,
            ContactId,
            SenderId,
            ConversationId,
            Status,
            Priority,
            IsForwardRestricted,
            RequestsReadAcknowledgement,
            IsReadAcknowledged,
            ExpiryMinutes,
            TimeStamp,
            IsRead,
            SendTime,
            Content,
            EncryptedContent,
            Direction,
            Subject,
            IsPartOfGroupConversation
        }


        /// <summary>
        /// Retrieve a count of the number of Contacts in the ContactactsTble DB table
        /// </summary>
        /// <returns></returns>
        public async Task<int> MessagesCountAllAsync()
        {
            return await AsyncConnection.ExecuteScalarAsync<int>("select count(*) from " + Tables.MessagesTable.ToString());
        }

        /// <summary>
        /// Return an ObservableCollection of Contacts retrieved from the DB
        /// Returned in chronological order from newest to oldest
        /// </summary>
        /// <returns></returns>
        public async Task<List<DbMessage>> MessagesGetAllAsync()
        {

            List<DbMessage> messagesTable = new List<DbMessage>(
                    (await AsyncConnection.Table<MessagesTable>().ToListAsync()).OrderByDescending(x => x.SendTime.ToUniversalTime())
                        .Select(x => x.Map()));
            return messagesTable;

        }

        /// <summary>
        /// Create a new DbMessage
        /// Returns the new instance
        /// </summary>
        /// <param name="dbMessage">The DbMessage to create</param>
        public async Task<DbMessage> MessageCreateAsync(DbMessage dbMessage)
        {
            var mappedMessage = dbMessage.Map();

            await AsyncConnection.InsertAsync(mappedMessage);

            if (dbMessage.Attachments.Any())
                await AsyncConnection.InsertAllAsync(dbMessage.Attachments.Select(x => x.Map()));

            return dbMessage;
        }

        /// <summary>
        /// Update a DbMessage's record
        /// Update uses the Pk property to locate the Contact based on
        /// its Primary Key value
        /// Returns the number of rows affected
        /// </summary>
        /// <param name="dbMessage"></param>
        public async Task<int> MessageUpdateAsync(DbMessage dbMessage)
        {
            var ret = await AsyncConnection.UpdateAsync(dbMessage.Map());


            if (dbMessage.Attachments.Any())
            {
                //refresh attachments
                await AttachmentDeleteForMessageAsync(dbMessage.ServerId);
                await AsyncConnection.InsertAllAsync(dbMessage.Attachments.Select(x => x.Map()));
            }

            return ret;
        }



        /// <summary>
        /// Delete a DbMessage from the local db
        /// Delete uses the Pk property to locate the Contact based on
        /// its Primary Key value
        /// </summary>
        /// <param name="dbMessage"></param>
        public async Task<int> MessageDeleteAsync(DbMessage dbMessage)
        {

            int rowsAffected = await AsyncConnection.DeleteAsync(dbMessage.Map());

            return rowsAffected;
        }

        /// <summary>
        /// Deletes CashewMessages for a CashewAccount and cascades deletion of Conversations and Attachments
        /// Note: Does not delete the attachment files - only the database rows
        /// </summary>
        /// <param name="cashewAccountId"></param>
        /// <returns></returns>
        //public async Task<int> DeleteMessages(long cashewAccountPK)
        public async Task<int> MessagesDeleteCascadingAsync(string cashewAccountId)
        {
            var messages = await AsyncConnection.Table<MessagesTable>().Where(x => x.CashewAccountId == cashewAccountId).ToListAsync();

            /* First delete the Attachment rows */
            foreach (var item in messages)
            {
                await AttachmentDeleteForMessageAsync(item.ServerId);
            }

            /* Now deleted the Conversations */
            int conversationsDeleted = await ConversationsForCashewAccountDeleteAsync(cashewAccountId);

            /* Now delete the Messages */
            string sqlString =
                "DELETE FROM " +
                    Tables.MessagesTable.ToString() +
                " WHERE " +
                    CashewAccountsColumns.CashewAccountId.ToString() +
                " = ? ";

            int rowsAffected = await AsyncConnection.ExecuteAsync(sqlString, cashewAccountId);

            return rowsAffected;
        }

        private async Task MessagesDeleteByConversationCascadingAsync(string chasewAccointId, string conversationId)
        {
            var messages = await MessagesGetListForConversationAsync(chasewAccointId, conversationId);
            foreach (var item in messages)
            {
                await AttachmentDeleteForMessageAsync(item.ServerId);
            }

            string sqlString =
               "DELETE FROM " +
                   Tables.MessagesTable.ToString() +
               " WHERE " +
                   CashewAccountsColumns.ConversationId.ToString() +
               " = ? ";

            int rowsAffected = await AsyncConnection.ExecuteAsync(sqlString, conversationId);
        }

        public async Task<DbMessage> MessageGetByServerIDAsync(int serverId)
        {
            string SQLString =
                "SELECT * FROM " +
                    Tables.MessagesTable.ToString() +
                " WHERE " +
                    MessagesColumns.ServerId.ToString() +
                " = ? ";

            List<MessagesTable> messagesTable = await AsyncConnection.QueryAsync<MessagesTable>(SQLString, serverId);
            MessagesTable messageTableRow = messagesTable.FirstOrDefault();

            if (messageTableRow == null)
            {
                return null;
            }

            var dbmessage = messageTableRow.Map();
            dbmessage.Attachments = await AttachmentsGetForMessageAsync(dbmessage);
            dbmessage.Conversation = await ConversationsGetByIdAsync(dbmessage.CashewAccountId, dbmessage.ConversationId);

            return dbmessage;
        }


        /// <summary>
        /// Retrieve a List of Messages for a CashewAccount
        /// </summary>
        /// <param name="cashewAccountID"></param>
        /// <returns></returns>
        public async Task<List<DbMessage>> MessagesGetListForCashewAccountAsync(string cashewAccountID)
        {
            var dbMessages = (await AsyncConnection.Table<MessagesTable>().Where(x => x.CashewAccountId == cashewAccountID).ToListAsync()).Select(x => x.Map()).ToList();

            foreach (var item in dbMessages)
            {
                item.Attachments = await AttachmentsGetForMessageAsync(item);
                item.Conversation = await ConversationsGetByIdAsync(item.CashewAccountId, item.ConversationId);
                item.Sender = await ContactGetBySenderIdAsync(item.SenderId);
            }
            return dbMessages;

        }

        public async Task<List<DbMessage>> MessagesGetListForConversationAsync(string cashewAccountID, string conversationId)
        {
            var messages = await AsyncConnection.Table<MessagesTable>().Where(x => x.CashewAccountId == cashewAccountID && x.ConversationId == conversationId).ToListAsync();
            var conversation = await ConversationsGetByIdAsync(cashewAccountID, conversationId);
            var dbMessages = messages.Select(x => x.Map(conversation)).ToList();
            foreach (var item in dbMessages)
            {
                item.Attachments = await AttachmentsGetForMessageAsync(item);
                item.Sender = await ContactGetBySenderIdAsync(item.SenderId);
            }
            return dbMessages;
        }
    }
}
