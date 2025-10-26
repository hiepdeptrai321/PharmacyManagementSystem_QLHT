package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc.ChiTietThuoc_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
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
import java.util.List;

public class DanhMucThuoc_Ctrl extends Application {

    public TableColumn<Thuoc_SanPham,String> colSTT;
    public TableColumn<Thuoc_SanPham,String> colMaThuoc;
    public TableColumn<Thuoc_SanPham,String> colTenThuoc;
    public TableColumn<Thuoc_SanPham,String> colChiTiet;
    public TableColumn<Thuoc_SanPham,String> colHamLuong;
    public TableColumn<Thuoc_SanPham,String> colSDK_GPNK;
    public TableColumn<Thuoc_SanPham,String> colXuatXu;
    public TableColumn<Thuoc_SanPham,String> colLoaiHang;
    public TableColumn<Thuoc_SanPham,String> colViTri;
    public TableView<Thuoc_SanPham> tbl_Thuoc;
    public Button btnImport;
    public Button btnExport;
    public TextField tfTimThuoc;
    public Button btnTimThuoc;
    public Button btnThemThuoc;
    @FXML
    private Button btnLamMoi;
    Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
    List<Thuoc_SanPham> list;

//  2. Khởi tạo
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/DanhMucThuoc_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        loadTable();
        btnLamMoi.setOnAction(e-> LamMoi());
        tfTimThuoc.setOnAction(e-> timThuoc());
        btnTimThuoc.setOnAction(e-> timThuoc());
    }

//  3. Tải bảng
    public void loadTable() {
        list = thuocDao.selectAll();
        ObservableList<Thuoc_SanPham> data = FXCollections.observableArrayList(list);
        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbl_Thuoc.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<Thuoc_SanPham,String>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<Thuoc_SanPham,String>("tenThuoc"));
        colTenThuoc.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
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
        colHamLuong.setCellValueFactory(new PropertyValueFactory<Thuoc_SanPham,String>("hamLuongDonVi"));
        colHamLuong.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
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
        colSDK_GPNK.setCellValueFactory(new PropertyValueFactory<Thuoc_SanPham,String>("SDK_GPNK"));
        colXuatXu.setCellValueFactory(new PropertyValueFactory<Thuoc_SanPham,String>("nuocSX"));
        colXuatXu.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
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
        colLoaiHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLoaiHang()!=null?cellData.getValue().getLoaiHang().getTenLoaiHang():""));
        colLoaiHang.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
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
        colViTri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVitri()!=null?cellData.getValue().getVitri().getTenKe():""));
        colChiTiet.setCellFactory(col -> new TableCell<Thuoc_SanPham, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    Thuoc_SanPham thuoc = getTableView().getItems().get(getIndex());
                    btnCapNhat(thuoc);
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

        tbl_Thuoc.setItems(data);
    }

    public void timThuoc(){
        String keyword = tfTimThuoc.getText().trim().toLowerCase();
        Thuoc_SanPham_Dao ts_dao = new Thuoc_SanPham_Dao();
        List<Thuoc_SanPham> dsTSLoc = ts_dao.selectByTuKhoa(keyword);
        ObservableList<Thuoc_SanPham> data = FXCollections.observableArrayList(dsTSLoc);
        tbl_Thuoc.setItems(data);
    }

//  Thêm thuốc
    public void themthuoc(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/ThemThuoc_GUI.fxml"));
            Parent root = loader.load();
            ThemThuoc_Ctrl ctrl = loader.getController();
            ctrl.setParent(this);

            Stage dialog = new Stage();
            dialog.initOwner(tbl_Thuoc.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thêm thuốc");
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Mở giao diện cập nhật
    public void btnCapNhat(Thuoc_SanPham thuoc) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/SuaXoaThuoc_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            SuaXoaThuoc_Ctrl ctrl = loader.getController();
            ctrl.initialize(thuoc);
            ctrl.setParent(this);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refestTable(){
        loadTable();
    }

    @FXML
    private void LamMoi() {
        tfTimThuoc.clear();
        loadTable();
    }

    public void btnThemThuocByExcel(ActionEvent actionEvent) {
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/ThemThuocBangFileExcel_GUI.fxml"));
            Parent root = loader.load();
            Stage dialog = new Stage();
            dialog.initOwner(tbl_Thuoc.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thêm thuốc bằng file Excel");
            dialog.showAndWait();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
