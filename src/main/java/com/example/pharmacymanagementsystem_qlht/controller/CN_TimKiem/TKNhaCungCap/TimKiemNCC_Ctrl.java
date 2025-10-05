package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class TimKiemNCC_Ctrl extends Application {
    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên nhà cung cấp",
                "Theo email",
                "Theo SDT"

        );
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKNhaCungCap/TKNCC_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
