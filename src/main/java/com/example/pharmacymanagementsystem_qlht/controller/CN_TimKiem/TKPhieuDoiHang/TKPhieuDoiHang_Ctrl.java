package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDoiHang;

import com.example.pharmacymanagementsystem_qlht.dao.PhieuDoiHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.TienIch.DoiNgay;
import com.example.pharmacymanagementsystem_qlht.model.PhieuTraHang;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.util.List;

public class TKPhieuDoiHang_Ctrl extends Application {
    @FXML
    private TableView<PhieuDoiHang> tblPD;
    @FXML
    private TableColumn<PhieuDoiHang, Number> colSTT;
    @FXML
    private TableColumn<PhieuDoiHang, String> colMaPD;
    @FXML
    private TableColumn<PhieuDoiHang, String> colMaHD;
    @FXML
    private TableColumn<PhieuDoiHang, String> colNgayLap;
    @FXML
    private TableColumn<PhieuDoiHang, String> colTenKH;
    @FXML
    private TableColumn<PhieuDoiHang, String> colSdtKH;
    @FXML
    private TableColumn<PhieuDoiHang, String> colTenNV;
    @FXML
    private TableColumn<PhieuDoiHang, String> colChiTiet;
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

    private final PhieuDoiHang_Dao phieuDoiHangDao = new PhieuDoiHang_Dao();

    @FXML
    public void initialize() {
        cboTimKiem.getItems().addAll(
                "Mã phiếu đổi", "Mã hóa đơn", "Tên khách hàng", "SĐT khách hàng", "Tên nhân viên", "Ngày lập"
        );
        cboTimKiem.setValue("Tiêu chí");
        cbLoc.getItems().addAll(
                "Tất cả", "Hôm nay", "7 ngày gần nhất", "Tháng này", "Năm nay"
        );
        cbLoc.setValue("⌛ Bộ lọc nhanh");
        tblPD.setRowFactory(tv -> {
            TableRow<PhieuDoiHang> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    PhieuDoiHang rowData = row.getItem();
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
        List<PhieuDoiHang> list = phieuDoiHangDao.selectAll();
        ObservableList<PhieuDoiHang> data = FXCollections.observableArrayList(list);
        colSTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tblPD .getItems().indexOf(cellData.getValue()) + 1)
        );
        colMaPD.setCellValueFactory(new PropertyValueFactory<>("maPD"));
        colMaHD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoaDon().getMaHD()));
        colNgayLap.setCellValueFactory(cellData -> new SimpleStringProperty(DoiNgay.dinhDangThoiGian(cellData.getValue().getNgayLap())));
        colTenKH.setCellValueFactory(cellData -> {
            if (cellData.getValue().getKhachHang() != null) {
                return new SimpleStringProperty(cellData.getValue().getKhachHang().getTenKH());
            } else {
                return new SimpleStringProperty("");
            }
        });
        colTenKH.setCellFactory(col -> new TableCell<PhieuDoiHang, String>() {
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
        colSdtKH.setCellValueFactory(cellData -> {
            if (cellData.getValue().getKhachHang() != null) {
                return new SimpleStringProperty(cellData.getValue().getKhachHang().getSdt());
            } else {
                return new SimpleStringProperty("");
            }
        });
        colTenNV.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNhanVien().getTenNV()));
        colTenNV.setCellFactory(col -> new TableCell<PhieuDoiHang, String>() {
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
        colChiTiet.setCellFactory(col -> new TableCell<PhieuDoiHang, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    PhieuDoiHang pd = getTableView().getItems().get(getIndex());
                    btnChiTietClick(pd);
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        tblPD.setItems(data);
    }

    private void btnChiTietClick(PhieuDoiHang pDoi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDoiHang/ChiTietPhieuDoiHang_GUI.fxml"));
            Parent root = loader.load();
            this.getClass();
            com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDoiHang.ChiTietPhieuDoiHang_Ctrl ctrl = loader.getController();
            ctrl.setPhieuDoiHang(pDoi);

            Stage dialog = new Stage();
            dialog.initOwner(btnTimKiem.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Chi tiết phiếu đổi hàng");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void timKiem() {
        String tieuChi = cboTimKiem.getValue();
        String noiDung = txtNoiDungTimKiem.getText().trim().toLowerCase();
        var tuNgay = dpTuNgay != null ? dpTuNgay.getValue() : null;
        var denNgay = dpDenNgay != null ? dpDenNgay.getValue() : null;
        List<PhieuDoiHang> list = phieuDoiHangDao.selectAll();
        if ((tieuChi == null || tieuChi.equals("Tiêu chí")) && (tuNgay == null && denNgay == null) && noiDung.isEmpty()) {
            loadTable();
            return;
        }

        List<PhieuDoiHang> filtered = list.stream().filter(pd -> {
            boolean match = true;
            switch (tieuChi) {
                case "Mã phiếu đổi":
                    match = pd.getMaPD().toLowerCase().contains(noiDung);
                    break;
                case "Mã hóa đơn":
                    match = pd.getHoaDon() != null && pd.getHoaDon().getMaHD() != null && pd.getHoaDon().getMaHD().toLowerCase().contains(noiDung);
                    break;
                case "Tên khách hàng":
                    match = pd.getKhachHang() != null && pd.getKhachHang().getTenKH() != null && pd.getKhachHang().getTenKH().toLowerCase().contains(noiDung);
                    break;
                case "SĐT khách hàng":
                    match = pd.getKhachHang() != null && pd.getKhachHang().getSdt() != null && pd.getKhachHang().getSdt().toLowerCase().contains(noiDung);
                    break;
                case "Tên nhân viên":
                    match = pd.getNhanVien() != null && pd.getNhanVien().getTenNV().toLowerCase().contains(noiDung);
                    break;
                case "Ngày lập":
                    match = pd.getNgayLap() != null && DoiNgay.dinhDangThoiGian(pd.getNgayLap()).contains(noiDung);
                    break;
            }
            if (tuNgay != null && pd.getNgayLap() != null) {
                match = match && !pd.getNgayLap().toLocalDateTime().toLocalDate().isBefore(tuNgay);
            }
            if (denNgay != null && pd.getNgayLap() != null) {
                match = match && !pd.getNgayLap().toLocalDateTime().toLocalDate().isAfter(denNgay);
            }
            return match;
        }).toList();
        tblPD.setItems(FXCollections.observableArrayList(filtered));
    }
    private void lamMoi() {
        txtNoiDungTimKiem.clear();
        cboTimKiem.setValue("Mã phiếu đổi");
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
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDoiHang/TKPhieuDoiHang_GUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
