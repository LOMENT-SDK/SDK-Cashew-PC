package com.loment.cashewnut.database.helper;

import com.loment.cashewnut.AppConfiguration;

public class DatabaseHandler {

	// Database Version
	public static final int DATABASE_VERSION = 1;
	// Database Name
	public static final String DATABASE_NAME = AppConfiguration
			.getDatabaseName();

	// index
	public static boolean isSetIndex = true;

	// Database Table Names
	public static final String TABLE_LOMENT_DATA = "table1";
	public static final String TABLE_ACCOUNTS = "table2";
	public static final String TABLE_MESSAGE = "table3";
	public static final String TABLE_ATTACHMENT = "table4";
	public static final String TABLE_RECIPIENT = "table5";
	public static final String TABLE_CONTACTS = "table6";
	public static final String TABLE_SETTINGS = "table7";
	public static final String TABLE_PROFILE = "table8";
	public static final String TABLE_SELF_PROFILE = "table9";
	public static final String TABLE_GROUPS = "table10";
	public static final String TABLE_MSG_SYNC_DETAILS = "table11";
	public static final String TABLE_GROUP_TYPE = "table12";
	public static final String TABLE_CONTACTS_REQUEST="table13";
	public static final String TABLE_AUTO_RESPONSE="table14";
	// public static final String
	// TABLE_SERVER_NOTIFICATIONS="server_notifications";

	// Loment_Data Table Columns names

	public static final String KEY_USERNAME = "record1";
	public static final String KEY_PASSWORD = "record2";
	public static final String KEY_PRIMARY_EMAIL = "record3";
	public static final String KEY_LOMENT_ID = "record4";
	public static final String KEY_MOBILE_NUMBER = "record5";
	public static final String KEY_COUNTRY_CODE = "record6";
	public static final String KEY_COUNTRY_ABBR = "record7";
	public static final String KEY_DEVICE_NAME = "record8";
	public static final String KEY_USER_ID = "record9";
	public static final String KEY_DEVICE_ID = "record10";
	public static final String KEY_SUBSCRIPTION_ID = "record11";
	public static final String KEY_SUBSCRIPTION_TYPE = "record12";
	public static final String KEY_SUBSCRIPTION_STATUS = "record13";
	public static final String KEY_START_DATE = "record14";
	public static final String KEY_END_DATE = "record15";

	// Accounts Table Columns names
	public static final String KEY_CASHEWNUT_ID = "record16";
	public static final String KEY_CREATATION_DATE = "record17";
	public static final String KEY_STATUS = "record18";
	public static final String KEY_RECEIVED_MESSAGE_QUEUE = "record19";
	public static final String KEY_START_MSG_ID = "record20";
	public static final String KEY_LAST_MSG_ID = "record21";

	// Common Column Names
	public static final String KEY_LOCAL_HEADER_ID = "record0";

	// Message Table Columns Names
	public static final String KEY_SERVER_MSGID = "record22";
	public static final String KEY_FROM = "record23";
	public static final String KEY_MSG_TYPE = "record24";
	public static final String KEY_MSG_FOLDER = "record25";
	public static final String KEY_PRIORITY = "record26";
	public static final String KEY_RESTRICTED = "record27";
	public static final String KEY_MSG_ACK = "record28";
	public static final String KEY_EXPIRY_MINUTS = "record29";
	public static final String KEY_EXPIRY_TIME = "record30";
	public static final String KEY_SCHEDULE_STATUS = "record31";
	public static final String KEY_SCHEDULE_TIME = "record32";
	public static final String KEY_SUBJECT = "record33";
	public static final String KEY_CREATION_TIME = "record34";
	public static final String KEY_LAST_UPDATE_TIME = "record35";
	public static final String KEY_SEND_STATUS = "record36";
	public static String KEY_NUMBER_OF_BODYPARTS = "record37";
	public static final String KEY_DELETE_STATUS = "record38";
	public static final String KEY_VERSION = "record39";
	public static final String KEY_MSG_FROM_THIS_DEVICE = "record40";

