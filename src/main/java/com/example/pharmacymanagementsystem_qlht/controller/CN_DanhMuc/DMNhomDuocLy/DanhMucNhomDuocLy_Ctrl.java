package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhomDuocLy;

import com.example.pharmacymanagementsystem_qlht.dao.NhomDuocLy_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhomDuocLy;
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

public class DanhMucNhomDuocLy_Ctrl extends Application {
    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    @FXML
    private Button btnThem;

    @FXML
    private Button btnLamMoi;

    @FXML
    private Button btnTim;

    @FXML
    private Button btnXoa;

    @FXML
    private TableColumn<NhomDuocLy, String> cotMaNDL;

    @FXML
    private TableColumn<NhomDuocLy, String> cotSTT;

    @FXML
    private TableColumn<NhomDuocLy, String> cotTenNDL;

    @FXML
    private TableColumn<NhomDuocLy, String> colChiTiet;

    @FXML
    private TableView<NhomDuocLy> tbNhomDuocLy;

    @FXML
    private TextField txtTimKiem;

    private NhomDuocLy_Dao nhomDuocLyDao = new NhomDuocLy_Dao();

    // 2. KHỞI TẠO (INITIALIZE)

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhomDuocLy/DanhMucNhomDuocLy.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/QuanLyKeHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        loadTable();
        btnTim.setOnAction(e -> TimKiem());
        btnLamMoi.setOnAction(e -> LamMoi());
        btnThem.setOnAction(e -> btnThemClick(new NhomDuocLy()));
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void loadTable() {
        List<NhomDuocLy> list = nhomDuocLyDao.selectAll();
        ObservableList<NhomDuocLy> data = FXCollections.observableArrayList(list);
        cotSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbNhomDuocLy.getItems().indexOf(cellData.getValue()) + 1))
        );
        cotMaNDL.setCellValueFactory(new PropertyValueFactory<>("maNDL"));
        cotTenNDL.setCellValueFactory(new PropertyValueFactory<>("tenNDL"));
        cotTenNDL.setCellFactory(col -> new TableCell<NhomDuocLy, String>() {
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
        colChiTiet.setCellFactory(col -> new TableCell<NhomDuocLy, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    NhomDuocLy ndl = getTableView().getItems().get(getIndex());
                    btnChiTietClick(ndl);
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
        tbNhomDuocLy.setItems(data);

    }
    private void TimKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        List<NhomDuocLy> list = nhomDuocLyDao.selectAll();
        if (keyword.isEmpty()) {
            tbNhomDuocLy.setItems(FXCollections.observableArrayList(list));
            return;
        }


        List<NhomDuocLy> filtered = list.stream()
                .filter(NhomDuocLy ->
                        (NhomDuocLy.getMaNDL() != null && NhomDuocLy.getMaNDL().toLowerCase().contains(keyword)) ||
                                (NhomDuocLy.getTenNDL() != null && NhomDuocLy.getTenNDL().toLowerCase().contains(keyword))

                )
                .toList();
        tbNhomDuocLy.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    private void LamMoi() {
        txtTimKiem.clear();
        loadTable();
    }
    public void btnThemClick(NhomDuocLy ndl) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhomDuocLy/ThemNhomDuocLy.fxml"));
            Parent root = loader.load();
            //ChiTietNhaCungCap_Ctrl ctrl = loader.getController();
           // ctrl.hienThiThongTin(ncc);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void btnChiTietClick(NhomDuocLy ndl) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhomDuocLy/XoaSuaNhomDuocLy.fxml"));
            Parent root = loader.load();
            SuaXoaNhomDuocLy ctrl = loader.getController();
            ctrl.hienThiThongTin(ndl);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
