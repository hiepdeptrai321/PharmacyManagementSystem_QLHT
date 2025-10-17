package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChiTietNhaCungCap_Ctrl extends Application {
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
    @Override
    public void start(javafx.stage.Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKNhaCungCap/ChiTietNhaCungCap_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        btnThoat.setOnAction(e -> anThoat());
    }


    public void setNhaCungCap(NhaCungCap ncc) {
        this.nhaCungCap = ncc;
        hienThiThongTin(ncc);
    }

    public void hienThiThongTin(NhaCungCap ncc) {
        if (ncc != null) {
            maNCC.setText(ncc.getMaNCC());
            tenNCC.setText(ncc.getTenNCC() != null ? ncc.getTenNCC() : "");
            DiaChi.setText(ncc.getDiaChi() != null ? ncc.getDiaChi() : "");
            SDT.setText(ncc.getSDT() != null ? ncc.getSDT() : "");
            Email.setText(ncc.getEmail()!= null ? ncc.getEmail() : "");
            GPKD.setText(ncc.getGPKD() != null ? ncc.getGPKD() : "");
            MST.setText(ncc.getMSThue() != null ? ncc.getMSThue() : "");
            TenCongTy.setText(ncc.getTenCongTy() != null ? ncc.getTenCongTy() : "");
            GhiChu.setText(ncc.getGhiChu() != null ? ncc.getGhiChu() : "");
        }
    }

    @FXML
    private void anThoat() {
        Stage stage = (Stage) btnThoat.getScene().getWindow();
        stage.close();
    }
}
