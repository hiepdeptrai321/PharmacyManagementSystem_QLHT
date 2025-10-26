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
    @FXML private TableView<ChiTietHoaDon> tblChiTietHoaDon;
    @FXML private TableColumn<ChiTietHoaDon, Number> colNSTT;
    @FXML private TableColumn<ChiTietHoaDon, String> colNTen;
    @FXML private TableColumn<ChiTietHoaDon, Integer> colNSL;
    @FXML private TableColumn<ChiTietHoaDon, String> colNDonVi;
    @FXML private TableColumn<ChiTietHoaDon, Double> colNDonGia;
    @FXML private TableColumn<ChiTietHoaDon, Double> colNChietKhau;
    @FXML private TableColumn<ChiTietHoaDon, Double> colNThanhTien;
    @FXML private Label lblMaHoaDonValue;
    @FXML private Label lblNgayLapValue;
    @FXML private Label lblTenNhanVienValue;
    @FXML private Label lblTenKhachHangValue;
    @FXML private Label lblSDTKhachHangValue;
    @FXML private Label lblGhiChuValue;
    @FXML private Label lblTongTienHang;
    @FXML Label lblGiamTheoSP;
    @FXML Label lblGiamTheoHD;
    @FXML Label lblVAT;
    @FXML Label lblTongThanhToan;
    @FXML Label lblPhuongThucTT;
    @FXML Label lblSoTienKhachDua;
    @FXML Label lblTienThua;

    @FXML private Button btnDong;
    @FXML private Button btnInHoaDon;

    private HoaDon hoaDon;

    @FXML
    public void initialize() {
        if (btnDong != null) btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
      //  if (btnInHoaDon != null) btnInHoaDon.setOnAction(e -> printInvoice());
    }


    public void setHoaDon(HoaDon hd) {
        this.hoaDon = hd;
        hienThiThongTin();
    }


    private void hienThiThongTin() {
        if (hoaDon == null) return;

        // Header info
        if (lblMaHoaDonValue != null) lblMaHoaDonValue.setText(safeStr(hoaDon.getMaHD()));
        if (lblNgayLapValue != null) {
            if (hoaDon.getNgayLap() != null) {
                var fmt = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                lblNgayLapValue.setText(fmt.format(hoaDon.getNgayLap()));
            } else lblNgayLapValue.setText("");
        }
        if (lblTenNhanVienValue != null)
            lblTenNhanVienValue.setText(hoaDon.getMaNV() != null ? safeStr(hoaDon.getMaNV().getTenNV()) : "");
        if (hoaDon.getMaKH() != null) {
            if (lblTenKhachHangValue != null) lblTenKhachHangValue.setText(safeStr(hoaDon.getMaKH().getTenKH()));
            if (lblSDTKhachHangValue != null) lblSDTKhachHangValue.setText(safeStr(hoaDon.getMaKH().getSdt()));
        } else {
            if (lblTenKhachHangValue != null) lblTenKhachHangValue.setText("Khách lẻ");
            if (lblSDTKhachHangValue != null) lblSDTKhachHangValue.setText("");
        }
        if (lblGhiChuValue != null) lblGhiChuValue.setText("");

        // Load details
        List<ChiTietHoaDon> list = new ChiTietHoaDon_Dao().selectByMaHD(hoaDon.getMaHD());
        tblChiTietHoaDon.setItems(FXCollections.observableArrayList(list));

        // STT
        if (colNSTT != null) {
            colNSTT.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(tblChiTietHoaDon.getItems().indexOf(cd.getValue()) + 1));
        }

        // Tên SP (từ lô -> thuốc)
        if (colNTen != null) {
            colNTen.setCellValueFactory(cel -> {
                Thuoc_SP_TheoLo lo = cel.getValue().getLoHang();
                String ten = (lo != null && lo.getThuoc() != null) ? safeStr(lo.getThuoc().getTenThuoc()) : "";
                return new SimpleStringProperty(ten);
            });
        }

        // Số lượng
        if (colNSL != null) {
            colNSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        }

        // Đơn vị
        if (colNDonVi != null) {
            colNDonVi.setCellValueFactory(cel ->
                    new SimpleStringProperty(tenDonViCoBan(cel.getValue().getLoHang())));
        }

        // Đơn giá (table: "đ")
        if (colNDonGia != null) {
            colNDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
            colNDonGia.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatVNDTable(item == null ? 0 : item));
                    setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
                }
            });
        }

        // Giảm giá theo SP (giá trị tuyệt đối, không %)
        if (colNChietKhau != null) {
            colNChietKhau.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
            colNChietKhau.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatVNDTable(item == null ? 0 : item));
                    setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
                }
            });
        }

        // Thành tiền = soLuong*donGia - giamGia (table: "đ")
        if (colNThanhTien != null) {
            colNThanhTien.setCellValueFactory(cel -> {
                ChiTietHoaDon r = cel.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return new ReadOnlyObjectWrapper<>(tt);
            });
            colNThanhTien.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatVNDTable(item == null ? 0 : item));
                    setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
                }
            });
        }

        // Summary (labels use "VND")
        double tongHang = 0.0;
        double giamTheoSp = 0.0;
        for (ChiTietHoaDon item : list) {
            tongHang += item.getSoLuong() * item.getDonGia();
            giamTheoSp += Math.max(0, item.getGiamGia());
        }
        double sauGiamDong = Math.max(0, tongHang - giamTheoSp);
        double giamTheoHd = 0.0; // no invoice-level KM stored => 0
        double baseVat = Math.max(0, sauGiamDong - giamTheoHd);
        double vat = Math.round(baseVat * 0.05); // 5% VAT, rounded to VND
        double tongThanhToan = baseVat + vat;

        if (lblTongTienHang != null) lblTongTienHang.setText(formatVNDLabel(tongHang));
        if (lblGiamTheoSP != null)      lblGiamTheoSP.setText(formatVNDLabel(giamTheoSp));
        if (lblGiamTheoHD != null)      lblGiamTheoHD.setText(formatVNDLabel(giamTheoHd));
        if (lblVAT != null)          lblVAT.setText(formatVNDLabel(vat));
        if (lblTongThanhToan != null) lblTongThanhToan.setText(formatVNDLabel(tongThanhToan));

        // The following are not stored with bill details; set sensible defaults
        if (lblPhuongThucTT != null) lblPhuongThucTT.setText("Tiền mặt");
        if (lblSoTienKhachDua != null)  lblSoTienKhachDua.setText(formatVNDLabel(0));
        if (lblTienThua != null)        lblTienThua.setText(formatVNDLabel(0));
    }
    private static String formatVNDTable(double v) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0");
        df.setGroupingUsed(true);
        return df.format(Math.max(0, Math.round(v))) + " đ";
    }
    private static String formatVNDLabel(double v) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0");
        df.setGroupingUsed(true);
        return df.format(Math.max(0, Math.round(v))) + " VND";
    }
    private static String safeStr(String s) { return s == null ? "" : s; }
    private static String tenDonViCoBan(Thuoc_SP_TheoLo lo) {
        if (lo == null || lo.getThuoc() == null) return "";
        try {
            String ten = lo.getThuoc().getTenDVTCoBan();
            return ten == null ? "" : ten;
        } catch (Exception ignore) { return ""; }
    }

}
