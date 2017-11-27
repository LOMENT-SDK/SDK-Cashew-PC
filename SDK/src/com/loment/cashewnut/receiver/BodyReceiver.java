package com.loment.cashewnut.receiver;

import java.util.HashMap;

/**
 * 
 * @author sekhar
 */
public class BodyReceiver implements ReceiveServerRespListener {
	private HashMap<String, String> tokenMapper = new HashMap<String, String>();
	public static String RECEIVED_BODY_TOKEN_AMQP = "cashew_boby";

	public BodyReceiver() {
		try {
			Receiver.getInstance().addReceiverListener(this);
		} catch (Exception ex) {
		}
	}

	public void getMessage(String id) {
		// BodyPartsJson header = new BodyPartsJson();
		// String token = "MSG_BODY-" + Receiver.getInstance().getToken();
		// // msgId, headerId = 0, bodyId = 1, attachmentId = 0
		// String data = header.getMessageByIdJson(id, "0", "1", "0", token);
		// Receiver.getInstance().receive(data, "MSG_GET");
		// tokenMapper.put(token, id);
	}

	public void listenForResponse(String token, String response) {
	}
}
