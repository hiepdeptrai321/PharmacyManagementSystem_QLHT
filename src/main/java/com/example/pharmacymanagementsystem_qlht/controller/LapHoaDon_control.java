package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LapHoaDon_control extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/LapHoaDon_GUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Lập Hóa Đơn");

        stage.setScene(scene);
        stage.show();
    }

}
