package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.dao.NhaCungCap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
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
import java.util.Objects;

public class DanhMucNhaCungCap_Ctrl extends Application {

    public TableColumn colChiTietNhaCungCap;
    public TableColumn<NhaCungCap, String> colChiTiet;
    @FXML
    private TableView<NhaCungCap> tblNhaCungCap;
    @FXML
    private TableColumn<NhaCungCap, String> colMaNCC;
    @FXML
    private TableColumn<NhaCungCap, String> colTenNCC;
    @FXML
    private TableColumn<NhaCungCap, String> colDiaChi;
    @FXML
    private TableColumn<NhaCungCap, String> colSDT;
    @FXML
    private TableColumn<NhaCungCap, String> colEmail;
    @FXML
    private TableColumn<NhaCungCap, String> colGhiChu;
    @FXML
    private TableColumn<NhaCungCap, String> colTenCongTy;
    @FXML
    private TableColumn<NhaCungCap, String> colSTT;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private Button btnLamMoi;
    @FXML
    private NhaCungCap_Dao nhaCungCapDao =  new NhaCungCap_Dao();
    @FXML
    private Button btnTim;


//  Phương thức khởi tạo
    @FXML
    public void initialize() {
        loadNhaCungCap();
        btnLamMoi.setOnAction(e-> LamMoi());
        btnTim.setOnAction(e-> TimKiem());
        txtTimKiem.setOnAction(e-> TimKiem());
    }

//  Load nhà cung cấp vào bảng
    public void loadNhaCungCap() {
        List<NhaCungCap> list = new NhaCungCap_Dao().selectAll();
        ObservableList<NhaCungCap> data = FXCollections.observableArrayList(list);

        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tblNhaCungCap.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaNCC.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
        colTenNCC.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
        colTenNCC.setCellFactory(col -> new TableCell<NhaCungCap, String>() {
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
        colSDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colChiTiet.setCellFactory(cel-> new TableCell<NhaCungCap, String>(){
            private final Button btn = new Button("Chi tiết");
            {
//              Thêm sự kiện cho Button chi tiết
                btn.setOnAction(event -> {
                    NhaCungCap ncc = getTableView().getItems().get(getIndex());
                    suaXoaNhaCungCap(ncc);
                });
                btn.setStyle("-fx-text-fill: white;-fx-background-color: rgba(50, 100, 255, 0.8);-fx-font-weight: bold;-fx-font-size: 11px");
            }
//          Thêm button vào cột chi tiết
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        tblNhaCungCap.setItems(data);
    }

//  Nhớ xóa
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/DanhMucNhaCungCap_GUI.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//  Button mở giao diện sửa xóa nhà cung cấp
    private void suaXoaNhaCungCap(NhaCungCap ncc) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/SuaXoaNhaCungCap_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

//          Thêm dữ liệu nhà cung cấp vào ctrl sửa xóa
            SuaXoaNhaCungCap_Ctrl ctrl = loader.getController();
            ctrl.initialize(ncc);


            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Button mở giao diện thêm nhà cung cấp
    public void btnThemNCC(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/ThemNhaCungCap_GUI.fxml"));
            Parent root = loader.load();

//          Thêm dữ liệu ctrl cha vào ctrl thêm
            ThemNhaCungCap_Ctrl ctrl = loader.getController();
            ctrl.setParentCtrl(this);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void TimKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        List<NhaCungCap> list = nhaCungCapDao.selectAll();
        if (keyword.isEmpty()) {
            tblNhaCungCap.setItems(FXCollections.observableArrayList(list));
            return;
        }


        List<NhaCungCap> filtered = list.stream()
                .filter(ncc ->
                        (ncc.getMaNCC() != null && ncc.getMaNCC().toLowerCase().contains(keyword)) ||
                                (ncc.getTenNCC() != null && ncc.getTenNCC().toLowerCase().contains(keyword))

                )
                .toList();

        tblNhaCungCap.setItems(FXCollections.observableArrayList(filtered));
    }

    public void refreshTable() {
        loadNhaCungCap();
    }
    @FXML
    private void LamMoi() {
        txtTimKiem.clear();
        loadNhaCungCap();
    }
}
