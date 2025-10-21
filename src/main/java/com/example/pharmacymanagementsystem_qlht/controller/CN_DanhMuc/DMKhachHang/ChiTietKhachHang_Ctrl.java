package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang;

import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChiTietKhachHang_Ctrl extends Application {
    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtNgaySinh;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;
    private KhachHang khachHang;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/SuaXoaKhachHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ChiTietKhachHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void hienThiThongTin(KhachHang kh) {
        if (kh != null) {
            khachHang = kh;
            txtTenKH.setText(kh.getTenKH());
            txtDiaChi.setText(kh.getDiaChi() != null ? kh.getDiaChi() : "");
            txtEmail.setText(kh.getEmail() != null ? kh.getEmail() : "");
            txtSDT.setText(kh.getSdt() != null ? kh.getSdt() : "");
            txtNgaySinh.setValue(kh.getNgaySinh());
        }
    }
}
