package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class TimKiemController {

    @FXML
    private Button bt1;
    private boolean isExpanded = false;

    @FXML
    private VBox tknangcao;

    @FXML
    private void morong() {
        isExpanded = !isExpanded;
        tknangcao.setVisible(isExpanded);
        tknangcao.setManaged(isExpanded);

    }
}