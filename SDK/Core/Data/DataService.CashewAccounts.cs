using Cashew.Model;
using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core.Data
{
    /// <summary>
    /// Database access methods and business logic for CashewAccounts
    /// </summary>
    public partial class DataService
    {
        /// <summary>
        /// Create a new CashewAccount row in the database
        /// </summary>
        /// <param name="cashewAccount"></param>
        /// <returns></returns>
        internal async Task<DbCashewAccount> CashewAccountCreate(CNCashewAccount cashewAccount)
        {
            DbCashewAccount dbCashewAccountToCreate = new DbCashewAccount()
            {
                UserName = cashewAccount.UserName,
                CreateDateUtc = cashewAccount.CreateDateUtc,
                LastUpdateDateUtc = cashewAccount.LastUpdateDateUtc,
                Status = cashewAccount.Status,
                LomentAccount = await _dataStore.LomentAccountGetAsync()
            };

            DbCashewAccount dbCashewAccountCreated = await _dataStore.CashewAccountCreateAsync(dbCashewAccountToCreate);
            return dbCashewAccountCreated;
        }

        /// <summary>
        /// Update an existing Cashew Account in the Database with the latest from the Cashew Server
        /// </summary>
        /// <param name="cashewAccount"></param>
        /// <param name="userIdk"></param>
        /// <returns></returns>
        internal async Task<DbCashewAccount> CashewAccountUpdate(CNCashewAccount cashewAccount)
        {
            DbCashewAccount dbCashewAccountToUpdate = new DbCashewAccount()
            {
                LomentAccount = await _dataStore.LomentAccountGetAsync(),
                UserName = cashewAccount.UserName,
                CreateDateUtc = cashewAccount.CreateDateUtc,
                LastUpdateDateUtc = cashewAccount.LastUpdateDateUtc,
                Status = cashewAccount.Status
            };

            int rowsAffected = await _dataStore.CashewAccountUpdateAsync(dbCashewAccountToUpdate);
            if (rowsAffected > 0)
            {
                DbCashewAccount dbCashewAccountUpdated = await _dataStore.CashewAccountGetByUserIdAsync(cashewAccount.UserName);
                return dbCashewAccountUpdated;
            }

            return null;
        }

        /// <summary>
        /// Synchronize the accounts 
        /// </summary>
        /// <param name="dataStore"></param>
        /// <param name="accountsOnTheServer"></param>
        /// <returns></returns>
        internal async Task CashewAccountsSynchronize(IEnumerable<CNCashewAccount> accountsOnTheServer)
        {

            /* Get the list of CashewAccounts in the database */
            List<DbCashewAccount> cashewAccounts = await _dataStore.CashewAccountsGetAllListAsync();

            /* Reconcile the two lists by adding new accounts to DB and updating existing ones */
            foreach (CNCashewAccount cashewAccount in accountsOnTheServer)
            {
                DbCashewAccount dbCashewAccount = cashewAccounts.SingleOrDefault(o => o.UserName == cashewAccount.UserName);

                if (dbCashewAccount == null)
                {
                    /* Account is new or not yet in the database so create it in the DB */
                    DbCashewAccount newDBCashewAccount = await CashewAccountCreate(cashewAccount);
                }
                else
                {
                    /* Account exists in DB s update it with latest */
                    DbCashewAccount updatedDBCashewAccount = await CashewAccountUpdate(cashewAccount);
                }
            }

            /* Reconcile the two lists by deleting CashewAccounts from the database that do not exist according the Cashew Server*/
            foreach (DbCashewAccount cashewAccount in cashewAccounts)
            {
                /* Looping through each CashewAccount in the database */

                /* Does it exist in the feed from the Cashew Server? */
                CNCashewAccount account = accountsOnTheServer.SingleOrDefault(o => o.UserName == cashewAccount.UserName);
                if (account == null)
                {
                    /* The account no longer exists on the Cashew Server so remove it from the database 
                       Message, Conversation and Attachments are also removed. Downloaded attachment files are not removed. */
                    int rowsAffected = await _dataStore.CashewAccountDeleteAsync(cashewAccount);

                }
            }
        }

        /// <summary>
        /// Retrieve a list of CashewAccounts from the database
        /// </summary>
        /// <param name="dataStore"></param>
        /// <returns></returns>
        internal static async Task<List<DbCashewAccount>> CashewAccountsGetList(DataStore dataStore)
        {

            List<DbCashewAccount> dbCashewAccounts = await dataStore.CashewAccountsGetAllListAsync();

            if( dbCashewAccounts == null)
            {
                throw new Exception("Retrieving Cashew Accounts failed.");
            }

            return dbCashewAccounts;

        }

        /// <summary>
        /// Return a count of the number of CashewAccounts in the database
        /// </summary>
        /// <param name="dataStore"></param>
        /// <returns></returns>
        internal static async Task<int> CashewAccountsGetCountOf(DataStore dataStore)
        {

            return await dataStore.CashewAccountsCountAsync();
 
        }

    }
}
