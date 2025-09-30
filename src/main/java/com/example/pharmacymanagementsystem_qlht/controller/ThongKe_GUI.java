package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ThongKe_GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ThongKeBanHang_ctrl.class.getResource("/com/example/pharmacymanagementsystem_qlht/ThongKeBanHang.fxml"));
        ThongKeBanHang_ctrl  thongkeBanHang_ctrl = new ThongKeBanHang_ctrl();
        loader.setController(thongkeBanHang_ctrl);
        Pane root = loader.load();
        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/pharmacymanagementsystem_qlht/ThongKeBanHang.css").toExternalForm()
       );
        primaryStage.setTitle("ThongKe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
