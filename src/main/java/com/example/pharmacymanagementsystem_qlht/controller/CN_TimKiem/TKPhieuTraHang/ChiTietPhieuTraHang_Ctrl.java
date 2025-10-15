package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuTraHang;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChiTietPhieuTraHang_Ctrl {

    @FXML
    private Button btnDong;


    @FXML
    public void initialize() {
        btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
    }
}
