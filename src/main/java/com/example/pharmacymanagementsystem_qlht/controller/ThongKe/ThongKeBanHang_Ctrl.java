package com.example.pharmacymanagementsystem_qlht.controller.ThongKe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ThongKeBanHang_Ctrl implements Initializable {

    @FXML
    private Button btnBang;

    @FXML
    private Button btnBieuDo;

    @FXML
    private AreaChart<String, Number> chartDoanhThu;

    @FXML
    private ToggleGroup date;

    @FXML
    private TableView<?> tableDoanhThu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // managed sẽ ràng buộc theo visible
        chartDoanhThu.managedProperty().bind(chartDoanhThu.visibleProperty());
        tableDoanhThu.managedProperty().bind(tableDoanhThu.visibleProperty());
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
}
