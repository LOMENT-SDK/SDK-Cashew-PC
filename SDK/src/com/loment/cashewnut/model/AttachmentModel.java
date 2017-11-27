/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.model;

/**
 * 
 * @author sekhar
 */
public class AttachmentModel {
	
	public static final int ID_IMAGE = 0;
	public static final int ID_VIDEO = 1;
	public static final int ID_AUDIO = 2;
	public static final int ID_FILE = 3;
	public static final int ID_CAPTURE = 4;
	public static final int ID_RECORD = 5;
	public static final int ID_CONTACT = 6;
	public static final int ID_LOCATION = 7;

	public static final int REQUEST_CODE_ATTACH_IMAGE = 1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 2;
	public static final int REQUEST_CODE_ATTACH_VIDEO = 3;
	public static final int REQUEST_CODE_TAKE_VIDEO = 4;
	public static final int REQUEST_CODE_ATTACH_SOUND = 5;
	public static final int REQUEST_CODE_RECORD_SOUND = 6;
	public static final int REQUEST_CODE_ATTACH_FILE = 7;
	public static final int REQUEST_CODE_PICK_LOCATION = 8;
	public static final int REQUEST_CODE_ADD_CONTACT = 9;
	public static final int REQUEST_CODE_PICK = 10;
	public static final int REQUEST_CODE_ECM_EXIT_DIALOG = 11;

	private int localAttachmentId;
	private String attachmentName; 
	private String attachmentFilePath; 
	private String attachmentType; 
	private int padding; 
	private long attachmentSize;
	private int localHeaderId;
	private String attachmentOriginalFilePath; 
	
	private MessageModel messageModel= null;
	
	public AttachmentModel(MessageModel messageModel){
		this.messageModel = messageModel;
	}
	
	public int getLocalHeaderId() {
		return localHeaderId;
	}

	public void setLocalHeaderId(int localHeaderId) {
		this.localHeaderId = localHeaderId;
	}

	
	public MessageModel getMessageHeaderModel() {
		return messageModel;
	}
	
	public int getLocalAttachmentId() {
		return localAttachmentId;
	}
	public void setLocalAttachmentId(int localAttachmentId) {
		this.localAttachmentId = localAttachmentId;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentFilePath() {
		return attachmentFilePath;
	}
	public void setAttachmentFilePath(String attachmentFilePath) {
		this.attachmentFilePath = attachmentFilePath;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public int getPadding() {
		return padding;
	}
	public void setPadding(int padding) {
		this.padding = padding;
	}
	public long getAttachmentSize() {
		return attachmentSize;
	}
	public void setAttachmentSize(long attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	public String getAttachmentOriginalFilePath() {
		return attachmentOriginalFilePath;
	}

	public void setAttachmentOriginalFilePath(String attachmentOriginalFilePath) {
		this.attachmentOriginalFilePath = attachmentOriginalFilePath;
	}
}
