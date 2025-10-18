package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuNhapHang;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuNhap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuNhap;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class ChiTietPhieuNhap_Ctrl {
    public TextField txtMaPhieuNhap;
    public TextField txtNhaCungCap;
    public TextField txtNgayNhap;
    public TextField txtTrangThai;
    public TextField txtNhanVien;
    public TextArea txtGhiChu;
    public TableView tblChiTietPhieuNhap;
    public TableColumn<ChiTietPhieuNhap,String> colTenThuoc;
    public TableColumn<ChiTietPhieuNhap,String> colMaThuoc;
    public TableColumn<ChiTietPhieuNhap,String> colSoLuong;
    public TableColumn<ChiTietPhieuNhap,String> colGiaNhap;
    public TableColumn<ChiTietPhieuNhap,String> colThue;
    public TableColumn<ChiTietPhieuNhap,String> colChietKhau;
    public Label lblTongGiaNhap;
    public TableColumn<ChiTietPhieuNhap,String> colMaLoHang;


    public void load(PhieuNhap temp){

        txtMaPhieuNhap.setText(temp.getMaPN());
        txtNhaCungCap.setText(temp.getNhaCungCap().getTenNCC());
        txtNgayNhap.setText(String.valueOf(temp.getNgayNhap()));
        txtTrangThai.setText(temp.getTrangThai()?"Hoàn thành":"Chưa hoàn thành");
        txtNhanVien.setText(temp.getNhanVien().getTenNV());
        txtGhiChu.setText(temp.getGhiChu());

        List<ChiTietPhieuNhap> list = new ChiTietPhieuNhap_Dao().getChiTietPhieuNhapByMaPN(temp.getMaPN());

        tblChiTietPhieuNhap.getItems().clear();
        tblChiTietPhieuNhap.getItems().addAll(list);

        colTenThuoc.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getThuoc().getTenThuoc()));
        colMaThuoc.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getThuoc().getMaThuoc()));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
        colThue.setCellValueFactory(new PropertyValueFactory<>("thue"));
        colChietKhau.setCellValueFactory(new PropertyValueFactory<>("chietKhau"));
        colMaLoHang.setCellValueFactory(new PropertyValueFactory<>("maLH"));
        Double tongGiaNhap = temp.getTongTien();
        lblTongGiaNhap.setText("Tổng giá nhập: " + String.format("%.2f", tongGiaNhap) +" VND");
    }
}
