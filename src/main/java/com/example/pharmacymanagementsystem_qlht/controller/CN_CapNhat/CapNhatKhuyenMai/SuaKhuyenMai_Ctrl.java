// java
package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TangKem;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.text.Normalizer;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SuaKhuyenMai_Ctrl extends Application {

    // FXML controls
    @FXML private Button btnHuy;
    @FXML private Button btnLuu;

    @FXML private TableView<ChiTietKhuyenMai> tbDSThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai,String> colMaThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai,String> colTenThuoc;
    @FXML private TableColumn<ChiTietKhuyenMai,Integer> colSLAP;
    @FXML private TableColumn<ChiTietKhuyenMai,Integer> colSLTD;
    @FXML private TableColumn<ChiTietKhuyenMai,Void> colXoaCT;

    @FXML private TabPane tabPaneProducts;
    @FXML private Tab tabTangKem;
    @FXML private TableView<Thuoc_SP_TangKem> tbTangKem;
    @FXML private TableColumn<Thuoc_SP_TangKem, String> colMaQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, String> colTenQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, Integer> colSLTang;
    @FXML private TableColumn<Thuoc_SP_TangKem, Void> colXoaQua;

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

    @FXML private AnchorPane paneThuocArea;

    // Data sources
    private final ObservableList<ChiTietKhuyenMai> ctItems = FXCollections.observableArrayList();
    private final ObservableList<Thuoc_SP_TangKem> giftItems = FXCollections.observableArrayList();

    private final ObservableList<Thuoc_SanPham> allThuoc = FXCollections.observableArrayList();
    private FilteredList<Thuoc_SanPham> filteredThuoc;

    private final ObservableList<Thuoc_SanPham> allThuocForGifts = FXCollections.observableArrayList();
    private FilteredList<Thuoc_SanPham> filteredThuocForGifts;

    @FXML
    public void initialize() {
        // Load suggestion sources
        try {
            List<Thuoc_SanPham> ds = new Thuoc_SanPham_Dao().selectAll();
            if (ds != null) allThuoc.setAll(ds);
            allThuocForGifts.setAll(allThuoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Bind thuốc table
        if (tbDSThuoc != null) {
            tbDSThuoc.setItems(ctItems);
            tbDSThuoc.setEditable(true);
        }
        if (colMaThuoc != null) {
            colMaThuoc.setCellValueFactory(cd -> {
                ChiTietKhuyenMai ct = cd.getValue();
                String ma = (ct != null && ct.getThuoc() != null && ct.getThuoc().getMaThuoc() != null)
                        ? String.valueOf(ct.getThuoc().getMaThuoc()) : "";
                return new SimpleStringProperty(ma);
            });
        }
        if (colTenThuoc != null) {
            colTenThuoc.setCellValueFactory(cd -> {
                ChiTietKhuyenMai ct = cd.getValue();
                String ten = (ct != null && ct.getThuoc() != null && ct.getThuoc().getTenThuoc() != null)
                        ? String.valueOf(ct.getThuoc().getTenThuoc()) : "";
                return new SimpleStringProperty(ten);
            });
        }
        if (colSLAP != null) {
            colSLAP.setEditable(true);
            colSLAP.setCellValueFactory(new PropertyValueFactory<>("slApDung"));
        }
        if (colSLTD != null) {
            colSLTD.setEditable(true);
            colSLTD.setCellValueFactory(new PropertyValueFactory<>("slToiDa"));
        }
        if (colSLAP != null && colSLTD != null) {
            installSpinnerColumn(colSLAP, 0, 1_000_000, 1, ChiTietKhuyenMai::getSlApDung, ChiTietKhuyenMai::setSlApDung);
            installSpinnerColumn(colSLTD, 0, 1_000_000, 1, ChiTietKhuyenMai::getSlToiDa, ChiTietKhuyenMai::setSlToiDa);
        }
        if (colXoaCT != null) {
            colXoaCT.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("X");
                {
                    btn.setOnAction(e -> {
                        int idx = getIndex();
                        if (idx >= 0 && idx < ctItems.size()) ctItems.remove(idx);
                    });
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
                @Override protected void updateItem(Void v, boolean empty) {
                    super.updateItem(v, empty);
                    setGraphic(empty ? null : btn);
                }
            });
        }

        // Bind gift table (model uses nested Thuoc + quantity)
        if (tbTangKem != null) tbTangKem.setItems(giftItems);
        if (colMaQua != null) {
            colMaQua.setCellValueFactory(cd -> {
                Thuoc_SP_TangKem g = cd.getValue();
                String ma = (g != null && g.getThuocTangKem() != null && g.getThuocTangKem().getMaThuoc() != null)
                        ? String.valueOf(g.getThuocTangKem().getMaThuoc()) : "";
                return new SimpleStringProperty(ma);
            });
        }
        if (colTenQua != null) {
            colTenQua.setCellValueFactory(cd -> {
                Thuoc_SP_TangKem g = cd.getValue();
                String ten = (g != null && g.getThuocTangKem() != null && g.getThuocTangKem().getTenThuoc() != null)
                        ? g.getThuocTangKem().getTenThuoc() : "";
                return new SimpleStringProperty(ten);
            });
        }
        if (colSLTang != null) {
            colSLTang.setCellValueFactory(cd -> {
                Thuoc_SP_TangKem g = cd.getValue();
                int sl = (g == null) ? 0 : g.getSoLuong();
                return new SimpleIntegerProperty(sl).asObject();
            });
            colSLTang.setEditable(true);
            colSLTang.setCellFactory(tc -> new TableCell<>() {
                private final Spinner<Integer> spinner = new Spinner<>();
                {
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    spinner.setEditable(true);
                    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0, 1));
                    spinner.valueProperty().addListener((o, ov, nv) -> {
                        int row = getIndex();
                        if (row >= 0 && row < giftItems.size() && nv != null) {
                            giftItems.get(row).setSoLuong(nv);
                        }
                    });
                }
                @Override protected void updateItem(Integer value, boolean empty) {
                    super.updateItem(value, empty);
                    if (empty || getIndex() < 0 || getIndex() >= giftItems.size()) { setGraphic(null); return; }
                    SpinnerValueFactory.IntegerSpinnerValueFactory vf =
                            (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
                    vf.setValue(value == null ? 0 : value);
                    setGraphic(spinner);
                }
            });
        }
        if (colXoaQua != null) {
            colXoaQua.setCellFactory(tc -> new TableCell<>() {
                private final Button btn = new Button("X");
                {
                    btn.setOnAction(e -> {
                        int idx = getIndex();
                        if (idx >= 0 && idx < giftItems.size()) giftItems.remove(idx);
                    });
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
                @Override protected void updateItem(Void v, boolean empty) {
                    super.updateItem(v, empty);
                    setGraphic(empty ? null : btn);
                }
            });
        }


        // Suggestions for thuốc (overlay above table)
        if (tfTimThuoc != null && listViewThuoc != null) {
            setupSuggestionList(tfTimThuoc, listViewThuoc, true);
            bindOverlayPositionForThuoc();
        }
        // Optional: gifts suggestion if you add listViewQua in FXML
        if (tfTimQua != null && listViewQua != null) {
            setupSuggestionList(tfTimQua, listViewQua, false);
        }

        // Gift tab visibility follows Loại KM
        if (cbLoaiKM != null) {
            cbLoaiKM.valueProperty().addListener((obs, o, n) -> updateGiftTabVisibility());
            updateGiftTabVisibility();
        }

        // Reflect selected row -> search field
        if (tbDSThuoc != null && tfTimThuoc != null) {
            tbDSThuoc.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && newItem.getThuoc() != null) {
                    tfTimThuoc.setText(Objects.toString(newItem.getThuoc().getTenThuoc(), ""));
                }
            });
        }
    }
    private static String normalizeText(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return n.toLowerCase().trim();
    }


    // \[Extracted reusable load methods\]
    public void loadDatatbCTKM(List<ChiTietKhuyenMai> list) {
        ctItems.setAll(list == null ? List.of() : list);
    }
    public void loadTableCTKM(String maKM) {
        List<ChiTietKhuyenMai> ds = new ChiTietKhuyenMai_Dao().selectByMaKM(maKM);
        loadDatatbCTKM(ds);
    }

    private void setupSuggestionList(TextField tf, ListView<Thuoc_SanPham> lv, boolean isThuocSearch) {
        lv.setVisible(false);
        lv.setManaged(false);
        lv.setPrefHeight(0);

        lv.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Thuoc_SanPham item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaThuoc() + " - " + item.getTenThuoc());
            }
        });

        if (isThuocSearch) {
            filteredThuoc = new FilteredList<>(allThuoc, p -> false); // start hidden
            lv.setItems(filteredThuoc);
        } else {
            filteredThuocForGifts = new FilteredList<>(allThuocForGifts, p -> false);
            lv.setItems(filteredThuocForGifts);
        }

        // filter logic driven by text
        Runnable applyFilter = () -> {
            String qn = normalizeText(tf.getText());
            if (isThuocSearch) {
                filteredThuoc.setPredicate(t -> {
                    if (qn.isEmpty()) return false;
                    String ma = normalizeText(t.getMaThuoc());
                    String ten = normalizeText(t.getTenThuoc());
                    return ma.contains(qn) || ten.contains(qn);
                });
                toggleSuggestion(tf, lv, !filteredThuoc.isEmpty() && !qn.isEmpty());
            } else {
                filteredThuocForGifts.setPredicate(t -> {
                    if (qn.isEmpty()) return false;
                    String ma = normalizeText(t.getMaThuoc());
                    String ten = normalizeText(t.getTenThuoc());
                    return ma.contains(qn) || ten.contains(qn);
                });
                toggleSuggestion(tf, lv, !filteredThuocForGifts.isEmpty() && !qn.isEmpty());
            }
        };

        tf.textProperty().addListener((obs, ov, nv) -> applyFilter.run());

        // focus behavior: only show when there is text and matches; hide on blur
        tf.focusedProperty().addListener((obs, was, is) -> {
            if (is) applyFilter.run();
            else hideSuggestion(lv);
        });
        lv.focusedProperty().addListener((obs, was, is) -> {
            if (!is && !tf.isFocused()) hideSuggestion(lv);
        });

        // keyboard navigation
        tf.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN -> {
                    if (lv.isVisible()) {
                        lv.requestFocus();
                        lv.getSelectionModel().selectFirst();
                    }
                }
                case ENTER -> {
                    // if list is visible and an item is highlighted, accept it
                    if (lv.isVisible() && lv.getSelectionModel().getSelectedItem() != null) {
                        Thuoc_SanPham sel = lv.getSelectionModel().getSelectedItem();
                        tf.setText(sel.getTenThuoc());
                        if (isThuocSearch) addThuocToCTKM(sel);
                        else addGiftItem(sel);
                        hideSuggestion(lv);
                    } else {
                        applyFilter.run();
                    }
                }
                case ESCAPE -> hideSuggestion(lv);
            }
        });

        lv.setOnMouseClicked(e -> {
            Thuoc_SanPham sel = lv.getSelectionModel().getSelectedItem();
            if (sel != null) {
                tf.setText(sel.getTenThuoc());
                if (isThuocSearch) addThuocToCTKM(sel);
                else addGiftItem(sel);
                hideSuggestion(lv);
                lv.getSelectionModel().clearSelection();
            }
        });
        lv.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> {
                    Thuoc_SanPham sel = lv.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        tf.setText(sel.getTenThuoc());
                        if (isThuocSearch) addThuocToCTKM(sel);
                        else addGiftItem(sel);
                        hideSuggestion(lv);
                    }
                }
                case ESCAPE -> hideSuggestion(lv);
            }
        });
    }

    private void addThuocToCTKM(Thuoc_SanPham sp) {
        if (sp == null) return;
        boolean exists = ctItems.stream().anyMatch(ct ->
                ct.getThuoc() != null &&
                        String.valueOf(ct.getThuoc().getMaThuoc()).equals(String.valueOf(sp.getMaThuoc()))
        );
        if (exists) return;

        ChiTietKhuyenMai ct = new ChiTietKhuyenMai();
        try { ct.setThuoc(sp); } catch (Exception ignored) {}
        try { ct.setSlApDung(1); } catch (Exception ignored) {}
        try { ct.setSlToiDa(1); } catch (Exception ignored) {}

        ctItems.add(ct);
    }

    private void addGiftItem(Thuoc_SanPham sp) {
        if (sp == null) return;
        boolean exists = giftItems.stream().anyMatch(g ->
                g.getThuocTangKem() != null &&
                        String.valueOf(g.getThuocTangKem().getMaThuoc()).equals(String.valueOf(sp.getMaThuoc()))
        );
        if (exists) return;

        Thuoc_SP_TangKem gift = new Thuoc_SP_TangKem();
        try { gift.setThuocTangKem(sp); } catch (Exception ignored) {}
        try { gift.setSoLuong(1); } catch (Exception ignored) {}

        giftItems.add(gift);
    }

    private void bindOverlayPositionForThuoc() {
        if (paneThuocArea == null || tfTimThuoc == null || listViewThuoc == null) return;
        Runnable pos = () -> positionSuggestionList(listViewThuoc, tfTimThuoc, paneThuocArea);
        paneThuocArea.sceneProperty().addListener((o, oldSc, sc) -> { if (sc != null) Platform.runLater(pos); });
        tfTimThuoc.widthProperty().addListener((o, a, b) -> pos.run());
        tfTimThuoc.heightProperty().addListener((o, a, b) -> pos.run());
        tfTimThuoc.localToSceneTransformProperty().addListener((o, a, b) -> pos.run());
        paneThuocArea.widthProperty().addListener((o, a, b) -> pos.run());
        paneThuocArea.heightProperty().addListener((o, a, b) -> pos.run());
    }

    private void positionSuggestionList(ListView<?> lv, TextField tf, AnchorPane container) {
        if (lv == null || tf == null || container == null || container.getScene() == null) return;
        Point2D tfSceneTopLeft = tf.localToScene(0, 0);
        Point2D containerLocalTopLeft = container.sceneToLocal(tfSceneTopLeft);
        double x = containerLocalTopLeft.getX();
        double y = containerLocalTopLeft.getY() + tf.getHeight();
        lv.relocate(x, y);
        lv.setPrefWidth(tf.getWidth());
    }

    private void toggleSuggestion(TextField tf, ListView<?> lv, boolean show) {
        if (show) {
            if (paneThuocArea != null && tf == tfTimThuoc && lv == listViewThuoc) {
                positionSuggestionList(lv, tf, paneThuocArea);
            }
            lv.setPrefHeight(160);
            lv.toFront();
        } else {
            lv.setPrefHeight(0);
        }
        lv.setVisible(show);
        lv.setManaged(false);
    }

    private void hideSuggestion(ListView<?> lv) {
        lv.setVisible(false);
        lv.setManaged(false);
        lv.setPrefHeight(0);
    }

    private void updateGiftTabVisibility() {
        if (tabPaneProducts == null || tabTangKem == null || cbLoaiKM == null) return;
        boolean show = "LKM001".equalsIgnoreCase(cbLoaiKM.getValue());
        if (show) {
            if (!tabPaneProducts.getTabs().contains(tabTangKem)) tabPaneProducts.getTabs().add(tabTangKem);
        } else {
            tabPaneProducts.getTabs().remove(tabTangKem);
        }
    }

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

    public void btnHuyClick() {
        if (tfTimThuoc == null) return;
        Stage stage = (Stage) tfTimThuoc.getScene().getWindow();
        stage.close();
    }

    public void btnLuuClick() {
        // TODO: persist km + ctItems + giftItems
    }

    private void installSpinnerColumn(
            TableColumn<ChiTietKhuyenMai, Integer> column,
            int min, int max, int step,
            Function<ChiTietKhuyenMai, Integer> getter,
            BiConsumer<ChiTietKhuyenMai, Integer> setter
    ) {
        column.setCellFactory(tc -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>();
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                spinner.setEditable(true);
                spinner.setMaxWidth(Double.MAX_VALUE);
                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min, step));
                spinner.valueProperty().addListener((obs, oldV, newV) -> {
                    int row = getIndex();
                    if (row >= 0 && getTableView() != null && row < getTableView().getItems().size()) {
                        ChiTietKhuyenMai item = getTableView().getItems().get(row);
                        if (item != null && newV != null) setter.accept(item, newV);
                    }
                });
                spinner.focusedProperty().addListener((obs, was, is) -> {
                    if (!is) {
                        try {
                            spinner.increment(0); // commit editor text
                        } catch (NumberFormatException ex) {
                            // reset to current model value
                            int row = getIndex();
                            if (row >= 0 && getTableView() != null && row < getTableView().getItems().size()) {
                                ChiTietKhuyenMai item = getTableView().getItems().get(row);
                                Integer v = item == null ? 0 : getter.apply(item);
                                ((SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory())
                                        .setValue(v == null ? 0 : v);
                            }
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || getTableView() == null) { setGraphic(null); return; }
                int idx = getIndex();
                if (idx < 0 || idx >= getTableView().getItems().size()) { setGraphic(null); return; }
                ChiTietKhuyenMai item = getTableView().getItems().get(idx);
                int current = 0;
                if (item != null) {
                    Integer v = getter.apply(item);
                    current = v == null ? 0 : v;
                }
                SpinnerValueFactory.IntegerSpinnerValueFactory vf =
                        (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
                vf.setMin(min); vf.setMax(max); vf.setAmountToStepBy(step); vf.setValue(current);
                setGraphic(spinner);
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(
                "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatKhuyenMai/SuaKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
