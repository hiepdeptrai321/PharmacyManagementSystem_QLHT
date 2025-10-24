package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

import com.example.pharmacymanagementsystem_qlht.dao.ThongKe_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeSanPham;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.scene.chart.CategoryAxis;
import java.net.URL;
import java.util.ResourceBundle;

public class ThongKeBanHang_Ctrl extends Application implements Initializable {

    // --- 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML) ---
    // (Đã sửa lại kiểu dữ liệu)
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
    private BarChart<String, Number> chartDoanhThu;
    @FXML
    private ToggleGroup date;



    @FXML
    private TableView<ThongKeBanHang> tableDoanhThu;
    @FXML
    private TableColumn<ThongKeBanHang, String> cotTG;
    @FXML
    private TableColumn<ThongKeBanHang, Integer> cotSLHoaDon;
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotTongGT;
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotGG;
    @FXML
    private TableColumn<ThongKeBanHang, Integer> cotDT; // Số lượng đơn trả
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotGTDonTra;
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotDoanhThu;
    @FXML
    private CategoryAxis xAxis;


    @FXML
    private TableView<ThongKeSanPham> tableTopSanPham;
    @FXML
    private TableColumn<ThongKeSanPham, String> cotMaThuoc;
    @FXML
    private TableColumn<ThongKeSanPham, String> cotTenThuoc;
    @FXML
    private TableColumn<ThongKeSanPham, Integer> cotSL;
    @FXML
    private TableColumn<ThongKeSanPham, Double> cotThanhTien;

    // --- 4. KHAI BÁO BIẾN XỬ LÝ LOGIC ---
    private ThongKe_Dao tkDao = new ThongKe_Dao();
    private ObservableList<ThongKeBanHang> listThongKe;
    private ObservableList<ThongKeSanPham> listTopSanPham;


    // --- 2. KHỞI TẠO (INITIALIZE) ---
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chartDoanhThu.managedProperty().bind(chartDoanhThu.visibleProperty());
        tableDoanhThu.managedProperty().bind(tableDoanhThu.visibleProperty());


        cboMQT.getItems().addAll("Lợi nhuận", "Nhân viên", "Khách hàng");
        cboThoiGian.getItems().addAll("Hôm nay", "Tuần này", "Tháng này", "Quý này","Tùy chọn");
        cboXuatfile.getItems().addAll("Excel", "PDF");


        cotTG.setCellValueFactory(new PropertyValueFactory<>("thoiGian"));
        cotSLHoaDon.setCellValueFactory(new PropertyValueFactory<>("soLuongHoaDon"));
        cotTongGT.setCellValueFactory(new PropertyValueFactory<>("tongGiaTri"));
        cotGG.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
        cotDT.setCellValueFactory(new PropertyValueFactory<>("soLuongDonTra"));
        cotGTDonTra.setCellValueFactory(new PropertyValueFactory<>("giaTriDonTra"));
        cotDoanhThu.setCellValueFactory(new PropertyValueFactory<>("doanhThu"));


        cotMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        cotTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        cotSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        cotThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        // --- Thêm Listener cho cboThoiGian ---
        cboThoiGian.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                loadData(newValue);
            }
        });


        cboThoiGian.setValue("Hôm nay");
        chartDoanhThu.setAnimated(false);
    }


    private void loadData(String thoiGian) {

        listTopSanPham = FXCollections.observableArrayList(tkDao.getTop5SanPham(thoiGian));
        tableTopSanPham.setItems(listTopSanPham);


        chartDoanhThu.getData().clear();



        if (thoiGian.equals("Hôm nay")) {



            listThongKe = FXCollections.observableArrayList(tkDao.getThongKeBanHang(thoiGian));
            tableDoanhThu.setItems(listThongKe);


            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu theo sản phẩm");

            for (ThongKeSanPham sp : listTopSanPham) {

                series.getData().add(new XYChart.Data<>(sp.getTenThuoc(), sp.getThanhTien()));
            }
            chartDoanhThu.getData().add(series);

        } else {

            listThongKe = FXCollections.observableArrayList(tkDao.getThongKeBanHang(thoiGian));
            tableDoanhThu.setItems(listThongKe);


            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu");

            for (ThongKeBanHang tk : listThongKe) {
                series.getData().add(new XYChart.Data<>(tk.getThoiGian(), tk.getDoanhThu()));
            }
            chartDoanhThu.getData().add(series);
        }
    }


    // --- 3. XỬ LÝ SỰ KIỆN GIAO DIỆN ---
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


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}