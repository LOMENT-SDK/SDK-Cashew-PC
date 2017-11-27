package com.loment.cashewnut.model;

import java.sql.Timestamp;

public class AutoResponseModel {
	
	public String cashewnutId;
	public String message;
	public Timestamp timestamp;
	
	public String getCashewnutId() {
		return cashewnutId;
	}
	public void setCashewnutId(String cashewnutId) {
		this.cashewnutId = cashewnutId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp2) {
		this.timestamp = timestamp2;
	}

	

}
