/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.activity.view.javafx.LoginFormJfx;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.SettingsModel;

/**
 *
 * @author sekhar
 */
public class MainMessageSettings implements Initializable {

	@FXML
	Button aboutButton;
	@FXML
	Button saveButton;
	@FXML
	Button cancelButton;
	@FXML
	Button reportsButton;
	@FXML
	Button changePassword;
	@FXML
	TextField mainSettingsExpireTextField;
	@FXML
	CheckBox mainSettingsExpireCheckbox;
	@FXML
	CheckBox mainSettingsPriorityCheckbox;
	@FXML
	CheckBox mianSettingsRestrictedCheckBox;
	@FXML
	CheckBox mainSettingsAckCheckBox;
	@FXML
	CheckBox mainSettingsPlaySoundCheckBox;
	@FXML
	CheckBox mainSettingsEnableCheckBox;
	@FXML
	ComboBox mainSettingsHibernateComboBox;
	@FXML
	ComboBox mainSettingsPriorityComboBox;
	@FXML
	ComboBox mainSettingsLanguageComboBox;
	@FXML
	CheckBox mainSettingsAutoResponseCheckBox;
	@FXML
	TextField mainSettingsAutoResponseTextField;
	@FXML
	HBox rightTitleHBox;
	@FXML
	Label titleLabel;
	@FXML
	Label accountLabel;
	@FXML
	Label secureandprivacyLabel;
	@FXML
	Label priorityIndicatorLabel;
	@FXML
	Label notificationSettingsLabel;
	@FXML
	Label additionalSettingsLabel;
	@FXML
	Label hibernateLabel;
	@FXML
	Label languageLabel;
	@FXML
	ImageView aboutImageView;

	@FXML
	ImageView reportsImageView;

	@FXML
	ImageView changePasswordImageView;

	@FXML
	ListView fontListView;
	@FXML
	Label fontLabel;
	@FXML
	ScrollPane scroll;
	@FXML
	ImageView saveButtonImage;
	int maxLength = 3;
	MessageControlParameters controlParameters;
	private SplitPane splitPane;
	List<String> familiesList = null;
	ObservableList<String> familiesObservableList = null;

	// private static final String STANDARD_BUTTON_STYLE =
	// "-fx-effect: dropshadow( three-pass-box , rgba(1,1,1,1) , 1, 1 , 1, 1 );\n"
	// + "    -fx-background-color: #F8F8F8;";
	//
	// private static final String HOVERED_BUTTON_STYLE =
	// " -fx-effect: dropshadow( three-pass-box , rgba(1,1,1,1) , 1, 1 , 1 , 1 );\n"
	// + "    -fx-background-color: linear-gradient(#F8F8F8 , #F8F8F8 );";
	public MainMessageSettings() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setPriority();
		setExpire();
		setAutoResponse();
		setHibernate();
		setLanguage();
		setSettingsData();
	
		// changeBackgroundOnHoverUsingBinding(aboutButton);
		// changeBackgroundOnHoverUsingBinding(reportsButton);
		// changeBackgroundOnHoverUsingBinding(changePassword);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_PREF_SIZE);
		rightTitleHBox.getChildren().add(2, spacer);
		titleLabel.setId("custom-label-title");
		try {
			aboutImageView.setImage(new Image(MainMessageSettings.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "ic_action_info.png")));

			reportsImageView.setImage(new Image(MainMessageSettings.class
					.getResourceAsStream(AppConfiguration.getIconPath()
							+ "ic_action_document.png")));

			changePasswordImageView.setImage(new Image(
					MainMessageSettings.class
							.getResourceAsStream(AppConfiguration.getIconPath()
									+ "ic_action_lock_closed_1.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveButtonImage.setImage(new Image(MainMessageSettings.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "ic_action_save.png")));

		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				saveSettings();
			}
		});

		mainSettingsEnableCheckBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				if (mainSettingsEnableCheckBox.isSelected()) {
					mainSettingsHibernateComboBox.setDisable(false);
				} else {
					mainSettingsHibernateComboBox.setDisable(true);
				}
			}
		});
