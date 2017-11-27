/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;

import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import org.json.JSONArray;

/**
 *
 * @author sekhar
 */
public class ForwardMessage extends Stage implements Initializable {
	@FXML
	BorderPane borderPane;
	
	@FXML
	ToolBar toolBar;
	@FXML
	TabPane forwardTabPane;
	@FXML
	AnchorPane contactsAnchorPane;
	@FXML
	AnchorPane groupsAnchorPane;
	@FXML
	private Button closeButton;
	@FXML
	private ImageView closeImageView;
	@FXML
	private Tab contactsTab;
	@FXML
	private Tab groupsTab;

	

	StackPane contactPane = new StackPane();
	StackPane groupPane = new StackPane();
	// contact List components
	ListView<Map.Entry<String, ContactsModel>> contactListView = null;
	List<Map.Entry<String, ContactsModel>> contactList;
	ObservableList<Map.Entry<String, ContactsModel>> contactObservableList = null;
	String bgColour = "-fx-background-color: transparent;";

	// Group List components
	ListView<Map.Entry<String, GroupModel>> groupListView = null;
	List<Entry<String, GroupModel>> groupList;
	ObservableList<Map.Entry<String, GroupModel>> groupObservableList = null;

	static int messageForwardType = -1;
	static String messageForwardTo = "";

	public ForwardMessage() {
		super();
	}

