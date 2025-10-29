package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKeHang;

import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap.ChiTietNhaCungCap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.KeHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.application.Platform;
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

public class DanhMucKeHang_Ctrl extends Application {
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
    private TableColumn<KeHang, String> cotMaKe;

    @FXML
    private TableColumn<KeHang, String> cotSTT;

    @FXML
    private TableColumn<KeHang, String> cotTenKe;

    @FXML
    private TableColumn<KeHang, String> colChiTiet;

    @FXML
    private TableView<KeHang> tblKeHang;

    @FXML
    private TextField txtTimKiem;

    private KeHang_Dao keHangDao = new KeHang_Dao();

    // 2. KHỞI TẠO (INITIALIZE)

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/DanhMucKeHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/QuanLyKeHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        btnTim.setOnAction(e -> TimKiem());
        btnLamMoi.setOnAction(e -> LamMoi());
        btnThem.setOnAction(e -> btnThemClick(new KeHang()));
        txtTimKiem.setOnAction(e -> TimKiem());
        Platform.runLater(()->{
            loadTable();
        });
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void loadTable() {
        List<KeHang> list = keHangDao.selectAll();
        ObservableList<KeHang> data = FXCollections.observableArrayList(list);
        cotSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tblKeHang.getItems().indexOf(cellData.getValue()) + 1))
        );
        cotMaKe.setCellValueFactory(new PropertyValueFactory<>("maKe"));
        cotTenKe.setCellValueFactory(new PropertyValueFactory<>("tenKe"));
        cotTenKe.setCellFactory(col -> new TableCell<KeHang, String>() {
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
        colChiTiet.setCellFactory(col -> new TableCell<KeHang, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    KeHang kh = getTableView().getItems().get(getIndex());
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
        tblKeHang.setItems(data);

    }
    private void TimKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        List<KeHang> list = keHangDao.selectAll();
        if (keyword.isEmpty()) {
            tblKeHang.setItems(FXCollections.observableArrayList(list));
            return;
        }


        List<KeHang> filtered = list.stream()
                .filter(keHang ->
                        (keHang.getMaKe() != null && keHang.getMaKe().toLowerCase().contains(keyword)) ||
                                (keHang.getTenKe() != null && keHang.getTenKe().toLowerCase().contains(keyword))

                )
                .toList();

        tblKeHang.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    private void LamMoi() {
        txtTimKiem.clear();
        loadTable();
    }
    public void btnThemClick(KeHang ke) {
        try {
            Stage dialog = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/ThemKe.fxml"));
            Parent root = loader.load();
            //ChiTietNhaCungCap_Ctrl ctrl = loader.getController();
            //ctrl.hienThiThongTin(ncc);
            dialog.initOwner(btnLamMoi.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thêm kệ hàng");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void btnChiTietClick(KeHang kh) {
        try {
            Stage dialog = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/XoaSuakeHang.fxml"));
            Parent root = loader.load();
            XoaSuaKeHang_Ctrl ctrl = loader.getController();
            ctrl.hienThiThongTin(kh);
            dialog.initOwner(btnLamMoi.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Chi tiết kệ hàng");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
