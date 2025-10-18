package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietHoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoaDon;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChiTietHoaDon_Ctrl {
    @FXML
    private TableView<ChiTietHoaDon> tblChiTietHoaDon;
    @FXML
    private TableColumn<ChiTietHoaDon, Number> colNSTT;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colNTen;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colNSL;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colNDonVi;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colNDonGia;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colNChietKhau;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colNThanhTien;
    @FXML
    private Label lblMaHoaDonValue;
    @FXML
    private Label lblNgayLapValue;
    @FXML
    private Label lblTenNhanVienValue;
    @FXML
    private Label lblTenKhachHangValue;
    @FXML
    private Label lblSDTKhachHangValue;
    @FXML
    private Label lblGhiChuValue;
    @FXML
    private Label lblTongTienHangValue;
    @FXML
    private Label lblChietKhauHDValue;
    @FXML
    private Label lblThueVATValue;
    @FXML
    private Label lblThanhToanValue;
    @FXML
    private Label lblPTTTValue;
    @FXML
    private Label lblTienKhachDuaValue;
    @FXML
    private Label lblTienThuaValue;
    @FXML
    private Button btnDong;
    @FXML
    private Button btnInHoaDon;

    private HoaDon hoaDon;

    @FXML
    public void initialize() {
        if (btnDong != null) btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
        // btnInHoaDon can be wired later if needed
    }

    public void setHoaDon(HoaDon hd) {
        this.hoaDon = hd;
        hienThiThongTin();
    }

    private void hienThiThongTin() {
        if (hoaDon == null) return;

        lblMaHoaDonValue.setText(hoaDon.getMaHD());
        if (hoaDon.getNgayLap() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            lblNgayLapValue.setText(formatter.format(hoaDon.getNgayLap()));
        } else {
            lblNgayLapValue.setText("");
        }
        if (hoaDon.getMaNV() != null) lblTenNhanVienValue.setText(hoaDon.getMaNV().getTenNV());
        else lblTenNhanVienValue.setText("");

        if (hoaDon.getMaKH() != null) {
            lblTenKhachHangValue.setText(hoaDon.getMaKH().getTenKH());
            lblSDTKhachHangValue.setText(hoaDon.getMaKH().getSdt());
        } else {
            lblTenKhachHangValue.setText("Khách lẻ");
            lblSDTKhachHangValue.setText("");
        }

        lblGhiChuValue.setText(hoaDon.getChiTietHD() != null ? "" : "");

        // Load details from DAO
        List<ChiTietHoaDon> list = new ChiTietHoaDon_Dao().selectByMaHD(hoaDon.getMaHD());

        tblChiTietHoaDon.setItems(FXCollections.observableArrayList(list));

        // STT
        colNSTT.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tblChiTietHoaDon.getItems().indexOf(cellData.getValue()) + 1));

        // Ten san pham via loHang -> thuoc
        colNTen.setCellValueFactory(cel -> {
            Thuoc_SP_TheoLo lo = cel.getValue().getLoHang();
            String ten = "";
            if (lo != null && lo.getThuoc() != null) ten = lo.getThuoc().getTenThuoc();
            return new SimpleStringProperty(ten);
        });

        // So luong
        colNSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        // Don vi (from thuoc.getTenDVTCoBan)
        if (tblChiTietHoaDon.getColumns().size() > 3) {
            TableColumn<ChiTietHoaDon, String> colDonVi = (TableColumn<ChiTietHoaDon, String>) tblChiTietHoaDon.getColumns().get(3);
            colDonVi.setCellValueFactory(cel -> {
                String tenDVT = "";
                if (cel.getValue() != null && cel.getValue().getLoHang() != null && cel.getValue().getLoHang().getThuoc() != null) {
                    tenDVT = cel.getValue().getLoHang().getThuoc().getTenDVTCoBan();
                    if (tenDVT == null) tenDVT = "";
                }
                return new SimpleStringProperty(tenDVT);
            });
        }

        // Don gia
        colNDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        // Chiet khau
        colNChietKhau.setCellValueFactory(new PropertyValueFactory<>("giamGia"));

        // Thanh tien
        if (tblChiTietHoaDon.getColumns().size() > 6) {
            TableColumn<ChiTietHoaDon, String> colThanhTien = (TableColumn<ChiTietHoaDon, String>) tblChiTietHoaDon.getColumns().get(6);
            colThanhTien.setCellValueFactory(cel -> {
                var item = cel.getValue();
                double thanh = item.getSoLuong() * item.getDonGia();
                if (item.getGiamGia() != 0) thanh = thanh * (1 - item.getGiamGia() / 100.0);
                return new SimpleStringProperty(String.format("%.2f", thanh));
            });
        }

        // Compute summary
        double tongHang = 0.0;
        double tongCK = 0.0;
        for (ChiTietHoaDon item : list) {
            double line = item.getSoLuong() * item.getDonGia();
            double discount = item.getGiamGia() != 0 ? line * item.getGiamGia() / 100.0 : 0.0;
            tongHang += line;
            tongCK += discount;
        }
        double sauCK = tongHang - tongCK;
        double vat = 0.0; // adjust if VAT is stored
        double thanhToan = sauCK + vat;

        lblTongTienHangValue.setText(String.format("%.2f VND", tongHang));
        lblChietKhauHDValue.setText(String.format("-%.2f VND", tongCK));
        lblThueVATValue.setText(String.format("%.2f VND", vat));
        lblThanhToanValue.setText(String.format("%.2f VND", thanhToan));
        lblPTTTValue.setText("Tiền mặt");
        lblTienKhachDuaValue.setText("0 VND");
        lblTienThuaValue.setText("0 VND");
    }

}
