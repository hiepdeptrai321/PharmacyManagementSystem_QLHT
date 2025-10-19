package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietHoatChat_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.HoatChat_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoatChat;
import com.example.pharmacymanagementsystem_qlht.model.HoatChat;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ChiTietThuoc_Ctrl {
    public TextField txtHamLuong;
    public TextField txtHangSanXuat;
    public TextField txtMaThuoc;
    public TextField txtTenThuoc;
    public TextField txtNuocSanXuat;
    public TextField txtQCDongGoi;
    public TextField txtSDK_GPNK;
    public TableView<ChiTietHoatChat> tblHoatChat;
    public TableColumn<ChiTietHoatChat,String> colMaHoatChat;
    public TableColumn<ChiTietHoatChat,String> colTenHoatChat;
    public TableColumn<ChiTietHoatChat,String> colHamLuong;
    public TextField txtDuongDung;
    public TextField txtViTri;
    public TextField txtLoaiHang;
    public TextField txtNhomDuocLy;
    public ImageView imgThuoc;

    public void initialize() {

    }

    public void load(Thuoc_SanPham thuoc) {
        txtMaThuoc.setText(thuoc.getMaThuoc());
        txtTenThuoc.setText(thuoc.getTenThuoc());
        txtHamLuong.setText(thuoc.getHamLuongDonVi());
        txtHangSanXuat.setText(thuoc.getHangSX());
        txtNuocSanXuat.setText(thuoc.getNuocSX());
        txtSDK_GPNK.setText(thuoc.getSDK_GPNK());
        txtQCDongGoi.setText(thuoc.getQuyCachDongGoi());
        txtLoaiHang.setText(thuoc.getLoaiHang().getTenLoaiHang());
        txtViTri.setText(thuoc.getVitri().getTenKe());
        txtNhomDuocLy.setText(thuoc.getNhomDuocLy().getTenNDL());
        txtDuongDung.setText(thuoc.getDuongDung());
        imgThuoc.setImage(new Image(new ByteArrayInputStream(thuoc.getHinhAnh())));
        List<ChiTietHoatChat> list = new ChiTietHoatChat_Dao().selectByMaThuoc(thuoc.getMaThuoc());
        ObservableList<ChiTietHoatChat> oblist = FXCollections.observableArrayList(list);
        colMaHoatChat.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getHoatChat().getMaHoatChat()));
        colTenHoatChat.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getHoatChat().getTenHoatChat()));
        colHamLuong.setCellValueFactory(cel -> new SimpleStringProperty(String.valueOf(cel.getValue().getHamLuong())));
        tblHoatChat.setItems(oblist);
    }
}
