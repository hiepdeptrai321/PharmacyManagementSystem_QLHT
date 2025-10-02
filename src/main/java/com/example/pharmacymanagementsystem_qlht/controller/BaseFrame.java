package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class BaseFrame extends BorderPane {
    public BaseFrame() {
        MenuBar navBar = new MenuBar();
        // Add menus/items to navBar as needed
        this.setTop(navBar);
        // Center will be set by child GUIs
    }
}

