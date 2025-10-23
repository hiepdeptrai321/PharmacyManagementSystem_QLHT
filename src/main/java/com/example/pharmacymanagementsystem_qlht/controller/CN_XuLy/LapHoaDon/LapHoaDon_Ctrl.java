package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapHoaDon;

import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
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
import java.time.LocalDate;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LapHoaDon_Ctrl extends Application {

    private final ObservableList<ChiTietHoaDon> dsChiTietHD = FXCollections.observableArrayList();
    private final IdentityHashMap<ChiTietHoaDon, ChiTietDonViTinh> dvtTheoDong = new java.util.IdentityHashMap<>();
    private final AtomicLong demTruyVan = new java.util.concurrent.atomic.AtomicLong(0);
    private volatile long idTruyVanMoiNhat = 0;
    private volatile Task<List<String>> goiYHienTai;
    @FXML
    private Button btnThemKH;
    @FXML
    private DatePicker dpNgayKeDon;
    @FXML
    private ChoiceBox<String> cbPhuongThucTT;
    @FXML
    private Pane paneTienMat;
    @FXML
    private TextField txtTimThuoc;
    @FXML
    private TextField txtTenKH;
    @FXML
    private TextField txtSDT;
    @FXML
    private TableView<ChiTietHoaDon> tblChiTietHD;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colSTT;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colTenSP;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colSL;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colDonVi;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colDonGia;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colChietKhau;
    @FXML
    private TableColumn <ChiTietHoaDon, String> colThanhTien;

    // popup suggestions
    private final ContextMenu goiYMenu = new ContextMenu();
    private final PauseTransition pause = new PauseTransition(Duration.millis(100));
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
        dpNgayKeDon.setValue(LocalDate.now());
        xuLyPhuongThucTT();
        xuLyTimThuoc();
        xuLyCssGoiY();
        xuLyChiTietHD();

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

        javafx.concurrent.Task<java.util.List<String>> newTask = new javafx.concurrent.Task<>() {
            @Override
            protected java.util.List<String> call() {
                if (isCancelled()) return java.util.Collections.emptyList();
                return thuocDao.timTheoTenChiTiet(key, 10);
            }
        };

        javafx.concurrent.Task<java.util.List<String>> prev = goiYHienTai;
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

                javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(8);
                row.getStyleClass().add("suggestion-row");
                row.setPrefWidth(menuWidth);
                row.setFillHeight(true);

                javafx.scene.control.Label nameLbl = new javafx.scene.control.Label(medicineName);
                nameLbl.getStyleClass().add("suggestion-name");
                nameLbl.setWrapText(true);

                javafx.scene.control.Label infoLbl = new javafx.scene.control.Label(infoText.isEmpty() ? "" : " | " + infoText);
                infoLbl.getStyleClass().add("suggestion-detail");
                infoLbl.setWrapText(true);

                javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                row.getChildren().addAll(nameLbl, infoLbl, spacer);

                javafx.scene.control.CustomMenuItem mi = new javafx.scene.control.CustomMenuItem(row, true);
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
                goiYMenu.show(txtTimThuoc, javafx.geometry.Side.BOTTOM, 0, 0);
            }
        });

        newTask.setOnCancelled(evt -> {
            // Do nothing on cancel
        });

        newTask.setOnFailed(evt -> {
            if (thisQueryId != idTruyVanMoiNhat) return; // ignore stale failures
            goiYMenu.hide();
            System.err.println("Không thể tải gợi ý: " + newTask.getException());
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Không thể tải gợi ý thuốc.\nVui lòng thử lại.",
                    javafx.scene.control.ButtonType.OK
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
        if (sp == null) return;

        ChiTietDonViTinh selected = chonDonViTheoTen(sp, tenDVTChon);
        if (selected == null || selected.getDvt() == null) {
            System.err.println("Missing unit for: " + sp.getTenThuoc());
            return;
        }

        double donGia = selected.getGiaBan();

        for (ChiTietHoaDon row : dsChiTietHD) {
            Thuoc_SP_TheoLo lo = row.getLoHang();
            Thuoc_SanPham rowSp = (lo != null) ? lo.getThuoc() : null;
            if (rowSp != null && sp.getMaThuoc() != null && sp.getMaThuoc().equals(rowSp.getMaThuoc())) {
                ChiTietDonViTinh rowDvt = dvtTheoDong.get(row);
                if (rowDvt != null &&
                        rowDvt.getDvt() != null &&
                        selected.getDvt() != null &&
                        rowDvt.getDvt().getMaDVT().equals(selected.getDvt().getMaDVT())) {
                    row.setSoLuong(row.getSoLuong() + 1);
                    if (tblChiTietHD != null) tblChiTietHD.refresh();
                    return;
                }
            }
        }
        ChiTietHoaDon cthd = new ChiTietHoaDon();
        ganThuocVaoCTHD(cthd, sp);
        cthd.setSoLuong(1);
        cthd.setDonGia(donGia);
        cthd.setGiamGia(0.0);
        dsChiTietHD.add(cthd);

        // Store the selected unit for this row
        dvtTheoDong.put(cthd, selected);

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
            colSL.setCellValueFactory(p ->
                    new ReadOnlyStringWrapper(String.valueOf(p.getValue().getSoLuong()))
            );

            colSL.setCellFactory(tc -> new TableCell<>() {
                private final Button btnTru = new Button("+");
                private final Button btnCong = new Button("-");
                private final TextField tf = new TextField();
                private final HBox box = new HBox(6, btnCong, tf, btnTru);

                {
                    tf.setPrefWidth(56);
                    tf.setMaxWidth(56);
                    tf.setStyle("-fx-alignment: center-right;");

                    TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
                        String newText = change.getControlNewText();
                        return newText.matches("\\d{0,6}") ? change : null;
                    });
                    tf.setTextFormatter(formatter);

                    btnCong.setOnAction(e -> {
                        goiYMenu.hide();
                        pause.stop();
                        adjust(-1);
                    });
                    btnTru.setOnAction(e -> {
                        goiYMenu.hide();
                        pause.stop();
                        adjust(1);
                    });
                    tf.setOnAction(e -> {
                        goiYMenu.hide();
                        pause.stop();
                        commitFromText();
                    });
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
                        if (tblChiTietHD != null) tblChiTietHD.refresh();
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
                        if (tblChiTietHD != null) tblChiTietHD.refresh();
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
                    new ReadOnlyStringWrapper(layTenDonVi(p.getValue()))
            );
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

    public void xoaRong(ActionEvent actionEvent) {
        txtTimThuoc.setText("");
    }
}