package com.loment.cashewnut.model;

import org.json.JSONArray;

public class GroupModel {

	public static final int OPERATION_CREATE_GROUP = 0;
	public static final int OPERATION_GET_GROUP_DATA = 1;
	public static final int OPERATION_ADD_MEMBER = 2;
	public static final int OPERATION_DELETE_MEMBER = 3;
	public static final int OPERATION_CHANGE_MAIN_ADMIN = 4;
	public static final int OPERATION_CHANGE_NAME = 5;

	public static final int OPERATION_CHANGED_AS_ADMIN = 6;
	public static final int OPERATION_CHANGED_AS_MEMBER = 7;
	public static final int OPERATION_FEATURE_ADDED = 8;
	public static final int OPERATION_FEATURE_REMOVED = 9;
	public static final int OPERATION_MEMBER_LEFT = 10;

	public static final int SUPERADMIN = 1;
	public static final int ADMIN = 2;
	public static final int MEMBER = 3;
	private int groupId = -1;
	private JSONArray members;
	private int type = 2;
	private String groupName = "";
	private String serverGroupId = "";
	private String owner = "";
	private int operation;
	private long creationTime = -1;
	private long lastUpdateTime = -1;
	private JSONArray feature;
	private String from = "";
	private Integer msgID = -1;

	private JSONArray admins;
	private int version = -1;

	public JSONArray getAdmins() {
		return admins;
	}

	public void setAdmins(JSONArray admins) {
		this.admins = admins;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getMsgID() {
		return msgID;
	}

	public void setMsgID(Integer msgID) {
		this.msgID = msgID;
	}

	public GroupModel(String gName, JSONArray membersList) {
			this.groupName = gName;
			this.members = membersList;

		}

	public GroupModel() {
			// TODO Auto-generated constructor stub
		}

	public JSONArray getFeature() {
		return feature;
	}

	public void setFeature(JSONArray feature) {
		this.feature = feature;
	}

	public String getServerGroupId() {
		return serverGroupId;
	}

	public void setServerGroupId(String serverGroupId) {
		this.serverGroupId = serverGroupId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public JSONArray getMembers() {
		return members;
	}

	public void setMembers(JSONArray members) {
		this.members = members;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
