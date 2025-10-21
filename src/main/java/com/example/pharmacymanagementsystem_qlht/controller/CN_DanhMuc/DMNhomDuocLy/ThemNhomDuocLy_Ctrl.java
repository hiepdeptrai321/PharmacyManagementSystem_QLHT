package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhomDuocLy;

import com.example.pharmacymanagementsystem_qlht.dao.KeHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.NhomDuocLy_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.NhomDuocLy;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ThemNhomDuocLy_Ctrl extends Application {
    @FXML
    private Button btnHuy;

    @FXML
    private Button btnThem;

    @FXML
    private TextArea txtMoTa;

    @FXML
    private TextField txtTenNhomDuocLy;

    private NhomDuocLy_Dao nhomDuocLyDao = new NhomDuocLy_Dao();


    @FXML
    public void initialize() {
        btnThem.setOnAction(e -> themNDL());
        btnHuy.setOnAction(e -> btnHuyClick());
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhomDuocLy/ThemNhomDuocLy.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThemNhaCungCap.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void btnHuyClick(){
        Stage stage = (Stage) txtTenNhomDuocLy.getScene().getWindow();
        stage.close();
    }

    @FXML
    void themNDL() {
        // Sinh mã tự động
        String maNDL = nhomDuocLyDao.generateNewMaNhomDL();
        String tenNDL = txtTenNhomDuocLy.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (tenNDL.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập tên nhóm dược lý!");
            return;
        }

        NhomDuocLy nhomDuocLy = new NhomDuocLy(maNDL, tenNDL, moTa);
        boolean success = nhomDuocLyDao.insert(nhomDuocLy);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm nhóm dược lý thành công!\nMã: " + maNDL);
            txtTenNhomDuocLy.clear();
            txtMoTa.clear();
            dongCuaSo();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm nhoóm dược lý thất bại!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void dongCuaSo() {
        Stage stage = (Stage) txtTenNhomDuocLy.getScene().getWindow();
        stage.close();
    }
}
