/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.model;

import java.util.Date;
import java.util.Vector;

import com.loment.cashewnut.database.RecepientStore;
import com.loment.cashewnut.util.Helper;

/**
 * 
 * @author sekhar
 */
public class HeaderModel {

	private int localHeaderId = -1;
	private int serverMessageId = -1;
	private String messageFrom;
	private int messageType;
	// Value 1 for Inbox and 2 for sentbox
	private String messageFolderType = "";
	private String customFolder = "inbox";

	private int priority = -1;
	private int restricted = -1;
	private int messageAck = -1;
	private int expiry = -1;
	private long expiryStartTime = -1;
	private String subject = "";
	private int messageFromThisDevice = -1;

	public int getMessageFromThisDevice() {
		return messageFromThisDevice;
	}

	public void setMessageFromThisDevice(int messageFromThisDevice) {
		this.messageFromThisDevice = messageFromThisDevice;
	}

	private long creationTime;
	private long lastUpdateTime;

	private String bodyUrl = "";

	// if no.of numberOfBodyparts == sendParts
	// failure status =-1

	private int sendParts;
	private int numberOfBodyparts;
	private int deleteStatus = -1;

	private MessageModel messageModel = null;

	private int scheduleStatus = -1;
	private long scheduleTime;
	private String headerVersion = "";
	private String token = "";
	private String threadId = "";
	private String groupId = "";
	
	private String body = "";
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHeaderVersion() {
		return headerVersion;
	}

	public void setHeaderVersion(String headerVersion) {
		this.headerVersion = headerVersion;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	// public String getGroupName() {
	// return groupName;
	// }
	//
	// public void setGroupName(String groupName) {
	// this.groupName = groupName;
	// }

	public int getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(int scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public long getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(long scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public HeaderModel() {
	}

	public HeaderModel(MessageModel messageModel) {
		this.messageModel = messageModel;
	}

	public MessageModel getMessageHeaderModel() {
		return messageModel;
	}

	public int getLocalHeaderId() {
		return localHeaderId;
	}

	public void setLocalHeaderId(int localHeaderId) {
		this.localHeaderId = localHeaderId;
	}

	public int getServerMessageId() {
		return serverMessageId;
	}

	public void setServerMessageId(int serverMessageId) {
		this.serverMessageId = serverMessageId;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMessageFolderType() {
		return messageFolderType;
	}

	public void setMessageFolderType(String messageFolderType) {
		this.messageFolderType = messageFolderType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getRestricted() {
		return restricted;
	}

	public void setRestricted(int restricted) {
		this.restricted = restricted;
	}

	public int getMessageAck() {
		return messageAck;
	}

	public void setMessageAck(int messageAck) {
		this.messageAck = messageAck;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	public long getExpiryStartTime() {
		return expiryStartTime;
	}

	public void setExpiryStartTime(long expiryStartTime) {
		this.expiryStartTime = expiryStartTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
		generateTimeString();
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getSendParts() {
		return sendParts;
	}

	public void setSendParts(int sendStatus) {
		this.sendParts = sendStatus;
	}

	public int getNumberOfBodyparts() {
		return numberOfBodyparts;
	}

	public void setNumberOfBodyparts(int numberOfBodyparts) {
		this.numberOfBodyparts = numberOfBodyparts;
	}

	public String getCustomFolder() {
		return customFolder;
	}

	public void setCustomFolder(String customFolder) {
		this.customFolder = customFolder;
	}

	private String dateString;
	private String timeString;

	protected void generateTimeString() {
		Date d = new Date();
		d.setTime(creationTime);
		String strDate = Helper.dateToStringFull(d);

		dateString = strDate.substring(0, 11);
		timeString = strDate.substring(12, 20);
	}

	public String getDateString() {
		return dateString;
	}

	public String getTimeString() {
		return timeString;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public int compareTo(HeaderModel latestMessage) {
		if (getCreationTime() < latestMessage.getCreationTime()) {
			return 1;
		}
		return -1;
	}

	public boolean isSelfMessage(String cashewnutId) {
		Vector<String> to = RecepientStore.getRecepientCashewnutIds(this
				.getLocalHeaderId());
		String from = this.getMessageFrom();
		return cashewnutId.equals(from) && to.contains(cashewnutId);
	}
}
