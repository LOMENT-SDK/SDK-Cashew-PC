package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.GroupModel;

public class DBGroupsType6Mapper {

	private static DBGroupsType6Mapper instance;
	private Connection connection;

	private DBGroupsType6Mapper() throws ClassNotFoundException {
		try {
			connection = DBConnection.getInstance().getConnection(
					DatabaseHandler.DATABASE_NAME);
			createTableIfNotExists();
		} catch (Exception ex) {
		}
	}

	public static DBGroupsType6Mapper getInstance() {
		if (instance == null) {
			try {
				instance = new DBGroupsType6Mapper();
			} catch (Exception ex) {
			}
		}
		return instance;
	}

	// private void createTableIfNotExists() {
	// PreparedStatement pst;
	// try {
	// pst = connection
	// .prepareStatement(DatabaseHandler.CREATE_TABLE_GROUPS);
	// pst.execute();
	// } catch (SQLException e) {
	// }
	// }

	private void createTableIfNotExists() {
		try {
			boolean isTableExist = false;
			DatabaseMetaData dbm = connection.getMetaData();
			ResultSet rs = dbm.getTables(null, null,
					DatabaseHandler.TABLE_GROUPS, null);
			if (rs.next()) {
				isTableExist = true;
			}

			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (isTableExist) {
				String strGroupAdmin = DatabaseHandler.KEY_GROUP_ADMINS;
				String strGroupFeatures = DatabaseHandler.KEY_GROUP_FEATURES;
				String strGroupExtras = DatabaseHandler.KEY_GROUP_EXTRAS;

				if (!isThere(strGroupAdmin)) {
					try {
						PreparedStatement pst;
						String alter = "ALTER TABLE "
								+ DatabaseHandler.TABLE_GROUPS + " ADD COLUMN "
								+ strGroupAdmin + " TEXT ; ";
						pst = connection.prepareStatement(alter);
						pst.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (!isThere(strGroupFeatures)) {
					try {
						PreparedStatement pst;
						String alter = "ALTER TABLE "
								+ DatabaseHandler.TABLE_GROUPS + " ADD COLUMN "
								+ strGroupFeatures + " TEXT ; ";
						pst = connection.prepareStatement(alter);
						pst.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (!isThere(strGroupExtras)) {
					try {
						PreparedStatement pst;
						String alter = "ALTER TABLE "
								+ DatabaseHandler.TABLE_GROUPS + " ADD COLUMN "
								+ strGroupExtras + " TEXT ; ";
						pst = connection.prepareStatement(alter);
						pst.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} else {
				PreparedStatement pst;
				pst = connection
						.prepareStatement(DatabaseHandler.CREATE_TABLE_GROUPS);
				pst.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean isThere(String column) {
		ResultSet rs = null;
		try {
			rs = getGroupData();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			for (int i = 1; i < numberOfColumns + 1; i++) {
				String columnName = rsMetaData.getColumnName(i);
				// Get the name of the column's table name
				if (column.equals(columnName)) {
					return true;
				}
			}
			// rs.findColumn(column);
			// return true;
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

	public long insert(GroupModel groupModel) {

		try {
			JSONArray groupMembers = new JSONArray();
			if (groupModel.getMembers() != null) {
				groupMembers = groupModel.getMembers();
			}

			JSONArray groupAdmins = new JSONArray();
			if (groupModel.getAdmins() != null) {
				groupAdmins = groupModel.getAdmins();
			}
			String groupAdminsString = groupAdmins.toString();

			JSONArray groupFeatures = new JSONArray();
			if (groupModel.getFeature() != null) {
				groupFeatures = groupModel.getFeature();
			}
			String groupgroupFeaturesString = groupFeatures.toString();

			createTableIfNotExists();
			PreparedStatement insertStmt = connection.prepareStatement(
					DatabaseHandler.INSERT_GROUP_DATA_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			insertStmt.setString(2, groupModel.getServerGroupId());
			insertStmt.setString(3, groupModel.getGroupName());
			insertStmt.setString(4, groupModel.getOwner());
			insertStmt.setString(6, groupMembers.toString());
			insertStmt.setString(7, groupAdminsString);
			insertStmt.setString(8, groupgroupFeaturesString);

			insertStmt.executeUpdate();

			ResultSet rs = insertStmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
		} finally {
			DBConnection.getInstance().closeLock();
		}
		return -1;
	}

	public int updateGroup(GroupModel groupModel, boolean isServerId) {

		int status = 0;
		String updateQuery = "";
		try {
			if (isServerId) {

				updateQuery = "UPDATE  " + DatabaseHandler.TABLE_GROUPS
						+ "  SET  " + DatabaseHandler.KEY_SERVER_GROUP_ID
						+ " = " + "?," + DatabaseHandler.KEY_GROUP_NAME + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_CREATED_BY + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_MEMBERS + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_ADMINS + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_FEATURES + " = "
						+ "?" + "  WHERE "
						+ DatabaseHandler.KEY_SERVER_GROUP_ID + " =  '"
						+ groupModel.getServerGroupId() + "'";
			} else {
				updateQuery = "UPDATE  " + DatabaseHandler.TABLE_GROUPS
						+ "  SET  " + DatabaseHandler.KEY_SERVER_GROUP_ID
						+ " = " + "?," + DatabaseHandler.KEY_GROUP_NAME + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_CREATED_BY + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_MEMBERS + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_ADMINS + " = "
						+ "?," + DatabaseHandler.KEY_GROUP_FEATURES + " = "
						+ "?" + "  WHERE " + DatabaseHandler.KEY_GROUP_ID
						+ " =  '" + groupModel.getGroupId() + "'";
			}

			PreparedStatement insertStmt = connection
					.prepareStatement(updateQuery);
			String members = "";
			String admins = "";
			String features = "";
			if (groupModel.getMembers() != null) {
				members = groupModel.getMembers().toString();
			}

			if (groupModel.getAdmins() != null) {
				admins = groupModel.getAdmins().toString();
			}

			if (groupModel.getFeature() != null) {
				features = groupModel.getFeature().toString();
			}

			insertStmt.setString(1, groupModel.getServerGroupId());
			insertStmt.setString(2, groupModel.getGroupName());
			insertStmt.setString(3, groupModel.getOwner());
			insertStmt.setString(4, members);
			insertStmt.setString(5, admins);
			insertStmt.setString(6, features);

			if (insertStmt.execute()) {
				status = 1;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return status;
	}

	public GroupModel getGroup(String groupID, boolean isServerId) {
		GroupModel groupModel = new GroupModel();
		ResultSet cursor = null;

		try {
			String selectQuery = "SELECT  * FROM  "
					+ DatabaseHandler.TABLE_GROUPS + " where ";

			if (isServerId) {
				selectQuery += DatabaseHandler.KEY_SERVER_GROUP_ID + "='"
						+ groupID + "'";
			} else {
				selectQuery += DatabaseHandler.KEY_GROUP_ID + "='" + groupID
						+ "'";
			}

			cursor = connection.createStatement().executeQuery(selectQuery);
			if (cursor.next()) {

				do {
					groupModel.setGroupId(cursor.getInt(1));
					groupModel.setServerGroupId(cursor.getString(2));
					groupModel.setGroupName(cursor.getString(3));
					groupModel.setOwner(cursor.getString(4));
					// 5 type
					JSONArray members = new JSONArray(cursor.getString(6) + "");
					groupModel.setMembers(members);

					String adminString = "";
					String featuresString = "";

					adminString = cursor.getString(7) + "";
					featuresString = cursor.getString(8) + "";

					if (adminString != null && !adminString.equals("")
							&& !adminString.equalsIgnoreCase("null")) {
						JSONArray admins = new JSONArray(adminString);
						groupModel.setAdmins(admins);
					}
					if (featuresString != null && !featuresString.equals("")
							&& !featuresString.equalsIgnoreCase("null")) {
						JSONArray features = new JSONArray(featuresString);
						groupModel.setFeature(features);
					}
					groupModel.setCreationTime(cursor.getInt(8));
					groupModel.setLastUpdateTime(cursor.getInt(9));
				} while (cursor.next());
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (SQLException ex) {
				}
			}
		}
		return groupModel;
	}

	public ResultSet getGroupData() {
		ResultSet cursor = null;
		try {
			String selectQuery = "SELECT  * FROM  "
					+ DatabaseHandler.TABLE_GROUPS;
			cursor = connection.createStatement().executeQuery(selectQuery);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
