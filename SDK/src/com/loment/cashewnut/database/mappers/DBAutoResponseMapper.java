package com.loment.cashewnut.database.mappers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.AutoResponseModel;
public class DBAutoResponseMapper {

	 private static DBAutoResponseMapper instance;
	    private Connection connection;
	    boolean isNotNull=true;
	    private DBAutoResponseMapper() throws ClassNotFoundException {
	        try {
	            connection = DBConnection.getInstance().getConnection(
	                    DatabaseHandler.DATABASE_NAME);
	            createTableIfNotExists(); 
	    
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
	    }

	    public static DBAutoResponseMapper getInstance() {
	        if (instance == null) {
	            try {
	                instance = new DBAutoResponseMapper();
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
	                    .prepareStatement(DatabaseHandler.CREATE_TABLE_AUTO_RESPONSE);
	            pst.execute();	            
	               
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public long insert(String cashewnutID, String message) {
	        DBConnection.getInstance().setLock();
	        ResultSet rs = null;
	        try {
	          
	            createTableIfNotExists();         
	            PreparedStatement insertStmt = connection.prepareStatement(
	                    DatabaseHandler.INSERT_AUTO_RESPONSE_DATA_QUERY,
	                    Statement.RETURN_GENERATED_KEYS);
	            Calendar calendar = Calendar.getInstance();
	         java.util.Date currentDate = calendar.getTime();
	         java.sql.Date date = new java.sql.Date(currentDate.getTime());
	         java.util.Date date1 = new Date();
	         Timestamp timestamp = new Timestamp(date1.getTime());	                     
	            insertStmt.setString(2, cashewnutID);
	            insertStmt.setString(3, message);
	            insertStmt.setDate(4,date);
	            insertStmt.setTimestamp(5,timestamp);	           
	            insertStmt.execute();
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
	    
	    public AutoResponseModel getAutoResponseContact(String cashewnutID) {
	        AutoResponseModel autoResponse = new AutoResponseModel();
	        
	        ResultSet cursor = null;
	        if (cashewnutID != null && cashewnutID.trim().length() > 0) {
	            try {

	                String selectQuery = "SELECT * FROM  "
	                        +DatabaseHandler.TABLE_AUTO_RESPONSE + "  where "  
	                        +DatabaseHandler.KEY_AUTO_RESPONSE_CASHEWID+"='"+cashewnutID+"' and " +DatabaseHandler.KEY_AUTO_RESPONSE_TIME+" = (SELECT MAX("
	                        +DatabaseHandler.KEY_AUTO_RESPONSE_TIME + ") FROM "+DatabaseHandler.TABLE_AUTO_RESPONSE+")";

	                cursor = connection.createStatement().executeQuery(selectQuery);
	                if (cursor != null && cursor.next()) {
	                	autoResponse.setCashewnutId(cursor.getString(2));
	                	autoResponse.setMessage(cursor.getString(3));
	                	autoResponse.setTimestamp(cursor.getTimestamp(5));
	                  isNotNull=false;
	                }	               
	                if (cursor != null) {
	                    cursor.close();
	                   
	                }
	                if(isNotNull)
	                {
	                	return null;
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return autoResponse;
	    }

/*	    public ArrayList<ContactsModel> getContactNameAndIDList() {
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
	    }*/
	
}
