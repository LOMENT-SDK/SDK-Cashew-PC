/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import org.json.JSONObject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.activity.controller.NetworkConnection;
import com.loment.cashewnut.activity.view.javafx.CountryCodes;
import com.loment.cashewnut.activity.view.javafx.CountrySearchActivity;
import com.loment.cashewnut.activity.view.javafx.RegisterFormJfx;
import com.loment.cashewnut.database.ProfileStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.sthithi.connection.AccountHandler;
import com.loment.cashewnut.sthithi.connection.SubscriptionHandler;
import com.loment.cashewnut.util.Helper;
import com.loment.cashewnut.search.CountryModal;

/**
 * FXML Controller class
 *
 * @author ajay
 */
public class Reports implements Initializable {

	@FXML
	BorderPane borderPane;

	LomentDataModel dataModel = null;
	LomentDataModel dataModel1 = null;
	@FXML
	private Label cashewIdLabel;
	@FXML
	private Label cashewIDLabel1;
	@FXML
	private Label cashewIDLabel2;
	@FXML
	private Label subscriptionDetailsLabel;
	@FXML
	private Label typeLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private Label lomentIDLabel;
	@FXML
	private Label primaryemailLabel;
	@FXML
	private Label subcriptionTypeLabel;
	@FXML
	private Label subcriptionStatus;
	@FXML
	private Label subcriptionStartDate;
	@FXML
	private Label subcriptionEndDate;
	@FXML
	private Label startDateLabel;
	@FXML
	private Label endDateLabel;
	@FXML
	private Label accountSubscriptionLabel;
	@FXML
	private Label registrationDetailsLabel;
	@FXML
	private Button closeButton;
	@FXML
	private Button accountSubscriptionButton;
	@FXML
	private ImageView closeButtonImage;
	@FXML
	private ToolBar toolBar;
	@FXML
	private HBox subcriptionStartDateLayout;
	@FXML
	private HBox subcriptionEndDateLayout;
	@FXML
	private HBox change_subscription_lbl_layout;
	@FXML
	private HBox change_subscription_layout;
	@FXML
	private HBox subscription_type_layout;
	@FXML
	private Label titleLabel;
	@FXML
	private Label nameLabel;
	@FXML
	private Label numberLabel;
	@FXML
	private Label userName;
	@FXML
	private Label phoneNumber;
	@FXML
	private Button editNameButton;
	@FXML
	private Button editNumberButton;
	@FXML
	private TextField updateNameText;
	@FXML
	private Button NameUpdateButton;
	@FXML
	private TextField updateNumberText;
	@FXML
	private Button NumberUpdateButton;
	/*@FXML
	private Button ImageButton;*/
	@FXML
	private Label errorNameLabel;
	@FXML
	private Label errorPhonenumberLabel;
	@FXML
	private ComboBox<CountryModal> countryCode;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private AnchorPane EditNameAnchorPane;
	@FXML
	private AnchorPane EditNumberAnchorPane;
	
	ArrayList<CountryModal> arraylist = null;
	boolean isMousePressed = false;
	private double xOffset = 0;
	private double yOffset = 0;