if(scroll.getOnMouseMoved() != null)
{
	CashewnutActivity.idleTime = System.currentTimeMillis();
}
		aboutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					setStage(e,1);
					CashewnutActivity.idleTime = System.currentTimeMillis();
					System.out.println("About");
				
				} catch (Exception ex) {
					Logger.getLogger(MainMessageSettings.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		});

		reportsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					//System.out.println("About");
					setStage(e,3);	
				} catch (IOException ex) {
					Logger.getLogger(MainMessageSettings.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		});

		changePassword.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					// System.out.println("PASSWORD");
					// changePassword();
					setStage(e,2);			
				} catch (IOException ex) {
					Logger.getLogger(MainMessageSettings.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}

		});
		fontListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				String selectedFamily = (String) fontListView
						.getSelectionModel().getSelectedItem();
				// Apply the selected font family
				Font selectedFont = Font.font(selectedFamily, 20.0);
				fontLabel.setText(selectedFamily);
				fontLabel.setFont(selectedFont);
				fontLabel.setStyle("-fx-font-family: " + selectedFamily + ";");
			}
		});
		setStrings();
	}
	Stage aboutStage=new Stage();
	// private void changeBackgroundOnHoverUsingBinding(Node node) {
	// node.styleProperty().bind(
	// Bindings
	// .when(node.hoverProperty())
	// .then(
	// new SimpleStringProperty(HOVERED_BUTTON_STYLE)
	// )
	// .otherwise(
	// new SimpleStringProperty(STANDARD_BUTTON_STYLE)
	// )
	// );
	// }
	void initData(MessageControlParameters controlParameters,
			SplitPane splitPane) {
		this.controlParameters = controlParameters;
		this.splitPane = splitPane;
	}

	
	private void setSettingsData() {
		familiesList = Font.getFamilies();
		familiesObservableList = FXCollections
				.observableArrayList(familiesList);
		fontListView.setItems(familiesObservableList);

		mainSettingsEnableCheckBox.setSelected(false);
		mainSettingsHibernateComboBox.setDisable(true);

		mainSettingsAckCheckBox.setSelected(false);
		mianSettingsRestrictedCheckBox.setSelected(false);
		mainSettingsExpireCheckbox.setSelected(false);
		mainSettingsExpireTextField.setText("");

		mainSettingsLanguageComboBox.getSelectionModel().select(0);
		mainSettingsHibernateComboBox.getSelectionModel().select(0);
		mainSettingsAutoResponseCheckBox.setSelected(false);
		mainSettingsAutoResponseTextField.setText("");
		mainSettingsPlaySoundCheckBox.setSelected(false);

		SettingsModel model = getDataFromDB();
		if (model != null) {
			if (model.getLanguage() != null
					&& !model.getLanguage().trim().equals("")) {
				ObservableList<String> data = mainSettingsLanguageComboBox
						.getItems();
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).equalsIgnoreCase(model.getLanguage())) {
						mainSettingsLanguageComboBox.getSelectionModel()
								.select(i);
						break;
					}
				}
			}

			if (model.getRememberStatus() < 1) {
				mainSettingsEnableCheckBox.setSelected(true);
				mainSettingsHibernateComboBox.setDisable(false);
				if (model.getHibernationTime() > 0) {
					ObservableList<String> data = mainSettingsHibernateComboBox
							.getItems();
					if (data.contains(model.getHibernationTime() + "")) {
						mainSettingsHibernateComboBox.getSelectionModel()
								.select(model.getHibernationTime() + "");
					}
				}
			}

			if (model.getAutoResponseStatus() > 0) {
				mainSettingsAutoResponseTextField.setText(model
						.getAutoResponseMessage());
				mainSettingsAutoResponseTextField.setDisable(false);
				mainSettingsAutoResponseCheckBox.setSelected(true);
			}

			if (model.getPlaySound() > 0) {
				mainSettingsPlaySoundCheckBox.setSelected(true);
			}

			if (model.getPriority() == 3) {
				mainSettingsPriorityComboBox.getSelectionModel().select(3);
			} else {
				mainSettingsPriorityComboBox.getSelectionModel().select(
						model.getPriority());
			}
			if (model.getAcknowledge() > -1) {
				mainSettingsAckCheckBox.setSelected(true);
			}
			if (model.getRestricted() > -1) {
				mianSettingsRestrictedCheckBox.setSelected(true);
			}
			if (model.getExpiryStatus() > -1) {
				mainSettingsExpireCheckbox.setSelected(true);
				mainSettingsExpireTextField.setDisable(false);
				mainSettingsExpireTextField.setText(model.getExpiryMinuts()
						+ "");
			}

			String selectedFamily = model.getFontFamily();
			if (selectedFamily != null && !selectedFamily.trim().equals("")) {
				int index = familiesObservableList.indexOf(selectedFamily);
				if (index >= 0) {
					if(fontListView.getOnMouseMoved() != null)
					{
						CashewnutActivity.idleTime = System.currentTimeMillis();
					}
					fontListView.getSelectionModel().select(index);
					fontListView.scrollTo(index);
					Font selectedFont = Font.font(selectedFamily, 20.0);
					fontLabel.setText(selectedFamily);
					fontLabel.setFont(selectedFont);
					fontLabel.setStyle("-fx-font-family: " + selectedFamily
							+ ";");
				}
			}
		}
	}

	private void setStrings() {
		if (aboutButton != null)
			aboutButton
					.setText(AppConfiguration.appConfString.conversation_about);

		if (reportsButton != null)
			reportsButton
					.setText(AppConfiguration.appConfString.conversation_reports1);

		if (accountLabel != null)
			accountLabel
					.setText(AppConfiguration.appConfString.settings_account);

		if (changePassword != null)
			changePassword
					.setText(AppConfiguration.appConfString.settings_change_password);

		if (secureandprivacyLabel != null)
			secureandprivacyLabel
					.setText(AppConfiguration.appConfString.settings_settings_privacy);

		if (priorityIndicatorLabel != null)
			priorityIndicatorLabel
					.setText(AppConfiguration.appConfString.dialog_priority_indicator);

		if (mainSettingsExpireCheckbox != null)
			mainSettingsExpireCheckbox
					.setText(AppConfiguration.appConfString.dialog_expiry_in);

		if (mainSettingsExpireTextField != null)
			mainSettingsExpireTextField
					.setPromptText(AppConfiguration.appConfString.dialog_mins);

		if (mianSettingsRestrictedCheckBox != null)
			mianSettingsRestrictedCheckBox
					.setText(AppConfiguration.appConfString.forward_backup_restricted);

		if (mainSettingsAckCheckBox != null)
			mainSettingsAckCheckBox
					.setText(AppConfiguration.appConfString.dialog_read_ack);

		if (notificationSettingsLabel != null)
			notificationSettingsLabel
					.setText(AppConfiguration.appConfString.settings_notification_settings);

		if (mainSettingsPlaySoundCheckBox != null)
			mainSettingsPlaySoundCheckBox
					.setText(AppConfiguration.appConfString.settings_play_sound);

		if (additionalSettingsLabel != null)
			additionalSettingsLabel
					.setText(AppConfiguration.appConfString.settings_additional_settings);

		if (mainSettingsEnableCheckBox != null)
			mainSettingsEnableCheckBox
					.setText(AppConfiguration.appConfString.settings_enable_login_screen);

		if (hibernateLabel != null)
			hibernateLabel
					.setText(AppConfiguration.appConfString.settings_hibernate);

		if (languageLabel != null)
			languageLabel
					.setText(AppConfiguration.appConfString.settings_language);

		if (fontLabel != null)
			fontLabel.setText(AppConfiguration.appConfString.font);

		if (mainSettingsAutoResponseCheckBox != null)
			mainSettingsAutoResponseCheckBox
					.setText(AppConfiguration.appConfString.settings_auto_response);

		if (mainSettingsAutoResponseTextField != null)
			mainSettingsAutoResponseTextField
					.setPromptText(AppConfiguration.appConfString.settings_auto_response_message);

		if (titleLabel != null)
			titleLabel
					.setText(AppConfiguration.appConfString.compose_message_settings);
	}

	public void saveSettings() {
		int priority_status = 3;
		int expiry_status = -1;
		int ack_status = -1;
		int autores_status = -1;
		int restricted_status = -1;
		int playsound_status = -1;
		int expiry_mins = -1;
		String autores_message = "";
		int hibernation_status = -1;
		int login_status = -1;
		String language = "";

		priority_status = mainSettingsPriorityComboBox.getSelectionModel()
				.getSelectedIndex();

		if (mianSettingsRestrictedCheckBox.isSelected()) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			restricted_status = MessageControlParameters.RESTRICTED;
		} else {
			restricted_status = -1;
		}

		if (mainSettingsExpireCheckbox.isSelected()
				&& !mainSettingsExpireTextField.getText().trim().equals("")) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			expiry_mins = Integer.parseInt(mainSettingsExpireTextField
					.getText());
			expiry_status = 1;
		} else {
			mainSettingsExpireCheckbox.setSelected(false);
			expiry_status = -1;
			expiry_mins = -1;
		}

		if (mainSettingsAckCheckBox.isSelected()) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			ack_status = 1;
		} else {
			ack_status = -1;
		}

		language = mainSettingsLanguageComboBox.getSelectionModel()
				.getSelectedItem().toString();

		if (!mainSettingsEnableCheckBox.isSelected()) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			login_status = 1;
		}

		if (mainSettingsEnableCheckBox.isSelected()
				&& mainSettingsHibernateComboBox.getSelectionModel()
						.getSelectedIndex() > 0) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			hibernation_status = mainSettingsHibernateComboBox
					.getSelectionModel().getSelectedIndex();
		}

		if (mainSettingsAutoResponseCheckBox.isSelected()) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			autores_message = mainSettingsAutoResponseTextField.getText();
			if (!autores_message.trim().equals("")) {
				autores_status = 1;
			} else {
				mainSettingsAutoResponseCheckBox.setSelected(false);
				autores_status = -1;
				autores_message = "";
			}
		}

		String selectedFamily = (String) fontListView.getSelectionModel()
				.getSelectedItem();
		splitPane.setStyle("-fx-font-family: " + selectedFamily + ";");

		if (mainSettingsPlaySoundCheckBox.isSelected()) {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			playsound_status = 1;
		}

		SettingsModel settingsModel = new SettingsModel();

		settingsModel.setPriority(priority_status);
		settingsModel.setExpiryStatus(expiry_status);
		settingsModel.setExpiryMinuts(expiry_mins);
		settingsModel.setAcknowledge(ack_status);
		settingsModel.setRestricted(restricted_status);

		settingsModel.setAutoResponseStatus(autores_status);
		settingsModel.setAutoResponseMessage(autores_message);

		settingsModel.setLanguage(language);
		settingsModel.setHibernationTime(hibernation_status);
		settingsModel.setRememberStatus(login_status);
		settingsModel.setPlaySound(playsound_status);
		settingsModel.setSignature("");
		settingsModel.setFontFamily(selectedFamily);

		// SettingsModel oldSettingsmodel = getDataFromDB();
		SettingsStore settingsMapper = new SettingsStore();
		settingsMapper.saveSettingsData(settingsModel);

		if (priority_status !=3) {
			controlParameters.setPriority(priority_status + "");
		} else {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			controlParameters
					.setPriority(MessageControlParameters.PRIORITY_INDICATOR_GRAY
							+ "");
		}
		controlParameters.setExpiry(expiry_mins + "");
		controlParameters.setRestricted(restricted_status + "");
		controlParameters.setMessageAck(ack_status + "");
		
		
		AppConfiguration.onLocaleChange(language);
		CashewnutActivity.idleTime = System.currentTimeMillis();
		ControlMessageOptions.getInstance().setHibernationTime();
		ControlMessageOptions.getInstance().startIdleTimer();
	}
	private SettingsModel getDataFromDB() {
		SettingsModel model = new SettingsStore().getSettingsData();
		if (model == null) {
			model = new SettingsModel();
			int priority_status = -1;
			int expiry_status = -1;
			int ack_status = -1;
			int autores_status = -1;
			int restricted_status = -1;
			int playsound_status = -1;
			int expiry_mins = -1;
			String autores_message = "";
			int hibernation_status = -1;
			int login_status = -1;
			String language = "";

			model.setPriority(priority_status);
			model.setExpiryStatus(expiry_status);
			model.setExpiryMinuts(expiry_mins);
			model.setAcknowledge(ack_status);
			model.setRestricted(restricted_status);

			model.setAutoResponseStatus(autores_status);
			model.setAutoResponseMessage(autores_message);

			model.setLanguage(language);
			model.setHibernationTime(hibernation_status);
			model.setRememberStatus(login_status);
			model.setPlaySound(playsound_status);
			model.setSignature("");
		}
		return model;
	}

	private void setLanguage() {
		
		ObservableList<String> options =AppConfiguration.getLanguage();
		
		mainSettingsLanguageComboBox.setItems(options);
		mainSettingsLanguageComboBox.getSelectionModel().select(0);
	}

	private void setHibernate() {
		ObservableList<String> hibernate = FXCollections.observableArrayList(
				"Never", "1 MInute", "2 Minutes", "5 Minutes", "10 Minutes");
		mainSettingsHibernateComboBox.setItems(hibernate);
		mainSettingsHibernateComboBox.getSelectionModel().select(0);

	}

	private void setPriority() {
		ObservableList<String> options = FXCollections.observableArrayList(
				"Highest", "High", "Medium", "Low");
		mainSettingsPriorityComboBox.setItems(options);
		mainSettingsPriorityComboBox.getSelectionModel().select(0);
	}

	private void setAutoResponse() {
		mainSettingsAutoResponseTextField.setVisible(false);
		mainSettingsAutoResponseCheckBox.selectedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(
							ObservableValue<? extends Boolean> observable,
							Boolean oldValue, Boolean newValue) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						if (mainSettingsAutoResponseCheckBox.isSelected()) {
							mainSettingsAutoResponseTextField.setVisible(true);
						} else {
							mainSettingsAutoResponseTextField.setVisible(false);
						}
					}
				});
	}
