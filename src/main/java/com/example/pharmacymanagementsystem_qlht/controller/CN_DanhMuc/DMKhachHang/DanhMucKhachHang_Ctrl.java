package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang;

import com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKeHang.XoaSuaKeHang_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.KhachHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class DanhMucKhachHang_Ctrl extends Application {
    @FXML
    private Button btnTim;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnthemKH;

    @FXML
    private TableColumn<KhachHang, String> cotChiTiet;

    @FXML
    private TableColumn<KhachHang, String> cotGioiTinh;

    @FXML
    private TableColumn<KhachHang, String> cotMaKH;

    @FXML
    private TableColumn<KhachHang, String> cotDiaChi;

    @FXML
    private TableColumn<KhachHang, String> cotSDT;

    @FXML
    private TableColumn<KhachHang, String> cotSTT;

    @FXML
    private TableColumn<KhachHang, String> cotTenKH;

    @FXML
    private TableView<KhachHang> tbKhachHang;

    private KhachHang_Dao khachHangDao = new KhachHang_Dao();
    @FXML
    private TextField txtTim;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/DanhMucKhachHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/QuanLyThuoc.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        loadTable();
        btnLamMoi.setOnAction(e -> LamMoi());
        btnTim.setOnAction(e -> TimKiem());
        btnthemKH.setOnAction(e -> btnThemClick(new KhachHang()));
        txtTim.setOnAction(e -> TimKiem());


    }
    public void loadTable() {
        List<KhachHang> list = khachHangDao.selectAll();
        ObservableList<KhachHang> data = FXCollections.observableArrayList(list);
        cotSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbKhachHang.getItems().indexOf(cellData.getValue()) + 1))
        );
        cotMaKH.setCellValueFactory(new PropertyValueFactory<>("MaKH"));
        cotTenKH.setCellValueFactory(new PropertyValueFactory<>("TenKH"));
        cotTenKH.setCellFactory(col -> new TableCell<KhachHang, String>() {
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
        cotGioiTinh.setCellValueFactory(cellData -> {
            Boolean gt = cellData.getValue().getGioiTinh();
            String gioiTinhText = (gt != null && gt) ? "Nam" : "Nữ";
            return new SimpleStringProperty(gioiTinhText);
        });
        cotGioiTinh.setCellFactory(col -> new TableCell<KhachHang, String>() {
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

        cotDiaChi.setCellValueFactory(new PropertyValueFactory<>("DiaChi"));
        cotDiaChi.setCellFactory(col -> new TableCell<KhachHang, String>() {
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
        cotSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));

        cotChiTiet.setCellFactory(col -> new TableCell<KhachHang, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    KhachHang kh = getTableView().getItems().get(getIndex());
                    btnChiTietClick(kh);
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
        tbKhachHang.setItems(data);
    }
    public void btnChiTietClick(KhachHang kh) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/SuaXoaKhachHang_GUI.fxml"));
            Parent root = loader.load();
            ChiTietKhachHang_Ctrl ctrl = loader.getController();
            ctrl.hienThiThongTin(kh);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnThemClick(KhachHang kh) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/ThemKhachHang_GUI.fxml"));
            Parent root = loader.load();
            //ChiTietNhaCungCap_Ctrl ctrl = loader.getController();
            //ctrl.hienThiThongTin(ncc);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void LamMoi() {
        txtTim.clear();
        loadTable();
    }
    private void TimKiem() {
        String keyword = txtTim.getText().trim().toLowerCase();
        List<KhachHang> list = khachHangDao.selectAll();
        if (keyword.isEmpty()) {
            tbKhachHang.setItems(FXCollections.observableArrayList(list));
            return;
        }


        List<KhachHang> filtered = list.stream()
                .filter(keHang ->
                        (keHang.getMaKH() != null && keHang.getMaKH().toLowerCase().contains(keyword)) ||
                                (keHang.getTenKH() != null && keHang.getTenKH().toLowerCase().contains(keyword))

                )
                .toList();

        tbKhachHang.setItems(FXCollections.observableArrayList(filtered));
    }

}
