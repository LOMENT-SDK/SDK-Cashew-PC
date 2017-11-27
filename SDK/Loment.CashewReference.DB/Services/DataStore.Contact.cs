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
    /// Partial class implementation for DataStore.Contacts
    /// </summary>
    public partial class DataStore
    {

        private enum ContactsColumns
        {
            SenderId,
            Email,
            Name,
            Phone,
            ImagePath
        }
 
        /// <summary>
        /// Retrieve a count of the number of Contacts in the ContactactsTble DB table
        /// </summary>
        /// <returns></returns>
        public async Task<int> ContactsCountAllAsync()
        {
            return await AsyncConnection.ExecuteScalarAsync<int>( "SELECT COUNT(*) FROM " + Tables.ContactsTable.ToString()) ;
        }

        /// <summary>
        /// Return an ObservableCollection of Contacts retrieved from the DB
        /// </summary>
        /// <returns></returns>
        public async Task<List<DbContact>> ContactsGetListOfAllAsync()
        {

            List<DbContact> contactsTable = new List<DbContact>(
                    (await AsyncConnection.Table<ContactsTable>().ToListAsync()).OrderBy(x => x.Name)
                        .Select(x => x.Map()));
            return contactsTable;

        }

        /// <summary>
        /// Create a new DbContact
        /// Returns the new instance
        /// </summary>
        /// <param name="dbContact">The DbContact to create</param>
        public async Task<DbContact> ContactCreateAsync(DbContact dbContact)
        {
            var mappedContact = dbContact.Map();

            int rowsAffected =  await AsyncConnection.InsertAsync(mappedContact);

            if( rowsAffected == 0)
            {
                return null;
            }

            return dbContact;
        }



        public async Task<DbContact> ContactGetBySenderIdAsync(string senderId)
        {
            string SQLString = 
                "SELECT * FROM " +
                    Tables.ContactsTable.ToString() +
                " WHERE " + 
                    ContactsColumns.SenderId.ToString() +
                " = ? ";

            List<ContactsTable> contactsTable = await AsyncConnection.QueryAsync<ContactsTable>(SQLString, senderId);
            ContactsTable contactsTableRow = contactsTable.FirstOrDefault();

            if (contactsTableRow == null)
            {
                return null;
            }
            return contactsTableRow.Map();

        }

        /// <summary>
        /// Update a DbContact's record
        /// Update uses the Pk property to locate the Contact based on
        /// its Primary Key value
        /// Returns the number of rows affected
        /// </summary>
        /// <param name="dbContact"></param>
        public async Task<int> ContactUpdateAsync(DbContact dbContact)
        {

            return await AsyncConnection.UpdateAsync(dbContact.Map());

        }

        /// <summary>
        /// Delete a DbContact from the local db
        /// Delete uses the Pk property to locate the Contact based on
        /// its Primary Key value
        /// </summary>
        /// <param name="dbContact"></param>
        public async Task<int> ContactDeleteAsync(DbContact dbContact)
        {

            int rowsAffected = await AsyncConnection.DeleteAsync(dbContact.Map());

            return rowsAffected;

        }

    }
}