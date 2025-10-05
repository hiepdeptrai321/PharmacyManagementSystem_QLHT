package com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuDatHang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LapPhieuDatHang_Ctrl extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_BanHang/LapPhieuDat/LapPhieuDatHang_GUI.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
