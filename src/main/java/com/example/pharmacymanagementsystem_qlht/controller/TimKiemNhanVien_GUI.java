package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class TimKiemNhanVien_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(TimKiemThuocController.class.getResource("/com/example/pharmacymanagementsystem_qlht/TKNhanVien_GUI.fxml"));
        TimKiemNhanVien_ctrl timkiemNhanVien_ctrl = new TimKiemNhanVien_ctrl();
        loader.setController(timkiemNhanVien_ctrl);
        Pane root = loader.load();
        Scene scene = new Scene(root, 800, 440);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/pharmacymanagementsystem_qlht/TimKiemNhanVien.css").toExternalForm()
        );
        primaryStage.setTitle("Tim Kiem");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
