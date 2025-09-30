package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Demo_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(TimKiemController.class.getResource("/com/example/pharmacymanagementsystem_qlht/TimKiem_GUI.fxml"));
            TimKiemController timkiemController = new TimKiemController();
            loader.setController(timkiemController);
            Pane root = loader.load();
            Scene scene = new Scene(root, 800, 440);
            primaryStage.setTitle("Tim Kiem");
            primaryStage.setScene(scene);
            primaryStage.show();
    }
}
