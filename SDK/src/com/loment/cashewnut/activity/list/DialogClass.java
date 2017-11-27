package com.loment.cashewnut.activity.list;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.model.GroupModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogClass extends Stage implements Initializable {

	@FXML
	AnchorPane dialogAnchorPane;
	@FXML
	HBox toolBar;
	@FXML
	Label titleDialogLabel;
	@FXML
	Label confirmLabel;
	@FXML
	Label confirmLabel1;
	@FXML
	Button okButton;
	@FXML
	Button cancelButton;
	GroupModel groupModel;
	boolean isChecked;
	String adapterGroupId = null;
	GroupActivity gActivity = null;

	public DialogClass() {
		super();
	}

	public DialogClass(Stage owner, double x, double y, boolean value, String groupId,GroupActivity gActivity) {
		super();
		try {
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_dialog.fxml"));
			fl.load();
			Parent root = fl.getRoot();
			Scene scene1 = new Scene(root);
			this.setScene(scene1);
			this.initStyle(StageStyle.UNDECORATED);
			this.initModality(Modality.WINDOW_MODAL);
			this.centerOnScreen();
			this.setResizable(false);
			DialogClass controller = fl.<DialogClass> getController();
			controller.initData(groupId, value,gActivity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void initData(String groupId, boolean value,GroupActivity gActivity) {
		this.adapterGroupId = groupId;
		this.isChecked = value;
		this.gActivity = gActivity;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		toolBar.setStyle("-fx-background-color:" + AppConfiguration.getBorderColour() + ";");
		titleDialogLabel.setText(AppConfiguration.appConfString.confirm);
		setStrings();

	}

	private void setStrings() {

		if (okButton != null)
			okButton.setText(AppConfiguration.appConfString.ok);

		if (cancelButton != null)
			cancelButton.setText(AppConfiguration.appConfString.no);

		if (confirmLabel1 != null)
			confirmLabel1.setText(AppConfiguration.appConfString.are_you_sure_you_want_to_change);
	}

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof Button) {
			Button clickedBtn = (Button) source;
			String id = clickedBtn.getId();
			if (id != null) {
				if (okButton != null && id.equalsIgnoreCase(okButton.getId())) {
					Stage stage = (Stage) okButton.getScene().getWindow();
					if (CashewnutApplication.isNetworkConnected()) {
						this.gActivity.enableOrDisableFeature(isChecked, adapterGroupId);
					}
					stage.close();
					return;
				}
				if (cancelButton != null && id.equalsIgnoreCase(cancelButton.getId())) {
					Stage stage = (Stage) dialogAnchorPane.getScene().getWindow();
					if (CashewnutApplication.isNetworkConnected()) {
						this.gActivity.disableFeature(isChecked);
					}
					stage.close();
					return;
				}
			}
		}
	}
}
