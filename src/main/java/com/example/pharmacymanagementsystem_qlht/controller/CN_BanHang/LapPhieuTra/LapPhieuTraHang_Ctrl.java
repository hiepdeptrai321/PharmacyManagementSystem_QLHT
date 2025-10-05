package com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuTra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LapPhieuTraHang_Ctrl extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_BanHang/LapPhieuTra/LapPhieuTraHang_GUI.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
