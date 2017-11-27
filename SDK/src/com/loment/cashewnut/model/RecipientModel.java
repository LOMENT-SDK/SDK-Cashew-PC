package com.loment.cashewnut.model;

public class RecipientModel {

	private int recepientLocalId;
	private String recepientCashewnutId; 
	private int receipientReadStatus;
	private int receipientAck;
	private int localHeaderId;
	private int receipientDeleteStatus = -1;
	
	private MessageModel messageModel= null;
	
	public RecipientModel(){
	}
	
	public RecipientModel(MessageModel messageModel){
		this.messageModel = messageModel;
	}
	
	public MessageModel getMessageHeaderModel() {
		return messageModel;
	}
	
	public String getRecepientCashewnutId() { 
		return recepientCashewnutId;
	}
	public void setRecepientCashewnutId(String recepientId) { 
		this.recepientCashewnutId = recepientId;
	}
	public int getReceipientReadStatus() {
		return receipientReadStatus;
	}
	public void setReceipientReadStatus(int receipientReadStatus) {
		this.receipientReadStatus = receipientReadStatus;
	}
	public int getReceipientAck() {
		return receipientAck;
	}
	public int getReceipientDeleteStatus() {
		return receipientDeleteStatus;
	}

	public void setReceipientDeleteStatus(int receipientDeleteStatus) {
		this.receipientDeleteStatus = receipientDeleteStatus;
	}

	public void setReceipientAck(int receipientAck) {
		this.receipientAck = receipientAck;
	}

	public int getLocalHeaderId() {
		return localHeaderId;
	}

	public void setLocalHeaderId(int localHeaderId) {
		this.localHeaderId = localHeaderId;
	}

	public int getRecepientLocalId() {
		return recepientLocalId;
	}

	public void setRecepientLocalId(int recepientLocalId) {
		this.recepientLocalId = recepientLocalId;
	}
}
