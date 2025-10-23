package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhanVien;

import com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc.SuaXoaThuoc_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.NhanVien_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DanhMucNhanVien_Ctrl extends Application {

    public TableView<NhanVien> tblNhanVien;
    public TableColumn<NhanVien, String> colSTT;
    public TableColumn<NhanVien, String> colMaNV;
    public TableColumn<NhanVien, String> colTenNV;
    public TableColumn<NhanVien, String> colGioiTinh;
    public TableColumn<NhanVien, String> colSDT;
    public TableColumn<NhanVien, String> colNgaySinh;
    public TableColumn<NhanVien, String> colEmail;
    public TableColumn<NhanVien, String> colDiaChi;
    public TableColumn<NhanVien, String> colTrangThai;
    public TableColumn<NhanVien, String> colCapNhat;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/DanhMucNhanVien_GUI.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        loadData();
    }

    public void loadData() {
        List<NhanVien> listTemp = new NhanVien_Dao().selectAll();
        List<NhanVien> list = new ArrayList<>();
        for(NhanVien nv : listTemp){
            if(!nv.isTrangThaiXoa()){
                list.add(nv);
            }
        }
        ObservableList<NhanVien> data = FXCollections.observableList(list);


        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tblNhanVien.getItems().indexOf(cellData.getValue()) + 1))
        );

        colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        colTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        colGioiTinh.setCellValueFactory(cellData -> {
            boolean gioiTinh = cellData.getValue().isGioiTinh();
            String text = gioiTinh ? "Nữ" : "Nam";
            return new SimpleStringProperty(text);
        });
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colTrangThai.setCellValueFactory(cellData -> {
            boolean trangThai = cellData.getValue().isTrangThai();
            String text = trangThai ? "Đang làm việc" : "Đã nghỉ việc";
            return new SimpleStringProperty(text);
        });
        colCapNhat.setCellFactory(col -> new TableCell<NhanVien, String>() {
            private final Button btn = new Button("Cập nhật");
            {
                btn.setOnAction(event -> {
                    NhanVien nhanVien = getTableView().getItems().get(getIndex());
                    btnCapNhat(nhanVien);
                });
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        tblNhanVien.setItems(data);
    }

    public void btnCapNhat(NhanVien nhanVien) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/SuaXoaNhanVien_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            SuaXoaNhanVien_Ctrl ctrl = loader.getController();
            NhanVien copy = new NhanVien(nhanVien);
            ctrl.initialize(copy);
            ctrl.setParent(this);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btnThemNhanVien(ActionEvent actionEvent) {
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/ThemNhanVien_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            ThemNhanVien_Ctrl ctrl = loader.getController();
            ctrl.setParent(this);


            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
