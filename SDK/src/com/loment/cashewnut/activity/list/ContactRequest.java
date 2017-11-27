package com.loment.cashewnut.activity.list;
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
import javafx.concurrent.WorkerStateEvent;
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
import com.loment.cashewnut.activity.controller.UserDetailsForm;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsType6Mapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.sender.SenderHandler;

public class ContactRequest extends Stage implements Initializable {

	@FXML
	AnchorPane contactsAnchorPane;
	@FXML
	BorderPane contactsRequestBorderPane;
	@FXML
	ToolBar toolBar;
	@FXML
	Label contactsRequest;
	@FXML
	TextField groupTextField;
	@FXML
	Label groupLabel;
	@FXML
	HBox hbox;
	@FXML
	private Button closeButton;
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
	
	public ContactRequest() {
		super();
	}

	public ContactRequest(Stage owner, double x, double y) {
		super();
		try {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fx_contact_request.fxml"));
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
			ContactRequest controller = fl.<ContactRequest> getController();

		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger(MessageSettingsDialog.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//if (contactsAnchorPane != null) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			contactsRequestBorderPane
					.setStyle("-fx-border-width:4pt; -fx-border-color:"
							+ AppConfiguration.getBorderColour() + ";");
			toolBar.setStyle("-fx-background-color:"
					+ AppConfiguration.getBorderColour() + ";");

			double r = 10.0;
			closeButton.setShape(new Circle(r));
			closeButton.setMinSize(2 * r, 2 * r);
			closeButton.setMaxSize(2 * r, 2 * r);
			

			closeImageView.setImage(new Image(ContactRequest.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "close.png")));
			closeImageView.getStyleClass().add("closeButtonCss");
			closeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					close1();
				}
			});

	
			createContactListComponents();
		//}
		//setStrings();
			
			if (contactsRequest != null)
				contactsRequest.setText("  "
						+ AppConfiguration.appConfString.contactRequests);
	}

	public void close1() {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		Stage stage = (Stage) contactsAnchorPane.getScene().getWindow();
		stage.close();
	}

	// =========================== Contact List ==========================
	boolean isContactRightClick = false;
	DBContactsMapper profileMapper = DBContactsMapper.getInstance();
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
								double r = 10.0;
								Label label = new Label();
								Label members = new Label();
								ImageView delIcon = ConversationsViewRenderer.getImageView(
										new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_request_cancel.png")),
										15);
								ImageView addIcon = ConversationsViewRenderer.getImageView(
										new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_request_ok.png")),
										15);
								try {

									String cid = t.getCashewnutId();
									String name = "";
									label = new Label(memberString);
									ContactsModel contactsModel = profileMapper
											.getContact(cid,1);
									name = contactsModel.getFirstName();
									if (name != null
											&& name.trim().length() > 0) {
										memberString = memberString + name;
										label = new Label(memberString);
										members = new Label(memberString+ t.getNotes());
									} else {
										memberString = memberString + cid;
										label = new Label(memberString);
										members = new Label(contactsModel.getNotes());
									}
								} catch (Exception e) {
								}
								ImageView user_icon_single = ConversationsViewRenderer
										.resize1(
												AppConfiguration.getUserIconPath()
														+ "ic_action_user.png",
												32);
								Label lblUserIcon = new Label(" ");
								Button addContact= new Button();
								addContact.setShape(new Circle(r));
								addContact.setMinSize(4 * r, 4 * r);
								addContact.setMaxSize(4 * r, 4 * r);
								addContact.setGraphic(addIcon);
								addContact.setOnMouseClicked(new EventHandler<MouseEvent>(){
									 
							          @Override
							          public void handle(MouseEvent arg0) {
							      		DBLomentDataMapper lomentDataMapper = DBLomentDataMapper
												.getInstance();
										LomentDataModel lomentDataModel = lomentDataMapper
												.getLomentData();
										String uName = lomentDataModel.getLomentId();
										String pWord = lomentDataModel.getPassword();
										UserDetailsForm service = new UserDetailsForm();
										service.setUseName(uName);
										service.setPassword(pWord);
										service.setCashewId(t.getCashewnutId());
										service.setStatus(1);
										service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
											@Override
											public void handle(WorkerStateEvent t) {
												boolean response = (boolean) t.getSource()
														.getValue();
												if (response) {
													addDataToContactList();
													//close1();
												} else {
												
												}
											}
										});
										service.start();
							        	  //profileMapper.update(t.cashewnutId, t.firstName, t.phone, t.emailId, 0);							      
							        	  	
							          }
							 
							      });
								Button deleteContact= new Button();
								deleteContact.setShape(new Circle(r));
								deleteContact.setMinSize(4 * r, 4 * r);
								deleteContact.setMaxSize(4 * r, 4 * r);
								deleteContact.setGraphic(delIcon);
								deleteContact.setOnMouseClicked(new EventHandler<MouseEvent>(){
									 
							          @Override
							          public void handle(MouseEvent arg0) {
							             
							        
							        	  profileMapper.update(t.cashewnutId, t.firstName, t.phone, t.emailId, 2);							      
							        	  addDataToContactList();					         
							          }
							 
							      });
							     									
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
								HBox buttonPanel=new HBox(addContact,deleteContact);
								buttonPanel.setSpacing(10);
								buttonPanel.setAlignment(Pos.CENTER);
								HBox mainPanel = new HBox(
										lblUserIcon1, cell,buttonPanel);
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
			ArrayList groupList1 = profileMapper.getRequestContactNameAndIDList();
			System.out.println("requestList"+ groupList1);
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
