package com.loment.cashewnut.jsonReceiverModel;

import com.loment.cashewnut.activity.controller.ThreadIdGenerator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.BodyModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.util.ContentType;

/**
 *
 * @author sekhar
 */
public class Header {

	int index = -1;
	public static String BODY_DATA = "data"; // "body_data";
	public static String ATT_DATA = "data"; // "att_data";
	private static String CLIENT_PARAMS = "client_params";
	private static String KEY_VERSION = "e_key_v";
	private static String TOKEN = "m_token_v";
	private static String SCHEDULE_TIME = "st";
	Vector<String> receipientVector = new Vector<String>();

	public Header() {
	}

	public MessageModel getHeaderUpdatedFlagsData(String jsonString, String cashewnutId) {
		try {
			boolean isGroupMessage = false;
			MessageModel messageModeles = new MessageModel();
			HeaderModel headModel = messageModeles.getHeaderModel();
			Vector<RecipientModel> receipientModelVector = messageModeles.getReceipient();

			// Variables
			String id = "";
			String from = "";
			JSONArray receipients = null;
			String group_id = "";
			String type = "";
			String folder = "";

			JSONObject controlParameters = null;
			String priority = "";
			String restricted = "";
			String ack = "";
			String expiry = "";

			JSONObject status = null;
			JSONArray receipientReadStatus = null;
			JSONArray receipientAckStatus = null;
			JSONArray receipientDeleted = null;
			String selfDeleted = "";

			JSONObject jsonMessageObject = new JSONObject(jsonString);
			JSONObject header = null;

			if (jsonMessageObject.has("header")) {
				header = (JSONObject) jsonMessageObject.get("header");
			} else if (jsonMessageObject.has("id")) {
				header = new JSONObject(jsonString);
			}

			// Set values
			id = header.getString("id");
			from = header.getString("from");
			receipients = header.getJSONArray("to");

			if (header.has("group_id") && !header.isNull("group_id")) {
				Object gid = header.get("group_id");
				if (gid != null) {
					if (gid instanceof String) {
						group_id = header.getString("group_id");
						isGroupMessage = true;
					} else if (gid instanceof JSONObject) {
						if (header.getJSONObject("group_id") != null) {
							group_id = header.getJSONObject("group_id").toString();
							isGroupMessage = true;
						}
					}
				}
			}

			type = header.getString("type");
			folder = header.getString("folder");

			controlParameters = header.getJSONObject("control_params");
			priority = controlParameters.getString("priority");
			restricted = controlParameters.getString("restricted");
			ack = controlParameters.getString("ack");
			expiry = controlParameters.getString("expiry");

			status = header.getJSONObject("status");
			receipientReadStatus = status.getJSONArray("read");
			receipientAckStatus = status.getJSONArray("ack");
			receipientDeleted = status.getJSONArray("recipient_deleted");
			selfDeleted = status.getString("self_deleted");

			for (int i = 0; i < receipients.length(); i++) {
				if (receipients.getString(i).equals(cashewnutId)) {
					this.index = i;
					break;
				}
			}

			if (index != -1 && !cashewnutId.equals(from)) {
				RecipientModel recepient = new RecipientModel(messageModeles);
				// in message
				String receipientReadStatusValue = receipientReadStatus.getString(index);
				String receipientAckStatusValue = receipientAckStatus.getString(index);
				String receipientDeletedValue = receipientDeleted.getString(index);
				String receipientsCashewnutIdVal = receipients.getString(index);

				recepient.setReceipientAck(Integer.parseInt(receipientAckStatusValue));
				recepient.setReceipientReadStatus(Integer.parseInt(receipientReadStatusValue));
				recepient.setRecepientCashewnutId(receipientsCashewnutIdVal);
				recepient.setReceipientDeleteStatus(Integer.parseInt(receipientDeletedValue));
				receipientModelVector.add(recepient);
				receipientVector.add(receipientsCashewnutIdVal);
				messageModeles.setBoxType(MessageModel.MESSAGE_FOLDER_TYPE_INBOX);

			} else {
				// sent messaege && self messagge
				for (int i = 0; i < receipients.length(); i++) {
					String receipientsCashewnutIdVal = receipients.getString(i);
					if (!from.equals(receipientsCashewnutIdVal) || !isGroupMessage){
						RecipientModel recepient = new RecipientModel(messageModeles);
						String receipientReadStatusValue = receipientReadStatus.getString(i);
						String receipientAckStatusValue = receipientAckStatus.getString(i);
						String receipientDeletedValue = receipientDeleted.getString(i);

						recepient.setReceipientAck(Integer.parseInt(receipientAckStatusValue));
						recepient.setReceipientReadStatus(Integer.parseInt(receipientReadStatusValue));
						recepient.setRecepientCashewnutId(receipientsCashewnutIdVal);
						recepient.setReceipientDeleteStatus(Integer.parseInt(receipientDeletedValue));
						receipientModelVector.add(recepient);
						receipientVector.add(receipientsCashewnutIdVal);
						messageModeles.setBoxType(MessageModel.MESSAGE_FOLDER_TYPE_SENTBOX);
					}
				}

				if (index != -1 && cashewnutId.equals(from) && !isGroupMessage) {
					// self message
					messageModeles.setBoxType(MessageModel.MESSAGE_FOLDER_TYPE_SELF);
				}
			}

			if (isGroupMessage) {
				headModel.setGroupId(group_id);
				headModel.setThreadId(group_id);
			} else {
				headModel.setGroupId("");
				String threadId = ThreadIdGenerator.getThreadId(from, receipientVector);
				headModel.setThreadId(threadId);
			}

			headModel.setServerMessageId(Integer.parseInt(id));
			headModel.setMessageType(Integer.parseInt(type));
			headModel.setMessageFrom(from);
			headModel.setMessageFolderType(folder);
			headModel.setPriority(Integer.parseInt(priority));
			headModel.setRestricted(Integer.parseInt(restricted));
			headModel.setMessageAck(Integer.parseInt(ack));
			headModel.setExpiry(Integer.parseInt(expiry));
			headModel.setDeleteStatus(Integer.parseInt(selfDeleted));

			return messageModeles;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getLastMessageSyncTime(String jsonString) {
		try {
			JSONObject jsonMessageObject = new JSONObject(jsonString);
			JSONObject header = (JSONObject) jsonMessageObject.get("header");
			String lastUpdateTime = header.getString("last_update_timestamp");
			return lastUpdateTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public MessageModel getHeaderDataAmqp(String jsonString, String cashewnutId) {
		try {
			MessageModel messageModeles = new MessageModel();
			HeaderModel headModel = messageModeles.getHeaderModel();
			Vector<AttachmentModel> attachmentModelVector = messageModeles.getAttachment();
			Vector<RecipientModel> receipientModelVector = messageModeles.getReceipient();

			// Variables
			String id = "";
			String from = "";
			JSONArray receipients = null;
			String type = "";
			String folder = "";
			String creationTimeStamp = "";
			// String lastUpdateTime = "";
			String group_id = "";
			JSONObject controlParameters = null;
			String priority = "";
			String restricted = "";
			String ack = "";
			String expiry = "";

			JSONObject status = null;
			JSONArray receipientFolder = null;
			JSONArray receipientReadStatus = null;
			JSONArray receipientAckStatus = null;
			JSONArray receipientDeleted = null;
			String selfDeleted = "";

			JSONObject subject = null;
			// String subjectSize = "";
			String subjectContent = "";

			// String bodySize = "";
			JSONObject attachments = null;
			int attachmentCount = 0;
			JSONArray attachmentDetails = null;

			JSONObject jsonMessageObject = new JSONObject(jsonString);
			JSONObject header = (JSONObject) jsonMessageObject.get("header");

			// Set values
			id = header.getString("id");
			from = header.getString("from");
			receipients = header.getJSONArray("to");
			if (header.has("group_id") && !header.isNull("group_id")) {
				Object gid = header.get("group_id");
				if (gid != null) {
					if (gid instanceof String) {
						group_id = header.getString("group_id");
					}
					if (gid instanceof JSONObject) {
						if (header.getJSONObject("group_id") != null) {
							group_id = header.getJSONObject("group_id").toString();
						}
					}
				}
			}
			type = header.getString("type");
			folder = header.getString("folder");
			creationTimeStamp = header.getString("creation_timestamp");
			// lastUpdateTime = header.getString("last_update_timestamp");

			controlParameters = header.getJSONObject("control_params");
			priority = controlParameters.getString("priority");
			restricted = controlParameters.getString("restricted");
			ack = controlParameters.getString("ack");
			expiry = controlParameters.getString("expiry");

			status = header.getJSONObject("status");
			receipientFolder = status.getJSONArray("folder");
			receipientReadStatus = status.getJSONArray("read");
			receipientAckStatus = status.getJSONArray("ack");
			receipientDeleted = status.getJSONArray("recipient_deleted");
			selfDeleted = status.getString("self_deleted");

			subject = header.getJSONObject("subject");
			// subjectSize = subject.getString("size");
			subjectContent = subject.getString("content");
			// bodySize = header.getString("body");

			if (group_id != null && !group_id.trim().equalsIgnoreCase("")
					&& !group_id.trim().equalsIgnoreCase("null")) {
				headModel.setGroupId(group_id);
			} else {
				headModel.setGroupId("");
			}

			if (header.has(CLIENT_PARAMS)) {
				Object params = header.optJSONObject(CLIENT_PARAMS);
				if (params != null && params instanceof JSONObject) {
					JSONObject clientParams = (JSONObject) params;
					if (clientParams.has(KEY_VERSION)) {
						String keyVersion = clientParams.getString(KEY_VERSION);
						headModel.setHeaderVersion(keyVersion);
					}
					if (clientParams.has(TOKEN)) {
						String token = clientParams.getString(TOKEN);
						headModel.setToken(token);
					}
					if (clientParams.has(SCHEDULE_TIME)) {

						Object time = clientParams.get(SCHEDULE_TIME);
						if (time instanceof Long) {
							Long scheduleTime = (Long) (time);
							headModel.setScheduleTime(scheduleTime);
						} else {
							headModel.setScheduleTime(-1);
						}

					}
				}
			}

			attachments = header.getJSONObject("attachments");
			attachmentCount = attachments.getInt("count");
			if (attachments.has("data")) {
				attachmentDetails = attachments.getJSONArray("data");
			}

			for (int i = 0; i < receipients.length(); i++) {
				if (receipients.getString(i).equals(cashewnutId)) {
					this.index = i;
					break;
				}
			}

			headModel.setNumberOfBodyparts(2);
			if (attachmentCount > 0) {
				for (int i = 0; i < attachmentDetails.length(); i++) {

					JSONObject attachmentjsonObj = attachmentDetails.getJSONObject(i);
					AttachmentModel attachment = new AttachmentModel(messageModeles);
					attachment.setAttachmentName(attachmentjsonObj.getString("name"));

					String extension = ContentType.getContentType(attachmentjsonObj.getString("name"));
					attachment.setAttachmentType(extension);
					attachment.setAttachmentSize(Integer.parseInt(attachmentjsonObj.getString("size")));
					attachment.setPadding(Integer.parseInt(attachmentjsonObj.getString("padding")));
					attachmentModelVector.add(attachment);
				}
				headModel.setNumberOfBodyparts(attachmentDetails.length() + 2);
			}

			if (index != -1 && !cashewnutId.equals(from)) {

				for (int i = 0; i < receipients.length(); i++) {
					RecipientModel recepient = new RecipientModel(messageModeles);
					String receipientFolderValue = receipientFolder.getString(i);
					String receipientReadStatusValue = receipientReadStatus.getString(i);
					String receipientAckStatusValue = receipientAckStatus.getString(i);
					String receipientDeletedValue = receipientDeleted.getString(i);
					String receipientsCashewnutIdVal = receipients.getString(i);
					recepient.setReceipientAck(Integer.parseInt(receipientAckStatusValue));
					recepient.setReceipientReadStatus(Integer.parseInt(receipientReadStatusValue));
					recepient.setRecepientCashewnutId(receipientsCashewnutIdVal);
					recepient.setReceipientDeleteStatus(Integer.parseInt(receipientDeletedValue));
					receipientModelVector.add(recepient);
					receipientVector.add(receipientsCashewnutIdVal);
				}
			} else {
				// sent messaege && self messagge
				for (int i = 0; i < receipients.length(); i++) {
					RecipientModel recepient = new RecipientModel(messageModeles);
					String receipientFolderValue = receipientFolder.getString(i);
					String receipientReadStatusValue = receipientReadStatus.getString(i);
					String receipientAckStatusValue = receipientAckStatus.getString(i);
					String receipientDeletedValue = receipientDeleted.getString(i);
					String receipientsCashewnutIdVal = receipients.getString(i);

					recepient.setReceipientAck(Integer.parseInt(receipientAckStatusValue));
					recepient.setReceipientReadStatus(Integer.parseInt(receipientReadStatusValue));
					recepient.setRecepientCashewnutId(receipientsCashewnutIdVal);
					recepient.setReceipientDeleteStatus(Integer.parseInt(receipientDeletedValue));
					receipientModelVector.add(recepient);
					receipientVector.add(receipientsCashewnutIdVal);
				}
				if (index != -1 && cashewnutId.equals(from)) {
					// self message
				}
				headModel.setSendParts(headModel.getNumberOfBodyparts());
			}

			headModel.setServerMessageId(Integer.parseInt(id));
			headModel.setMessageType(Integer.parseInt(type));
			headModel.setMessageFrom(from);
			headModel.setMessageFolderType(folder);
			headModel.setPriority(Integer.parseInt(priority));
			headModel.setRestricted(Integer.parseInt(restricted));
			headModel.setMessageAck(Integer.parseInt(ack));
			headModel.setExpiry(Integer.parseInt(expiry));
			headModel.setSubject(subjectContent);
			headModel.setDeleteStatus(Integer.parseInt(selfDeleted));

			if (group_id != null && !group_id.trim().equalsIgnoreCase("")
					&& !group_id.trim().equalsIgnoreCase("null")) {
				headModel.setGroupId(group_id);
				headModel.setThreadId(group_id);
			} else {
				headModel.setGroupId("");
				String threadId = ThreadIdGenerator.getThreadId(from, receipientVector);
				headModel.setThreadId(threadId);
			}

			try {
				headModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
				headModel.setLastUpdateTime(getTimeStampCurrentTimeZone(creationTimeStamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return messageModeles;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long getTimeStampCurrentTimeZone(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date date = sdf.parse(time);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date.getTime());
			calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));

			// System.out.println(calendar.getTime().getTime());
			return calendar.getTime().getTime();
		} catch (Exception e) {
		}
		return 0;
	}

	public BodyModel getBodyData(String jsonString, MessageModel messageModel) {
		try {
			JSONObject jsonMessageObject = new JSONObject(jsonString);
			if (jsonMessageObject.has("body")) {
				BodyModel bodyModel = messageModel.getBodyModel();
				JSONObject body = (JSONObject) jsonMessageObject.get("body");
				if (body instanceof JSONObject) {
					bodyModel.setServerMessageId(body.getString("msg_id"));
					bodyModel.setBodyContent(body.getString(BODY_DATA));
					return bodyModel;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BodyModel getBodyDataAmqp(String jsonString, MessageModel messageModel, String messageId) {
		try {
			JSONObject jsonMessageObject = new JSONObject(jsonString);
			if (jsonMessageObject.has("body")) {
				BodyModel bodyModel = messageModel.getBodyModel();
				Object value = jsonMessageObject.get("body");
				if (value instanceof JSONObject) {
					JSONObject body = (JSONObject) value;
					bodyModel.setServerMessageId(body.getString("msg_id"));
					bodyModel.setBodyContent(body.getString(BODY_DATA));
					return bodyModel;
				} else if (value instanceof String) {
					bodyModel.setServerMessageId(messageId);
					bodyModel.setBodyContent((String) value);
					return bodyModel;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
