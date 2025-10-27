
package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoatDong;

import com.example.pharmacymanagementsystem_qlht.dao.HoatDong_Dao;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.HoatDong;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class TKHoatDong_Ctrl extends javafx.application.Application {

    @FXML private TextField tfTim;
    @FXML private Button btnTim;
    @FXML private Button btnLamMoi;
    @FXML private TableView<HoatDong> tbHoatDong;
    @FXML private TableColumn<HoatDong, String> colSTT;
    @FXML private TableColumn<HoatDong, String> colMa;
    @FXML private TableColumn<HoatDong, String> colLoai;
    @FXML private TableColumn<HoatDong, String> colBang;
    @FXML private TableColumn<HoatDong, String> colThoiGian;
    @FXML private TableColumn<HoatDong, String> colNguoi;
    @FXML private TableColumn<HoatDong, String> colChiTiet;

    @FXML private DatePicker dpTuNgay;
    @FXML private DatePicker dpDenNgay;
    @FXML private ComboBox<String> cbBoLoc;

    private final HoatDong_Dao dao = new HoatDong_Dao();
    private final SimpleDateFormat tsFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    public void initialize() {
        configureColumns();
        configureFilters();
        if (btnTim != null) btnTim.setOnAction(e -> applySearch());
        tfTim.setOnAction(e-> applySearch());
        Platform.runLater(()-> {
            loadTable();
        });
        btnLamMoi.setOnAction(e-> LamMoi());
    }

    private void configureFilters() {
        cbBoLoc.setItems(FXCollections.observableArrayList("Tất cả", "Hôm nay", "Tuần này", "Tháng này"));
        cbBoLoc.getSelectionModel().selectFirst();

        if (tfTim != null) {
            tfTim.setOnAction(e -> applySearch());
            tfTim.setOnKeyPressed(k -> { if (k.getCode() == KeyCode.ENTER) applySearch(); });
        }

        if (dpTuNgay != null) dpTuNgay.valueProperty().addListener((obs, o, n) -> applySearch());
        if (dpDenNgay != null) dpDenNgay.valueProperty().addListener((obs, o, n) -> applySearch());

        if (cbBoLoc != null) cbBoLoc.setOnAction(e -> {
            String sel = cbBoLoc.getValue();
            setRangeForPreset(sel);
            applySearch();
        });
    }

    private void setRangeForPreset(String preset) {
        if (preset == null || "Tất cả".equals(preset)) {
            if (dpTuNgay != null) dpTuNgay.setValue(null);
            if (dpDenNgay != null) dpDenNgay.setValue(null);
            return;
        }
        LocalDate now = LocalDate.now();
        switch (preset) {
            case "Hôm nay" -> {
                if (dpTuNgay != null) dpTuNgay.setValue(now);
                if (dpDenNgay != null) dpDenNgay.setValue(now);
            }
            case "Tuần này" -> {
                LocalDate start = now.with(DayOfWeek.MONDAY);
                LocalDate end = now.with(DayOfWeek.SUNDAY);
                if (dpTuNgay != null) dpTuNgay.setValue(start);
                if (dpDenNgay != null) dpDenNgay.setValue(end);
            }
            case "Tháng này" -> {
                LocalDate start = now.withDayOfMonth(1);
                LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
                if (dpTuNgay != null) dpTuNgay.setValue(start);
                if (dpDenNgay != null) dpDenNgay.setValue(end);
            }
            default -> {
                if (dpTuNgay != null) dpTuNgay.setValue(null);
                if (dpDenNgay != null) dpDenNgay.setValue(null);
            }
        }
    }

    private void configureColumns() {
        colSTT.setCellValueFactory(cellData -> {
            int idx = cellData.getTableView().getItems().indexOf(cellData.getValue());
            String display = idx < 0 ? "" : String.valueOf(idx + 1);
            return new javafx.beans.property.SimpleStringProperty(display);
        });

        colMa.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colLoai.setCellValueFactory(new PropertyValueFactory<>("loaiHD"));
        colBang.setCellValueFactory(new PropertyValueFactory<>("bang"));

        colThoiGian.setCellValueFactory(cd -> {
            Timestamp t = cd.getValue().getThoiGian();
            String s = t == null ? "" : tsFormat.format(t);
            return new javafx.beans.property.SimpleStringProperty(s);
        });

        colNguoi.setCellValueFactory(cd -> {
            NhanVien nv = cd.getValue().getNhanVien();
            String s = nv == null ? "" : nv.getTenNV();
            return new javafx.beans.property.SimpleStringProperty(s);
        });
        colNguoi.setCellFactory(col -> new TableCell<HoatDong, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        colChiTiet.setCellFactory(col -> new TableCell<HoatDong, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    HoatDong hd = getTableView().getItems().get(getIndex());
                    showDetails(hd);
                });
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void loadTable() {
        List<HoatDong> list = dao.selectAll();
        tbHoatDong.setItems(FXCollections.observableArrayList(list));
        tbHoatDong.refresh();
    }

    private void applySearch() {
        String keyword = tfTim == null ? "" : (tfTim.getText() == null ? "" : tfTim.getText().trim().toLowerCase());

        ZoneId zone = ZoneId.systemDefault();
        final long sMillis;
        final long eMillis;
        if (dpTuNgay != null && dpTuNgay.getValue() != null) {
            sMillis = dpTuNgay.getValue().atStartOfDay(zone).toInstant().toEpochMilli();
        } else {
            sMillis = Long.MIN_VALUE;
        }
        if (dpDenNgay != null && dpDenNgay.getValue() != null) {
            eMillis = dpDenNgay.getValue().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1;
        } else {
            eMillis = Long.MAX_VALUE;
        }

        List<HoatDong> list = dao.selectAll();
        List<HoatDong> filtered = list.stream()
                .filter(h -> {
                    boolean matchesKeyword;
                    if (keyword.isEmpty()) matchesKeyword = true;
                    else {
                        boolean ma = h.getMaHD() != null && h.getMaHD().toLowerCase().contains(keyword);
                        boolean nv = h.getNhanVien() != null && h.getNhanVien().toString().toLowerCase().contains(keyword);
                        matchesKeyword = ma || nv;
                    }

                    Timestamp t = h.getThoiGian();
                    long tms = t == null ? Long.MIN_VALUE : t.getTime();
                    boolean matchesDate = (tms >= sMillis) && (tms <= eMillis);

                    return matchesKeyword && matchesDate;
                })
                .toList();

        tbHoatDong.setItems(FXCollections.observableArrayList(filtered));
        tbHoatDong.refresh();
    }

    private void showDetails(HoatDong hd) {
        Stage chiTiet = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoatDong/ChiTietHoatDong_GUI.fxml"));
            Parent root = loader.load();
            var ctrl = loader.getController();
            if (ctrl instanceof ChiTietHoatDong_Ctrl) {
                ((ChiTietHoatDong_Ctrl) ctrl).loadData(hd);
            }
            Stage dialog = new Stage();
            dialog.initOwner(btnLamMoi.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Chi tiết hoạt động");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void LamMoi() {
        tfTim.clear();
        loadTable();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoatDong/TKHoatDong_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
