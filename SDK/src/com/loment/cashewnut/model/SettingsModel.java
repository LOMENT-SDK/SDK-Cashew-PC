package com.loment.cashewnut.model;

public class SettingsModel {

	private int priority = 3;
	private int restricted = -1;
	private int acknowledge = -1;
	private int expiryStatus = -1;
	private int expiryMinuts;

	private String language;
	private int hibernationTime = -1;

	private int playSound = -1;
	private int autoResponseStatus = -1;
	private String autoResponseMessage;
	private String signature;
	private int rememberStatus = -1;
        private String fontFamily = "";

	public int getRememberStatus() {
		return rememberStatus;
	}

	public void setRememberStatus(int rememberStatus) {
		this.rememberStatus = rememberStatus;
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

	public int getAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(int acknowledge) {
		this.acknowledge = acknowledge;
	}

	public int getExpiryStatus() {
		return expiryStatus;
	}

	public void setExpiryStatus(int expiryStatus) {
		this.expiryStatus = expiryStatus;
	}

	public int getExpiryMinuts() {
		return expiryMinuts;
	}

	public void setExpiryMinuts(int expiryMinuts) {
		this.expiryMinuts = expiryMinuts;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getHibernationTime() {
		return hibernationTime;
	}

	public void setHibernationTime(int hibernationTime) {
		this.hibernationTime = hibernationTime;
	}

	public int getPlaySound() {
		return playSound;
	}

	public void setPlaySound(int playSound) {
		this.playSound = playSound;
	}

	public int getAutoResponseStatus() {
		return autoResponseStatus;
	}

	public void setAutoResponseStatus(int autoResponseStatus) {
		this.autoResponseStatus = autoResponseStatus;
	}

	public String getAutoResponseMessage() {
		return autoResponseMessage;
	}

	public void setAutoResponseMessage(String autoResponseMessage) {
		this.autoResponseMessage = autoResponseMessage;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

    /**
     * @return the fontFamily
     */
    public String getFontFamily() {
        return this.fontFamily;
    }

    /**
     * @param fontFamily the fontFamily to set
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

}
