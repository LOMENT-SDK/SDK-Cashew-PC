/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.net.URL;
import java.util.ResourceBundle;

import com.loment.cashewnut.AppConfiguration;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ajay
 */
public class About implements Initializable {

	@FXML
	BorderPane borderPane;

	@FXML
	ToolBar toolBar;
	@FXML
	private Label versionLabel;
	/*@FXML
	private Label buildTImeLabel;*/
	@FXML
	private Label mailidLabel;
	@FXML
	private Label versionLabel1;
	/*@FXML
	private Label buildTImeLabel1;*/
	@FXML
	private Label mailidLabel1;
	@FXML
	private Label emailIDLabel;
	@FXML
	private Label titleLabel;
	@FXML
	private Label logoLabel;
	@FXML
	private Label lomentLogoLabel;

	@FXML
	private Button closeButton;
	@FXML
	private ImageView closeButtonImage;
	boolean isMousePressed = false;
	private double xOffset = 0;
	private double yOffset = 0;
	@FXML
	private ImageView cashewLogo;

	@FXML
	private ImageView lomentLogo;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ConversationsViewRenderer renderer = new ConversationsViewRenderer();
		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
				+ AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

		// TODO
		versionLabel.setText("2.1.1 (41)");
		//buildTImeLabel.setText("18-AUG-2016 01:50 PM");
		mailidLabel.setText(AppConfiguration.appConfString.support_loment_net);

		closeButtonImage.setImage(new Image(About.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "close.png")));
		if (cashewLogo != null) {
			cashewLogo.setImage(new Image(About.class
					.getResourceAsStream(AppConfiguration.getAppLogoPath()
							+ "cashew.png")));
		}
		if (lomentLogo != null) {
			lomentLogo.setImage(new Image(About.class
					.getResourceAsStream(AppConfiguration.getAppLogoPath()
							+ "loment.png")));
		}

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
						ConversationsViewRenderer renderer = new ConversationsViewRenderer();
						Stage stage = (Stage) emailIDLabel.getScene()
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
						ConversationsViewRenderer renderer = new ConversationsViewRenderer();
						xOffset = event.getSceneX();
						yOffset = event.getSceneY();
						isMousePressed = true;

					}
				});
		toolBar.addEventFilter(MouseEvent.MOUSE_RELEASED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						ConversationsViewRenderer renderer = new ConversationsViewRenderer();
						isMousePressed = false;
					}
				});

		toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ConversationsViewRenderer renderer = new ConversationsViewRenderer();
				if (isMousePressed) {
					Stage stage = (Stage) toolBar.getScene().getWindow();
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			}
		});
		setStrings();
	}

	private void setStrings() {
		if (versionLabel1 != null)
			versionLabel1.setText(AppConfiguration.appConfString.about_version);

	/*	if (buildTImeLabel1 != null)
			buildTImeLabel1
					.setText(AppConfiguration.appConfString.about_build_time);*/

		if (mailidLabel1 != null)
			mailidLabel1.setText(AppConfiguration.appConfString.about_email);

		if (mailidLabel != null)
			mailidLabel
					.setText(AppConfiguration.appConfString.support_loment_net);

		if (emailIDLabel != null)
			emailIDLabel
					.setText(AppConfiguration.appConfString.website_loment_net);
		if (lomentLogoLabel != null)
			lomentLogoLabel
					.setText(AppConfiguration.appConfString.website_loment_name);

		if (logoLabel != null) {
			logoLabel.setText(AppConfiguration.appConfString.about_comment);
			logoLabel.setFont(AppConfiguration.setFont());

		}

		if (titleLabel != null) {
			titleLabel.setText(" "
					+ AppConfiguration.appConfString.conversation_about);
			titleLabel.setFont(AppConfiguration.setFont());
		}
	}

}
