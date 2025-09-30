package com.example.pharmacymanagementsystem_qlht.controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class TimKiemNhanVien_ctrl {

    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên nhân viên",
                "Theo email",
                "Theo SDT"

        );
    }
}
