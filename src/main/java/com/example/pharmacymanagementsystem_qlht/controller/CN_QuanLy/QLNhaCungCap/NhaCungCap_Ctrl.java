package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.dao.NhaCungCap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class NhaCungCap_Ctrl extends Application {

    @FXML
    private TableView<NhaCungCap> tblNhaCungCap;
    @FXML
    private TableColumn<NhaCungCap, String> colMaNCC;
    @FXML
    private TableColumn<NhaCungCap, String> colTenNCC;
    @FXML
    private TableColumn<NhaCungCap, String> colDiaChi;
    @FXML
    private TableColumn<NhaCungCap, String> colSDT;
    @FXML
    private TableColumn<NhaCungCap, String> colEmail;
    @FXML
    private TableColumn<NhaCungCap, String> colGhiChu;
    @FXML
    private TableColumn<NhaCungCap, String> colTenCongTy;

    @FXML
    public void initialize() {
        loadNhaCungCap();
    }

    public void loadNhaCungCap() {
        List<NhaCungCap> list = new NhaCungCap_Dao().selectAll();
        ObservableList<NhaCungCap> data = FXCollections.observableArrayList(list);

        colMaNCC.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
        colTenNCC.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        tblNhaCungCap.setItems(data);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/DanhMucNhaCungCap_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
