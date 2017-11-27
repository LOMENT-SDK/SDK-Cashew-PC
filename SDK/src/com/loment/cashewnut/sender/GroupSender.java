package com.loment.cashewnut.sender;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsType6Mapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.jsonSenderModel.SenderGroupModelJson;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.receiver.GroupReceiver;
import com.loment.cashewnut.receiver.GroupReceiverType6;
import com.loment.cashewnut.receiver.Receiver;
import com.loment.cashewnut.util.Helper;

public class GroupSender implements SendServerRespListener {
	private HashMap<String, GroupModel> tokenMapper = new HashMap<String, GroupModel>();

	DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
	AccountsModel accountsModel = accountsMapper.getAccount();
	String cashewnutId = accountsModel.getCashewnutId();

	public GroupSender() {
		try {
			Sender.getInstance().addSenderListener(this);
		} catch (Exception ex) {
		}
	}

	public void send(final GroupModel gModel) {
		String token = "group-" + Sender.getInstance().getToken();
		int operation = gModel.getOperation();
		String data = "";
		// type 4 is old group format
		int type = 4;
		if (AppConfiguration.GROUP_TYPE6) {
			type = 6;
		}

		try {
			switch (operation) {
			case GroupModel.OPERATION_CREATE_GROUP:
				data = new SenderGroupModelJson().getGroupCreationData(gModel, token, type);
				break;
			case GroupModel.OPERATION_GET_GROUP_DATA:
				data = new SenderGroupModelJson().getGroupDataByGroupId(gModel.getServerGroupId());
				break;
			case GroupModel.OPERATION_ADD_MEMBER:
				data = new SenderGroupModelJson().addMembersToGroup(gModel, token, type);
				break;
			case GroupModel.OPERATION_DELETE_MEMBER:
				data = new SenderGroupModelJson().removeMembersFromGroup(gModel, token, type);
				break;
			case GroupModel.OPERATION_CHANGE_MAIN_ADMIN:
				data = new SenderGroupModelJson().changeOwner(gModel, token, type);
				break;
			case GroupModel.OPERATION_CHANGE_NAME:
				data = new SenderGroupModelJson().changeGroupName(gModel, token, type);
				break;
			case GroupModel.OPERATION_CHANGED_AS_ADMIN:
				data = new SenderGroupModelJson().adminChanged(gModel, token, true, type);
				break;
			case GroupModel.OPERATION_CHANGED_AS_MEMBER:
				data = new SenderGroupModelJson().adminChanged(gModel, token, false, type);
				break;
			case GroupModel.OPERATION_FEATURE_ADDED:
				data = new SenderGroupModelJson().featureChanged(gModel, token, false);
				break;
			case GroupModel.OPERATION_FEATURE_REMOVED:
				data = new SenderGroupModelJson().featureChanged(gModel, token, true);
				break;

			case GroupModel.OPERATION_MEMBER_LEFT:
				data = new SenderGroupModelJson().membersLeftFromGroup(gModel, token, type);
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Sender.getInstance().sendAmqp(data, token, 2, null);
		tokenMapper.put(token, gModel);
	}

	public void listenForResponse(String token, String response) {
		if (tokenMapper.containsKey(token)) {
			if (response.equals("-1")) {
				return;
			}
			if (AppConfiguration.GROUP_TYPE6) {
				type6Response(token, response);
			} else {
				type4Response(token, response);
			}
			tokenMapper.remove(token);
		}
	}

	public void type4Response(String token, String response) {

		try {
			JSONObject jObject = new JSONObject(response);

			GroupModel gModel = tokenMapper.get(token);
			int operation = gModel.getOperation();
			
			int status = -1;
			if (jObject.has("status")) {
				status = jObject.getInt("status");
			}

			switch (operation) {
			case GroupModel.OPERATION_CREATE_GROUP:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");
					int id = gModel.getGroupId();

					GroupModel gModel1 = DBGroupsMapper.getInstance().getGroup(id + "", false);
					gModel1.setServerGroupId(serverGroupId);
					DBGroupsMapper.getInstance().updateGroup(gModel1, false);
					insertGroupMessage(gModel1, GroupModel.OPERATION_CREATE_GROUP);
				}
				break;
			case GroupModel.OPERATION_GET_GROUP_DATA:
				if (status == 0 && jObject.has("GRP_CONF")) {
					// System.out
					// .println("response:" + jObject.toString());
					Receiver.getInstance().fireReceiverListener(GroupReceiver.GROUP_CONFIG_TOKEN_AMQP_TYPE4,
							jObject.getJSONObject("GRP_CONF").toString());
				}
				break;
			case GroupModel.OPERATION_ADD_MEMBER:
				// System.out.println("response:" + jObject.toString());
				if (status == 0) {
					String id = gModel.getServerGroupId();

					GroupModel gModel1 = DBGroupsMapper.getInstance().getGroup(id + "", true);

					JSONArray sourceArray = gModel.getMembers();
					JSONArray destinationArray = gModel1.getMembers();

					for (int i = 0; i < sourceArray.length(); i++) {
						int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
						if (index < 0) {
							destinationArray.put(sourceArray.get(i));
						}
					}
					gModel1.setMembers(destinationArray);

					DBGroupsMapper.getInstance().updateGroup(gModel1, false);
					insertGroupMessage(gModel, GroupModel.OPERATION_ADD_MEMBER);
				}
				break;
			case GroupModel.OPERATION_DELETE_MEMBER:
				// System.out.println("response:" + jObject.toString());
				try {
					if (status == 0) {
						String id = gModel.getServerGroupId();

						GroupModel gModel1 = DBGroupsMapper.getInstance().getGroup(id + "", true);

						JSONArray sourceArray = gModel.getMembers();
						JSONArray destinationArray = gModel1.getMembers();

						for (int i = 0; i < sourceArray.length(); i++) {
							int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
							if (index > -1) {
								destinationArray.remove(index);
							}
						}
						gModel1.setMembers(destinationArray);

						DBGroupsMapper.getInstance().updateGroup(gModel1, false);
						insertGroupMessage(gModel, GroupModel.OPERATION_DELETE_MEMBER);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case GroupModel.OPERATION_CHANGED_AS_ADMIN:
				// System.out.println("response:" + jObject.toString());
				if (status == 0) {
					String id = gModel.getServerGroupId();

					GroupModel gModel1 = DBGroupsMapper.getInstance().getGroup(id + "", true);
					gModel1.setOwner(gModel.getOwner());

					DBGroupsMapper.getInstance().updateGroup(gModel1, false);

					insertGroupMessage(gModel, GroupModel.OPERATION_CHANGED_AS_ADMIN);
				}
				break;
			case GroupModel.OPERATION_CHANGE_NAME:
				// System.out.println("response:" + jObject.toString());
				if (status == 0) {
					String id = gModel.getServerGroupId();

					GroupModel gModel1 = DBGroupsMapper.getInstance().getGroup(id + "", true);
					gModel1.setGroupName(gModel.getGroupName());

					DBGroupsMapper.getInstance().updateGroup(gModel1, false);
					insertGroupMessage(gModel, GroupModel.OPERATION_CHANGE_NAME);
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void type6Response(String token, String response) {
		try {
			JSONObject jObject = new JSONObject(response);
			GroupModel chagedGroupModel = tokenMapper.get(token);
			int operation = chagedGroupModel.getOperation();
		
			int status = -1;
			if (jObject.has("status")) {
				status = jObject.getInt("status");
			}

			switch (operation) {
			case GroupModel.OPERATION_CREATE_GROUP:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {

					String serverGroupId = jObject.getString("group_id");
					int id = chagedGroupModel.getGroupId();

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(id + "", false);
					gModel1.setServerGroupId(serverGroupId);
					chagedGroupModel.setServerGroupId(serverGroupId);
					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, false);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						//insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getGroupName());
					} else {
						insertGroupMessage(chagedGroupModel, GroupModel.OPERATION_CREATE_GROUP);
					}
				}
				break;
			case GroupModel.OPERATION_GET_GROUP_DATA:
				if (status == 0 && jObject.has("GRP_CONF")) {
					// System.out.println("response:" + jObject.toString());

					// group config
					if (AppConfiguration.GROUP_TYPE6) {
						Receiver.getInstance().fireReceiverListener(GroupReceiverType6.GROUP_CONFIG_TOKEN_AMQP_TYPE6,
								jObject.toString());
					} else {
						Receiver.getInstance().fireReceiverListener(GroupReceiver.GROUP_CONFIG_TOKEN_AMQP_TYPE4,
								jObject.toString());
					}

					// Receiver.getInstance().fireReceiverListener(
					// GroupReceiver.GROUP_CONFIG_TOKEN_AMQP_TYPE4,
					// jObject.getString("GRP_CONF"));
				}
				break;
		case GroupModel.OPERATION_ADD_MEMBER:
				// System.out.println("response:" + jObject.toString());
				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");
					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);

					JSONArray sourceArray = chagedGroupModel.getMembers();
					JSONArray destinationArray = gModel1.getMembers();

					for (int i = 0; i < sourceArray.length(); i++) {
						int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
						if (index < 0) {
							destinationArray.put(sourceArray.get(i));
						}
					}
					gModel1.setMembers(destinationArray);

					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setFrom(cashewnutId);
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getMembers().toString());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_ADD_MEMBER);
					}
				}
				break;
			case GroupModel.OPERATION_DELETE_MEMBER:
				// System.out.println("response:" + jObject.toString());
				try {
					if (status == 0) {
						String serverGroupId = jObject.getString("group_id");

						GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);

						JSONArray sourceArray = chagedGroupModel.getMembers();
						JSONArray destinationArray = gModel1.getMembers();
						Vector<Integer> removedIndexes = new Vector<Integer>();

						for (int i = 0; i < sourceArray.length(); i++) {
							int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
							if (index > -1) {
								// destinationArray.remove(index);
								removedIndexes.add(index);
							}
						}

						JSONArray finalDestinationArray = Helper.removeJSONArrayIndex(destinationArray, removedIndexes);
						gModel1.setMembers(finalDestinationArray);

						DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

						if (AppConfiguration.GROUP_TYPE6) {
							String creationTimeStamp = jObject.getString("creation_timestamp");
							String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
							chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
							chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
							chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
							insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getMembers().toString());
						} else {
							insertGroupMessage(gModel1, GroupModel.OPERATION_DELETE_MEMBER);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case GroupModel.OPERATION_CHANGE_MAIN_ADMIN:
				// System.out.println("response:" + jObject.toString());
				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);
					gModel1.setOwner(chagedGroupModel.getOwner());

					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getOwner());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_CHANGE_MAIN_ADMIN);
					}
				}
				break;
			case GroupModel.OPERATION_CHANGE_NAME:
				// System.out.println("response:" + jObject.toString());
				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);
					gModel1.setGroupName(chagedGroupModel.getGroupName());

					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getGroupName());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_CHANGE_NAME);
					}
				}
				break;

			case GroupModel.OPERATION_CHANGED_AS_ADMIN:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);

					JSONArray sourceArray = chagedGroupModel.getAdmins();
					JSONArray destinationArray = gModel1.getAdmins();

					for (int i = 0; i < sourceArray.length(); i++) {
						int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
						if (index < 0) {
							destinationArray.put(sourceArray.get(i));
						}
					}
					gModel1.setAdmins(destinationArray);
					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getAdmins().toString());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_CHANGED_AS_ADMIN);
					}
				}
				break;
			case GroupModel.OPERATION_CHANGED_AS_MEMBER:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);

					JSONArray sourceArray = chagedGroupModel.getAdmins();
					JSONArray destinationArray = gModel1.getAdmins();
					Vector<Integer> removedIndexes = new Vector<Integer>();

					for (int i = 0; i < sourceArray.length(); i++) {
						int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
						if (index > -1) {
							// destinationArray.remove(index);
							removedIndexes.add(index);
						}
					}

					JSONArray finalDestinationArray = Helper.removeJSONArrayIndex(destinationArray, removedIndexes);
					gModel1.setAdmins(finalDestinationArray);
					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);
					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getAdmins().toString());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_CHANGED_AS_MEMBER);
					}
				}
				break;
			case GroupModel.OPERATION_FEATURE_ADDED:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);
					gModel1.setFeature(chagedGroupModel.getFeature());

					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getFeature().toString());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_FEATURE_ADDED);
					}

				}
				break;
			case GroupModel.OPERATION_FEATURE_REMOVED:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);
					gModel1.setFeature(chagedGroupModel.getFeature());
					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getFeature().toString());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_FEATURE_REMOVED);
					}
				}
				break;
			case GroupModel.OPERATION_MEMBER_LEFT:
				// System.out.println("response:" + jObject.toString());

				if (status == 0) {
					String serverGroupId = jObject.getString("group_id");

					GroupModel gModel1 = DBGroupsType6Mapper.getInstance().getGroup(serverGroupId, true);
					JSONArray sourceArray = chagedGroupModel.getMembers();
					JSONArray destinationArray = gModel1.getMembers();
					Vector<Integer> removedIndexes = new Vector<Integer>();

					for (int i = 0; i < sourceArray.length(); i++) {
						int index = getStringIndexOfJSONArray(destinationArray, (String) sourceArray.get(i));
						if (index > -1) {
							// destinationArray.remove(index);
							removedIndexes.add(index);
						}
					}

					JSONArray finalDestinationArray = Helper.removeJSONArrayIndex(destinationArray, removedIndexes);
					gModel1.setMembers(finalDestinationArray);

					DBGroupsType6Mapper.getInstance().updateGroup(gModel1, true);

					if (AppConfiguration.GROUP_TYPE6) {
						String creationTimeStamp = jObject.getString("creation_timestamp");
						String lastUpdateTimeStamp = jObject.getString("last_update_timestamp");
						chagedGroupModel.setCreationTime(getTimeStampCurrentTimeZone(creationTimeStamp));
						chagedGroupModel.setLastUpdateTime(getTimeStampCurrentTimeZone(lastUpdateTimeStamp));
						chagedGroupModel.setMsgID(Integer.parseInt(jObject.getString("msg_id")));
						insertNewGroupMessage(chagedGroupModel, chagedGroupModel.getMembers().toString());
					} else {
						insertGroupMessage(gModel1, GroupModel.OPERATION_MEMBER_LEFT);
					}
				}
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertNewGroupMessage(GroupModel gModel, String change) {
		try {
			DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
			HeaderModel headerModel = new HeaderModel();
			headerModel.setGroupId(gModel.getServerGroupId());
			headerModel.setThreadId(gModel.getServerGroupId());
			headerModel.setMessageFrom(gModel.getFrom());
			headerModel.setMessageType(MessageModel.MESSAGE_TYPE_NEW_GROUPSMESSAGE);
			headerModel.setCreationTime(gModel.getCreationTime());
			headerModel.setLastUpdateTime(gModel.getLastUpdateTime());
			headerModel.setPriority(gModel.getOperation());
			headerModel.setSubject(change);
			headerModel.setCreationTime(gModel.getCreationTime());
			headerModel.setLastUpdateTime(gModel.getLastUpdateTime());
			headerModel.setServerMessageId(gModel.getMsgID());
			headerModel.setMessageFrom(gModel.getFrom());

			HeaderModel localHeaderModel = headerMapper.getMessageHeaderById(headerModel.getServerMessageId(), false);
			if (localHeaderModel == null || localHeaderModel.getServerMessageId() < 0) {
				headerMapper.insert(headerModel);
			} else {
				headerMapper.update(headerModel);
			}
			addToList(headerModel, MessageModel.ACTION_ADDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void insertGroupMessage(GroupModel gModel, int cmd) {
		try {
			DBHeaderMapper headerMapper = DBHeaderMapper.getInstance();
			HeaderModel headerModel = new HeaderModel();
			headerModel.setGroupId(gModel.getServerGroupId());
			headerModel.setThreadId(gModel.getServerGroupId());
			headerModel.setMessageFrom(cashewnutId);
			headerModel.setMessageType(MessageModel.LOCAL_MESSAGE_TYPE_GROUP);
			headerModel.setCreationTime(System.currentTimeMillis());
			if (cmd == GroupModel.OPERATION_CREATE_GROUP) {
				headerModel.setSubject(gModel.getGroupName());
				headerModel.setPriority(cmd);
			} else if (cmd == GroupModel.OPERATION_ADD_MEMBER) {
				headerModel.setSubject(gModel.getMembers().toString());
				headerModel.setPriority(cmd);
			} else if (cmd == GroupModel.OPERATION_DELETE_MEMBER) {
				headerModel.setSubject(gModel.getMembers().toString());
				headerModel.setPriority(cmd);
			} else if (cmd == GroupModel.OPERATION_CHANGE_NAME) {
				headerModel.setSubject(gModel.getGroupName());
				headerModel.setPriority(cmd);
			} else if (cmd == GroupModel.OPERATION_CHANGED_AS_ADMIN) {
				headerModel.setSubject(gModel.getOwner());
				headerModel.setPriority(cmd);
			}
			headerMapper.insert(headerModel);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addToList(headerModel, MessageModel.ACTION_ADDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addToList(HeaderModel headerModel, int action) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				// fire to list
				((CashewnutActivity) CashewnutActivity.currentActivity).processMessage(headerModel, action);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
