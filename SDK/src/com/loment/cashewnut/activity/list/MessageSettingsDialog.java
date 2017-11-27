/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.model.MessageControlParameters;
import com.loment.cashewnut.model.SettingsModel;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author sekhar
 */
public class MessageSettingsDialog extends Stage implements Initializable {

    @FXML
    ComboBox priorityComboBox;
    @FXML
    ComboBox hoursComboBox;
    @FXML
    ComboBox minutesComboBox;
    @FXML
    CheckBox restructedCheckBox;
    @FXML
    CheckBox ackCheckBox;
    @FXML
    CheckBox expiryCheckBox;
    @FXML
    TextField expiryTextField;
    @FXML
    Button okButton;
    @FXML
    HBox toolBar;
    @FXML
    Button cancelButton;
    @FXML
    Label titleSettingsLabel;
    @FXML
    Label priorityIndicatorLabel;
    @FXML
    CheckBox scheduleCheckBox;
    @FXML
    DatePicker datePicker;
    @FXML
    AnchorPane  anchorPane;
    ArrayList<Long> hours=new ArrayList<Long>();
    ArrayList<Long> minutes=new ArrayList<Long>();
    MessageControlParameters controlParameters;

    public MessageSettingsDialog() {
        super();
    }

    public MessageSettingsDialog(MessageControlParameters controlParameters, Stage owner, double x, double y) {
        super();
        try {
        	CashewnutActivity.idleTime = System.currentTimeMillis();
            initOwner(owner);
            FXMLLoader fl = new FXMLLoader();
            fl.setLocation(getClass().getResource("fxml_settings.fxml"));
            fl.load();
            Parent root = fl.getRoot();
            Scene scene = new Scene(root);
            this.setScene(scene);
            this.initStyle(StageStyle.UNDECORATED);
            this.initModality(Modality.WINDOW_MODAL);
            //this.centerOnScreen();
            this.setResizable(false);

            this.setX(x);
            this.setY(y);

            MessageSettingsDialog controller
                    = fl.<MessageSettingsDialog>getController();
            controller.initData(controlParameters);
            
        } catch (Exception ex) {
            Logger.getLogger(MessageSettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	CashewnutActivity.idleTime = System.currentTimeMillis();
    	toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

        expiryCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (expiryCheckBox.isSelected()) {
                    expiryTextField.setVisible(true);
                } else {
                    expiryTextField.setVisible(false);
                }
            }
        });
       scheduleCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (scheduleCheckBox.isSelected()) {
                    datePicker.setVisible(true);
                    
                    hoursComboBox.setVisible(true);
                    minutesComboBox.setVisible(true);
                } else {
                    datePicker.setVisible(false);
                    hoursComboBox.setVisible(false);
                    minutesComboBox.setVisible(false);
                }
            }
        });
        expiryTextField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    char ch = expiryTextField.getText().charAt(oldValue.intValue());
