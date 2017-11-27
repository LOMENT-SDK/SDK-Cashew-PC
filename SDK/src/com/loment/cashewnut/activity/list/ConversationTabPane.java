/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ConversationTabPane extends TabPane {
    public ConversationTabPane(String[] tabNames) {
        this.getStylesheets().add(this.getClass().getResource(
                "tabbed_pan_style.css"
        ).toExternalForm());

        for (String tabName : tabNames) {
            Tab tab = new Tab();
            tab.setText(tabName);
            tab.setClosable(false);
            this.getTabs().add(tab);
        }
    }
}
