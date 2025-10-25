package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuNhapHang;

import com.example.pharmacymanagementsystem_qlht.TienIch.VNDFormatter;
import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LapPhieuNhapHang_Ctrl extends Application {
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colSTT;
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colMaThuoc;
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colTenThuoc;
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colLoHang;
    @FXML
    public TableColumn<CTPN_TSPTL_CHTDVT, LocalDate> colHanSuDung;
    public TableColumn<CTPN_TSPTL_CHTDVT, Integer> colSoLuong;
    public TableColumn<CTPN_TSPTL_CHTDVT, Double> colDonGiaNhap;
    public TableColumn<CTPN_TSPTL_CHTDVT, Float> colChietKhau;
    public TableColumn<CTPN_TSPTL_CHTDVT, Float> colThue;
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colXoa;
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colDonViNhap;
    public TableColumn<CTPN_TSPTL_CHTDVT, LocalDate> colNSX;
    public TableColumn<CTPN_TSPTL_CHTDVT, String> colThanhTien;
    @FXML
    private TableView<CTPN_TSPTL_CHTDVT> tblNhapThuoc;
    @FXML
    private ComboBox<String> cbxNCC;
    @FXML
    private TextField txtMaPhieuNhap;
    @FXML
    private DatePicker txtNgayNhap;
    @FXML
    private TextArea txtGhiChu;
    @FXML
    private TextField txtTongGiaNhap;
    @FXML
    private TextField txtTongTienChietKhau;
    @FXML
    private TextField txtTongTienThue;
    @FXML
    private TextField txtThanhTien;
    @FXML
    private TextField txtTimKiemChiTietDonViTinh;
    @FXML
    private ListView<String> listViewNhaCungCap;
    @FXML
    private ListView<ChiTietDonViTinh> listViewChiTietDonViTinh;

    private ObservableList<ChiTietDonViTinh> allChiTietDonViTinh;
    private ObservableList<NhaCungCap> listNCC;
    private NhaCungCap ncc = new NhaCungCap();
    private int maLoHienTai = 0;
    private ChiTietPhieuNhap_Dao ctpn_dao = new ChiTietPhieuNhap_Dao();
    private ObservableList<CTPN_TSPTL_CHTDVT> listNhapThuoc = FXCollections.observableArrayList();
    private PhieuNhap_Dao phieuNhapDao = new PhieuNhap_Dao();
    private List<PhieuNhap> allPhieuNhaps = phieuNhapDao.selectAll();

    //  ============================================================================================phương thức initialize
    public void initialize() {
        taiDanhSachNCC();
        timKiemNhaCungCap();
        timKiemDonViTinh();
        loadTable();
//      Khởi tạo mã lô hàng hiện tại từ DB
        String lastMaLH = ctpn_dao.generateMaLH();
        if (lastMaLH != null && lastMaLH.startsWith("LH")) {
            maLoHienTai = Integer.parseInt(lastMaLH.substring(2)) - 1;
        } else {
            maLoHienTai = 0;
        }
        suKienThemChiTietDonViTinhVaoBang();
        suKienThemMotDongMoiVaoBang();
        listNhapThuoc.addListener((javafx.collections.ListChangeListener<CTPN_TSPTL_CHTDVT>) change -> {
            boolean shouldUpdate = false;
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                    shouldUpdate = true;
                }
            }
            if (shouldUpdate) {
                // đảm bảo chạy trên UI thread
                Platform.runLater(this::suKienThemMotDongMoiVaoBang);
            }
        });


    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/LapPhieuNhapHang_GUI.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //  ============================================================================================Tải danh sách nhà cung cấp vào ComboBox
    private void taiDanhSachNCC() {

//      lấy danh sách nhà cung cấp từ cơ sở dữ liệu
        listNCC = FXCollections.observableArrayList(new NhaCungCap_Dao().selectAll());
        ObservableList<String> nccList = FXCollections.observableArrayList();

//      thêm nhà cung cấp vào danh sách hiển thị
        for (NhaCungCap ncc : listNCC) {
            nccList.add(ncc.getMaNCC() + " - " + ncc.getTenNCC());
        }

//      thiết lập danh sách cho ComboBox
        cbxNCC.setItems(nccList);
        cbxNCC.setEditable(true);
    }

    //  ============================================================================================Tìm kiếm nhà cung cấp trong ComboBox
    private void timKiemNhaCungCap() {

//      Chỉnh style cho list view nhà cung cấp
        listViewNhaCungCap.setVisible(false);
        listViewNhaCungCap.setStyle("-fx-background-color: white; -fx-border-color: #dcdcdc; " + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2);" + "-fx-border-color: #cccccc;" + "-fx-border-width: 1;");
        listViewNhaCungCap.setFocusTraversable(false);

//      =======================Thêm listener cho giá trị được chọn trong ComboBox
        cbxNCC.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cbxNCC.getEditor().setText(newVal);

//              Tìm nhà cung cấp tương ứng với giá trị được chọn
                ncc = listNCC.stream()
                        .filter(item -> (item.getMaNCC() + " - " + item.getTenNCC()).equals(newVal))
                        .findFirst()
                        .orElse(null);
                listViewNhaCungCap.setVisible(false);
            }
        });

