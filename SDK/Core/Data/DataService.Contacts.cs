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
    /// Database access methods and business logic for Contacts
    /// </summary>
    public partial class DataService
    {
        internal async Task<DbContact> CreateContactIfNeeded(CNMessageBase message)
        {

            DbContact dbContactExisting = await _dataStore.ContactGetBySenderIdAsync(message.SenderId);

            if (dbContactExisting != null)
            {
                return dbContactExisting;
            }

            DbContact dbContactToCreate = new DbContact()
            {
                SenderId = message.SenderId
            };

            return await _dataStore.ContactCreateAsync(dbContactToCreate);

        }
    }
}
