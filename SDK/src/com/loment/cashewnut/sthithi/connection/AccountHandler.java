package com.loment.cashewnut.sthithi.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.util.Helper;

public class AccountHandler {

	private static AccountHandler _instance;

	public static final String USERNAME = "name";
	public static final String PASSWORD = "password";
	public static final String PRIMARY_EMAIL = "primary_email";
	public static final String PRIMARY_MOBILE_NUMBER = "primary_mobile_number";
	public static final String COUNTRY_CODE = "country_abbrev";

	private AccountHandler() {

	}

	public static AccountHandler getInstance() {
		if (_instance == null) {
			_instance = new AccountHandler();
		}
		return _instance;
	}

	/**
	 * ******************************************************* validate loment
	 * user
	 *
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @return
	 */
	public Integer isValidUser(String email, String password) {
		Integer status = -1;
		try {
			String url = AppConfiguration.getSthithiApi() + "/user/" + email + "/authenticate";
			String post = "password=" + password;
			DataConnection conn = DataConnection.getInstance();
			String responseText = conn.sendPost(url, post.getBytes());

			JSONObject responseJson = new JSONObject(responseText);
			status = (Integer) responseJson.get("status");
			String res = responseJson.toString();
			// System.out.println("login device response===" + res);
			if (status == 0) {
				// status is 0 means user is authenticated.
				String fullname = responseJson.getString("name");
				String country = responseJson.getString("country");
				String userid = responseJson.getString("userid");
				String mobile_number = responseJson.getString("primary_mobile_number");
				DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
				headerMapper.insert(
						new LomentDataModel(fullname, password, email, email, mobile_number, country, country, userid));
				return status;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public synchronized boolean getNameFromSthithi(String lomentID, String password, String cashewnutID,int profile_status) {
		try {
			// System.out.println("cashewnutID === " + cashewnutID);
			String url = AppConfiguration.getSthithiApi() + "/user/" + lomentID + "/otheruser/cashewnut/" + cashewnutID
					+ "/getprofile/";
			DataConnection conn = DataConnection.getInstance();

			String post = "password=" + password;
			String responseText = conn.sendPost(url, post.getBytes());
			JSONObject responseJson = new JSONObject(responseText);
			if (responseJson != null && responseJson.has("status")) {
				Integer status = (Integer) responseJson.get("status");
				String res = responseJson.toString();
				// System.out.println("get name response===" + res);
				if (status == 0) { // status is 0 means user is authenticated.
					String fullname = responseJson.getString("name");
					String phone = responseJson.getString("phone");
					String email = responseJson.getString("email");
					// System.out.println("========"+email+phone);
					DBContactsMapper profileMapper = DBContactsMapper.getInstance();
					ContactsModel contactsModel = profileMapper.getContact(cashewnutID,0);
					if(profile_status==0)
					{
						if (contactsModel.getFirstName() == null || contactsModel.getFirstName().trim().length() < 1) {
							profileMapper.insert(cashewnutID, fullname, phone, email);
							// System.out.println("Contact inserted successfully");
						} else {
							profileMapper.update(cashewnutID, fullname, phone, email,0);
							// System.out.println("Contact updated successfully");
						}	
					}
					else
					{
						profileMapper.update(cashewnutID, fullname, phone, email,0);	
					}
					

					return true;
				} else if (status == 30002) {
					// System.out.println("Cashewnut Id sent " + cashewnutID
					// + "does not exist");
					return false;
				}
			} else {
				// System.out.println(responseJson);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ******************************************************* register loment
	 * account
	 */
	public String register(String name, String lomentpwd, String primaryemail, String number, String country_code) {
		String post = "name=" + name + "&password=" + lomentpwd + "&primary_email=" + primaryemail
				+ "&primary_mobile_number=" + number + "&country_abbrev=" + country_code + "&partner="
				+ AppConfiguration.getPartnerId();
		String response = "";
		try {
			DataConnection conn = DataConnection.getInstance();
			response = conn.sendPost(AppConfiguration.getSthithiApi() + "/user/register", post.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public String register(String name, String lomentpwd, String primaryemail, String number, String country_code,
			String cashew_id, String promo_code) {
		Vector deviceId = getDeviceID();
		final int DEVICE_TYPE_PC = 7;
		String deviceidstring = "";
		if (deviceId.size() > 0) {
			deviceidstring = (String) deviceId.elementAt(0);
		}

		String post = "name=" + name + "&password=" + lomentpwd + "&primary_email=" + primaryemail
				+ "&primary_mobile_number=" + number + "&country_abbrev=" + country_code + "&partner="
				+ AppConfiguration.getPartnerId() + "&cashew_id=" + cashew_id + "&promo_code=" + promo_code
				+ "&operating_system=" + getOS() + "&device_name=" + "new" + "&device_id=" + "new"
				+ "&device_identifier=" + deviceidstring + "&device_type_id=" + DEVICE_TYPE_PC;

		String response = "";
		try {
			DataConnection conn = DataConnection.getInstance();
			response = conn.sendPost(AppConfiguration.getSthithiApi() + "/user/register/one/step", post.getBytes());
			JSONObject responseJson = new JSONObject(response);

			Integer status = (Integer) responseJson.get("status");
			if (status == 0) {

				JSONObject user_profile = responseJson.getJSONObject("user_profile");
				String userid = (String) user_profile.get("userid");

				DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
				LomentDataModel lomentDataModel = new LomentDataModel(name, lomentpwd, primaryemail, primaryemail,
						number, country_code, country_code, userid);

				JSONObject device_data = responseJson.getJSONObject("device_data");

				// String device_id = device_data.getString("device_id");
				String device_name = device_data.getString("device_name");

				lomentDataModel.setDeviceId(deviceidstring);
				lomentDataModel.setDeviceName(device_name);
				headerMapper.insert(lomentDataModel);

				JSONObject cashew_data = (JSONObject) responseJson.get("cashew_data");
				String _cashew = (String) cashew_data.get("username");

				DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
				AccountsModel acc = new AccountsModel();
				acc.setCashewnutId(_cashew);
				accountsMapper.insert(acc);
				return "0";
			} else if (status == 80101 || status == 80102 || status == 80103) {
				return "-2";
			} else if (status == 10102) {
				// 10102 - Duplicate email,
				return "-3";
			} else if (status == 10104) {
				// 10104 - Invalid data (invalid email address format).
				return "-4";
			} else if (status == 30103 || status == 30203) {
				return "-5";
			}
			return "-1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-1";
	}

	/**
	 * ******************************************************* register loment
	 * account
	 *
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @param countryCode
	 *            country code
	 * @param mobileNumber
	 *            mobile number
	 * @param email
	 *            email
	 * @return true: success, false: fail
	 */
	public int addAccount(String cashewnutId, String lomentId) {
		try {
			Vector deviceId = getDeviceID();
			int val = -1;
			JSONObject responseJson = null;
			for (int i = 0; i < deviceId.size(); i++) {
				String url = AppConfiguration.getSthithiApi() + "/user/" + lomentId + "/product/cashewnut/device/"
						+ deviceId.elementAt(i) + "/accounts/new/";
				String post = "cashewnut_username=" + cashewnutId;
				String response = DataConnection.getInstance().sendPost(url, post.getBytes());
				responseJson = new JSONObject(response);
				Integer status = (Integer) responseJson.get("status");
				if (status == 0) {
					DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
					AccountsModel acc = new AccountsModel();
					acc.setCashewnutId(cashewnutId);
					accountsMapper.insert(acc);
					return 0;
				} else if (status == 30103) {
					// Given username is already in use
					return 1;
				}
			}
			return -1;
		} catch (Exception e) {
		}
		return -2;
	}

	public boolean addDevice(String primaryEmail) {
		try {
			String platform = AccountHandler.getInstance().getPlatform();
			String OS = AccountHandler.getInstance().getOS();

			int devicetype = 9;
			final int DEVICE_TYPE_PC = 7;
			if (platform.equalsIgnoreCase("pc")) {
				devicetype = DEVICE_TYPE_PC;
			}

			Vector deviceIds = getDeviceID();
			String server_device_id;
			String device_name;
			String device_id;
			String url;

			if (deviceIds.size() > 1) {
				url = AppConfiguration.getSthithiApi() + "/user/" + primaryEmail + "/device/all";
				String responseText = DataConnection.getInstance().sendGetRequest(url, null);
				JSONObject responseJson = new JSONObject(responseText);
				Integer status = (Integer) responseJson.get("status");
				if (status == 0) {
					JSONObject device = getAddedDevice(responseJson, deviceIds);
					if (device != null) {
						device_id = device.getString("device_id");
						device_name = device.getString("name");
						DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
						headerMapper.updateLomentData(new LomentDataModel(primaryEmail, device_name, device_id));
						return true;
					}
				}
			} else {
				url = AppConfiguration.getSthithiApi() + "/user/" + primaryEmail + "/device/" + deviceIds.elementAt(0);
				String responseText = DataConnection.getInstance().sendGetRequest(url, null);
				JSONObject responseJson = new JSONObject(responseText);
				Integer status = (Integer) responseJson.get("status");
				if (status == 0) {
					// System.out.println(responseJson);
					JSONArray devices = (JSONArray) responseJson.get("devices");
					JSONObject device = (JSONObject) devices.getJSONObject(0);

					server_device_id = device.getString("device_id");
					device_id = deviceIds.elementAt(0) + "";
					device_name = device.getString("name");
					DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
					headerMapper.updateLomentData(new LomentDataModel(primaryEmail, device_name, device_id));
					return true;
				}
			}

			// ===========================================================================
			url = AppConfiguration.getSthithiApi() + "/user/" + primaryEmail + "/device/new";

			for (int i = 0; i < deviceIds.size(); i++) {
				String post = "device_identifier=" + deviceIds.elementAt(i) + "&device_type_id=" + devicetype
						+ "&operating_system=" + OS;

				String responseText = DataConnection.getInstance().sendPost(url, post.getBytes());
				JSONObject responseJson = new JSONObject(responseText);
				Integer status = (Integer) responseJson.get("status");
				// System.out.println(responseJson);

				if (status == 0) {
					server_device_id = responseJson.getString("device_id");
					device_id = deviceIds.elementAt(i) + "";
					device_name = responseJson.getString("device_name");
					DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
					headerMapper.updateLomentData(new LomentDataModel(primaryEmail, device_name, device_id));
					return true;
				}

				if (status == 20102) {
					url = AppConfiguration.getSthithiApi() + "/user/" + primaryEmail + "/device/"
							+ deviceIds.elementAt(i);
					responseText = DataConnection.getInstance().sendGetRequest(url, null);
					responseJson = new JSONObject(responseText);
					status = (Integer) responseJson.get("status");
					// System.out.println(responseJson);
					if (status == 0) {

						// {"devices":[{"creation_date":"2013-06-14
						// 11:32:04","device_id":"3ae0d8328e6c5cb3","status":"Active","operating_system":"L27.12.1-P1_QUANTA_20110422_quantaonly-1132-g","last_update_date":"2013-06-14
						// 11:32:04","device_type_id":"1","name":"Android
						// Phone-10"}]
						JSONArray devices = (JSONArray) responseJson.get("devices");
						JSONObject device = (JSONObject) devices.getJSONObject(0);
						server_device_id = "";
						device_id = device.getString("device_id");
						device_name = device.getString("name");
						DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
						headerMapper.updateLomentData(new LomentDataModel(primaryEmail, device_name, device_id));
						return true;
					}
				}
				return status == 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static JSONObject getAddedDevice(JSONObject responseJson, Vector deviceIds) {
		try {
			JSONArray jsonAccounts = (JSONArray) responseJson.get("devices");
			if (jsonAccounts.length() > 0) {
				for (int i = 0; i < jsonAccounts.length(); i++) {
					if (!jsonAccounts.isNull(i)) {
						JSONObject account = jsonAccounts.getJSONObject(i);
						String device_id = account.getString("device_id");
						for (int i1 = 0; i1 < deviceIds.size(); i1++) {
							if (device_id.equalsIgnoreCase(deviceIds.elementAt(i1) + "")) {
								return account;
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAccountsForValidUser(String primaryemail) {

		try {
			DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
			LomentDataModel model = headerMapper.getLomentData();
			if (getDeviceID().contains(model.getDeviceId())) {
				String url = AppConfiguration.getSthithiApi() + "/user/" + primaryemail + "/product/cashewnut/device/"
						+ model.getDeviceId() + "/accounts/all";

				String responseText = DataConnection.getInstance().sendGetRequest(url, null);
				// System.out.println(url);

				JSONObject responseJson = new JSONObject(responseText);
				// System.out.println(responseJson);
				Integer status = (Integer) responseJson.get("status");
				int val = status.intValue();
				if (val == 0) {
					return responseText;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Vector getDeviceID() {
		Vector deviceId = getDeviceIDPlatform(false);
		if (deviceId != null && deviceId.size() > 0) {
			return deviceId;
		} else {
			String id = null;
			deviceId = new Vector();
			try {
				Enumeration<NetworkInterface> ne = NetworkInterface.getNetworkInterfaces();
				while (ne.hasMoreElements()) {
					NetworkInterface networkInterface = (NetworkInterface) ne.nextElement();
					if (!networkInterface.isVirtual() && networkInterface.getHardwareAddress() != null) {
						if (networkInterface.getName().startsWith("eth")) {
							id = convert2Hex(networkInterface.getHardwareAddress(),
									networkInterface.getHardwareAddress().length);
							deviceId.addElement(id);
							return deviceId;
						}
						if (networkInterface.getName().startsWith("wlan") && id == null) {
							id = convert2Hex(networkInterface.getHardwareAddress(),
									networkInterface.getHardwareAddress().length);
						}
					}
				}
				if (id == null || id.trim().equals("")) {
					NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
					id = convert2Hex(networkInterface.getHardwareAddress(),
							networkInterface.getHardwareAddress().length);
				}
			} catch (Exception e) {
				e.printStackTrace();
				id = null;
			}
			deviceId.addElement(id);
			return deviceId;
		}
	}

	private static String convert2Hex(byte[] buf, int length) {

		String T = "";

		for (int x = 0; x < length; x++) {
			int y = buf[x];
			if (y < 0) {
				y += 256;
			}
			String d = Integer.toHexString(y);
			if (d.length() == 1) {
				T += "0";
			}
			T += d;
		}
		return T;
	}

	public static Vector getDeviceIDPlatform(boolean forSubscription) {
		Vector deviceId = new Vector();

		String Platform = "";
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			// System.out.println("This is Windows");
			Platform = "windows";
		} else if (OS.indexOf("mac") >= 0) {
			// System.out.println("This is Mac");
			Platform = "macintosh";
		} else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
			// System.out.println("This is Unix or Linux");
			Platform = "linux";
		} else if (OS.indexOf("sunos") >= 0) {
			// System.out.println("This is Solaris");
			Platform = "solaris";
			return null;
		} else {
			// System.out.println("Your OS is not support!!");
			return null;
		}

		Process p;
		Hashtable<String, String> val = new Hashtable<String, String>();
		int wiredC = 1000;
		int wirelessC = 1000;

		String wiredAdapter = "";
		String wirelessAdapter = "";

		String hwAddrStart = "";
		String ipConfigCmd = "";

		if (Platform.equals("windows")) {
			try {
				ipConfigCmd = "getmac /fo csv /nh";
				p = Runtime.getRuntime().exec(ipConfigCmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String s;
				while ((s = in.readLine()) != null) {
					String line = s.split(",")[0].replace('"', ' ');
					if (!line.trim().equalsIgnoreCase("Disabled")) {
						line = Helper.replace(line, "-", "");
						line = line.toLowerCase().trim();
						if (line.length() > 5 && !line.equals("n/a")) {
							// System.out.println(line);
							if (line != null && line.trim().length() > 6) {
								deviceId.addElement(line);
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return deviceId;
		}

		if (Platform == "linux") {
			wiredAdapter = "eth";
			wirelessAdapter = "wlan";
			hwAddrStart = "HWaddr ";
			ipConfigCmd = "ifconfig -a";
		}

		if (Platform == "macintosh") {
			wiredAdapter = "en";
			wirelessAdapter = "en";
			hwAddrStart = "ether ";
			ipConfigCmd = "ifconfig -a";
		}

		try {
			p = Runtime.getRuntime().exec(ipConfigCmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = in.readLine()) != null) {
				if (s.startsWith(wiredAdapter) == true || s.startsWith(wirelessAdapter)) {
					String AdpName = s.substring(0, s.indexOf(" "));
					AdpName = AdpName.replace(':', ' ');
					AdpName = AdpName.trim();
					while (s.indexOf(hwAddrStart) == -1) {
						s = in.readLine();
					}
					s = s.trim().substring(hwAddrStart.length() + s.indexOf(hwAddrStart) - 1);
					val.put(AdpName, s.trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		try {
			Enumeration keys = val.keys();
			while (keys.hasMoreElements()) {
				String adp = (String) keys.nextElement();
				if (adp.startsWith(wiredAdapter) == true) {
					if (Integer.parseInt((adp.substring(wiredAdapter.length()))) < wiredC) {
						wiredC = Integer.parseInt((adp.substring(wiredAdapter.length())));
					}
				}
				if (adp.startsWith(wirelessAdapter) == true) {
					if (Integer.parseInt((adp.substring(wirelessAdapter.length()))) < wirelessC) {
						wirelessC = Integer.parseInt((adp.substring(wirelessAdapter.length())));
					}
				}
			}
			if (wiredC != 1000) {
				String line = ((String) val.get(wiredAdapter + wiredC)).trim();
				if (line != null && line.trim().length() > 6) {
					line = Helper.replace(line, ":", "");
					deviceId.addElement(line);
				}
			}
			if (wirelessC != 1000) {
				String line = ((String) val.get(wirelessAdapter + wirelessC)).trim();
				if (line != null && line.trim().length() > 6) {
					line = Helper.replace(line, ":", "");
					deviceId.addElement(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return deviceId;
	}

	public String editName(String email, String userName) {
		String url = AppConfiguration.getSthithiApi() + "/user/" + email + "/account";
		String post = "name=" + userName;
		String response = "";
		String fullname = "";

		try {
			DataConnection conn = DataConnection.getInstance();
			response = conn.sendPost(url, post.getBytes());
			JSONObject responseJson = new JSONObject(response);
			if (responseJson != null && responseJson.has("status")) {
				Integer status = (Integer) responseJson.get("status");
				if (status == 0) {

					JSONObject userDetails = responseJson.getJSONObject("user_details");
					fullname = userDetails.get("name").toString().trim();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fullname;

	}

	public String editNumber(String email, String phoneNumber, String countryAbbr) {
		String url = AppConfiguration.getSthithiApi() + "/user/" + email + "/account";
		String post = "primary_mobile_number=" + phoneNumber + "&country_abbrev=" + countryAbbr;
		String response = "";
		String number = "";

		try {
			DataConnection conn = DataConnection.getInstance();
			response = conn.sendPost(url, post.getBytes());
			JSONObject responseJson = new JSONObject(response);
			System.out.println(responseJson);
			if (responseJson != null && responseJson.has("status")) {
				Integer status = (Integer) responseJson.get("status");
				if (status == 0) {

					JSONObject userDetails = responseJson.getJSONObject("user_details");
					number = userDetails.get("primary_mobile_number").toString().trim();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}

	public boolean hasActiveLicense() {
		return true;
	}

	public String getPlatform() {
		return "pc";
	}

	public String getOS() {
		return System.getProperty("os.name");
	}
}
