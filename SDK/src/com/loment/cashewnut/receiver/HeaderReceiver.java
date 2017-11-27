package com.loment.cashewnut.receiver;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.activity.list.ControlMessageOptions;
import com.loment.cashewnut.activity.list.ConversationView;
import com.loment.cashewnut.connection.ReceipientConnection;
import com.loment.cashewnut.database.AttachmentStore;
import com.loment.cashewnut.database.BodyStore;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.database.RecepientStore;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBAutoResponseMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.database.mappers.DBMessageSyncDataMapper;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.jsonReceiverModel.Header;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.AutoResponseModel;
import com.loment.cashewnut.model.BodyModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.model.SettingsModel;
import com.loment.cashewnut.sender.SenderHandler;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

//import com.loment.cashewnut.database.mappers.DBContactsMapper;
/**
 *
 * @author sekhar
 */
public class HeaderReceiver implements ReceiveServerRespListener {

	private HashMap<String, String> tokenMapper = new HashMap<String, String>();
	private String cashewnutId = "";
	public static String RECEIVED_HEADER_TOKEN_AMQP = "cashew_header";
	public static String RECEIVED_CONTACT_HEADER="contact_header";
	private HashMap<String, Message> headerMapper = new HashMap<String, Message>();
	long lastMessageTime = 0;
	int size = 0;

