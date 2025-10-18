package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuDatHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuDatHang;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDatHang;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChiTietPhieuDatHang_Ctrl  {
    @FXML
    private PhieuDatHang phieuDatHang;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, Number> colSTT;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colTenSP;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, Integer> colSoLuong;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colDonVi;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, Double> colDonGia;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colNhaCungCap; // used for chiết khấu percent

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colThanhTien;

    @FXML
    private TableView<ChiTietPhieuDatHang> tblChiTietPhieuDat;

    @FXML
    private Label lblMaPhieuDatValue;

    @FXML
    private Label lblNgayLapValue;

    @FXML
    private Label lblTenNhanVienValue;

    @FXML
    private Label lblTenNCCValue;

    @FXML
    private Label lblSDTNCCValue;

    @FXML
    private Label lblGhiChuValue;

    @FXML
    private Label lblTongTienDatValue;

    @FXML
    private Label lblChietKhauPDValue;

    @FXML
    private Label lblThueVATValue;

    @FXML
    private Label lblTongTienPhaiDatValue;

    @FXML
    private Label lblPTTTValue;

    @FXML
    private Label lblTienDaThanhToanValue;

    @FXML
    private Label lblTienConLaiValue;

    @FXML
    private Button btnInPhieuDat;

    @FXML
    private Button btnDong;

    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    @FXML
    public void initialize() {
        // Close button
        if (btnDong != null) {
            btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
        }
    }

    public void setPhieuDatHang(PhieuDatHang pDat) {
        this.phieuDatHang = pDat;
        hienThiThongTin();
    }

    private void hienThiThongTin() {
        if (phieuDatHang == null) return;

        lblMaPhieuDatValue.setText(phieuDatHang.getMaPDat());

        if (phieuDatHang.getNgayLap() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            lblNgayLapValue.setText(formatter.format(phieuDatHang.getNgayLap()));
        } else {
            lblNgayLapValue.setText("Không rõ");
        }

        if (phieuDatHang.getNhanVien() != null)
            lblTenNhanVienValue.setText(phieuDatHang.getNhanVien().getTenNV());
        else
            lblTenNhanVienValue.setText("");

        // Supplier (NCC) - in PhieuDat model KhachHang used as supplier in some contexts; set if available
        if (phieuDatHang.getKhachHang() != null) {
            lblTenNCCValue.setText(phieuDatHang.getKhachHang().getTenKH());
            lblSDTNCCValue.setText(phieuDatHang.getKhachHang().getSdt());
        } else {
            lblTenNCCValue.setText("");
            lblSDTNCCValue.setText("");
        }

        lblGhiChuValue.setText(phieuDatHang.getGhiChu() != null ? phieuDatHang.getGhiChu() : "");

        // Load detail rows
        List<ChiTietPhieuDatHang> list = new ChiTietPhieuDatHang_Dao().selectBySql("SELECT * FROM ChiTietPhieuDatHang WHERE MaPDat = ?", phieuDatHang.getMaPDat());

        tblChiTietPhieuDat.getItems().clear();
        tblChiTietPhieuDat.getItems().addAll(list);

        // STT
        colSTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tblChiTietPhieuDat.getItems().indexOf(cellData.getValue()) + 1)
        );

        // Ten san pham
        colTenSP.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getThuoc().getTenThuoc()));

        // So luong
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        // Đơn vị
        colDonVi.setCellValueFactory(cel -> {
            String tenDVT = "";
            if (cel.getValue() != null && cel.getValue().getThuoc() != null) {
                tenDVT = cel.getValue().getThuoc().getTenDVTCoBan();
                if (tenDVT == null) tenDVT = "";
            }
            return new SimpleStringProperty(tenDVT);
        });

        // Đơn giá
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        // Chiết khấu column (giamGia)
        colNhaCungCap.setCellValueFactory(cel -> new SimpleStringProperty(String.format("%.2f", cel.getValue().getGiamGia())));

        // Thành tiền
        colThanhTien.setCellValueFactory(cel -> {
            ChiTietPhieuDatHang item = cel.getValue();
            double thanh = item.getSoLuong() * item.getDonGia();
            // Apply giamGia if giamGia is percentage (assumption)
            if (item.getGiamGia() != 0) {
                thanh = thanh * (1 - item.getGiamGia() / 100.0);
            }
            return new SimpleStringProperty(String.format("%.2f", thanh));
        });

        // Compute summary totals
        double tongTruocCK = 0.0;
        double tongGiamGiaValue = 0.0; // total discount amount
        for (ChiTietPhieuDatHang item : list) {
            double line = item.getSoLuong() * item.getDonGia();
            double discountAmount = 0.0;
            if (item.getGiamGia() != 0) {
                discountAmount = line * item.getGiamGia() / 100.0;
            }
            tongTruocCK += line;
            tongGiamGiaValue += discountAmount;
        }

        double tongSauCK = tongTruocCK - tongGiamGiaValue;
        double thueVAT = 0.0; // If your app stores VAT separately, adjust here. Default 0.
        double tongPhaiDat = tongSauCK + thueVAT;

        lblTongTienDatValue.setText(String.format("%.2f VND", tongTruocCK));
        lblChietKhauPDValue.setText(String.format("-%.2f VND", tongGiamGiaValue));
        lblThueVATValue.setText(String.format("%.2f VND", thueVAT));
        lblTongTienPhaiDatValue.setText(String.format("%.2f VND", tongPhaiDat));
        lblTienDaThanhToanValue.setText(String.format("%.2f VND", phieuDatHang.getSoTienCoc()));
        lblTienConLaiValue.setText(String.format("%.2f VND", Math.max(0.0, tongPhaiDat - phieuDatHang.getSoTienCoc())));

        // Payment method placeholder
        lblPTTTValue.setText("Chuyển khoản");
    }
}
