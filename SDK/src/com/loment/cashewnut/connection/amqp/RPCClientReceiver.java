package com.loment.cashewnut.connection.amqp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.connection.ServerData;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.receiver.ContactReceiver;
import com.loment.cashewnut.receiver.FlagsStatusReceiver;
import com.loment.cashewnut.receiver.GroupReceiver;
import com.loment.cashewnut.receiver.GroupReceiverType6;
import com.loment.cashewnut.receiver.HeaderReceiver;
import com.loment.cashewnut.receiver.Receiver;
import com.loment.cashewnut.receiver.ReceiverHandler;
import com.loment.cashewnut.sthithi.connection.SubscriptionHandler;
import com.loment.cashewnut.util.Helper;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.PossibleAuthenticationFailureException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 
 * @author sekhar
 */
public class RPCClientReceiver extends ReceiverClientAmqp {

	private static RPCClientReceiver instance;
	private static int i = 0;

	private RPCClientReceiver() {
	}

	public synchronized boolean openConnection() {
		try {

			if (connection == null || !connection.isOpen() || !isRunning) {

				if (!SubscriptionHandler.getInstance().getSubscriptionStatusFromDB()) {
					if (isPrintLog)
						printLog("20. Subscription fail.");
					return false;
				}

				RPCClientReceiverTimerThread.getInstance().startIdleTimer();
				if (CashewnutApplication.isInternetOn()) {

					replyQueueName = getReplyQueueName();
					if (replyQueueName.trim().equals("")) {
						return false;
					}
					int port = AppConfiguration.AMQP_DEFAULT_PORT_NUMBER;
					try {
						init(port);
					} catch (java.net.ConnectException e) {
						port = AppConfiguration.AMQP_PUBLIC_PORT_NUMBER;
						init(port);
					}
					String consumerTag = setupConsumer();
					isRunning = true;
					if (isPrintLog) {
						printDetails(consumerTag);
					}
					return true;
				} else {
					if (isPrintLog)
						printLog("2. Network un available - Connection not Opened");
				}
			}
		} catch (PossibleAuthenticationFailureException e) {
			if (isPrintLog)
				printLog("3." + e.getMessage());
			e.printStackTrace();
			closeConnection();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			openConnection();
		} catch (ConsumerCancelledException e) {
			if (isPrintLog)
				printLog("4." + e.getMessage());
			e.printStackTrace();
			closeConnection();
		} catch (ShutdownSignalException e) {
			if (isPrintLog)
				printLog("5." + e.getMessage() + e.getReason());
			closeConnection();
			ReceiverHandler.getInstance().syncMessages();
		} catch (Exception e) {
			if (isPrintLog)
				printLog("6." + e.getMessage());
			e.printStackTrace();
			closeConnection();
		}
		return false;
	}

