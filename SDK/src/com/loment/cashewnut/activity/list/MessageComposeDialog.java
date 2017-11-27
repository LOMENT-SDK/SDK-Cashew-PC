/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.activity.controller.UserDetailsForm;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.model.SettingsModel;
import com.loment.cashewnut.sender.SenderHandler;
import com.loment.cashewnut.sthithi.connection.SubscriptionHandler;
import com.loment.cashewnut.util.ContentType;
import com.loment.cashewnut.util.Helper;

/**
 *
 * @author sekhar
 */
public class MessageComposeDialog extends Stage implements Initializable {

	@FXML
	BorderPane borderPane;

	@FXML
	HBox toolBar;

	@FXML
	private Label composeLabel;

	@FXML
	private Button composeSettingsImageButton;
	@FXML
	private Button composeAttachmentButton;
	@FXML
	private Button composeSendButton;
	@FXML
	private HTMLEditor htmlEditor;
	@FXML
	private HBox composeAttachmentHBox;
	@FXML
	private TextField recpTextField;
	@FXML
	private Button closeButton;

	@FXML
	private BorderPane composeBorderPane;	
	@FXML
	private ImageView settingsImageView1;
	@FXML
	private ImageView settingsImageView11;
	@FXML
	private ImageView settingsImageView111;
	@FXML
	private ImageView emojiButtonImageView;
	@FXML
	private Button emojiButton;
	public MessageComposeDialog() {

	}

	public MessageComposeDialog(Stage owner, double width, double heigth) {
		super();
		try {
			initOwner(owner);

			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_compose.fxml"));
			borderPane=fl.load();
			composeBorderPane=(BorderPane)borderPane.getChildren().get(0);
			HBox newOne=(HBox) composeBorderPane.getRight();
			emojiButton=(Button) newOne.getChildren().get(0);
			//System.out.println(newOne.getChildren());
			Parent root = fl.getRoot();

			Scene scene = new Scene(root);
			this.setScene(scene);
			scene.getStylesheets().add(
					ConversationView.class.getResource("messageCompose.css")
							.toExternalForm());
			this.getIcons().add(
					new Image(ConversationView.class
							.getResourceAsStream(AppConfiguration
									.getAppLogoPath() + "cashew48.png")));

			this.initStyle(StageStyle.UNDECORATED);
			this.initModality(Modality.WINDOW_MODAL);
			// this.alwaysOnTopProperty();
			// this.initModality(Modality.APPLICATION_MODAL);
			// this.centerOnScreen();
			this.setResizable(false);

			this.setWidth(width - 50);
			this.setHeight(heigth - 100);
			double Left = this.getOwner().getX() + this.getOwner().getWidth()
					- this.getWidth();
			double Top = this.getOwner().getY() + this.getOwner().getHeight()
					- this.getHeight();

			this.setX(Left);
			this.setY(Top);

		} catch (Exception ex) {
			Logger.getLogger(MessageSettingsDialog.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getComposeLayout1();
		hideHTMLEditorToolbars();
		setStrings();
		//setComposeTextAreaHeight(composeTextArea);
	}

	MediaPlayer mediaPlayer = null;
	String groupId = "";
	String adapterFrom = "";
	String adapterThreadId = "";
	private final HashMap<String, String> attachmentsMap = new HashMap<String, String>();
	 private void hideHTMLEditorToolbars(){
		 new Thread(){
			 public void run()
			 {	
	       
	        Platform.runLater(() -> {
	        
	            Node[] nodes = htmlEditor.lookupAll(".tool-bar").toArray(new Node[0]);
	            for (Node node : nodes) {
	                node.setVisible(false);
	                node.setManaged(false);
	            }
	            htmlEditor.setVisible(true);
	        });
			 }
		 }.start();
	 }
	private void getComposeLayout1() {
		
		if (composeSendButton != null) {

			borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
					+ AppConfiguration.getBorderColour() + ";");
			toolBar.setStyle("-fx-background-color:"
					+ AppConfiguration.getBorderColour() + ";");

			settingsImageView1.setImage(new Image(ConversationView.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "ic_action_gear.png")));
			settingsImageView11.setImage(new Image(ConversationView.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "ic_action_attachment.png")));
			settingsImageView111.setImage(new Image(ConversationView.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "ic_action_send.png")));

