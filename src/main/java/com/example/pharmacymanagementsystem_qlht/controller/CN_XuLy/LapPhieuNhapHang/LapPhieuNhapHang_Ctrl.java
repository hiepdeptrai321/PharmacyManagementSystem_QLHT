package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuNhapHang;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LapPhieuNhapHang_Ctrl extends Application {
    @FXML
    private TextField timKiemThuocC;
    @FXML
    private ListView<?> listViewThuoc;

    @FXML
    public void initialize() {
        listViewThuoc.setVisible(false);

        timKiemThuocC.focusedProperty().addListener((obs, oldVal, newVal) -> {
            listViewThuoc.setVisible(newVal);
        });
    }

    public void themPhieuNhapHang(MouseEvent mouseEvent) {
        System.out.println("Thêm phiếu nhập hàng");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/LapPhieuNhapHang_GUI.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}