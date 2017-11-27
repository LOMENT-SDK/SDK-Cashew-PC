using Cashew.Model;
using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Cashew;

namespace Core.Data
{
    /// <summary>
    /// Database access methods and business logic for the LomentAccount
    /// </summary>
    public partial class DataService
    {
        internal static async void LomentAccountCreateOrUpdate(DataStore dataStore, CNLomentAccount authenticatedLomentAccount)
        {
            DbLomentAccount lomentAccountExisting = await dataStore.LomentAccountGetAsync();

            if (lomentAccountExisting == null)
            {
                /* Account does not exist so create a new one */
                lomentAccountExisting = await dataStore.LomentAccountCreateAsync(new DbLomentAccount
                {
                    DeviceId = authenticatedLomentAccount.DeviceId,
                    Email = authenticatedLomentAccount.Email,
                    Password = authenticatedLomentAccount.Password,
                    Phone = authenticatedLomentAccount.Phone,
                    UserId = authenticatedLomentAccount.UserId,
                    UserName = authenticatedLomentAccount.UserName
                });
            }
            else
            {
                /* Account already exists - update it with the latest */
                DbLomentAccount lomentAccountToUpdate = new DbLomentAccount
                {
                    Pk = lomentAccountExisting.Pk,
                    DeviceId = authenticatedLomentAccount.DeviceId,
                    Email = authenticatedLomentAccount.Email,
                    Password = authenticatedLomentAccount.Password,
                    Phone = authenticatedLomentAccount.Phone,
                    UserId = authenticatedLomentAccount.UserId,
                    UserName = authenticatedLomentAccount.UserName
                };

                int rowsAffected = await dataStore.LomentAccountUpdateAsync(lomentAccountToUpdate);
            }
        }

   

        /// <summary>
        /// Get the Loment Account
        /// </summary>
        /// <param name="dataStore"></param>
        /// <returns></returns>
        internal static async Task<DbLomentAccount> LomentAccountGet(DataStore dataStore)
        {

            List<DbLomentAccount> dbLomentAccounts = await dataStore.LomentAccountsGetListofAllAsync();

            if ((dbLomentAccounts == null) || (dbLomentAccounts.Count == 0))
            {
                return null;
            }

            return dbLomentAccounts.FirstOrDefault();

        }


        /// <summary>
        /// Remove the LomentAccount from the database
        /// </summary>
        /// <param name="dataStore"></param>
        /// <returns></returns>
        private static async Task<bool> LomentAccountDelete(DataStore dataStore)
        {

            /* How many exist */
            int numberOfLomentAccounts = await dataStore.LomentGeAccountsCountAllAsync();

            if (await dataStore.LomentGeAccountsCountAllAsync() == 0)
            {
                /* No LomentAccounts in the database */
                return true;
            }


            /* Successfully created new LomentAccount, now deleted it. */
            int rowsAffected = await dataStore.CasehwAccountsDeleteAllAsync();


            if (rowsAffected == 0)
            {
                throw new Exception("Unable to remove Loment Account from database.");
            }

            return true;

        }
    }
}
