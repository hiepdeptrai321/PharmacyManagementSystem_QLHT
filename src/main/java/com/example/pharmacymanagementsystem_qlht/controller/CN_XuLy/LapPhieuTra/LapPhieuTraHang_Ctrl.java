package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuTra;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Table placeholders
        if (tblSanPhamHoaDon != null) {
            tblSanPhamHoaDon.setPlaceholder(new Label("Chưa có sản phẩm trong hóa đơn"));
            try { tblSanPhamHoaDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
        }
        if (tblChiTietTraHang != null) {
            tblChiTietTraHang.setPlaceholder(new Label("Chưa có chi tiết trả hàng"));
            try { tblChiTietTraHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
        }

        // Default labels
        if (lblTongTienGoc != null) lblTongTienGoc.setText("0 VNĐ");
        if (lblTongTienTraLai != null) lblTongTienTraLai.setText("0 VNĐ");
        if (lblVAT != null) lblVAT.setText("0 VNĐ");
        if (lblSoTienTraLai != null) lblSoTienTraLai.setText("0 VNĐ");

        // Simple handlers (placeholder logic)
        if (btnTimHoaDon != null) btnTimHoaDon.setOnAction(e -> onTimHoaDon());
        if (btnDatHang != null) btnDatHang.setOnAction(e -> onInPhieuTra());
        if (btnThanhToan != null) btnThanhToan.setOnAction(e -> onTraHang());
    }

    private void onTimHoaDon() {
        System.out.println("Tìm hóa đơn: " + (txtTimHoaDon != null ? txtTimHoaDon.getText() : ""));
    }

    private void onInPhieuTra() {
        System.out.println("In phiếu trả clicked");
    }

    private void onTraHang() {
        System.out.println("Trả hàng clicked");
    }


}