	private SplitPane parentSplitPane;
	String textUpdated = "";
	String numberUpdated = "";
	String primaryEmail = "";
	String updated = "";
	String countryAbbr = "";
	String countryAbbrev = "";
	String number_db = "";
	InputStream is;
	String code;
	String number = "";
	String name = "";
	ImageView user_icon_single = null;
	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO

		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:" + AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:" + AppConfiguration.getBorderColour() + ";");

		dataModel = DBLomentDataMapper.getInstance().getLomentData();
		primaryEmail = dataModel.getEmail();
		String cashewnutId = DBAccountsMapper.getInstance().getAccount().getCashewnutId();
		String name = dataModel.getUsername();

		try {
			is = CountryCodes.class.getResource("country_codes.csv").openStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CountryCodes.getInstance().fillCountryCodes(is);
		code = CountryCodes.getInstance().getTelephoneCodeByCountry(dataModel.getCountryAbbr());
		countryAbbrev = CountryCodes.getInstance().getIsoCodeByCountry(dataModel.getCountryCode());
		number = dataModel.getMobileNumber();
		System.out.println(countryAbbr);
		getCountryCodes();
		setCountryCodes();
		progressIndicator.setVisible(false);
		progressIndicator.setProgress(progressIndicator.INDETERMINATE_PROGRESS);
		errorNameLabel.setVisible(false);
		errorNameLabel.setTextFill(Color.web("RED"));
		errorPhonenumberLabel.setTextFill(Color.web("RED"));
		setPhoneNumberListener();
		primaryemailLabel.setText(primaryEmail.trim());
		cashewIdLabel.setText(cashewnutId.trim());
		nameLabel.setText(name);
		numberLabel.setText(number);
		countryCode.setVisible(false);
		
		ImageView editButtonIcon = ConversationsViewRenderer
				.resizeAttachment(AppConfiguration.getIconPath() + "ic_action_edit.png", 10, 15);
		ImageView editButtonIcon1 = ConversationsViewRenderer
				.resizeAttachment(AppConfiguration.getIconPath() + "ic_action_edit.png", 10, 15);
		//ImageButton.setShape(new Circle(40));
		//ImageButton.setMaxSize(40,40);
		ImageView ImageButtonEdit = ConversationsViewRenderer
				.resizeAttachment(AppConfiguration.getUserIconPath() + "ic_action_users_green.png", 50, 50);	
				 final Circle clip = new Circle();
				 clip.setRadius(60);
				 ImageButtonEdit.setClip(clip);
				// ImageButton.setGraphic(ImageButtonEdit);
				 editNameButton.setGraphic(editButtonIcon1);
		editNumberButton.setGraphic(editButtonIcon);
		closeButtonImage
				.setImage(new Image(Reports.class.getResourceAsStream(AppConfiguration.getIconPath() + "close.png")));

		double r = 10.5;
		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);

	/*	ImageButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				try {
					String path = changeGroupIcon();
					File f = new File(path);
					Platform.runLater(new Runnable() {
						public void run() {						
							ImagePattern img = new ImagePattern(new Image(f.toURI().toString(),50,50,true,true));
							final Circle clip = new Circle();
							clip.setRadius(70);
							clip.setFill(img);
							ImageButton.setGraphic(clip);
							
						}
					});
				} catch (Exception ex) {

					ex.printStackTrace();
				}
			}
			
			
		});*/
		
		
		editNameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
				
						updateNameText.setVisible(true);
						updateNameText.setText(nameLabel.getText());
						nameLabel.setVisible(false);
						editNameButton.setVisible(false);
						NameUpdateButton.setVisible(true);
						errorNameLabel.setVisible(false);
						progressIndicator.setVisible(false);
					}
				});
			}
		});

		NameUpdateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				progressIndicator.setVisible(true);

				new Thread() {
					public void run() {
						textUpdated = updateNameText.getText();
						if (textUpdated != null && textUpdated.trim().length() > 2) {
							String nameFromDb = updateName();
							if (nameFromDb != null) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										setNameListener();
										updateNameText.setVisible(false);
										NameUpdateButton.setVisible(false);
										nameLabel.setVisible(true);
										nameLabel.setText(nameFromDb);
										editNameButton.setVisible(true);
										errorNameLabel.setVisible(false);
										progressIndicator.setVisible(false);
									}
								});
							} else {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										errorNameLabel.setVisible(true);
										errorNameLabel.setText(AppConfiguration.appConfString.connection_lost);
										errorNameLabel.setTextFill(Color.web("RED"));
										progressIndicator.setVisible(false);
									}
								});
							}
						} else {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									labelVisible(errorNameLabel,
											AppConfiguration.appConfString.signup_username_min_2chars, "error");
									errorNameLabel.setVisible(true);
									progressIndicator.setVisible(false);
								}
							});
						}
					}
				}.start();
			}
		});

		editNumberButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						numberLabel.setVisible(false);
						updateNumberText.setVisible(true);
						editNumberButton.setVisible(false);
						countryCode.setVisible(true);
						NumberUpdateButton.setVisible(true);
						progressIndicator.setVisible(false);
						errorNameLabel.setVisible(false);
					}
				});
			}
		});

		NumberUpdateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				CountryModal model = countryCode.getSelectionModel().getSelectedItem();
				updateNumberText.setVisible(false);
				NumberUpdateButton.setVisible(false);
				new Thread()

				{
					public void run() {
						progressIndicator.setVisible(true);
						numberUpdated = updateNumberText.getText();
						if (numberUpdated != null && numberUpdated.length() > 7 && model != null) {
							String numberFromDb = updateNumber();
							if (numberFromDb != null) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										countryCode.setVisible(false);
										numberLabel.setVisible(true);

										numberLabel.setText(numberFromDb);
										editNumberButton.setVisible(true);
										errorPhonenumberLabel.setVisible(false);
										progressIndicator.setVisible(false);
										errorNameLabel.setVisible(false);
									}
								});
							} else {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										errorPhonenumberLabel.setVisible(true);
										updateNumberText.setVisible(true);
										NumberUpdateButton.setVisible(true);
										errorNameLabel.setVisible(false);
										errorPhonenumberLabel
												.setText(AppConfiguration.appConfString.connection_lost);
										//errorPhonenumberLabel.setTextFill(Color.web("RED"));
										progressIndicator.setVisible(false);
									}
								});
							}
						} else {
							if (model == null) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										errorNameLabel.setVisible(true);
										progressIndicator.setVisible(false);
										labelVisible(errorNameLabel, AppConfiguration.appConfString.select_country, "error");
									}
								});
							}
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									// errorNameLabel.setVisible(false);
									errorPhonenumberLabel.setVisible(true);
									countryCode.setVisible(true);
									updateNumberText.setVisible(true);
									NumberUpdateButton.setVisible(true);
									labelVisible(errorPhonenumberLabel,
											AppConfiguration.appConfString.signup_mobile_number_7char, "error");
									progressIndicator.setVisible(false);
								}
							});
						}
					}
				}.start();
			}
		});

		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						Stage stage = (Stage) closeButton.getScene().getWindow();
						stage.close();
					}
				});
			}
		});
		toolBar.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
				isMousePressed = true;

			}
		});
		toolBar.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
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

		accountSubscriptionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				close1();
				// System.out.println("PASSWORD");
				try {
					FXMLLoader loader = new FXMLLoader(ConversationView.class.getResource("fxml_subscription.fxml"));
					Parent settings = (Parent) loader.load();
					Subscription controller = loader.<Subscription> getController();
					controller.initData(parentSplitPane);
					Stage newStage = new Stage();
					Scene scene = new Scene(settings);
					scene.getStylesheets().add(RegisterFormJfx.class.getResource("Style.css").toExternalForm());
					newStage.setScene(scene);
					newStage.initStyle(StageStyle.UNDECORATED);
					newStage.show();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		setStrings();
		setSubscriptionData();
		getDataFromServer();
	}

	public void close1() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
	public String downloadFile(File file) throws IOException {
		String extension = "";
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		String absolutePath = Helper.getAbsolutePath("user_profile");
		if (!absolutePath.endsWith("//")) {
			absolutePath = absolutePath + "//";
		}
	
		String cashewnutId = DBAccountsMapper.getInstance().getAccount().getCashewnutId();
		String sourceFilePath = absolutePath +cashewnutId;
		File newFile = new File(sourceFilePath);
		if (newFile.exists()||newFile.getName().startsWith(cashewnutId)) {
			newFile.delete();
		}
		GroupActivity.copyFile(file, newFile);
		return sourceFilePath;
	}
	public String changeGroupIcon() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("GIF", "*.gif"),
				new FileChooser.ExtensionFilter("BMP", "*.bmp"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			return downloadFile(selectedFile);
		} else {
			return null;
		}

	}
	private void setSubscriptionData() {
		try {
			LomentDataModel dataModel = ProfileStore.getInstance().getProfileData();

			if (dataModel != null) {
				subcriptionStartDateLayout.setVisible(View.VISIBLE);
				subcriptionEndDateLayout.setVisible(View.VISIBLE);
				subscription_type_layout.setVisible(View.VISIBLE);
				change_subscription_lbl_layout.setVisible(View.VISIBLE);
				change_subscription_layout.setVisible(View.VISIBLE);
				if (dataModel.getSubscriptionType() != null && !dataModel.getSubscriptionType().trim().equals("")) {
					if (dataModel.getSubscriptionType().trim().equals("T")) {
						subcriptionTypeLabel.setText("Trial");
					}
					if (dataModel.getSubscriptionType().trim().equals("P")) {
						subcriptionTypeLabel.setText("Paid");
						change_subscription_lbl_layout.setVisible(View.GONE);
						change_subscription_layout.setVisible(View.GONE);
					}
				}
				if (dataModel.getSubscriptionStatus() != null && !dataModel.getSubscriptionStatus().trim().equals("")) {
					if (dataModel.getSubscriptionStatus().trim().equals("Active")) {

						subcriptionStatus.setText(dataModel.getSubscriptionStatus());
						if (dataModel.getStartDate() != null) {
							String startDate = dataModel.getStartDate();
							startDate = Helper.replace(startDate, "00:00:00", "");
							subcriptionStartDate.setText(startDate);
						}
						if (dataModel.getEndDate() != null) {
							String endDate = dataModel.getEndDate();
							endDate = Helper.replace(endDate, "00:00:00", "");
							subcriptionEndDate.setText(endDate);
						}
					} else {
						subcriptionStatus.setText(dataModel.getSubscriptionStatus());
						subcriptionStartDateLayout.setVisible(View.GONE);
						subcriptionEndDateLayout.setVisible(View.GONE);
						subscription_type_layout.setVisible(View.GONE);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getDataFromServer() {
		try {
			NetworkConnection connection = new NetworkConnection();
			connection.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					Boolean isConnected = (Boolean) t.getSource().getValue();
					if (isConnected) {

						new Thread() {
							@Override
							public void run() {
								JSONObject responseJson = SubscriptionHandler.getInstance()
										.getSubscriptionDataFromSthithi();
								if (responseJson != null && !responseJson.toString().trim().equals(""))
									SubscriptionHandler.getInstance().subscriptionStatus(responseJson, false);
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										setSubscriptionData();
									}
								});

							}
						}.start();
					} else {
						// AppConfiguration.AppConfString.sender_network_unavailable
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								setSubscriptionData();
							}
						});
					}
				}
			});
			connection.start();
		} catch (Exception e) {
			setSubscriptionData();
		}
	}

	private void setStrings() {
		if (lomentIDLabel != null)
			lomentIDLabel.setText(AppConfiguration.appConfString.reports_loment_id);

		if (cashewIDLabel1 != null)
			cashewIDLabel1.setText(AppConfiguration.appConfString.cashew_id);

		if (cashewIDLabel2 != null)
			cashewIDLabel2.setText(AppConfiguration.appConfString.cashew_comments);

		if (typeLabel != null)
			typeLabel.setText(AppConfiguration.appConfString.reports_type);

		if (statusLabel != null)
			statusLabel.setText(AppConfiguration.appConfString.reports_status);

		if (subscriptionDetailsLabel != null)
			subscriptionDetailsLabel.setText(AppConfiguration.appConfString.reports_subscription_details);

		if (startDateLabel != null)
			startDateLabel.setText(AppConfiguration.appConfString.reports_start_date);

		if (endDateLabel != null)
			endDateLabel.setText(AppConfiguration.appConfString.reports_end_date);

		if (accountSubscriptionLabel != null)
			accountSubscriptionLabel.setText(AppConfiguration.appConfString.reports_account_subscription);

		if (userName != null)
			userName.setText(AppConfiguration.appConfString.reports_userName);

		if (phoneNumber != null)
			phoneNumber.setText(AppConfiguration.appConfString.reports_number);
		if (accountSubscriptionButton != null)
			accountSubscriptionButton.setText(AppConfiguration.appConfString.reports_activate_subscription);

		if (registrationDetailsLabel != null)
			registrationDetailsLabel.setText(AppConfiguration.appConfString.reports_registration_details);

		if (titleLabel != null) {
			titleLabel.setText(" " + AppConfiguration.appConfString.conversation_reports1);
			titleLabel.setFont(AppConfiguration.setFont());
		}
	}

	public String updateName() {
		// System.out.println(texUpated);
		if ((CashewnutApplication.isNetworkConnected())) {
			updated = AccountHandler.getInstance().editName(primaryEmail, textUpdated);
			dataModel1 = DBLomentDataMapper.getInstance().getLomentData();
			String storedNum=dataModel1.getMobileNumber();
			dataModel.setUsername(updated);
			dataModel.setMobileNumber(storedNum);
			DBLomentDataMapper.getInstance().updateUserDetails(dataModel);
			dataModel = DBLomentDataMapper.getInstance().getLomentData();
			return dataModel.getUsername();
		} else {
			return null;
		}
	}

	public String updateNumber() {
		CountryModal model = countryCode.getSelectionModel().getSelectedItem();
		String code = model.getCountryCode();
		final String countryAbbr = model.getCountryAbbr();

		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		numberUpdated = code + numberUpdated;
		if ((CashewnutApplication.isNetworkConnected())) {
			number_db = AccountHandler.getInstance().editNumber(primaryEmail, numberUpdated, countryAbbr);
			dataModel1 = DBLomentDataMapper.getInstance().getLomentData();
			String storedName=dataModel1.getUsername();
			dataModel.setMobileNumber(number_db);
			dataModel.setUsername(storedName);
			DBLomentDataMapper.getInstance().updateUserDetails(dataModel);
			dataModel = DBLomentDataMapper.getInstance().getLomentData();
			return dataModel.getMobileNumber();
		} else {
			return null;
		}
	}

	private void setPhoneNumberListener() {
		updateNumberText.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					CashewnutActivity.idleTime = System.currentTimeMillis();
					char ch = updateNumberText.getText().charAt(oldValue.intValue());
					// System.out.println("Length:" + oldValue + " "
					// + newValue + " " + ch);
					if (!(ch >= '0' && ch <= '9')) {
						updateNumberText.setText(
								updateNumberText.getText().substring(0, updateNumberText.getText().length() - 1));
					}
					labelHidden(errorPhonenumberLabel);
				}
			}
		});
	}

	private boolean phoneNumberCheck() {
		if (updateNumberText.getText() == null || updateNumberText.getText().trim().length() < 1) {
			labelVisible(errorPhonenumberLabel, AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		} else if (updateNumberText.getText().trim().length() < 7) {
			labelVisible(errorPhonenumberLabel, AppConfiguration.appConfString.signup_mobile_number_7char, "error");
			return false;
		}
		return true;
	}

	private boolean nameCheck() {
		if (updateNameText.getText() == null || updateNameText.getText().trim().length() < 1) {
			labelVisible(errorNameLabel, AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		} else if (updateNameText.getText().trim().length() < 2) {
			labelVisible(errorNameLabel, AppConfiguration.appConfString.signup_username_min_2chars, "error");
			return false;
		}
		return true;
	}

	private void setNameListener() {
		updateNameText.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				CashewnutActivity.idleTime = System.currentTimeMillis();
				labelHidden(errorNameLabel);
			}
		});
	}

	public void labelVisible(Label label, String text, String type) {
		label.getStyleClass().clear();
		System.out.println(text);
		label.setWrapText(true);
		label.getStyleClass().add("visible");
		label.getStyleClass().add(type);
		label.setText(text);
	}

	public void labelHidden(Label label) {
		label.setText("");
		label.getStyleClass().add("hidden");
	}

	private void setCountryCodes() {
		ObservableList<CountryModal> myComboBoxData = FXCollections.observableArrayList(arraylist);
		countryCode.setItems(myComboBoxData);

		// Define rendering of the list of values in ComboBox drop down.
		countryCode.setCellFactory(new Callback<ListView<CountryModal>, ListCell<CountryModal>>() {

			public ListCell<CountryModal> call(ListView<CountryModal> comboBox) {
				return new ListCell<CountryModal>() {
					
					@Override
					protected void updateItem(CountryModal item, boolean empty) {
						CashewnutActivity.idleTime = System.currentTimeMillis();
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
						} else {
							setText(item.getCountryName() + "  " + item.getCountryCode());
						}
					}
				};
			}
		});

		countryCode.setConverter(new StringConverter<CountryModal>() {
			@Override
			public String toString(CountryModal person) {
				if (person == null) {
					return null;
				} else {
					return "+" + person.getCountryCode();
				}
			}

			@Override
			public CountryModal fromString(String personString) {
				return null; // No conversion fromString needed.
			}
		});
	}

	public void getCountryCodes() {
		try {
			CountrySearchActivity countryList = new CountrySearchActivity();
			arraylist = countryList.getCountryList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initData(SplitPane splitPane) {
		this.parentSplitPane = splitPane;
	}

}
