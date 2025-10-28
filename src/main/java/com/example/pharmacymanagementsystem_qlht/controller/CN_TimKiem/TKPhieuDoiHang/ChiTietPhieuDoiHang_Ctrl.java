package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDoiHang;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuDoiHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.TienIch.DoiNgay;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class ChiTietPhieuDoiHang_Ctrl  {
    @FXML private Button btnDong;
    @FXML private Button btnInPhieuDoi;
    @FXML private TableColumn<ChiTietPhieuDoiHang, String> colLyDo;
    @FXML private TableColumn<ChiTietPhieuDoiHang, String> colSTT;
    @FXML private TableColumn<ChiTietPhieuDoiHang, String> colSoLuong;
    @FXML private TableColumn<ChiTietPhieuDoiHang, String> colDonVi;
    @FXML private TableColumn<ChiTietPhieuDoiHang, String> colTenSP;


    @FXML private Label lblGhiChuValue;
    @FXML private Label lblMaPhieuDoiValue;
    @FXML private Label lblNgayLapValue;
    @FXML private Label lblSDTKhachHangValue;
    @FXML private Label lblTenKhachHangValue;
    @FXML private Label lblTenNhanVienValue;


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
            lblNgayLapValue.setText(DoiNgay.dinhDangThoiGian(phieuDoiHang.getNgayLap()));
            lblTenNhanVienValue.setText(phieuDoiHang.getNhanVien().getTenNV());
            lblTenKhachHangValue.setText(phieuDoiHang.getKhachHang() != null ? phieuDoiHang.getKhachHang().getTenKH() : "Khách lẻ");
            lblSDTKhachHangValue.setText(phieuDoiHang.getKhachHang() != null ? phieuDoiHang.getKhachHang().getSdt() : "");
            lblGhiChuValue.setText(phieuDoiHang.getGhiChu() != null ? phieuDoiHang.getGhiChu() : "");
            //tblChiTietPhieuDoi.setItems(phieuDoiHang.getChiTietPhieuDoiHang());

            List<ChiTietPhieuDoiHang> list = new ChiTietPhieuDoiHang_Dao().getChiTietPhieuDoiByMaPT(phieuDoiHang.getMaPD());
            tblChiTietPhieuDoi.getItems().clear();
            tblChiTietPhieuDoi.getItems().addAll(list);

            colSTT.setCellValueFactory(cellData ->
                    new SimpleStringProperty(String.valueOf(tblChiTietPhieuDoi.getItems().indexOf(cellData.getValue()) + 1))
            );
            colTenSP.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getThuoc().getTenThuoc())
            );
            colSoLuong.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuong()))
            );
            colDonVi.setCellValueFactory(cel -> {
                if (cel.getValue().getDvt() != null && cel.getValue().getDvt().getTenDonViTinh() != null) {
                    return new SimpleStringProperty(cel.getValue().getDvt().getTenDonViTinh());
                }
                return new SimpleStringProperty("");
            });
            colLyDo.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getLyDoDoi())
            );
        }
    }

    public void xuLyInPhieu(ActionEvent actionEvent) {
    }

    public void xuLyDong(ActionEvent actionEvent) {
    }
}