	// Attachment Table Columns Names
	public static String KEY_ATTACHMENT_ID = "record41";
	public static String KEY_ATTACHMENT_NAME = "record42";
	public static String KEY_ATTACHMENT_SIZE = "record43";
	public static String KEY_ATTACHMENT_TYPE = "record44";
	public static String KEY_ATTACHMENT_PADDING = "record45";
	public static String KEY_ATTACHMENT_PATH = "record46";

	// Recipient Table Columns Names
	public static String KEY_RECIPIENT_LOCAL_ID = "record58";
	public static String KEY_RECIPIENT_CASHEWNUT_ID = "record59";
	public static String KEY_RECIPIENT_FOLDER = "record60";
	public static String KEY_REC_READ_STATUS = "record61";
	public static String KEY_REC_ACK_STATUS = "record62";
	public static String KEY_REC_DELETE_STATUS = "record63";

	// Settings table colum names
	public static final String KEY_SETTINGS_PRIORITY = "record47";
	public static final String KEY_SETTINGS_RESTRICTED = "record48";
	public static final String KEY_SETTINGS_EXPIRY_MINUTS = "record49";
	public static final String KEY_SETTINGS_EXPIRY_STATUS = "record50";
	public static final String KEY_SETTINGS_ACK = "record51";
	public static final String KEY_SETTINGS_LANGUAGE = "record52";
	public static final String KEY_HIBERNATION_TIME = "record53";
	public static final String KEY_HIBERNATION_STATUS = "record54";
	public static final String KEY_REMEMBER_ME_STATUS = "record55";
	public static final String KEY_PLAY_NOTIFICATION = "record56";
	public static final String KEY_VIBRATE_NOTIFICATION = "record57";

	public static final String KEY_SIGNATURE = "record64";
	public static final String KEY_AUTORESPONSE_STATUS = "record65";
	public static final String KEY_AUTORESPONSE_MESSAGE = "record66";
	public static final String KEY_FONT_FAMILY = "record83";

	// CONTACTS Table - column names
	public static final String KEY_CONTACT_ID = "record67";
	public static final String KEY_CONTACT_LINKED_ID = "record68";
	public static final String KEY_CONTACT_SERVER_ID = "record69";
	public static final String KEY_OWNER_LOMENT_ID = "record70";
	public static final String KEY_PROFILE_ID = "record71";
	public static final String KEY_FIRSTNAME = "record72";
	public static final String KEY_LASTNAME = "record73";
	public static final String KEY_WALNUT_ID = "record74";
	public static final String KEY_PEANUT_ID = "record75";
	public static final String KEY_PHONE = "record76";
	public static final String KEY_EMAIL = "record77";
	public static final String KEY_PHOTO_URL = "record78";
	public static final String KEY_PHOTO = "record79";
	public static final String KEY_NOTES = "record80";
	public static final String KEY_STATUS_MSG = "record81";
	public static final String KEY_LAST_UPDATE_DATE = "record82";
	public static final String KEY_LAST_MESSAGE_SYNC_TIME = "record83";
	
	// Groups Table Columns
	public static final String KEY_GROUP_ID = "record58";
	public static final String KEY_SERVER_GROUP_ID = "record59";
	public static final String KEY_GROUP_NAME = "record60";
	public static final String KEY_GROUP_CREATED_BY = "record61";
	public static final String KEY_GROUP_TYPE = "record62";
	public static final String KEY_GROUP_MEMBERS = "record63";
	public static final String KEY_GROUP_ADMINS = "record84";
	public static final String KEY_GROUP_FEATURES = "record85";
	public static final String KEY_GROUP_EXTRAS = "record86";
	
	// Group Type Column names
	public static boolean KEY_GROUPS_TYPE = true;
	
	//contacts Request columns
	public static final String KEY_LOCAL_ID = "local_id";
	public static final String KEY_SERVER_ID = "server_id";
	public static final String KEY_CONTACT_FROM = "message_from";
	public static final String KEY_TO = "message_to";
	public static final String KEY_TYPE = "message_type";
	public static final String KEY_REQUEST_STATUS = "message_status";
	public static final String KEY_COMMENTS = "message_comments";
	public static final String KEY_COMMAND = "message_command";
	public static final String KEY_REQUEST_CREATION_TIME = "creation_time";
	public static final String KEY_REQUEST_LAST_UPDATE_TIME = "last_updated_time";
	public static final String KEY_SERVER_NAME = "server_name";
	public static final String KEY_PHONE_NUMBER = "phone_number";
	public static final String KEY_EMAIL_ID= "email_id";
	
