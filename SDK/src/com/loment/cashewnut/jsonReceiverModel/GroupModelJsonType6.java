package com.loment.cashewnut.jsonReceiverModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.connection.ReceipientConnection;
import com.loment.cashewnut.model.GroupModel;

public class GroupModelJsonType6 {
	// JSON
	private final static String GROUP_ID = "group_id";
	private final static String MEMBERS = "members";
	private final static String TYPE = "type";
	private final static String GROUP_NAME = "name";
	private final static String OWNER = "owner";
	private final static String CREATION_TIME = "creation_time";
	private final static String LAST_UPDATE_TIME = "last_update_time";

	private final static String CREATION_TIME_STAMP = "creation_timestamp";
	private final static String LAST_UPDATE_TIME_STAMP = "last_update_timestamp";

	private static String CLIENT_PARAMS = "client_params";
	private static String VERSION = "e_key_v";
	private static String TOKEN = "m_token_v";
	private static String FROM = "from";

	private static String FEATURES = "features";
	private static String MAIN_ADMIN = "owner";

	private static String GROUP_CREATED = "group_created";
	private static String MEMBER_ADDED = "group_member_added";
	private static String MEMBER_REMOVED = "group_member_removed";
	private static String MEMBER_LEFT = "group_member_left";
	private static String GROUPNAME_CHANGED = "group_name_changed";
	private static String CHANGED_AS_ADMIN = "group_admin_added";
	private static String CHANGED_AS_MEMBER = "group_admin_removed";
	private static String MAINADMIN_CHANGED = "group_main_admin_changed";
	private static String FEATURE_ADDED = "group_feature_added";
	private static String FEATURE_REMOVED = "group_feature_removed";

