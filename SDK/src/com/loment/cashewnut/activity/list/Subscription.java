/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.Toast;
import com.loment.cashewnut.activity.controller.NetworkConnection;
import com.loment.cashewnut.sthithi.connection.SubscriptionHandler;

import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author ajay
 */
public class Subscription implements Initializable {

	@FXML
	private Label labelText;
	@FXML
	private Label labelText1;
	@FXML
	private Button closeButton;
	@FXML
	private Button SubscribeButton;
	@FXML
	private Hyperlink clickHereButton;
	@FXML
	private TextField subscribeText;
	@FXML
	private ImageView closeButtonImage;
	@FXML
	private Label titleLabel;

	boolean isMousePressed = false;
	private double xOffset = 0;
	private double yOffset = 0;
	@FXML
	private ToolBar toolBar;
	@FXML
	private Label subscribeErrorLabel;
	@FXML
	ProgressIndicator addSubscribeProgress;
	private SplitPane parentSplitPane;

	@FXML
	private BorderPane borderPane;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
				+ AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

		closeButtonImage.setImage(new Image(Subscription.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "close.png")));
		addSubscribeProgress
				.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		addSubscribeProgress.setVisible(false);
		addSubscribeProgress.setStyle("-fx-accent:"
				+ AppConfiguration.getProgressColor());

		double r = 10.5;
		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);

		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						Stage stage = (Stage) closeButton.getScene()
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
						xOffset = event.getSceneX();
						yOffset = event.getSceneY();
						isMousePressed = true;

					}
				});
		toolBar.addEventFilter(MouseEvent.MOUSE_RELEASED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						isMousePressed = false;
					}
				});

		toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isMousePressed) {
					Stage stage = (Stage) toolBar.getScene().getWindow();
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			}
		});

		clickHereButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				open(AppConfiguration.getPamentApi());
			}
		});

		SubscribeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				activateAccountTask();
			}
		});

		setStrings();
	}

	private void setStrings() {

		if (labelText != null) {
			labelText
					.setText(AppConfiguration.appConfString.activate_subscription);
			labelText.setWrapText(true);
		}

		if (labelText1 != null) {
			labelText1
					.setText(AppConfiguration.appConfString.activate_no_activation_key);
			labelText1.setWrapText(true);
		}

		if (clickHereButton != null) {
			clickHereButton
					.setText(AppConfiguration.appConfString.activate_click_here);
			clickHereButton.setWrapText(true);
		}

		if (SubscribeButton != null)
			SubscribeButton
					.setText(AppConfiguration.appConfString.activate_subscribe);

		if (subscribeText != null)
			subscribeText
					.setPromptText(AppConfiguration.appConfString.activate_activation_key);

		if (titleLabel != null) {
			titleLabel.setText(" "
					+ AppConfiguration.appConfString.activate_subscription1);
			titleLabel.setFont(AppConfiguration.setFont());
		}

	}

	public void labelVisible(Label label, String text) {
		label.setWrapText(true);
		label.getStyleClass().add("error"); // red colour
		label.getStyleClass().add("visible");
		if (text != null && !text.trim().equals("")) {
			text = "*" + text;
		}
		label.setText(text);
	}

	private void activateAccountTask() {
		try {

			labelVisible(subscribeErrorLabel, "");
			String activationKey = subscribeText.getText();
			if (!validateData(activationKey)) {
				return;
			}
			addSubscribeProgress.setVisible(true);
			NetworkConnection connection = new NetworkConnection();
			connection.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent t) {
							Boolean isConnected = (Boolean) t.getSource()
									.getValue();
							if (isConnected) {

								new Thread() {
									public void run() {
										JSONObject responseJson = SubscriptionHandler
												.getInstance().activateAccount(
														activationKey);
										// System.out.println("subs res: "
										// + responseJson);

										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												String status[] = null;
												if (responseJson != null
														&& !responseJson
																.toString()
																.trim()
																.equals(""))
													status = SubscriptionHandler
															.getInstance()
															.subscriptionStatus(
																	responseJson,
																	true);

												if (status != null
														&& status.length == 2) {
													if (status[0]
															.equals("success")) {
														close1();
														displayToast(
																AppConfiguration.appConfString.activate_cashewnut_successfully,
																3000);
													} else {
														labelVisible(
																subscribeErrorLabel,
																status[1]);
													}
												}
												addSubscribeProgress
														.setVisible(false);
											}
										});

									}
								}.start();
							} else {
								addSubscribeProgress.setVisible(false);
								labelVisible(
										subscribeErrorLabel,
										AppConfiguration.appConfString.sender_network_unavailable);
							}
						}
					});
			connection.start();
		} catch (Exception e) {
			addSubscribeProgress.setVisible(false);
		}
	}

	private Toast displayToast(String val, long time) {
		try {
			Stage stage = (Stage) parentSplitPane.getScene().getWindow();
			Toast tost = Toast.makeText(val, Duration.millis(time));
			tost.show(stage);
			return tost;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close1() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	protected boolean validateData(String activationK_key) {
		StringBuffer error = new StringBuffer();
		if (activationK_key.trim().length() < 1) {
//			error.append("Please enter Activation Key ");
			labelVisible(subscribeErrorLabel, AppConfiguration.appConfString.subscription_enter_activationkey + "!!");
			return false;
		}

		return true;
	}

	public void open(String url) {
		try {
			new Thread() {
				public void run() {

					try {
						URI u = new URI(url);
						java.awt.Desktop.getDesktop().browse(u);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initData(SplitPane splitPane) {
		this.parentSplitPane = splitPane;
	}
}