//      =======================Thêm listener cho txt của ComboBox
        cbxNCC.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {

//          Nếu txt rỗng thì ẩn list view
            if (newVal == null || newVal.trim().isEmpty()) {
                listViewNhaCungCap.setVisible(false);
                return;
            }

//          Ẩn cbx khi mà đang hiển thị list view
            cbxNCC.hide();

//          Lọc danh sách nhà cung cấp dựa trên từ khóa nhập vào
            String keyword = newVal.toLowerCase().trim();
            ObservableList<String> danhSachDaLoc = FXCollections.observableArrayList();

//          Lặp qua tất cả nhà cung cấp và thêm vào danh sách lọc nếu khớp với từ khóa
            for (NhaCungCap ncc : listNCC) {
                String nccDaLoc = ncc.getMaNCC() + " - " + ncc.getTenNCC();
                if (nccDaLoc.toLowerCase().contains(keyword)) {
                    danhSachDaLoc.add(nccDaLoc);
                }
            }

//          Cập nhật danh sách hiển thị trong list view
            listViewNhaCungCap.setItems(danhSachDaLoc);

//          Nếu ko có kết quả thì ẩn list view
            listViewNhaCungCap.setVisible(!danhSachDaLoc.isEmpty());
        });

//      =======================Xử lý sự kiện khi chọn một nhà cung cấp từ list view
        listViewNhaCungCap.setOnMouseClicked(e -> {
            String selected = listViewNhaCungCap.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cbxNCC.getEditor().setText(selected);
                listViewNhaCungCap.setVisible(false);

//              Tìm nhà cung cấp tương ứng với giá trị được chọn
                ncc = listNCC.stream()
                        .filter(item -> (item.getMaNCC() + " - " + item.getTenNCC()).equals(selected))
                        .findFirst()
                        .orElse(null);

//              Nếu tìm thấy thì hiển thị trong txt của cbxNCC
                if (ncc != null) {
                    cbxNCC.getEditor().setText(ncc.getMaNCC() + " - " + ncc.getTenNCC());
                }
            }
        });


//      =======================Ẩn list view khi ComboBox mất focus
        cbxNCC.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) listViewNhaCungCap.setVisible(false);
        });

    }

    //  ============================================================================================Thiết lập chức năng tìm kiếm chi tiết đơn vị tính
    private void timKiemDonViTinh() {

//      Lấy tất cả chi tiết đơn vị tính từ cơ sở dữ liệu và chỉnh style cho list view Chi Tiết Đơn Vị Tính
        allChiTietDonViTinh = FXCollections.observableArrayList(new ChiTietDonViTinh_Dao().selectAll());
        listViewChiTietDonViTinh.setItems(allChiTietDonViTinh);
        listViewChiTietDonViTinh.setVisible(false);
        listViewChiTietDonViTinh.setStyle("-fx-background-color: white; -fx-border-color: #dcdcdc; " + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2);" + "-fx-border-color: #cccccc;" + "-fx-border-width: 1;");

//      =======================Hiển thị trong list view Chi Tiết Đơn Vị Tính
        listViewChiTietDonViTinh.setCellFactory(data -> new ListCell<>() {
            @Override
            protected void updateItem(ChiTietDonViTinh item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getThuoc().getMaThuoc() + " - " +
                            item.getThuoc().getTenThuoc() + " - " +
                            item.getDvt().getTenDonViTinh());
                }
            }
        });

