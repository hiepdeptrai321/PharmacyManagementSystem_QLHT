package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ChiTietNhaCungCap_Ctrl extends Application {
    @Override
    public void start(javafx.stage.Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/ChiTietNhaCungCap_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
