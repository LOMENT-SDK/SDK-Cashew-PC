package com.loment.cashewnut;

import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;

import com.loment.cashewnut.I18N.AppConfigurationStrings;

public class AppConfiguration {

	public static int CASHEW = 1;
	public static int SAFECOM_MESSENGER = 2;
	public static int AQUA = 3;

	public static int type = CASHEW;
	public static String languageBundle = "bundles.Cashew";
	
	public static boolean GROUP_TYPE6 =  true;

	// public static int type = SAFECOM_MESSENGER;
	// public static String languageBundle = "bundles.Safecom";

	// public static int type = AQUA;
	// public static String languageBundle = "bundles.Aqua";

	public static Locale localeEnglish = new Locale("en", "EN");
	public static Locale localeFrench = new Locale("fr", "FR");
	public static Locale localeChinese = new Locale("cn", "CN");
	public static final AppConfigurationStrings appConfString = new AppConfigurationStrings(
			languageBundle, localeEnglish);

	public static final int AMQP_DEFAULT_PORT_NUMBER = 5672;
	public static final int AMQP_PUBLIC_PORT_NUMBER = 80;

	final static double FONT_SIZE = 15.0;

	public static void onLocaleChange(String Language) {
		if (Language != null && !Language.trim().equals("")) {

			if (Language.equalsIgnoreCase("English")) {
				appConfString.setI18NResources(languageBundle, localeEnglish);
			}

			if (Language.equalsIgnoreCase("French")) {
				appConfString.setI18NResources(languageBundle, localeFrench);
			}
			if (Language.equalsIgnoreCase("Chinese")) {
				appConfString.setI18NResources(languageBundle, localeChinese);
			}

			try {
				if (CashewnutActivity.currentActivity != null)
					// fire to list
					((CashewnutActivity) CashewnutActivity.currentActivity)
							.onLocaleChange();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getAmqpMsgApi() {
		String value = "";
		switch (type) {
		case 1:
			value = "amqpmsg.cashew.loment.net";
			//value = "10.1.1.14";
			break;
		case 2:
			value = "imamqpmsg.safecom.agency";
			break;
		case 3:
			value = "amqpmsg.cashew.loment.net";
			break;
		}
		return value;
	}

	public static String getAmqpReqApi() {
		String value = "";
		switch (type) {
		case 1:
			value = "amqpreq.cashew.loment.net";
			//value = "10.1.1.14";
			break;
		case 2:
			value = "imamqpresp.safecom.agency";
			break;
		case 3:
			value = "amqpreq.cashew.loment.net";
			break;
		}
		return value;
	}

	public static String getAmqpRespApi() {
		String value = "";
		switch (type) {
		case 1:
			value = "amqpresp.cashew.loment.net";
			//value = "10.1.1.14";
			break;
		case 2:
			value = "imamqpresp.safecom.agency";
			break;
		case 3:
			value = "amqpresp.cashew.loment.net";
			break;
		}
		return value;
	}

	public static String getAttachmentApi() {
		String value = "";
		switch (type) {
		case 1:
			value = "https://attachcashew.loment.net";
			//value = "http://10.1.1.14";
			break;
		case 2:
			value = "http://improcess.safecom.agency";
			break;
		case 3:
			value = "https://attachcashew.loment.net";
			break;
		}
		return value;
	}

	public static String getSthithiApi() {
		String value = "";
		switch (type) {
		case 1:
			value = "https://api-sthithi.loment.net";
			//value = "http://10.1.1.14:9880";
			break;
		case 2:
			value = "https://api-sthithi.safecom.agency";
			break;
		case 3:
			value = "https://api-sthithi.loment.net";
			break;
		}
		return value;
	}

	public static String getPamentApi() {
		String value = "";
		switch (type) {
		case 1:
			value = "http://www.loment.net/pricing/";
			break;
		case 2:
			value = "https://payment.safecom.agency";
			break;
		case 3:
			value = "http://www.cryptel.pro/";
			break;
		}
		return value;
	}

	public static String getDatabaseName() {
		String value = "";
		switch (type) {
		case 1:
			value = "cashewnut2.0.db";
			break;
		case 2:
			value = "SCMDB2.0.db";
			break;
		case 3:
			value = "aqua2.0.db";
			break;
		}
		return value;
	}

	public static String getFolderName() {
		String value = "";
		switch (type) {
		case 1:
			value = ".cashew2";
			break;
		case 2:
			value = ".safecomMessenger";
			break;
		case 3:
			value = ".aqua2";
			break;
		}
		return value;
	}

	public static String getPartnerId() {
		String value = "";
		switch (type) {
		case 1:
			value = "1";
			break;
		case 2:
			value = "2";
			break;
		case 3:
			value = "3";
			break;
		}
		return value;
	}

	public static String getBackgroundImagePath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/background/";
			break;
		case 2:
			value = "/resources/sc_icons/background/";
			break;
		case 3:
			value = "/resources/aqua_icons/background/";
			break;
		}
		return value;
	}

	public static String getMessageBubblePath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/bubbles/";
			break;
		case 2:
			value = "/resources/sc_icons/bubbles/";
			break;
		case 3:
			value = "/resources/aqua_icons/bubbles/";
			break;
		}
		return value;
	}

	public static String getDocIconPath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/doc_icon/";
			break;
		case 2:
			value = "/resources/sc_icons/doc_icon/";
			break;
		case 3:
			value = "/resources/aqua_icons/doc_icon/";
			break;
		}
		return value;
	}

	public static String getIconPath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/icon/";
			break;
		case 2:
			value = "/resources/sc_icons/icon/";
			break;
		case 3:
			value = "/resources/aqua_icons/icon/";
			break;
		}
		return value;
	}

	public static String getEmojiPath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/emojis/";
			break;
		case 2:
			value = "/resources/sc_icons/icon/";
			break;
		case 3:
			value = "/resources/aqua_icons/icon/";
			break;
		}
		return value;
	}
	
	public static String getAppLogoPath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/logo/";
			break;
		case 2:
			value = "/resources/sc_icons/logo/";
			break;
		case 3:
			value = "/resources/aqua_icons/logo/";
			break;
		}
		return value;
	}

	public static String getUserIconPath() {
		String value = "";
		switch (type) {
		case 1:
			value = "/resources/loment_icons/user_icon/";
			break;
		case 2:
			value = "/resources/sc_icons/user_icon/";
			break;
		case 3:
			value = "/resources/aqua_icons/user_icon/";
			break;
		}
		return value;
	}

	public static String getBorderColour() {
		String value = "";
		switch (type) {
		case 1:
			value = "#8F7057";
			break;
		case 2:
			value = "#052C55";// c9e8ff
			break;
		case 3:
			value = "#a60900";
			break;
		}
		return value;
	}

	public static String getConvTitlebarColour() {
		String value = "";
		switch (type) {
		case 1:
			value = "#d7ccc8";
			break;
		case 2:
			value = "#c9e8ff";
			break;
		case 3:
			value = "#ffd0cd";
			break;
		}
		return value;
	}

	public static String getProgressColor() {
		String value = "";
		switch (type) {
		case 1:
			value = "#CCCCFF";
			break;
		case 2:
			value = "#052c55";
			break;
		case 3:
			value = "#A60900";
			break;
		}
		return value;
	}

	public static ObservableList<String> getLanguage() {
		ObservableList<String> options = FXCollections
				.observableArrayList("English");
		switch (type) {
		case 1:
			options = FXCollections
					.observableArrayList(
							AppConfiguration.appConfString.english, "Chinese",
							"French");
			break;
		case 2:
			options = FXCollections
					.observableArrayList(AppConfiguration.appConfString.english);
			break;
		case 3:
			options = FXCollections
					.observableArrayList(
							AppConfiguration.appConfString.english, "Chinese",
							"French");
			break;
		}
		return options;
	}

	public static Font setFont() {
		Font Font = new Font(FONT_SIZE);
		return Font;
	}

	// // API's for Local Testing
	// public static String AMQPMSGAPI = "10.1.1.6";
	// public static String AMQPREQAPI = "10.1.1.6";
	// public static String AMQPRESPAPI = "10.1.1.6";
	// public static String UPLOADDOWNLOADAPI = "http://10.1.1.6";
	// public static String STHITHIAPI = "http://api-sthithi.local";

	// public static String AMQPMSGAPI = "183.82.105.103";
	// public static String AMQPREQAPI = "183.82.105.103";
	// public static String AMQPRESPAPI = "183.82.105.103";
	// public static String UPLOADDOWNLOADAPI = "http://183.82.105.103";
	// public static String STHITHIAPI = "http://183.82.105.103";

}