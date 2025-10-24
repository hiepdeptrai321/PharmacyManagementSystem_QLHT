
package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TangKem;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SuaKhuyenMai_Ctrl {

    // FXML controls
    @FXML private Button btnHuy;

    @FXML private TableView<ChiTietKhuyenMai> tbDSThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, String>  colMaThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, String>  colTenThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, Integer> colSLAP;
    @FXML private TableColumn<ChiTietKhuyenMai, Integer> colSLTD;
    @FXML private TableColumn<ChiTietKhuyenMai, Void>    colXoaCT;

    @FXML private TabPane tabPaneProducts;
    @FXML private Tab tabTangKem;
    @FXML private Tab tabHoaDon;

    @FXML private TableView<Thuoc_SP_TangKem> tbTangKem;
    @FXML private TableColumn<Thuoc_SP_TangKem, String>  colMaQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, String>  colTenQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, Integer> colSLTang;
    @FXML private TableColumn<Thuoc_SP_TangKem, Void>    colXoaQua;

    @FXML private TextField tfTimThuoc;
    @FXML private ListView<Thuoc_SanPham> listViewThuoc;

    @FXML private TextField tfTimQua;
    @FXML private ListView<Thuoc_SanPham> listViewQua;

    @FXML private TextField tfTenKM;
    @FXML private TextField tfMaKM;
    @FXML private ComboBox<String> cbLoaiKM;
    @FXML private TextField tfGiaTri;
    @FXML private DatePicker dpTuNgay;
    @FXML private DatePicker dpDenNgay;
    @FXML private TextField tfMoTa;

    // New invoice-related fields
    @FXML private TextField tfGiaTriHoaDon;

    // Data sources
    private final ObservableList<ChiTietKhuyenMai>  ctItems   = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SP_TangKem>  giftItems = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SanPham>     allThuoc  = FXCollections.observableArrayList();
    private final Locale VN_LOCALE = new Locale("vi", "VN");
    private final NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(VN_LOCALE);

    // Focus listener management for tfGiaTri (attach only for LKM002 or LKM004)
    private ChangeListener<Boolean> giaTriFocusListener;
    private boolean giaTriFormattingEnabled = false;

    @FXML
    public void initialize() {
        // Load thuốc source once
        loadAllThuoc();

        // Bind thuốc table
        setupThuocTable();

        // Bind quà tặng table
        setupGiftTable();

        // ListView behaviors like SuaXoaThuoc_Ctrl
        initThuocListView();
        initQuaListView();

        // Gift tab visibility + invoice/product toggle follows Loại KM
        if (cbLoaiKM != null) {
            cbLoaiKM.valueProperty().addListener((obs, o, n) -> {
                // Update gift tab (existing behavior)
                updateGiftTabVisibility();
                // Update tabs + formatting based on selected tenLoai (resolve to maLoai)
                updateTabsBasedOnLoaiTen(n);
            });
            // ensure initial state
            updateGiftTabVisibility();
            updateTabsBasedOnLoaiTen(cbLoaiKM == null ? null : cbLoaiKM.getValue());
        }
        cbLoaiKM.setEditable(false);


        setupCurrencyField(tfGiaTriHoaDon);



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
                Integer v = e.getNewValue() == null ? 0 : Math.max(0, e.getNewValue());
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
                Integer v = e.getNewValue() == null ? 0 : Math.max(0, e.getNewValue());
                row.setSlToiDa(v);
                tbDSThuoc.refresh();
            });
        }
        if (colXoaCT != null) {
            colXoaCT.setCellFactory(col -> new TableCell<>() {
                private final Button btn = new Button("Xóa");
                {
                    btn.setOnAction(ev -> {
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
                Integer v = e.getNewValue() == null ? 0 : Math.max(0, e.getNewValue());
                row.setSoLuong(v);
                tbTangKem.refresh();
            });
        }
        if (colXoaQua != null) {
            colXoaQua.setCellFactory(col -> new TableCell<>() {
                private final Button btn = new Button("Xóa");
                {
                    btn.setOnAction(ev -> {
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

    // ListView behaviors similar to SuaXoaThuoc_Ctrl
    private void initThuocListView() {
        if (tfTimThuoc == null || listViewThuoc == null) return;

        listViewThuoc.setVisible(false);
        listViewThuoc.setManaged(false);
        listViewThuoc.setPrefHeight(0);
        listViewThuoc.setItems(allThuoc);

        listViewThuoc.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(Thuoc_SanPham item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaThuoc() + " - " + item.getTenThuoc());
            }
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
                ct.getThuoc() != null &&
                        sp.getMaThuoc().equals(ct.getThuoc().getMaThuoc())
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
                g.getThuocTangKem() != null &&
                        sp.getMaThuoc().equals(g.getThuocTangKem().getMaThuoc())
        );
        if (exists) return;

        Thuoc_SP_TangKem gift = new Thuoc_SP_TangKem();
        gift.setThuocTangKem(sp);
        gift.setSoLuong(1);
        giftItems.add(gift);
    }

    private void updateGiftTabVisibility() {
        if (tabTangKem == null || cbLoaiKM == null) return;
        boolean enable = "Tặng kèm sản phẩm".equalsIgnoreCase(String.valueOf(cbLoaiKM.getValue()));
        tabTangKem.setDisable(!enable);
    }

    // Reusable load methods for CTKM table
    public void loadDatatbCTKM(List<ChiTietKhuyenMai> list) {
        ctItems.setAll(list == null ? List.of() : list);
    }

    public void loadDatatbQuaTang(List<Thuoc_SP_TangKem> list) {
        giftItems.setAll(list == null ? List.of() : list);
    }


    // Populate form from KhuyenMai
    public void loadData(KhuyenMai km) {
        if (km == null) return;
        if (tfMaKM  != null) tfMaKM.setText(km.getMaKM());
        if (tfTenKM != null) tfTenKM.setText(km.getTenKM());
        if (cbLoaiKM != null && km.getLoaiKM() != null) cbLoaiKM.setValue(km.getLoaiKM().getTenLoai());
        if (dpTuNgay != null && km.getNgayBatDau() != null) dpTuNgay.setValue(km.getNgayBatDau().toLocalDate());
        if (dpDenNgay != null && km.getNgayKetThuc() != null) dpDenNgay.setValue(km.getNgayKetThuc().toLocalDate());
        if (tfMoTa != null) tfMoTa.setText(km.getMoTa());
        String loaiKM = cbLoaiKM.getValue();
        if ("Tặng kèm sản phẩm".equalsIgnoreCase(loaiKM)) {
            tfGiaTri.setEditable(false);
        } else {
            tfGiaTri.setEditable(true);
        } if(cbLoaiKM.getValue().equalsIgnoreCase("LKM001")){
            tfGiaTri.setEditable(false);
        }

        // Always format invoice-applied field
        if (tfGiaTriHoaDon != null) tfGiaTriHoaDon.setText(formatVND(km.getGiaTriApDung()));

        // Decide tfGiaTri formatting by maLoai
        if (km.getLoaiKM() != null && km.getLoaiKM().getMaLoai() != null) {
            String maLoai = km.getLoaiKM().getMaLoai();
            updateTabsBasedOnMaLoai(maLoai);
            boolean shouldFormatGiaTri = "LKM002".equalsIgnoreCase(maLoai) || "LKM004".equalsIgnoreCase(maLoai);
            if (tfGiaTri != null) {
                if (shouldFormatGiaTri) tfGiaTri.setText(formatVND(km.getGiaTriKM()));
                else tfGiaTri.setText(String.valueOf(km.getGiaTriKM()));
            }
        } else {
            // fallback: update tabs based on current cbLoaiKM value (resolve name -> code)
            updateTabsBasedOnLoaiTen(cbLoaiKM == null ? null : cbLoaiKM.getValue());
            // If we cannot resolve maLoai, leave tfGiaTri as plain number
            if (tfGiaTri != null) tfGiaTri.setText(String.valueOf(km.getGiaTriKM()));
        }
    }

    // Update tab enable/disable using MaLoai directly (used when clicking Chi tiết)
    private void updateTabsBasedOnMaLoai(String maLoai) {
        if (tabPaneProducts == null) return;
        Tab tabThuoc = tabPaneProducts.getTabs().size() > 0 ? tabPaneProducts.getTabs().get(0) : null;
        boolean isInvoice = "LKM004".equalsIgnoreCase(maLoai) || "LKM005".equalsIgnoreCase(maLoai);
        if (tabThuoc != null) tabThuoc.setDisable(isInvoice);
        if (tabHoaDon != null) tabHoaDon.setDisable(!isInvoice);

        // Enable tfGiaTri formatting only for LKM002 or LKM004
        boolean enableGiaTriFmt = "LKM002".equalsIgnoreCase(maLoai) || "LKM004".equalsIgnoreCase(maLoai);
        enableGiaTriFormatting(enableGiaTriFmt);
    }

    // Resolve tenLoai -> maLoai and then call updateTabsBasedOnMaLoai
    private void updateTabsBasedOnLoaiTen(String tenLoai) {
        if (tenLoai == null) {
            updateTabsBasedOnMaLoai(null);
            return;
        }
        try {
            var loai = new LoaiKhuyenMai_Dao().selectByTen(tenLoai);
            if (loai != null && loai.getMaLoai() != null) {
                updateTabsBasedOnMaLoai(loai.getMaLoai());
                return;
            }
        } catch (Exception ex) {
            // ignore and fallback to name-based heuristic
        }
        // Fallback heuristic: if tenLoai contains 'hóa đơn' enable invoice tab
        boolean isInvoiceByName = tenLoai.toLowerCase().contains("hóa đơn") || tenLoai.toLowerCase().contains("hoa don") || tenLoai.toLowerCase().contains("giảm theo tổng hóa đơn");
        Tab tabThuoc = tabPaneProducts == null ? null : (tabPaneProducts.getTabs().size() > 0 ? tabPaneProducts.getTabs().get(0) : null);
        if (tabThuoc != null) tabThuoc.setDisable(isInvoiceByName);
        if (tabTangKem != null) tabTangKem.setDisable(isInvoiceByName);
        if (tabHoaDon != null) tabHoaDon.setDisable(!isInvoiceByName);

        // Cannot determine maLoai here -> disable giaTri formatting to be safe
        enableGiaTriFormatting(false);
    }

    // Attach or detach focus listener to tfGiaTri to enable/disable VND formatting
    private void enableGiaTriFormatting(boolean enable) {
        if (tfGiaTri == null) return;
        if (enable && !giaTriFormattingEnabled) {
            giaTriFocusListener = (obs, oldF, newF) -> {
                if (newF) {
                    // gained focus -> show plain number for editing
                    String raw = tfGiaTri.getText();
                    if (raw != null) {
                        String cleaned = raw.replaceAll("[^\\d,.-]", "");
                        cleaned = cleaned.replaceAll("\\.", "");
                        cleaned = cleaned.replace(',', '.');
                        tfGiaTri.setText(cleaned);
                        Platform.runLater(() -> tfGiaTri.positionCaret(tfGiaTri.getText().length()));
                    }
                } else {
                    // lost focus -> format to VND
                    double v = parseCurrencyToDouble(tfGiaTri.getText());
                    tfGiaTri.setText(formatVND(v));
                }
            };
            tfGiaTri.focusedProperty().addListener(giaTriFocusListener);
            // format current text
            double v = parseCurrencyToDouble(tfGiaTri.getText());
            tfGiaTri.setText(formatVND(v));
            giaTriFormattingEnabled = true;
        } else if (!enable && giaTriFormattingEnabled) {
            // remove listener and set plain numeric text
            tfGiaTri.focusedProperty().removeListener(giaTriFocusListener);
            giaTriFocusListener = null;
            giaTriFormattingEnabled = false;
            String cleaned = tfGiaTri.getText() == null ? "" : tfGiaTri.getText().replaceAll("[^\\d,.-]", "");
            cleaned = cleaned.replaceAll("\\.", "");
            cleaned = cleaned.replace(',', '.');
            if (cleaned.isEmpty()) cleaned = "0";
            tfGiaTri.setText(cleaned);
        } else if (!enable) {
            // ensure not formatted when already disabled
            String cleaned = tfGiaTri.getText() == null ? "" : tfGiaTri.getText().replaceAll("[^\\d,.-]", "");
            cleaned = cleaned.replaceAll("\\.", "");
            cleaned = cleaned.replace(',', '.');
            if (cleaned.isEmpty()) cleaned = "0";
            tfGiaTri.setText(cleaned);
        }
    }

    // UI events
    public void btnHuyClick() {
        Stage stage = (Stage) (btnHuy != null ? btnHuy.getScene().getWindow()
                : (tfTenKM != null ? tfTenKM.getScene().getWindow() : null));
        if (stage != null) stage.close();
    }

    public void btnLuuClick() {
        try {
            // 1. Lấy dữ liệu chung từ form
            String maKM = tfMaKM.getText();
            String tenKM = tfTenKM.getText();
            String loaiTen = cbLoaiKM == null ? null : cbLoaiKM.getValue();
            String moTa = tfMoTa == null ? "" : tfMoTa.getText();
            java.sql.Date tuNgay = dpTuNgay == null || dpTuNgay.getValue() == null ? null : java.sql.Date.valueOf(dpTuNgay.getValue());
            java.sql.Date denNgay = dpDenNgay == null || dpDenNgay.getValue() == null ? null : java.sql.Date.valueOf(dpDenNgay.getValue());

            // Resolve LoaiKhuyenMai by name
            var loai = loaiTen == null ? null : new LoaiKhuyenMai_Dao().selectByTen(loaiTen);

            // 2. Determine values depending on MaLoai
            float giaTriKMVal = 0f;
            double giaTriApDungVal = 0d;

            if (loai != null && loai.getMaLoai() != null &&
                    ("LKM004".equalsIgnoreCase(loai.getMaLoai()) || "LKM005".equalsIgnoreCase(loai.getMaLoai()))) {
                giaTriKMVal = (float) parseCurrencyToDouble(tfGiaTri == null ? "0" : tfGiaTri.getText());
                giaTriApDungVal = parseCurrencyToDouble(tfGiaTriHoaDon == null ? "0" : tfGiaTriHoaDon.getText());
            } else {
                giaTriKMVal = (float) parseCurrencyToDouble(tfGiaTri == null ? "0" : tfGiaTri.getText());
            }

            // 3. Build KhuyenMai and set invoice-applied value
            KhuyenMai km = new KhuyenMai(maKM, loai, tenKM, giaTriKMVal, tuNgay, denNgay, moTa);
            km.setGiaTriApDung(giaTriApDungVal);

            // DAOs
            KhuyenMai_Dao kmDao = new KhuyenMai_Dao();
            ChiTietKhuyenMai_Dao ctDao = new ChiTietKhuyenMai_Dao();
            Thuoc_SP_TangKem_Dao giftDao = new Thuoc_SP_TangKem_Dao();

            // 4. Insert or update KhuyenMai
            try {
                if (kmDao.selectById(maKM) != null) {
                    kmDao.update(km);
                } else {
                    kmDao.insert(km);
                }
            } catch (Exception ex) {
                // if selectById throws because not found, fallback to insert
                kmDao.insert(km);
            }

            // 5. Update chi tiết (clear old + insert current)
            List<ChiTietKhuyenMai> oldCT = ctDao.selectByMaKM(maKM);
            for (ChiTietKhuyenMai ct : oldCT) {
                ctDao.deleteById(ct.getThuoc().getMaThuoc(), maKM);
            }
            for (ChiTietKhuyenMai ct : ctItems) {
                ct.setKhuyenMai(km);
                ctDao.insert(ct);
            }

            // 6. Update quà tặng (clear old + insert current)
            List<Thuoc_SP_TangKem> oldGifts = giftDao.selectByMaKM(maKM);
            for (Thuoc_SP_TangKem g : oldGifts) {
                giftDao.deleteById(g.getThuocTangKem().getMaThuoc(), maKM);
            }
            for (Thuoc_SP_TangKem g : giftItems) {
                g.setKhuyenmai(km);
                giftDao.insert(g);
            }

            // 7. Close form
            btnHuyClick();
        } catch (Exception ex) {
            ex.printStackTrace();
            // optional: show Alert to user
        }
    }
    private String formatVND(double value) {
        return CURRENCY_FMT.format(value);
    }

    private double parseCurrencyToDouble(String text) {
        if (text == null) return 0d;
        String cleaned = text.replaceAll("[^\\d,.-]", ""); // keep digits, comma, dot, minus
        if (cleaned.isEmpty()) return 0d;
        // remove thousand separators (dots), convert comma to dot for decimals
        cleaned = cleaned.replaceAll("\\.", "");
        cleaned = cleaned.replace(',', '.');
        try {
            return Double.parseDouble(cleaned);
        } catch (Exception ex) {
            return 0d;
        }
    }

    // Setup behavior for a TextField: unformat on focus gain, format on focus lost
    private void setupCurrencyField(TextField tf) {
        if (tf == null) return;
        tf.focusedProperty().addListener((obs, oldF, newF) -> {
            if (newF) {
                // gained focus -> show plain number for editing
                String raw = tf.getText();
                if (raw != null) {
                    String cleaned = raw.replaceAll("[^\\d,.-]", "");
                    // remove thousand dots left in cleaned so user sees digits
                    cleaned = cleaned.replaceAll("\\.", "");
                    cleaned = cleaned.replace(',', '.');
                    tf.setText(cleaned);
                    Platform.runLater(() -> tf.positionCaret(tf.getText().length()));
                }
            } else {
                // lost focus -> format to VND
                double v = parseCurrencyToDouble(tf.getText());
                tf.setText(formatVND(v));
            }
        });
        // initial formatting if value present
        if (tf.getText() != null && !tf.getText().isBlank()) {
            double v = parseCurrencyToDouble(tf.getText());
            tf.setText(formatVND(v));
        }
    }
}
