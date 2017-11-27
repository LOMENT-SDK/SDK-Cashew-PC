package com.loment.cashewnut.activity.view.javafx;

import com.loment.cashewnut.AppConfiguration;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterFormJfx extends Application {

	@FXML
	private AnchorPane loginScreen;
	Scene scene;
	Stage stage;

	/* loginScreen */

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource(
				"fxml_register.fxml"));
		try {
			if (!stage.isShowing()) {
				stage.initStyle(StageStyle.UNDECORATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		stage.setTitle(AppConfiguration.appConfString.app_name);
		scene = new Scene(root, 960, 700);
		stage.setScene(scene);
		stage.getIcons().add(
				new Image(RegisterFormJfx.class
						.getResourceAsStream(AppConfiguration.getAppLogoPath()
								+ "cashew48.png")));
		scene.getStylesheets()
				.add(RegisterFormJfx.class.getResource("Style.css")
						.toExternalForm());
		stage.show();

		ResizeListener listener = new ResizeListener();
		scene.setOnMouseMoved(listener);
		scene.setOnMousePressed(listener);
		scene.setOnMouseDragged(listener);
	}

	class ResizeListener implements EventHandler<MouseEvent> {

		double dx;
		double dy;
		double deltaX;
		double deltaY;
		double border = 10;
		boolean moveH;
		boolean moveV;
		boolean resizeH = false;
		boolean resizeV = false;

		@Override
		public void handle(MouseEvent t) {
			if (MouseEvent.MOUSE_MOVED.equals(t.getEventType())) {
				if (t.getX() < border && t.getY() < border) {
					scene.setCursor(Cursor.NW_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = true;
					moveV = true;
				} else if (t.getX() < border
						&& t.getY() > scene.getHeight() - border) {
					scene.setCursor(Cursor.SW_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = true;
					moveV = false;
				} else if (t.getX() > scene.getWidth() - border
						&& t.getY() < border) {
					scene.setCursor(Cursor.NE_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = false;
					moveV = true;
				} else if (t.getX() > scene.getWidth() - border
						&& t.getY() > scene.getHeight() - border) {
					scene.setCursor(Cursor.SE_RESIZE);
					resizeH = true;
					resizeV = true;
					moveH = false;
					moveV = false;
				} else if (t.getX() < border
						|| t.getX() > scene.getWidth() - border) {
					scene.setCursor(Cursor.E_RESIZE);
					resizeH = true;
					resizeV = false;
					moveH = (t.getX() < border);
					moveV = false;
				} else if (t.getY() < border
						|| t.getY() > scene.getHeight() - border) {
					scene.setCursor(Cursor.N_RESIZE);
					resizeH = false;
					resizeV = true;
					moveH = false;
					moveV = (t.getY() < border);
				} else {
					scene.setCursor(Cursor.DEFAULT);
					resizeH = false;
					resizeV = false;
					moveH = false;
					moveV = false;
				}
			} else if (MouseEvent.MOUSE_PRESSED.equals(t.getEventType())) {
				dx = stage.getWidth() - t.getX();
				dy = stage.getHeight() - t.getY();
			} else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {
				if (resizeH && resizeV) {
					if (stage.getWidth() <= 600) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							if (t.getX() < 0) {// if new > old, it's permitted
								stage.setWidth(deltaX + stage.getWidth());
								stage.setX(t.getScreenX());
							}
						} else {
							if (t.getX() + dx - stage.getWidth() > 0) {
								stage.setWidth(t.getX() + dx);
							}
						}
					} else if (stage.getWidth() > 600) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							stage.setWidth(deltaX + stage.getWidth());
							stage.setX(t.getScreenX());
						} else {
							stage.setWidth(t.getX() + dx);
						}
					}

					if (stage.getHeight() <= 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							if (t.getY() < 0) {// if new > old, it's permitted
								stage.setHeight(deltaY + stage.getHeight());
								stage.setY(t.getScreenY());
							}
						} else {
							if (t.getY() + dy - stage.getHeight() > 0) {
								stage.setHeight(t.getY() + dy);
							}
						}
					} else if (stage.getHeight() > 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							stage.setHeight(deltaY + stage.getHeight());
							stage.setY(t.getScreenY());
						} else {
							stage.setHeight(t.getY() + dy);
						}
					}
				} else if (resizeH) {
					if (stage.getWidth() <= 600) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							if (t.getX() < 0) {// if new > old, it's permitted
								stage.setWidth(deltaX + stage.getWidth());
								stage.setX(t.getScreenX());
							}
						} else {
							if (t.getX() + dx - stage.getWidth() > 0) {
								stage.setWidth(t.getX() + dx);
							}
						}
					} else if (stage.getWidth() > 600) {
						if (moveH) {
							deltaX = stage.getX() - t.getScreenX();
							stage.setWidth(deltaX + stage.getWidth());
							stage.setX(t.getScreenX());
						} else {
							stage.setWidth(t.getX() + dx);
						}
					}
				} else if (resizeV) {
					if (stage.getHeight() <= 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							if (t.getY() < 0) {// if new > old, it's permitted
								stage.setHeight(deltaY + stage.getHeight());
								stage.setY(t.getScreenY());
							}
						} else {
							if (t.getY() + dy - stage.getHeight() > 0) {
								stage.setHeight(t.getY() + dy);
							}
						}
					} else if (stage.getHeight() > 500) {
						if (moveV) {
							deltaY = stage.getY() - t.getScreenY();
							stage.setHeight(deltaY + stage.getHeight());
							stage.setY(t.getScreenY());
						} else {
							stage.setHeight(t.getY() + dy);
						}
					}
				}
			}
		}
	}
}