//                    System.out.println("Length:" + oldValue + "  "
//                            + newValue + " " + ch);
                    if (!(ch >= '0' && ch <= '9')) {
                        expiryTextField.setText(expiryTextField.getText()
                                .substring(0, expiryTextField.getText().length() - 1));
                    }

                    if (newValue.intValue() > maxLength) {
                        expiryTextField.setText(expiryTextField.getText()
                                .substring(0, maxLength));
                    }
                }
            }
        });

        try {
            SettingsModel model = new SettingsStore().getSettingsData();
           
            if (model != null) {
                String selectedFamily = model.getFontFamily();
                if (selectedFamily != null && !selectedFamily.trim().equals("")) {
                    String style = "-fx-font-family: " + selectedFamily + ";"
                            + " -fx-border-color: #A8A8A8;"
                            + " -fx-border-width: 2;";
                    anchorPane.setStyle(style);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setStrings();
    }
    
    private void setStrings() {
		if (priorityIndicatorLabel != null)
			priorityIndicatorLabel.setText(AppConfiguration.appConfString.dialog_priority_indicator);
		
		if (priorityComboBox != null)
			priorityComboBox.setPromptText(AppConfiguration.appConfString.settings_low);
		
		if (expiryCheckBox != null)
			expiryCheckBox.setText(AppConfiguration.appConfString.dialog_expiry_in);
		
		if (expiryTextField != null)
			expiryTextField.setPromptText(AppConfiguration.appConfString.dialog_mins);
		
		if (restructedCheckBox != null)
			restructedCheckBox.setText(AppConfiguration.appConfString.forward_backup_restricted);
		
		if (ackCheckBox != null)
			ackCheckBox.setText(AppConfiguration.appConfString.dialog_read_ack);
		if (scheduleCheckBox != null)
			scheduleCheckBox.setText(AppConfiguration.appConfString.dialog_schedule_message);
		
		if (okButton != null)
			okButton.setText(AppConfiguration.appConfString.ok);
		
		if (cancelButton != null)
			cancelButton.setText(AppConfiguration.appConfString.no);
    }

    void initData(MessageControlParameters controlParameters) {
        this.controlParameters = controlParameters;
        setValues(controlParameters);
    }
    int maxLength = 3;

    private void setValues(MessageControlParameters controlParameters) throws NumberFormatException {
        if (priorityComboBox != null) {
            String priority = controlParameters.getPriority();
            String expiry = controlParameters.getExpiry();
            String restructed = controlParameters.getRestricted();
            String ack = controlParameters.getMessageAck();
            long scheduleTime=controlParameters.getMessageSchedule();
            ObservableList<String> options
                    = FXCollections.observableArrayList(
                            "Highest",
                            "High",
                            "Medium",
                            "Low"
                    );
            titleSettingsLabel.setText(AppConfiguration.appConfString.compose_message_settings);
            priorityComboBox.setItems(options);
            expiryTextField.setText("");

            if (priority.trim().length() > 0 && !priority.trim().equals("-1")) {
                priorityComboBox.getSelectionModel().select(Integer.parseInt(priority));
            } else {
                priorityComboBox.setValue("Low");
            }

            if(hoursComboBox!=null)
            {
            	for(long i=00;i<24;i++)
            	{
            		hours.add(i);
            	}
            	ObservableList<Long> hourslist
                = FXCollections.observableArrayList(hours);
            	hoursComboBox.setItems(hourslist);
            	
            }
            if(minutesComboBox!=null)
            {
            	for(long i=00;i<=59;i++)
            	{
            		minutes.add(i);
            	}
            	
            	ObservableList<Long> minutesList
                = FXCollections.observableArrayList(minutes);
            	minutesComboBox.setItems(minutesList);
            }
            
            
            if (restructed.trim().length() > 0 && !restructed.trim().equals("-1")) {
                restructedCheckBox.setSelected(true);
            }

            if (ack.trim().length() > 0 && !ack.trim().equals("-1")) {
                ackCheckBox.setSelected(true);
            }

           if(scheduleTime>0)
            {
            	scheduleCheckBox.setSelected(false);
            }
            
            if (expiry.trim().length() > 0 && !expiry.trim().equals("-1")) {
                expiryCheckBox.setSelected(true);
                expiryTextField.setText(expiry);
                expiryTextField.setVisible(true);
            } else {
                expiryTextField.setVisible(false);
                expiryCheckBox.setSelected(false);
            }
        }
    }

    
    
    public void getValues() {
        controlParameters.setExpiry("-1");
        controlParameters.setRestricted("-1");
        controlParameters.setPriority("1");
        controlParameters.setMessageAck("-1");
        controlParameters.setMessageSchedule(-1);

        controlParameters.setPriority(priorityComboBox.getSelectionModel().getSelectedIndex()
                + "");

        if (expiryCheckBox.isSelected()) {
        	CashewnutActivity.idleTime = System.currentTimeMillis();
            String expiry = (expiryTextField.getText()
                    .trim().length() > 0) ? expiryTextField
                            .getText().trim() : "-1";
            controlParameters.setExpiry(expiry);
        }
       
        
       if (scheduleCheckBox.isSelected()) {
                    
        	try
        	{
        	String date=(String)datePicker.getValue().toString();
        	long hours=((Long)hoursComboBox.getValue())*(60*60);
        	long minutes=((Long)minutesComboBox.getValue())*60;
        	long milliSeconds=((hours+minutes))*1000;   	
        	 DateFormat formatter;
             formatter = new SimpleDateFormat("yyyy-MM-dd");
             Date date2= (Date) formatter.parse(date);
             long dateTomilli=date2.getTime();
             long scheduleTime=milliSeconds+dateTomilli;
             if(scheduleTime>System.currentTimeMillis())
             {
            	 
        	controlParameters.setMessageSchedule((scheduleTime));
        	
             }
             else
             {
            	 controlParameters.setMessageSchedule(-1);
             }
        	
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        }
        else
        {
        	controlParameters.setMessageSchedule(-1);
        	
        }
        

        if (restructedCheckBox.isSelected()) {
        	CashewnutActivity.idleTime = System.currentTimeMillis();
            controlParameters.setRestricted("3");
        }

        if (ackCheckBox.isSelected()) {
        	CashewnutActivity.idleTime = System.currentTimeMillis();
            controlParameters.setMessageAck("1");
        }
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedBtn = (Button) source;
            String id = clickedBtn.getId();
            if (id != null) {
            	CashewnutActivity.idleTime = System.currentTimeMillis();
                if (okButton != null && id.equalsIgnoreCase(okButton.getId())) {
                    getValues();
                    Stage stage = (Stage) okButton.getScene().getWindow();
                    stage.close();
                   
                    return;
                }
                if (cancelButton != null && id.equalsIgnoreCase(cancelButton.getId())) {
                	CashewnutActivity.idleTime = System.currentTimeMillis();
                    Stage stage = (Stage) okButton.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }
}