private void setStage(ActionEvent e,int i) throws IOException
{
	Stage stage = new Stage();
	Parent root=null;
    switch (i) {
    case 1:

    	root = FXMLLoader.load(MainMessageSettings.class
    			.getResource("fxml_about.fxml"));
        break;
    case 2:

    	 root = FXMLLoader.load(MainMessageSettings.class
    			.getResource("fxml_changePassword.fxml"));
    	 
        break;
    case 3:

    	root = FXMLLoader.load(MainMessageSettings.class
    			.getResource("fxml_reports.fxml"));
        break;
}
	// changePassword();
    stage.setScene(new Scene(root));
	stage.initStyle(StageStyle.UNDECORATED);
	stage.setResizable(false);
	stage.initModality(Modality.WINDOW_MODAL);
	stage.initOwner(((Node) e.getSource()).getScene()
			.getWindow());
	splitPane.setOpacity(0.7);
	aboutStage=stage;
	stage.show();
	splitPane.setOpacity(1.0);
	
}
	private void setExpire() {
		mainSettingsExpireTextField.setVisible(false);
		mainSettingsExpireCheckbox.selectedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(
							ObservableValue<? extends Boolean> observable,
							Boolean oldValue, Boolean newValue) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						if (mainSettingsExpireCheckbox.isSelected()) {
							mainSettingsExpireTextField.setVisible(true);
						} else {
							mainSettingsExpireTextField.setVisible(false);
						}
					}
				});

		mainSettingsExpireTextField.lengthProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						if (newValue.intValue() > oldValue.intValue()) {
							char ch = mainSettingsExpireTextField.getText()
									.charAt(oldValue.intValue());
//							System.out.println("Length:" + oldValue + "  "
//									+ newValue + " " + ch);
							if (!(ch >= '0' && ch <= '9')) {
								mainSettingsExpireTextField
										.setText(mainSettingsExpireTextField
												.getText().substring(
														0,
														mainSettingsExpireTextField
																.getText()
																.length() - 1));
							}

							if (newValue.intValue() > maxLength) {
								mainSettingsExpireTextField
										.setText(mainSettingsExpireTextField
												.getText().substring(0,
														maxLength));
							}
						}
					}
				});
	}
	public void closing()
	{
		if(CashewnutActivity.close)
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
				
				
					
					aboutStage.close();
					
					
				}
			});
			
		}
	}
}
