package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class TimKiemThuoc_Ctrl {
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