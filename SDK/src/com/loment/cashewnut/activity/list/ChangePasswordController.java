/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.activity.controller.ChangePasswordTask;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.loment.cashewnut.activity.controller.NetworkConnection;
import com.loment.cashewnut.connection.amqp.RPCClientSender;
import com.loment.cashewnut.receiver.ReceiverHandler;

import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ajay
 */
public class ChangePasswordController extends Stage implements Initializable {
	@FXML
	BorderPane borderPane;

	@FXML
	ToolBar toolBar;

	@FXML
	private Button changePasswordOkButton;
	@FXML
	private Button changePasswordCancelButton;
	@FXML
	private TextField oldPasswordTextField;
	@FXML
	private TextField newPasswordTextField;
	@FXML
	private TextField confirmPasswordTextField;
	@FXML
	private Button closeButton;
	@FXML
	private ImageView closeButtonImage;
	@FXML
	private Label errorLabel;
	@FXML
	private Label oldPasswordLabel;
	@FXML
	private Label newPasswordLabel;
	@FXML
	private Label confirmPasswordLabel;
	@FXML
	private Label titleLabel;
	@FXML
	ProgressIndicator changePasswordProgress;
	boolean isMousePressed = false;
	private double xOffset = 0;
	private double yOffset = 0;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
				+ AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

		closeButtonImage.setImage(new Image(About.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "close.png")));
		changePasswordProgress
		.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		changePasswordProgress.setVisible(false);
		changePasswordProgress.setStyle("-fx-accent:" + AppConfiguration.getProgressColor());

		double r = 10.5;
		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);

		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						Stage stage = (Stage) oldPasswordTextField.getScene()
								.getWindow();
						stage.close();
					}
				});
			}
		});
		toolBar.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						xOffset = event.getSceneX();
						yOffset = event.getSceneY();
						isMousePressed = true;

					}
				});
		toolBar.addEventFilter(MouseEvent.MOUSE_RELEASED,
				new EventHandler<MouseEvent>() {
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
		newPasswordTextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// this will run whenever text is changed
				
			}
		});
		
		if((oldPasswordTextField.getOnKeyTyped() != null)||oldPasswordTextField.getOnAction()!= null)
		{
			CashewnutActivity.idleTime = System.currentTimeMillis();
		}
		
		setStrings();
	}

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof Button) {
			Button clickedBtn = (Button) source;
			String id = clickedBtn.getId();
			if (id != null) {
				
				errorLabel.setVisible(false);
				if (changePasswordOkButton != null
						&& id.equalsIgnoreCase(changePasswordOkButton.getId())) {
					changePassword();
					return;
				}
				if (changePasswordCancelButton != null
						&& id.equalsIgnoreCase(changePasswordCancelButton
								.getId())) {
					Stage stage = (Stage) changePasswordOkButton.getScene()
							.getWindow();
					stage.close();
				}
			}
		}
	}

	
	private boolean validateNewPassword(String oldpass, String newpass,
			String repeatpass) {
		String password = DBLomentDataMapper.getInstance().getLomentData()
				.getPassword();

		if (oldpass.trim().equals("") || newpass.trim().equals("")) {
			labelVisible(errorLabel,
					AppConfiguration.appConfString.error_fields_required);
			return false;
		} else if (!password.equals(oldpass)) {
			labelVisible(errorLabel,
					AppConfiguration.appConfString.edit_old_password_not_match);
			return false;
		} else if (newpass.length() < 4 || !newpass.equals(repeatpass)) {
			if (newpass.length() < 4) {
				labelVisible(
						errorLabel,
						AppConfiguration.appConfString.edit_password_lessthan_4chars);
				return false;
			} else if (!newpass.equals(repeatpass)) {
				labelVisible(errorLabel,
						AppConfiguration.appConfString.edit_new_repeat_passwords_not_same);
				return false;
			}
		}
		return true;
	}

	
	
	
	private void setStrings() {
		if (oldPasswordLabel != null)
			oldPasswordLabel
					.setText(AppConfiguration.appConfString.edit_old_password);

		if (oldPasswordTextField != null)
			oldPasswordTextField
					.setPromptText(AppConfiguration.appConfString.edit_old_password);

		if (newPasswordLabel != null)
			newPasswordLabel
					.setText(AppConfiguration.appConfString.edit_new_password);

		if (newPasswordTextField != null)
			newPasswordTextField
					.setPromptText(AppConfiguration.appConfString.edit_new_password);

		if (confirmPasswordLabel != null)
			confirmPasswordLabel
					.setText(AppConfiguration.appConfString.edit_confirm_password);

		if (confirmPasswordTextField != null)
			confirmPasswordTextField
					.setPromptText(AppConfiguration.appConfString.edit_confirm_password);

		if (changePasswordOkButton != null)
			changePasswordOkButton
					.setText(AppConfiguration.appConfString.dlg_ok);

		if (changePasswordCancelButton != null)
			changePasswordCancelButton
					.setText(AppConfiguration.appConfString.dlg_cancel);

		if (titleLabel != null) {
			titleLabel.setText(" "
					+ AppConfiguration.appConfString.change_password);
			titleLabel.setFont(AppConfiguration.setFont());
		}
	}
	public void listener()
	{
		newPasswordTextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// this will run whenever text is changed
				
			}
		});
		
		oldPasswordTextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// this will run whenever text is changed
				
			}
		});
		confirmPasswordTextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				// this will run whenever text is changed
				
			}
		});
	}
	
	
	public void labelVisible(Label label, String text) {
		label.getStyleClass().add("error"); // red colour
		label.setVisible(true);
		label.setText("*" + text);
		String cssDefault = "-fx-text-fill:#f50e0e;";
		label.setStyle(cssDefault);
	}

	private String changePassword() {
		final String oldpass = oldPasswordTextField.getText();
		final String newpass = newPasswordTextField.getText();
		final String repeatpass = confirmPasswordTextField.getText();
		final String primaryEmail = DBLomentDataMapper.getInstance()
				.getLomentData().getEmail();

		if (validateNewPassword(oldpass, newpass, repeatpass)) {
			changePasswordProgress.setVisible(true);
			NetworkConnection connection = new NetworkConnection();
			connection.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					Boolean isConnected = (Boolean) t.getSource().getValue();
					if (isConnected) {
						new Thread() {
							public void run() {
								final ChangePasswordTask changepassword = new ChangePasswordTask(
										newpass, primaryEmail);
								final String res = changepassword.execute();
								if (res == null || res.trim().equals("")) {
									return;
								}
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										if (changepassword.onPostExecute(res)) {
											Stage stage = (Stage) changePasswordOkButton
													.getScene().getWindow();
											stage.close();

											RPCClientSender.getInstance()
													.close();
											ReceiverHandler.getInstance()
													.syncMessages();

											Stage dialogStage = getDialog(AppConfiguration.appConfString.edit_password_changed_successfully);
											dialogStage.show();
											changePasswordProgress.setVisible(false);
										} else {
											labelVisible(
													errorLabel,
													AppConfiguration.appConfString.sender_network_unavailable);
											changePasswordProgress.setVisible(false);
										}
									}
								});
							}
						}.start();
					} else {
						labelVisible(
								errorLabel,
								AppConfiguration.appConfString.sender_network_unavailable);
						changePasswordProgress.setVisible(false);
					}
				}
			});
			connection.start();
		}
		return null;
	}

	
	
	private Stage getDialog(String body) {
		CashewnutActivity.idleTime = System.currentTimeMillis();
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
