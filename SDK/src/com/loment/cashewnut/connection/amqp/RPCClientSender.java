package com.loment.cashewnut.connection.amqp;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.connection.ServerData;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.jsonSenderModel.Attachment;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.receiver.Receiver;
import com.loment.cashewnut.receiver.ReceiverHandler;
import com.loment.cashewnut.sender.Sender;
import com.loment.cashewnut.sthithi.connection.AccountHandler;
import com.loment.cashewnut.util.Base64;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 
 * @author sekhar
 */
public class RPCClientSender extends SenderClientAmqp {
	private static RPCClientSender instance;

	private RPCClientSender() {
	}

	public synchronized boolean openConnection() {
		try {

			if (reqConnection != null && reqConnection.isOpen()
					&& isReqRunning == false) {
				reqConnection.close();
			}
			if (respConnection != null && respConnection.isOpen()
					&& isRespRunning == false) {
				respConnection.close();
			}

			int port = AppConfiguration.AMQP_DEFAULT_PORT_NUMBER;// Amqp default port
			
			if ((respConnection == null || !respConnection.isOpen() || !isRespRunning)
					&& CashewnutApplication.isInternetOn()) {
				isAuthenticated = false;

				try {
					initRespConnection(port);
				} catch (java.net.ConnectException e) {
					port = AppConfiguration.AMQP_PUBLIC_PORT_NUMBER;
					initRespConnection(port);
				}

				setupConsumer();
				Thread.sleep(200);
				isRespRunning = true;
			}

			if ((reqConnection == null || !reqConnection.isOpen() || !isReqRunning)
					&& CashewnutApplication.isInternetOn()) {
				isAuthenticated = false;
				try {
					initReqConnection(port);
				} catch (java.net.ConnectException e) {
					port = AppConfiguration.AMQP_PUBLIC_PORT_NUMBER;
					initReqConnection(port);
				}

				Thread.sleep(100);
				isReqRunning = true;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void setupConsumer() throws IOException {
		replyQueueName = respChannel.queueDeclare().getQueue();
		consumer = new QueueingConsumer(respChannel);
		respChannel.basicConsume(replyQueueName, false, consumer);
	}

	private void initReqConnection(int port) throws IOException {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setRequestedHeartbeat(60);
			factory.setHost(AppConfiguration.getAmqpReqApi());
			factory.setUsername("req_writer");
			factory.setPassword("r3q_Wr1t3r@312");
			factory.setVirtualHost("cashew_requests");
			factory.setPort(port);// 5672
			reqConnection = factory.newConnection();
			reqChannel = reqConnection.createChannel();
		} catch (java.net.ConnectException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	private void initRespConnection(int port) throws IOException {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setRequestedHeartbeat(65);
			factory.setHost(AppConfiguration.getAmqpRespApi());
			factory.setUsername("resp_reader");
			factory.setPassword("r35p_R3@d3r@312");
			factory.setVirtualHost("cashew_responses");
			factory.setPort(port);// 5672
			respConnection = factory.newConnection();
			respChannel = respConnection.createChannel();
		} catch (java.net.ConnectException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	public static RPCClientSender getInstance() {
		try {
			if (instance == null) {
				instance = new RPCClientSender();

				clientwriter = new ClientWriterAmqp("client");
				clientreader = new ClientReaderAmqp("client");
				clientAttachmentWriter = new ClientAttachmentWriter(
						"clientAttachment");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public void send(ServerData data) {
		openConnection();

		if (reqConnection == null || !reqConnection.isOpen()
				|| respConnection == null || !respConnection.isOpen()) {
			// failure condition
			evaluteValueSendFeature(data.getKey());
			return;
		}

		if (data != null) {
			senderQueue.addElement(data);
		}
		send();
	}

	public void sendAttachment(ServerData data) {
		openConnection();

		if (reqConnection == null || !reqConnection.isOpen()
				|| respConnection == null || !respConnection.isOpen()) {
			// failure condition
			evaluteValueSendFeature(data.getKey());
			return;
		}
		if (data != null) {
			senderAttachmentQueue.addElement(data);
		}

		sendAttachment();
	}

	private void send() {
		try {
			if (clientreader != null) {
				clientreader.startThread();
			}

			if (clientwriter != null) {
				clientwriter.sendData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendAttachment() {
		try {
			if (senderAttachmentQueue.size() < 1) {
				return;
			}
			clientAttachmentWriter.sendData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		closeConnection();
	}
}

class ClientReaderAmqp extends SenderClientAmqp implements Runnable {
	private Thread thread;
	private String threadName;

	public ClientReaderAmqp(String name) {
		this.threadName = name;
		startThread();
	}

	public void startThread() {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

	@Override
	public void run() {
		try {
			if (CashewnutApplication.isInternetOn()) {
				callResponse();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void callResponse() {
		try {
			while (isRespRunning) {

				if (!respConnection.isOpen() || consumer == null) {
					isRespRunning = false;
					continue;
				}

				if (consumer != null) {
					QueueingConsumer.Delivery delivery = consumer
							.nextDelivery();
					if (delivery != null) {
						String response = new String(delivery.getBody());
						String token = delivery.getProperties()
								.getCorrelationId();

						evaluateValueSendSuccess(response, token);

						// if (evaluteValue(response, token, 1)) {
						// notifyAuthentication();
						// } else {
						// notifyFailedAuthentication(response, token);
						// }

						try {
							handleAckNack(token, 2);
							respChannel.basicAck(delivery.getEnvelope()
									.getDeliveryTag(), false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		} catch (AlreadyClosedException e) {
			// possibly check if channel was closed
			// by the time we started action and reasons for
			// closing it
			handleAckNack("", 3);
			closeConnection();
		} catch (ShutdownSignalException e) {
			// possibly check if channel was closed
			// by the time we started action and reasons for
			// closing it
			handleAckNack("", 3);
			closeConnection();
			// e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// e.printStackTrace();
			handleAckNack("", 3);
			closeConnection();
		} catch (Exception e) {
			// e.printStackTrace();
			handleAckNack("", 3);
			closeConnection();
		}
	}

	private void notifyAuthentication() {
		if (senderQueue.size() > 0) {
			ServerData serverData = (ServerData) senderQueue.elementAt(0);
			if (serverData.getSendStatus() == ServerData.TO_WAIT) {
				serverData.setSendStatus(ServerData.TO_SENT);
			}
		}
	}

	private void notifyFailedAuthentication(String response, String token) {
		if (senderQueue.size() > 0) {
			ServerData serverData = (ServerData) senderQueue.elementAt(0);
			if (serverData.getSendStatus() == ServerData.TO_WAIT) {
				serverData.setSendStatus(ServerData.TO_FAILED);
				// System.out.println(" @@ Cashewnut- " + token + "- " +
				// response);
			}
		}
	}
}

class ClientWriterAmqp extends SenderClientAmqp implements Runnable {
	private static Thread clientWriterAmqpThread = null;
	private static String threadName;

	public ClientWriterAmqp(String name) {
		threadName = name;
	}

	public void sendData() {
		if (clientWriterAmqpThread == null || !clientWriterAmqpThread.isAlive()) {
			clientWriterAmqpThread = new Thread(this, threadName);
			clientWriterAmqpThread.start();

		}
	}

	@Override
	public void run() {
		try {
			do {
				if (!reqConnection.isOpen() || !respConnection.isOpen()) {
					isReqRunning = false;
					isRespRunning = false;
					break;
				} else if (!isAuthenticated) {
					if (!authenticateUser()) {
						return;
					}
					int count = 0;
					waitForAuthResponse(count);
				} else {
					if (senderQueue.size() > 0) {
						ServerData serverData = (ServerData) senderQueue
								.elementAt(0);
						call(serverData);
						senderQueue.remove(serverData);
					}
				}
			} while (senderQueue.size() > 0 && isReqRunning && isRespRunning);
		} catch (Exception e) {
			e.printStackTrace();
			closeConnection();
		}
	}

	private boolean authenticateUser() throws Exception {
		try {
			String authData = getAuthMessage();
			if (authData == null || authData.trim().equals("")) {
				return false;
			}
			String token = AUTH_DATA + Sender.getInstance().getToken();
			ServerData headerAuthData = new ServerData(authData, token);
			headerAuthData.setSendStatus(ServerData.TO_WAIT);
			call(headerAuthData);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void call(ServerData message) throws Exception {

		BasicProperties props = new BasicProperties.Builder()
				.correlationId(message.getKey()).replyTo(replyQueueName)
				.build();
		reqChannel.basicPublish("", requestQueueName, props, message.getData()
				.getBytes());
		handleAckNack(message.getKey(), 1);
	}

	public void waitForAuthResponse(int count) {
		while (!isAuthenticated && count++ < 15) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ClientAttachmentWriter extends SenderClientAmqp implements Runnable {
	private Thread thread;
	private String threadName;

	public ClientAttachmentWriter(String name) {
		this.threadName = name;
	}

	public void sendData() {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

	@Override
	public void run() {
		try {
			while (senderAttachmentQueue.size() > 0) {

				ServerData serverData = (ServerData) senderAttachmentQueue
						.elementAt(0);
				String response = sendAttachmentContent(serverData.getUrl(),
						serverData.getKey());

				if (response != null) {
					String data = serverData.getData() + response
							+ Attachment.endAttachmentJsonPartAmqp();
					serverData.setData(data);
					RPCClientSender.getInstance().send(serverData);
				} else {
					// failure condition
				}
				if (senderAttachmentQueue.size() > 0) {
					senderAttachmentQueue.removeElementAt(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized static String sendAttachmentContent(String filePath,
			String token) {
		try {
			if (filePath != null && !filePath.equals("")) {
				File file = new File(filePath);
				if (file.exists()) {
					String responseUrl = new AttachmentDataConnection()
							.uploadAttachment(filePath, token);
					if (responseUrl != null
							&& !responseUrl.toString().equals("")) {
						JSONObject jsonMessageObject = new JSONObject(
								responseUrl);
						if (jsonMessageObject.has("uploaded_file_name")) {
							String fileName = (String) jsonMessageObject
									.get("uploaded_file_name");
							return fileName;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

class SenderClientAmqp {
	protected static Vector<ServerData> senderQueue = new Vector<ServerData>();
	protected static Vector<ServerData> senderAttachmentQueue = new Vector<ServerData>();

	protected static Vector<String> ackNackQueue = new Vector<String>();

	protected volatile static boolean isRespRunning = false;
	protected volatile static boolean isReqRunning = false;
	protected String requestQueueName = "rpc_queue";
	protected static final String AUTH_DATA = "Auth@";
	public static final String COMMAND_QUIT = "QUIT";

	protected static Connection reqConnection;
	protected static Channel reqChannel;

	protected static Connection respConnection;
	protected static Channel respChannel;
	protected static String replyQueueName;
	protected static QueueingConsumer consumer;

	protected static ClientWriterAmqp clientwriter;
	protected static ClientReaderAmqp clientreader;
	protected static ClientAttachmentWriter clientAttachmentWriter;
	protected static boolean isAuthenticated;

	protected String getAuthMessage() {
		String key = getAuthKey();
		if (key != null && !key.trim().equals("")) {
			return "{\"cmd\":1,\"key\":\"" + key + "\"}";
		}
		return "";
	}

	protected String getAuthKey() {
		String key = "";
		DBLomentDataMapper lomentDataMapper = DBLomentDataMapper.getInstance();
		DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();

		LomentDataModel lomentDataModel = lomentDataMapper.getLomentData();
		AccountsModel accountsModel = accountsMapper.getAccount();
		String deviceId = "";

		if (AccountHandler.getDeviceID()
				.contains(lomentDataModel.getDeviceId())) {
			deviceId = lomentDataModel.getDeviceId();
			if (!lomentDataModel.getLomentId().trim().equals("")
					&& !lomentDataModel.getPassword().trim().equals("")
					&& !accountsModel.getCashewnutId().trim().equals("")
					&& deviceId != null) {

				key = Base64.encode((lomentDataModel.getLomentId() + ":"
						+ lomentDataModel.getPassword() + ":"
						+ accountsModel.getCashewnutId() + ":" + deviceId)
						.getBytes());
			}
		}
		return key;
	}

	protected synchronized void handleAckNack(String token, int type) {
		try {
			switch (type) {
			case 1:
				ackNackQueue.add(token);
				break;
			case 2:
				ackNackQueue.remove(token);
				break;
			case 3:
				// failure condition
				// ackNackQueue.clear();
				while (ackNackQueue.size() > 0) {
					String t = ackNackQueue.get(0);
					evaluteValueSendFeature(t);
					ackNackQueue.remove(t);
				}

			}
		} catch (Exception e) {
		}
	}

	public void evaluteValueSendFeature(String token) {
		Sender.getInstance().fireSenderListener(token, "-1" + "");
	}

	protected boolean evaluateValueSendSuccess(String message, String token) {
		try {
			// success condition

			// cmd:1. Authentication
			// cmd:2. Header and Body send
			// cmd:3. Attachment send
			// cmd:4. flag send
			// cmd:5. Message sync
			// cmd:6. Get Attachment Data
			// cmd:7. Single message
			// cmd:8. create group
			// cmd:9. Add member
			// cmd:10.Remove member
			// cmd:11.Change Group Name
			// cmd:12.Change Admin
			// cmd:13.Get Group data//17134

			if (message != null && !message.trim().equals("")
					&& message.trim().startsWith("{")) {
				// System.out.println(message);
				JSONObject jObject = new JSONObject(message);
				if (jObject.has("sync_required")
						&& jObject.getString("sync_required").equals("true")) {
					ReceiverHandler.getInstance().requestMsgSync(true);
				}
				if (jObject.has("queue") && jObject.getString("queue") != null) {
					RPCClientReceiver.setReplyQueueName(jObject
							.getString("queue"));
					isAuthenticated = true;
					RPCClientReceiver.getInstance().getMessages();
				}

				if (jObject.has("status")) {
					String status = jObject.get("status").toString();
					if (status.equals("3")) {
						// {"cmd":"2","status":3,"comments":"No sessions  found for given session id"}
						isAuthenticated = false;
					}
				}

				if (jObject.has("cmd")) {
					String cmd = jObject.get("cmd").toString();
					if (cmd.equals("6") || cmd.equals("4") || cmd.equals("7")) {
						// flags update call && get attachment
						Receiver.getInstance().fireReceiverListener(token,
								message + "");
					}
					if (cmd.equals("3") || cmd.equals("2") || cmd.equals("8")
							|| cmd.equals("13") || cmd.equals("9")
							|| cmd.equals("10") || cmd.equals("12")
							|| cmd.equals("11")) {
						// Header & body and attachment send response
						Sender.getInstance().fireSenderListener(token,
								message + "");
					}
					if (cmd.equals("1")) {
						String status = jObject.get("status").toString();
						if (status.equals("10001")) {
							// CashewnutApplication.isValidPassword = false;
							return false;
						}
					}
				}
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected void closeConnection() {
		try {
			closeChannel(respChannel);
			closeReqConnection();
			closeRespConnection();

			isReqRunning = false;
			isRespRunning = false;
			isAuthenticated = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void closeChannel(Channel channel) {
		if (channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (Exception e) {

			}
		}
	}

	protected void closeRespConnection() throws IOException {
		try {
			if (respConnection != null && respConnection.isOpen()) {
				respConnection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void closeReqConnection() throws IOException {
		try {
			if (reqConnection != null && reqConnection.isOpen()) {
				reqConnection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}