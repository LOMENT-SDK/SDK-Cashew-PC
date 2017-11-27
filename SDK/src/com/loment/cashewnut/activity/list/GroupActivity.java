package com.loment.cashewnut.activity.list;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsType6Mapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.sender.SenderHandler;
import com.loment.cashewnut.util.Helper;
import com.loment.cashewnut.activity.controller.ThreadIdGenerator;
import com.loment.cashewnut.activity.list.ConversationView;
import com.loment.cashewnut.activity.list.ConversationsListRenderer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author sekhar
 */
public class GroupActivity {

	// contact List components
	ListView<Map.Entry<String, ContactsModel>> contactListView = null;
	List<Map.Entry<String, ContactsModel>> contactList;
	ObservableList<Map.Entry<String, ContactsModel>> contactObservableList = null;
	String bgColour = "-fx-background-color: transparent;";
	// Group Icon
	HashMap<String, String> group_map = new HashMap<String, String>();
	// Group List components
	ListView<Map.Entry<String, GroupModel>> groupListView = null;
	List<Map.Entry<String, GroupModel>> groupList;
	ObservableList<Map.Entry<String, GroupModel>> groupObservableList = null;

	// private ArrayList<String> adapterToVector = null;
	private String adapterFrom = "";
	private String adapterGroupId = "";
	DBGroupsType6Mapper groupMapper = DBGroupsType6Mapper.getInstance();
	ArrayList<ContactsModel> groupList1 = new ArrayList<ContactsModel>();
	CheckBox feature4 = new CheckBox("Allow Others to Add Members");
	boolean isGroupMember = false;
	boolean isAdmin = false;
	boolean isMainAdmin = false;
	boolean isFeatureEnabled = false;
	boolean isChange = false;
	String groupName = "";
	GroupModel gModel;
	BorderPane root1 = new BorderPane();// to center

	@FXML
	AnchorPane contactsAnchorPane;
	Stage stage = null;
	SplitPane splitPane = null;
	double width;
	double height;

	String cashewnutId = "";
	@FXML
	Button okButton;
	@FXML
	Button cancelButton;
	ImageView user_icon_single = null;
	Label lblUserIcon = new Label(" ");
	Label lblimg = new Label("");
	boolean isFeature4Checked=true;
	/**
	 * Called when the activity is first created.
	 *
	 * @param adapterToVector
	 * @param groupId
	 */
	ConversationsListRenderer conversationsListRenderer;
	ConversationView conversationView;

	public GroupActivity(ConversationView conversationView, String groupId, Stage stage, SplitPane splitPane,
			double width, double height) {

		this.adapterGroupId = groupId;
		this.stage = stage;
		this.splitPane = splitPane;
		this.width = width;
		this.height = height;
		this.conversationView = conversationView;
		setData();
	}

	public GroupActivity() {
		// TODO Auto-generated constructor stub
	}

