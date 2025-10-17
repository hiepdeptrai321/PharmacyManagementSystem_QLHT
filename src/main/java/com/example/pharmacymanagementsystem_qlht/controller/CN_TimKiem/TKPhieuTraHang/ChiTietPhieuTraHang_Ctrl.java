package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuTraHang;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuNhap_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuTraHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChiTietPhieuTraHang_Ctrl {

    @FXML
    private PhieuTraHang phieuTraHang;
    @FXML
    private TableColumn<ChiTietPhieuTraHang, String> ColDongia;

    @FXML
    private Button btnDong;

    @FXML
    private Button btnInPhieuTra;


    @FXML
    private TableColumn<ChiTietPhieuTraHang, String> colLyDo;

    @FXML
    private TableColumn<ChiTietPhieuTraHang, String> colSTT;

    @FXML
    private TableColumn<ChiTietPhieuTraHang, String> colSoLuong;

    @FXML
    private TableColumn<ChiTietPhieuTraHang, String> colTenSP;

    @FXML
    private TableColumn<ChiTietPhieuTraHang, Double> colThanhTien;

    @FXML
    private Label lblChietKhauPTValue;

    @FXML
    private Label lblGhiChuValue;

    @FXML
    private Label lblMaPhieuTraValue;

    @FXML
    private Label lblNgayLapValue;

    @FXML
    private Label lblPTHTValue;

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
    private Label lblTienKhachNhanValue;

    @FXML
    private Label lblTienThuaValue;

    @FXML
    private Label lblTongTienPhaiTraValue;

    @FXML
    private Label lblTongTienTraValue;

    @FXML
    private TableView<ChiTietPhieuTraHang> tblChiTietPhieuTra;


    @FXML
    public void initialize() {
        btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
    }
    public void setPhieuTraHang(PhieuTraHang pTra) {
        this.phieuTraHang = pTra;
        hienThiThongTin();
    }

    private void hienThiThongTin() {
        if (phieuTraHang != null) {
        lblMaPhieuTraValue.setText(phieuTraHang.getMaPT());

            Timestamp ngayLapTimestamp = phieuTraHang.getNgayLap();

            if (ngayLapTimestamp != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String ngayLapFormatted = formatter.format(ngayLapTimestamp);
                lblNgayLapValue.setText(ngayLapFormatted);
            } else {
                lblNgayLapValue.setText("Không rõ");
            }
            lblTenNV.setText(phieuTraHang.getNhanVien().getTenNV());
            lblTenKhachHangValue.setText(phieuTraHang.getKhachHang() != null ? phieuTraHang.getKhachHang().getTenKH() : "Khách lẻ");
            lblSDTKhachHangValue.setText(phieuTraHang.getKhachHang().getSdt());
            lblGhiChuValue.setText(phieuTraHang.getGhiChu() != null ? phieuTraHang.getGhiChu() : "");

            List<ChiTietPhieuTraHang> list = new ChiTietPhieuTraHang_Dao().getChiTietPhieuTraByMaPT(phieuTraHang.getMaPT());

            tblChiTietPhieuTra.getItems().clear();
            tblChiTietPhieuTra.getItems().addAll(list);

            colSTT.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(tblChiTietPhieuTra.getItems().indexOf(cellData.getValue()) + 1))
            );
            colTenSP.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getThuoc().getTenThuoc()));
            colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
            //colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
            ColDongia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
            colLyDo.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getPhieuTraHang().getLyDoTra()));
            colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTienTra"));
            //Double tongGiaNhap = temp.getTongTien();
            //lblTongGiaNhap.setText("Tổng giá nhập: " + String.format("%.2f", tongGiaNhap) +" VND");

        }
    }
}
