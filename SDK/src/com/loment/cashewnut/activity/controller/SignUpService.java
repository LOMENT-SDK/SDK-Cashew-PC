package com.loment.cashewnut.activity.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.json.JSONObject;

import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.sthithi.connection.AccountHandler;

public class SignUpService {

	private final StringProperty name = new SimpleStringProperty(this,
			"userName");
	private final StringProperty email = new SimpleStringProperty(this, "email");
	private final StringProperty password = new SimpleStringProperty(this,
			"password");
	private final StringProperty mobile = new SimpleStringProperty(this,
			"mobile");
	private final StringProperty countryCode = new SimpleStringProperty(this,
			"mobile");

	public final void setCountryCode(String value) {
		countryCode.set(value);
	}

	public final String getCountryCode() {
		return countryCode.get();
	}

	public final void setEmail(String value) {
		email.set(value);
	}

	public final String getEmail() {
		return email.get();
	}

	public final void setMobile(String value) {
		mobile.set(value);
	}

	public final String getMobile() {
		return mobile.get();
	}

	public final void setUseName(String value) {
		name.set(value);
	}

	public final String getUserName() {
		return name.get();
	}

	public final StringProperty userNameProperty() {
		return name;
	}

	public final void setPassword(String value) {
		password.set(value);
	}

	public final String getPassword() {
		return password.get();
	}

	public final StringProperty userPasswordProperty() {
		return password;
	}

	// @Override
	// protected Task createTask() {
	// final String _uname = getUserName();
	// final String _pass = getPassword();
	// final String _email = getEmail();
	// final String _mobile = getMobile();
	// final String _countryCode = getCountryCode();
	//
	// return new Task<String>() {
	// @Override
	// protected String call() throws Exception {
	//
	// String response1 = AccountHandler.getInstance()
	// .register(_uname, _pass, _email, _mobile, _countryCode);
	// JSONObject responseJson = new JSONObject(response1);
	// Integer status = (Integer) responseJson.get("status");
	// int val = status;
	//
	// if (val == 0) {
	// String userid = (String) responseJson.get("userid");
	// DBLomentDataMapper headerMapper = DBLomentDataMapper
	// .getInstance();
	// headerMapper.insert(new LomentDataModel(_uname, _pass,
	// _email, _email, _mobile, _countryCode, _countryCode, userid));
	// } else {
	// if (val == 10102) {
	// return "-1";
	// } else {
	// return "-2";
	// }
	// }
	// if (!AccountHandler.getInstance().addDevice(_email)) {
	// System.out.println("Add device Failed .... ");
	// return "-3";
	// }
	// if (AccountHandler.getInstance().addAccount(_mobile, _email) == 1) {
	// // cashew id already exist
	// return "-4";
	// }
	// if (val == 0) {
	// return "0";
	// }
	// return "";
	// }
	// };
	// }

	public String signupResponse() {
		try {
			final String _uname = getUserName();
			final String _pass = getPassword();
			final String _email = getEmail();
			final String _mobile = getMobile();
			final String _countryCode = getCountryCode();
			String response1 = AccountHandler.getInstance().register(_uname,
					_pass, _email, _mobile, _countryCode);
			JSONObject responseJson = new JSONObject(response1);
			Integer status = (Integer) responseJson.get("status");
			int val = status;

			if (val == 0) {
				String userid = (String) responseJson.get("userid");
				DBLomentDataMapper headerMapper = DBLomentDataMapper
						.getInstance();
				headerMapper.insert(new LomentDataModel(_uname, _pass, _email,
						_email, _mobile, _countryCode, _countryCode, userid));
			} else {
				if (val == 10102) {
					return "-1";
				} else {
					return "-2";
				}
			}
			if (!AccountHandler.getInstance().addDevice(_email)) {
				System.out.println("Add device Failed .... ");
				return "-3";
			}
			if (AccountHandler.getInstance().addAccount(_mobile, _email) == 1) {
				// cashew id already exist
				return "-4";
			}
			if (val == 0) {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
