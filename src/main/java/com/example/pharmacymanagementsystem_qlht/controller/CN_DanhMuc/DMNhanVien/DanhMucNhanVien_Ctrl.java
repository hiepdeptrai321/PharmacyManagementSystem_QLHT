package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhanVien;

import com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc.SuaXoaThuoc_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.NhanVien_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    private Button btnLamMoi;
    @FXML
    private Button btnTim;
    @FXML
    private TextField txtTim;
    private NhanVien_Dao nhanVienDao = new NhanVien_Dao();


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/DanhMucNhanVien_GUI.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        txtTim.setOnAction(e->TimKiem());
        btnTim.setOnAction(e->TimKiem());
        btnLamMoi.setOnAction(e-> LamMoi());
        Platform.runLater(()->{
            loadData();
        });
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
        colTenNV.setCellFactory(col -> new TableCell<NhanVien, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
        colGioiTinh.setCellValueFactory(cellData -> {
            boolean gioiTinh = cellData.getValue().isGioiTinh();
            String text = gioiTinh ? "Nữ" : "Nam";
            return new SimpleStringProperty(text);
        });
        colGioiTinh.setCellFactory(col -> new TableCell<NhanVien, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setCellFactory(col -> new TableCell<NhanVien, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colDiaChi.setCellFactory(col -> new TableCell<NhanVien, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
        colTrangThai.setCellValueFactory(cellData -> {
            boolean trangThai = cellData.getValue().isTrangThai();
            String text = trangThai ? "Đang làm việc" : "Đã nghỉ việc";
            return new SimpleStringProperty(text);
        });
        colTrangThai.setCellFactory(col -> new TableCell<NhanVien, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/SuaXoaNhanVien_GUI.fxml"));
            Parent root = loader.load();

            SuaXoaNhanVien_Ctrl ctrl = loader.getController();
            NhanVien copy = new NhanVien(nhanVien);
            ctrl.initialize(copy);
            ctrl.setParent(this);

            Stage dialog = new Stage();
            dialog.initOwner(txtTim.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Cập nhật nhân viên");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btnThemNhanVien(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/ThemNhanVien_GUI.fxml"));
            Parent root = loader.load();

            ThemNhanVien_Ctrl ctrl = loader.getController();
            ctrl.setParent(this);

            Stage dialog = new Stage();
            dialog.initOwner(txtTim.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thêm nhân viên");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void TimKiem() {
        String keyword = txtTim.getText().trim().toLowerCase();
        List<NhanVien> list = nhanVienDao.selectAll();
        if (keyword.isEmpty()) {
            tblNhanVien.setItems(FXCollections.observableArrayList(list));
            return;
        }


        List<NhanVien> filtered = list.stream()
                .filter(nhanVien ->
                        (nhanVien.getMaNV() != null && nhanVien.getMaNV().toLowerCase().contains(keyword)) ||
                                (nhanVien.getTenNV() != null && nhanVien.getTenNV().toLowerCase().contains(keyword))

                )
                .toList();

        tblNhanVien.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    private void LamMoi() {
        txtTim.clear();
        loadData();
    }
}
