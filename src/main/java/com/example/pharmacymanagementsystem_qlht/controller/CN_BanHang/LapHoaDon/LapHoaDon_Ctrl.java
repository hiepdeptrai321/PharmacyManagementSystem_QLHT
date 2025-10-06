package com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapHoaDon;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import java.util.ResourceBundle;


public class LapHoaDon_Ctrl extends Application {
//    @FXML
//    private ToggleButton myToggleButton;

//    @FXML
//    public void initialize(URL location, ResourceBundle resources) {
//        myToggleButton.getStyleClass().add("toggle-switch");
//
//    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_BanHang/LapHoaDon/LapHoaDon_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}