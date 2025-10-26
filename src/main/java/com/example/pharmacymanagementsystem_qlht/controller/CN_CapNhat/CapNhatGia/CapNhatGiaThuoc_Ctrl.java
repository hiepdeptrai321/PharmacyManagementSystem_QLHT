package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatGia;

import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CapNhatGiaThuoc_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)

    public TextField tfTimThuoc;
    public Button btnTimThuoc;
    public TableView<Thuoc_SanPham> tbThuoc;
    public TableColumn<Thuoc_SanPham,String> colSTT;
    public TableColumn<Thuoc_SanPham,String> colMaThuoc;
    public TableColumn<Thuoc_SanPham,String> colTenThuoc;
    public TableColumn<Thuoc_SanPham,String> colDVT;
    public TableColumn<Thuoc_SanPham,String> colGiaNhap;
    public TableColumn<Thuoc_SanPham,String> colGiaBan;
    public TableColumn<Thuoc_SanPham,String> colChiTiet;
    public Button btnReset;

    // 2. KHỞI TẠO (INITIALIZE)

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/CapNhatGiaThuoc_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void initialize() {
        loadTable();
        tfTimThuoc.setOnAction(e-> timThuoc());
        btnReset.setOnAction(e-> LamMoi());
    }

    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN

    public void loadTable() {
        Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
        List<Thuoc_SanPham> list = thuocDao.selectAllSLTheoDonViCoBan_ChiTietDVT();
        ObservableList<Thuoc_SanPham> data = FXCollections.observableArrayList(list);

        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbThuoc.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        vnFormat.setGroupingUsed(true);
        vnFormat.setMaximumFractionDigits(0);

        colGiaNhap.setCellValueFactory(cd -> {
            Object val = cd.getValue().getGiaNhapCoBan();
            if (val == null) return new SimpleStringProperty("");
            Number num;
            if (val instanceof Number) num = (Number) val;
            else {
                try { num = Double.parseDouble(val.toString()); }
                catch (Exception e) { return new SimpleStringProperty(""); }
            }
            return new SimpleStringProperty(vnFormat.format(num));
        });

        colGiaBan.setCellValueFactory(cd -> {
            Object val = cd.getValue().getGiaBanCoBan();
            if (val == null) return new SimpleStringProperty("");
            Number num;
            if (val instanceof Number) num = (Number) val;
            else {
                try { num = Double.parseDouble(val.toString()); }
                catch (Exception e) { return new SimpleStringProperty(""); }
            }
            return new SimpleStringProperty(vnFormat.format(num));
        });
        colDVT.setCellValueFactory(new PropertyValueFactory<>("tenDVTCoBan"));
        colChiTiet.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
                btn.setOnAction(event -> {
                    Thuoc_SanPham thuoc = getTableView().getItems().get(getIndex());
                    showSuaGiaThuoc(thuoc);
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tbThuoc.setItems(data);
    }

    // 4. XỬ LÝ NGHIỆP VỤ

    public void timThuoc() {
        String keyword = tfTimThuoc.getText().trim().toLowerCase();
        Thuoc_SanPham_Dao ts_dao = new Thuoc_SanPham_Dao();
        List<Thuoc_SanPham> dsTSLoc;
        if (keyword.isEmpty()) {
            dsTSLoc = ts_dao.selectAllSLTheoDonViCoBan_ChiTietDVT();
        } else {
            dsTSLoc = ts_dao.selectSLTheoDonViCoBanByTuKhoa_ChiTietDVT(keyword);
        }
        ObservableList<Thuoc_SanPham> data = FXCollections.observableArrayList(dsTSLoc);
        tbThuoc.setItems(data);
    }

    private void showSuaGiaThuoc(Thuoc_SanPham thuoc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/SuaGiaThuoc_GUI.fxml"));
            Parent root = loader.load();
            SuaGiaThuoc_Ctrl controller = loader.getController();
            controller.setThuoc(thuoc); // Implement setThuoc in SuaGiaThuoc_Ctrl
            Stage stage = new Stage();
            stage.setTitle("Sửa giá thuốc");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            stage.setOnHidden(e-> loadTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void LamMoi() {
        tfTimThuoc.clear();
        loadTable();
    }
}
