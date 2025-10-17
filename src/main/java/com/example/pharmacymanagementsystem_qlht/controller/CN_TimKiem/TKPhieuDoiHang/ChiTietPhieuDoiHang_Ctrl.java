package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDoiHang;

import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.model.PhieuTraHang;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ChiTietPhieuDoiHang_Ctrl  {
    @FXML
    private Button btnDong;

    @FXML
    private Button btnInPhieuDoi;

    @FXML
    private TableColumn<ChiTietPhieuDoiHang, String> colLyDo;

    @FXML
    private TableColumn<ChiTietPhieuDoiHang, String> colSTT;

    @FXML
    private TableColumn<ChiTietPhieuDoiHang, String> colSoLuong;

    @FXML
    private TableColumn<ChiTietPhieuDoiHang, String> colTenSP;

    @FXML
    private Label lblChietKhauPDoiValue;

    @FXML
    private Label lblGhiChuValue;

    @FXML
    private Label lblMaPhieuDoiValue;

    @FXML
    private Label lblNgayLapValue;

    @FXML
    private Label lblPTTTValue;

    @FXML
    private Label lblSDTKH;

    @FXML
    private Label lblSDTKhachHangValue;

    @FXML
    private Label lblTenKH;

    @FXML
    private Label lblTenKhachHangValue;

    @FXML
    private Label lblTenNV;

    @FXML
    private Label lblTenNhanVienValue;

    @FXML
    private Label lblThueVATValue;

    @FXML
    private Label lblTienConLaiValue;

    @FXML
    private Label lblTienDaThanhToanValue;

    @FXML
    private Label lblTongTienDoiValue;

    @FXML
    private Label lblTongTienPhaiDoiValue;

    @FXML
    private TableView<ChiTietPhieuDoiHang> tblChiTietPhieuDoi;

    private PhieuDoiHang phieuDoiHang;

    @FXML
    public void initialize() {
        btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
    }
    public void setPhieuDoiHang(PhieuDoiHang pDoi) {
        this.phieuDoiHang = pDoi;
        hienThiThongTin();
    }
    private void hienThiThongTin() {
        if (phieuDoiHang != null) {
            lblMaPhieuDoiValue.setText(phieuDoiHang.getMaPD());
            lblNgayLapValue.setText(phieuDoiHang.getNgayLap().toString());
            lblTenNhanVienValue.setText(phieuDoiHang.getNhanVien().getTenNV());
            lblTenKhachHangValue.setText(phieuDoiHang.getKhachHang().getTenKH());
            lblSDTKhachHangValue.setText(phieuDoiHang.getKhachHang().getSdt());
            //lblPTTTValue.setText(phieuDoiHang.get());
            //lblChietKhauPDoiValue.setText(String.valueOf(phieuDoiHang.getChietKhau()) + "%");
            //lblTongTienPhaiDoiValue.setText(String.format("%.2f", phieuDoiHang.tinhTongTienPhaiDoi()));
            //lblTongTienDoiValue.setText(String.format("%.2f", phieuDoiHang.tinhTongTienDoi()));
            //lblTienDaThanhToanValue.setText(String.format("%.2f", phieuDoiHang.getTienDaThanhToan()));
            //lblTienConLaiValue.setText(String.format("%.2f", phieuDoiHang.tinhTienConLai()));
            //lblThueVATValue.setText(String.format("%.2f", phieuDoiHang.tinhThueVAT()));
            lblGhiChuValue.setText(phieuDoiHang.getGhiChu() != null ? phieuDoiHang.getGhiChu() : "");
            //tblChiTietPhieuDoi.setItems(phieuDoiHang.getChiTietPhieuDoiHang());
        }
    }
}

