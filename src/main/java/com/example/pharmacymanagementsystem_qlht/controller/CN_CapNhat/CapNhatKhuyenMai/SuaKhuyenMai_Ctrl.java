// java
package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TangKem;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;

public class SuaKhuyenMai_Ctrl {

    // FXML controls
    @FXML private Button btnHuy;
    @FXML private Button btnLuu;

    @FXML private TableView<ChiTietKhuyenMai> tbDSThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, String>  colMaThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, String>  colTenThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai, Integer> colSLAP;
    @FXML private TableColumn<ChiTietKhuyenMai, Integer> colSLTD;
    @FXML private TableColumn<ChiTietKhuyenMai, Void>    colXoaCT;

    @FXML private TabPane tabPaneProducts;
    @FXML private Tab tabTangKem;
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

    // Data sources
    private final ObservableList<ChiTietKhuyenMai>  ctItems   = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SP_TangKem>  giftItems = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SanPham>     allThuoc  = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load thuốc source once
        loadAllThuoc();

        // Bind thuốc table
        setupThuocTable();

        // Bind quà tặng table
        setupGiftTable();

        // ListView behaviors like SuaXoaThuoc_Ctrl
        initThuocListViewLikeSuaXoa();
        initQuaListViewLikeSuaXoa();

        // Gift tab visibility follows Loại KM (LKM001)
        if (cbLoaiKM != null) {
            cbLoaiKM.valueProperty().addListener((obs, o, n) -> updateGiftTabVisibility());
            updateGiftTabVisibility();
        }
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
    private void initThuocListViewLikeSuaXoa() {
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
                listViewThuoc.setPrefHeight(filtered.isEmpty() ? 0 : 160);
                listViewThuoc.toFront();
            } else {
                listViewThuoc.setVisible(false);
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

    private void initQuaListViewLikeSuaXoa() {
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
                listViewQua.setPrefHeight(filtered.isEmpty() ? 0 : 160);
                listViewQua.toFront();
            } else {
                listViewQua.setVisible(false);
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
        boolean enable = "LKM001".equalsIgnoreCase(String.valueOf(cbLoaiKM.getValue()));
        tabTangKem.setDisable(!enable);
    }

    // Reusable load methods for CTKM table
    public void loadDatatbCTKM(List<ChiTietKhuyenMai> list) {
        ctItems.setAll(list == null ? List.of() : list);
    }

    public void loadTableCTKM(String maKM) {
        List<ChiTietKhuyenMai> ds = new ChiTietKhuyenMai_Dao().selectByMaKM(maKM);
        loadDatatbCTKM(ds);
    }

    // Populate form from KhuyenMai
    public void loadData(KhuyenMai km) {
        if (km == null) return;
        if (tfMaKM  != null) tfMaKM.setText(km.getMaKM());
        if (tfTenKM != null) tfTenKM.setText(km.getTenKM());
        if (cbLoaiKM != null && km.getLoaiKM() != null) cbLoaiKM.setValue(km.getLoaiKM().getMaLoai());
        if (tfGiaTri != null) tfGiaTri.setText(String.valueOf(km.getGiaTriKM()));
        if (dpTuNgay != null && km.getNgayBatDau() != null) dpTuNgay.setValue(km.getNgayBatDau().toLocalDate());
        if (dpDenNgay != null && km.getNgayKetThuc() != null) dpDenNgay.setValue(km.getNgayKetThuc().toLocalDate());
        if (tfMoTa != null) tfMoTa.setText(km.getMoTa());
    }

    // UI events
    public void btnHuyClick() {
        Stage stage = (Stage) (btnHuy != null ? btnHuy.getScene().getWindow()
                : (tfTenKM != null ? tfTenKM.getScene().getWindow() : null));
        if (stage != null) stage.close();
    }

    public void btnLuuClick() {
        // Implement persist logic for KhuyenMai + ctItems + giftItems if/when needed
    }
}
