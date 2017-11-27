package com.loment.cashewnut.sender;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.database.AttachmentStore;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.jsonSenderModel.Attachment;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;

public class AttachmentSender implements SendServerRespListener {

	private HashMap<String, Integer> tokenMapper = new HashMap<String, Integer>();

	public AttachmentSender() {
		try {
			Sender.getInstance().addSenderListener(this);
		} catch (Exception ex) {
		}
	}

	public void send(int id) {
		MessageModel messageModeles = new MessageStore()
				.getMessageByHeaderId(id);
		HeaderModel headModel = messageModeles.getHeaderModel();

		Attachment body = new Attachment();
		String token = "attachment-" + Sender.getInstance().getToken();
		int sendStatus = headModel.getSendParts() - 2;
		int unsentAttachmentLocalID = Integer.parseInt(messageModeles
				.getAttachment().elementAt(sendStatus) + "");

		AttachmentModel attachmentModel = (AttachmentModel) new AttachmentStore()
				.getAttachmentByAttachmentLocalId(unsentAttachmentLocalID);

		String data = body.getAttachmentBodyAmqp(attachmentModel,
				headModel.getServerMessageId() + "", token);
		Sender.getInstance().sendAmqp(data, token, 1,
				attachmentModel.getAttachmentFilePath());
		tokenMapper.put(token, id);
	}

	@Override
	public void listenForResponse(String token, String response) {
		if (tokenMapper.containsKey(token)) {
			try {
				if (response.equals("-1")) {
					int id = Integer.parseInt("" + tokenMapper.get(token));
					HeaderModel messageModel = new HeaderStore()
							.getHeaderById(id, true);
					if (messageModel != null) {
						messageModel.setSendParts(-1);
						new HeaderStore()
								.updateHeaderById(messageModel, id);
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
