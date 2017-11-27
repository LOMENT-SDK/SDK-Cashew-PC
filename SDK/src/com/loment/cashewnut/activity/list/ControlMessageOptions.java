package com.loment.cashewnut.activity.list;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.activity.list.ConversationsViewRenderer.MessageViewModel;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.SettingsModel;
import com.loment.cashewnut.model.StatusFlagsModel;
import com.loment.cashewnut.receiver.ReceiverHandler;
import com.loment.cashewnut.sender.MessageSender;

/**
*
* @author sekhar
*/
public class ControlMessageOptions {

    private static Timer t1 = new Timer();
    private Timer t = new Timer();
    private boolean isTimerRunning = false;
    private static HashMap<Integer, Long> expiryVector = new HashMap<Integer, Long>();
    private static HashMap<Integer,Object>scheduleMessage=new HashMap<Integer,Object>();
    private static HashMap<Integer,Object>senderscheduleMessage=new HashMap<Integer,Object>();
    private static HashMap<Integer, Long> scheduleMessageQueue = new HashMap<Integer, Long>();
    static ControlMessageOptions instance = null;
    static int currentTimer = 0;
    static int privacy = 1;
    private boolean isSchedulTimerRunning = false;
    private String cashewnutId = "";
    private ControlMessageOptions() {
        setHibernationTime();
    }

    public void setHibernationTime() {
        SettingsModel model = new SettingsStore().getSettingsData();
        currentTimer = -1;
        privacy = 1;
        if (model != null) {
            privacy = model.getRememberStatus();
            if (privacy < 1) {
                currentTimer = model.getHibernationTime();
            }
        }
    }

    public static ControlMessageOptions getInstance() {
        if (instance == null) {
            instance = new ControlMessageOptions();
        }
        return instance;
    }

/*    public void getScheduleMessagesFromHeaderStore() {
        List headerList = DBHeaderMapper.getInstance()
                .getScheduleMessageHeaderList();
        if (headerList != null && headerList.size() > 0) {
            for (int i = 0; i < headerList.size(); i++) {
                HeaderModel header = (HeaderModel) headerList.get(i);
                if (!scheduleMessageQueue
                        .containsKey(header.getLocalHeaderId())) {
                    scheduleMessageQueue.put(header.getLocalHeaderId(),
                            header.getScheduleTime());
                }
            }
        }
    }*/

    public void setSchedule(int id, long scheduleTime) {
        if (!scheduleMessageQueue.containsKey(id)) {
            scheduleMessageQueue.put(id, scheduleTime);
        }
    }
    

