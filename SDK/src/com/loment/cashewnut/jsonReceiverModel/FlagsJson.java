package com.loment.cashewnut.jsonReceiverModel;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import com.loment.cashewnut.model.StatusFlagsModel;

/**
 * 
 * @author sekhar
 */
public class FlagsJson {
	private static String MESSAGE_STATUS = "status";
	private static String MESSAGE_RECEIVER_FOLDER = "folder";
	private static String MESSAGE_SENDER_FOLDER = "folder";

	private static String MESSAGE_READ = "read";
	private static String MESSAGE_ACK = "ack";
	private static String MESSAGE_SELF_DELETED = "self_deleted";
	private static String MESSAGE_RECIPIENT_DELETED = "recipient_deleted";

	public String getFlagsByIdJson(Vector<StatusFlagsModel> flagsObjectVector,
			String token) {
		try {
			JSONObject mainListjson = new JSONObject();
			for (int i = 0; i < flagsObjectVector.size(); i++) {
				StatusFlagsModel model = (StatusFlagsModel) flagsObjectVector
						.elementAt(i);
				JSONObject statusJson = new JSONObject();
				JSONObject statusListJson = new JSONObject();

				if (model.getReceiverFolder() != null
						&& !model.getReceiverFolder().trim().equals("")) {
					statusJson.put(MESSAGE_SENDER_FOLDER,
							model.getReceiverFolder());
				}
				if (model.getSelfDeleted() != null
						&& !model.getSelfDeleted().trim().equals("")) {
					statusJson
							.put(MESSAGE_SELF_DELETED, model.getSelfDeleted());
				}

				if (model.getReceiverFolder() != null
						&& !model.getReceiverFolder().trim().equals("")) {
					statusListJson.put(MESSAGE_RECEIVER_FOLDER,
							model.getReceiverFolder());
				}
				if (model.getRead() != null
						&& !model.getRead().trim().equals("")) {
					statusListJson.put(MESSAGE_READ, model.getRead());
				}
				if (model.getAck() != null && !model.getAck().trim().equals("")) {
					statusListJson.put(MESSAGE_ACK, model.getAck());
				}
				if (model.getRecpDeleted() != null
						&& !model.getRecpDeleted().trim().equals("")) {
					statusListJson.put(MESSAGE_RECIPIENT_DELETED,
							model.getRecpDeleted());
				}
				if (statusListJson.length() > 0) {
					statusJson.put(MESSAGE_STATUS, statusListJson);
				}
				if (statusJson.length() > 0) {
					mainListjson.put(model.getMessageId() + "", statusJson);
				}
			}
			mainListjson.put("token", token);
			return mainListjson.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getFlagsByIdJsonAmqp(
			Vector<StatusFlagsModel> flagsObjectVector, String token) {
		try {
			JSONObject mainListjson = new JSONObject();
			for (int i = 0; i < flagsObjectVector.size(); i++) {
				StatusFlagsModel model = (StatusFlagsModel) flagsObjectVector
						.elementAt(i);
				JSONObject statusJson = new JSONObject();
				JSONObject statusListJson = new JSONObject();

				if (model.getReceiverFolder() != null
						&& !model.getReceiverFolder().trim().equals("")) {
					statusJson.put(MESSAGE_SENDER_FOLDER,
							model.getReceiverFolder());
				}
				if (model.getSelfDeleted() != null
						&& !model.getSelfDeleted().trim().equals("")) {
					statusJson
							.put(MESSAGE_SELF_DELETED, model.getSelfDeleted());
				}

				if (model.getReceiverFolder() != null
						&& !model.getReceiverFolder().trim().equals("")) {
					statusListJson.put(MESSAGE_RECEIVER_FOLDER,
							model.getReceiverFolder());
				}
				if (model.getRead() != null
						&& !model.getRead().trim().equals("")) {
					statusListJson.put(MESSAGE_READ, model.getRead());
				}
				if (model.getAck() != null && !model.getAck().trim().equals("")) {
					statusListJson.put(MESSAGE_ACK, model.getAck());
				}
				if (model.getRecpDeleted() != null
						&& !model.getRecpDeleted().trim().equals("")) {
					statusListJson.put(MESSAGE_RECIPIENT_DELETED,
							model.getRecpDeleted());
				}
				if (statusListJson.length() > 0) {
					statusJson.put(MESSAGE_STATUS, statusListJson);
				}
				if (statusJson.length() > 0) {
					mainListjson.put(model.getMessageId() + "", statusJson);
				}
			}

			JSONObject finalObject = new JSONObject();
			finalObject.put("data", mainListjson);
			finalObject.put("cmd", "4");

			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getSingleHeaderDataAmqp(
			Vector<StatusFlagsModel> flagsObjectVector, String token) {
		if (flagsObjectVector.size() > 0) {
			StatusFlagsModel model = (StatusFlagsModel) flagsObjectVector
					.elementAt(0);
			JSONObject headerMain = new JSONObject();
			JSONObject finalObject = new JSONObject();
			try {
				headerMain.put("msg_id", model.getMessageId());
				finalObject.put("data", headerMain);
				finalObject.put("cmd", "7");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return finalObject.toString() + "\r\n";

		}
		return "";
	}
}
