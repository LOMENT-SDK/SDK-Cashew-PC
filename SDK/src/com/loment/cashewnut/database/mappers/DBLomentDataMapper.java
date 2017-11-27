package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.enc.Crypter;
import com.loment.cashewnut.enc.EncoderDecoder;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.enc.MessageKey;
import com.loment.cashewnut.model.LomentDataModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBLomentDataMapper {

    private static DBLomentDataMapper instance;
    private Connection connection;

    public DBLomentDataMapper() throws ClassNotFoundException {
        try {
            connection = DBConnection.getInstance().getConnection(
                    DatabaseHandler.DATABASE_NAME);
            createTableIfNotExists();
        } catch (Exception ex) {
        }
    }

    public static DBLomentDataMapper getInstance() {
        if (instance == null) {
            try {
                instance = new DBLomentDataMapper();
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
                    .prepareStatement(DatabaseHandler.CREATE_LOMENT_DATA_TABLE);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long insert(LomentDataModel lomentDataModel) {

        DBConnection.getInstance().setLock();
        try {
            createTableIfNotExists();
            PreparedStatement insertStmt = connection.prepareStatement(
                    DatabaseHandler.INSERT_LOMENT_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);

            String lomentId = lomentDataModel.getLomentId();

            insertStmt.setString(1, encptValue(lomentId, lomentDataModel.getUsername()));
            insertStmt.setString(2, encptValue(lomentId, lomentDataModel.getPassword()));
            insertStmt.setString(3, encptValue(keyString, lomentDataModel.getLomentId()));
            insertStmt.setString(4, encptValue(lomentId, lomentDataModel.getEmail()));
            insertStmt.setString(5, encptValue(lomentId, lomentDataModel.getMobileNumber()));
            insertStmt.setString(6, encptValue(lomentId, lomentDataModel.getCountryCode()));
            insertStmt.setString(7, encptValue(lomentId, lomentDataModel.getCountryAbbr()));
            insertStmt.setString(8, encptValue(lomentId, lomentDataModel.getDeviceName()));
            insertStmt.setString(9, encptValue(lomentId, lomentDataModel.getDeviceId()));
            insertStmt.setString(10, encptValue(lomentId, lomentDataModel.getUserId()));
            insertStmt.setString(11, encptValue(lomentId, lomentDataModel.getSubscriptionId()));
            insertStmt.setString(12, encptValue(lomentId, lomentDataModel.getSubscriptionType()));
            insertStmt.setString(13, encptValue(lomentId, lomentDataModel.getSubscriptionStatus()));
            insertStmt.setString(14, encptValue(lomentId, lomentDataModel.getStartDate()));
            insertStmt.setString(15, encptValue(lomentId, lomentDataModel.getEndDate()));
            insertStmt.executeUpdate();

            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.getInstance().closeLock();
        }
        return -1;
    }

    public LomentDataModel getLomentData() {

        String selectQuery = "SELECT  * FROM  "
                + DatabaseHandler.TABLE_LOMENT_DATA;
        ResultSet cursor = null;
        LomentDataModel lomentData = new LomentDataModel();
        try {
            cursor = connection.createStatement().executeQuery(selectQuery);
            if (cursor.next()) {
                do {
                    String lomentId = decptValue(keyString, cursor.getString(3));

                    lomentData.setUsername(decptValue(lomentId, cursor.getString(1)));
                    lomentData.setPassword(decptValue(lomentId, cursor.getString(2)));
                    lomentData.setLomentId(lomentId);
                    lomentData.setEmail(decptValue(lomentId, cursor.getString(4)));
                    lomentData.setMobileNumber(decptValue(lomentId, cursor.getString(5)));
                    lomentData.setCountryCode(decptValue(lomentId, cursor.getString(6)));
                    lomentData.setCountryAbbr(decptValue(lomentId, cursor.getString(7)));
                    lomentData.setDeviceName(decptValue(lomentId, cursor.getString(8)));
                    lomentData.setDeviceId(decptValue(lomentId, cursor.getString(9)));
                    lomentData.setUserId(decptValue(lomentId, cursor.getString(10)));
                    lomentData.setSubscriptionId(decptValue(lomentId, cursor.getString(11)));
                    lomentData.setSubscriptionType(decptValue(lomentId, cursor.getString(12)));
                    lomentData.setSubscriptionStatus(decptValue(lomentId, cursor.getString(13)));
                    lomentData.setStartDate(decptValue(lomentId, cursor.getString(14)));
                    lomentData.setEndDate(decptValue(lomentId, cursor.getString(15)));
                } while (cursor.next());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lomentData;
    }

    public int updateLomentData(LomentDataModel lomentDataModel) {
        int status = 0;
        String lomentId = lomentDataModel.getLomentId();
        try {
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_LOMENT_DATA
                    + "  SET  " + DatabaseHandler.KEY_DEVICE_NAME + " = " + "?,"
                    + DatabaseHandler.KEY_DEVICE_ID + " = " + "?,"
                    + DatabaseHandler.KEY_SUBSCRIPTION_ID + " = " + "?,"
                    + DatabaseHandler.KEY_SUBSCRIPTION_TYPE + " = " + "?,"
                    + DatabaseHandler.KEY_SUBSCRIPTION_STATUS + " = " + "?,"
                    + DatabaseHandler.KEY_START_DATE + " = " + "?,"
                    + DatabaseHandler.KEY_END_DATE + " = " + "?" + " WHERE "
                    + DatabaseHandler.KEY_LOMENT_ID + " =  " + "'"
                    + encptValue(keyString, lomentId) + "' ";

            PreparedStatement insertStmt = connection
                    .prepareStatement(updateQuery);

            insertStmt.setString(1, encptValue(lomentId, lomentDataModel.getDeviceName()));
            insertStmt.setString(2, encptValue(lomentId, lomentDataModel.getDeviceId()));
            insertStmt.setString(3, encptValue(lomentId, lomentDataModel.getSubscriptionId()));
            insertStmt.setString(4, encptValue(lomentId, lomentDataModel.getSubscriptionType()));
            insertStmt.setString(5, encptValue(lomentId, lomentDataModel.getSubscriptionStatus()));
            insertStmt.setString(6, encptValue(lomentId, lomentDataModel.getStartDate()));
            insertStmt.setString(7, encptValue(lomentId, lomentDataModel.getEndDate()));
         

            if (insertStmt.executeUpdate() > 0) {
                status = 1;
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return status;
    }

    // public int deleteAll() {
    // return wdb.delete(DatabaseHandler1.TABLE_LOMENT_DATA, null, null);
    // }
    public int updateLomentAccountPassword(LomentDataModel lomentDataModel) {
        int status = 0;
        PreparedStatement insertStmt = null;
        try {
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_LOMENT_DATA
                    + "  SET  " + DatabaseHandler.KEY_PASSWORD + " = " + "?"
                    + "  WHERE " + DatabaseHandler.KEY_LOMENT_ID + " =  " + "'"
                    + encptValue(keyString, lomentDataModel.getLomentId()) + "'";

            insertStmt = connection
                    .prepareStatement(updateQuery);
            insertStmt.setString(1, encptValue(lomentDataModel.getLomentId(), lomentDataModel.getPassword()));
            if (insertStmt.executeUpdate() > 0) {
                status = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (insertStmt != null) {
                try {
                    insertStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBLomentDataMapper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return status;
    }

    public int updateSubscriptionData(LomentDataModel lomentDataModel) {
        int status = 0;
        try {
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_LOMENT_DATA
                    + "  SET  " + DatabaseHandler.KEY_SUBSCRIPTION_TYPE + " = "
                    + "?," + DatabaseHandler.KEY_SUBSCRIPTION_STATUS + " = " + "?,"
                    + DatabaseHandler.KEY_START_DATE + " = " + "?,"
                    + DatabaseHandler.KEY_END_DATE + " = " + "?" + "  WHERE "
                    + DatabaseHandler.KEY_LOMENT_ID + " =  " + "'"
                    + encptValue(keyString, lomentDataModel.getLomentId()) + "'";

            PreparedStatement insertStmt = connection
                    .prepareStatement(updateQuery);
            insertStmt.setString(1, encptValue(lomentDataModel.getLomentId(), lomentDataModel.getSubscriptionType()));
            insertStmt.setString(2, encptValue(lomentDataModel.getLomentId(), lomentDataModel.getSubscriptionStatus()));
            insertStmt.setString(3, encptValue(lomentDataModel.getLomentId(), lomentDataModel.getStartDate()));
            insertStmt.setString(4, encptValue(lomentDataModel.getLomentId(), lomentDataModel.getEndDate()));
            if (insertStmt.executeUpdate() > 0) {
                status = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    
    public int updateUserDetails(LomentDataModel lomentDataModel) {
        int status = 0;
        String lomentId = lomentDataModel.getLomentId();
        try {
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_LOMENT_DATA
                    + "  SET  "+ DatabaseHandler.KEY_MOBILE_NUMBER + " = " + "?,"
                    + DatabaseHandler.KEY_USERNAME + " = " + "?"+ " WHERE "
                    + DatabaseHandler.KEY_LOMENT_ID + " =  " + "'"
                    + encptValue(keyString, lomentId) + "' ";

            PreparedStatement insertStmt = connection
                    .prepareStatement(updateQuery);

            insertStmt.setString(1, encptValue(lomentId, lomentDataModel.getMobileNumber()));
            insertStmt.setString(2, encptValue(lomentId, lomentDataModel.getUsername()));

            if (insertStmt.executeUpdate() > 0) {
                status = 1;
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return status;
    }

    private String encptKey(String value) throws IOException {
        Key key = new MessageKey(value);
        String string = EncoderDecoder.encode(Crypter.encrypt(key,
                value.getBytes()));
        return string;
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

    private String keyString = "lomentaccountkey";
}