    public static boolean deleteMessageLocal(HeaderModel selectedItem) {
        boolean isDeleted = false;
        try {
            isDeleted = new MessageStore().removeMessageByHeaderId(selectedItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public static boolean deleteMessageOnServer(HeaderModel selectedItem,
            String boxType) {
        if (CashewnutApplication.isInternetOn()
                && selectedItem.getServerMessageId() != -1) {
            StatusFlagsModel model = new StatusFlagsModel();
            model.setMessageId(selectedItem.getServerMessageId());
            if (boxType.equals(MessageModel.MESSAGE_FOLDER_TYPE_INBOX)) {
                model.setRecpDeleted("1");
            } else {
                model.setSelfDeleted("1");
            }
            Vector<StatusFlagsModel> modelVector = new Vector<StatusFlagsModel>();
            modelVector.addElement(model);
            sendUpdateCall(modelVector);
            return true;
        }
        return false;
    }

    // server id,boxtype
    public static boolean deleteMultipleMessageOnServer(
            HashMap<Integer, String> headerList) {
        if (CashewnutApplication.isInternetOn()) {
            final Vector<StatusFlagsModel> modelVector = new Vector<StatusFlagsModel>();
            for (Entry<Integer, String> entry : headerList.entrySet()) {
                int id = entry.getKey();
                String boxType = entry.getValue();
                if (id > 0) {
                    StatusFlagsModel model = new StatusFlagsModel();
                    model.setMessageId(id);
                    if (boxType.equals(MessageModel.MESSAGE_FOLDER_TYPE_INBOX)) {
                        model.setRecpDeleted("1");
                    } else {
                        model.setSelfDeleted("1");
                    }
                    modelVector.addElement(model);
                }
            }
            if (modelVector.size() > 0) {
                sendUpdateCall(modelVector);
            }
            return true;
        }
        return false;
    }

    public static void setReadStatusMultipleMessageOnServer(
            final Vector<Integer> serverMsgIds) {
        try {
			if (CashewnutApplication.isInternetOn()) {
			    final Vector<StatusFlagsModel> modelVector = new Vector<StatusFlagsModel>();
			    for (int i = 0; i < serverMsgIds.size(); i++) {
			        if (serverMsgIds.get(i) == -1) {
			            continue;
			        }
			        StatusFlagsModel model = new StatusFlagsModel();
			        model.setMessageId(serverMsgIds.get(i));
			        model.setRead("1");
			        modelVector.addElement(model);
			    }
			    if (modelVector.size() > 0) {
			        sendUpdateCall(modelVector);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

//	public static void getSingleMessage(final Integer serverMsgId) {
//		if (CashewnutApplication.isInternetOn()) {
//			
//		new Thread() { 
//			public void run() {
//				StatusFlagsModel model = new StatusFlagsModel();
//				model.setMessageId(serverMsgId);
//				Vector<StatusFlagsModel> modelVector = new Vector<StatusFlagsModel>();
//				modelVector.addElement(model);
//				
//				ReceiverHandler.getInstance().getflagReceiver()
//						.getSingleMessage(modelVector);
//			}
//		}.start();
//		}
//	}
    private static void sendUpdateCall(
            final Vector<StatusFlagsModel> modelVector) {
        new Thread() {
            public void run() {
                ReceiverHandler.getInstance().getflagReceiver()
                        .getMessage(modelVector);

            }
        }.start();
    }

    public boolean checkForAcknowledgement(HeaderModel header,
            String cashewnutId) {
        if (header.getMessageAck() == MessageControlParameters.ACK_SENDER
                && !header.getMessageFrom().trim().equals(cashewnutId)) {
            return true;
        }
        return false;
    }

    public void viewAcknowledgementMessage(HeaderModel header,
            String cashewnutId) {
    	
        // TODO Auto-generated method stub
        if (header.getMessageAck() == MessageControlParameters.ACK_SENDER
                && !header.getMessageFrom().trim().equals(cashewnutId)) {
            // show dialog with yes no
            if (CashewnutApplication.isInternetOn()) {
                StatusFlagsModel model = new StatusFlagsModel();
                model.setMessageId(header.getServerMessageId());
                model.setAck("1");
                model.setRead("1");
                Vector<StatusFlagsModel> modelVector = new Vector<StatusFlagsModel>();
                modelVector.addElement(model);
                ReceiverHandler.getInstance().getflagReceiver()
                        .getMessage(modelVector);
              
            }
        }
    }
    public void viewExpiryMessage(HeaderModel header,
            String cashewnutId) {
        // TODO Auto-generated method stub
        if ( !header.getMessageFrom().trim().equals(cashewnutId)) {
            // show dialog with yes no
            if (CashewnutApplication.isInternetOn()) {
                StatusFlagsModel model = new StatusFlagsModel();
                model.setMessageId(header.getServerMessageId()); 
                model.setAck(header.getMessageAck()+"");
                model.setRead("1");
                Vector<StatusFlagsModel> modelVector = new Vector<StatusFlagsModel>();
                modelVector.addElement(model);
                ReceiverHandler.getInstance().getflagReceiver()
                        .getMessage(modelVector);
            }
        }
    }
    
    
    static boolean isRestricted(HeaderModel header, String cashewnutId,
            String boxType) {
        if (!boxType.equals(MessageModel.MESSAGE_FOLDER_TYPE_INBOX)) {
            return false;
        }
        if (!header.getMessageFrom().trim().equals(cashewnutId)
                && header.getRestricted() == MessageControlParameters.RESTRICTED) {
            return true;
        }
        return false;
    }

    public static void setExpiry(int id, long expiryTime) {
        if (!expiryVector.containsKey(id)) {
            expiryVector.put(id, expiryTime);
        }
    }
public static void setScheduleThread(int id,HeaderModel header)
{
	if(!scheduleMessage.containsKey(id))
	{
		scheduleMessage.put(id,header);
	}
}
public static void setSenderScheduler(int id,HeaderModel header)
{
	if(!senderscheduleMessage.containsKey(id))
	{
		senderscheduleMessage.put(id,header);
	}
}

    public void startMessageExpiraryTimer() {
        t1 = new Timer();
       TimerTask tt = new TimerTask() {
            public void run() {
                try {
                	Iterator it=expiryVector.entrySet().iterator();
                    //for (Entry<Integer, Long> entry : expiryVector.entrySet())
                	while(it.hasNext())
                    {
                		Entry<Integer, Long> entry=(Entry<Integer, Long>) it.next();
                        int id = entry.getKey();
                        long expTime = entry.getValue();
                        if (expTime <= System.currentTimeMillis()) {
                            HeaderStore headerStore = new HeaderStore();
                            HeaderModel headerModel = headerStore
                                    .getHeaderById(id, true);
                            if (headerModel != null) {
                                HeaderModel header = headerStore.getHeaderById(
                                        id, true);
                                header.setDeleteStatus(MessageModel.RECIPIENT_DELETED_STATUS);
                                headerStore.updateHeaderById(header, id);
                                deleteMessageOnServer(headerModel,
                                        MessageModel.MESSAGE_FOLDER_TYPE_INBOX);
                                //expiryVector.remove(id);
                                it.remove();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Schedule messages
                /*try {
                    for (Entry<Integer, Long> entry : scheduleMessageQueue
                            .entrySet()) {
                        int id = entry.getKey();
                        long scheduleTime = entry.getValue();
                        long currentTime = System.currentTimeMillis() + 10000;
                        if (scheduleTime <= currentTime) {

                            HeaderModel hm = DBHeaderMapper.getInstance()
                                    .getMessageHeaderById(id);
                            if (hm != null && hm.getLocalHeaderId() > 0) {
                                hm.setCreationTime(scheduleTime);
                                hm.setScheduleTime(-1);
                                if (CashewnutApplication.isInternetOn()) {
                                    new HeaderStore().updateHeaderById(hm, id);
                                    new MessageSender().sendNextPart(id);
                                } else {
                                    hm.setSendParts(-1);
                                    new HeaderStore().updateHeaderById(hm, id);
                                }
                                scheduleMessageQueue.remove(id);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
               try {
                    for (Entry<Integer, Object> entry : senderscheduleMessage
                            .entrySet()) {
                        int id = entry.getKey();
                        HeaderModel hm = (HeaderModel) entry.getValue();
                        long scheduleTime=hm.getScheduleTime(); 
                        long currentTime = System.currentTimeMillis() + 10000;
                        HeaderModel hm1 = DBHeaderMapper.getInstance()
                                .getMessageHeaderById(id);
                        DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
            			AccountsModel accountsModel = accountsMapper.getAccount();
            			cashewnutId = accountsModel.getCashewnutId();
                       
                if(scheduleTime<=currentTime)
                {
                	
                	if(hm.getMessageFrom().equals(cashewnutId))
                	{
                		
                		if(hm1.getNumberOfBodyparts()==hm.getSendParts())
                		{
                			//hm.setScheduleTime(-1);
                			hm.setSendParts(2);
                		new HeaderStore().updateHeaderById(hm, id);
                
                		if (CashewnutActivity.currentActivity != null) {
                			try {
                				 //System.out.println(cashewnutId);
                				// System.out.println("fire msg to conversation .....");
                				// fire to list
                				((ConversationView) CashewnutActivity.currentActivity)
                						.processMessage(hm,  2);
                			} catch (Exception e) {
                				e.printStackTrace();
                			}
                		//}
                		}	
                	}
                }
                    }
                    }
                }
                    catch(Exception e)
                    {
                    	e.printStackTrace();
                    }
              
                try {
                    for (Entry<Integer, Object> entry : scheduleMessage
                            .entrySet()) {
                        int id = entry.getKey();
                        HeaderModel hm = (HeaderModel) entry.getValue();
                        long scheduleTime=hm.getScheduleTime(); 
                        long currentTime = System.currentTimeMillis() + 10000;
                        HeaderModel hm1 = DBHeaderMapper.getInstance()
                                .getMessageHeaderById(id);
                        DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
            			AccountsModel accountsModel = accountsMapper.getAccount();
            			cashewnutId = accountsModel.getCashewnutId();
                        if (scheduleTime <= currentTime) {

                        	if(!(hm.getMessageFrom().equals(cashewnutId)))
                        	{
                        
                            if (hm1!= null && hm1.getLocalHeaderId() > 0) {
                                //hm1.setCreationTime(scheduleTime);
                            	
                               
                            	if (CashewnutActivity.currentActivity != null) {
                        			try {
                        				
                        				// System.out.println("fire msg to conversation .....");
                        				// fire to list
                        				((ConversationView) CashewnutActivity.currentActivity)
                        						.processMessage(hm1, 0);
                        			} catch (Exception e) {
                        				e.printStackTrace();
                        			}
                        		}
                                scheduleMessage.remove(id);
                            }
                        	}
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // task,delay,period
        t1.scheduleAtFixedRate(tt, 10000, 20000);
      
    }

    public void setMessageSchedulTimer(boolean isSchedulTimerRunning) {
        this.isSchedulTimerRunning = isSchedulTimerRunning;
    }

    public void startIdleTimer() {
        if (isTimerRunning) {
            return;
        }

        t = new Timer();
        TimerTask tt = new TimerTask() {
            public void run() {
                try {

                    long time = 0;
                    switch (currentTimer) {
                        case 1:
                            time = 1;
                            break;
                        case 2:
                            time = 2;
                            break;
                        case 3:
                            time = 5;
                            break;
                        case 4:
                            time = 10;
                            break;
                    }

                    if (time <= 0) {
                        stopIdleTimer();
                        return;
                    }
                    ((ConversationView) CashewnutActivity.currentActivity).checkHibernate(60 * 1000 * time);
                    
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        t.schedule(tt, 10000, 10000);
        isTimerRunning = true;
    }

    public void stopIdleTimer() {
        if (t != null) {
            t.cancel();
            CashewnutActivity.isHibernate = false;
            isTimerRunning = false;
        }
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }
}
