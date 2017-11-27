/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loment.cashewnut.activity.list;

import com.loment.cashewnut.AppConfiguration;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
*
* @author sekhar
*/
public class MediaControl extends BorderPane {

    private MediaPlayer mp;
    private MediaView mediaView;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private HBox mediaBar;
    MediaControl mediaControl = this;

    ImageView imageViewPlay = ConversationsViewRenderer.resize1(AppConfiguration.getIconPath() + "playButton.png", 26);
    ImageView imageViewPause = ConversationsViewRenderer.resize1(AppConfiguration.getIconPath() + "pauseButton.png", 26);
    private Pane mvPane;
    int type;

    @Override
    protected void layoutChildren() {
        if (mediaView != null && getBottom() != null) {
            mediaView.setFitWidth(getWidth());
            mediaView.setFitHeight(getHeight() - getBottom().prefHeight(-1));
        }
        super.layoutChildren();
        if (mediaView != null && getCenter() != null) {
            mediaView.setTranslateX((((Pane) getCenter()).getWidth() - mediaView.prefWidth(-1)) / 2);
            mediaView.setTranslateY((((Pane) getCenter()).getHeight() - mediaView.prefHeight(-1)) / 2);
        }
    }

    @Override
    protected double computeMinWidth(double height) {
        return -1;
    }

    @Override
    protected double computeMinHeight(double width) {
        if (type == 1) {
            return 10;
        } else {
            return 200;
        }

    }

    @Override
    protected double computePrefWidth(double height) {
        return Math.max(mp.getMedia().getWidth(), mediaBar.prefWidth(height));
    }

    @Override
    protected double computePrefHeight(double width) {
        return mp.getMedia().getHeight() + mediaBar.prefHeight(width);
    }

    @Override
    protected double computeMaxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    protected double computeMaxHeight(double width) {
        return Double.MAX_VALUE;
    }

    public void setEndOfMedia() {

    }

    public MediaControl(final MediaPlayer mp, int type) {
        this.mp = mp;
        this.type = type;
        setStyle("-fx-background-color:rgba(0, 0, 0, 0);"); // TODO: Use css file
        mediaView = new MediaView(mp);
        mvPane = new Pane();
        mvPane.getChildren().add(mediaView);
        if (type == 1) {
            setStyle("-fx-background-color: transparant;"); // TODO: Use css file
            //mvPane.setPrefSize(10, 10);
        } else {
            mvPane.setPrefSize(10, 10);
            mvPane.setStyle("-fx-background-color: black;"); // TODO: Use css file
            setCenter(mvPane);
        }

        mediaBar = new HBox(5.0);
        mediaBar.setPadding(new Insets(5, 10, 5, 10));
        mediaBar.setAlignment(Pos.CENTER_LEFT);
        BorderPane.setAlignment(mediaBar, Pos.CENTER);

        final Button playButton = ButtonBuilder.create()
                .minWidth(Control.USE_PREF_SIZE)
                .build();
        //playButton.setText("P"); 
        playButton.setGraphic(imageViewPlay);
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                updateValues();
                Status status = mp.getStatus();
                if (status == Status.UNKNOWN
                        || status == Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == Status.PAUSED
                        || status == Status.READY
                        || status == Status.STOPPED) {
                    // rewind the movie if we're sitting at the end
                    if (atEndOfMedia) {
                        mp.seek(mp.getStartTime());
                        atEndOfMedia = false;
                        playButton.setGraphic(imageViewPlay);
                        //playButton.setText(">");
                        updateValues();
                    }
                    mp.play();
                    playButton.setGraphic(imageViewPause);
                    //playButton.setText("||");
                    MediaControlListner.getInstance().listenForResponse(mp, mediaControl);
                } else {
                    mp.pause();
                }
            }
        });
        mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable,
                    Duration oldValue, Duration newValue) {
                updateValues();
            }
        });
        mp.setOnPlaying(new Runnable() {
            public void run() {

                if (stopRequested) {
                    mp.pause();
                    stopRequested = false;
                } else {
                    playButton.setGraphic(imageViewPause);
                    // playButton.setText("||");
                }
            }
        });
        mp.setOnPaused(new Runnable() {
            public void run() {

                playButton.setGraphic(imageViewPlay);
                // playButton.setText("||");
            }
        });
        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });

        mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        mp.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (!repeat) {
                    playButton.setGraphic(imageViewPlay);
                    //playButton.setText(">");
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            }
        });
        mediaBar.getChildren().add(playButton);

        // Time label
        Label timeLabel = new Label("Time");
        timeLabel.setMinWidth(Control.USE_PREF_SIZE);
        mediaBar.getChildren().add(timeLabel);

        // Time slider
        timeSlider = SliderBuilder.create()
                .minWidth(30)
                .maxWidth(Double.MAX_VALUE)
                .build();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    if (duration != null) {
                        mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                    }
                    updateValues();

                }
            }
        });
        mediaBar.getChildren().add(timeSlider);

        // Play label
        playTime = LabelBuilder.create()
                //.prefWidth(130)
                .minWidth(Control.USE_PREF_SIZE)
                .build();

        mediaBar.getChildren().add(playTime);

        // Volume label
        Label volumeLabel = new Label("Vol");
        volumeLabel.setMinWidth(Control.USE_PREF_SIZE);
        mediaBar.getChildren().add(volumeLabel);

        // Volume slider
        volumeSlider = SliderBuilder.create()
                .prefWidth(70)
                .minWidth(30)
                .maxWidth(Region.USE_PREF_SIZE)
                .build();
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
            }
        });
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (volumeSlider.isValueChanging()) {
                    mp.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
        mediaBar.getChildren().add(volumeSlider);

        setBottom(mediaBar);

    }

    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null && duration != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mp.getVolume() * 100));
                    }
                }
            });
        }
    }

    private String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,
                        durationMinutes, durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",
                        elapsedMinutes, elapsedSeconds);
            }
        }
    }
}
