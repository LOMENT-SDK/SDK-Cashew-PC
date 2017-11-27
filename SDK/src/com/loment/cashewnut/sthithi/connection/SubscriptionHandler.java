package com.loment.cashewnut.sthithi.connection;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.LomentDataModel;

public class SubscriptionHandler {

	private static SubscriptionHandler instance;
	private static boolean isActivate = false;

	private SubscriptionHandler() {
		SubscriptionHandler.instance = this;
	}

	public static SubscriptionHandler getInstance() {
		if (SubscriptionHandler.instance == null) {
			SubscriptionHandler.instance = new SubscriptionHandler();
		}
		return SubscriptionHandler.instance;
	}

	public JSONObject activateAccount(String activationKey) {

		LomentDataModel lomentDataModel = DBLomentDataMapper.getInstance()
				.getLomentData();

		if (AccountHandler.getDeviceID()
				.contains(lomentDataModel.getDeviceId())) {

			String post = "key=" + activationKey;

			String url = AppConfiguration.getSthithiApi() + "/user/"
					+ lomentDataModel.getLomentId() + "/subscription/3/device/"
					+ lomentDataModel.getDeviceId() + "/link/";
			String responseText;
			try {
				responseText = DataConnection.getInstance().sendPost(url,
						post.getBytes());
				JSONObject responseJson = new JSONObject(responseText);
				return responseJson;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	public JSONObject getSubscriptionDataFromSthithi() {
		LomentDataModel lomentDataModel = DBLomentDataMapper.getInstance()
				.getLomentData();

		if (AccountHandler.getDeviceID()
				.contains(lomentDataModel.getDeviceId())) {
			String url = AppConfiguration.getSthithiApi() + "/user/"
					+ lomentDataModel.getLomentId() + "/subscription/3/device/"
					+ lomentDataModel.getDeviceId() + "/";
			String responseText;
			try {
				responseText = DataConnection.getInstance().sendGetRequest(url,
						"");
				JSONObject responseJson = new JSONObject(responseText);
				return responseJson;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public String[] subscriptionStatus(JSONObject responseJson,
			boolean isSubscribe) {
		String result[] = new String[2];
		try {
			if (responseJson != null) {
				Integer status = (Integer) responseJson.get("status");
				int val = status.intValue();
				if (val == 0) {
					JSONObject lineItems = (JSONObject) responseJson
							.get("subscription_details");
					String subscriptionStatus = "";
					if (lineItems.has("subscription_status")) {
						subscriptionStatus = (String) lineItems
								.get("subscription_status");
					}
					if (lineItems.has("status")) {
						subscriptionStatus = (String) lineItems.get("status");
					}
					if (subscriptionStatus.equals("0")) {
						this.storeSubscriptionDetails("", "", "Inactive", "");
						result[1] = "Inactive";
						result[0] = "fail";
						isActivate = false;
					} else if (subscriptionStatus.equals("1")) {
						String subscriptionStartDate = "";
						String subscriptionEndDate = "";

						if (lineItems.has("subscription_start_date")) {
							subscriptionStartDate = (String) lineItems
									.get("subscription_start_date");
						}
						if (lineItems.has("start_date")) {
							subscriptionStartDate = (String) lineItems
									.get("start_date");
						}
						if (lineItems.has("subscription_end_date")) {
							subscriptionEndDate = (String) lineItems
									.get("subscription_end_date");
						}
						if (lineItems.has("end_date")) {
							subscriptionEndDate = (String) lineItems
									.get("end_date");
						}

						String type = "";
						if (lineItems.has("type")) {
							// {"subscription_details":{"id":"5140","end_date":"2014-07-15 00:00:00",
							// "type":"P","start_date":"2013-07-15 00:00:00","status":"1"},
							// "createNew":null,"utc_timestamp":1405412732,"status":0,"comments":"success"}
							type = (String) lineItems.get("type");
						} else if (isSubscribe) {
							// {"subscription_details":{"bill_id":"1756","subscription_end_date":"2014-07-15 00:00:00",
							// "subscription_start_date":"2013-07-15 00:00:00","subscription_status":"1"},
							// "status":0,"utc_timestamp":1405411910,"comments":"success"}
							type = "P";
						}

						this.storeSubscriptionDetails(subscriptionStartDate,
								subscriptionEndDate, "Active", type);
						result[0] = "success";
						result[1] = AppConfiguration.appConfString.activate_active;
						isActivate = true;
					}
				} else {
					if (val == 10002 || val == 10003) {
						result[0] = "fail";
						result[1] = AppConfiguration.appConfString.activate_account_not_found;
					} else if (val == 50211) {
						result[0] = "fail";
						result[1] = (AppConfiguration.appConfString.activate_primary_username_activationkey_not_found);
					} else if (val == 40116) {
						result[0] = "fail";
						result[1] = (AppConfiguration.appConfString.activate_primary_username_activationkey_not_found);
					} else if (val == 50209) {
						this.storeSubscriptionDetails("", "", "Inactive", "");
						result[0] = "fail";
						result[1] = "Inactive";
					} else if (val != 0) {
						result[0] = "fail";
						result[1] = (AppConfiguration.appConfString.activate_email_support);
					}
					isActivate = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void checkSubscription() {
		new Thread(new Runnable() {
			public void run() {
				try {
					getSubscriptionStatus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public boolean getSubscriptionStatus() throws Exception {
		if (isActivate) {
			return true;
		}
		try {
			if (CashewnutApplication.isInternetOn()) {
				JSONObject object = getSubscriptionDataFromSthithi();
				if (object != null && !object.toString().trim().equals(""))
					subscriptionStatus(object, false);
				return isActivate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSubscriptionStatusFromDB();
	}

	public boolean getSubscriptionStatusFromDB() {
		try {
			LomentDataModel dataModel = getSubscriptionDetails();
			if (dataModel != null) {
				if (dataModel.getSubscriptionStatus() != null
						&& dataModel.getSubscriptionStatus().trim()
								.equals("Active")) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void storeSubscriptionDetails(String startDate, String endDate,
			String status, String type) {
		try {
			LomentDataModel lomentDataModel = DBLomentDataMapper.getInstance()
					.getLomentData();

			lomentDataModel.setSubscriptionStatus(status);
			lomentDataModel.setSubscriptionType(type);
			lomentDataModel.setStartDate(startDate);
			lomentDataModel.setEndDate(endDate);
			lomentDataModel.setLomentId(lomentDataModel.getLomentId());

			DBLomentDataMapper.getInstance().updateSubscriptionData(
					lomentDataModel);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public LomentDataModel getSubscriptionDetails() {
		LomentDataModel lomentDataModel = DBLomentDataMapper.getInstance()
				.getLomentData();
		if (lomentDataModel != null
				&& lomentDataModel.getSubscriptionStatus() != null
				&& !lomentDataModel.getSubscriptionStatus().trim().equals("")) {
			// Trail is "T" and Subscribed is "P" - type of subscription
			// "Active" and "Inactive" - status of subscription
			return lomentDataModel;
		}
		return null;
	}
}