	//Auto Response Columns
	public static final String KEY_RECEPIENT_ID="recepient_id";
	public static final String KEY_AUTO_RESPONSE_CASHEWID="auto_response_cashewId";
	public static final String KEY_AUTO_RESPONSE_MESSAGE="auto_response_message";
	public static final String KEY_AUTO_RESPONSE_DATE="auto_response_date";
	public static final String KEY_AUTO_RESPONSE_TIME="auto_response_time";
	// CONTACTS table create statement
	public static final String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_CONTACTS
			+ "("
			+ KEY_CONTACT_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_CONTACT_SERVER_ID
			+ " INTEGER,"
			+ KEY_CONTACT_LINKED_ID
			+ " TEXT,"
			+ KEY_FIRSTNAME
			+ " TEXT,"
			+ KEY_LASTNAME
			+ " TEXT,"
			+ KEY_WALNUT_ID
			+ " TEXT,"
			+ KEY_CASHEWNUT_ID
			+ " TEXT,"
			+ KEY_PEANUT_ID
			+ " TEXT,"
			+ KEY_PHONE
			+ " TEXT,"
			+ KEY_EMAIL
			+ " TEXT,"
			+ KEY_PHOTO_URL
			+ " TEXT ,"
			+ KEY_PHOTO
			+ " INTEGER ,"
			+ KEY_NOTES
			+ " TEXT ,"
			+ KEY_LAST_UPDATE_DATE + " DATE"
		+ ")";

	public static String INSERT_CONTACTS_DATA_QUERY = "insert into  "
			+ TABLE_CONTACTS + "  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static String ALTER_CONTACT_TABLE="ALTER TABLE "+TABLE_CONTACTS+" ADD COLUMN "+KEY_STATUS_MSG+" INTEGER DEFAULT "+ 0;

	// Settings table create statement
	public static final String CREATE_LAST_MESSAGE_SYNC_TIME_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MSG_SYNC_DETAILS
			+ "("
			+ KEY_LAST_MESSAGE_SYNC_TIME
			+ " TEXT " + ")";

	public static String INSERT_LAST_MESSAGE_SYNC_TIME_DATA_QUERY = "insert into  "
			+ TABLE_MSG_SYNC_DETAILS + "  values(?)";

	// Settings table create statement
	public static final String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_SETTINGS
			+ "("
			+ KEY_SETTINGS_PRIORITY
			+ " INTEGER,"
			+ KEY_SETTINGS_RESTRICTED
			+ " INTEGER,"
			+ KEY_SETTINGS_EXPIRY_MINUTS
			+ " INTEGER,"
			+ KEY_SETTINGS_EXPIRY_STATUS
			+ " INTEGER,"
			+ KEY_SETTINGS_ACK
			+ " INTEGER,"
			+ KEY_SETTINGS_LANGUAGE
			+ " TEXT,"
			+ KEY_HIBERNATION_TIME
			+ " INTEGER,"
			+ KEY_REMEMBER_ME_STATUS
			+ " INTEGER,"
			+ KEY_PLAY_NOTIFICATION
			+ " INTEGER,"
			+ KEY_SIGNATURE
			+ " TEXT,"
			+ KEY_AUTORESPONSE_MESSAGE
			+ " TEXT,"
			+ KEY_AUTORESPONSE_STATUS
			+ " INTEGER,"
			+ KEY_FONT_FAMILY
			+ " TEXT " + ")";

