package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhanVien;

import com.example.pharmacymanagementsystem_qlht.dao.NhanVien_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ThemTaiKhoan_Ctrl{
    public TextField txtTaiKhoan;
    public TextField txtMatKhau;
    public NhanVien nhanVien;
    public boolean isSaved = false;
    public NhanVien_Dao nhanVien_Dao = new NhanVien_Dao();
    List<NhanVien> listNhanVien = new ArrayList<>();

    public void initialize(){
    }

    public NhanVien getUpdatedNhanVien() {
        return nhanVien;
    }

    public void btnLuu(ActionEvent actionEvent) {
        if(!kiemTraTrungTaiKhoan()){
            return;
        }
        isSaved = true;
        nhanVien.setTaiKhoan(txtTaiKhoan.getText());
        nhanVien.setMatKhau(txtMatKhau.getText());
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

    public boolean kiemTraTrungTaiKhoan() {
        listNhanVien = nhanVien_Dao.selectAll();
        String taiKhoanNhap = txtTaiKhoan.getText();
        if(taiKhoanNhap.isEmpty()){
            System.out.println("Tài khoản không được để trống!");
            return false;
        }else if(txtMatKhau.getText().isEmpty()){
            System.out.println("Mật khẩu không được để trống!");
            return false;
        }else if(txtMatKhau.getText().length()<6){
            System.out.println("Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }else{
            for (NhanVien nv : listNhanVien) {
                if (nv.getTaiKhoan().equals(taiKhoanNhap)) {
                    System.out.println("Tài khoản đã tồn tại!");
                    return false;
                }
            }
            return true;
        }
    }
}
