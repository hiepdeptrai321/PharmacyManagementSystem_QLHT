package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang;

import com.example.pharmacymanagementsystem_qlht.dao.KhachHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

public class TimKiemKhachHang_Ctrl extends Application {
    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnTim;

    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    private TableColumn<KhachHang, String> cotDiaChi;

    @FXML
    private TableColumn<KhachHang, String> cotEmail;

    @FXML
    private TableColumn<KhachHang, String> cotGT;

    @FXML
    private TableColumn<KhachHang, String> cotMaKH;

    @FXML
    private TableColumn<KhachHang, String> cotNgaySinh;

    @FXML
    private TableColumn<KhachHang, String> cotSDT;

    @FXML
    private TableColumn<KhachHang, String> cotTenKH;

    @FXML
    private TableColumn<KhachHang, String> cotSTT;

    @FXML
    private TableView<KhachHang> tbKhachHang;

    @FXML
    private Pane mainPane;

    @FXML
    private TextField txtTimKiem;
    private KhachHang_Dao khachHangDao = new KhachHang_Dao();
    // 2. KHỞI TẠO (INITIALIZE)
    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên khách hàng",
                "Theo email",
                "Theo SDT"

        );
        cboTimKiem.setValue("Theo mã, tên khách hàng");
        loadTable();
        btnLamMoi.setOnAction(e -> LamMoi());
        btnTim.setOnAction(e -> TimKiem());
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/TimKiemNhanVien.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void loadTable() {
        List<KhachHang> list = khachHangDao.selectAll();
        ObservableList<KhachHang> data = FXCollections.observableArrayList(list);
        cotSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbKhachHang.getItems().indexOf(cellData.getValue()) + 1))
        );
        cotMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        cotTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        cotGT.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        cotNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        cotSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        cotEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        cotDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        tbKhachHang.setItems(data);
    }
    @FXML
    private void LamMoi() {
        txtTimKiem.clear();
        cboTimKiem.setValue("Theo mã, tên khách hàng");
        loadTable();
    }
    @FXML
    private void TimKiem() {
        String criteria = cboTimKiem.getValue();
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        List<KhachHang> list = khachHangDao.selectAll();
        List<KhachHang> filtered = list.stream().filter(kh -> {
            switch (criteria) {
                case "Theo mã, tên khách hàng":
                    return kh.getMaKH().toLowerCase().contains(keyword) ||
                            kh.getTenKH().toLowerCase().contains(keyword);
                case "Theo email":
                    return kh.getEmail().toLowerCase().contains(keyword);
                case "Theo SDT":
                    return kh.getSdt().toLowerCase().contains(keyword);
                default:
                    return true;
            }
        }).toList();
        tbKhachHang.setItems(FXCollections.observableArrayList(filtered));
    }
}
