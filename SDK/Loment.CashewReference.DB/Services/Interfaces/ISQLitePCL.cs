using SQLite.Net.Async;
using SQLite.Net.Interop;
using System.Collections.Generic;

namespace Loment.CashewReference.DB.Services.Interfaces
{
    public interface ISqLitePcl
    {
        IReflectionService GetReflectionService();
        SQLiteAsyncConnection GetConnection(string dbFileName);

        List<string> GetAvailableConnections();
    }
}
