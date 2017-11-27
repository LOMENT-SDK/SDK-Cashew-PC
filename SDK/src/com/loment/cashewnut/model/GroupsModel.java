package com.loment.cashewnut.model;

public class GroupsModel {
private int groupID;
private String serverGroupID;
private String groupName;
private String createdBy;
private String groupMembers;

public int getGroupID() {
	return groupID;
}
public void setGroupID(int groupID) {
	this.groupID = groupID;
}
public String getServerGroupID() {
	return serverGroupID;
}
public void setServerGroupID(String serGroupID) {
	this.serverGroupID = serGroupID;
}
public String getGroupName() {
	return groupName;
}
public void setGroupName(String groupName) {
	this.groupName = groupName;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public String getGroupMembers() {
	return groupMembers;
}
public void setGroupMembers(String groupMembers) {
	this.groupMembers = groupMembers;
}
}
