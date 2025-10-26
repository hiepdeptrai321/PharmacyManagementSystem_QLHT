package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuTraHang;

import com.example.pharmacymanagementsystem_qlht.dao.PhieuTraHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDatHang;
import com.example.pharmacymanagementsystem_qlht.model.PhieuTraHang;
import com.example.pharmacymanagementsystem_qlht.TienIch.DoiNgay;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;

public class TKPhieuTraHang_Ctrl extends Application {
    @FXML
    private TableView<PhieuTraHang> tblPT;
    @FXML
    private TableColumn<PhieuTraHang, Number> colSTT;
    @FXML
    private TableColumn<PhieuTraHang, String> colMaPT;
    @FXML
    private TableColumn<PhieuTraHang, String> colMaHD;
    @FXML
    private TableColumn<PhieuTraHang, String> colNgayLap;
    @FXML
    private TableColumn<PhieuTraHang, String> colTenKH;
    @FXML
    private TableColumn<PhieuTraHang, String> colSdtKH;
    @FXML
    private TableColumn<PhieuTraHang, String> colTenNV;
    @FXML
    private TableColumn<PhieuTraHang, String> colChiTiet;
    @FXML
    private ComboBox<String> cboTimKiem;
    @FXML
    private TextField txtNoiDungTimKiem;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private ComboBox<String> cbLoc;
    @FXML
    private Button btnTimKiem;
    @FXML
    private Button btnHuyBo;

    private final PhieuTraHang_Dao phieuTraHangDao = new PhieuTraHang_Dao();

    @FXML
    public void initialize() {

        cboTimKiem.getItems().addAll(
            "Mã phiếu trả", "Mã hóa đơn", "Tên khách hàng", "SĐT khách hàng", "Tên nhân viên", "Ngày lập"
        );
        cboTimKiem.setValue("Tiêu chí");
        cbLoc.getItems().addAll(
            "Tất cả", "Hôm nay", "7 ngày gần nhất", "Tháng này", "Năm nay"
        );
        cbLoc.setValue("⌛ Bộ lọc nhanh");
        tblPT.setRowFactory(tv -> {
            TableRow<PhieuTraHang> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    PhieuTraHang rowData = row.getItem();
                    btnChiTietClick(rowData);
                }
            });
            return row;
        });
        btnTimKiem.setOnAction(e -> timKiem());
        btnHuyBo.setOnAction(e -> lamMoi());
        cbLoc.setOnAction(e -> boLocNhanh());
        Platform.runLater(()->{
            loadTable();
        });
    }

    public void loadTable() {
        List<PhieuTraHang> list = phieuTraHangDao.selectAll();
        ObservableList<PhieuTraHang> data = FXCollections.observableArrayList(list);
        colSTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tblPT .getItems().indexOf(cellData.getValue()) + 1)
        );
        colMaPT.setCellValueFactory(new PropertyValueFactory<>("maPT"));
