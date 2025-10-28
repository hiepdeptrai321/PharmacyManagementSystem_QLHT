package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuTra;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import com.example.pharmacymanagementsystem_qlht.service.TraHangItem;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.WARNING;

public class LapPhieuTraHang_Ctrl extends Application {
    @FXML private TextField txtTimHoaDon;
    @FXML private Button btnTimHoaDon;
    @FXML private TableView<ChiTietHoaDon> tblSanPhamHoaDon;
    @FXML private TableColumn<ChiTietHoaDon, String> colSTTGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colTenSPGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colSLGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colDonViGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colDonGiaGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colGiamGiaGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colThanhTienGoc;
    @FXML private TableColumn<ChiTietHoaDon, Void> colTra;

    @FXML private TextField lblMaHDGoc;
    @FXML private Label lblTenKH;
    @FXML private Label lblSDT;
    @FXML private DatePicker dpNgayLapPhieu;
    @FXML private Label lblTongTienGoc;
    @FXML private Label lblTongTienTraLai;
    @FXML private Label lblVAT;
    @FXML private Label lblSoTienTraLai;
    @FXML private TextArea txtGhiChu;



    @FXML private TableView<TraHangItem> tblChiTietTraHang;
    @FXML private TableColumn<TraHangItem, String> colSTTTra;
    @FXML private TableColumn<TraHangItem, String> colTenSPTra;
    @FXML private TableColumn<TraHangItem, Number> colSLTra;
    @FXML private TableColumn<TraHangItem, String> colLyDo;
    @FXML private TableColumn<TraHangItem, String> colDonViTra;
    @FXML private TableColumn<TraHangItem, Double> colDonGiaTra;
    @FXML private TableColumn<TraHangItem, Double> colThanhTienTra;
    @FXML private TableColumn<TraHangItem, Void> colBo;



    private final ObservableList<ChiTietHoaDon> dsGoc = FXCollections.observableArrayList();
    private final ObservableList<TraHangItem> dsTra = FXCollections.observableArrayList();
    private final Map<String, TraHangItem> traByKey = new HashMap<>();
    private final HoaDon_Dao hoaDonDao = new HoaDon_Dao();
    private final ChiTietHoaDon_Dao cthdDao = new ChiTietHoaDon_Dao();
    private final KhachHang_Dao khDao = new KhachHang_Dao();

