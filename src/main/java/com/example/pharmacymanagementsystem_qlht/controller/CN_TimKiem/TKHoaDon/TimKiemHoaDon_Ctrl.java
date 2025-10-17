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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class TimKiemHoaDon_Ctrl extends Application {

    public TableView<HoaDon> tblHD;
    public TableColumn<HoaDon,String> colMaHD;
    public TableColumn<HoaDon,String> colNgayLap;
    public TableColumn<HoaDon,String> colTenKH;
    public TableColumn<HoaDon,String> colSdtKH;
    public TableColumn<HoaDon,String> colTenNV;
    public TableColumn<HoaDon,Integer> colSLP;
    public TableColumn<HoaDon,String> colChiTiet;

    @FXML
    private ComboBox<String> cboTieuChiTimKiem;
    @FXML
    private TextField txtNoiDungTimKiem;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private ComboBox<String> cboBoLocNhanh;
    @FXML
    private Button btnTimKiem;
    @FXML
    private Button btnHuyBo;

    private HoaDon_Dao hoaDonDao = new HoaDon_Dao();
    private PhieuDoiHang_Dao phieuDoiHangDao = new PhieuDoiHang_Dao();
    private PhieuTraHang_Dao phieuTraHangDao = new PhieuTraHang_Dao();

    @FXML
    public void initialize() {
        // tiêu chí tìm kiếm
        cboTieuChiTimKiem.getItems().addAll(
                "Mã hóa đơn", "Tên khách hàng", "SĐT khách hàng", "Tên nhân viên", "Ngày lập", "Khách vãng lai"
        );
        cboTieuChiTimKiem.setValue("Tiêu chí");
        // bộ lọc nhanh
        cboBoLocNhanh.getItems().addAll(
                "Tất cả", "Hôm nay", "7 ngày gần nhất", "Tháng này", "Năm nay"
        );
        cboBoLocNhanh.setValue("⌛ Bộ lọc nhanh");
        // sự kiện lọc nhanh
        cboBoLocNhanh.setOnAction(e -> boLocNhanh());
        // sự kiện gõ text
        txtNoiDungTimKiem.textProperty().addListener((obs, oldVal, newVal) -> {
            String tieuChi = cboTieuChiTimKiem.getValue();
            if (!"Ngày lập".equals(tieuChi)) {
                timKiem();
            }
        });
        // Sự kiện đổi tiêu chí tìm kiếm: nếu chuyển sang Ngày lập thì không auto tìm
        cboTieuChiTimKiem.setOnAction(e -> {
            String tieuChi = cboTieuChiTimKiem.getValue();
            if (!"Ngày lập".equals(tieuChi)) {
                timKiem();
            }
        });
        btnHuyBo.setOnAction(e -> lamMoi());
        btnTimKiem.setOnAction(e -> timKiem());
        // Cho phép double click vào dòng để xem chi tiết
        tblHD.setRowFactory(tv -> {
            TableRow<HoaDon> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    HoaDon rowData = row.getItem();
                    btnChiTietClick(rowData);
                }
            });
            return row;
        });
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
        System.out.println("Xem chi tiết hóa đơn: " + hoaDon.getMaHD());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoaDon/ChiTietHoaDon_GUI.fxml"));
            Parent root = loader.load();

            // Optionally pass data to the detail controller
            com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon.ChiTietHoaDon_Ctrl controller = loader.getController();
            controller.setHoaDon(hoaDon);

            Stage stage = new Stage();
            stage.setTitle("Chi tiết hóa đơn");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timKiem() {
        String tieuChi = cboTieuChiTimKiem.getValue();
        String noiDung = txtNoiDungTimKiem.getText().trim().toLowerCase();
        var tuNgay = dpTuNgay.getValue();
        var denNgay = dpDenNgay.getValue();
        List<HoaDon> list = hoaDonDao.selectAll();

        if ((tieuChi == null || tieuChi.equals("Tiêu chí")) && (tuNgay == null && denNgay == null) && noiDung.isEmpty()) {
            loadTable();
            return;
        }
        List<HoaDon> filtered = list.stream().filter(hd -> {
            boolean match = true;
            if (tieuChi != null && !tieuChi.equals("Tiêu chí")) {
                switch (tieuChi) {
                    case "Mã hóa đơn":
                        match = hd.getMaHD().toLowerCase().contains(noiDung);
                        break;
                    case "Tên khách hàng":
                        match = hd.getMaKH() != null && hd.getMaKH().getTenKH() != null && hd.getMaKH().getTenKH().toLowerCase().contains(noiDung);
                        break;
                    case "SĐT khách hàng":
                        match = hd.getMaKH() != null && hd.getMaKH().getSdt() != null && hd.getMaKH().getSdt().toLowerCase().contains(noiDung);
                        break;
                    case "Tên nhân viên":
                        match = hd.getMaNV() != null && hd.getMaNV().getTenNV().toLowerCase().contains(noiDung);
                        break;
                    case "Ngày lập":
                        match = hd.getNgayLap() != null && hd.getNgayLap().toLocalDateTime().toLocalDate().toString().contains(noiDung);
                        break;
                    case "Khách vãng lai":
                        match = (hd.getMaKH() == null) || (hd.getMaKH().getMaKH() == null) || ("".equals(hd.getMaKH().getMaKH()));
                        break;
                }
            }
            // Lọc theo ngày nếu có chọn ngày
            if (tuNgay != null && hd.getNgayLap() != null) {
                match = match && !hd.getNgayLap().toLocalDateTime().toLocalDate().isBefore(tuNgay);
            }
            if (denNgay != null && hd.getNgayLap() != null) {
                match = match && !hd.getNgayLap().toLocalDateTime().toLocalDate().isAfter(denNgay);
            }
            return match;
        }).toList();
        tblHD.setItems(FXCollections.observableArrayList(filtered));
    }

    private void lamMoi() {
        txtNoiDungTimKiem.clear();
        cboTieuChiTimKiem.setValue("Mã hóa đơn");
        dpTuNgay.setValue(null);
        dpDenNgay.setValue(null);
        cboBoLocNhanh.setValue("Tất cả");
        loadTable();
    }

    private void boLocNhanh() {
        String loc = cboBoLocNhanh.getValue();
        System.out.println("DEBUG: Giá trị lọc nhanh được chọn: " + loc);
        var today = java.time.LocalDate.now();
        switch (loc) {
            case "Tất cả":
                dpTuNgay.setValue(null);
                dpDenNgay.setValue(null);
                break;
            case "Hôm nay":
                dpTuNgay.setValue(today);
                dpDenNgay.setValue(today);
                break;
            case "7 ngày gần nhất":
                dpTuNgay.setValue(today.minusDays(6));
                dpDenNgay.setValue(today);
                break;
            case "Tháng này":
                dpTuNgay.setValue(today.withDayOfMonth(1));
                dpDenNgay.setValue(today);
                break;
            case "Năm nay":
                dpTuNgay.setValue(today.withDayOfYear(1));
                dpDenNgay.setValue(today);
                break;
            default:
                // Nếu giá trị không khớp, reset về mặc định
                dpTuNgay.setValue(null);
                dpDenNgay.setValue(null);
                break;
        }
        timKiem();
    }

}
