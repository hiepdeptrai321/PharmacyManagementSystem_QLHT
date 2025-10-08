package com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
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

import java.util.List;

public class SuaXoaKhuyenMai_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public Button btnLuu;
    public Button btnHuy;
    public Button btnXoa;
    public TableView<ChiTietKhuyenMai> tbDSThuoc;
    public TableColumn<ChiTietKhuyenMai,String> colMaThuoc;
    public TableColumn<ChiTietKhuyenMai,String> colTenThuoc;
    public TableColumn<ChiTietKhuyenMai,Integer> colSLAP;
    public TableColumn<ChiTietKhuyenMai,Integer> colSLTD;
    public TableColumn<ChiTietKhuyenMai,String> colXoaCT;
    @FXML
    private TextField tfTimThuoc;
    @FXML
    private ListView<?> listViewThuoc;


    @FXML
    private TextField tfTenKM;
    @FXML
    private ComboBox<String> cbLoaiKM;
    @FXML
    private TextField tfGiaTri;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private TextField tfMoTa;

    // 2. KHỞI TẠO (INITIALIZE)

    @FXML
    public void initialize() {
        listViewThuoc.setVisible(false);
        tfTimThuoc.focusedProperty().addListener((obs, oldVal, newVal) -> {
            listViewThuoc.setVisible(newVal);
        });
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_QuanLy/QLKhuyenMai/SuaXoaKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN

    public void loadData(KhuyenMai km) {
        if (km == null) return;
        tfTenKM.setText(km.getTenKM());
        cbLoaiKM.setValue(km.getLoaiKM().getMaLoai());
        tfGiaTri.setText(String.valueOf(km.getGiaTriKM()));
        dpTuNgay.setValue(km.getNgayBatDau().toLocalDate());
        dpDenNgay.setValue(km.getNgayKetThuc().toLocalDate());
        tfMoTa.setText(km.getMoTa());
        ChiTietKhuyenMai_Dao ctkm_dao = new ChiTietKhuyenMai_Dao();
    }

    public void loadDatatbCTKM(List<ChiTietKhuyenMai> dsCTKM){
        ObservableList<ChiTietKhuyenMai> listCTKM = FXCollections.observableArrayList(dsCTKM);
        colMaThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThuoc().getMaThuoc()));
        colTenThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThuoc().getTenThuoc()));
        colSLAP.setCellValueFactory(new PropertyValueFactory<>("slApDung"));
        colSLTD.setCellValueFactory(new PropertyValueFactory<>("slToiDa"));
        colXoaCT.setCellFactory(col -> new TableCell<ChiTietKhuyenMai, String>() {
            private final Button btn = new Button("X");
            {
                btn.setOnAction(event -> {
                    ChiTietKhuyenMai ctkm = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(ctkm);
                });
                btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        tbDSThuoc.setItems(listCTKM);
    }

    public void btnHuyClick(){
        Stage stage = (Stage) tfTimThuoc.getScene().getWindow();
        stage.close();
    }
    public void btnLuuClick(){
        // TODO
    }
    public void btnXoaClick(){
        // TODO
    }


    // 4. XỬ LÝ NGHIỆP VỤ
}
