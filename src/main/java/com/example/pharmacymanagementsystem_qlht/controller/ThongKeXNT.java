package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ThongKeXNT extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ThongKeXNT_ctrl.class.getResource("/com/example/pharmacymanagementsystem_qlht/ThongKeXNT.fxml"));
        ThongKeXNT_ctrl thongkeXNT_ctrl = new ThongKeXNT_ctrl();
        loader.setController(thongkeXNT_ctrl);
        Pane root = loader.load();
        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add(
               getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm()
        );
        primaryStage.setTitle("ThongKe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
