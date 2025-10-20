package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.KeHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.LoaiHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.NhomDuocLy_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.LoaiHang;
import com.example.pharmacymanagementsystem_qlht.model.NhomDuocLy;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class ThemThuoc_Ctrl {

    public Button btnHuy;
    public Button btnThem;
    public TextField txtTenThuoc;
    public ComboBox cbxLoaiHang;
    public ComboBox cbxViTri;
    public TextField txtMaThuoc;
    public ImageView imgThuoc_SanPham;
    public TextField txtDonViHamLuong;
    public TextField txtHamLuong;
    public TextField txtHangSanXuat;
    public ComboBox cbxNhomDuocLy;
    public TextField txtNuocSanXuat;
    public TextField txtQuyCachDongGoi;
    public TextField txtSDK_GPNK;
    public TextField txtDuongDung;
    public TableColumn colMaHoatChat;
    public TableColumn colTenHoatChat;
    public TableColumn colHamLuong;
    public TableColumn colXoa;
    public TextField txtTimKiemHoatChat;
    public ListView listViewHoatChat;
    private List<LoaiHang> listLoaiHang;
    private List<KeHang> listKeHang;
    private List<NhomDuocLy> listNhomDuocLy;

    public void initialize() {
        listLoaiHang = new LoaiHang_Dao().selectAll();
        for(KeHang keHang : listKeHang) {
            cbxLoaiHang.getItems().addAll(keHang.getTenKe());
        }
        listKeHang = new KeHang_Dao().selectAll();
        for(KeHang keHang : listKeHang) {
            cbxViTri.getItems().addAll(keHang.getTenKe());
        }
        listNhomDuocLy = new NhomDuocLy_Dao().selectAll();
        for(NhomDuocLy nhomDuocLy : listNhomDuocLy) {
            cbxNhomDuocLy.getItems().addAll(nhomDuocLy.getTenNDL());
        }
        
    }

    public void btnHuyClick() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    public void chonFile(ActionEvent actionEvent) {
    }

    public void btnThemThuoc(ActionEvent actionEvent) {
    }
}
