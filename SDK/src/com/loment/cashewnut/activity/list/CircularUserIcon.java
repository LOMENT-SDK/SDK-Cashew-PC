/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import com.loment.cashewnut.CashewnutActivity;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

// java 8 code.
public class CircularUserIcon extends StackPane {

	int i = 1;

	public CircularUserIcon(String data) {
		Text text = createText(data);
		Circle circle = encircle(text);

		this.getChildren().addAll(circle, text);
		this.setPadding(new Insets(2));
	}

	private Text createText(String string) {
		Text text = new Text(string);
		text.setBoundsType(TextBoundsType.VISUAL);
		// text.setStyle(
		// "-fx-font-family: Amble CN;"
		// + " -fx-font-style: bold;"
		// + " -fx-font-size: 20;"
		// );

		text.setStyle("-fx-font-size: 20; -fx-base: #b6e7c9;");

		return text;
	}

	private Circle encircle(Text text) {
		Color color = Color.ORCHID;

		int index = CashewnutActivity.colourIndex;
		switch (index) {
		case 1:
			color = Color.ORCHID;
			break;
		case 2:
			color = Color.MEDIUMPURPLE;
			break;
		case 3:
			color = Color.LIGHTPINK;
			break;
		case 4:
			color = Color.TOMATO;
			break;
		case 5:
			color = Color.TAN;
			break;
		case 6:
			color = Color.TURQUOISE;
		}
		if (CashewnutActivity.colourIndex++ > 6) {
			CashewnutActivity.colourIndex = 1;
		}

		Circle circle = new Circle();
		circle.setFill(color);
		final double PADDING = 8;
		circle.setRadius(getWidth(text) / 2 + PADDING);

		return circle;
	}

	private double getWidth(Text text) {
		new Scene(new Group(text));
		text.applyCss();

		return text.getLayoutBounds().getWidth();
	}
}
