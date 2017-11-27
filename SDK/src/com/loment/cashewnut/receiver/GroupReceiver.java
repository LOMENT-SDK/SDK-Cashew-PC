package com.loment.cashewnut.receiver;

import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.jsonReceiverModel.GroupModelJson;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;

public class GroupReceiver implements ReceiveServerRespListener {
	public static String GROUP_CONFIG_TOKEN_AMQP_TYPE4 = "group_config_type4";
	String cashewnutId = "";

	public GroupReceiver() {
		try {
			Receiver.getInstance().addReceiverListener(this);
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel accountsModel = accountsMapper.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void listenForResponse(String token, String response) {
		if (token.equals(GROUP_CONFIG_TOKEN_AMQP_TYPE4)) {
			try {
				GroupModel groupModel = GroupModelJson.getGroupData(response);
				if (groupModel != null) {
					
					try {
						if (cashewnutId.equals(groupModel.getOwner())) {
							Thread.sleep(6000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					GroupModel gModel = DBGroupsMapper.getInstance().getGroup(
							groupModel.getServerGroupId(), true);
					if (gModel == null || gModel.getGroupId() < 0) {
						DBGroupsMapper.getInstance().insert(groupModel);
						insertGroupMessage(groupModel, null);
					} else {
						// update group
						DBGroupsMapper.getInstance().updateGroup(groupModel,
								true);
						insertGroupMessage(groupModel, gModel);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void insertGroupMessage(GroupModel groupModelServer,
			GroupModel groupModelLocal) {
		try {
                    
			int type = -1;
			if (groupModelLocal == null || groupModelLocal.getGroupId() == -1
					|| groupModelLocal.getGroupName() == null
					|| groupModelLocal.getGroupName().equals("")) {
				type = GroupModel.OPERATION_CREATE_GROUP;
			} else {
				JSONArray sourceArray = groupModelServer.getMembers();
				JSONArray destinationArray = groupModelLocal.getMembers();
				if (!groupModelServer.getOwner().equals(
						groupModelLocal.getOwner())) {
					type = GroupModel.OPERATION_CHANGED_AS_ADMIN;
				} else if (!groupModelServer.getGroupName().equals(
						groupModelLocal.getGroupName())) {
					type = GroupModel.OPERATION_CHANGE_NAME;
				} else if (sourceArray.length() < destinationArray.length()) {
					type = GroupModel.OPERATION_DELETE_MEMBER;
				} else if (sourceArray.length() > destinationArray.length()) {
					type = GroupModel.OPERATION_ADD_MEMBER;
				}
			}

			if (type > -1) {
				DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
				HeaderModel headerModel = new HeaderModel();
				headerModel.setGroupId(groupModelServer.getServerGroupId());
				headerModel.setThreadId(groupModelServer.getServerGroupId());
				headerModel.setMessageFrom(groupModelServer.getOwner());
				headerModel
						.setMessageType(MessageModel.LOCAL_MESSAGE_TYPE_GROUP);

				if (groupModelServer.getLastUpdateTime() > -1) {
					headerModel.setCreationTime(groupModelServer
							.getLastUpdateTime());
					headerModel.setLastUpdateTime(groupModelServer
							.getLastUpdateTime());
				} else {
					headerModel.setCreationTime(System.currentTimeMillis());
					headerModel.setLastUpdateTime(System.currentTimeMillis());
				}

				if (type == GroupModel.OPERATION_CREATE_GROUP) {
					if (groupModelServer.getCreationTime() > -1) {
						headerModel.setCreationTime(groupModelServer
								.getCreationTime());
					}
					headerModel.setSubject(groupModelServer.getGroupName());
					headerModel.setPriority(type);
				} else if (type == GroupModel.OPERATION_ADD_MEMBER) {
					JSONArray members = new JSONArray();
					JSONArray sourceArray = groupModelServer.getMembers();
					JSONArray destinationArray = groupModelLocal.getMembers();

					for (int i = 0; i < sourceArray.length(); i++) {
						int index = getStringIndexOfJSONArray(destinationArray,
								(String) sourceArray.get(i));
						if (index < 0) {
							members.put(sourceArray.get(i));
						}
					}

					headerModel.setSubject(members.toString());
					headerModel.setPriority(type);
				} else if (type == GroupModel.OPERATION_DELETE_MEMBER) {
					JSONArray members = new JSONArray();
					JSONArray sourceArray = groupModelServer.getMembers();
					JSONArray destinationArray = groupModelLocal.getMembers();

					for (int i = 0; i < destinationArray.length(); i++) {
						int index = getStringIndexOfJSONArray(sourceArray,
								(String) destinationArray.get(i));
						if (index == -1) {
							members.put(destinationArray.get(i));
						}
					}

					headerModel.setSubject(members.toString());
					headerModel.setPriority(type);
				} else if (type == GroupModel.OPERATION_CHANGE_NAME) {
					headerModel.setSubject(groupModelServer.getGroupName());
					headerModel.setPriority(type);
				} else if (type == GroupModel.OPERATION_CHANGED_AS_ADMIN) {
					headerModel.setMessageFrom(groupModelLocal.getOwner());
					headerModel.setSubject(groupModelServer.getOwner());
					headerModel.setPriority(type);
				}
				headerMapper.insert(headerModel);
				addToList(headerModel, MessageModel.ACTION_ADDED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addToList(HeaderModel headerModel, int action) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				// fire to list
				((CashewnutActivity) CashewnutActivity.currentActivity)
						.processMessage(headerModel, action);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	int getStringIndexOfJSONArray(JSONArray jsonArray, String element) {
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				if (element.equals(jsonArray.get(i))) {
					return i;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

}
