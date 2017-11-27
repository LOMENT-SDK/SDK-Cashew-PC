/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class FigurePane extends BorderPane {

    private final Label bubble = new Label();

    public FigurePane() {
        BorderPane bubblePane = new BorderPane();
        bubblePane.setCenter(bubble);

        bubble.setWrapText(true);
        bubble.setFocusTraversable(false);
        bubble.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane temp = new BorderPane();
        temp.setBottom(bubblePane);

        super.setCenter(temp);
        super.setMaxWidth(300);

        bubble.skinProperty().addListener(new ChangeListener<Skin<?>>() {
            @Override
            public void changed(
                    ObservableValue<? extends Skin<?>> ov, Skin<?> t, Skin<?> t1) {
                if (t1 != null && t1.getNode() instanceof Region) {
                    Region r = (Region) t1.getNode();
                    ScrollBar scrollBarv = (ScrollBar) r.lookup(".scroll-bar:vertical");
                    if (scrollBarv != null) {
                        scrollBarv.setDisable(true);
                        scrollBarv.setOpacity(0.0);
                    }

//                    r.getChildrenUnmodifiable().stream().
//                            filter(n -> n instanceof Region).
//                            map(n -> (Region) n).
//                            forEach(n -> n.setBackground(Background.EMPTY));
//
//                    r.getChildrenUnmodifiable().stream().
//                            filter(n -> n instanceof Control).
//                            map(n -> (Control) n).
//                            forEach(c -> c.skinProperty().addListener(this)); // *
                }
            }
        });
    }

    public void setBubbleText(String message) {
        bubble.setText(message);
    }

    public void setBubbleTextColour(String colour) {
        bubble.setStyle("-fx-text-fill: " + colour + ";");
    }
}
