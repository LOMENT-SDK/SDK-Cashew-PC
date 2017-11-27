package com.loment.cashewnut.sender;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.activity.controller.ThreadIdGenerator;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.BodyModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.util.ContentType;
import com.loment.cashewnut.util.Helper;

/**
 * 
 * @author sekhar
 */

public class SenderHandler {
	private static HeaderSender headerSender = null;
	private static BodySender bodySender = null;
	private static AttachmentSender attachmentSenders = null;
	public static SenderHandler instance = null;
	private static GroupSender groupSender = null;

	private SenderHandler() {
		headerSender = new HeaderSender();
		bodySender = new BodySender();
		attachmentSenders = new AttachmentSender();
		groupSender = new GroupSender();
	}

	public synchronized void send(final MessageModel messageModel) {
		new MessageSender().SendMessage(messageModel);
	}

	public void sendGroup(GroupModel sgModel) {
		getGroupSender().send(sgModel);
	}

	public static SenderHandler getInstance() {
		if (instance == null) {
			instance = new SenderHandler();
		}
		return instance;
	}

	public HeaderSender getHeaderSender() {
		return headerSender;
	}

	public BodySender getBodySender() {
		return bodySender;
	}

	public AttachmentSender getAttachmentSender() {
		return attachmentSenders;
	}

	public GroupSender getGroupSender() {
		return groupSender;
	}

	public MessageModel constructMessage(String from, String to, String subject, String message,
			HashMap<String, String> attachmentsMap, MessageControlParameters controlParameters) {

		MessageModel messageModel = new MessageModel();
		HeaderModel header = messageModel.getHeaderModel();
		BodyModel bodyModel = messageModel.getBodyModel();
		int bodyPartsCount = 2; // header and body
		header.setServerMessageId(-1);
		header.setMessageType(MessageModel.MESSAGE_TYPE_CASHEWNUT);
		header.setMessageFolderType(MessageModel.MESSAGE_FOLDER_TYPE_SENTBOX);
		header.setSendParts(MessageModel.INITIAL_SEND_STATUS);
		header.setCreationTime(System.currentTimeMillis());
		header.setLastUpdateTime(System.currentTimeMillis());
		header.setPriority(Integer.parseInt(controlParameters.getPriority()));
		header.setRestricted(Integer.parseInt(controlParameters.getRestricted()));
		header.setMessageAck(Integer.parseInt(controlParameters.getMessageAck()));
		header.setExpiry(Integer.parseInt(controlParameters.getExpiry()));
		header.setScheduleTime(controlParameters.getMessageSchedule());		
		header.setMessageFrom(from);
		header.setSubject(subject);

		if (!message.equals("")) {
			bodyModel.setBodyContent(message);
		} else {
			bodyModel.setBodyContent("   ");
		}

		if (to.startsWith("grp_") || to.startsWith("GRP_")) {
			header.setHeaderVersion(CashewnutActivity.HEADER_VERSION_V2);
			header.setThreadId(to);
			header.setGroupId(to);
		} else {
			header.setHeaderVersion(CashewnutActivity.HEADER_VERSION);
			// receipients
			Vector<String> reveipientVector = getReceipients(to);
			for (int i = 0; i < reveipientVector.size(); i++) {
				String toValue = recepientOnly(reveipientVector.elementAt(i));
				if (toValue != null && !toValue.equals("")) {
					RecipientModel recepient = new RecipientModel(messageModel);
					recepient.setRecepientCashewnutId(toValue);
					recepient.setReceipientAck(MessageModel.RECIPIENT_STATUS_UNACK);
					recepient.setReceipientReadStatus(MessageModel.RECIPIENT_STATUS_UNREAD);
					messageModel.addReceipient(recepient);
				}
			}

			String threadId = ThreadIdGenerator.getThreadId(from, reveipientVector);
			header.setThreadId(threadId);
		}

		// attachments
		if (attachmentsMap.size() > 0) {
			Set<?> mapSet = (Set<?>) attachmentsMap.entrySet();
			Iterator<?> mapIterator = mapSet.iterator();
			while (mapIterator.hasNext()) {
				Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>) mapIterator.next();
				String attName = (String) mapEntry.getKey();
				String attPath = (String) mapEntry.getValue();

				int paddingsize = 0;
				String uriString = attPath.toString();
				if (uriString.startsWith("file://")) {
					File f = new File(uriString.substring("file://".length()));
					long size = f.length();
					paddingsize = (int) (16 - (size % 16));
					if (paddingsize == 16) {
						paddingsize = 0;
					}
				}

				long size = 0;
				String extension = "";
				try {
					File fc = new File(attPath);
					size = fc.length();
					paddingsize = (int) (16 - (size % 16));
					if (paddingsize == 16) {
						paddingsize = 0;
					}
					extension = ContentType.getContentType(attPath);
				} catch (Exception e) {
					e.printStackTrace();
				}

				AttachmentModel attachment = new AttachmentModel(messageModel);
				attachment.setAttachmentName(attName);
				attachment.setAttachmentFilePath(attPath);
				attachment.setAttachmentSize(size);
				attachment.setPadding(paddingsize);
				attachment.setAttachmentType(extension);
				messageModel.addAttachment(attachment);
				bodyPartsCount++;
			}
		}

		header.setNumberOfBodyparts(bodyPartsCount);
		return messageModel;
	}

	public Vector<String> getReceipients(String s) {

		Vector<String> recp = null;

		try {
			s = s.trim();
			if (s.indexOf(",") == -1) {
				recp = new Vector<String>();
				recp.add(s);
				return recp;
			}
			if (s.endsWith(",")) {
				s = s.substring(0, s.length() - 1);
			}
			if (s.startsWith(",")) {
				s = s.substring(1, s.length());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		recp = Helper.SplittoVector(s, ",");
		return recp;
	}

	public static String recepientOnly(String s) {
		try {
			if (s.indexOf("<") == -1) {
				return s;
			}
			return s.substring(s.indexOf("<") + 1, s.indexOf(">"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void result(String result) {
		// System.out.println(result);
	}

}
