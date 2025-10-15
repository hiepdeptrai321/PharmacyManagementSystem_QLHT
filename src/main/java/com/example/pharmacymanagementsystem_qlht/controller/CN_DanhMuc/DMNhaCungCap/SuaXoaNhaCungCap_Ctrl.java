package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.dao.NhaCungCap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SuaXoaNhaCungCap_Ctrl {
    public TextField txtTen;
    public TextField txtDiaChi;
    public TextField txtSDT;
    public TextField txtEmail;
    public TextField txtGPKD_SDK;
    public TextField txtTenCongTy;
    public TextField txtMaSoThue;
    public TextArea txtGhiChu;
    private String maNCC;

    public void initialize(NhaCungCap ncc) {
        ThemDuLieuThuoc(ncc);
        maNCC = ncc.getMaNCC();
    }

    private void ThemDuLieuThuoc(NhaCungCap ncc) {
        txtTen.setText(ncc.getTenNCC());
        txtDiaChi.setText(ncc.getDiaChi());
        txtSDT.setText(ncc.getSDT());
        txtEmail.setText(ncc.getEmail());
        txtGPKD_SDK.setText(ncc.getGPKD());
        txtTenCongTy.setText(ncc.getTenCongTy());
        txtMaSoThue.setText(ncc.getMSThue());
        txtGhiChu.setText(ncc.getGhiChu());
    }

    public void CapNhatNCC(MouseEvent mouseEvent) {
        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNCC(maNCC);
        ncc.setTenNCC(txtTen.getText());
        ncc.setDiaChi(txtDiaChi.getText());
        ncc.setSDT(txtSDT.getText());
        ncc.setEmail(txtEmail.getText());
        ncc.setGPKD(txtGPKD_SDK.getText());
        ncc.setTenCongTy(txtTenCongTy.getText());
        ncc.setMSThue(txtMaSoThue.getText());
        ncc.setGhiChu(txtGhiChu.getText());

        NhaCungCap_Dao ncc_dao = new NhaCungCap_Dao();
        ncc_dao.update(ncc);
    }

    public void XoaNCC(MouseEvent mouseEvent) {
        NhaCungCap_Dao ncc_dao = new NhaCungCap_Dao();
        ncc_dao.deleteById(ncc_dao);
    }
}
