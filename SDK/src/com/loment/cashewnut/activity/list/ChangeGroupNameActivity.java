/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.GroupModel;
import com.loment.cashewnut.receiver.Receiver;
import com.loment.cashewnut.sender.SenderHandler;

/**
 *
 * @author sekhar
 */
public class ChangeGroupNameActivity extends Stage implements Initializable {

	@FXML
	BorderPane borderPane;

	@FXML
	ToolBar toolBar;

	@FXML
	private Button closeButton;
	@FXML
	private ImageView closeImageView;
	@FXML
	AnchorPane changeGroupNameAnchorPane;
	@FXML
	TextField groupNameTextfield;
	@FXML
	Button createButton;
	@FXML
	Label errorGroupNameLabel;
	@FXML
	Label changeGroupNameLabel;
	@FXML
	Label groupNameLabel;
	static String groupName;
	static boolean isChangedGroupName = false;
	private String groupId;
	boolean isChange;
	private String cashewnutId = "";
	public ChangeGroupNameActivity() {
		super();
	}

	public ChangeGroupNameActivity(String groupId, Stage owner, double x,
			double y) {
		super();
		try {
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_changeGroupName.fxml"));
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

			ChangeGroupNameActivity controller = fl
					.<ChangeGroupNameActivity> getController();
			controller.initData(groupId);

		} catch (Exception ex) {
		}
	}

	void initData(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		groupName = "";
		isChangedGroupName = false;

		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
				+ AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

		double r = 10.0;
		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);
		closeImageView.setImage(new Image(ChangeGroupNameActivity.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "close.png")));
		closeImageView.getStyleClass().add("closeButtonCss");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				close1();
			}
		});

		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				errorGroupNameLabel.setVisible(false);
				errorGroupNameLabel.setText("");

				groupName = groupNameTextfield.getText().trim();
				if (groupName == null || groupName.trim().equals("")) {
					errorGroupNameLabel.setVisible(true);
					errorGroupNameLabel
							.setText("*"
									+ AppConfiguration.appConfString.group_name_error_hint);
				} else {
					final GroupModel storedGModel = DBGroupsMapper
							.getInstance().getGroup(groupId, true);
					try {
						AccountsModel accountsModel = DBAccountsMapper.getInstance()
								.getAccount();
						cashewnutId = accountsModel.getCashewnutId();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (storedGModel != null && storedGModel.getGroupId() > -1) {
						final String oldGroupName = storedGModel.getGroupName();

						final GroupModel gModel = new GroupModel();
						gModel.setGroupId(storedGModel.getGroupId());
						gModel.setServerGroupId(groupId);
						gModel.setGroupName(groupName);
						gModel.setFrom(cashewnutId);
						gModel.setOperation(GroupModel.OPERATION_CHANGE_NAME);

						new Thread() {
							public void run() {
								try {
									SenderHandler.getInstance().sendGroup(
											gModel);

									int loopCount = 0;
									do {
										Thread.sleep(2000);
										GroupModel gModel = DBGroupsMapper
												.getInstance().getGroup(
														storedGModel
																.getGroupId()
																+ "", false);
										if (gModel.getServerGroupId() != null
												&& !gModel.getServerGroupId()
														.equals("")) {
											isChangedGroupName = !gModel
													.getGroupName().equals(
															oldGroupName);
											if (isChangedGroupName) {
												System.out
														.println("Group Name Successfully Updated");
												break;
											}
										}
									} while (!isChangedGroupName
											&& loopCount++ < 5);

									if (isChangedGroupName) {
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								close1();
							}
						}.start();
					}
				}
			}
		});
		setStrings();
	}

	private void setStrings() {
		if (changeGroupNameLabel != null)
			changeGroupNameLabel
					.setText(AppConfiguration.appConfString.change_group_name_hint);
		if (createButton != null)
			createButton.setText(AppConfiguration.appConfString.dlg_ok);
		if (groupNameTextfield != null)
			groupNameTextfield
					.setPromptText(AppConfiguration.appConfString.group_name_hint);
		if (groupNameLabel != null)
			groupNameLabel.setText(AppConfiguration.appConfString.group_name);
		if (errorGroupNameLabel != null)
			errorGroupNameLabel.setText("*"
					+ AppConfiguration.appConfString.group_name_error_hint);
	}

	public boolean isChangedGroupName() {
		return isChangedGroupName;
	}

	public String getChangedGroupName() {
		return groupName;
	}

	public void close1() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage stage = (Stage) changeGroupNameAnchorPane.getScene()
						.getWindow();
				stage.close();
			}
		});
	}
}
