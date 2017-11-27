package com.loment.cashewnut.connection;

public class ServerData {
	
	public static final int TO_READY = 0;
	public static final int TO_WAIT = 1;
	public static final int TO_SENT = 2;
	public static final int TO_FAILED = 3;
	public static final int TO_MAX_AUTH_TRIES = 3;
	
	public int sendStatus = 0;
	public int authTryCount =0;

	public int getAuthTryCount() {
		return authTryCount;
	}

	public void setAuthTryCount(int authTryCount) {
		this.authTryCount += authTryCount;
	}

	String data;

	String type;
	String url;
	String key;

	public ServerData(String data, String type) {
		this.data = data;
		this.type = type;
		this.key = type;
	}

	public ServerData(String data, String url, String type) {
		this.data = data;
		this.type = type;
		this.url = url;
		this.key = type;
	}

	public String getType() {
		return this.type;
	}

	public String getData() {
		return this.data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public String getUrl() {
		return this.url;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}