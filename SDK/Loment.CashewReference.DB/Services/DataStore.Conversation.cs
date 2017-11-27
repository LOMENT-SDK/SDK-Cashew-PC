using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Model.Dao.Tables;
using Loment.CashewReference.DB.Model.Dao;
using System;

namespace Loment.CashewReference.DB.Services
{
    /// <summary>
    /// Partial class implementation for DataStore.Conversation
    /// </summary>
    public partial class DataStore
    {

        private enum ConversationsColumns
        {
            CashewAccountFk,
            UniqueName,
            GroupId,
            GroupName,
            OwnerId
        }

        /// <summary>
        /// Retrieve a count of the number of Conversations in the ConversationactsTble DB table
        /// </summary>
        /// <returns></returns>
        public async Task<int> ConversationsCountAllAsync()
        {
            return await AsyncConnection.ExecuteScalarAsync<int>("SELECT COUNT(*) FROM " + Tables.ConversationsTable.ToString());
        }

        /// <summary>
        /// Return an ObservableCollection of Conversations retrieved from the DB
        /// </summary>
        /// <returns></returns>
        public async Task<List<DbConversation>> ConversationsGetByCashewAccountAsync(string cashewAccountId)
        {

            List<DbConversation> contactsTable = new List<DbConversation>(
                    (await AsyncConnection.Table<ConversationsTable>().Where(x => x.CashewAccountId == cashewAccountId).ToListAsync()).OrderBy(x => x.Name)
                        .Select(x => x.Map()));

            foreach (var item in contactsTable.Where(x => x.IsGroupConversation))
            {
                var members = await AsyncConnection.Table<GroupMemberTable>().Where(x => x.ConversationId == item.ConversationId).ToListAsync();
                item.Members = members.Select(x => x.ContactSenderId).ToList();
            }

            return contactsTable;

        }

        public async Task<DbConversation> ConversationsGetByIdAsync(string cashewAccountId, string conversationId)
        {
            return (await AsyncConnection.Table<ConversationsTable>().Where(x => x.CashewAccountId == cashewAccountId && x.ConversationId == conversationId).FirstOrDefaultAsync())?.Map();
        }

        /// <summary>
        /// Create a new DbConversation
        /// Returns the new instance
        /// </summary>
        /// <param name="dbConversation">The DbConversation to create</param>
        public async Task<DbConversation> ConversationCreateAsync(DbConversation dbConversation)
        {
            var mappedConversation = dbConversation.Map();

            await AsyncConnection.InsertOrIgnoreAsync(mappedConversation);

            if (dbConversation.Members.Any())
            {
                await AsyncConnection.InsertAllAsync(dbConversation.MapToMembers());
            }

            return dbConversation;
        }

        /// <summary>
        /// Update a DbConversation's record
        /// Update uses the Pk property to locate the Conversation based on
        /// its Primary Key value
        /// Returns the number of rows affected
        /// </summary>
        /// <param name="dbConversation"></param>
        public async Task<int> ConversationUpdateAsync(DbConversation dbConversation)
        {
            var conversationTable = await AsyncConnection.FindAsync<ConversationsTable>(x => x.CashewAccountId == dbConversation.CashewAccountId && x.ConversationId == dbConversation.ConversationId);
            conversationTable.Name = dbConversation.Name;
            conversationTable.OwnerId = dbConversation.OwnerId;

            var ret = await AsyncConnection.UpdateAsync(conversationTable);

            var members = await AsyncConnection.Table<GroupMemberTable>().Where(x => x.ConversationId == dbConversation.ConversationId).ToListAsync();
            foreach (var item in members)
            {
                if (dbConversation.Members.Any(x => x == item.ContactSenderId) == false)
                    await AsyncConnection.DeleteAsync(item);
            }

            foreach (var item in dbConversation.MapToMembers())
            {
                if (members.Any(x => x.ContactSenderId == item.ContactSenderId) == false)
                    await AsyncConnection.InsertAsync(item);
            }

            return ret;
        }

        /// <summary>
        /// Delete a DbConversation from the local db
        /// Delete uses the Pk property to locate the Conversation based on
        /// its Primary Key value
        /// </summary>
        /// <param name="dbConversation"></param>
        public async Task<int> ConversationDeleteAsync(DbConversation dbConversation)
        {

            var conversationTable = await AsyncConnection.FindAsync<ConversationsTable>(x => x.CashewAccountId == dbConversation.CashewAccountId && x.ConversationId == dbConversation.ConversationId);

            int rowsAffected = await AsyncConnection.DeleteAsync(conversationTable);

            if (dbConversation.Members.Any())
            {
                var members = await AsyncConnection.Table<GroupMemberTable>().Where(x => x.ConversationId == dbConversation.ConversationId).ToListAsync();
                foreach (var item in members)
                {
                    await AsyncConnection.DeleteAsync(item);
                }
            }

            await MessagesDeleteByConversationCascadingAsync(dbConversation.CashewAccountId, dbConversation.ConversationId);



            return rowsAffected;
        }


    }
}