	public HeaderReceiver() {
		try {
			Receiver.getInstance().addReceiverListener(this);
			AccountsModel accountsModel = DBAccountsMapper.getInstance()
					.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
			startIdleTimer();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void listenForResponse(String token, String response) {

		lastMessageTime = System.currentTimeMillis();
		if (token.equals(RECEIVED_HEADER_TOKEN_AMQP)) {
			try {
				JSONObject jsonMessageObject = new JSONObject(response);
				if (jsonMessageObject.has("header")) {
					JSONObject header = (JSONObject) jsonMessageObject
							.get("header");
					if (header.length() > 0) {
						int serverMessageId = Integer.parseInt(header
								.getString("id"));
						HeaderModel headerModel = new HeaderStore()
								.getHeaderById(serverMessageId, false);
						if (headerModel != null
								&& headerModel.getLocalHeaderId() != -1) {
							// update header
							updateMessage(headerModel, response);
						} else {
							
							saveMessage(response);
					
							 }
							
												Thread.sleep(300);
					}
					saveLastMessageSyncTime(response);
				}
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
		
		
		
	
		
	}

	private void saveLastMessageSyncTime(String response) {
		try {
			String time = Header.getLastMessageSyncTime(response);
			if (time == null || time.trim().equals("")) {
				return;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = sdf.parse(time);

			String lastMessageSyncTime = getLastMessageSyncTime();
			if (lastMessageSyncTime == null
					|| lastMessageSyncTime.trim().equals("")
					|| date.compareTo(sdf.parse(lastMessageSyncTime)) > 0) {

				DBMessageSyncDataMapper mapper = DBMessageSyncDataMapper
						.getInstance();

				// SharedPreferences preferences = Preferences.getPreferences(
				// CashewnutApplication.app).getPreferences();
				// SharedPreferences.Editor editor = preferences.edit();

				// editor.putString("lastMessageSyncTime", time);
				// editor.commit();

				mapper.saveLastMessageSyncTime(time);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static String getLastMessageSyncTime() {
		try {

			DBMessageSyncDataMapper mapper = DBMessageSyncDataMapper
					.getInstance();
			return mapper.getLastMessageSyncTime();

			// SharedPreferences preferences = Preferences.getPreferences(
			// CashewnutApplication.app).getPreferences();
			// String replyQueueName = preferences.getString(
			// "lastMessageSyncTime", "");
			// return replyQueueName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}	
	private void saveMessage(String response) {
		Header headerJson = new Header();
		HeaderModel headerModel = null;
		MessageModel messageModel = headerJson.getHeaderDataAmqp(response,
				cashewnutId);

		if (isExpiredMessage(messageModel)) {
			return;
		}
		if (isSelfSentMessage(messageModel)) {
			return;
		}

		int localHeaderId = new HeaderStore().saveHeader(messageModel);
		if (localHeaderId != -1) {
			try {
				RecepientStore receipientStore = new RecepientStore();
				BodyStore bodyStore = new BodyStore();
				AttachmentStore attachmentStore = new AttachmentStore();

				receipientStore.saveRecepients(messageModel, localHeaderId);
				attachmentStore.saveAttachments(messageModel, localHeaderId);
				BodyModel bodyModel = headerJson.getBodyDataAmqp(response,
						messageModel, messageModel.getHeaderModel()
								.getServerMessageId() + "");

				if (bodyModel != null) {
					bodyModel.setLocalHeaderId(localHeaderId);
					bodyStore.saveBody(messageModel, localHeaderId, true);
				}
			} catch (Exception e1) {
			}

			headerModel = messageModel.getHeaderModel();
			headerModel.setLocalHeaderId(localHeaderId);
			// try {
			addToList(headerModel, MessageModel.ACTION_ADDED);
			// notification(messageModel);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			
		 SettingsModel model = new SettingsStore().getSettingsData();
		 if(model!=null)
		 {
			 if(model.getAutoResponseMessage().length()>1)
			 {
				 HashMap<String, String> attachmentMap=new HashMap<String,String>();
				 MessageControlParameters controlParameters = new MessageControlParameters();		
				 controlParameters.setPriority(MessageControlParameters.PRIORITY_INDICATOR_GRAY + "");
				 controlParameters.setExpiry("-1");
					controlParameters.setRestricted("-1");
					controlParameters.setMessageAck("-1");
					controlParameters.setMessageSchedule(-1);
				 DBAutoResponseMapper autoResponse=DBAutoResponseMapper.getInstance();
				 AutoResponseModel autoResponseModel=new AutoResponseModel();
				 autoResponseModel= autoResponse.getAutoResponseContact(headerModel.getMessageFrom());
				 if(autoResponseModel!=null)
				 {					
					 Date date = new Date(autoResponseModel.getTimestamp().getTime());
					 Format format = new SimpleDateFormat("yyyy MM dd");
					 format.format(date);
					 java.util.Date date1 = new Date();
					 format.format(date1);					 
					 if(!(format.format(date1).equals(format.format(date)))||!(autoResponseModel.getMessage().equalsIgnoreCase(model.getAutoResponseMessage())))
					 {
						 autoResponse.insert(headerModel.getMessageFrom(), model.getAutoResponseMessage());
						 final MessageModel messageModel1 = SenderHandler.getInstance().constructMessage(cashewnutId,
									headerModel.getMessageFrom(), "  ", model.getAutoResponseMessage(), attachmentMap, controlParameters);									
							SenderHandler.getInstance().send(messageModel1); 
					 }
				 }
				 else
				 {
					 autoResponse.insert(headerModel.getMessageFrom(), model.getAutoResponseMessage());
					 final MessageModel messageModel1 = SenderHandler.getInstance().constructMessage(cashewnutId,
								headerModel.getMessageFrom(), "  ", model.getAutoResponseMessage(), attachmentMap, controlParameters);									
						SenderHandler.getInstance().send(messageModel1);
				 }
				
					
			 }
		 }
		}
	}

	private boolean isSelfSentMessage(MessageModel messageModel) {
		// TODO Auto-generated method stub
		String token = messageModel.getHeaderModel().getToken();
		if (token != null && token.trim().length() > 0) {
			return SenderHandler.getInstance().getHeaderSender()
					.isTokenContains(token);
		}
		return false;
	}

	private boolean isExpiredMessage(MessageModel messageModel) {
		if (messageModel.getHeaderModel().getMessageFrom().equals(cashewnutId)) {
			if (messageModel.getHeaderModel().getDeleteStatus() > 0) {
				return true;
			}
		} else {
			Vector recpVector = messageModel.getReceipient();
			for (int i = 0; i < recpVector.size(); i++) {
				RecipientModel recepient = (RecipientModel) recpVector.get(i);
				if (recepient.getRecepientCashewnutId().equals(cashewnutId)) {
					if (recepient.getReceipientDeleteStatus() > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void notification(MessageModel messageModel) {
		Vector<RecipientModel> recipientList = messageModel.getReceipient();
		for (int i = 0; i < recipientList.size(); i++) {
			RecipientModel recipient = (RecipientModel) recipientList
					.elementAt(i);
			if (cashewnutId.equals(recipient.getRecepientCashewnutId())) {
				if (recipient.getReceipientReadStatus() != MessageModel.RECIPIENT_STATUS_READ) {
					// play sound notification
					// status bar notification
					CashewnutApplication.createNotification(messageModel, 1);
				}
			}
		}
	}

	private void updateMessage(HeaderModel headerModel, String response) {
		if (headerModel.getLocalHeaderId() != -1) {
			MessageModel messageUpdateModel = new Header()
					.getHeaderUpdatedFlagsData(response, cashewnutId);

			try {
				if (isExpiredMessage(messageUpdateModel)) {
					new MessageStore().removeMessageByHeaderId(headerModel);
					// addToList(headerModel, MessageModel.ACTION_DELETED);
					return;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (messageUpdateModel.getBoxType().equals(
					MessageModel.MESSAGE_FOLDER_TYPE_INBOX)) {
				// update values
				try {
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
							if (updatedRecpModel
									.getRecepientCashewnutId()
									.equals(recpModel.getRecepientCashewnutId())
									&& recpModel.getRecepientCashewnutId()
											.equals(cashewnutId)) {
								if (recpModel.getReceipientReadStatus() < MessageModel.RECIPIENT_STATUS_READ) {
									recpModel
											.setReceipientReadStatus(updatedRecpModel
													.getReceipientReadStatus());
								}
								if (recpModel.getReceipientAck() < MessageModel.RECIPIENT_STATUS_ACK) {
									recpModel.setReceipientAck(updatedRecpModel
											.getReceipientAck());
								}

								DBRecipientMapper.getInstance().update(
										recpModel,
										recpModel.getRecepientLocalId());
							}
						}

					}
					// if (CashewnutActivity.getCurrentRunningActivity() !=
					// null) {
					addToList(headerModel, MessageModel.ACTION_UPDATE);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (messageUpdateModel.getBoxType().equals(
					MessageModel.MESSAGE_FOLDER_TYPE_SENTBOX)) {
				try {
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
							if (updatedRecpModel
									.getRecepientCashewnutId()
									.equals(recpModel.getRecepientCashewnutId())) {
								recpModel
										.setReceipientReadStatus(updatedRecpModel
												.getReceipientReadStatus());
								recpModel.setReceipientAck(updatedRecpModel
										.getReceipientAck());
								DBRecipientMapper.getInstance().update(
										recpModel,
										recpModel.getRecepientLocalId());
							}
						}
					}
					// if (CashewnutActivity.getCurrentRunningActivity() !=
					// null) {
					addToList(headerModel, MessageModel.ACTION_UPDATE);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	DBLomentDataMapper lomentDataMapper = DBLomentDataMapper.getInstance();
	LomentDataModel lomentDataModel = lomentDataMapper.getLomentData();
	String lomentId = lomentDataModel.getLomentId();
	String password = lomentDataModel.getPassword();
	DBContactsMapper profileMapper = DBContactsMapper.getInstance();

	private void getGroupDetailsFromServer(HeaderModel headerModel) {
		try {
			final String group_id = headerModel.getGroupId();
			if (group_id != null && !group_id.trim().equalsIgnoreCase("")) {
				final GroupModel gModel = DBGroupsMapper.getInstance()
						.getGroup(group_id, true);
				gModel.setFrom(cashewnutId);
				if (gModel.getGroupName().equals("")
						|| gModel.getMembers() == null
						|| gModel.getMembers().length() == 0) {

					if (!CashewnutApplication.isNetworkConnected()) {
						return;
					}
					new Thread(new Runnable() {
						public void run() {

							if (gModel.getGroupId() < 0) {
								gModel.setServerGroupId(group_id);
								int localId = (int) DBGroupsMapper
										.getInstance().insert(gModel);
								gModel.setGroupId(localId);
							}

							gModel.setOperation(GroupModel.OPERATION_GET_GROUP_DATA);
							SenderHandler.getInstance().sendGroup(gModel);
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getRecepientNameFromSthithiIfNotExists(HeaderModel headerModel) {
		DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
		Vector recipientList = recipientMapper
				.getReceipientsModelByHeaderId(headerModel.getLocalHeaderId());
		ContactsModel contactsModel = null;
		for (int i = 0; i < recipientList.size() && recipientList.size() > 0; i++) {
			RecipientModel recipient = (RecipientModel) recipientList
					.elementAt(i);
			String cashewId = recipient.getRecepientCashewnutId();
			if (cashewId == null || cashewId.trim().length() < 6) {
				continue;
			}
			contactsModel = profileMapper.getContact(cashewId,0);

			if (contactsModel.getFirstName() == null
					|| contactsModel.getFirstName().trim().length() < 1) {
				if (contactsModel.getPhone() == null
						|| contactsModel.getPhone().trim().length() < 1) {
					ReceipientConnection.getInstance().get(
							recipient.getRecepientCashewnutId().trim());
				}
			}
		}

		contactsModel = profileMapper.getContact(headerModel.getMessageFrom(),0);
		if (contactsModel.getFirstName() == null
				|| contactsModel.getFirstName().trim().length() < 1) {
			if (contactsModel.getPhone() == null
					|| contactsModel.getPhone().trim().length() < 1) {
				ReceipientConnection.getInstance().get(
						headerModel.getMessageFrom().trim());
			}
		}
	}

	// ======================================================================
	private void createTask(final Object object, final int type) {
		MessageData data = new MessageData();
		data.setObject(object);
		data.setType(type);
		messageQueue.add(data);
		if (messagePushListner == null) {
			messagePushListner = new MeassagePushListner("messagePushListner");
		}
		messagePushListner.sendData();
	}

	class MeassagePushListner implements Runnable {

		private Thread thread;
		private final String threadName;

		public MeassagePushListner(String name) {
			this.threadName = name;
		}

		public void sendData() {
			if (thread == null || !thread.isAlive()) {
				thread = new Thread(this, threadName);
				thread.setDaemon(true);
				thread.start();
			}
		}

		@Override
		public void run() {

			Service<Void> service = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							// Background work
							final CountDownLatch latch = new CountDownLatch(1);
							try {
								while (messageQueue.size() > 0) {
									MessageData data = messageQueue.get(0);
									try {
										addToList(data);
										messageQueue.remove(0);
									} catch (Exception e) {
									}
								}
							} finally {
								latch.countDown();
							}
							latch.await();
							return null;
						}

					};
				}
			};
			service.start();
		}
	}

	private void addToList(MessageData data) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				// System.out.println("fire msg to conversation .....");
				// fire to list
				((ConversationView) CashewnutActivity.currentActivity)
						.processMessage(data.getObject(), data.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final Vector<MessageData> messageQueue = new Vector<MessageData>();
	private static final Vector<String> threadQueue = new Vector<String>();
	private static MeassagePushListner messagePushListner;

	// ===============================================================================

	public void addToList(HeaderModel headerModel, int action) {
		lastMessageTime = System.currentTimeMillis();
		if (action == MessageModel.ACTION_ADDED) {
			// if (!threadQueue.contains(headerModel.getThreadId())) {
			// threadQueue.add(headerModel.getThreadId());
			// createTask(headerModel, action);
			// } else {
			// Message message = new Message();
			// message.setAction(action);
			// message.setHeaderModel(headerModel);
			// headerMapper.put(headerModel.getThreadId(), message);
			// }
			if(headerModel.getScheduleTime()>System.currentTimeMillis())
			{   
				if(!(headerModel.getMessageFrom().equals(cashewnutId)))
				{
				int id=headerModel.getLocalHeaderId();
				headerModel.setCreationTime(headerModel.getScheduleTime());
				new HeaderStore().updateScheduleMessages(headerModel,id);
				ControlMessageOptions.setScheduleThread(headerModel.getLocalHeaderId(), headerModel);
				}
			}
			else
			{
			MessageData data = new MessageData();
			data.setObject(headerModel);
			data.setType(action);

			addToList(data);
			lastMessageTime = System.currentTimeMillis();
			}
		} else {
			// update and delete
			Message message = new Message();
			message.setAction(action);
			message.setHeaderModel(headerModel);
			headerMapper.put(headerModel.getThreadId(), message);
		}
	}
	public static String getTimeStampCurrentTimeZone(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//try {
		java.util.Date date = sdf.parse(time);

			/*Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date.getTime());
			calendar.add(Calendar.MILLISECOND,TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
			return calendar.getTime().getTime();*/
			return null;
		//} catch (Exception e) {
		//}
		//return date;
	}

	public final void startIdleTimer() {
		if (isTimerRunning) {
			return;
		}
		t = new Timer();
		TimerTask tt = new TimerTask() {
			public void run() {
				if ((CashewnutApplication.isAmqpQueueEmpty || lastMessageTime + 4000 < System
						.currentTimeMillis()) && headerMapper.size() > 0) {
					Iterator<Map.Entry<String, Message>> iterator = headerMapper
							.entrySet().iterator();
					while (iterator.hasNext()) {
						try {
							Map.Entry<String, Message> e = iterator.next();
							createTask(e.getValue().getHeaderModel(), e
									.getValue().getAction());
							iterator.remove();
							Thread.sleep(100);
						} catch (Exception ex) {
						}
					}
				}

				if (headerMapper.isEmpty()) {
					// stopIdleTimer();
				}
			}
		};
		t.schedule(tt, 1000, 1000);
		isTimerRunning = true;
	}

	public void stopIdleTimer() {
		if (t != null) {
			t.cancel();
			isTimerRunning = false;
		}
	}

	public boolean isTimerRunning() {
		return isTimerRunning;
	}

	private boolean isTimerRunning = false;
	private Timer t = new Timer();

	private class Message {

		int action;
		HeaderModel headerModel;

		public HeaderModel getHeaderModel() {
			return headerModel;
		}

		public void setHeaderModel(HeaderModel headerModel) {
			this.headerModel = headerModel;
		}

		public int getAction() {
			return action;
		}

		public void setAction(int action) {
			this.action = action;
		}
	}
}
