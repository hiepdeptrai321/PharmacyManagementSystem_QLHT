package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDoi;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LapPhieuDoiHang_Ctrl extends Application {
    @FXML private TextField txtTimHoaDonGoc;
    @FXML private Button btnTim;
    @FXML private TableView<?> tblSanPhamGoc;
    @FXML private TableView<?> tblChiTietDoiHang;
    @FXML private TextField txtMaHoaDonGoc;
    @FXML private DatePicker dpNgayLapPhieu;
    @FXML private Label lblTongTienGoc;
    @FXML private Label lblTongTienDoi;
    @FXML private Label lblVAT;
    @FXML private Label lblTienTraLai;
    @FXML private TextArea txtGhiChu;
    @FXML private Button btnDatHang;
    @FXML private Button btnThanhToan;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDoi/LapPhieuDoiHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
