package com.loment.cashewnut.database.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.MessageModel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DBAttachmentMapper {

	private Connection connection;
	private static DBAttachmentMapper instance;

	private DBAttachmentMapper() throws ClassNotFoundException {
		try {
			connection = DBConnection.getInstance().getConnection(
					DatabaseHandler.DATABASE_NAME);
			createTableIfNotExists();
		} catch (Exception ex) {
		}
	}

	public static DBAttachmentMapper getInstance() {
		if (instance == null) {
			try {
				instance = new DBAttachmentMapper();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return instance;
	}

	private void createTableIfNotExists() {
		try {
			PreparedStatement pst = connection
					.prepareStatement(DatabaseHandler.CREATE_TABLE_ATTACHMENT);
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (DatabaseHandler.isSetIndex) {
			try {
				String index = "CREATE  INDEX IF NOT EXISTS 'attachment_header_message_id_index' ON '"
						+ DatabaseHandler.TABLE_ATTACHMENT
						+ "' ('"
						+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "');";

				PreparedStatement pstIndex = connection.prepareStatement(index);
				pstIndex.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized long insert(AttachmentModel attachmentModel) {
		ResultSet rs = null;
		try {
			PreparedStatement insertStmt = connection.prepareStatement(
					DatabaseHandler.INSERT_ATTACHMENT_DATA_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			insertStmt.setInt(2, attachmentModel.getLocalHeaderId());
			insertStmt.setString(3, attachmentModel.getAttachmentName());
			insertStmt.setString(4, attachmentModel.getAttachmentFilePath());
			insertStmt.setString(5, attachmentModel.getAttachmentType());
			insertStmt.setLong(6, attachmentModel.getAttachmentSize());
			insertStmt.setInt(7, attachmentModel.getPadding());
			insertStmt.executeUpdate();
			// System.out.println("Data Inserted successfully into Message Table");

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
				}
			}
		}
		return -1;
	}

	public Vector<Integer> getAttachmentIdsByHeaderId(int id) {
		String MakeSelect = "";

		if (DatabaseHandler.isSetIndex) {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_ATTACHMENT
					+ "  INDEXED BY attachment_header_message_id_index where "
					+ DatabaseHandler.KEY_LOCAL_HEADER_ID + "  =  " + id + "";
		} else {
			MakeSelect = "select * from  " + DatabaseHandler.TABLE_ATTACHMENT
					+ "  where " + DatabaseHandler.KEY_LOCAL_HEADER_ID
					+ "  =  " + id + "";
		}

		ResultSet cursor = null;
		try {
			cursor = connection.createStatement().executeQuery(MakeSelect);
			Vector<Integer> attachmentIds = new Vector<Integer>();
			if (cursor.next()) {
				do {
					attachmentIds.addElement(cursor.getInt(1));
				} while (cursor.next());
			}
			cursor.close();
			return attachmentIds;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public AttachmentModel getAttachmentByAttachmentId(int id) {
		String MakeSelect = "select * from  "
				+ DatabaseHandler.TABLE_ATTACHMENT + "  where "
				+ DatabaseHandler.KEY_ATTACHMENT_ID + "  =  " + id + "";
		ResultSet cursor = null;
		try {
			// Cursor cursor = rdb.rawQuery(MakeSelect, null);

			MessageModel messageModel = new MessageModel();
			AttachmentModel attachment = new AttachmentModel(messageModel);
			try {
				cursor = connection.createStatement().executeQuery(MakeSelect);
				if (cursor.next()) {
					do {
						attachment.setLocalAttachmentId(cursor.getInt(1));
						attachment.setLocalHeaderId(cursor.getInt(2));
						attachment.setAttachmentName(cursor.getString(3));
						attachment.setAttachmentFilePath(cursor.getString(4));
						attachment.setAttachmentType(cursor.getString(5));
						attachment.setAttachmentSize(cursor.getInt(6));
						attachment.setPadding(cursor.getInt(7));

					} while (cursor.next());
				}
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return attachment;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public int update(AttachmentModel attachmentModel, int id) {

		int status = 0;
		String updateQuery = "UPDATE  " + DatabaseHandler.TABLE_ATTACHMENT
				+ "  SET  " + DatabaseHandler.KEY_ATTACHMENT_NAME + " = "
				+ "?," + DatabaseHandler.KEY_ATTACHMENT_PATH + " = " + "?,"
				+ DatabaseHandler.KEY_ATTACHMENT_TYPE + " = " + "?,"
				+ DatabaseHandler.KEY_ATTACHMENT_SIZE + " = " + "?,"
				+ DatabaseHandler.KEY_ATTACHMENT_PADDING + " = " + "?"
				+ "  WHERE " + DatabaseHandler.KEY_ATTACHMENT_ID + " =  " + id
				+ " ";

		try {

			PreparedStatement insertStmt = connection
					.prepareStatement(updateQuery);

			insertStmt.setString(1, attachmentModel.getAttachmentName());
			insertStmt.setString(2, attachmentModel.getAttachmentFilePath());
			insertStmt.setString(3, attachmentModel.getAttachmentType());
			insertStmt.setLong(4, attachmentModel.getAttachmentSize());
			insertStmt.setInt(5, attachmentModel.getPadding());

			if (insertStmt.execute()) {
				status = 1;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return status;
	}

	public int deleteAttachmentsByHeaderId(int msgId) {

		String deleteMsgQuery = " DELETE FROM "
				+ DatabaseHandler.TABLE_ATTACHMENT + "  WHERE "
				+ DatabaseHandler.KEY_LOCAL_HEADER_ID + " = " + msgId + " ";

		try {
			if (connection.createStatement().execute(deleteMsgQuery)) {
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;

	}
}
