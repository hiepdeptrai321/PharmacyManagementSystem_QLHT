package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapHoaDon;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon.ChiTietHoaDon_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHangTrongHD_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import com.example.pharmacymanagementsystem_qlht.service.ApDungKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.service.DichVuKhuyenMai;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.properties.HorizontalAlignment;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import javafx.stage.FileChooser;
import java.io.File;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.pharmacymanagementsystem_qlht.TienIch.TuyChinhAlert.hien;
import static com.example.pharmacymanagementsystem_qlht.TienIch.TuyChinhAlert.hienOpt;
import static javafx.scene.control.Alert.AlertType.*;

public class LapHoaDon_Ctrl extends Application {
    public static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";
    private final ObservableList<ChiTietHoaDon> dsChiTietHD = FXCollections.observableArrayList();
    private final IdentityHashMap<ChiTietHoaDon, ChiTietDonViTinh> dvtTheoDong = new IdentityHashMap<>();
    private final AtomicLong demTruyVan = new AtomicLong(0);

    private volatile long idTruyVanMoiNhat = 0;
    private volatile Task<List<String>> goiYHienTai;
    private final DichVuKhuyenMai dichVuKM = new DichVuKhuyenMai();
    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private final Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();
    private final IdentityHashMap<ChiTietHoaDon, ApDungKhuyenMai> kmTheoDong = new IdentityHashMap<>();
    private final Thuoc_SanPham_Dao spDao = new Thuoc_SanPham_Dao();
    private final ConcurrentHashMap<String, String> tenSpCache = new ConcurrentHashMap<>();
    private Map<ChiTietHoaDon, Integer> baseQtyMap = new IdentityHashMap<>();
    @FXML private Button btnThanhToanVaIn;
    @FXML private Button btnThemKH;
    @FXML private DatePicker dpNgayLap;
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
    @FXML private TableColumn <ChiTietHoaDon, String> colBo;
    @FXML private Label lblTongTien;
    @FXML private Label lblGiamGia;
    @FXML private Label lblVAT;
    @FXML private Label lblThanhTien;
    @FXML private TextField txtSoTienKhachDua;
    @FXML private Label lblTienThua;
    @FXML private Button btnThanhToan;
    @FXML private Label lblGiamTheoHD;
    @FXML private RadioButton rbOTC;
    @FXML private TextField txtMaDonThuoc;
    private Stage qrStage;
    private static final String OTC_OFF = "Không kê đơn(OTC)";
    private static final String OTC_ON  = "Kê đơn(ETC)";

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
        dpNgayLap.setValue(LocalDate.now());
        xuLyPhuongThucTT();
        xuLyTimThuoc();
        xuLyCssGoiY();
        xuLyChiTietHD();
        tblChiTietHD.getItems().addListener((ListChangeListener<Object>) c -> tinhTongTien());
        tinhTongTien();
        initTienMatEvents();
        chuyenHoaDon();
    }

    private void chuyenHoaDon() {
        if (rbOTC == null) return;
        ToggleGroup group = rbOTC.getToggleGroup();
        if (group != null) group.selectToggle(null);
        rbOTC.setSelected(false);

        rbOTC.textProperty().bind(
                Bindings.when(rbOTC.selectedProperty())
                        .then(OTC_ON)
                        .otherwise(OTC_OFF)
        );
        rbOTC.selectedProperty().addListener((obs, was, isSelected) -> applyLoaiHoaDonUI(isSelected));
        // Apply initial state
        applyLoaiHoaDonUI(rbOTC.isSelected());
    }
    private void applyLoaiHoaDonUI(boolean isETC) {
        // ETC => allow entering prescription id and date
        if (txtMaDonThuoc != null) {
            txtMaDonThuoc.setDisable(!isETC);
            txtMaDonThuoc.setEditable(isETC);
            if (!isETC) txtMaDonThuoc.clear();
        }
    }



    private void xuLyPhuongThucTT() {
        if (cbPhuongThucTT != null) {
            cbPhuongThucTT.getItems().clear();
            cbPhuongThucTT.getItems().addAll("Tiền mặt", "Chuyển khoản");
            cbPhuongThucTT.setValue("Tiền mặt");
            themFieldTienMat("Tiền mặt");
            anQR();

            cbPhuongThucTT.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.equals(oldVal)) {
                    return;
                }

                themFieldTienMat(newVal);

                if ("Chuyển khoản".equals(newVal)) {
                    hienThiQR();
                } else {
                    anQR();
                }
            });
        }
    }
    private void themFieldTienMat(String value) {
        if (paneTienMat != null) {
            boolean visible = "Tiền mặt".equals(value);
            paneTienMat.setVisible(visible);
            paneTienMat.setManaged(visible);
        }
    }
    private void hienThiQR() {
        if (qrStage != null && qrStage.isShowing()) {
            qrStage.toFront();
            return;
        }

        qrStage = new Stage();

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label label = new Label("Quét mã QR dưới đây để thanh toán");
        InputStream is = getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/qr_mb.jpg");
        Image qrImg = (is != null) ? new Image(is) : new Image(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/img/qr_mb.jpg").toExternalForm());
        ImageView qrImage = new ImageView(qrImg);
        qrImage.setFitWidth(500);
        qrImage.setPreserveRatio(true);
        vbox.getChildren().addAll(label, qrImage);
        Scene scene = new Scene(vbox, 600, 600);
        qrStage.setScene(scene);
        qrStage.setTitle("Thanh Toán Chuyển Khoản");
        qrStage.show();
    }

    private void anQR() {
        if (qrStage != null && qrStage.isShowing()) {
            qrStage.close();
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
                    ERROR,
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
            System.err.println("Tải thuốc thất bại " + task.getException());
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
                        // AFTER changing quantity, attempt auto-conversion to larger unit(s)
                        autoConvertUnitsAfterChange(row);
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
                            // attempt auto-conversion after user edited absolute number
                            autoConvertUnitsAfterChange(row);
                            if (tblChiTietHD != null) tblChiTietHD.refresh();
                            tinhTongTien();
                        }
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
        if (colBo != null) {
            // Provide a dummy value so the column renders
            colBo.setCellValueFactory(data -> new ReadOnlyStringWrapper(""));

            colBo.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("↑"); // up arrow
                {
                    btn.setTooltip(new Tooltip("Bỏ dòng này"));
                    btn.setFocusTraversable(false);
                    btn.setOnAction(e -> {
                        int index = getIndex();
                        if (index < 0 || index >= getTableView().getItems().size()) return;

                        ChiTietHoaDon item = getTableView().getItems().get(index);
                        if (item == null) return;

                        // Clean up per-row mappings to avoid leaks
                        dvtTheoDong.remove(item);
                        kmTheoDong.remove(item);

                        // Remove the row; STT will auto-shift (index-based STT)
                        dsChiTietHD.remove(item);

                        // Refresh UI and totals
                        if (tblChiTietHD != null) tblChiTietHD.refresh();
                        capNhatTongTien(); // or tinhTongTien();
                    });
                    // Optional styling
                    btn.setMaxWidth(Double.MAX_VALUE);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setStyle("-fx-alignment: center;");
                }
                @Override
                protected void updateItem(String val, boolean empty) {
                    super.updateItem(val, empty);
                    setText(null);
                    setGraphic(empty ? null : btn);
                }
            });

            colBo.setSortable(false);
            colBo.setReorderable(false);
            colBo.setEditable(false);
            colBo.setStyle("-fx-alignment: CENTER; -fx-padding: 0 4 0 4;");
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
        return ten + giftSuffixForRow(row);
    }
    private String giftSuffixForRow(ChiTietHoaDon row) {
        com.example.pharmacymanagementsystem_qlht.service.ApDungKhuyenMai ap = kmTheoDong.get(row);
        if (ap == null) {
            if (row == null || row.getLoHang() == null || row.getLoHang().getThuoc() == null) return "";
            String maThuoc = row.getLoHang().getThuoc().getMaThuoc();
            ap = kmService.apDungChoSP(maThuoc, Math.max(0, row.getSoLuong()),
                    java.math.BigDecimal.valueOf(Math.max(0, row.getDonGia())),
                    java.time.LocalDate.now());
        }
        if (ap == null || ap.getFreeItems().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String ma : ap.getFreeItems().keySet()) {
            String name = tenSpCache.computeIfAbsent(ma, k -> {
                var sp = spDao.selectById(k);
                return (sp != null && sp.getTenThuoc() != null) ? sp.getTenThuoc() : "";
            });
            if (name != null && !name.isBlank()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(name);
            }
        }
        return sb.length() > 0 ? " (có tặng kèm " + sb + ")" : "";
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





//------------Xử lý khách hàng ----------------
    @FXML
    private void xuLyTimKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml"));
            Parent root = loader.load();
            TimKiemKhachHangTrongHD_Ctrl ctrl = loader.getController();

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
    private void canhBaoTonKhongDu() {

        Alert a = new Alert(Alert.AlertType.WARNING, "Số lượng tồn không đủ", ButtonType.OK);
        if (tblChiTietHD != null && tblChiTietHD.getScene() != null) {
            a.initOwner(tblChiTietHD.getScene().getWindow());
        }
        a.showAndWait();
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
        BigDecimal tongHang = BigDecimal.ZERO;
        BigDecimal giamDong = BigDecimal.ZERO;

        for (ChiTietHoaDon row : dsChiTietHD) {
            BigDecimal line = BigDecimal.valueOf(row.getDonGia()).multiply(BigDecimal.valueOf(row.getSoLuong()));
            tongHang = tongHang.add(line);
            giamDong = giamDong.add(BigDecimal.valueOf(row.getGiamGia()));
        }

        BigDecimal baseForInvoiceKM = tongHang.subtract(giamDong).max(BigDecimal.ZERO);

        ApDungKhuyenMai kmHD = tinhKMHoaDon(baseForInvoiceKM);

        BigDecimal giamHoaDon = kmHD.getDiscount() == null ? BigDecimal.ZERO : kmHD.getDiscount();
        BigDecimal baseSauHD = baseForInvoiceKM.subtract(giamHoaDon).max(BigDecimal.ZERO);
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
            ApDungKhuyenMai kq =
                    kmService.apDungChoSP(maThuoc, soLuong, donGia, LocalDate.now());

            kmTheoDong.put(row, kq);
            row.setGiamGia(kq != null && kq.getDiscount() != null ? kq.getDiscount().doubleValue() : 0.0);
        } finally {
            capNhatTongTien();
        }
    }
    //-------XuLy khi qua don vi duoi
    private ChiTietDonViTinh findNextLargerUnit(Thuoc_SanPham sp, ChiTietDonViTinh current) {
        if (sp == null || sp.getDsCTDVT() == null || current == null) return null;
        return sp.getDsCTDVT().stream()
                .filter(ct -> ct.getHeSoQuyDoi() > current.getHeSoQuyDoi())
                .sorted(Comparator.comparingDouble(ChiTietDonViTinh::getHeSoQuyDoi))
                .findFirst()
                .orElse(null);
    }
    private void autoConvertUnitsAfterChange(ChiTietHoaDon changedRow) {
        if (changedRow == null || changedRow.getLoHang() == null) return;
        Thuoc_SanPham sp = spOf(changedRow);

        if (sp == null || sp.getDsCTDVT() == null || sp.getDsCTDVT().size() < 2) {
            apDungKMChoRow(changedRow); // Vẫn áp dụng KM cho dòng vừa thay đổi
            tinhTongTien(); // Tính lại tổng tiền
            return;
        }

        String maThuoc = sp.getMaThuoc();

        // 1 Lấy danh sách DVT của sản phẩm, to toi nho
        List<ChiTietDonViTinh> dvtsSorted = sp.getDsCTDVT().stream()
                .sorted(Comparator.comparingDouble(ChiTietDonViTinh::getHeSoQuyDoi).reversed())
                .toList(); 

        if (dvtsSorted.isEmpty()) return; 

        // 2 Tính tổng số lượng cơ bản (vd: tổng số 'viên') từ all dòng
        int tongSLCoBan = 0;
        for (ChiTietHoaDon row : List.copyOf(dsChiTietHD)) {
            Thuoc_SanPham spRow = spOf(row);
            if (spRow != null && spRow.getMaThuoc().equals(maThuoc)) {
                tongSLCoBan += toBaseQty(row.getSoLuong(), dvtOf(row));
            }
        }

        // 3 Xóa all dòng của sản phẩm này
        dsChiTietHD.removeIf(row -> {
            Thuoc_SanPham spRow = spOf(row);
            if (spRow != null && spRow.getMaThuoc().equals(maThuoc)) {
                dvtTheoDong.remove(row);
                kmTheoDong.remove(row);
                return true; 
            }
            return false; // neu sp khac thi giu lai
        });

        // 4 Phân bổ lại `tongSLCoBan` vào các dòng mới (từ lớn đến nhỏ)
        int remainingBaseQty = tongSLCoBan;

        for (ChiTietDonViTinh dvt : dvtsSorted) { // Vòng lặp từ Hộp -> Vỉ -> Viên
            if (remainingBaseQty == 0) break; // Hết số lượng để chia

            int heSo = (int)Math.round(heSo(dvt));
            if (heSo <= 0) continue; // Bỏ qua DVT lỗi nếu có

            int newQty = remainingBaseQty / heSo; // Số lượng ở đơn vị DVT này

            if (newQty > 0) {
                ChiTietHoaDon newRow = new ChiTietHoaDon();
                ganThuocVaoCTHD(newRow, sp);
                newRow.setSoLuong(newQty);
                newRow.setDonGia(dvt.getGiaBan());

                dvtTheoDong.put(newRow, dvt); //  map DVT cho dòng mới
                dsChiTietHD.add(newRow);     // Thêm dòng mới vào danh sách

                // CapNhat
                remainingBaseQty = remainingBaseQty % heSo;
            }
        }
        if (tblChiTietHD != null) tblChiTietHD.refresh();
        tinhTongTien();
    }




    //-----Xu Ly giao dich

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
            hien(WARNING, "Lỗi thanh toán", "Chưa có sản phẩm nào trong hóa đơn.");
            return;
        }
        NhanVien nv = DangNhap_Ctrl.user;
        if (nv == null) {
            hien(ERROR, "Lỗi đăng nhập", "Chưa có nhân viên nào đăng nhập.");
            return;
        }
        final String SQL_NEXT_MAHD     = "SELECT TOP 1 MaHD FROM HoaDon ORDER BY MaHD DESC";
        final String SQL_INSERT_HOADON = "INSERT INTO HoaDon (MaHD, MaNV, MaKH, NgayLap, TrangThai, LoaiHoaDon, MaDonThuoc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        final String SQL_INSERT_CTHD   = "INSERT INTO ChiTietHoaDon (MaHD, MaLH, MaDVT, SoLuong, DonGia, GiamGia) VALUES (?, ?, ?, ?, ?, ?)";
        final String SQL_NEXT_MAKH     = "SELECT TOP 1 MaKH FROM KhachHang ORDER BY MaKH DESC";
        final String SQL_INSERT_KH     = "INSERT INTO KhachHang (MaKH, TenKH, SDT) VALUES (?, ?, ?)";
        String thanhTienStr = lblThanhTien.getText().replaceAll("[^0-9]", "");
        if (thanhTienStr.isEmpty()) {
            hien(ERROR, "Lỗi dữ liệu", "Không thể đọc tổng tiền từ giao diện.");
            return;
        }

        double thanhTien = Double.parseDouble(thanhTienStr);
        double thanhTienLamTron = Math.ceil(thanhTien / 1000) * 1000;
        String soTienKhachDuaStr = txtSoTienKhachDua.getText().replaceAll("[^0-9]", "");
        if (soTienKhachDuaStr.isEmpty()) {
            hien(WARNING, "Thiếu thông tin", "Vui lòng nhập số tiền khách đưa trước khi thanh toán.");
            return;
        }
        double soTienKhachDua = Double.parseDouble(soTienKhachDuaStr);
        if (soTienKhachDua < thanhTienLamTron) {
            hien(ERROR, "Thiếu tiền",
                    "Số tiền khách đưa không đủ để thanh toán.\n" +
                            "Cần ít nhất: " + String.format("%,.0f đ", thanhTienLamTron));
            return;
        }

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
            baseQtyMap.clear();
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
                int needBase = line.getSoLuong();
                if (needBase <= 0) {
                    throw new IllegalStateException("Số lượng không hợp lệ cho lô hàng " + lo.getMaLH());
                }
                if (lotNow.getSoLuongTon() < needBase) {
                    throw new IllegalStateException("Không đủ số lượng tồn " + lo.getMaLH() + ". Cần " + needBase + ", chỉ còn " + lotNow.getSoLuongTon());
                }
            }
            String loaiHoaDon = "OTC";
            String maDonThuoc = null;

            if (rbOTC.isSelected()) {
                loaiHoaDon = "ETC";
                maDonThuoc = txtMaDonThuoc.getText();

                if (maDonThuoc == null || maDonThuoc.trim().isEmpty()) {
                    // new Alert(Alert.AlertType.ERROR, "Hóa đơn ETC bắt buộc phải có Mã đơn thuốc.").showAndWait();
                    // con.rollback(); // Hủy giao dịch
                    // return; // Dừng lại

                    throw new IllegalStateException("Hóa đơn Kê đơn (ETC) bắt buộc phải có Mã đơn thuốc.");
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
                ps.setString(6, loaiHoaDon);
                ps.setObject(7, maDonThuoc);
                ps.executeUpdate();
            }

            // 4 Thêm ChiTietHoaDon
            Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();
            for (ChiTietHoaDon line : tblChiTietHD.getItems()) {
                Thuoc_SP_TheoLo lo = line.getLoHang();
                String maLH = lo.getMaLH();

                int soLuongDisplay = line.getSoLuong();
                String maDVTDisplay = line.getDvt().getMaDVT();
                double donGiaDisplay = line.getDonGia(); // Vd: 100.000
                double giamGiaDisplay = line.getGiamGia();

                Integer soLuongBase = baseQtyMap.get(line);
                if (soLuongBase == null || soLuongBase <= 0) {
                    throw new IllegalStateException("Không tìm thấy số lượng cơ bản để trừ kho cho: " + line);
                }

                try (PreparedStatement psCT = con.prepareStatement(SQL_INSERT_CTHD)) {
                    psCT.setString(1, maHD);
                    psCT.setString(2, maLH);
                    psCT.setString(3, maDVTDisplay);
                    psCT.setInt(4, soLuongDisplay);
                    psCT.setDouble(5, donGiaDisplay);
                    psCT.setDouble(6, giamGiaDisplay);
                    psCT.executeUpdate();
                }

                boolean ok = loDao.giamTonKhoByMaLo(con, maLH, soLuongBase);
                if (!ok) {
                    throw new IllegalStateException("Không thể cập nhật tồn kho cho lô " + maLH + ". Dữ liệu có thể đã thay đổi.");
                }
            }

            dsChiTietHD.clear();
            dsChiTietHD.addAll(tblChiTietHD.getItems());
            con.commit();
            Optional<ButtonType> result = hienOpt(
                    INFORMATION,
                    "Thanh toán thành công",
                    "Hóa đơn " + maHD + " đã được lập thành công."
            );
            HoaDon hdMoi = new HoaDon();
            hdMoi.setMaHD(maHD);
            hdMoi.setMaNV(nv);
            hdMoi.setMaKH(kh);
            hdMoi.setNgayLap(Timestamp.valueOf(LocalDateTime.now()));
            hdMoi.setLoaiHoaDon(loaiHoaDon);
            hdMoi.setTrangThai(true);
            hdMoi.setMaDonThuoc(maDonThuoc);

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoaDon/ChiTietHoaDon_GUI.fxml"));
                    Parent root = loader.load();

                    // Lấy controller để truyền dữ liệu hóa đơn
                    ChiTietHoaDon_Ctrl ctrl = loader.getController();
                    ctrl.setHoaDon(hdMoi);

                    Stage stage = new Stage();
                    stage.setTitle("Chi tiết hóa đơn " + maHD);
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                   // ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();

                } catch (IOException e) {
                    hien(ERROR, "Lỗi",
                            "Không thể mở giao diện chi tiết hóa đơn:\n" + e.getMessage());
                    e.printStackTrace();
                }
            }

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
            hien(ERROR, "Lỗi", "Lập hóa đơn thất bại:\n" + ex.getMessage());
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
        Map<String, List<Thuoc_SP_TheoLo>> lotCache = new HashMap<>();
        // luu sl co ban can giam theo dong
        Map<ChiTietHoaDon, Integer> baseQtyToReduce = new IdentityHashMap<>();

        // lap tung dong trong gio hang
        for (ChiTietHoaDon original : chiTietList) {
            if (original == null || original.getLoHang() == null || original.getLoHang().getThuoc() == null) {
                throw new IllegalStateException("Thông tin sản phẩm trong giỏ hàng không hợp lệ.");
            }

            Thuoc_SanPham sp = original.getLoHang().getThuoc();
            String maThuoc = sp.getMaThuoc();
            int requestedDisplayQty = Math.max(0, original.getSoLuong());
            if (requestedDisplayQty <= 0) continue;

            // 1. Lấy dvt co ban cua dòng gốc
            ChiTietDonViTinh dvtGoc = dvtOf(original);
            if (dvtGoc == null) dvtGoc = layDVTCoBan(sp);
            double heSoGoc = heSo(dvtGoc);
            original.setDvt(dvtGoc.getDvt());

            // 2. Tính tổng số lượng cơ bản cần thiết
            int neededBaseQty = (int) Math.round(requestedDisplayQty * heSoGoc);


            List<Thuoc_SP_TheoLo> danhSachLo = lotCache.get(maThuoc);
            if (danhSachLo == null) {
                danhSachLo = loDao.selectAllAvailableLots(con, maThuoc);
                lotCache.put(maThuoc, danhSachLo); // Lưu vào cache
            }

            if (danhSachLo == null || danhSachLo.isEmpty()) {
                throw new IllegalStateException("Không đủ hàng cho sản phẩm: " + sp.getTenThuoc());
            }

            // 4. Tìm lô phù hơp
            Thuoc_SP_TheoLo selectedLot = null;
            for (Thuoc_SP_TheoLo lo : danhSachLo) {
                if (lo.getSoLuongTon() >= neededBaseQty) {
                    selectedLot = lo;
                    break;
                }
            }

            // 5. Kiểm tra kết quả
            if (selectedLot == null) {
                throw new IllegalStateException("Không tìm thấy lô nào đủ " + neededBaseQty
                        + " (đv cơ bản) cho " + requestedDisplayQty + " " + dvtGoc.getDvt().getTenDonViTinh()
                        + " " + sp.getTenThuoc());
            }

            // 6. Gán lô đã chọn vào origi
            original.setLoHang(selectedLot);

            selectedLot.setSoLuongTon(selectedLot.getSoLuongTon() - neededBaseQty);
            baseQtyToReduce.put(original, neededBaseQty);
        }
        this.baseQtyMap = baseQtyToReduce;

    }

