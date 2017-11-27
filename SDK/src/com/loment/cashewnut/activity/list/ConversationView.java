package com.loment.cashewnut.activity.list;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat.Field;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.media.Player;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.controlsfx.control.Notifications;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.activity.controller.NetworkConnection;
import com.loment.cashewnut.activity.controller.ThreadIdGenerator;
import com.loment.cashewnut.activity.controller.ValidatePasswordTask;
import com.loment.cashewnut.activity.view.javafx.LoginFormJfx;
import com.loment.cashewnut.connection.ReceipientConnection;
import com.loment.cashewnut.connection.amqp.RPCClientSender;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.MessageStore;
import com.loment.cashewnut.database.ProfileStore;
import com.loment.cashewnut.database.RecepientStore;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBHeaderMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.model.SettingsModel;
import com.loment.cashewnut.receiver.ReceiverHandler;
import com.loment.cashewnut.sender.SenderHandler;
import com.loment.cashewnut.sthithi.connection.SubscriptionHandler;
import com.loment.cashewnut.util.ContentType;
import com.loment.cashewnut.util.Helper;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.CursorManager;
import com.sun.webkit.WebPage;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 * @author sekhar
 */
public class ConversationView extends CashewnutActivity implements Initializable {

	@FXML
	private Label dateLabel;
	@FXML
	private Label label;
	Map<String, Circle> cache1 = CashewnutActivity.group_cache1;
	String cashewnutId = "";
	String lomentId = "";
	String password = "";
	public boolean searching;
	private HashMap<String, Long> threadIds = new HashMap<String, Long>();
	private HashMap<String, Long> Schedulethread = new HashMap<String, Long>();
	private static String threadId;
	Vector<String> receipients = new Vector<String>();
	// conversation list components
	ListView<Map.Entry<String, ConversationPrimer>> listView = null;
	List<Entry<String, ConversationPrimer>> list = null;
	List<Entry<String, ConversationPrimer>> list1 = null;
	ObservableList<Map.Entry<String, ConversationPrimer>> myObservableList = null;

	Vector<ConversationPrimer> selectedHeaders = new Vector<ConversationPrimer>();
	// conversation view components
	ListView<HeaderModel> messageListView = null;
	ArrayList<HeaderModel> messageList;
	ObservableList<HeaderModel> myMessageObservableList = null;

	// Group List components
	ListView<Map.Entry<String, GroupModel>> groupListView = null;
	List<Entry<String, GroupModel>> groupList;
	ObservableList<Map.Entry<String, GroupModel>> groupObservableList = null;

	// contact List components
	ListView<Map.Entry<String, ContactsModel>> contactListView = null;
	List<Entry<String, ContactsModel>> contactList;
	ObservableList<Map.Entry<String, ContactsModel>> contactObservableList = null;

	String bgColour = "-fx-background-color: transparent;";
	TextArea textArea = new TextArea();
	private final HashMap<String, String> attachmentsMap = new HashMap<String, String>();
	private final MessageControlParameters controlParameters = new MessageControlParameters();
	boolean isRightClick = false;
	ProgressIndicator myProgressIndicator1 = null;
	HBox progressPairPane = null;
	boolean isEmoji = true;
	String rightImage = ConversationView.class
			.getResource(AppConfiguration.getBackgroundImagePath() + "background2.png").toExternalForm();
	@FXML
	private HBox toolbarButtonsHBox;
	boolean isMultiSelected = false;
	@FXML
	CheckBox chatTitleBarCheckBox;
	HashMap<String, Image> sEmojisMap = new HashMap();
	private TextArea composeTextArea = new TextArea();
	DropShadow shadow = new DropShadow();
	Button cancel = new Button("");
	ImageView user_icon_single = null;
	int emojiCount = 0;
	boolean status=false;
	String backSpaceEmoji = "";
	List<String> backSpaceList = new LinkedList();
	List<String> imageList = new LinkedList();
	List<String> backSpaceEmojiList = new LinkedList();
	String trimedHtml = "";
	boolean isBackSpace = true;
	String orginal = "";
	String sb = "";
	static Notifications nf =null;
	public void init1() throws Exception {
		try {
			htmlEditor.setVisible(false);
			getComposeLayout();
			ConversationView.currentActivity = this;
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel accountsModel = accountsMapper.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
			checkSubscription();
			LomentDataModel lomentDataModel = DBLomentDataMapper.getInstance().getLomentData();
			lomentId = lomentDataModel.getLomentId();
			password = lomentDataModel.getPassword();
			createConversationMessageListComponents();
			createConversationMessageViewComponents();
			createGroupListComponents();
			createContactListComponents();
			getConversationMessageListFromDBTask();
			CashewnutActivity.recentEmoji = true;
			listView.setStyle("-fx-background-image: url(\"" + rightImage + "\");");
			messageListView.setStyle("-fx-background-image: url(\"" + rightImage + "\");");
			groupListView.setStyle("-fx-background-image: url(\"" + rightImage + "\");");
			contactListView.setStyle("-fx-background-image: url(\"" + rightImage + "\");");

			// control parms
			controlParameters.setPriority(MessageControlParameters.PRIORITY_INDICATOR_GRAY + "");
			controlParameters.setExpiry("-1");
			controlParameters.setRestricted("-1");
			controlParameters.setMessageAck("-1");
			controlParameters.setMessageSchedule(-1);
			setLeftPane();
			setRightPane();

			try {
				idleTime = System.currentTimeMillis();
				ControlMessageOptions.getInstance().startMessageExpiraryTimer();
				// ControlMessageOptions.getInstance().startIdleTimer();
				checkPassword(lomentId, password, null, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	SideBar sidebar = null;

	private void setRightPane() {
		if (viewRightPane != null) {
			viewRightPane.autosize();
			messageListView.autosize();
			ConversationsearchButton.setVisible(true);
			conversationSearchHBox.setVisible(true);
			sidebar = new SideBar(rightBorderPane.getWidth(), chatTitleBarLabel);
			sidebar.setCenter(messageListView);
			messageBorderPane.setCenter(sidebar);
			messageBorderPane.setStyle(bgColour);
			rightBorderPane.setCenter(messageBorderPane);
			rightBorderPane.setStyle(bgColour);
			viewRightPane.setStyle(bgColour);
			isRightProfilePane = false;
		}
	}

	MainMessageSettings controller = null;
	boolean isRightProfilePane = false;

	private BorderPane createSidebarContent() {
		BorderPane lyricPane = new BorderPane();
		try {

			HeaderModel header = null;
			if (myMessageObservableList.size() > 0) {
				header = myMessageObservableList.get(0);
			}

			String adapterFrom1 = "";
			String adapterGroupId = "";
			ArrayList<String> adapterToVector = new ArrayList<String>();

			switch (conversatonType) {
			case 1:
				if (conversationGroupId != null && !conversationGroupId.trim().equals("")) {
					adapterGroupId = conversationGroupId.trim();
				} else {
					adapterFrom1 = conversationFrom;
					for (int i = 0; i < conversationReceipients.size(); i++) {
						adapterToVector.add(conversationReceipients.get(i));
					}
				}
				break;
			case 2:
				adapterGroupId = conversationGroupId.trim();
				break;
			case 3:
				adapterFrom1 = conversationFrom;
				adapterToVector.add(cashewnutId);
			}
			isRightProfilePane = true;
			setComposeBorderPane(false);
			GroupModel gModel = null;

			if (adapterGroupId != null && !adapterGroupId.trim().equals("")) {
				DBGroupsMapper groupMapper = DBGroupsMapper.getInstance();
				gModel = groupMapper.getGroup(adapterGroupId, true);
			}

			if (gModel != null) {
				Stage stage = (Stage) toolBar.getScene().getWindow();
				double width = viewRightPane.getWidth();
				double height = viewRightPane.getHeight();

				GroupActivity ProfileActivity = new GroupActivity(this, adapterGroupId, stage, splitPane, width,
						height);
				multiSelectButton.setVisible(false);
				ConversationsearchButton.setVisible(false);
				conversationSearch.setVisible(false);
				ConversationSearchAnchorPane.setVisible(false);
				rightOptionHBox.setVisible(false);
				conversationSearchHBox.setVisible(false);
				messageBorderPane.setCenter(null);
				messageBorderPane.setCenter(ProfileActivity.getLayout());
				chatTitleBarLabel.setText(AppConfiguration.appConfString.conversation_groupinfo);
			} else {
				ProfileActivity ProfileActivity = new ProfileActivity(adapterToVector, adapterFrom1, adapterGroupId);
				multiSelectButton.setVisible(false);
				ConversationsearchButton.setVisible(false);
				conversationSearch.setVisible(false);
				ConversationSearchAnchorPane.setVisible(false);
				rightOptionHBox.setVisible(false);
				messageBorderPane.setCenter(null);
				messageBorderPane.setCenter(ProfileActivity.getLayout());
				chatTitleBarLabel.setText(AppConfiguration.appConfString.conversation_contactinfo);
			}
		} catch (Exception ex) {
			Logger.getLogger(ConversationView.class.getName()).log(Level.SEVERE, null, ex);
		}
		return lyricPane;
	}

	private void setStrings() {
		if (dateLabel != null)
			dateLabel.setText(AppConfiguration.appConfString.conversation_date);

		if (label != null)
			label.setText(AppConfiguration.appConfString.conversation_label);

		if (leftListContentLabel != null)
			leftListContentLabel.setText(AppConfiguration.appConfString.conversation_chat_list);

		if (composeTextArea != null)
			composeTextArea.setPromptText(AppConfiguration.appConfString.compose_type_message);
		if (search != null)
			search.setPromptText(AppConfiguration.appConfString.contact_search + "...");
		if (conversationSearch != null)
			conversationSearch.setPromptText(AppConfiguration.appConfString.contact_search + "...");
	}

	private static final String HOVERED_BUTTON_STYLE = "-fx-background-color:\n"
			+ " linear-gradient(#e8f5fc, #7fe6e6 ),\n"
			+ " radial-gradient(center 60% -40%, radius 200%, #e8f5fc 45%, #7fe6e6  50%);\n"
			+ " -fx-background-radius: 0;\n" + " -fx-background-insets:0;\n"
			+ " -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.1) , 1, 0.0 , 0 , 0 );\n"
			+ " -fx-text-fill: #395306;";

	final BorderPane listBorderPane = new BorderPane();
	private void setLeftPane() {
		if (leftChatListBorderPane != null) {
			listView.autosize();
			setStyle(listImageButton);
			progressPairPane = new HBox();
			myProgressIndicator1 = new ProgressIndicator();
			myProgressIndicator1.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			progressPairPane.setVisible(false);
			myProgressIndicator1.setPrefSize(35, 35);
			myProgressIndicator1.setStyle("-fx-accent:" + AppConfiguration.getProgressColor());
			progressPairPane.getChildren().add(myProgressIndicator1);
			progressPairPane.setAlignment(Pos.BOTTOM_RIGHT);
			listBorderPane.setCenter(listView);
			addGroupButton.setVisible(false);
			leftListContentLabel.setVisible(true);
			contactsAnchorPane.setVisible(true);
			searchAnchorPane.setVisible(false);

			searchCloseButton.setVisible(false);
			searchButton.setVisible(false);
			searchButtonAnchorPane.setVisible(false);
			listSearchAnchorPane.setVisible(true);
			ListsearchButton.setVisible(true);

			ContactRequestButton.setVisible(true);
			GroupsearchButton.setVisible(false);
			leftChatListBorderPane.setCenter(listBorderPane);
			contactsAnchorPane.getChildren().add(progressPairPane);
			leftListChatListHBox.getChildren().add(progressPairPane);
			Region spacer1 = new Region();
			HBox.setHgrow(spacer1, Priority.ALWAYS);
			spacer1.setMinWidth(Region.USE_PREF_SIZE);
			leftListChatListHBox.getChildren().add(spacer1);
			leftListContentLabel.setId("custom-label-main-title");

			listImageButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					setStyle(listImageButton);

					if (listBorderPane.getCenter() != listView) {
						leftListContentLabel.setText(AppConfiguration.appConfString.conversation_chat_list);
						listBorderPane.setCenter(null);
						listBorderPane.setCenter(listView);
						addGroupButton.setVisible(false);
						searchAnchorPane.setVisible(false);
						searchButton.setVisible(false);
						searchButtonAnchorPane.setVisible(false);
						listSearchAnchorPane.setVisible(true);
						ListsearchButton.setVisible(true);

						ContactRequestButton.setVisible(true);
						GroupsearchButton.setVisible(false);
						searchCloseButton.setVisible(true);
						search.setVisible(false);
						leftListContentLabel.setVisible(true);
					}
					addRightConversationPane();
				}
			});
			writeImageButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					setStyle(writeImageButton);
					newCompose();
				}
			});
			contactImageButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();

