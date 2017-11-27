using System;
using System.Linq;
using System.Threading.Tasks;

using Loment.CashewReference.DB.Model;
using Loment.CashewReference.DB.Model.Dao;
using Loment.CashewReference.DB.Model.Dao.Tables;

namespace Loment.CashewReference.DB.Services
{
    /// <summary>
    ///     The usage of AppConfig is simple.
    ///     First, call DataStore.LoadAppConfig() (done).
    ///     Next, when editing app wide default config settings, use MainViewModel.AppConfigViewModel, followed by
    ///     AppConfigViewModel.UpdateAppConfigCommand.Execute()
    ///     Finally, when composing a new Email, set the UI in accordance with the MainViewModel.AppConfigViewModel.Entity
    ///     properties
    /// </summary>
    public partial class DataStore
    {
        /// <summary>
        /// The singleton DbAppConfig, call LoadAppConfig prior to using it
        /// </summary>
        private DbAppConfig _appConfig;

        public DbAppConfig DbAppConfig
        {
            get
            {
                if (_appConfig == null)
                {
                    throw new Exception("DataStore.LoadAppConfig() must be called prior to accessing DataStore.AppConfig");
                }
                return _appConfig;
            }
            private set { _appConfig = value; }
        }

        /// <summary>
        /// Call this prior to accessing AppConfig
        /// </summary>
        /// <returns></returns>
        public async Task<DbAppConfig> AppConfigLoadAsync()
        {
            if (_appConfig != null) { 
                return DbAppConfig;
            }

            await AsyncConnection.CreateTableAsync<AppConfigTable>();

            var appConfigTbl = (await AsyncConnection.Table<AppConfigTable>().ToListAsync()).FirstOrDefault();

            if (appConfigTbl != null)
            {
                DbAppConfig = appConfigTbl.Map();
            }
            else
            {
                // was null, reuse with a new DTO
                appConfigTbl = new DbAppConfig().Map();

                // insert, gets ID
                await AsyncConnection.InsertAsync(appConfigTbl);

                // use this new one
                DbAppConfig = appConfigTbl.Map();
            }

            return DbAppConfig;
        }

        /// <summary>
        /// Persist the updated AppConfig (singleton)
        /// </summary>
        /// <param name="appConfig"></param>
        /// <returns></returns>
        public async Task<int> AppConfigUpdateAsync(DbAppConfig appConfig)
        {
            /* Update the database */
            int rowsAffeted = await AsyncConnection.UpdateAsync(appConfig.Map());

            /* Set the new cached object to the one provided */
            DbAppConfig = appConfig;

            return rowsAffeted;

        }

        /// <summary>
        /// Persist the login user name
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public async Task<int> LomentLoginUserNameSaveAsync(string username)
        {
            DbAppConfig.LomentUserName = username;

            return await AppConfigUpdateAsync(DbAppConfig);

        }

        /// <summary>
        /// Persist the new Active state of the Loment account
        /// </summary>
        /// <param name="isActive"></param>
        /// <returns></returns>
        public async Task<int> LomentAccountSubscriptionIsActiveSaveAsync(bool isActive)
        {
            DbAppConfig.LomentAccountSubscriptionIsActive = isActive;

            return await AppConfigUpdateAsync(DbAppConfig);
            
        }


        /// <summary>
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public async Task<int> LomentLoginInfoSaveAsync(string username, bool isRememberPassword, string password)
        {
            DbAppConfig.LomentUserName      = username;
            DbAppConfig.IsRememberPassword  = isRememberPassword;

            if (isRememberPassword)
            {
                DbAppConfig.Password = password;
            }

            return await AppConfigUpdateAsync(DbAppConfig);

        }
    }
}
