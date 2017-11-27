package com.loment.cashewnut.jsonReceiverModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author sekhar
 */
public class MessageIds {
	private static String MESSAGE_FOLDER_TYPE = "folder";
	private static String START_MESSAGE_ID = "start_msg_id";
	private static String END_MESSAGE_ID = "end_msg_id";
	private static String MESSAGE_LAST_SYNC_TIME = "start_msg_time";

	public String getNewMessageIdsJsonObjectAmqp(String foldername,
			String startMessageId, String endMessageId, String token) {
		JSONObject newMessageIdsList = new JSONObject();
		JSONObject finalObject = new JSONObject();
		try {
			newMessageIdsList.put(MESSAGE_FOLDER_TYPE, foldername);
			newMessageIdsList.put(START_MESSAGE_ID, startMessageId);
			newMessageIdsList.put(END_MESSAGE_ID, endMessageId);
			finalObject.put("data", newMessageIdsList);
			finalObject.put("cmd", "5");
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getNewMessageIdsJsonObjectAmqp(String foldername,
			String startMessageId, String endMessageId, String lastMessageTime,
			String token) {
		JSONObject newMessageIdsList = new JSONObject();
		JSONObject finalObject = new JSONObject();
		try {
			newMessageIdsList.put(MESSAGE_FOLDER_TYPE, foldername);
			newMessageIdsList.put(START_MESSAGE_ID, startMessageId);
			newMessageIdsList.put(END_MESSAGE_ID, endMessageId);
			newMessageIdsList.put(MESSAGE_LAST_SYNC_TIME, lastMessageTime);

			finalObject.put("data", newMessageIdsList);
			finalObject.put("cmd", "5");

			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

}
