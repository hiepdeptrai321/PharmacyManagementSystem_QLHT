package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapHoaDon;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import com.example.pharmacymanagementsystem_qlht.service.ApDungKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.service.DichVuKhuyenMai;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LapHoaDon_Ctrl extends Application {

    private final ObservableList<ChiTietHoaDon> dsChiTietHD = FXCollections.observableArrayList();
    private final IdentityHashMap<ChiTietHoaDon, ChiTietDonViTinh> dvtTheoDong = new java.util.IdentityHashMap<>();
    private final AtomicLong demTruyVan = new java.util.concurrent.atomic.AtomicLong(0);
    private volatile long idTruyVanMoiNhat = 0;
    private volatile Task<List<String>> goiYHienTai;
    private final DichVuKhuyenMai dichVuKM = new DichVuKhuyenMai();
    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private final Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();

    @FXML private Button btnThemKH;
    @FXML private DatePicker dpNgayKeDon;
    @FXML private ChoiceBox<String> cbPhuongThucTT;
    @FXML private Pane paneTienMat;
    @FXML private TextField txtTimThuoc;
    @FXML private TextField txtTenKH;
    @FXML private TextField txtSDT;
    @FXML private TableView<ChiTietHoaDon> tblChiTietHD;
    @FXML private TableColumn <ChiTietHoaDon, String> colSTT;
    @FXML private TableColumn <ChiTietHoaDon, String> colTenSP;
    @FXML private TableColumn <ChiTietHoaDon, String> colSL;
    @FXML private TableColumn <ChiTietHoaDon, String> colDonVi;
    @FXML private TableColumn <ChiTietHoaDon, String> colDonGia;
    @FXML private TableColumn <ChiTietHoaDon, String> colChietKhau;
    @FXML private TableColumn <ChiTietHoaDon, String> colThanhTien;
    @FXML private Label lblTongTien;
    @FXML private Label lblGiamGia;
    @FXML private Label lblVAT;
    @FXML private Label lblThanhTien;
    @FXML private TextField txtSoTienKhachDua;
    @FXML private Label lblTienThua;
    @FXML private Button btnThanhToan;
    @FXML private Label lblGiamTheoHD;

    // popup suggestions
    private final ContextMenu goiYMenu = new ContextMenu();
    private final PauseTransition pause = new PauseTransition(Duration.millis(0));
    private final Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
    private static final String GoiY_css = "/com/example/pharmacymanagementsystem_qlht/css/GoiYThuoc.css";
    private boolean GoiY_cssat = false;
    private boolean tamDungGoiY = false;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        VND.setMaximumFractionDigits(0);
        dpNgayKeDon.setValue(LocalDate.now());
        xuLyPhuongThucTT();
        xuLyTimThuoc();
        xuLyCssGoiY();
        xuLyChiTietHD();
        tblChiTietHD.getItems().addListener((ListChangeListener<Object>) c -> tinhTongTien());
        tinhTongTien();
        initTienMatEvents();
    }



    private void xuLyPhuongThucTT() {
        if (cbPhuongThucTT != null) {
            cbPhuongThucTT.getItems().clear();
            cbPhuongThucTT.getItems().addAll("Phương thức thanh toán", "Tiền mặt", "Chuyển khoản");
            cbPhuongThucTT.setValue("Phương thức thanh toán");
            themFieldTienMat("Phương thức thanh toán");
            cbPhuongThucTT.setOnShowing(event -> {
                cbPhuongThucTT.getItems().remove("Phương thức thanh toán");
            });
            cbPhuongThucTT.setOnHiding(event -> {
                if (!cbPhuongThucTT.getItems().contains("Phương thức thanh toán")) {
                    cbPhuongThucTT.getItems().add(0, "Phương thức thanh toán");
                }
            });
            cbPhuongThucTT.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                themFieldTienMat(newVal);
                if ("Chuyển khoản".equals(newVal)) {
                    hienThiQR();
                }
            });
        }
    }
    private void xuLyTimThuoc() {
        if (txtTimThuoc == null) return;
        pause.setOnFinished(evt -> {
            if (!tamDungGoiY) {
                String keyword = txtTimThuoc.getText();
                layDanhSachThuoc(keyword);
            }
        });
        txtTimThuoc.textProperty().addListener((obs, oldText, newText) -> {
            pause.playFromStart();
        });
        txtTimThuoc.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                Platform.runLater(() -> goiYMenu.hide());
            } else {
                String keyword = txtTimThuoc.getText();
                layDanhSachThuoc(keyword);
            }
        });
    }
    private void xuLyChiTietHD() {
        if (tblChiTietHD != null) {
            tblChiTietHD.setItems(dsChiTietHD);
            cauHinhCotBang();
        }
    }
    private void xuLyCssGoiY() {
        if (!GoiY_cssat) {
            taiCSSGoiYThuoc();
            GoiY_cssat = true;
        }
    }
    private String parseDonVi(String infoText) {
        if (infoText == null) return null;
        // Expect: "Số lượng tồn: <n> <đvt>"
        Matcher m = Pattern.compile("Số lượng tồn:\\s*\\d+\\s+(.*)$").matcher(infoText.trim());
        if (m.find()) return m.group(1).trim();
        return null;
    }
    private void layDanhSachThuoc(String keyword) {
        if (txtTimThuoc == null) return;

        final String key = (keyword == null) ? "" : keyword.trim();
        if (key.isEmpty()) {
            Platform.runLater(goiYMenu::hide);
            return;
        }

        // Increment query id and cancel the previous task if any
        final long thisQueryId = demTruyVan.incrementAndGet();
        idTruyVanMoiNhat = thisQueryId;

        Task<List<String>> newTask = new Task<>() {
            @Override
            protected java.util.List<String> call() {
                if (isCancelled()) return java.util.Collections.emptyList();
                return thuocDao.timTheoTenChiTiet(key, 10);
            }
        };

        Task<java.util.List<String>> prev = goiYHienTai;
        if (prev != null && prev.isRunning()) {
            prev.cancel(true);
        }
        goiYHienTai = newTask;

        newTask.setOnSucceeded(evt -> {
            // Ignore if this is not the latest query or the text changed since we started
            if (thisQueryId != idTruyVanMoiNhat) return;
            String currentText = txtTimThuoc.getText() == null ? "" : txtTimThuoc.getText().trim();
            if (!currentText.equalsIgnoreCase(key)) return;

            java.util.List<String> results = newTask.getValue();
            if (results == null || results.isEmpty()) {
                goiYMenu.hide();
                return;
            }

            goiYMenu.getItems().clear();
            taiCSSGoiYThuoc();
            final double menuWidth = Math.max(300, txtTimThuoc.getWidth());
            int index = 0;

            for (String detail : results) {
                String medicineName;
                String infoText = "";
                int sep = detail.indexOf(" | ");
                if (sep > 0) {
                    medicineName = detail.substring(0, sep);
                    infoText = detail.substring(sep + 3);
                } else {
                    medicineName = detail;
                }

                final String tenDVTChon = parseDonVi(infoText);

                HBox row = new HBox(8);
                row.getStyleClass().add("suggestion-row");
                row.setPrefWidth(menuWidth);
                row.setFillHeight(true);

                Label nameLbl = new Label(medicineName);
                nameLbl.getStyleClass().add("suggestion-name");
                nameLbl.setWrapText(true);

                Label infoLbl = new Label(infoText.isEmpty() ? "" : " | " + infoText);
                infoLbl.getStyleClass().add("suggestion-detail");
                infoLbl.setWrapText(true);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                row.getChildren().addAll(nameLbl, infoLbl, spacer);

                CustomMenuItem mi = new CustomMenuItem(row, true);
                mi.getStyleClass().add("suggestion-item");
                if (index < results.size() - 1) {
                    mi.getStyleClass().add("has-separator");
                }

                final String tenThuocCuoi = medicineName;
                mi.setOnAction(ae -> {
                    txtTimThuoc.setText(tenThuocCuoi);
                    goiYMenu.hide();
                    xuLyChonThuoc(tenThuocCuoi, tenDVTChon);
                });

                goiYMenu.getItems().add(mi);
                index++;
            }

            if (!goiYMenu.isShowing() && txtTimThuoc.isFocused()) {
                goiYMenu.show(txtTimThuoc, Side.BOTTOM, 0, 0);
            }
        });

        newTask.setOnCancelled(evt -> {
            // Do nothing on cancel
        });

        newTask.setOnFailed(evt -> {
            if (thisQueryId != idTruyVanMoiNhat) return; // ignore stale failures
            goiYMenu.hide();
            System.err.println("Không thể tải gợi ý: " + newTask.getException());
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Không thể tải gợi ý thuốc.\nVui lòng thử lại.",
                    ButtonType.OK
            );
            if (txtTimThuoc != null && txtTimThuoc.getScene() != null) {
                alert.initOwner(txtTimThuoc.getScene().getWindow());
            }
            alert.showAndWait();
        });

        Thread th = new Thread(newTask);
        th.setDaemon(true);
        th.start();
    }

    // Replace the whole xuLyChonThuoc method
    private void xuLyChonThuoc(String tenThuoc, String tenDVT) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) return;
        final String key = tenThuoc.trim();
        txtTimThuoc.setText("");

        tamDungGoiY = true;
        pause.stop();
        if (goiYMenu.isShowing()) goiYMenu.hide();

        Task<Thuoc_SanPham> task = new Task<>() {
            @Override
            protected Thuoc_SanPham call() {
                List<Thuoc_SanPham> list = thuocDao.selectSLTheoDonViCoBanByTuKhoa_ChiTietDVT(key);
                if (list == null || list.isEmpty()) return null;
                for (Thuoc_SanPham sp : list) {
                    if (sp.getTenThuoc() != null && key.equalsIgnoreCase(sp.getTenThuoc())) return sp;
                }
                return list.get(0);
            }
        };

        task.setOnSucceeded(e -> {
            try {
                Thuoc_SanPham sp = task.getValue();
                if (sp != null) {
                    if (txtTimThuoc != null) txtTimThuoc.setUserData(sp);
                    themVaoBang(sp, tenDVT); // <-- use chosen unit
                    if (txtTimThuoc != null) {
                        txtTimThuoc.requestFocus();
                        txtTimThuoc.positionCaret(txtTimThuoc.getText().length());
                    }
                }
            } finally {
                tamDungGoiY = false;
            }
        });

        task.setOnFailed(e -> {
            tamDungGoiY = false;
            System.err.println("Fetch medicine failed: " + task.getException());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    private ChiTietDonViTinh chonDonViTheoTen(Thuoc_SanPham sp, String tenDVT) {
        if (sp == null || sp.getDsCTDVT() == null || sp.getDsCTDVT().isEmpty()) return null;
        if (tenDVT != null && !tenDVT.isBlank()) {
            for (ChiTietDonViTinh ct : sp.getDsCTDVT()) {
                if (ct.getDvt() != null && tenDVT.equalsIgnoreCase(ct.getDvt().getTenDonViTinh())) {
                    return ct;
                }
            }
        }
        for (ChiTietDonViTinh ct : sp.getDsCTDVT()) if (ct.isDonViCoBan()) return ct;
        return sp.getDsCTDVT().get(0);
    }
    private void themVaoBang(Thuoc_SanPham sp, String tenDVTChon) {
        if (sp == null || sp.getMaThuoc() == null) return;

        ChiTietDonViTinh chosen = chonDonViTheoTen(sp, tenDVTChon);
        if (chosen == null || chosen.getDvt() == null) return;

        int tonBase = tonBase(sp);
        if (tonBase <= 0) { canhBaoTonKhongDu(); return; }

        int curBase = tongDangChonBase(sp);
        int addBase = toBaseQty(1, chosen);
        if (curBase + addBase > tonBase) { canhBaoTonKhongDu(); return; }

        ChiTietHoaDon same = timDongGiongDVT(sp, chosen);
        if (same != null) {
            // increase existing row of the same unit if still within stock
            int remainBaseForThisRow = tonBase - tongDangChonBaseTru(sp, same);
            int maxForThisRow = (int)Math.floor(remainBaseForThisRow / heSo(chosen));
            if (same.getSoLuong() + 1 > maxForThisRow) { canhBaoTonKhongDu(); return; }
            same.setSoLuong(same.getSoLuong() + 1);
            same.setDonGia(chosen.getGiaBan());
            apDungKMChoRow(same);
            if (tblChiTietHD != null) tblChiTietHD.refresh();
            return;
        }

        // create new row with the chosen unit
        ChiTietHoaDon cthd = new ChiTietHoaDon();
        ganThuocVaoCTHD(cthd, sp);
        cthd.setSoLuong(1);
        cthd.setDonGia(chosen.getGiaBan());
        apDungKMChoRow(cthd);
        dsChiTietHD.add(cthd);
        dvtTheoDong.put(cthd, chosen);

        if (tblChiTietHD != null) tblChiTietHD.refresh();
        Platform.runLater(() -> {
            if (txtTimThuoc != null) {
                txtTimThuoc.requestFocus();
                txtTimThuoc.positionCaret(txtTimThuoc.getText().length());
            }
        });
    }

    private void taiCSSGoiYThuoc() {
        // Keep default class and add our custom class if missing
        if (!goiYMenu.getStyleClass().contains("suggestion-menu")) {
            goiYMenu.getStyleClass().add("suggestion-menu");
        }

        goiYMenu.setOnShowing(e -> {
            var url = getClass().getResource(GoiY_css);
            if (url == null) return;

            var scene = goiYMenu.getScene();
            if (scene == null) return;

            String css = url.toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
        });
    }
    private void cauHinhCotBang() {
        if (tblChiTietHD == null) return;
        if (colSTT != null) {
            colSTT.setCellFactory(tc -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : String.valueOf(getIndex() + 1));
                }
            });
            colSTT.setSortable(false);
            colSTT.setReorderable(false);
        }
        if (colTenSP != null) {
            colTenSP.setCellValueFactory(p ->
                    new ReadOnlyStringWrapper(layTenSP(p.getValue()))
            );
        }
        if (colSL != null) {
            colSL.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getSoLuong())));
            colSL.setCellFactory(tc -> new TableCell<>() {
                private final Button btnMinus = new Button("-");
                private final Button btnPlus  = new Button("+");
                private final TextField tf    = new TextField();
                private final HBox box        = new HBox(6, btnMinus, tf, btnPlus);

                {
                    tf.setPrefWidth(56);
                    tf.setMaxWidth(56);
                    tf.setStyle("-fx-alignment: center-right;");
                    tf.setTextFormatter(new TextFormatter<>(chg ->
                            chg.getControlNewText().matches("\\d{0,6}") ? chg : null));

                    btnMinus.setOnAction(e -> { goiYMenu.hide(); pause.stop(); adjust(-1); });
                    btnPlus.setOnAction(e -> { goiYMenu.hide(); pause.stop(); adjust(+1); });
                    tf.setOnAction(e -> { goiYMenu.hide(); pause.stop(); commitFromText(); });
                    tf.focusedProperty().addListener((o, was, now) -> { if (!now) commitFromText(); });
                }

                private void adjust(int delta) {
                    int idx = getIndex();
                    if (idx < 0 || idx >= getTableView().getItems().size()) return;
                    ChiTietHoaDon row = getTableView().getItems().get(idx);
                    if (row == null) return;

                    Thuoc_SanPham sp = spOf(row);
                    ChiTietDonViTinh dvt = dvtOf(row);
                    if (sp == null || dvt == null) return;

                    int cur = row.getSoLuong();
                    int target = Math.max(1, cur + delta);

                    // compute max allowed for this row given other rows
                    int max = maxSLDong(row);
                    if (target > max) {
                        canhBaoTonKhongDu();
                        target = Math.max(1, max);
                    }
                    if (target != cur) {
                        row.setSoLuong(target);
                        apDungKMChoRow(row);
                        if (tblChiTietHD != null) tblChiTietHD.refresh();
                        tinhTongTien();
                    }
                    tf.setText(String.valueOf(row.getSoLuong()));
                }

                private void commitFromText() {
                    int idx = getIndex();
                    if (idx < 0 || idx >= getTableView().getItems().size()) return;
                    ChiTietHoaDon row = getTableView().getItems().get(idx);
                    if (row == null) return;

                    String s = tf.getText();
                    if (s == null || s.isBlank()) { tf.setText(String.valueOf(row.getSoLuong())); return; }

                    try {
                        int entered = Integer.parseInt(s);
                        if (entered <= 0) entered = 1;
                        int max = maxSLDong(row);
                        if (entered > max) {
                            canhBaoTonKhongDu();
                            entered = Math.max(1, max);
                        }
                        if (entered != row.getSoLuong()) {
                            row.setSoLuong(entered);
                            apDungKMChoRow(row);
                            if (tblChiTietHD != null) tblChiTietHD.refresh();
                            tinhTongTien();
                        }
                        tf.setText(String.valueOf(row.getSoLuong()));
                        tf.setText(String.valueOf(row.getSoLuong()));
                    } catch (NumberFormatException ignore) {
                        tf.setText(String.valueOf(row.getSoLuong()));
                    }
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) { setGraphic(null); setText(null); }
                    else {
                        ChiTietHoaDon r = getTableView().getItems().get(getIndex());
                        tf.setText(r != null ? String.valueOf(r.getSoLuong()) : "1");
                        setGraphic(box);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                }
            });
        }
        if (colDonVi != null) {
            colDonVi.setCellValueFactory(p ->
                    new ReadOnlyStringWrapper(layTenDonVi(p.getValue()))
            );
            canPhai(colDonVi);
        }
        if (colDonGia != null) {
            colDonGia.setCellValueFactory(p ->
                    new ReadOnlyStringWrapper(formatVND(p.getValue().getDonGia()))
            );
            canPhai(colDonGia);
        }
        if (colChietKhau != null) {
            colChietKhau.setCellValueFactory(p ->
                    new ReadOnlyStringWrapper(formatVND(p.getValue().getGiamGia()))
            );
            canPhai(colChietKhau);
        }
        if (colThanhTien != null) {
            colThanhTien.setCellValueFactory(p -> {
                ChiTietHoaDon r = p.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return new ReadOnlyStringWrapper(formatVND(tt));
            });
            canPhai(colThanhTien);
        }

        dsChiTietHD.addListener((ListChangeListener<ChiTietHoaDon>) c -> tblChiTietHD.refresh());
    }

    private void canPhai(TableColumn<ChiTietHoaDon, String> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
            }
        });
    }

    private String layTenSP(ChiTietHoaDon row) {
        if (row == null || row.getLoHang() == null) return "";
        var sp = row.getLoHang().getThuoc();
        String ten = (sp != null && sp.getTenThuoc() != null) ? sp.getTenThuoc() : "";
        String donVi = layTenDonVi(row);
        return ten;
    }
    private String layTenDonVi(ChiTietHoaDon row) {
        if (row == null) return "";
        ChiTietDonViTinh dvtChon = dvtTheoDong.get(row);
        if (dvtChon != null && dvtChon.getDvt() != null && dvtChon.getDvt().getTenDonViTinh() != null) {
            return dvtChon.getDvt().getTenDonViTinh();
        }
        if (row.getLoHang() == null) return "";
        return tenDonViCoBan(row.getLoHang().getThuoc());
    }

    // Trả về tên đơn vị cơ bản ko thì lấy đơn vị đầu tiên
    private String tenDonViCoBan(Thuoc_SanPham sp) {
        if (sp == null || sp.getDsCTDVT() == null || sp.getDsCTDVT().isEmpty()) return "";
        for (ChiTietDonViTinh ct : sp.getDsCTDVT()) {
            if (ct.isDonViCoBan() && ct.getDvt() != null && ct.getDvt().getTenDonViTinh() != null) {
                return ct.getDvt().getTenDonViTinh();
            }
        }
        ChiTietDonViTinh first = sp.getDsCTDVT().get(0);
        return (first.getDvt() != null) ? String.valueOf(first.getDvt().getTenDonViTinh()) : "";
    }

    // Gắn sp vào cthd qua lo hang (theo model)
    private void ganThuocVaoCTHD(ChiTietHoaDon cthd, Thuoc_SanPham sp) {
        Thuoc_SP_TheoLo lo = new Thuoc_SP_TheoLo();
        lo.setThuoc(sp);
        cthd.setLoHang(lo);
    }

    private String formatVND(double v) {
        return String.format("%,.0f đ", v);
    }


    private void themFieldTienMat(String value) {
        if (paneTienMat != null) {
            paneTienMat.setVisible("Tiền mặt".equals(value));
        }
    }
    private int tonToiDaTheoSP(Thuoc_SanPham sp, ChiTietDonViTinh dvt) {
        if (sp == null || sp.getMaThuoc() == null || dvt == null) return Integer.MAX_VALUE;
        int tongTonCoBan = thuocDao.getTongTonCoBan(sp.getMaThuoc());
        double heSo = dvt.getHeSoQuyDoi() > 0 ? dvt.getHeSoQuyDoi() : 1.0;
        return (int) Math.floor(tongTonCoBan / heSo);
    }

    private int tonToiDaTheoDong(ChiTietHoaDon row) {
        if (row == null || row.getLoHang() == null) return Integer.MAX_VALUE;
        Thuoc_SanPham sp = row.getLoHang().getThuoc();
        ChiTietDonViTinh dvt = dvtTheoDong.get(row);
        return tonToiDaTheoSP(sp, dvt);
    }

    private void canhBaoTonKhongDu() {
        Alert a = new Alert(Alert.AlertType.WARNING, "Số lượng tồn không đủ", ButtonType.OK);
        if (tblChiTietHD != null && tblChiTietHD.getScene() != null) {
            a.initOwner(tblChiTietHD.getScene().getWindow());
        }
        a.showAndWait();
    }

    private void hienThiQR() {
        Stage qrStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label label = new Label("Quét mã QR dưới đây để thanh toán");
        // Link ma QR
        InputStream is = getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/qr_mb.jpg");
        Image qrImg = (is != null) ? new Image(is) : new Image(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/img/qr_mb.jpg").toExternalForm());
        ImageView qrImage = new ImageView(qrImg);
        qrImage.setFitWidth(500);
        qrImage.setPreserveRatio(true);
        vbox.getChildren().addAll(label, qrImage);
        Scene scene = new Scene(vbox, 600, 600);
        qrStage.setScene(scene);
        qrStage.show();
    }
//------------Xử lý khách hàng ----------------
    @FXML
    private void xuLyTimKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml"));
            Parent root = loader.load();
            TimKiemKhachHang_Ctrl ctrl = loader.getController();

            Stage stage = new Stage();
            ctrl.setOnSelected((KhachHang kh) -> {
                if (txtTenKH != null) txtTenKH.setText(kh.getTenKH());
                if (txtSDT != null) txtSDT.setText(kh.getSdt());
                stage.close();
            });

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xuLyThemKH(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/ThemKhachHang_GUI.fxml"));
            Parent root = loader.load();
            Object ctrl = loader.getController();

            Stage st = new Stage();
            st.setScene(new Scene(root));
            if (btnThemKH != null && btnThemKH.getScene() != null) {
                st.initOwner(btnThemKH.getScene().getWindow());
            }

            st.setOnHidden(e -> {
                try {
                    Object o = null;
                    try {
                        java.lang.reflect.Method m1 = ctrl.getClass().getMethod("getKhachHangMoi");
                        o = m1.invoke(ctrl);
                    } catch (NoSuchMethodException ignore) {
                        try {
                            java.lang.reflect.Method m2 = ctrl.getClass().getMethod("getSavedKhachHang");
                            o = m2.invoke(ctrl);
                        } catch (NoSuchMethodException ignored) { }
                    }
                    if (o instanceof KhachHang kh) {
                        Platform.runLater(() -> {
                            if (txtTenKH != null) txtTenKH.setText(kh.getTenKH());
                            if (txtSDT != null) txtSDT.setText(kh.getSdt());
                        });
                    }
                } catch (Exception ignored) {
                }
            });
            st.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //----------Xử lý tbao SLTon ---------------
    private double heSo(ChiTietDonViTinh dvt) {
        return (dvt == null || dvt.getHeSoQuyDoi() <= 0) ? 1.0 : dvt.getHeSoQuyDoi();
    }
    private int toBaseQty(int soLuong, ChiTietDonViTinh dvt) {
        return (int)Math.round(soLuong * heSo(dvt));
    }
    private Thuoc_SanPham spOf(ChiTietHoaDon row) {
        return (row != null && row.getLoHang() != null) ? row.getLoHang().getThuoc() : null;
    }
    private ChiTietDonViTinh dvtOf(ChiTietHoaDon row) {
        return dvtTheoDong.get(row);
    }
    private int tonBase(Thuoc_SanPham sp) {
        return (sp == null) ? 0 : thuocDao.getTongTonCoBan(sp.getMaThuoc());
    }
    private ChiTietDonViTinh layDVTCoBan(Thuoc_SanPham sp) {
        if (sp == null || sp.getDsCTDVT() == null || sp.getDsCTDVT().isEmpty()) return null;
        for (ChiTietDonViTinh ct : sp.getDsCTDVT()) if (ct.isDonViCoBan()) return ct;
        // fallback: unit with smallest conversion factor
        ChiTietDonViTinh min = sp.getDsCTDVT().get(0);
        for (ChiTietDonViTinh ct : sp.getDsCTDVT()) {
            if (ct.getHeSoQuyDoi() < min.getHeSoQuyDoi()) min = ct;
        }
        return min;
    }
    private int fromBaseQty(int baseQty, ChiTietDonViTinh dvt) {
        return (int) Math.floor(baseQty / heSo(dvt));
    }

    private int tongDangChonBase(Thuoc_SanPham sp) {
        if (sp == null || sp.getMaThuoc() == null) return 0;
        String ma = sp.getMaThuoc();
        int sum = 0;
        for (ChiTietHoaDon r : dsChiTietHD) {
            Thuoc_SanPham rsp = spOf(r);
            if (rsp != null && ma.equals(rsp.getMaThuoc())) {
                sum += toBaseQty(r.getSoLuong(), dvtOf(r));
            }
        } return sum;
    }

    private int tongDangChonBaseTru(Thuoc_SanPham sp, ChiTietHoaDon truRow) {
        if (sp == null || sp.getMaThuoc() == null) return 0;
        String ma = sp.getMaThuoc();
        int sum = 0;
        for (ChiTietHoaDon r : dsChiTietHD) {
            if (r == truRow) continue;
            Thuoc_SanPham rsp = spOf(r);
            if (rsp != null && ma.equals(rsp.getMaThuoc())) {
                sum += toBaseQty(r.getSoLuong(), dvtOf(r));
            }
        }
        return sum;
    }
    private ChiTietHoaDon timDongGiongDVT(Thuoc_SanPham sp, ChiTietDonViTinh dvt) {
        if (sp == null || sp.getMaThuoc() == null || dvt == null || dvt.getDvt() == null) return null;
        String ma = sp.getMaThuoc();
        String maDvt = dvt.getDvt().getMaDVT();
        for (ChiTietHoaDon r : dsChiTietHD) {
            Thuoc_SanPham rsp = spOf(r);
            ChiTietDonViTinh rdvt = dvtOf(r);
            if (rsp != null && rdvt != null && rdvt.getDvt() != null &&
                    ma.equals(rsp.getMaThuoc()) && maDvt.equals(rdvt.getDvt().getMaDVT())) {
                return r;
            }
        }
        return null;
    }
    private int maxSLDong(ChiTietHoaDon row) {
        Thuoc_SanPham sp = spOf(row);
        ChiTietDonViTinh dvt = dvtOf(row);
        if (sp == null || dvt == null) return Integer.MAX_VALUE;
        int baseRemain = tonBase(sp) - tongDangChonBaseTru(sp, row);
        if (baseRemain <= 0) return 0;
        return (int)Math.floor(baseRemain / heSo(dvt));
    }
    private ChiTietHoaDon gopDongCungSanPhamVeCoBan(Thuoc_SanPham sp) {
        if (sp == null || sp.getMaThuoc() == null) return null;
        String ma = sp.getMaThuoc();
        ChiTietDonViTinh base = layDVTCoBan(sp);
        if (base == null) return null;

        ChiTietHoaDon first = null;
        int totalBase = 0;
        java.util.List<ChiTietHoaDon> remove = new java.util.ArrayList<>();

        for (ChiTietHoaDon r : dsChiTietHD) {
            Thuoc_SP_TheoLo lo = r.getLoHang();
            if (lo != null && lo.getThuoc() != null && ma.equals(lo.getThuoc().getMaThuoc())) {
                if (first == null) first = r; else remove.add(r);
                totalBase += toBaseQty(r.getSoLuong(), dvtTheoDong.get(r));
            }
        }

        if (first != null) {
            dvtTheoDong.put(first, base);
            first.setDonGia(base.getGiaBan());
            first.setSoLuong(fromBaseQty(totalBase, base)); // base unit => exact integer
            if (!remove.isEmpty()) dsChiTietHD.removeAll(remove);
        }
        return first;
    }
    //---------Xu Ly Khuyen Mai ---------
    private final DichVuKhuyenMai kmService = new DichVuKhuyenMai();
    private ApDungKhuyenMai tinhKMHoaDon(BigDecimal baseSauKMHang) {
        return kmService.apDungChoHoaDon(baseSauKMHang, LocalDate.now());
    }

    private ApDungKhuyenMai tinhKMChoDong(String maThuoc, int soLuong, BigDecimal donGia) {
        return kmService.apDungChoSP(maThuoc, soLuong, donGia, LocalDate.now());
    }


    private void capNhatTongTien() {
        BigDecimal tongHang = BigDecimal.ZERO;   // sum of price*qty
        BigDecimal giamDong = BigDecimal.ZERO;   // sum of per-row discounts

        for (ChiTietHoaDon row : dsChiTietHD) {
            BigDecimal line = BigDecimal.valueOf(row.getDonGia()).multiply(BigDecimal.valueOf(row.getSoLuong()));
            tongHang = tongHang.add(line);
            giamDong = giamDong.add(BigDecimal.valueOf(row.getGiamGia()));
        }

        // Base for invoice-level KM = subtotal after per-row KM
        BigDecimal baseForInvoiceKM = tongHang.subtract(giamDong).max(BigDecimal.ZERO);

        // Only 1 best invoice KM (as per new rule)
        ApDungKhuyenMai kmHD = tinhKMHoaDon(baseForInvoiceKM);
        BigDecimal giamHoaDon = kmHD.getDiscount() == null ? BigDecimal.ZERO : kmHD.getDiscount();

        // Base after invoice KM
        BigDecimal baseSauHD = baseForInvoiceKM.subtract(giamHoaDon).max(BigDecimal.ZERO);

        // VAT 5% on the amount after all discounts
        BigDecimal vat = baseSauHD.multiply(new BigDecimal("0.05")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal thanhTien = baseSauHD.add(vat);

        // Update UI
        if (lblGiamGia != null)     lblGiamGia.setText(cur(giamDong));
        if (lblTongTien != null)    lblTongTien.setText(cur(baseForInvoiceKM));
        if (lblGiamTheoHD != null)  lblGiamTheoHD.setText(cur(giamHoaDon));
        if (lblVAT != null)         lblVAT.setText(cur(vat));
        if (lblThanhTien != null)   lblThanhTien.setText(cur(thanhTien));

        updateTienThua();
    }
    // 5) After updating per-row KM, re-run totals (keeps invoice KM in sync)
    private void apDungKMChoRow(ChiTietHoaDon row) {
        if (row == null || row.getLoHang() == null || row.getLoHang().getThuoc() == null) return;

        String maThuoc = row.getLoHang().getThuoc().getMaThuoc();
        int soLuong = row.getSoLuong();
        BigDecimal donGia = BigDecimal.valueOf(row.getDonGia());

        try {
            ApDungKhuyenMai kq = tinhKMChoDong(maThuoc, soLuong, donGia);
            row.setGiamGia(kq != null && kq.getDiscount() != null ? kq.getDiscount().doubleValue() : 0.0);
        } finally {
            // Recompute totals so invoice KM (best of LKM004/LKM005) stays in sync
            capNhatTongTien();
        }
    }

    // 6) Hook recomputation on list changes (add/remove rows)
    private void initListeners() {
        // If dsChiTietHD is ObservableList:
        // dsChiTietHD.addListener((ListChangeListener<ChiTietHoaDon>) c -> capNhatTongTien());
        // Also call capNhatTongTien() after quantity/price edits.
    }


    //-----Xu Ly giao dich
    private String toTangKemText(Map<String, Integer> freeMap) {
        if (freeMap == null || freeMap.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : freeMap.entrySet()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(e.getKey()).append("×").append(e.getValue());
        }
        return sb.toString();
    }
    private String cur(BigDecimal v) { return VND.format(v.max(BigDecimal.ZERO)); }
    private void tinhTongTien() {
        if (tblChiTietHD == null) return;

        BigDecimal tongSauGiamTruocVAT = BigDecimal.ZERO;
        BigDecimal tongGiamGia = BigDecimal.ZERO;

        for (ChiTietHoaDon r : tblChiTietHD.getItems()) {
            if (r == null) continue;

            apDungKMChoRow(r);

            BigDecimal soLuong = BigDecimal.valueOf(Math.max(0, r.getSoLuong()));
            BigDecimal donGia = BigDecimal.valueOf(Math.max(0, r.getDonGia()));
            BigDecimal giamGia = BigDecimal.valueOf(Math.max(0, r.getGiamGia()));

            BigDecimal thanhTienRaw = soLuong.multiply(donGia);
            BigDecimal thanhSauGiam = thanhTienRaw.subtract(giamGia);
            if (thanhSauGiam.signum() < 0) thanhSauGiam = BigDecimal.ZERO;

            tongGiamGia = tongGiamGia.add(giamGia);
            tongSauGiamTruocVAT = tongSauGiamTruocVAT.add(thanhSauGiam);
        }

        BigDecimal vat = tongSauGiamTruocVAT.multiply(new BigDecimal("0.05"))
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal thanhTien = tongSauGiamTruocVAT.add(vat);

        if (lblGiamGia != null) lblGiamGia.setText(cur(tongGiamGia));
        if (lblTongTien != null) lblTongTien.setText(cur(tongSauGiamTruocVAT));
        if (lblVAT != null) lblVAT.setText(cur(vat));
        if (lblThanhTien != null) lblThanhTien.setText(cur(thanhTien));
        updateTienThua();
    }
    //----------Xử lý Tien Thua ----------
    private void initTienMatEvents() {
        txtSoTienKhachDua.textProperty().addListener((obs, oldVal, newVal) -> updateTienThua());
        txtSoTienKhachDua.textProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            String digits = n.replaceAll("[^\\d]", "");
            if (!n.equals(digits)) {
                int caret = txtSoTienKhachDua.getCaretPosition();
                Platform.runLater(() -> {
                    txtSoTienKhachDua.setText(digits);
                    txtSoTienKhachDua.positionCaret(Math.min(caret - (n.length() - digits.length()), digits.length()));
                });
            }
        });
        updateTienThua();
    }
    private void updateTienThua() {
        long thanhTien = ceilToThousand(parseVND(lblThanhTien.getText()));
        long khachDua = parseVND(txtSoTienKhachDua.getText());

        if (khachDua < thanhTien) {
            lblTienThua.setText("Chưa đủ tiền thanh toán");
            lblTienThua.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else {
            long tienThua = khachDua - thanhTien;
            lblTienThua.setText(formatVND(tienThua));
            lblTienThua.setStyle("");
        }
    }

    private long parseVND(String text) {
        if (text == null) return 0L;
        String digits = text.replaceAll("[^\\d]", "");
        return digits.isEmpty() ? 0L : Long.parseLong(digits);
    }

    private String formatVND(long amount) {
        DecimalFormat df = new DecimalFormat("#,###");
        df.setGroupingUsed(true);
        return df.format(amount) + " đ";
    }

    private long ceilToThousand(long amount) {
        if (amount <= 0) return 0;
        long mod = amount % 1000;
        return mod == 0 ? amount : (amount / 1000 + 1) * 1000;
    }


    public void xoaRong(ActionEvent actionEvent) {
        txtTimThuoc.setText("");
    }
    @FXML
    private void xuLyThanhToan(ActionEvent actionEvent) {
        if (tblChiTietHD.getItems() == null || tblChiTietHD.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Không có sản phẩm nào để thanh toán.").showAndWait();
            return;
        }
        NhanVien nv = DangNhap_Ctrl.user;
        if (nv == null) {
            new Alert(Alert.AlertType.ERROR, "Chưa có nhân viên nào đăng nhập.").showAndWait();
            return;
        }
        final String SQL_NEXT_MAHD     = "SELECT TOP 1 MaHD FROM HoaDon ORDER BY MaHD DESC";
        final String SQL_INSERT_HOADON = "INSERT INTO HoaDon (MaHD, MaNV, MaKH, NgayLap, TrangThai) VALUES (?, ?, ?, ?, ?)";
        final String SQL_INSERT_CTHD   = "INSERT INTO ChiTietHoaDon (MaHD, MaLH, SoLuong, DonGia, GiamGia) VALUES (?, ?, ?, ?, ?)";
        final String SQL_NEXT_MAKH     = "SELECT TOP 1 MaKH FROM KhachHang ORDER BY MaKH DESC";
        final String SQL_INSERT_KH     = "INSERT INTO KhachHang (MaKH, TenKH, SDT) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = ConnectDB.getInstance();
            con.setAutoCommit(false);
            KhachHang kh = null;
            if (txtTenKH != null && txtSDT != null) {
                String ten = txtTenKH.getText();
                String sdt = txtSDT.getText();

                if ((ten != null && !ten.isBlank()) || (sdt != null && !sdt.isBlank())) {
                    kh = new KhachHang();
                    kh.setTenKH(ten);
                    kh.setSdt(sdt);

                    KhachHang_Dao khDao = new KhachHang_Dao();
                    kh = khDao.findOrCreateByPhone(sdt, ten);
                }
            }

            if (kh != null && (kh.getMaKH() == null || kh.getMaKH().isBlank())) {
                String maKH = "KH001";
                try (PreparedStatement ps = con.prepareStatement(SQL_NEXT_MAKH);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String last = rs.getString(1);
                        try {
                            int num = Integer.parseInt(last.substring(2)) + 1;
                            maKH = String.format("KH%03d", num);
                        } catch (Exception ignored) { }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_KH)) {
                    ps.setString(1, maKH);
                    ps.setString(2, kh.getTenKH());
                    ps.setString(3, kh.getSdt());
                    ps.executeUpdate();
                }
                kh.setMaKH(maKH);
            }

            allocateLotsAutomatically(con, tblChiTietHD.getItems());

            // 1 Ktra tồn kho
            for (ChiTietHoaDon line : tblChiTietHD.getItems()) {
                Thuoc_SP_TheoLo lo = line.getLoHang();
                if (lo == null || lo.getMaLH() == null || lo.getMaLH().isBlank()) {
                    throw new IllegalStateException("Vui lòng chọn lô (MaLH) cho từng sản phẩm trước khi thanh toán.");
                }
                Thuoc_SP_TheoLo lotNow = new Thuoc_SP_TheoLo_Dao().selectById(lo.getMaLH());
                if (lotNow == null) {
                    throw new IllegalStateException("Không tìm thấy thông tin lô hàng " + lo.getMaLH());
                }
                int needBase = toBaseUnits(line);
                if (needBase <= 0) {
                    throw new IllegalStateException("Số lượng không hợp lệ cho lô hàng " + lo.getMaLH());
                }
                if (lotNow.getSoLuongTon() < needBase) {
                    throw new IllegalStateException("Không đủ số lượng tồn " + lo.getMaLH() + ". Cần " + needBase + ", chỉ còn " + lotNow.getSoLuongTon());
                }
            }

            // 2) Tạo MaHD
            String maHD = "HD001";
            try (PreparedStatement ps = con.prepareStatement(SQL_NEXT_MAHD);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastMa = rs.getString(1);
                    try {
                        int num = Integer.parseInt(lastMa.substring(2)) + 1;
                        maHD = String.format("HD%03d", num);
                    } catch (Exception ignore) { }
                }
            }


            // 3 Thêm HoaDon
            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_HOADON)) {
                ps.setString(1, maHD);
                ps.setString(2, nv.getMaNV());
                ps.setString(3, kh != null ? kh.getMaKH() : null);
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps.setBoolean(5, true);
                ps.executeUpdate();
            }

            // 4 Thêm ChiTietHoaDon
            Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();
            for (ChiTietHoaDon line : tblChiTietHD.getItems()) {
                Thuoc_SP_TheoLo lo = line.getLoHang();
                String maLH = lo.getMaLH();

                int soLuongBase = toBaseUnits(line);
                double donGia = line.getDonGia();
                double giamGia = line.getGiamGia();

                try (PreparedStatement psCT = con.prepareStatement(SQL_INSERT_CTHD)) {
                    psCT.setString(1, maHD);
                    psCT.setString(2, maLH);
                    psCT.setInt(3, soLuongBase);
                    psCT.setDouble(4, donGia);
                    psCT.setDouble(5, giamGia);
                    psCT.executeUpdate();
                }

                boolean ok = loDao.giamTonKhoByMaLo(con, maLH, soLuongBase);
                if (!ok) {
                    throw new IllegalStateException("Không thể cập nhật tồn kho cho lô " + maLH + ". Dữ liệu có thể đã thay đổi.");
                }
            }

            con.commit();

            new Alert(Alert.AlertType.INFORMATION, "Lập hóa đơn thành công.").showAndWait();

            tblChiTietHD.getItems().clear();
            lblTongTien.setText("0 VNĐ");
            lblVAT.setText("0 VNĐ");
            lblThanhTien.setText("0 VNĐ");
            lblGiamGia.setText("0 VNĐ");
            lblGiamTheoHD.setText("0 VNĐ");
            txtSoTienKhachDua.clear();
            lblTienThua.setText("0 VNĐ");
        } catch (Exception ex) {
            ex.printStackTrace();
            if (con != null) {
                try {
                    if (!con.getAutoCommit()) con.rollback();
                } catch (Exception ignore) { }
            }
            new Alert(Alert.AlertType.ERROR, "Lập hóa đơn thất bại: " + ex.getMessage()).showAndWait();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception ignore) { }
            }
        }
    }
    private void allocateLotsAutomatically(Connection con, List<ChiTietHoaDon> chiTietList) throws SQLException {
        Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();
        List<ChiTietHoaDon> perLotLines = new ArrayList<>();

        for (ChiTietHoaDon original : chiTietList) {
            if (original == null || original.getLoHang() == null || original.getLoHang().getThuoc() == null) {
                throw new IllegalStateException("Thông tin sản phẩm không hợp lệ.");
            }
            Thuoc_SanPham sp = original.getLoHang().getThuoc();
            int requestedDisplayQty = Math.max(0, original.getSoLuong());
            if (requestedDisplayQty <= 0) continue;

            ChiTietDonViTinh selDvt = dvtOf(original);
            if (selDvt == null) selDvt = layDVTCoBan(sp);
            double factor = heSo(selDvt);

            int remainingBase = (int) Math.round(requestedDisplayQty * factor);

            while (remainingBase > 0) {
                Thuoc_SP_TheoLo lo = loDao.selectOldestAvailableLot(con, sp.getMaThuoc());
                if (lo == null) {
                    throw new IllegalStateException("Không đủ hàng cho sản phẩm: " + sp.getTenThuoc());
                }

                int availableBase = lo.getSoLuongTon();
                int takeBase = Math.min(remainingBase, availableBase);
                int takeDisplay = (int) Math.round(takeBase / factor);
                if (takeDisplay <= 0) takeDisplay = 1; // ensure at least one display unit

                ChiTietHoaDon cthdTheoLo = new ChiTietHoaDon();
                cthdTheoLo.setLoHang(lo);
                cthdTheoLo.setSoLuong(takeDisplay);
                cthdTheoLo.setDonGia(original.getDonGia());
                cthdTheoLo.setGiamGia(original.getGiamGia());

                perLotLines.add(cthdTheoLo);
                dvtTheoDong.put(cthdTheoLo, selDvt);

                remainingBase -= takeBase;
            }
        }

        chiTietList.clear();
        chiTietList.addAll(perLotLines);
    }

    private String getMaThuocFromRow(ChiTietHoaDon line) {
        if (line == null || line.getLoHang() == null) return null;
        try {
            // common model: lot holds Thuoc object
            var lo = line.getLoHang();
            try {
                if (lo.getThuoc() != null && lo.getThuoc().getMaThuoc() != null) {
                    return lo.getThuoc().getMaThuoc();
                }
            } catch (Exception ignore) { }
            try {
                java.lang.reflect.Method m = lo.getClass().getMethod("getMaThuoc");
                Object v = m.invoke(lo);
                if (v != null) return String.valueOf(v);
            } catch (NoSuchMethodException ignore) { }
        } catch (Exception ignored) { }
        return null;
    }


    private int toBaseUnits(ChiTietHoaDon line) {
        if (line == null) return 0;
        ChiTietDonViTinh dvt = dvtOf(line);
        double factor = heSo(dvt);
        return (int) Math.round(line.getSoLuong() * factor);
    }

}