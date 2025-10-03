package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class TimKiemKhachHang_Ctrl {

    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên khách hàng",
                "Theo email",
                "Theo SDT"

        );
    }
}