//    @FXML
//    private void xuLyXuatPDF(ActionEvent event) {
//        // 1. Kiểm tra dữ liệu
//        if (dsChiTietHD == null || dsChiTietHD.isEmpty()) {
//            hien(WARNING, "Không có dữ liệu", "Chưa có chi tiết hóa đơn nào để xuất.");
//            return;
//        }
//
//        // 2. Mở FileChooser
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Lưu Hóa Đơn PDF");
//        fileChooser.setInitialFileName("HoaDon_" + LocalDate.now());
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
//
//        Stage stage = null;
//        if (btnThanhToanVaIn != null && btnThanhToanVaIn.getScene() != null) {
//            stage = (Stage) btnThanhToanVaIn.getScene().getWindow();
//        }
//
//        File file = fileChooser.showSaveDialog(stage);
//
//        // 3. Gọi hàm xuất PDF
//        if (file != null) {
//            try {
//                xuatHoaDonPDF(file);
//                hien(INFORMATION, "Thành công", "Hóa đơn đã được xuất ra file PDF:\n" + file.getAbsolutePath());
//            } catch (IOException e) {
//                e.printStackTrace();
//                hien(ERROR, "Lỗi", "Có lỗi xảy ra khi xuất file PDF:\n" + e.getMessage());
//            }
//        }
//    }


    private void xuatHoaDonPDF(File file) throws IOException {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 1. Thiết lập Font
        PdfFont font;
        try {
            font = PdfFontFactory.createFont(FONT_PATH);
        } catch (IOException e) {
            System.err.println("Không tìm thấy font tại: " + FONT_PATH + ". Sử dụng font mặc định.");
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        }
        document.setFont(font);

        // 2. Header có logo
        try {
            String logoPath = "/com/example/pharmacymanagementsystem_qlht/img/logo.png";
            InputStream is = getClass().getResourceAsStream(logoPath);
            if (is != null) {
                byte[] bytes = is.readAllBytes();
                com.itextpdf.io.image.ImageData imageData = com.itextpdf.io.image.ImageDataFactory.create(bytes);
                com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(imageData);
                logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                logo.scaleToFit(120f, 120f);
                document.add(logo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.add(new Paragraph("Quốc Khánh Pharmacy")
                .setFontSize(16).setBold().setTextAlignment(TextAlignment.CENTER).setMarginTop(5));
        document.add(new Paragraph("Địa chỉ: 12 Đường Nguyễn Văn Bảo, phường 4, Gò Vấp, TP Hồ Chí Minh")
                .setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Hotline: 1800 6868")
                .setFontSize(10).setTextAlignment(TextAlignment.CENTER));

        // 3. Tiêu đề Hóa đơn
        document.add(new Paragraph("HOÁ ĐƠN BÁN LẺ")
                .setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER).setMarginTop(15));

        document.add(new Paragraph("Ngày lập: " + (dpNgayLap.getValue() != null ? dpNgayLap.getValue().toString() : LocalDate.now().toString()))
                .setTextAlignment(TextAlignment.CENTER));

        // 4. Mã đơn thuốc (
        if (rbOTC != null && rbOTC.isSelected()) {
            String maDonThuoc = (txtMaDonThuoc != null) ? txtMaDonThuoc.getText() : "";
            if (maDonThuoc != null && !maDonThuoc.isBlank()) {
                document.add(new Paragraph("Mã đơn thuốc: " + maDonThuoc)
                        .setTextAlignment(TextAlignment.CENTER).setFontSize(10));
            }
        }

        // 5. Thông tin khách hàng và nhân viên (ĐÃ CẬP NHẬT)
        document.add(new Paragraph("THÔNG TIN GIAO DỊCH")
                .setFontSize(14).setBold().setMarginTop(15));

        document.add(new Paragraph("Khách hàng: " + (txtTenKH.getText() != null ? txtTenKH.getText() : "Khách lẻ")));
        document.add(new Paragraph("Số điện thoại: " + (txtSDT.getText() != null ? txtSDT.getText() : "")));
        document.add(new Paragraph("Phương thức thanh toán: " + (cbPhuongThucTT.getValue() != null ? cbPhuongThucTT.getValue() : "Tiền mặt")));

        // ----- BẮT ĐẦU THÊM MỚI -----
        // Lấy nhân viên đang đăng nhập (người lập HĐ/người in)
        String tenNhanVien = "Không rõ";
        if (DangNhap_Ctrl.user != null && DangNhap_Ctrl.user.getTenNV() != null) {
            tenNhanVien = DangNhap_Ctrl.user.getTenNV();
        }
        document.add(new Paragraph("Nhân viên: " + tenNhanVien));
        // ----- KẾT THÚC THÊM MỚI -----

        // 6. Bảng chi tiết sản phẩm
        document.add(new Paragraph("DANH SÁCH SẢN PHẨM")
                .setFontSize(14).setBold().setMarginTop(15));

        float[] columnWidths = {1, 5, 1.5f, 2, 2.5f, 2.5f, 3};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Headers của bảng
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colSTT.getText()).setBold()));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colTenSP.getText()).setBold()));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colSL.getText()).setBold()));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colDonVi.getText()).setBold()));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colDonGia.getText()).setBold()));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colChietKhau.getText()).setBold()));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(colThanhTien.getText()).setBold()));

        // Dữ liệu bảng
        int stt = 1;
        for (ChiTietHoaDon cthd : dsChiTietHD) {
            String tenSP = layTenSP(cthd);
            int soLuong = cthd.getSoLuong();
            String donVi = layTenDonVi(cthd);
            double donGia = cthd.getDonGia();
            double chietKhau = cthd.getGiamGia();
            double thanhTien = Math.max(0, (soLuong * donGia) - chietKhau);

            table.addCell(String.valueOf(stt++));
            table.addCell(tenSP);
            table.addCell(String.valueOf(soLuong)).setTextAlignment(TextAlignment.CENTER);
            table.addCell(donVi);
            table.addCell(formatVND(donGia)).setTextAlignment(TextAlignment.RIGHT);
            table.addCell(formatVND(chietKhau)).setTextAlignment(TextAlignment.RIGHT);
            table.addCell(formatVND(thanhTien)).setTextAlignment(TextAlignment.RIGHT);
        }
        document.add(table);

        // 7. Tổng kết (Đọc từ Label)
        document.add(new Paragraph("Tổng giảm giá: " + lblGiamGia.getText())
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("Tổng tiền: " + lblTongTien.getText())
                .setTextAlignment(TextAlignment.RIGHT).setMarginTop(10));
        document.add(new Paragraph("Giảm giá theo hóa đơn: " + lblGiamTheoHD.getText())
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("Thuế (VAT 5%): " + lblVAT.getText())
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("TỔNG THANH TOÁN: " + lblThanhTien.getText())
                .setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(14));

        // 8. Thông tin tiền mặt (Footer)
        String phuongThucTT = cbPhuongThucTT.getValue();
        if ("Tiền mặt".equals(phuongThucTT) && txtSoTienKhachDua != null && lblTienThua != null) {

            String tienKhachDuaStr = txtSoTienKhachDua.getText();
            String tienThuaStr = lblTienThua.getText();

            try {
                tienKhachDuaStr = formatVND(parseVND(tienKhachDuaStr));
            } catch (Exception e) {
                tienKhachDuaStr = "0 đ";
            }

            if (tienThuaStr.contains("Chưa đủ")) {
                tienThuaStr = "0 đ";
            }

            document.add(new Paragraph("Tiền khách đưa: " + tienKhachDuaStr)
                    .setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(14).setMarginTop(5));
            document.add(new Paragraph("Tiền thừa: " + tienThuaStr)
                    .setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(14));
        }

        // 9. Đóng file
        document.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void lamMoiGiaoDienSauThanhToan() {
        tblChiTietHD.getItems().clear();
        txtTimThuoc.clear();
        lblTongTien.setText("0 VNĐ");
        lblVAT.setText("0 VNĐ");
        lblThanhTien.setText("0 VNĐ");
        lblGiamGia.setText("0 VNĐ");
        lblGiamTheoHD.setText("0 VNĐ");
        txtSoTienKhachDua.clear();
        lblTienThua.setText("0 VNĐ");
        txtTenKH.clear();
        txtSDT.clear();
        rbOTC.setSelected(false);
        txtMaDonThuoc.clear();
    }

    public void xuLyHuy(ActionEvent actionEvent) {
        lamMoiGiaoDienSauThanhToan();
    }
}