	private void setData() {
		if (adapterGroupId != null && !adapterGroupId.trim().equals("")) {
			gModel = groupMapper.getGroup(adapterGroupId, true);
			if (gModel != null) {

				if (cashewnutId == null || cashewnutId.trim().equals("")) {
					DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
					AccountsModel accountsModel = accountsMapper.getAccount();
					cashewnutId = accountsModel.getCashewnutId();
				}

				isGroupMember = false;
				isMainAdmin = false;
				isAdmin = false;
				groupList1.clear();

				groupName = gModel.getGroupName();

				if (gModel.getOwner() != null && gModel.getOwner().trim().equals(cashewnutId)) {
					isMainAdmin = true;
				} else if (AppConfiguration.GROUP_TYPE6) {
					if (gModel.getAdmins() != null && gModel.getAdmins().toString().contains(cashewnutId)) {
						isAdmin = true;
					}

				}

				try {
					DBContactsMapper profileMapper = DBContactsMapper.getInstance();
					JSONArray members = gModel.getMembers();
					JSONArray admins = gModel.getAdmins();
					for (int i = 0; i < members.length(); i++) {
						ContactsModel cModel = new ContactsModel();
						cModel.setCashewnutId(members.getString(i));
						cModel=profileMapper.getContact(members.getString(i), 0);
						if (gModel.getOwner() != null && gModel.getOwner().trim().equals(members.getString(i))) {
							cModel.setType("(Main Admin)");
							cModel.setMemberType(GroupModel.SUPERADMIN);
						} else if (AppConfiguration.GROUP_TYPE6) {
							if (admins != null && admins.toString().contains(members.getString(i))) {
								cModel.setType("(Admin)");
								cModel.setMemberType(GroupModel.ADMIN);
							}
						} else {
							cModel.setType("  ");
							cModel.setMemberType(GroupModel.MEMBER);
						}
						if (cashewnutId.trim().equals(members.getString(i)) || cashewnutId.equals(gModel.getOwner())) {
							isGroupMember = true;
						}
						groupList1.add(cModel);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (AppConfiguration.GROUP_TYPE6) {
				setFeatureContainer(gModel);
			}
		}
	}

	protected void setFeatureContainer(final GroupModel gModel) {
		try {
			if (gModel.getFeature() != null && gModel.getFeature().toString().contains("4")) {
				feature4.setSelected(true);
			} else {
				if(gModel.getFeature().length()<1)
				{
					feature4.setSelected(true);
				}
				else
				{
					feature4.setSelected(false);	
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	BorderPane mainGroupBorderPane = new BorderPane();

	public BorderPane getLayout() {
		if (groupName == null) {
			groupName = "  ";
		}
		BorderPane from = getGroupNameCell(groupName);
		createContactListComponents();

		mainGroupBorderPane.setTop(from);
		mainGroupBorderPane.setCenter(contactListView);

		if (!isMainAdmin) {
			Button editGroupButton = new Button();
			editGroupButton.setId("custom-label-from");

			editGroupButton.setWrapText(true);
			editGroupButton.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;");

			editGroupButton.setPadding(new Insets(15, 15, 15, 15));

			editGroupButton.setAlignment(Pos.CENTER);

			BorderPane toolBar = new BorderPane();
			toolBar.setCenter(editGroupButton);
			if (isAdmin) {
				editGroupButton.setText(AppConfiguration.appConfString.change_member);
				toolBar.setStyle("-fx-background-color: #DEDEDC;");
				editGroupButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						ContactsModel cModel = new ContactsModel();
						cModel.setCashewnutId(cashewnutId);
						ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
						contactList.add(cModel);
						changeAsMember(contactList);
					}
				});
			} else if (isGroupMember) {
				editGroupButton.setText(AppConfiguration.appConfString.conversation_exitfromgroup);
				toolBar.setStyle("-fx-background-color: #DEDEDC;");
				editGroupButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent e) {
						ContactsModel cModel = new ContactsModel();
						cModel.setCashewnutId(cashewnutId);

						ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
						contactList.add(cModel);
						deleteMember(contactList);
					}
				});
			} else {
				editGroupButton.setText(AppConfiguration.appConfString.conversation_not_member);
				toolBar.setStyle("-fx-background-color: #FFFFFF;");
			}

			mainGroupBorderPane.setBottom(toolBar);
		}
		return mainGroupBorderPane;
	}

	// =========================== Contact List ==========================
	// ========================================================================
	boolean isContactRightClick = false;

	private void createContactListComponents() {
		contactListView = new ListView<Map.Entry<String, ContactsModel>>();
		contactList = new ArrayList<Map.Entry<String, ContactsModel>>();
		contactObservableList = FXCollections.observableArrayList(contactList);
		contactListView.setPrefWidth(290);

		ChangeListener changeListener = (ChangeListener) new ChangeListener() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				final Map.Entry<String, ContactsModel> entry = (Map.Entry<String, ContactsModel>) newValue;
				if (entry != null) {
					if (isContactRightClick) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
							}
						});
					}
				}
			}
		};
		contactListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				isContactRightClick = false;
				if (!event.isSecondaryButtonDown()) {
					isContactRightClick = true;
				}
			}
		});

		contactListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				isContactRightClick = false;
				if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
					isContactRightClick = true;
				}
			}
		});

		contactListView.setItems(null);
		contactListView.setItems(contactObservableList);
		contactListView.getSelectionModel().selectedItemProperty().addListener(changeListener);

		contactListView.setCellFactory(
				new Callback<ListView<Map.Entry<String, ContactsModel>>, ListCell<Map.Entry<String, ContactsModel>>>() {
					@Override
					public ListCell<Map.Entry<String, ContactsModel>> call(
							ListView<Map.Entry<String, ContactsModel>> p) {
						ListCell<Map.Entry<String, ContactsModel>> cell = new ListCell<Map.Entry<String, ContactsModel>>() {
							@Override
							protected void updateItem(Map.Entry<String, ContactsModel> t, boolean bln) {
								super.updateItem(t, bln);
								if (t != null && t.getValue() != null) {
									HBox cell = getGroupCell(t.getValue());
									setGraphic(cell);
									ContextMenu rootContextMenu = getContextListMenu(t.getValue());
									setContextMenu(rootContextMenu);
								} else {
									setGraphic(null);
								}
							}
						};
						return cell;
					}
				});
		addDataToContactList();
	}

	private ContextMenu getContextListMenu(final ContactsModel premier) {
		// instantiate the root context menu
		final ContextMenu rootContextMenu = new ContextMenu();
		MenuItem cmItem1 = new MenuItem(AppConfiguration.appConfString.Make_Main_Admin);
		cmItem1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
				contactList.add(premier);
				setAsAdmin(contactList);
			}
		});

		MenuItem cmItem2 = new MenuItem(AppConfiguration.appConfString.send_message);
		cmItem2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				conversationView.viewConversationFContact(premier.cashewnutId);
			}
		});

		MenuItem cmItem3 = new MenuItem(AppConfiguration.appConfString.delete_member);
		cmItem3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// delete item
				ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
				contactList.add(premier);

				deleteMember(contactList);

			}
		});
		MenuItem cmItem4 = new MenuItem(AppConfiguration.appConfString.make_admin);// change
																					// to
																					// mainadmin
		cmItem4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
				contactList.add(premier);
				changeAsAdmin(contactList);
			}
		});
		MenuItem cmItem5 = new MenuItem(AppConfiguration.appConfString.change_member);
		cmItem5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
				contactList.add(premier);
				changeAsMember(contactList);
			}
		});
		if (isGroupMember) {
			if (isMainAdmin) {
				if (AppConfiguration.GROUP_TYPE6) {
					root1.setVisible(true);
					if (!cashewnutId.trim().equals(premier.getCashewnutId())) {
						if (gModel.getAdmins() != null
								&& gModel.getAdmins().toString().contains(premier.getCashewnutId())) {
							rootContextMenu.getItems().add(cmItem1);
							rootContextMenu.getItems().add(cmItem2);
							rootContextMenu.getItems().add(cmItem5);
						} else if (gModel.getAdmins() != null
								&& !gModel.getAdmins().toString().contains(premier.getCashewnutId())) {
							rootContextMenu.getItems().add(cmItem3);
							rootContextMenu.getItems().add(cmItem2);
							rootContextMenu.getItems().add(cmItem4);
							rootContextMenu.getItems().add(cmItem1);
						}
					}
				} else {
					if (!cashewnutId.trim().equals(premier.getCashewnutId())) {
						rootContextMenu.getItems().add(cmItem3);
						rootContextMenu.getItems().add(cmItem2);
						rootContextMenu.getItems().add(cmItem1);
					}
				}
			} else if (isAdmin) {
				if (!cashewnutId.trim().equals(premier.getCashewnutId())) {
					if (!gModel.getAdmins().toString().contains(premier.getCashewnutId())
							&& !gModel.getOwner().equals(premier.getCashewnutId())) {
						try {
							rootContextMenu.getItems().add(cmItem3);
							rootContextMenu.getItems().add(cmItem2);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						rootContextMenu.getItems().add(cmItem2);
					}
				}
			}
			else
			{
				rootContextMenu.getItems().add(cmItem2);
			}
		}
		return rootContextMenu;
	}

	private HBox getGroupCell(ContactsModel t) {
		DBContactsMapper profileMapper = DBContactsMapper.getInstance();
		String memberString = "";
		Label label = new Label();
		Label members = new Label();

		try {
			String cid = t.getCashewnutId();
			String name = "";
			// label = new Label(memberString);
			ContactsModel contactsModel = profileMapper.getContact(cid,0);
			name = contactsModel.getFirstName();
			if (name != null && name.trim().length() > 0) {
				memberString = memberString + name;
				label = new Label(memberString + "  " + t.getType());
				members = new Label(cid);
			} else {
				memberString = memberString + cid + "  ";
				label = new Label(memberString);
				members = new Label("");
			}
		} catch (Exception e) {
		}
		ImageView user_icon_single = ConversationsViewRenderer
				.resize1(AppConfiguration.getUserIconPath() + "ic_action_user_48.png", 48);
		Label lblUserIcon = new Label(" ");
		CircularUserIcon lblUserIcon1 = new CircularUserIcon("NS");
		Text text = (Text) lblUserIcon1.getChildren().get(1);
		text.setText(memberString.charAt(0) + "");
		lblUserIcon.setGraphic(user_icon_single);
		label.setId("custom-label-from");
		members.setId("custom-label-from");
		VBox cell = new VBox(label, members);
		cell.setSpacing(4);
		cell.setAlignment(Pos.CENTER_LEFT);
		cell.setPrefWidth(200);
		cell.setPadding(new Insets(0, 10, 0, 2));
		cell.setPrefHeight(60);
		HBox mainPanel = new HBox(lblUserIcon, cell);
		mainPanel.setPadding(new Insets(0, 10, 0, 2));
		mainPanel.setAlignment(Pos.CENTER_LEFT);
		return mainPanel;
	}

	private BorderPane getGroupNameCell(String t) {
		Label label = new Label(t);
		Button editGroupNameButton = new Button();
		Button editImageButton = new Button();
		Button addMembersButton = new Button();

		ImageView addMembersButtonIcon = ConversationsViewRenderer
				.resize1(AppConfiguration.getIconPath() + "ic_action_add.png", 22);

		ImageView editGroupButtonIcon = ConversationsViewRenderer
				.resize1(AppConfiguration.getIconPath() + "ic_action_edit.png", 22);

		label.setWrapText(true);
		label.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.2em 0.0em 0.0em 0.0em;");
		label.setId("custom-label-from");

		editGroupNameButton.setWrapText(true);
		editGroupNameButton.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.2em 0.0em 0.0em 0.0em;");

		// editGroupButton.setText("Edit Name");
		editGroupNameButton.setGraphic(editGroupButtonIcon);
		editGroupNameButton.setAlignment(Pos.CENTER_RIGHT);

		editImageButton.setWrapText(true);
		editImageButton.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.2em 0.0em 0.0em 0.0em;");
		editImageButton.setText("Edit");
		// editImageButton.setGraphic(editGroupButtonIcon);
		editImageButton.setAlignment(Pos.CENTER_RIGHT);
		addMembersButton.setWrapText(true);
		addMembersButton.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.2em 0.0em 0.0em 2.0em;");
		addMembersButton.setGraphic(addMembersButtonIcon);
		addMembersButton.setAlignment(Pos.CENTER_RIGHT);
		addMembersButton.setPrefHeight(60);
		String absolutePath = Helper.getAbsolutePath("group_profile");	
		File f = null;
		File[] files = new File(absolutePath).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.startsWith(adapterGroupId)) {
					f = file;
					break;
				}
			}
		}
		
		HBox cell = new HBox();
		cell.getChildren().add(label);
		if (isMainAdmin || isAdmin)
			cell.getChildren().add(editGroupNameButton);
		cell.setSpacing(10);
		cell.setAlignment(Pos.CENTER_LEFT);
		cell.setPrefWidth(200);
		cell.setPadding(new Insets(0, 0, 0, 2));
		cell.setPrefHeight(60);
		HBox imageCell = new HBox();
		imageCell.setSpacing(10);
		if (f != null && f.exists()) {
			user_icon_single = ConversationsViewRenderer.resizeGroupIcon(f.toURI().toString(), 140);
			imageCell.setPadding(new Insets(10, 0, 0, -40));

		} else {
			user_icon_single = ConversationsViewRenderer
					.resize1(AppConfiguration.getUserIconPath() + "ic_action_users_green.png", 48);
			imageCell.setPadding(new Insets(10, 0, 0, 2));
		}

		lblUserIcon.setGraphic(user_icon_single);
		imageCell.setAlignment(Pos.CENTER_LEFT);
		imageCell.setPrefWidth(200);
		imageCell.getChildren().add(lblUserIcon);
		imageCell.getChildren().add(editImageButton);
		BorderPane root = new BorderPane();
		root1.setPadding(new Insets(0, 0, 10, 0));
		root1.setCenter(feature4);// to center
		root1.setVisible(false);
		VBox mainPanel = new VBox(imageCell, cell);
		mainPanel.setPadding(new Insets(0, 10, 0, 170));
		mainPanel.setAlignment(Pos.CENTER);
		if (isMainAdmin || isAdmin)
			root.setLeft(addMembersButton);
		root.setCenter(mainPanel);
		root.setBottom(root1);// to center
		editGroupNameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				changeGroupName();
			}
		});

		editImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				try {
					String path = changeGroupIcon();
					File f = new File(path);
					Platform.runLater(new Runnable() {
						public void run() {
							user_icon_single = ConversationsViewRenderer.resizeGroupIcon(f.toURI().toString(), 130);
							lblUserIcon.setGraphic(user_icon_single);
							updateView(adapterGroupId);
							conversationView.addDataToGroupList();
						}
					});
				} catch (Exception ex) {

					ex.printStackTrace();
				}
			}
		});

		feature4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (feature4.isSelected()) {
					confirm(true);
				} else {
					confirm(false);
				}
			}
		});

		addMembersButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				addMembersToGroup();
			}
		});

		return root;
	}

	ChangeGroupNameActivity chooseaddcontact = null;
	AddGroup chooseaddgroup = null;
	DialogClass choseok = null;

	public void confirm(boolean value) {
		if (choseok != null && choseok.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);
		choseok = new DialogClass(stage, width, height, value, adapterGroupId, this);
		choseok.showAndWait();
		splitPane.setOpacity(1);

	}

	public void changeGroupName() {
		if (chooseaddcontact != null && chooseaddcontact.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);
		chooseaddcontact = new ChangeGroupNameActivity(adapterGroupId, stage, width, height);
		chooseaddcontact.showAndWait();
		splitPane.setOpacity(1);

		if (chooseaddcontact.isChangedGroupName()) {
			groupName = chooseaddcontact.getChangedGroupName();
			BorderPane from = getGroupNameCell(groupName);
			mainGroupBorderPane.setTop(null);
			mainGroupBorderPane.setTop(from);
		}
	}

	public void addMembersToGroup() {
		if (chooseaddgroup != null && chooseaddgroup.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);

		chooseaddgroup = new AddGroup(stage, width, height, adapterGroupId);
		chooseaddgroup.showAndWait();

		String groupServerId = chooseaddgroup.getGroupId();
		if (groupServerId != null && !groupServerId.equals("")) {
			GroupModel groupModel = Helper.getGroupDetails(adapterGroupId);
			if (groupModel != null) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						setData();
						getLayout();
					}
				});

			}
		}
		splitPane.setOpacity(1);
	}

	void addDataToContactList() {
		try {
			cleanContactList();

			if (groupList1.size() > 0) {
				// Sorting
				Collections.sort(groupList1, new Comparator<ContactsModel>() {
					@Override
					public int compare(ContactsModel groupModel1, ContactsModel groupModel2) {
						return groupModel1.getFirstName().compareTo(groupModel2.getFirstName());
					}
				});

				ArrayList<Integer> contactIds = new ArrayList<Integer>();
				for (Object groupList11 : groupList1) {
					ContactsModel gModel = (ContactsModel) groupList11;
					if (contactIds.contains(gModel.getCashewnutId())) {
						continue;
					}
					contactIds.add(gModel.getContactId());
					Map.Entry<String, ContactsModel> entry = new AbstractMap.SimpleEntry(gModel.getCashewnutId(),
							gModel);
					contactList.add(entry);
				
				}
				
				contactObservableList.setAll(contactList);
				contactListView.setItems(contactObservableList);
			}
		} catch (Exception e) {
		}
	}

	public void deleteMember(ArrayList<ContactsModel> contactList) {
		try {
			if (!CashewnutApplication.isNetworkConnected()) {
				return;
			}

			if (contactList == null || contactList.size() < 0) {
				return;
			}

			if (adapterGroupId != null && !adapterGroupId.trim().equalsIgnoreCase("")) {
				final GroupModel storedGModel = DBGroupsType6Mapper.getInstance().getGroup(adapterGroupId, true);

				if (storedGModel != null && storedGModel.getGroupId() > -1) {
					final int size = storedGModel.getMembers().length();

					JSONArray members = new JSONArray();
					for (int i = 0; i < contactList.size(); i++) {
						members.put(contactList.get(i).getCashewnutId());
					}
					final GroupModel gModel = new GroupModel();
					gModel.setGroupId(storedGModel.getGroupId());
					gModel.setServerGroupId(adapterGroupId);
					gModel.setMembers(members);
					gModel.setFrom(cashewnutId);
					gModel.setOperation(GroupModel.OPERATION_DELETE_MEMBER);
					new Thread() {
						public void run() {
							try {
								SenderHandler.getInstance().sendGroup(gModel);
								boolean isChanged = false;
								int loopCount = 0;
								do {
									Thread.sleep(2000);
									GroupModel gModel = DBGroupsType6Mapper.getInstance()
											.getGroup(storedGModel.getGroupId() + "", false);
									if (gModel.getServerGroupId() != null && !gModel.getServerGroupId().equals("")) {
										isChanged = gModel.getMembers().length() < size;
										if (isChanged) {
											break;
										}
									}
								} while (!isChanged && loopCount++ < 5);
								if (isChanged) {
									GroupModel groupModel = Helper.getGroupDetails(adapterGroupId);
									if (groupModel != null) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												setData();
												getLayout();
											}
										});
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAsAdmin(ArrayList<ContactsModel> contactList) {
		try {
			if (!CashewnutApplication.isNetworkConnected()) {
				return;
			}

			if (contactList == null || contactList.size() < 0) {
				return;
			}

			if (adapterGroupId != null && !adapterGroupId.trim().equalsIgnoreCase("")) {
				final GroupModel storedGModel = DBGroupsType6Mapper.getInstance().getGroup(adapterGroupId, true);

				if (storedGModel != null && storedGModel.getGroupId() > -1) {
					final String admin = storedGModel.getOwner();
					final GroupModel gModel = new GroupModel();
					gModel.setGroupId(storedGModel.getGroupId());
					gModel.setFrom(cashewnutId);
					gModel.setServerGroupId(adapterGroupId);
					for (int i = 0; i < contactList.size(); i++) {
						gModel.setOwner(contactList.get(i).getCashewnutId());
					}
					gModel.setOperation(GroupModel.OPERATION_CHANGE_MAIN_ADMIN);

					new Thread() {
						public void run() {
							try {
								SenderHandler.getInstance().sendGroup(gModel);
								boolean isChanged = false;
								int loopCount = 0;
								do {
									Thread.sleep(2000);
									GroupModel groupModel = DBGroupsType6Mapper.getInstance()
											.getGroup(storedGModel.getGroupId() + "", false);
									if (groupModel.getServerGroupId() != null
											&& !groupModel.getServerGroupId().equals("")) {
										isChanged = !groupModel.getOwner().equals(admin);
										if (isChanged) {
											break;
										}
									}
								} while (!isChanged && loopCount++ < 5);
								if (isChanged) {
									GroupModel groupModel = Helper.getGroupDetails(adapterGroupId);
									if (groupModel != null) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												setData();
												getLayout();
											}
										});
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeAsAdmin(ArrayList<ContactsModel> contactList) {
		try {
			if (!CashewnutApplication.isNetworkConnected()) {
				return;
			}
			if (contactList == null || contactList.size() < 0) {
				return;
			}
			if (adapterGroupId != null && !adapterGroupId.trim().equalsIgnoreCase("")) {
				final GroupModel storedGModel = DBGroupsType6Mapper.getInstance().getGroup(adapterGroupId, true);
				JSONArray admins = new JSONArray();
				if (storedGModel != null && storedGModel.getGroupId() > -1) {
					final int size = storedGModel.getAdmins().length();

					for (int i = 0; i < contactList.size(); i++) {
						if (getStringIndexOfJSONArray(admins, contactList.get(i).getCashewnutId()) < 0) {
							admins.put(contactList.get(i).getCashewnutId());
						}
					}

					final GroupModel gModel = new GroupModel();
					gModel.setGroupId(storedGModel.getGroupId());
					gModel.setServerGroupId(adapterGroupId);
					gModel.setAdmins(admins);
					gModel.setFrom(cashewnutId);
					gModel.setOperation(GroupModel.OPERATION_CHANGED_AS_ADMIN);
					new Thread() {
						public void run() {
							try {
								SenderHandler.getInstance().sendGroup(gModel);
								boolean isChanged = false;
								int loopCount = 0;
								do {
									Thread.sleep(2000);
									GroupModel gModel = DBGroupsType6Mapper.getInstance()
											.getGroup(storedGModel.getGroupId() + "", false);
									if (gModel.getServerGroupId() != null && !gModel.getServerGroupId().equals("")) {
										isChanged = gModel.getAdmins().length() > size;

										if (isChanged) {
											break;
										}
									}
								} while (!isChanged && loopCount++ < 5);
								if (isChanged) {
									GroupModel groupModel = Helper.getGroupDetails(adapterGroupId);
									if (groupModel != null) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												setData();
												getLayout();
											}
										});
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeAsMember(ArrayList<ContactsModel> contactList) {
		try {

			if (!CashewnutApplication.isNetworkConnected()) {
				return;
			}

			if (contactList == null || contactList.size() < 0) {
				return;
			}

			if (adapterGroupId != null && !adapterGroupId.trim().equalsIgnoreCase("")) {
				final GroupModel storedGModel = DBGroupsType6Mapper.getInstance().getGroup(adapterGroupId, true);

				if (storedGModel != null && storedGModel.getGroupId() > -1) {
					final int size = storedGModel.getAdmins().length();

					JSONArray admins = new JSONArray();
					for (int i = 0; i < contactList.size(); i++) {
						admins.put(contactList.get(i).getCashewnutId());
					}
					final GroupModel gModel = new GroupModel();
					gModel.setGroupId(storedGModel.getGroupId());
					gModel.setServerGroupId(adapterGroupId);
					gModel.setAdmins(admins);
					gModel.setFrom(cashewnutId);
					gModel.setOperation(GroupModel.OPERATION_CHANGED_AS_MEMBER);
					new Thread() {
						public void run() {
							try {
								SenderHandler.getInstance().sendGroup(gModel);
								boolean isChanged = false;
								int loopCount = 0;
								do {
									Thread.sleep(2000);
									GroupModel groupModel = DBGroupsType6Mapper.getInstance()
											.getGroup(storedGModel.getGroupId() + "", false);
									if (groupModel.getServerGroupId() != null
											&& !groupModel.getServerGroupId().equals("")) {
										isChanged = groupModel.getAdmins().length() < size;
										if (isChanged) {
											break;
										}
									}
								} while (!isChanged && loopCount++ < 5);
								if (isChanged) {
									GroupModel groupModel = Helper.getGroupDetails(adapterGroupId);
									if (groupModel != null) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												setData();
												getLayout();
											}
										});
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public void enableOrDisableFeature(final boolean wantToEnable, String adapterGroupId) {
		try {
			if (!CashewnutApplication.isNetworkConnected()) {
				return;
			}

			if (adapterGroupId != null && !adapterGroupId.trim().equalsIgnoreCase("")) {
				final GroupModel storedGModel = DBGroupsType6Mapper.getInstance().getGroup(adapterGroupId, true);
				if (storedGModel != null && storedGModel.getGroupId() > -1) {
					final String oldFeature = storedGModel.getFeature().toString();
					final GroupModel gModel = new GroupModel();
					gModel.setGroupId(storedGModel.getGroupId());
					gModel.setServerGroupId(adapterGroupId);
					gModel.setFrom(cashewnutId);
					JSONArray features = new JSONArray();
					if (wantToEnable) {
						features.put("2");
						features.put("4");
						gModel.setOperation(GroupModel.OPERATION_FEATURE_ADDED);
						gModel.setFeature(features);
					} else {
						features.put("2");
						gModel.setOperation(GroupModel.OPERATION_FEATURE_REMOVED);
						gModel.setFeature(features);
					}

					new Thread() {
						public void run() {
							try {
								SenderHandler.getInstance().sendGroup(gModel);
								boolean isChanged = false;
								int loopCount = 0;
								do {
									Thread.sleep(2000);
									GroupModel groupModel = DBGroupsType6Mapper.getInstance()
											.getGroup(storedGModel.getGroupId() + "", false);
									if (groupModel.getServerGroupId() != null
											&& !groupModel.getServerGroupId().equals("")) {
										isChanged = !groupModel.getFeature().toString().equals(oldFeature);
										if (isChanged) {
											setData();
											if (wantToEnable) {

											} else {

											}
											break;
										}
									}
								} while (!isChanged || loopCount++ < 5);
								isChange = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disableFeature(final boolean isChecked) {
		try {
			feature4.setSelected(!isChecked);
			root1.setCenter(null);
			root1.setCenter(feature4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void cleanContactList() {
		contactListView.getItems().clear();
		contactObservableList.clear();
		contactList.clear();
	}

	public String changeGroupIcon() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("GIF", "*.gif"),
				new FileChooser.ExtensionFilter("BMP", "*.bmp"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			return downloadFile(selectedFile);
		} else {
			return null;
		}

	}

	public String downloadFile(File file) throws IOException {
		String extension = "";
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		String absolutePath = Helper.getAbsolutePath("group_profile");
		if (!absolutePath.endsWith("//")) {
			absolutePath = absolutePath + "//";
		}
	
		String sourceFilePath = absolutePath + adapterGroupId;
		File newFile = new File(sourceFilePath);
		if (newFile.exists()||newFile.getName().startsWith(adapterGroupId)) {
			newFile.delete();
		}
		copyFile(file, newFile);
		return sourceFilePath;
	}

	public static void copyFile(File oldLocation, File newLocation) throws IOException {
		if (oldLocation.exists()) {
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(oldLocation));
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newLocation, false));
			try {
				byte[] buff = new byte[8192];
				int numChars;
				while ((numChars = reader.read(buff, 0, buff.length)) != -1) {
					writer.write(buff, 0, numChars);
				}
			} catch (IOException ex) {
				throw new IOException(
						"IOException when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
			} finally {
				try {
					if (reader != null) {
						writer.close();
						reader.close();
					}
				} catch (IOException ex) {
				}
			}
		} else {
			throw new IOException("Old location does not exist when transferring " + oldLocation.getPath() + " to "
					+ newLocation.getPath());
		}
	}
	public void updateView(String latestHeaderThreadId) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				List<HeaderModel> headerModelList = DBHeaderMapper.getInstance()
						.getLatestMessageHeadersByGroupId(latestHeaderThreadId);
				if (headerModelList.size() > 0) {
					HeaderModel headerModel = headerModelList.get(0);
					// fire to list
					((CashewnutActivity) this.conversationView).processMessage(headerModel, MessageModel.ACTION_UPDATE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
