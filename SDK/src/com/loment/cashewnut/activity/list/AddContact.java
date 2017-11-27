/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.activity.controller.UserDetailsForm;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.LomentDataModel;

/**
 *
 * @author sekhar
 */
public class AddContact extends Stage implements Initializable {

	@FXML
	BorderPane borderPane;

	@FXML
	ToolBar toolBar;

	@FXML
	AnchorPane contactsAnchorPane;
	@FXML
	AnchorPane groupsAnchorPane;
	@FXML
	private Button closeButton;
	@FXML
	private ImageView closeImageView;
	@FXML
	AnchorPane contactAnchorPane;
	@FXML
	TextField cashewidTextfield;
	@FXML
	Button createButton;
	@FXML
	Label cashewidLabel;
	@FXML
	private Label addCashewIDLabel;
	@FXML
	private Label cashewIDLabel;
	String cashewid;
	@FXML
	ProgressIndicator addContactProgress;
	

	public AddContact() {
		super();
	}

	public AddContact(Stage owner, double x, double y) {
		super();
		try {
			CashewnutActivity.idleTime = System.currentTimeMillis();
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_addcontact.fxml"));
			fl.load();
			Parent root = fl.getRoot();

			Scene scene1 = new Scene(root);
			scene1.getStylesheets().add(
					ForwardMessage.class.getResource("Style.css")
							.toExternalForm());
			this.setScene(scene1);
			this.initStyle(StageStyle.UNDECORATED);
			this.initModality(Modality.WINDOW_MODAL);
			this.centerOnScreen();
			this.setResizable(false);
		} catch (Exception ex) {
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		addContactProgress
		.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		addContactProgress.setVisible(false);
		addContactProgress.setStyle("-fx-accent:" + AppConfiguration.getProgressColor());
		double r = 10.0;

		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
				+ AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);
		closeImageView.setImage(new Image(AddContact.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "close.png")));
		closeImageView.getStyleClass().add("closeButtonCss");
		
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				close1();
			}
		});

		cashewidTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// this will run whenever text is changed
			
			}
		});
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				cashewidLabel.setVisible(false);
				cashewidLabel.setText("");
				addContactProgress.setVisible(true);
				cashewid = cashewidTextfield.getText().trim();
				if (cashewid == null || cashewid.trim().equals("")) {
					cashewidLabel.setVisible(true);
					addContactProgress.setVisible(false);
					cashewidLabel
							.setText("*"
									+ AppConfiguration.appConfString.message_compose_to_error);
				} else {
					if (validate(cashewid)) {
						DBContactsMapper profileMapper = DBContactsMapper
								.getInstance();
						ContactsModel contactsModel = profileMapper
								.getContact(cashewid,0);
						String recpentName = contactsModel.getFirstName();
						if (recpentName != null
								&& !recpentName.trim().equals("")) {
							//System.out.println("Contact Successfully Updated  "
							//		+ cashewid);
							addContactProgress.setVisible(false);
							close1();
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
							service.setCashewId(cashewid);
							service.setStatus(0);
							service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
								@Override
								public void handle(WorkerStateEvent t) {
									boolean response = (boolean) t.getSource()
											.getValue();
									if (response) {
										
										close1();
									} else {
										cashewidLabel.setVisible(true);
										addContactProgress.setVisible(false);
										cashewidLabel
												.setText("*"
														+ AppConfiguration.appConfString.message_compose_not_exist);
									}
								}
							});
							service.start();
						}
					} else {
						// enter valid Cashew id
						cashewidLabel.setVisible(true);
						addContactProgress.setVisible(false);
						cashewidLabel
								.setText("*"
										+ AppConfiguration.appConfString.valid_cashewnut_id1);
					}
				}
			}
		});
		setStrings();
		
	}
	public AnchorPane closing()
	{
		return contactAnchorPane;
	}
	private void setStrings() {
		if (addCashewIDLabel != null)
			addCashewIDLabel.setText("  "
					+ AppConfiguration.appConfString.add_contact);
		if (createButton != null)
			createButton.setText(AppConfiguration.appConfString.dlg_ok);
		if (cashewidTextfield != null)
			cashewidTextfield
					.setPromptText(AppConfiguration.appConfString.message_compose_to_hint);
		if (cashewIDLabel != null)
			cashewIDLabel
					.setText(AppConfiguration.appConfString.profile_app_name1);
		if (cashewidLabel != null)
			cashewidLabel.setText("* "
					+ AppConfiguration.appConfString.message_compose_to_error);
	}

	public void close1() {
		CashewnutActivity.idleTime = System.currentTimeMillis();
		Stage stage = (Stage) contactAnchorPane.getScene().getWindow();
		stage.close();
	}

	private static final String CASHEWNUTID_PATTERN = "^([A-Z]|[a-z]|[0-9])+(\\.|_)?([A-Z]|[a-z]|[0-9])+$";

	public boolean validate(final String cashewnutID) {
		boolean ismaches = false;
		if (cashewnutID != null && !cashewnutID.trim().equals("")
				&& cashewnutID.length() >2) {
			Pattern pattern = Pattern.compile(CASHEWNUTID_PATTERN);
			Matcher matcher = pattern.matcher(cashewnutID);
			ismaches = matcher.matches();
		}

		return ismaches;
	}
}
