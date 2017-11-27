package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DBRecipientMapper {

	private static DBRecipientMapper instance;
	private Connection connection;

	public DBRecipientMapper() {
		try {
			connection = DBConnection.getInstance().getConnection(DatabaseHandler.DATABASE_NAME);
			createTableIfNotExists();
		} catch (Exception ex) {
		}
	}

	public static DBRecipientMapper getInstance() {
		if (instance == null) {
			try {
				instance = new DBRecipientMapper();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return instance;
	}

	private void createTableIfNotExists() {

		try {
			PreparedStatement pst = connection.prepareStatement(DatabaseHandler.CREATE_TABLE_RECIPIENT);
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (DatabaseHandler.isSetIndex) {
			try {
				String index = "CREATE  INDEX IF NOT EXISTS 'receipient_header_message_id_index' ON '"
						+ DatabaseHandler.TABLE_RECIPIENT + "' ('" + DatabaseHandler.KEY_LOCAL_HEADER_ID + "');";

				PreparedStatement pstIndex = connection.prepareStatement(index);
				pstIndex.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized long insert(RecipientModel recipientModel) {
		ResultSet rs = null;
		try {
			PreparedStatement insertStmt = connection.prepareStatement(DatabaseHandler.INSERT_RECEIPIENT_DATA_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			insertStmt.setInt(2, recipientModel.getLocalHeaderId());
			insertStmt.setString(3, recipientModel.getRecepientCashewnutId());
			insertStmt.setInt(4, recipientModel.getReceipientReadStatus());
			insertStmt.setInt(5, recipientModel.getReceipientAck());
			insertStmt.setInt(6, recipientModel.getReceipientDeleteStatus());
			// insertStmt.executeUpdate();
			insertStmt.execute();
			// System.out.println("Data Inserted successfully into Message
			// Table");

			rs = insertStmt.getGeneratedKeys();
			if (rs.next()) {
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
				Logger.getLogger(DBHeaderMapper.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return -1;
	}

	public synchronized void insertBatch(MessageModel messageModel, int localHeaderId) {
		Vector receipientModelVector = messageModel.getReceipient();
		insertBatch(receipientModelVector, localHeaderId);
	}

	public void insertBatch(Vector receipientModelVector, int localHeaderId) {
		try {
			if (receipientModelVector.size() < 1) {
				return;
			}
			PreparedStatement insertStmt = connection.prepareStatement(DatabaseHandler.INSERT_RECEIPIENT_DATA_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			connection.setAutoCommit(false);

			for (int i = 0; i < receipientModelVector.size(); i++) {
				RecipientModel recipientModel = (RecipientModel) receipientModelVector.elementAt(i);
				insertStmt.setInt(2, localHeaderId);
				insertStmt.setString(3, recipientModel.getRecepientCashewnutId());
				insertStmt.setInt(4, recipientModel.getReceipientReadStatus());
				insertStmt.setInt(5, recipientModel.getReceipientAck());
				insertStmt.setInt(6, recipientModel.getReceipientDeleteStatus());
				insertStmt.addBatch();
			}

			int[] affectedRecords = insertStmt.executeBatch();
			// System.out.println(affectedRecords.length + " " +
			// (System.currentTimeMillis() - lastMessageTime));
			connection.commit();
			connection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<RecipientModel> getMessageHeader() {
		List<RecipientModel> receipientList = new ArrayList<RecipientModel>();
		String selectQuery = "SELECT  * FROM  " + DatabaseHandler.TABLE_RECIPIENT;

		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(selectQuery);
			if (cursor.next()) {
				do {
					RecipientModel receipient = new RecipientModel();
					receipient.setRecepientLocalId(cursor.getInt(1));
					receipient.setLocalHeaderId(cursor.getInt(2));
					receipient.setRecepientCashewnutId(cursor.getString(3));
					receipient.setReceipientReadStatus(cursor.getInt(4));
					receipient.setReceipientAck(cursor.getInt(5));
					receipient.setReceipientDeleteStatus(cursor.getInt(6));
					receipientList.add(receipient);
				} while (cursor.next());
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return receipientList;
	}

	public Vector<Integer> getReceipientIdsByHeaderLocalId(int id) {
		// index

		String MakeSelect = "";

		if (DatabaseHandler.isSetIndex) {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT
					+ "  INDEXED BY receipient_header_message_id_index where " + DatabaseHandler.KEY_LOCAL_HEADER_ID
					+ "  =  " + id + "";
		} else {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT + "  where "
					+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "  =  " + id + "";
		}

		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(MakeSelect);
			Vector<Integer> receipientIdsVector = new Vector<Integer>();
			if (cursor.next()) {
				do {
					receipientIdsVector.addElement(cursor.getInt(1));
				} while (cursor.next());
			}
			cursor.close();
			return receipientIdsVector;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ResultSet getReceipientCursorByRecepientId(int id) {
		String MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT + "  where "
				+ DatabaseHandler.KEY_RECIPIENT_LOCAL_ID + "  =  " + id + "";
		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(MakeSelect);
			return cursor;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public RecipientModel getReceipientByRecepientId(int id) {
		try {
			ResultSet cursor = getReceipientCursorByRecepientId(id);
			MessageModel messageModel = new MessageModel();
			RecipientModel receipient = new RecipientModel(messageModel);

			try {
				if (cursor.next()) {
					do {
						receipient.setRecepientLocalId(cursor.getInt(1));
						receipient.setLocalHeaderId(cursor.getInt(2));
						receipient.setRecepientCashewnutId(cursor.getString(3));
						receipient.setReceipientReadStatus(cursor.getInt(4));
						receipient.setReceipientAck(cursor.getInt(5));
						receipient.setReceipientDeleteStatus(cursor.getInt(6));

					} while (cursor.next());
				}
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return receipient;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ResultSet getReceipientsCursorByHeaderId(int id) {
		// index

		String MakeSelect = "";
		if (DatabaseHandler.isSetIndex) {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT
					+ "  INDEXED BY receipient_header_message_id_index where " + DatabaseHandler.KEY_LOCAL_HEADER_ID
					+ "  =  " + id + "";
		} else {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT + "  where "
					+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "  =  " + id + "";
		}

		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(MakeSelect);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getReceipientsReadStatusByHeaderId(int id, String cashewnutId) {
		// index

		String MakeSelect = "";
		if (DatabaseHandler.isSetIndex) {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT
					+ "  INDEXED BY receipient_header_message_id_index where " + DatabaseHandler.KEY_LOCAL_HEADER_ID
					+ "  =  " + id + " AND " + DatabaseHandler.KEY_RECIPIENT_CASHEWNUT_ID + " = '" + cashewnutId + "'";
		} else {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_RECIPIENT + "  where "
					+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "  =  " + id + " AND "
					+ DatabaseHandler.KEY_RECIPIENT_CASHEWNUT_ID + " = '" + cashewnutId + "'";
		}

		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(MakeSelect);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Vector<RecipientModel> getReceipientsModelByHeaderId(int id) {
		Vector<RecipientModel> receipientVector = null;
		try {
			ResultSet cursor = getReceipientsCursorByHeaderId(id);
			receipientVector = new Vector<RecipientModel>();
			try {
				if (cursor.next()) {
					do {
						RecipientModel receipient = new RecipientModel();
						receipient.setRecepientLocalId(cursor.getInt(1));
						receipient.setLocalHeaderId(cursor.getInt(2));
						receipient.setRecepientCashewnutId(cursor.getString(3));
						receipient.setReceipientReadStatus(cursor.getInt(4));
						receipient.setReceipientAck(cursor.getInt(5));
						receipient.setReceipientDeleteStatus(cursor.getInt(6));
						receipientVector.addElement(receipient);
					} while (cursor.next());
				}
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return receipientVector;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return receipientVector;
	}

	public synchronized int update(RecipientModel recipientModel, int id) {
		int status = 0;
		String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_RECIPIENT + "  SET  "
				+ DatabaseHandler.KEY_REC_READ_STATUS + " = " + "?," + DatabaseHandler.KEY_REC_ACK_STATUS + " = " + "?"
				+ "  WHERE " + DatabaseHandler.KEY_RECIPIENT_LOCAL_ID + " =  " + id + " ";
		try {

			PreparedStatement insertStmt = connection.prepareStatement(updateQuery);

			insertStmt.setInt(1, recipientModel.getReceipientReadStatus());
			insertStmt.setInt(2, recipientModel.getReceipientAck());

			if (insertStmt.execute()) {
				status = 1;
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return status;
	}

	public int deleteReceipientByHeaderId(int msgId) {
		String deleteMsgQuery = " DELETE FROM " + DatabaseHandler.TABLE_RECIPIENT + "  WHERE "
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
}
