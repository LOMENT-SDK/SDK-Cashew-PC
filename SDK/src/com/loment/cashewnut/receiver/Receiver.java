package com.loment.cashewnut.receiver;

import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;

import com.loment.cashewnut.connection.ServerData;
import com.loment.cashewnut.connection.amqp.RPCClientReceiver;
import com.loment.cashewnut.connection.amqp.RPCClientSender;

/**
 *
 * @author sekhar
 */
public class Receiver {

    private static Receiver instance = null;
    private Vector<ReceiveServerRespListener> receiverListeners = new Vector<ReceiveServerRespListener>();
    private static long token = 0;

    private Receiver() {
    }

    public static Receiver getInstance() {
        if (instance == null) {
            instance = new Receiver();
        }
        return instance;
    }

    public long lastrefreshTime;

    /**
     *
     * @param data
     * @param typeOfBodyPart
     * @param url
     */
    public void receiveAmqp(String data, String key) {
        try {
            ServerData headerData = new ServerData(data, key);
            RPCClientSender.getInstance().send(headerData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveAMQP() {
        RPCClientReceiver.getInstance().getMessages();
    }

    public String getToken() {
        token++;

        String corrId = UUID.randomUUID().toString();
        return corrId + token;

		// Date currentDate = new Date();
        // return currentDate.getTime() + token;
    }

    public void addReceiverListener(ReceiveServerRespListener listener) {
        if (listener != null && !receiverListeners.contains(listener)) {
            receiverListeners.addElement(listener);
        }
    }

    public synchronized void fireReceiverListener(String currToken,
            String response) {

        if (receiverListeners != null && receiverListeners.isEmpty()) {
            ReceiverHandler.getInstance().getreceiverListeners();
        }

        if (receiverListeners != null && receiverListeners.size() > 0) {

                    Enumeration<ReceiveServerRespListener> enumeration = receiverListeners
                            .elements();
                    while (enumeration.hasMoreElements()) {
                        ReceiveServerRespListener listener = enumeration.nextElement();
                        listener.listenForResponse(currToken, response);
                    }
        }
    }
}
