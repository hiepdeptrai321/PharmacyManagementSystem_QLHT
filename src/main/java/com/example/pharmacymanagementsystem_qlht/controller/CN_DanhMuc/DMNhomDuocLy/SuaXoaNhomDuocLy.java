package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhomDuocLy;

import com.example.pharmacymanagementsystem_qlht.dao.KeHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.NhomDuocLy_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import com.example.pharmacymanagementsystem_qlht.model.NhomDuocLy;
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

public class SuaXoaNhomDuocLy extends Application {

    @FXML
    private Pane btnLuu;

    @FXML
    private Pane btnXoa;

    @FXML
    private TextArea txtMota;

    @FXML
    private TextField txtTenNDL;

    private NhomDuocLy_Dao nhomDuocLyDao = new NhomDuocLy_Dao();
    private Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
    private NhomDuocLy nhomDuocLyhientai;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhomDuocLy/XoaSuaNhomDuocLy.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThemNhaCungCap.css" + "").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        btnLuu.setOnMouseClicked(event -> luuNDL());
        btnXoa.setOnMouseClicked(event -> xoaNDL());
    }


    public void hienThiThongTin(NhomDuocLy ndl) {
        if (ndl != null) {
            nhomDuocLyhientai = ndl;
            txtTenNDL.setText(ndl.getTenNDL());
            txtMota.setText(ndl.getMoTa() != null ? ndl.getMoTa() : "");

        }
    }
    private void luuNDL() {
        if (nhomDuocLyhientai == null) {
            showAlert("Lỗi", "Không có dữ liệu kệ hàng để cập nhật!", Alert.AlertType.ERROR);
            return;
        }

        String tenNDL = txtTenNDL.getText().trim();
        String moTa = txtMota.getText().trim();

        if (tenNDL.isEmpty()) {
            showAlert("Lỗi", "Tên nhóm dược lý không được để trống!", Alert.AlertType.ERROR);
            return;
        }

        nhomDuocLyhientai.setTenNDL(tenNDL);
        nhomDuocLyhientai.setMoTa(moTa);

        boolean success = nhomDuocLyDao.update(nhomDuocLyhientai);
        if (success) {
            showAlert("Thành công", "Cập nhật nhóm dược lý hàng thành công!", Alert.AlertType.INFORMATION);
            dongCuaSo();
        } else {
            showAlert("Thất bại", "Không thể cập nhật nhóm dược lý!", Alert.AlertType.ERROR);
        }
    }

    private void xoaNDL() {
        if (nhomDuocLyhientai == null) {
            showAlert("Lỗi", "Không có dữ liệu để xóa!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Lấy danh sách thuốc còn trong kệ
            List<String> thuocTrongKe = thuocDao.layDanhSachThuocTheoKe(nhomDuocLyhientai.getMaNDL());

            if (thuocTrongKe != null && !thuocTrongKe.isEmpty()) {
                showThuocConTrongKe(thuocTrongKe);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận");
            confirm.setHeaderText("Bạn có chắc muốn xóa nhóm dược lý này?");
            confirm.setContentText("Tên nhóm dược lý: " + nhomDuocLyhientai.getTenNDL());
            if (confirm.showAndWait().orElse(null) != ButtonType.OK) {
                return;
            }

            // Thực hiện xóa
            boolean success = nhomDuocLyDao.deleteById(nhomDuocLyhientai.getMaNDL());
            if (success) {
                showAlert("Thành công", "Đã xóa nhóm dược lý thành công!", Alert.AlertType.INFORMATION);
                dongCuaSo();
            } else {
                showAlert("Thất bại", "Không thể xóa nhóm dược lý!", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống", "Đã xảy ra lỗi khi xóa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showThuocConTrongKe(List<String> thuocTrongKe) {
        StringBuilder sb = new StringBuilder("Nhóm dược lý này vẫn còn các thuốc sau:\n\n");
        for (String tenThuoc : thuocTrongKe) {
            sb.append("- ").append(tenThuoc).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Không thể xóa nhóm dược lý");
        alert.setHeaderText("Không thể xóa! nhóm dược lý này vẫn còn thuốc.");

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
        Stage stage = (Stage) txtTenNDL.getScene().getWindow();
        stage.close();
    }
}
