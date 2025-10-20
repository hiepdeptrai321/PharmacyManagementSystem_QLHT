package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class DanhMucThuoc_Ctrl extends Application {
//  1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public TextField tfTimThuoc;
    public Button btnTimThuoc;
    public Button btnThemThuoc;
    public TableColumn<Thuoc_SanPham,String> colSTT;
    public TableColumn<Thuoc_SanPham,String> colMaThuoc;
    public TableColumn<Thuoc_SanPham,String>  colTenThuoc;
    public TableColumn<Thuoc_SanPham,String>  colChiTiet;
    public Button btnImport;
    public Button btnExport;
    public TableView<Thuoc_SanPham>  tbl_Thuoc;
    public TableColumn<Thuoc_SanPham,String>  colHamLuong;
    public TableColumn<Thuoc_SanPham,String>  colSDK_GPNK;
    public TableColumn<Thuoc_SanPham,String>  colXuatXu;
    public TableColumn<Thuoc_SanPham,String>  colLoaiHang;
    public TableColumn<Thuoc_SanPham,String>  colViTri;

//  2. KHỞI TẠO (INITIALIZE)
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/DanhMucThuoc_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        loadTable();
    }

//  3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void loadTable() {
        List<Thuoc_SanPham> list = new Thuoc_SanPham_Dao().selectAll();
        ObservableList<Thuoc_SanPham> data = FXCollections.observableArrayList(list);

        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbl_Thuoc.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<>("HamLuongDonVi"));
        colSDK_GPNK.setCellValueFactory(new PropertyValueFactory<>("SDK_GPNK"));
        colXuatXu.setCellValueFactory(new PropertyValueFactory<>("nuocSX"));
        colLoaiHang.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLoaiHang().getTenLoaiHang())));
        colViTri.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getVitri().getTenKe())));
        colChiTiet.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    Thuoc_SanPham thuoc = getTableView().getItems().get(getIndex());
                     btnChiTietClick();
                });
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tbl_Thuoc.setItems(data);
    }

    public void btnThemThuocClick() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/ThemThuoc_GUI.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  4. XỬ LÝ NGHIỆP VỤ
//  Tìm kiếm thuốc theo từ khóa
    public void timThuoc(){
        String keyword = tfTimThuoc.getText().trim().toLowerCase();
        Thuoc_SanPham_Dao ts_dao = new Thuoc_SanPham_Dao();
        List<Thuoc_SanPham> dsTSLoc = ts_dao.selectByTuKhoa(keyword);
        ObservableList<Thuoc_SanPham> data = FXCollections.observableArrayList(dsTSLoc);
        tbl_Thuoc.setItems(data);
    }

//
    public void btnChiTietClick() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/SuaXoaThuoc_GUI.fxml"));
            Scene scene = new Scene(root);
            SuaXoaThuoc_Ctrl ctrl = new SuaXoaThuoc_Ctrl();
            ctrl.initialize();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
