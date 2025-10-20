package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKeHang;

import com.example.pharmacymanagementsystem_qlht.dao.KeHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class XoaSuaKeHang_Ctrl extends Application {

    @FXML
    private Pane btnLuu;

    @FXML
    private Pane btnXoa;

    @FXML
    private TextArea txtMota;

    @FXML
    private TextField txtTenKe;

    private KeHang_Dao keHangDao = new KeHang_Dao();
    private Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
    private KeHang keHangHienTai;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/XoaSuakeHang.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThemNhaCungCap.css" + "").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        btnLuu.setOnMouseClicked(event -> luuKeHang());
        btnXoa.setOnMouseClicked(event -> xoaKeHang());
    }


    public void hienThiThongTin(KeHang kh) {
        if (kh != null) {
            keHangHienTai = kh;
            txtTenKe.setText(kh.getTenKe());
            txtMota.setText(kh.getMoTa() != null ? kh.getMoTa() : "");

        }
    }
    private void luuKeHang() {
        if (keHangHienTai == null) {
            showAlert("Lỗi", "Không có dữ liệu kệ hàng để cập nhật!", Alert.AlertType.ERROR);
            return;
        }

        String tenKe = txtTenKe.getText().trim();
        String moTa = txtMota.getText().trim();

        if (tenKe.isEmpty()) {
            showAlert("Lỗi", "Tên kệ không được để trống!", Alert.AlertType.ERROR);
            return;
        }

        keHangHienTai.setTenKe(tenKe);
        keHangHienTai.setMoTa(moTa);

        boolean success = keHangDao.update(keHangHienTai);
        if (success) {
            showAlert("Thành công", "Cập nhật kệ hàng thành công!", Alert.AlertType.INFORMATION);
            dongCuaSo();
        } else {
            showAlert("Thất bại", "Không thể cập nhật kệ hàng!", Alert.AlertType.ERROR);
        }
    }

    private void xoaKeHang() {
        if (keHangHienTai == null) {
            showAlert("Lỗi", "Không có dữ liệu kệ hàng để xóa!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Lấy danh sách thuốc còn trong kệ
            List<String> thuocTrongKe = thuocDao.layDanhSachThuocTheoKe(keHangHienTai.getMaKe());

            if (thuocTrongKe != null && !thuocTrongKe.isEmpty()) {
                showThuocConTrongKe(thuocTrongKe);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận");
            confirm.setHeaderText("Bạn có chắc muốn xóa kệ này?");
            confirm.setContentText("Tên kệ: " + keHangHienTai.getTenKe());
            if (confirm.showAndWait().orElse(null) != ButtonType.OK) {
                return;
            }

            // Thực hiện xóa
            boolean success = keHangDao.deleteById(keHangHienTai.getMaKe());
            if (success) {
                showAlert("Thành công", "Đã xóa kệ hàng thành công!", Alert.AlertType.INFORMATION);
                dongCuaSo();
            } else {
                showAlert("Thất bại", "Không thể xóa kệ hàng!", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống", "Đã xảy ra lỗi khi xóa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showThuocConTrongKe(List<String> thuocTrongKe) {
        StringBuilder sb = new StringBuilder("Kệ này vẫn còn các thuốc sau:\n\n");
        for (String tenThuoc : thuocTrongKe) {
            sb.append("- ").append(tenThuoc).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Không thể xóa kệ hàng");
        alert.setHeaderText("Không thể xóa! Kệ này vẫn còn thuốc.");

        // Tạo TextArea có scrollbar
        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(250);

        alert.getDialogPane().setContent(textArea);

        alert.showAndWait();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void dongCuaSo() {
        Stage stage = (Stage) txtTenKe.getScene().getWindow();
        stage.close();
    }
}
