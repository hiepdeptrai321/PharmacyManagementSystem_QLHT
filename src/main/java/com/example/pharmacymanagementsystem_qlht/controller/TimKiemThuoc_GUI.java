package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class TimKiemThuoc_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(TimKiemThuocController.class.getResource("/com/example/pharmacymanagementsystem_qlht/TKThuoc_GUI.fxml"));
            TimKiemThuocController timkiemThuocController = new TimKiemThuocController();
            loader.setController(timkiemThuocController);
            Pane root = loader.load();
            Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/pharmacymanagementsystem_qlht/TimKiemThuoc.css").toExternalForm()
        );
            primaryStage.setTitle("Tim Kiem");
            primaryStage.setScene(scene);
            primaryStage.show();
    }
}
