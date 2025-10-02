package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLThuoc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ThemThuoc_Ctrl extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLThuoc/ThemThuoc_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/DangNhap.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
