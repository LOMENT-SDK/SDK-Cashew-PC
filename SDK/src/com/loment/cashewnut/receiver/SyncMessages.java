package com.loment.cashewnut.receiver;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.connection.amqp.RPCClientSender;
import com.loment.cashewnut.jsonReceiverModel.MessageIds;
import com.loment.cashewnut.sender.Sender;

/**
 * 
 * @author sekhar
 */
public class SyncMessages implements ReceiveServerRespListener {
	private HashMap<String, String> tokenMapper = new HashMap<String, String>();
	private static boolean amqpSync = false;

	public SyncMessages() {
		try {
			Receiver.getInstance().addReceiverListener(this);
		} catch (Exception ex) {
		}
	}

	public void sync(String foldername, String startMessageId,
			String endMessageId, String lastMessageSyncTime) {
		try {
			// folder name : "all" - all messages
			// startMessageId : "0" - start
			// endMessageId : "null" - end id

			MessageIds ids = new MessageIds();
			String token = "ID-" + Receiver.getInstance().getToken();

			String data = "";

			if (lastMessageSyncTime != null
					&& !lastMessageSyncTime.trim().equals("")) {
				data = ids.getNewMessageIdsJsonObjectAmqp(foldername, "null",
						"null", lastMessageSyncTime, token);
			} else {

				data = ids.getNewMessageIdsJsonObjectAmqp(foldername,
						startMessageId, endMessageId, token);
			}

			// System.out.println("Sync Message  ..." + data);
			if (!amqpSync) {
				amqpSync = true;
				// amqp connection
				Sender.getInstance().sendAmqp(data, token, 0, null);
				tokenMapper.put(token, startMessageId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void amqpSync() {
		try {
			RPCClientSender.getInstance().send(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Receiver.getInstance().receiveAMQP();
	}

	public void listenForResponse(String token, String response) {
		try {
			JSONObject jObject = new JSONObject(response);
			if (tokenMapper.containsKey(token) || jObject.has("msg_ids")) {
				tokenMapper.remove(token);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
