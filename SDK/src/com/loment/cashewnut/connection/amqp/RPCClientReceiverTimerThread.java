package com.loment.cashewnut.connection.amqp;

import java.util.Timer;
import java.util.TimerTask;


import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.receiver.Receiver;

/**
 * 
 * @author sekhar
 */
public class RPCClientReceiverTimerThread {

	private Timer t = new Timer();
	private boolean isTimerRunning = false;
	static RPCClientReceiverTimerThread instance = null;
	static long period = 60000;
	static int privacy = 1;

	private RPCClientReceiverTimerThread() {
		period = 120000 ; // 2 mins
	}

	public static RPCClientReceiverTimerThread getInstance() {
		if (instance == null) {
			instance = new RPCClientReceiverTimerThread();
		}
		return instance;
	}

	public void startIdleTimer() {
		if (isTimerRunning) {
			return;
		}

		t = new Timer();
		TimerTask tt = new TimerTask() {
			public void run() {
				try {
					Receiver.getInstance().receiveAMQP();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		t.schedule(tt, 10000, period);
		isTimerRunning = true;
	}

	public void stopIdleTimer() {
		if (t != null) {
			t.cancel();
			CashewnutActivity.isHibernate = false;
			isTimerRunning = false;
		}
	}

	public boolean isTimerRunning() {
		return isTimerRunning;
	}
}
