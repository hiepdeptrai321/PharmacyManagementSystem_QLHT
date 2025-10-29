package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatGia;

import com.example.pharmacymanagementsystem_qlht.dao.DonViTinh_Dao;
import com.example.pharmacymanagementsystem_qlht.model.DonViTinh;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ThemDVT_Ctrl{
    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThem;

    @FXML
    private TextField txtKyHieu;
    @FXML
    private TextField txtTenDVT;

    private DonViTinh_Dao donViTinh_dao = new DonViTinh_Dao();


    @FXML
    public void initialize() {
        btnThem.setOnAction(e -> themDVT());
        btnHuy.setOnAction(e -> btnHuyClick());
    }

    public void btnHuyClick(){
        Stage stage = (Stage) txtTenDVT.getScene().getWindow();
        stage.close();
    }

    @FXML
    void themDVT() {
        // Sinh mã tự động
        String maDVT = donViTinh_dao.generatekeyDonViTinh();
        String tenDVT = txtTenDVT.getText().trim();
        String kyHieu = txtKyHieu.getText().trim();

        if (tenDVT.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập tên kệ hàng!");
            return;
        }

        DonViTinh donViTinh = new DonViTinh(maDVT, tenDVT, kyHieu);
        boolean success = donViTinh_dao.insert(donViTinh);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm đơn vị tính thành công!\nMã: " + maDVT);
            txtTenDVT.clear();
            txtKyHieu.clear();
            dongCuaSo();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm đơn vị tính thất bại!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void dongCuaSo() {
        Stage stage = (Stage) txtTenDVT.getScene().getWindow();
        stage.close();
    }
}

