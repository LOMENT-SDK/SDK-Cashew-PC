package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;

public class DBMessageSyncDataMapper {

	private static DBMessageSyncDataMapper instance;
	private Connection connection;

	private DBMessageSyncDataMapper() throws ClassNotFoundException {
		try {
			connection = DBConnection.getInstance().getConnection(
					DatabaseHandler.DATABASE_NAME);
			createTableIfNotExists();
		} catch (Exception ex) {
		}
	}

	public static DBMessageSyncDataMapper getInstance() {
		if (instance == null) {
			try {
				instance = new DBMessageSyncDataMapper();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return instance;
	}

	private void createTableIfNotExists() {
		try {
			boolean isTableExist = false;
			DatabaseMetaData dbm = connection.getMetaData();
			ResultSet rs = dbm.getTables(null, null,
					DatabaseHandler.TABLE_MSG_SYNC_DETAILS, null);
			if (rs.next()) {
				isTableExist = true;
			}

			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!isTableExist) {

				PreparedStatement pst;
				pst = connection
						.prepareStatement(DatabaseHandler.CREATE_LAST_MESSAGE_SYNC_TIME_TABLE);
				pst.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private long insert(String lastMessageSyncTime) {

		DBConnection.getInstance().setLock();
		try {

			PreparedStatement insertStmt = connection.prepareStatement(
					DatabaseHandler.INSERT_LAST_MESSAGE_SYNC_TIME_DATA_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			insertStmt.setString(1, lastMessageSyncTime);

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

	private int update(String lastMessageSyncTime) {
		int status = 0;
		String updateQuery = "UPDATE  "
				+ DatabaseHandler.TABLE_MSG_SYNC_DETAILS + "  SET  "
				+ DatabaseHandler.KEY_LAST_MESSAGE_SYNC_TIME + " = " + "?";
		PreparedStatement insertStmt = null;

		try {
			insertStmt = connection.prepareStatement(updateQuery);
			insertStmt.setString(1, lastMessageSyncTime);

			if (insertStmt.execute()) {
				status = 1;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				insertStmt.close();
			} catch (Exception ex) {
				Logger.getLogger(DBMessageSyncDataMapper.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		return status;
	}

	private ResultSet getMainSettingData() {
		ResultSet cursor = null;
		try {
			String selectQuery = "SELECT  * FROM  "
					+ DatabaseHandler.TABLE_MSG_SYNC_DETAILS;
			cursor = connection.createStatement().executeQuery(selectQuery);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getLastMessageSyncTime() {
		ResultSet cursor = null;
		String val = null;
		try {
			cursor = getMainSettingData();
			if (cursor != null && cursor.next()) {
				 val =  cursor.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cursor.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return val;
	}

	public void saveLastMessageSyncTime(String lastMessageSyncTime) {
		boolean isSave = false;

		try {
			ResultSet cursor = getMainSettingData();
			if (cursor == null) {
				isSave = true;
			}
			if (!cursor.next()) {
				isSave = true;
				cursor.close();
			}
		} catch (Exception e) {
			isSave = true;
			e.printStackTrace();
		}
		if (isSave) {
			insert(lastMessageSyncTime);
		} else {
			update(lastMessageSyncTime);
		}
	}
}
