package com.loment.cashewnut.model;

public class StatusFlagsModel {
	int messageId;
	String senderFolder;
	String receiverFolder;
	String Read;
	String ack;
	String selfDeleted;
	String recpDeleted;
	
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getSenderFolder() {
		return senderFolder;
	}
	public void setSenderFolder(String senderFolder) {
		this.senderFolder = senderFolder;
	}
	public String getReceiverFolder() {
		return receiverFolder;
	}
	public void setReceiverFolder(String receiverFolder) {
		this.receiverFolder = receiverFolder;
	}
	public String getRead() {
		return Read;
	}
	public void setRead(String read) {
		Read = read;
	}
	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack;
	}
	public String getSelfDeleted() {
		return selfDeleted;
	}
	public void setSelfDeleted(String selfDeleted) {
		this.selfDeleted = selfDeleted;
	}
	public String getRecpDeleted() {
		return recpDeleted;
	}
	public void setRecpDeleted(String recpDeleted) {
		this.recpDeleted = recpDeleted;
	}
}
