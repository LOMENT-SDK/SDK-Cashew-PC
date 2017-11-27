package com.loment.cashewnut.sender;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.jsonSenderModel.Body;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;

/**
 * 
 * @author sekhar
 */
public class BodySender implements SendServerRespListener {
	private HashMap<String, Integer> tokenMapper = new HashMap<String, Integer>();

	public BodySender() {
		try {
			Sender.getInstance().addSenderListener(this);
		} catch (Exception ex) {
		}
	}

	public void send(int id) {
		// MessageModel messageModeles = (MessageModel) new
		// MessageStore().getMessageByHeaderId(id);
		// String token = "body-" + Sender.getInstance().getToken();
		// Body body = new Body();
		// String data = body.getMessageBody(messageModeles, token);
		// Sender.getInstance().send(data, 1, null);
		// tokenMapper.put(token, id);
	}

	@Override
	public void listenForResponse(String token, String response) {
		if (tokenMapper.containsKey(token)) {
			try {
				if (response.equals("-1")) {
					int id = Integer.parseInt("" + tokenMapper.get(token));
					HeaderModel messageModel = new HeaderStore().getHeaderById(
							id, true);
					if (messageModel != null) {
						messageModel.setSendParts(-1);
						new HeaderStore().updateHeaderById(messageModel, id);
					}
				} else {
					JSONObject jObject = new JSONObject(response);
					if (jObject.has("msg_id")) {
						int id = Integer.parseInt("" + tokenMapper.get(token));
						MessageModel messageModel = (MessageModel) new MessageStore()
								.getMessageByHeaderId(id);
						HeaderModel headModel = messageModel.getHeaderModel();

						headModel.setSendParts(headModel.getSendParts() + 1);
						headModel.setServerMessageId(Integer.parseInt(jObject
								.get("msg_id") + ""));
						new HeaderStore().updateHeaderById(headModel, id);
						MessageSender messageSender = new MessageSender();
						messageSender.sendNextPart(id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
