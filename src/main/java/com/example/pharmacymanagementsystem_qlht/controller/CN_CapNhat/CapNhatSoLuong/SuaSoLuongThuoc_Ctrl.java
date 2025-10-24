package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatSoLuong;

import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.HoatDong_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SP_TheoLo_Dao;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SuaSoLuongThuoc_Ctrl {


    public Button btnLuu;
    public Button btnHuy;
    @FXML
    private TextField tfMaThuoc;
    @FXML
    private TextField tfTenThuoc;
    @FXML
    private TextField tfSoLuongTon;
    @FXML
    private ComboBox<String> cbViTri;
    @FXML
    private ComboBox<String> cbLoaiHang;

    private Thuoc_SP_TheoLo thuoc_sp_theoLo;

    public void setThuoc(Thuoc_SP_TheoLo thuocLo) {
        if (thuocLo == null) return;
        this.thuoc_sp_theoLo = thuocLo;
        Thuoc_SanPham thuoc = thuocLo.getThuoc();
        tfMaThuoc.setText(thuoc.getMaThuoc());
        tfTenThuoc.setText(thuoc.getTenThuoc());
        tfSoLuongTon.setText(String.valueOf(thuocLo.getSoLuongTon()));
        cbViTri.setValue(thuoc.getVitri().getTenKe());
        cbLoaiHang.setValue(thuoc.getLoaiHang().getTenLoaiHang());
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            tfSoLuongTon.requestFocus();
        });
    }

    @FXML
    private void btnLuuClick() {
        if (thuoc_sp_theoLo != null) {
            try {
                int newSoLuong = Integer.parseInt(tfSoLuongTon.getText().trim());
                thuoc_sp_theoLo.setSoLuongTon(newSoLuong);
                Thuoc_SP_TheoLo_Dao tstl_dao = new Thuoc_SP_TheoLo_Dao();
                tstl_dao.update(thuoc_sp_theoLo);
                System.out.println("Cập nhật số lượng thành công!");
                closeWindow();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnHuyClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) tfMaThuoc.getScene().getWindow();
        stage.close();
    }

}
