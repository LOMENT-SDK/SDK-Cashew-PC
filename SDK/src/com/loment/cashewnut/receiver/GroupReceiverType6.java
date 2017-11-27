package com.loment.cashewnut.receiver;

import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsType6Mapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.jsonReceiverModel.GroupModelJson;
import com.loment.cashewnut.jsonReceiverModel.GroupModelJsonType6;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.sender.SenderHandler;

public class GroupReceiverType6 implements ReceiveServerRespListener {

	public static String GROUP_CONFIG_TOKEN_AMQP_TYPE6 = "group_config_type6";
	public static String RECEIVED_GROUP_TOKEN_AMQP_TYPE6 = "cashew_group_message_type6";
	String cashewnutId = "";

	public GroupReceiverType6() {
		try {
			Receiver.getInstance().addReceiverListener(this);
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel accountsModel = accountsMapper.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void listenForResponse(String token, String response) {
		GroupModel groupModel = null;
		try {
			if (token.equals(RECEIVED_GROUP_TOKEN_AMQP_TYPE6)) {
				groupModel = GroupModelJsonType6.getGroupDataType6(response);
				try {
					if (cashewnutId.equals(groupModel.getOwner())) {
						Thread.sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				insertGroup(groupModel, token);
			} else if (token.equals(GROUP_CONFIG_TOKEN_AMQP_TYPE6)) {
				groupModel = GroupModelJsonType6.getGroupConfig(response);
				try {
					if (cashewnutId.equals(groupModel.getOwner())) {
						Thread.sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				GroupModel groupModelLocal = DBGroupsMapper.getInstance()
						.getGroup(groupModel.getServerGroupId(), true);

				if (groupModelLocal == null || groupModelLocal.getGroupId() < 0) {
					DBGroupsType6Mapper.getInstance().insert(groupModel);
				} else {
					DBGroupsType6Mapper.getInstance().updateGroup(groupModel,
							true);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	protected void insertGroup(GroupModel groupModel, String token)
			throws JSONException {
		GroupModel groupModelLocal = DBGroupsType6Mapper.getInstance()
				.getGroup(groupModel.getServerGroupId(), true);
		if (groupModelLocal == null || groupModelLocal.getGroupId() < 0) {
			DBGroupsType6Mapper.getInstance().insert(groupModel);
			insertNewGroupMessageType6(groupModel);
		} else {
			if (groupModel.getOperation() == GroupModel.OPERATION_ADD_MEMBER) {
				getGroupConfigFromServer(groupModel);
				JSONArray sourceArray = groupModel.getMembers();
				JSONArray destinationArray = groupModelLocal.getMembers();

				for (int i = 0; i < sourceArray.length(); i++) {
					destinationArray.put(sourceArray.get(i));
				}
				groupModelLocal.setMembers(destinationArray);
			} else if (groupModel.getOperation() == GroupModel.OPERATION_DELETE_MEMBER) {
				JSONArray members = new JSONArray();
				JSONArray sourceArray = groupModel.getMembers();
				JSONArray destinationArray = groupModelLocal.getMembers();
				for (int i = 0; i < destinationArray.length(); i++) {
					int index = getStringIndexOfJSONArray(sourceArray,
							(String) destinationArray.get(i));
					if (index == -1) {
						members.put(destinationArray.get(i));
					}
				}
				groupModelLocal.setMembers(members);
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGED_AS_ADMIN) {
				JSONArray sourceArray = groupModel.getAdmins();
				JSONArray destinationArray = groupModelLocal.getAdmins();

				for (int i = 0; i < sourceArray.length(); i++) {
					destinationArray.put(sourceArray.get(i));
				}
				groupModelLocal.setAdmins(destinationArray);
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGED_AS_MEMBER) {
				JSONArray members = new JSONArray();
				JSONArray sourceArray = groupModel.getAdmins();
				JSONArray destinationArray = groupModelLocal.getAdmins();
				if (destinationArray != null) {
					for (int i = 0; i < destinationArray.length(); i++) {
						int index = getStringIndexOfJSONArray(sourceArray,
								destinationArray.get(i) + "");
						if (index == -1) {
							members.put(destinationArray.get(i));
						}
					}
				}
				groupModelLocal.setAdmins(members);
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGE_NAME) {

				groupModelLocal.setGroupName(groupModel.getGroupName());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGE_MAIN_ADMIN) {

				groupModelLocal.setOwner(groupModel.getOwner());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_FEATURE_ADDED) {

				groupModelLocal.setFeature(groupModel.getFeature());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_FEATURE_REMOVED) {
				groupModelLocal.setFeature(groupModel.getFeature());
			}
			if (groupModelLocal.getLastUpdateTime() < groupModel
					.getLastUpdateTime()) {
				groupModelLocal.setLastUpdateTime(groupModel
						.getLastUpdateTime());
				DBGroupsType6Mapper.getInstance().updateGroup(groupModelLocal,
						true);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				insertNewGroupMessageType6(groupModel);
			}
		}
	}

	protected void getGroupConfigFromServer(GroupModel groupModel) {
		GroupModel gModel = new GroupModel();
		gModel.setOperation(GroupModel.OPERATION_GET_GROUP_DATA);
		gModel.setServerGroupId(groupModel.getServerGroupId());
		gModel.setFrom(cashewnutId);
		SenderHandler.getInstance().sendGroup(gModel);
	}

	private void insertNewGroupMessageType6(GroupModel groupModel) {
		try {
			DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
			HeaderModel headerModel = new HeaderModel();
			headerModel.setGroupId(groupModel.getServerGroupId());
			headerModel.setThreadId(groupModel.getServerGroupId());
			headerModel.setMessageFrom(groupModel.getFrom());
			headerModel
					.setMessageType(MessageModel.MESSAGE_TYPE_NEW_GROUPSMESSAGE);
			headerModel.setCreationTime(groupModel.getCreationTime());
			headerModel.setLastUpdateTime(groupModel.getLastUpdateTime());
			headerModel.setPriority(groupModel.getOperation());
			headerModel.setLastUpdateTime(groupModel.getLastUpdateTime());
			headerModel.setServerMessageId(groupModel.getMsgID());
			// headerModel.setMessageFrom(groupModel.getFrom());
			if (groupModel.getOperation() == GroupModel.OPERATION_CREATE_GROUP) {
				headerModel.setSubject(groupModel.getGroupName());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_ADD_MEMBER) {
				headerModel.setSubject(groupModel.getMembers().toString());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_DELETE_MEMBER) {
				headerModel.setSubject(groupModel.getMembers().toString());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGE_NAME) {
				headerModel.setSubject(groupModel.getGroupName());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGE_MAIN_ADMIN) {
				headerModel.setSubject(groupModel.getOwner());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGED_AS_ADMIN) {
				headerModel.setSubject(groupModel.getAdmins().toString());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_CHANGED_AS_MEMBER) {
				headerModel.setSubject(groupModel.getAdmins().toString());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_FEATURE_ADDED) {
				headerModel.setSubject(groupModel.getFeature().toString());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_FEATURE_REMOVED) {
				headerModel.setSubject(groupModel.getFeature().toString());
			} else if (groupModel.getOperation() == GroupModel.OPERATION_MEMBER_LEFT) {
				headerModel.setSubject(groupModel.getMembers().toString());
			}
			HeaderModel localHeaderModel = headerMapper.getMessageHeaderById(
					headerModel.getServerMessageId(), false);
			if (localHeaderModel == null
					|| localHeaderModel.getServerMessageId() < 0) {
				headerMapper.insert(headerModel);
				addToList(headerModel, MessageModel.ACTION_ADDED);
			} else {
				headerMapper.update(headerModel);
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
