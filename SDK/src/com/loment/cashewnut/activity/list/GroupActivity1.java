//package com.loment.cashewnut.activity.list;
//
//import java.util.AbstractMap;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.ContextMenu;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.scene.control.MenuItem;
//import javafx.scene.control.SplitPane;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//import javafx.util.Callback;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import com.loment.cashewnut.AppConfiguration;
//import com.loment.cashewnut.CashewnutApplication;
//import com.loment.cashewnut.database.mappers.DBAccountsMapper;
//import com.loment.cashewnut.database.mappers.DBContactsMapper;
//import com.loment.cashewnut.database.mappers.DBGroupsMapper;
//import com.loment.cashewnut.model.AccountsModel;
//import com.loment.cashewnut.model.ContactsModel;
//import com.loment.cashewnut.model.GroupModel;
//import com.loment.cashewnut.sender.SenderHandler;
//import com.loment.cashewnut.util.Helper;
//
///**
//*
//* @author sekhar
//*/
//public class GroupActivity1 {
//
//	// contact List components
//	ListView<Map.Entry<String, ContactsModel>> contactListView = null;
//	List<Map.Entry<String, ContactsModel>> contactList;
//	ObservableList<Map.Entry<String, ContactsModel>> contactObservableList = null;
//	String bgColour = "-fx-background-color: transparent;";
//
//	// Group List components
//	ListView<Map.Entry<String, GroupModel>> groupListView = null;
//	List<Map.Entry<String, GroupModel>> groupList;
//	ObservableList<Map.Entry<String, GroupModel>> groupObservableList = null;
//
//	// private ArrayList<String> adapterToVector = null;
//	private String adapterFrom = "";
//	private String adapterGroupId = "";
//	DBGroupsMapper groupMapper = DBGroupsMapper.getInstance();
//	ArrayList<ContactsModel> groupList1 = new ArrayList<ContactsModel>();
//
//	boolean isGroupMember = false;
//	boolean isAdmin = false;
//	String groupName = "";
//
//	@FXML
//	AnchorPane contactsAnchorPane;
//	Stage stage = null;
//	SplitPane splitPane = null;
//	double width;
//	double height;
//
//	String cashewnutId = "";
//
//	/**
//	 * Called when the activity is first created.
//	 *
//	 * @param adapterToVector
//	 * @param groupId
//	 */
//	public GroupActivity1(String groupId, Stage stage, SplitPane splitPane,
//			double width, double height) {
//
//		this.adapterGroupId = groupId;
//		this.stage = stage;
//		this.splitPane = splitPane;
//		this.width = width;
//		this.height = height;
//
//		setData();
//	}
//
//	private void setData() {
//		if (adapterGroupId != null && !adapterGroupId.trim().equals("")) {
//			GroupModel gModel = groupMapper.getGroup(adapterGroupId, true);
//			if (gModel != null) {
//
//				// this.adapterFrom = adapterFrom;
//				// this.adapterToVector = adapterToVector;
//				if (cashewnutId == null || cashewnutId.trim().equals("")) {
//					DBAccountsMapper accountsMapper = DBAccountsMapper
//							.getInstance();
//					AccountsModel accountsModel = accountsMapper.getAccount();
//					cashewnutId = accountsModel.getCashewnutId();
//				}
//
//				isGroupMember = false;
//				isAdmin = false;
//				groupList1.clear();
//
//				groupName = gModel.getGroupName();
//
//				if (gModel.getOwner() != null
//						&& gModel.getOwner().trim().equals(cashewnutId)) {
//					isAdmin = true;
//				}
//
//				try {
//					JSONArray members = gModel.getMembers();
//					for (int i = 0; i < members.length(); i++) {
//						ContactsModel cModel = new ContactsModel();
//						cModel.setCashewnutId(members.getString(i));
//
//						if (gModel.getOwner() != null
//								&& gModel.getOwner().trim()
//										.equals(members.getString(i))) {
//							cModel.setType(AppConfiguration.appConfString.group_admin1);
//						} else {
//							cModel.setType("  ");
//						}
//
//						if (cashewnutId != null
//								&& cashewnutId.trim().equals(
//										members.getString(i))) {
//							isGroupMember = true;
//						}
//
//						groupList1.add(cModel);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//	}
//
//	BorderPane mainGroupBorderPane = new BorderPane();
//
//	public BorderPane getLayout() {
//		if (groupName == null) {
//			groupName = "  ";
//		}
//		BorderPane from = getGroupNameCell(groupName);
//		createContactListComponents();
//
//		mainGroupBorderPane.setTop(from);
//		mainGroupBorderPane.setCenter(contactListView);
//
//		if (!isAdmin) {
//			Button editGroupButton = new Button();
//			editGroupButton.setId("custom-label-from");
//
//			editGroupButton.setWrapText(true);
//			editGroupButton.setStyle("-fx-background-radius: 0, 0;"
//					+ " -fx-background-color: transparent;");
//
//			editGroupButton.setPadding(new Insets(15, 15, 15, 15));
//
//			editGroupButton.setAlignment(Pos.CENTER);
//			
//			BorderPane toolBar = new BorderPane();
//			toolBar.setCenter(editGroupButton);
//
//			if (isGroupMember) {
//				editGroupButton.setText(AppConfiguration.appConfString.conversation_exitfromgroup);
//				toolBar.setStyle("-fx-background-color: #DEDEDC;");
//				editGroupButton.setOnAction(new EventHandler<ActionEvent>() {
//					@Override
//					public void handle(ActionEvent e) {
//						ContactsModel cModel = new ContactsModel();
//						cModel.setCashewnutId(cashewnutId);
//
//						ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
//						contactList.add(cModel);
//						deleteMember(contactList);
//					}
//				});
//			} else {
//				editGroupButton
//						.setText(AppConfiguration.appConfString.conversation_not_member);
//				toolBar.setStyle("-fx-background-color: #FFFFFF;");
//			}
//
//			mainGroupBorderPane.setBottom(toolBar);
//		}
//
//		return mainGroupBorderPane;
//	}
//
//	// =========================== Contact List ==========================
//	// ========================================================================
//	boolean isContactRightClick = false;
//
//	private void createContactListComponents() {
//		contactListView = new ListView<Map.Entry<String, ContactsModel>>();
//		contactList = new ArrayList<Map.Entry<String, ContactsModel>>();
//		contactObservableList = FXCollections.observableArrayList(contactList);
//		// contactsAnchorPane.getChildren().add(contactListView);
//		contactListView.setPrefWidth(290);
//
//		ChangeListener changeListener = (ChangeListener) new ChangeListener() {
//			public void changed(ObservableValue observable, Object oldValue,
//					Object newValue) {
//				final Map.Entry<String, ContactsModel> entry = (Map.Entry<String, ContactsModel>) newValue;
//				if (entry != null) {
//					if (isContactRightClick) {
//						Platform.runLater(new Runnable() {
//							@Override
//							public void run() {
//							}
//						});
//					}
//				}
//			}
//		};
//
//		contactListView.addEventFilter(MouseEvent.MOUSE_PRESSED,
//				new EventHandler<MouseEvent>() {
//
//					public void handle(MouseEvent event) {
//						isContactRightClick = false;
//						if (!event.isSecondaryButtonDown()) {
//							isContactRightClick = true;
//						}
//					}
//				});
//
//		contactListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
//			@Override
//			public void handle(KeyEvent ke) {
//				isContactRightClick = false;
//				if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
//					isContactRightClick = true;
//				}
//			}
//		});
//
//		contactListView.setItems(null);
//		contactListView.setItems(contactObservableList);
//		contactListView.getSelectionModel().selectedItemProperty()
//				.addListener(changeListener);
//
//		contactListView
//				.setCellFactory(new Callback<ListView<Map.Entry<String, ContactsModel>>, ListCell<Map.Entry<String, ContactsModel>>>() {
//					@Override
//					public ListCell<Map.Entry<String, ContactsModel>> call(
//							ListView<Map.Entry<String, ContactsModel>> p) {
//						ListCell<Map.Entry<String, ContactsModel>> cell = new ListCell<Map.Entry<String, ContactsModel>>() {
//							@Override
//							protected void updateItem(
//									Map.Entry<String, ContactsModel> t,
//									boolean bln) {
//								super.updateItem(t, bln);
//								if (t != null && t.getValue() != null) {
//									HBox cell = getGroupCell(t.getValue());
//									setGraphic(cell);
//									ContextMenu rootContextMenu = getContextListMenu(t
//											.getValue());
//									setContextMenu(rootContextMenu);
//								} else {
//									setGraphic(null);
//								}
//							}
//						};
//						return cell;
//					}
//				});
//		addDataToContactList();
//	}
//
//	private ContextMenu getContextListMenu(final ContactsModel premier) {
//		// instantiate the root context menu
//
//		final ContextMenu rootContextMenu = new ContextMenu();
//		MenuItem cmItem1 = new MenuItem(AppConfiguration.appConfString.set_group_admin);
//		cmItem1.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent e) {
//				ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
//				contactList.add(premier);
//				setAsAdmin(contactList);
//			}
//		});
//
//		MenuItem cmItem2 = new MenuItem(AppConfiguration.appConfString.send_message);
//		cmItem2.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//
//			}
//		});
//
//		MenuItem cmItem3 = new MenuItem(AppConfiguration.appConfString.delete_member);
//		cmItem3.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent e) {
//				// delete item
//				ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
//				contactList.add(premier);
//				deleteMember(contactList);
//			}
//		});
//
//		if (!premier.getCashewnutId().equals(cashewnutId)) {
//			if (isAdmin && !premier.getCashewnutId().equals(cashewnutId)) {
//				rootContextMenu.getItems().add(cmItem1);
//				rootContextMenu.getItems().add(cmItem2);
//				rootContextMenu.getItems().add(cmItem3);
//			} else {
//				rootContextMenu.getItems().add(cmItem2);
//			}
//		}
//
//		return rootContextMenu;
//	}
//
//	private HBox getGroupCell(ContactsModel t) {
//		DBContactsMapper profileMapper = DBContactsMapper.getInstance();
//		String memberString = "";
//		Label label = new Label();
//		Label members = new Label();
//
//		try {
//			String cid = t.getCashewnutId();
//			String name = "";
//			// label = new Label(memberString);
//			ContactsModel contactsModel = profileMapper.getContact(cid);
//			name = contactsModel.getFirstName();
//			if (name != null && name.trim().length() > 0) {
//				memberString = memberString + name;
//				label = new Label(memberString + "  " + t.getType());
//				members = new Label(cid);
//			} else {
//				memberString = memberString + cid + "  ";
//				label = new Label(memberString);
//				members = new Label("");
//			}
//		} catch (Exception e) {
//		}
//		ImageView user_icon_single = ConversationsViewRenderer.resize1(
//				AppConfiguration.getUserIconPath() + "ic_action_user_48.png", 48);
//		Label lblUserIcon = new Label(" ");
//		lblUserIcon.setGraphic(user_icon_single);
//
//		CircularUserIcon lblUserIcon1 = new CircularUserIcon("NS");
//		Text text = (Text) lblUserIcon1.getChildren().get(1);
//		text.setText(memberString.charAt(0) + "");
//
//		label.setId("custom-label-from");
//		members.setId("custom-label-from");
//		VBox cell = new VBox(label, members);
//		cell.setSpacing(4);
//		cell.setAlignment(Pos.CENTER_LEFT);
//		cell.setPrefWidth(200);
//		cell.setPadding(new Insets(0, 10, 0, 2));
//		cell.setPrefHeight(60);
//
//		HBox mainPanel = new HBox(lblUserIcon, cell);
//		mainPanel.setPadding(new Insets(0, 10, 0, 2));
//		mainPanel.setAlignment(Pos.CENTER_LEFT);
//		return mainPanel;
//	}
//
//	private BorderPane getGroupNameCell(String t) {
//		Label label = new Label(t);
//		Button editGroupNameButton = new Button();
//		Button addMembersButton = new Button();
//
//		ImageView user_icon_single = ConversationsViewRenderer.resize1(
//				AppConfiguration.getUserIconPath() + "ic_action_users_green.png", 48);
//
//		ImageView addMembersButtonIcon = ConversationsViewRenderer.resize1(
//				AppConfiguration.getIconPath() + "ic_action_add.png", 22);
//
//		ImageView editGroupButtonIcon = ConversationsViewRenderer.resize1(
//				AppConfiguration.getIconPath() + "ic_action_edit.png", 22);
//
//		Label lblUserIcon = new Label(" ");
//		lblUserIcon.setGraphic(user_icon_single);
//
//		label.setWrapText(true);
//		label.setStyle("-fx-background-radius: 0, 0;"
//				+ " -fx-background-color: transparent;"
//				+ " -fx-padding: 0.2em 0.0em 0.0em 0.0em;");
//		label.setId("custom-label-from");
//
//		editGroupNameButton.setWrapText(true);
//		editGroupNameButton.setStyle("-fx-background-radius: 0, 0;"
//				+ " -fx-background-color: transparent;"
//				+ " -fx-padding: 0.2em 0.0em 0.0em 0.0em;");
//
//		// editGroupButton.setText("Edit Name");
//		editGroupNameButton.setGraphic(editGroupButtonIcon);
//		editGroupNameButton.setAlignment(Pos.CENTER_RIGHT);
//
//		addMembersButton.setWrapText(true);
//		addMembersButton.setStyle("-fx-background-radius: 0, 0;"
//				+ " -fx-background-color: transparent;"
//				+ " -fx-padding: 0.2em 0.0em 0.0em 2.0em;");
//
//		// addMembersButton.setText("Add");
//		addMembersButton.setGraphic(addMembersButtonIcon);
//		addMembersButton.setAlignment(Pos.CENTER_RIGHT);
//		addMembersButton.setPrefHeight(60);
//
//		HBox cell = new HBox();
//		cell.getChildren().add(label);
//		if (isAdmin)
//			cell.getChildren().add(editGroupNameButton);
//
//		cell.setSpacing(10);
//		cell.setAlignment(Pos.CENTER_LEFT);
//		cell.setPrefWidth(200);
//		cell.setPadding(new Insets(0, 10, 0, 2));
//		cell.setPrefHeight(60);
//
//		BorderPane root = new BorderPane();
//
//		HBox mainPanel = new HBox(lblUserIcon, cell);
//		mainPanel.setPadding(new Insets(0, 10, 0, 2));
//		mainPanel.setAlignment(Pos.CENTER);
//
//		if (isAdmin)
//			root.setLeft(addMembersButton);
//		root.setCenter(mainPanel);
//
//		// HBox mainPanel = new HBox(lblUserIcon, cell);
//		// mainPanel.setPadding(new Insets(0, 10, 0, 2));
//		// mainPanel.setAlignment(Pos.CENTER_LEFT);
//
//		editGroupNameButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				changeGroupName();
//			}
//		});
//
//		addMembersButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				addMembersToGroup();
//			}
//		});
//
//		return root;
//	}
//
//	ChangeGroupNameActivity chooseaddcontact = null;
//	AddGroup chooseaddgroup = null;
//
//	public void changeGroupName() {
//		if (chooseaddcontact != null && chooseaddcontact.isShowing()) {
//			return;
//		}
//		splitPane.setOpacity(0.6);
//		chooseaddcontact = new ChangeGroupNameActivity(adapterGroupId, stage,
//				width, height);
//		chooseaddcontact.showAndWait();
//		splitPane.setOpacity(1);
//
//		if (chooseaddcontact.isChangedGroupName()) {
//			groupName = chooseaddcontact.getChangedGroupName();
//			BorderPane from = getGroupNameCell(groupName);
//			mainGroupBorderPane.setTop(null);
//			mainGroupBorderPane.setTop(from);
//		}
//	}
//
//	public void addMembersToGroup() {
//		if (chooseaddgroup != null && chooseaddgroup.isShowing()) {
//			return;
//		}
//		splitPane.setOpacity(0.6);
//
//		chooseaddgroup = new AddGroup(stage, width, height, adapterGroupId);
//		chooseaddgroup.showAndWait();
//
//		String groupServerId = chooseaddgroup.getGroupId();
//		if (groupServerId != null && !groupServerId.equals("")) {
//			GroupModel groupModel = Helper.getGroupDetails(adapterGroupId);
//			if (groupModel != null) {
//				Platform.runLater(new Runnable() {
//					@Override
//					public void run() {
//						setData();
//						getLayout();
//					}
//				});
//
//			}
//		}
//		splitPane.setOpacity(1);
//	}
//
//	void addDataToContactList() {
//		try {
//			cleanContactList();
//			// DBContactsMapper profileMapper = DBContactsMapper.getInstance();
//			// if (adapterToVector.size() > 0) {
//			// for (int i = 0; i < adapterToVector.size(); i++) {
//			//
//			// ContactsModel cModel = profileMapper
//			// .getContact(adapterToVector.get(i));
//			// cModel.setCashewnutId(adapterToVector.get(i));
//			// groupList1.add(cModel);
//			// }
//			// }
//
//			if (groupList1.size() > 0) {
//				// Sorting
//				Collections.sort(groupList1, new Comparator<ContactsModel>() {
//					@Override
//					public int compare(ContactsModel groupModel1,
//							ContactsModel groupModel2) {
//						return groupModel1.getFirstName().compareTo(
//								groupModel2.getFirstName());
//					}
//				});
//
//				ArrayList<Integer> contactIds = new ArrayList<Integer>();
//				for (Object groupList11 : groupList1) {
//					ContactsModel gModel = (ContactsModel) groupList11;
//					if (contactIds.contains(gModel.getCashewnutId())) {
//						continue;
//					}
//					contactIds.add(gModel.getContactId());
//					Map.Entry<String, ContactsModel> entry = new AbstractMap.SimpleEntry(
//							gModel.getCashewnutId(), gModel);
//					contactList.add(entry);
//				}
//
//				contactObservableList.setAll(contactList);
//				contactListView.setItems(contactObservableList);
//			}
//		} catch (Exception e) {
//			// e.printStackTrace();
//		}
//	}
//
//	public void deleteMember(ArrayList<ContactsModel> contactList) {
//		try {
//			if (!CashewnutApplication.isNetworkConnected()) {
////				System.out.println("network_unavailable");
//				return;
//			}
//
//			if (contactList == null || contactList.size() < 0) {
//				return;
//			}
//
//			if (adapterGroupId != null
//					&& !adapterGroupId.trim().equalsIgnoreCase("")) {
//				final GroupModel storedGModel = DBGroupsMapper.getInstance()
//						.getGroup(adapterGroupId, true);
//
//				if (storedGModel != null && storedGModel.getGroupId() > -1) {
//					final int size = storedGModel.getMembers().length();
//
//					JSONArray members = new JSONArray();
//					for (int i = 0; i < contactList.size(); i++) {
//						members.put(contactList.get(i).getCashewnutId());
//					}
//					final GroupModel gModel = new GroupModel();
//					gModel.setGroupId(storedGModel.getGroupId());
//					gModel.setServerGroupId(adapterGroupId);
//					gModel.setMembers(members);
//					gModel.setOperation(GroupModel.OPERATION_DELETE_MEMBER);
//					new Thread() {
//						public void run() {
//							try {
//								SenderHandler.getInstance().sendGroup(gModel);
//								boolean isChanged = false;
//								int loopCount = 0;
//								do {
//									Thread.sleep(2000);
//									GroupModel gModel = DBGroupsMapper
//											.getInstance().getGroup(
//													storedGModel.getGroupId()
//															+ "", false);
//									if (gModel.getServerGroupId() != null
//											&& !gModel.getServerGroupId()
//													.equals("")) {
//										isChanged = gModel.getMembers()
//												.length() < size;
//										if (isChanged) {
////											System.out
////													.println("Group_member_deleted_successfully");
//											break;
//										}
//									}
//								} while (!isChanged && loopCount++ < 5);
//								if (isChanged) {
//									GroupModel groupModel = Helper
//											.getGroupDetails(adapterGroupId);
//									if (groupModel != null) {
//										Platform.runLater(new Runnable() {
//											@Override
//											public void run() {
//												setData();
//												getLayout();
//											}
//										});
//									}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}.start();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void setAsAdmin(ArrayList<ContactsModel> contactList) {
//		try {
//			if (!CashewnutApplication.isNetworkConnected()) {
////				System.out.println("network_unavailable");
//				return;
//			}
//
//			if (contactList == null || contactList.size() < 0) {
//				return;
//			}
//
//			if (adapterGroupId != null
//					&& !adapterGroupId.trim().equalsIgnoreCase("")) {
//				final GroupModel storedGModel = DBGroupsMapper.getInstance()
//						.getGroup(adapterGroupId, true);
//
//				if (storedGModel != null && storedGModel.getGroupId() > -1) {
//					final String admin = storedGModel.getOwner();
//					final GroupModel gModel = new GroupModel();
//					gModel.setGroupId(storedGModel.getGroupId());
//					gModel.setServerGroupId(adapterGroupId);
//					for (int i = 0; i < contactList.size(); i++) {
//						gModel.setOwner(contactList.get(i).getCashewnutId());
//					}
//					gModel.setOperation(GroupModel.OPERATION_CHANGE_MAIN_ADMIN);
//					new Thread() {
//						public void run() {
//							try {
//								SenderHandler.getInstance().sendGroup(gModel);
//								boolean isChanged = false;
//								int loopCount = 0;
//								do {
//									Thread.sleep(2000);
//									GroupModel gModel = DBGroupsMapper
//											.getInstance().getGroup(
//													storedGModel.getGroupId()
//															+ "", false);
//									if (gModel.getServerGroupId() != null
//											&& !gModel.getServerGroupId()
//													.equals("")) {
//										isChanged = !gModel.getOwner().equals(
//												admin);
//										if (isChanged) {
////											System.out
////													.println("Group_admin_changed");
//											break;
//										}
//									}
//								} while (!isChanged && loopCount++ < 5);
//
//								if (isChanged) {
//									GroupModel groupModel = Helper
//											.getGroupDetails(adapterGroupId);
//									if (groupModel != null) {
//										Platform.runLater(new Runnable() {
//											@Override
//											public void run() {
//												setData();
//												getLayout();
//											}
//										});
//									}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}.start();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	void cleanContactList() {
//		contactListView.getItems().clear();
//		contactObservableList.clear();
//		contactList.clear();
//	}
//}
