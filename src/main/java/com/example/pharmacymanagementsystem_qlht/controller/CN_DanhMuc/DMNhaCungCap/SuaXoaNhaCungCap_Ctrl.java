package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.dao.NhaCungCap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Optional;

public class SuaXoaNhaCungCap_Ctrl {
    public TextField txtTen;
    public TextField txtDiaChi;
    public TextField txtSDT;
    public TextField txtEmail;
    public TextField txtGPKD_SDK;
    public TextField txtTenCongTy;
    public TextField txtMaSoThue;
    public TextArea txtGhiChu;
    private NhaCungCap ncc;

    public void initialize(NhaCungCap ncc) {
        this.ncc = ncc;
        ThemDuLieuThuoc(ncc);
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
        Dialog<ButtonType> dialogMain = new Dialog<>();
        dialogMain.setTitle("Xác nhận");
        DialogPane dialogTemp = dialogMain.getDialogPane();
        dialogTemp.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
        dialogTemp.getButtonTypes().addAll(javafx.scene.control.ButtonType.CANCEL);
        dialogTemp.setContentText("Bạn có muốn cập nhật nhà cung cấp này không?");
        Optional<ButtonType> result = dialogMain.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ncc.setMaNCC(ncc.getMaNCC());
            ncc.setTenNCC(txtTen.getText());
            ncc.setDiaChi(txtDiaChi.getText());
            ncc.setSDT(txtSDT.getText());
            ncc.setEmail(txtEmail.getText());
            ncc.setGPKD(txtGPKD_SDK.getText());
            ncc.setTenCongTy(txtTenCongTy.getText());
            ncc.setMSThue(txtMaSoThue.getText());
            ncc.setGhiChu(txtGhiChu.getText());

            NhaCungCap_Dao ncc_dao = new NhaCungCap_Dao();
            if(ncc_dao.update(ncc)){
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Thông báo");
                DialogPane dialogPane = dialog.getDialogPane();
                dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
                dialogPane.setContentText("Cập nhật nhà cung cấp thành công!");
                dialog.showAndWait();
            } else {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Thông báo");
                DialogPane dialogPane = dialog.getDialogPane();
                dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
                dialogPane.setContentText("Cập nhật nhà cung cấp thất bại!");
                dialog.showAndWait();
            }
        }
    }

    public void XoaNCC(MouseEvent mouseEvent) {
        NhaCungCap_Dao ncc_dao = new NhaCungCap_Dao();
        ncc_dao.deleteById(ncc);
    }

    public void dong() {
        Stage stage = (Stage) txtTen.getScene().getWindow();
        stage.close();
    }

    public void btnXoa(KeyEvent keyEvent) {
        NhaCungCap_Dao ncc_dao = new NhaCungCap_Dao();
        ncc_dao.deleteById(ncc);
        dong();
    }

    public void btnHuy(MouseEvent keyEvent) {
        dong();
    }
}
