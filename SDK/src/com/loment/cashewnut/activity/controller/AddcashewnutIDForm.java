package com.loment.cashewnut.activity.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.sthithi.connection.AccountHandler;

/**
*
* @author sekhar
*/
public class AddcashewnutIDForm {

	private final StringProperty cashewId = new SimpleStringProperty(this,
			"cashewID");

	public final void setCashewId(String value) {
		cashewId.set(value);
	}

	public final String getCashewId() {
		return cashewId.get();
	}

	public final StringProperty userNameProperty() {
		return cashewId;
	}

	// @Override
	// protected Task createTask() {
	// final String _uname = getCashewId();
	// return new Task<Integer>() {
	// @Override
	// protected Integer call() throws Exception {
	//
	// DBLomentDataMapper lomentDataMapper = DBLomentDataMapper
	// .getInstance();
	// LomentDataModel lomentDataModel = lomentDataMapper
	// .getLomentData();
	// String _userId = lomentDataModel.getLomentId();
	// int response = AccountHandler.getInstance().addAccount(
	// _uname, _userId);
	//
	// return response;
	//
	// }
	// };
	// }

	public int cashewIDResponse() {
		final String _uname = getCashewId();
		DBLomentDataMapper lomentDataMapper = DBLomentDataMapper.getInstance();
		LomentDataModel lomentDataModel = lomentDataMapper.getLomentData();
		String _userId = lomentDataModel.getLomentId();
		int response = AccountHandler.getInstance().addAccount(_uname, _userId);

		return response;
	}

}