	public ForwardMessage(Stage owner, double x, double y) {
		super();
		try {
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_forward.fxml"));
			fl.load();
			Parent root = fl.getRoot();
			 //ScrollPane pane = new ScrollPane();
			Scene scene1 = new Scene(root);
			scene1.getStylesheets().add(
					ForwardMessage.class.getResource("Style.css")
							.toExternalForm());
			this.setScene(scene1);
			this.initStyle(StageStyle.UNDECORATED);
			this.initModality(Modality.WINDOW_MODAL);
			this.centerOnScreen();
			this.setResizable(true);

		} catch (Exception ex) {
			Logger.getLogger(MessageSettingsDialog.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		messageForwardType = -1;
		messageForwardTo = "";
		if (forwardTabPane != null) {
			
			borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
					+ AppConfiguration.getBorderColour() + ";");
			toolBar.setStyle("-fx-background-color:"+AppConfiguration.getBorderColour()+";");
			
			double r = 10.0;
			closeButton.setShape(new Circle(r));
			closeButton.setMinSize(2 * r, 2 * r);
			closeButton.setMaxSize(2 * r, 2 * r);
			closeImageView.setImage(new Image(ForwardMessage.class
					.getResourceAsStream(AppConfiguration.getIconPath() + "close.png")));
			closeImageView.getStyleClass().add("closeButtonCss");
			closeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					close1();
				}
			});
			createContactListComponents();
			createGroupListComponents();
		}
		setStrings();
	}
	
	private void setStrings() {
		if (contactsTab != null)
			contactsTab.setText(AppConfiguration.appConfString.conversation_contacts);
		if (groupsTab != null)
		groupsTab.setText(AppConfiguration.appConfString.conversation_groups);
	}

	public void close1() {
		Stage stage = (Stage) forwardTabPane.getScene().getWindow();
		stage.close();
	}

	// =========================== group List ==========================
	// ========================================================================
	boolean isGroupRightClick;

	private void createGroupListComponents() {
		groupListView = new ListView();
		groupList = new ArrayList();
		groupObservableList = FXCollections.observableArrayList(groupList);
		groupsAnchorPane.getChildren().add(groupListView);
		groupListView.setPrefWidth(290);
borderPane.getChildren().add(groupsAnchorPane);
ChangeListener changeListener = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				final Map.Entry<String, GroupModel> entry = (Map.Entry<String, GroupModel>) newValue;
				if (entry != null) {
					if (isGroupRightClick) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								messageForwardType = 1;
								messageForwardTo = entry.getValue()
										.getServerGroupId();
								close1();
							}
						});
					}
				}
			}
		};

		groupListView.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						isGroupRightClick = false;
						if (!event.isSecondaryButtonDown()) {
							isGroupRightClick = true;
						}
					}
				});

		groupListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				isGroupRightClick = false;
				if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
					isGroupRightClick = true;
				}
			}
		});

		groupListView.setItems(null);
		groupListView.setItems(groupObservableList);
		groupListView.getSelectionModel().selectedItemProperty()
				.addListener(changeListener);

		groupListView
				.setCellFactory(new Callback<ListView<Map.Entry<String, GroupModel>>, ListCell<Map.Entry<String, GroupModel>>>() {
					@Override
					public ListCell<Map.Entry<String, GroupModel>> call(
							ListView<Map.Entry<String, GroupModel>> p) {
						ListCell<Map.Entry<String, GroupModel>> cell = new ListCell<Map.Entry<String, GroupModel>>() {
							@Override
							protected void updateItem(
									Map.Entry<String, GroupModel> t, boolean bln) {
								super.updateItem(t, bln);
								if (t != null) {
									HBox cell = getGroupCell(t.getValue());
									setGraphic(cell);
								} else {
									setGraphic(null);
								}
							}

							private HBox getGroupCell(GroupModel t) {
								DBContactsMapper profileMapper = DBContactsMapper
										.getInstance();
								JSONArray membersJson = t.getMembers();
								String memberString = "";

								for (int i = 0; i < membersJson.length(); i++) {
									try {
										String cid = membersJson.get(i)
												.toString();
										String name = "";
										ContactsModel contactsModel = profileMapper
												.getContact(cid,0);
										name = contactsModel.getFirstName();
										if (name != null
												&& name.trim().length() > 0) {
											if (i > 0) {
												memberString = memberString
														+ ", ";
											}
											memberString = memberString + name;
										} else {
											if (i > 0) {
												memberString = memberString
														+ ", ";
											}
											memberString = memberString + cid;
										}
									} catch (Exception e) {
									}
								}
								ImageView user_icon_single = ConversationsViewRenderer
										.resize1(
												AppConfiguration.getUserIconPath() + "ic_action_users_green.png",
												48);
								Label label = new Label(t.getGroupName());
								label.setId("custom-label-from");

								Label members = new Label(memberString);
								Label lblUserIcon = new Label(" ");
								lblUserIcon.setGraphic(user_icon_single);
								members.setId("custom-label-from");

								VBox cell = new VBox(label, members);
								cell.setSpacing(2);
								cell.setAlignment(Pos.CENTER_LEFT);
								cell.setPrefWidth(200);
								cell.setPadding(new Insets(0, 10, 0, 2));
								cell.setPrefHeight(50);

								HBox mainPanel = new HBox(lblUserIcon, cell);
								mainPanel.setPadding(new Insets(1, 2, 1, 2));
								return mainPanel;
							}
						};
						return cell;
					}
				});
		addDataToGroupList();
	}

	void addDataToGroupList() {
		try {
			cleanGroupList();
			DBGroupsMapper profileMapper = DBGroupsMapper.getInstance();
			ArrayList groupList1 = profileMapper.getGroupsList();
			if (groupList1 != null) {

				// Sorting
				Collections.sort(groupList1, new Comparator<GroupModel>() {
					@Override
					public int compare(GroupModel groupModel1,
							GroupModel groupModel2) {
						return groupModel1.getGroupName().compareTo(
								groupModel2.getGroupName());
					}
				});

				ArrayList groupIds = new ArrayList();
				for (Object groupList11 : groupList1) {
					GroupModel gModel = (GroupModel) groupList11;
					if (groupIds.contains(gModel.getServerGroupId())
							|| gModel.getGroupName().trim().equals("")) {
						continue;
					}
					groupIds.add(gModel.getServerGroupId());
					Map.Entry<String, GroupModel> entry = new AbstractMap.SimpleEntry(
							gModel.getServerGroupId(), gModel);
					groupList.add(entry);
				}

				groupObservableList.setAll(groupList);
				groupListView.setItems(groupObservableList);

			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	void cleanGroupList() {
		groupListView.getItems().clear();
		groupObservableList.clear();
		groupList.clear();
	}

	// =========================== Contact List ==========================
	// ========================================================================
	boolean isContactRightClick = false;

	private void createContactListComponents() {
		contactListView = new ListView();
		contactList = new ArrayList();
		contactObservableList = FXCollections.observableArrayList(contactList);
		contactsAnchorPane.getChildren().add(contactListView);
		contactListView.setPrefWidth(287);
		//contactListView.setMaxHeight(borderPane.getMaxHeight());
	borderPane.getChildren().add(contactsAnchorPane);
		

		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				final Map.Entry<String, ContactsModel> entry = (Map.Entry<String, ContactsModel>) newValue;
				if (entry != null) {
					if (isContactRightClick) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								messageForwardType = 0;
								messageForwardTo = entry.getValue()
										.getCashewnutId();
								close1();
							}
						});
					}
				}
			}
		};

		contactListView.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					@Override
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
		
		contactListView.getSelectionModel().selectedItemProperty()
				.addListener(changeListener);
		 

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
												AppConfiguration.getUserIconPath() + "ic_action_user.png",
												32);
								Label lblUserIcon = new Label(" ");
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

								HBox mainPanel = new HBox(lblUserIcon1, cell);
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

	public int getMessageForwardType() {
		return messageForwardType;
	}

	public String getMessageForwardTo() {
		return messageForwardTo;
	}
}
