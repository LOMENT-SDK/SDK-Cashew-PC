package com.loment.cashewnut.model;

import java.util.Vector;

public class MessageModel {

	public static int RECIPIENT_STATUS_READ = 1;
	public static int RECIPIENT_STATUS_UNREAD = 0;

	public static int RECIPIENT_STATUS_UNACK = 0;
	public static int RECIPIENT_STATUS_ACK = 1;
	public static int RECIPIENT_INITIAL_DELETED_STATUS =-1;
	
	public static int SENT_DELETED_STATUS = 1;
	public static int RECIPIENT_DELETED_STATUS = 2;
	public static int SELF_DELETED_STATUS = 3; // in and sent

	public static String MESSAGE_FOLDER_TYPE_INBOX = "inbox";
	public static String MESSAGE_FOLDER_TYPE_SENTBOX = "sent";
	public static String MESSAGE_FOLDER_TYPE_SELF = "self";

	public static int MESSAGE_TYPE_CASHEWNUT = 1;
	public static int MESSAGE_TYPE_WELCOME = 2;
	public static int MESSAGE_TYPE_GROUP = 4;
	public static int MESSAGE_TYPE_CONTACT = 3;
	public static int MESSAGE_TYPE_AUTORESPONSE=5;
	public static int MESSAGE_TYPE_NEW_GROUPSMESSAGE=6;
	public static int MESSAGE_TYPE_SCREENSHOT=7;
	public static int MESSAGE_TYPE_DATE = 100;
	public static int LOCAL_MESSAGE_TYPE_GROUP = 101;
	public static int LOCAL_MESSAGE_TIME = 102;
	
	public static int LOCAL_MESSAGE_LOADMORE = 1000;
	public static int INITIAL_SEND_STATUS = 0;

	public static int ACTION_ADDED = 0;
	public static int ACTION_DELETED = 1;
	public static int ACTION_UPDATE = 2;
	public static int ACTION_INPROGRESS = 3;
	public static int ACTION_ATTACHMENT_ADDED = 4;
	public static int ACTION_CONTACT_REQUEST = 5;

	private Vector<AttachmentModel> attachments = null;
	private Vector<RecipientModel> receipients = null;
	private Vector<Integer> receipientLocalIds = null;
	private BodyModel bodyModel = null;
	private HeaderModel headerModel = null;
	private String boxType = "";

	public MessageModel() {
		headerModel = new HeaderModel(this);
		bodyModel = new BodyModel(this);
		attachments = new Vector<AttachmentModel>();
		receipients = new Vector<RecipientModel>();
		receipientLocalIds = new Vector<Integer>();
	}

	public BodyModel getBodyModel() {
		return this.bodyModel;
	}

	public HeaderModel getHeaderModel() {
		return this.headerModel;
	}

	public void setHeaderModel(HeaderModel headerModel) {
		this.headerModel = headerModel;
	}

	public void addAttachment(Vector<AttachmentModel> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(AttachmentModel attachment) {
		attachments.addElement(attachment);
	}

	public Vector<AttachmentModel> getAttachment() {
		return attachments;
	}

	public void addReceipient(Vector<RecipientModel> receipients) {
		this.receipients = receipients;
	}

	public void addReceipient(RecipientModel recipientModel) {
		receipients.addElement(recipientModel);
	}

	public Vector<RecipientModel> getReceipient() {
		return receipients;
	}

	public Vector<Integer> getReceipientLocalIds() {
		return receipientLocalIds;
	}

	public void setReceipientLocalIds(Vector<Integer> receipientLocalIds) {
		this.receipientLocalIds = receipientLocalIds;
	}

	public String getBoxType() {
		return boxType;
	}

	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}

}
