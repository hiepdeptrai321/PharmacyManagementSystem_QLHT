package com.example.pharmacymanagementsystem_qlht.controller.ThongKe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ThongKe_GUI extends Application {
    private Button btnBang;
    private Button btnBieuDo;
    private AreaChart<String, Number> chartDoanhThu;
    private ToggleGroup date;
    private TableView<?> tableDoanhThu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ThongKeBanHang_Ctrl.class.getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml"));
        ThongKeBanHang_Ctrl thongkeBanHang_ctrl = new ThongKeBanHang_Ctrl();
        loader.setController(thongkeBanHang_ctrl);
        Pane root = loader.load();
<<<<<<< Updated upstream
        Scene scene = new Scene(root, 1200, 704);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm()
        );
=======
        Scene scene = new Scene(root, 1200, 750);
>>>>>>> Stashed changes
        primaryStage.setTitle("ThongKe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void initialize(URL location, ResourceBundle resources) {
        // managed sẽ ràng buộc theo visible
        chartDoanhThu.managedProperty().bind(chartDoanhThu.visibleProperty());
        tableDoanhThu.managedProperty().bind(tableDoanhThu.visibleProperty());
    }
    private void hienThiBieuDo(ActionEvent event) {
        chartDoanhThu.setVisible(true);
        tableDoanhThu.setVisible(false);
    }
    private void hienThiBang(ActionEvent event) {
        chartDoanhThu.setVisible(false);
        tableDoanhThu.setVisible(true);
    }
}
