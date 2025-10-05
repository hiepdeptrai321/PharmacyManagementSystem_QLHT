package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.util.List;

public class QuanLyKhuyenMai_Ctrl extends Application {
    @FXML
    public TableView<KhuyenMai> tbKM;
    @FXML
    private Button btnthemthuoc;
    @FXML
    private TableColumn<KhuyenMai, String> colMaKM;
    @FXML
    private TableColumn<KhuyenMai, String> colTenKM;
    @FXML
    private TableColumn<KhuyenMai, String> colLoaiKM;
    @FXML
    private TableColumn<KhuyenMai, Float> colGiaTri;
    @FXML
    private TableColumn<KhuyenMai, java.sql.Date> colNBD;
    @FXML
    private TableColumn<KhuyenMai, java.sql.Date> colNKT;
    private KhuyenMai_Dao khuyenMaiDao = new KhuyenMai_Dao();

    public void initialize() {
        loadTable();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLKhuyenMai/QuanLyKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loadTable() {

        List<KhuyenMai> list = khuyenMaiDao.selectAll();
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(list);

        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
        colLoaiKM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLoaiKM().getMaLoai()));
        colGiaTri.setCellValueFactory(new PropertyValueFactory<>("giaTriKM"));
        colNBD.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNKT.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));

        tbKM.setItems(data);
    }
}