    private final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    @Override
    public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuTra/LapPhieuTraHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        dpNgayLapPhieu.setValue(LocalDate.now());
        if (tblSanPhamHoaDon != null) {
            tblSanPhamHoaDon.setItems(dsGoc);
            tblSanPhamHoaDon.setPlaceholder(new Label("Chưa có sản phẩm gốc"));
            safeSetConstrainedResize(tblSanPhamHoaDon);
        }
        if (tblChiTietTraHang != null) {
            tblChiTietTraHang.setItems(dsTra);
            tblChiTietTraHang.setEditable(true);
            tblChiTietTraHang.setPlaceholder(new Label("Chưa có chi tiết đổi hàng"));
            safeSetConstrainedResize(tblChiTietTraHang);
        }
        setupTblGocColumns();
        setupTblTraColumns();
    }
    private void safeSetConstrainedResize(TableView<?> tv) {
        try { tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
    }

    private void setupTblGocColumns() {
        if (colSTTGoc != null) {
            colSTTGoc.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(String.valueOf(tblSanPhamHoaDon.getItems().indexOf(cd.getValue()) + 1))
            );
            colSTTGoc.setCellFactory(tc -> new TableCell<ChiTietHoaDon, String>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            });
            colSTTGoc.setSortable(false);
            colSTTGoc.setReorderable(false);
        }
        if (colTenSPGoc != null) {
            colTenSPGoc.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(tenSP(cd.getValue()))
            );
        }
        if (colSLGoc != null) {
            colSLGoc.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(String.valueOf(cd.getValue().getSoLuong()))
            );
            alignRight(colSLGoc);
        }

        // Đơn vị
        if (colDonViGoc != null) {
            colDonViGoc.setCellValueFactory(cd -> {
                ChiTietHoaDon c = cd.getValue();
                String dv = "";
                if (c != null && c.getDvt() != null && c.getDvt().getTenDonViTinh() != null) {
                    dv = c.getDvt().getTenDonViTinh();
                } else {
                    dv = donViCoBan(c);
                }
                return new ReadOnlyObjectWrapper<>(dv);
            });
            alignRight(colDonViGoc);
        }
        if (colDonGiaGoc != null) {
            colDonGiaGoc.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(vnd(cd.getValue().getDonGia()))
            );
            alignRight(colDonGiaGoc);
        }
        if (colGiamGiaGoc != null) {
            colGiamGiaGoc.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(vnd(cd.getValue().getGiamGia()))
            );
            alignRight(colGiamGiaGoc);
        }
        if (colThanhTienGoc != null) {
            colThanhTienGoc.setCellValueFactory(cd -> {
                ChiTietHoaDon r = cd.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return new ReadOnlyObjectWrapper<>(vnd(tt));
            });
            alignRight(colThanhTienGoc);
        }
        if (colTra != null) {
            colTra.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("↓");
                {
                    btn.getStyleClass().add("btn-doi");
                    btn.setTooltip(new Tooltip("Chuyển xuống danh sách đổi"));
                    btn.setOnAction(e -> {
                        int idx = getIndex();
                        if (idx < 0 || idx >= tblSanPhamHoaDon.getItems().size()) return;

                        ChiTietHoaDon goc = tblSanPhamHoaDon.getItems().get(idx);
                        if (goc == null) return;

                        xuLyChuyenSangTra(goc);
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
            colTra.setSortable(false);
            colTra.setReorderable(false);
        }

    }

    private void xuLyChuyenSangTra(ChiTietHoaDon cthdGoc) {
        if (cthdGoc == null || cthdGoc.getLoHang() == null) return;
        String key = cthdGoc.getLoHang().getMaLH() + "_" +
                (cthdGoc.getDvt() != null ? cthdGoc.getDvt().getMaDVT() : "null");
        int max = Math.max(0, cthdGoc.getSoLuong());
        TraHangItem vm = traByKey.get(key);
        if (vm == null) {
            vm = new TraHangItem(cthdGoc, 1, "");
            dsTra.add(vm);
            traByKey.put(key, vm);
        } else {
            int next = Math.min(max, vm.getSoLuongTra() + 1);
            vm.setSoLuongTra(next);
        }
        capNhatTienTra();
        if (tblChiTietTraHang != null) tblChiTietTraHang.refresh();
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

    
    private void setupTblTraColumns() {
        if (colSTTTra != null) {
            colSTTTra.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : String.valueOf(getIndex() + 1));
                }
            });
            colSTTTra.setSortable(false);
            colSTTTra.setReorderable(false);
        }
        if (colTenSPTra != null) {
            colTenSPTra.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> tenSP(p.getValue().getGoc())));
        }
        if (colDonViTra != null) {
            colDonViTra.setCellValueFactory(p -> javafx.beans.binding.Bindings.createStringBinding(() -> {
                ChiTietHoaDon goc = p.getValue().getGoc();
                if (goc == null) return "";
                DonViTinh dvt = goc.getDvt();
                if (dvt != null && dvt.getTenDonViTinh() != null) {
                    return dvt.getTenDonViTinh();
                }
                return donViCoBan(goc);
            }));
            alignRight(colDonViTra);
        }
        if (colSLTra != null) {
            colSLTra.setCellValueFactory(p -> p.getValue().soLuongTraProperty());
            colSLTra.setCellFactory(tc -> new TableCell<>() {
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
                    if (idx < 0 || idx >= tblChiTietTraHang.getItems().size()) return;
                    TraHangItem vm = tblChiTietTraHang.getItems().get(idx);
                    if (vm == null) return;
                    int cur = vm.getSoLuongTra();
                    int max = vm.getGoc() == null ? Integer.MAX_VALUE : Math.max(0, vm.getGoc().getSoLuong());
                    int target = Math.max(1, Math.min(max, cur + delta));
                    if (target != cur) vm.setSoLuongTra(target);
                    capNhatTienTra();
                    tf.setText(String.valueOf(vm.getSoLuongTra()));
                }
                private void commitFromText() {
                    int idx = getIndex();
                    if (idx < 0 || idx >= tblChiTietTraHang.getItems().size()) return;
                    TraHangItem vm = tblChiTietTraHang.getItems().get(idx);
                    if (vm == null) return;
                    String s = tf.getText();
                    try {
                        int val = Integer.parseInt(s);
                        if (val <= 0) val = 1;
                        int max = vm.getGoc() == null ? Integer.MAX_VALUE : Math.max(0, vm.getGoc().getSoLuong());
                        if (val > max) val = max;
                        vm.setSoLuongTra(val);
                        capNhatTienTra();
                    } catch (NumberFormatException ignore) {
                        tf.setText(String.valueOf(vm.getSoLuongTra()));
                    }
                }
                @Override protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); setText(null); }
                    else {
                        TraHangItem vm = getTableView().getItems().get(getIndex());
                        tf.setText(vm != null ? String.valueOf(vm.getSoLuongTra()) : "1");
                        setGraphic(box);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                }
            });
        }
        if (colDonGiaTra != null) {
            colDonGiaTra.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(cd.getValue().getDonGiaUnit())
            );
            colDonGiaTra.setCellFactory(tc -> new TableCell<TraHangItem, Double>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        double val = item == null ? 0.0 : item.doubleValue();
                        setText(vnd(val)); // dùng hàm vnd(double) của controller (đã có)
                        setStyle("-fx-alignment: CENTER-RIGHT;");
                    }
                }
            });
        }
        if (colThanhTienTra != null) {
            colThanhTienTra.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(cd.getValue().getThanhTienTra())
            );
            colThanhTienTra.setCellFactory(tc -> new TableCell<TraHangItem, Double>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(vnd(item));
                    }
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            });
        }
        if (colLyDo != null) {
            colLyDo.setCellValueFactory(p -> p.getValue().lyDoProperty());
            colLyDo.setCellFactory(TextFieldTableCell.forTableColumn());
            colLyDo.setOnEditCommit(ev -> {
                TraHangItem vm = ev.getRowValue();
                if (vm != null) vm.setLyDo(ev.getNewValue() == null ? "" : ev.getNewValue().trim());
            });
        }
        if (colBo != null) {
            colBo.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("✕");
                {
                    btn.getStyleClass().add("btn-bo");
                    btn.setTooltip(new Tooltip("Bỏ khỏi danh sách đổi"));
                    btn.setOnAction(e -> {
                        int idx = getIndex();
                        if (idx < 0 || idx >= tblChiTietTraHang.getItems().size()) return;

                        TraHangItem vm = tblChiTietTraHang.getItems().get(idx);
                        if (vm != null && vm.getGoc() != null && vm.getGoc().getLoHang() != null) {
                            String key = vm.getGoc().getLoHang().getMaLH() + "_" +
                                    (vm.getGoc().getDvt() != null ? vm.getGoc().getDvt().getMaDVT() : "null");
                            traByKey.remove(key);
                        }
                        dsTra.remove(vm);
                        tblChiTietTraHang.refresh();
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



    // ========== Nghiệp vụ ==========

    public void xuLyTimHDGoc(ActionEvent actionEvent) {
        String ma = txtTimHoaDon.getText().trim();
        if (ma.isBlank()) {
            thongBaoTuyChinh(WARNING, "Thiếu thông tin", "Vui lòng nhập mã hóa đơn.");
            return;
        }


        try {
            HoaDon hd = hoaDonDao.selectById(ma);

            if (hd == null) {
                thongBaoTuyChinh(WARNING, "Không tìm thấy", "Hóa đơn không tồn tại.");
                return;
            }

            // load line items first to compute discounts
            List<ChiTietHoaDon> list = cthdDao.selectByMaHD(ma);
            double perLineDiscount = 0;
            if (list != null) {
                for (ChiTietHoaDon ct : list) {
                    if (ct != null) perLineDiscount += ct.getGiamGia();
                }
            }

            double invoiceLevelDiscount = 0;
            try {
                String[] names = {"getGiamGia", "getGiamGiaTong", "getTongGiamGia", "getTienGiamGia", "getKhuyenMai"};
                for (String nm : names) {
                    try {
                        Method m = hd.getClass().getMethod(nm);
                        if (m != null) {
                            Object val = m.invoke(hd);
                            if (val instanceof Number) {
                                invoiceLevelDiscount = ((Number) val).doubleValue();
                                break;
                            } else if (val != null) {
                                invoiceLevelDiscount = Double.parseDouble(val.toString());
                                break;
                            }
                        }
                    } catch (NoSuchMethodException ignored) { }
                }
            } catch (Exception ignored) { }

            if (invoiceLevelDiscount + perLineDiscount > 0) {
                thongBaoTuyChinh(WARNING,
                        "Hóa đơn có áp dụng giảm giá tổng!",
                        "Không thể thực hiện trả hàng với hóa đơn này.");
                return;
            }

            LocalDate ngayHD = hd.getNgayLap().toLocalDateTime().toLocalDate();
            if (ngayHD.isBefore(LocalDate.now().minusDays(7))) {
                thongBaoTuyChinh(WARNING, "Quá hạn", "Hóa đơn đã quá 7 ngày, không thể trả hàng.");
                return;
            }

            lblMaHDGoc.setText(hd.getMaHD());

            String tenKH = hd.getMaKH() != null && hd.getMaKH().getTenKH() != null
                    ? hd.getMaKH().getTenKH()
                    : "";
            String sdt = hd.getMaKH() != null && hd.getMaKH().getSdt() != null
                    ? hd.getMaKH().getSdt()
                    : "";

            lblTenKH.setText(tenKH);
            lblSDT.setText(sdt);
            dpNgayLapPhieu.setValue(LocalDate.now());

            List<ChiTietHoaDon> lines = cthdDao.selectByMaHD(ma);
            dsTra.clear();
            traByKey.clear();
            dsGoc.setAll(lines);
            double tongTienGoc = 0;
            for (ChiTietHoaDon ct : lines) {
                double tt = ct.getSoLuong() * ct.getDonGia() - ct.getGiamGia();
                tongTienGoc += Math.max(0, tt);
            }
            lblTongTienGoc.setText(vnd(tongTienGoc));
            tblChiTietTraHang.refresh();

            tblSanPhamHoaDon.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            thongBaoTuyChinh(ERROR, "Lỗi", "Không thể tải hóa đơn.");
        }
    }

    public void xuLyTraHangVaIn(ActionEvent actionEvent) {
    }

    public void xuLyTraHang(ActionEvent actionEvent) {
    }
    private void capNhatTienTra() {
        double tongTienTraHang = 0;

        for (TraHangItem item : dsTra) {
            ChiTietHoaDon goc = item.getGoc();
            if (goc == null) continue;

            double tt = item.getSoLuongTra() * goc.getDonGia() - goc.getGiamGia();
            tongTienTraHang += Math.max(0, tt);
        }

        lblTongTienTraLai.setText(vnd(tongTienTraHang));

        double vat = tongTienTraHang * 0.05; // bạn đặt VAT = 8% phải không?
        lblVAT.setText(vnd(vat));

        double soTienTra = tongTienTraHang + vat;
        lblSoTienTraLai.setText(vnd(soTienTra));
    }

    private void thongBaoTuyChinh(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(header);
        alert.setHeaderText(header);
        alert.setContentText(message);
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongBaoAlert.css"
        ).toExternalForm());
        Stage stage = (Stage) pane.getScene().getWindow();
        switch (type) {
            case WARNING:
                pane.getStyleClass().add("warning-alert");
                stage.getIcons().add(new Image(
                        getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/iconcanhbao.jpg")));
                break;
            case INFORMATION:
                pane.getStyleClass().add("info-alert");
                break;
            case ERROR:
                pane.getStyleClass().add("error-alert");
                break;
        }

        stage.setWidth(550);
        stage.setHeight(260);

        alert.showAndWait();
    }

    public void xuLyHuy(ActionEvent actionEvent) {
        dsGoc.clear();
        dsTra.clear();
        lblMaHDGoc.setText("");
        lblTenKH.setText("");
        lblSDT.setText("");
        dpNgayLapPhieu.setValue(LocalDate.now());
        txtGhiChu.setText("");
        txtTimHoaDon.clear();
        tblSanPhamHoaDon.getItems().clear();
        tblChiTietTraHang.getItems().clear();
        txtTimHoaDon.requestFocus();
    }
}