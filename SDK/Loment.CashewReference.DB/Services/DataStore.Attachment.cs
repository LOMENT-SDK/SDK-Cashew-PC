
using System.Linq;
using System.Threading.Tasks;

using Loment.CashewReference.DB.Model.Dao;
using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Model.Dao.Tables;
using System.Collections.Generic;
using System;

namespace Loment.CashewReference.DB.Services
{
    /// <summary>
    /// Partial class implementation for DataStore.Attachment
    /// </summary>
    public partial class DataStore
    {

        private enum AttachmentsColumns
        {
            Pk,
            CashewAccountUserId,
            MessageId,
            Name,
            FileName,
            Padding
        }


        /// <summary>
        /// Retrieve a count of the number of Contacts in the ContactactsTble DB table
        /// </summary>
        /// <returns></returns>
        public async Task<int> AttachmentsCountAsync()
        {
            return await AsyncConnection.ExecuteScalarAsync<int>("SELECT COUNT(*) FROM " + Tables.AttachmentsTable.ToString());
        }



        /// <summary>
        /// Return an List of Contacts retrieved from the DB
        /// Returned in chronological order from newest to oldest
        /// </summary>
        /// <returns></returns>
        public async Task<List<DbAttachment>> AttachmentsGetForMessageAsync(DbMessage message)
        {
            /* Note: MessageId is the ServerId of the message on the Cashew Server */


            List<DbAttachment> attachmentsTable = (await AsyncConnection.Table<AttachmentsTable>().Where(x => x.MessageId == message.ServerId).ToListAsync()).Select(x => x.Map(message)).ToList();

            return attachmentsTable;

        }

        /// <summary>
        /// Create a new DbAttachment
        /// Returns the new instance
        /// </summary>
        /// <param name="dbAttachment">The DbAttachment to create</param>
        public async Task<DbAttachment> AttachmentCreateAsync(DbAttachment dbAttachment)
        {
            var mappedAttachment = dbAttachment.Map();

            int pK = await AsyncConnection.InsertAsync(mappedAttachment);

            dbAttachment.Pk = pK;

            return dbAttachment;
        }

        /// <summary>
        /// Update a DbAttachment's record
        /// Update uses the Pk property to locate the Contact based on
        /// its Primary Key value
        /// Returns the number of rows affected
        /// </summary>
        /// <param name="dbAttachment"></param>
        public async Task<int> AttachmentUpdateAsync(DbAttachment dbAttachment)
        {

            return await AsyncConnection.UpdateAsync(dbAttachment.Map());

        }

        /// <summary>
        /// Delete a DbAttachment from the local db
        /// Delete uses the Pk property to locate the Contact based on
        /// its Primary Key value
        /// </summary>
        /// <param name="dbAttachment"></param>
        public async Task<int> AttachmentDeleteAsync(DbAttachment dbAttachment)
        {

            int rowsAffected = await AsyncConnection.DeleteAsync(dbAttachment.Map());

            return rowsAffected;
        }

        /// <summary>
        /// Remove the Attachments for a the Messages in a CashewAccount
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public async Task<int> AttachmentsForCashewAccountDeleteAsync(string userId)
        {
            string sqlString =
                "DELETE FROM " +
                    Tables.AttachmentsTable.ToString() +
                " WHERE " +
                    CashewAccountsColumns.CashewAccountId.ToString() +
                " = ? ";

            int rowsAffected = await AsyncConnection.ExecuteAsync(sqlString, userId);
            return rowsAffected;
        }

        private async Task<int> AttachmentDeleteForMessageAsync(int serverId)
        {
            string sqlString =
              "DELETE FROM " +
                  Tables.AttachmentsTable.ToString() +
              " WHERE " +
                  AttachmentsColumns.MessageId.ToString() +
              " = ? ";

            int rowsAffected = await AsyncConnection.ExecuteAsync(sqlString, serverId);
            return rowsAffected;
        }

    }
}

