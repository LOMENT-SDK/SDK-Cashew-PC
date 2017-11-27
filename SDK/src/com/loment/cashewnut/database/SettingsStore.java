package com.loment.cashewnut.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.loment.cashewnut.database.mappers.DBSettingsMapper;
import com.loment.cashewnut.model.SettingsModel;

public class SettingsStore {

	public SettingsModel getSettingsData() {
		ResultSet cursor = null;
		try {
			DBSettingsMapper settings = DBSettingsMapper.getInstance();
			cursor = settings.getMainSettingData();
			if (cursor != null && cursor.next()) {
				SettingsModel settingsModel = new SettingsModel();
				settingsModel.setPriority(cursor.getInt(1));
				settingsModel.setRestricted(cursor.getInt(2));
				settingsModel.setExpiryMinuts(cursor.getInt(3));
				settingsModel.setExpiryStatus(cursor.getInt(4));
				settingsModel.setAcknowledge(cursor.getInt(5));
				settingsModel.setLanguage(cursor.getString(6));
				settingsModel.setHibernationTime(cursor.getInt(7));
				settingsModel.setRememberStatus(cursor.getInt(8));
				settingsModel.setPlaySound(cursor.getInt(9));
				settingsModel.setSignature(cursor.getString(10));
				settingsModel.setAutoResponseMessage(cursor.getString(11));
				settingsModel.setAutoResponseStatus(cursor.getInt(12));
				settingsModel.setFontFamily(cursor.getString(13));
				return settingsModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cursor.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public void saveSettingsData(SettingsModel settingsModel) {
		boolean isSave = false;
		DBSettingsMapper settings;
		settings = DBSettingsMapper.getInstance();
		try {
			ResultSet cursor = settings.getMainSettingData();
			if (cursor == null) {
				isSave = true;
			}
			if (!cursor.next()) {
				isSave = true;
				cursor.close();
			}
		} catch (Exception e) {
			isSave = true;
			e.printStackTrace();
		}
		if (isSave) {
			settings.insert(settingsModel);
		} else {
			settings.update(settingsModel);
		}
	}

}
