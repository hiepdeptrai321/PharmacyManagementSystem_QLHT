// java
package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.LoaiKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SP_TangKem_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThemKhuyenMai_Ctrl {

    @FXML private TextField tfTenKM;
    @FXML private ComboBox<LoaiKhuyenMai> cbLoaiKM;
    @FXML private TextField tfGiaTri;
    @FXML private DatePicker dpTuNgay;
    @FXML private DatePicker dpDenNgay;
    @FXML private TextField tfMoTa;
    @FXML private Tab tabHoaDon;
    @FXML private TextField tfGiaTriHoaDon;

    @FXML private TableView<ChiTietKhuyenMai> tbDSThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, String>  colMaThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, String>  colTenThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, Integer> colSLAP;
    @FXML private TableColumn<ChiTietKhuyenMai, Integer> colSLTD;
    @FXML private TableColumn<ChiTietKhuyenMai, Void>    colXoaCT;

    @FXML private TabPane tabPaneProducts;
    @FXML private Tab tabTangKem;
    @FXML private Tab tabThuoc;
    @FXML private TableView<Thuoc_SP_TangKem> tbTangKem;
    @FXML private TableColumn<Thuoc_SP_TangKem, String>  colMaQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, String>  colTenQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, Integer> colSLTang;
    @FXML private TableColumn<Thuoc_SP_TangKem, Void>    colXoaQua;

    @FXML private TextField tfTimThuoc;
    @FXML private ListView<Thuoc_SanPham> listViewThuoc;

    @FXML private TextField tfTimQua;
    @FXML private ListView<Thuoc_SanPham> listViewQua;

    @FXML private Button btnThem;
    @FXML private Button btnHuy;

    private final ObservableList<ChiTietKhuyenMai> ctItems   = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SP_TangKem> giftItems = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SanPham> allThuoc     = FXCollections.observableArrayList();

    private final KhuyenMai_Dao kmDao               = new KhuyenMai_Dao();
    private final ChiTietKhuyenMai_Dao ctDao        = new ChiTietKhuyenMai_Dao();
    private final LoaiKhuyenMai_Dao loaiDao         = new LoaiKhuyenMai_Dao();
    private final Thuoc_SP_TangKem_Dao giftDao      = new Thuoc_SP_TangKem_Dao();

    private javafx.beans.value.ChangeListener<Boolean> giaTriFocusListener;
    private boolean giaTriFormattingEnabled = false;
    private final Locale VN_LOCALE = new Locale("vi", "VN");
    private final NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(VN_LOCALE);

    @FXML
    public void initialize() {
        // Load Loại khuyến mãi
        List<LoaiKhuyenMai> loaiKMList = loaiDao.selectAll();
        cbLoaiKM.getItems().addAll(loaiKMList);
        cbLoaiKM.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(LoaiKhuyenMai item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTenLoai());
            }
        });
        cbLoaiKM.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(LoaiKhuyenMai item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTenLoai());
            }
        });

        // Source data
        loadAllThuoc();

        // Tables like in SuaKhuyenMai_Ctrl
        setupThuocTable();
        setupGiftTable();

        // ListView behaviors
        initThuocListView();
        initQuaListView();

        // Gift tab visibility and giaTri formatting by LoaiKM - attach single listener
        cbLoaiKM.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateGiftTabVisibility();
            updateTabsBasedOnLoaiKM();
            tfGiaTri.clear();
        });

        // ensure initial state
        updateGiftTabVisibility();
        updateTabsBasedOnLoaiKM();


        if (tfGiaTriHoaDon != null) setupCurrencyField(tfGiaTriHoaDon);
        setupEnterKeyHandlers();

    }

    private void loadAllThuoc() {
        try {
            List<Thuoc_SanPham> ds = new Thuoc_SanPham_Dao().selectAll();
            if (ds != null) allThuoc.setAll(ds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setupThuocTable() {
        if (tbDSThuoc == null) return;
        tbDSThuoc.setItems(ctItems);
        tbDSThuoc.setEditable(true);

        if (colMaThuoc != null) {
            colMaThuoc.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue() != null && cd.getValue().getThuoc() != null
                            ? cd.getValue().getThuoc().getMaThuoc() : "")
            );
        }
        if (colTenThuoc != null) {
            colTenThuoc.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue() != null && cd.getValue().getThuoc() != null
                            ? cd.getValue().getThuoc().getTenThuoc() : "")
            );
        }
        if (colSLAP != null) {
            colSLAP.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue() != null ? cd.getValue().getSlApDung() : 0).asObject()
            );
            colSLAP.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            colSLAP.setOnEditCommit(e -> {
                ChiTietKhuyenMai row = e.getRowValue();
                int v = e.getNewValue() == null ? 0 : Math.max(0, e.getNewValue());
                row.setSlApDung(v);
                tbDSThuoc.refresh();
            });
        }
        if (colSLTD != null) {
            colSLTD.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue() != null ? cd.getValue().getSlToiDa() : 0).asObject()
            );
            colSLTD.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            colSLTD.setOnEditCommit(e -> {
                ChiTietKhuyenMai row = e.getRowValue();
                int v = e.getNewValue() == null ? 0 : Math.max(0, e.getNewValue());
                row.setSlToiDa(v);
                tbDSThuoc.refresh();
            });
        }
        if (colXoaCT != null) {
            colXoaCT.setCellFactory(col -> new TableCell<>() {
                private final Button btn = new Button("Xóa");
                { btn.setOnAction(ev -> {
                    ChiTietKhuyenMai item = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(item);
                });
                }
                @Override protected void updateItem(Void v, boolean empty) {
                    super.updateItem(v, empty);
                    setGraphic(empty ? null : btn);
                }
            });
        }
    }

    private void setupGiftTable() {
        if (tbTangKem == null) return;
        tbTangKem.setItems(giftItems);
        tbTangKem.setEditable(true);

        if (colMaQua != null) {
            colMaQua.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue() != null && cd.getValue().getThuocTangKem() != null
                            ? cd.getValue().getThuocTangKem().getMaThuoc() : "")
            );
        }
        if (colTenQua != null) {
            colTenQua.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue() != null && cd.getValue().getThuocTangKem() != null
                            ? cd.getValue().getThuocTangKem().getTenThuoc() : "")
            );
        }
        if (colSLTang != null) {
            colSLTang.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue() != null ? cd.getValue().getSoLuong() : 0).asObject()
            );
            colSLTang.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            colSLTang.setOnEditCommit(e -> {
                Thuoc_SP_TangKem row = e.getRowValue();
                int v = e.getNewValue() == null ? 0 : Math.max(0, e.getNewValue());
                row.setSoLuong(v);
                tbTangKem.refresh();
            });
        }
        if (colXoaQua != null) {
            colXoaQua.setCellFactory(col -> new TableCell<>() {
                private final Button btn = new Button("Xóa");
                { btn.setOnAction(ev -> {
                    Thuoc_SP_TangKem item = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(item);
                });
                }
                @Override protected void updateItem(Void v, boolean empty) {
                    super.updateItem(v, empty);
                    setGraphic(empty ? null : btn);
                }
            });
        }
    }

    private void initThuocListView() {
        if (tfTimThuoc == null || listViewThuoc == null) return;

        listViewThuoc.setVisible(false);
        listViewThuoc.setManaged(false);
        listViewThuoc.setPrefHeight(0);
        listViewThuoc.setItems(allThuoc);

        listViewThuoc.setCellFactory(lv -> {
            ListCell<Thuoc_SanPham> cell = new ListCell<>() {
                @Override
                protected void updateItem(Thuoc_SanPham item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getMaThuoc() + " - " + item.getTenThuoc());
                }
            };
            cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && !cell.isEmpty()) {
                    cell.setStyle("-fx-background-color: #d3d3d3;"); // darken on hover
                } else {
                    cell.setStyle("");
                }
            });
            return cell;
        });

        tfTimThuoc.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                String keyword = newVal.toLowerCase();
                ObservableList<Thuoc_SanPham> filtered = FXCollections.observableArrayList();
                for (Thuoc_SanPham sp : allThuoc) {
                    String ma = sp.getMaThuoc() == null ? "" : sp.getMaThuoc().toLowerCase();
                    String ten = sp.getTenThuoc() == null ? "" : sp.getTenThuoc().toLowerCase();
                    if (ma.contains(keyword) || ten.contains(keyword)) filtered.add(sp);
                }
                listViewThuoc.setItems(filtered);
                listViewThuoc.setVisible(!filtered.isEmpty());
                listViewThuoc.setManaged(!filtered.isEmpty());
                listViewThuoc.setPrefHeight(filtered.isEmpty() ? 0 : 160);
                listViewThuoc.toFront();
            } else {
                listViewThuoc.setVisible(false);
                listViewThuoc.setManaged(false);
                listViewThuoc.setPrefHeight(0);
                listViewThuoc.setItems(allThuoc);
            }
        });

        listViewThuoc.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                addThuocToCTKM(newSel);
                tfTimThuoc.clear();
                listViewThuoc.setVisible(false);
                listViewThuoc.setPrefHeight(0);
                Platform.runLater(() -> {
                    listViewThuoc.getSelectionModel().clearSelection();
                    listViewThuoc.refresh();
                });
            }
        });
    }

    private void initQuaListView() {
        if (tfTimQua == null || listViewQua == null) return;

        listViewQua.setVisible(false);
        listViewQua.setManaged(false);
        listViewQua.setPrefHeight(0);
        listViewQua.setItems(allThuoc);

        listViewQua.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(Thuoc_SanPham item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaThuoc() + " - " + item.getTenThuoc());
            }
        });

        tfTimQua.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                String keyword = newVal.toLowerCase();
                ObservableList<Thuoc_SanPham> filtered = FXCollections.observableArrayList();
                for (Thuoc_SanPham sp : allThuoc) {
                    String ma = sp.getMaThuoc() == null ? "" : sp.getMaThuoc().toLowerCase();
                    String ten = sp.getTenThuoc() == null ? "" : sp.getTenThuoc().toLowerCase();
                    if (ma.contains(keyword) || ten.contains(keyword)) filtered.add(sp);
                }
                listViewQua.setItems(filtered);
                listViewQua.setVisible(!filtered.isEmpty());
                listViewQua.setManaged(!filtered.isEmpty());
                listViewQua.setPrefHeight(filtered.isEmpty() ? 0 : 160);
                listViewQua.toFront();
            } else {
                listViewQua.setVisible(false);
                listViewQua.setManaged(false);
                listViewQua.setPrefHeight(0);
                listViewQua.setItems(allThuoc);
            }
        });

        listViewQua.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                addGiftItem(newSel);
                tfTimQua.clear();
                listViewQua.setVisible(false);
                listViewQua.setPrefHeight(0);
                Platform.runLater(() -> {
                    listViewQua.getSelectionModel().clearSelection();
                    listViewQua.refresh();
                });
            }
        });
    }

    private void addThuocToCTKM(Thuoc_SanPham sp) {
        if (sp == null) return;
        boolean exists = ctItems.stream().anyMatch(ct ->
                ct.getThuoc() != null && sp.getMaThuoc().equals(ct.getThuoc().getMaThuoc())
        );
        if (exists) return;

        ChiTietKhuyenMai ct = new ChiTietKhuyenMai();
        ct.setThuoc(sp);
        ct.setSlApDung(1);
        ct.setSlToiDa(1);
        ctItems.add(ct);
    }

    private void addGiftItem(Thuoc_SanPham sp) {
        if (sp == null) return;
        boolean exists = giftItems.stream().anyMatch(g ->
                g.getThuocTangKem() != null && sp.getMaThuoc().equals(g.getThuocTangKem().getMaThuoc())
        );
        if (exists) return;

        Thuoc_SP_TangKem gift = new Thuoc_SP_TangKem();
        gift.setThuocTangKem(sp);
        gift.setSoLuong(1);
        giftItems.add(gift);
    }

    private void updateGiftTabVisibility() {
        if (tabTangKem == null || cbLoaiKM == null) return;
        LoaiKhuyenMai selected = cbLoaiKM.getValue();
        boolean enable = selected != null && "LKM001".equalsIgnoreCase(selected.getMaLoai());
        tabTangKem.setDisable(!enable);
        // If type is LKM001, disable giaTri input. Otherwise enable it.
        if (tfGiaTri != null) {
            tfGiaTri.setDisable(enable);
            if (enable) {
                tfGiaTri.setText("");
            }
        }
    }

    @FXML
    private void updateTabsBasedOnLoaiKM() {
        if (cbLoaiKM == null) return;
        LoaiKhuyenMai selected = cbLoaiKM.getValue();
        String maLoai = selected == null ? null : selected.getMaLoai();

        boolean isInvoiceType = maLoai != null && ("LKM004".equalsIgnoreCase(maLoai) || "LKM005".equalsIgnoreCase(maLoai));

        // Prefer tab from tabPaneProducts if available, otherwise fall back to injected field
        Tab localTabThuoc = null;
        if (tabPaneProducts != null && !tabPaneProducts.getTabs().isEmpty()) {
            localTabThuoc = tabPaneProducts.getTabs().get(0);
        }
        if (localTabThuoc == null) localTabThuoc = this.tabThuoc;

        // Safely apply disables only when tabs are non-null
        if (tabHoaDon != null) tabHoaDon.setDisable(!isInvoiceType);
        if (localTabThuoc != null) localTabThuoc.setDisable(isInvoiceType);

        if (isInvoiceType && tfGiaTriHoaDon != null) {
            // clear to allow user input (will be formatted on focus lost)
            tfGiaTriHoaDon.setText("");
        }

        // Enable giaTri formatting only for LKM002 or LKM004
        boolean enableGiaTriFmt = maLoai != null && ("LKM002".equalsIgnoreCase(maLoai) || "LKM004".equalsIgnoreCase(maLoai));
        enableGiaTriFormatting(enableGiaTriFmt);

        // Ensure tfGiaTri enabled/disabled state consistent (LKM001 disables)
        if (tfGiaTri != null) {
            if ("LKM001".equalsIgnoreCase(maLoai)) {
                tfGiaTri.setDisable(true);
                tfGiaTri.setText("");
            } else {
                tfGiaTri.setDisable(false);
                if (enableGiaTriFmt) {
                    double v = parseCurrencyToDouble(tfGiaTri.getText());
                    tfGiaTri.setText(formatVND(v));
                }
            }
        }
    }

    @FXML
    public void btnThemClick() {
        Window owner = btnThem.getScene().getWindow();

        if (cbLoaiKM.getValue() == null || tfTenKM.getText().isBlank()
                || dpTuNgay.getValue() == null || dpDenNgay.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, owner, "Thiếu dữ liệu", "Vui lòng nhập đầy đủ thông tin bắt buộc.");
            return;
        }

        if (!tabTangKem.isDisabled() && giftItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, owner, "Thiếu dữ liệu", "Vui lòng thêm quà tặng cho khuyến mãi.");
            return;
        }

        float giaTri = 0f;
        double giaTriHoaDon = 0d;

        try {
            String maLoai = cbLoaiKM.getValue().getMaLoai();
            if ("LKM004".equalsIgnoreCase(maLoai) || "LKM005".equalsIgnoreCase(maLoai)) {
                // invoice-related promotions need both values -> parse using currency parser
                giaTri = (float) parseCurrencyToDouble(tfGiaTri == null ? "0" : tfGiaTri.getText());
                giaTriHoaDon = parseCurrencyToDouble(tfGiaTriHoaDon == null ? "0" : tfGiaTriHoaDon.getText());
            } else if ("LKM001".equalsIgnoreCase(maLoai)) {
                // LKM001 does not require giaTri
                giaTri = 0f;
            } else {
                // other types parse giaTri (may be plain number or formatted)
                giaTri = (float) parseCurrencyToDouble(tfGiaTri == null ? "0" : tfGiaTri.getText());
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, owner, "Giá trị không hợp lệ", "Giá trị phải là số.");
            return;
        }

        final float finalGiaTri = giaTri;
        final double finalGiaTriHoaDon = giaTriHoaDon;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận thêm khuyến mãi?", ButtonType.YES, ButtonType.NO);
        confirm.initOwner(owner);
        confirm.setHeaderText("Xác nhận");
        confirm.showAndWait().ifPresent(res -> {
            if (res != ButtonType.YES) return;

            try {
                String maKM = kmDao.generateNewMaKM();
                KhuyenMai km = new KhuyenMai(
                        maKM,
                        cbLoaiKM.getValue(),
                        tfTenKM.getText().trim(),
                        finalGiaTri,
                        java.sql.Date.valueOf(dpTuNgay.getValue()),
                        java.sql.Date.valueOf(dpDenNgay.getValue()),
                        tfMoTa.getText() == null ? "" : tfMoTa.getText().trim()
                );
                km.setGiaTriApDung(finalGiaTriHoaDon);

                // Insert Khuyến mãi
                kmDao.insert(km);

                // Insert Chi tiết áp dụng
                for (ChiTietKhuyenMai ct : ctItems) {
                    ct.setKhuyenMai(km);
                    ctDao.insert(ct);
                }

                showAlert(Alert.AlertType.INFORMATION, owner, "Thành công", "Thêm khuyến mãi thành công.");
                btnHuyClick();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, owner, "Lỗi", ex.getMessage());
            }
        });
    }

    @FXML
    public void btnHuyClick() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, Window owner, String header, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.initOwner(owner);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private String formatVND(double value) {
        return CURRENCY_FMT.format(value);
    }

    private double parseCurrencyToDouble(String text) {
        if (text == null) return 0d;
        String cleaned = text.replaceAll("[^\\d,.-]", ""); // keep digits, comma, dot, minus
        if (cleaned.isEmpty()) return 0d;
        cleaned = cleaned.replaceAll("\\.", "");
        cleaned = cleaned.replace(',', '.');
        try {
            return Double.parseDouble(cleaned);
        } catch (Exception ex) {
            return 0d;
        }
    }

    private void setupCurrencyField(javafx.scene.control.TextField tf) {
        if (tf == null) return;
        tf.focusedProperty().addListener((obs, oldF, newF) -> {
            if (newF) {
                String raw = tf.getText();
                if (raw != null) {
                    String cleaned = raw.replaceAll("[^\\d,.-]", "");
                    cleaned = cleaned.replaceAll("\\.", "");
                    cleaned = cleaned.replace(',', '.');
                    tf.setText(cleaned);
                    javafx.application.Platform.runLater(() -> tf.positionCaret(tf.getText().length()));
                }
            } else {
                double v = parseCurrencyToDouble(tf.getText());
                tf.setText(formatVND(v));
            }
        });
        if (tf.getText() != null && !tf.getText().isBlank()) {
            double v = parseCurrencyToDouble(tf.getText());
            tf.setText(formatVND(v));
        }
    }

    // Enable or disable VND formatting behavior for tfGiaTri (attach/detach listener)
    private void enableGiaTriFormatting(boolean enable) {
        if (tfGiaTri == null) return;
        if (enable && !giaTriFormattingEnabled) {
            giaTriFocusListener = (obs, oldF, newF) -> {
                if (newF) {
                    String raw = tfGiaTri.getText();
                    if (raw != null) {
                        String cleaned = raw.replaceAll("[^\\d,.-]", "");
                        cleaned = cleaned.replaceAll("\\.", "");
                        cleaned = cleaned.replace(',', '.');
                        tfGiaTri.setText(cleaned);
                        javafx.application.Platform.runLater(() -> tfGiaTri.positionCaret(tfGiaTri.getText().length()));
                    }
                } else {
                    double v = parseCurrencyToDouble(tfGiaTri.getText());
                    tfGiaTri.setText(formatVND(v));
                }
            };
            tfGiaTri.focusedProperty().addListener(giaTriFocusListener);
            double v = parseCurrencyToDouble(tfGiaTri.getText());
            tfGiaTri.setText(formatVND(v));
            giaTriFormattingEnabled = true;
        } else if (!enable && giaTriFormattingEnabled) {
            tfGiaTri.focusedProperty().removeListener(giaTriFocusListener);
            giaTriFocusListener = null;
            giaTriFormattingEnabled = false;
            String cleaned = tfGiaTri.getText() == null ? "" : tfGiaTri.getText().replaceAll("[^\\d,.-]", "");
            cleaned = cleaned.replaceAll("\\.", "");
            cleaned = cleaned.replace(',', '.');
            if (cleaned.isEmpty()) cleaned = "0";
            tfGiaTri.setText(cleaned);
        } else if (!enable) {
            String cleaned = tfGiaTri.getText() == null ? "" : tfGiaTri.getText().replaceAll("[^\\d,.-]", "");
            cleaned = cleaned.replaceAll("\\.", "");
            cleaned = cleaned.replace(',', '.');
            if (cleaned.isEmpty()) cleaned = "0";
            tfGiaTri.setText(cleaned);
        }
    }
    private void setupEnterKeyHandlers() {
        // basic text fields -> move focus logically
        addEnterHandler(tfTenKM, () -> { if (cbLoaiKM != null) cbLoaiKM.requestFocus(); });
        addEnterHandlerForComboBox(cbLoaiKM, () -> { if (tfGiaTri != null) tfGiaTri.requestFocus(); });

        addEnterHandler(tfGiaTri, () -> { if (dpTuNgay != null) dpTuNgay.requestFocus(); });
        addEnterHandlerDatePicker(dpTuNgay, () -> { if (dpDenNgay != null) dpDenNgay.requestFocus(); });
        addEnterHandlerDatePicker(dpDenNgay, () -> { if (tfMoTa != null) tfMoTa.requestFocus(); });

        addEnterHandler(tfMoTa, () -> {
            if (tabHoaDon != null && !tabHoaDon.isDisable() && tfGiaTriHoaDon != null) tfGiaTriHoaDon.requestFocus();
            else if (tfTimThuoc != null) tfTimThuoc.requestFocus();
        });

        addEnterHandler(tfGiaTriHoaDon, () -> {
            if (tfTimThuoc != null) tfTimThuoc.requestFocus();
            else if (btnThem != null) btnThem.fire();
        });

        // search fields -> if list visible, pick first item; otherwise move focus
        addEnterHandler(tfTimThuoc, () -> {
            if (listViewThuoc != null && listViewThuoc.isVisible() && !listViewThuoc.getItems().isEmpty()) {
                listViewThuoc.getSelectionModel().select(0);
                addThuocToCTKM(listViewThuoc.getSelectionModel().getSelectedItem());
                tfTimThuoc.clear();
                listViewThuoc.setVisible(false);
                listViewThuoc.setPrefHeight(0);
            } else if (tfTimQua != null) tfTimQua.requestFocus();
        });

        addEnterHandler(tfTimQua, () -> {
            if (listViewQua != null && listViewQua.isVisible() && !listViewQua.getItems().isEmpty()) {
                listViewQua.getSelectionModel().select(0);
                addGiftItem(listViewQua.getSelectionModel().getSelectedItem());
                tfTimQua.clear();
                listViewQua.setVisible(false);
                listViewQua.setPrefHeight(0);
            } else if (btnThem != null) btnThem.requestFocus();
        });

        // make add button default so pressing Enter triggers it when focused
        if (btnThem != null) btnThem.setDefaultButton(true);
    }

    private void addEnterHandler(TextField tf, Runnable action) {
        if (tf == null) return;
        tf.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                ev.consume();
                action.run();
            }
        });
    }

    private <T> void addEnterHandlerForComboBox(ComboBox<T> cb, Runnable action) {
        if (cb == null) return;
        cb.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                ev.consume();
                action.run();
            }
        });
    }

    private void addEnterHandlerDatePicker(DatePicker dp, Runnable action) {
        if (dp == null) return;
        dp.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                ev.consume();
                action.run();
            }
        });
    }
}