	public static String INSERT_SETTINGS_DATA_QUERY = "insert into  "
			+ TABLE_SETTINGS + "  values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	// Create Database Tables
	// Loment_Data table create statement
	public static final String CREATE_LOMENT_DATA_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_LOMENT_DATA
			+ "("
			+ KEY_USERNAME
			+ " TEXT,"
			+ KEY_PASSWORD
			+ " TEXT,"
			+ KEY_LOMENT_ID
			+ " TEXT,"
			+ KEY_PRIMARY_EMAIL
			+ " TEXT,"
			+ KEY_MOBILE_NUMBER
			+ " TEXT,"
			+ KEY_COUNTRY_CODE
			+ " TEXT,"
			+ KEY_COUNTRY_ABBR
			+ " TEXT,"
			+ KEY_DEVICE_NAME
			+ " TEXT,"
			+ KEY_DEVICE_ID
			+ " TEXT,"
			+ KEY_USER_ID
			+ " TEXT,"
			+ KEY_SUBSCRIPTION_ID
			+ " TEXT,"
			+ KEY_SUBSCRIPTION_TYPE
			+ " TEXT,"
			+ KEY_SUBSCRIPTION_STATUS
			+ " TEXT,"
			+ KEY_START_DATE
			+ " TEXT,"
			+ KEY_END_DATE + " TEXT" + ")";

	public static String INSERT_LOMENT_DATA_QUERY = "insert into  "
			+ TABLE_LOMENT_DATA + "  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	public static final String CREATE_TABLE_CONTACTS_REQUEST = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_CONTACTS_REQUEST
			+ "("
			+ KEY_LOCAL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_SERVER_ID
			+ " TEXT,"
			+ KEY_FROM
			+ " TEXT,"
			+ KEY_TO
			+ " TEXT,"
			+ KEY_TYPE
			+ " INTEGER,"
			+ KEY_STATUS
			+ " INTEGER,"
			+ KEY_COMMENTS
			+ " TEXT,"
			+ KEY_CREATION_TIME
			+ " INTEGER,"
			+ KEY_LAST_UPDATE_TIME
			+ " INTEGER,"
			+ KEY_PHONE_NUMBER
			+ " TEXT,"
			+ KEY_EMAIL_ID
			+ " TEXT," 
			+ KEY_SERVER_NAME 
			+ " TEXT" + ")";
	
	public static String CONTACTS_REQUEST_DATA_QUERY = "insert into  "
			+ TABLE_LOMENT_DATA + "  values(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	// ===========================================================================

	// Loment_Data table create statement
	public static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_ACCOUNTS
			+ "("
			+ KEY_CASHEWNUT_ID
			+ " TEXT,"
			+ KEY_CREATATION_DATE
			+ " TEXT,"
			+ KEY_STATUS
			+ " TEXT,"
			+ KEY_START_MSG_ID
			+ " INTEGER ,"
			+ KEY_LAST_MSG_ID
			+ " INTEGER,"
			+ KEY_RECEIVED_MESSAGE_QUEUE + " TEXT" + ")";

	public static String INSERT_ACCOUNT_DATA_QUERY = "insert into  "
			+ TABLE_ACCOUNTS + "  values(?,?,?,?,?,?)";

	// Create Message Table if not Exists

	public static final String CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MESSAGE
			+ "("
			+ KEY_LOCAL_HEADER_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_SERVER_MSGID
			+ " INTEGER,"
			+ KEY_FROM
			+ " TEXT,"
			+ KEY_MSG_TYPE
			+ " INTEGER ,"
			+ KEY_MSG_FOLDER
			+ " TEXT,"
			+ KEY_PRIORITY
			+ " INTEGER ,"
			+ KEY_RESTRICTED
			+ " INTEGER,"
			+ KEY_MSG_ACK
			+ " INTEGER ,"
			+ KEY_EXPIRY_MINUTS
			+ " INTEGER,"
			+ KEY_EXPIRY_TIME
			+ " INTEGER,"
			+ KEY_SCHEDULE_STATUS
			+ " INTEGER,"
			+ KEY_SCHEDULE_TIME
			+ " INTEGER,"
			+ KEY_SUBJECT
			+ " TEXT ,"
			+ KEY_CREATION_TIME
			+ " INTEGER,"
			+ KEY_LAST_UPDATE_TIME
			+ " INTEGER ,"
			+ KEY_SEND_STATUS
			+ " INTEGER,  "
			+ KEY_NUMBER_OF_BODYPARTS
			+ " INTEGER,"
			+ KEY_DELETE_STATUS
			+ " INTEGER, "
			+ KEY_VERSION
			+ " TEXT, "
			+ KEY_MSG_FROM_THIS_DEVICE
			+ " INTEGER, "
			+ KEY_GROUP_ID + " TEXT, " + KEY_GROUP_NAME + " TEXT " + ")";

