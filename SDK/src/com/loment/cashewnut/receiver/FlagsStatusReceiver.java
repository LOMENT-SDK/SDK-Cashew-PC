package com.loment.cashewnut.receiver;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.activity.list.ControlMessageOptions;
import com.loment.cashewnut.activity.list.ConversationView;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.jsonReceiverModel.FlagsJson;
import com.loment.cashewnut.jsonReceiverModel.Header;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.model.StatusFlagsModel;
import com.loment.cashewnut.sender.Sender;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sekhar
 */
public class FlagsStatusReceiver implements ReceiveServerRespListener {

    public static String RECEIVED_FLAG_UPDATE_TOKEN_AMQP = "cashew_falg_update";
    private HashMap<String, Vector<StatusFlagsModel>> tokenMapper = new HashMap<String, Vector<StatusFlagsModel>>();
    private String cashewnutId;

    public FlagsStatusReceiver() {
        try {
            Receiver.getInstance().addReceiverListener(this);
            DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
            AccountsModel accountsModel = accountsMapper.getAccount();
            cashewnutId = accountsModel.getCashewnutId();
        } catch (Exception ex) {
        }
    }

    public void getMessage(Vector<StatusFlagsModel> flagsObjectVector) {
        FlagsJson header = new FlagsJson();
        String token = "MSG_Flags-" + Receiver.getInstance().getToken();

        // amqp connection
        String data = header.getFlagsByIdJsonAmqp(flagsObjectVector, token);
        Sender.getInstance().sendAmqp(data, token, 0, null);
        tokenMapper.put(token, flagsObjectVector);
    }

