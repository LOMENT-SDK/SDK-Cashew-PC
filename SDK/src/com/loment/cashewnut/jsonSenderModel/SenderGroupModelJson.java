package com.loment.cashewnut.jsonSenderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.model.GroupModel;

public class SenderGroupModelJson {

	// JSON

	private final String GROUP_ID = "group_id";
	private final String MEMBERS = "members";
	private final String TYPE = "type";
	private final String GROUP_NAME = "name";
	private final String OWNER = "owner_cashew_id";
	private final String ADD_MEMBERS = "members_to_add";
	private final String REMOVE_MEMBERS = "members_to_remove";
	private final String NEW_OWNER = "new_owner";

	private static String CLIENT_PARAMS = "client_params";
	private static String VERSION = "e_key_v";
	private static String TOKEN = "m_token_v";
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

	JSONObject clientParams = new JSONObject();
	JSONObject header = new JSONObject();
	JSONObject body = new JSONObject();
	JSONObject members = new JSONObject();
	JSONObject groupMain = new JSONObject();
	JSONObject finalObject = new JSONObject();

	public String getGroupCreationData(GroupModel gModel, String token, int type) {

		try {
			if (type == 6) {
				JSONObject groupCreate = new JSONObject();
				JSONArray changeFeature = new JSONArray();
				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(TYPE, "6");
				groupCreate.put(MEMBERS, gModel.getMembers());
				groupCreate.put(GROUP_NAME, gModel.getGroupName());
				changeFeature.put("2");
				changeFeature.put("4");
				groupCreate.put(FEATURES, changeFeature);
				body.put(GROUP_CREATED, groupCreate);
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			} else if (type == 4) {
				groupMain.put(MEMBERS, gModel.getMembers());
				groupMain.put(TYPE, "2");
				groupMain.put(GROUP_NAME, gModel.getGroupName());

				finalObject.put("data", groupMain);
				finalObject.put("cmd", 8);
			}
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String addMembersToGroup(GroupModel gModel, String token, int type) {
		try {
			if (type == 6) {
				JSONObject addMembers = new JSONObject();
				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(GROUP_ID, gModel.getServerGroupId());
				header.put(TYPE, "6");
				addMembers.put(MEMBERS, gModel.getMembers());
				body.put(MEMBER_ADDED, addMembers);
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			} else if (type == 4) {
				groupMain.put(GROUP_ID, gModel.getServerGroupId());
				groupMain.put(ADD_MEMBERS, gModel.getMembers());
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 9);
				System.out.println("" + finalObject.toString());
			}
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String removeMembersFromGroup(GroupModel gModel, String token, int type) {
		try {
			if (type == 6) {
				JSONObject removeMembers = new JSONObject();
				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(GROUP_ID, gModel.getServerGroupId());
				header.put(TYPE, "6");
				removeMembers.put(MEMBERS, gModel.getMembers());
				body.put(MEMBER_REMOVED, removeMembers);
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			} else if (type == 4) {
				groupMain.put(GROUP_ID, gModel.getServerGroupId());
				groupMain.put(REMOVE_MEMBERS, gModel.getMembers());
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 10);
			}
			// System.out.println("" + finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String changeGroupName(GroupModel gModel, String token, int type) {
		try {
			if (type == 6) {
				JSONObject groupNameChanged = new JSONObject();

				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(GROUP_ID, gModel.getServerGroupId());
				header.put(TYPE, "6");
				groupNameChanged.put(GROUP_NAME, gModel.getGroupName());
				body.put(GROUPNAME_CHANGED, groupNameChanged);
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			} else if (type == 4) {
				groupMain.put(GROUP_ID, gModel.getServerGroupId());
				groupMain.put(GROUP_NAME, gModel.getGroupName());
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 11);
			}
			// System.out.println("" + finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String changeOwner(GroupModel gModel, String token, int type) {
		try {
			if (type == 6) {
				JSONObject changemainAdmin = new JSONObject();
				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(GROUP_ID, gModel.getServerGroupId());
				header.put(TYPE, "6");
				changemainAdmin.put(MAIN_ADMIN, gModel.getOwner());
				body.put(MAINADMIN_CHANGED, changemainAdmin);
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			} else if (type == 4) {
				groupMain.put(GROUP_ID, gModel.getServerGroupId());
				groupMain.put(NEW_OWNER, gModel.getOwner());
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 12);
			}
			// System.out.println("" + finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String featureChanged(GroupModel gModel, String token, boolean isFeatureRemoved) {
		try {

			JSONObject featureChanged = new JSONObject();
			JSONArray changeFeature = new JSONArray();

			clientParams.put(TOKEN, token);
			header.put(CLIENT_PARAMS, clientParams);
			header.put(GROUP_ID, gModel.getServerGroupId());
			header.put(TYPE, "6");
			if (isFeatureRemoved) {
				changeFeature.put("2");
				featureChanged.put(FEATURES, changeFeature);
				body.put(FEATURE_REMOVED, featureChanged);
			} else {
				changeFeature.put("2");
				changeFeature.put("4");
				featureChanged.put(FEATURES, changeFeature);
				body.put(FEATURE_ADDED, featureChanged);
			}
			groupMain.put("header", header);
			groupMain.put("body", body);
			finalObject.put("data", groupMain);
			finalObject.put("cmd", 2);
			// System.out.println(""+finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String adminChanged(GroupModel gModel, String token, boolean isChangeAsAdmin, int type) {
		try {
			if (type == 6) {
				JSONObject adminChanged = new JSONObject();
				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(GROUP_ID, gModel.getServerGroupId());
				header.put(TYPE, "6");
				adminChanged.put(MEMBERS, gModel.getAdmins());
				if (isChangeAsAdmin) {
					body.put(CHANGED_AS_ADMIN, adminChanged);
				} else {
					body.put(CHANGED_AS_MEMBER, adminChanged);
				}
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			}
			// System.out.println("" + finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String membersLeftFromGroup(GroupModel gModel, String token, int type) {
		try {
			if (type == 6) {
				JSONObject memberLeft = new JSONObject();
				clientParams.put(TOKEN, token);
				header.put(CLIENT_PARAMS, clientParams);
				header.put(GROUP_ID, gModel.getServerGroupId());
				header.put(TYPE, "6");
				memberLeft.put(MEMBERS, gModel.getMembers());
				body.put(MEMBER_LEFT, memberLeft);// MEMBER_LEFT
				groupMain.put("header", header);
				groupMain.put("body", body);
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 2);
			} else if (type == 4) {
				groupMain.put(GROUP_ID, gModel.getServerGroupId());
				groupMain.put(REMOVE_MEMBERS, gModel.getMembers());
				finalObject.put("data", groupMain);
				finalObject.put("cmd", 10);
			}
			// System.out.println("" + finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getGroupDataByGroupId(String gId) {
		JSONObject groupMain = new JSONObject();
		JSONObject finalObject = new JSONObject();

		try {
			groupMain.put(GROUP_ID, gId);
			finalObject.put("data", groupMain);
			finalObject.put("cmd", 13);
			System.out.println("" + finalObject.toString());
			return finalObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}
