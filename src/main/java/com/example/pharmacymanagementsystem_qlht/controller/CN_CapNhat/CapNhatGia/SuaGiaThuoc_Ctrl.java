package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatGia;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietDonViTinh_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietDonViTinh;
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
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class SuaGiaThuoc_Ctrl extends Application {
    // FXML controls
    @FXML
    private TextField tfMaThuoc;
    @FXML
    private TextField tfTenThuoc;
    @FXML
    private ComboBox<String> cbLoaiHang;

    @FXML
    private TableView<ChiTietDonViTinh> tbDVT;
    @FXML
    private TableColumn<ChiTietDonViTinh, String> colDVT;
    @FXML
    private TableColumn<ChiTietDonViTinh, String> colKH;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colHeSo;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colGiaNhap;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colGiaBan;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colDVCB;
    @FXML
    private TableColumn<ChiTietDonViTinh, Void> colXoa;

    private Thuoc_SanPham thuoc;
    private ObservableList<ChiTietDonViTinh> listGia = FXCollections.observableArrayList();
    private final ChiTietDonViTinh_Dao ctDVTDao = new ChiTietDonViTinh_Dao();

    @FXML
    public void initialize() {
        colDVT.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDvt().getTenDonViTinh()));
        colKH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDvt().getKiHieu()));
        colHeSo.setCellValueFactory(new PropertyValueFactory<>("heSoQuyDoi"));
        colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colDVCB.setCellValueFactory(new PropertyValueFactory<>("donViCoBan"));

        addDeleteButtonToTable();

        tbDVT.setItems(listGia);
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<ChiTietDonViTinh, Void>, TableCell<ChiTietDonViTinh, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ChiTietDonViTinh, Void> call(final TableColumn<ChiTietDonViTinh, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Xóa");

                    {
                        btn.setOnAction(event -> {
                            ChiTietDonViTinh ct = getTableView().getItems().get(getIndex());
                            if (ct != null) {
                                // remove from UI list
                                listGia.remove(ct);
                                // TODO: delete from database if desired, e.g. ctDVTDao.delete(ct.getId() or appropriate key)
                                // Example (uncomment and adapt if DAO method exists):
                                // ctDVTDao.deleteById(ct.getId());
                            }
                        });
                        btn.getStyleClass().add("btn-delete");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colXoa.setCellFactory(cellFactory);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/SuaGiaThuoc_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // called by caller to supply Thuoc_SanPham to edit
    public void setThuoc(Thuoc_SanPham thuoc) {
        this.thuoc = thuoc;
        if (tfMaThuoc != null) {
            tfMaThuoc.setText(thuoc.getMaThuoc());
        }
        if (tfTenThuoc != null) {
            tfTenThuoc.setText(thuoc.getTenThuoc());
        }
        // load list of prices/units
        loadListGia(this.thuoc.getMaThuoc());
    }

    // load list from DAO into observable list and refresh table
    public void loadListGia(String maThuoc) {
        List<ChiTietDonViTinh> loaded = ctDVTDao.selectByMaThuoc(maThuoc);
        listGia.setAll(loaded);
    }

    // optional save/cancel handlers can be added here
}