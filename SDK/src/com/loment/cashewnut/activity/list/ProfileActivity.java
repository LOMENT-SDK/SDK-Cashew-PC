package com.loment.cashewnut.activity.list;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBContactsMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.ContactsModel;
import com.loment.cashewnut.model.GroupModel;

/**
*
* @author sekhar
*/
public class ProfileActivity {

    // contact List components
    ListView<Map.Entry<String, ContactsModel>> contactListView = null;
    List<Map.Entry<String, ContactsModel>> contactList;
    ObservableList<Map.Entry<String, ContactsModel>> contactObservableList = null;
    String bgColour = "-fx-background-color: transparent;";

    // Group List components
    ListView<Map.Entry<String, GroupModel>> groupListView = null;
    List<Map.Entry<String, GroupModel>> groupList;
    ObservableList<Map.Entry<String, GroupModel>> groupObservableList = null;

    private ArrayList<String> adapterToVector = null;
    private String adapterFrom = "";
    private String adapterGroupId = "";

    @FXML
    AnchorPane contactsAnchorPane;

    String cashewnutId = "";

    /**
     * Called when the activity is first created.
     *
     * @param adapterToVector
     * @param groupId
     */
	public ProfileActivity(ArrayList<String> adapterToVector,
			String adapterFrom, String groupId) {
		this.adapterGroupId = groupId;
		this.adapterFrom = adapterFrom;
		this.adapterToVector = adapterToVector;
		if (cashewnutId == null || cashewnutId.trim().equals("")) {
			DBAccountsMapper accountsMapper = DBAccountsMapper.getInstance();
			AccountsModel accountsModel = accountsMapper.getAccount();
			cashewnutId = accountsModel.getCashewnutId();
		}
	}

    public BorderPane getLayout() {
        DBContactsMapper profileMapper = DBContactsMapper.getInstance();
        ContactsModel cModel = profileMapper.getContact(adapterFrom,0);
        cModel.setCashewnutId(adapterFrom);
        HBox from = getGroupCell(cModel);
        from.setAlignment(Pos.CENTER);
        createContactListComponents();
        BorderPane messageBorderPane = new BorderPane();
        messageBorderPane.setTop(from);
        messageBorderPane.setCenter(contactListView);

        return messageBorderPane;
    }

    // =========================== Contact List ==========================
    // ========================================================================
    boolean isContactRightClick = false;

    private void createContactListComponents() {
        contactListView = new ListView();
        contactList = new ArrayList();
        contactObservableList = FXCollections.observableArrayList(contactList);
        //contactsAnchorPane.getChildren().add(contactListView);
        contactListView.setPrefWidth(290);

        ChangeListener changeListener = (ChangeListener) new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {
                final Map.Entry<String, ContactsModel> entry = (Map.Entry<String, ContactsModel>) newValue;
                if (entry != null) {
                    if (isContactRightClick) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                }
            }
        };

