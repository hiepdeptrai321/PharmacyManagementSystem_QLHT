package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.LoaiHang_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Objects;

public class TimKiemThuoc_Ctrl extends Application {

    public TableColumn<Thuoc_SanPham,String> colTenThuoc;
    public TableColumn<Thuoc_SanPham,String> colMaThuoc;
    public TableColumn<Thuoc_SanPham,String> colHamLuong;
    public TableColumn<Thuoc_SanPham,String> colXuatXu;
    public TableColumn<Thuoc_SanPham,String> colSDK_GPNK;
    public TableColumn<Thuoc_SanPham,String> colLoaiHang;
    public TableColumn<Thuoc_SanPham,String> colViTri;
    public TableColumn<Thuoc_SanPham,String> colChiTiet;
    public TableView<Thuoc_SanPham> tbl_Thuoc;
    public ComboBox cbxLoaiHang;
    public ComboBox cbxXuatSu;
    public TextField txtHamLuongMin;
    public TextField txtHamLuongMax;
    @FXML
    private ComboBox<String> cboTimKiem;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private ObservableList<Thuoc_SanPham> duLieuChinh = FXCollections.observableArrayList();
    @FXML
    private FilteredList<Thuoc_SanPham> duLieu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//  phương thức khởi tạo
    @FXML
    public void initialize() {

        duLieu = new FilteredList<>(duLieuChinh, sp -> true);
        tbl_Thuoc.setItems(duLieu);
        cboTimKiem.getItems().addAll("Loại tìm kiếm", "Mã thuốc", "Tên thuốc", "Nước sản xuất", "Loại hàng", "Vị trí");
        cboTimKiem.setValue("Loại tìm kiếm");
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> TimKiemTxt());
        cboTimKiem.valueProperty().addListener((obs, oldVal, newVal) -> TimKiemTxt());
        cbxLoaiHang.getItems().addFirst("Chọn loại hàng");
        cbxLoaiHang.setValue("Chọn loại hàng");
        cbxXuatSu.getItems().addFirst("Chọn xuất xứ");
        cbxXuatSu.setValue("Chọn xuất xứ");
        cbxLoaiHang.getItems().addAll(new LoaiHang_Dao().getAllTenLH());
        cbxXuatSu.getItems().addAll(new Thuoc_SanPham_Dao().getAllXuatXu());
        cbxLoaiHang.setOnAction(e -> TimKiemLoc());
        cbxXuatSu.setOnAction(e -> TimKiemLoc());
        txtHamLuongMin.textProperty().addListener((obs, oldVal, newVal) -> TimKiemLoc());
        txtHamLuongMax.textProperty().addListener((obs, oldVal, newVal) -> TimKiemLoc());
        Platform.runLater(()->{
            duLieuChinh.addAll(new Thuoc_SanPham_Dao().selectAll());
            loadTable();
        });
    }

//  button chuyển sang giao diện chi tiết thuốc
    private void btnChiTietClick(Thuoc_SanPham sp) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/ChiTietThuoc_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            this.getClass();
            ChiTietThuoc_Ctrl ctrl = loader.getController();
            ctrl.load(sp);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  button xóa rỗng các trường lọc và tìm kiếm
    public void btnXoaRong(ActionEvent actionEvent) {
        txtTimKiem.clear();
        cbxLoaiHang.setValue("Chọn loại hàng");
        cbxXuatSu.setValue("Chọn xuất xứ");
        txtHamLuongMin.clear();
        txtHamLuongMax.clear();
        TimKiemLoc();
    }

    private void loadTable() {
        colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<>("HamLuongDonVi"));
        colXuatXu.setCellValueFactory(new PropertyValueFactory<>("nuocSX"));
        colSDK_GPNK.setCellValueFactory(new PropertyValueFactory<>("SDK_GPNK"));
        colLoaiHang.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getLoaiHang().getTenLoaiHang()));
        colViTri.setCellValueFactory(cel -> new SimpleStringProperty(cel.getValue().getVitri().getTenKe()));
        colChiTiet.setCellFactory(cel -> new TableCell<Thuoc_SanPham, String>() {
            private final Button btn = new Button("Chi tiết");
            {
                btn.setOnAction(event -> {
                    Thuoc_SanPham temp = getTableView().getItems().get(getIndex());
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

//  funtion lọc dữ liệu từ các combobox và textfield
    private void TimKiemLoc() {
        String loaiHang = (String) cbxLoaiHang.getValue();
        String xuatXu = (String) cbxXuatSu.getValue();
        String hamLuongMinStr = txtHamLuongMin.getText().trim();
        String hamLuongMaxStr = txtHamLuongMax.getText().trim();

        Double hamLuongMin = hamLuongMinStr.isEmpty() ? null : Double.parseDouble(hamLuongMinStr);
        Double hamLuongMax = hamLuongMaxStr.isEmpty() ? null : Double.parseDouble(hamLuongMaxStr);

        duLieu.setPredicate(sp -> {
            boolean matchesLoaiHang = (loaiHang == null || loaiHang.isEmpty()||loaiHang=="Chọn loại hàng" || sp.getLoaiHang().getTenLoaiHang().equals(loaiHang));
            boolean matchesXuatXu = (xuatXu == null || xuatXu.isEmpty() || xuatXu =="Chọn xuất xứ" || sp.getNuocSX().equals(xuatXu));
            boolean matchesHamLuong = true;

            if (hamLuongMin != null) {
                matchesHamLuong = sp.getHamLuong() >= hamLuongMin;
            }
            if (hamLuongMax != null) {
                matchesHamLuong = matchesHamLuong && sp.getHamLuong() <= hamLuongMax;
            }

            return matchesLoaiHang && matchesXuatXu && matchesHamLuong;
        });
    }

    private void TimKiemTxt() {
        String filterType = cboTimKiem.getValue();
        String filterText = txtTimKiem.getText().toLowerCase().trim();

        duLieu.setPredicate(thuoc -> {
            if (filterText.isEmpty() || filterType.equals("Tất cả")) {
                return true;
            }

            switch (filterType) {
                case "Mã thuốc":
                    return thuoc.getMaThuoc().toLowerCase().contains(filterText);
                case "Tên thuốc":
                    return thuoc.getTenThuoc().toLowerCase().contains(filterText);
                case "Nước sản xuất":
                    return thuoc.getNuocSX().toLowerCase().contains(filterText);
                case "Loại hàng":
                    return thuoc.getLoaiHang().getTenLoaiHang().toLowerCase().contains(filterText);
                case "Vị trí":
                    return thuoc.getVitri().getTenKe().toLowerCase().contains(filterText);
                default:
                    return false;
            }
        });
    }
}