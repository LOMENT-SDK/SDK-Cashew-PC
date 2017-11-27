package com.loment.cashewnut.connection;

import java.util.Vector;

import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.sthithi.connection.AccountHandler;

public class ReceipientConnection {

    private static ReceipientConnection instance = null;
    private String lomentId = "";
    private String password = "";
    ReceipientDetails clientReader = null;

    private ReceipientConnection() {
        DBLomentDataMapper lomentDataMapper = DBLomentDataMapper.getInstance();
        LomentDataModel lomentDataModel = lomentDataMapper.getLomentData();
        lomentId = lomentDataModel.getLomentId();
        password = lomentDataModel.getPassword();
    }

    public static ReceipientConnection getInstance() {
        if (instance == null) {
            instance = new ReceipientConnection();
        }
        return instance;
    }

    public void get(String data) {
        if (clientReader == null) {
            clientReader = new ReceipientDetails("contact");
        }
        clientReader.sendData(data);
    }

    protected static Vector<String> vector = new Vector<String>();

    class ReceipientDetails implements Runnable {

        private Thread thread;
        private String threadName;

        public ReceipientDetails(String name) {
            this.threadName = name;
        }

        public void sendData(String receipientId) {
            if (receipientId != null && receipientId.trim().length() > 0
                    && !vector.contains(receipientId)) {
                vector.add(receipientId);
            }
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }

        @Override
        public void run() {
            try {
                while (vector.size() > 0) {
                    try {
                        if (CashewnutApplication.isInternetOn()) {
                            AccountHandler.getInstance().getNameFromSthithi(
                                    lomentId, password, vector.get(0),0);
                        }
                        vector.removeElementAt(0);
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
