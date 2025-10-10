package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/ChiTietNhaCungCap_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        btnThoat.setOnAction(e -> anThoat());
    }


    public void setNhaCungCap(NhaCungCap ncc) {
        this.nhaCungCap = ncc;
        hienThiThongTin();
    }

    private void hienThiThongTin() {
        if (nhaCungCap != null) {
            maNCC.setText(nhaCungCap.getMaNCC());
            tenNCC.setText(nhaCungCap.getTenNCC() != null ? nhaCungCap.getTenNCC() : "");
            DiaChi.setText(nhaCungCap.getDiaChi() != null ? nhaCungCap.getDiaChi() : "");
            SDT.setText(nhaCungCap.getSDT() != null ? nhaCungCap.getSDT() : "");
            Email.setText(nhaCungCap.getEmail()!= null ? nhaCungCap.getEmail() : "");
            GPKD.setText(nhaCungCap.getGPKD() != null ? nhaCungCap.getGPKD() : "");
            MST.setText(nhaCungCap.getMSThue() != null ? nhaCungCap.getMSThue() : "");
            TenCongTy.setText(nhaCungCap.getTenCongTy() != null ? nhaCungCap.getTenCongTy() : "");
            GhiChu.setText(nhaCungCap.getGhiChu() != null ? nhaCungCap.getGhiChu() : "");
        }
    }

    @FXML
    private void anThoat() {
        Stage stage = (Stage) btnThoat.getScene().getWindow();
        stage.close();
    }
}
