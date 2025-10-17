package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuNhap;

import com.example.pharmacymanagementsystem_qlht.dao.PhieuNhap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class TimKiemPhieuNhap_Ctrl extends Application {
    public TableColumn<PhieuNhap, String> colChiTiet;
    public ComboBox cbxTimKiem;
    public TextField txtTimKiem;
    public DatePicker txtNgayNhapMax;
    public DatePicker txtNgayNhapMin;
    public ComboBox chonNhanVien;
    public ComboBox cbxChonNhaCC;
    public CheckBox cboxTrangThai;
    @FXML
    private TableView<PhieuNhap> tblPhieuNhap;
    @FXML
    private TableColumn<PhieuNhap, String> colMaPN;
    @FXML
    private TableColumn<PhieuNhap, String> colNhaCungCap;
    @FXML
    private TableColumn<PhieuNhap, Timestamp> colNgayNhap;
    @FXML
    private TableColumn<PhieuNhap, String> colTrangThai;
    @FXML
    private TableColumn<PhieuNhap, String> colGhiChu;
    @FXML
    private TableColumn<PhieuNhap, String> colNhanVien;
    @FXML
    private ObservableList<PhieuNhap> duLieuChinh = FXCollections.observableArrayList();
    @FXML
    private FilteredList<PhieuNhap> duLieu;

    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuNhapHang/TKPhieuNhapHang_GUI.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        duLieuChinh.addAll(new PhieuNhap_Dao().selectAll());
        duLieu = new FilteredList<>(duLieuChinh, sp -> true);
        tblPhieuNhap.setItems(duLieu);
        ThemPhieuNhapVaoCot();
        cbxTimKiem.getItems().addAll("Loại tìm kiếm", "Mã phiếu nhập", "Nhà cung cấp", "Nhân viên");
        cbxTimKiem.setValue("Loại tìm kiếm");
        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> TimKiemtxt());
        cbxChonNhaCC.getItems().addAll(new PhieuNhap_Dao().getAllNCC());
        cbxChonNhaCC.getItems().addFirst("Chọn nhà cung cấp");
        chonNhanVien.getItems().addAll(new PhieuNhap_Dao().getAllNhanVien());
        chonNhanVien.getItems().addFirst("Chọn nhân viên");
        cbxChonNhaCC.setOnAction(event -> Loc());
        chonNhanVien.setOnAction(event -> Loc());
        cboxTrangThai.setOnAction(event -> Loc());
        txtNgayNhapMin.setOnAction(event -> Loc());
        txtNgayNhapMax.setOnAction(event -> Loc());
    }

    public void ThemPhieuNhapVaoCot() {
        colMaPN.setCellValueFactory(new PropertyValueFactory<>("maPN"));
        colNhaCungCap.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getNhaCungCap().getTenNCC())
        );
        colNgayNhap.setCellValueFactory(new PropertyValueFactory<>("ngayNhap"));
        colTrangThai.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getTrangThai() ? "Hoàn tất" : "Chưa hoàn tất")
        );
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colNhanVien.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getNhanVien().getTenNV())
        );
        colChiTiet.setCellFactory(cel -> new TableCell<PhieuNhap, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    PhieuNhap temp = getTableView().getItems().get(getIndex());
                    btnChiTietClick(temp);
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
    private void btnChiTietClick(PhieuNhap pn) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuNhapHang/ChiTietPhieuNhapHang_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            ChiTietPhieuNhap_Ctrl ctrl = loader.getController();
            ctrl.load(pn);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void TimKiemtxt() {
        String text = txtTimKiem.getText().toLowerCase();
        if (text == null || text.isEmpty()) {
            duLieu.setPredicate(sp -> true);
        } else {
            switch (cbxTimKiem.getValue().toString()) {
                case "Mã phiếu nhập":
                    duLieu.setPredicate(sp -> sp.getMaPN().toLowerCase().contains(text));
                    break;
                case "Nhà cung cấp":
                    duLieu.setPredicate(sp -> sp.getNhaCungCap().getTenNCC().toLowerCase().contains(text));
                    break;
                case "Nhân viên":
                    duLieu.setPredicate(sp -> sp.getNhanVien().getTenNV().toLowerCase().contains(text));
                    break;
                default:
                    duLieu.setPredicate(sp -> true);
                    break;
            }
        }
    }
    private void Loc() {
        String ncc = (String) cbxChonNhaCC.getValue();
        String nv = (String) chonNhanVien.getValue();
        Boolean trangThai = cboxTrangThai.isSelected() ? true : null;
        Timestamp ngayMin = txtNgayNhapMin.getValue() != null ? Timestamp.valueOf(txtNgayNhapMin.getValue().atStartOfDay()) : null;
        Timestamp ngayMax = txtNgayNhapMax.getValue() != null ? Timestamp.valueOf(txtNgayNhapMax.getValue().atStartOfDay()) : null;

        duLieu.setPredicate(sp -> {
            boolean matchesNCC = (ncc == null || ncc.equals("Chọn nhà cung cấp")) || sp.getNhaCungCap().getTenNCC().equals(ncc);
            boolean matchesNV = (nv == null || nv.equals("Chọn nhân viên")) || sp.getNhanVien().getTenNV().equals(nv);
            boolean matchesTrangThai = (trangThai == null) || sp.getTrangThai() == trangThai;
            boolean matchesNgayMin = (ngayMin == null) || !sp.getNgayNhap().before(ngayMin);
            boolean matchesNgayMax = (ngayMax == null) || !sp.getNgayNhap().after(ngayMax);

            return matchesNCC && matchesNV && matchesTrangThai && matchesNgayMin && matchesNgayMax;
        });
    }

    public void btnXoaRong(ActionEvent actionEvent) {
        txtTimKiem.clear();
        cbxChonNhaCC.setValue("Chọn nhà cung cấp");
        chonNhanVien.setValue("Chọn nhân viên");
        cboxTrangThai.setSelected(false);
        txtNgayNhapMin.setValue(null);
        txtNgayNhapMax.setValue(null);
        Loc();
    }
}
