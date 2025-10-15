package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ChiTietPhieuDatHang_Ctrl  {
    @FXML
    private Button btnDong;


    @FXML
    public void initialize() {
        btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
        //btnInHoaDon.setOnAction(e -> inHoaDon());
    }


}