    public void listenForResponse(String token, String response) {
        try {
            JSONObject jsonMessageObject = new JSONObject(response);
            if (tokenMapper.containsKey(token)) {
                // sender response
                senderResponse(token, jsonMessageObject);
            } else if (token.equals(RECEIVED_FLAG_UPDATE_TOKEN_AMQP)) {
                // update flags
                try {
                    int serverMessageId = Integer.parseInt(jsonMessageObject
                            .getString("id"));
                    HeaderModel headerModel = new HeaderStore().getHeaderById(
                            serverMessageId, false);
                    if (headerModel != null
                            && headerModel.getLocalHeaderId() != -1
                            && headerModel.getDeleteStatus() < 1) {
                        // update header
                        updateMessage(headerModel, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                   // Logger.getLogger(FlagsStatusReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void senderResponse(String token, JSONObject jsonMessageObject) {
        try {
            if (jsonMessageObject.length() > 0) {
                Vector<StatusFlagsModel> model = (Vector<StatusFlagsModel>) tokenMapper
                        .get(token);
                for (int i = 0; i < model.size(); i++) {
                	
                    boolean isDeleted = false;
                    boolean isSelfDeleted = false;
                    boolean isUpdated = false;

                    StatusFlagsModel flagsModel = model.elementAt(i);
                    int msgId = flagsModel.getMessageId();
                    String read = flagsModel.getRead();
                    String ack = flagsModel.getAck();
                    String deleted = flagsModel.getRecpDeleted();
                    String selfDeleted = flagsModel.getSelfDeleted();

                    HeaderStore headerStore = new HeaderStore();
                    HeaderModel headerModel = headerStore.getHeaderById(msgId,
                            false);

                    if (headerModel != null) {

                        if (selfDeleted != null
                                && !selfDeleted.trim().equals("")) {
                            isSelfDeleted = true;
                        }

                        Vector recipientList = DBRecipientMapper.getInstance()
                                .getReceipientsModelByHeaderId(
                                        headerModel.getLocalHeaderId());
                        for (int index = 0; index < recipientList.size(); index++) {
                            RecipientModel recipient = (RecipientModel) recipientList
                                    .elementAt(index);
                            if (cashewnutId.equals(recipient
                                    .getRecepientCashewnutId())) {
                                if (deleted != null
                                        && !deleted.trim().equals("")) {
                                    isDeleted = true;
                                }
                                if (ack != null && !ack.trim().equals("")) {
                                    recipient.setReceipientAck(1);
                                    isUpdated = true;
                                }
                                if (read != null && !read.trim().equals("")) {
                                    recipient.setReceipientReadStatus(1);
                                    isUpdated = true;
                                }
                                if (isUpdated && !isDeleted) {
                                    DBRecipientMapper.getInstance().update(
                                            recipient,
                                            recipient.getRecepientLocalId());
                                }
                                break;
                            }
                        }

                        if (isSelfDeleted || isDeleted) {
                            try {
                                ControlMessageOptions
                                        .deleteMessageLocal(headerModel);
                                addToList(headerModel,
                                        MessageModel.ACTION_DELETED);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (isUpdated) {
                            addToList(headerModel, MessageModel.ACTION_UPDATE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMessage(HeaderModel headerModel, String response) {
        if (headerModel.getLocalHeaderId() != -1) {

            boolean isDeleted = false;
            boolean isSelfDeleted = false;
            boolean isUpdated = false;

            MessageModel messageUpdateModel = new Header()
                    .getHeaderUpdatedFlagsData(response, cashewnutId);
            if (messageUpdateModel.getBoxType().equals(
                    MessageModel.MESSAGE_FOLDER_TYPE_INBOX)) {
                Vector<RecipientModel> updateReceipients = messageUpdateModel
                        .getReceipient();
                Vector<RecipientModel> recipientList = DBRecipientMapper
                        .getInstance().getReceipientsModelByHeaderId(
                                headerModel.getLocalHeaderId());
                for (int i = 0; i < updateReceipients.size(); i++) {
                    RecipientModel updatedRecpModel = updateReceipients.get(i);
                    for (int m = 0; m < recipientList.size(); m++) {
                        RecipientModel recpModel = recipientList.get(m);
                        if (updatedRecpModel.getRecepientCashewnutId().equals(
                                recpModel.getRecepientCashewnutId())
                                && recpModel.getRecepientCashewnutId().equals(
                                        cashewnutId)) {
                            if (updatedRecpModel.getReceipientDeleteStatus() > 0) {
                                isDeleted = true;
                                break;
                            } else {
                                isUpdated = true;
                                updateRecepientsData(updatedRecpModel,
                                        recpModel);
                            }
                        }
                    }
                }
            } else if (messageUpdateModel.getBoxType().equals(
                    MessageModel.MESSAGE_FOLDER_TYPE_SENTBOX)) {
                try {
                    HeaderModel updatedHeaderModel = messageUpdateModel
                            .getHeaderModel();
                    if (updatedHeaderModel != null
                            && updatedHeaderModel.getDeleteStatus() > 0) {
                        isSelfDeleted = true;
                    } else {
                        Vector<RecipientModel> updateReceipientList = messageUpdateModel
                                .getReceipient();
                        Vector<RecipientModel> recipientList = DBRecipientMapper
                                .getInstance().getReceipientsModelByHeaderId(
                                        headerModel.getLocalHeaderId());
                        for (int i = 0; i < updateReceipientList.size(); i++) {
                            RecipientModel updatedRecpModel = updateReceipientList
                                    .get(i);
                            for (int m = 0; m < recipientList.size(); m++) {
                                RecipientModel recpModel = recipientList.get(m);
                                if (updatedRecpModel.getRecepientCashewnutId()
                                        .equals(recpModel
                                                .getRecepientCashewnutId())) {
                                    isUpdated = true;
                                    updateRecepientsData(updatedRecpModel,
                                            recpModel);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                if (isSelfDeleted || isDeleted) {
                    ControlMessageOptions.deleteMessageLocal(headerModel);
                    if (CashewnutActivity.currentActivity != null) {
                        addToList(headerModel, MessageModel.ACTION_DELETED);
                    }
                    return;
                }

                if (isUpdated) {
                    if (CashewnutActivity.currentActivity != null) {
                        addToList(headerModel, MessageModel.ACTION_UPDATE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateRecepientsData(RecipientModel updatedRecpModel,
            RecipientModel recpModel) {
        recpModel.setReceipientReadStatus(updatedRecpModel
                .getReceipientReadStatus());
        recpModel.setReceipientAck(updatedRecpModel.getReceipientAck());
        recpModel.setReceipientDeleteStatus(updatedRecpModel
                .getReceipientDeleteStatus());
        DBRecipientMapper.getInstance().update(recpModel,
                recpModel.getRecepientLocalId());
    }

//    public void addToList(HeaderModel headerModel, int type) {
//        try {
//            // fire to list
//            ((CashewnutActivity) CashewnutActivity.currentActivity)
//                    .processMessage(headerModel, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
	private void addToList(HeaderModel headerModel, int type) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				// fire to list
				((ConversationView) CashewnutActivity.currentActivity)
						.processMessage(headerModel, type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
