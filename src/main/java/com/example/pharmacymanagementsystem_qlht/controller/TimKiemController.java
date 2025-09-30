package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.control.ComboBox;

public class TimKiemController {
    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên thuốc",
                "Theo hoạt chất",
                "Theo SDK_GPNK",
                "Theo nhóm dược lý"
        );
    }

}