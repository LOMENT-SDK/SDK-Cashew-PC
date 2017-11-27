/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

/**
 *
 * @author ajay
 */
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsType6Mapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.sender.SenderHandler;

/**
 *
 * @author sekhar
 */
public class AddGroup extends Stage implements Initializable {

	@FXML
	AnchorPane contactsAnchorPane;

	@FXML
	BorderPane addGroupBorderPane;

	@FXML
	ToolBar toolBar;

	@FXML
	TextField groupTextField;
	@FXML
	Label groupLabel, groupLabel1;
	@FXML
	HBox hbox;
	@FXML
	private Button closeButton;
	@FXML
	private Button groupAddButton;
	@FXML
	private ImageView closeImageView;
	String gName = null;
	// contact List components
	ListView<Map.Entry<String, ContactsModel>> contactListView = null;
	List<Map.Entry<String, ContactsModel>> contactList;

	ObservableList<Map.Entry<String, ContactsModel>> contactObservableList = null;
	String bgColour = "-fx-background-color: transparent;";
	ArrayList<GroupModel> listitems = new ArrayList<GroupModel>();
	ArrayList<GroupModel> resultItems = new ArrayList<GroupModel>();
	ArrayList<GroupModel> groupsList;
	ArrayList<GroupModel> arraylist = null;
	// ListAdapter listAdapter;
	String cashewnutId = "";
	Vector<ContactsModel> selectedHeaders = new Vector<ContactsModel>();
	private static String groupServerId = "";

	public AddGroup() {
		super();
	}

