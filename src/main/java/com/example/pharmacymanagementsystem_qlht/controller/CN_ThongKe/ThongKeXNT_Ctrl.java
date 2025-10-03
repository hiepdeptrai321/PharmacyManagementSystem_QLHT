package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class ThongKeXNT_Ctrl extends Application {
    @FXML
    private ComboBox<String> btnMQT;

    @FXML
    private ComboBox<String> btnNhomHang;

    @FXML
    private ComboBox<String> btnThoiGian;

    @FXML
    private ComboBox<String> btnXuatFile;

    @FXML
    private ToggleGroup date;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeXNT_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/QuanLyThuoc.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
