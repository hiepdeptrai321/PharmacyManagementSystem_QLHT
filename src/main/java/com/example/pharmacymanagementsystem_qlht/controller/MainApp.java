package com.example.pharmacymanagementsystem_qlht.controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        DangNhap_GUI login = new DangNhap_GUI();
        stage.setScene(new Scene(login, 600, 400));
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void laphoadon(ActionEvent actionEvent) {
        HoaDon_GUI hoadon = new HoaDon_GUI();
        Stage stage = new Stage();
        stage.setScene(new Scene(hoadon, 800, 600));
        stage.setTitle("Hóa Đơn");
        stage.show();
    }
}

