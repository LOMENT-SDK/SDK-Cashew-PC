package com.loment.cashewnut;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.loment.cashewnut.model.MessageModel;

public class CashewnutApplication {

	public static int requestID = 0;
	public static boolean isAmqpQueueEmpty;
	
	public static boolean isNetworkConnected() {
		return isInternetOn();
	}

	public static boolean isInternetOn() {
		boolean available = false;
		try {
			final URLConnection connection = new URL("https://www.google.com/")
					.openConnection();
			connection.connect();
			available = true;
		} catch (final MalformedURLException e) {
		} catch (final IOException e) {
			available = false;
		}
		return available;
	}

	public static void createNotification(MessageModel messageModel, int count) {
	}
	
}
