package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class TimKiemThuoc_Ctrl extends Application {
    @FXML
    public TableColumn colMaThuoc;
    @FXML
    public TableColumn colTenThuoc;
    @FXML
    public TableColumn colHoatChat;
    @FXML
    public TableColumn colHamLuong;
    @FXML
    public TableColumn colSDK_GPNK;
    @FXML
    public TableColumn colHangSX;
    @FXML
    public TableView tblThuoc_SP;
    @FXML
    private ComboBox<String> cboTimKiem;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/TimKiemThuoc.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void initialize() {
        LoadData();
    }
    private void LoadData() {
        List<Thuoc_SanPham> list = new Thuoc_SanPham_Dao().selectAll();
        ObservableList<Thuoc_SanPham> data = FXCollections.observableList(list);
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
//        colHoatChat.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getNhomDuocLy().getTenNDL()));
//        colHamLuong.setCellValueFactory(new PropertyValueFactory<>("hamLuong+\" \"+donViHamLuong"));
        colSDK_GPNK.setCellValueFactory(new PropertyValueFactory<>("SDK_GPNK"));
        colHangSX.setCellValueFactory(new PropertyValueFactory<>("hangSX"));
        tblThuoc_SP.setItems(data);
    }
}