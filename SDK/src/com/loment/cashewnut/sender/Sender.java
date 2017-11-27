package com.loment.cashewnut.sender;

import java.net.Socket;
import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;

import com.loment.cashewnut.connection.ServerData;
import com.loment.cashewnut.connection.amqp.RPCClientSender;

/**
 * 
 * @author sekhar
 */
public class Sender {

	private static Sender instance = null;
	private static long token = 0;
	private Vector<SendServerRespListener> senderListeners = new Vector<SendServerRespListener>();

	private Sender() {
	}

	public static Sender getInstance() {
		if (instance == null) {
			instance = new Sender();
		}
		return instance;
	}

	/**
	 * 
	 * @param data
	 * @param typeOfBodyPart
	 * @param url
	 */
	public void sendAmqp(String data, String token, int typeOfBodyPart, String url) {
		try {
			switch (typeOfBodyPart) {
			case 0:
				// header and body
				ServerData headerData = new ServerData(data, token);
				RPCClientSender.getInstance().send(headerData);
				break;
			case 1:
				// attachment
				ServerData AttachmentData = new ServerData(data, url, token);
				RPCClientSender.getInstance().sendAttachment(AttachmentData);
				break;
			case 2:
				// group
				ServerData groupData = new ServerData(data, token);
				RPCClientSender.getInstance().send(groupData);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getToken() {
		token++;

		String corrId = UUID.randomUUID().toString();
		return corrId + token;

		// Date currentDate = new Date();
		// return currentDate.getTime() + token;
	}

	public void addSenderListener(SendServerRespListener listener) {
		if (listener != null && !senderListeners.contains(listener)) {
			senderListeners.addElement(listener);
		}
	}

	public synchronized void fireSenderListener(String currToken, String response) {
		if (senderListeners != null && senderListeners.size() > 0) {
			Enumeration<SendServerRespListener> enumeration = senderListeners.elements();
			while (enumeration.hasMoreElements()) {
				SendServerRespListener listener = enumeration.nextElement();
				listener.listenForResponse(currToken, response);
			}
		}
	}
}
