package com.loment.cashewnut.activity.list;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.timer.Timer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.connection.amqp.AttachmentDataConnection;
import com.loment.cashewnut.crypto.CashewnutMessageCrypter;
import com.loment.cashewnut.database.AttachmentStore;
import com.loment.cashewnut.database.BodyStore;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.model.StatusFlagsModel;
import com.loment.cashewnut.receiver.AttachmentReceiver;
import com.loment.cashewnut.receiver.ReceiverHandler;
import com.loment.cashewnut.util.ContentType;
import com.loment.cashewnut.util.Helper;

/**
 *
 * @author sekhar
 */
public class ConversationsViewRenderer extends TabPane {

	String cashewnutId = "";
	DBContactsMapper profileMapper = null;
	DBRecipientMapper recipientMapper = null;
	private Timer t = new Timer();
	ImageView expiry_iconw = resize1(AppConfiguration.getIconPath() + "ic_action_clock.png", 14);
	ImageView label_expiry_iconw = resize1(AppConfiguration.getIconPath() + "ic_action_clock.png", 14);
	ImageView restricted_iconw = resize1(AppConfiguration.getIconPath() + "ic_action_halt.png", 14);
	ImageView unack_iconr = resize1(AppConfiguration.getIconPath() + "ic_action_lock_closed.png", 14);
	ImageView ack_iconr = resize1(AppConfiguration.getIconPath() + "ic_action_lock_open.png", 14);
	ImageView partialack_icon = resize1(AppConfiguration.getIconPath() + "ic_action_starlock_gray.png", 14);
	ImageView priority_iconr = resize1(AppConfiguration.getIconPath() + "priority_red.png", 14);
	ImageView priority_icony = resize1(AppConfiguration.getIconPath() + "priority_yellow.png", 14);
	ImageView priority_icongy = resize1(AppConfiguration.getIconPath() + "priority_gray.png", 14);
	ImageView priority_icongr = resize1(AppConfiguration.getIconPath() + "priority_green.png", 14);	

	ImageView single_tick_icon = resize1(AppConfiguration.getIconPath() + "ic_action_tick.png", 11);
	ImageView dubble_tick_icon = resize1(AppConfiguration.getIconPath() + "ic_action_tick_2.png", 11);
	ImageView failure_tick_icon = resize1(AppConfiguration.getIconPath() + "ic_action_failure.png", 11);
	ImageView attachment_icon = resize1(AppConfiguration.getIconPath() + "ic_action_attachment.png", 14);

	ImageView user_icon_single = resize1(AppConfiguration.getUserIconPath() + "ic_action_user_48.png", 48);
	ImageView user_icon_single1 = resize1(AppConfiguration.getUserIconPath() + "ic_action_user_48.png", 48);

	ImageView vedioPlayIcon = resize1(AppConfiguration.getDocIconPath() + "playVideo.png", 40);
	ImageView vedioPlay_fullscreen = resize1(AppConfiguration.getDocIconPath() + "ic_full_screen.png", 16);

	ImageView datePurpleLine1 = resizeAttachment(AppConfiguration.getIconPath() + "datePurpleLine.png", 100, 40);
	ImageView datePurpleLine2 = resizeAttachment(AppConfiguration.getIconPath() + "datePurpleLine.png", 100, 40);

