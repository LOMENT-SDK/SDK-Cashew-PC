package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.SettingsModel;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBSettingsMapper {

    private static DBSettingsMapper instance;
    private Connection connection;

    private DBSettingsMapper() throws ClassNotFoundException {
        try {
            connection = DBConnection.getInstance().getConnection(
                    DatabaseHandler.DATABASE_NAME);
            createTableIfNotExists();
        } catch (Exception ex) {
        }
    }

    public static DBSettingsMapper getInstance() {
        if (instance == null) {
            try {
                instance = new DBSettingsMapper();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instance;
    }

//    private void createTableIfNotExists() {
//        try {
//            PreparedStatement pst;
//            pst = connection
//                    .prepareStatement(DatabaseHandler.CREATE_SETTINGS_TABLE);
//            pst.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    private void createTableIfNotExists() {
        try {
            boolean isTableExist = false;
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet rs = dbm.getTables(null, null, DatabaseHandler.TABLE_SETTINGS, null);
            if (rs.next()) {
                isTableExist = true;
            }

            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isTableExist) {
                String strColName = DatabaseHandler.KEY_FONT_FAMILY;
                if (!isThere(strColName)) {
                    try {
                        PreparedStatement pst;
                        String alter = "ALTER TABLE " + DatabaseHandler.TABLE_SETTINGS + " ADD COLUMN " + strColName + " TEXT ; ";
                        pst = connection.prepareStatement(alter);
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                PreparedStatement pst;
                pst = connection
                        .prepareStatement(DatabaseHandler.CREATE_SETTINGS_TABLE);
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private boolean isThere(String column) {
        ResultSet rs = null;
        try {
            rs = getMainSettingData();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount();
            for (int i = 1; i < numberOfColumns + 1; i++) {
                String columnName = rsMetaData.getColumnName(i);
                // Get the name of the column's table name
                if (column.equals(columnName)) {
                    return true;
                }
            }
            //rs.findColumn(column);
            //return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public long insert(SettingsModel settingsModel) {

        DBConnection.getInstance().setLock();
        try {

            PreparedStatement insertStmt = connection.prepareStatement(
                    DatabaseHandler.INSERT_SETTINGS_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, settingsModel.getPriority());
            insertStmt.setInt(2, settingsModel.getRestricted());
            insertStmt.setInt(3, settingsModel.getExpiryMinuts());
            insertStmt.setInt(4, settingsModel.getExpiryStatus());
            insertStmt.setInt(5, settingsModel.getAcknowledge());
            insertStmt.setString(6, settingsModel.getLanguage());
            insertStmt.setInt(7, settingsModel.getHibernationTime());
            insertStmt.setInt(8, settingsModel.getRememberStatus());
            insertStmt.setInt(9, settingsModel.getPlaySound());
            insertStmt.setString(10, settingsModel.getSignature());
            insertStmt.setString(11, settingsModel.getAutoResponseMessage());
            insertStmt.setInt(12, settingsModel.getAutoResponseStatus());
            insertStmt.setString(13, settingsModel.getFontFamily());

            insertStmt.executeUpdate();
            //System.out.println("Data Inserted successfully into Message Table");

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

    public int update(SettingsModel settingsModel) {
        int status = 0;
        String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_SETTINGS
                + "  SET  " + DatabaseHandler.KEY_SETTINGS_PRIORITY + " = "
                + "?," + DatabaseHandler.KEY_SETTINGS_RESTRICTED + " = "
                + "?," + DatabaseHandler.KEY_SETTINGS_EXPIRY_MINUTS + " = "
                + "?," + DatabaseHandler.KEY_SETTINGS_EXPIRY_STATUS + " = "
                + "?," + DatabaseHandler.KEY_SETTINGS_ACK + " = " + "?,"
                + DatabaseHandler.KEY_SETTINGS_LANGUAGE + " = " + "?,"
                + DatabaseHandler.KEY_HIBERNATION_TIME + " = " + "?,"
                + DatabaseHandler.KEY_REMEMBER_ME_STATUS + " = " + "?,"
                + DatabaseHandler.KEY_PLAY_NOTIFICATION + " = " + "?,"
                + DatabaseHandler.KEY_SIGNATURE + " = " + "?,"
                + DatabaseHandler.KEY_AUTORESPONSE_MESSAGE + " = " + "?,"
                + DatabaseHandler.KEY_AUTORESPONSE_STATUS + " = " + "?,"
                + DatabaseHandler.KEY_FONT_FAMILY + " = " + "?";
        PreparedStatement insertStmt = null;

        try {
            insertStmt = connection
                    .prepareStatement(updateQuery);

            insertStmt.setInt(1, settingsModel.getPriority());
            insertStmt.setInt(2, settingsModel.getRestricted());
            insertStmt.setInt(3, settingsModel.getExpiryMinuts());
            insertStmt.setInt(4, settingsModel.getExpiryStatus());
            insertStmt.setInt(5, settingsModel.getAcknowledge());
            insertStmt.setString(6, settingsModel.getLanguage());
            insertStmt.setInt(7, settingsModel.getHibernationTime());
            insertStmt.setInt(8, settingsModel.getRememberStatus());
            insertStmt.setInt(9, settingsModel.getPlaySound());
            insertStmt.setString(10, settingsModel.getSignature());
            insertStmt.setString(11, settingsModel.getAutoResponseMessage());
            insertStmt.setInt(12, settingsModel.getAutoResponseStatus());
            insertStmt.setString(13, settingsModel.getFontFamily());

            if (insertStmt.execute()) {
                status = 1;
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                insertStmt.close();
            } catch (Exception ex) {
                Logger.getLogger(DBSettingsMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

    public ResultSet getMainSettingData() {
        ResultSet cursor = null;
        try {
            String selectQuery = "SELECT  * FROM  "
                    + DatabaseHandler.TABLE_SETTINGS;
            cursor = connection.createStatement().executeQuery(selectQuery);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
