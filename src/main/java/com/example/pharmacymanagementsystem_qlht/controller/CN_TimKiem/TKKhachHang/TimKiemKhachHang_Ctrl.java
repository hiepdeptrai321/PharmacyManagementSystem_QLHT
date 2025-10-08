package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class TimKiemKhachHang_Ctrl extends Application {
    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    private Pane mainPane;

    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên khách hàng",
                "Theo email",
                "Theo SDT"

        );
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/TimKiemNhanVien.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