	public static String INSERT_HEADER_DATA_QUERY = "insert into  "
			+ TABLE_MESSAGE
			+ "  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	// ===================================================================================================================

	// Create Attachment Table if not Exists
	public static final String CREATE_TABLE_ATTACHMENT = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_ATTACHMENT
			+ "("
			+ KEY_ATTACHMENT_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_LOCAL_HEADER_ID
			+ " INTEGER,"
			+ KEY_ATTACHMENT_NAME
			+ " TEXT,"
			+ KEY_ATTACHMENT_PATH
			+ " TEXT, "
			+ KEY_ATTACHMENT_TYPE
			+ " TEXT, "
			+ KEY_ATTACHMENT_SIZE
			+ " INTEGER,"
			+ KEY_ATTACHMENT_PADDING
			+ " INTEGER,"
			+ " FOREIGN KEY("
			+ KEY_LOCAL_HEADER_ID
			+ ") REFERENCES "
			+ TABLE_MESSAGE
			+ "("
			+ KEY_LOCAL_HEADER_ID
			+ "))";

	public static String INSERT_ATTACHMENT_DATA_QUERY = "insert into  "
			+ TABLE_ATTACHMENT + "  values(?,?,?,?,?,?,?)";

	// =====================================================================================================================

	// Create Recipient Table if not Exists
	public static String CREATE_TABLE_RECIPIENT = "CREATE TABLE IF NOT EXISTS  "
			+ TABLE_RECIPIENT
			+ "("
			+ KEY_RECIPIENT_LOCAL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ KEY_LOCAL_HEADER_ID
			+ " INTEGER,"
			+ KEY_RECIPIENT_CASHEWNUT_ID
			+ " TEXT,"
			+ KEY_REC_READ_STATUS
			+ " INTEGER,"
			+ KEY_REC_ACK_STATUS
			+ " INTEGER,"
			+ KEY_REC_DELETE_STATUS
			+ " INTEGER,"
			+ "FOREIGN KEY("
			+ KEY_LOCAL_HEADER_ID
			+ ") REFERENCES  "
			+ TABLE_MESSAGE + "(" + KEY_LOCAL_HEADER_ID + ")" + ")";

	public static String INSERT_RECEIPIENT_DATA_QUERY = "insert into  "
			+ TABLE_RECIPIENT + "  values(?,?,?,?,?,?)";

	// ==========================================================================================================================

	// Create Recipient Table if not Exists
	// Create Groups Table if not Exists
	public static String CREATE_TABLE_GROUPS = "CREATE TABLE IF NOT EXISTS  " 
			+ TABLE_GROUPS + "(" 
			+ KEY_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
			+ KEY_SERVER_GROUP_ID + " TEXT," 
			+ KEY_GROUP_NAME + " TEXT,"
			+ KEY_GROUP_CREATED_BY + " TEXT," 
			+ KEY_GROUP_TYPE + " INTEGER," 
			+ KEY_GROUP_MEMBERS + " TEXT,"
			+ KEY_GROUP_ADMINS + " TEXT," 
			+ KEY_GROUP_FEATURES + " TEXT," 
			+ KEY_GROUP_EXTRAS + " TEXT " + ")";

		public static String INSERT_GROUP_DATA_QUERY = "insert into  " 
				+ TABLE_GROUPS + "  values(?,?,?,?,?,?,?,?,?)";
		
		// Group Type table create statement
		public static final String CREATE_TABLE_GROUP_TYPE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_GROUP_TYPE + "("
				+ KEY_GROUPS_TYPE + " BOOLEAN " + ")";
		
		public static String INSERT_GROUP_TYPE_QUERY = "insert into " 
				+ TABLE_GROUP_TYPE + " values(?)";

		//Auto Response Table
		
