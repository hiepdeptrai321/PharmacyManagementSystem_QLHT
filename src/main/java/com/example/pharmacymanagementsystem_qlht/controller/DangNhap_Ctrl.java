package com.example.pharmacymanagementsystem_qlht.controller;

import com.example.pharmacymanagementsystem_qlht.dao.NhanVien_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class DangNhap_Ctrl extends Application {
    public CheckBox checkDangNhap;
    public Label lbhotline;
    public TextField tfTaiKhoan;
    public TextField tfMatKhau;
    public Button btnAnMK;
    public PasswordField tfMatKhauAn;
    public Button btnDangNhap;
    public static NhanVien user;

    private final Preferences prefs = Preferences.userNodeForPackage(DangNhap_Ctrl.class);

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/DangNhap_GUI.fxml"));
        Scene scene = new Scene(root, 488, 649);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/DangNhap.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public void initialize() {
        // Sync password fields
        tfMatKhauAn.textProperty().addListener((obs, oldText, newText) -> {
            if (!tfMatKhau.isVisible()) return;
            tfMatKhau.setText(newText);
        });
        tfMatKhau.textProperty().addListener((obs, oldText, newText) -> {
            if (!tfMatKhau.isVisible()) return;
            tfMatKhauAn.setText(newText);
        });

        // Load remembered credentials
        String savedUser = prefs.get("username", "");
        String savedPass = prefs.get("password", "");
        if (!savedUser.isEmpty() && !savedPass.isEmpty()) {
            tfTaiKhoan.setText(savedUser);
            tfMatKhauAn.setText(savedPass);
            checkDangNhap.setSelected(true);
        }
    }

    public void anmatkhau(ActionEvent actionEvent) {
        boolean isVisible = tfMatKhau.isVisible();
        if (isVisible) {
            tfMatKhauAn.setText(tfMatKhau.getText());
            tfMatKhauAn.setVisible(true);
            tfMatKhau.setVisible(false);
            btnAnMK.setText("\uD83D\uDC41\uFE0F\u200D\uD83D\uDDE8\uFE0F");
        } else {
            tfMatKhau.setText(tfMatKhauAn.getText());
            tfMatKhau.setVisible(true);
            tfMatKhauAn.setVisible(false);
            btnAnMK.setText("👁");
        }
    }

    public void btnDangNhapClick() {
        String username = tfTaiKhoan.getText();
        String password = tfMatKhau.isVisible() ? tfMatKhau.getText() : tfMatKhauAn.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            return;
        }

        NhanVien_Dao dao = new NhanVien_Dao();
        NhanVien nv = new NhanVien_Dao().selectByTKVaMK(username,password);

        if (nv != null) {
            // Remember credentials if checked
            if (checkDangNhap.isSelected()) {
                prefs.put("username", username);
                prefs.put("password", password);
            } else {
                prefs.remove("username");
                prefs.remove("password");
            }
            user = nv;
            String role = nv.getVaiTro();
            try {
                Stage newStage = new Stage();
                Parent root;
                if ("Quản lý".equalsIgnoreCase(role)) {
                    root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CuaSoChinh_QuanLy_GUI.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CuaSoChinh_NhanVien_GUI.fxml"));
                }
                Scene scene = new Scene(root);
                newStage.setScene(scene);
                newStage.show();
                Stage currentStage = (Stage) btnDangNhap.getScene().getWindow();
                currentStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}