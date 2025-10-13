package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChiTietNhaCungCap_Ctrl {
    @FXML
    private TextField DiaChi;

    @FXML
    private TextField Email;

    @FXML
    private TextField GPKD;

    @FXML
    private TextArea GhiChu;

    @FXML
    private TextField MST;

    @FXML
    private TextField SDT;

    @FXML
    private TextField TenCongTy;

    @FXML
    private Button btnThoat;

    @FXML
    private TextField maNCC;

    @FXML
    private TextField tenNCC;
    private NhaCungCap nhaCungCap;

    public void hienThiThongTin(NhaCungCap ncc) {
        maNCC.setText(String.valueOf(ncc.getMaNCC()));
        tenNCC.setText(String.valueOf(ncc.getTenNCC()));
        DiaChi.setText(String.valueOf(ncc.getDiaChi()));
        SDT.setText(String.valueOf(ncc.getSDT()));
        Email.setText(String.valueOf(ncc.getEmail()));
        TenCongTy.setText(String.valueOf(ncc.getTenCongTy()));
        MST.setText(String.valueOf(ncc.getMSThue()));
        GPKD.setText(String.valueOf(ncc.getGPKD()));
        GhiChu.setText(String.valueOf(ncc.getGhiChu()));
    }

    @FXML
    private void anThoat() {
        Stage stage = (Stage) btnThoat.getScene().getWindow();
        stage.close();
    }
}