	Label lblPriorityIcon = new Label(" ");
	Label lblrestrictedIcon = new Label(" ");
	Label lblExpiryIcon = new Label(" ");
	Label lblACKIcon = new Label(" ");
	Label lblUnAckIcon = new Label(" ");
	Label lblPatialAckIcon = new Label("");
	Label lblDeliveryIcon = new Label(" ");
	Label lblAttachmentIcon = new Label(" ");
	Label lblrecepientString = new Label(" ");
	Label lblDate = new Label(" ");
	Label lblUserLeftIcon = new Label();
	Label lblUserRightIcon = new Label();
	Label lblExpiryTime = new Label("");
	Button attachmentButton = new Button();
	TextFlow messageTextAreaPane = null;
	HBox priorityPane = new HBox();
	VBox messageBodyPane = new VBox();
	VBox middleMessagePane = new VBox();
	HBox bottemDatePanel = new HBox();
	//TextFlow textFlow = new TextFlow();
	ProgressIndicator myProgressIndicator = new ProgressIndicator();
	ProgressBar progressBar = new ProgressBar();
	HashMap <String,Image>sEmojisMap = new HashMap();
	public ConversationsViewRenderer() {
		try {
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel accountsModel = accountsMapper.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
			recipientMapper = DBRecipientMapper.getInstance();
			profileMapper = DBContactsMapper.getInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// lblPriorityIcon.setGraphic(priority_icongy);
		lblrestrictedIcon.setGraphic(restricted_iconw);
		lblACKIcon.setGraphic(ack_iconr);
		lblUnAckIcon.setGraphic(unack_iconr);
		lblPatialAckIcon.setGraphic(partialack_icon);
		lblExpiryIcon.setGraphic(expiry_iconw);
		lblAttachmentIcon.setGraphic(attachment_icon);
		lblUserLeftIcon.setGraphic(user_icon_single);
		lblUserRightIcon.setGraphic(user_icon_single1);
		messageTextAreaPane = new TextFlow();
		bottemDatePanel.setAlignment(Pos.BASELINE_RIGHT);
		myProgressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		progressBar.setPrefSize(100, 15);
		myProgressIndicator.setPrefSize(20, 20);
		progressBar.setStyle("-fx-accent: blue;");
	}
	public HBox getListCellRendererComponent(Object value, CheckBox chk,boolean status) {
		HBox mainPanel = null;
		if (value == null) {
			return mainPanel;
		}

		ConversationPrimer m = (ConversationPrimer) value;
		HeaderModel model = m.getLatestMessage();

		if (model.getMessageType() == MessageModel.LOCAL_MESSAGE_TYPE_GROUP
				|| model.getMessageType() == MessageModel.LOCAL_MESSAGE_TIME) {

			if (model.getPriority() > -1 && model.getMessageType() == MessageModel.LOCAL_MESSAGE_TYPE_GROUP) {
				mainPanel = getGroupMessage(model);
			} else {
				mainPanel = getCashewMessageTimeStamp(model);
			}
		} else if (model.getPriority() > -1 && model.getMessageType() == MessageModel.MESSAGE_TYPE_NEW_GROUPSMESSAGE) {
			mainPanel = getNewGroupMessage(model);
		} else if (model.getMessageType() == MessageModel.MESSAGE_TYPE_CASHEWNUT) {
			mainPanel = getCashewMessage(chk, m, model,status);
		} else if ((model.getMessageType() == MessageModel.MESSAGE_TYPE_WELCOME)) {
			mainPanel = setWelcomeMessageView(model);
		}	
		 else if ((model.getMessageType() == MessageModel.MESSAGE_TYPE_SCREENSHOT)) {
				 mainPanel = getScreenShotMessage(model,m);;
			}
		return mainPanel;
	}
	private HBox setWelcomeMessageView(HeaderModel header) {
		String content = "";
		HBox mainPanel = new HBox(3);
		mainPanel.setAlignment(Pos.TOP_CENTER);
		Button textArea = new Button();
		textArea.setWrapText(true);
		textArea.setMaxWidth(300);
		textArea.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.6em 0.0em 0.0em 0.0em;");
		textArea.setId("custom-label-time");
		Vector recipientList = recipientMapper.getReceipientsModelByHeaderId(header.getLocalHeaderId());
		String messageString = getInMessageString(header, recipientList, header.getSubject());
		textArea.setMnemonicParsing(false);
		textArea.setText(messageString);
		String bubbleImage = ConversationsViewRenderer.class
				.getResource(AppConfiguration.getMessageBubblePath() + "BubbleRight.png").toExternalForm();

		String style = "-fx-background-image: url(\"" + bubbleImage + "\");";
		textArea.setStyle(style);
		mainPanel.getChildren().add(textArea);
		return mainPanel;
	}
	private HBox getCashewMessageTimeStamp(HeaderModel model) {
		String content;
		HBox mainPanel = new HBox(3);
		mainPanel.setAlignment(Pos.TOP_CENTER);
		Button textArea = new Button();
		textArea.setWrapText(true);
		textArea.setMaxWidth(300);
		textArea.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.6em 0.0em 0.0em 0.0em;");
		textArea.setId("custom-label-message");
		content = model.getSubject();
		textArea.setText(content);

		mainPanel.getChildren().add(datePurpleLine1);
		mainPanel.getChildren().add(textArea);
		mainPanel.getChildren().add(datePurpleLine2);
		return mainPanel;
	}

	private HBox getGroupMessage(HeaderModel model) {
		String content = "";
		HBox mainPanel = new HBox(3);
		mainPanel.setAlignment(Pos.TOP_CENTER);
		Button textArea = new Button();
		textArea.setWrapText(true);
		textArea.setMaxWidth(300);
		textArea.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.6em 0.0em 0.0em 0.0em;");
		textArea.setId("custom-label-time");
		content = getGroupMessageBody(model, content);
		textArea.setText(content);

		String bubbleImage = ConversationsViewRenderer.class
				.getResource(AppConfiguration.getMessageBubblePath() + "BubbleRight.png").toExternalForm();

		String style = "-fx-background-image: url(\"" + bubbleImage + "\");";
		textArea.setStyle(style);
		mainPanel.getChildren().add(textArea);
		return mainPanel;
	}
	private HBox getScreenShotMessage(HeaderModel model,final ConversationPrimer m) {
		DBContactsMapper profileMapper = DBContactsMapper.getInstance();
		ContactsModel contactModel=new ContactsModel();
		String content = "";
		HBox mainPanel = new HBox(3);
		mainPanel.setAlignment(Pos.TOP_CENTER);
		Button textArea = new Button();
		textArea.setWrapText(true);
		textArea.setMaxWidth(300);
		textArea.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.6em 0.0em 0.0em 0.0em;");
		textArea.setId("custom-label-time");
		
		if (!model.getMessageFrom().equals(cashewnutId)) {
			// in message
			messageViewModel = getInMessageView(model, m);
		} else {
			// sent message
			messageViewModel = getSentMessageView(model);
		}
		String screenShotMessage="";
		final String message = messageViewModel.getMessage().trim();
		
		if(model.getMessageFrom().equals(cashewnutId))
		{
			
			screenShotMessage="You " +message;
		}
		else
		{
			contactModel=profileMapper.getContact(model.getMessageFrom(),0);
			screenShotMessage=contactModel.getFirstName().concat(" "+message);
		}
		textArea.setText(screenShotMessage);

		String bubbleImage = ConversationsViewRenderer.class
				.getResource(AppConfiguration.getMessageBubblePath() + "BubbleLeft.png").toExternalForm();

		String style = "-fx-background-image: url(\"" + bubbleImage + "\");";
		textArea.setStyle(style);
		
		mainPanel.getChildren().add(textArea);
		return mainPanel;
	}
	
	
	private HBox getNewGroupMessage(HeaderModel model) {
		String content = "";
		HBox mainPanel = new HBox(3);
		mainPanel.setAlignment(Pos.TOP_CENTER);
		Button textArea = new Button();
		textArea.setWrapText(true);
		textArea.setMaxWidth(300);
		textArea.setStyle("-fx-background-radius: 0, 0;" + " -fx-background-color: transparent;"
				+ " -fx-padding: 0.6em 0.0em 0.0em 0.0em;");
		textArea.setId("custom-label-time");
		content = getNewGroupMessageBody(model, content);
		textArea.setText(content);

		String bubbleImage = ConversationsViewRenderer.class
				.getResource(AppConfiguration.getMessageBubblePath() + "BubbleRight.png").toExternalForm();

		String style = "-fx-background-image: url(\"" + bubbleImage + "\");";
		textArea.setStyle(style);
		
		mainPanel.getChildren().add(textArea);
		return mainPanel;
	}

	private String getGroupMessageBody(HeaderModel header, String content) {
		try {
			if (header.getPriority() == GroupModel.OPERATION_CREATE_GROUP) {
				content = AppConfiguration.appConfString.group_created + "";

			} else if (header.getPriority() == GroupModel.OPERATION_ADD_MEMBER) {
				ContactsModel contactsModel = null;
				String titleString = "";
				JSONArray members = new JSONArray(header.getSubject());
				for (int i = 0; i < members.length(); i++) {
					String id = members.get(i) + "";
					contactsModel = profileMapper.getContact(id,0);
					if (titleString.length() > 0) {
						titleString += ", ";
					}

					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString += contactsModel.getFirstName();
					} else {
						titleString += id;
					}
				}

				if (members.length() > 1) {
					content = titleString + "  " + AppConfiguration.appConfString.are_added_to_group + "";
				} else {
					content = titleString + " " + AppConfiguration.appConfString.added_to_group + "";
				}
			} else if (header.getPriority() == GroupModel.OPERATION_DELETE_MEMBER) {
				ContactsModel contactsModel = null;
				String titleString = "";

				JSONArray members = new JSONArray(header.getSubject());
				for (int i = 0; i < members.length(); i++) {
					String id = members.get(i) + "";
					contactsModel = profileMapper.getContact(id,0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString = contactsModel.getFirstName();
					} else {
						titleString = id;
					}
					break;
				}

				content = titleString + " " + AppConfiguration.appConfString.moved_out_of_this_group + "";
			} else if (header.getPriority() == GroupModel.OPERATION_CHANGE_NAME) {
				content = AppConfiguration.appConfString.group_name_changed_to + " " + "\"" + header.getSubject()
						+ "\"";

			} else if (header.getPriority() == GroupModel.OPERATION_CHANGED_AS_ADMIN) {
				ContactsModel contactsModel = null;
				String titleString = "";

				String id = header.getSubject() + "";
				contactsModel = profileMapper.getContact(id,0);

				if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
					titleString += contactsModel.getFirstName();
				} else {
					titleString += id;
				}

				content = AppConfiguration.appConfString.group_admin_changed_to + " " + "\"" + titleString + "\"";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return content;
	}

	private String getNewGroupMessageBody(HeaderModel header, String content) {
		try {
			if (header.getPriority() == GroupModel.OPERATION_CREATE_GROUP) {

				ContactsModel contactsModel = null;
				String titleString = "";
				if (header.getMessageFrom().equals(cashewnutId)) {
					titleString = AppConfiguration.appConfString.you + "";
				} else {

					contactsModel = profileMapper.getContact(header.getMessageFrom(),0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString += contactsModel.getFirstName();
					} else {
						titleString += header.getMessageFrom();
					}
				}
				content = titleString + " " + AppConfiguration.appConfString.new_Group_Created + " " + "\""// string
																											// added
						+ header.getSubject() + "\"";
			} else if (header.getPriority() == GroupModel.OPERATION_ADD_MEMBER) {
				ContactsModel contactsModel = null;
				String titleString = "";
				String name = "";
				JSONArray members = new JSONArray(header.getSubject());

				for (int i = 0; i < members.length(); i++) {
					String id = members.get(i) + "";

					if (cashewnutId.equals(id)) {
						if (titleString.length() > 0) {
							titleString += ", ";
						}
						titleString += AppConfiguration.appConfString.you + "";
					} else {

						contactsModel = profileMapper.getContact(id,0);
						if (titleString.length() > 0) {
							titleString += ", ";
						}

						if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
							titleString += contactsModel.getFirstName();
						} else {
							titleString += id;
						}

					}
				}

				if (header.getMessageFrom().equals(cashewnutId)) {
					name = AppConfiguration.appConfString.you + "";
				} else {

					contactsModel = profileMapper.getContact(header.getMessageFrom(),0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						name = contactsModel.getFirstName();
					} else {
						name = header.getMessageFrom();
					}
				}
				content = name + " " + AppConfiguration.appConfString.new_Member_Added + " " + titleString;

			} else if (header.getPriority() == GroupModel.OPERATION_DELETE_MEMBER) {

				ContactsModel contactsModel = null;
				String titleString = "";
				String name = "";

				if (header.getMessageFrom().equals(cashewnutId)) {
					name = AppConfiguration.appConfString.you + "";
				} else {

					contactsModel = profileMapper.getContact(header.getMessageFrom(),0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						name = contactsModel.getFirstName();
					} else {
						name = header.getMessageFrom();
					}
				}

				JSONArray members = new JSONArray(header.getSubject());
				String id = members.get(0) + "";
				if (id.equals(cashewnutId)) {
					titleString = AppConfiguration.appConfString.you + "";
				} else {
					contactsModel = profileMapper.getContact(id,0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString = contactsModel.getFirstName();
					} else {
						titleString = id;
					}
				}
				if (header.getMessageFrom().equals(id.trim())) {
					content = name + " " + AppConfiguration.appConfString.member_Exit + "";
				} else {
					content = name + " " + AppConfiguration.appConfString.member_Removed + " " + titleString;
				}

			} else if (header.getPriority() == GroupModel.OPERATION_CHANGE_NAME) {
				ContactsModel contactsModel = null;
				String name = "";
				if (header.getMessageFrom().equals(cashewnutId)) {
					name = AppConfiguration.appConfString.you + "";
				} else {

					contactsModel = profileMapper.getContact(header.getMessageFrom(),0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						name = contactsModel.getFirstName();
					} else {
						name = header.getMessageFrom();
					}
				}

				content = name + " " + AppConfiguration.appConfString.change_Group_Name + " " + "\""
						+ header.getSubject() + "\"";

			} else if (header.getPriority() == GroupModel.OPERATION_CHANGE_MAIN_ADMIN) {

				ContactsModel contactsModel = null;
				String titleString = "";

				String id = header.getSubject() + "";
				contactsModel = profileMapper.getContact(id,0);

				if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
					titleString += contactsModel.getFirstName();
				} else {
					titleString += id;
				}

				if (id.equals(cashewnutId)) {
					content = AppConfiguration.appConfString.you + " "
							+ AppConfiguration.appConfString.are_now_an_mainAdmin + "";
				} else {

					content = titleString + " " + AppConfiguration.appConfString.is_now_an_mainAdmin + "";
				}

			} else if (header.getPriority() == GroupModel.OPERATION_CHANGED_AS_ADMIN) {
				// DBContactsMapper profileMapper =
				// DBContactsMapper.getInstance();
				ContactsModel contactsModel = null;
				String titleString = "";

				JSONArray members = new JSONArray(header.getSubject());
				for (int i = 0; i < members.length(); i++) {
					String id = members.get(i) + "";
					contactsModel = profileMapper.getContact(id,0);

					if (titleString.length() > 0) {
						titleString += ", ";
					}

					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString += contactsModel.getFirstName();
					} else {
						titleString += id;
					}
				}

				String name = "";
				if (header.getMessageFrom().equals(cashewnutId)) {
					name = AppConfiguration.appConfString.you + "";
				} else {

					contactsModel = profileMapper.getContact(header.getMessageFrom(),0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						name = contactsModel.getFirstName();
					} else {
						name = header.getMessageFrom();
					}
				}

				content = name + " " + AppConfiguration.appConfString.made + " " + "\"" + titleString + "\"" + " "
						+ AppConfiguration.appConfString.an_Admin + "";
			} else if (header.getPriority() == GroupModel.OPERATION_CHANGED_AS_MEMBER) {
				// DBContactsMapper profileMapper =
				// DBContactsMapper.getInstance();
				ContactsModel contactsModel = null;
				String titleString = "";

				JSONArray members = new JSONArray(header.getSubject());
				for (int i = 0; i < members.length(); i++) {
					String id = members.get(i) + "";
					contactsModel = profileMapper.getContact(id,0);
					if (titleString.length() > 0) {
						titleString += ", ";
					}
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString += contactsModel.getFirstName();
					} else {
						titleString += id;
					}
				}
				String name = "";
				if (header.getMessageFrom().equals(cashewnutId)) {
					name = AppConfiguration.appConfString.you + "";
				} else {

					contactsModel = profileMapper.getContact(header.getMessageFrom(),0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						name = contactsModel.getFirstName();
					} else {
						name = header.getMessageFrom();
					}
				}

