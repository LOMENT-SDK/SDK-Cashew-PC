package com.loment.cashewnut.database.mappers;

import com.loment.cashewnut.database.ProfileStore;
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
import com.loment.cashewnut.model.AccountsModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBAccountsMapper {

    private static DBAccountsMapper instance;
    private Connection connection;

    private DBAccountsMapper() throws ClassNotFoundException {
        try {
            connection = DBConnection.getInstance().getConnection(
                    DatabaseHandler.DATABASE_NAME);

            createTableIfNotExists();
        } catch (Exception ex) {
        }
    }

    public static DBAccountsMapper getInstance() {
        if (instance == null) {
            try {
                instance = new DBAccountsMapper();
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
                    .prepareStatement(DatabaseHandler.CREATE_ACCOUNTS_TABLE);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long insert(AccountsModel accountsModel) {

        DBConnection.getInstance().setLock();
        try {

            String lomentId = ProfileStore.getInstance().getLomentId();

            createTableIfNotExists();
            PreparedStatement insertStmt = connection.prepareStatement(
                    DatabaseHandler.INSERT_ACCOUNT_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);

            insertStmt.setString(1, encptValue(lomentId, accountsModel.getCashewnutId()));
            insertStmt.setString(2, encptValue(lomentId, accountsModel.getCreationDate()));
            insertStmt.setString(3, encptValue(lomentId, accountsModel.getStatus()));
            insertStmt.setLong(4, accountsModel.getStartMsgId());
            insertStmt.setLong(5, accountsModel.getLastMsgId());
            insertStmt.setString(6, encptValue(lomentId, accountsModel.getReceivedMsgQueue()));

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

    public AccountsModel getAccount() {
        ResultSet cursor = null;
        String selectQuery = "SELECT  * FROM  "
                + DatabaseHandler.TABLE_ACCOUNTS;
        String lomentId = ProfileStore.getInstance().getLomentId();
        try {
            cursor = connection.createStatement().executeQuery(selectQuery);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        AccountsModel accountsModel = new AccountsModel();
        try {
            if (cursor.next()) {
                do {
                    accountsModel.setCashewnutId(decptValue(lomentId, cursor.getString(1)));
                    accountsModel.setCreationDate(decptValue(lomentId, cursor.getString(2)));
                    accountsModel.setStatus(decptValue(lomentId, cursor.getString(3)));
                    accountsModel.setStartMsgId(cursor.getLong(4));
                    accountsModel.setLastMsgId(cursor.getLong(5));
                    accountsModel.setReceivedMsgQueue(decptValue(lomentId, cursor.getString(6)));

                } while (cursor.next());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountsModel;
    }

    public int updateAccount(AccountsModel accountsModel) {

        int status = 0;
        try {
            String lomentId = ProfileStore.getInstance().getLomentId();
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_ACCOUNTS
                    + "  SET  " + DatabaseHandler.KEY_CASHEWNUT_ID + " = " + "?,"
                    + DatabaseHandler.KEY_CREATATION_DATE + " = " + "?,"
                    + DatabaseHandler.KEY_STATUS + " = " + "?,"
                    + DatabaseHandler.KEY_START_MSG_ID + " = " + "?,"
                    + DatabaseHandler.KEY_LAST_MSG_ID + " = " + "?" + "  WHERE "
                    + DatabaseHandler.KEY_CASHEWNUT_ID + " =  " + "'"
                    + encptValue(lomentId, accountsModel.getCashewnutId()) + "'" + " ";

            PreparedStatement insertStmt = connection
                    .prepareStatement(updateQuery);

            insertStmt.setString(1, encptValue(lomentId, accountsModel.getCashewnutId()));
            insertStmt.setString(2, encptValue(lomentId, accountsModel.getCreationDate()));
            insertStmt.setString(3, encptValue(lomentId, accountsModel.getStatus()));
            insertStmt.setLong(4, accountsModel.getStartMsgId());
            insertStmt.setLong(5, accountsModel.getLastMsgId());
            
            if (insertStmt.execute()) {
                status = 1;
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return status;
    }

    public synchronized int updateReceivedMsgQueue(String CashewId, String queue) {

        int status = 0;
        PreparedStatement insertStmt = null;
        try {
            String lomentId = ProfileStore.getInstance().getLomentId();
            String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_ACCOUNTS
                    + "  SET  " + DatabaseHandler.KEY_RECEIVED_MESSAGE_QUEUE
                    + " = " + "?" + "  WHERE " + DatabaseHandler.KEY_CASHEWNUT_ID
                    + " =  " + "'" + encptValue(lomentId, CashewId) + "'" + " ";

            insertStmt = connection
                    .prepareStatement(updateQuery);

            insertStmt.setString(1, encptValue(lomentId, queue));

            if (insertStmt.executeUpdate() > 0) {
                status = 1;
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
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
