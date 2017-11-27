package com.loment.cashewnut.activity.list;

import static com.loment.cashewnut.activity.list.ConversationsViewRenderer.resize1;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.crypto.CashewnutMessageCrypter;
import com.loment.cashewnut.database.BodyStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.RecipientModel;
import com.loment.cashewnut.util.Helper;

/**
 *
 * @author sekhar
 */
public class ConversationsListRenderer {

	private static final long serialVersionUID = 1L;
	ImageView user_icon_single = null;
	String cashewnutId = "";
	DBContactsMapper profileMapper = null;
	DBRecipientMapper recipientMapper = null;

	ImageView expiry_iconw = resize1(AppConfiguration.getIconPath() + "ic_action_clock.png", 14);
	ImageView restricted_iconw = resize1(AppConfiguration.getIconPath() + "ic_action_halt.png", 14);
	ImageView unack_iconr = resize1(AppConfiguration.getIconPath() + "ic_action_lock_closed.png", 14);
	ImageView ack_iconr = resize1(AppConfiguration.getIconPath() + "ic_action_lock_open.png", 14);
	ImageView partialack_icon = resize1(AppConfiguration.getIconPath() + "ic_action_starlock_gray.png", 14);
	ImageView priority_iconr = resize1(AppConfiguration.getIconPath() + "priority_red.png", 14);
	ImageView priority_icony = resize1(AppConfiguration.getIconPath() + "priority_yellow.png", 14);
	ImageView priority_icongy = resize1(AppConfiguration.getIconPath() + "priority_gray.png", 14);
	ImageView priority_icongr = resize1(AppConfiguration.getIconPath() + "priority_green.png", 14);
	ImageView failure_tick_icon = resize1(AppConfiguration.getIconPath() + "ic_action_failure.png", 11);
	ImageView single_tick_icon = resize1(AppConfiguration.getIconPath() + "ic_action_tick.png", 11);
	ImageView dubble_tick_icon = resize1(AppConfiguration.getIconPath() + "ic_action_tick_2.png", 11);
	ImageView attachment_icon = resize1(AppConfiguration.getIconPath() + "ic_action_attachment.png", 14);
	
	HBox priorityPane = new HBox();
	HBox mainPanel = new HBox(4);

	VBox middleBox = new VBox(3);
	HBox subMiddle = new HBox();
	HBox subMiddle1 = new HBox();
	VBox rightPane = new VBox();
	VBox leftPane = new VBox();
	HBox messagePane = new HBox();

	Label lblBodyString = new Label("");
	Label lblrecepientString = new Label("");
	Label lblDate = new Label("");
	StackPane lblUserIcon = null;
	Label welcomeIcon=null;
	Label lblCount = new Label("");
	Label lblSpace = new Label(" ");

