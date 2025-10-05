package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuNhap;

import com.example.pharmacymanagementsystem_qlht.dao.PhieuNhap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TimKiemPhieuNhap_Ctrl extends Application {
    @FXML
    private TableView<PhieuNhap> tblPhieuNhap;
    @FXML
    private TableColumn<PhieuNhap, String> colMaPN;
    @FXML
    private TableColumn<PhieuNhap, String> colNhaCungCap;
    @FXML
    private TableColumn<PhieuNhap, Timestamp> colNgayNhap;
    @FXML
    private TableColumn<PhieuNhap, String> colTrangThai;
    @FXML
    private TableColumn<PhieuNhap, String> colGhiChu;
    @FXML
    private TableColumn<PhieuNhap, String> colNhanVien;

    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuNhapHang/TKPhieuNhapHang_GUI.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        loadPhieuNhap();
    }

    public void loadPhieuNhap() {
        List<PhieuNhap> list = new PhieuNhap_Dao().selectAll();
        ObservableList<PhieuNhap> data = FXCollections.observableArrayList(list);

        colMaPN.setCellValueFactory(new PropertyValueFactory<>("maPN"));
        colNhaCungCap.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getNhaCungCap().getTenNCC())
        );
        colNgayNhap.setCellValueFactory(new PropertyValueFactory<>("ngayNhap"));
        colTrangThai.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getTrangThai() ? "Hoàn tất" : "Chưa hoàn tất")
        );
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colNhanVien.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getNhanVien().getTenNV())
        );

        tblPhieuNhap.setItems(data);
    }
}
