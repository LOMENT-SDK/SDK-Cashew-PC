/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.jsonSenderModel;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.model.AttachmentModel;

/**
 * 
 * @author sekhar
 */
public class Attachment {
	private static String ATTACHMENT_NAME = "attachment_name";
	private static String ATTACHMENT_SIZE = "size";
	private static String ATTACHMENT_PADDING = "padding";
	private static String MESSAGE_ID = "msg_id";
	private static String TOKEN = "token";

	private static String ATTACHMENT_NAME1 = "attachment_name";
	private static String ATTACHMENT_SIZE1 = "size";
	private static String ATTACHMENT_PADDING1 = "padding";
	private static String MESSAGE_ID1 = "msg_id";
	private static String ATTACGMENT_BODY_DATA1 = "data";
	private static String UPLOADED_FILE_NAME = "uploaded_file_name";

	public String getAttachmentBodyAmqp(AttachmentModel attachment,
			String msgId, String token) {
		String attachmentJsonObj = "{" + "\"cmd\":3,\"" + ATTACGMENT_BODY_DATA1
				+ "\":" + "{\"" + MESSAGE_ID1 + "\":\"" + msgId + "\",\""
				+ ATTACHMENT_NAME1 + "\":\"" + attachment.getAttachmentName()
				+ "\",\"" + ATTACHMENT_SIZE1 + "\":\""
				+ attachment.getAttachmentSize() + "\",\""
				+ ATTACHMENT_PADDING1 + "\":\"" + attachment.getPadding()
				+ "\",\"" + UPLOADED_FILE_NAME + "\":\"";
		return attachmentJsonObj;
	}

	public static String endAttachmentJsonPartAmqp() {
		String json = "\"}}";
		return json;
	}
}