	private void printDetails(String consumerTag) {
		try {
			printLog("7.rqn: " + replyQueueName.substring(replyQueueName.lastIndexOf("_")));
			if (consumerTag == null || consumerTag.trim().equals("") || consumerTag.trim().equals("null")) {
				printLog("8.consumerTag " + "false");
			} else {
				printLog("8.consumerTag " + "true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printLog("1.Connection Opened");
	}

	private void init(int port) throws Exception {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setRequestedHeartbeat(20);
			factory.setHost(AppConfiguration.getAmqpMsgApi());
			factory.setUsername("msg_reader");
			factory.setPassword("w5g_R3@d3r@312");
			factory.setVirtualHost("cashew_messages");

			factory.setPort(port);// 5672
			connection = factory.newConnection();
			channel = connection.createChannel();

		} catch (java.net.ConnectException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	private String setupConsumer() throws IOException {
		consumer = new QueueingConsumer(channel);
		String consumerTag = channel.basicConsume(replyQueueName, false, consumer);
		return consumerTag;
	}

	public static String getReplyQueueName() {
		try {
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel model = accountsMapper.getAccount();
			String replyQueueName = model.getReceivedMsgQueue();
			if (replyQueueName != null) {
				return replyQueueName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void setReplyQueueName(String replyQueueName) {
		// System.out.println("quename" + replyQueueName);
		try {
			if (replyQueueName != null) {

				DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
				AccountsModel model = accountsMapper.getAccount();
				accountsMapper.updateReceivedMsgQueue(model.getCashewnutId(), replyQueueName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static RPCClientReceiver getInstance() {
		try {
			if (instance == null) {
				instance = new RPCClientReceiver();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public void close() throws Exception {
		connection.close();
	}

	public void getMessages() {
		try {
			openConnection();
			if (connection == null || !connection.isOpen()) {
				if (isPrintLog)
					printLog("9." + "Error: connection not opened");
				return;
			}

			if (clientreader == null) {
				clientreader = new ClientReceiverReaderAmqp("clientReceiver");
			} else {
				clientreader.startThread();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ClientReceiverReaderAmqp extends ReceiverClientAmqp implements Runnable {
	private Thread thread;
	private String threadName;

	public ClientReceiverReaderAmqp(String name) {
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
			callResponse();
		} catch (Exception e) {
			if (isPrintLog)
				printLog("10." + e.getMessage());
		}
	}

	public void callResponse() {
		try {
			while (isRunning) {

				if (!connection.isOpen()) {
					if (isPrintLog)
						printLog("11.callResponse connection close");
					isRunning = false;
					continue;
				}
				if (consumer == null) {
					if (isPrintLog)
						printLog("12.callResponse consumer obj null");
					isRunning = false;
					continue;
				}

				if (consumer != null) {
					QueueingConsumer.Delivery delivery = consumer.nextDelivery(5000);
					if (delivery != null) {
						CashewnutApplication.isAmqpQueueEmpty = false;
						String response = new String(delivery.getBody());
						try {
							evaluteValue(response);
						} catch (Exception e) {
							e.printStackTrace();
						}

						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

					} else {
						if (!CashewnutApplication.isAmqpQueueEmpty) {
							CashewnutApplication.isAmqpQueueEmpty = true;
							if (CashewnutActivity.currentActivity != null)
								((CashewnutActivity) CashewnutActivity.currentActivity).processMessage(null,
										MessageModel.ACTION_INPROGRESS);
						}
					}
				}
			}
		} catch (AlreadyClosedException e) {
			// possibly check if channel was closed
			// by the time we started action and reasons for
			// closing it
			if (isPrintLog)
				printLog("13." + e.getMessage());

			closeConnection();

			// System.out.println("Localised Message: " +
			// e.getLocalizedMessage());
			// System.out.println("Reason: " + e.getReason());
			// System.out.println("Reference: " + e.getReference());

			reConnect();

		} catch (ShutdownSignalException e) {
			// possibly check if channel was closed
			// by the time we started action and reasons for
			// closing it
			if (isPrintLog)
				printLog("14." + e.getMessage() + "  && Reason:" + e.getReason());

			closeConnection();

			// System.out.println("Localised Message: " +
			// e.getLocalizedMessage());
			// System.out.println("Reason: " + e.getReason());
			// System.out.println("Reference: " + e.getReference());

			reConnect();

			// e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			if (isPrintLog)
				printLog("15." + e.getMessage());
			// e.printStackTrace();
			closeConnection();
			ReceiverHandler.getInstance().requestMsgSync(true);
		}

		catch (Exception e) {
			if (isPrintLog)
				printLog("16." + e.getMessage());
			// e.printStackTrace();
			closeConnection();
		}
	}

	private void reConnect() {
		try {
			if (isPrintLog)
				printLog("17." + " try to connection re-open");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			RPCClientReceiver.getInstance().openConnection();
			if (connection == null || !connection.isOpen()) {
				return;
			}
			callResponse();
		} catch (Exception e) {
			if (isPrintLog)
				printLog("18." + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static final String MESSAGE_QUEUE_FULL_MESSAGE = "MSG_FULL";
	public static final String MESSAGE_QUEUE_HEADER_MESSAGE = "MSG_HEADER";
	public static final String MESSAGE_QUEUE_HEADER = "header";
	public static final String MESSAGE_QUEUE_BODY = "body";
	public static final String MESSAGE_QUEUE_TYPE = "type";
	public static final String MESSAGE_QUEUE_GROUP_CREATION_MESSAGE = "GRP_CONF";
	public static final String MESSAGE_QUEUE_GROUP_ADMINS = "admins";

	private void evaluteValue(String message) {
		try {
			JSONObject jObject = new JSONObject(message);
			// System.out.println(message);

			if (jObject.has(MESSAGE_QUEUE_FULL_MESSAGE)) {
				Receiver.getInstance().fireReceiverListener(HeaderReceiver.RECEIVED_HEADER_TOKEN_AMQP,
						jObject.getJSONObject("MSG_FULL").toString());
				return;
			}
			if (jObject.has(MESSAGE_QUEUE_HEADER_MESSAGE)) {
				Receiver.getInstance().fireReceiverListener(FlagsStatusReceiver.RECEIVED_FLAG_UPDATE_TOKEN_AMQP,
						jObject.getJSONObject("MSG_HEADER").toString());
				return;
			}

			if (jObject.has(MESSAGE_QUEUE_GROUP_CREATION_MESSAGE)) {
				groupConfigMessageType4(jObject);
				return;
			}

			if (jObject.has(MESSAGE_QUEUE_HEADER)) {
				JSONObject header = (JSONObject) jObject.get("header");
//				int msgType = Integer.parseInt(header.getString("type") + "");
				int msgType = header.getInt(MESSAGE_QUEUE_TYPE);
				switch (msgType) {
				case 1:
					receiveCashewMsg(jObject);
					break;
				case 2:
					// welcome
					receiveCashewMsg(jObject);
					break;
				case 3:
					 //contact
					 Receiver.getInstance().fireReceiverListener(
					 ContactReceiver.RECEIVED_CONTACT_HEADER_TOKEN_AMQP,
					 jObject.toString());
					break;
				case 4:
					// group config
					if (!AppConfiguration.GROUP_TYPE6) {
						groupConfigMessageType4(header);
					}
					break;
				case 5:
					// auto response
					break;
				case 6:
					// setGroupType6();
					// group config
					if (AppConfiguration.GROUP_TYPE6) {
						groupConfigMessageType6(jObject);
					}
					break;
				case 7:
					// setGroupType6();
					// group config
					receiveCashewMsg(jObject);
					break;
					
				default:
					// Alert user to upgrade app.
					break;
				}
			} else if (jObject.has(MESSAGE_QUEUE_TYPE)) {
				// JSONObject type = jObject.getJSONObject("type");
				String type =  (String) jObject.get(MESSAGE_QUEUE_TYPE);
				int msgType = Integer.parseInt(type.toString() + "");
				switch (msgType) {
				case 1:
					Receiver.getInstance().fireReceiverListener(FlagsStatusReceiver.RECEIVED_FLAG_UPDATE_TOKEN_AMQP,
							jObject.toString());
					break;

				case 3:
					// contact
					Receiver.getInstance().fireReceiverListener(
					 ContactReceiver.RECEIVED_CONTACT_HEADER_TOKEN_AMQP,
					jObject.toString());
					break;
				case 4:
					// group config
					if (!AppConfiguration.GROUP_TYPE6) {
						groupConfigMessageType4(jObject);
					}
					break;
				case 7:
					// setGroupType6();
					// group config
					receiveCashewMsg(jObject);
					break;
				default:
					// Alert user to upgrade app.
					break;
				}
			}
			// Alert user to upgrade app.
		} catch (JSONException e) {
			if (isPrintLog)
				printLog("14." + e.getMessage());
			e.printStackTrace();
			printLog("14." + message);
		}
	}

	private void groupConfigMessageType6(JSONObject responseJson) {
		// new group config
		Receiver.getInstance().fireReceiverListener(GroupReceiverType6.RECEIVED_GROUP_TOKEN_AMQP_TYPE6,
				responseJson.toString());
	}

	private void groupConfigMessageType4(JSONObject responseJson) throws JSONException {
		//
		if (!AppConfiguration.GROUP_TYPE6) {
			// old group config
			if (responseJson.has("GRP_CONF")) {
				Receiver.getInstance().fireReceiverListener(GroupReceiver.GROUP_CONFIG_TOKEN_AMQP_TYPE4,
						responseJson.get("GRP_CONF").toString());
			} else {
				Receiver.getInstance().fireReceiverListener(GroupReceiver.GROUP_CONFIG_TOKEN_AMQP_TYPE4,
						responseJson.toString());
			}
		}
	}

	private void receiveCashewMsg(JSONObject jObject) throws JSONException {
		if (jObject.has(MESSAGE_QUEUE_BODY)) {
			Receiver.getInstance().fireReceiverListener(HeaderReceiver.RECEIVED_HEADER_TOKEN_AMQP, jObject.toString());
		} else {
			Receiver.getInstance().fireReceiverListener(FlagsStatusReceiver.RECEIVED_FLAG_UPDATE_TOKEN_AMQP,
					jObject.toString());
		}
	}
}

class ReceiverClientAmqp {
	protected static Vector<ServerData> receiverQueue = new Vector<ServerData>();

	protected volatile static boolean isRunning = false;
	protected String requestQueueName = "rpc_queue";

	protected static Connection connection;
	protected static Channel channel;
	protected static String replyQueueName;
	protected static QueueingConsumer consumer;
	private static String logFilePath = "";
	protected static boolean isPrintLog = false;

	protected static ClientReceiverReaderAmqp clientreader;

	protected void closeConnection() {
		closeChannelSilently(channel);
		closeConnectionSilently(connection);
		isRunning = false;
	}

	protected void closeChannelSilently(Channel channel) {
		if (channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException e) {
				if (isPrintLog)
					printLog("20." + "Problem closing down channel");
			} catch (AlreadyClosedException e) {
				if (isPrintLog)
					printLog("21." + "Channel was already closed");
			} catch (ShutdownSignalException e) {
				if (isPrintLog)
					printLog("22." + "Got a shutdown signal while closing channel");
			} catch (Exception e1) {
				if (isPrintLog)
					printLog("24." + e1.getMessage());
			}
		}
	}

	private void closeConnectionSilently(Connection connection) {
		if (connection != null && connection.isOpen()) {
			try {
				connection.close();
			} catch (IOException e) {
				if (isPrintLog)
					printLog("25." + "Problem closing down connection");
			} catch (AlreadyClosedException e) {
				if (isPrintLog)
					printLog("26." + "Connection was already closed");
			} catch (ShutdownSignalException e) {
				if (isPrintLog)
					printLog("27." + "Got a shutdown signal while closing connection");
			} catch (Exception e1) {
				if (isPrintLog)
					printLog("28." + e1.getMessage());
			}
		}
	}

	protected void printLog(String log) {
		if (!isPrintLog) {
			return;
		}
		if (logFilePath == null || logFilePath.trim().equals("")) {
			logFilePath = getLogFilePath();
		}
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String val = ft.format(date) + ":" + " CR - " + log + "\n";
		try {
			FileUtils.writeByteArrayToFile(new File(logFilePath), val.getBytes(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getLogFilePath() {
		String absolutePath = Helper.getCashewnutFolderPath("logs");
		String destFilePath = absolutePath + "log" + ".txt";
		return destFilePath;
	}
}