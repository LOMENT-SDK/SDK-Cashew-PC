package com.loment.cashewnut.sender;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.jsonSenderModel.Header;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;

/**
 * 
 * @author sekhar
 */
public class HeaderSender implements SendServerRespListener {
	private HashMap<String, Integer> tokenMapper = new HashMap<String, Integer>();

	public HeaderSender() {
		try {
			Sender.getInstance().addSenderListener(this);
		} catch (Exception ex) {
		}
	}

	public void send(int id) {
		MessageModel messageModeles = (MessageModel) new MessageStore().getMessageByHeaderId(id);
		String token = "header-" + Sender.getInstance().getToken();
		Header body = new Header();

		// amqp connection
		String data = body.getHeaderDataAmqp(messageModeles, token);
		Sender.getInstance().sendAmqp(data, token, 0, null);

		tokenMapper.put(token, id);
	}

	public void listenForResponse(String token, String response) {
		if (tokenMapper.containsKey(token)) {
			try {
				if (response.equals("-1")) {
					int id = Integer.parseInt("" + tokenMapper.get(token));
					HeaderModel messageModel = new HeaderStore().getHeaderById(id, true);
					if (messageModel != null) {
						messageModel.setSendParts(-1);
						new HeaderStore().updateHeaderById(messageModel, id);
						addToList(messageModel, MessageModel.ACTION_UPDATE);
					}
				} else {
					JSONObject jObject = new JSONObject(response);
					if (jObject.has("msg_id")) {
						int id = Integer.parseInt("" + tokenMapper.get(token));
						HeaderModel messageModel = new HeaderStore().getHeaderById(id, true);
						if (messageModel != null) {
							int bodyPartsCount = 2; // header and body
							messageModel.setSendParts(messageModel.getSendParts() + bodyPartsCount);
							messageModel.setServerMessageId(Integer.parseInt("" + jObject.get("msg_id")));
							new HeaderStore().updateHeaderById(messageModel, id);

							try {
								if (jObject.has("recipients")) {
									// if group message
									JSONArray recipients = jObject.getJSONArray("recipients");
									Vector<RecipientModel> receipientModelVector = new Vector<RecipientModel>();
									for (int i = 0; i < recipients.length(); i++) {
										RecipientModel recipientModel = new RecipientModel();
										recipientModel.setRecepientCashewnutId(recipients.getString(i));
										recipientModel.setReceipientReadStatus(MessageModel.RECIPIENT_STATUS_UNREAD);
										recipientModel.setReceipientAck(MessageModel.RECIPIENT_STATUS_UNACK);
										recipientModel.setReceipientDeleteStatus(MessageModel.RECIPIENT_INITIAL_DELETED_STATUS);
										receipientModelVector.add(recipientModel); 
									}

									DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
									recipientMapper.insertBatch(receipientModelVector, id);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}

							MessageSender messageSender = new MessageSender();
							messageSender.sendNextPart(messageModel.getLocalHeaderId());

						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			tokenMapper.remove(token);
		}
	}

	public void addToList(HeaderModel headerModel, int action) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				// fire to list
				((CashewnutActivity) CashewnutActivity.currentActivity).processMessage(headerModel, action);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isTokenContains(String value) {
		return tokenMapper.containsKey(value);
	}
}
