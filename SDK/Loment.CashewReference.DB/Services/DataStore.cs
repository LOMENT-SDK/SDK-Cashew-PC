using System;
using System.Threading.Tasks;

using SQLite.Net.Async;

using Loment.CashewReference.DB.Model.Dao.Tables;

using Loment.CashewReference.DB.Services.Interfaces;
using Loment.CashewReference.DB.Model;
using System.Collections.Generic;

namespace Loment.CashewReference.DB.Services
{

    /// <summary>
    /// Root portion of teh DataStore partial class
    /// </summary>
    public partial class DataStore
    {
        private readonly ISqLitePcl _sqlitePcl;

        private enum Tables
        {
            AppConfigTable,
            AttachmentsTable,
            ContactsTable,
            ConversationsTable,
            MessagesTable,
            LomentAccountTable, 
            GroupMemberTable
        }


        /// <summary>
        /// Asynchronous database connection
        /// </summary>
        public SQLiteAsyncConnection AsyncConnection { get; internal set; }

        /// <summary>
        /// Name of the database file
        /// </summary>
        public String DbFileName { get; internal set; }


        /// <summary>
        /// DataStor constructor
        /// </summary>
        /// <param name="sqlitePcl"></param>
        /// <param name="dbFileName"></param>
        public DataStore(ISqLitePcl sqlitePcl)
        {
            _sqlitePcl = sqlitePcl;
        }

        public List<string> GetAvailbleAccountDatabases()
        {
            return _sqlitePcl.GetAvailableConnections();
        }

        /// <summary>
        /// Create database tables if needed
        /// </summary>
        /// <returns></returns>
        public async Task InitDatabaseTablesAsync(string dbFileName)
        {
            DbFileName = dbFileName;

            AsyncConnection = _sqlitePcl.GetConnection(DbFileName);

            /* Creates the DbAppConfigTable and inserts an empty row. Also creates in-memory DbAppConfig object */
            await AppConfigLoadAsync();
            //await AsyncConnection.CreateTableAsync<AppConfigTable>();

            /* Create the rest of the tables */
            await AsyncConnection.CreateTableAsync<LomentAccountTable>();
            await AsyncConnection.CreateTableAsync<ContactsTable>();
            await AsyncConnection.CreateTableAsync<GroupMemberTable>();
            await AsyncConnection.CreateTableAsync<ConversationsTable>();
            await AsyncConnection.CreateTableAsync<MessagesTable>();
            await AsyncConnection.CreateTableAsync<AttachmentsTable>();
        }


    }
}
