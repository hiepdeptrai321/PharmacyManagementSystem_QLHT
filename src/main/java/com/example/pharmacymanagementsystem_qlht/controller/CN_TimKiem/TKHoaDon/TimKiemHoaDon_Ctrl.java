package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;

import com.example.pharmacymanagementsystem_qlht.dao.HoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuDoiHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuTraHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class TimKiemHoaDon_Ctrl extends Application {

    @FXML
    private TableView<HoaDon> tbHD;
    @FXML
    private TableColumn<HoaDon, String> colMaHD;
    @FXML
    private TableColumn<HoaDon, String> colNgayLap;
    @FXML
    private TableColumn<HoaDon, String> colTenKH;
    @FXML
    private TableColumn<HoaDon, String> colSdtKH;
    @FXML
    private TableColumn<HoaDon, String> colTenNV;
    @FXML
    private TableColumn<HoaDon, Integer> colSoLuongPhieuDoiTra;
    @FXML
    private TableColumn<HoaDon, String> colChiTiet; // Optional detail button column

    private HoaDon_Dao hoaDonDao = new HoaDon_Dao();
    private PhieuDoiHang_Dao phieuDoiHangDao = new PhieuDoiHang_Dao();
    private PhieuTraHang_Dao phieuTraHangDao = new PhieuTraHang_Dao();





    // Optional method for detail button click
    private void btnChiTietClick(HoaDon hoaDon) {
        // Implement detail view logic here
        System.out.println("Xem chi tiết hóa đơn: " + hoaDon.getMaHD());
        // You can open a detail window or navigate to detail view
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoaDon/TKHoaDon_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
