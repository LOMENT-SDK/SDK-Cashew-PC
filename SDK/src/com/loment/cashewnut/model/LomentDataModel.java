package com.loment.cashewnut.model;

public class LomentDataModel {
	
	public String username;
	public String password;
	public String email;
	public String lomentId;
	public String mobileNumber;
	public String countryCode;	
	public String countryAbbr;

	public String userId;
	public String deviceName;
	public String deviceId;
	public String subscriptionId;
	public String subscriptionType;
	public String subscriptionStatus;
	public String startDate;
	public String endDate;
	
	public LomentDataModel(String username, String password, String email,
			String lomentId, String mobileNumber, String countryCode,
			String countryAbbr,String userId) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.lomentId = lomentId;
		this.mobileNumber = mobileNumber;
		this.countryCode = countryCode;
		this.countryAbbr = countryAbbr;
		this.userId = userId;
	}	
	
	
	public LomentDataModel(String lomentId, String deviceName, String deviceId) {
		super();
		this.lomentId = lomentId;
		this.deviceName = deviceName;
		this.deviceId = deviceId;
	}


	public LomentDataModel(String subscriptionId, String subscriptionType,
			String subscriptionStatus, String startDate, String endDate) {
		super();
		this.subscriptionId = subscriptionId;
		this.subscriptionType = subscriptionType;
		this.subscriptionStatus = subscriptionStatus;
		this.startDate = startDate;
		this.endDate = endDate;
	}



	public LomentDataModel() {
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLomentId() {
		return lomentId;
	}
	public void setLomentId(String lomentId) {
		this.lomentId = lomentId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryAbbr() {
		return countryAbbr;
	}
	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.endDate = userId;
	}
}
