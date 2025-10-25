package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuNhapHang;

import com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatGia.ThietLapDonViTinh_SuaXoa_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatGia.ThietLapDonViTinh_Them_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc.ThemThuoc_Ctrl;
import com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap.ChiTietNhaCungCap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.ChiTietDonViTinh_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietDonViTinh;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.util.Callback;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ThemThuoc_LapPhieuNhapHang_Ctrl{

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)

    @FXML
    private TextField tfMaThuoc;
    @FXML
    private TextField tfTenThuoc;
    @FXML
    private TextField tfLoaiHang;

    @FXML
    private TableView<ChiTietDonViTinh> tbDVT;
    @FXML
    private TableColumn<ChiTietDonViTinh, String> colDVT;
    @FXML
    private TableColumn<ChiTietDonViTinh, String> colKH;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colHeSo;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colGiaNhap;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colGiaBan;
    @FXML
    private TableColumn<ChiTietDonViTinh, Object> colDVCB;
    @FXML
    private TableColumn<ChiTietDonViTinh, Void> colXoa;
    public Button btnLuu;
    public Button btnHuy;
    private Thuoc_SanPham thuoc;
    private ObservableList<ChiTietDonViTinh> listGia = FXCollections.observableArrayList();
    private final ChiTietDonViTinh_Dao ctDVTDao = new ChiTietDonViTinh_Dao();
    private LapPhieuNhapHang_Ctrl parentCtrl;

    // 2. KHỞI TẠO (INITIALIZE)

    @FXML
    public void initialize() {
        colDVT.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDvt().getTenDonViTinh()));
        colKH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDvt().getKiHieu()));
        colHeSo.setCellValueFactory(new PropertyValueFactory<>("heSoQuyDoi"));
        colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colDVCB.setCellValueFactory(new PropertyValueFactory<>("donViCoBan"));

        addDetailButtonToTable();

        tbDVT.setItems(listGia);
    }

    public void setParentCtrl(LapPhieuNhapHang_Ctrl parentCtrl) {
        this.parentCtrl = parentCtrl;
    }

    // Set thuốc lên tf và load bảng giá
    public void setThuoc(Thuoc_SanPham thuoc) {
        this.thuoc = thuoc;
        tfMaThuoc.setText(thuoc.getMaThuoc());
        tfTenThuoc.setText(thuoc.getTenThuoc());
        loadListGia(this.thuoc.getMaThuoc());
    }

    //Hàm thêm nút chi tiết vào bảng giá
    private void addDetailButtonToTable() {
        Callback<TableColumn<ChiTietDonViTinh, Void>, TableCell<ChiTietDonViTinh, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ChiTietDonViTinh, Void> call(final TableColumn<ChiTietDonViTinh, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Chi tiết");

                    {
                        btn.setOnAction(event -> {
                            int row = getIndex();
                            ChiTietDonViTinh ct = getTableView().getItems().get(row);
                            if (ct == null || thuoc == null) return;

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                                        "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/ThietLapDonViTinh_SuaXoa_GUI.fxml"));
                                Parent root = loader.load();
                                ThietLapDonViTinh_SuaXoa_Ctrl ctrl = loader.getController();

                                // Sử dụng setContext để thiết lập callback và thông tin cần thiết
                                ctrl.setContext(
                                        thuoc.getMaThuoc(),
                                        updated -> {
                                            listGia.set(row, updated);
                                            tbDVT.refresh();
                                        },
                                        deleted -> {
                                            listGia.remove(ct);
                                            tbDVT.refresh();
                                        }
                                );

                                ctrl.setCtdvt(ct);

                                Stage dialog = new Stage();
                                dialog.initOwner(tbDVT.getScene().getWindow());
                                //Set modality để chặn tương tác với cửa sổ cha, có thể dùng APPPLICATION_MODAL để chặn tất cả cửa sổ khác
                                dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
                                dialog.setScene(new Scene(root));
                                dialog.setTitle("Chi tiết đơn vị tính");
                                dialog.showAndWait();
                            } catch (Exception e) {
                                new Alert(Alert.AlertType.ERROR, "Không mở được cửa sổ chi tiết.").showAndWait();
                            }
                        });
                        btn.getStyleClass().add("btnChiTiet");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
        colXoa.setCellFactory(cellFactory);
    }

    // load danh sách giá từ qua ctdvt dao
    public void loadListGia(String maThuoc) {
        List<ChiTietDonViTinh> loaded = ctDVTDao.selectByMaThuoc(maThuoc);
        listGia.setAll(loaded);
    }

    public void btnThietLapGiaClick() {
        if (thuoc == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/ThietLapDonViTinh_Them_GUI.fxml"));
            Parent root = loader.load();
            ThietLapDonViTinh_Them_Ctrl ctrl = loader.getController();

            // Sử dụng setContext để thiết lập callback lấy về chi tiết đơn vị tính vừa thêm và cập nhật table
            ctrl.setContext(thuoc.getMaThuoc(), newItem -> {
                listGia.add(newItem);
                tbDVT.refresh();
            });

            Stage dialog = new Stage();
            dialog.initOwner(tbDVT.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thiết lập đơn vị tính");
            dialog.showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Không mở được cửa sổ thiết lập đơn vị tính.").showAndWait();
        }
    }

    public void btnLuuClick() {
        if (thuoc == null) {
            new Alert(Alert.AlertType.WARNING, "Chưa chọn thuốc.").showAndWait();
            return;
        }
        if(!donViCoBanCheck()) {
            return;
        }
        try {
            // setThuoc đang chọn cho tất cả object chi tiết dvt trong listGia
            listGia.forEach(ct -> ct.setThuoc(thuoc));

            //Lấy danh sách chi tiết đơn vị tính hiện có trong database
            List<ChiTietDonViTinh> existing = ctDVTDao.selectByMaThuoc(thuoc.getMaThuoc());
            //Tạo các khóa đã có và hiện tại để so sánh
            Set<String> existingKeys = existing.stream().map(this::keyOf).collect(Collectors.toSet());
            Set<String> currentKeys  = listGia.stream().map(this::keyOf).collect(Collectors.toSet());

            //Kiểm tra và thực hiện insert hoặc update
            for (ChiTietDonViTinh ct : listGia) {
                String key = keyOf(ct);
                if (existingKeys.contains(key)) {
                    ctDVTDao.update(ct);
                } else {
                    ctDVTDao.insert(ct);
                }
            }
            //Xoá các chi tiết đơn vị tính đã bị xoá khỏi danh sách hiện tại
            for (ChiTietDonViTinh ex : existing) {
                if (!currentKeys.contains(keyOf(ex))) {
                    ctDVTDao.deleteById(ex.getThuoc().getMaThuoc(), ex.getDvt().getMaDVT());
                }
            }

            loadListGia(this.thuoc.getMaThuoc());
            tbDVT.refresh();
            new Alert(Alert.AlertType.INFORMATION, "Lưu thành công.").showAndWait();
            if (parentCtrl != null) {
                parentCtrl.timKiemDonViTinh();
            }
            Stage stage = (Stage) btnLuu.getScene().getWindow();
            stage.close();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Lỗi khi lưu dữ liệu: " + ex.getMessage()).showAndWait();
        }
    }
    private boolean donViCoBanCheck() {
        long baseCount = listGia.stream().filter(ChiTietDonViTinh::isDonViCoBan).count();

        if (baseCount == 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Chưa có đơn vị cơ bản. Vui lòng chọn 1 đơn vị làm cơ bản (DVCB).").showAndWait();
            return false;
        }
        if (baseCount > 1) {
            new Alert(Alert.AlertType.WARNING,
                    "Chỉ được có đúng 1 đơn vị cơ bản. Vui lòng bỏ chọn DVCB ở các đơn vị còn lại.").showAndWait();
            return false;
        }
        return true;
    }

    private String keyOf(ChiTietDonViTinh ct) {
        return (ct.getThuoc() != null ? ct.getThuoc().getMaThuoc() : "") + "#" +
                (ct.getDvt() != null ? ct.getDvt().getMaDVT() : "");
    }

    public void btnHuyClick() {
        Stage stage = (Stage) tfMaThuoc.getScene().getWindow();
        stage.close();
    }


    public void btnThemThuocClick(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/ThemThuoc_GUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            ThemThuoc_Ctrl ctrl = loader.getController();
            if (ctrl.getThuocThem() != null) {
                setThuoc(ctrl.getThuocThem());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}