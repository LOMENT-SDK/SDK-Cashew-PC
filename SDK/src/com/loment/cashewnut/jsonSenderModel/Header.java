package com.loment.cashewnut.jsonSenderModel;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loment.cashewnut.database.AttachmentStore;
import com.loment.cashewnut.database.BodyStore;
import com.loment.cashewnut.database.RecepientStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.receiver.Receiver;

/**
 * 
 * @author sekhar
 */
public class Header {

	private static String ID = "id";
	private static String FROM = "from";
	private static String TO = "to";
	private static String CONTROLPARAMETERS = "control_params";
	private static String PRIORITY = "priority";
	private static String MESSAGE_TYPE = "type";
	private static String MESSAGE_FOLDER_TYPE = "folder";
	private static String RESTRICTED = "restricted";
	private static String ACKNOWLEDGEMENT = "ack";
	private static String EXPIRY = "expiry";
	private static String SUBJECT = "subject";
	private static String SUBJECT_SIZE = "size";
	private static String SUBJECT_CONTENT = "content";
	private static String MESSAGE_BODY = "body";
	private static String MESSAGE_BODY_SIZE = "size";
	private static String ATTACHMENT = "attachments";
	private static String ATTACHMENT_COUNT = "count";
	private static String ATTACHMENT_DATA = "data";
	private static String ATTACHMENT_NAME = "name";
	private static String ATTACHMENT_SIZE = "size";
	private static String ATTACHMENT_PADDING = "padding";
	private static String CLIENT_PARAMS = "client_params";
	private static String VERSION = "e_key_v";
	private static String TOKEN = "m_token_v";
	private static String SCHEDULE_TIME="st";
	public Header() {
	}

	public String getHeaderDataAmqp(MessageModel messageModeles, String token) {

		HeaderModel headModel = messageModeles.getHeaderModel();
		Vector attachmentModelVector = messageModeles.getAttachment();
		Vector receipientModelVector = messageModeles.getReceipientLocalIds();

		JSONObject headerMain = new JSONObject();
		JSONObject finalObject = new JSONObject();

		JSONObject header = new JSONObject();
		JSONObject subject = new JSONObject();
		JSONObject body = new JSONObject();
		JSONObject controlParms = new JSONObject();
		JSONObject attachments = new JSONObject();

		JSONObject attachmentParms = new JSONObject();
		Object recepints =null;
		boolean isSelfMessage = false;
		try {

			AccountsModel accountsModel = DBAccountsMapper.getInstance()
					.getAccount();
			String cashewnutId = accountsModel.getCashewnutId();

			//if(receipientModelVector.size()>0){
				
				recepints = new JSONObject();
				
				
							for (int i = 1; i <= receipientModelVector.size(); i++) {
				int localid = Integer.parseInt(receipientModelVector
						.elementAt(i - 1) + "");
				RecipientModel receipientModel = RecepientStore
						.getreceipientsByReceipientLocalId(localid);
				((JSONObject) recepints).put(i + "", receipientModel.getRecepientCashewnutId()
						+ "");
				if (cashewnutId.equals(receipientModel
						.getRecepientCashewnutId() + "")) {
					isSelfMessage = true;
				} 
			}
			//}else{
				//recepints = new JSONArray();
			//}

			controlParms.put(PRIORITY, headModel.getPriority() + "");
			controlParms.put(RESTRICTED, headModel.getRestricted() + "");
			controlParms.put(ACKNOWLEDGEMENT, headModel.getMessageAck() + "");
			controlParms.put(EXPIRY, headModel.getExpiry() + "");

			subject.put(SUBJECT_SIZE, headModel.getSubject().length() + "");
			subject.put(SUBJECT_CONTENT, headModel.getSubject());
			body.put(MESSAGE_BODY_SIZE, getData(headModel).length() + "");

			for (int i = 1; i <= attachmentModelVector.size(); i++) {
				int localid = (Integer) attachmentModelVector.elementAt(i - 1);
				AttachmentModel attachmentModel = new AttachmentStore()
						.getAttachmentByAttachmentLocalId(localid);
				JSONObject attachment = new JSONObject();
				attachment.put(ATTACHMENT_NAME,
						attachmentModel.getAttachmentName());
				attachment.put(ATTACHMENT_SIZE,
						attachmentModel.getAttachmentSize() + "");
				attachment.put(ATTACHMENT_PADDING, attachmentModel.getPadding()
						+ "");
				attachmentParms.put(i + "", attachment);
			}

			attachments
					.put(ATTACHMENT_COUNT, attachmentModelVector.size() + "");
			attachments.put(ATTACHMENT_DATA, attachmentParms);

			JSONObject version = new JSONObject();
			version.put(VERSION, headModel.getHeaderVersion());
			//if (isSelfMessage) {
				version.put(TOKEN, token);
				version.put(SCHEDULE_TIME, headModel.getScheduleTime());
				
			//}

			header.put(CLIENT_PARAMS, version);
			header.put(ATTACHMENT, attachments);
			header.put(MESSAGE_BODY, body);
			header.put(SUBJECT, subject);
			header.put(CONTROLPARAMETERS, controlParms);
			header.put(MESSAGE_TYPE, headModel.getMessageType() + "");
			header.put(MESSAGE_FOLDER_TYPE, "inbox");
			header.put(TO, recepints);
			
			if(receipientModelVector.size() < 1 && headModel.getGroupId() != null){
				header.put("group_id", headModel.getGroupId());
			}
			
			header.put(FROM, headModel.getMessageFrom());
			header.put(ID, "new");			header.put("token", token);

			headerMain.put("header", header);
			headerMain.put("body", getData(headModel));

			finalObject.put("data", headerMain);
			finalObject.put("cmd", "2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalObject.toString() + "\r\n";
	}

	private String getData(HeaderModel headModel) {
		try {
			String body = new BodyStore().getBody(headModel.getLocalHeaderId());
			return body;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
