package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDatHang;

import com.example.pharmacymanagementsystem_qlht.TienIch.VNDFormatter;
import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuDatHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.PhieuDatHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LapPhieuDatHang_Ctrl extends Application {
    // Left / search area
    @FXML private Pane searchPane;
    @FXML private TextField tfTimSanPham;

    // Table and its columns (typed)
    @FXML private TableView<ChiTietPhieuDatHang> tbSanPham;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colSTT;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colTenSP;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colSoLuong;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colDonVi;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colDonGia;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colThanhTien;
    @FXML private TableColumn<ChiTietPhieuDatHang, String> colXoa;

    // Info panel (right)
    @FXML private Pane infoPane;
    @FXML private TextField tfMa;
    @FXML private DatePicker dpNgayLap;
    @FXML private TextField tfTenKH;
    @FXML private Label lbTongTien;
    @FXML private Label lbThue;
    @FXML private Label lbTongTT;
    @FXML private TextField tfTienCoc;
    @FXML private Label lbTienThieu;
    @FXML private TextArea tfGhiChu;
    @FXML private Button btnDatHang;
    @FXML private Button btnHuy;

    public KhachHang khachHang;


    // Additional fields in info panel
    @FXML private TextField tfSDT;
    @FXML private TextField tfMaDon;
    @FXML private ComboBox<String> cbLoaiDon;
    @FXML private Button btnTimKH;
    @FXML private Button btnThemKH;

    // Suggestion/context menu support
    private final ContextMenu goiYMenu = new ContextMenu();
    private final PauseTransition pause = new PauseTransition(Duration.millis(250));
    private final Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
    private final AtomicLong demTruyVan = new AtomicLong(0);
    private volatile long idTruyVanMoiNhat = 0;
    private volatile Task<List<String>> goiYHienTai;
    private volatile boolean tamDungGoiY = false;
    private final VNDFormatter vndFmt = new VNDFormatter();


    private static final String GoiY_css = "/com/example/pharmacymanagementsystem_qlht/css/GoiYThuoc.css";
    private boolean GoiY_cssat = false;

    // Table data and unit tracking (behaves like LapHoaDon)
    private final ObservableList<ChiTietPhieuDatHang> dsChiTietPD = FXCollections.observableArrayList();
    private final IdentityHashMap<ChiTietPhieuDatHang, ChiTietDonViTinh> dvtTheoDong = new IdentityHashMap<>();

    @FXML
    public void initialize() {
        if (tbSanPham != null) {
            tbSanPham.setPlaceholder(new Label("Chưa có sản phẩm nào"));
            try { tbSanPham.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
            tbSanPham.setItems(dsChiTietPD);
            cauHinhCotBang();
        }
        if (dpNgayLap != null) dpNgayLap.setValue(LocalDate.now());
        dpNgayLap.setEditable(false);

        if (lbTongTien != null) lbTongTien.setText("0 VNĐ");
        if (lbThue != null) lbThue.setText("0 VNĐ");
        if (lbTongTT != null) lbTongTT.setText("0 VNĐ");
        if (lbTienThieu != null) lbTienThieu.setText("0 VNĐ");

        if (btnDatHang != null) btnDatHang.setOnAction(e -> onDatHang());
        if (btnHuy != null) btnHuy.setOnAction(e -> onHuy());

        btnTimKH.setOnAction(e -> xuLyTimKhachHang());
        btnThemKH.setOnAction(this::xuLyThemKH);

        tfMa.setText(new PhieuDatHang_Dao().generateNewMaPDat());
        tfMa.setEditable(false);

        xuLyGoiYSanPham();
        xuLyCssGoiY();
        loadCbLoaiDon();
        kiemTraLoaiDon();
        initTienCocEvents();
        tbSanPham.getItems().addListener((javafx.collections.ListChangeListener<ChiTietPhieuDatHang>) c -> capNhatTongTienPhieuDat());
    }

    public void loadCbLoaiDon(){
        cbLoaiDon.getItems().clear();
        cbLoaiDon.getItems().addAll("Kê đơn","Không kê đơn");
        cbLoaiDon.getSelectionModel().select(1);
    }

    public void kiemTraLoaiDon(){
        if(cbLoaiDon.getSelectionModel().getSelectedItem().equals("Không kê đơn")){
            tfMaDon.setEditable(false);
        }
        cbLoaiDon.setOnAction(e->{
            String loaiDon = cbLoaiDon.getSelectionModel().getSelectedItem();
            if(loaiDon != null){
                if(loaiDon.equals("Kê đơn")){
                    tfMaDon.setEditable(true);
                } else {
                    tfMaDon.clear();
                    tfMaDon.setEditable(false);
                }
            }
        });
    }

    // ---------- suggestion wiring already present (unchanged) ----------
    private void xuLyGoiYSanPham() {
        if (tfTimSanPham == null) return;

        pause.setOnFinished(evt -> {
            if (!tamDungGoiY) {
                String keyword = tfTimSanPham.getText();
                layDanhSachSanPham(keyword);
            }
        });

        tfTimSanPham.textProperty().addListener((obs, oldText, newText) -> {
            pause.playFromStart();
        });

        tfTimSanPham.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                Platform.runLater(goiYMenu::hide);
            } else {
                String keyword = tfTimSanPham.getText();
                layDanhSachSanPham(keyword);
            }
        });
    }

    private void layDanhSachSanPham(String keyword) {
        if (tfTimSanPham == null) return;

        final String key = (keyword == null) ? "" : keyword.trim();
        if (key.isEmpty()) {
            Platform.runLater(goiYMenu::hide);
            return;
        }

        final long thisQueryId = demTruyVan.incrementAndGet();
        idTruyVanMoiNhat = thisQueryId;

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                if (isCancelled()) return java.util.Collections.emptyList();
                return thuocDao.timTheoTenChiTietAll(key, 5);
            }
        };

        Task<List<String>> prev = goiYHienTai;
        if (prev != null && prev.isRunning()) prev.cancel(true);
        goiYHienTai = task;

        task.setOnSucceeded(evt -> {
            if (thisQueryId != idTruyVanMoiNhat) return;
            String currentText = tfTimSanPham.getText() == null ? "" : tfTimSanPham.getText().trim();
            if (!currentText.equalsIgnoreCase(key)) return;

            List<String> results = task.getValue();
            if (results == null || results.isEmpty()) {
                goiYMenu.hide();
                return;
            }
            taiCSSGoiYThuoc();
            goiYMenu.getItems().clear();
            final double menuWidth = Math.max(300, tfTimSanPham.getWidth());
            int index = 0;
            for (String detail : results) {
                String name;
                String infoText = "";
                int sep = detail.indexOf(" | ");
                if (sep > 0) {
                    name = detail.substring(0, sep);
                    infoText = detail.substring(sep + 3);
                } else {
                    name = detail;
                }

                HBox row = new HBox(8);
                row.setPrefWidth(menuWidth);

                Label nameLbl = new Label(name);
                nameLbl.getStyleClass().add("suggestion-name");
                Label infoLbl = new Label(infoText.isEmpty() ? "" : " | " + infoText);
                infoLbl.getStyleClass().add("suggestion-detail");

                Region spacer = new Region();
                HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                row.getChildren().addAll(nameLbl, infoLbl, spacer);

                CustomMenuItem mi = new CustomMenuItem(row, true);
                if (index < results.size() - 1) mi.getStyleClass().add("has-separator");

                final String chosen = name;
                final String chosenUnit = parseDonVi(infoText);
                mi.setOnAction(ae -> {
                    tfTimSanPham.requestFocus();
                    tfTimSanPham.positionCaret(tfTimSanPham.getText().length());
                    tfTimSanPham.clear();
                    goiYMenu.hide();
                    xuLyChonSanPham(chosen, chosenUnit);
                });

                goiYMenu.getItems().add(mi);
                index++;
            }

            if (!goiYMenu.isShowing() && tfTimSanPham.isFocused()) {
                goiYMenu.show(tfTimSanPham, javafx.geometry.Side.BOTTOM, 0, 0);
            }
        });

        task.setOnFailed(evt -> {
            if (thisQueryId != idTruyVanMoiNhat) return;
            goiYMenu.hide();
            System.err.println("Suggestion load failed: " + task.getException());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    // ---------- selection handling: fetch full object and add to tbSanPham ----------
    private String parseDonVi(String infoText) {
        if (infoText == null) return null;
        Matcher m = Pattern.compile("Số lượng tồn:\\s*\\d+\\s+(.*)$").matcher(infoText.trim());
        if (m.find()) return m.group(1).trim();
        return null;
    }

    private void xuLyChonSanPham(String tenSanPham, String tenDVT) {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) return;
        final String key = tenSanPham.trim();

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
                    if (tfTimSanPham != null) tfTimSanPham.setUserData(sp);
                    themVaoBang(sp, tenDVT);
                }
            } finally {
                tamDungGoiY = false;
            }
        });

        task.setOnFailed(e -> {
            tamDungGoiY = false;
            System.err.println("Fetch product failed: " + task.getException());
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
        if (sp == null) return;

        ChiTietDonViTinh selected = chonDonViTheoTen(sp, tenDVTChon);
        if (selected == null || selected.getDvt() == null) {
            System.err.println("Missing unit for: " + sp.getTenThuoc());
            return;
        }

        double donGia = selected.getGiaBan();

        for (ChiTietPhieuDatHang row : dsChiTietPD) {
            Thuoc_SanPham rowSp = row.getThuoc();
            if (rowSp != null && sp.getMaThuoc() != null && sp.getMaThuoc().equals(rowSp.getMaThuoc())) {
                ChiTietDonViTinh rowDvt = dvtTheoDong.get(row);
                if (rowDvt != null &&
                        rowDvt.getDvt() != null &&
                        selected.getDvt() != null &&
                        rowDvt.getDvt().getMaDVT().equals(selected.getDvt().getMaDVT())) {
                    row.setSoLuong(row.getSoLuong() + 1);
                    if (tbSanPham != null) tbSanPham.refresh();
                    return;
                }
            }
        }
        ChiTietPhieuDatHang ctpd = new ChiTietPhieuDatHang();
        ganThuocVaoCTHD(ctpd, sp);
        ctpd.setSoLuong(1);
        ctpd.setDonGia(donGia);
        ctpd.setGiamGia(0.0);
        dsChiTietPD.add(ctpd);

        // Store the selected unit for this row
        dvtTheoDong.put(ctpd, selected);

        Platform.runLater(() -> {
            if (tfTimSanPham != null) {
                tfTimSanPham.requestFocus();
                tfTimSanPham.positionCaret(tfTimSanPham.getText().length());
            }
        });
    }

    private void ganThuocVaoCTHD(ChiTietPhieuDatHang cthd, Thuoc_SanPham sp) {
        // assign product directly (no LoHang)
        cthd.setThuoc(sp);
    }

    private String layTenSP(ChiTietPhieuDatHang row) {
        if (row == null) return "";
        var sp = row.getThuoc();
        String ten = (sp != null && sp.getTenThuoc() != null) ? sp.getTenThuoc() : "";
        return ten;
    }

    private String layTenDonVi(ChiTietPhieuDatHang row) {
        if (row == null) return "";
        ChiTietDonViTinh dvtChon = dvtTheoDong.get(row);
        if (dvtChon != null && dvtChon.getDvt() != null && dvtChon.getDvt().getTenDonViTinh() != null) {
            return dvtChon.getDvt().getTenDonViTinh();
        }
        Thuoc_SanPham sp = row.getThuoc();
        if (sp == null || sp.getDsCTDVT() == null || sp.getDsCTDVT().isEmpty()) return "";
        ChiTietDonViTinh first = sp.getDsCTDVT().get(0);
        return (first.getDvt() != null) ? String.valueOf(first.getDvt().getTenDonViTinh()) : "";
    }

    private String formatVND(double v) {
        return String.format("%,.0f đ", v);
    }

    // ---------- CSS loader (unchanged) ----------
    private void xuLyCssGoiY() {
        if (!GoiY_cssat) {
            taiCSSGoiYThuoc();
            GoiY_cssat = true;
        }
    }

    private void taiCSSGoiYThuoc() {
        // ensure the menu has the base style class
        if (!goiYMenu.getStyleClass().contains("suggestion-menu")) {
            goiYMenu.getStyleClass().add("suggestion-menu");
        }

        // handler: when the popup's scene is available, add stylesheet to it
        goiYMenu.setOnShowing(e -> {
            var url = getClass().getResource(GoiY_css);
            if (url == null) return;

            var scene = goiYMenu.getScene();
            if (scene != null) {
                String css = url.toExternalForm();
                if (!scene.getStylesheets().contains(css)) {
                    scene.getStylesheets().add(css);
                }
                return;
            }

            // fallback: attach stylesheet to the owner control's scene so styles apply to children
            if (tfTimSanPham != null && tfTimSanPham.getScene() != null) {
                String css = url.toExternalForm();
                var ownerScene = tfTimSanPham.getScene();
                if (!ownerScene.getStylesheets().contains(css)) {
                    ownerScene.getStylesheets().add(css);
                }
            }
        });
    }
    // java
    private void cauHinhCotBang() {
        if (tbSanPham == null) return;

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
                    new javafx.beans.property.ReadOnlyStringWrapper(layTenSP(p.getValue()))
            );
        }

        if (colSoLuong != null) {
            colSoLuong.setCellValueFactory(p ->
                    new javafx.beans.property.ReadOnlyStringWrapper(String.valueOf(p.getValue().getSoLuong()))
            );

            colSoLuong.setCellFactory(tc -> new TableCell<>() {
                private final Button btnDec = new Button("-");
                private final Button btnInc = new Button("+");
                private final TextField tf = new TextField();
                private final HBox box = new HBox(6, btnDec, tf, btnInc);

                {
                    tf.setPrefWidth(56);
                    tf.setMaxWidth(56);
                    tf.setStyle("-fx-alignment: center-right;");

                    // allow only digits up to reasonable length
                    javafx.scene.control.TextFormatter<Integer> formatter = new javafx.scene.control.TextFormatter<>(change -> {
                        String newText = change.getControlNewText();
                        return newText.matches("\\d{0,6}") ? change : null;
                    });
                    tf.setTextFormatter(formatter);

                    btnDec.setOnAction(e -> adjust(-1));
                    btnInc.setOnAction(e -> adjust(1));

                    tf.setOnAction(e -> commitFromText());
                    tf.focusedProperty().addListener((o, w, f) -> { if (!f) commitFromText(); });
                }

                private void adjust(int delta) {
                    int idx = getIndex();
                    if (idx < 0 || idx >= getTableView().getItems().size()) return;
                    ChiTietPhieuDatHang row = getTableView().getItems().get(idx);
                    if (row == null) return;

                    int cur = row.getSoLuong();
                    int target = Math.max(1, cur + delta);

                    if (target != cur) {
                        row.setSoLuong(target);
                        // refresh view and update totals
                        if (tbSanPham != null) tbSanPham.refresh();
                        capNhatTongTienPhieuDat();
                    }
                    tf.setText(String.valueOf(row.getSoLuong()));
                }

                private void commitFromText() {
                    int idx = getIndex();
                    if (idx < 0 || idx >= getTableView().getItems().size()) return;
                    ChiTietPhieuDatHang row = getTableView().getItems().get(idx);
                    if (row == null) return;

                    String s = tf.getText();
                    if (s == null || s.isBlank()) { tf.setText(String.valueOf(row.getSoLuong())); return; }

                    try {
                        int entered = Integer.parseInt(s);
                        if (entered <= 0) entered = 1;
                        if (entered != row.getSoLuong()) {
                            row.setSoLuong(entered);
                            if (tbSanPham != null) tbSanPham.refresh();
                            capNhatTongTienPhieuDat();
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
                        ChiTietPhieuDatHang r = getTableView().getItems().get(getIndex());
                        tf.setText(r != null ? String.valueOf(r.getSoLuong()) : "1");
                        setGraphic(box);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                }
            });
        }

        if (colDonVi != null) {
            colDonVi.setCellValueFactory(p ->
                    new javafx.beans.property.ReadOnlyStringWrapper(layTenDonVi(p.getValue()))
            );
        }

        if (colDonGia != null) {
            colDonGia.setCellValueFactory(p ->
                    new javafx.beans.property.ReadOnlyStringWrapper(formatVND(p.getValue().getDonGia()))
            );
            canPhai(colDonGia);
        }

        if (colThanhTien != null) {
            colThanhTien.setCellValueFactory(p -> {
                ChiTietPhieuDatHang r = p.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return new javafx.beans.property.ReadOnlyStringWrapper(formatVND(tt));
            });
            canPhai(colThanhTien);
        }

        // keep table refreshed when list changes
        dsChiTietPD.addListener((javafx.collections.ListChangeListener<ChiTietPhieuDatHang>) c -> {
            if (tbSanPham != null) tbSanPham.refresh();
        });
    }

    private void xuLyTimKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml"));
            Parent root = loader.load();
            Object ctrl = loader.getController();

            Stage dlg = new Stage();
            dlg.initModality(Modality.APPLICATION_MODAL);
            if (btnTimKH != null && btnTimKH.getScene() != null) {
                dlg.initOwner(btnTimKH.getScene().getWindow());
            }

            // If controller is the typed search controller, set the callback like LapHoaDon_Ctrl
            if (ctrl instanceof TimKiemKhachHang_Ctrl tkCtrl) {
                tkCtrl.setOnSelected((KhachHang kh) -> {
                    if (tfTenKH != null) tfTenKH.setText(kh.getTenKH());
                    if (tfSDT != null) tfSDT.setText(kh.getSdt());
                    khachHang = kh;
                    dlg.close();
                });
                dlg.setScene(new Scene(root));
                dlg.showAndWait();
                return;
            }

            // fallback: show dialog modally and try to read selected afterwards
            dlg.setScene(new Scene(root));
            dlg.showAndWait();
            Object selected = extractSelectedFromController(ctrl);
            populateCustomerFields(selected);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void canPhai(TableColumn<ChiTietPhieuDatHang, String> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
            }
        });
    }

    @FXML
    private void xuLyThemKH(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/ThemKhachHang_GUI.fxml"));
            Parent root = loader.load();
            Object ctrl = loader.getController();

            // try to switch controller to add-mode if supported
            String[] addMethods = {"setAddMode", "showAddMode", "enableAddMode", "openAddDialog", "themMoi"};
            for (String mName : addMethods) {
                try {
                    var m = ctrl.getClass().getMethod(mName);
                    m.invoke(ctrl);
                    break;
                } catch (NoSuchMethodException ignored) {
                } catch (Exception ex) {
                    System.err.println("Invoke add-mode failed: " + ex);
                    break;
                }
            }

            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            if (btnThemKH != null && btnThemKH.getScene() != null) {
                st.initOwner(btnThemKH.getScene().getWindow());
            }
            st.setScene(new Scene(root));

            // extract result after dialog closes (like LapHoaDon_Ctrl)
            st.setOnHidden(e -> {
                try {
                    Object selected = extractSelectedFromController(ctrl);
                    populateCustomerFields(selected);
                } catch (Exception ex) {
                    System.err.println("Extracting new customer failed: " + ex);
                }
            });

            st.show();
        } catch (Exception ex) {
            System.err.println("Open add-customer dialog error: " + ex);
        }
    }
    private Object extractSelectedFromController(Object ctrl) {
        if (ctrl == null) return null;
        String[] candidateGetters = {"getKhachHang", "getSelectedKhachHang", "getSelected", "getKQ", "getResult"};
        for (String name : candidateGetters) {
            try {
                var m = ctrl.getClass().getMethod(name);
                return m.invoke(ctrl);
            } catch (NoSuchMethodException ignored) {
            } catch (Exception ex) {
                System.err.println("Failed to invoke " + name + ": " + ex);
            }
        }
        return null;
    }

    private void populateCustomerFields(Object kh) {
        if (kh == null) return;
        try {
            String ten = null;
            String sdt = null;

            // try common name getters
            String[] nameGetters = {"getHoTen", "getTenKhachHang", "getTen", "getName"};
            for (String g : nameGetters) {
                try {
                    var m = kh.getClass().getMethod(g);
                    Object val = m.invoke(kh);
                    if (val != null) { ten = String.valueOf(val); break; }
                } catch (NoSuchMethodException ignored) {}
            }

            // try common phone getters
            String[] phoneGetters = {"getSdt", "getSoDienThoai", "getPhone"};
            for (String g : phoneGetters) {
                try {
                    var m = kh.getClass().getMethod(g);
                    Object val = m.invoke(kh);
                    if (val != null) { sdt = String.valueOf(val); break; }
                } catch (NoSuchMethodException ignored) {}
            }

            final String nameFinal = ten;
            final String phoneFinal = sdt;
            Platform.runLater(() -> {
                if (tfTenKH != null && nameFinal != null) tfTenKH.setText(nameFinal);
                if (tfSDT != null && phoneFinal != null) tfSDT.setText(phoneFinal);
            });
        } catch (Exception ex) {
            System.err.println("Populate customer fields error: " + ex);
        }
    }
    private void showValidationAlert(String title, String message, Control focus) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.initOwner((btnDatHang != null && btnDatHang.getScene() != null) ? btnDatHang.getScene().getWindow() : null);
        a.showAndWait();
        if (focus != null) {
            Platform.runLater(() -> focus.requestFocus());
        }
    }

    private Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        try {
            String cleaned = s.replaceAll("[^0-9\\-,\\.]", "").replace(',', '.');
            if (cleaned.isBlank()) return null;
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean validateInputs(boolean requirePrintAfter) {
        // require at least one product
        if (dsChiTietPD.isEmpty()) {
            showValidationAlert("Validation", "Vui lòng thêm ít nhất một sản phẩm.", tfTimSanPham);
            return false;
        }

        // validate quantities and prices per row
        for (int i = 0; i < dsChiTietPD.size(); i++) {
            ChiTietPhieuDatHang row = dsChiTietPD.get(i);
            if (row.getSoLuong() <= 0) {
                showValidationAlert("Validation", "Số lượng phải lớn hơn 0 tại dòng " + (i+1), tbSanPham);
                return false;
            }
            if (row.getDonGia() < 0) {
                showValidationAlert("Validation", "Đơn giá không hợp lệ tại dòng " + (i+1), tbSanPham);
                return false;
            }
        }


        // when "Kê đơn" selected, tfMaDon is required
        String loai = cbLoaiDon != null ? cbLoaiDon.getSelectionModel().getSelectedItem() : null;
        if ("Kê đơn".equalsIgnoreCase(loai)) {
            if (tfMaDon == null || tfMaDon.getText().isBlank()) {
                showValidationAlert("Validation", "Đã chọn 'Kê đơn' — vui lòng nhập mã đơn.", tfMaDon);
                return false;
            }
        }

        // deposit numeric and non-negative
        if (tfTienCoc != null && !tfTienCoc.getText().isBlank()) {
            Double tienCoc = parseDoubleOrNull(tfTienCoc.getText());
            if (tienCoc == null || tienCoc < 0) {
                showValidationAlert("Validation", "Tiền cọc không hợp lệ. Nhập số >= 0.", tfTienCoc);
                return false;
            }
            // optional: ensure deposit does not exceed total (if lbTongTT holds parseable number)
            Double tong = parseDoubleOrNull(lbTongTT != null ? lbTongTT.getText() : null);
            if (tong != null && tienCoc > tong) {
                showValidationAlert("Validation", "Tiền cọc không được lớn hơn tổng cộng.", tfTienCoc);
                return false;
            }
        }

        return true;
    }
    private BigDecimal parseDecimalOrZero(String text) {
        if (text == null || text.trim().isEmpty()) return BigDecimal.ZERO;

        try {
            // Loại bỏ ký tự không phải số
            String cleaned = text.replaceAll("[^\\d]", ""); // chỉ giữ lại chữ số
            if (cleaned.isEmpty()) return BigDecimal.ZERO;

            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private String formatVND(BigDecimal amount) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        df.setGroupingUsed(true);
        long v = (amount == null) ? 0L : amount.setScale(0, RoundingMode.HALF_UP).longValue();
        return df.format(Math.max(0L, v)) + " đ";
    }

    private void updateTienThieuPhieuDat(BigDecimal tongThanhToan) {
        if (lbTienThieu == null) return;
        BigDecimal tienCoc = parseDecimalOrZero(tfTienCoc != null ? tfTienCoc.getText() : null);
        if (tienCoc.compareTo(tongThanhToan) < 0) {
            BigDecimal conThieu = tongThanhToan.subtract(tienCoc).max(BigDecimal.ZERO);
            lbTienThieu.setText(formatVND(conThieu));
            lbTienThieu.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else {
            lbTienThieu.setText(formatVND(BigDecimal.ZERO));
            lbTienThieu.setStyle("");
        }
    }

    private void capNhatTongTienPhieuDat() {
        if (dsChiTietPD == null) return;

        BigDecimal tongHang = BigDecimal.ZERO;
        BigDecimal tongGiam = BigDecimal.ZERO;

        for (ChiTietPhieuDatHang r : dsChiTietPD) {
            if (r == null) continue;
            BigDecimal soLuong = BigDecimal.valueOf(Math.max(0, r.getSoLuong()));
            BigDecimal donGia = BigDecimal.valueOf(Math.max(0, r.getDonGia()));
            BigDecimal giam = BigDecimal.valueOf(Math.max(0, r.getGiamGia()));

            BigDecimal lineTotal = soLuong.multiply(donGia);
            BigDecimal lineAfterDiscount = lineTotal.subtract(giam).max(BigDecimal.ZERO);

            tongHang = tongHang.add(lineAfterDiscount);
            tongGiam = tongGiam.add(giam);
        }

        BigDecimal vat = tongHang.multiply(new BigDecimal("0.05")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongThanhToan = tongHang.add(vat).max(BigDecimal.ZERO);

        if (lbTongTien != null) lbTongTien.setText(formatVND(tongHang));
        if (lbThue != null)     lbThue.setText(formatVND(vat));
        if (lbTongTT != null)   lbTongTT.setText(formatVND(tongThanhToan));

        updateTienThieuPhieuDat(tongThanhToan);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDat/LapPhieuDatHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void initTienCocEvents() {
        if (tfTienCoc == null || lbTienThieu == null || lbTongTT == null) return;
        vndFmt.applyNumberFormatter(tfTienCoc);
        tfTienCoc.textProperty().addListener((obs, oldVal, newVal) -> updateTienThieuFromCoc());
        tfTienCoc.focusedProperty().addListener((obs, was, now) -> {
            if (!now) Platform.runLater(this::updateTienThieuFromCoc);
        });
        Platform.runLater(this::updateTienThieuFromCoc);
    }

    private void updateTienThieuFromCoc() {
        if (lbTongTT == null || lbTienThieu == null || tfTienCoc == null) return;

        BigDecimal tong = parseDecimalOrZero(lbTongTT.getText());
        BigDecimal coc  = parseDecimalOrZero(tfTienCoc.getText());

        BigDecimal thieu = tong.subtract(coc).max(BigDecimal.ZERO);

        if (coc.compareTo(tong) < 0) {
            lbTienThieu.setText(formatVND(thieu));
            lbTienThieu.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else {
            lbTienThieu.setText(formatVND(BigDecimal.ZERO));
            lbTienThieu.setStyle("");
        }
    }
    private void onDatHang() {
        if (!validateInputs(false)) return;

        try {
            // Build header
            PhieuDatHang pd = new PhieuDatHang();
            pd.setMaPDat(tfMa != null ? tfMa.getText() : null);
            pd.setNgayLap(new java.sql.Timestamp(System.currentTimeMillis()));
            pd.setSoTienCoc(parseDecimalOrZero(tfTienCoc != null ? tfTienCoc.getText() : null).doubleValue());
            pd.setGhiChu(tfGhiChu != null ? tfGhiChu.getText() : null);
            pd.setNhanVien(DangNhap_Ctrl.user);

            // Use existing khachHang field (must be set elsewhere)
            if (khachHang == null) {
                showValidationAlert("Validation", "Vui lòng chọn hoặc thêm khách hàng trước khi đặt hàng.", tfTenKH);
                return;
            }
            pd.setKhachHang(khachHang);

            // Persist header
            PhieuDatHang_Dao pdDao = new PhieuDatHang_Dao();
            boolean savedHeader = pdDao.insert(pd);
            if (!savedHeader) {
                showValidationAlert("Error", "Lưu phiếu đặt hàng thất bại.", null);
                return;
            }

            // Persist details
            ChiTietPhieuDatHang_Dao ctDao = new ChiTietPhieuDatHang_Dao();
            for (ChiTietPhieuDatHang row : dsChiTietPD) {
                if (row == null) continue;
                Thuoc_SanPham thuoc = row.getThuoc();
                if (thuoc == null || thuoc.getMaThuoc() == null) continue; // skip invalid rows
                ChiTietPhieuDatHang ct = new ChiTietPhieuDatHang();
                ct.setPhieuDatHang(pd);
                ct.setThuoc(thuoc);
                ct.setSoLuong(row.getSoLuong());
                ct.setDonGia(row.getDonGia());
                ct.setGiamGia(row.getGiamGia());
                ctDao.insert(ct);
            }

            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Success");
                a.setHeaderText(null);
                a.setContentText("Đặt hàng đã được lưu thành công.");
                a.initOwner((btnDatHang != null && btnDatHang.getScene() != null) ? btnDatHang.getScene().getWindow() : null);

                var result = a.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDatHang/ChiTietPhieuDatHang_GUI.fxml"));
                        Parent root = loader.load();
                        Object ctrl = loader.getController();

                        if (ctrl instanceof com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang.ChiTietPhieuDatHang_Ctrl) {
                            var detailCtrl = (com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang.ChiTietPhieuDatHang_Ctrl) ctrl;
                            // pass the saved PhieuDatHang (pd) to the detail controller
                            detailCtrl.setPhieuDatHang(pd);
                        }

                        Stage st = new Stage();
                        st.initModality(Modality.APPLICATION_MODAL);
                        st.initOwner((btnDatHang != null && btnDatHang.getScene() != null) ? btnDatHang.getScene().getWindow() : null);
                        st.setScene(new Scene(root));
                        st.showAndWait();

                        // after the detail window is closed, clear/reset the form
                        onHuy();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // still reset UI if error occurs (optional)
                        onHuy();
                    }
                }
            });

            // Reset UI
            if (tfMa != null) tfMa.setText(new PhieuDatHang_Dao().generateNewMaPDat());


        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
            showValidationAlert("Error", "Lỗi khi lưu phiếu đặt: " + msg, null);
        }
    }
    public void onHuy(){
        // Dialog hỏi xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận hủy");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn hủy phiếu đặt hàng không?");
        confirmAlert.initOwner((btnHuy != null && btnHuy.getScene() != null) ? btnHuy.getScene().getWindow() : null);
        var result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        if (tfMa != null) tfMa.setText(new PhieuDatHang_Dao().generateNewMaPDat());
        if (tfTenKH != null) tfTenKH.clear();
        if (tfSDT != null) tfSDT.clear();
        khachHang = null;
        if (tfGhiChu != null) tfGhiChu.clear();
        if (tfTienCoc != null) tfTienCoc.clear();
        dsChiTietPD.clear();
        capNhatTongTienPhieuDat();
    }
}
