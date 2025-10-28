package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKeHang;

import com.example.pharmacymanagementsystem_qlht.dao.KeHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ThemKe_Ctrl extends Application {
    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThem;

    @FXML
    private TextArea txtMoTa;

    @FXML
    private TextField txtTenKe;

    private KeHang_Dao keHangDao = new KeHang_Dao();


    @FXML
    public void initialize() {
        btnThem.setOnAction(e -> themKe());
        btnHuy.setOnAction(e -> btnHuyClick());
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/ThemKe.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThemNhaCungCap.css" + "").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void btnHuyClick(){
        Stage stage = (Stage) txtTenKe.getScene().getWindow();
        stage.close();
    }

    @FXML
    void themKe() {
        // Sinh mã tự động
        String maKe = keHangDao.generateNewMaKeHang();
        String tenKe = txtTenKe.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (tenKe.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập tên kệ hàng!");
            return;
        }

        KeHang keHang = new KeHang(maKe, tenKe, moTa);
        boolean success = keHangDao.insert(keHang);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm kệ hàng thành công!\nMã: " + maKe);
            txtTenKe.clear();
            txtMoTa.clear();
            dongCuaSo();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm kệ hàng thất bại!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void dongCuaSo() {
        Stage stage = (Stage) txtTenKe.getScene().getWindow();
        stage.close();
    }
    }
