package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DangNhap_Ctrl extends Application {
    public CheckBox checkdn;
    public Label lbhotline;
    public TextField tfmk;
    public Button btnanmk;
    public PasswordField tfmkan;
    public Button btndn;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/DangNhap_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/DangNhap.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void anmatkhau(ActionEvent actionEvent) {
        boolean isVisible = tfmk.isVisible();
        if (isVisible) {
            tfmkan.setText(tfmk.getText());
            tfmkan.setVisible(true);
            tfmk.setVisible(false);
            btnanmk.setText("\uD83D\uDC41\uFE0F\u200D\uD83D\uDDE8\uFE0F");
        } else {
            tfmk.setText(tfmkan.getText());
            tfmk.setVisible(true);
            tfmkan.setVisible(false);
            btnanmk.setText("👁");
        }
    }
    private void initialize() {
        // Đồng bộ password field và texfield tfmkan -> tfmk
        tfmkan.textProperty().addListener((obs, oldText, newText) -> {
            if (!tfmk.isVisible()) return;
            tfmk.setText(newText);
        });
        tfmk.textProperty().addListener((obs, oldText, newText) -> {
            if (!tfmk.isVisible()) return;
            tfmkan.setText(newText);
        });
    }
}
