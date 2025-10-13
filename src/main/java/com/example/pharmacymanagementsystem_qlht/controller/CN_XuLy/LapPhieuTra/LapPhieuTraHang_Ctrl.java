package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuTra;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LapPhieuTraHang_Ctrl extends Application {
    @FXML private TextField txtTimHoaDon;
    @FXML private Button btnTimHoaDon;
    @FXML private TableView<?> tblSanPhamHoaDon;
    @FXML private TableView<?> tblChiTietTraHang;
    @FXML private TextField txtMaHoaDonGoc;
    @FXML private TextField txtNgayLapHoaDon;
    @FXML private Label lblTongTienGoc;
    @FXML private Label lblTongTienTraLai;
    @FXML private Label lblVAT;
    @FXML private Label lblSoTienTraLai;
    @FXML private TextArea txtGhiChu;
    @FXML private Button btnDatHang;
    @FXML private Button btnThanhToan;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuTra/LapPhieuTraHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
