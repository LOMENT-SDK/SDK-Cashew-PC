package com.loment.cashewnut.activity.list;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.Controller;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.util.Helper;

import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmojiPopUp extends Stage implements Initializable {
	@FXML
	TabPane EmojiTabPane;
	@FXML
	BorderPane EmojiBorder;
	@FXML
	GridPane peopleGrid;
	@FXML
	GridPane RecentGrid;
	@FXML
	GridPane ObjectGrid;
	@FXML
	GridPane NatureGrid;
	@FXML
	GridPane PlacesGrid;
	@FXML
	GridPane SymbolGrid;
	@FXML
	AnchorPane PeopleAnchor;
	@FXML
	AnchorPane RecentAnchor;
	@FXML
	AnchorPane objectAnchor;
	@FXML
	AnchorPane natureAnchor;
	@FXML
	AnchorPane placesAnchor;
	@FXML
	AnchorPane symbolAnchor;
	@FXML
	Button closeButtonEmoji;
	@FXML
	ImageView closeImageView;
	@FXML
	ImageView recentImageView;
	@FXML
	ImageView peopleImageView;
	@FXML
	ImageView objectImageView;
	@FXML
	ImageView natureImageView;
	@FXML
	ImageView placesImageView;
	@FXML
	ImageView symbolImageView;
	@FXML
	ScrollBar PeopleScroll;
	@FXML
	ScrollBar ObjectScroll;
	@FXML
	ScrollBar NatureScroll;
	@FXML
	ScrollBar PlacesScroll;
	@FXML
	ScrollBar SymbolScroll;
	@FXML
	ScrollBar RecentScroll;
	@FXML
	Button emojiButton;		
	public EmojiPopUp() {
		super();
	}

	public EmojiPopUp(Stage owner, double x, double y) {
		super();
		try {
			initOwner(owner);
			FXMLLoader fl = new FXMLLoader();
			fl.setLocation(getClass().getResource("fxml_emoji.fxml"));
			EmojiBorder = fl.load();
			ToolBar tb = (ToolBar) EmojiBorder.getTop();
			ObservableList ob = tb.getItems();
			closeButtonEmoji = (Button) ob.get(0);
			closeImageView = (ImageView) closeButtonEmoji.getGraphic();
			EmojiTabPane = (TabPane) EmojiBorder.getChildren().get(0);
			RecentAnchor = (AnchorPane) EmojiTabPane.getTabs().get(0).getContent();			
			PeopleAnchor = (AnchorPane) EmojiTabPane.getTabs().get(1).getContent();
			objectAnchor = (AnchorPane) EmojiTabPane.getTabs().get(2).getContent();
			natureAnchor=(AnchorPane) EmojiTabPane.getTabs().get(3).getContent();
			placesAnchor=(AnchorPane) EmojiTabPane.getTabs().get(4).getContent();
			symbolAnchor=(AnchorPane) EmojiTabPane.getTabs().get(5).getContent();
			RecentGrid = (GridPane) RecentAnchor.getChildren().get(0);
			peopleGrid = (GridPane) PeopleAnchor.getChildren().get(0);
			ObjectGrid=(GridPane) objectAnchor.getChildren().get(0);
			NatureGrid=(GridPane) natureAnchor.getChildren().get(0);
			PlacesGrid=(GridPane) placesAnchor.getChildren().get(0);
			SymbolGrid=(GridPane) symbolAnchor.getChildren().get(0);
			recentImageView=(ImageView) EmojiTabPane.getTabs().get(0).getGraphic();
			peopleImageView=(ImageView) EmojiTabPane.getTabs().get(1).getGraphic();
			objectImageView=(ImageView) EmojiTabPane.getTabs().get(2).getGraphic();
			natureImageView=(ImageView) EmojiTabPane.getTabs().get(3).getGraphic();
			placesImageView=(ImageView) EmojiTabPane.getTabs().get(4).getGraphic();
			symbolImageView=(ImageView) EmojiTabPane.getTabs().get(5).getGraphic();
			RecentGrid.setHgap(10);
			RecentGrid.setVgap(10);
			peopleGrid.setHgap(10);
			peopleGrid.setVgap(10);
			ObjectGrid.setHgap(10);
			ObjectGrid.setVgap(10);
			NatureGrid.setHgap(10);
			NatureGrid.setVgap(10);
			PlacesGrid.setHgap(10);
			PlacesGrid.setVgap(10);
			SymbolGrid.setHgap(10);
			SymbolGrid.setVgap(10);
			setRecentEmoji();
			PeopleScroll=(ScrollBar) PeopleAnchor.getChildren().get(1);
			ObjectScroll=(ScrollBar) objectAnchor.getChildren().get(1);
			NatureScroll=(ScrollBar) natureAnchor.getChildren().get(1);
			PlacesScroll=(ScrollBar) placesAnchor.getChildren().get(1);
			SymbolScroll=(ScrollBar) symbolAnchor.getChildren().get(1);
			RecentScroll=(ScrollBar) RecentAnchor.getChildren().get(1);
			PeopleScroll.valueProperty().addListener((ObservableValue<? extends Number> ov, 
			            Number old_val, Number new_val) -> {
			        	    peopleGrid.setVgrow(PeopleAnchor, Priority.ALWAYS);
			        	    peopleGrid.setLayoutY(-new_val.doubleValue());	        	
			        });
			ObjectScroll.valueProperty().addListener((ObservableValue<? extends Number> ov, 
		            Number old_val, Number new_val) -> {
		            	ObjectGrid.setVgrow(objectAnchor, Priority.ALWAYS);
		            	ObjectGrid.setLayoutY(-new_val.doubleValue());	        	
		        });
			NatureScroll.valueProperty().addListener((ObservableValue<? extends Number> ov, 
		            Number old_val, Number new_val) -> {
		            	NatureGrid.setVgrow(natureAnchor, Priority.ALWAYS);
		            	NatureGrid.setLayoutY(-new_val.doubleValue());	        	
		        });
			PlacesScroll.valueProperty().addListener((ObservableValue<? extends Number> ov, 
		            Number old_val, Number new_val) -> {
		            	PlacesGrid.setVgrow(placesAnchor, Priority.ALWAYS);
		            	PlacesGrid.setLayoutY(-new_val.doubleValue());	        	
		        });			
			SymbolScroll.valueProperty().addListener((ObservableValue<? extends Number> ov, 
		            Number old_val, Number new_val) -> {
		            	SymbolGrid.setVgrow(symbolAnchor, Priority.ALWAYS);
		            	SymbolGrid.setLayoutY(-new_val.doubleValue());	        	
		        });
			RecentScroll.valueProperty().addListener((ObservableValue<? extends Number> ov, 
		            Number old_val, Number new_val) -> {
		            	RecentGrid.setVgrow(RecentAnchor, Priority.ALWAYS);
		            	RecentGrid.setLayoutY(-new_val.doubleValue());	        	
		        });
			
			Iterator entries = Emoji.sEmojisMap.entrySet().iterator();
			int i = 1;
			int objectCount=183;
			int natureCount=365;
			int placesCount=521;
			int symbolCount=677;
			int addRow=0;
			int objAddRow=0;
			int natureAddRow=0;
			int placesAddRow=0;
			int symbolAddRow=0;
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();				
				peopleGrid.addRow(addRow,
						(ImageView) (ConversationsViewRenderer.getImageView((Image) (thisEntry.getValue()), 22)));
				
if(i%13==0)
{
	addRow++;
}
if(i>155)
{
	break;
}				
				i++;
			}	
			
			
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				ObjectGrid.addRow(objAddRow,
						(ImageView) (ConversationsViewRenderer.getImageView((Image) (thisEntry.getValue()), 22)));
				if(objectCount%13==0)
				{
					objAddRow++;
				}
				if(objectCount>337)
				{
					break;
				}
				objectCount++;
			}
			
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				NatureGrid.addRow(natureAddRow,
						(ImageView) (ConversationsViewRenderer.getImageView((Image) (thisEntry.getValue()), 22)));
				if(natureCount%13==0)
				{
					natureAddRow++;
				}
				if(natureCount>519)
				{
					break;
				}
				natureCount++;
			}
			
			
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				PlacesGrid.addRow(placesAddRow,
						(ImageView) (ConversationsViewRenderer.getImageView((Image) (thisEntry.getValue()), 22)));
				if(placesCount%13==0)
				{
					placesAddRow++;
				}
				if(placesCount>675)
				{
					break;
				}
				placesCount++;
			}	
			
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				SymbolGrid.addRow(symbolAddRow,
						(ImageView) (ConversationsViewRenderer.getImageView((Image) (thisEntry.getValue()), 22)));
				if(symbolCount%13==0)
				{
					symbolAddRow++;
				}
				if(symbolCount>831)
				{
					break;
				}
				symbolCount++;
			}
			
			Parent root = fl.getRoot();
			Scene scene = new Scene(root);
			this.setScene(scene);
			this.initStyle(StageStyle.UNDECORATED);
			this.initModality(Modality.WINDOW_MODAL);
			this.setResizable(true);
			
			double r = 10.0;
			closeButtonEmoji.setShape(new Circle(r));
			closeButtonEmoji.setMinSize(2 * r, 2 * r);
			closeButtonEmoji.setMaxSize(2 * r, 2 * r);
			closeImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getIconPath() + "close.png")));
			closeImageView.getStyleClass().add("closeButtonCss");
			
			recentImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2665.png")));
			
			peopleImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f60a.png")));
			objectImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f4e2.png")));
			natureImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f339.png")));			
			placesImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_2708.png")));
			symbolImageView.setImage(
					new Image(EmojiPopUp.class.getResourceAsStream(AppConfiguration.getEmojiPath() + "emoji_1f197.png")));
		} catch (Exception ex) {
			Logger.getLogger(MessageSettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	public void setRecentEmoji()
	{
		//if(CashewnutActivity.recentEmoji)
		//{
			File dir = new File(Helper.getAbsolutePath("RecentEmojis"));
			  File[] directoryListing = dir.listFiles();
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
					
					Emoji.RecentsEmojisMap.put(child.getName().replace(".png", ""), new Image(child.toURI().toString()));
			    }
			    
			    }
				Iterator recent = Emoji.RecentsEmojisMap.entrySet().iterator();
				int k = 1;
				int addRowRecent=0;
				RecentGrid.getChildren().clear();
				while (recent.hasNext()) {
					Entry recentEnt = (Entry) recent.next();
					
					RecentGrid.addRow(addRowRecent,
							(ImageView)(ConversationsViewRenderer.getImageView((Image)(recentEnt.getValue()), 22)));
					if(k%13==0)
					{
						addRowRecent++;
					}
					if(k>155)
					{
						break;
					}
					k++;
			}
				//}
	}
	
	public void close1() {
		System.out.println(this.EmojiBorder);
		Stage stage = (Stage) this.EmojiBorder.getScene().getWindow();
		stage.close();
	}
}
