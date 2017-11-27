package com.loment.cashewnut.jsonReceiverModel;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.database.AttachmentStore;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.HeaderModel;

/**
 * 
 * @author sekhar
 */
public class BodyPartsJson {
	private static String MESSAGE_ID = "msg_id";
	private static String MESSAAGE_HEADER = "header";
	private static String MESSAGE_BODY = "body";
	private static String MESSAGE_ATTACHMENTS = "attachments";

	public String getMessageByIdJson(String msgId, String headerId,
			String bodyId, String attachmentId, String token) {

		JSONObject newMessageIdsList = new JSONObject();
		try {
			newMessageIdsList.put(MESSAGE_ID, msgId);
			newMessageIdsList.put(MESSAAGE_HEADER, headerId);
			newMessageIdsList.put(MESSAGE_BODY, bodyId);
			newMessageIdsList.put(MESSAGE_ATTACHMENTS, attachmentId);
			newMessageIdsList.put("token", token);

			return newMessageIdsList.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAttachmentByIdJsonAmqp(int msgId) {

		try {

			HeaderModel headerModel = new HeaderStore().getHeaderById(msgId,
					false);
			AttachmentStore attachmentStore = new AttachmentStore();
			Vector attachmentModelVector = attachmentStore
					.getAttachmentsByHeaderLocalId(headerModel
							.getLocalHeaderId());
			if (attachmentModelVector.size() > 0) {

				JSONObject newMessageIdsList = new JSONObject();
				JSONObject finalObject = new JSONObject();
				int localid = (Integer) attachmentModelVector.elementAt(0);
				AttachmentModel attachmentModel = attachmentStore
						.getAttachmentByAttachmentLocalId(localid);
				String name = attachmentModel.getAttachmentName();
				long attSize = attachmentModel.getAttachmentSize();
				int padding = attachmentModel.getPadding();

				newMessageIdsList.put(MESSAGE_ID, msgId);
				newMessageIdsList.put("attachment_name", name);
				newMessageIdsList.put("size", attSize);
				newMessageIdsList.put("padding", padding);

				finalObject.put("data", newMessageIdsList);
				finalObject.put("cmd", "6");

				return finalObject.toString();

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
