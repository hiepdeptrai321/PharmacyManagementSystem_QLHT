package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SuaXoaKhuyenMai_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public Button btnLuu;
    public Button btnHuy;
    public Button btnXoa;
    @FXML
    private TextField timKiemThuocC;
    @FXML
    private ListView<?> listViewThuoc;


    @FXML
    private TextField tfTenKM;
    @FXML
    private ComboBox<String> cbLoaiKM;
    @FXML
    private TextField tfGiaTri;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private TextField tfMoTa;

    // 2. KHỞI TẠO (INITIALIZE)

    @FXML
    public void initialize() {
        listViewThuoc.setVisible(false);
        timKiemThuocC.focusedProperty().addListener((obs, oldVal, newVal) -> {
            listViewThuoc.setVisible(newVal);
        });
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLKhuyenMai/SuaXoaKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN

    public void loadData(KhuyenMai km) {
        if (km == null) return;
        tfTenKM = new TextField();
        tfGiaTri = new TextField();
        tfMoTa = new TextField();
        dpTuNgay = new DatePicker();
        dpDenNgay = new DatePicker();
        cbLoaiKM = new ComboBox<>();
        tfTenKM.setText(km.getTenKM());
        cbLoaiKM.setValue(km.getLoaiKM().getMaLoai());
        tfGiaTri.setText(String.valueOf(km.getGiaTriKM()));
        dpTuNgay.setValue(km.getNgayBatDau().toLocalDate());
        dpDenNgay.setValue(km.getNgayKetThuc().toLocalDate());
        tfMoTa.setText(km.getMoTa());
    }

    public void btnHuyClick(){
        Stage stage = (Stage) timKiemThuocC.getScene().getWindow();
        stage.close();
    }
    public void btnLuuClick(){
        // TODO
    }
    public void btnXoaClick(){
        // TODO
    }

    // 4. XỬ LÝ NGHIỆP VỤ
}