//      =======================Thêm listener cho txt tìm kiếm Chi Tiết Đơn Vị Tính
        txtTimKiemChiTietDonViTinh.textProperty().addListener((obs, oldVal, newVal) -> {

//          Nếu txt rỗng thì ẩn list view
            if (newVal == null || newVal.trim().isEmpty()) {
                listViewChiTietDonViTinh.setVisible(false);
                return;
            }

//          Lọc danh sách Chi Tiết Đơn Vị Tính dựa trên từ khóa nhập vào
            String keyword = newVal.toLowerCase();
            ObservableList<ChiTietDonViTinh> filtered = FXCollections.observableArrayList();

//          Lặp qua tất cả chi tiết đơn vị tính và thêm vào danh sách lọc nếu khớp với từ khóa
            for (ChiTietDonViTinh item : allChiTietDonViTinh) {
                if (item.getThuoc().getMaThuoc().toLowerCase().contains(keyword)
                        || item.getThuoc().getTenThuoc().toLowerCase().contains(keyword)) {
                    filtered.add(item);
                }
            }

//          Cập nhật danh sách hiển thị trong list view
            listViewChiTietDonViTinh.setItems(filtered);
            listViewChiTietDonViTinh.setVisible(!filtered.isEmpty());
        });
    }

    public void btnThemThuocClick(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/ThemThuoc_LapPhieuNhapHang_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ThemThuoc_LapPhieuNhapHang_Ctrl ctrl = loader.getController();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTable() {
//      Cột STT
        colSTT.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(tblNhapThuoc.getItems().indexOf(cellData.getValue()) + 1)));
//      Cột mã thuốc
        colMaThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChiTietDonViTinh().getThuoc().getMaThuoc()));

//      Cột tên thuốc
        colTenThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChiTietDonViTinh().getThuoc().getTenThuoc()));

//      Cột đơn vị nhập
        colDonViNhap.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChiTietDonViTinh().getDvt().getTenDonViTinh()));

//      Cột mã lô hàng
        colLoHang.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChiTietSP_theoLo().getMaLH()));

//      Cột ngày sản xuất
        colNSX.setCellFactory(column -> new TableCell<CTPN_TSPTL_CHTDVT, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setEditable(false);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                CTPN_TSPTL_CHTDVT rowItem = getTableRow().getItem();

//              Lấy giá trị sau khi chọn ngày
                Date NSX = rowItem.getChiTietSP_theoLo() != null ? rowItem.getChiTietSP_theoLo().getNsx() : null;
                datePicker.setValue(NSX != null ? NSX.toLocalDate() : null);


//              Xóa listener cũ
                datePicker.valueProperty().removeListener((obs, oldVal, newVal) -> {
                });

//              Thêm listener mới
                datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
                    if (newDate != null) {
                        if (rowItem.getChiTietSP_theoLo() == null)
                            rowItem.setChiTietSP_theoLo(new Thuoc_SP_TheoLo());

                        rowItem.getChiTietSP_theoLo().setNsx(Date.valueOf(newDate));
                        getTableView().refresh();
                        if (newDate.isAfter(Date.valueOf(LocalDate.now()).toLocalDate())) {
                            rowItem.getChiTietSP_theoLo().setNsx(null);
                            getTableView().refresh();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setHeaderText(null);
                            alert.setContentText("Ngày sản xuất không được sau ngày hiện tại!");
                            alert.getButtonTypes().setAll(ButtonType.OK);
                            alert.showAndWait();
                        }
                    }
                });

                setGraphic(datePicker);
            }

            @Override
            public void startEdit() {
                super.startEdit();
                Platform.runLater(() -> datePicker.requestFocus());
            }
        });
//      Cột hạn sử dụng
        colHanSuDung.setCellFactory(column -> new TableCell<CTPN_TSPTL_CHTDVT, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setEditable(false);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                CTPN_TSPTL_CHTDVT rowItem = getTableRow().getItem();

//              Lấy giá trị sau khi chọn ngày
                Date hsdDate = rowItem.getChiTietSP_theoLo() != null ? rowItem.getChiTietSP_theoLo().getHsd() : null;
                datePicker.setValue(hsdDate != null ? hsdDate.toLocalDate() : null);

//              Xóa listener cũ
                datePicker.valueProperty().removeListener((obs, oldVal, newVal) -> {
                });

//              Thêm listener mới
                datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
                    if (newDate != null) {
                        if (rowItem.getChiTietSP_theoLo() == null)
                            rowItem.setChiTietSP_theoLo(new Thuoc_SP_TheoLo());

                        rowItem.getChiTietSP_theoLo().setHsd(Date.valueOf(newDate));
                        getTableView().refresh();
                        if (newDate.isBefore(Date.valueOf(LocalDate.now()).toLocalDate())) {
                            rowItem.getChiTietSP_theoLo().setHsd(null);
                            getTableView().refresh();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setHeaderText(null);
                            alert.setContentText("Ngày sản xuất không được sau ngày hiện tại!");
                            alert.getButtonTypes().setAll(ButtonType.OK);
                            alert.showAndWait();
                        }
                    }
                });

                setGraphic(datePicker);
            }

            @Override
            public void startEdit() {
                super.startEdit();
                Platform.runLater(() -> datePicker.requestFocus());
            }
        });

