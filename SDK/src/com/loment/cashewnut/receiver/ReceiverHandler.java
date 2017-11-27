package com.loment.cashewnut.receiver;

import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;

/**
 *
 * @author sekhar
 */
public class ReceiverHandler {

    public static ReceiverHandler instance = null;
    private static HeaderReceiver headerReceiver = null;
    private static BodyReceiver bodyReceiver = null;
    private static AttachmentReceiver attachmentReceiver = null;
    private static SyncMessages SyncMessages = null;
    private static FlagsStatusReceiver flagReveiver = null;
    public static int getNumberMessage = 10;
    private static GroupReceiver groupReceiver = null;
    private static GroupReceiverType6 groupReceiverType6 = null;
    private static ContactReceiver contactReceiver=null;

    public static ReceiverHandler getInstance() {
        if (instance == null) {
            instance = new ReceiverHandler();
        }
        return instance;
    }

    private String cashewnutID = "";

    private ReceiverHandler() {
        headerReceiver = new HeaderReceiver();
        bodyReceiver = new BodyReceiver();
        attachmentReceiver = new AttachmentReceiver();
        SyncMessages = new SyncMessages();
        flagReveiver = new FlagsStatusReceiver();
        groupReceiver = new GroupReceiver();
        groupReceiverType6 = new GroupReceiverType6();
        contactReceiver=new ContactReceiver();
        try {
            cashewnutID = DBAccountsMapper.getInstance().getAccount()
                    .getCashewnutId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncMessages() {
        requestMsgSync(false);
    }

    public void requestMsgSync(final boolean isAutoSync) {
        new Thread() {
            public void run() {
                if (CashewnutApplication.isInternetOn()) {
                    if (isAutoSync) {
                        long lastmessageId = 0;
                        String lastMessageSyncTime = null;
                        try {
                            long msgId = DBHeaderMapper.getInstance()
                                    .getMaxServerMessageId(cashewnutID);
                            if (msgId > -1) {
                                lastmessageId = msgId;
                            }
                            lastMessageSyncTime = HeaderReceiver
                                    .getLastMessageSyncTime();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SyncMessages.sync("all", lastmessageId + "", "null",
                                lastMessageSyncTime);
                    } else {
                        SyncMessages.amqpSync();
                    }
                }
            }
        }.start();
    }

    public void loadMoreMessages() {
        ReceiverHandler.getInstance().syncMessages();
    }

    public HeaderReceiver getHeaderReceiver() {
        return headerReceiver;
    }

    public BodyReceiver getBodyReceiver() {
        return bodyReceiver;
    }

    public AttachmentReceiver getAttachmentReceiver() {
        return attachmentReceiver;
    }

    public SyncMessages getsyncReceiver() {
        return SyncMessages;
    }

    public FlagsStatusReceiver getflagReceiver() {
        return flagReveiver;
    }

    public GroupReceiver getgroupReceiver() {
        return groupReceiver;
    }
    
    public GroupReceiverType6 getgroupReceiverType6() {
        return groupReceiverType6;
    }
public ContactReceiver getcontactReceiver()
{
	return contactReceiver;
}
    public void getreceiverListeners() {
        // receiverListeners
    }
}
