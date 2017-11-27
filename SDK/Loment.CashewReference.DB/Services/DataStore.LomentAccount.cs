using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Model.Dao.Tables;
using Loment.CashewReference.DB.Model.Dao;


namespace Loment.CashewReference.DB.Services
{
    /// <summary>
    /// Partial class implementation for DataStore.LomentAccount
    /// </summary>
    public partial class DataStore
    {
        private enum LomentAccountsColumnNames
        {
        }


        /// <summary>
        /// Retrieve a count of the number of LomentAccounts in the LomentAccountactsTble DB table
        /// Note: Should only ever be one instance
        /// </summary>
        /// <returns></returns>
        public async Task<int> LomentGeAccountsCountAllAsync()
        {
            return await AsyncConnection.ExecuteScalarAsync<int>("SELECT COUNT(*) FROM " + Tables.LomentAccountTable.ToString());
        }

        /// <summary>
        /// Return an ObservableCollection of LomentAccounts retrieved from the DB
        /// Note: Should only ever be one instance
        /// </summary>
        /// <returns></returns>
        public async Task<List<DbLomentAccount>> LomentAccountsGetListofAllAsync()
        {

            List<DbLomentAccount> lomentAccountTable = new List<DbLomentAccount>(
                    (await AsyncConnection.Table<LomentAccountTable>().ToListAsync()).OrderBy(x => x.Pk)
                        .Select(x => x.Map()));
            return lomentAccountTable;

        }

        /// <summary>
        /// Create a new DbLomentAccount
        /// Returns the new instance
        /// Note: Should only ever be one instance
        /// </summary>
        /// <param name="dbLomentAccount">The DbLomentAccount to create</param>
        public async Task<DbLomentAccount> LomentAccountCreateAsync(DbLomentAccount dbLomentAccount)
        {
            var mappedLomentAccount = dbLomentAccount.Map();

            await AsyncConnection.InsertAsync(mappedLomentAccount);

            dbLomentAccount.Pk = mappedLomentAccount.Pk;

            return dbLomentAccount;
        }

        /// <summary>
        /// Update a DbLomentAccount's record
        /// Update uses the Pk property to locate the LomentAccount based on
        /// its Primary Key value
        /// Returns the number of rows affected
        /// Note: Should only ever be one instance
        /// </summary>
        /// <param name="dbLomentAccount"></param>
        public async Task<int> LomentAccountUpdateAsync(DbLomentAccount dbLomentAccount)
        {

            return await AsyncConnection.UpdateAsync(dbLomentAccount.Map());

        }

        /// <summary>
        /// Delete a DbLomentAccount from the local db
        /// Delete uses the Pk property to locate the LomentAccount based on
        /// its Primary Key value
        /// Note: Should only ever be one instance
        /// </summary>
        /// <param name="dbLomentAccount"></param>
        public async Task<int> LomentAccountDeleteAsync(DbLomentAccount dbLomentAccount)
        {

            int rowsAffected = await AsyncConnection.DeleteAsync(dbLomentAccount.Map());

            return rowsAffected;
        }

        /// <summary>
        /// Return a single instance of a LomentAccount table row based on the primary key
        /// </summary>
        /// <param name="pk"></param>
        /// <returns></returns>
        public async Task<DbLomentAccount> LomentAccountGetByPkAsync(long pk)
        {
            LomentAccountTable lomentAccountTable = await AsyncConnection.GetAsync<LomentAccountTable>(pk);

            return lomentAccountTable.Map();
        }

        /// <summary>
        /// Return a single instance of a LomentAccount table
        /// This is an alternative accessor method for use where the PK value is not already known
        /// </summary>
        /// <param name="pk"></param>
        /// <returns></returns>
        public async Task<DbLomentAccount> LomentAccountGetAsync()
        {
            var lomentAccountTable = (await AsyncConnection.Table<LomentAccountTable>().ToListAsync()).FirstOrDefault();

            if ( lomentAccountTable == null)
            {
                return null;
            }

            return lomentAccountTable.Map();
        }


        public async Task<int> LomentAccountsDeleteAllAsync()
        {
            int rowsAffected = await AsyncConnection.DeleteAllAsync<LomentAccountTable>();
            return rowsAffected;
        }
    }
}