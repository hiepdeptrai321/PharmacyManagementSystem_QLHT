package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDatHang;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LapPhieuDatHang_Ctrl implements Initializable {
    @FXML private TextField txtTimSanPham;
    @FXML private Button btnTimSanPham;
    @FXML private TableView<?> tblSanPhamDatHang;
    @FXML private TextField txtMaPhieuDat;
    @FXML private DatePicker dpNgayLapPhieu;
    @FXML private TextField txtTenKhachHang;
    @FXML private Label lblTrangThai;
    @FXML private Label lblTongTienHang;
    @FXML private Label lblVAT;
    @FXML private Label lblTongThanhToan;
    @FXML private TextField txtTienCoc;
    @FXML private Label lblConLai;
    @FXML private TextArea txtGhiChu;
    @FXML private Button btnDatHang;
    @FXML private Button btnThanhToan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tblSanPhamDatHang != null) {
            tblSanPhamDatHang.setPlaceholder(new Label("Chưa có sản phẩm nào"));
            try { tblSanPhamDatHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
        }

        if (dpNgayLapPhieu != null) dpNgayLapPhieu.setValue(java.time.LocalDate.now());

        if (lblTongTienHang != null) lblTongTienHang.setText("0 VNĐ");
        if (lblVAT != null) lblVAT.setText("0 VNĐ");
        if (lblTongThanhToan != null) lblTongThanhToan.setText("0 VNĐ");
        if (lblConLai != null) lblConLai.setText("0 VNĐ");
        if (lblTrangThai != null) lblTrangThai.setText("Chờ thanh toán");

        if (btnTimSanPham != null) btnTimSanPham.setOnAction(e -> onTimSanPham());
        if (btnDatHang != null) btnDatHang.setOnAction(e -> onDatHang());
        if (btnThanhToan != null) btnThanhToan.setOnAction(e -> onThanhToan());
    }

    private void onTimSanPham() {
        System.out.println("Tìm sản phẩm: " + (txtTimSanPham != null ? txtTimSanPham.getText() : ""));
    }

    private void onDatHang() {
        System.out.println("Đặt hàng clicked");
    }

    private void onThanhToan() {
        System.out.println("Thanh toán clicked");
    }
}
