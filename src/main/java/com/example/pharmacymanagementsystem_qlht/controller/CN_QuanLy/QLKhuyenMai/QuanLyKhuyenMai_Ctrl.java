package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.util.List;

public class QuanLyKhuyenMai_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    @FXML
    public TableView<KhuyenMai> tbKM;
    public TextField tfTimKM;
    @FXML
    private Button btnthemKM;
    @FXML
    public TableColumn<KhuyenMai, String> colChiTiet;
    @FXML
    public TableColumn<KhuyenMai, String> colSTT;
    @FXML
    private TableColumn<KhuyenMai, String> colMaKM;
    @FXML
    private TableColumn<KhuyenMai, String> colTenKM;
    @FXML
    private TableColumn<KhuyenMai, String> colLoaiKM;
    @FXML
    private TableColumn<KhuyenMai, Float> colGiaTri;
    @FXML
    private TableColumn<KhuyenMai, java.sql.Date> colNBD;
    @FXML
    private TableColumn<KhuyenMai, java.sql.Date> colNKT;
    private KhuyenMai_Dao khuyenMaiDao = new KhuyenMai_Dao();

    // 2. KHỞI TẠO (INITIALIZE)
    public void initialize() {
        loadTable();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/QuanLyKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN

    public void loadTable() {

        List<KhuyenMai> list = khuyenMaiDao.selectAll();
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(list);
        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbKM.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
        colLoaiKM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLoaiKM().getMaLoai()));
        colGiaTri.setCellValueFactory(new PropertyValueFactory<>("giaTriKM"));
        colNBD.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNKT.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colChiTiet.setCellFactory(col -> new TableCell<KhuyenMai, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    KhuyenMai km = getTableView().getItems().get(getIndex());
                    btnChiTietClick(km);
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
        tbKM.setItems(data);
    }
    public void btnChiTietClick(KhuyenMai km) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/SuaXoaKhuyenMai_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            this.getClass();
            SuaXoaKhuyenMai_Ctrl ctrl = loader.getController();
            ctrl.loadData(km);
            ctrl.loadDatatbCTKM(new ChiTietKhuyenMai_Dao().selectByMaKM(km.getMaKM()));
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnThemKMClick() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/ThemKhuyenMai_GUI.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timKhuyenMai(){
        String keyword = tfTimKM.getText().trim().toLowerCase();
        KhuyenMai_Dao km_dao = new KhuyenMai_Dao();
        List<KhuyenMai> dsKMLoc = km_dao.selectByTuKhoa(keyword);
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(dsKMLoc);
        tbKM.setItems(data);
    }

    // 4. XỬ LÝ NGHIỆP VỤ
}
