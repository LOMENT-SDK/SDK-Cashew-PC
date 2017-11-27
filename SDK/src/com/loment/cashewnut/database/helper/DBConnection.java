package com.loment.cashewnut.database.helper;

import com.loment.cashewnut.util.Helper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.JDBC;

/**
 *
 * @author sekhar
 */
public class DBConnection extends JDBC {

	// register the driver
	public static String DriverName = "org.sqlite.JDBC";
	// now we set up a set of fairly basic string variables to use in the body
	// of the code proper
	public static String jdbc = "jdbc:sqlite";
	private static DBConnection instance = null;
	private Connection connection = null;
	boolean debug = true;
	public static final Lock lock = new ReentrantLock();

	private DBConnection() {
	}

	public static DBConnection getInstance() {
		try {
			if (instance == null) {
				instance = new DBConnection();
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return instance;
	}

	public Connection getConnection(String dbName) {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				// DriverManager.registerDriver(this);

				// SQLiteDataSource dataSource = new SQLiteDataSource();
				// dataSource.setUrl("jdbc:sqlite:" + dbName);
				// connection = dataSource.getConnection();

				// Class.forName("org.sqlite.JDBC");
				// connection = DriverManager.getConnection("jdbc:sqlite:" +
				// dbName);
				// SQLiteConfig config = new SQLiteConfig();
				// config.setReadOnly(true);
				// config.setSharedCache(true);
				// config.recursiveTriggers(true);
				// ... other configuration can be set via SQLiteConfig object

				String dataBase = Helper.getCashewnutDatabasePath(dbName);
				connection = DriverManager.getConnection("jdbc:sqlite:"
						+ dataBase);
			} catch (Exception ex) {
				Logger.getLogger(DBConnection.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		return connection;
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(DBConnection.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	public void setLock() {
		// lock.lock();
	}

	public void closeLock() {
		// lock.unlock();
	}
}