			// String rightImage = ConversationView.class.getResource(
			// "/resources/background/background2.png").toExternalForm();
			//
			// borderPane.setStyle("-fx-background-image: url(\"" + rightImage
			// + "\");");
			emojiButtonImageView.setImage(new Image(ConversationView.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60a.png")));
			setControlParameters();
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
			composeSendButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					sendMessage();
				}
			});

			composeSettingsImageButton
					.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							CashewnutActivity.idleTime = System.currentTimeMillis();
							createContent();
						}
					});

			// ======================================================================
			final VBox attachmentPane = new VBox();
			attachmentPane.setPadding(new Insets(10, 10, 10, 10));
			attachmentPane.setAlignment(Pos.BOTTOM_RIGHT);

			final Button attachment = new Button("");
			attachment.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
			attachment.setContentDisplay(ContentDisplay.LEFT);
			ImageView cancelImage = ConversationsViewRenderer.resize1(
					AppConfiguration.getIconPath() + "cancel.png", 28);

			Button cancel = new Button("");
			cancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								CashewnutActivity.idleTime = System.currentTimeMillis();
								attachment.setGraphic(null);
								attachmentsMap.clear();
								if (composeAttachmentHBox.getChildren().size() > 0) {
									composeAttachmentHBox.getChildren().remove(
											0);
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
			composeAttachmentButton
					.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							try {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										CashewnutActivity.idleTime = System.currentTimeMillis();
										setAttachmentToComposeLayout();
									}
								});
							} catch (Exception e1) {
							}
						}

						private void setAttachmentToComposeLayout() {
							final File file = getDownloadsPathOfFile();
							if (file != null) {
								attachment.setGraphic(null);
								attachmentsMap.clear();
								if (composeAttachmentHBox.getChildren().size() > 0) {
									composeAttachmentHBox.getChildren().remove(
											0);
								}
								attachmentPane.getChildren().remove(1);
								if (mediaPlayer != null) {
									mediaPlayer.dispose();
								}
								String path = file.getPath().replace('\\', '/');
								String extension = ContentType
										.getContentType(path);
								if (ContentType.isVideoType(extension)) {
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											try {
												Media media = new Media(file
														.toURI().toURL()
														.toString());
												mediaPlayer = new MediaPlayer(
														media);
												mediaPlayer.setAutoPlay(false);
												MediaControl mediaControl = new MediaControl(
														mediaPlayer, -1);
												attachmentPane.getChildren()
														.add(mediaControl);
												attachmentPane
														.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
											} catch (Exception ex) {
											}
										}
									});
								} else if (ContentType.isAudioType(extension)) {
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											try {
												Media media = new Media(file
														.toURI().toURL()
														.toString());
												mediaPlayer = new MediaPlayer(
														media);
												mediaPlayer.setAutoPlay(false);
												MediaControl mediaControl = new MediaControl(
														mediaPlayer, 1);
												attachmentPane.getChildren()
														.add(mediaControl);
												attachmentPane
														.setStyle("-fx-background-color: Snow;");
											} catch (Exception ex) {
											}
										}
									});
								} else if (ContentType.isImageType(extension)) {
									ImageView user_icon_single2 = ConversationsViewRenderer
											.getImageIcon(path, 100, 100);
									attachment.setGraphic(user_icon_single2);
									attachmentPane.getChildren()
											.add(attachment);
								} else {
									ImageView imageView = ConversationsViewRenderer
											.getDefaultBitmap(extension);
									if (imageView != null) {
										attachment.setGraphic(imageView);
										attachmentPane.getChildren().add(
												attachment);
									}
								}
								attachmentsMap.put(file.getName(), path);
								// System.out.println(path);
								// System.out.println(file.getName());
								composeAttachmentHBox.getChildren().add(
										attachmentPane);
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
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						htmlEditor.setHtmlText("");
						recpTextField.setText("");
						attachmentsMap.clear();
						if (composeAttachmentHBox.getChildren().size() > 0) {
							composeAttachmentHBox.getChildren().remove(0);
						}
						Stage stage = (Stage) htmlEditor.getScene()
								.getWindow();
						stage.close();
					}
				});
			}
		});

		try {
			SettingsModel model = new SettingsStore().getSettingsData();
			if (model != null) {
				String selectedFamily = model.getFontFamily();
				if (selectedFamily != null && !selectedFamily.trim().equals("")) {
					String style = "-fx-font-family: " + selectedFamily + "; "
							+ "-fx-border-width: 4; -fx-background-insets: 5;"
							+ " -fx-border-color: #00e5ee;";
					borderPane.setStyle(style);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setStrings() {
		/*if (composeTextArea != null)
			composeTextArea
					.setPromptText(AppConfiguration.appConfString.compose_type_message);*/

		if (recpTextField != null)
			recpTextField
					.setPromptText(AppConfiguration.appConfString.message_compose_to_hint);

		if (composeLabel != null)
			composeLabel
					.setText(AppConfiguration.appConfString.compose_compose);

	}

	private void setControlParameters() {
		SettingsModel model = new SettingsStore().getSettingsData();
		if (model == null) {
			model = new SettingsModel();
			int priority_status = -1;
			int expiry_status = -1;
			int ack_status = -1;
			int restricted_status = -1;
			int expiry_mins = -1;

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
	}

	private void sendMessage() {
		String cashewId = recpTextField.getText().trim().toLowerCase();
		if (validate(cashewId)) {
			DBContactsMapper profileMapper = DBContactsMapper.getInstance();
			ContactsModel contactsModel = profileMapper.getContact(cashewId,0);
			String recpentName = contactsModel.getFirstName();
			if (recpentName != null && !recpentName.trim().equals("")) {
				send();
			} else {
				DBLomentDataMapper lomentDataMapper = DBLomentDataMapper
						.getInstance();
				LomentDataModel lomentDataModel = lomentDataMapper
						.getLomentData();
				String uName = lomentDataModel.getLomentId();
				String pWord = lomentDataModel.getPassword();

				UserDetailsForm service = new UserDetailsForm();
				service.setUseName(uName);
				service.setPassword(pWord);
				service.setCashewId(cashewId);
				service.setStatus(0);
				service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						boolean response = (boolean) t.getSource().getValue();
						if (response) {
							send();
						} else {
							System.out.println("Receipient not exist");
							Stage dialogStage = getDialog(AppConfiguration.appConfString.message_compose_not_exist);
							dialogStage.show();
							// cashew id not exist
						}
					}
				});
				service.start();
			}
		} else {
			// enter valid receipient
			// System.out.println("Enter valid receipient");
		}
	}

	private void send() {
		if (!SubscriptionHandler.getInstance().getSubscriptionStatusFromDB()) {
			return;
		}
		String to = recpTextField.getText().trim().toLowerCase();

		DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
		AccountsModel accountsModel = accountsMapper.getAccount();
		String from = accountsModel.getCashewnutId();
	    String extension="0x";	  		 				  		
	  		String testing = htmlEditor.getHtmlText().toString();
	  		String imageName="";		
	  		while(testing.contains(".png"))
	  		{	  		
	  			imageName=testing.substring(testing.indexOf(".png")-5,testing.indexOf(".png"));
	  			String hexaCode=new String(Character.toChars(Integer.decode(extension.concat(imageName))));
	  			String imagePath=testing.substring(testing.indexOf(".png")-56, testing.indexOf(".png")+29);
	  			testing=testing.replaceFirst(imagePath, hexaCode);
	  			
	  		}
	  		String nohtml = testing.replaceAll("\\<.*?>","");	  		
	  		String resulting=nohtml.replace("&nbsp;", "");	  		
	  		if (resulting.trim().equals("") && attachmentsMap.size() < 1 ) {				
				return;
			}
		final MessageModel messageModel = SenderHandler.getInstance()
				.constructMessage(from, to, "  ", resulting,
						attachmentsMap, controlParameters);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				SenderHandler.getInstance().send(messageModel);
				htmlEditor.setHtmlText("");
				simpleIntegerProperty.setValue(20);
				recpTextField.setText("");
				attachmentsMap.clear();
				if (composeAttachmentHBox.getChildren().size() > 0) {
					composeAttachmentHBox.getChildren().remove(0);
				}
				Stage stage = (Stage) htmlEditor.getScene().getWindow();
				stage.close();
			}
		});
	}

	int composeTextAreaRowHeight = 2;
	SimpleIntegerProperty simpleIntegerProperty = new SimpleIntegerProperty(20);

	
	private static final String CASHEWNUTID_PATTERN = "^([A-Z]|[a-z]|[0-9])+(\\.|_)?([A-Z]|[a-z]|[0-9])+$";

	public boolean validate(final String cashewnutID) {
		boolean ismaches = false;
		if (cashewnutID != null && !cashewnutID.trim().equals("")
				&& cashewnutID.length() > 2) {
			Pattern pattern = Pattern.compile(CASHEWNUTID_PATTERN);
			Matcher matcher = pattern.matcher(cashewnutID);
			ismaches = matcher.matches();
		}
		return ismaches;

	}

	MessageSettingsDialog chooseStage = null;
	private final MessageControlParameters controlParameters = new MessageControlParameters();

	public void createContent() {
		if (chooseStage != null && chooseStage.isShowing()) {
			chooseStage.close();
			return;
		}

		Stage stage = (Stage) composeSettingsImageButton.getScene().getWindow();

		double x = stage.getX() + composeSettingsImageButton.getWidth();
		double y = stage.getY() + stage.getHeight() - 268
				- composeBorderPane.getHeight();

		chooseStage = new MessageSettingsDialog(controlParameters, stage, x, y);
		chooseStage.showAndWait();
	}

	public File getDownloadsPathOfFile() {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		Stage stage = (Stage) htmlEditor.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		// Set extension filter
		fileChooser.setTitle(AppConfiguration.appConfString.select_attachment);
		return fileChooser.showOpenDialog(stage);
	}

	EmojiPopUp popupStage =null;
	public void createEmojiContent() throws IOException {
		if (popupStage != null && popupStage.isShowing()) {
			popupStage.close();
			return;
		}
		//CashewnutActivity.recentEmoji=false;
		 //splitPane.setOpacity(0.8);
		 Stage stage = (Stage) emojiButton.getScene().getWindow();
		 Bounds localBounds = emojiButton.localToScene(emojiButton.getBoundsInLocal());
		 double x = stage.getX() + localBounds.getMaxX() + emojiButton.getWidth();
		 double y = stage.getY() + localBounds.getMaxY() - 120;
		 Popup popup = new Popup();
    	 popup.centerOnScreen();
         popupStage = new EmojiPopUp(stage, x, y);
         popup.setX(600);
         popup.setY(120);
         popup.getContent().addAll(popupStage.EmojiBorder);
         popup.show(popupStage.getOwner());
     	 ToolBar tb=(ToolBar) popupStage.EmojiBorder.getTop();
         popupStage.EmojiTabPane=(TabPane) popupStage.EmojiBorder.getChildren().get(0);
         popupStage.RecentAnchor=(AnchorPane) popupStage.EmojiTabPane.getTabs().get(1).getContent();
         popupStage.peopleGrid=(GridPane) popupStage.RecentAnchor.getChildren().get(0);
         popupStage.ObjectGrid=(GridPane)popupStage.objectAnchor.getChildren().get(0);
         popupStage.NatureGrid=(GridPane)popupStage.natureAnchor.getChildren().get(0);
         popupStage.PlacesGrid=(GridPane)popupStage.placesAnchor.getChildren().get(0);
         popupStage.SymbolGrid=(GridPane)popupStage.symbolAnchor.getChildren().get(0);
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
                 for( Node node:  popupStage.peopleGrid.getChildren()) {
                     if( node instanceof ImageView) {
                         if( node.getBoundsInParent().contains(e.getX(), e.getY())) {                           
                             for(Entry<String, Image> entry :Emoji.sEmojisMap.entrySet())
                      	   {	 
                             if(entry.getValue().equals(((ImageView) node).getImage()))
                             {	
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
                for( Node node:  popupStage.ObjectGrid.getChildren()) {
                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getX(), e.getY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            for(Entry<String, Image> entry :Emoji.sEmojisMap.entrySet())
                     	   {	 
                            if(entry.getValue().equals(((ImageView) node).getImage()))
                            {	
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
                for( Node node:  popupStage.NatureGrid.getChildren()) {
                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getX(), e.getY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            for(Entry<String, Image> entry :Emoji.sEmojisMap.entrySet())
                     	   {	 
                            if(entry.getValue().equals(((ImageView) node).getImage()))
                            {	
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
                for( Node node:  popupStage.PlacesGrid.getChildren()) {
                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getX(), e.getY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            for(Entry<String, Image> entry :Emoji.sEmojisMap.entrySet())
                     	   {	 
                            if(entry.getValue().equals(((ImageView) node).getImage()))
                            {	
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
                for( Node node:  popupStage.SymbolGrid.getChildren()) {
                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getX(), e.getY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            for(Entry<String, Image> entry :Emoji.sEmojisMap.entrySet())
                     	   {	 
                            if(entry.getValue().equals(((ImageView) node).getImage()))
                            {	
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
                for( Node node:  popupStage.RecentGrid.getChildren()) {
                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getX(), e.getY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            for(Entry<String, Image> entry :Emoji.RecentsEmojisMap.entrySet())
                     	   {	 
                            if(entry.getValue().equals(((ImageView) node).getImage()))
                            {
                            	try {
                            		int sil=Integer.decode(entry.getKey());                  	
                            		StringBuilder htmlText=new StringBuilder(htmlEditor.getHtmlText().replace("</p>", ""));
                            		String imgContent="<img src=\"" + 
                              				getClass().getResource("/resources/loment_icons/emojis/emoji_"+entry.getKey().substring(2)+".png")        				 + 
                         				     "\" width=\"18\" height=\"20\" >";
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

	
	}
	
	 public void emojitoImage(String emojiKey,Image emojiImage)
	 {
		 String image="";
			try {
				
				image="0x"+String.format("%010x", new BigInteger(1, emojiKey.getBytes("UTF-32"))).replaceFirst("^0+(?!$)", "");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
	 	 StringBuilder htmlText=new StringBuilder(htmlEditor.getHtmlText().replace("</p>", ""));
	 	 String imgContent="<img src=\"" + 
				getClass().getResource("/resources/loment_icons/emojis/emoji_"+image.substring(2)+".png")        				 + 
			     "\" width=\"18\" height=\"20\" >";
	 	 htmlText.append(imgContent);
	 	 htmlEditor.setHtmlText(htmlText.substring(0));                            	
	 	 //composeTextArea.appendText(emojiKey);
	 	 recentEmoji(emojiKey,emojiImage); 
	 }
	 
		public void recentEmoji(String entry,Image img)
		{
			String absolutePath = Helper.getAbsolutePath("RecentEmojis");
		  	 BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
	    	 String decimal=null;
			try {
				decimal = String.format("%010x", new BigInteger(1, entry.getBytes("UTF-32")));
			} catch (UnsupportedEncodingException e2) {								
				e2.printStackTrace();
			}							
	 		String sourceFilePath = absolutePath+"0x"+decimal.replaceFirst("^0+(?!$)", "")+".png";
	 	File f = new File(sourceFilePath);
	    		try {
					ImageIO.write(bImage, "png", f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    		
	    	popupStage.setRecentEmoji();
		}
	private Stage getDialog(String body) {
		final Stage dialogStage = new Stage();
		Button ok = new Button(AppConfiguration.appConfString.dlg_ok);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				dialogStage.close();
			}
		});
		VBox vbox = VBoxBuilder.create()
				.children(new Text(body), new Label("      "), ok)
				.alignment(Pos.CENTER).padding(new Insets(5)).build();
		dialogStage.initModality(Modality.WINDOW_MODAL);

		GridPane gridpane = new GridPane();
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(10);
		gridpane.setVgap(10);

		final String cssDefault = "-fx-border-color: #A8A8A8;\n"
				+ "-fx-border-width: 6;\n";

		gridpane.add(vbox, 1, 1, 10, 10);
		gridpane.setStyle(cssDefault);
		dialogStage.setScene(new Scene(gridpane));
		return dialogStage;
	}
}
