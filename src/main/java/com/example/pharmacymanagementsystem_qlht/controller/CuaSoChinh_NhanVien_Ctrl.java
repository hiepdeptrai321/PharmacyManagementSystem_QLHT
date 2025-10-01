package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CuaSoChinh_NhanVien_Ctrl extends Application {
    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CuaSoChinh_NhanVien_GUI.fxml"));
        primaryStage.setTitle("Hệ thống quản lý hiệu thuốc");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logo.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void LapHoaDon(ActionEvent actionEvent) {
    }
}
