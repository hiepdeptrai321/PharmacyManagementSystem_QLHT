package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuDatHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.DonViTinh_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuDatHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuNhap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChiTietPhieuDatHang_Ctrl  {
    @FXML
    private PhieuDatHang phieuDatHang;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, Number> colSTT;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colTenSP;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, Integer> colSoLuong;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colDonVi;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, Double> colDonGia;

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colNhaCungCap; // used for chiết khấu percent

    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colThanhTien;
    @FXML
    private TableColumn<ChiTietPhieuDatHang, String> colTT;

    @FXML
    private TableView<ChiTietPhieuDatHang> tblChiTietPhieuDat;

    @FXML
    private Label lblMaPhieuDatValue;

    @FXML
    private Label lblNgayLapValue;

    @FXML
    private Label lblTenNhanVienValue;

    @FXML
    private Label lblTenNCCValue;

    @FXML
    private Label lblSDTNCCValue;

    @FXML
    private Label lblGhiChuValue;

    @FXML
    private Label lblTongTienDatValue;

    @FXML
    private Label lblChietKhauPDValue;

    @FXML
    private Label lblThueVATValue;

    @FXML
    private Label lblTongTienPhaiDatValue;

    @FXML
    private Label lblPTTTValue;

    @FXML
    private Label lblTienDaThanhToanValue;

    @FXML
    private Label lblTienConLaiValue;
    @FXML
    private Label lbTT;

    @FXML
    private Button btnInPhieuDat;

    @FXML
    private Button btnLapHoaDon;

    @FXML
    private Button btnDong;

    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    @FXML
    public void initialize() {
        // Close button
        if (btnDong != null) {
            btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
        }
        btnLapHoaDon.setOnAction(e -> onLapHoaDon());

        Platform.runLater(()->{
            Stage dialog = (Stage) btnDong.getScene().getWindow();
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
        });
    }

    public void setPhieuDatHang(PhieuDatHang pDat) {
        this.phieuDatHang = pDat;
        hienThiThongTin();
    }

    private void hienThiThongTin() {
        if (phieuDatHang == null) return;

        lblMaPhieuDatValue.setText(phieuDatHang.getMaPDat());

        if (phieuDatHang.getNgayLap() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            lblNgayLapValue.setText(formatter.format(phieuDatHang.getNgayLap()));
        } else {
            lblNgayLapValue.setText("Không rõ");
        }

        if (phieuDatHang.getNhanVien() != null)
            lblTenNhanVienValue.setText(phieuDatHang.getNhanVien().getTenNV());
        else
            lblTenNhanVienValue.setText("");

        // Supplier (NCC) - in PhieuDat model KhachHang used as supplier in some contexts; set if available
        if (phieuDatHang.getKhachHang() != null) {
            lblTenNCCValue.setText(phieuDatHang.getKhachHang().getTenKH());
            lblSDTNCCValue.setText(phieuDatHang.getKhachHang().getSdt());
        } else {
            lblTenNCCValue.setText("");
            lblSDTNCCValue.setText("");
        }

        lblGhiChuValue.setText(phieuDatHang.getGhiChu() != null ? phieuDatHang.getGhiChu() : "");
        if (phieuDatHang != null && lbTT != null) {
            final String STATUS_AV = "status-available";
            final String STATUS_UNAV = "status-unavailable";
            final String STATUS_FI = "status-finished";

            Platform.runLater(() -> {
                lbTT.getStyleClass().removeAll(STATUS_AV, STATUS_UNAV);
                if(phieuDatHang.getTrangthai() == 0){
                    lbTT.setText("Chưa có hàng");
                    lbTT.getStyleClass().add(STATUS_AV);
                }
                else if(phieuDatHang.getTrangthai() == 1){
                    lbTT.setText("Sẵn hàng");
                    lbTT.getStyleClass().add(STATUS_UNAV);
                }
                else{
                    lbTT.setText("Đã hoàn thành");
                    lbTT.getStyleClass().add(STATUS_FI);
                }
            });
        }
        // Load detail rows
        List<ChiTietPhieuDatHang> list = new ChiTietPhieuDatHang_Dao().selectBySql("SELECT * FROM ChiTietPhieuDatHang WHERE MaPDat = ?", phieuDatHang.getMaPDat());

        tblChiTietPhieuDat.getItems().clear();
        tblChiTietPhieuDat.getItems().addAll(list);


        // STT
        colSTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tblChiTietPhieuDat.getItems().indexOf(cellData.getValue()) + 1)
        );

        // Ten san pham
        colTenSP.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getThuoc().getTenThuoc()));

        // So luong
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        // Đơn vị
        colDonVi.setCellValueFactory(cel -> {
            String tenDVT = "";
            if (cel.getValue() != null && cel.getValue().getThuoc() != null) {
                tenDVT =  new DonViTinh_Dao().selectById(cel.getValue().getDvt()).getTenDonViTinh();
                if (tenDVT == null) tenDVT = "";
            }
            return new SimpleStringProperty(tenDVT);
        });

        // Đơn giá
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        // Chiết khấu column (giamGia)
        colNhaCungCap.setCellValueFactory(cel -> new SimpleStringProperty(String.format("%.2f", cel.getValue().getGiamGia())));

        // Thành tiền
        colThanhTien.setCellValueFactory(cel -> {
            ChiTietPhieuDatHang item = cel.getValue();
            double thanh = item.getSoLuong() * item.getDonGia();
            // Apply giamGia if giamGia is percentage (assumption)
            if (item.getGiamGia() != 0) {
                thanh = thanh * (1 - item.getGiamGia() / 100.0);
            }
            return new SimpleStringProperty(String.format("%.2f", thanh));
        });

        colTT.setCellValueFactory(cellData ->{
            return new SimpleStringProperty(cellData.getValue().isTrangThai() ? "Sẵn hàng" : "Hết hàng");
        });

        // Compute summary totals
        double tongTruocCK = 0.0;
        double tongGiamGiaValue = 0.0; // total discount amount
        for (ChiTietPhieuDatHang item : list) {
            double line = item.getSoLuong() * item.getDonGia();
            double discountAmount = 0.0;
            if (item.getGiamGia() != 0) {
                discountAmount = line * item.getGiamGia() / 100.0;
            }
            tongTruocCK += line;
            tongGiamGiaValue += discountAmount;
        }

        double tongSauCK = tongTruocCK - tongGiamGiaValue;
        double thueVAT = 0.0; // If your app stores VAT separately, adjust here. Default 0.
        double tongPhaiDat = tongSauCK + thueVAT;

        lblTongTienDatValue.setText(String.format("%.2f VND", tongTruocCK));
        lblChietKhauPDValue.setText(String.format("-%.2f VND", tongGiamGiaValue));
        lblThueVATValue.setText(String.format("%.2f VND", thueVAT));
        lblTongTienPhaiDatValue.setText(String.format("%.2f VND", tongPhaiDat));
        lblTienDaThanhToanValue.setText(String.format("%.2f VND", phieuDatHang.getSoTienCoc()));
        lblTienConLaiValue.setText(String.format("%.2f VND", Math.max(0.0, tongPhaiDat - phieuDatHang.getSoTienCoc())));

        // Payment method placeholder
        lblPTTTValue.setText("Chuyển khoản");
    }
    // java
    public void onLapHoaDon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml"));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller == null) throw new IllegalStateException("Cannot get LapHoaDon controller");

            // Build list of ChiTietHoaDon
            List<ChiTietHoaDon> hdLines = new ArrayList<>();
            for (ChiTietPhieuDatHang pdRow : tblChiTietPhieuDat.getItems()) {
                if (pdRow == null) continue;
                ChiTietHoaDon hdRow = new ChiTietHoaDon();
                Thuoc_SP_TheoLo lo = new Thuoc_SP_TheoLo();
                lo.setThuoc(pdRow.getThuoc());
                hdRow.setLoHang(lo);
                hdRow.setSoLuong(pdRow.getSoLuong());
                hdRow.setDonGia(pdRow.getDonGia());
                hdRow.setGiamGia(pdRow.getGiamGia());
                hdLines.add(hdRow);
            }

            // Try to inject into ObservableList field `dsChiTietHD` or call `setInitialChiTiet`
            boolean injected = false;
            try {
                Field dsField = controller.getClass().getDeclaredField("dsChiTietHD");
                dsField.setAccessible(true);
                @SuppressWarnings("unchecked")
                javafx.collections.ObservableList<ChiTietHoaDon> targetList =
                        (javafx.collections.ObservableList<ChiTietHoaDon>) dsField.get(controller);
                if (targetList != null) {
                    targetList.clear();
                    targetList.addAll(hdLines);
                    injected = true;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) { }

            if (!injected) {
                try {
                    Method m = controller.getClass().getMethod("setInitialChiTiet", java.util.List.class);
                    m.invoke(controller, hdLines);
                    injected = true;
                } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException ignored) { }
            }

            // Prefill customer/deposit via reflection (best-effort)
            try {
                Field txtTenField = controller.getClass().getDeclaredField("txtTenKH");
                Field txtSdtField = controller.getClass().getDeclaredField("txtSDT");
                Field txtDepositField = controller.getClass().getDeclaredField("txtSoTienKhachDua");
                txtTenField.setAccessible(true);
                txtSdtField.setAccessible(true);
                txtDepositField.setAccessible(true);

                javafx.scene.control.TextField txtTen = (javafx.scene.control.TextField) txtTenField.get(controller);
                javafx.scene.control.TextField txtSdt = (javafx.scene.control.TextField) txtSdtField.get(controller);
                javafx.scene.control.TextField txtDeposit = (javafx.scene.control.TextField) txtDepositField.get(controller);

                if (phieuDatHang != null) {
                    if (phieuDatHang.getKhachHang() != null) {
                        if (txtTen != null) txtTen.setText(phieuDatHang.getKhachHang().getTenKH());
                        if (txtSdt != null) txtSdt.setText(phieuDatHang.getKhachHang().getSdt());
                    }
                    if (txtDeposit != null) txtDeposit.setText(String.valueOf(phieuDatHang.getSoTienCoc()));
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) { }

            openLapHoaDonInMainAndCloseCurrent(root);

        } catch (Exception ex) {
            ex.printStackTrace();
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Không thể mở giao diện Hóa đơn: " + ex.getMessage(), javafx.scene.control.ButtonType.OK);
            if (tblChiTietPhieuDat != null && tblChiTietPhieuDat.getScene() != null) {
                a.initOwner(tblChiTietPhieuDat.getScene().getWindow());
            }
            a.showAndWait();
        }
    }

    private void openLapHoaDonInMainAndCloseCurrent(Parent lapPane) {
        try {
            // Current window (the dialog or current stage)
            Window currentWindow = null;
            if (tblChiTietPhieuDat != null && tblChiTietPhieuDat.getScene() != null) {
                currentWindow = tblChiTietPhieuDat.getScene().getWindow();
            } else {
                // fallback to focused/showing
                for (Window w : Window.getWindows()) {
                    if (w.isFocused()) { currentWindow = w; break; }
                }
                if (currentWindow == null) {
                    for (Window w : Window.getWindows()) {
                        if (w.isShowing()) { currentWindow = w; break; }
                    }
                }
            }

            // Find main stage which contains #pnlChung
            Stage mainStage = null;
            for (Window w : Window.getWindows()) {
                if (w instanceof Stage s && s.getScene() != null) {
                    javafx.scene.Node found = s.getScene().lookup("#pnlChung");
                    if (found instanceof javafx.scene.layout.Pane) {
                        mainStage = s;
                        break;
                    }
                }
            }

            if (mainStage != null) {
                final Stage ms = mainStage;
                final Window cw = currentWindow;
                javafx.application.Platform.runLater(() -> {
                    try {
                        javafx.scene.Node node = ms.getScene().lookup("#pnlChung");
                        if (node instanceof javafx.scene.layout.Pane pnlChung) {
                            pnlChung.getChildren().setAll(lapPane);
                        } else {
                            // fallback: show in new stage
                            Stage st = new Stage();
                            st.setScene(new Scene(lapPane));
                            st.show();
                        }
                        if (cw instanceof Stage s) s.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
