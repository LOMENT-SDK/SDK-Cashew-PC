package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;

public class DBHeaderMapper {
	private static DBHeaderMapper instance;
	private Connection connection;

	private DBHeaderMapper() throws ClassNotFoundException {
		try {
			connection = DBConnection.getInstance().getConnection(DatabaseHandler.DATABASE_NAME);
			createTableIfNotExists();
		} catch (Exception ex) {
		}
	}

	public static DBHeaderMapper getInstance() {
		if (instance == null) {
			try {
				instance = new DBHeaderMapper();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return instance;
	}

	private void createTableIfNotExists() {

		try {
			PreparedStatement pst;
			pst = connection.prepareStatement(DatabaseHandler.CREATE_TABLE_MESSAGE);
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (DatabaseHandler.isSetIndex) {
			try {
				String index = "CREATE  INDEX IF NOT EXISTS 'server_message_id_index' ON '"
						+ DatabaseHandler.TABLE_MESSAGE + "' ('" + DatabaseHandler.KEY_SERVER_MSGID + "');";

				PreparedStatement pstIndex = connection.prepareStatement(index);
				pstIndex.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				String index = "CREATE  INDEX IF NOT EXISTS 'thread_id_index' ON '" + DatabaseHandler.TABLE_MESSAGE
						+ "' ('" + DatabaseHandler.KEY_GROUP_ID + "');";

				PreparedStatement pstIndex = connection.prepareStatement(index);
				pstIndex.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized long insert(HeaderModel messageModel) {
		ResultSet rs = null;
		try {
			// long lastMessageTime = System.currentTimeMillis();
			// createTableIfNotExists();
			PreparedStatement insertStmt = connection.prepareStatement(DatabaseHandler.INSERT_HEADER_DATA_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			// connection.setAutoCommit(false);
			insertStmt.setInt(2, messageModel.getServerMessageId());
			insertStmt.setString(3, messageModel.getMessageFrom());
			insertStmt.setInt(4, messageModel.getMessageType());
			insertStmt.setString(5, messageModel.getMessageFolderType());
			insertStmt.setInt(6, messageModel.getPriority());
			insertStmt.setInt(7, messageModel.getRestricted());
			insertStmt.setInt(8, messageModel.getMessageAck());
			insertStmt.setInt(9, messageModel.getExpiry());
			insertStmt.setLong(10, messageModel.getExpiryStartTime());
			insertStmt.setInt(11, messageModel.getScheduleStatus());
			insertStmt.setLong(12, messageModel.getScheduleTime());
			insertStmt.setString(13, messageModel.getSubject());
			insertStmt.setLong(14, messageModel.getCreationTime());
			insertStmt.setLong(15, messageModel.getLastUpdateTime());
			insertStmt.setInt(16, messageModel.getSendParts());
			insertStmt.setInt(17, messageModel.getNumberOfBodyparts());
			insertStmt.setInt(18, messageModel.getDeleteStatus());
			insertStmt.setString(19, messageModel.getHeaderVersion());
			insertStmt.setInt(20, 1);
			insertStmt.setString(21, messageModel.getThreadId());
			insertStmt.setString(22, messageModel.getGroupId());

			// insertStmt.addBatch();
			// int[] affectedRecords = insertStmt.executeBatch();
			// connection.commit();
			// connection.setAutoCommit(true);
			insertStmt.execute();

			rs = insertStmt.getGeneratedKeys();
			if (rs.next()) {
				// System.out.println(System.currentTimeMillis() -
				// lastMessageTime);
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
			}
		}
		return -1;
	}

	public ResultSet getMessageHeaders() throws SQLException {
		String selectQuery = "SELECT  * FROM  " + DatabaseHandler.TABLE_MESSAGE + " ORDER BY "
				+ DatabaseHandler.KEY_LOCAL_HEADER_ID;
		ResultSet rs = connection.createStatement().executeQuery(selectQuery);
		return rs;
	}

	public ResultSet getLatestMessageHeadersByGroupId() throws SQLException {

		String messages = "SELECT m1.* FROM " + DatabaseHandler.TABLE_MESSAGE + " m1 JOIN (SELECT "
				+ DatabaseHandler.KEY_GROUP_ID + ", MAX(" + DatabaseHandler.KEY_CREATION_TIME + ") "
				+ DatabaseHandler.KEY_CREATION_TIME + " FROM " + DatabaseHandler.TABLE_MESSAGE + " GROUP BY "
				+ DatabaseHandler.KEY_GROUP_ID + ")" + " m2 ON m1." + DatabaseHandler.KEY_GROUP_ID + "= m2."
				+ DatabaseHandler.KEY_GROUP_ID + " AND " + "m1." + DatabaseHandler.KEY_CREATION_TIME + "= m2."
				+ DatabaseHandler.KEY_CREATION_TIME + " ORDER BY " + DatabaseHandler.KEY_CREATION_TIME + " ASC";

		ResultSet rs = connection.createStatement().executeQuery(messages);
		return rs;
	}

	public List<HeaderModel> getLatestMessageHeadersByGroupId(String threadId) {
		String selectQuery = "SELECT m1.* FROM " + DatabaseHandler.TABLE_MESSAGE + " m1 JOIN (SELECT "
				+ DatabaseHandler.KEY_GROUP_ID + ", MAX(" + DatabaseHandler.KEY_CREATION_TIME + ") "
				+ DatabaseHandler.KEY_CREATION_TIME + " FROM " + DatabaseHandler.TABLE_MESSAGE + " WHERE "
				+ DatabaseHandler.KEY_GROUP_ID + "= '" + threadId + "'" + ")" + " m2 ON m1."
				+ DatabaseHandler.KEY_GROUP_ID + "= m2." + DatabaseHandler.KEY_GROUP_ID + " AND " + "m1."
				+ DatabaseHandler.KEY_CREATION_TIME + "= m2." + DatabaseHandler.KEY_CREATION_TIME;

		List<HeaderModel> headerModel = getMessageHeaders(selectQuery);
		return headerModel;
	}

	public int getUnreadCountByGroupId(String cashewnutId, String threadId) {
		try {
			String messageIds = "select " + DatabaseHandler.KEY_LOCAL_HEADER_ID + " from "
					+ DatabaseHandler.TABLE_MESSAGE + " where "+DatabaseHandler.KEY_MSG_TYPE+"=1 and " + DatabaseHandler.KEY_GROUP_ID + " = '" + threadId
					+ "'  and " + DatabaseHandler.KEY_FROM + " NOT LIKE '" + cashewnutId + "'";
			String recipintIds = "select COUNT(" + DatabaseHandler.KEY_LOCAL_HEADER_ID + ") from "
					+ DatabaseHandler.TABLE_RECIPIENT + " where (" + DatabaseHandler.KEY_RECIPIENT_CASHEWNUT_ID + "= '"
					+ cashewnutId + "' and " + DatabaseHandler.KEY_REC_READ_STATUS + "= 0) and "
					+ DatabaseHandler.KEY_LOCAL_HEADER_ID + " in(" + messageIds + ")";

			int count = 0;
			ResultSet rs = connection.createStatement().executeQuery(recipintIds);
			if (rs != null && rs.next()) {
				do {
					count = rs.getInt(1);
				
					// System.out.print(count);
				} while (rs.next());
				rs.close();
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public ResultSet getConversationViewCursor(String threadId) {
		ResultSet cursor = null;

		String selectQuery = "";
		try {
			if (DatabaseHandler.isSetIndex) {
				selectQuery = "select  * from " + DatabaseHandler.TABLE_MESSAGE + " INDEXED BY thread_id_index where "
						+ DatabaseHandler.KEY_GROUP_ID + "= '" + threadId + "'";
			} else {
				selectQuery = "select  * from " + DatabaseHandler.TABLE_MESSAGE + " where "
						+ DatabaseHandler.KEY_GROUP_ID + "= '" + threadId + "'";

			}
			cursor = connection.createStatement().executeQuery(selectQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	// public List<HeaderModel> getConversationViewHeaders(String threadId) {
	// List<HeaderModel> headerList = new ArrayList<HeaderModel>();
	// try {
	// String selectQuery = "select * from "
	// + DatabaseHandler.TABLE_MESSAGE + " where "
	// + DatabaseHandler.KEY_GROUP_ID + "= '" + threadId + "'";
	// headerList = getMessageHeaders(selectQuery);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return headerList;
	// }

	public ResultSet getExpiryMessageHeaders(String cashewnutId) {
		ResultSet cursor = null;
		try {
			String selectQuery = "select  * from " + DatabaseHandler.TABLE_MESSAGE + " where "
					+ DatabaseHandler.KEY_EXPIRY_TIME + " > -1 and " + DatabaseHandler.KEY_EXPIRY_MINUTS + " > -1 and "
					+ DatabaseHandler.KEY_FROM + "  NOT LIKE '" + cashewnutId + "'";
			cursor = connection.createStatement().executeQuery(selectQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	private List<HeaderModel> getMessageHeaders(String selectQuery) {
		List<HeaderModel> headerList = new ArrayList<HeaderModel>();
		ResultSet cursor = null;
		try {

			cursor = connection.createStatement().executeQuery(selectQuery);
			if (cursor != null && cursor.next()) {
				do {
					HeaderModel header = new HeaderModel();
					header.setLocalHeaderId(cursor.getInt(1));
					header.setServerMessageId(cursor.getInt(2));
					header.setMessageFrom(cursor.getString(3));
					header.setMessageType(cursor.getInt(4));
					header.setMessageFolderType(cursor.getString(5));
					header.setPriority(cursor.getInt(6));
					header.setRestricted(cursor.getInt(7));
					header.setMessageAck(cursor.getInt(8));
					header.setExpiry(cursor.getInt(9));
					header.setExpiryStartTime(cursor.getLong(10));
					header.setScheduleStatus(cursor.getInt(11));
					header.setScheduleTime(cursor.getLong(12));

					header.setSubject(cursor.getString(13));
					header.setCreationTime(cursor.getLong(14));
					header.setLastUpdateTime(cursor.getLong(15));
					header.setSendParts(cursor.getInt(16));
					header.setNumberOfBodyparts(cursor.getInt(17));
					header.setDeleteStatus(cursor.getInt(18));
					header.setHeaderVersion(cursor.getString(19));
					header.setThreadId(cursor.getString(21));
					header.setGroupId(cursor.getString(22));

					headerList.add(header);
				} while (cursor.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return headerList;
	}

	public List<HeaderModel> getMessageHeaderList() {
		String selectQuery = "SELECT  * FROM  " + DatabaseHandler.TABLE_MESSAGE;
		return getMessageHeaders(selectQuery);
	}

	public List<HeaderModel> getScheduleMessageHeaderList() {
		String selectQuery = "SELECT  * FROM  " + DatabaseHandler.TABLE_MESSAGE + " where "
				+ DatabaseHandler.KEY_SCHEDULE_TIME + ">0";
		return getMessageHeaders(selectQuery);
	}

	public long getMaxServerMessageId(String cashewnutID) {
		try {

			String selectQuery = "SELECT  * FROM  " + DatabaseHandler.TABLE_MESSAGE + "  where "
					+ DatabaseHandler.KEY_SERVER_MSGID + "  =  (SELECT MAX(" + DatabaseHandler.KEY_SERVER_MSGID
					+ ") FROM " + DatabaseHandler.TABLE_MESSAGE + "  where " + DatabaseHandler.KEY_FROM + " != '"
					+ cashewnutID + "' )";

			List<HeaderModel> headerList = getMessageHeaders(selectQuery);
			if (headerList != null && headerList.size() > 0) {
				return headerList.get(0).getServerMessageId();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public HeaderModel getMessageHeaderById(int id, boolean isLocalID) {
		String MakeSelect = "";
		if (!isLocalID) {
			String idString = DatabaseHandler.KEY_SERVER_MSGID;
			if (DatabaseHandler.isSetIndex) {
				MakeSelect = "select * from  " + DatabaseHandler.TABLE_MESSAGE
						+ "  INDEXED  BY server_message_id_index where " + idString + "  =  " + id + "";

			} else {
				MakeSelect = "select * from  " + DatabaseHandler.TABLE_MESSAGE + "  where " + idString + "  =  " + id
						+ "";
			}
		} else {
			String idString = DatabaseHandler.KEY_LOCAL_HEADER_ID;
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_MESSAGE + "  where " + idString + "  =  " + id + "";
		}
		List<HeaderModel> headerModel = getMessageHeaders(MakeSelect);
		if (headerModel.size() > 0) {
			return headerModel.get(0);
		}
		return null;
	}

	public HeaderModel getMessageHeaderById(int id) {

		String MakeSelect = "select * from  " + DatabaseHandler.TABLE_MESSAGE + "  where "
				+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "  =  " + id + "";

		ResultSet cursor = null;
		try {

			cursor = connection.createStatement().executeQuery(MakeSelect);
			HeaderModel header = new HeaderModel();
			if (cursor != null && cursor.next()) {
				do {

					header.setLocalHeaderId(cursor.getInt(1));
					header.setServerMessageId(cursor.getInt(2));
					header.setMessageFrom(cursor.getString(3));
					header.setMessageType(cursor.getInt(4));
					header.setMessageFolderType(cursor.getString(5));
					header.setPriority(cursor.getInt(6));
					header.setRestricted(cursor.getInt(7));
					header.setMessageAck(cursor.getInt(8));
					header.setExpiry(cursor.getInt(9));
					header.setExpiryStartTime(cursor.getLong(10));
					header.setScheduleStatus(cursor.getInt(11));
					header.setScheduleTime(cursor.getLong(12));

					header.setSubject(cursor.getString(13));
					header.setCreationTime(cursor.getLong(14));
					header.setLastUpdateTime(cursor.getLong(15));
					header.setSendParts(cursor.getInt(16));
					header.setNumberOfBodyparts(cursor.getInt(17));
					header.setDeleteStatus(cursor.getInt(18));
					header.setHeaderVersion(cursor.getString(19));
					header.setThreadId(cursor.getString(21));
					header.setGroupId(cursor.getString(22));

				} while (cursor.next());
				return header;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public ResultSet getCursorById(int id) {
		String MakeSelect = "select * from  " + DatabaseHandler.TABLE_MESSAGE + "  where "
				+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "  =  " + id + "";

		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(MakeSelect);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cursor;
	}

	public boolean isMessageExists(int id, boolean isLocalID) {
		HeaderModel headerModel = getMessageHeaderById(id, isLocalID);
		return headerModel != null;
	}

	public synchronized int update(HeaderModel messageModel, int id) {
		int status = 0;

		String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_MESSAGE + "  SET  " + DatabaseHandler.KEY_SERVER_MSGID
				+ " = " + "?," + DatabaseHandler.KEY_FROM + " = " + "?," + DatabaseHandler.KEY_MSG_TYPE + " = " + "?,"
				+ DatabaseHandler.KEY_MSG_FOLDER + " = " + "?," + DatabaseHandler.KEY_PRIORITY + " = " + "?,"
				+ DatabaseHandler.KEY_RESTRICTED + " = " + "?," + DatabaseHandler.KEY_MSG_ACK + " = " + "?,"
				+ DatabaseHandler.KEY_EXPIRY_MINUTS + " = " + "?," + DatabaseHandler.KEY_EXPIRY_TIME + " = " + "?,"
				+ DatabaseHandler.KEY_SCHEDULE_STATUS + " = " + "?," + DatabaseHandler.KEY_SCHEDULE_TIME + " = " + "?,"
				+ DatabaseHandler.KEY_SUBJECT + " = " + "?," + DatabaseHandler.KEY_CREATION_TIME + " = " + "?,"
				+ DatabaseHandler.KEY_LAST_UPDATE_TIME + " = " + "?," + DatabaseHandler.KEY_SEND_STATUS + " = " + "?,"
				+ DatabaseHandler.KEY_NUMBER_OF_BODYPARTS + " = " + "?," + DatabaseHandler.KEY_DELETE_STATUS + " = "
				+ "?," + DatabaseHandler.KEY_GROUP_ID + " = " + "?," + DatabaseHandler.KEY_GROUP_NAME + " = " + "?"
				+ "  WHERE " + DatabaseHandler.KEY_LOCAL_HEADER_ID + " =  " + id + " ";

		try {
			PreparedStatement insertStmt = connection.prepareStatement(updateQuery);

			insertStmt.setInt(1, messageModel.getServerMessageId());
			insertStmt.setString(2, messageModel.getMessageFrom());
			insertStmt.setInt(3, messageModel.getMessageType());
			insertStmt.setString(4, messageModel.getMessageFolderType());
			insertStmt.setInt(5, messageModel.getPriority());
			insertStmt.setInt(6, messageModel.getRestricted());
			insertStmt.setInt(7, messageModel.getMessageAck());
			insertStmt.setInt(8, messageModel.getExpiry());
			insertStmt.setLong(9, messageModel.getExpiryStartTime());
			insertStmt.setInt(10, messageModel.getScheduleStatus());
			insertStmt.setLong(11, messageModel.getScheduleTime());
			insertStmt.setString(12, messageModel.getSubject());
			insertStmt.setLong(13, messageModel.getCreationTime());
			insertStmt.setLong(14, messageModel.getLastUpdateTime());
			insertStmt.setInt(15, messageModel.getSendParts());
			insertStmt.setInt(16, messageModel.getNumberOfBodyparts());
			insertStmt.setInt(17, messageModel.getDeleteStatus());
			insertStmt.setString(18, messageModel.getThreadId());
			insertStmt.setString(19, messageModel.getGroupId());

			if (insertStmt.execute()) {
				status = 1;
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return status;
	}

	public int update(HeaderModel messageModel) {

		int status = 0;

		String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_MESSAGE + "  SET  " + DatabaseHandler.KEY_SERVER_MSGID
				+ " = " + "?," + DatabaseHandler.KEY_FROM + " = " + "?," + DatabaseHandler.KEY_MSG_TYPE + " = " + "?,"
				+ DatabaseHandler.KEY_PRIORITY + " = " + "?," + DatabaseHandler.KEY_SUBJECT + " = " + "?,"
				+ DatabaseHandler.KEY_CREATION_TIME + " = " + "?," + DatabaseHandler.KEY_LAST_UPDATE_TIME + " = " + "?,"
				+ DatabaseHandler.KEY_SEND_STATUS + " = " + "?," + DatabaseHandler.KEY_DELETE_STATUS + " = " + "?,"
				+ DatabaseHandler.KEY_GROUP_ID + " = " + "?," + DatabaseHandler.KEY_GROUP_NAME + " = " + "?"
				+ "  WHERE " + DatabaseHandler.KEY_LOCAL_HEADER_ID + " =  '" + messageModel.getServerMessageId() + "'";

		try {

			PreparedStatement insertStmt = connection.prepareStatement(updateQuery);

			insertStmt.setInt(1, messageModel.getServerMessageId());
			insertStmt.setString(2, messageModel.getMessageFrom());
			insertStmt.setInt(3, messageModel.getMessageType());
			insertStmt.setInt(4, messageModel.getPriority());
			insertStmt.setString(5, messageModel.getSubject());
			insertStmt.setLong(6, messageModel.getCreationTime());
			insertStmt.setLong(7, messageModel.getLastUpdateTime());
			insertStmt.setInt(8, messageModel.getSendParts());
			insertStmt.setInt(9, messageModel.getDeleteStatus());
			insertStmt.setString(10, messageModel.getThreadId());
			insertStmt.setString(11, messageModel.getGroupId());

			if (insertStmt.execute()) {
				status = 1;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return status;
	}

	public int updateForGroupId(String groupId, int id) {
		int status = 0;
		String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_MESSAGE + "  SET  " + DatabaseHandler.KEY_GROUP_ID
				+ " = " + "?" + "  WHERE " + DatabaseHandler.KEY_LOCAL_HEADER_ID + " =  " + id + " ";
		try {
			PreparedStatement insertStmt = connection.prepareStatement(updateQuery);
			insertStmt.setString(1, groupId);
			if (insertStmt.execute()) {
				status = 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	public int deleteByHeaderId(int msgId) {

		String deleteMsgQuery = " DELETE FROM " + DatabaseHandler.TABLE_MESSAGE + "  WHERE "
				+ DatabaseHandler.KEY_LOCAL_HEADER_ID + " = " + msgId + " ";

		try {
			if (connection.createStatement().execute(deleteMsgQuery)) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// schedule message update
	public synchronized int updateScheduleMessages(HeaderModel messageModel, int id) {
		int status = 0;
		String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_MESSAGE + "  SET  " + DatabaseHandler.KEY_CREATION_TIME
				+ " = " + "?" + "  WHERE " + DatabaseHandler.KEY_LOCAL_HEADER_ID + " =  " + id + " ";

		try {
			PreparedStatement insertStmt = connection.prepareStatement(updateQuery);
			insertStmt.setLong(1, messageModel.getCreationTime());
			if (insertStmt.execute()) {
				status = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}

	// public int deleteAll() {
	// return wdb.delete(DatabaseHandler1.TABLE_MESSAGE, null, null);
	// }
}
