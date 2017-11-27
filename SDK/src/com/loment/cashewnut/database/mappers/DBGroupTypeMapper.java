package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.loment.cashewnut.database.ProfileStore;
import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupTypeModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.model.SettingsModel;

public class DBGroupTypeMapper {

	private static DBGroupTypeMapper instance;
	private Connection connection;

	private DBGroupTypeMapper() throws ClassNotFoundException {
		try {
			connection = DBConnection.getInstance().getConnection(DatabaseHandler.DATABASE_NAME);
			createTableIfNotExists();
		} catch (Exception ex) {
		}
	}

	public static DBGroupTypeMapper getInstance() {
		if (instance == null) {
			try {
				instance = new DBGroupTypeMapper();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return instance;
	}

	private void createTableIfNotExists() {
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(DatabaseHandler.CREATE_TABLE_GROUP_TYPE);
//			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean insert(GroupTypeModel groupTypeModel) {

		DBConnection.getInstance().setLock();
		try {

			PreparedStatement insertStmt = connection.prepareStatement(DatabaseHandler.INSERT_GROUP_TYPE_QUERY,
					Statement.RETURN_GENERATED_KEYS);
//			insertStmt.setBoolean(1, groupTypeModel.isGroupType());
//			insertStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.getInstance().closeLock();
		}
		return true;
	}
}