				content = name + " " + AppConfiguration.appConfString.changed + " " + "\"" + titleString + "\"" + " "
						+ AppConfiguration.appConfString.to_a_member + "";

			} else if (header.getPriority() == GroupModel.OPERATION_FEATURE_ADDED) {
				content = AppConfiguration.appConfString.feature_added + "";
			} else if (header.getPriority() == GroupModel.OPERATION_FEATURE_REMOVED) {
				content = AppConfiguration.appConfString.feature_removed + "";
			} else if (header.getPriority() == GroupModel.OPERATION_MEMBER_LEFT) {

				ContactsModel contactsModel = null;
				String titleString = "";

				JSONArray members = new JSONArray(header.getSubject());
				for (int i = 0; i < members.length(); i++) {
					String id = members.get(i) + "";
					contactsModel = profileMapper.getContact(id,0);
					if (contactsModel.getFirstName() != null && contactsModel.getFirstName() != "") {
						titleString = contactsModel.getFirstName();
					} else {
						titleString = id;
					}
					break;
				}
				content = titleString + " " + AppConfiguration.appConfString.member_Exit + "";
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return content;
	}

	private HBox getCashewMessage(CheckBox chk, ConversationPrimer m, HeaderModel model,boolean status) {
		HBox mainPanel = new HBox(2);
		try {
			messageBodyPane.setStyle("-fx-alignment: CENTER;");
			messageBodyPane.setMaxWidth(300);
			messageBodyPane.getChildren().add(messageTextAreaPane);
			bottemDatePanel.getChildren().add(lblDate);
			bottemDatePanel.getChildren().add(lblDeliveryIcon);

			middleMessagePane.setSpacing(4);
			messageBodyPane.setSpacing(4);
			middleMessagePane.getChildren().add(priorityPane);

			middleMessagePane.getChildren().add(lblrecepientString);
			middleMessagePane.getChildren().add(messageBodyPane);
			middleMessagePane.getChildren().add(bottemDatePanel);
			String bubbleImage = "";
			if (model.getMessageFrom().equals(cashewnutId)) {
				middleMessagePane.setId("hbox-custom_left");
				mainPanel.getChildren().add(middleMessagePane);
				mainPanel.getChildren().add(lblUserRightIcon);
				if (chk != null) {
				if(status==true)
				{
					chk.setSelected(true);
				}
				else
				{
					chk.setSelected(false);
				}
					mainPanel.getChildren().add(chk);
				}
				mainPanel.setAlignment(Pos.TOP_RIGHT);

				bubbleImage = ConversationsViewRenderer.class
						.getResource(AppConfiguration.getMessageBubblePath() + "BubbleRight.png").toExternalForm();
			} else {
				middleMessagePane.setId("hbox-custom_right");
				if (chk != null) {
					
					if(status==true)
					{
						chk.setSelected(true);
					}
					else
					{
						chk.setSelected(false);
					}
						mainPanel.getChildren().add(chk);
				}
				mainPanel.getChildren().add(lblUserLeftIcon);
				mainPanel.getChildren().add(middleMessagePane);

				bubbleImage = ConversationsViewRenderer.class
						.getResource(AppConfiguration.getMessageBubblePath() + "BubbleLeft.png").toExternalForm();
			}
			String style = "-fx-background-image: url(\"" + bubbleImage + "\");";
			middleMessagePane.setStyle(style);
			setData(model, m);
		} catch (Exception e) {
			return null;
		}
		if (messageTextAreaPane.getChildren().get(0) instanceof Text) {
			((Text) messageTextAreaPane.getChildren().get(0)).setId("custom-label-message");
		} else if (messageTextAreaPane.getChildren().get(0) instanceof Button) {
			Button textArea = (Button) messageTextAreaPane.getChildren().get(0);
			textArea.setWrapText(true);
			textArea.setMaxWidth(300);
			textArea.setStyle("-fx-background-radius: 0, 0; -fx-background-color: transparent;");
			textArea.setId("custom-label-message");
		}

		lblDate.setId("custom-label-time");
		lblrecepientString.setId("custom-label-from");
		return mainPanel;
	}

	// =========================================================================
	public void setAttachment(final HeaderModel header, ConversationPrimer m) {

		AttachmentModel attachmentModel = getAttachment(header);
		if (attachmentModel != null) {
			final String path = attachmentModel.getAttachmentFilePath().replace('\\', '/');
			if (path != null && !path.trim().equals("")) {
				String contentType = attachmentModel.getAttachmentType();

				if (contentType == null || contentType.trim().equals("")) {
					contentType = ContentType.getContentType(path);
				}
				
				if (ContentType.isAudioType(contentType)) {
					if (messageBodyPane.getChildren().contains(attachmentButton)) {
						messageBodyPane.getChildren().remove(attachmentButton);
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								if (Helper.getOs().equalsIgnoreCase("linux")) {
									attachmentButton.setGraphic(vedioPlayIcon);
									attachmentButton.setStyle("-fx-background-radius: 0, 0;");
									messageBodyPane.getChildren().add(0, attachmentButton);
									attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent e) {
											viewAttachment(path);
										}
									});
								} else {
									try {
										if(attachmentModel.getAttachmentType().equalsIgnoreCase(ContentType.AUDIO_M4A))
										{
											attachmentButton.setGraphic(vedioPlayIcon);
											attachmentButton.setStyle("-fx-background-radius: 0, 0;");
											messageBodyPane.getChildren().add(0, attachmentButton);
											attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
												@Override
												public void handle(ActionEvent e) {
													viewAttachment(path);
												}
											});
										}
										else
										{
										File file = new File(path);										
										Media media = new Media(file.toURI().toURL().toString());
										MediaPlayer mediaPlayer = new MediaPlayer(media);
										mediaPlayer.setAutoPlay(false);
										MediaControl mediaControl = new MediaControl(mediaPlayer, 1);
										mediaControl.setStyle("-fx-background-radius: 0, 0;");
										VBox attachmentPane = new VBox(mediaControl);
										attachmentPane.setStyle("-fx-background-color: DimGrey;");
										attachmentPane.setPrefWidth(320);// prefWidth
										attachmentPane.setPrefHeight(60);// prefHeight
										messageBodyPane.getChildren().add(0, attachmentPane);
										}
									} catch (Exception e2) {
										attachmentButton.setGraphic(vedioPlayIcon);
										attachmentButton.setStyle("-fx-background-radius: 0, 0;");
										messageBodyPane.getChildren().add(0, attachmentButton);
										attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												viewAttachment(path);
											}
										});
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				} else if (ContentType.isImageType(contentType)) {
					if (messageBodyPane.getChildren().contains(attachmentButton)) {
						messageBodyPane.getChildren().remove(attachmentButton);
					}
					try {
						File file = new File(path);
						ImageView sendImage = ConversationsViewRenderer
								.resizeAttachment(file.toURI().toURL().toString(), 100, 0);
						attachmentButton.setGraphic(sendImage);
						attachmentButton.setStyle("-fx-background-radius: 0, 0;");
						messageBodyPane.getChildren().add(0, attachmentButton);
						attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								viewAttachment(path);
							}
						});
					} catch (MalformedURLException ex) {
					}
				} else if (ContentType.isSupportedVideoType(contentType)) {
					if (messageBodyPane.getChildren().contains(attachmentButton)) {
						messageBodyPane.getChildren().remove(attachmentButton);
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							setVideo();
						}

						private void setVideo() {
							try {
								if (Helper.getOs().equalsIgnoreCase("linux")) {
									attachmentButton.setGraphic(vedioPlayIcon);
									attachmentButton.setStyle("-fx-background-radius: 0, 0;");
									messageBodyPane.getChildren().add(0, attachmentButton);
									attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent e) {
											viewAttachment(path);
										}
									});
								} else {
									try {
										File file = new File(path);
										Media media = new Media(file.toURI().toURL().toString());
										MediaPlayer mediaPlayer = new MediaPlayer(media);
										mediaPlayer.setAutoPlay(false);
										MediaControl mediaControl = new MediaControl(mediaPlayer, -1);
										VBox attachmentPane = new VBox(mediaControl);
										attachmentPane.setPrefWidth(360);// prefWidth
										attachmentPane.setPrefHeight(240);// prefHeight
										attachmentButton.setMinSize(4, 18);
										attachmentButton.setGraphic(vedioPlay_fullscreen);
										attachmentButton.setTranslateY(-247);
										attachmentButton.setTranslateX(330);
										attachmentPane.getChildren().add(attachmentButton);
										messageBodyPane.getChildren().add(0, attachmentPane);

										attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												mediaPlayer.stop();
												viewAttachment(path);
											}
										});
									} catch (Exception ex) {
										attachmentButton.setGraphic(vedioPlayIcon);
										attachmentButton.setStyle("-fx-background-radius: 0, 0;");
										messageBodyPane.getChildren().add(0, attachmentButton);
										attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent e) {
												viewAttachment(path);
											}
										});
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				} else {
					attachmentButton.setGraphic(vedioPlayIcon);
					attachmentButton.setStyle("-fx-background-radius: 0, 0;");
					messageBodyPane.getChildren().add(0, attachmentButton);
					attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							viewAttachment(path);
						}
					});
					//setFileImageView(path);
				}
			}
		} else {
			attachmentButton.setStyle("-fx-background-radius: 0, 0;");
			messageBodyPane.getChildren().add(0, attachmentButton);
			attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (CashewnutApplication.isInternetOn()) {
						if (!isAttachmentDownloading) {

							// System.out.println(m.getTfrProgressPersentage());
							isAttachmentDownloading = true;
							try {
								downloadAttachment(header, m);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			});
		}
	}

	public void setFileImageView(final String path) {
		try {
			attachmentButton.setStyle("-fx-background-radius: 0, 0;");
			if (!messageBodyPane.getChildren().contains(attachmentButton)) {
				messageBodyPane.getChildren().add(0, attachmentButton);
			}
			attachmentButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					viewAttachment(path);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void viewAttachment(final String fileUrl) {
		new Thread() {
			@Override
			public void run() {
				if (java.awt.Desktop.isDesktopSupported()) {
					try {
						String filepath = fileUrl.replace('\\', '/');

						File file = new File(filepath);
						if (file.exists()) {
							Desktop.getDesktop().open(file);
						} else {
							// File is not exists
							// System.out.println("File is not exists");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}

		}.start();
	}

	boolean isAttachmentDownloading = false;

	private void downloadAttachment(final HeaderModel header, ConversationPrimer m) throws IOException {
		bottemDatePanel.getChildren().add(myProgressIndicator);
		final String absolutePath = Helper.getCashewnutFolderPath("attachment");
		String destFilePath = absolutePath + header.getLocalHeaderId() + "_" + 0 + ".txt";
		final File file = new File(destFilePath);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String tempFile = ReceiverHandler.getInstance().getAttachmentReceiver()
							.getMessage(header.getServerMessageId());

					final File destTempFile = new File(AttachmentReceiver.getTempFilePath(tempFile));
					// AttachmentDataConnection.getInstance().downloadAttachment(tempFile,
					// destFilePath, header.getLocalHeaderId());
					boolean isDownloaded = false;
					while (!isDownloaded) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (file.exists()) {
							isDownloaded = true;

							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										bottemDatePanel.getChildren().remove(myProgressIndicator);
										// attachment downloaded
										getAttachment(header);
										setAttachment(header, m);

									} catch (Exception ex) {
									}
								}
							});
						} else if (destTempFile.exists()) {
							// downloading attachment....
							long tempFileSize = destTempFile.length();
							long totalSize = getAttachmentSize(header);
						}
					}
				} catch (Exception e) {
					isAttachmentDownloading = false;
				}
			}

			private long getAttachmentSize(final HeaderModel header) {
				AttachmentStore attachmentStore = new AttachmentStore();
				int headerId = header.getLocalHeaderId();
				Vector attachmentModelVector = attachmentStore.getAttachmentsByHeaderLocalId(headerId);
				for (int i = 0; i < attachmentModelVector.size();) {
					int localid = (Integer) attachmentModelVector.elementAt(i);
					AttachmentModel attachmentModel = attachmentStore.getAttachmentByAttachmentLocalId(localid);

					String name = attachmentModel.getAttachmentName();
					long size = attachmentModel.getAttachmentSize();
					int padding = attachmentModel.getPadding();
					String path = attachmentModel.getAttachmentFilePath();
					return size;
				}
				return 0;
			}
		}).start();
	}

	public AttachmentModel getAttachment(final HeaderModel header) {
		AttachmentModel attachmentModel = null;
		try {
			DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
			Vector<RecipientModel> recipientList = recipientMapper
					.getReceipientsModelByHeaderId(header.getLocalHeaderId());

			Key key = null;
			if (header.getGroupId() != null && !header.getGroupId().equals("")) {
				key = CashewnutMessageCrypter.getMessageKey(header.getHeaderVersion(), header.getMessageFrom(),
						header.getSubject(), null, header.getGroupId().trim());
			} else {
				key = CashewnutMessageCrypter.getMessageKey(header.getHeaderVersion(), header.getMessageFrom(),
						header.getSubject(), recipientList, "");
			}
			attachmentModel = getAttachmentData(header, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentModel;
	}

	private AttachmentModel getAttachmentData(final HeaderModel header, Key key) {

		AttachmentStore attachmentStore = new AttachmentStore();
		int headerId = header.getLocalHeaderId();
		Vector attachmentModelVector = attachmentStore.getAttachmentsByHeaderLocalId(headerId);
		for (int i = 0; i < attachmentModelVector.size();) {
			int localid = (Integer) attachmentModelVector.elementAt(i);
			AttachmentModel attachmentModel = attachmentStore.getAttachmentByAttachmentLocalId(localid);
			String name = attachmentModel.getAttachmentName();
			long size = attachmentModel.getAttachmentSize();
			int padding = attachmentModel.getPadding();
			String path = attachmentModel.getAttachmentOriginalFilePath();
			try {
				if (header.getMessageFromThisDevice() > 1 && path != null && !path.trim().equalsIgnoreCase("")) {
					File file = new File(path);
					if (file != null && file.exists()) {
						long fileSize = 0;
						fileSize = file.length();
						if (fileSize == size) {
							attachmentModel.setAttachmentFilePath(path);
							return attachmentModel;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String sourcefileName = header.getLocalHeaderId() + "_" + i;
			String destPath = attachmentStore.decryptAttachmentAndSaveLocalFolderPath(name, sourcefileName, key,
					padding);
			if (destPath != null) {
				attachmentModel.setAttachmentFilePath(destPath);
			} else {
				return null;
			}
			return attachmentModel;

		}
		return null;

		// /*return null;
		// }
		// }*/;

	}

	// ============================================================================
	private Text createText(String string) {
		Text text = new Text(string);
		text.setWrappingWidth(300);
		text.setBoundsType(TextBoundsType.VISUAL);
		return text;
	}

	MessageViewModel messageViewModel;

	public MessageViewModel getMessageViewModel() {
		return messageViewModel;
	}

	public void setMessageViewModel(MessageViewModel messageViewModel) {
		this.messageViewModel = messageViewModel;
	}

	public void forwardMessage(ConversationPrimer premier, ConversationView conversationView) throws UnsupportedEncodingException {
		if (premier.getLatestMessage().getNumberOfBodyparts() > 2) {
			AttachmentModel attachmentModel = getAttachment(premier.getLatestMessage());
			if (attachmentModel != null) {
				final String path = attachmentModel.getAttachmentFilePath().replace('\\', '/');
				if (path != null && !path.trim().equals("")) {
					conversationView.forward(getMessageViewModel().getMessage(), path);
				}
			} else {
				
				final Stage dialogStage = new Stage();
				Button ok = new Button(AppConfiguration.appConfString.yes);
				dialogStage.initStyle(StageStyle.UNDECORATED);
				ok.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						/*new Thread(new Runnable() {
							@Override
							public void run() {*/
								try {
									downloadAttachment(premier.getLatestMessage(),premier);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

						dialogStage.close();
					}
				});

				Button cancel = new Button(AppConfiguration.appConfString.no);
				cancel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						dialogStage.close();
					}
				});

				HBox hbox = new HBox(ok, cancel);
				hbox.setSpacing(10);
				hbox.setAlignment(Pos.BOTTOM_CENTER);
				Font font = lblDate.getFont();
				Text text = createText(AppConfiguration.appConfString.forwardAttachment);
				text.setFont(font);
				text.setStyle("-fx-font-size: 14; -fx-base: #b6e7c9;");

				VBox vbox = VBoxBuilder.create().children(text, new Label("      "), hbox).alignment(Pos.CENTER)
						.padding(new Insets(5)).build();
				dialogStage.initModality(Modality.WINDOW_MODAL);

				GridPane gridpane = new GridPane();
				gridpane.setPadding(new Insets(5));
				gridpane.setHgap(10);
				gridpane.setVgap(10);

				final String cssDefault = "-fx-border-color: #A8A8A8;\n" + "-fx-border-width: 6;\n";

				gridpane.add(vbox, 1, 1, 10, 10);
				gridpane.setStyle(cssDefault);
				dialogStage.setScene(new Scene(gridpane));
			
				dialogStage.show();
			
				 //notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
				//conversationView.forward(getMessageViewModel().getMessage(), "");
			}
		} else {
			conversationView.forward(getMessageViewModel().getMessage(), "");
		}
	}

	private void setData(final HeaderModel model, final ConversationPrimer m) throws UnsupportedEncodingException, DecoderException {
		if (!model.getMessageFrom().equals(cashewnutId)) {
			// in message
			messageViewModel = getInMessageView(model, m);
		} else {
			// sent message
			messageViewModel = getSentMessageView(model);
		}
		setPriority(messageViewModel, m);
		final String message = messageViewModel.getMessage().trim();
		if (!model.getMessageFrom().equals(cashewnutId) && messageViewModel.getAck() == 1) {
			// all recipients un acknowledged
			viewForAcknowledgeMessage(model, m, message);
		} else if (!model.getMessageFrom().equals(cashewnutId) && messageViewModel.isExpiry()
				&& model.getExpiryStartTime() < 0 && messageViewModel.getAck() < 1) {		
			// all recipients un acknowledge
			viewForExpiryMessage(model, m, message);
		} else {
			setExpiryMessageToVector(model);
			m.setAck(false);
			 Text text;
			 text=new Text(message);
			 StringBuilder forwardString=new StringBuilder("");
			 String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
		        byte[] utf8 = message.getBytes("UTF-8");
		        String string1 = new String(utf8, "UTF-8");
		        Pattern pattern = Pattern.compile(regexPattern);
		        Matcher matcher = pattern.matcher(string1);   
		       int start=0;
		       int end=0;
		       int ending=0;
		       int duplicate=0;
		       int emojisLength=0;
		        String str215=null;
		        String str214=null;
		        String s1=null;
		        Image img=null;
		        String[] matcherArray=null;
		        int differenceLength=0;
		        int loopVar=0;
		    	while (matcher.find()) {
		    		 if(ending<1)
		    		 {
		    		String str212=string1.replace(string1.substring(matcher.start()),"");
		    		Text text2;
		    		text2=new Text(str212);
		    		forwardString.append(str212);
                	messageTextAreaPane.getChildren().add(text2);
		    		 }
		    		 else
		    		 {
		    			  str214=string1.substring(end,matcher.start());
				    		str215=string1.substring(matcher.end());
				    		Text text2;
				    		forwardString.append(str214);
				    		text2=new Text(str214);
		                	messageTextAreaPane.getChildren().add(text2);
		                	
		    		 }
			      	  for(Entry<String, Image> entry :Emoji.sEmojisMap.entrySet())
		           	   {	  
		                  if(matcher.group().contains(entry.getKey()))
		                  {
	                		
		                		 List<String> strings = new LinkedList<String>();
		                		 int index = 0;
		                		 while (index < matcher.group().length()) {
		                		     strings.add(matcher.group().substring(index, Math.min(index + 2,matcher.group().length())));
		                		     index += 2;
		                		 }
		                		 for(int b=0;b<strings.size();b++)
			                		{
			                			if(strings.get(b).equals(entry.getKey()))
			                					{
			                				 messageTextAreaPane.getChildren().add(getImageView(entry.getValue(),19));
			                				 forwardString.append(entry.getKey());
			                					}
			                		}
		                	 } 		                		  
		                		  
		                  }
			      	emojisLength=matcher.group().length();
			      	start=matcher.start();
		    		end=matcher.end();
		    		ending++;
			        }		
		    	if(string1.length()>end&&end>1)
		    	{
		    		Text text2;		    					
		    		String lastString=string1.substring(end);
		    		forwardString.append(lastString);
		    		text2=new Text(lastString);
                	messageTextAreaPane.getChildren().add(text2);
		    	}
		    	differenceLength=emojisLength-duplicate;
		    	
		    	if(end<1)
		    	{
		    		if(text.getText().equalsIgnoreCase(MessageControlParameters.ACK_MESSAGE))
		    		{
		    			messageTextAreaPane.getChildren().remove(text);
		    			 Text ackText;
		    			 ackText=new Text(message);
		    			messageTextAreaPane.getChildren().add(ackText);
		    		}
		    		else
		    		{
		    			messageTextAreaPane.getChildren().add(text);
		    		}
		    		
		    	} 
		    	//messageViewModel.setMessage(forwardString.toString());
			if (messageViewModel.getAttachmentType() != null
					&& !messageViewModel.getAttachmentType().trim().equals("")) {
				ImageView imageView = getDefaultBitmap(messageViewModel.getAttachmentType());
				if (imageView != null) {
					attachmentButton.setGraphic(imageView);
				}
			}
			if (model.getNumberOfBodyparts() > 2) {
				setAttachment(model, m);
			}
		}

		lblDate.setText(messageViewModel.getDate() + "   ");

		if (messageViewModel.getSendStatus1() == 0) {
			if (!bottemDatePanel.getChildren().contains(myProgressIndicator)) {
				bottemDatePanel.getChildren().add(myProgressIndicator);
			}
			myProgressIndicator.setVisible(true);
		}
		if (messageViewModel.getSendStatus1() == 1) {
			// sent
			lblDeliveryIcon.setGraphic(single_tick_icon);
			myProgressIndicator.setVisible(true);
		}

		if (messageViewModel.getSendStatus1() == 3) {
			// delivered
			lblDeliveryIcon.setGraphic(dubble_tick_icon);
			myProgressIndicator.setVisible(true);
		}
		if (messageViewModel.getSendStatus1() == 2) {
			// failed
			lblDeliveryIcon.setGraphic(failure_tick_icon);
		}
		if (messageViewModel.getSendStatus1() == 4) {
			lblDeliveryIcon.setGraphic(expiry_iconw);
		}
		if (!model.getMessageFrom().equals(cashewnutId)) {
			String from = messageViewModel.getMessageFrom();
			if (from != null && !from.trim().equals("")) {
				lblrecepientString.setText(from);
			} else {
				middleMessagePane.getChildren().remove(lblrecepientString);
			}
		} else {
			if (middleMessagePane.getChildren().contains(lblrecepientString)) {
				middleMessagePane.getChildren().remove(lblrecepientString);
			}
		}

	}
	public void viewForExpiryMessage(final HeaderModel model, final ConversationPrimer m, final String message) {
				
		final Button button = new Button();
		button.setGraphic(label_expiry_iconw);
		button.setTextFill(Color.WHITE);	
		button.setText(AppConfiguration.appConfString.conversation_click_to_view);
		messageTextAreaPane.getChildren().add(button);
		m.setAck(false);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setExpiryMessageToVector(model);
				m.setAck(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ControlMessageOptions option = ControlMessageOptions.getInstance();
							option.viewExpiryMessage(model, cashewnutId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				Text text = createText(message);
				messageTextAreaPane.getChildren().remove(button);
				messageTextAreaPane.getChildren().add(text);

				if (messageViewModel.getAttachmentType() != null
						&& !messageViewModel.getAttachmentType().trim().equals("")) {
					ImageView imageView = getDefaultBitmap(messageViewModel.getAttachmentType());
					if (imageView != null) {
						attachmentButton.setGraphic(imageView);
					}
				}
				if (model.getNumberOfBodyparts() > 2) {

					setAttachment(model, m);
				}
			}
		});
		
	}
	public void viewForAcknowledgeMessage(final HeaderModel model, final ConversationPrimer m, final String message) {
		
		Button button = new Button();
		Text text;	
		button.setText(message);
		messageTextAreaPane.getChildren().add(button);
		m.setAck(true);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Stage dialogStage = getAckDialog(model,m,message);
				dialogStage.show();
			
			
			}
		});
		
	}

	java.util.Timer timer = null;
	boolean isProgress = false;

	private void setExpiryMinsLable(final HeaderModel model) {
		Long expiryTime1 = model.getExpiryStartTime();
		if (System.currentTimeMillis() < expiryTime1) {
			if (timer == null) {
				timer = new java.util.Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (!isProgress) {
									try {
										isProgress = true;
										Long expiryTime = expiryTime1 - System.currentTimeMillis();
										if (expiryTime > 0) {
											String timeString = getCountTime(expiryTime);
											lblExpiryTime.setText(timeString);
											
										} else {
											int id = model.getLocalHeaderId();
											HeaderStore headerStore = new HeaderStore();
											HeaderModel headerModel = headerStore.getHeaderById(id, true);
											if (headerModel != null) {
												HeaderModel header = headerStore.getHeaderById(id, true);
												updateView(header, MessageModel.ACTION_DELETED);
												header.setDeleteStatus(MessageModel.RECIPIENT_DELETED_STATUS);
												new HeaderStore().updateHeaderById(header, id);
												ControlMessageOptions.deleteMessageOnServer(headerModel,
														MessageModel.MESSAGE_FOLDER_TYPE_INBOX);

											}
											timer.cancel();
										}
										isProgress = false;
									} catch (Exception e) {
										e.printStackTrace();
										timer.cancel();
									}
								}
							}
						});
					}
				}, 0, 1000);
			}
		}
	}

	public void updateView(HeaderModel headerModel, int action) {
		if (CashewnutActivity.currentActivity != null) {
			try {
				// fire to list
				((CashewnutActivity) CashewnutActivity.currentActivity).processMessage(headerModel, action);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getCountTime(long millisUntilFinished) {
		millisUntilFinished = millisUntilFinished / 1000;
		long hours = millisUntilFinished / 3600;
		long minutes = (millisUntilFinished % 3600) / 60;
		long seconds = millisUntilFinished % 60;

		String senderExpirymins1 = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return senderExpirymins1;
	}

	private void setExpiryMessageToVector(final HeaderModel header) {
		if (!header.getMessageFrom().equals(cashewnutId) && header.getExpiry() > -1
				&& header.getExpiryStartTime() < 0) {
			try {
				Long expiryTime = System.currentTimeMillis() + (header.getExpiry() * 60 * 1000);
				header.setExpiryStartTime(expiryTime);
				new HeaderStore().updateHeaderById(header, header.getLocalHeaderId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!header.getMessageFrom().equals(cashewnutId) && header.getExpiry() > -1
				&& header.getExpiryStartTime() > -1) {
			try {
				setExpiryMinsLable(header);
				ControlMessageOptions.setExpiry(header.getLocalHeaderId(), header.getExpiryStartTime());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setPriority(MessageViewModel messageViewModel, ConversationPrimer m) {
		int priority = messageViewModel.getPrority();
		switch (priority) {
		case MessageControlParameters.PRIORITY_INDICATOR_GRAY:
			lblPriorityIcon.setGraphic(priority_icongy);
			priorityPane.getChildren().add(lblPriorityIcon);
			break;
		case MessageControlParameters.PRIORITY_INDICATOR_GREEN:
			lblPriorityIcon.setGraphic(priority_icongr);
			priorityPane.getChildren().add(lblPriorityIcon);
			break;
		case MessageControlParameters.PRIORITY_INDICATOR_YELLOW:
			lblPriorityIcon.setGraphic(priority_icony);
			priorityPane.getChildren().add(lblPriorityIcon);
			break;
		case MessageControlParameters.PRIORITY_INDICATOR_RED:
			lblPriorityIcon.setGraphic(priority_iconr);
			priorityPane.getChildren().add(lblPriorityIcon);
			break;
		default:
			lblPriorityIcon.setGraphic(priority_icongy);
			priorityPane.getChildren().add(lblPriorityIcon);
		}

		if (messageViewModel.isRestructed()) {
			priorityPane.getChildren().add(lblrestrictedIcon);
			m.setRestructed(messageViewModel.isRestructed());
		}
		if (messageViewModel.isExpiry()) {
			priorityPane.getChildren().add(lblExpiryIcon);
			priorityPane.getChildren().add(lblExpiryTime);
		}
		if (messageViewModel.getAck() > -1) {
			if (messageViewModel.getAck() == 1) {
				// all recipients un acknowledged
				priorityPane.getChildren().add(lblUnAckIcon);

			} else if (messageViewModel.getAck() == 2) {
				// partially acknowledged
				priorityPane.getChildren().add(lblPatialAckIcon);
			} else if (messageViewModel.getAck() == 0) {
				// all recipients acknowledged
				priorityPane.getChildren().add(lblACKIcon);
			}
		}
		if (messageViewModel.getAttachmentType() != null && !messageViewModel.getAttachmentType().trim().equals("")) {
			priorityPane.getChildren().add(lblAttachmentIcon);

		}
	}

	public static ImageView resize1(String path, int size) {
		Image image = null;
		Map cache = CashewnutActivity.cache;
		if (!cache.containsKey(path)) {
			image = new Image(path);
			cache.put(path, image);
		} else {

			image = (Image) cache.get(path);
		}
		return getImageView(image, size);
	}

	public static ImageView resizeGroupIcon(String path, int size) {
		Image image;
		Map cache1 = CashewnutActivity.group_cache;
		image = new Image(path);
		cache1.put(path, image);
		image = (Image) cache1.get(path);
		return getImageView(image, size);

	}

	public static ImageView getImageView(Image image, int size) {
		ImageView myImageView = new ImageView(image);
		myImageView.setFitWidth(size);
		myImageView.setFitHeight(size);
		myImageView.setPreserveRatio(true);
		myImageView.setPickOnBounds(true);
		myImageView.setSmooth(true);
		myImageView.setCache(true);
		return myImageView;
	}

	public static ImageView resizeAttachment(String path, double width, double height) {
		try {
			Image image;
			Map cache = CashewnutActivity.cache;
			if (!cache.containsKey(path)) {
				image = new Image(path, width, height, true, true);
				cache.put(path, image);
			} else {
				image = (Image) cache.get(path);
			}
			return new ImageView(image);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return null;
	}

	public static ImageView getImageIcon(String path, int width, int height) {
		Image image;
		image = new Image(new File(path).toURI().toString());

		ImageView myImageView = new ImageView(image);
		myImageView.setFitWidth(width);
		myImageView.setFitHeight(height);
		myImageView.setPreserveRatio(true);
		myImageView.setSmooth(true);
		myImageView.setCache(true);

		return myImageView;
	}

	public static ImageView getDefaultBitmap(String contentType) {
		try {
			if (ContentType.isAudioType(contentType)) {
				ImageView user_icon_documentaudio = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentaudio.png", 96, 0);
				return user_icon_documentaudio;
			} else if (ContentType.isImageType(contentType)) {
				ImageView user_icon_documentpicture = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentpicture.png", 96, 0);
				return user_icon_documentpicture;
			} else if (ContentType.isVideoType(contentType)) {
				ImageView user_icon_documentvideo = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentvideo.png", 96, 0);
				return user_icon_documentvideo;
			} else if (contentType.endsWith("mov")) {
				ImageView user_icon_documentvideo = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentvideo.png", 96, 0);
				return user_icon_documentvideo;
			}

			else if (contentType.endsWith("pdf")) {
				ImageView user_icon_documentpdf = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentpdf.png", 96, 0);
				return user_icon_documentpdf;
			} else if (contentType.endsWith("doc") || contentType.endsWith("docx")) {
				ImageView user_icon_documentdoc = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentdoc.png", 96, 0);
				return user_icon_documentdoc;
			} else if (contentType.endsWith("html")) {
				ImageView user_icon_documenthtml = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documenthtml.png", 96, 0);
				return user_icon_documenthtml;
			} else if (contentType.endsWith("xls") || contentType.endsWith("xlsx")) {
				ImageView user_icon_documentexcel = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentexcel.png", 96, 0);
				return user_icon_documentexcel;
			} else if (contentType.endsWith("ppt") || contentType.endsWith("pptx")) {
				ImageView user_icon_documentppt = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documentppt.png", 96, 0);
				return user_icon_documentppt;
			} else if (contentType.endsWith("txt")) {
				ImageView user_icon_documenttxt = resizeAttachment(
						AppConfiguration.getDocIconPath() + "documenttxt.png", 96, 0);
				return user_icon_documenttxt;
			} else {
				ImageView user_icon_ic_file = resizeAttachment(AppConfiguration.getDocIconPath() + "ic_file.png", 96,
						0);
				return user_icon_ic_file;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ======================================================================================
	private MessageViewModel getInMessageView(HeaderModel header, ConversationPrimer m) {
		// get data from header
		int localId = header.getLocalHeaderId();
		String from = header.getMessageFrom();
		int priority = header.getPriority();
		boolean isRestrictedMessage = header.getRestricted() == MessageControlParameters.RESTRICTED;
		boolean isExpiryMessage = header.getExpiry() > -1;
		boolean isAckMessage = header.getMessageAck() == MessageControlParameters.ACK_SENDER;
		String timeString = getTimeString(header, header.getCreationTime());
		int recpAckStatus = -1;
		boolean isSelfMessage = false;

		if (cashewnutId.equals(from)) {
			isSelfMessage = true;
		}

		MessageViewModel messageViewModel = new MessageViewModel();
		messageViewModel.setPrority(priority);
		messageViewModel.setExpiry(isExpiryMessage);
		messageViewModel.setRestructed(isRestrictedMessage);
		messageViewModel.setDate(timeString);

		Vector recipientList = recipientMapper.getReceipientsModelByHeaderId(localId);
		for (int i = 0; i < recipientList.size(); i++) {
			RecipientModel recipient = (RecipientModel) recipientList.elementAt(i);
			if (cashewnutId.equals(recipient.getRecepientCashewnutId())) {
				recpAckStatus = recipient.getReceipientAck();
			}
		}

		String messageString = getInMessageString(header, recipientList, header.getSubject());
		if (isAckMessage && !isSelfMessage) {
			if (recpAckStatus == MessageControlParameters.ACK_READ_RECEIPT) {
				messageViewModel.setAck(0);
			} else {
				if(header.getMessageType()!=7&&(header.getExpiryStartTime()<0))
				{
				messageString = MessageControlParameters.ACK_MESSAGE;
				messageViewModel.setAck(1);
				}
			}
		}

		messageViewModel.setMessage(messageString);

		if (recipientList.size() > 1 || isGroupMessage(header)) {
			messageViewModel.setMessageFrom(getMessageFrom(from));
		}

		if (header.getNumberOfBodyparts() > 2) {
			String type = getAttachmentType(header);
			messageViewModel.setAttachmentType(type);

		}
		return messageViewModel;
	}

	private String getMessageFrom(String from) {
		ContactsModel contactsModel = profileMapper.getContact(from,0);
		if (contactsModel != null && !contactsModel.getFirstName().equals("")) {
			return contactsModel.getFirstName() + ":";
		} else {
			return from + ":";
		}
	}

	private String getInMessageString(HeaderModel header, Vector recipientList, String subject) {
		if (subject == null || subject.equalsIgnoreCase("no subject") || subject.trim().equals("")) {
			Key key;
			if (isGroupMessage(header)) {
				key = CashewnutMessageCrypter.getMessageKey(header.getHeaderVersion(), header.getMessageFrom(),
						header.getSubject(), null, header.getGroupId().trim());
			} else {
				key = CashewnutMessageCrypter.getMessageKey(header.getHeaderVersion(), header.getMessageFrom(),
						header.getSubject(), recipientList, "");

			}
			CashewnutMessageCrypter crypter = new CashewnutMessageCrypter();
			String body = (new BodyStore()).getBody(header.getLocalHeaderId());
			try {
				if (key != null) {
					subject = crypter.decryptFromBase64(body, key);
				} else {
					subject = body + "";
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return subject + "     ";
	}

	// ==============================================================================
	private MessageViewModel getSentMessageView(HeaderModel header) {
		MessageViewModel messageViewModel = new MessageViewModel();

		// get data from header
		int localId = header.getLocalHeaderId();
		long scheduleTime = header.getScheduleTime();
		int priority = header.getPriority();
		boolean isRestrictedMessage = header.getRestricted() == MessageControlParameters.RESTRICTED;

		boolean isExpiryMessage = header.getExpiry() > -1;
		int sendParts = header.getSendParts();
		int totalParts = header.getNumberOfBodyparts();

		boolean isAckMessage = header.getMessageAck() == MessageControlParameters.ACK_SENDER;
		int acknowledgedCount = 0;

		String timeString = (getTimeString(header, header.getCreationTime()) + "   ");

		messageViewModel.setPrority(priority);
		messageViewModel.setExpiry(isExpiryMessage);
		messageViewModel.setRestructed(isRestrictedMessage);
		messageViewModel.setDate(timeString);

		Vector recipientList = recipientMapper.getReceipientsModelByHeaderId(localId);
		for (int index = 0; index < recipientList.size(); index++) {
			RecipientModel recipient = (RecipientModel) recipientList.elementAt(index);
			if (isAckMessage) {
				if (recipient.getReceipientAck() == MessageControlParameters.ACK_READ_RECEIPT) {
					acknowledgedCount++;
				}
			}
		}

		if (isAckMessage) {
			if (recipientList.size() > 0 && recipientList.size() == acknowledgedCount) {
				// all recipients acknowledged
				messageViewModel.setAck(0);
			} else if (recipientList.size() > 1 && acknowledgedCount > 0) {
				// partially acknowledged
				messageViewModel.setAck(2);
			} else {
				// all recipients un acknowledged
				messageViewModel.setAck(1);
			}
		}

		String messageString = getsentDisplayString(header, header.getSubject(), recipientList);
		messageViewModel.setMessage(messageString);

		if (header.getNumberOfBodyparts() > 2) {
			// 3 and above - attachments
			String type = getAttachmentType(header);
			messageViewModel.setAttachmentType(type);

		}

		if (totalParts - sendParts == 0) {
			// sent

			messageViewModel.setSendStatus1(1);
		} else if (sendParts < 0) {
			// failed
			messageViewModel.setSendStatus1(2);
		} else {
			// sending
			messageViewModel.setSendStatus1(0);
		}
		if (scheduleTime > System.currentTimeMillis()) {

			messageViewModel.setSendStatus1(4);
			ControlMessageOptions.setSenderScheduler(header.getLocalHeaderId(), header);

		}
		if (recipientList.size() == 1) {
			RecipientModel recipient = (RecipientModel) recipientList.elementAt(0);
			if (totalParts - sendParts == 0 && recipient.getReceipientReadStatus() == 1) {
				// delivered
				messageViewModel.setSendStatus1(3);
			}

		}
		return messageViewModel;
	}

	private String getsentDisplayString(HeaderModel header, String subject, Vector recipientList) {
		if (subject == null || subject.equalsIgnoreCase("no subject") || subject.trim().equals("")) {
			Key key;
			if (isGroupMessage(header)) {
				key = CashewnutMessageCrypter.getMessageKey(header.getHeaderVersion(), header.getMessageFrom(),
						header.getSubject(), null, header.getGroupId().trim());
			} else {
				key = CashewnutMessageCrypter.getMessageKey(header.getHeaderVersion(), header.getMessageFrom(),
						header.getSubject(), recipientList, "");

			}
			String body = (new BodyStore()).getBody(header.getLocalHeaderId());
			try {
				if (key != null) {
					subject = new CashewnutMessageCrypter().decryptFromBase64(body, key);
				} else {
					subject = body + "";
				}
			} catch (Exception e) {
				subject = body;
			}
		}
		return subject + "     ";
	}

	public boolean isGroupMessage(HeaderModel header) {
		return header.getGroupId() != null && !header.getGroupId().equals("");
	}

	public String getTimeString(HeaderModel header, long creationTime) {

		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
		return "     " + formatter.format(creationTime);
	}

	private String getAttachmentType(HeaderModel header) {
		AttachmentStore attachmentStore = new AttachmentStore();
		int headerId = header.getLocalHeaderId();
		Vector attachmentModelVector = attachmentStore.getAttachmentsByHeaderLocalId(headerId);
		for (int i = 0; i < attachmentModelVector.size();) {
			int localid = (Integer) attachmentModelVector.elementAt(i);
			AttachmentModel attachmentModel = attachmentStore.getAttachmentByAttachmentLocalId(localid);

			String type = attachmentModel.getAttachmentType();
			return type.toLowerCase();
		}
		return "plain";
	}

	private Stage getAckDialog(final HeaderModel model,final ConversationPrimer m, final String message) {
		final Stage dialogStage = new Stage();
		Button ok = new Button(AppConfiguration.appConfString.yes);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		ok.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent actionEvent) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ControlMessageOptions option = ControlMessageOptions.getInstance();
							option.viewAcknowledgementMessage(model, cashewnutId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				
				
				dialogStage.close();
				
			}
		});

		Button cancel = new Button(AppConfiguration.appConfString.no);
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
	
				dialogStage.close();
			}
		});

		HBox hbox = new HBox(ok, cancel);
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		Font font = lblDate.getFont();
		Text text = createText(AppConfiguration.appConfString.conversation_send_acknowledgement);
		text.setFont(font);
		text.setStyle("-fx-font-size: 14; -fx-base: #b6e7c9;");

		VBox vbox = VBoxBuilder.create().children(text, new Label("      "), hbox).alignment(Pos.CENTER)
				.padding(new Insets(5)).build();
		dialogStage.initModality(Modality.WINDOW_MODAL);

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

	// ==============================================================================
	public class MessageViewModel {

		String message;
		String attachmentType;
		String date;
		String messageFrom;

		private int type;
		int boxType;
		int prority = -1;
		boolean restructed;
		boolean expiry;
		int sendStatus = -1;
		int ack = -1;
		long schedule = -1;
		long expiryTime;

		public long getExpiryTime() {
			return expiryTime;
		}

		public void setExpiryTime(long expiryTime) {
			this.expiryTime = expiryTime;
		}

		public long getSchedule() {
			return schedule;
		}

		public void setSchedule(long schedule) {
			this.schedule = schedule;
		}

		public String getMessageFrom() {
			return messageFrom;
		}

		public void setMessageFrom(String messageFrom) {
			this.messageFrom = messageFrom;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getAttachmentType() {
			return attachmentType;
		}

		public void setAttachmentType(String attachmentType) {
			this.attachmentType = attachmentType;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getPrority() {
			return prority;
		}

		public void setPrority(int prority) {
			this.prority = prority;
		}

		public boolean isExpiry() {
			return expiry;
		}

		public void setExpiry(boolean expiry) {
			this.expiry = expiry;
		}

		public boolean isRestructed() {
			return restructed;
		}

		public void setRestructed(boolean restructed) {
			this.restructed = restructed;
		}

		public int getSendStatus1() {
			return sendStatus;
		}

		public void setSendStatus1(int sendStatus) {
			this.sendStatus = sendStatus;
		}

		public int getAck() {
			return ack;
		}

		public void setAck(int ack) {
			this.ack = ack;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}
	}
}
