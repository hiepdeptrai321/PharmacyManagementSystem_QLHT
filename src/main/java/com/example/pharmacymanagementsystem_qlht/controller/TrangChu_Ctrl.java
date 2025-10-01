package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TrangChu_Ctrl extends Application {
    public Pane pnlChung;

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

    public void LapHoaDon(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/LapHoaDon_GUI.fxml"));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