					setStyle(contactImageButton);
					addDataToContactList();
					if (listBorderPane.getCenter() != contactListView) {
						leftListContentLabel.setVisible(true);
						searchAnchorPane.setVisible(false);
						listBorderPane.setCenter(null);
						listBorderPane.setCenter(contactListView);
						leftListContentLabel.setText(AppConfiguration.appConfString.conversation_contacts);
						addGroupButton.setVisible(true);
						searchButton.setVisible(true);
						searchButtonAnchorPane.setVisible(true);
						listSearchAnchorPane.setVisible(false);
						ListsearchButton.setVisible(false);
						ContactRequestButton.setVisible(false);
						GroupsearchButton.setVisible(false);
						searchCloseButton.setVisible(false);
						search.setVisible(false);
					}
					addRightConversationPane();
				}
			});
			groupImgeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();

					setStyle(groupImgeButton);
					addDataToGroupList();

					if (listBorderPane.getCenter() != groupListView) {
						leftListContentLabel.setVisible(true);
						searchAnchorPane.setVisible(false);
						listBorderPane.setCenter(null);
						listBorderPane.setCenter(groupListView);
						leftListContentLabel.setText(AppConfiguration.appConfString.conversation_group_list);
						addGroupButton.setVisible(true);
						addGroupButton.setVisible(true);
						GroupsearchButton.setVisible(true);
						listSearchAnchorPane.setVisible(false);
						ListsearchButton.setVisible(false);
						ContactRequestButton.setVisible(false);
						searchButton.setVisible(false);
						search.setVisible(false);
						searchButtonAnchorPane.setVisible(true);
					}
					addRightConversationPane();
				}
			});
			addGroupButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					if (listBorderPane.getCenter() == contactListView) {
						addContact();
					}
					if (listBorderPane.getCenter() == groupListView) {
						addGroup();
					}
				}
			});
			searchButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					progressPairPane.setVisible(false);
					searchAnchorPane.setVisible(true);
					search.clear();
					if (search.isVisible()) {
						leftListContentLabel.setVisible(true);
						search.setVisible(false);
						searchCloseButton.setVisible(false);
						searchAnchorPane.setVisible(false);
					} else {
						leftListContentLabel.setVisible(false);
						search.setVisible(true);
						searchCloseButton.setVisible(true);
					}
					if (listBorderPane.getCenter() == contactListView) {
						search.textProperty().addListener(new ChangeListener() {
							public void changed(ObservableValue observable, Object oldVal, Object newVal) {
								search((String) oldVal, (String) newVal);
							}
						});
					}

				}
			});
			GroupsearchButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					progressPairPane.setVisible(false);
					searchAnchorPane.setVisible(true);
					search.clear();
					if (search.isVisible()) {
						leftListContentLabel.setVisible(true);
						search.setVisible(false);
						searchCloseButton.setVisible(false);
						searchAnchorPane.setVisible(false);
					} else {
						leftListContentLabel.setVisible(false);
						search.setVisible(true);
						searchCloseButton.setVisible(true);
					}

					if (listBorderPane.getCenter() == groupListView) {
						search.textProperty().addListener(new ChangeListener() {
							public void changed(ObservableValue observable, Object oldVal, Object newVal) {

								if (isSearch == true) {
									isSearch = false;
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								searchGroup((String) oldVal, (String) newVal);

							}
						});
					}

				}
			});

			searchCloseButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					search.clear();

				}
			});

			ContactRequestButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					contactRequest();

				}
			});

			settingsImageButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						CashewnutActivity.close = false;
						setStyle(settingsImageButton);
						if (viewRightPane.getChildren().contains(rightBorderPane)) {

							MainMessageSettings settings = getMainSettingsLayout();

						}
					} catch (Exception ex) {

					}
				}

			});
			signoutImageButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					setStyle(signoutImageButton);
					logout();
				}
			});
		}
	}

	private void setStyle(Node node) {
		listImageButton.setStyle(bgColour);
		writeImageButton.setStyle(bgColour);
		contactImageButton.setStyle(bgColour);
		settingsImageButton.setStyle(bgColour);
		groupImgeButton.setStyle(bgColour);

		node.setStyle(HOVERED_BUTTON_STYLE);
	}

	private MainMessageSettings getMainSettingsLayout() throws IOException {
		if (CashewnutActivity.close && controller != null) {
			controller.closing();
			return null;
		}
		FXMLLoader loader = new FXMLLoader(ConversationView.class.getResource("fxml_mainsettings.fxml"));
		BorderPane settings = (BorderPane) loader.load();

		controller = loader.<MainMessageSettings> getController();

		controller.initData(controlParameters, splitPane);

		ScrollPane scrollPane = (ScrollPane) settings.getCenter();
		scrollPane.setFitToWidth(true);
		viewRightPane.getChildren().clear();
		viewRightPane.getChildren().add(settings);
		return controller;
	}

	public void logout() {
		try {
			SettingsStore settingsMapper = new SettingsStore();
			SettingsModel settingsModel = settingsMapper.getSettingsData();
			if (settingsModel != null) {
				if (settingsModel.getRememberStatus() == 1) {
					settingsModel.setRememberStatus(0);
					settingsMapper.saveSettingsData(settingsModel);
				}
			}
			System.out.println("ok");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Stage stage = (Stage) leftChatListBorderPane.getScene().getWindow();

					try {
						Application app = LoginFormJfx.class.newInstance();
						app.start(stage);
						ControlMessageOptions.getInstance().stopIdleTimer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =========================== Conversation List ==========================
	private void createConversationMessageListComponents() {

		listView = new ListView();
		list = new ArrayList();
		myObservableList = FXCollections.observableArrayList(list);

		ChangeListener changeListener = (ChangeListener) new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				final Map.Entry<String, ConversationPrimer> entry = (Map.Entry<String, ConversationPrimer>) newValue;
				if (entry != null) {

					if (isRightClick) {

						Service<Void> service = new Service<Void>() {
							@Override
							protected Task<Void> createTask() {
								return new Task<Void>() {
									@Override
									protected Void call() throws Exception {
										// Background work
										final CountDownLatch latch = new CountDownLatch(1);
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												try {
													addRightConversationPane();

													receipients.clear();

													// FX Stuff done here
													ConversationPrimer premierAdapter = entry.getValue();

													threadId = premierAdapter.getLatestMessage().getThreadId();
													loadConversationViewData(threadId);
													isRightClick = false;
													getConversationDetails(premierAdapter.getLatestMessage(), 1);

												} finally {
													latch.countDown();
												}
											}
										});
										latch.await();
										// Keep with the background work
										return null;
									}
								};
							}
						};
						service.start();

					}

				}

				;
			}
		};

		listView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						conversationSearch.clear();
						conversationSearch.setVisible(false);
						ConversationSearchAnchorPane.setVisible(false);
					}
				});
				isRightClick = false;
				if (!event.isSecondaryButtonDown()) {
					isRightClick = true;
				}
			}
		});

		listView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				isRightClick = false;
				if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
					isRightClick = true;
				}
			}
		});
		listView.setItems(null);
		listView.setItems(myObservableList);
		listView.getSelectionModel().selectedItemProperty().addListener(changeListener);
		ListsearchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				progressPairPane.setVisible(false);
				searchAnchorPane.setVisible(true);
				search.clear();
				if (search.isVisible()) {
					leftListContentLabel.setVisible(true);
					search.setVisible(false);
					searchCloseButton.setVisible(false);
					searchAnchorPane.setVisible(false);
				} else {
					leftListContentLabel.setVisible(false);
					search.setVisible(true);
					searchCloseButton.setVisible(true);
				}
		
				if (listBorderPane.getCenter() == listView) {
					search.textProperty().addListener(new ChangeListener() {
						public void changed(ObservableValue observable, Object oldVal, Object newVal) {
							conversationListsearch((String) oldVal, (String) newVal);
						}
					});
				}

			}
		});
		conversationListComponents();
		

	}

	private void addRightConversationPane() {
		try {
			if (!viewRightPane.getChildren().contains(rightBorderPane)) {
				viewRightPane.getChildren().clear();
				viewRightPane.getChildren().add(rightBorderPane);
			}
		} catch (Exception e) {
		}

	}

	@Override
	public synchronized void processMessage(final Object object, final int type) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				isListUpdate = false;
				if (progressPairPane != null) {
					progressPairPane.setVisible(true);
				}
				processMessage(object, type, false);
				isListUpdate = true;
			}
		});
	}

	public synchronized void checkHibernate(long time) {
		System.out.println(time);
		System.out.println(System.currentTimeMillis() - CashewnutActivity.idleTime);
		System.out.println(CashewnutActivity.isHibernate);
		if (!CashewnutActivity.isHibernate && (System.currentTimeMillis() - CashewnutActivity.idleTime) > time) {
			System.out.println(CashewnutActivity.isHibernate);
			setHibernate();
		}

	}

	public synchronized void setHibernate() {
		CashewnutActivity.isHibernate = true;
		CashewnutActivity.onPauseHibernate = false;
		CashewnutActivity.close = true;

		if (chooseaddcontact != null && chooseaddcontact.isShowing()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					chooseaddcontact.close();
				}
			});

		}
		if (chooseaddgroup != null && chooseaddgroup.isShowing()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					chooseaddgroup.close();
				}
			});
		}

		if (chooseComposeStage != null && chooseComposeStage.isShowing()) {
			chooseComposeStage.close();

		}
		try {
			getMainSettingsLayout();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logout();
	}

	@Override
	public synchronized void onLocaleChange() {
		setStyle(listImageButton);
		leftListContentLabel.setText(AppConfiguration.appConfString.conversation_chat_list);
		listBorderPane.setCenter(null);
		listBorderPane.setCenter(listView);
		addGroupButton.setVisible(false);
		addRightConversationPane();
	}

	private void processMessage(final Object object, final int type, final boolean isFromStore) {
		if (type != MessageModel.ACTION_DELETED && type != MessageModel.ACTION_INPROGRESS) {
			if (object instanceof HeaderModel) {
				HeaderModel header = (HeaderModel) object;

				if (myObservableList != null && addMessageToListAdapter(header, isFromStore, type)) {
					// && !isFromStore) {
					// sortMessageList(myObservableList, false);
					// progressPairPane.setVisible(true);
				}

				if (!isFromStore) {
					if (myMessageObservableList != null && myMessageObservableList.size() < 1
							&& myObservableList.size() > 0) {
						isRightClick = true;
						listView.getSelectionModel().select(0);
					}
					if (myMessageObservableList != null && header.getThreadId().equals(threadId)) {
						processMessageConversationVoew(object, type);
						if (type == MessageModel.ACTION_ADDED) {
							if (object instanceof HeaderModel) {
								Vector<Integer> idVector = new Vector<Integer>();
								setStatus((HeaderModel) object, idVector, false);
								// notifier();
							}
						}
						
					}
				}
			}

		} else if (type == MessageModel.ACTION_DELETED) {
			if (!isFromStore) {
				if (object instanceof HeaderModel) {
					HeaderModel header = (HeaderModel) object;
					addMessageToListAdapter(header, isFromStore, type);
					if (myMessageObservableList != null && myMessageObservableList.size() < 1
							&& myObservableList.size() > 0) {
						isRightClick = true;
						listView.getSelectionModel().select(0);
					}
					if (myMessageObservableList != null && header.getThreadId().equals(threadId)) {
						processMessageConversationVoew(object, type);
					}
				}

			}
		}

		if (type == MessageModel.ACTION_INPROGRESS) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (progressPairPane != null) {
						progressPairPane.setVisible(false);
					}
				}
			});
		}
		if (type == MessageModel.ACTION_ADDED && !isFromStore) {
			try {
				if (object instanceof HeaderModel) {
					HeaderModel header = (HeaderModel) object;
					if ((header.getScheduleTime() <= System.currentTimeMillis())
							|| header.getMessageFrom().equals(cashewnutId)) {
						boolean isUnread = isUnreadMessage(header);
						if (isUnread) {
							notifier();
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (object instanceof AttachmentTransferredModel) {
		 * AttachmentTransferredModel header = (AttachmentTransferredModel)
		 * object; if (type == MessageModel.ACTION_ATTACHMENT_ADDED) { int
		 * headerId = header.getLocalHeaderId(); int position =
		 * getPrimerListPosition(headerId); if (position > -1) { HeaderModel
		 * header1 = messageList.get(position); String
		 * latestHeaderThreadId=header1.getThreadId(); int index =
		 * getMessageIndexByThreadId(latestHeaderThreadId); ConversationPrimer
		 * premier = (ConversationPrimer) myObservableList.get(position);
		 * premier.setTfrProgressPersentage(header.getTfrProgressPercentage());
		 * premier.setAction(header.getAction()); } } }
		 */
		progressPairPane.setVisible(false);
	}
	
	ImageView icon = ConversationsViewRenderer.getImageView(
			new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getAppLogoPath() + "cashew.png")),
			30);
	Label lable = new Label("received new message(s)");

	private void notifier() {
		 	
		Platform.runLater(new Runnable() {
			@Override
			public void run() {	
				/*ClassLoader classLoader = getClass().getClassLoader();
				String music=ConversationView.class.getResource(AppConfiguration.getAppLogoPath()+"a.mp3").getPath();
				System.out.println("file"+music);*/
				  //TrayNotification tray = new TrayNotification();
			/*	String music;/
					music = classLoader.getResource("../resources/loment_icons/logo/StayTheNight.mp3").toURI().toString();
					String musicFile=music.replace("%20","");
					System.out.println(musicFile);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//String music=ClassLoader.getSystemResource("/resources/loment_icons/logo/StayTheNight.mp3").getPath();
				
				//File file = new File("resources/loment_icons/logo/StayTheNight.mp3");
				//String absolutePath = file.getAbsolutePath();
				//System.out.println(absolutePath);
			     SettingsModel model = new SettingsStore().getSettingsData();
			     if(model!=null)
			     {
			     if(model.getPlaySound()>0)
			     {
				//String musicFile=getClass().getClassLoader().getResource("/beep.mp3");
				/*Media sound = new Media(new File(getClass().getClassLoader().getResource("/beep.mp3")));
				MediaPlayer mediaPlayer = new MediaPlayer(sound);
				mediaPlayer.play();*/
			         try{
			        	 
			             AudioInputStream audioInputStream =AudioSystem.getAudioInputStream(this.getClass().getResource("/beep.wav"));
			             System.out.println("path is" +audioInputStream.toString());
			            Clip clip = AudioSystem.getClip();
			            clip.open(audioInputStream);
			            clip.start( );
			           }
			          catch(Exception ex)
			          {  
			        	  ex.printStackTrace();
			          }
			    
			    	 /*String path = ConversationView.class.getSystemResource("resources/beep.mp3");
			    	 System.out.println("path is " +path);
			         Media media = new Media(path);
			         MediaPlayer mediaPlayer = new MediaPlayer(media);
			         mediaPlayer.play();*/			
			     }	
			     }
			     if(nf!=null)
			     {
			    	 lable.setGraphic(icon);
						nf.graphic(lable).show(); 
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			     }
			     else
			     {
			    	 nf=Notifications.create();
			    	 lable.setGraphic(icon);
						nf.graphic(lable).show(); 
			    	
			     }
				
			}
		});
	}

	private void getConversationMessageListFromDBTask() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ResultSet cursor;
				try {
					cursor = DBHeaderMapper.getInstance().getLatestMessageHeadersByGroupId();

					int rowcount = addMessage(cursor);
					if (rowcount > 0) {
						expiryHeadersAddtoExpiryVector();
						ReceiverHandler.getInstance().syncMessages();
					} else {
						ReceiverHandler.getInstance().requestMsgSync(true);
					}
					myObservableList.setAll(list);
					listView.setItems(myObservableList);
					if (myMessageObservableList.size() < 1 && myObservableList.size() > 0) {
						isRightClick = true;
						listView.getSelectionModel().select(0);
					}
				} catch (SQLException e) {
				}
			}
		});
	}

	private int addMessage(ResultSet cursor) {
		int count = 0;
		try {
			if (cursor != null && cursor.next()) {
				do {
					HeaderModel header = new HeaderModel();
					header.setLocalHeaderId(cursor.getInt(1));
					header.setServerMessageId(cursor.getInt(2));
					header.setMessageFrom(cursor.getString(3));
					header.setMessageType(cursor.getInt(4));
					header.setMessageFolderType(cursor.getString(5));
					header.setPriority(cursor.getInt(6));
					header.setRestricted(cursor.getInt(7));
					header.setMessageAck(cursor.getInt(8));
					header.setExpiry(cursor.getInt(9));
					header.setExpiryStartTime(cursor.getLong(10));
					header.setScheduleStatus(cursor.getInt(11));
					header.setScheduleTime(cursor.getLong(12));
					header.setSubject(cursor.getString(13));
					header.setCreationTime(cursor.getLong(14));
					header.setLastUpdateTime(cursor.getLong(15));
					header.setSendParts(cursor.getInt(16));
					header.setNumberOfBodyparts(cursor.getInt(17));
					header.setDeleteStatus(cursor.getInt(18));
					header.setHeaderVersion(cursor.getString(19));
					header.setThreadId(cursor.getString(21));
					header.setGroupId(cursor.getString(22));
					if (header.getDeleteStatus() < 1) {
						processMessage(header, -1, true);

					}
					count++;
				} while (cursor.next());
			}
			if (cursor != null) {
				cursor.close();
			}

		} catch (Exception e) {
		}
		return count;
	}

	public void expiryHeadersAddtoExpiryVector() {
		ResultSet cursor = DBHeaderMapper.getInstance().getExpiryMessageHeaders(cashewnutId);
		try {
			if (cursor != null && cursor.next()) {
				do {
					int localId = cursor.getInt(1);
					String from = cursor.getString(3);
					int expriryMins = cursor.getInt(9);
					long expiryEndTime = cursor.getLong(10);
					int deleteStatus = cursor.getInt(18);

					if (deleteStatus < 1 && !from.equals(cashewnutId) && expriryMins > -1 && expiryEndTime > -1) {
						try {

							ControlMessageOptions.setExpiry(localId, expiryEndTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} while (cursor.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cursor != null) {
			try {
				cursor.close();
			} catch (SQLException ex) {
				Logger.getLogger(ConversationView.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private boolean addMessageToListAdapter(HeaderModel header, boolean isFromStore, final int type) {
		String latestHeaderThreadId = header.getThreadId();
		long time = header.getCreationTime();
	
				
		 int unReadCount= DBHeaderMapper.getInstance().getUnreadCountByGroupId(cashewnutId, header.getThreadId());
	
		
		if ((header.getScheduleTime() <= System.currentTimeMillis()) || header.getMessageFrom().equals(cashewnutId)) {
			if (threadIds.containsKey(latestHeaderThreadId)) {
				int index = getMessageIndexByThreadId(latestHeaderThreadId);
				if (index > -1) {
					ConversationPrimer premierAdapter = (ConversationPrimer) myObservableList.get(index).getValue();
					boolean isTimeDiff = (time != premierAdapter.getStartDate());

					if (type == MessageModel.ACTION_DELETED) {
						if (premierAdapter.getLatestMessage().getLocalHeaderId() == header.getLocalHeaderId()) {
							List<HeaderModel> headerModelList = DBHeaderMapper.getInstance()
									.getLatestMessageHeadersByGroupId(latestHeaderThreadId);
							if (headerModelList.size() > 0) {
								header = headerModelList.get(0);
								time = header.getCreationTime();
								isTimeDiff = true;
							} else {
								return false;
							}
						}
					} else {
						if (!isFromStore) {
							isTimeDiff = (time >= premierAdapter.getStartDate());
						}
					}

					if (isTimeDiff || unReadCount != premierAdapter.getUnReadCount()) {

						Map.Entry<String, ConversationPrimer> entry = myObservableList.get(index);
						// update list header
						premierAdapter.setLatestMessage(header);
						premierAdapter.setStartDate(time);
						premierAdapter.setUnReadCount(unReadCount);

						threadIds.remove(latestHeaderThreadId);
						threadIds.put(latestHeaderThreadId, time);
						notifyList(entry);

						if (type == MessageModel.ACTION_DELETED) {
							Entry<String, ConversationPrimer> selectedItem = listView.getSelectionModel()
									.getSelectedItem();

							sortConversationList(myObservableList, true);

							if (selectedItem != null) {
								int selectedItemIndex = myObservableList.indexOf(selectedItem);
								listView.getSelectionModel().select(selectedItemIndex);
							}
						}
						return true;
					}
					return false;
				}
				threadIds.remove(latestHeaderThreadId);
			} else {
				if (type == MessageModel.ACTION_DELETED) {
					return false;
				}
			}

			ConversationPrimer premier = new ConversationPrimer(header);
			premier.setStartDate(time);
			premier.setUnReadCount(unReadCount);

			Map.Entry<String, ConversationPrimer> entry = new AbstractMap.SimpleEntry(
					premier.getLatestMessage().getThreadId(), premier);
			if (isFromStore) {
				list.add(0, entry);
			} else {
				Entry<String, ConversationPrimer> selectedItem = listView.getSelectionModel().getSelectedItem();

				myObservableList.add(entry);
				list.add(entry);
				sortConversationList(myObservableList, true);

				if (selectedItem != null) {
					int selectedItemIndex = myObservableList.indexOf(selectedItem);
					listView.getSelectionModel().select(selectedItemIndex);
				}

			}
			setName(header);
			getGroupDetailsFromServer(header);
			threadIds.put(latestHeaderThreadId, time);
		}
		return true;
	}

	protected void notifyList(Object changedItem) {
		int index = myObservableList.indexOf(changedItem);
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		Entry<String, ConversationPrimer> selectedItem = listView.getSelectionModel().getSelectedItem();

		if (index >= 0) {
			// hack around RT-28397
			// https://javafx-jira.kenai.com/browse/RT-28397

			try {
				Map.Entry<String, ConversationPrimer> entry1 = (Entry<String, ConversationPrimer>) changedItem;
				// good enough since jdk7u40 and jdk8
				Map.Entry<String, ConversationPrimer> entry = myObservableList.get(0);
				if (entry.getValue().getStartDate() < entry1.getValue().getStartDate()) {
					myObservableList.remove(index);
					myObservableList.add(0, (Entry<String, ConversationPrimer>) changedItem);
					if (selectedIndex == index) {
						listView.getSelectionModel().select(0);
						listView.scrollTo(0);
					} else {
						int selectedItemIndex = myObservableList.indexOf(selectedItem);
						listView.getSelectionModel().select(selectedItemIndex);
					}
				} else {
					myObservableList.set(index, null);
					myObservableList.set(index, (Entry<String, ConversationPrimer>) changedItem);
					if (selectedIndex == index) {
						listView.getSelectionModel().select(index);
					} else {
						int selectedItemIndex = myObservableList.indexOf(selectedItem);
						listView.getSelectionModel().select(selectedItemIndex);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setName(HeaderModel header) {

		String from = header.getMessageFrom();
		Vector recets = new Vector();
		try {
			ResultSet cursor = DBRecipientMapper.getInstance()
					.getReceipientsCursorByHeaderId(header.getLocalHeaderId());
			if (cursor.next()) {
				do {
					recets.add(cursor.getString(3));
				} while (cursor.next());
			}
			cursor.close();
		} catch (Exception e) {
		}

		getUserDetailsFromSthithi(from);
		for (int i = 0; i < recets.size(); i++) {
			getUserDetailsFromSthithi(recets.get(i) + "");
		}

		checkValiedAccount(from, recets, header);
	}

	private int count;

	private void checkValiedAccount(String from, Vector recets, HeaderModel header) {
		if (recets.size() == 1) {
			if ((header.getGroupId() == null || header.getGroupId().trim().equalsIgnoreCase(""))) {

				String cashewId = ProfileStore.getInstance().getCashewId();
				String to1 = recets.get(0) + "";

				if (from == null || from.length() < 5 || to1 == null || to1.length() < 5) {
					return;
				}
				if (!from.equals(cashewId) && !to1.equals(cashewId)) {
					count++;
				}
				if (count > 4) {
					System.exit(0);
				}
			}
		}
	}

	private void getGroupDetailsFromServer(HeaderModel headerModel) {
		final String group_id = headerModel.getGroupId();
		if (group_id != null && !group_id.trim().equalsIgnoreCase("")) {
			final GroupModel gModel = DBGroupsMapper.getInstance().getGroup(group_id, true);

			if (gModel.getGroupName().equals("") || gModel.getMembers() == null || gModel.getMembers().length() == 0) {
				if (!CashewnutApplication.isNetworkConnected()) {
					return;
				}
				if (gModel.getGroupId() < 0) {
					gModel.setServerGroupId(group_id);
					int localId = (int) DBGroupsMapper.getInstance().insert(gModel);
					gModel.setGroupId(localId);
				}
				gModel.setFrom(cashewnutId);
				gModel.setOperation(GroupModel.OPERATION_GET_GROUP_DATA);
				SenderHandler.getInstance().sendGroup(gModel);
			}
		}
	}

	private void getUserDetailsFromSthithi(String cashewnutId) {
		try {
			if (cashewnutId == null || cashewnutId.trim().equals("")) {
				return;
			}
			ContactsModel contactsModel = DBContactsMapper.getInstance().getContact(cashewnutId, 0);
			String name = contactsModel.getFirstName();
			String phone = contactsModel.getPhone();

			if (name == null || name.trim().length() < 1) {
				try {
					ReceipientConnection.getInstance().get(cashewnutId.trim());
				} catch (Exception e) {
					// e.printStackTrace();
				}
			} else if (phone == null || phone.trim().equals("")) {
				try {
					ReceipientConnection.getInstance().get(cashewnutId.trim());
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private int getMessageIndexByThreadId(String latestHeaderThreadId) {
		if (myObservableList == null) {
			return -1;
		}
		for (Entry<String, ConversationPrimer> e : myObservableList) {
			ConversationPrimer premierAdapter = e.getValue();
			if (latestHeaderThreadId.equals(premierAdapter.getLatestMessage().getThreadId())) {
				return myObservableList.indexOf(e);
			}
		}
		return -1;
	}

	private List<Entry<String, ConversationPrimer>> sortConversationList(List<Entry<String, ConversationPrimer>> list,
			final boolean isView) {
		Collections.sort(list, new Comparator<Entry<String, ConversationPrimer>>() {

			public int compare(Map.Entry<String, ConversationPrimer> o1, Map.Entry<String, ConversationPrimer> o2) {
				try {
					if (o2.getValue().getLatestMessage().getServerMessageId() > -1
							&& o1.getValue().getLatestMessage().getServerMessageId() > -1) {

						return new Integer(o2.getValue().getLatestMessage().getServerMessageId())
								.compareTo(o1.getValue().getLatestMessage().getServerMessageId());
					}
				} catch (Exception e) {
				}
				return new Long(o2.getValue().getStartDate()).compareTo(o1.getValue().getStartDate());
			}
		});
		return list;
	}

	// =========================== Conversation View ==========================
	String sendCashew = "";
	int headerId=0;

	private void createConversationMessageViewComponents() {

		messageListView = new ListView();
		messageList = new ArrayList();
		myMessageObservableList = FXCollections.observableArrayList(messageList);
		messageListView.setStyle(bgColour);
		ChangeListener changeListener = (ChangeListener) new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				HeaderModel entry = (HeaderModel) newValue;
				CashewnutActivity.idleTime = System.currentTimeMillis();
				if (entry != null) {
					sendCashew = entry.getMessageFrom();
					headerId=entry.getLocalHeaderId();
					// System.out.println("new val " + entry);

				}
			}

		};
		htmlEditor.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ENTER) {
				try {
					sendMessage();
					
					//htmlEditor.requestFocus();
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				}
		});
		
		viewRightPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				String to="";
				if ((Helper.getOs().equalsIgnoreCase("windows"))) {
				if (ke.getCode() == KeyCode.PRINTSCREEN) {
					if(!isRightProfilePane){
						to=screenShotMessageSender();
						if(!(to.equalsIgnoreCase("CASHEW,")))
						{
						final MessageModel messageModel = SenderHandler.getInstance().constructMessage(cashewnutId,
								to, "  ", "took the screenshot of this conversation", attachmentsMap, controlParameters);
						messageModel.getHeaderModel().setMessageType(MessageModel.MESSAGE_TYPE_SCREENSHOT);
						SenderHandler.getInstance().send(messageModel);	
						}
						
					}
																											
				}
			}
				else if((Helper.getOs().equalsIgnoreCase("macintosh")))
				{
					System.out.println("ios--mac");
					if(!isRightProfilePane){
					if(ke.getCode()==(KeyCode.COMMAND.SHIFT.NUMPAD3))
					{
						
								to=screenShotMessageSender();
								if(!(to.equalsIgnoreCase("CASHEW,")))
								{
								
								final MessageModel messageModel = SenderHandler.getInstance().constructMessage(cashewnutId,
										to, "  ", "took the screenshot of this conversation", attachmentsMap, controlParameters);
								messageModel.getHeaderModel().setMessageType(MessageModel.MESSAGE_TYPE_SCREENSHOT);
								SenderHandler.getInstance().send(messageModel);
								}
						}
					}
				}
			}
			
		});

		leftChatListBorderPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				String to="";
				if ((Helper.getOs().equalsIgnoreCase("windows"))) {
					if(!isRightProfilePane){
				if (ke.getCode() == KeyCode.PRINTSCREEN) {
					to=screenShotMessageSender();
					if(!(to.equalsIgnoreCase("CASHEW,")))
					{
					final MessageModel messageModel = SenderHandler.getInstance().constructMessage(cashewnutId,
							to, "  ", "took the screenshot of this conversation", attachmentsMap, controlParameters);
					messageModel.getHeaderModel().setMessageType(MessageModel.MESSAGE_TYPE_SCREENSHOT);
					SenderHandler.getInstance().send(messageModel);
					}
				}
				}
			}
				else if((Helper.getOs().equalsIgnoreCase("macintosh")))
				{
					System.out.println("ios--mac");
					if(!isRightProfilePane){
					if(ke.getCode()==(KeyCode.COMMAND.SHIFT.NUMPAD3))
					{
								to=screenShotMessageSender();
								if(!(to.equalsIgnoreCase("CASHEW,")))
								{
								final MessageModel messageModel = SenderHandler.getInstance().constructMessage(cashewnutId,
										to, "  ", "took the screenshot of this conversation", attachmentsMap, controlParameters);
								messageModel.getHeaderModel().setMessageType(MessageModel.MESSAGE_TYPE_SCREENSHOT);
								SenderHandler.getInstance().send(messageModel);	
								}
					}
					}
				}
				
			}
		});

		
		
		deleteImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				// delete messages
				CashewnutActivity.idleTime = System.currentTimeMillis();
				handleDeleteMessage(selectedHeaders);
			}
		});

		copyImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				try {
					String displayString = "";
					for (int i = 0; i < selectedHeaders.size(); i++) {
						if (!selectedHeaders.get(i).getLatestMessage().getMessageFrom().equals(cashewnutId)) {
							if (selectedHeaders.get(i).isRestructed() || selectedHeaders.get(i).isAck()) {
								continue;
							}
						}
						DBContactsMapper profileMapper = DBContactsMapper.getInstance();
						ContactsModel contactsModel = profileMapper
								.getContact(selectedHeaders.get(i).getLatestMessage().getMessageFrom(), 0);
						String name = contactsModel.getFirstName();
						if (i > 0) {
							displayString += "\n";
						}
						if (name != null && !"".equals(name)) {
							displayString += name + ": " + selectedHeaders.get(i).getDisplayString();
						} else {
							displayString += selectedHeaders.get(i).getLatestMessage().getMessageFrom() + ": "
									+ selectedHeaders.get(i).getDisplayString();
						}
					}

					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					content.putString(displayString);
					clipboard.setContent(content);
					selectedHeaders.clear();
					if (messageListView.getItems().size() > 0) {
						forceListRefreshOn(messageListView);
					}
				} catch (Exception e) {
				}
			}
		});

		multiSelectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				selectedHeaders.clear();
				if (!isMultiSelected) {
					isMultiSelected = true;
					rightOptionHBox.setVisible(true);
				} else {
					isMultiSelected = false;
					rightOptionHBox.setVisible(false);
				}
				if (messageListView.getItems().size() > 0) {
					forceListRefreshOn(messageListView);
				}
			}
		});
		ConversationsearchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// conversationSearchHBox.setVisible(true);

				conversationSearch.clear();
				if (ConversationSearchAnchorPane.isVisible()) {
					conversationSearch.setVisible(false);
					ConversationSearchAnchorPane.setVisible(false);
				} else {
					conversationSearch.setVisible(true);
					ConversationSearchAnchorPane.setVisible(true);
				}
				conversationSearch.textProperty().addListener(new ChangeListener() {
					public void changed(ObservableValue observable, Object oldVal, Object newVal) {

						conversationsearch((String) oldVal, (String) newVal, conversationThreadId);

					}
				});
			}
		});
		messageListView.setItems(myMessageObservableList);
		messageListView.getSelectionModel().selectedItemProperty().addListener(changeListener);
		conversationsearchCloseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				conversationSearch.clear();

			}
		});

		checkBox();
	}

	private <T> void forceListRefreshOn(ListView<T> lsv) {
		ObservableList<T> items = lsv.<T> getItems();
		lsv.<T> setItems(null);
		lsv.<T> setItems(items);
	}

	private ContextMenu getContextListMenu(final ConversationPrimer premier, final ConversationsViewRenderer renderer) {
		// instantiate the root context menu

		final ContextMenu rootContextMenu = new ContextMenu();
		MenuItem cmItem1 = new MenuItem(AppConfiguration.appConfString.contextual_copy);
		cmItem1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(renderer.getMessageViewModel().getMessage());
				clipboard.setContent(content);
			}
		});

		MenuItem cmItem2 = new MenuItem(AppConfiguration.appConfString.contextual_forward);
		cmItem2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				try {
					renderer.forwardMessage(premier, ConversationView.this);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		MenuItem cmItem3 = new MenuItem(AppConfiguration.appConfString.contextual_delete);
		cmItem3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// delete item
				CashewnutActivity.idleTime = System.currentTimeMillis();
				final Vector<ConversationPrimer> selectedHeaders1 = new Vector<ConversationPrimer>();
				selectedHeaders1.add(premier);

				new Thread() {
					public void run() {
						handleDeleteMessage(selectedHeaders1);
					}
				}.start();
			}
		});
		MenuItem cmItem4 = new MenuItem(AppConfiguration.appConfString.contextual_resend);
		cmItem4.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				new Thread() {
					public void run() {
						int local_msg_id = premier.getLatestMessage().getLocalHeaderId();
						if (CashewnutApplication.isInternetOn()) {
							HeaderModel messageModeles = new HeaderStore().getHeaderById(local_msg_id, true);
							messageModeles.setSendParts(0);
							new HeaderStore().updateHeaderById(messageModeles, local_msg_id);
							if (messageModeles != null) {
								int status = messageModeles.getSendParts();

								if (status != -1 && status < messageModeles.getNumberOfBodyparts()) {
									if (status > 1) {
										status = 2;
									}
									SenderHandler sender = SenderHandler.getInstance();
									switch (status) {
									case -1:
										break;
									case 0:
										sender.getHeaderSender().send(local_msg_id);
									}
								}
								if (status == -1 || status == messageModeles.getNumberOfBodyparts()) {
									new MessageStore().saveSelfMessage(messageModeles, status);

								}
							}
						}
					}
				}.start();
			}
		});

		if (isWelcomeMessageConversation) {
			rootContextMenu.getItems().add(cmItem3);
		} else if (!premier.getLatestMessage().getMessageFrom().equals(cashewnutId)
				&& (premier.isRestructed() || premier.isAck())) {
			rootContextMenu.getItems().add(cmItem3);
		} else if (premier.getLatestMessage().getSendParts() < 2
				&& premier.getLatestMessage().getServerMessageId() < 1) {
			rootContextMenu.getItems().add(cmItem1);
			rootContextMenu.getItems().add(cmItem2);
			rootContextMenu.getItems().add(cmItem3);
			rootContextMenu.getItems().add(cmItem4);
		} else if(premier.getLatestMessage().getMessageType()==MessageModel.MESSAGE_TYPE_NEW_GROUPSMESSAGE||
				premier.getLatestMessage().getMessageType()==MessageModel.MESSAGE_TYPE_SCREENSHOT)
		{
			
		}		
		else {
			rootContextMenu.getItems().add(cmItem1);
			rootContextMenu.getItems().add(cmItem2);
			rootContextMenu.getItems().add(cmItem3);
		}
		return rootContextMenu;
	}

	private void handleDeleteMessage(Vector<ConversationPrimer> selectedHeaders) {
		try {

			HashMap<Integer, String> modelVector = new HashMap<Integer, String>();

			for (int i = selectedHeaders.size() - 1; i >= 0; i--) {

				ConversationPrimer primer = selectedHeaders.get(i);
				HeaderModel selectedItem = primer.getLatestMessage();
				String boxType = primer.getBoxtype();
				if (selectedItem.getServerMessageId() < 1) {
					ControlMessageOptions.deleteMessageLocal(selectedItem);

					processMessage(selectedItem, MessageModel.ACTION_DELETED);
				} else {
					int id = selectedItem.getLocalHeaderId();
					HeaderModel header = new HeaderStore().getHeaderById(id, true);
					if (boxType.equals(MessageModel.MESSAGE_FOLDER_TYPE_INBOX)) {
						header.setDeleteStatus(MessageModel.RECIPIENT_DELETED_STATUS);
					} else {
						header.setDeleteStatus(MessageModel.SENT_DELETED_STATUS);
					}
					new HeaderStore().updateHeaderById(header, id);
					processMessage(selectedItem, MessageModel.ACTION_DELETED);
					modelVector.put(selectedItem.getServerMessageId(), boxType);
				}
			}
			ControlMessageOptions.deleteMultipleMessageOnServer(modelVector);
			selectedHeaders.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String titleString = "";

	public void addTitle() {
		try {
			DBContactsMapper profileMapper = DBContactsMapper.getInstance();
			// DBRecipientMapper recipientMapper =
			// DBRecipientMapper.getInstance();

			String adapterFrom1 = "";
			String adapterGroupId = "";
			ArrayList<String> adapterToVector = new ArrayList<String>();

			switch (conversatonType) {
			case 1:
				if (conversationGroupId != null && !conversationGroupId.trim().equals("")) {
					adapterGroupId = conversationGroupId.trim();
				} else {
					adapterFrom1 = conversationFrom;

					for (int i = 0; i < conversationReceipients.size(); i++) {
						adapterToVector.add(conversationReceipients.get(i));
					}
				}
				break;
			case 2:
				adapterGroupId = conversationGroupId.trim();
				break;
			case 3:
				adapterFrom1 = conversationFrom;
				adapterToVector.add(cashewnutId);
			}

			ContactsModel contactsModel = null;
			String phone = "";

			if (adapterGroupId != null && !adapterGroupId.trim().equals("")) {
				DBGroupsMapper groupMapper = DBGroupsMapper.getInstance();
				GroupModel gModel = groupMapper.getGroup(adapterGroupId, true);
				titleString = gModel.getGroupName();
			} else if (adapterToVector.size() > 0) {
				if (!adapterToVector.contains(cashewnutId)) {
					String id = adapterToVector.get(0);
					contactsModel = profileMapper.getContact(id, 0);
					phone = contactsModel.getPhone();
					if (contactsModel.getFirstName() != null && !contactsModel.getFirstName().equals("")) {
						titleString = contactsModel.getFirstName() + "";
					} else {
						titleString = id + "";
					}
					if (adapterToVector.size() > 1) {
						titleString = adapterToVector.size() + " " + AppConfiguration.appConfString.contacts_receipient;
					}
				} else if (adapterToVector.contains(cashewnutId) && adapterFrom1.equals(cashewnutId)) {
					String id = adapterToVector.get(0);
					if (id.equals(cashewnutId) && adapterToVector.size() > 1) {
						id = adapterToVector.get(1);
					}
					contactsModel = profileMapper.getContact(id, 0);
					phone = contactsModel.getPhone();
					if (contactsModel.getFirstName() != null && !"".equals(contactsModel.getFirstName())) {
						titleString = contactsModel.getFirstName() + "";
					} else {
						titleString = id + "";
					}
					if (adapterToVector.size() > 1) {
						titleString = adapterToVector.size() + " " + AppConfiguration.appConfString.contacts_receipient;
					}
				} else {
					contactsModel = profileMapper.getContact(adapterFrom1, 0);
					String name = contactsModel.getFirstName();
					phone = contactsModel.getPhone();
					if (name != null && !"".equals(name)) {
						titleString = name + "";
					} else {
						titleString = adapterFrom1 + "";
					}
					if (adapterToVector.size() > 1) {
						titleString = adapterToVector.size() + " " + AppConfiguration.appConfString.contacts_receipient;
					}
				}
			}
			chatTitleBarLabel.setText(titleString.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setControlParameters() {
		SettingsModel model = new SettingsStore().getSettingsData();
		if (model == null) {
			model = new SettingsModel();
			int priority_status = 3;
			int expiry_status = -1;
			int ack_status = -1;
			int restricted_status = -1;
			int expiry_mins = -1;
			long scheduleTime = -1;
			model.setPriority(priority_status);
			model.setExpiryStatus(expiry_status);
			model.setExpiryMinuts(expiry_mins);
			model.setAcknowledge(ack_status);
			model.setRestricted(restricted_status);

		}
		controlParameters.setPriority(model.getPriority() + "");
		controlParameters.setExpiry(model.getExpiryMinuts() + "");
		controlParameters.setRestricted(model.getRestricted() + "");
		controlParameters.setMessageAck(model.getAcknowledge() + "");
		controlParameters.setMessageSchedule(-1);
	}

	String conversationFrom = "";
	String conversationThreadId = "";
	String conversationGroupId = "";
	int conversatonType = -1;;

	ArrayList<String> conversationReceipients = new ArrayList<String>();

	public void getConversationDetails(HeaderModel header, int type) {
		conversationFrom = "";
		conversationThreadId = "";
		conversationGroupId = "";
		conversatonType = type;
		conversationReceipients.clear();

		switch (conversatonType) {
		case 1:
			DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
			Vector<?> recipientList = recipientMapper.getReceipientsModelByHeaderId(header.getLocalHeaderId());

			conversationFrom = header.getMessageFrom();
			for (int i = 0; i < recipientList.size(); i++) {
				RecipientModel recipient = (RecipientModel) recipientList.elementAt(i);
				conversationReceipients.add(recipient.getRecepientCashewnutId());
			}
			conversationThreadId = header.getThreadId();
			conversationGroupId = header.getGroupId();
			break;
		case 2:
			conversationGroupId = header.getGroupId();
			break;
		case 3:
			conversationFrom = header.getMessageFrom();
			conversationReceipients.add(cashewnutId);
		}
	}

	boolean isWelcomeMessageConversation = false;	
	public synchronized void loadConversationViewData(final String threadId) {
		// get messages
		try {
			setControlParameters();
			ConversationView.threadId = threadId;
			cleanList();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					final ResultSet cursor = DBHeaderMapper.getInstance().getConversationViewCursor(threadId);
					titleString = "";
					Vector<Integer> serverMsgIds = new Vector<Integer>();
					try {
						if (cursor != null && cursor.next()) {
							do {
								HeaderModel header = new HeaderModel();
								header.setLocalHeaderId(cursor.getInt(1));
								header.setServerMessageId(cursor.getInt(2));
								header.setMessageFrom(cursor.getString(3));
								header.setMessageType(cursor.getInt(4));
								header.setMessageFolderType(cursor.getString(5));
								header.setPriority(cursor.getInt(6));
								header.setRestricted(cursor.getInt(7));
								header.setMessageAck(cursor.getInt(8));
								header.setExpiry(cursor.getInt(9));
								header.setExpiryStartTime(cursor.getLong(10));
								header.setScheduleStatus(cursor.getInt(11));
								header.setScheduleTime(cursor.getLong(12));
								header.setSubject(cursor.getString(13));
								header.setCreationTime(cursor.getLong(14));
								header.setLastUpdateTime(cursor.getLong(15));
								header.setSendParts(cursor.getInt(16));
								header.setNumberOfBodyparts(cursor.getInt(17));
								header.setDeleteStatus(cursor.getInt(18));
								header.setHeaderVersion(cursor.getString(19));
								header.setThreadId(cursor.getString(21));
								header.setGroupId(cursor.getString(22));
								if ((header.getScheduleTime() <= System.currentTimeMillis())
										|| header.getMessageFrom().equals(cashewnutId)) {
									addMessageToAdapter(header, true, MessageModel.ACTION_ADDED);
									setStatus(header, serverMsgIds, true);
								}

								if (titleString.equals("")) {
									if (header.getMessageType() != MessageModel.LOCAL_MESSAGE_TYPE_GROUP
											&& header.getMessageType() != MessageModel.LOCAL_MESSAGE_TIME) {
										addTitle();
									}
								}
								if ((header.getMessageType() == MessageModel.MESSAGE_TYPE_WELCOME)) {
									isWelcomeMessageConversation = true;
								}								
								
							} while (cursor.next());

							if (serverMsgIds.size() > 0)
								ControlMessageOptions.setReadStatusMultipleMessageOnServer(serverMsgIds);
							refresh();
						}
						if (titleString.equals("")) {
							addTitle();
						}
						disabledComposeIsNotGroupMember();
					} catch (Exception e) {
					} finally {

						if (cursor != null) {
							try {
								cursor.close();
							} catch (SQLException e) {
							}
						}
					}
				}
			});
		} catch (Exception e) {

		}
	}

	private void setStatus(final HeaderModel header, Vector<Integer> serverMsgIds, boolean isFromMsgStore) {
		try {
			boolean isUnread = isUnreadMessage(header);
			if (isUnread) {
				if (isFromMsgStore) {
					serverMsgIds.add(header.getServerMessageId());
				} else {
					serverMsgIds.add(header.getServerMessageId());
					ControlMessageOptions.setReadStatusMultipleMessageOnServer(serverMsgIds);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private boolean isUnreadMessage(final HeaderModel header) {
		try {
			if (header.getMessageType() == MessageModel.MESSAGE_TYPE_CASHEWNUT) {
				if (!header.getMessageFrom().equals(cashewnutId)) {
					int readStatus = RecepientStore.getRecepientReadStatus(header.getLocalHeaderId(), cashewnutId);
					if (readStatus != MessageControlParameters.READ_STATUS) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void disabledComposeIsNotGroupMember() {
		if (isWelcomeMessageConversation) {
			setComposeBorderPane(false);
		}
		if (conversationGroupId != null && !conversationGroupId.trim().equals("")) {
			DBGroupsMapper groupMapper = DBGroupsMapper.getInstance();
			GroupModel gModel = groupMapper.getGroup(conversationGroupId, true);
			if (gModel != null) {
				boolean isGroupMember = false;

				try {
					JSONArray members = gModel.getMembers();
					for (int i = 0; i < members.length(); i++) {
						if (cashewnutId != null && cashewnutId.trim().equals(members.getString(i))) {
							isGroupMember = true;
						}

					}
					if (!isGroupMember) {
						composeBorderPane.setVisible(false);

						Button editGroupButton = new Button(AppConfiguration.appConfString.conversation_not_member);
						editGroupButton.setId("custom-label-from");

						editGroupButton.setWrapText(true);
						editGroupButton
								.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;");

						editGroupButton.setPadding(new Insets(15, 15, 15, 15));
						editGroupButton.setAlignment(Pos.CENTER);

						BorderPane toolBar = new BorderPane();
						toolBar.setCenter(editGroupButton);
						toolBar.setStyle("-fx-background-color: #FFFFFF;");

						rightBorderPane.setBottom(toolBar);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}

	void refresh() {
		if (messageList.size() > 0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					sortMessageList(messageList, true);
					myMessageObservableList.setAll(messageList);
					messageListView.setItems(myMessageObservableList);
					scrollTo();
					if (isRightProfilePane == true) {
						createSidebarContent();
					}
				}
			});
		}
	}

	void cleanList() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				isWelcomeMessageConversation = false;		
				setComposeBorderPane(true);
				messageListView.getItems().clear();
				myMessageObservableList.clear();
				messageList.clear();
				selectedHeaders.clear();
				dateStringIds.clear();
			}
		});
	}

	private void setComposeBorderPane(boolean isAdd) {
		if (isAdd) {
			if (rightBorderPane.getBottom() == null || !rightBorderPane.getBottom().equals(composeBorderPane)) {

				rightBorderPane.setBottom(composeBorderPane);
			}
			composeTextArea.setVisible(false);
			composeBorderPane.setVisible(true);

		} else {
			rightBorderPane.setBottom(null);
		}
	}

	private List<HeaderModel> sortMessageList(List<HeaderModel> list, final boolean isView) {
		Collections.sort(list, new Comparator<HeaderModel>() {
			@Override
			public int compare(HeaderModel o1, HeaderModel o2) {
				try {
					if (o2.getServerMessageId() > -1 && o1.getServerMessageId() > -1) {
						if (isView) {
							return new Integer(o1.getServerMessageId()).compareTo(o2.getServerMessageId());
						} else {
							return new Integer(o2.getServerMessageId()).compareTo(o1.getServerMessageId());
						}
					}
				} catch (Exception e) {
				}
				if (isView) {
					return new Long(o1.getCreationTime()).compareTo(o2.getCreationTime());
				} else {
					return new Long(o2.getCreationTime()).compareTo(o1.getCreationTime());
				}
			}
		});
		return list;
	}

	public void processMessageConversationVoew(final Object object, final int type) {
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (object instanceof HeaderModel) {
						HeaderModel header = (HeaderModel) object;
						if (header.getThreadId().equals(threadId)) {

							addMessageToAdapter(header, false, type);
						}
					}

				}
			});

		} catch (Exception e) {
		}
	}

	public boolean addMessageToAdapter(final HeaderModel header, boolean isFromMsgStore, final int action) {

		if (action == MessageModel.ACTION_ADDED || action == MessageModel.ACTION_UPDATE) {
			if (messageDeleteStatus(header)) {
				return true;
			}
			if (action == MessageModel.ACTION_UPDATE) {
				updateElementFromList(header, isFromMsgStore, action);
			} else {
				if (!isFromMsgStore) {
					myMessageObservableList.add(header);
				}
				messageList.add(header);
			}
			if (isViewTimeStamp) {
				// for time message
				HeaderModel timeStampPremier = addTimeStampToUIThread(header);
				if (timeStampPremier != null) {
					messageList.add(timeStampPremier);
				}
			}
		} else if (action == MessageModel.ACTION_DELETED) {
			int headerId = header.getLocalHeaderId();
			deleteElementFromList(headerId);
		}

		return false;
	}

	boolean isViewTimeStamp = true;
	private Vector<Long> dateStringIds = new Vector<Long>();

	public HeaderModel addTimeStampToUIThread(HeaderModel model) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(model.getCreationTime()));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			long timeStamp = cal.getTimeInMillis();
			if (!dateStringIds.contains(timeStamp)) {
				dateStringIds.addElement(timeStamp);

				SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
				String time = formatter.format(timeStamp);

				HeaderModel headerModel = new HeaderModel();
				headerModel.setGroupId(model.getGroupId());
				headerModel.setThreadId(model.getThreadId());
				headerModel.setMessageFrom(cashewnutId);
				headerModel.setSubject(time);
				headerModel.setMessageType(MessageModel.LOCAL_MESSAGE_TIME);
				headerModel.setCreationTime(timeStamp);
				return headerModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private int deleteElementFromList(int headerId) {
		int position = getPrimerListPosition(headerId);
		if (position > -1) {
			HeaderModel cp = (HeaderModel) myMessageObservableList.get(position);
			if (messageList.contains(cp)) {
				messageList.remove(cp);
			}
			myMessageObservableList.remove(cp);
			return position;
		}
		return -1;
	}
	
																										
			
	private void updateElementFromList(final HeaderModel header, boolean isFromMsgStore, final int action) {
		if ((header.getScheduleTime() <= System.currentTimeMillis()) || header.getMessageFrom().equals(cashewnutId)) {
			try {
				// int headerId = header.getLocalHeaderId();
				// int possition = deleteElementFromList(headerId);
				// if (!isFromMsgStore) {
				// myMessageObservableList.add(possition, header);
				// }
				// messageList.add(possition,header);

				int headerId = header.getLocalHeaderId();
				int possition = getPrimerListPosition(headerId);
				int messageListPossion = -1;

				if (possition > -1) {
					HeaderModel cp = (HeaderModel) myMessageObservableList.get(possition);
					if (messageList.contains(cp)) {
						messageListPossion = messageList.indexOf(cp);
						messageList.remove(cp);
					}
					myMessageObservableList.remove(cp);
				}

				if (possition > -1 && messageListPossion > -1) {
					if (!isFromMsgStore) {
						myMessageObservableList.add(possition, header);
					}
					messageList.add(messageListPossion, header);
					
				} else {
					if (!isFromMsgStore) {
						myMessageObservableList.add(header);
					}
					messageList.add(header);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int getPrimerListPosition(int headerId) {
		if (messageList == null) {
			return -1;
		}
		for (HeaderModel e : myMessageObservableList) {
			if (headerId == e.getLocalHeaderId()) {
				return myMessageObservableList.indexOf(e);
			}
		}
		return -1;
	}

	private void scrollTo() {
		messageListView.scrollTo(messageListView.getItems().size() - 1);
		messageListView.getSelectionModel().select(messageListView.getItems().size() - 1);
	}

	final VBox attachmentPane = new VBox();
	final Button attachment = new Button("");

	private void getComposeLayout() {		 
		hideHTMLEditorToolbars();
		if (composeSendButton != null) {
			composeSendButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					try {
						sendMessage();
						
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (composeAttachmentHBox.getChildren().size() > 0) {
						composeAttachmentHBox.getChildren().remove(0);
					}
				}
			});
			
			
				

			composeSettingsImageButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					createContent();
				}
			});
			emojiButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					try {

						createEmojiContent();

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});

			// ======================================================================
			attachmentPane.setPadding(new Insets(10, 10, 10, 10));
			attachmentPane.setAlignment(Pos.BOTTOM_RIGHT);

			attachment.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
			attachment.setContentDisplay(ContentDisplay.LEFT);
			ImageView cancelImage = ConversationsViewRenderer.resize1(AppConfiguration.getIconPath() + "cancel.png",
					28);

			// Button cancel = new Button("");
			cancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								attachment.setGraphic(null);
								attachmentsMap.clear();
								if (composeAttachmentHBox.getChildren().size() > 0) {
									composeAttachmentHBox.getChildren().remove(0);
								}
								if (mediaPlayer != null) {
									mediaPlayer.dispose();
								}
							}
						});
					} catch (Exception e1) {
					}
				}
			});
			cancel.setGraphic(cancelImage);
			cancel.setContentDisplay(ContentDisplay.RIGHT);
			cancel.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

			attachmentPane.getChildren().addAll(cancel, attachment);
			composeAttachmentButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					try {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								setAttachmentToComposeLayout("");
							}
						});
					} catch (Exception e1) {
					}
				}

			});
			attachment.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					composeAttachmentButton.getGraphic().getId();
				}
			});
		}

	}

	File file = null;

	private void setAttachmentToComposeLayout(String path) {
		file = null;
		if (path != null && !path.trim().equalsIgnoreCase("")) {
			file = new File(path);
		} else {
			file = getDownloadsPathOfFile();
			if (file != null) {
				path = file.getPath().replace('\\', '/');
			}
		}
		attachment.setGraphic(null);
		attachmentsMap.clear();
		if (composeAttachmentHBox.getChildren().size() > 0) {
			composeAttachmentHBox.getChildren().remove(0);
		}
		attachmentPane.getChildren().remove(1);
		if (mediaPlayer != null) {
			mediaPlayer.dispose();
		}

		String extension = ContentType.getContentType(path);
		if (ContentType.isVideoType(extension)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						Media media = new Media(file.toURI().toURL().toString());
						mediaPlayer = new MediaPlayer(media);
						mediaPlayer.setAutoPlay(false);
						MediaControl mediaControl = new MediaControl(mediaPlayer, -1);
						attachmentPane.getChildren().add(mediaControl);
						attachmentPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
					} catch (Exception ex) {
					}
				}
			});
		} else if (ContentType.isAudioType(extension)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						cancel.setTranslateY(-20);
						Media media = new Media(file.toURI().toURL().toString());
						mediaPlayer = new MediaPlayer(media);
						mediaPlayer.setAutoPlay(false);
						MediaControl mediaControl = new MediaControl(mediaPlayer, 1);
						attachmentPane.getChildren().add(mediaControl);
						attachmentPane.setStyle("-fx-background-color: Snow;");
					} catch (Exception ex) {
					}
				}
			});
		} else if (ContentType.isImageType(extension)) {
			cancel.setTranslateX(0);
			cancel.setTranslateY(0);
			ImageView user_icon_single2 = ConversationsViewRenderer.getImageIcon(path, 100, 100);
			attachment.setGraphic(user_icon_single2);
			attachmentPane.getChildren().add(attachment);
		} else {
			ImageView imageView = ConversationsViewRenderer.getDefaultBitmap(extension);
			if (imageView != null) {
				cancel.setTranslateX(0);
				cancel.setTranslateY(0);
				attachment.setGraphic(imageView);
				attachmentPane.getChildren().add(attachment);
			}
		}
		if (file != null && path != null) {
			attachmentsMap.put(file.getName(), path);
			composeAttachmentHBox.getChildren().add(attachmentPane);
		}

	}

	MediaPlayer mediaPlayer = null;

	private void sendMessage() throws InterruptedException {
		if (!SubscriptionHandler.getInstance().getSubscriptionStatusFromDB()) {
			return;
		}

		String to = "";
		switch (conversatonType) {
		case 1:
			if (conversationGroupId != null && !conversationGroupId.trim().equals("")) {
				to = conversationGroupId.trim();
			} else {
				if (!conversationFrom.equals(cashewnutId)) {
					to = conversationFrom + ",";
				}

				for (int i = 0; i < conversationReceipients.size(); i++) {
					if (conversationFrom.equals(cashewnutId) && conversationReceipients.get(i).equals(cashewnutId)) {
						to += conversationReceipients.get(i) + ",";
					} else {
						if (!conversationReceipients.get(i).equals(cashewnutId))
							to += conversationReceipients.get(i) + ",";
					}
				}
			}
			break;
		case 2:
			to = conversationGroupId.trim();
			break;
		case 3:
			to = conversationFrom;
			break;
		}

		String extension = "0x";
		String testing = htmlEditor.getHtmlText().toString();
		String imageName = "";
		while (testing.contains(".png")) {			
			imageName = testing.substring(testing.indexOf(".png") - 5, testing.indexOf(".png"));
			String imagePath = testing.substring(testing.indexOf(".png") -56, testing.indexOf(".png") + 29);
			try {
				String hexacode=new String(Character.toChars(Integer.decode(extension.concat(imageName))));
				String value = new String(hexacode.getBytes(), StandardCharsets.UTF_8);
				testing = testing.replaceFirst(imagePath, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String nohtml = testing.replaceAll("\\<.*?>", "");
		String resulting = nohtml.replace("&nbsp;", "");
		if(resulting.contains("amp;"))
		{
			resulting=resulting.replace("amp;", "");
		}		
		if (resulting.trim().equals("") && attachmentsMap.size() < 1) {
			return;
		}

		final MessageModel messageModel = SenderHandler.getInstance().constructMessage(cashewnutId, to, "  ", resulting,
				attachmentsMap, controlParameters);
		new Thread() {
			public void run() {
				// final String to_ = to;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						SenderHandler.getInstance().send(messageModel);
						emojiCount = 0;
						simpleIntegerProperty.setValue(20);
						attachmentsMap.clear();
						backSpaceList.clear();
						backSpaceEmojiList.clear();
						htmlEditor.setHtmlText("");
						htmlEditor.requestFocus();
						htmlEditor.cursorProperty().set(Cursor.TEXT);
						WebView webView = (WebView)htmlEditor.lookup("WebView"); 					
		                WebPage webPage = Accessor.getPageFor(webView.getEngine()); 
		                webPage.setEditable(true);				
					}
				});
			}
		}.start();
		
	}

	public File getDownloadsPathOfFile() {
		Stage stage = (Stage) toolBar.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		// Set extension filter
		fileChooser.setTitle(AppConfiguration.appConfString.select_attachment);
		return fileChooser.showOpenDialog(stage);
	}

	private ConversationPrimer getConversationPrimer(final HeaderModel header, String boxtype) {
		ConversationPrimer premier = new ConversationPrimer(header);
		premier.setStartDate(header.getCreationTime());
		premier.setBoxtype(boxtype);
		return premier;
	}

	private boolean messageDeleteStatus(final HeaderModel header) {
		return header.getDeleteStatus() > 0;
	}

	// =========================== group List ==========================
	// ========================================================================
	boolean isGroupRightClick;

	private void createGroupListComponents() {
		groupListView = new ListView();
		groupList = new ArrayList();
		groupObservableList = FXCollections.observableArrayList(groupList);

		ChangeListener changeListener = (ChangeListener) new ChangeListener() {

			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				final Map.Entry<String, GroupModel> entry = (Map.Entry<String, GroupModel>) newValue;
				if (entry != null) {
					if (isGroupRightClick) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								viewConversationFGroupId(entry.getValue().getServerGroupId());
							}
						});
					}
				}
			}
		};

		groupListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				isGroupRightClick = false;
				if (!event.isSecondaryButtonDown()) {
					isGroupRightClick = true;
				}
			}
		});

		groupListView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				isGroupRightClick = false;
				if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
					isGroupRightClick = true;
				}
			}
		});

		groupListView.setItems(null);
		groupListView.setItems(groupObservableList);
		groupListView.getSelectionModel().selectedItemProperty().addListener(changeListener);

		groupListView.setCellFactory(
				new Callback<ListView<Map.Entry<String, GroupModel>>, ListCell<Map.Entry<String, GroupModel>>>() {
					@Override
					public ListCell<Map.Entry<String, GroupModel>> call(ListView<Map.Entry<String, GroupModel>> p) {
						ListCell<Map.Entry<String, GroupModel>> cell = new ListCell<Map.Entry<String, GroupModel>>() {
							@Override
							protected void updateItem(Map.Entry<String, GroupModel> t, boolean bln) {
								super.updateItem(t, bln);
								if (t != null) {
									HBox cell = getGroupCell(t.getValue());
									setGraphic(cell);
								} else {
									setGraphic(null);
								}
							}

							private HBox getGroupCell(GroupModel t) {
								// new Thread() {
								// public void run() {
								DBContactsMapper profileMapper = DBContactsMapper.getInstance();
								JSONArray membersJson = t.getMembers();
								String memberString = "";
								for (int i = 0; i < membersJson.length(); i++) {
									try {
										String cid = membersJson.get(i).toString();
										String name = "";
										ContactsModel contactsModel = profileMapper.getContact(cid, 0);
										name = contactsModel.getFirstName();
										if (name != null && name.trim().length() > 0) {
											if (i > 0) {
												memberString = memberString + ", ";
											}
											memberString = memberString + name;
										} else {
											if (i > 0) {
												memberString = memberString + ", ";
											}
											memberString = memberString + cid;
										}
									} catch (Exception e) {
									}
								}
								user_icon_single = ConversationsViewRenderer
										.resize1(AppConfiguration.getUserIconPath() + "ic_action_users_green.png", 48);

								Label lblUserIcon = new Label(" ");
								lblUserIcon.setGraphic(user_icon_single);
								if (searching) {

									String absolutePath = Helper.getAbsolutePath("group_profile");
									Set set = cache1.entrySet();
									Iterator iterator = set.iterator();
									while (iterator.hasNext()) {
										Map.Entry mentry = (Map.Entry) iterator.next();
										if (mentry.getKey().toString().contains(t.getServerGroupId()))

								{
											Platform.runLater(new Runnable() {
												@Override
												public void run() {
													lblUserIcon.setGraphic((Node) mentry.getValue());
												}
											});
										} else {
											lblUserIcon.setGraphic(user_icon_single);
										}

									}

								} else {
									setGroupIcon(t, lblUserIcon);
								}

								Label label = new Label(t.getGroupName());
								label.setId("custom-label-from");
								Label members = new Label(memberString);
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
	}

	private void setGroupIcon(final GroupModel t, final Label lblUserIcon) {
		new Thread() {
			public void run() {

				String absolutePath = Helper.getAbsolutePath("group_profile");
				File[] files = new File(absolutePath).listFiles();
				for (File file : files) {
					if (file.isFile()) {
						String fileName = file.getName();
						if (fileName.startsWith(t.getServerGroupId())) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									ImagePattern img = new ImagePattern(new Image(file.toURI().toString()));
									final Circle clip = new Circle();
									clip.setRadius(21);
									clip.setFill(img);

									cache1.put(file.toURI().toString(), clip);
									lblUserIcon.setGraphic(clip);
								}
							});
							break;
						}
					}
				}
			}
		}.start();
	}

	private void viewConversationFGroupId(String threadId1) {
		addRightConversationPane();
		receipients.clear();
		threadId = threadId1;
		loadConversationViewData(threadId);
		isContactRightClick = false;

		HeaderModel header = new HeaderModel();
		header.setThreadId(threadId);
		header.setGroupId(threadId);
		getConversationDetails(header, 2);
	}

	static boolean isSearch = false;

	@SuppressWarnings("unchecked")
	void addDataToGroupList() {
		try {

			cleanGroupList();
			DBGroupsMapper profileMapper = DBGroupsMapper.getInstance();
			ArrayList groupList1 = profileMapper.getGroupsList();
			if (groupList1 != null) {
				// Sorting
				Collections.sort(groupList1, new Comparator<GroupModel>() {
					@Override
					public int compare(final GroupModel groupModel1, final GroupModel groupModel2) {
						return groupModel1.getGroupName().compareTo(groupModel2.getGroupName());
					}
				});

				ArrayList groupIds = new ArrayList();
				for (Object groupList11 : groupList1) {
					GroupModel gModel = (GroupModel) groupList11;

					if (groupIds.contains(gModel.getServerGroupId()) || gModel.getGroupName().trim().equals("")) {
						continue;
					}

					groupIds.add(gModel.getServerGroupId());
					Map.Entry<String, GroupModel> entry = new AbstractMap.SimpleEntry(gModel.getServerGroupId(),
							gModel);
					groupList.add(entry);
				}

				groupObservableList.setAll(groupList);
				groupListView.setItems(groupObservableList);

			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public synchronized void searchGroup(String oldVal, String newVal) {

		if (oldVal != null && (newVal.length() < oldVal.length())) {
			groupListView.setItems(groupObservableList);
		}

		String value = newVal.toLowerCase();
		try {
			new Thread() {
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							isSearch = true;
							cleanGroupList();
							DBGroupsMapper profileMapper = DBGroupsMapper.getInstance();
							ArrayList groupList1 = profileMapper.getGroupsList();
							if (groupList1 != null) {

								// Sorting
								Collections.sort(groupList1, new Comparator<GroupModel>() {
									@Override
									public int compare(GroupModel groupModel1, GroupModel groupModel2) {
										return groupModel1.getGroupName().compareTo(groupModel2.getGroupName());
									}
								});

								ArrayList groupIds = new ArrayList();
								for (Object groupList11 : groupList1) {

									if (isSearch != true) {
										break;
									}
									GroupModel gModel = (GroupModel) groupList11;
									DBContactsMapper profile = DBContactsMapper.getInstance();
									JSONArray membersJson = gModel.getMembers();
									String memberString = "";
									for (int i = 0; i < membersJson.length(); i++) {
										if (isSearch != true) {
											break;
										}
										try {
											String cid = membersJson.get(i).toString();
											String name = "";
											ContactsModel contactsModel = profile.getContact(cid, 0);
											name = contactsModel.getFirstName();
											if (name != null && name.trim().length() > 0) {
												if (i > 0) {
													memberString = memberString + ", ";
												}
												memberString = memberString + name;
											} else {
												if (i > 0) {
													memberString = memberString + ", ";
												}
												memberString = memberString + cid;
											}
											if (gModel.getGroupName().toLowerCase().contains(value)
													|| memberString.toLowerCase().contains(value)) {

												if (groupIds.contains(gModel.getServerGroupId())
														|| gModel.getGroupName().trim().equals("")) {
													continue;
												}
												groupIds.add(gModel.getServerGroupId());
												Map.Entry<String, GroupModel> entry = new AbstractMap.SimpleEntry(
														gModel.getServerGroupId(), gModel);
												groupList.add(entry);
											}
										} catch (Exception e) {
										}
									}

								}
								searching = true;
								groupObservableList.setAll(groupList);
								groupListView.setItems(groupObservableList);
							}
							isSearch = false;
						}
					});
				}
			}.start();

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
	boolean isContactRightClick;

	private void createContactListComponents() {
		contactListView = new ListView();
		contactList = new ArrayList();
		contactObservableList = FXCollections.observableArrayList(contactList);

		ChangeListener changeListener = (ChangeListener) new ChangeListener() {

			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				final Map.Entry<String, ContactsModel> entry = (Map.Entry<String, ContactsModel>) newValue;
				if (entry != null) {
					if (isContactRightClick) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								viewConversationFContact(entry.getValue().getCashewnutId());
							}
						});
					}
				}
			}
		};

		contactListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

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
								} else {
									setGraphic(null);
								}
							}

							private HBox getGroupCell(ContactsModel t) {
								DBContactsMapper profileMapper = DBContactsMapper.getInstance();
								String memberString = "";
								Label label = new Label();
								Label members = new Label();

								try {
									String cid = t.getCashewnutId();
									String name = "";
									label = new Label(memberString);
									ContactsModel contactsModel = profileMapper.getContact(cid, 0);
									name = contactsModel.getFirstName();
									if (name != null && name.trim().length() > 0) {
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
										.resize1(AppConfiguration.getIconPath() + "ic_cashew.png", 42);
								Label lblUserIcon = new Label(" ");
								lblUserIcon.setGraphic(user_icon_single);
								CircularUserIcon lblUserIcon1 = new CircularUserIcon("NS");
								Text text = (Text) lblUserIcon1.getChildren().get(1);
								text.setText(memberString.charAt(0) + "");
								label.setId("custom-label-from");
								members.setId("custom-label-from");
								VBox cell = new VBox(label, members);
								cell.setSpacing(4);
								cell.setAlignment(Pos.CENTER_LEFT);
								cell.setPrefWidth(200);
								cell.setPadding(new Insets(0, 10, 0, 2));
								cell.setPrefHeight(60);
								HBox mainPanel=null;
								if(t.getCashewnutId().equalsIgnoreCase("CASHEW"))									
								{
									 mainPanel = new HBox(lblUserIcon, cell);	
								}
								else
								{
									 mainPanel = new HBox(lblUserIcon1, cell);
								}
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

	public void viewConversationFContact(String cashewnutId1) {
		addRightConversationPane();
		receipients.clear();
		receipients.add(cashewnutId1);
		threadId = ThreadIdGenerator.getThreadId(cashewnutId, receipients);
		loadConversationViewData(threadId);
		isContactRightClick = false;
		setRightPane();
		HeaderModel header = new HeaderModel();
		header.setMessageFrom(cashewnutId1);
		header.setThreadId(threadId);
		getConversationDetails(header, 3);
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
					public int compare(ContactsModel groupModel1, ContactsModel groupModel2) {
						return groupModel1.getFirstName().compareTo(groupModel2.getFirstName());
					}
				});

				ArrayList contactIds = new ArrayList();
				for (Object groupList11 : groupList1) {
					ContactsModel gModel = (ContactsModel) groupList11;
					if (contactIds.contains(gModel.getContactId())) {
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
			// e.printStackTrace();
		}
	}

	public void search(String oldVal, String newVal) {
		if (oldVal != null && (newVal.length() < oldVal.length())) {
			contactListView.setItems(contactObservableList);
		}
		String value = newVal.toLowerCase();
		try {
			cleanContactList();
			DBContactsMapper profileMapper = DBContactsMapper.getInstance();
			ArrayList groupList1 = profileMapper.getContactNameAndIDList();
			if (groupList1 != null) {

				// Sorting
				Collections.sort(groupList1, new Comparator<ContactsModel>() {
					@Override
					public int compare(ContactsModel groupModel1, ContactsModel groupModel2) {
						return groupModel1.getFirstName().compareTo(groupModel2.getFirstName());
					}
				});

				ArrayList contactIds = new ArrayList();
				for (Object groupList11 : groupList1) {
					ContactsModel gModel = (ContactsModel) groupList11;
					if ((gModel.cashewnutId).toLowerCase().contains(value)
							|| (gModel.firstName).toLowerCase().contains(value)) {
						if (contactIds.contains(gModel.getContactId())) {
							continue;
						}
						contactIds.add(gModel.getContactId());
						Map.Entry<String, ContactsModel> entry = new AbstractMap.SimpleEntry(gModel.getCashewnutId(),
								gModel);
						contactList.add(entry);
					}
				}
				contactObservableList.setAll(contactList);
				contactListView.setItems(contactObservableList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * DBContactsMapper profileMapper = DBContactsMapper.getInstance();
		 * ArrayList groupList1 = profileMapper.getContactNameAndIDList();
		 * ArrayList contactIds = new ArrayList(); for (Object groupList11 :
		 * groupList1) { ContactsModel gModel = (ContactsModel) groupList11; if
		 * (contactIds.contains(gModel.getContactId())) { continue; } boolean
		 * match = true; contactIds.add(gModel.getContactId());
		 * Map.Entry<String, ContactsModel> entry = new
		 * AbstractMap.SimpleEntry(gModel.getCashewnutId(), gModel); if
		 * (!entry.getKey().equals(newVal)) { match = false; break; } if (match)
		 * { subentries.addAll(entryText); } contactList.add(entry); }
		 * ObservableList<Entry<String, ContactsModel>> subentries =
		 * FXCollections.observableArrayList(); for (Object entry :
		 * contactListView.getItems()) { boolean match = true; Map<String,
		 * ContactsModel>[] entryText = (Map<String, ContactsModel>[])entry;
		 * 
		 * 
		 * } contactListView.setItems(subentries);
		 */
	}

	void cleanContactList() {
		contactListView.getItems().clear();
		contactObservableList.clear();
		contactList.clear();
	}

	AddGroup chooseaddgroup = null;
	AddContact chooseaddcontact = null;
	ContactRequest contactRequest = null;

	public void addContact() {

		if (chooseaddcontact != null && chooseaddcontact.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);
		Stage stage = (Stage) toolBar.getScene().getWindow();

		chooseaddcontact = new AddContact(stage, viewRightPane.getWidth(), viewRightPane.getHeight());

		chooseaddcontact.showAndWait();

		splitPane.setOpacity(1);
		addDataToContactList();
	}

	public void addGroup() {
		if (chooseaddgroup != null && chooseaddgroup.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);
		Stage stage = (Stage) toolBar.getScene().getWindow();

		chooseaddgroup = new AddGroup(stage, viewRightPane.getWidth(), viewRightPane.getHeight(), "");
		chooseaddgroup.showAndWait();

		String groupServerId = chooseaddgroup.getGroupId();
		if (groupServerId != null && !groupServerId.equals("")) {
			// System.out.println(groupServerId);
			viewConversationFGroupId(groupServerId);

			if (listBorderPane.getCenter() == groupListView) {
				// /update group chat list
				setStyle(groupImgeButton);
				addDataToGroupList();
			}

		}
		splitPane.setOpacity(1);
	}

	public void contactRequest() {
		if (contactRequest != null && contactRequest.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);
		Stage stage = (Stage) toolBar.getScene().getWindow();
		contactRequest = new ContactRequest(stage, viewRightPane.getWidth(), viewRightPane.getHeight());
		contactRequest.showAndWait();
		splitPane.setOpacity(1);
	}

	MessageSettingsDialog chooseStage = null;
	MessageComposeDialog chooseComposeStage = null;
	ForwardMessage chooseForwardStage = null;

	public void createContent() {
		if (chooseStage != null && chooseStage.isShowing()) {
			chooseStage.close();
			return;
		}

		splitPane.setOpacity(0.8);
		Stage stage = (Stage) settingsImageButton.getScene().getWindow();

		Bounds localBounds = settingsImageButton.localToScene(settingsImageButton.getBoundsInLocal());
		double x = stage.getX() + localBounds.getMaxX() + listLeftPane.getWidth() + settingsImageButton.getWidth();
		double y = stage.getY() + localBounds.getMaxY() - 120;

		chooseStage = new MessageSettingsDialog(controlParameters, stage, x, y);		
		chooseStage.showAndWait();
		splitPane.setOpacity(1);
	}

	EmojiPopUp popupStage = null;

	public void createEmojiContent() throws IOException {
		if (popupStage != null && popupStage.isShowing()) {
			popupStage.close();
			return;
		}
		// CashewnutActivity.recentEmoji=false;
		splitPane.setOpacity(0.8);
		Stage stage = (Stage) emojiButton.getScene().getWindow();
		Bounds localBounds = emojiButton.localToScene(emojiButton.getBoundsInLocal());
		double x = stage.getX() + localBounds.getMaxX() + listLeftPane.getWidth() + emojiButton.getWidth();
		double y = stage.getY() + localBounds.getMaxY() - 120;
		Popup popup = new Popup();
		popup.centerOnScreen();
		popupStage = new EmojiPopUp(stage, x, y);
		popup.setX(600);
		popup.setY(120);
		popup.getContent().addAll(popupStage.EmojiBorder);
		popup.show(popupStage.getOwner());
		ToolBar tb = (ToolBar) popupStage.EmojiBorder.getTop();
		popupStage.EmojiTabPane = (TabPane) popupStage.EmojiBorder.getChildren().get(0);
		popupStage.RecentAnchor = (AnchorPane) popupStage.EmojiTabPane.getTabs().get(1).getContent();
		popupStage.peopleGrid = (GridPane) popupStage.RecentAnchor.getChildren().get(0);
		popupStage.ObjectGrid = (GridPane) popupStage.objectAnchor.getChildren().get(0);
		popupStage.NatureGrid = (GridPane) popupStage.natureAnchor.getChildren().get(0);
		popupStage.PlacesGrid = (GridPane) popupStage.placesAnchor.getChildren().get(0);
		popupStage.SymbolGrid = (GridPane) popupStage.symbolAnchor.getChildren().get(0);
		popupStage.closeButtonEmoji.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				popup.hide();
			}
		});

		popupStage.peopleGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : popupStage.peopleGrid.getChildren()) {
					if (node instanceof ImageView) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							for (Entry<String, Image> entry : Emoji.sEmojisMap.entrySet()) {
								if (entry.getValue().equals(((ImageView) node).getImage())) {
									emojitoImage(entry.getKey(), entry.getValue());
									break;
								}
							}
						}
					}

				}
			}
		});
		popupStage.ObjectGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : popupStage.ObjectGrid.getChildren()) {
					if (node instanceof ImageView) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							// System.out.println( "Node: " + node + " at " +
							// GridPane.getRowIndex( node) + "/" +
							// GridPane.getColumnIndex( node));
							for (Entry<String, Image> entry : Emoji.sEmojisMap.entrySet()) {
								if (entry.getValue().equals(((ImageView) node).getImage())) {
									emojitoImage(entry.getKey(), entry.getValue());
									break;
								}
							}
						}
					}

				}
			}
		});

		popupStage.NatureGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : popupStage.NatureGrid.getChildren()) {
					if (node instanceof ImageView) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							// System.out.println( "Node: " + node + " at " +
							// GridPane.getRowIndex( node) + "/" +
							// GridPane.getColumnIndex( node));
							for (Entry<String, Image> entry : Emoji.sEmojisMap.entrySet()) {
								if (entry.getValue().equals(((ImageView) node).getImage())) {
									emojitoImage(entry.getKey(), entry.getValue());
									break;
								}
							}
						}
					}

				}
			}
		});
		popupStage.PlacesGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : popupStage.PlacesGrid.getChildren()) {
					if (node instanceof ImageView) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							// System.out.println( "Node: " + node + " at " +
							// GridPane.getRowIndex( node) + "/" +
							// GridPane.getColumnIndex( node));
							for (Entry<String, Image> entry : Emoji.sEmojisMap.entrySet()) {
								if (entry.getValue().equals(((ImageView) node).getImage())) {
									emojitoImage(entry.getKey(), entry.getValue());
									break;
								}
							}
						}
					}

				}
			}
		});
		popupStage.SymbolGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : popupStage.SymbolGrid.getChildren()) {
					if (node instanceof ImageView) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							// System.out.println( "Node: " + node + " at " +
							// GridPane.getRowIndex( node) + "/" +
							// GridPane.getColumnIndex( node));
							for (Entry<String, Image> entry : Emoji.sEmojisMap.entrySet()) {
								if (entry.getValue().equals(((ImageView) node).getImage())) {
									emojitoImage(entry.getKey(), entry.getValue());
									break;
								}
							}
						}
					}

				}
			}
		});
		popupStage.RecentGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : popupStage.RecentGrid.getChildren()) {
					if (node instanceof ImageView) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							// System.out.println( "Node: " + node + " at " +
							// GridPane.getRowIndex( node) + "/" +
							// GridPane.getColumnIndex( node));
							for (Entry<String, Image> entry : Emoji.RecentsEmojisMap.entrySet()) {
								if (entry.getValue().equals(((ImageView) node).getImage())) {
									try {
										int sil = Integer.decode(entry.getKey());
										StringBuilder htmlText = new StringBuilder(
												htmlEditor.getHtmlText().replace("</p>", ""));
										String imgContent = "<img src=\""
												+ getClass().getResource("/resources/loment_icons/emojis/emoji_"
														+ entry.getKey().substring(2) + ".png")
												+ "\" width=\"18\" height=\"20\" >";
										htmlText.append(imgContent);
										htmlEditor.setHtmlText(htmlText.substring(0));
										break;
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

								}
							}
						}
					}

				}

			}
		});

		splitPane.setOpacity(1);
	}

	public void recentEmoji(String entry, Image img) {
		String absolutePath = Helper.getAbsolutePath("RecentEmojis");
		BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
		String decimal = null;
		try {
			decimal = String.format("%010x", new BigInteger(1, entry.getBytes("UTF-32")));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String sourceFilePath = absolutePath + "0x" + decimal.replaceFirst("^0+(?!$)", "") + ".png";
		File f = new File(sourceFilePath);
		try {
			ImageIO.write(bImage, "png", f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		popupStage.setRecentEmoji();
	}

	public void emojitoImage(String emojiKey, Image emojiImage) {
		String image = "";
		try {

			image = "0x" + String.format("%010x", new BigInteger(1, emojiKey.getBytes("UTF-32")))
					.replaceFirst("^0+(?!$)", "");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		StringBuilder htmlText = new StringBuilder(htmlEditor.getHtmlText().replace("</p>", ""));
		String imgContent = "<img src=\""
				+ getClass().getResource("/resources/loment_icons/emojis/emoji_" + image.substring(2) + ".png")
				+ "\" width=\"18\" height=\"20\" >";
		htmlText.append(imgContent);
		htmlEditor.setHtmlText(htmlText.substring(0));
		composeTextArea.appendText(emojiKey);
		recentEmoji(emojiKey, emojiImage);
	}

	public void newCompose() {
		if (chooseComposeStage != null && chooseComposeStage.isShowing()) {
			// chooseComposeStage.close();
			return;
		}
		splitPane.setOpacity(0.8);

		boolean isContainsCompose = false;
		if (rightBorderPane.getBottom() != null && rightBorderPane.getBottom().equals(composeBorderPane)) {
			isContainsCompose = true;
		}

		if (isContainsCompose) {
			setComposeBorderPane(false);
		}

		Stage stage = (Stage) toolBar.getScene().getWindow();

		chooseComposeStage = new MessageComposeDialog(stage, viewRightPane.getWidth(), viewRightPane.getHeight());
		// chooseComposeStage.borderPane.getChildren();
		chooseComposeStage.showAndWait();
		splitPane.setOpacity(1);

		if (isContainsCompose) {
			setComposeBorderPane(true);
			disabledComposeIsNotGroupMember();
		}
	}

	public void forward(String message, String attachment) throws UnsupportedEncodingException {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		if (chooseForwardStage != null && chooseForwardStage.isShowing()) {
			return;
		}
		splitPane.setOpacity(0.6);
		Stage stage = (Stage) toolBar.getScene().getWindow();

		chooseForwardStage = new ForwardMessage(stage, viewRightPane.getWidth(), viewRightPane.getHeight());
		chooseForwardStage.showAndWait();
		splitPane.setOpacity(1);
		String image = "";
		String imgContent = "";
		StringBuilder forwardMessage = new StringBuilder("");
		String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
		byte[] utf8 = message.getBytes("UTF-8");
		String string1 = new String(utf8, "UTF-8");
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(string1);
		while (matcher.find()) {
			if (message.contains(matcher.group())) {
				List<String> strings = new ArrayList<String>();
				int index = 0;
				StringBuilder stb = new StringBuilder();
				while (index < matcher.group().length()) {
					strings.add(matcher.group().substring(index, Math.min(index + 2, matcher.group().length())));
					String ss = matcher.group().substring(index, Math.min(index + 2, matcher.group().length()));
					image = String.format("%010x", new BigInteger(1, ss.getBytes("UTF-32"))).replaceFirst("^0+(?!$)",
							"");
					imgContent = "<img src=\""
							+ getClass().getResource("/resources/loment_icons/emojis/emoji_" + image + ".png")
							+ "\" width=\"18\" height=\"20\" >";
					stb.append(imgContent);
					index += 2;
				}
				if (message.contains(matcher.group())) {
					message = message.replace(matcher.group(), stb.toString());
				}
			}

		}
		if (chooseForwardStage.getMessageForwardType() == 0) {
			// contact
			String cashewnutId1 = chooseForwardStage.getMessageForwardTo();
			viewConversationFContact(cashewnutId1);
			htmlEditor.setHtmlText(message);		
			if (attachment != null && !attachment.trim().equalsIgnoreCase("")) {
				try {
					setAttachmentToComposeLayout(attachment);
				} catch (Exception e) {
				}
			}
		}

		if (chooseForwardStage.getMessageForwardType() == 1) {
			String threadId1 = chooseForwardStage.getMessageForwardTo();
			viewConversationFGroupId(threadId1);
			htmlEditor.setHtmlText(message);
			if (attachment != null && !attachment.trim().equalsIgnoreCase("")) {
				try {
					setAttachmentToComposeLayout(attachment);
				} catch (Exception e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	// ==========================================================================
	@Override
	public void start(final Stage stage) throws Exception {
		this.stage = stage;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("fxml_list.fxml"));
					// scene = new Scene(root, 900, 600);
					Platform.setImplicitExit(true);
					try {
						if (!stage.isShowing()) {
							stage.initStyle(StageStyle.UNDECORATED);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					scene = stage.getScene();
					if (scene == null) {
						scene = new Scene(root, 1000, 600);
						stage.setScene(scene);
						stage.setResizable(true);
					} else {
						stage.getScene().setRoot(root);
						scene.getStylesheets().clear();
						stage.setResizable(true);
					}

					scene.getStylesheets().add(ConversationView.class.getResource("background.css").toExternalForm());
					stage.setScene(scene);
					stage.setResizable(true);
					stage.show();
					ResizeListener listener = new ResizeListener();
					scene.setOnMouseMoved(listener);
					scene.setOnMousePressed(listener);
					scene.setOnMouseDragged(listener);
				} catch (Exception ex) {
				}
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					if (listLeftPane != null) {
						splitPane.setDividerPositions(0.4, 0.8);
						SplitPane.setResizableWithParent(listLeftPane, Boolean.TRUE);

						ImageView icon = ConversationsViewRenderer.getImageView(new Image(ConversationView.class
								.getResourceAsStream(AppConfiguration.getAppLogoPath() + "cashew48.png")), 30);

						splitPane.setStyle("-fx-font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");

						try {
							SettingsModel model = new SettingsStore().getSettingsData();
							if (model != null) {
								String selectedFamily = model.getFontFamily();
								if (selectedFamily != null && !selectedFamily.trim().equals("")) {
									splitPane.setStyle("-fx-font-family: " + selectedFamily + ";");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						Region spacer = new Region();
						HBox.setHgrow(spacer, Priority.ALWAYS);
						spacer.setMinWidth(Region.USE_PREF_SIZE);

						toolBar.getItems().add(spacer);
						toolBar.getItems().add(icon);
						chatTitleBarLabel.setId("custom-label-title");
						setToolbarComponents();
						setBorderColour();
						setImages();
						setComposeTextAreaHeight();

						init1();

						splitPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent me) {
								CashewnutActivity.idleTime = System.currentTimeMillis();
								splitPane.getScene().setCursor(Cursor.DEFAULT);
							}
						});

						toolBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent me) {
								CashewnutActivity.idleTime = System.currentTimeMillis();
								toolBar.getScene().setCursor(Cursor.DEFAULT);
							}
						});

						leftOptionVBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent me) {
								// CashewnutActivity.idleTime =
								// System.currentTimeMillis();
								leftOptionVBox.getScene().setCursor(Cursor.DEFAULT);
							}
						});

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		setStrings();
	}

	private void setBorderColour() {
		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:" + AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:" + AppConfiguration.getBorderColour() + ";");
		chatTitleBar.setStyle("-fx-background-color:" + AppConfiguration.getConvTitlebarColour() + ";");
		// borderPane.USE_PREF_SIZE;
	}

	private void setImages() {
		messageSyncImage1.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_reload.png")));
		messageSyncImage11.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_add.png")));
		settingsImageView1.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_gear.png")));
		emojiButtonImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60a.png")));
		settingsImageView11.setImage(new Image(ConversationView.class
				.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_attachment.png")));
		settingsImageView111.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_send.png")));
		listImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_chat.png")));
		writeImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_sms.png")));
		contactImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getUserIconPath() + "ic_action_user.png")));
		groupImageView.setImage(new Image(ConversationView.class
				.getResourceAsStream(AppConfiguration.getUserIconPath() + "ic_action_users.png")));
		settingsImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_gear.png")));
		signoutImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_logout.png")));
		copyImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_copy1.png")));
		deleteImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_trash1.png")));
		multiSelectImageView.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "ic_menu_select_all.png")));
		slideButtonImage.setImage(new Image(ConversationView.class
				.getResourceAsStream(AppConfiguration.getIconPath() + "ic_action_previous_message_light.png")));

		searchImage.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "search.png")));
		ListsearchImage.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "search.png")));
		GroupsearchImage.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "search.png")));
		ConversationsearchImage.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "search.png")));
		conversationSearchCloseImage.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "searchClose.png")));

		searchCloseImage.setImage(new Image(
				ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "searchClose.png")));

		requestImage.setImage(new Image(ConversationView.class
				.getResourceAsStream(AppConfiguration.getUserIconPath() + "ic_action_users_green.png")));
	}

	int composeTextAreaRowHeight = 2;
	SimpleIntegerProperty simpleIntegerProperty = new SimpleIntegerProperty(20);

	private void setComposeTextAreaHeight() {
		htmlEditor.prefHeightProperty().bindBidirectional(simpleIntegerProperty);
		htmlEditor.minHeightProperty().bindBidirectional(simpleIntegerProperty);
		if(htmlEditor.getHeight()>100)
		{
			htmlEditor.setPrefHeight(80);
		}
		composeTextArea.scrollTopProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number old, Number newval) {
				if (newval.intValue() > composeTextAreaRowHeight && simpleIntegerProperty.get() < 150) {
					int val = simpleIntegerProperty.get() + newval.intValue();
					simpleIntegerProperty.setValue(val);
					System.out.println(simpleIntegerProperty.get());
					//htmlEditor.getOnScrollStarted()
				}
			}
		});

		composeTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// this will run whenever text is changed
				if (composeTextArea.getText().equals("")) {
					simpleIntegerProperty.setValue(20);
				}
			}
		});
	}

	private void hideHTMLEditorToolbars() {
		new Thread() {
			public void run() {

				Platform.runLater(() -> {

					Node[] nodes = htmlEditor.lookupAll(".tool-bar").toArray(new Node[0]);
					for (Node node : nodes) {
						node.setVisible(false);
						node.setManaged(false);
					}
					htmlEditor.setVisible(true);
					htmlEditor.setMaxHeight(100);
					
			           
					
				});
			}
		}.start();
	}

	private void setToolbarComponents() {
		double r = 10.0;

		maximizeImageView.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "maximize.png")));

		minimizeImageView.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "minimize.png")));

		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);
		closeImageView.setImage(
				new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getIconPath() + "close.png")));
		closeImageView.getStyleClass().add("closeButtonCss");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				close();
			}
		});

		maximizeButton.setShape(new Circle(r));
		maximizeButton.setMinSize(2 * r, 2 * r);
		maximizeButton.setMaxSize(2 * r, 2 * r);
		maximizeButton.getStyleClass().add("closeButtonCss");
		maximizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				if (!Platform.isFxApplicationThread()) // Ensure on correct
														// thread else
				// hangs X under Unbuntu
				{
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								_maximize();
							} catch (Exception e) {

							}
						}
					});
				} else {
					try {
						_maximize();
					} catch (Exception e) {

					}
				}
			}
		});

		minimizeButton.setShape(new Circle(r));
		minimizeButton.setMinSize(2 * r, 2 * r);
		minimizeButton.setMaxSize(2 * r, 2 * r);
		minimizeButton.getStyleClass().add("closeButtonCss");
		minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				minimize();
			}
		});

		toolBar.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
				isMousePressed = true;

			}
		});
		toolBar.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				isMousePressed = false;
			}
		});

		toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isMousePressed) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					Stage stage = (Stage) toolBar.getScene().getWindow();
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			}
		});
	}

	private boolean isMaximize = false;
	private Rectangle2D backupWindowBounds;
	boolean isMac = false;

	private void _maximize() {
		Stage stage = (Stage) toolBar.getScene().getWindow();
		final double stageY = isMac ? stage.getY() - 22 : stage.getY(); // TODO
																		// Workaround
																		// for
																		// RT-13980
		final Screen screen = Screen.getScreensForRectangle(stage.getX(), stageY, 1, 1).get(0);
		Rectangle2D bounds = screen.getVisualBounds();
		if (bounds.getMinX() == stage.getX() && bounds.getMinY() == stageY && bounds.getWidth() == stage.getWidth()
				&& bounds.getHeight() == stage.getHeight()) {
			if (backupWindowBounds != null) {
				stage.setX(backupWindowBounds.getMinX());
				stage.setY(backupWindowBounds.getMinY());
				stage.setWidth(backupWindowBounds.getWidth());
				stage.setHeight(backupWindowBounds.getHeight());
				isMaximize = false;
			}
		} else {
			backupWindowBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
			final double newStageY = isMac ? screen.getVisualBounds().getMinY() + 22
					: screen.getVisualBounds().getMinY(); // TODO Workaround for
															// RT-13980
			stage.setX(screen.getVisualBounds().getMinX());
			stage.setY(newStageY);
			stage.setWidth(screen.getVisualBounds().getWidth());
			stage.setHeight(screen.getVisualBounds().getHeight());
			isMaximize = true;
			// btnMaximize.setGraphic(imgOriSize);
		}
	}

	public void close() {
		final Stage stage = (Stage) toolBar.getScene().getWindow();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
				System.exit(0);
			}
		});

	}

	public void minimize() {

		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					_minimize();
				}
			});
		} else {
			_minimize();
		}
	}

	private void _minimize() {
		Stage stage = (Stage) toolBar.getScene().getWindow();
		stage.setIconified(true);
	}

	class ResizeListener implements EventHandler<MouseEvent> {

		double dx;
		double dy;
		double deltaX;
		double deltaY;
		double border = 10;
		boolean moveH;
		boolean moveV;
		boolean resizeH = false;
		boolean resizeV = false;

		@Override
		public void handle(MouseEvent t) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			if (MouseEvent.MOUSE_MOVED.equals(t.getEventType())) {
				if (t.getX() < border && t.getY() < border) {
					scene.setCursor(Cursor.NW_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = true;
					moveV = true;
				} else if (t.getX() < border && t.getY() > scene.getHeight() - border) {
					scene.setCursor(Cursor.SW_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = true;
					moveV = false;
				} else if (t.getX() > scene.getWidth() - border && t.getY() < border) {
					scene.setCursor(Cursor.NE_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = false;
					moveV = true;
				} else if (t.getX() > scene.getWidth() - border && t.getY() > scene.getHeight() - border) {
					scene.setCursor(Cursor.SE_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = false;
					moveV = false;
				} else if (t.getX() < border || t.getX() > scene.getWidth() - border) {
					scene.setCursor(Cursor.E_RESIZE);
					resizeH = true;
					resizeV = false;
					moveH = (t.getX() < border);
					moveV = false;
				} else if (t.getY() < border || t.getY() > scene.getHeight() - border) {
					scene.setCursor(Cursor.N_RESIZE);
					resizeH = false;
					resizeV = true;
					moveH = false;
					moveV = (t.getY() < border);
				} else {
					scene.setCursor(Cursor.DEFAULT);
					resizeH = false;
					resizeV = false;
					moveH = false;
					moveV = false;
				}
			} else if (MouseEvent.MOUSE_PRESSED.equals(t.getEventType())) {
				dx = stage.getWidth() - t.getX();
				dy = stage.getHeight() - t.getY();
			} else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {
				if (resizeH && resizeV) {
					if (stage.getWidth() <= 1000) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							if (t.getX() < 0) {// if new > old, it's permitted
								stage.setWidth(deltaX + stage.getWidth());
								stage.setX(t.getScreenX());
							}
						} else {
							if (t.getX() + dx - stage.getWidth() > 0) {
								stage.setWidth(t.getX() + dx);
							}
						}
					} else if (stage.getWidth() > 600) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							stage.setWidth(deltaX + stage.getWidth());
							stage.setX(t.getScreenX());
						} else {
							stage.setWidth(t.getSceneX() + dx);
						}
					}

					if (stage.getHeight() <= 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							if (t.getY() < 0) {// if new > old, it's permitted
								stage.setHeight(deltaY + stage.getHeight());
								stage.setY(t.getScreenY());
							}
						} else {
							if (t.getY() + dy - stage.getHeight() > 0) {
								stage.setHeight(t.getY() + dy);
							}
						}
					} else if (stage.getHeight() > 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							stage.setHeight(deltaY + stage.getHeight());
							stage.setY(t.getScreenY());
						} else {
							stage.setHeight(t.getY() + dy);
						}
					}
				} else if (resizeH) {
					if (stage.getWidth() <= 1000) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							if (t.getX() < 0) {// if new > old, it's permitted
								stage.setWidth(deltaX + stage.getWidth());
								stage.setX(t.getScreenX());
							}
						} else {
							if (t.getX() + dx - stage.getWidth() > 0) {
								stage.setWidth(t.getX() + dx);
							}
						}
					} else if (stage.getWidth() > 600) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							stage.setWidth(deltaX + stage.getWidth());
							stage.setX(t.getScreenX());
						} else {
							stage.setWidth(t.getX() + dx);
						}
					}
				} else if (resizeV) {
					if (stage.getHeight() <= 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							if (t.getY() < 0) {// if new > old, it's permitted
								stage.setHeight(deltaY + stage.getHeight());
								stage.setY(t.getScreenY());
							}
						} else {
							if (t.getY() + dy - stage.getHeight() > 0) {
								stage.setHeight(t.getY() + dy);
							}
						}
					} else if (stage.getHeight() > 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							stage.setHeight(deltaY + stage.getHeight());
							stage.setY(t.getScreenY());
						} else {
							stage.setHeight(t.getY() + dy);
						}
					}
				}
			}
		}
	}
	@FXML
	private ImageView listImageView;
	@FXML
	private ImageView writeImageView;
	@FXML
	private ImageView contactImageView;
	@FXML
	private ImageView groupImageView;
	@FXML
	private ImageView settingsImageView;
	@FXML
	private ImageView signoutImageView;
	@FXML
	private AnchorPane leftSyncHbox;
	@FXML
	private AnchorPane listSearchAnchorPane;
	@FXML
	private ImageView messageSyncImage11;
	@FXML
	private ImageView messageSyncImage1;
	@FXML
	private ImageView searchImage;
	@FXML
	private ImageView ListsearchImage;
	@FXML
	private ImageView GroupsearchImage;
	@FXML
	private ImageView ConversationsearchImage;
	@FXML
	private ImageView searchCloseImage;
	@FXML
	private BorderPane chatTitleBar;
	@FXML
	private ImageView settingsImageView1;
	@FXML
	private ImageView settingsImageView11;
	@FXML
	private ImageView settingsImageView111;
	@FXML
	private ImageView emojiButtonImageView;
	@FXML
	private BorderPane borderPane;
	@FXML
	private ToolBar toolBar;
	@FXML
	private Button closeButton;
	@FXML
	private Button minimizeButton;
	@FXML
	private Button maximizeButton;

	@FXML
	private ImageView maximizeImageView;

	@FXML
	private ImageView minimizeImageView;

	@FXML
	private ImageView closeImageView;
	@FXML
	private Button listImageButton;
	@FXML
	private Button writeImageButton;
	@FXML
	private Button contactImageButton;
	@FXML
	private Button settingsImageButton;
	@FXML
	private Button groupImgeButton;
	@FXML
	private Button signoutImageButton;
	@FXML
	private SplitPane splitPane;
	@FXML
	StackPane listLeftPane;
	@FXML
	StackPane viewRightPane;
	@FXML
	private Button addGroupButton;
	@FXML
	private BorderPane rightBorderPane;

	@FXML
	private BorderPane messageBorderPane;

	@FXML
	private Button composeSettingsImageButton;
	@FXML
	private Button composeAttachmentButton;
	@FXML
	private Button composeSendButton;
	@FXML
	private Button emojiButton;
	/*
	 * @FXML private TextArea composeTextArea;
	 */
	@FXML
	private HBox composeAttachmentHBox;
	@FXML
	private BorderPane composeBorderPane;
	@FXML
	private Label leftListContentLabel;
	@FXML
	private Button messageSyncButton;
	@FXML
	private BorderPane leftChatListBorderPane;
	@FXML
	private BorderPane leftChatListHeaderBorderPane;
	@FXML
	private HBox leftListChatListHBox;
	@FXML
	private VBox leftOptionVBox;
	@FXML
	private Button chatTitleBarLabel;

	@FXML
	private Button copyImageButton;

	@FXML
	private Button deleteImageButton;
	@FXML
	private ImageView deleteImageView;
	@FXML
	private ImageView copyImageView;
	@FXML
	private HBox rightOptionHBox;
	@FXML
	private HBox conversationSearchHBox;
	@FXML
	private Button multiSelectButton;
	@FXML
	private ImageView multiSelectImageView;
	@FXML
	private ImageView slideButtonImage;
	@FXML
	private Button searchButton;
	@FXML
	private Button ListsearchButton;
	@FXML
	private Button GroupsearchButton;
	@FXML
	private Button ConversationsearchButton;
	@FXML
	private TextField search;
	@FXML
	private TextField conversationSearch;
	@FXML
	private AnchorPane mainAnchorPane, contactsAnchorPane, searchButtonAnchorPane;
	@FXML
	private HBox searchHBox;
	@FXML
	private BorderPane searchAnchorPane;
	@FXML
	private BorderPane ConversationSearchAnchorPane;
	@FXML
	private Button conversationsearchCloseButton;
	@FXML
	private Button searchCloseButton;
	@FXML
	private ImageView conversationSearchCloseImage;
	@FXML
	private Button ContactRequestButton;
	@FXML
	private ImageView requestImage;
	@FXML
	private HTMLEditor htmlEditor;
	boolean isMousePressed = false;
	private double xOffset = 0;
	private double yOffset = 0;
	Scene scene;
	Stage stage;

	/**
	 * Animates a node on and off screen to the left.
	 */
	class SideBar extends BorderPane {
		SideBar sideBar = this;

		SideBar(final double expandedWidth, Button controlButton) {
			controlButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					isRightProfilePane = false;

					if (conversationGroupId != null && !conversationGroupId.trim().equals("")) {
						// if change group
						addTitle();
					}
					chatTitleBarLabel.setText(titleString.trim());
					setComposeBorderPane(true);
					disabledComposeIsNotGroupMember();

					final Animation hideSidebar = new Transition() {
						{
							setCycleDuration(Duration.millis(250));
						}

						@Override
						protected void interpolate(double frac) {
							final double curWidth = expandedWidth * (1.0 - frac);
							setPrefWidth(curWidth);
							setTranslateX(-expandedWidth + curWidth);
						}
					};
					hideSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
							CashewnutActivity.idleTime = System.currentTimeMillis();
							createSidebarContent();
							setVisible(false);
						}
					});
					// create an animation to show a sidebar.
					final Animation showSidebar = new Transition() {
						{
							setCycleDuration(Duration.millis(300));
						}

						protected void interpolate(double frac) {
							final double curWidth = expandedWidth * frac;
							setPrefWidth(curWidth);
							setTranslateX(-expandedWidth + curWidth);
						}
					};
					showSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
							CashewnutActivity.idleTime = System.currentTimeMillis();

						}
					});
					if (showSidebar.statusProperty().get() == Animation.Status.STOPPED
							&& hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
						if (isVisible()) {

							hideSidebar.play();

						} else {

							messageBorderPane.setCenter(null);
							messageBorderPane.setCenter(sideBar);
							multiSelectButton.setVisible(true);
							conversationSearchHBox.setVisible(true);
							ConversationsearchButton.setVisible(true);
							setVisible(true);
							showSidebar.play();
							checkBox();
							isMultiSelected = false;

						}
					}
				}
			});
		}
	}

	int checkPasswordTrys = 0;

	public void checkPassword(final String uName, final String pWord, final Stage dialogStage,
			final boolean isPrimary) {
		NetworkConnection connection = new NetworkConnection();
		connection.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Boolean isConnected = (Boolean) t.getSource().getValue();
				if (isConnected) {
					final ValidatePasswordTask service = new ValidatePasswordTask(uName, pWord, isPrimary);

					new Thread() {
						public void run() {
							String response = service.execute();
							final int val = service.onPostExecute(response);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									if (dialogStage != null) {
										dialogStage.close();
									}
									if (val == 0) {
									}
									if (val == 1) {
										RPCClientSender.getInstance().close();
										ReceiverHandler.getInstance().syncMessages();
									}
									if (val == 2) {
										String dialog = "";
										if (checkPasswordTrys == 0) {
											dialog = AppConfiguration.appConfString.conversation_password_incorrect;
											checkPasswordTrys++;
										} else {
											dialog = AppConfiguration.appConfString.conversation_password_incorrect1;
										}
										splitPane.setOpacity(0.5);
										Stage dialogStage = getDialog(dialog);
										dialogStage.showAndWait();
										splitPane.setOpacity(1);
									}
								}
							});
						}
					}.start();
				} else {
					// labelVisible(loginLabel,
					// "Please check your network connection !!");
				}
			}
		});
		connection.start();
	}

	private Stage getDialog(String body) {
		ConversationsViewRenderer renderer = new ConversationsViewRenderer();
		Stage owner = (Stage) toolBar.getScene().getWindow();

		final PasswordField passwordTextField = new PasswordField();
		final Stage dialogStage = new Stage();

		dialogStage.initOwner(owner);

		Button ok = new Button(AppConfiguration.appConfString.dlg_ok);
		Button cancel = new Button(AppConfiguration.appConfString.dlg_cancel);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				String val = passwordTextField.getText();
				if (!val.trim().equals("")) {
					checkPassword(lomentId, val, dialogStage, true);
					dialogStage.close();
				}
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

				dialogStage.close();
				logout();
			}
		});

		HBox hbox = HBoxBuilder.create().children(ok, new Label("      "), cancel).alignment(Pos.CENTER)
				.padding(new Insets(10)).build();

		VBox vbox = VBoxBuilder.create()
				.children(createText(body), new Label("      "), passwordTextField, new Label("      "), hbox)
				.alignment(Pos.CENTER).padding(new Insets(10)).build();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);

		GridPane gridpane = new GridPane();
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(10);
		gridpane.setVgap(10);

		final String cssDefault = "-fx-border-color: #A8A8A8;\n" + "-fx-border-width: 6;\n";

		gridpane.add(vbox, 1, 1, 10, 10);
		gridpane.setStyle(cssDefault);
		dialogStage.setScene(new Scene(gridpane));
		return dialogStage;
	}

	private Text createText(String string) {
		ConversationsViewRenderer renderer = new ConversationsViewRenderer();
		Text text = new Text(string);
		text.setWrappingWidth(200);
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setStyle("-fx-font-size: 14; -fx-base: #b6e7c9;");
		return text;
	}

	private void checkSubscription() {
		try {
			NetworkConnection connection = new NetworkConnection();
			connection.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					Boolean isConnected = (Boolean) t.getSource().getValue();
					if (isConnected) {
						new Thread() {
							public void run() {
								JSONObject responseJson = SubscriptionHandler.getInstance()
										.getSubscriptionDataFromSthithi();
								if (responseJson != null && !responseJson.toString().trim().equals(""))
									SubscriptionHandler.getInstance().subscriptionStatus(responseJson, false);
							}
						}.start();
					}
				}
			});
			connection.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String screenShotMessageSender()
	{
		String to="";
		switch (conversatonType) {
		case 1:
			if (conversationGroupId != null && !conversationGroupId.trim().equals("")) {
				to = conversationGroupId.trim();
			} else {
				if (!conversationFrom.equals(cashewnutId)) {
					to = conversationFrom + ",";
				}

				for (int i = 0; i < conversationReceipients.size(); i++) {
					if (conversationFrom.equals(cashewnutId) && conversationReceipients.get(i).equals(cashewnutId)) {
						to += conversationReceipients.get(i) + ",";
					} else {
						if (!conversationReceipients.get(i).equals(cashewnutId))
							to += conversationReceipients.get(i) + ",";
					}
				}
			}
			break;
		case 2:
			to = conversationGroupId.trim();
			break;
		case 3:
			to = conversationFrom;
			break;
			
		}		
return to;
		
	}
	private void checkBox() {

		messageListView.setCellFactory(new Callback<ListView<HeaderModel>, ListCell<HeaderModel>>() {
			@Override
			public ListCell<HeaderModel> call(ListView<HeaderModel> p) {
				ListCell<HeaderModel> cell = new ListCell<HeaderModel>() {
					@Override
					protected void updateItem(final HeaderModel t, boolean bln) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						super.updateItem(t, bln);
						try {
							if (t != null) {
								ConversationsViewRenderer renderer = new ConversationsViewRenderer();
								ConversationPrimer premier;							
								String from = t.getMessageFrom();
								if (cashewnutId.equals(from)) {
									premier = getConversationPrimer(t, MessageModel.MESSAGE_FOLDER_TYPE_SENTBOX);
								} else {
									premier = getConversationPrimer(t, MessageModel.MESSAGE_FOLDER_TYPE_INBOX);
								}
						
								CheckBox chk1 = null;
								status=false;
								if (isMultiSelected) {
								
								
									chk1 = new CheckBox();
									final ConversationPrimer selectedPremier = premier;
									chk1.selectedProperty().addListener(new ChangeListener<Boolean>() {
										@Override
										public void changed(ObservableValue<? extends Boolean> observable,
												Boolean oldValue, Boolean newValue) {
											CashewnutActivity.idleTime = System.currentTimeMillis();
											if (newValue) {
												selectedHeaders.add(selectedPremier);
												status=true;
											} else {
												selectedHeaders.remove(selectedPremier);
												status=false;
											}
										}
									});
								}
								for(ConversationPrimer slected:selectedHeaders)
								{
									if(slected.getDisplayString().equalsIgnoreCase(premier.getDisplayString()))
									{
										status=true;
									}
								}
								
								HBox cell = renderer.getListCellRendererComponent(premier, chk1,status);
								setGraphic(cell);
								cell.setStyle(bgColour);

								if (t.getMessageType() != MessageModel.LOCAL_MESSAGE_TYPE_GROUP
										&& t.getMessageType() != MessageModel.LOCAL_MESSAGE_TIME) {
									ContextMenu rootContextMenu = getContextListMenu(premier, renderer);
									setContextMenu(rootContextMenu);
								}
							} else {
								setGraphic(null);
								setStyle(bgColour);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				return cell;
			}
		});
	}

	public synchronized void conversationsearch(String oldVal, String newVal, String thread) {
		ArrayList<HeaderModel> list1 = new ArrayList<>();
		ConversationPrimer premier;
		String from = "";
		String value = newVal.toLowerCase();

		try {
			HeaderModel header = new HeaderModel();
			for (int i = 0; i < messageList.size(); i++) {
				header = messageList.get(i);
				from = header.getMessageFrom();

				if (cashewnutId.equals(from)) {
					premier = getConversationPrimer(header, MessageModel.MESSAGE_FOLDER_TYPE_SENTBOX);
				} else {
					premier = getConversationPrimer(header, MessageModel.MESSAGE_FOLDER_TYPE_INBOX);
				}

				if (oldVal != null && (newVal.length() < oldVal.length())) {

					if (premier.getDisplayString().toLowerCase().contains(value)) {
						list1.add(header);

					}

				} else if (premier.getDisplayString().toLowerCase().contains(value)) {

					list1.add(header);

				}

			}

			sortMessageList(list1, true);
			myMessageObservableList.setAll(list1);
			messageListView.setItems(myMessageObservableList);

		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	private void conversationListComponents() {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		list.clear();
		listView.setCellFactory(
				new Callback<ListView<Map.Entry<String, ConversationPrimer>>, ListCell<Map.Entry<String, ConversationPrimer>>>() {
					@Override
					public ListCell<Map.Entry<String, ConversationPrimer>> call(
							ListView<Map.Entry<String, ConversationPrimer>> p) {
						ListCell<Map.Entry<String, ConversationPrimer>> cell = new ListCell<Map.Entry<String, ConversationPrimer>>() {
							@Override
							protected void updateItem(Map.Entry<String, ConversationPrimer> t, boolean bln) {

								super.updateItem(t, bln);
								if (t != null) {
									CashewnutActivity.idleTime = System.currentTimeMillis();
									ConversationPrimer message = t.getValue();
									ConversationsListRenderer renderer = new ConversationsListRenderer();
									HBox cell;
									try {
										
										cell = renderer.getListCellRendererComponent(message);
										setGraphic(cell);
										cell.setStyle(bgColour);
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else {
									setGraphic(null);
								}
							}
						};
						return cell;
					}
				});
	}

	public synchronized void conversationListsearch(String oldVal, String newVal) {
		new Thread() {
			public void run() {

				String cashew = "";
				String groupName = "";
				String contactName = "";
				String value = newVal.toLowerCase();
				ConversationPrimer primer = null;
				Map.Entry<String, ConversationPrimer> entry = null;
				list1 = new ArrayList();
				list1.clear();
				for (int i = 0; i < list.size(); i++) {

					entry = list.get(i);
					primer = entry.getValue();
					DBContactsMapper profileMapper = DBContactsMapper.getInstance();
					ContactsModel profileModel = profileMapper.getContact(primer.getLatestMessage().getMessageFrom(),
							0);

					if (cashewnutId.equalsIgnoreCase(profileModel.getCashewnutId())) {
						Vector recipientList = DBRecipientMapper.getInstance()
								.getReceipientsModelByHeaderId(primer.getLatestMessage().getLocalHeaderId());
						for (int index = 0; index < recipientList.size(); index++) {
							RecipientModel recipient = (RecipientModel) recipientList.elementAt(index);
							cashew = recipient.getRecepientCashewnutId();
						}
						contactName = profileMapper.getContact(cashew, 0).firstName;
					} else {
						contactName = profileModel.firstName;
					}
					if (primer.getLatestMessage().getGroupId() != null) {
						DBGroupsMapper groupMapper = DBGroupsMapper.getInstance();
						GroupModel gModel = groupMapper.getGroup(primer.getLatestMessage().getGroupId(), true);
						groupName = gModel.getGroupName();
					}

					if (oldVal != null && (newVal.length() < oldVal.length())) {
						if (primer.getDisplayString().toLowerCase().contains(newVal)
								|| contactName.toLowerCase().contains(newVal)
								|| groupName.toLowerCase().contains(newVal)) {
							if(!(list1.contains(entry)))
							{
								list1.add(entry);
							}
							
						}

					} else if (primer.getDisplayString().toLowerCase().contains(newVal)
							|| contactName.toLowerCase().contains(newVal) || groupName.toLowerCase().contains(newVal)) {
						if(!(list1.contains(entry)))
						{
							list1.add(entry);
						}
						
					}
					

				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						myObservableList.setAll(list1);
						listView.setItems(myObservableList);
					
					}
				});
			}
		}.start();
	}

}
