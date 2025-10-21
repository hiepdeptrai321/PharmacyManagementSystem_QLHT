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
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Date;
import java.util.List;

public class ThemKhuyenMai_Ctrl {

    @FXML private TextField tfTenKM;
    @FXML private ComboBox<LoaiKhuyenMai> cbLoaiKM;
    @FXML private TextField tfGiaTri;
    @FXML private DatePicker dpTuNgay;
    @FXML private DatePicker dpDenNgay;
    @FXML private TextField tfMoTa;

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

    @FXML private Button btnThem;
    @FXML private Button btnHuy;

    private final ObservableList<ChiTietKhuyenMai> ctItems   = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SP_TangKem> giftItems = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SanPham> allThuoc     = FXCollections.observableArrayList();

    private final KhuyenMai_Dao kmDao               = new KhuyenMai_Dao();
    private final ChiTietKhuyenMai_Dao ctDao        = new ChiTietKhuyenMai_Dao();
    private final LoaiKhuyenMai_Dao loaiDao         = new LoaiKhuyenMai_Dao();
    private final Thuoc_SP_TangKem_Dao giftDao      = new Thuoc_SP_TangKem_Dao();

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

        // Gift tab visibility by LoaiKM
        cbLoaiKM.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateGiftTabVisibility());
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
        tfGiaTri.setText("");
        tfGiaTri.setDisable(enable);
    }

    @FXML
    public void btnThemClick() {
        Window owner = btnThem.getScene().getWindow();

        if (cbLoaiKM.getValue() == null || tfTenKM.getText().isBlank()
                || dpTuNgay.getValue() == null || dpDenNgay.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, owner, "Thiếu dữ liệu", "Vui lòng nhập đầy đủ thông tin bắt buộc.");
            return;
        }

        float giaTri;


        if(!tabTangKem.isDisabled() && giftItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, owner, "Thiếu dữ liệu", "Vui lòng thêm quà tặng cho khuyến mãi.");
            return;
        }
        //nếu loại khuyến mãi là tặng kèm thì pha set giá trị = 0
        if (cbLoaiKM.getValue().getMaLoai().equals("LKM001")) {
            giaTri = 0f;
        } else {
            try {
                giaTri = Float.parseFloat(tfGiaTri.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, owner, "Giá trị không hợp lệ", "Giá trị khuyến mãi phải là số.");
                return;
            }
        }

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
                        giaTri,
                        Date.valueOf(dpTuNgay.getValue()),
                        Date.valueOf(dpDenNgay.getValue()),
                        tfMoTa.getText() == null ? "" : tfMoTa.getText().trim()
                );

                // Insert Khuyến mãi
                kmDao.insert(km);

                // Insert Chi tiết áp dụng
                for (ChiTietKhuyenMai ct : ctItems) {
                    ct.setKhuyenMai(km);
                    ctDao.insert(ct);
                }

                // Insert Quà tặng nếu loại KM là LKM001
                if (!tabTangKem.isDisabled()) {
                    for (Thuoc_SP_TangKem g : giftItems) {
                        g.setKhuyenmai(km);
                        giftDao.insert(g);
                    }
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
}
