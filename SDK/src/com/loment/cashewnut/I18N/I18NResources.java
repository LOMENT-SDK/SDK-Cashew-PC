package com.loment.cashewnut.I18N;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18NResources {
	private static ResourceBundle resources;

	public I18NResources(String type,Locale locale) {
		try {
			resources = ResourceBundle.getBundle(type, locale);
		} catch (MissingResourceException mre) {
			System.err.println("properties not found");
		}
	}
	public String getValue(String key) {
		try {
			String value = resources.getString(key);
			return value;
		} catch (Exception e) {
			System.err.println(key + " value not exist");
			//e.printStackTrace();
		}
		return key;
	}
}