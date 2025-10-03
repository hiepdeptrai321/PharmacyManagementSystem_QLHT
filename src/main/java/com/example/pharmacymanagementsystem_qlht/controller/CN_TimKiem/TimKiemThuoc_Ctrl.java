package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class TimKiemThuoc_Ctrl extends Application {
    @FXML
    private ComboBox<String> cboTimKiem;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc_Ctrl.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/TimKiemThuoc.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên thuốc",
                "Theo hoạt chất",
                "Theo SDK_GPNK",
                "Theo nhóm dược lý"
        );
    }
}