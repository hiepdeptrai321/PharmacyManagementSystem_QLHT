package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKeHang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ThemKe_Ctrl extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLKeHang/ThemKe.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThemNhaCungCap.css" + "").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
