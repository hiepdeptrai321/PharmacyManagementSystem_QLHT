package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDoiHang;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class ChiTietPhieuDoiHang_Ctrl extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDoiHang/ChiTietPhieuDoiHang_GUI.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }
}
