package com.loment.cashewnut.jsonReceiverModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loment.cashewnut.model.GroupModel;

public class GroupModelJson {
	// JSON
	private final static String GROUP_ID = "group_id";
	private final static String MEMBERS = "members";
	private final static String TYPE = "type";
	private final static String GROUP_NAME = "name";
	private final static String OWNER = "owner";
	private final static String CREATION_TIME = "creation_time";
	private final static String LAST_UPDATE_TIME = "last_update_time";

	public static GroupModel getGroupData(String response) {

		try {
			JSONObject jsonGroupObject = new JSONObject(response);

			if (jsonGroupObject.has("header")) {
				jsonGroupObject = jsonGroupObject.getJSONObject("header");
			}

			if (jsonGroupObject.has(GROUP_ID)) {
				String serverGroupId = jsonGroupObject.getString(GROUP_ID);
				String groupName = jsonGroupObject.getString(GROUP_NAME);
				int type = jsonGroupObject.getInt(TYPE);
				String owner = jsonGroupObject.getString(OWNER);
				JSONArray members = jsonGroupObject.getJSONArray(MEMBERS);
				String creation_time = "";
				String last_update_time = "";

				GroupModel groupModel = new GroupModel();
				groupModel.setServerGroupId(serverGroupId);
				groupModel.setGroupName(groupName);
				groupModel.setOwner(owner);
				groupModel.setType(type);
				groupModel.setMembers(members);

				try {
					if (jsonGroupObject.has(CREATION_TIME)) {
						creation_time = jsonGroupObject
								.getString(CREATION_TIME);
						groupModel
								.setCreationTime(getTimeStampCurrentTimeZone(creation_time));
					}

					if (jsonGroupObject.has(LAST_UPDATE_TIME)) {
						last_update_time = jsonGroupObject
								.getString(LAST_UPDATE_TIME);
						groupModel
								.setLastUpdateTime(getTimeStampCurrentTimeZone(last_update_time));
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

	public static long getTimeStampCurrentTimeZone(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date date = sdf.parse(time);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date.getTime());
			calendar.add(Calendar.MILLISECOND,
					TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));

			// System.out.println(calendar.getTime().getTime());
			return calendar.getTime().getTime();
		} catch (Exception e) {
		}
		return 0;
	}
}
