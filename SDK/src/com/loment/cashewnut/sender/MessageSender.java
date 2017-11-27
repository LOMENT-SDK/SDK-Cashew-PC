package com.loment.cashewnut.sender;

import java.util.Vector;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.activity.list.ConversationView;
import com.loment.cashewnut.connection.ReceipientConnection;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;

/**
 *
 * @author sekhar
 */
public class MessageSender {

	DBContactsMapper profileMapper = DBContactsMapper.getInstance();

	public int SendMessage(final MessageModel messageModel) {
	//messageModel.getHeaderModel().setScheduleTime(-1);
		final int localHeaderId = new MessageStore().saveMessage(messageModel);
		try {
			if (localHeaderId != -1) {
				if (CashewnutApplication.isNetworkConnected()) {
					addToList(messageModel.getHeaderModel());
					new Thread() {
						public void run() {
							sendNextPart(localHeaderId);
						}
					}.start();
				} else {
					HeaderModel headerModel = messageModel.getHeaderModel();
					headerModel.setSendParts(-1);
					new HeaderStore().updateHeaderById(headerModel,
							localHeaderId);
					addToList(headerModel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return localHeaderId;
	}

	public void addToList(HeaderModel headerModel) {
		try {
			if (CashewnutActivity.currentActivity != null) {
				// fire to list
				((ConversationView) CashewnutActivity.currentActivity)
						.processMessage(headerModel, MessageModel.ACTION_ADDED);
			}

			DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
			Vector<RecipientModel> recipientList = recipientMapper
					.getReceipientsModelByHeaderId(headerModel
							.getLocalHeaderId());
			ContactsModel contactsModel = null;
			for (int i = 0; i < recipientList.size(); i++) {
				RecipientModel recipient = (RecipientModel) recipientList
						.elementAt(i);
				contactsModel = profileMapper.getContact(recipient
						.getRecepientCashewnutId(),0);
				if (contactsModel.getFirstName() == null
						|| contactsModel.getFirstName().trim().length() < 1) {
					if (contactsModel.getPhone() == null
							|| contactsModel.getPhone().trim().length() < 1) {
						ReceipientConnection.getInstance().get(
								recipient.getRecepientCashewnutId().trim());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendNextPart(int local_msg_id) {
		if (CashewnutApplication.isInternetOn()) {
			HeaderModel messageModeles = (HeaderModel) new HeaderStore()
					.getHeaderById(local_msg_id, true);
			if (messageModeles != null) {
				int status = messageModeles.getSendParts();

				if (status != -1
						&& status < messageModeles.getNumberOfBodyparts()) {

					if (status > 1) {
						status = 2;
					}

					SenderHandler sender = SenderHandler.getInstance();
					switch (status) {
					case -1:
						break;
					case 0:
						sender.getHeaderSender().send(local_msg_id);
						
						break;
					case 1:
						// sender.getBodySender().send(local_msg_id);
						break;
					case 2:
						sender.getAttachmentSender().send(local_msg_id);
						 
					}
				}
				if (status == -1
						|| status == messageModeles.getNumberOfBodyparts()) {
					new MessageStore().saveSelfMessage(messageModeles, status);
				}
			}
		}
	}
}
