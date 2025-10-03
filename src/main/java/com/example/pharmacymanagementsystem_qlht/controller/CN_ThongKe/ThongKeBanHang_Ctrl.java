package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ThongKeBanHang_Ctrl extends Application {

    @FXML
    private Button btnBang;

    @FXML
    private Button btnBieuDo;

    @FXML
    private ComboBox<String> cboMQT;

    @FXML
    private ComboBox<String> cboThoiGian;

    @FXML
    private ComboBox<String> cboXuatfile;

    @FXML
    private AreaChart<?, ?> chartDoanhThu;

    @FXML
    private ToggleGroup date;

    @FXML
    private TableView<?> tableDoanhThu;


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
