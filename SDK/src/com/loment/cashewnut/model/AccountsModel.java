package com.loment.cashewnut.model;

public class AccountsModel {

	public String cashewnutId;
	public String creationDate;
	public String status;
	public long startMsgId;
	public long lastMsgId;
        private String receivedMsgQueue;
	
	
	public AccountsModel(String cashewnutId, String creationDate, String status) {
		super();
		this.cashewnutId = cashewnutId;
		this.creationDate = creationDate;
		this.status = status;
	}
	
	public AccountsModel() {
	}

	public String getCashewnutId() {
		return cashewnutId;
	}
	public void setCashewnutId(String cashewnutId) {
		this.cashewnutId = cashewnutId;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getStartMsgId() {
		return startMsgId;
	}
	public void setStartMsgId(long startMsgId) {
		this.startMsgId = startMsgId;
	}
	public long getLastMsgId() {
		return lastMsgId;
	}
	public void setLastMsgId(long lastMsgId) {
		this.lastMsgId = lastMsgId;
	}

    public String getReceivedMsgQueue() {
        return receivedMsgQueue;
    }

    public void setReceivedMsgQueue(String receivedMsgQueue) {
        this.receivedMsgQueue = receivedMsgQueue;
    }
}
