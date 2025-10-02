package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DangNhap_GUI extends BaseFrame {
    public Button btn1;

    public DangNhap_GUI() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/DangNhap.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Đăng Nhập");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}