	public AddGroup(Stage owner, double x, double y, String groupServerId) {
		super();
		try {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_addgroup.fxml"));
			fl.load();
			Parent root = fl.getRoot();
			Scene scene1 = new Scene(root);
			scene1.getStylesheets().add(
					ForwardMessage.class.getResource("Style.css")
							.toExternalForm());
			this.setScene(scene1);
			this.initStyle(StageStyle.UNDECORATED);
			this.initModality(Modality.WINDOW_MODAL);
			this.initStyle(StageStyle.TRANSPARENT);
			this.centerOnScreen();
			this.setResizable(false);

			AddGroup controller = fl.<AddGroup> getController();
			controller.initData(groupServerId);

		} catch (Exception ex) {
			Logger.getLogger(MessageSettingsDialog.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	boolean isCreateNewGroup = true;

	void initData(String groupId) {
		groupServerId = groupId;
		if (groupServerId != null && !groupServerId.trim().equalsIgnoreCase("")) {
			groupTextField.setVisible(false);
			isCreateNewGroup = false;
			groupAddButton.setText(AppConfiguration.appConfString.add_member);
			addGroupBorderPane.setCenter(null);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (contactsAnchorPane != null) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			addGroupBorderPane
					.setStyle("-fx-border-width:4pt; -fx-border-color:"
							+ AppConfiguration.getBorderColour() + ";");
			toolBar.setStyle("-fx-background-color:"
					+ AppConfiguration.getBorderColour() + ";");

			double r = 10.0;
			closeButton.setShape(new Circle(r));
			closeButton.setMinSize(2 * r, 2 * r);
			closeButton.setMaxSize(2 * r, 2 * r);
			groupAddButton.getStyleClass().add("creategroupButtonCss");

			closeImageView.setImage(new Image(AddGroup.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "close.png")));
			closeImageView.getStyleClass().add("closeButtonCss");
			closeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					close1();
				}
			});

			groupAddButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					if (isCreateNewGroup) {
						gName = groupTextField.getText().trim();
						if (gName == null || gName.trim().equals("")) {
							//System.out.println("Enter the name");
							groupLabel.setVisible(true);
							groupLabel.setText("*"+ AppConfiguration.appConfString.group_name_hint);
						} else if (selectedHeaders == null
								|| selectedHeaders.size() < 1) {

							groupLabel.setVisible(true);
							groupLabel.
							setText("*"+ AppConfiguration.appConfString.select_one_participent);
						} else {
							createGroup();
							close1();
						}
					} else {
						if (selectedHeaders == null
								|| selectedHeaders.size() < 1) {
							groupLabel.setVisible(true);
							groupLabel
									.setText("*"+ AppConfiguration.appConfString.select_one_participent);
						} else {
							addMember();
							close1();
						}
					}
				}
			});
			createContactListComponents();
		}
		setStrings();
	}

	private void createGroup() {
		if (CashewnutApplication.isNetworkConnected()) {
			try {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				DBAccountsMapper accountsMapper = DBAccountsMapper
						.getInstance();
				AccountsModel accountsModel = accountsMapper.getAccount();
				cashewnutId = accountsModel.getCashewnutId();

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			JSONArray members = new JSONArray();
			members.put(cashewnutId);
			for (int i = 0; i < selectedHeaders.size(); i++) {
				if (getStringIndexOfJSONArray(members, selectedHeaders.get(i)
						.getCashewnutId()) < 0) {
					members.put(selectedHeaders.get(i).getCashewnutId());
				}
			}
			final GroupModel gModel = new GroupModel();
			gModel.setGroupName(gName);
			gModel.setOwner(cashewnutId);
			gModel.setMembers(members);
			gModel.setFrom(cashewnutId);
			try {
				int id = (int) DBGroupsType6Mapper.getInstance().insert(gModel);
				gModel.setGroupId(id);
				gModel.setOperation(GroupModel.OPERATION_CREATE_GROUP);
				SenderHandler.getInstance().sendGroup(gModel);
				groupServerId = "";
				int loopCount = 0;
				do {
					Thread.sleep(2000);
					groupServerId = getGroupDetails(id + "");
				} while ((groupServerId == null || groupServerId.equals(""))
						&& loopCount++ < 8);

				if (groupServerId != null && !groupServerId.equals("")) {
					ArrayList<String> receipients = new ArrayList<String>();

					try {
						JSONArray members1 = gModel.getMembers();
						for (int i = 0; i < members1.length(); i++) {
							receipients.add(members1.getString(i));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// intent.putExtra("from", cashewnutId);
					// intent.putExtra("to", receipients);
					// intent.putExtra("threadId", groupServerId);
					// intent.putExtra("groupId", groupServerId);
					// return intent;
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}
	
	private void setStrings() {
		if (groupAddButton != null)
		groupAddButton.setText(AppConfiguration.appConfString.creategroup);
		if (groupTextField != null)
		groupTextField.setPromptText(AppConfiguration.appConfString.creategroup);
	}

	public boolean addMember() {
		try {
			if (!CashewnutApplication.isNetworkConnected()) {
				//System.out.println("network_unavailable");
				return false;
			}
			CashewnutActivity.idleTime = System.currentTimeMillis();
			if (groupServerId != null
					&& !groupServerId.trim().equalsIgnoreCase("")) {
				final GroupModel storedGModel = DBGroupsType6Mapper.getInstance()
						.getGroup(groupServerId, true);

				if (storedGModel != null && storedGModel.getGroupId() > -1) {
					final int size = storedGModel.getMembers().length();
					JSONArray members = new JSONArray();
					for (int i = 0; i < selectedHeaders.size(); i++) {
						if (getStringIndexOfJSONArray(members, selectedHeaders
								.get(i).getCashewnutId()) < 0) {
							members.put(selectedHeaders.get(i).getCashewnutId());
						}
					}
					if (members.length() < 1) {
						return true;
					}
					final GroupModel gModel = new GroupModel();
					gModel.setGroupId(storedGModel.getGroupId());
					gModel.setServerGroupId(groupServerId);
					gModel.setMembers(members);
					gModel.setFrom(cashewnutId);
					gModel.setOperation(GroupModel.OPERATION_ADD_MEMBER);
					try {
						SenderHandler.getInstance().sendGroup(gModel);
						boolean isChanged = false;
						int loopCount = 0;
						do {
							Thread.sleep(2000);
							GroupModel gModel1 = DBGroupsMapper.getInstance()
									.getGroup(storedGModel.getGroupId() + "",
											false);
							if (gModel1.getServerGroupId() != null
									&& !gModel1.getServerGroupId().equals("")) {
								isChanged = gModel1.getMembers().length() > size;
								if (isChanged) {
								}
							}
						} while (!isChanged && loopCount++ < 8);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getGroupId() {
		return groupServerId;
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

	private String getGroupDetails(final String group_id) {
		if (group_id != null && !group_id.trim().equalsIgnoreCase("")) {
			final GroupModel gModel = DBGroupsMapper.getInstance().getGroup(
					group_id, false);
			if (gModel.getServerGroupId() != null
					&& !gModel.getServerGroupId().equals("")) {
				return gModel.getServerGroupId();
			}
		}
		return null;
	}

	public void close1() {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		Stage stage = (Stage) contactsAnchorPane.getScene().getWindow();
		stage.close();
	}

	// =========================== Contact List ==========================
	boolean isContactRightClick = false;

	private ConversationPrimer getConversationPrimer(final HeaderModel header,
			String boxtype) {
		ConversationPrimer premier = new ConversationPrimer(header);
		premier.setStartDate(header.getCreationTime());
		premier.setBoxtype(boxtype);
		return premier;
	}

	private void createContactListComponents() {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		contactListView = new ListView();
		contactList = new ArrayList();
		contactObservableList = FXCollections.observableArrayList(contactList);
		contactsAnchorPane.getChildren().addAll(contactListView);
		contactListView.setPrefWidth(400);

		contactListView.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					public void handle(MouseEvent event) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						isContactRightClick = false;
						if (!event.isSecondaryButtonDown()) {
							isContactRightClick = true;
						}
					}
				});
		contactListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				isContactRightClick = false;
				if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
					isContactRightClick = true;
				}
			}
		});
		contactListView.setItems(contactObservableList);
		contactListView
				.setCellFactory(new Callback<ListView<Map.Entry<String, ContactsModel>>, ListCell<Map.Entry<String, ContactsModel>>>() {
					@Override
					public ListCell<Map.Entry<String, ContactsModel>> call(
							ListView<Map.Entry<String, ContactsModel>> p) {
						ListCell<Map.Entry<String, ContactsModel>> cell = new ListCell<Map.Entry<String, ContactsModel>>() {
							@Override
							protected void updateItem(
									Map.Entry<String, ContactsModel> t,
									boolean bln) {
								super.updateItem(t, bln);
								if (t != null && t.getValue() != null) {
									CashewnutActivity.idleTime = System.currentTimeMillis();
									ConversationPrimer premier;

									HBox cell = getGroupCell(t.getValue());
									setGraphic(cell);
								} else {
									setGraphic(null);
								}
							}

							private HBox getGroupCell(ContactsModel t) {
								DBContactsMapper profileMapper = DBContactsMapper
										.getInstance();
								String memberString = "";
								Label label = new Label();
								Label members = new Label();
								try {

									String cid = t.getCashewnutId();
									String name = "";
									label = new Label(memberString);
									ContactsModel contactsModel = profileMapper
											.getContact(cid,0);
									name = contactsModel.getFirstName();
									if (name != null
											&& name.trim().length() > 0) {
										memberString = memberString + name;
										label = new Label(memberString);
										members = new Label(cid);
									} else {
										memberString = memberString + cid;
										label = new Label(memberString);
										members = new Label("");
									}
								} catch (Exception e) {
								}
								ImageView user_icon_single = ConversationsViewRenderer
										.resize1(
												AppConfiguration.getUserIconPath()
														+ "ic_action_user.png",
												32);
								Label lblUserIcon = new Label(" ");
								CheckBox groupCheckBox = new CheckBox();
								groupCheckBox
										.selectedProperty()
										.addListener(
												(ObservableValue<? extends Boolean> observable,
														Boolean oldValue,
														Boolean newValue) -> {
													if (newValue) {
														if (!selectedHeaders
																.contains(t)) {
															selectedHeaders
																	.add(t);
//															System.out.println("selected Id: "
//																	+ t.getCashewnutId());
														}
													} else {
														selectedHeaders
																.remove(t);
//														System.out.println("un selected Id: "
//																+ t.getCashewnutId());
													}
												});
								// System.out
								// .println("CheckBox Action (selected:)");
								lblUserIcon.setGraphic(user_icon_single);

								CircularUserIcon lblUserIcon1 = new CircularUserIcon(
										"NS");
								Text text = (Text) lblUserIcon1.getChildren()
										.get(1);
								text.setText(memberString.charAt(0) + "");

								label.setId("custom-label-from");
								members.setId("custom-label-from");
								VBox cell = new VBox(label, members);
								cell.setSpacing(4);
								cell.setAlignment(Pos.CENTER_LEFT);
								cell.setPrefWidth(200);
								cell.setPadding(new Insets(0, 10, 0, 2));
								cell.setPrefHeight(60);

								HBox mainPanel = new HBox(groupCheckBox,
										lblUserIcon1, cell);
								mainPanel.setPadding(new Insets(0, 10, 0, 2));
								mainPanel.setAlignment(Pos.CENTER_LEFT);
								return mainPanel;
							}
						};
						return cell;
					}
				});
		addDataToContactList();
	}

	void addDataToContactList() {
		try {
			cleanContactList();
			DBContactsMapper profileMapper = DBContactsMapper.getInstance();
			ArrayList groupList1 = profileMapper.getContactNameAndIDList();

			if (groupList1 != null) {
				// Sorting
				Collections.sort(groupList1, new Comparator<ContactsModel>() {
					@Override
					public int compare(ContactsModel groupModel1,
							ContactsModel groupModel2) {
						return groupModel1.getFirstName().compareTo(
								groupModel2.getFirstName());
					}
				});
				ArrayList contactIds = new ArrayList();
				for (Object groupList11 : groupList1) {
					ContactsModel gModel = (ContactsModel) groupList11;
					if (contactIds.contains(gModel.getContactId())) {
						continue;
					}
					contactIds.add(gModel.getContactId());
					Map.Entry<String, ContactsModel> entry = new AbstractMap.SimpleEntry(
							gModel.getCashewnutId(), gModel);
					contactList.add(entry);
				}
				contactObservableList.setAll(contactList);
				contactListView.setItems(contactObservableList);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	void cleanContactList() {
		contactListView.getItems().clear();
		contactObservableList.clear();
		contactList.clear();
	}
}
