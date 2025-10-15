package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDoiHang;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChiTietPhieuDoiHang_Ctrl  {
    @FXML
    private Button btnDong;

    @FXML
    public void initialize() {
        btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
    }
}
