package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDoi;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LapPhieuDoiHang_Ctrl implements Initializable {
    @FXML private TextField txtTimHoaDonGoc;
    @FXML private Button btnTim;
    @FXML private TableView<?> tblSanPhamGoc;
    @FXML private TableView<?> tblChiTietDoiHang;
    @FXML private TextField txtMaHoaDonGoc;
    @FXML private DatePicker dpNgayLapPhieu;
    @FXML private Label lblTongTienGoc;
    @FXML private Label lblTongTienDoi;
    @FXML private Label lblVAT;
    @FXML private Label lblTienTraLai;
    @FXML private TextArea txtGhiChu;
    @FXML private Button btnDatHang;
    @FXML private Button btnThanhToan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tblSanPhamGoc != null) {
            tblSanPhamGoc.setPlaceholder(new Label("Chưa có sản phẩm gốc"));
            try { tblSanPhamGoc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
        }
        if (tblChiTietDoiHang != null) {
            tblChiTietDoiHang.setPlaceholder(new Label("Chưa có chi tiết đổi hàng"));
            try { tblChiTietDoiHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
        }

        if (lblTongTienGoc != null) lblTongTienGoc.setText("0 VNĐ");
        if (lblTongTienDoi != null) lblTongTienDoi.setText("0 VNĐ");
        if (lblVAT != null) lblVAT.setText("0 VNĐ");
        if (lblTienTraLai != null) lblTienTraLai.setText("0 VNĐ");

        if (btnTim != null) btnTim.setOnAction(e -> onTim());
        if (btnDatHang != null) btnDatHang.setOnAction(e -> onInPhieu());
        if (btnThanhToan != null) btnThanhToan.setOnAction(e -> onDoiHang());
    }

    private void onTim() {
        System.out.println("Tìm hóa đơn gốc: " + (txtTimHoaDonGoc != null ? txtTimHoaDonGoc.getText() : ""));
    }

    private void onInPhieu() {
        System.out.println("In phiếu đổi clicked");
    }

    private void onDoiHang() {
        System.out.println("Đổi hàng clicked");
    }
}
