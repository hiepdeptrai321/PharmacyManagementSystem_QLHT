package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ThongKeBanHang_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    @FXML
    private Button btnBang;

    @FXML
    private Button btnBieuDo;

    @FXML
    private Button btnXuat;

    @FXML
    private ComboBox<String> cboMQT;

    @FXML
    private ComboBox<String> cboThoiGian;

    @FXML
    private ComboBox<String> cboXuatfile;

    @FXML
    private AreaChart<HoaDon, String> chartDoanhThu;

    @FXML
    private TableColumn<?, ?> cotDT;

    @FXML
    private TableColumn<?, ?> cotDoanhThu;

    @FXML
    private TableColumn<?, ?> cotGG;

    @FXML
    private TableColumn<?, ?> cotGTDonTra;

    @FXML
    private TableColumn<?, ?> cotMaThuoc;

    @FXML
    private TableColumn<?, ?> cotSL;

    @FXML
    private TableColumn<?, ?> cotSLHoaDon;

    @FXML
    private TableColumn<?, ?> cotTG;

    @FXML
    private TableColumn<?, ?> cotTenThuoc;

    @FXML
    private TableColumn<?, ?> cotThanhTien;

    @FXML
    private TableColumn<?, ?> cotTongGT;

    @FXML
    private ToggleGroup date;

    @FXML
    private RadioButton rbtnChon;

    @FXML
    private DatePicker rbtnNgay;

    @FXML
    private TableView<?> tableDoanhThu;

    // 2. KHỞI TẠO (INITIALIZE)
    public void initialize() {
        // managed sẽ ràng buộc theo visible
        chartDoanhThu.managedProperty().bind(chartDoanhThu.visibleProperty());
        tableDoanhThu.managedProperty().bind(tableDoanhThu.visibleProperty());
        cboMQT.getItems().addAll(
                "Lợi nhuận",
                "Bán hàng",
                "Nhân viên",
                "Khách hàng"

        );
        cboThoiGian.getItems().addAll(
                "Hôm nay",
                "Tuần này",
                "Tháng này",
                "Quý này"

        );
        cboXuatfile.getItems().addAll(
                "Excel",
                "PDF"

        );
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    @FXML
    private void hienThiBieuDo(ActionEvent event) {
        chartDoanhThu.setVisible(true);
        tableDoanhThu.setVisible(false);
    }

    @FXML
    private void hienThiBang(ActionEvent event) {
        chartDoanhThu.setVisible(false);
        tableDoanhThu.setVisible(true);
    }


}
