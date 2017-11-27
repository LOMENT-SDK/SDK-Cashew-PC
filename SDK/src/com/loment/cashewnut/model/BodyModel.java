package com.loment.cashewnut.model;

public class BodyModel {
	private int localHeaderId = -1;
	private String serverMessageId;
	private String bodyFilePath;
	private long bodySize;
	private String bodyContent= "";
	
	
	private MessageModel messageModel= null;
	

	public BodyModel(MessageModel messageModel){
		this.messageModel = messageModel;
	}
	
	public MessageModel getMessageHeaderModel() {
		return messageModel;
	}
	
	public long getBodySize() {
		return bodySize;
	}
	public void setBodySize(long bodySize) {
		this.bodySize = bodySize;
	}
	public int getLocalHeaderId() {
		return localHeaderId;
	}
	public void setLocalHeaderId(int localHeaderId) {
		this.localHeaderId = localHeaderId;
	}
	public String getServerMessageId() {
		return serverMessageId;
	}
	public void setServerMessageId(String serverMessageId) {
		this.serverMessageId = serverMessageId;
	}
	public String getBodyFilePath() {
		return bodyFilePath;
	}
	public void setBodyFilePath(String bodyFilePath) {
		this.bodyFilePath = bodyFilePath;
	}

	public String getBodyContent() {
		return this.bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}
}
