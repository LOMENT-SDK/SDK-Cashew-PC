package com.loment.cashewnut.activity.controller;

import com.loment.cashewnut.sthithi.connection.AccountHandler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SignUpService1 {

	private final StringProperty name = new SimpleStringProperty(this,
			"userName");
	private final StringProperty email = new SimpleStringProperty(this, "email");
	private final StringProperty password = new SimpleStringProperty(this,
			"password");
	private final StringProperty mobile = new SimpleStringProperty(this,
			"mobile");
	private final StringProperty countryCode = new SimpleStringProperty(this,
			"mobile");
	private StringProperty cashewId = new SimpleStringProperty(this, "cashewId");
	private StringProperty promoCode = new SimpleStringProperty(this, "pCode");

	public String getCashewId() {
		return cashewId.get();
	}

	public void setCashewId(String value) {
		cashewId.set(value);
	}

	public String getPromoCode() {
		return promoCode.get();
	}

	public void setPromoCode(String value) {
		promoCode.set(value);
	}

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

	public String signupResponse() {
		try {
			final String _pass = getPassword();
			final String _email = getEmail();

			// String[] splitParts = email.get().split("[@]");
			// final String _uname = splitParts[0];

			final String _uname = getUserName();

			final String _mobile = getMobile();
			final String _countryCode = getCountryCode();
			final String _cashewId = getCashewId();
			final String _promoCode = getPromoCode();
			String response = AccountHandler.getInstance()
					.register(_uname, _pass, _email, _mobile, _countryCode,
							_cashewId, _promoCode);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}
}