        contactListView.addEventFilter(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent event) {
                        isContactRightClick = false;
                        if (!event.isSecondaryButtonDown()) {
                            isContactRightClick = true;
                        }
                    }
                });

        contactListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
            	CashewnutActivity.idleTime = System.currentTimeMillis();
                isContactRightClick = false;
                if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
                    isContactRightClick = true;
                }
            }
        });

        contactListView.setItems(null);
        contactListView.setItems(contactObservableList);
        contactListView.getSelectionModel().selectedItemProperty()
                .addListener(changeListener);

        contactListView
                .setCellFactory(new Callback<ListView<Map.Entry<String, ContactsModel>>, ListCell<Map.Entry<String, ContactsModel>>>() {
                    @Override
                    public ListCell<Map.Entry<String, ContactsModel>> call(
                            ListView<Map.Entry<String, ContactsModel>> p) {
                                ListCell<Map.Entry<String, ContactsModel>> cell = new ListCell<Map.Entry<String, ContactsModel>>() {
                                
                                    @Override
                                    protected void updateItem(
                                            Map.Entry<String, ContactsModel> t,
                                            boolean bln) {
                                    	CashewnutActivity.idleTime = System.currentTimeMillis();
                                                super.updateItem(t, bln);
                                                if (t != null && t.getValue() != null) {
                                                    HBox cell = getGroupCell(t.getValue());
                                                    setGraphic(cell);
                                                } else {
                                                    setGraphic(null);
                                                }
                                            }
                                };
                                return cell;
                            }
                });
        addDataToContactList();
    }

    private HBox getGroupCell(ContactsModel t) {
        DBContactsMapper profileMapper = DBContactsMapper
                .getInstance();
        String memberString = "";
        Label label = new Label();
        Label members = new Label();

        try {
            String cid = t.getCashewnutId();
            String name = "";
            //label = new Label(memberString);
            ContactsModel contactsModel = profileMapper
                    .getContact(cid,0);
            name = contactsModel.getFirstName();
            if (name != null
                    && name.trim().length() > 0) {
                memberString = memberString + name;
                label = new Label(memberString);
                members = new Label(cid);
            } else {
                memberString = memberString + cid + "  ";
                label = new Label(memberString);
                members = new Label("");
            }
        } catch (Exception e) {
        }
        ImageView user_icon_single =null;
        		if(t.cashewnutId.equalsIgnoreCase("CASHEW"))
        		{
        			user_icon_single=ConversationsViewRenderer
        	                .resize1(
        	                        AppConfiguration.getIconPath() + "ic_cashew.png",
        	                        48);
        		}
        		else
        		{
        user_icon_single=ConversationsViewRenderer
                .resize1(
                        AppConfiguration.getUserIconPath() + "ic_action_user_48.png",
                        48);
        		}
        Label lblUserIcon = new Label(" ");
        lblUserIcon.setGraphic(user_icon_single);

        CircularUserIcon lblUserIcon1 = new CircularUserIcon(
                "NS");
        Text text = (Text) lblUserIcon1.getChildren()
                .get(1);
        text.setText(memberString.charAt(0) + "");

        label.setId("custom-label-from");
        members.setId("custom-label-from");
        VBox cell = new VBox(label, members);
        cell.setSpacing(4);
        cell.setAlignment(Pos.CENTER_LEFT);
        cell.setPrefWidth(200);
        cell.setPadding(new Insets(0, 10, 0, 2));
        cell.setPrefHeight(60);

        HBox mainPanel = new HBox(lblUserIcon, cell);
        mainPanel.setPadding(new Insets(0, 10, 0, 2));
        mainPanel.setAlignment(Pos.CENTER_LEFT);
        return mainPanel;
    }

    void addDataToContactList() {
        try {
            cleanContactList();
            DBContactsMapper profileMapper = DBContactsMapper.getInstance();
            ArrayList<ContactsModel> groupList1 = new ArrayList<ContactsModel>();  

            if (adapterToVector.size() > 0) {
                for (int i = 0; i < adapterToVector.size(); i++) {

                    ContactsModel cModel = profileMapper.getContact(adapterToVector.get(i),0);
                    cModel.setCashewnutId(adapterToVector.get(i));
                    groupList1.add(cModel);
                }
            }

            if (groupList1.size() > 0) {
                // Sorting
                Collections.sort(groupList1, new Comparator<ContactsModel>() {
                    @Override
                    public int compare(ContactsModel groupModel1,
                            ContactsModel groupModel2) {
                        return groupModel1.getFirstName().compareTo(
                                groupModel2.getFirstName());
                    }
                });

                ArrayList<Integer> contactIds = new ArrayList<Integer>();
                for (Object groupList11 : groupList1) {
                    ContactsModel gModel = (ContactsModel) groupList11;
                    if (contactIds.contains(gModel.getContactId())) {
                        continue;
                    }
                    contactIds.add(gModel.getContactId());
                    Map.Entry<String, ContactsModel> entry = new AbstractMap.SimpleEntry(
                            gModel.getCashewnutId(), gModel);
                    contactList.add(entry);
                }

                contactObservableList.setAll(contactList);
                contactListView.setItems(contactObservableList);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    void cleanContactList() {
        contactListView.getItems().clear();
        contactObservableList.clear();
        contactList.clear();
    }
}