//      Cột nút Xóa
        colXoa.setCellFactory(cellData -> new TableCell<CTPN_TSPTL_CHTDVT, String>() {
            private final Button btn = new Button("Xóa");

            {
                btn.setOnAction(event -> {
                    CTPN_TSPTL_CHTDVT item = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(item);
                    suKienThemMotDongMoiVaoBang();
                });

            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colSoLuong.setCellFactory(column -> new TableCell<CTPN_TSPTL_CHTDVT, Integer>() {
            private final TextField textField = new TextField();

            {
                textField.setAlignment(Pos.CENTER_RIGHT);
                textField.setPrefWidth(80);

                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        CTPN_TSPTL_CHTDVT rowItem = getTableRow() != null ? getTableRow().getItem() : null;
                        if (rowItem != null) {
                            String txt = textField.getText();
                            try {
                                int sl = Integer.parseInt(txt);
                                if (sl < 0) {
                                    textField.setText("0");
                                } else {
                                    rowItem.getChiTietPhieuNhap().setSoLuong(sl);
                                    suKienThemMotDongMoiVaoBang();
                                    tblNhapThuoc.refresh();
                                }
                            } catch (NumberFormatException ex) {
                                Integer cur = rowItem.getChiTietPhieuNhap().getSoLuong();
                                textField.setText(cur != null ? cur.toString() : "0");
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CTPN_TSPTL_CHTDVT rowItem = getTableRow().getItem();
                    Integer soLuong = rowItem.getChiTietPhieuNhap().getSoLuong();
                    textField.setText(soLuong != null ? soLuong.toString() : "0");
                    setGraphic(textField);
                }
            }
        });

        colDonGiaNhap.setCellFactory(column -> new TableCell<CTPN_TSPTL_CHTDVT, Double>() {
            private final TextField textField = new TextField();
            private final VNDFormatter vndFormatter = new VNDFormatter();

            {
                textField.setAlignment(Pos.CENTER_RIGHT);
                textField.setPrefWidth(100);
                vndFormatter.applyNumberFormatter(textField);

                // Khi rời ô (người dùng nhập xong) -> parse & cập nhật model 1 lần
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) { // focus lost
                        CTPN_TSPTL_CHTDVT rowItem = getTableRow() != null ? getTableRow().getItem() : null;
                        if (rowItem != null) {
                            String txt = textField.getText();
                            try {
                                double gia = vndFormatter.parseFormattedNumber(txt);
                                if (gia < 0) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Thông báo");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Giá nhập phải lớn hơn hoặc bằng 0");
                                    alert.getButtonTypes().setAll(ButtonType.OK);
                                    alert.showAndWait();
                                } else {
                                    rowItem.getChiTietPhieuNhap().setGiaNhap(gia);
                                    suKienThemMotDongMoiVaoBang();
                                    tblNhapThuoc.refresh();
                                }
                            } catch (NumberFormatException ex) {
                                // revert về giá model cũ
                                Double current = rowItem.getChiTietPhieuNhap().getGiaNhap();
                                textField.setText(current != null ? String.format("%.0f", current) : "0");
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CTPN_TSPTL_CHTDVT rowItem = getTableRow().getItem();
                    Double giaNhap = rowItem.getChiTietPhieuNhap().getGiaNhap();
                    textField.setText(giaNhap != null ? String.format("%.0f", giaNhap) : "0");
                    setGraphic(textField);
                }
            }
        });

        colChietKhau.setCellFactory(column -> new TableCell<CTPN_TSPTL_CHTDVT, Float>() {
            private final TextField textField = new TextField();

            {
                textField.setAlignment(Pos.CENTER_RIGHT);
                textField.setPrefWidth(80);

                // Khi người dùng rời ô nhập → parse giá trị và cập nhật model
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) { // Focus lost
                        CTPN_TSPTL_CHTDVT rowItem = getTableRow() != null ? getTableRow().getItem() : null;
                        if (rowItem != null) {
                            String txt = textField.getText().trim();
                            try {
                                float ck = Float.parseFloat(txt);
                                if (ck < 0 || ck > 100) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Thông báo");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Chiết khấu phải nằm trong khoảng 0 - 100%");
                                    alert.showAndWait();
                                    // reset về giá trị cũ
                                    textField.setText(String.valueOf(rowItem.getChiTietPhieuNhap().getChietKhau()));
                                } else {
                                    rowItem.getChiTietPhieuNhap().setChietKhau(ck);
                                    suKienThemMotDongMoiVaoBang(); // Cập nhật tổng sau khi chỉnh xong
                                    tblNhapThuoc.refresh();
                                }
                            } catch (NumberFormatException ex) {
                                // nếu nhập không hợp lệ → revert
                                float cur = rowItem.getChiTietPhieuNhap().getChietKhau();
                                textField.setText(String.valueOf(cur));
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CTPN_TSPTL_CHTDVT rowItem = getTableRow().getItem();
                    float ck = rowItem.getChiTietPhieuNhap().getChietKhau();
                    textField.setText(String.valueOf(ck));
                    setGraphic(textField);
                }
            }
        });

        colThue.setCellFactory(column -> new TableCell<CTPN_TSPTL_CHTDVT, Float>() {
            private final TextField textField = new TextField();

            {
                textField.setAlignment(Pos.CENTER_RIGHT);
                textField.setPrefWidth(80);

                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) { // Khi focus bị mất
                        CTPN_TSPTL_CHTDVT rowItem = getTableRow() != null ? getTableRow().getItem() : null;
                        if (rowItem != null) {
                            String txt = textField.getText().trim();
                            try {
                                float thue = Float.parseFloat(txt);
                                if (thue < 0 || thue > 100) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Thông báo");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Thuế phải nằm trong khoảng 0 - 100%");
                                    alert.showAndWait();
                                    textField.setText(String.valueOf(rowItem.getChiTietPhieuNhap().getThue()));
                                } else {
                                    rowItem.getChiTietPhieuNhap().setThue(thue);
                                    suKienThemMotDongMoiVaoBang(); // Gọi lại hàm tổng
                                    tblNhapThuoc.refresh();
                                }
                            } catch (NumberFormatException ex) {
                                float cur = rowItem.getChiTietPhieuNhap().getThue();
                                textField.setText(String.valueOf(cur));
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CTPN_TSPTL_CHTDVT rowItem = getTableRow().getItem();
                    float thue = rowItem.getChiTietPhieuNhap().getThue();
                    textField.setText(String.valueOf(thue));
                    setGraphic(textField);
                }
            }
        });

        colThanhTien.setCellValueFactory(cellData -> {
            CTPN_TSPTL_CHTDVT item = cellData.getValue();
            if (item == null || item.getChiTietPhieuNhap() == null) {
                return new SimpleStringProperty("0");
            }

            ChiTietPhieuNhap ctpn = item.getChiTietPhieuNhap();

            double giaNhap = ctpn.getGiaNhap();
            int soLuong = ctpn.getSoLuong();
            float ck = ctpn.getChietKhau();
            float thue = ctpn.getThue();

            // Tính tiền hàng, chiết khấu, thuế và tổng
            double tienHang = giaNhap * soLuong;
            double tienCK = tienHang * ck;
            double tienThue = (tienHang - tienCK) * thue;
            double tong = tienHang - tienCK + tienThue;

            VNDFormatter vndFormatter = new VNDFormatter();
            return new SimpleStringProperty(vndFormatter.format(tong));
        });

//      Gán list vào bảng
        tblNhapThuoc.setItems(listNhapThuoc);
    }


    public void suKienThemChiTietDonViTinhVaoBang() {
        listViewChiTietDonViTinh.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ChiTietDonViTinh chiTietDonViTinh = (ChiTietDonViTinh) newVal;
                txtTimKiemChiTietDonViTinh.clear();
                listViewChiTietDonViTinh.setVisible(false);

                // Kiểm tra nếu DVT đã có trong bảng chưa
                boolean daTonTai = listNhapThuoc.stream().anyMatch(item ->
                        item.getChiTietDonViTinh().getDvt().getMaDVT()
                                .equals(chiTietDonViTinh.getDvt().getMaDVT())
                );

                if (!daTonTai) {
                    // Tạo 3 đối tượng con
                    ChiTietDonViTinh ctdvt = new ChiTietDonViTinh();
                    ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
                    Thuoc_SP_TheoLo tsptl = new Thuoc_SP_TheoLo();

                    // Sinh mã lô mới
                    maLoHienTai++;
                    String maLH = String.format("LH%05d", maLoHienTai);

                    // Gán thông tin vào từng đối tượng
                    ctdvt.setDvt(chiTietDonViTinh.getDvt());
                    ctdvt.setThuoc(chiTietDonViTinh.getThuoc());

                    tsptl.setThuoc(chiTietDonViTinh.getThuoc());
                    tsptl.setMaLH(maLH);

                    ctpn.setThuoc(chiTietDonViTinh.getThuoc());
                    ctpn.setMaLH(maLH);

                    // Gộp vào 1 đối tượng model tổng hợp
                    CTPN_TSPTL_CHTDVT newItem = new CTPN_TSPTL_CHTDVT();
                    newItem.setChiTietDonViTinh(ctdvt);
                    newItem.setChiTietPhieuNhap(ctpn);
                    newItem.setChiTietSP_theoLo(tsptl);

                    // Thêm vào danh sách chính
                    listNhapThuoc.add(newItem);
                    tblNhapThuoc.setItems(listNhapThuoc);
                    tblNhapThuoc.refresh();

                } else {
                    // Thông báo khi đã tồn tại
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Chi tiết đơn vị tính đã tồn tại trong danh sách!");
                    alert.showAndWait();
                }

                Platform.runLater(() -> {
                    listViewChiTietDonViTinh.getSelectionModel().clearSelection();
                    listViewChiTietDonViTinh.refresh();
                });
            }
        });

    }

    public void suKienThemMotDongMoiVaoBang() {
        double tongGiaNhap = 0.0;
        double tongTienChietKhau = 0.0;
        double tongTienThue = 0.0;
        double thanhTien = 0.0;

        for (CTPN_TSPTL_CHTDVT item : listNhapThuoc) {
            if (item == null || item.getChiTietPhieuNhap() == null) continue;

            ChiTietPhieuNhap ctpn = item.getChiTietPhieuNhap();

            double giaNhap = ctpn.getGiaNhap();
            int soLuong = ctpn.getSoLuong();
            float ck = ctpn.getChietKhau();
            float thue = ctpn.getThue();

            // Tính tiền hàng, chiết khấu, thuế và tổng
            double tienHang = giaNhap * soLuong;
            double tienCK = tienHang * ck;
            double tienThue = (tienHang - tienCK) * thue;
            double tong = tienHang - tienCK + tienThue;

            tongGiaNhap += tienHang;
            tongTienChietKhau += tienCK;
            tongTienThue += tienThue;
            thanhTien += tong;
        }

        // Dùng VNDFormatter của bạn để hiển thị tiền theo định dạng
        VNDFormatter vndFormatter = new VNDFormatter();

        txtTongGiaNhap.setText(vndFormatter.format(tongGiaNhap));
        txtTongTienChietKhau.setText(vndFormatter.format(tongTienChietKhau));
        txtTongTienThue.setText(vndFormatter.format(tongTienThue));
        txtThanhTien.setText(vndFormatter.format(thanhTien));
    }

    public void btnLuu(MouseEvent mouseEvent) {
        if (listNhapThuoc.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng thêm thuốc vào phiếu nhập trước khi lưu!");
            alert.showAndWait();
            return;
        }

        if (ncc == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn nhà cung cấp trước khi lưu!");
            alert.showAndWait();
            return;
        }

        for (CTPN_TSPTL_CHTDVT item : listNhapThuoc) {
            if (item.getChiTietSP_theoLo().getNsx() == null || item.getChiTietSP_theoLo().getHsd() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập ngày sản xuất và hạn sử dụng cho: " + item.getChiTietDonViTinh().getThuoc().getMaThuoc());
                alert.showAndWait();
                return;
            }
            if (item.getChiTietPhieuNhap().getSoLuong() == 0 || item.getChiTietPhieuNhap().getSoLuong() <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng nhập của thuốc:" + item.getChiTietDonViTinh().getThuoc().getMaThuoc() + " phải lớn hơn 0!");
                alert.showAndWait();
                return;
            }
            if (item.getChiTietPhieuNhap().getGiaNhap() == 0 || item.getChiTietPhieuNhap().getGiaNhap() < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Giá nhập của thuốc:" + item.getChiTietDonViTinh().getThuoc().getMaThuoc() + " phải lớn hơn hoặc bằng 0!");
                alert.showAndWait();
                return;
            }
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận lưu");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn lưu phiếu nhập này không?");

        Optional<ButtonType> resultConfirm = confirm.showAndWait();

        if (resultConfirm.isEmpty() || resultConfirm.get() != ButtonType.OK) {
            return;
        }
        try {
            PhieuNhap phieuNhap = new PhieuNhap();
            phieuNhap.setNhaCungCap(ncc);
            phieuNhap.setNgayNhap(
                    txtNgayNhap.getEditor().getText().isEmpty()
                            ? Date.valueOf(LocalDate.now())
                            : Date.valueOf(txtNgayNhap.getValue())
            );

            String maPN = txtMaPhieuNhap.getText().trim();

            for (PhieuNhap pn : allPhieuNhaps) {
                if (pn.getMaPN().equals(maPN)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Mã phiếu nhập đã tồn tại. Vui lòng nhập mã khác!");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }

            if (maPN.isEmpty()) {
                maPN = phieuNhapDao.generateMaPN();
            }

            phieuNhap.setMaPN(maPN);
            phieuNhap.setGhiChu(txtGhiChu.getText());
            phieuNhap.setTrangThai(true);

            // Nhân viên tạm thời
            NhanVien nvTemp = new NhanVien_Dao().selectById("NV001");
            phieuNhap.setNhanVien(nvTemp);

            // 🧾 Lưu từng chi tiết phiếu nhập
            boolean allSuccess = true;
            for (CTPN_TSPTL_CHTDVT itemTemp : listNhapThuoc) {
                ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
                ctpn.setPhieuNhap(phieuNhap);
                ctpn.setThuoc(itemTemp.getChiTietPhieuNhap().getThuoc());
                ctpn.setMaLH(itemTemp.getChiTietSP_theoLo().getMaLH());
                ctpn.setSoLuong(itemTemp.getChiTietPhieuNhap().getSoLuong());
                ctpn.setGiaNhap(itemTemp.getChiTietPhieuNhap().getGiaNhap());
                ctpn.setChietKhau(itemTemp.getChiTietPhieuNhap().getChietKhau());
                ctpn.setThue(itemTemp.getChiTietPhieuNhap().getThue());

                Thuoc_SP_TheoLo lo = new Thuoc_SP_TheoLo();
                lo.setThuoc(itemTemp.getChiTietSP_theoLo().getThuoc());
                lo.setMaLH(itemTemp.getChiTietSP_theoLo().getMaLH());
                lo.setSoLuongTon(itemTemp.getChiTietPhieuNhap().getSoLuong());
                lo.setNsx(itemTemp.getChiTietSP_theoLo().getNsx());
                lo.setHsd(itemTemp.getChiTietSP_theoLo().getHsd());

                String maDVT = itemTemp.getChiTietDonViTinh().getDvt().getMaDVT();

                // ⚙️ Gọi DAO để lưu
                boolean result = phieuNhapDao.luuPhieuNhap(phieuNhap, ctpn, lo, maDVT);
                if (!result) {
                    allSuccess = false;
                }
            }

            if (allSuccess) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Lưu phiếu nhập thành công!");
                alert.showAndWait();
                clearAll();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Một số chi tiết không được lưu thành công!");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Đã xảy ra lỗi khi lưu phiếu nhập!");
            alert.showAndWait();
        }
    }


    private void clearAll() {
        txtMaPhieuNhap.clear();
        txtGhiChu.clear();
        cbxNCC.getEditor().clear();
        txtNgayNhap.setValue(LocalDate.now());
        listNhapThuoc.clear();
        tblNhapThuoc.refresh();
        txtNgayNhap.getEditor().clear();
        suKienThemMotDongMoiVaoBang();
    }

    public void btnHuy(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận hủy");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn hủy phiếu nhập này không? Tất cả dữ liệu sẽ bị xóa!");

        // Hiển thị hộp thoại và chờ người dùng chọn
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            clearAll();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Đã hủy");
            info.setHeaderText(null);
            info.setContentText("Phiếu nhập đã được hủy.");
            info.showAndWait();
        }
    }
}