//        colMaHD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoaDon().getMaHD()));
        colMaHD.setCellValueFactory(cellData -> {
            PhieuTraHang pt = cellData.getValue();
            if (pt.getHoaDon() != null) {
                // k null
                return new SimpleStringProperty(pt.getHoaDon().getMaHD());
            } else {
                // null
                return new SimpleStringProperty("---");
            }
        });
        colNgayLap.setCellValueFactory(cellData -> new SimpleStringProperty(DoiNgay.dinhDangThoiGian(cellData.getValue().getNgayLap())));
        colTenKH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKhachHang().getTenKH()));
        colTenKH.setCellFactory(col -> new TableCell<PhieuTraHang, String>() {
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
        colSdtKH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKhachHang().getSdt()));
        colTenNV.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNhanVien().getTenNV()));
        colTenNV.setCellFactory(col -> new TableCell<PhieuTraHang, String>() {
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
        colChiTiet.setCellFactory(col -> new TableCell<PhieuTraHang, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    PhieuTraHang pt = getTableView().getItems().get(getIndex());
                    btnChiTietClick(pt);
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        tblPT.setItems(data);
    }

    private void btnChiTietClick(PhieuTraHang pTra) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuTraHang/ChiTietPhieuTraHang_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            this.getClass();
            ChiTietPhieuTraHang_Ctrl ctrl = loader.getController();
            ctrl.setPhieuTraHang(pTra);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timKiem() {
        String tieuChi = cboTimKiem.getValue();

        String noiDung = txtNoiDungTimKiem.getText().trim().toLowerCase();
        var tuNgay = dpTuNgay != null ? dpTuNgay.getValue() : null;
        var denNgay = dpDenNgay != null ? dpDenNgay.getValue() : null;
        List<PhieuTraHang> list = phieuTraHangDao.selectAll();
        if ((tieuChi == null || tieuChi.equals("Tiêu chí")) && (tuNgay == null && denNgay == null) && noiDung.isEmpty()) {
            loadTable();
            return;
        }
        List<PhieuTraHang> filtered = list.stream().filter(pt -> {
            boolean match = true;
            switch (tieuChi) {
                case "Mã phiếu trả":
                    match = pt.getMaPT().toLowerCase().contains(noiDung);
                    break;
                case "Mã hóa đơn":
                    match = pt.getHoaDon() != null && pt.getHoaDon().getMaHD() != null && pt.getHoaDon().getMaHD().toLowerCase().contains(noiDung);
                    break;
                case "Tên khách hàng":
                    match = pt.getKhachHang() != null && pt.getKhachHang().getTenKH() != null && pt.getKhachHang().getTenKH().toLowerCase().contains(noiDung);
                    break;
                case "SĐT khách hàng":
                    match = pt.getKhachHang() != null && pt.getKhachHang().getSdt() != null && pt.getKhachHang().getSdt().toLowerCase().contains(noiDung);
                    break;
                case "Tên nhân viên":
                    match = pt.getNhanVien() != null && pt.getNhanVien().getTenNV().toLowerCase().contains(noiDung);
                    break;
                case "Ngày lập":
                    match = pt.getNgayLap() != null && DoiNgay.dinhDangThoiGian(pt.getNgayLap()).contains(noiDung);
                    break;
            }
            if (tuNgay != null && pt.getNgayLap() != null) {
                match = match && !pt.getNgayLap().toLocalDateTime().toLocalDate().isBefore(tuNgay);
            }
            if (denNgay != null && pt.getNgayLap() != null) {
                match = match && !pt.getNgayLap().toLocalDateTime().toLocalDate().isAfter(denNgay);
            }
            return match;
        }).toList();
        tblPT.setItems(FXCollections.observableArrayList(filtered));
    }

    private void lamMoi() {
        txtNoiDungTimKiem.clear();
        cboTimKiem.setValue("Mã phiếu trả");
        if (dpTuNgay != null) dpTuNgay.setValue(null);
        if (dpDenNgay != null) dpDenNgay.setValue(null);
        cbLoc.setValue("Tất cả");
        loadTable();
    }

    private void boLocNhanh() {
        String loc = cbLoc.getValue();
        var today = java.time.LocalDate.now();
        if (loc == null) return;
        switch (loc) {
            case "Tất cả":
                if (dpTuNgay != null) dpTuNgay.setValue(null);
                if (dpDenNgay != null) dpDenNgay.setValue(null);
                break;
            case "Hôm nay":
                if (dpTuNgay != null) dpTuNgay.setValue(today);
                if (dpDenNgay != null) dpDenNgay.setValue(today);
                break;
            case "7 ngày gần nhất":
                if (dpTuNgay != null) dpTuNgay.setValue(today.minusDays(6));
                if (dpDenNgay != null) dpDenNgay.setValue(today);
                break;
            case "Tháng này":
                if (dpTuNgay != null) dpTuNgay.setValue(today.withDayOfMonth(1));
                if (dpDenNgay != null) dpDenNgay.setValue(today);
                break;
            case "Năm nay":
                if (dpTuNgay != null) dpTuNgay.setValue(today.withDayOfYear(1));
                if (dpDenNgay != null) dpDenNgay.setValue(today);
                break;
        }
        timKiem();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuTraHang/TKPhieuTraHang_GUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
