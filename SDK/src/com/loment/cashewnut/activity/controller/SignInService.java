package com.loment.cashewnut.activity.controller;

import com.loment.cashewnut.sthithi.connection.AccountHandler;

public class SignInService {

	// private final StringProperty userName = new SimpleStringProperty(this,
	// "userName");
	// private final StringProperty password = new SimpleStringProperty(this,
	// "password");
	//
	// public final void setUseName(String value) {
	// userName.set(value);
	// }
	//
	// public final String getUserName() {
	// return userName.get();
	// }
	//
	// public final StringProperty userNameProperty() {
	// return userName;
	// }
	//
	// public final void setPassword(String value) {
	// password.set(value);
	// }
	//
	// public final String getPassword() {
	// return password.get();
	// }
	//
	// public final StringProperty userPasswordProperty() {
	// return password;
	// }

	// @Override
	// protected Task<String> createTask() {
	// final String _uname = getUserName();
	// final String _pass = getPassword();
	// return new Task<String>() {
	// @Override
	// protected String call() throws Exception {
	// return getResponse(_uname, _pass);
	// }
	//
	// private String getResponse(final String _uname, final String _pass) {
	// System.out.println("Check  valid user call.....");
	// if (!AccountHandler.getInstance().isValidUser(_uname, _pass)) {
	// System.out.println("Signin Failed .... ");
	// return "-1";
	// }
	// System.out.println("Sending add device call....");
	// if (!AccountHandler.getInstance().addDevice(_uname)) {
	// System.out.println("Add device Failed .... ");
	// return "-2";
	// }
	// System.out.println("Sending get linked cashew ids call.....");
	// String response = AccountHandler.getInstance()
	// .getAccountsForValidUser(_uname);
	// System.out.println("Response get linked cashew ids call.....");
	// //fXMLExampleController.signInAccountResponse(response);
	// return response;
	// }
	//
	// @Override
	// protected void succeeded() {
	// super.succeeded();
	// updateMessage("Done!");
	// }
	//
	// @Override
	// protected void cancelled() {
	// super.cancelled();
	// updateMessage("Cancelled!");
	// }
	//
	// @Override
	// protected void failed() {
	// super.failed();
	// updateMessage("Failed!");
	// }
	// };
	// }

	public String getResponse(final String _uname, final String _pass) {
		int respCode = AccountHandler.getInstance().isValidUser(_uname, _pass);
		if (respCode == 10204) {
			System.out.println("In Active User .... ");
			return "-2";
		}
		System.out.println("Check  valid user call.....");
		if (respCode != 0) {
			System.out.println("Signin Failed .... ");
			return "-1";
		}
		System.out.println("Sending add device call....");
		if (!AccountHandler.getInstance().addDevice(_uname)) {
			System.out.println("Add device Failed .... ");
			return "-3";
		}
		System.out.println("Sending get linked cashew ids call.....");
		String response = AccountHandler.getInstance().getAccountsForValidUser(
				_uname);
		System.out.println("Response get linked cashew ids call.....");
		// fXMLExampleController.signInAccountResponse(response);
		return response;
	}

}
