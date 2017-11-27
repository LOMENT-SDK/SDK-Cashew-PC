package com.loment.cashewnut.database;

import java.sql.ResultSet;
import java.util.Vector;

import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;

public class RecepientStore {

    public synchronized void saveRecepients1(MessageModel messageModel, int localHeaderId) {
        DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
        Vector receipientModelVector = messageModel.getReceipient();
        for (int i = 0; i < receipientModelVector.size(); i++) {
            RecipientModel recipientModel = (RecipientModel) receipientModelVector
                    .elementAt(i);
            recipientModel.setLocalHeaderId(localHeaderId);
            recipientMapper.insert(recipientModel);
        }
    }

    public synchronized void saveRecepients(MessageModel messageModel, int localHeaderId) {
        if (localHeaderId < 0) {
            return;
        }
        DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
        recipientMapper.insertBatch(messageModel, localHeaderId);
    }

    public static Vector<Integer> getReceipientsByHeaderLocalId(int i) {
        try {
            DBRecipientMapper receipientMapper = DBRecipientMapper.getInstance();
            Vector<Integer> receipientModelIdVector = receipientMapper.getReceipientIdsByHeaderLocalId(i);
            return receipientModelIdVector;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RecipientModel getreceipientsByReceipientLocalId(int i) {
        try {
            DBRecipientMapper receipientMapper = DBRecipientMapper.getInstance();
            RecipientModel receipientModelIdVector = receipientMapper.getReceipientByRecepientId(i);
            return receipientModelIdVector;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getRecepientCashewnutIds(int headerId) {
        // Receipients
        ResultSet cursor = DBRecipientMapper.getInstance()
                .getReceipientsCursorByHeaderId(headerId);
        Vector<String> to = new Vector<String>();
        try {
            if (cursor.next()) {
                do {
                    to.add(cursor.getString(2));
                } while (cursor.next());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return to;
    }

    public static int getRecepientReadStatus(int headerId, String cashewnutId) {

        ResultSet cursor = DBRecipientMapper.getInstance()
                .getReceipientsReadStatusByHeaderId(headerId, cashewnutId);
        int readStatus = 0;
        try {
            if (cursor != null && cursor.next()) {
                // Receipients Read status
                readStatus = cursor.getInt(4);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readStatus;
    }

    public void deleteRecepientByHeaderId(int localHeaderId) {
        DBRecipientMapper header = DBRecipientMapper.getInstance();
        header.deleteReceipientByHeaderId(localHeaderId);
    }
}
