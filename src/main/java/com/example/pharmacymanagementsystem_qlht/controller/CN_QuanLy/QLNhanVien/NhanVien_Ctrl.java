package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhanVien;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NhanVien_Ctrl extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLNhanVien/QuanLyNhanVien_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
