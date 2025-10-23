package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhanVien;

import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SuaTaiKhoan_Ctrl{
    public TextField txtTaiKhoan;
    public TextField txtMatKhau;
    public NhanVien nhanVien;
    public boolean isSaved = false;

    public void initialize(NhanVien nhanVien){
        loadTaiKhoan(nhanVien);
        this.nhanVien=nhanVien;
    }

    public void loadTaiKhoan(NhanVien nhanVien){
        txtTaiKhoan.setText(nhanVien.getTaiKhoan());
        txtMatKhau.setText(nhanVien.getMatKhau());
    }

    public NhanVien getUpdatedNhanVien() {
        return nhanVien;
    }

    public void btnLuu(ActionEvent actionEvent) {
        isSaved = true;
        nhanVien.setTaiKhoan(txtTaiKhoan.getText());
        nhanVien.setMatKhau(txtMatKhau.getText());
//        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
        dong();
    }

    private void dong(){
        Stage stage = (Stage) txtTaiKhoan.getScene().getWindow();
        stage.close();
    }

    public void btnHuy(ActionEvent actionEvent) {
        isSaved = false;
        dong();
    }
}
