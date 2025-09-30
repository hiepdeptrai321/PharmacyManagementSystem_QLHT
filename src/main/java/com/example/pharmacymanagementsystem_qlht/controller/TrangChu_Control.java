package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class TrangChu_Control extends Application {
    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/TrangChu_GUI.fxml"));
        primaryStage.setTitle("Hệ thống quản lý hiệu thuốc");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logo.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
