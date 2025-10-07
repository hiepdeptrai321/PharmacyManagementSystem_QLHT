package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class ThemKhuyenMai_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    @FXML
    private TextField timKiemThuocC;
    @FXML
    private ListView<?> listViewThuoc;

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
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLKhuyenMai/ThemKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void btnHuyClick(){
        Stage stage = (Stage) timKiemThuocC.getScene().getWindow();
        stage.close();
    }
    public void btnThemClick(){
        // TODO
    }

    // 4. XỬ LÝ NGHIỆP VỤ
    public void themKhuyenMaiVaChiTiet(KhuyenMai km, List<ChiTietKhuyenMai> ct){
        String maKM = new KhuyenMai_Dao().generateNewMaKM();
        km.setMaKM(maKM);
        for(ChiTietKhuyenMai x : ct){
            x.setKhuyenMai(km);
        }
        KhuyenMai_Dao km_dao = new KhuyenMai_Dao();
        ChiTietKhuyenMai_Dao ctkm_dao = new ChiTietKhuyenMai_Dao();
        km_dao.insert(km);
        for(ChiTietKhuyenMai x : ct) {
            ctkm_dao.insert(x);
        }
    }
}
