using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Threading.Tasks;

using SQLite.Net;
using SQLite.Net.Async;
using SQLite.Net.Interop;
using SQLite.Net.Platform.Win32;

using Cashew.Utility.Services;
using Loment.CashewReference.DB.Services.Interfaces;
using System.Linq;

namespace WpfCashew.Services
{

    internal class Connection
    {
        public SQLiteConnection SQLiteConnection { get; set; }
        public SQLiteAsyncConnection SQLiteAsyncConnection { get; set; }

        public Connection(SQLiteConnection sqLiteConnection, SQLiteAsyncConnection sqLiteAsyncConnection)
        {
            SQLiteConnection = sqLiteConnection;
            SQLiteAsyncConnection = sqLiteAsyncConnection;
        }
    }

    public class WpfSqlitePcl : ISqLitePcl
    {
        private static readonly Dictionary<string, Connection> ConnectionCache = new Dictionary<string, Connection>();

        private readonly IPlatformUtility _platformUtility;

        private readonly IReflectionService _reflectionService;


        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="platformUtility"></param>
        public WpfSqlitePcl(IPlatformUtility platformUtility)
        {
            _platformUtility = platformUtility;

            _reflectionService = new SQLitePlatformWin32().ReflectionService;

            Directory.CreateDirectory(_platformUtility.LocalStorageFullPath);
        }

        /// <summary>
        /// Get the reflection services
        /// </summary>
        /// <returns></returns>
        public IReflectionService GetReflectionService()
        {
            return _reflectionService;
        }

        /// <summary>
        /// Retrieves a SQLiteConnection
        /// </summary>
        /// <param name="dbFileName"></param>
        /// <returns></returns>
        private SQLiteAsyncConnection GetConnection(string dbFileName)
        {
            if (ConnectionCache.ContainsKey(dbFileName))
                return ConnectionCache[dbFileName].SQLiteAsyncConnection;

            var path = Path.Combine(_platformUtility.LocalStorageFullPath, dbFileName);

#if DEBUG
            Debug.WriteLine("DB path: {0}", path);
#endif

            var db = new SQLiteConnectionWithLock(new SQLitePlatformWin32(), new SQLiteConnectionString(path, storeDateTimeAsTicks: false));

            var asyncDb = new SQLiteAsyncConnection(() => db);

            ConnectionCache[dbFileName] = new Connection(db, asyncDb);

            return asyncDb;
        }

        /// <summary>
        /// Get a connection to the database by passing in the file name
        /// In this application the file name is assumed to be the authenticated user's name
        /// </summary>
        /// <param name="dbFileName"></param>
        /// <returns></returns>
        SQLiteAsyncConnection ISqLitePcl.GetConnection(string dbFileName)
        {
            return GetConnection(dbFileName);
        }

        /// <summary>
        /// Disconnects from the database and deletes the database file
        /// </summary>
        /// <param name="dbFileName"></param>
        /// <returns></returns>
        public async Task DeleteDatabase(string dbFileName)
        {

            if (ConnectionCache.ContainsKey(dbFileName))
            {
                ConnectionCache.Remove(dbFileName);

                var path = Path.Combine(_platformUtility.LocalStorageFullPath, dbFileName);

                await _platformUtility.DeleteFile(path);
            }
            else
            {
                throw new ArgumentException("Can not destroy a connection that does not exist.");
            }
        }

        public List<string> GetAvailableConnections()
        {
            var dbFiles = _platformUtility.ListFiles("*.db");
            return dbFiles;
        }
    }
}