	public static GroupModel getGroupConfig(String response) {
		try {
			String serverGroupId = "";
			String groupName = "";
			int type = -1;
			String owner = "";
			JSONArray members = new JSONArray();
			String creation_time = "";
			String last_update_time = "";
			String from = "";
			JSONObject jsonGroupObj = new JSONObject(response);
			JSONObject jsonGroupConfig = jsonGroupObj.getJSONObject("GRP_CONF");
			GroupModel groupModel = new GroupModel();

			if (jsonGroupConfig.has(GROUP_ID)) {
				serverGroupId = jsonGroupConfig.getString(GROUP_ID);
				groupName = jsonGroupConfig.getString(GROUP_NAME);
				type = jsonGroupConfig.getInt(TYPE);
				owner = jsonGroupConfig.getString(OWNER);
				members = jsonGroupConfig.getJSONArray(MEMBERS);

				groupModel.setServerGroupId(serverGroupId);
				groupModel.setGroupName(groupName);
				groupModel.setOwner(owner);
				groupModel.setType(type);
				groupModel.setMembers(members);
				for (int i = 0; i < members.length(); i++) {
					ReceipientConnection.getInstance().get(members.get(i) + "");
				}
				try {
					if (jsonGroupConfig.has(CREATION_TIME)) {
						creation_time = jsonGroupConfig.getString(CREATION_TIME);
						groupModel.setCreationTime(getTimeStampCurrentTimeZone(creation_time));
					}

					if (jsonGroupConfig.has(LAST_UPDATE_TIME)) {
						last_update_time = jsonGroupConfig.getString(LAST_UPDATE_TIME);
						groupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(last_update_time));
					}

					if (jsonGroupConfig.has("admins") && !jsonGroupConfig.isNull("admins")) {
						JSONArray admins = jsonGroupConfig.getJSONArray("admins");
						groupModel.setAdmins(admins);
					}
					if (jsonGroupConfig.has("features") && !jsonGroupConfig.isNull("features")) {
						JSONArray features = jsonGroupConfig.getJSONArray("features");
						groupModel.setFeature(features);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return groupModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GroupModel getGroupDataType6(String response) {
		try {
			String serverGroupId = "";
			String groupName = "";
			int type = -1;
			String owner = "";
			JSONArray members = new JSONArray();
			String creation_time = "";
			String last_update_time = "";
			String from = "";
			Integer id = -1;
			JSONObject jsonGroupObject = new JSONObject(response);
			JSONObject header = new JSONObject();
			JSONObject body = new JSONObject();
			GroupModel groupModel = new GroupModel();

			if (jsonGroupObject.has("header")) {
				header = jsonGroupObject.getJSONObject("header");
				serverGroupId = header.getString(GROUP_ID);
				type = header.getInt(TYPE);
				from = header.getString(FROM);
				id = Integer.parseInt(header.getString("id"));
				groupModel.setMsgID(id);
				groupModel.setServerGroupId(serverGroupId);
				groupModel.setFrom(from);
				groupModel.setType(type);

				try {
					if (header.has(CREATION_TIME_STAMP)) {
						creation_time = header.getString(CREATION_TIME_STAMP);
						groupModel.setCreationTime(getTimeStampCurrentTimeZone(creation_time));
					}

					if (header.has(LAST_UPDATE_TIME_STAMP)) {
						last_update_time = header.getString(LAST_UPDATE_TIME_STAMP);
						groupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(last_update_time));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (jsonGroupObject.has("body")) {

				body = jsonGroupObject.getJSONObject("body");
				try {

					@SuppressWarnings("unchecked")
					Iterator<String> keySet = body.keys();
					while (keySet.hasNext()) {
						String key = keySet.next();

						JSONObject operation = (JSONObject) body.get(key);

						if (body.has(GROUP_CREATED)) {
							members = operation.getJSONArray(MEMBERS);
							groupModel.setMembers(members);
							groupModel.setGroupName(operation.getString(GROUP_NAME));
							JSONArray features = operation.getJSONArray(FEATURES);
							groupModel.setFeature(features);
							groupModel.setOperation(GroupModel.OPERATION_CREATE_GROUP);

							for (int i = 0; i < members.length(); i++) {
								ReceipientConnection.getInstance().get(members.get(i) + "");
							}
							groupModel.setOwner(from);
						} else if (body.has(MEMBER_ADDED)) {
							JSONArray change = operation.getJSONArray(MEMBERS);
							groupModel.setMembers(change);
							groupModel.setOperation(GroupModel.OPERATION_ADD_MEMBER);
						} else if (body.has(MEMBER_REMOVED)) {
							JSONArray change = operation.getJSONArray(MEMBERS);
							groupModel.setMembers(change);
							groupModel.setOperation(GroupModel.OPERATION_DELETE_MEMBER);
						} else if (body.has(GROUPNAME_CHANGED)) {
							groupModel.setGroupName(operation.getString(GROUP_NAME));
							groupModel.setOperation(GroupModel.OPERATION_CHANGE_NAME);
						} else if (body.has(CHANGED_AS_ADMIN) && !body.isNull(CHANGED_AS_ADMIN)) {
							JSONArray change = operation.getJSONArray(MEMBERS);
							if (!change.toString().equals("") && !change.toString().equalsIgnoreCase("null")) {
								groupModel.setAdmins(change);
								groupModel.setOperation(GroupModel.OPERATION_CHANGED_AS_ADMIN);
							}
						} else if (body.has(CHANGED_AS_MEMBER) && !body.isNull(CHANGED_AS_MEMBER)) {
							JSONArray change = operation.getJSONArray(MEMBERS);
							if (!change.toString().equals("") && !change.toString().equalsIgnoreCase("null")) {
								groupModel.setAdmins(change);
								groupModel.setOperation(GroupModel.OPERATION_CHANGED_AS_MEMBER);
							}
						} else if (body.has(MAINADMIN_CHANGED) && !body.isNull(MAINADMIN_CHANGED)) {
							groupModel.setOwner(operation.getString(MAIN_ADMIN));
							groupModel.setOperation(GroupModel.OPERATION_CHANGE_MAIN_ADMIN);

						} else if (body.has(FEATURE_ADDED) && !body.isNull(FEATURE_ADDED)) {
							JSONArray change = operation.getJSONArray(FEATURES);
							if (!change.toString().equals("") && !change.toString().equalsIgnoreCase("null")) {
								groupModel.setFeature(change);
								groupModel.setOperation(GroupModel.OPERATION_FEATURE_ADDED);
							}
						} else if (body.has(FEATURE_REMOVED) && !body.isNull(FEATURE_REMOVED)) {
							JSONArray change = operation.getJSONArray(FEATURES);
							if (!change.toString().equals("") && !change.toString().equalsIgnoreCase("null")) {
								groupModel.setFeature(change);
								groupModel.setOperation(GroupModel.OPERATION_FEATURE_REMOVED);
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return groupModel;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long getTimeStampCurrentTimeZone(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date date = sdf.parse(time);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date.getTime());
			calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));

			// System.out.println(calendar.getTime().getTime());
			return calendar.getTime().getTime();
		} catch (Exception e) {
		}
		return 0;
	}
}
