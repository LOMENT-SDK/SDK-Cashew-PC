package com.loment.cashewnut.database;

import java.util.List;
import java.util.Vector;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;

/**
 *
 * @author sekhar
 */
public class MessageStore {

    public int saveMessage(MessageModel messageModel) {
        int localHeaderId = -1;
        try {
            HeaderStore headerStore = new HeaderStore();
            localHeaderId = headerStore.saveHeader(messageModel);
            if (localHeaderId != -1) {
                messageModel.getHeaderModel().setLocalHeaderId(localHeaderId);

                BodyStore bodyStore = new BodyStore();
                AttachmentStore attachmentStore = new AttachmentStore();
                RecepientStore receipientStore = new RecepientStore();

                bodyStore.saveBody(messageModel, localHeaderId, false);

                receipientStore.saveRecepients(messageModel, localHeaderId);
                attachmentStore.saveAttachments(messageModel, localHeaderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localHeaderId;
    }

    public MessageModel getMessageByHeaderId(int localHeaderId) {
        try {
            MessageModel messageModel = new MessageModel();
            messageModel.setHeaderModel(new HeaderStore().getHeaderById(
                    localHeaderId, true));

            messageModel.setReceipientLocalIds(RecepientStore
                    .getReceipientsByHeaderLocalId(localHeaderId));
            Vector<?> attachments = new AttachmentStore()
                    .getAttachmentsByHeaderLocalId(localHeaderId);
            if (attachments != null) {
                messageModel.addAttachment(new AttachmentStore()
                        .getAttachmentsByHeaderLocalId(localHeaderId));
            }
            return messageModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeMessageByHeaderId(HeaderModel headerModel) {
        try {
            DBHeaderMapper header = DBHeaderMapper.getInstance();
            if (header.isMessageExists(headerModel.getLocalHeaderId(), true)) {
                BodyStore bodyStore = new BodyStore();
                AttachmentStore attachmentStore = new AttachmentStore();
                RecepientStore receipientStore = new RecepientStore();

                receipientStore.deleteRecepientByHeaderId(headerModel.getLocalHeaderId());
                bodyStore.deleteBody(headerModel.getLocalHeaderId());
                attachmentStore.deleteAttachmentsByHeaderId(headerModel);
                header.deleteByHeaderId(headerModel.getLocalHeaderId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

	// public boolean removeMessagesByThreardId(String threadId) {
    // try {
    // DBHeaderMapper header = DBHeaderMapper.getInstance();
    //
    // final List<HeaderModel> headerList = DBHeaderMapper
    // .getInstance().getConversationViewHeaders(threadId);
    //
    // for (int i = 0; i < headerList.size(); i++) {
    //
    // if (header.isMessageExists(headerList.get(i).getLocalHeaderId(), true)) {
    // BodyStore bodyStore = new BodyStore();
    // AttachmentStore attachmentStore = new AttachmentStore();
    // RecepientStore receipientStore = new RecepientStore();
    //
    // receipientStore.deleteRecepientByHeaderId(headerList.get(i).getLocalHeaderId());
    // bodyStore.deleteBody(headerList.get(i).getLocalHeaderId());
    // attachmentStore.deleteAttachmentsByHeaderId(headerList.get(i).getLocalHeaderId());
    // header.deleteByHeaderId(headerList.get(i).getLocalHeaderId());
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return true;
    // }
    public void saveSelfMessage(HeaderModel messageModeles, int status) {
        if (status != -1 && status == messageModeles.getNumberOfBodyparts()) {
            // for self message
            DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
            AccountsModel accountsModel = accountsMapper.getAccount();
            String cashewnutId = accountsModel.getCashewnutId();
            Vector<String> to = RecepientStore
                    .getRecepientCashewnutIds(messageModeles.getLocalHeaderId());
            String from = messageModeles.getMessageFrom();
            if (cashewnutId.equals(from) && to.contains(cashewnutId)) {
                // fire to list
                ((CashewnutActivity) CashewnutActivity.currentActivity)
                        .processMessage(messageModeles,
                                MessageModel.ACTION_ADDED);
            } else {
                
                ((CashewnutActivity) CashewnutActivity.currentActivity)
                        .processMessage(messageModeles,
                                MessageModel.ACTION_UPDATE);
            	}
         
        }
    }
}
