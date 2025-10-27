package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDoi;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietHoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.HoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.KhachHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
import com.example.pharmacymanagementsystem_qlht.service.DoiHangItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LapPhieuDoiHang_Ctrl extends Application {
    @FXML private TextField txtTimHoaDonGoc;
    @FXML private Button btnTimHD;
    @FXML private TableView<ChiTietHoaDon> tblSanPhamGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colSTTGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colTenSPGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colSoLuongGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colDonViGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colDonGiaGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colGiamGiaGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colThanhTienGoc;
    @FXML private TableColumn<ChiTietHoaDon, Void> colDoi;

    @FXML private TableView<DoiHangItem> tblSanPhamDoi;
    @FXML private TableColumn<DoiHangItem, String> colSTTDoi;
    @FXML private TableColumn<DoiHangItem, String> colTenSPDoi;
    @FXML private TableColumn<DoiHangItem, Number> colSoLuongDoi;
    @FXML private TableColumn<DoiHangItem, String> colDonViDoi;
    @FXML private TableColumn<DoiHangItem, String> colLyDo;
    @FXML private TableColumn<DoiHangItem, Void> colBo;

    @FXML private TextField lblMaHDGoc;
    @FXML private TextField lblTenKH;
    @FXML private TextField lblSDT;
    @FXML private DatePicker dpNgayLapPhieu;
    @FXML private TextArea txtGhiChu;

    // State
    private final ObservableList<ChiTietHoaDon> dsGoc = FXCollections.observableArrayList();
    private final ObservableList<DoiHangItem> dsDoi = FXCollections.observableArrayList();
    private final Map<String, DoiHangItem> doiByMaLH = new HashMap<>();

    // DAO
    private final HoaDon_Dao hoaDonDao = new HoaDon_Dao();
    private final ChiTietHoaDon_Dao cthdDao = new ChiTietHoaDon_Dao();
    private final KhachHang_Dao khDao = new KhachHang_Dao();

    @FXML
    public void initialize() {
        if (dpNgayLapPhieu != null) dpNgayLapPhieu.setValue(LocalDate.now());
        guiMacDinh();
    }

    private void xuLyChuyenSangDoi(ChiTietHoaDon cthdGoc) {
        if (cthdGoc == null || cthdGoc.getLoHang() == null) return;
        String key = cthdGoc.getLoHang().getMaLH();
        int max = Math.max(0, cthdGoc.getSoLuong());
        DoiHangItem vm = doiByMaLH.get(key);
        if (vm == null) {
            vm = new DoiHangItem(cthdGoc, 1, "");
            dsDoi.add(vm);
            doiByMaLH.put(key, vm);
        } else {
            int next = Math.min(max, vm.getSoLuongDoi() + 1);
            vm.setSoLuongDoi(next);
        }
        if (tblSanPhamDoi != null) tblSanPhamDoi.refresh();
    }

    public void guiMacDinh(){

        if (tblSanPhamGoc != null) {
            tblSanPhamGoc.setItems(dsGoc);
            tblSanPhamGoc.setPlaceholder(new Label("Chưa có sản phẩm gốc"));
            safeSetConstrainedResize(tblSanPhamGoc);
        }
        if (tblSanPhamDoi != null) {
            tblSanPhamDoi.setItems(dsDoi);
            tblSanPhamDoi.setEditable(true);
            tblSanPhamDoi.setPlaceholder(new Label("Chưa có chi tiết đổi hàng"));
            safeSetConstrainedResize(tblSanPhamDoi);
        }
        setupTblGocColumns();
        setupTblDoiColumns();
    }

    private void setupTblGocColumns() {
        if (colSTTGoc != null) {
            colSTTGoc.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : String.valueOf(getIndex() + 1));
                }
            });
            colSTTGoc.setSortable(false);
            colSTTGoc.setReorderable(false);
        }
        if (colTenSPGoc != null) {
            colTenSPGoc.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> tenSP(p.getValue())));
        }
        if (colSoLuongGoc != null) {
            colSoLuongGoc.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> String.valueOf(p.getValue().getSoLuong())));
            alignRight(colSoLuongGoc);
        }
        if (colDonViGoc != null) {
            colDonViGoc.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> donViCoBan(p.getValue())));
            alignRight(colDonViGoc);
        }
        if (colDonGiaGoc != null) {
            colDonGiaGoc.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> vnd(p.getValue().getDonGia())));
            alignRight(colDonGiaGoc);
        }
        if (colGiamGiaGoc != null) {
            colGiamGiaGoc.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> vnd(p.getValue().getGiamGia())));
            alignRight(colGiamGiaGoc);
        }
        if (colThanhTienGoc != null) {
            colThanhTienGoc.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(() -> {
                ChiTietHoaDon r = p.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return vnd(tt);
            }));
            alignRight(colThanhTienGoc);
        }
        if (colDoi != null) {
            colDoi.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("↓");
                {
                    btn.setTooltip(new Tooltip("Chuyển xuống danh sách đổi"));
                    btn.setOnAction(e -> {
                        int idx = getIndex();
                        if (idx < 0 || idx >= tblSanPhamGoc.getItems().size()) return;
                        ChiTietHoaDon goc = tblSanPhamGoc.getItems().get(idx);
                        if (goc == null || goc.getLoHang() == null) return;
                        xuLyChuyenSangDoi(goc);
                    });
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setStyle("-fx-alignment: center;");
                }
                @Override protected void updateItem(Void it, boolean empty) {
                    super.updateItem(it, empty);
                    setGraphic(empty ? null : btn);
                    setText(null);
                }
            });
            colDoi.setSortable(false);
            colDoi.setReorderable(false);
        }
    }

    private void setupTblDoiColumns() {
        if (colSTTDoi != null) {
            colSTTDoi.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : String.valueOf(getIndex() + 1));
                }
            });
            colSTTDoi.setSortable(false);
            colSTTDoi.setReorderable(false);
        }
        if (colTenSPDoi != null) {
            colTenSPDoi.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> tenSP(p.getValue().getGoc())));
        }
        if (colDonViDoi != null) {
            colDonViDoi.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> donViCoBan(p.getValue().getGoc())));
            alignRight(colDonViDoi);
        }
        if (colSoLuongDoi != null) {
            colSoLuongDoi.setCellValueFactory(p -> p.getValue().soLuongDoiProperty());
            colSoLuongDoi.setCellFactory(tc -> new TableCell<>() {
                private final Button btnMinus = new Button("-");
                private final Button btnPlus = new Button("+");
                private final TextField tf = new TextField();
                private final HBox box = new HBox(6, btnMinus, tf, btnPlus);
                {
                    tf.setPrefWidth(56);
                    tf.setMaxWidth(56);
                    tf.setStyle("-fx-alignment: center-right;");
                    tf.setTextFormatter(new TextFormatter<>(chg ->
                            chg.getControlNewText().matches("\\d{0,6}") ? chg : null));

                    btnMinus.setOnAction(e -> adjust(-1));
                    btnPlus.setOnAction(e -> adjust(+1));
                    tf.setOnAction(e -> commitFromText());
                    tf.focusedProperty().addListener((o, was, now) -> { if (!now) commitFromText(); });
                }
                private void adjust(int delta) {
                    int idx = getIndex();
                    if (idx < 0 || idx >= tblSanPhamDoi.getItems().size()) return;
                    DoiHangItem vm = tblSanPhamDoi.getItems().get(idx);
                    if (vm == null) return;
                    int cur = vm.getSoLuongDoi();
                    int max = vm.getGoc() == null ? Integer.MAX_VALUE : Math.max(0, vm.getGoc().getSoLuong());
                    int target = Math.max(1, Math.min(max, cur + delta));
                    if (target != cur) vm.setSoLuongDoi(target);
                    tf.setText(String.valueOf(vm.getSoLuongDoi()));
                }
                private void commitFromText() {
                    int idx = getIndex();
                    if (idx < 0 || idx >= tblSanPhamDoi.getItems().size()) return;
                    DoiHangItem vm = tblSanPhamDoi.getItems().get(idx);
                    if (vm == null) return;
                    String s = tf.getText();
                    try {
                        int val = Integer.parseInt(s);
                        if (val <= 0) val = 1;
                        int max = vm.getGoc() == null ? Integer.MAX_VALUE : Math.max(0, vm.getGoc().getSoLuong());
                        if (val > max) val = max;
                        vm.setSoLuongDoi(val);
                    } catch (NumberFormatException ignore) {
                        tf.setText(String.valueOf(vm.getSoLuongDoi()));
                    }
                }
                @Override protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); setText(null); }
                    else {
                        DoiHangItem vm = getTableView().getItems().get(getIndex());
                        tf.setText(vm != null ? String.valueOf(vm.getSoLuongDoi()) : "1");
                        setGraphic(box);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                }
            });
        }
        if (colLyDo != null) {
            colLyDo.setCellValueFactory(p -> p.getValue().lyDoProperty());
            colLyDo.setCellFactory(TextFieldTableCell.forTableColumn());
            colLyDo.setOnEditCommit(ev -> {
                DoiHangItem vm = ev.getRowValue();
                if (vm != null) vm.setLyDo(ev.getNewValue() == null ? "" : ev.getNewValue().trim());
            });
        }
        if (colBo != null) {
            colBo.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("✕");
                {
                    btn.setTooltip(new Tooltip("Bỏ khỏi danh sách đổi"));
                    btn.setOnAction(e -> {
                        int idx = getIndex();
                        if (idx < 0 || idx >= tblSanPhamDoi.getItems().size()) return;
                        DoiHangItem vm = tblSanPhamDoi.getItems().get(idx);
                        if (vm != null && vm.getGoc() != null && vm.getGoc().getLoHang() != null) {
                            String key = vm.getGoc().getLoHang().getMaLH();
                            doiByMaLH.remove(key);
                        }
                        dsDoi.remove(vm);
                        if (tblSanPhamDoi != null) tblSanPhamDoi.refresh();
                    });
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setStyle("-fx-alignment: center;");
                }
                @Override protected void updateItem(Void it, boolean empty) {
                    super.updateItem(it, empty);
                    setGraphic(empty ? null : btn);
                    setText(null);
                }
            });
            colBo.setSortable(false);
            colBo.setReorderable(false);
        }
    }

    private <S, T> void alignRight(TableColumn<S, T> col) {
        col.setCellFactory(tc -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(item));
                setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
            }
        });
    }

    private String tenSP(ChiTietHoaDon row) {
        if (row == null || row.getLoHang() == null) return "";
        Thuoc_SP_TheoLo lo = row.getLoHang();
        Thuoc_SanPham sp = lo.getThuoc();
        return (sp != null && sp.getTenThuoc() != null) ? sp.getTenThuoc() : "";
    }

    private String donViCoBan(ChiTietHoaDon row) {
        if (row == null || row.getLoHang() == null) return "";
        Thuoc_SanPham sp = row.getLoHang().getThuoc();
        if (sp == null || sp.getDsCTDVT() == null || sp.getDsCTDVT().isEmpty()) return "";
        for (ChiTietDonViTinh ct : sp.getDsCTDVT()) {
            if (ct.isDonViCoBan() && ct.getDvt() != null && ct.getDvt().getTenDonViTinh() != null) {
                return ct.getDvt().getTenDonViTinh();
            }
        }
        ChiTietDonViTinh first = sp.getDsCTDVT().get(0);
        return first.getDvt() != null ? first.getDvt().getTenDonViTinh() : "";
    }

    private String vnd(double v) { return vnd(Math.round(Math.max(0, v))); }
    private String vnd(long amount) {
        DecimalFormat df = new DecimalFormat("#,###");
        df.setGroupingUsed(true);
        return df.format(Math.max(0, amount)) + " VNĐ";
    }

    private void safeSetConstrainedResize(TableView<?> tv) {
        try { tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDoi/LapPhieuDoiHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void xuLyTimHoaDonGoc(ActionEvent actionEvent) {
        String ma = txtTimHoaDonGoc == null ? null : txtTimHoaDonGoc.getText();
        if (ma == null || ma.isBlank()) {
            alert(Alert.AlertType.WARNING, "Vui lòng nhập mã hóa đơn gốc.");
            return;
        }
        try {
            HoaDon hd = hoaDonDao.selectById(ma);
            if (hd == null) {
                alert(Alert.AlertType.WARNING, "Không tìm thấy hóa đơn gốc.");
                return;
            }

            String tenKH = "";
            String sdt = "";
            if (hd.getMaKH() != null && hd.getMaKH().getMaKH() != null) {
                KhachHang kh = khDao.selectById(hd.getMaKH().getMaKH());
                if (kh != null) {
                    tenKH = kh.getTenKH() == null ? "" : kh.getTenKH();
                    sdt = kh.getSdt() == null ? "" : kh.getSdt();
                }
            }

            if (lblMaHDGoc != null) lblMaHDGoc.setText(hd.getMaHD());
            if (lblTenKH != null) lblTenKH.setText(tenKH);
            if (lblSDT != null) lblSDT.setText(sdt);
            if (dpNgayLapPhieu != null) dpNgayLapPhieu.setValue(LocalDate.now());

            List<ChiTietHoaDon> lines = cthdDao.selectByMaHD(ma);
            dsGoc.setAll(lines);
            dsDoi.clear();
            doiByMaLH.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
            alert(Alert.AlertType.ERROR, "Lỗi tải hóa đơn gốc: " + ex.getMessage());
        }
    }

    public void xuLyInPhieuDoi(ActionEvent actionEvent) {
        System.out.println("In phiếu đổi clicked");
    }

    public void xuLyDoiHang(ActionEvent actionEvent) {
        System.out.println("Đổi hàng clicked");
    }
}
