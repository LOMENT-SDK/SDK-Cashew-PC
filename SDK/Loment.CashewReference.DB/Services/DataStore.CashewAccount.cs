
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
    /// Partial class implementation for DataStore.CashewAccount
    /// </summary>
    public partial class DataStore
    {
        private DbCashewAccount _newCashewAccount;


        private enum CashewAccountsColumns
        {
            CashewAccountId,
            ConversationId
        }


        /// <summary>
        /// Retrieve a count of the number of CashewAccounts in the CashewAccountsTable
        /// </summary>
        /// <returns></returns>
        public async Task<int> CashewAccountsCountAsync()
        {
            return await AsyncConnection.ExecuteScalarAsync<int>("SELECT COUNT(*) FROM " + Tables.ContactsTable.ToString() + " where IsCashewAccount = 1");
        }

        /// <summary>
        /// Return an ObservableCollection of CashewAccounts retrieved from the DB
        /// </summary>
        /// <returns></returns>
        public async Task<List<DbCashewAccount>> CashewAccountsGetAllListAsync()
        {
            var lomentAccount = await LomentAccountGetAsync();

            List<DbCashewAccount> cashewAccountsTable = new List<DbCashewAccount>(
                    (await AsyncConnection.Table<ContactsTable>().ToListAsync()).Where(x=>x.IsCashewAccount).OrderBy(x => x.SenderId)
                        .Select(x => x.Map(lomentAccount)));
            return cashewAccountsTable;

        }

        /// <summary>
        /// Create a new DbCashewAccount
        /// Returns the new instance
        /// </summary>
        /// <param name="dbCashewAccount">The DbCashewAccount to create</param>
        public async Task<DbCashewAccount> CashewAccountCreateAsync(DbCashewAccount dbCashewAccount)
        {
            var mappedCashewAccount = dbCashewAccount.Map();

            await AsyncConnection.InsertAsync(mappedCashewAccount);

            //dbCashewAccount.Pk = mappedCashewAccount.Pk;

            return dbCashewAccount;
        }

        /// <summary>
        /// Update a DbCashewAccount's record
        /// Update uses the Pk property to locate the CashewAccount based on
        /// its Primary Key value
        /// Returns the number of rows affected
        /// </summary>
        /// <param name="dbCashewAccount"></param>
        public async Task<int> CashewAccountUpdateAsync(DbCashewAccount dbCashewAccount)
        {

            return await AsyncConnection.UpdateAsync(dbCashewAccount.Map());

        }

        /// <summary>
        /// Delete a DbCashewAccount from the local db
        /// Delete uses the Pk property to locate the CashewAccount based on
        /// its Primary Key value
        /// </summary>
        /// <param name="dbCashewAccount"></param>
        public async Task<int> CashewAccountDeleteAsync(DbCashewAccount dbCashewAccount)
        {

            //long cashewAccountPK = dbCashewAccount.Pk;

            int rowsAffectedMessages = await MessagesDeleteCascadingAsync(dbCashewAccount.UserName );

            int rowsAffectedCashewAccounts = await AsyncConnection.DeleteAsync(dbCashewAccount.Map());

            return rowsAffectedCashewAccounts;
        }


        /// <summary>
        /// Delete the Conversations related to Messages for a selected CashewAccount
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public async Task<int> ConversationsForCashewAccountDeleteAsync(string userId)
        {
            string sqlString =
                "DELETE FROM " +
                    Tables.ConversationsTable.ToString() +
                " WHERE " +
                    CashewAccountsColumns.CashewAccountId.ToString() +
                " = ? ";

            int rowsAffected = await AsyncConnection.ExecuteAsync( sqlString , userId);

            return rowsAffected;

        }


        /// <summary>
        /// Return a single instance of a CashewAccounts table row based on the primary key
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public async Task<DbCashewAccount> CashewAccountGetByUserIdAsync(string cashewAccountId)
        {
            ContactsTable cashewAccountsTable = await AsyncConnection.GetAsync<ContactsTable>(cashewAccountId);

            return cashewAccountsTable.Map(await LomentAccountGetAsync());
            
        }

        /// <summary>
        /// Remove all CashewAccounts from a table
        /// Note: Does not cascade delete Messages, Conversations and Attachments
        /// </summary>
        /// <returns></returns>
        public async Task<int> CasehwAccountsDeleteAllAsync()
        {
            var cashewAccounts = await AsyncConnection.Table<ContactsTable>().Where(x=>x.IsCashewAccount).ToListAsync();
            foreach (var item in cashewAccounts)
            {
                await AsyncConnection.DeleteAsync<ContactsTable>(item);
            }
 
            return cashewAccounts.Count;
        }
    }
}