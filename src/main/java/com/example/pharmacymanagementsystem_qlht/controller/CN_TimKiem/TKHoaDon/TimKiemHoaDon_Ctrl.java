package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;

import com.example.pharmacymanagementsystem_qlht.dao.HoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuDoiHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuTraHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class TimKiemHoaDon_Ctrl extends Application {

    @FXML
    private TableView<HoaDon> tblHD;
    @FXML
    private TableColumn<HoaDon, String> colMaHD;
    @FXML
    private TableColumn<HoaDon, String> colNgayLap;
    @FXML
    private TableColumn<HoaDon, String> colTenKH;
    @FXML
    private TableColumn<HoaDon, String> colSdtKH;
    @FXML
    private TableColumn<HoaDon, String> colTenNV;
    @FXML
    private TableColumn<HoaDon, Integer> colSLP;
    @FXML
    private TableColumn<HoaDon, String> colChiTiet;

    private HoaDon_Dao hoaDonDao = new HoaDon_Dao();
    private PhieuDoiHang_Dao phieuDoiHangDao = new PhieuDoiHang_Dao();
    private PhieuTraHang_Dao phieuTraHangDao = new PhieuTraHang_Dao();

    @FXML
    public void initialize() {
        loadTable();
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoaDon/TKHoaDon_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loadTable() {
        List<HoaDon> list = hoaDonDao.selectAll();
        System.out.println("DEBUG: Số lượng hóa đơn load được từ CSDL: " + list.size());

        if (list.isEmpty()) {
            System.out.println("DEBUG: Không có dữ liệu hóa đơn nào trong CSDL.");

            return;
        }

        ObservableList<HoaDon> data = FXCollections.observableArrayList(list);

        colMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colNgayLap.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNgayLap().toString())
        );
        colTenKH.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaKH().getTenKH())
        );
        colSdtKH.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaKH().getSdt())
        );
        colTenNV.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaNV().getTenNV())
        );
        colSLP.setCellValueFactory(cellData -> {
            int soLuong = phieuDoiHangDao.countByHoaDon(cellData.getValue().getMaHD()) +
                    phieuTraHangDao.countByHoaDon(cellData.getValue().getMaHD());
            return new SimpleIntegerProperty(soLuong).asObject();
        });
        colChiTiet.setCellFactory(col -> new TableCell<HoaDon, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    HoaDon hd = getTableView().getItems().get(getIndex());
                    btnChiTietClick(hd);
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tblHD.setItems(data);
    }



    // Optional method for detail button click
    private void btnChiTietClick(HoaDon hoaDon) {
        // Implement detail view logic here
        System.out.println("Xem chi tiết hóa đơn: " + hoaDon.getMaHD());
        // You can open a detail window or navigate to detail view
    }


}