	Label lblPriorityIcon = new Label(" ");
	Label lblrestrictedIcon = new Label("  ");
	Label lblExpiryIcon = new Label("  ");
	Label lblACKIcon = new Label("  ");
	Label lblPatialAckIcon = new Label("  ");
	Label lblUnAckIcon = new Label("  ");
	Label lblDeliveryIcon = new Label("  ");
	Label lblsentIcon = new Label("  ");
	Label lblAttachmentIcon = new Label("  ");
	Region region=new Region();
	public ConversationsListRenderer() {
		try {
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel accountsModel = accountsMapper.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
			recipientMapper = DBRecipientMapper.getInstance();
			profileMapper = DBContactsMapper.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lblUserIcon = new CircularUserIcon("NS");
		
		lblPriorityIcon.setGraphic(priority_icongy);
		lblrestrictedIcon.setGraphic(restricted_iconw);
		lblACKIcon.setGraphic(ack_iconr);
		lblPatialAckIcon.setGraphic(partialack_icon);
		lblUnAckIcon.setGraphic(unack_iconr);
		lblExpiryIcon.setGraphic(expiry_iconw);
		lblAttachmentIcon.setGraphic(attachment_icon);
		middleBox.setSpacing(4);
		region.setStyle(("-fx-font-size: 10.5pt;" + "-fx-text-fill:#000000;"));
		subMiddle.setSpacing(4);
		subMiddle.getChildren().add(lblrecepientString);
		subMiddle1.getChildren().add(lblCount);
		middleBox.getChildren().add(subMiddle);
		lblCount.setStyle(" -fx-background: #124784;" + "-fx-background-color: #F0591E;" + " -fx-border-radius: 5;"
				+ "-fx-background-radius:5px;" + "-fx-font-size: 10px; -fx-font-weight: bold;");
		rightPane.setAlignment(Pos.CENTER_RIGHT);
		rightPane.getChildren().add(subMiddle1);
		rightPane.getChildren().add(priorityPane);
		rightPane.getChildren().add(lblDate);
		priorityPane.getChildren().add(new Label(" "));
		priorityPane.setAlignment(Pos.CENTER_RIGHT);
		rightPane.setPrefWidth(100);
		rightPane.setPadding(new Insets(0, 1, 0, 1));
		rightPane.setSpacing(4);

		middleBox.setAlignment(Pos.CENTER_LEFT);
		messagePane.getChildren().add(lblBodyString);

		middleBox.getChildren().add(messagePane);

		leftPane.setPrefWidth(30);
		leftPane.setAlignment(Pos.CENTER_LEFT);
		middleBox.setPrefWidth(200);
		middleBox.setPadding(new Insets(0, 2, 0, 2));

		mainPanel.getChildren().add(leftPane);
		mainPanel.getChildren().add(middleBox);
		mainPanel.getChildren().add(rightPane);

		mainPanel.setAlignment(Pos.CENTER_LEFT);
		mainPanel.setPrefWidth(300);
		mainPanel.setPadding(new Insets(0, 10, 0, 2));
		mainPanel.setPrefHeight(70);
	}

	public HBox getListCellRendererComponent(Object value) throws UnsupportedEncodingException {
		if (value == null) {
			return null;
		}
		ConversationPrimer m = (ConversationPrimer) value;
		HeaderModel model = m.getLatestMessage();
		String content = "";

		if (model.getMessageType() == MessageModel.LOCAL_MESSAGE_TYPE_GROUP
				|| model.getMessageType() == MessageModel.LOCAL_MESSAGE_TIME) {

			if (model.getPriority() > -1 && model.getMessageType() == MessageModel.LOCAL_MESSAGE_TYPE_GROUP) {
				getGroupMessageBody(model);
			}
		} else if (model.getMessageType() == MessageModel.MESSAGE_TYPE_NEW_GROUPSMESSAGE
				|| model.getMessageType() == MessageModel.LOCAL_MESSAGE_TIME) {

			if (model.getPriority() > -1 && model.getMessageType() == MessageModel.MESSAGE_TYPE_NEW_GROUPSMESSAGE) {
				getNewGroupMessageBody(model);
			}
		} else if (model.getMessageType() == MessageModel.MESSAGE_TYPE_CASHEWNUT) {
			setData(model, m.getUnReadCount());
		} else if ((model.getMessageType() == MessageModel.MESSAGE_TYPE_WELCOME)) {
			setWelcomeMessageView(model);
		}
		 else if (model.getMessageType() == MessageModel.MESSAGE_TYPE_SCREENSHOT) {
			 setScreenShotData(model);
			}
		
		lblBodyString.setId("custom-label-message");
		lblDate.setId("custom-label-time");
		lblrecepientString.setId("custom-label-from");
		return mainPanel;
	}

	private void getGroupMessageBody(HeaderModel header) {
		String content = "";
		try {
			if (header.getPriority() == GroupModel.OPERATION_CREATE_GROUP) {
				content = AppConfiguration.appConfString.group_created + "";

			} else if (header.getPriority() == GroupModel.OPERATION_ADD_MEMBER) {
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

				if (members.length() > 1) {
					content = titleString + " " + AppConfiguration.appConfString.are_added_to_group + "";
				} else {
					content = titleString + " " + AppConfiguration.appConfString.added_to_group + "";
				}
			} else if (header.getPriority() == GroupModel.OPERATION_DELETE_MEMBER) {
				// DBContactsMapper profileMapper =
				// DBContactsMapper.getInstance();
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

				// DBContactsMapper profileMapper =
				// DBContactsMapper.getInstance();
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

			String name = getRecpDisplayString(null, "", header.getGroupId());
			String date = getTimeString(header, header.getCreationTime());

			lblBodyString.setText(content);
			lblDate.setText(date + "   ");
			lblrecepientString.setText(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getNewGroupMessageBody(HeaderModel header) {
		try {
			updateGroupIcon(header.getGroupId());
			String content = "";
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
			String name = getRecpDisplayString(null, "", header.getGroupId());
			String date = getTimeString(header, header.getCreationTime());
			lblBodyString.setText(content);
			lblDate.setText(date + "   ");
			lblrecepientString.setText(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return;
	}

	private void setWelcomeMessageView(HeaderModel header) {

		Vector recipientList = recipientMapper.getReceipientsModelByHeaderId(header.getLocalHeaderId());

		String messageString = getInMessageString(header, recipientList, header.getSubject());
		String from = header.getMessageFrom();
		String recpDisplayString = getRecpDisplayString(recipientList, from, "");
		String date = getTimeString(header, header.getCreationTime());
		ImagePattern img = new ImagePattern(new Image(AppConfiguration.getIconPath()+"ic_cashew.png"));
		final Circle clip = new Circle();
		clip.setRadius(21);
		clip.setFill(img);
		
		lblBodyString.setText(messageString);
		lblDate.setText(date + "   ");
		lblrecepientString.setText(recpDisplayString);
		leftPane.getChildren().add(clip);
		//leftPane.getChildren().add(lblUserIcon);
	}

	private void setScreenShotData(HeaderModel model)
	{
		DBContactsMapper profileMapper = DBContactsMapper.getInstance();
		ContactsModel contactModel=new ContactsModel();
		MessageViewModel messageViewModel = null;
		String screenShotString="";
		if (!model.getMessageFrom().equals(cashewnutId)) {
			// in message
			messageViewModel = getInMessageView(model,0);
			
				contactModel=profileMapper.getContact(model.getMessageFrom(),0);
				screenShotString=contactModel.getFirstName();
			
		} else {
			// sent message
			messageViewModel = getSentMessageView(model);
			screenShotString="You";
		}
		setPriority(messageViewModel);
		String message = messageViewModel.getMessage().trim();
		message=screenShotString+" "+message;
		message = message.replace("\n\r", " ");
		message = message.replace("\n", " ");
		message = message.replace("\r", " ");	
			 Text text;
			 text=new Text(message);
			 messagePane.getChildren().add(text);
			 String from = messageViewModel.getMessageFrom();
				lblrecepientString.setText(from);
	}
	private void setData(HeaderModel model, int unreadMessageCount) throws UnsupportedEncodingException {
		MessageViewModel messageViewModel = null;		
		if (!model.getMessageFrom().equals(cashewnutId)) {
			// in message
			messageViewModel = getInMessageView(model, unreadMessageCount);
		} else {
			// sent message
			messageViewModel = getSentMessageView(model);
		}
		setPriority(messageViewModel);

		String message = messageViewModel.getMessage().trim();
		message = message.replace("\n\r", " ");
		message = message.replace("\n", " ");
		message = message.replace("\r", " ");

		if (!model.getMessageFrom().equals(cashewnutId) && messageViewModel.getAck() == 1) {
			lblBodyString.setText(MessageControlParameters.ACK_MESSAGE);
		} else if (!model.getMessageFrom().equals(cashewnutId) && messageViewModel.isExpiry()
				&& model.getExpiryStartTime() < 0 && messageViewModel.getAck() < 0) {
			lblBodyString.setText(MessageControlParameters.EXPIRY_MESSAGE);
		} else {
			 Text text;
			 text=new Text(message);
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
		    		messagePane.getChildren().add(text2);
		    		 }
		    		 else
		    		 {
		    			  str214=string1.substring(end,matcher.start());
				    		str215=string1.substring(matcher.end());
				    		Text text2;
				    		text2=new Text(str214);
				    		messagePane.getChildren().add(text2);
		                	
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
			                				messagePane.getChildren().add(ConversationsViewRenderer.getImageView(entry.getValue(),19));
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
		    			text2=new Text(lastString);	    		
		    			messagePane.getChildren().add(text2);
		    	}
		    	differenceLength=emojisLength-duplicate;
		    	
		    	if(end<1)
		    	{
		    		messagePane.getChildren().add(text);
		    	}
	    	
			//lblBodyString.setText(message);

		}

		lblDate.setText(messageViewModel.getDate() + "   ");

		if (messageViewModel.getSendStatus1() == 0) {
			// sending
			// lblDeliveryIcon.setGraphic();
		}
		if (messageViewModel.getSendStatus1() == 1) {
			// sent
			lblDeliveryIcon.setGraphic(single_tick_icon);
			priorityPane.getChildren().add(lblDeliveryIcon);
		}
		if (messageViewModel.getSendStatus1() == 3) {
			// delivered
			lblDeliveryIcon.setGraphic(dubble_tick_icon);
			priorityPane.getChildren().add(lblDeliveryIcon);
		}
		if (messageViewModel.getSendStatus1() == 2) {
			// failed
			// lblDeliveryIcon.setGraphic();
			lblDeliveryIcon.setGraphic(failure_tick_icon);
			priorityPane.getChildren().add(lblDeliveryIcon);
		}
		if (messageViewModel.getSendStatus1() == 4) {
			lblDeliveryIcon.setGraphic(expiry_iconw);
			priorityPane.getChildren().add(lblDeliveryIcon);
		}
		String from = messageViewModel.getMessageFrom();
		lblrecepientString.setText(from);
	}

	public void setPriority(MessageViewModel messageViewModel) {
		int priority = messageViewModel.getPrority();
		switch (priority) {
		case MessageControlParameters.PRIORITY_INDICATOR_GRAY:
			lblPriorityIcon.setGraphic(priority_icongy);
			messagePane.getChildren().add(0, lblPriorityIcon);
			break;
		case MessageControlParameters.PRIORITY_INDICATOR_GREEN:
			lblPriorityIcon.setGraphic(priority_icongr);
			messagePane.getChildren().add(0, lblPriorityIcon);
			break;
		case MessageControlParameters.PRIORITY_INDICATOR_YELLOW:
			lblPriorityIcon.setGraphic(priority_icony);
			messagePane.getChildren().add(0, lblPriorityIcon);
			break;
		case MessageControlParameters.PRIORITY_INDICATOR_RED:
			lblPriorityIcon.setGraphic(priority_iconr);
			messagePane.getChildren().add(0, lblPriorityIcon);
			break;
		default:
			lblPriorityIcon.setGraphic(priority_icongy);
			messagePane.getChildren().add(0, lblPriorityIcon);
		}

		if (messageViewModel.isRestructed()) {
			priorityPane.getChildren().add(lblrestrictedIcon);
		}
		if (messageViewModel.isExpiry()) {
			priorityPane.getChildren().add(lblExpiryIcon);
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
		if (messageViewModel.getAttachment() != null && !messageViewModel.getAttachment().trim().equals("")) {
			priorityPane.getChildren().add(lblAttachmentIcon);
		}
	}

	// ======================================================================================
	private MessageViewModel getInMessageView(HeaderModel header, int unreadCount) {
		// get data from header
		int localId = header.getLocalHeaderId();
		String from = header.getMessageFrom();
		int priority = header.getPriority();
		boolean isRestrictedMessage = header.getRestricted() == MessageControlParameters.RESTRICTED;

		boolean isExpiryMessage = header.getExpiry() > -1;
		boolean isAckMessage = header.getMessageAck() == MessageControlParameters.ACK_SENDER;
		String timeString = getTimeString(header, header.getCreationTime());

		int recpAckStatus = -1;
		int recpReadStatus = -1;
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

		String recpDisplayString = "";
		if (isGroupMessage(header)) {
			recpDisplayString = getRecpDisplayString(null, "", header.getGroupId());
			updateGroupIcon(header.getGroupId());

		} else {
			recpDisplayString = getRecpDisplayString(recipientList, from, "");
			leftPane.getChildren().add(lblUserIcon);
		}
		if (recpDisplayString == null || recpDisplayString.trim().equals("")) {
			recpDisplayString = "   ";
			// leftPane.getChildren().add(lblUserIcon);
		}

		Text text = (Text) lblUserIcon.getChildren().get(1);
		text.setText(recpDisplayString.charAt(0) + "");
		lblCount.setText(" " + unreadCount + " ");
		if (unreadCount > 0) {
			lblCount.setVisible(true);
			// lblBodyString.setStyle("-fx-font-weight: bold;");
		} else {
			lblCount.setVisible(false);
		}

		messageViewModel.setMessageFrom(recpDisplayString);

		String messageString = getInMessageString(header, recipientList, header.getSubject());
		if (isAckMessage && !isSelfMessage) {
			if (recpAckStatus == MessageControlParameters.ACK_READ_RECEIPT) {
				messageViewModel.setAck(0);
			} else {
				if(header.getMessageType()!=7)
				{
				messageString = MessageControlParameters.ACK_MESSAGE;
				messageViewModel.setAck(1);
				}
			}
		}
		messageViewModel.setMessage(messageString);
		if (header.getNumberOfBodyparts() > 2) {
			messageViewModel.setAttachment("attachment url");
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

	// public int getTotalUnreadMessages() {
	// int unreadCount = 0;
	// for (ConversationPrimer cp : selectedHeaders) {
	// if (cp.getUnReadCount() > 0) {
	// unreadCount = unreadCount + cp.getUnReadCount();
	// }
	// }
	// return unreadCount;
	// }
	private String getInMessageString(HeaderModel header, Vector recipientList, String subject) {
		if (subject == null || subject.equalsIgnoreCase("no subject") || subject.trim().equals("")) {
			Key key = null;
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

				if (subject != null && subject.length() > 20) {
					subject = subject.substring(0, 20) + "..";
				}

				if (header.getNumberOfBodyparts() > 2) {
					if (subject == null || subject.trim().equals("")) {
						subject = "(Attachment)" + "";
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return subject + "     ";
	}

	// ==============================================================================
	private String getRecpDisplayString(Vector<RecipientModel> recipientList, String from, String groupId) {
		try {
			StringBuffer recpDisplayString = new StringBuffer();
			ContactsModel contactsModel = null;
			String recpentName = "";
			String recpCashewId = "";
			if (groupId != null && !groupId.trim().equals("")) {
				DBGroupsMapper groupMapper = DBGroupsMapper.getInstance();
				GroupModel gModel = groupMapper.getGroup(groupId, true);
				recpDisplayString.append(gModel.getGroupName());

			} else if (recipientList.size() > 1 || from.trim().equals("")) {
				for (int i = 0; i < recipientList.size(); i++) {
					RecipientModel recipient = (RecipientModel) recipientList.elementAt(i);
					contactsModel = profileMapper.getContact(recipient.getRecepientCashewnutId(),0);
					recpentName = contactsModel.getFirstName();
					recpCashewId = recipient.getRecepientCashewnutId();
					if (i > 0) {
						recpDisplayString.append(", ");
					}
					if (recpentName != null && !recpentName.trim().equals("")) {
						recpDisplayString.append(recpentName.trim());
					} else {
						recpDisplayString.append(recpCashewId);
					}
				}
			} else {
				contactsModel = profileMapper.getContact(from,0);
				recpentName = contactsModel.getFirstName();
				if (recpentName != null && !recpentName.trim().equals("")) {
					recpDisplayString.append(recpentName + " ");
				} else {
					recpDisplayString.append(from + " ");
				}
			}
			return recpDisplayString.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private MessageViewModel getSentMessageView(HeaderModel header) {
		MessageViewModel messageViewModel = new MessageViewModel();
		// get data from header
		int localId = header.getLocalHeaderId();

		int priority = header.getPriority();
		boolean isRestrictedMessage = header.getRestricted() == MessageControlParameters.RESTRICTED;
		long scheduleTime = header.getScheduleTime();
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

		String recpDisplayString = "";

		if (isGroupMessage(header)) {
			recpDisplayString = getRecpDisplayString(null, "", header.getGroupId());
			updateGroupIcon(header.getGroupId());

		} else {
			recpDisplayString = getRecpDisplayString(recipientList, "", "");
			leftPane.getChildren().add(lblUserIcon);
		}

		if (recpDisplayString == null || recpDisplayString.length() < 1) {
			recpDisplayString = "   ";

		}

		Text text = (Text) lblUserIcon.getChildren().get(1);
		text.setText(recpDisplayString.charAt(0) + "");

		messageViewModel.setMessageFrom(recpDisplayString);

		if (isAckMessage) {
			if (recipientList.size() == acknowledgedCount) {
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
			messageViewModel.setAttachment("attachment url");
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
			// ControlMessageOptions.setSenderScheduler(header.getLocalHeaderId(),
			// header);

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
			Key key = null;
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
				// System.out.println("" + e.getMessage());
				subject = body;
			}

			if (subject != null && subject.length() > 20) {
				subject = subject.substring(0, 20) + "..";
			}

			if (header.getNumberOfBodyparts() > 2) {
				if (subject == null || subject.trim().equals("")) {
					subject = "(Attachment)" + "";
				}
			}
		}
		return subject + "     ";
	}

	public boolean isGroupMessage(HeaderModel header) {
		if (header.getGroupId() != null && !header.getGroupId().equals("")) {
			return true;
		}
		return false;
	}

	public String getTimeString(HeaderModel header, long creationTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy");

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		long todayTime = today.getTime().getTime();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(creationTime);

		if (todayTime < creationTime) {
			// message came today. display only time
			return formatter.format(creationTime);
		} else if (calendar.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {
			return formatter2.format(creationTime);
		} else {
			return formatter1.format(creationTime);
		}
	}

	// ==============================================================================

	private class MessageViewModel {

		String message;
		String attachment;
		String date;
		String messageFrom;

		private int type;
		int boxType;
		int prority = -1;
		boolean restructed;
		boolean expiry;
		int sendStatus = -1;
		int ack = -1;

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

		public String getAttachment() {
			return attachment;
		}

		public void setAttachment(String attachment) {
			this.attachment = attachment;
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

	public void updateGroupIcon(String groupId) {
		File f = null;
		String absolutePath = Helper.getAbsolutePath("group_profile");
		File[] files = new File(absolutePath).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.startsWith(groupId)) {
					f = file.getAbsoluteFile();
					break;
				}
			}

		}
		if (f != null) {
			ImagePattern img = new ImagePattern(new Image(f.toURI().toString()));
			final Circle clip = new Circle();
			clip.setRadius(21);
			clip.setFill(img);
			leftPane.getChildren().add(clip);
		} else {
			leftPane.getChildren().add(lblUserIcon);
		}
	}
}
