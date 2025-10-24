package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDatHang;

import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl;
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

import java.net.URL;
import java.time.LocalDate;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LapPhieuDatHang_Ctrl extends Application {
    // Left / search area
    @FXML private Pane searchPane;
    @FXML private TextField tfTimSanPham;

    // Table and its columns (typed)
    @FXML private TableView<ChiTietHoaDon> tbSanPham;
    @FXML private TableColumn<ChiTietHoaDon, String> colSTT;
    @FXML private TableColumn<ChiTietHoaDon, String> colTenSP;
    @FXML private TableColumn<ChiTietHoaDon, String> colSoLuong;
    @FXML private TableColumn<ChiTietHoaDon, String> colDonVi;
    @FXML private TableColumn<ChiTietHoaDon, String> colDonGia;
    @FXML private TableColumn<ChiTietHoaDon, String> colThanhTien;
    @FXML private TableColumn<ChiTietHoaDon, String> colXoa;

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
    @FXML private Button btnDatHangVaIn;

    // Additional fields in info panel
    @FXML private TextField tfSDT;
    @FXML private TextField tfMaDon;
    @FXML private ComboBox<?> cbLoaiDon;
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

    private static final String GoiY_css = "/com/example/pharmacymanagementsystem_qlht/css/GoiYThuoc.css";
    private boolean GoiY_cssat = false;

    // Table data and unit tracking (behaves like LapHoaDon)
    private final ObservableList<ChiTietHoaDon> dsChiTietPD = FXCollections.observableArrayList();
    private final IdentityHashMap<ChiTietHoaDon, ChiTietDonViTinh> dvtTheoDong = new IdentityHashMap<>();

    @FXML
    public void initialize() {
        if (tbSanPham != null) {
            tbSanPham.setPlaceholder(new Label("Chưa có sản phẩm nào"));
            try { tbSanPham.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); } catch (Exception ignored) {}
            tbSanPham.setItems(dsChiTietPD);
            cauHinhCotBang();
        }
        dpNgayLap.setEditable(false);
        if (dpNgayLap != null) dpNgayLap.setValue(LocalDate.now());

        if (lbTongTien != null) lbTongTien.setText("0 VNĐ");
        if (lbThue != null) lbThue.setText("0 VNĐ");
        if (lbTongTT != null) lbTongTT.setText("0 VNĐ");
        if (lbTienThieu != null) lbTienThieu.setText("0 VNĐ");

        if (btnDatHang != null) btnDatHang.setOnAction(e -> onDatHang());
        if (btnDatHangVaIn != null) btnDatHangVaIn.setOnAction(e -> onDatHanngVaIn());

        btnTimKH.setOnAction(e -> xuLyTimKhachHang());
        btnThemKH.setOnAction(this::xuLyThemKH);
        xuLyGoiYSanPham();
        xuLyCssGoiY();
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

        for (ChiTietHoaDon row : dsChiTietPD) {
            Thuoc_SP_TheoLo lo = row.getLoHang();
            Thuoc_SanPham rowSp = (lo != null) ? lo.getThuoc() : null;
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
        ChiTietHoaDon ctpd = new ChiTietHoaDon();
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

    // Attach Thuoc to ChiTietHoaDon via Thuoc_SP_TheoLo model (same pattern as LapHoaDon)
    private void ganThuocVaoCTHD(ChiTietHoaDon cthd, Thuoc_SanPham sp) {
        Thuoc_SP_TheoLo lo = new Thuoc_SP_TheoLo();
        lo.setThuoc(sp);
        cthd.setLoHang(lo);
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
        return ten;
    }
    private String layTenDonVi(ChiTietHoaDon row) {
        if (row == null) return "";
        ChiTietDonViTinh dvtChon = dvtTheoDong.get(row);
        if (dvtChon != null && dvtChon.getDvt() != null && dvtChon.getDvt().getTenDonViTinh() != null) {
            return dvtChon.getDvt().getTenDonViTinh();
        }
        if (row.getLoHang() == null) return "";
        // try to get first available unit name
        Thuoc_SanPham sp = row.getLoHang().getThuoc();
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

                    btnDec.setOnAction(e -> {
                        if (tbSanPham != null) tbSanPham.getScene().getWindow(); // keep access to scene
                        adjust(-1);
                    });
                    btnInc.setOnAction(e -> adjust(1));

                    tf.setOnAction(e -> commitFromText());
                    tf.focusedProperty().addListener((o, w, f) -> {
                        if (!f) commitFromText();
                    });
                }

                private void adjust(int delta) {
                    int rowIndex = getIndex();
                    if (rowIndex < 0 || rowIndex >= getTableView().getItems().size()) return;
                    ChiTietHoaDon item = getTableView().getItems().get(rowIndex);
                    if (item == null) return;
                    int cur = item.getSoLuong();
                    int next = Math.max(1, cur + delta);
                    if (next != cur) {
                        item.setSoLuong(next);
                        tf.setText(String.valueOf(next));
                        if (tbSanPham != null) tbSanPham.refresh();
                    }
                }

                private void commitFromText() {
                    int rowIndex = getIndex();
                    if (rowIndex < 0 || rowIndex >= getTableView().getItems().size()) return;
                    ChiTietHoaDon item = getTableView().getItems().get(rowIndex);
                    if (item == null) return;
                    String s = tf.getText();
                    if (s == null || s.isBlank()) {
                        tf.setText(String.valueOf(item.getSoLuong()));
                        return;
                    }
                    try {
                        int val = Math.max(1, Integer.parseInt(s));
                        item.setSoLuong(val);
                        tf.setText(String.valueOf(val));
                        if (tbSanPham != null) tbSanPham.refresh();
                    } catch (NumberFormatException ignore) {
                        tf.setText(String.valueOf(item.getSoLuong()));
                    }
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        int rowIndex = getIndex();
                        ChiTietHoaDon rowItem = (rowIndex >= 0 && rowIndex < getTableView().getItems().size())
                                ? getTableView().getItems().get(rowIndex) : null;
                        tf.setText(rowItem != null ? String.valueOf(rowItem.getSoLuong()) : "1");
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
                ChiTietHoaDon r = p.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return new javafx.beans.property.ReadOnlyStringWrapper(formatVND(tt));
            });
            canPhai(colThanhTien);
        }

        // keep table refreshed when list changes
        dsChiTietPD.addListener((javafx.collections.ListChangeListener<ChiTietHoaDon>) c -> {
            if (tbSanPham != null) tbSanPham.refresh();
        });
    }


    private void onDatHang() {
        System.out.println("Đặt hàng clicked");
    }

    private void onDatHanngVaIn() {
        System.out.println("Thanh toán clicked");
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
            System.err.println("Customer dialog error: " + ex);
        }
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

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDat/LapPhieuDatHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
