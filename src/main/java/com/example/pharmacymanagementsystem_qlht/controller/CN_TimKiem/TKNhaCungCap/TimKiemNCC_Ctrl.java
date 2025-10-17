package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import com.example.pharmacymanagementsystem_qlht.dao.NhaCungCap_Dao;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;

public class TimKiemNCC_Ctrl extends Application {
    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)

    @FXML
    private ComboBox<String> cboTimKiem;

    @FXML
    private TextField txtTimKiem;

    @FXML
    private Button btnTim;

    @FXML
    private Button btnLamMoi;

    @FXML
    private TableColumn<NhaCungCap, String> cotDiaChi;

    @FXML
    private TableColumn<NhaCungCap, String> cotEmil;

    @FXML
    private TableColumn<NhaCungCap, String> cotMNCC;

    @FXML
    private TableColumn<NhaCungCap, String> cotSDT;

    @FXML
    private TableColumn<NhaCungCap, String> cotSTT;

    @FXML
    private TableColumn<NhaCungCap, String> cotTenNCC;
    @FXML
    private TableColumn<NhaCungCap, String> cotChiTiet;
    @FXML
    private TableView<NhaCungCap> tbNCC;
    private NhaCungCap_Dao nhaCungCapDao = new NhaCungCap_Dao();

    // 2. KHỞI TẠO (INITIALIZE)
    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Theo mã, tên nhà cung cấp",
                "Theo email",
                "Theo SDT"

        );
        cboTimKiem.setValue("Theo mã, tên nhà cung cấp");
        cotChiTiet.setCellFactory(col -> new TableCell<NhaCungCap, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    NhaCungCap ncc = getTableView().getItems().get(getIndex());
                    btnChiTietClick(ncc);
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
        loadTable();
        btnTim.setOnAction(e -> TimKiem());
        btnLamMoi.setOnAction(e -> LamMoi());

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKNhaCungCap/TKNCC_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/TimKiemNhanVien.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void loadTable() {
        List<NhaCungCap> list = nhaCungCapDao.selectAll();
        ObservableList<NhaCungCap> data = FXCollections.observableArrayList(list);
        cotSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbNCC.getItems().indexOf(cellData.getValue()) + 1))
        );
        cotMNCC.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
        cotTenNCC.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
        cotDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        cotSDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        cotEmil.setCellValueFactory(new PropertyValueFactory<>("email"));
        cotChiTiet.setCellFactory(col -> new TableCell<NhaCungCap, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    NhaCungCap ncc = getTableView().getItems().get(getIndex());
                    btnChiTietClick(ncc);
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
        tbNCC.setItems(data);

        }
    public void btnChiTietClick(NhaCungCap ncc) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKNhaCungCap/ChiTietNhaCungCap_GUI.fxml"));
            Parent root = loader.load();
            ChiTietNhaCungCap_Ctrl ctrl = loader.getController();
            ctrl.hienThiThongTin(ncc);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void TimKiem() {
        String criteria = cboTimKiem.getValue();
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        List<NhaCungCap> list = nhaCungCapDao.selectAll();
        List<NhaCungCap> filtered = list.stream().filter(ncc -> {
            switch (criteria) {
                case "Theo mã, tên nhà cung cấp":
                    return ncc.getMaNCC().toLowerCase().contains(keyword) ||
                            ncc.getTenNCC().toLowerCase().contains(keyword);
                case "Theo email":
                    return ncc.getEmail().toLowerCase().contains(keyword);
                case "Theo SDT":
                    return ncc.getSDT().toLowerCase().contains(keyword);
                default:
                    return true;
            }
        }).toList();
        tbNCC.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    private void LamMoi() {
        txtTimKiem.clear();
        cboTimKiem.setValue("Theo mã, tên nhà cung cấp");
        loadTable();
    }

}