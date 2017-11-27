package com.loment.cashewnut.activity.view.javafx;
import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.CashewnutApplication;
import com.loment.cashewnut.activity.controller.AddcashewnutIDForm;
import com.loment.cashewnut.activity.controller.JsonUtility;
import com.loment.cashewnut.activity.controller.NetworkConnection;
import com.loment.cashewnut.activity.controller.SignInService;
import com.loment.cashewnut.activity.controller.SignUpService1;
import com.loment.cashewnut.activity.controller.ValidatePasswordTask;
import com.loment.cashewnut.activity.list.ConversationView;
import com.loment.cashewnut.database.ProfileStore;
import com.loment.cashewnut.database.SettingsStore;
import com.loment.cashewnut.database.mappers.DBAccountsMapper;
import com.loment.cashewnut.database.mappers.DBLomentDataMapper;
import com.loment.cashewnut.model.AccountsModel;
import com.loment.cashewnut.model.LomentDataModel;
import com.loment.cashewnut.model.SettingsModel;
import com.loment.cashewnut.search.CountryModal;
import com.loment.cashewnut.sthithi.connection.DataConnection;

import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.async.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class FXMLExampleController implements Initializable {

	// sign in
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private Button signinButton;

	// registration
	@FXML
	private Button signupButton;
	@FXML
	private TextField name;
	@FXML
	private TextField email;
	@FXML
	private TextField confirmEmail;
	@FXML
	private ComboBox<CountryModal> countryCode;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField passWord;
	@FXML
	private TextField cPassword;
	@FXML
	private TextField promoCode;
	@FXML
	private TextField cashewId;

	@FXML
	private AnchorPane addAccount;

	@FXML
	private Button accountsButton;

	@FXML
	private ComboBox addAccountMenuButton;

	@FXML
	private Button newCashewIdTextField;

	@FXML
	private Button loginButton;

	@FXML
	private Label loginLabel;
	@FXML
	private Label label;
	@FXML
	private Label signLabel;
	@FXML
	private Label signUpLabel;
	@FXML
	private Label errorNameLabel;
	@FXML
	private Label errorMailLabel;
	// @FXML
	// private Label emailConfirmMailLabel;
	@FXML
	private Label errorPhonenumberLabel;
	@FXML
	private Label errorPasswordLabel;
	// @FXML
	// private Label errorCPasswordLabel;
	@FXML
	private Label cashewidErrorLabel;
	@FXML
	private Label promoErrorLabel;

	@FXML
	private Label errorAddCashewIdLabel;
	@FXML
	private Label ForgotPasswordLabel;
	@FXML
	private TextField loginPassword;

	@FXML
	private Text loginUserNameLabel;

	@FXML
	private Text accountsText;

	@FXML
	private Text accountsText1;

	@FXML
	private Hyperlink forgetPasswordLogin;

	@FXML
	private Text textCashew;

	@FXML
	private Text appName;

	@FXML
	private Text textCashewComments;

	@FXML
	private CheckBox remember;

	@FXML
	BorderPane borderPane;

	@FXML
	private ToolBar toolBar;

	@FXML
	private TabPane registerTabPane;

	@FXML
	private Button closeButton;

	@FXML
	private Button minimizeButton;

	@FXML
	private Button maximizeButton;

	@FXML
	private ImageView maximizeImageView;

	@FXML
	private ImageView minimizeImageView;

	@FXML
	private ImageView closeImageView;

	@FXML
	private HBox toolbarButtonsHBox;

	@FXML
	private ComboBox languageComboBox;

	@FXML
	private ImageView loginCashewLogo;

	@FXML
	private ImageView cashewLogo;

	@FXML
	private ImageView lomentLogo;

	@FXML
	ProgressIndicator signInProgress;

	@FXML
	ProgressIndicator signUpProgress;

	@FXML
	ProgressIndicator loginProgress;

	@FXML
	ProgressIndicator forgotPasswordProgress;

	@FXML
	ProgressIndicator addCashewIdProgress;

	@FXML
	private StackPane loginLeftPane;

	@FXML
	private Pane registerLeftPane;

	@FXML
	private Tab SignUpTab;
	@FXML
	private Tab SignInTab;

	@FXML
	private Hyperlink hyperlink;
	@FXML
	private Hyperlink hyperlinkForgotPassword;

	ArrayList<CountryModal> arraylist = null;

	String uName = "";
	String pWord = "";

	String emailID = "";
	String cEmailID = "";
	String cashewID = "";
	String pCode = "";
	String mobile = "";
	String cCode = "";
	String cpassword = "";
	String country = "";

	private double xOffset = 0;
	private double yOffset = 0;
	boolean cashewIdFromDb=true;
	boolean isMousePressed = false;
	String rightImage = FXMLExampleController.class.getResource(
			AppConfiguration.getBackgroundImagePath() + "background2.png")
			.toExternalForm();

	// String emailHintString =
	// "This Email address will be used to Login and to contact you.111111";

	// String cashewIdHintString =
	// "This ID can be used to Chat with others. Share as needed.Aqua ID can be 5 to 25 characters with only one dot(.) or underscore (_) allowed.";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (email != null) {
			setLanguageComponents();
			setNameListener();
			setPhoneNumberListener();
			setEmailListener();
			setCemailListener();
			setPasswordListener();
			setCpasswordListener();
			setCashewIdListener();
			getCountryCodes();
			setCountryCodes();
			setPromoCodeListener();

		
			
		}

		if (loginPassword != null && loginUserNameLabel != null) {
			DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
			LomentDataModel lomentData = headerMapper.getLomentData();
			if (lomentData != null) {
				loginUserNameLabel.setText(lomentData.getUsername()
						.toUpperCase());
			}
		}
	/*	loginPassword.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {							
							labelHidden(ForgotPasswordLabel);
						}
					}
				});*/
		
		if (loginCashewLogo != null) {
			loginLeftPane.setStyle("-fx-background-image: url(\"" + rightImage
					+ "\");");
			loginCashewLogo.setImage(new Image(FXMLExampleController.class
					.getResourceAsStream(AppConfiguration.getAppLogoPath()
							+ "cashew90.png")));
			loginProgress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			loginProgress.setVisible(false);
			loginProgress.setStyle("-fx-accent:"
					+ AppConfiguration.getProgressColor());

			StackPane.setAlignment(loginCashewLogo, Pos.CENTER_LEFT);//
		}
		if (cashewLogo != null) {
			registerLeftPane.setStyle("-fx-background-image: url(\""
					+ rightImage + "\");");
			cashewLogo.setImage(new Image(FXMLExampleController.class
					.getResourceAsStream(AppConfiguration.getAppLogoPath()
							+ "cashew90.png")));
		}
		if (lomentLogo != null) {
			lomentLogo.setImage(new Image(FXMLExampleController.class
					.getResourceAsStream(AppConfiguration.getAppLogoPath()
							+ "loment.png")));
		}

		if (signInProgress != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (AppConfiguration.type == AppConfiguration.SAFECOM_MESSENGER) {
						SignUpTab.getTabPane().getTabs().remove(SignUpTab);
						SignInTab.setText("");
						SignInTab
								.setStyle("-fx-border-color:#D8CABF; -fx-background-color: #D8CABF; ");
						signinButton.requestFocus();
					} else {
						hyperlink.setVisible(false);
					}

				}
			});

			hyperlink.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					open("https://registeraccount.safecom.agency/");
				}
			});							
			signInProgress
					.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			signInProgress.setVisible(false);
			signInProgress.setStyle("-fx-accent:"
					+ AppConfiguration.getProgressColor());

			signUpProgress
					.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			signUpProgress.setVisible(false);
			signUpProgress.setStyle("-fx-accent:"
					+ AppConfiguration.getProgressColor());

			forgotPasswordProgress
					.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			forgotPasswordProgress.setVisible(false);
			forgotPasswordProgress.setStyle("-fx-accent:"
					+ AppConfiguration.getProgressColor());

			addCashewIdProgress
					.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			addCashewIdProgress.setVisible(false);
			addCashewIdProgress.setStyle("-fx-accent:"
					+ AppConfiguration.getProgressColor());

			LomentDataModel lomentData = null;
			AccountsModel accountData = null;
			try {
				DBLomentDataMapper headerMapper = DBLomentDataMapper
						.getInstance();
				lomentData = headerMapper.getLomentData();

				DBAccountsMapper accountsMapper = DBAccountsMapper
						.getInstance();
				accountData = accountsMapper.getAccount();

				if (lomentData != null && lomentData.getLomentId() != null
						&& lomentData.getPassword() != null
						&& !lomentData.getLomentId().equalsIgnoreCase("")) {
					if (accountData == null
							|| accountData.getCashewnutId() == null
							|| accountData.getCashewnutId().trim().equals("")) {
						registerTabPane.setVisible(false);
						addAccountMenuButton.setVisible(false);
						accountsButton.setVisible(false);
						newCashewIdTextField.setVisible(true);
						newCashewIdTextField.setFocusTraversable(true);
						addAccount.setVisible(true);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setToolbarComponents();
		setStrings();
	}

	private void setToolbarComponents() {
		borderPane.setStyle("-fx-border-width:4pt; -fx-border-color:"
				+ AppConfiguration.getBorderColour() + ";");
		toolBar.setStyle("-fx-background-color:"
				+ AppConfiguration.getBorderColour() + ";");

		double r = 10.0;
		maximizeImageView.setImage(new Image(FXMLExampleController.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "maximize.png")));

		minimizeImageView.setImage(new Image(FXMLExampleController.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "minimize.png")));

		closeImageView.setImage(new Image(FXMLExampleController.class
				.getResourceAsStream(AppConfiguration.getIconPath()
						+ "close.png")));

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_PREF_SIZE);

		toolBar.getItems().add(1, spacer);

		closeButton.setShape(new Circle(r));
		closeButton.setMinSize(2 * r, 2 * r);
		closeButton.setMaxSize(2 * r, 2 * r);
		closeImageView.getStyleClass().add("closeButtonCss");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				close();
			}
		});

		maximizeButton.setShape(new Circle(r));
		maximizeButton.setMinSize(2 * r, 2 * r);
		maximizeButton.setMaxSize(2 * r, 2 * r);
		maximizeButton.getStyleClass().add("closeButtonCss");
		maximizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (!Platform.isFxApplicationThread()) // Ensure on correct
				// thread else
				// hangs X under Unbuntu
				{
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							_maximize();
						}
					});
				} else {
					_maximize();
				}
			}
		});

		minimizeButton.setShape(new Circle(r));
		minimizeButton.setMinSize(2 * r, 2 * r);
		minimizeButton.setMaxSize(2 * r, 2 * r);
		minimizeButton.getStyleClass().add("closeButtonCss");
		minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				minimize();
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

		toolBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				toolBar.getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

	private void setLanguageComponents() {

		if (languageComboBox != null) {
			ObservableList<String> options = AppConfiguration.getLanguage();
			languageComboBox.setItems(options);
			languageComboBox.setValue(AppConfiguration.appConfString.english);
			languageComboBox.getStyleClass().add("comboBoxCellCss");

			// languageComboBox.setOnMouseClicked(new EventHandler<MouseEvent>()
			// {
			// @Override
			// public void handle(MouseEvent t) {
			// try {
			// String language = (String) languageComboBox
			// .getSelectionModel().getSelectedItem();
			// AppConfiguration.onLocaleChange(language);
			// setStrings();
			// saveLanguage(language);
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// });

			languageComboBox.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener() {
						@Override
						public void changed(ObservableValue ov, Object t,
								Object t1) {
							try {
								String language = (String) t1;
								AppConfiguration.onLocaleChange(language.trim());
								setStrings();
								saveLanguage(language.trim());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

		}

	}

	private void setStrings() {
		if (SignInTab != null)
			SignInTab.setText(" " + AppConfiguration.appConfString.sign_in1);

		if (username != null)
			username.setPromptText(AppConfiguration.appConfString.et_username1);

		if (password != null)
			password.setPromptText(AppConfiguration.appConfString.et_password);

		if (hyperlinkForgotPassword != null)
			hyperlinkForgotPassword
					.setText(AppConfiguration.appConfString.forgot_password);

		if (hyperlink != null)
			hyperlink.setText(AppConfiguration.appConfString.create_an_account);

		if (signinButton != null)
			signinButton.setText(AppConfiguration.appConfString.btn_sign1);

		if (SignUpTab != null)
			SignUpTab.setText(AppConfiguration.appConfString.btn_signup);

		if (name != null)
			name.setPromptText(AppConfiguration.appConfString.et_name);
		if (errorMailLabel != null) {
			labelVisible(errorMailLabel, AppConfiguration.appConfString.lomentid_hint_string, "info");
		}

		if (cashewidErrorLabel != null) {
			labelVisible(cashewidErrorLabel, AppConfiguration.appConfString.cashewid_hint_string, "info");
		}

		if (email != null)
			email.setPromptText(AppConfiguration.appConfString.et_email1);

		if (confirmEmail != null)
			confirmEmail.setPromptText(AppConfiguration.appConfString.confirm_email);

		if (cashewId != null)
			cashewId.setPromptText("Cashew ID");

		if (promoCode != null)
			promoCode.setPromptText("Promo Code");

		if (countryCode != null)
			countryCode
					.setPromptText(AppConfiguration.appConfString.country_code);

		if (phoneNumber != null)
			phoneNumber.setPromptText(AppConfiguration.appConfString.et_mobile);

		if (passWord != null)
			passWord.setPromptText(AppConfiguration.appConfString.et_password);

		if (cPassword != null)
			cPassword
					.setPromptText(AppConfiguration.appConfString.et_password_confirm);

		if (signupButton != null)
			signupButton.setText(AppConfiguration.appConfString.btn_signup1);

		if (addAccountMenuButton != null)
			addAccountMenuButton
					.setPromptText(AppConfiguration.appConfString.et_select_cashewnut_id);

		if (accountsText != null)
			accountsText
					.setText(AppConfiguration.appConfString.profile_app_name);

		if (accountsText1 != null)
			accountsText1
					.setText(AppConfiguration.appConfString.messenger_id_usage1);

		if (accountsButton != null)
			accountsButton.setText(AppConfiguration.appConfString.btn_add_acc);

		if (newCashewIdTextField != null)
			newCashewIdTextField
					.setText(AppConfiguration.appConfString.create_new_id);

		if (textCashew != null)
			textCashew.setText(AppConfiguration.appConfString.Page1Title);

		if (textCashewComments != null)
			textCashewComments
					.setText(AppConfiguration.appConfString.Page1Message);

		if (appName != null) {
			appName.setText(AppConfiguration.appConfString.Page1Title);
			StackPane.setAlignment(appName, Pos.CENTER_LEFT);//
		}

		if (languageComboBox != null)
			languageComboBox
					.setPromptText(AppConfiguration.appConfString.english);

		if (label != null)
			label.setText(AppConfiguration.appConfString.conversation_label);

		if (loginButton != null)
			loginButton.setText(AppConfiguration.appConfString.login);
		if (loginPassword != null)
			loginPassword
					.setPromptText(AppConfiguration.appConfString.et_password);
		if (remember != null)
			remember.setText(AppConfiguration.appConfString.remember_password);
		if (forgetPasswordLogin != null)
		{
			forgetPasswordLogin
					.setText(AppConfiguration.appConfString.forgot_password);
		forgotPasswordProgress.setVisible(false);
		forgotPasswordProgress
		.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		forgotPasswordProgress.setVisible(false);
		forgotPasswordProgress.setStyle("-fx-accent:"
		+ AppConfiguration.getProgressColor());
		forgetPasswordLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("forgot password");
				forgotPassword();
			}
		});
	}
	}

	public void saveLanguage(String language) {
		try {
			SettingsStore settingsMapper = new SettingsStore();
			SettingsModel settingsModel = settingsMapper.getSettingsData();

			if (settingsModel != null) {
				settingsModel.setLanguage(language);
			} else {
				settingsModel = new SettingsModel();

				int priority_status = 3;
				int expiry_status = -1;
				int ack_status = -1;
				int autores_status = -1;
				int restricted_status = -1;
				int playsound_status = -1;
				int expiry_mins = -1;
				String autores_message = "";
				int hibernation_status = -1;
				int login_status = 1;

				settingsModel.setPriority(priority_status);
				settingsModel.setExpiryStatus(expiry_status);
				settingsModel.setExpiryMinuts(expiry_mins);
				settingsModel.setAcknowledge(ack_status);
				settingsModel.setRestricted(restricted_status);

				settingsModel.setAutoResponseStatus(autores_status);
				settingsModel.setAutoResponseMessage(autores_message);

				settingsModel.setLanguage(language);
				settingsModel.setHibernationTime(hibernation_status);
				settingsModel.setRememberStatus(login_status);
				settingsModel.setPlaySound(playsound_status);
				settingsModel.setSignature("");
				settingsModel.setFontFamily("");
			}

			settingsMapper.saveSettingsData(settingsModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		final Stage stage = (Stage) toolBar.getScene().getWindow();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage.fireEvent(new WindowEvent(stage,
						WindowEvent.WINDOW_CLOSE_REQUEST));
				System.exit(0);
			}
		});

	}

	public void minimize() {

		if (!Platform.isFxApplicationThread()) // Ensure on correct thread else
		// hangs X under Unbuntu
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					_minimize();
				}
			});
		} else {
			_minimize();
		}
	}

	private void _minimize() {
		Stage stage = (Stage) toolBar.getScene().getWindow();
		stage.setIconified(true);
	}

	// ObservableList<Screen> screens = Screen.getScreensForRectangle(
	// primaryStage.getX(), primaryStage.getY(), 1, 1);
	// screen = Screen.getScreensForRectangle(0, 0, 1, 1).get(0);
	private boolean isMaximize = false;
	private Rectangle2D backupWindowBounds;
	boolean isMac = false;

	private void _maximize() {
		Stage stage = (Stage) toolBar.getScene().getWindow();
		final double stageY = isMac ? stage.getY() - 22 : stage.getY(); // TODO
		// Workaround
		// for
		// RT-13980
		final Screen screen = Screen.getScreensForRectangle(stage.getX(),
				stageY, 1, 1).get(0);
		Rectangle2D bounds = screen.getVisualBounds();
		if (bounds.getMinX() == stage.getX() && bounds.getMinY() == stageY
				&& bounds.getWidth() == stage.getWidth()
				&& bounds.getHeight() == stage.getHeight()) {
			if (backupWindowBounds != null) {
				stage.setX(backupWindowBounds.getMinX());
				stage.setY(backupWindowBounds.getMinY());
				stage.setWidth(backupWindowBounds.getWidth());
				stage.setHeight(backupWindowBounds.getHeight());
				isMaximize = false;
				// btnMaximize.setGraphic(imgMaximize);
			}
		} else {
			backupWindowBounds = new Rectangle2D(stage.getX(), stage.getY(),
					stage.getWidth(), stage.getHeight());
			final double newStageY = isMac ? screen.getVisualBounds().getMinY() + 22
					: screen.getVisualBounds().getMinY(); // TODO Workaround for
			// RT-13980
			stage.setX(screen.getVisualBounds().getMinX());
			stage.setY(newStageY);
			stage.setWidth(screen.getVisualBounds().getWidth());
			stage.setHeight(screen.getVisualBounds().getHeight());
			isMaximize = true;
			// btnMaximize.setGraphic(imgOriSize);
		}
	}

	// ==================================================================================
	private void setCountryCodes() {
		ObservableList<CountryModal> myComboBoxData = FXCollections
				.observableArrayList(arraylist);
		countryCode.setItems(myComboBoxData);

		// Define rendering of the list of values in ComboBox drop down.
		countryCode
				.setCellFactory(new Callback<ListView<CountryModal>, ListCell<CountryModal>>() {

					public ListCell<CountryModal> call(
							ListView<CountryModal> comboBox) {
						return new ListCell<CountryModal>() {
							@Override
							protected void updateItem(CountryModal item,
									boolean empty) {
								super.updateItem(item, empty);

								if (item == null || empty) {
									setText(null);
								} else {
									setText(item.getCountryName() + "  "
											+ item.getCountryCode());
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

	private void setEmailListener() {
		email.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				// labelHidden(errorMailLabel);
				labelVisible(errorMailLabel,
						AppConfiguration.appConfString.lomentid_hint_string,
						"info");
			}
		});

		email.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							emailCheck();
							// labelHidden(errorMailLabel);
							labelVisible(
									errorMailLabel,
									AppConfiguration.appConfString.lomentid_hint_string,
									"info");
						}
					}
				});

		email.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					emailCheck();
				}
				if (ke.getCode() == KeyCode.DOWN) {
					confirmEmail.requestFocus();
					emailCheck();
				}
			}
		});
	}

	private void setCemailListener() {
		confirmEmail.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				// labelHidden(errorMailLabel);
				labelVisible(errorMailLabel,
						AppConfiguration.appConfString.lomentid_hint_string,
						"info");
			}
		});

		confirmEmail.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							emailCheck();
							// labelHidden(errorMailLabel);
							labelVisible(
									errorMailLabel,
									AppConfiguration.appConfString.lomentid_hint_string,
									"info");
						}
					}
				});

		confirmEmail.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					email.requestFocus();
					confirmEmailCheck();
				}
				if (ke.getCode() == KeyCode.DOWN) {
					passWord.requestFocus();
					confirmEmailCheck();
				}
			}
		});
	}

	private void setPasswordListener() {
		passWord.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				labelHidden(errorPasswordLabel);
				labelHidden(ForgotPasswordLabel);
			}
		});

		passWord.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							confirmEmailCheck();						
							labelHidden(errorPasswordLabel);
							if(ForgotPasswordLabel!=null)
							{
								labelHidden(ForgotPasswordLabel);
							}
							
						}
					}
				});

		passWord.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					confirmEmail.requestFocus();
					passwordCheck();
				}
				if (ke.getCode() == KeyCode.DOWN) {
					cPassword.requestFocus();
					passwordCheck();
				}
			}
		});
	}

	private void setCpasswordListener() {
		cPassword.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				labelHidden(errorPasswordLabel);
			}
		});

		cPassword.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							passwordCheck();
							labelHidden(errorPasswordLabel);
						}
					}
				});

		cPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					passWord.requestFocus();
					cPasswordCheck();
				}
				if (ke.getCode() == KeyCode.DOWN) {
					cashewId.requestFocus();
					cPasswordCheck();
				}
			}
		});
	}

	private void setCashewIdListener() {
		cashewId.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				// labelHidden(cashewidErrorLabel);
				labelVisible(cashewidErrorLabel,
						AppConfiguration.appConfString.cashewid_hint_string,
						"info");
			}
		});

		cashewId.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							cPasswordCheck();
							// labelHidden(cashewidErrorLabel);
							labelVisible(
									cashewidErrorLabel,
									AppConfiguration.appConfString.cashewid_hint_string,
									"info");
						}
					}
				});

		cashewId.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					cPassword.requestFocus();
					cashewIdCheck();
				}
				if (ke.getCode() == KeyCode.DOWN) {
					phoneNumber.requestFocus();
					cashewIdCheck();
				}
			}
		});
	}

	private void setPromoCodeListener() {
		promoCode.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				labelHidden(promoErrorLabel);
			}
		});

		promoCode.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							// signupButton.requestFocus();
							phoneNumberCheck();
							labelHidden(promoErrorLabel);
						}
					}
				});

		promoCode.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					phoneNumber.requestFocus();
					promoCodeCheck();
				}
				if (ke.getCode() == KeyCode.DOWN) {
					signupButton.requestFocus();
					promoCodeCheck();
				}
			}
		});
	}

	private void setPhoneNumberListener() {
		phoneNumber.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					char ch = phoneNumber.getText().charAt(oldValue.intValue());
					// System.out.println("Length:" + oldValue + " "
					// + newValue + " " + ch);
					if (!(ch >= '0' && ch <= '9')) {
						phoneNumber.setText(phoneNumber.getText().substring(0,
								phoneNumber.getText().length() - 1));
					}
					labelHidden(errorPhonenumberLabel);
				}
			}
		});

		phoneNumber.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							labelHidden(errorPhonenumberLabel);
						}
					}
				});

		phoneNumber.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.UP) {
					cashewId.requestFocus();
					phoneNumberCheck();
				}

				if (ke.getCode() == KeyCode.DOWN) {
					promoCode.requestFocus();
					phoneNumberCheck();

				}
			}
		});
	}

	private void setNameListener() {
		name.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				labelHidden(errorNameLabel);
			}
		});
		name.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.DOWN) {
					email.requestFocus();
					nameCheck();
				}
			}
		});
		name.addEventFilter(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

					public void handle(MouseEvent event) {
						if (!event.isSecondaryButtonDown()) {
							labelHidden(errorNameLabel);
						}
					}
				});
	}

	private boolean nameCheck() {
		if (name.getText() == null || name.getText().trim().length() < 1) {
			labelVisible(errorNameLabel,
					AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		} else if (name.getText().trim().length() < 2) {
			labelVisible(errorNameLabel,
					AppConfiguration.appConfString.signup_username_min_2chars,
					"error");
			return false;
		}
		return true;
	}

	private boolean phoneNumberCheck() {
		if (phoneNumber.getText() == null
				|| phoneNumber.getText().trim().length() < 1) {
			labelVisible(errorPhonenumberLabel,
					AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		} else if (phoneNumber.getText().trim().length() < 7) {
			labelVisible(errorPhonenumberLabel,
					AppConfiguration.appConfString.signup_mobile_number_7char,
					"error");
			return false;
		}
		return true;
	}

	private boolean promoCodeCheck() {
		if (promoCode.getText() == "") {
			labelVisible(promoErrorLabel, "Enter Promo code", "error");
			return false;
		}
		return true;
	}

	private boolean emailCheck() {
		if (email.getText() == null || email.getText().trim().length() < 1) {
			labelVisible(errorMailLabel,
					AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		} else if (email.getText().trim().length() < 4) {
			labelVisible(errorMailLabel,
					AppConfiguration.appConfString.signup_valid_email, "error");
			return false;
		} else if (!isvalidate(email.getText())) {
			labelVisible(errorMailLabel,
					AppConfiguration.appConfString.signup_valid_email, "error");
			return false;
		}
		return true;
	}

	private boolean confirmEmailCheck() {
		if (confirmEmail.getText() == null) {
			labelVisible(errorMailLabel, "Emails are not equal", "error");
			return false;
		} else if (!isvalidate(confirmEmail.getText())) {
			labelVisible(errorMailLabel,
					AppConfiguration.appConfString.signup_valid_email, "error");
			return false;
		} else if (!email.getText().trim()
				.equals(confirmEmail.getText().trim())) {
			labelVisible(errorMailLabel, "Emails are not equal", "error");
			return false;
		}
		return true;
	}

	private boolean countryCodeCheck() {
		CountryModal model = countryCode.getSelectionModel().getSelectedItem();
		if (model == null) {
			labelVisible(errorPhonenumberLabel,
					AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		}
		return true;
	}

	private boolean passwordCheck() {
		if (passWord.getText() == null
				|| passWord.getText().trim().length() < 1) {
			labelVisible(errorPasswordLabel,
					AppConfiguration.appConfString.phone_number_check, "error");
			return false;
		} else if (passWord.getText().trim().length() < 4) {
			labelVisible(errorPasswordLabel,
					AppConfiguration.appConfString.signup_password_min_4char,
					"error");
			return false;
		}
		return true;
	}

	private boolean cPasswordCheck() {
		if (cPassword.getText() == null) {
			labelVisible(
					errorPasswordLabel,
					AppConfiguration.appConfString.error_passwords_are_not_equal,
					"error");
			return false;
		} else if (!passWord.getText().trim()
				.equals(cPassword.getText().trim())) {
			labelVisible(
					errorPasswordLabel,
					AppConfiguration.appConfString.error_passwords_are_not_equal,
					"error");
			return false;
		}
		return true;
	}

	private boolean cashewIdCheck() {
		if (cashewId.getText() == null) {
			labelVisible(cashewidErrorLabel, "Enter Cashew ID", "error");
			return false;
		} else if (cashewId == null || cashewId.getText().trim().equals("")
				|| cashewId.getText().trim().length() < 5) {
			labelVisible(cashewidErrorLabel,
					AppConfiguration.appConfString.valid_cashewnut_id, "error");
			return false;
		} else if (!validate(cashewId.getText())) {
			labelVisible(cashewidErrorLabel,
					AppConfiguration.appConfString.valid_cashewnut_id, "error");
			return false;
		}
		return true;
	}

	public void labelVisible(Label label, String text, String type) {
		label.getStyleClass().clear();
		label.setWrapText(true);
		label.getStyleClass().add("visible");
		label.getStyleClass().add(type);
		label.setText(text);
	}

	public void labelHidden(Label label) {
		label.setText("");
		label.getStyleClass().add("hidden");
	}

	// ====================================================================================
	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof Button) {
			Button clickedBtn = (Button) source;
			String id = clickedBtn.getId();
			if (id != null) {
				if (signinButton != null
						&& id.equalsIgnoreCase(signinButton.getId())) {
					labelHidden(signLabel);
					signInAccount();
					return;
				}
				if (signupButton != null
						&& id.equalsIgnoreCase(signupButton.getId())) {
					signUpAccount();
					return;
				}
				if (accountsButton != null
						&& id.equalsIgnoreCase(accountsButton.getId())) {
					final String cashewnutId = getSelectedCashewID();
					cashewIdFromDb=false;
					createcashewId(cashewnutId);
				}
				if (newCashewIdTextField != null
						&& id.equalsIgnoreCase(newCashewIdTextField.getId())) {

					String body = AppConfiguration.appConfString.messenger_id_is
							+ "\n\n"
							+ AppConfiguration.appConfString.messenger_id_is1;

					addAccount.setOpacity(0.5);
					createNewCashewId(body);
					addAccount.setOpacity(1);
				}
				if (loginButton != null
						&& id.equalsIgnoreCase(loginButton.getId())) {					
					login();
				}
				
			}
			return;
		}

		// if (source instanceof ComboBox) {
		// ComboBox clickedBtn = (ComboBox) source;
		// String id = clickedBtn.getId();
		// if (id != null && id.equalsIgnoreCase(addAccountMenuButton.getId()))
		// {
		// String selectedCashewID = (String) addAccountMenuButton
		// .getSelectionModel().getSelectedItem();
		// if (selectedCashewID
		// .trim()
		// .equals(AppConfiguration.AppConfString.account_create_cashew_id)) {
		// newCashewIdTextField.setVisible(true);
		// newCashewIdTextField.setFocusTraversable(true);
		// } else {
		// newCashewIdTextField.setVisible(false);
		// }
		// }
		// }

	}

	public boolean isvalidate(final String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}

	private FXMLExampleController FXMLExampleController = this;

	public void signInAccount() {

		if (signInProgress.isVisible() || signUpProgress.isVisible()) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.login_please_wait, "error");
		}

		uName = username.getText();
		pWord = password.getText();

		if (uName.trim().equals("") && pWord.trim().equals("")) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.signin_enter_lomentid
							+ " and "
							+ AppConfiguration.appConfString.et_password,
					"error");
			return;
		}

		if (uName.trim().equals("")) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.signin_enter_lomentid,
					"error");
			return;
		} else if (!isvalidate(uName)) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.login_enter_password,
					"error");
			return;
		}

		if (pWord.trim().equals("")) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.login_enter_password,
					"error");
			return;
		} else if (pWord.length() < 4) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.login_enter_password,
					"error");
			return;
		}

		NetworkConnection connection = new NetworkConnection();
		signInProgress.setVisible(true);
		connection.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Boolean isConnected = (Boolean) t.getSource().getValue();
				if (isConnected) {
					final SignInService service = new SignInService();
					signInProgress.setVisible(true);

					new Thread() {
						public void run() {
							String response = service.getResponse(uName, pWord);
							signInAccountResponse(response);
						}
					}.start();
				} else {
					signInProgress.setVisible(false);
					labelVisible(
							signLabel,
							AppConfiguration.appConfString.sender_network_unavailable,
							"error");
				}
			}
		});
		connection.start();
	}

	public void signInAccountResponse(final String response) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// System.out.println(response);
				// System.out.println("Linked cashew ids call response.....");
				if (response != null && response.trim().equals("-1")) {
					// fail
					labelVisible(signLabel,
							AppConfiguration.appConfString.signin_login_fail,
							"error");
					signInProgress.setVisible(false);
					return;
				}
				if (response != null && response.trim().equals("-2")) {
					// fail
					labelVisible(
							signLabel,
							AppConfiguration.appConfString.safecom_registration_link,
							"error");
					signInProgress.setVisible(false);
					return;
				}
				if (response != null && response.trim().equals("-3")) {
					// fail
					signInProgress.setVisible(false);
					return;
				}
				try {
					if (response != null && !response.trim().equals("")) {
						ArrayList<String> thisDevice = JsonUtility
								.getCashewnutIdForThisDevice(response);
						ArrayList<String> otherDevice = JsonUtility
								.getCashewnutIdForOtherDevice(response);

						// addAccountMenuButton.getItems().add(AppConfiguration.AppConfString.account_create_cashew_id);
						// newCashewIdTextField.setVisible(false);

						boolean idsExists = false;
						for (String temp : thisDevice) {
							if (temp.trim().length() > 2) {
								addAccountMenuButton.getItems().add(temp);
								idsExists = true;
							}
						}
						for (String temp : otherDevice) {
							if (temp.trim().length() > 2) {
								addAccountMenuButton.getItems().add(temp);
								idsExists = true;
							}
						}
						if (!idsExists) {
							registerTabPane.setVisible(false);
							// System.out.println("0 cashew device accounts ");
							addAccountMenuButton.setVisible(false);
							accountsButton.setVisible(false);
							newCashewIdTextField.setVisible(true);
							newCashewIdTextField.setFocusTraversable(true);
							addAccount.setVisible(true);
						} else {
							registerTabPane.setVisible(false);
							addAccount.setVisible(true);
						}
					} else {
						registerTabPane.setVisible(false);
						// System.out.println("0 cashew device accounts ");
						addAccountMenuButton.setVisible(false);
						accountsButton.setVisible(false);
						newCashewIdTextField.setVisible(true);
						newCashewIdTextField.setFocusTraversable(true);
						addAccount.setVisible(true);
					}
					signInProgress.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void signUpAccount() {
		if (signUpProgress.isVisible()) {
			labelVisible(errorMailLabel,
					AppConfiguration.appConfString.login_please_wait, "error");
		}
		uName = name.getText();
		pWord = passWord.getText();
		emailID = email.getText();

		mobile = phoneNumber.getText();
		cpassword = cPassword.getText();
		cEmailID = confirmEmail.getText();
		cashewID = cashewId.getText().toLowerCase();
		pCode = promoCode.getText().trim();

		//country = countryCode.getCursor().toString();
		boolean isvaliedName = nameCheck();
		boolean isvaliedPhone = phoneNumberCheck();
		boolean isvaliedEmail = emailCheck();
		boolean isvaliedCEmail = confirmEmailCheck();
		boolean isvaliedCashewId = cashewIdCheck();
		boolean isvaliedPass = passwordCheck();
		boolean isvaliedCPass = cPasswordCheck();
		boolean isvaliedcCode = countryCodeCheck();
		boolean isvaliedPromoCode = promoCodeCheck();

		if (!isvaliedName || !isvaliedPhone || !isvaliedEmail
				|| !isvaliedCEmail || !isvaliedPass || !isvaliedCPass
				|| !isvaliedCashewId || !isvaliedcCode || !isvaliedPromoCode) {
			return;
		}

		CountryModal model = countryCode.getSelectionModel().getSelectedItem();
		String code = model.getCountryCode();
		final String countryAbbr = model.getCountryAbbr();

		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		mobile = code + mobile;
		NetworkConnection connection = new NetworkConnection();
		signUpProgress.setVisible(true);
		connection.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Boolean isConnected = (Boolean) t.getSource().getValue();
				if (isConnected) {
					final SignUpService1 service = new SignUpService1();
					service.setUseName(uName);
					service.setPassword(pWord);
					service.setEmail(emailID);
					service.setMobile(mobile);
					service.setCountryCode(countryAbbr);
					service.setCashewId(cashewID);
					service.setPromoCode(pCode);

					signUpProgress.setVisible(true);
					new Thread() {
						public void run() {
							String response = service.signupResponse();
							signupResponse(response);
						}
					}.start();
				} else {
					signUpProgress.setVisible(false);
					labelVisible(
							promoErrorLabel,
							AppConfiguration.appConfString.sender_network_unavailable,
							"error");
				}
			}
		});
		connection.start();
	}

	private void signupResponse(final String response) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (response != null && response.trim().equals("0")) {
					try {
						Stage stage = (Stage) promoCode.getScene().getWindow();
						runAnotherApp(ConversationView.class, stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (response != null && response.trim().equals("-2")) {
					signUpProgress.setVisible(false);
					labelVisible(promoErrorLabel,
							AppConfiguration.appConfString.signup_fail
									+ "Invalid Promo code !!", "error");
				}
				if (response != null && response.trim().equals("-3")) {
					signUpProgress.setVisible(false);
					labelVisible(
							errorMailLabel,
							AppConfiguration.appConfString.signup_fail
									+ AppConfiguration.appConfString.signup_lomentid_already_exists,
							"error");
				}
				if (response != null && response.trim().equals("-4")) {
					signUpProgress.setVisible(false);
					labelVisible(
							errorMailLabel,
							AppConfiguration.appConfString.signup_fail
									+ AppConfiguration.appConfString.signin_enter_valid_email,
							"error");
				}
				if (response != null && response.trim().equals("-5")) {
					signUpProgress.setVisible(false);
					labelVisible(
							cashewidErrorLabel,
							AppConfiguration.appConfString.signup_fail
									+ AppConfiguration.appConfString.msg_cashewnut_id_exist,
							"error");
				}
				if (response != null && response.trim().equals("-1")) {
					// invalid response
				}
				signUpProgress.setVisible(false);
			}
		});
	}

	public void createcashewId(String cashewnutId) {
		labelVisible(errorAddCashewIdLabel, "  ", "error");
		if (signUpProgress.isVisible()) {
			labelVisible(errorAddCashewIdLabel,
					AppConfiguration.appConfString.login_please_wait, "error");
		}
		if(cashewIdFromDb)
		{
		if (cashewnutId == null || cashewnutId.trim().equals("")
				|| cashewnutId.trim().length() <5) {
			labelVisible(errorAddCashewIdLabel,
					AppConfiguration.appConfString.valid_cashewnut_id, "error");
			return;
		}
		}
		if (!validate(cashewnutId)) {
			labelVisible(errorAddCashewIdLabel,
					AppConfiguration.appConfString.valid_cashewnut_id, "error");
			return;
		}

		NetworkConnection connection = new NetworkConnection();

		connection.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						Boolean isConnected = (Boolean) t.getSource()
								.getValue();
						if (isConnected) {
							addCashewIdProgress.setVisible(true);
							final AddcashewnutIDForm service = new AddcashewnutIDForm();
							service.setCashewId(cashewnutId);
							addCashewIdProgress.setVisible(true);
							new Thread() {
								public void run() {
									int response = service.cashewIDResponse();
									getCashewIDResponse(response);
								}
							}.start();
						} else {
							addCashewIdProgress.setVisible(false);
							labelVisible(
									errorAddCashewIdLabel,
									AppConfiguration.appConfString.sender_network_unavailable,
									"error");
						}
					}
				});
		connection.start();
	}

	private void createNewCashewId(String body) {
		labelVisible(errorAddCashewIdLabel, "  ", "error");
		Stage owner = (Stage) newCashewIdTextField.getScene().getWindow();
		final TextField passwordTextField = new TextField("");
		final Stage dialogStage = new Stage();

		dialogStage.initOwner(owner);

		Button ok = new Button(AppConfiguration.appConfString.dlg_ok);
		Button cancel = new Button(AppConfiguration.appConfString.dlg_cancel);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				String val = passwordTextField.getText();
				if (!val.trim().equals("")) {
					//createcashewId(val);
					createcashewId(val.toLowerCase());
					dialogStage.close();
				}
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				dialogStage.close();
			}
		});

		HBox hbox = HBoxBuilder.create()
				.children(ok, new Label("      "), cancel)
				.alignment(Pos.CENTER).padding(new Insets(10)).build();

		VBox vbox = VBoxBuilder
				.create()
				.children(createText(body), new Label("      "),
						passwordTextField, new Label("      "), hbox)
				.alignment(Pos.CENTER).padding(new Insets(10)).build();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);

		GridPane gridpane = new GridPane();
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(10);
		gridpane.setVgap(10);

		final String cssDefault = "-fx-border-color: #A8A8A8;\n"
				+ "-fx-border-width: 6;\n";

		gridpane.add(vbox, 1, 1, 10, 10);
		gridpane.setStyle(cssDefault);
		dialogStage.setScene(new Scene(gridpane));
		dialogStage.showAndWait();
	}

	private Text createText(String string) {
		Text text = new Text(string);
		text.setWrappingWidth(200);
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setStyle("-fx-font-size: 14; -fx-base: #b6e7c9;");
		return text;
	}

	private void getCashewIDResponse(final Integer response) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (response == 0) {
					try {
						addCashewIdProgress.setVisible(false);
						Stage stage = (Stage) addAccountMenuButton.getScene()
								.getWindow();
						runAnotherApp(ConversationView.class, stage);
						return;
					} catch (Exception ex) {
						Logger.getLogger(FXMLExampleController.class.getName())
								.log(Level.SEVERE, null, ex);
					}
				}
				if (response == 1) {
					// cashew id already exist
					labelVisible(
							errorAddCashewIdLabel,
							AppConfiguration.appConfString.msg_cashewnut_id_exist,
							"error");
				}
				if (response == -1) {
					// device id not exist
				}
				if (response == -2) {
					// exception
				}
				addCashewIdProgress.setVisible(false);
			}
		});
	}

	private String getSelectedCashewID() {
		String cashewnutId = "";
		if (addAccountMenuButton.isVisible()) {
			cashewnutId = (String) addAccountMenuButton.getSelectionModel()
					.getSelectedItem();
			if (cashewnutId == null) {
				return "";
			}
			if (!cashewnutId.trim().equals(
					AppConfiguration.appConfString.et_select_cashewnut_id)
					&& !cashewnutId
							.trim()
							.equals(AppConfiguration.appConfString.account_create_cashew_id)) {
				return cashewnutId;
			}
		}
		return cashewnutId;
	}

	public boolean validate(final String cashewnutID) {
		String CASHEWNUTID_PATTERN = "^([A-Z]|[a-z]|"
				+ "[0-9])+(\\.|_)?([A-Z]|[a-z]|[0-9])+$";
		Pattern pattern = Pattern.compile(CASHEWNUTID_PATTERN);
		Matcher matcher = pattern.matcher(cashewnutID);
		return matcher.matches();

	}

	// Splash screen timer
	private void login() {
		labelVisible(loginLabel, "", "error");
		String pass = loginPassword.getText();
		if (pass.trim().equals("")) {
			labelVisible(loginLabel,
					AppConfiguration.appConfString.login_enter_password,
					"error");
			return;
		}

		try {
			DBLomentDataMapper headerMapper = DBLomentDataMapper.getInstance();
			LomentDataModel lomentData = headerMapper.getLomentData();
			if (lomentData != null && lomentData.getLomentId() != null
					&& lomentData.getPassword() != null) {

				if (lomentData.getPassword().trim().equals(pass)) {

					DBAccountsMapper accountsMapper = DBAccountsMapper
							.getInstance();
					AccountsModel accountData = accountsMapper.getAccount();

					if (accountData == null
							|| accountData.getCashewnutId() == null
							|| accountData.getCashewnutId().trim().equals("")) {
						Stage stage = (Stage) loginPassword.getScene()
								.getWindow();
						runAnotherApp(RegisterFormJfx.class, stage);

					} else {
						if (remember.isSelected()) {
							saveLoginStatus();
						}
						Stage stage = (Stage) loginPassword.getScene()
								.getWindow();
						runAnotherApp(ConversationView.class, stage);
					}
				} else {
					// labelVisible(loginLabel, "Incorrect Password !!");
					checkPassword(lomentData.getLomentId(), pass);
				}
			}
		} catch (Exception e) {
		}
	}

	public void checkPassword(final String uName, final String pWord) {
		if (pWord.trim().equals("")) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.login_enter_password,
					"error");
			return;
		} else if (pWord.length() < 4) {
			labelVisible(signLabel,
					AppConfiguration.appConfString.login_enter_valid_password,
					"error");
			return;
		}

		NetworkConnection connection = new NetworkConnection();
		loginProgress.setVisible(true);
		connection.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Boolean isConnected = (Boolean) t.getSource().getValue();
				if (isConnected) {
					final ValidatePasswordTask service = new ValidatePasswordTask(
							uName, pWord, true);
					loginProgress.setVisible(true);

					new Thread() {
						public void run() {
							String response = service.execute();
							final int val = service.onPostExecute(response);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									if (val == 0 || val == 1) {
										try {
											if (remember.isSelected()) {
												saveLoginStatus();
											}
											Stage stage = (Stage) loginPassword
													.getScene().getWindow();
											runAnotherApp(
													ConversationView.class,
													stage);
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}
									if (val == 2) {
										labelVisible(
												loginLabel,
												AppConfiguration.appConfString.login_incorrect_password,
												"error");
									}
									loginProgress.setVisible(false);
								}
							});
						}
					}.start();
				} else {
					loginProgress.setVisible(false);
					labelVisible(
							loginLabel,
							AppConfiguration.appConfString.sender_network_unavailable,
							"error");
				}
			}
		});
		connection.start();
	}

	public void saveLoginStatus() {
		try {
			SettingsStore settingsMapper = new SettingsStore();
			SettingsModel settingsModel = settingsMapper.getSettingsData();

			if (settingsModel != null) {
				settingsModel.setRememberStatus(1);
			} else {
				settingsModel = new SettingsModel();

				int priority_status = 3;
				int expiry_status = -1;
				int ack_status = -1;
				int autores_status = -1;
				int restricted_status = -1;
				int playsound_status = -1;
				int expiry_mins = -1;
				String autores_message = "";
				int hibernation_status = -1;
				int login_status = 1;
				String language = "";

				settingsModel.setPriority(priority_status);
				settingsModel.setExpiryStatus(expiry_status);
				settingsModel.setExpiryMinuts(expiry_mins);
				settingsModel.setAcknowledge(ack_status);
				settingsModel.setRestricted(restricted_status);

				settingsModel.setAutoResponseStatus(autores_status);
				settingsModel.setAutoResponseMessage(autores_message);

				settingsModel.setLanguage(language);
				settingsModel.setHibernationTime(hibernation_status);
				settingsModel.setRememberStatus(login_status);
				settingsModel.setPlaySound(playsound_status);
				settingsModel.setSignature("");
				settingsModel.setFontFamily("");
			}

			settingsMapper.saveSettingsData(settingsModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runAnotherApp(Class<? extends Application> anotherAppClass,
			Stage anotherStage) throws Exception {

		Application app2 = anotherAppClass.newInstance();
		// System.out.println(app2 + " " + anotherStage);
		app2.start(anotherStage);
	}

	public void open(String url) {
		new Thread() {
			public void run() {
				try {
					URI u = new URI(url);
					java.awt.Desktop.getDesktop().browse(u);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void forgotPassword()
	{
		NetworkConnection connection = new NetworkConnection();
		forgotPasswordProgress.setVisible(true);
		connection.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Boolean isConnected = (Boolean) t.getSource().getValue();
				if (isConnected) {
					
					

					new Thread() {
						public void run() {
							
							try {
								DataConnection conn = DataConnection.getInstance();
								final String primaryEmail = ProfileStore.getInstance()
										.getProfileData().getEmail();
								String url =  AppConfiguration.getSthithiApi()+"/user/"
										+ primaryEmail + "/account/recovery";								
								String response = conn.sendGetRequest(url,null);
								forgotPasswordProgress.setVisible(false);
								JSONObject responseJson = new JSONObject(response);
								Integer status = (Integer) responseJson.get("status");
								if (status == 0) {
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											labelVisible(
													ForgotPasswordLabel,
													AppConfiguration.appConfString.forgotPasswordLinkSent+primaryEmail,
													"error");
											
										}
									});
								}
								else
								{
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											labelVisible(
													ForgotPasswordLabel,
													AppConfiguration.appConfString.sender_network_unavailable,
													"error");
											
										}
									});
									
								}
								System.out.println("response is" + response);
							
							} catch (Exception e) {
								
								e.printStackTrace();
							}
							
							
						}
					}.start();
				} else {
					loginProgress.setVisible(false);			
					labelVisible(
							loginLabel,
							AppConfiguration.appConfString.sender_network_unavailable,
							"error");
				}
			}
		});
		connection.start();
	}
	
}
