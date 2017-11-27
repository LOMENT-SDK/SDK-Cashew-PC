package com.loment.cashewnut.database.mappers;

import com.loment.cashewnut.database.ProfileStore;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.enc.Crypter;
import com.loment.cashewnut.enc.EncoderDecoder;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.enc.MessageKey;
import com.loment.cashewnut.model.ContactsModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContactsMapper {

    private static DBContactsMapper instance;
    private Connection connection;

    private DBContactsMapper() throws ClassNotFoundException {
        try {
            connection = DBConnection.getInstance().getConnection(
                    DatabaseHandler.DATABASE_NAME);
            createTableIfNotExists(); 
    
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }

    public static DBContactsMapper getInstance() {
        if (instance == null) {
            try {
                instance = new DBContactsMapper();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instance;
    }
   

    private void createTableIfNotExists() {
        PreparedStatement pst;
        try {
            pst = connection
                    .prepareStatement(DatabaseHandler.CREATE_TABLE_CONTACTS);
            pst.execute();
            Statement st = connection.createStatement();
            String sql = "select * from "+ DatabaseHandler.TABLE_CONTACTS+" ";
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int rowCount = metaData.getColumnCount();
            boolean isMyColumnPresent = false;
            String myColumnName = DatabaseHandler.KEY_STATUS_MSG;
            for (int i = 1; i <= rowCount; i++) {
            	 
              if (myColumnName.equals(metaData.getColumnName(i))) {
            	 
                isMyColumnPresent = true;
              }
            }
            if(!isMyColumnPresent)
            {
            String query=DatabaseHandler.ALTER_CONTACT_TABLE;
            PreparedStatement stmt=connection.prepareStatement(query);
           stmt.execute();
           System.out.println("altered");
           
            }    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long insert(String cashewnutID, String name, String phone,
            String email) {

        DBConnection.getInstance().setLock();
        ResultSet rs = null;
        try {
            String lomentId = ProfileStore.getInstance().getLomentId();
            createTableIfNotExists();         
            PreparedStatement insertStmt = connection.prepareStatement(
                    DatabaseHandler.INSERT_CONTACTS_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
            ContactsModel contactModel = new ContactsModel();
            contactModel.setFirstName(name);
            contactModel.setCashewnutId(cashewnutID);
            contactModel.setPhone(phone);
            contactModel.setEmailId(email);
            insertStmt.setInt(2, contactModel.getContactServerId());
            insertStmt.setString(3, encptValue(lomentId, contactModel.getContactLinkedId()));
            insertStmt.setString(4, encptValue(lomentId, contactModel.getFirstName()));
            insertStmt.setString(5, encptValue(lomentId, "  "));
            insertStmt.setString(6, encptValue(lomentId, contactModel.getWalnutId()));
            insertStmt.setString(7, encptValue(lomentId, contactModel.getCashewnutId()));
            insertStmt.setString(8, encptValue(lomentId, contactModel.getPeanutId()));
            insertStmt.setString(9, encptValue(lomentId, contactModel.getPhone()));
            insertStmt.setString(10, encptValue(lomentId, contactModel.getEmailId()));
            insertStmt.setString(11, encptValue(lomentId, contactModel.getPhotoUrl()));
            insertStmt.setInt(12, contactModel.getPhotoExist());
            insertStmt.setString(13, encptValue(lomentId, contactModel.getNotes()));
            insertStmt.setString(14, encptValue(lomentId, contactModel.getLastUpdateDate()));           
            insertStmt.setInt(15,contactModel.getStatus());
            insertStmt.execute();
            //System.out.println("Data Inserted successfully into Message Table");
            rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBContactsMapper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            DBConnection.getInstance().closeLock();
        }
        return -1;
    }
    public long insertRequest(String cashewnutID, String name, String phone,
            String email,int status,String comments,int serverId) {

        DBConnection.getInstance().setLock();
        ResultSet rs = null;
        try {
            String lomentId = ProfileStore.getInstance().getLomentId();
            createTableIfNotExists();
           
            PreparedStatement insertStmt = connection.prepareStatement(
                    DatabaseHandler.INSERT_CONTACTS_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);

            ContactsModel contactModel = new ContactsModel();
            contactModel.setFirstName(name);
            contactModel.setCashewnutId(cashewnutID);
            contactModel.setPhone(phone);
            contactModel.setEmailId(email);
            contactModel.setStatus(status);
            contactModel.setNotes(comments);
            contactModel.setContactServerId(serverId);
            insertStmt.setInt(2, contactModel.getContactServerId());
            insertStmt.setString(3, encptValue(lomentId, contactModel.getContactLinkedId()));
            insertStmt.setString(4, encptValue(lomentId, contactModel.getFirstName()));
            insertStmt.setString(5, encptValue(lomentId, "  "));
            insertStmt.setString(6, encptValue(lomentId, contactModel.getWalnutId()));
            insertStmt.setString(7, encptValue(lomentId, contactModel.getCashewnutId()));
            insertStmt.setString(8, encptValue(lomentId, contactModel.getPeanutId()));
            insertStmt.setString(9, encptValue(lomentId, contactModel.getPhone()));
            insertStmt.setString(10, encptValue(lomentId, contactModel.getEmailId()));
            insertStmt.setString(11, encptValue(lomentId, contactModel.getPhotoUrl()));
            insertStmt.setInt(12, contactModel.getPhotoExist());
            insertStmt.setString(13, encptValue(lomentId, contactModel.getNotes()));
            insertStmt.setString(14, encptValue(lomentId, contactModel.getLastUpdateDate()));           
            insertStmt.setInt(15,contactModel.getStatus());
            insertStmt.execute();
            //System.out.println("Data Inserted successfully into Message Table");
            rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBContactsMapper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            DBConnection.getInstance().closeLock();
        }
        return -1;
    }
    public synchronized long update(String cashewnutID, String name, String phone,
            String email,int contact_status) {
        int status = 0;
        String lomentId = ProfileStore.getInstance().getLomentId();
         PreparedStatement insertStmt = null;
        try {
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_CONTACTS
                    + "  SET  " + DatabaseHandler.KEY_FIRSTNAME + " = " + "?,"
                    + DatabaseHandler.KEY_CASHEWNUT_ID + " = " + "?,"
                    + DatabaseHandler.KEY_PHONE + " = " + "?,"
                    + DatabaseHandler.KEY_STATUS_MSG + "=" + "?,"
                    + DatabaseHandler.KEY_EMAIL + " = " + "?" + "  WHERE "
                    + DatabaseHandler.KEY_CASHEWNUT_ID + " =  " + "'" + encptValue(lomentId, cashewnutID)
                    +  "' ";

            insertStmt = connection
                    .prepareStatement(updateQuery);

            insertStmt.setString(1, encptValue(lomentId, name));
            insertStmt.setString(2, encptValue(lomentId, cashewnutID));
            insertStmt.setString(3, encptValue(lomentId, phone));
            insertStmt.setInt(4, contact_status);
            insertStmt.setString(5, encptValue(lomentId, email));

            if (insertStmt.executeUpdate() > 0) {
                status = 1;
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }finally {
            try {
                if (insertStmt != null) {
                    insertStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBHeaderMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

    public ContactsModel getContact(String cashewnutID,int status) {

        String lomentId = ProfileStore.getInstance().getLomentId();
        ContactsModel contact = new ContactsModel();
        ResultSet cursor = null;
        if (cashewnutID != null && cashewnutID.trim().length() > 0) {
            try {

                String selectQuery = "SELECT * FROM  "
                        + DatabaseHandler.TABLE_CONTACTS + "  where "  
                        +DatabaseHandler.KEY_STATUS_MSG+"="+status+" and "
                        + DatabaseHandler.KEY_CASHEWNUT_ID + "  =  '"
                        + encptValue(lomentId, cashewnutID) + "' ";

                cursor = connection.createStatement().executeQuery(selectQuery);
                if (cursor != null && cursor.next()) {
                    contact.setContactId(cursor.getInt(1));
                    contact.setContactServerId(cursor.getInt(2));
                    contact.setContactLinkedId(decptValue(lomentId, cursor.getString(3)));
                    contact.setFirstName(decptValue(lomentId, cursor.getString(4)));
                    contact.setLastName(decptValue(lomentId, cursor.getString(5)));
                    contact.setWalnutId(decptValue(lomentId, cursor.getString(6)));
                    contact.setCashewnutId(decptValue(lomentId, cursor.getString(7)));
                    contact.setPeanutId(decptValue(lomentId, cursor.getString(8)));
                    contact.setPhone(decptValue(lomentId, cursor.getString(9)));
                    contact.setEmailId(decptValue(lomentId, cursor.getString(10)));
                    contact.setPhotoUrl(decptValue(lomentId, cursor.getString(11)));
                    contact.setPhotoExist(cursor.getInt(12));
                    contact.setNotes(decptValue(lomentId, cursor.getString(13)));
                    contact.setLastUpdateDate(decptValue(lomentId, cursor.getString(14)));
                    contact.setStatus(cursor.getInt(15));
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contact;
    }

    public ArrayList<ContactsModel> getContactNameAndIDList() {
        String selectQuery = "SELECT " + DatabaseHandler.KEY_CONTACT_ID + ","
                + DatabaseHandler.KEY_FIRSTNAME + ","
                + DatabaseHandler.KEY_CASHEWNUT_ID + ","
                + DatabaseHandler.KEY_PHONE +","
                +DatabaseHandler.KEY_EMAIL+" FROM  "
                + DatabaseHandler.TABLE_CONTACTS+" where "  
                +DatabaseHandler.KEY_STATUS_MSG+" = 0";
        ResultSet cursor = null;
        ArrayList<ContactsModel> contactsModel = new ArrayList<ContactsModel>();
        try {
            String lomentId = ProfileStore.getInstance().getLomentId();
            cursor = connection.createStatement().executeQuery(selectQuery);
            if (cursor.next()) {
                do {
                    ContactsModel contact = new ContactsModel();
                    contact.setContactId(cursor.getInt(1));
                    contact.setFirstName(decptValue(lomentId, cursor.getString(2)));
                    contact.setCashewnutId(decptValue(lomentId, cursor.getString(3)));
                    contact.setPhone(decptValue(lomentId, cursor.getString(4)));
                    contact.setEmailId(decptValue(lomentId, cursor.getString(5)));
                    contactsModel.add(contact);
                } while (cursor.next());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactsModel;
    }
    
    public ArrayList<ContactsModel> getRequestContactNameAndIDList() {
    	String selectQuery = "SELECT " + DatabaseHandler.KEY_CONTACT_ID + ","
                + DatabaseHandler.KEY_FIRSTNAME + ","
                + DatabaseHandler.KEY_CASHEWNUT_ID + ","
                + DatabaseHandler.KEY_PHONE + " FROM  "
                + DatabaseHandler.TABLE_CONTACTS+" where "  
                +DatabaseHandler.KEY_STATUS_MSG+" =1";
        ResultSet cursor = null;
        ArrayList<ContactsModel> contactsModel = new ArrayList<ContactsModel>();
        try {
            String lomentId = ProfileStore.getInstance().getLomentId();
            cursor = connection.createStatement().executeQuery(selectQuery);
            if (cursor.next()) {
                do {
                    ContactsModel contact = new ContactsModel();
                    contact.setContactId(cursor.getInt(1));
                    contact.setFirstName(decptValue(lomentId, cursor.getString(2)));
                    contact.setCashewnutId(decptValue(lomentId, cursor.getString(3)));
                    contact.setPhone(decptValue(lomentId, cursor.getString(4)));
                    contactsModel.add(contact);
                } while (cursor.next());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactsModel;
    }
    
    
    

    private String encptValue(String keyStr, String value) throws IOException {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        Key key = new MessageKey(keyStr);
        String string = EncoderDecoder.encode(Crypter.encrypt(key,
                value.getBytes("UTF-8")));
        return string;
    }

    private String decptValue(String keyStr, String value) throws IOException {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        byte[] data = EncoderDecoder.decode(value);
        byte[] record = Crypter.decrypt(new MessageKey(keyStr), data);
        String string = new String(record, "UTF-8");
        return string;
    }
}