		public static String CREATE_TABLE_AUTO_RESPONSE = "CREATE TABLE IF NOT EXISTS  " 
				+ TABLE_AUTO_RESPONSE + "(" 
				+ KEY_RECEPIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," 
				+ KEY_AUTO_RESPONSE_CASHEWID + " TEXT," 
				+ KEY_AUTO_RESPONSE_MESSAGE + " TEXT,"
				+ KEY_AUTO_RESPONSE_DATE + " DATE," 
				+ KEY_AUTO_RESPONSE_TIME + " TIMESTAMP"  + ")";
		
		public static String INSERT_AUTO_RESPONSE_DATA_QUERY = "insert into  "
				+ TABLE_AUTO_RESPONSE + "  values(?,?,?,?,?)";
		
	public DatabaseHandler() {
	}

	// public static void createTables(SQLiteDatabase db) {
	// Log.d("== table check ==", " Checking whether exists  the table");
	// // creating required tables
	// db.execSQL(CREATE_LOMENT_DATA_TABLE);
	// db.execSQL(CREATE_ACCOUNTS_TABLE);
	// db.execSQL(CREATE_TABLE_MESSAGE);
	// db.execSQL(CREATE_TABLE_ATTACHMENT);
	// db.execSQL(CREATE_TABLE_RECIPIENT);
	// db.execSQL(CREATE_SETTINGS_TABLE);
	// db.execSQL(CREATE_TABLE_CONTACTS);
	// db.execSQL(CREATE_TABLE_GROUPS);
	// // alterTable(db);
	// }

	// private static void alterTable(SQLiteDatabase db) throws SQLException {
	// // alter table
	// boolean versionExists = existsColumnInTable(db,
	// DatabaseHandler.TABLE_MESSAGE, KEY_VERSION);
	// boolean selfExists = existsColumnInTable(db,
	// DatabaseHandler.TABLE_MESSAGE, KEY_MSG_FROM_THIS_DEVICE);
	// boolean groupIdExists = existsColumnInTable(db,
	// DatabaseHandler.TABLE_MESSAGE, KEY_GROUP_ID);
	// boolean groupNameExists = existsColumnInTable(db,
	// DatabaseHandler.TABLE_MESSAGE, KEY_GROUP_NAME);
	//
	// if (!versionExists) {
	// db.execSQL("ALTER TABLE " + TABLE_MESSAGE + " ADD COLUMN "
	// + KEY_VERSION + " TEXT ; ");
	// }
	//
	// if (!selfExists) {
	// db.execSQL("ALTER TABLE " + TABLE_MESSAGE + " ADD COLUMN "
	// + KEY_MSG_FROM_THIS_DEVICE + " INTEGER ; ");
	// }
	//
	// if (!groupIdExists) {
	// db.execSQL("ALTER TABLE " + TABLE_MESSAGE + " ADD COLUMN "
	// + KEY_GROUP_ID + " TEXT ; ");
	// }
	//
	// if (!groupNameExists) {
	// db.execSQL("ALTER TABLE " + TABLE_MESSAGE + " ADD COLUMN "
	// + KEY_GROUP_NAME + " TEXT ; ");
	// }
	// }
	//
	// private static boolean existsColumnInTable(SQLiteDatabase inDatabase,
	// String inTable, String columnToCheck) {
	// Cursor mCursor = null;
	// try {
	// // query 1 row
	// mCursor = inDatabase.rawQuery("SELECT * FROM " + inTable
	// + " LIMIT 0", null);
	//
	// // getColumnIndex gives us the index (0 to ...) of the column -
	// // otherwise we get a -1
	// if (mCursor.getColumnIndex(columnToCheck) != -1)
	// return true;
	// else
	// return false;
	//
	// } catch (Exception Exp) {
	// // something went wrong. Missing the database? The table?
	// Log.d("... - existsColumnInTable",
	// "When checking whether a column exists in the table, an error occurred: "
	// + Exp.getMessage());
	// return false;
	// } finally {
	// mCursor.close();
	// }
	// }
	//
	// public void droptables(SQLiteDatabase db) {
	// // on upgrade drop older tables
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOMENT_DATA);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACHMENT);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPIENT);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
	// }
	//
	// // closing database
	// public void closeDB(SQLiteDatabase db) {
	// if (db != null && db.isOpen())
	// db.close();
	// }

}
