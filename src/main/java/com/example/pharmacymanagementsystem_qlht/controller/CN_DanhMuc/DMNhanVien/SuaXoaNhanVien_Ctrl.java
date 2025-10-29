package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhanVien;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.LuongNhanVien;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class SuaXoaNhanVien_Ctrl {

    public TextField txtMaNV;
    public TextField txtTenNV;
    public TextField txtSDT;
    public TextField txtEmail;
    public ComboBox cbxGioiTinh;
    public TextField txtDiaChi;
    public TextField txtNgayBD;
    public TextField txtNgayKT;
    public ComboBox cbxTrangThai;
    public DatePicker txtNgaySinh;
    public TableView<LuongNhanVien> tblLuongNV;
    public TableColumn<LuongNhanVien,String> colMaLuong;
    public TableColumn<LuongNhanVien,String> colTuNgay;
    public TableColumn<LuongNhanVien,String> colDenNgay;
    public TableColumn<LuongNhanVien,Double> colLuongCoBan;
    public TableColumn<LuongNhanVien,Double> colPhuCap;
    public TableColumn<LuongNhanVien,String> colGhiChu;
    private List<LuongNhanVien> listLuong;
    private NhanVien nhanVien;
    public DanhMucNhanVien_Ctrl danhMucNhanVien_Ctrl;

    public void initialize(NhanVien nhanVien) {
        cbxGioiTinh.getItems().addAll("Nam", "Nữ");
        cbxTrangThai.getItems().addAll("Đang làm việc", "Đã nghỉ việc");
        loadDataLuongNhanVien(nhanVien);
        this.nhanVien = nhanVien;

    }

    public void loadDataLuongNhanVien(NhanVien nhanVien) {
        txtMaNV.setText(nhanVien.getMaNV());
        txtTenNV.setText(nhanVien.getTenNV());
        txtSDT.setText(nhanVien.getSdt());
        txtEmail.setText(nhanVien.getEmail());
        txtNgaySinh.setValue(nhanVien.getNgaySinh().toLocalDate());
        cbxGioiTinh.setValue(nhanVien.isGioiTinh()? "Nữ" : "Nam");
        txtDiaChi.setText(nhanVien.getDiaChi());
        cbxTrangThai.setValue(nhanVien.isTrangThai()? "Đang làm việc" : "Đã nghỉ việc");
        txtNgaySinh.setValue(nhanVien.getNgaySinh().toLocalDate());
        txtNgayBD.setText(nhanVien.getNgayVaoLam().toLocalDate().toString());
        if (nhanVien.getNgayNghiLam() != null) {
            txtNgayKT.setText(nhanVien.getNgayNghiLam().toLocalDate().toString());
        } else {
            txtNgayKT.setText("Đang làm việc");
        }

        listLuong = new LuongNhanVien_Dao().selectByMaNV(nhanVien.getMaNV());
        ObservableList<LuongNhanVien> dataLuong = javafx.collections.FXCollections.observableList(listLuong);

        colMaLuong.setCellValueFactory(new PropertyValueFactory<>("maLNV"));
        colTuNgay.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        colDenNgay.setCellValueFactory(cellData -> {
            Date denNgaySql = cellData.getValue().getDenNgay();
            String text;

            if (denNgaySql == null) {
                text = "Đang áp dụng";
            } else {
                LocalDate denNgay = ((java.sql.Date) denNgaySql).toLocalDate();
                text = denNgay.toString();
            }

            return new SimpleStringProperty(text);
        });

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

//      Cột lương cơ bản
        colLuongCoBan.setCellValueFactory(new PropertyValueFactory<>("luongCoBan"));
        colLuongCoBan.setCellFactory(column -> new TableCell<LuongNhanVien, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item) + " ₫");
                }
            }
        });

//      Cột phụ cấp
        colPhuCap.setCellValueFactory(new PropertyValueFactory<>("phuCap"));
        colPhuCap.setCellFactory(column -> new TableCell<LuongNhanVien, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item) + " ₫");
                }
            }
        });
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        tblLuongNV.setItems(dataLuong);
    }

    public void btnSuaTaiKhoan(ActionEvent actionEvent) {
        try {
            Stage dialog = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/SuataiKhoan_GUI.fxml"));
            Parent root = loader.load();

            SuaTaiKhoan_Ctrl ctrl = loader.getController();
            ctrl.initialize(nhanVien);

            if(ctrl.isSaved){
                NhanVien updatedNV = ctrl.getUpdatedNhanVien();
                if (updatedNV==null) return;
                nhanVien.setTaiKhoan(updatedNV.getTaiKhoan());
                nhanVien.setMatKhau(updatedNV.getMatKhau());
            }


            dialog.initOwner(txtDiaChi.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Sửa tài khoản nhân viên");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnThayDoiLuong(ActionEvent actionEvent) {
        try {
            Stage dialog = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/ThietLapLuongNV_GUI.fxml"));
            Parent root = loader.load();

            ThietLapLuongNV_Ctrl ctrl = loader.getController();
            LuongNhanVien luongNhanVienTemp = new LuongNhanVien();
            for(LuongNhanVien lnv : listLuong){
                if(lnv.getDenNgay()==null){
                    luongNhanVienTemp = new LuongNhanVien(
                            lnv.getMaLNV(),
                            lnv.getTuNgay(),
                            lnv.getDenNgay(),
                            lnv.getLuongCoBan(),
                            lnv.getPhuCap(),
                            lnv.getGhiChu(),
                            lnv.getNhanVien()
                    );
                    break;
                }
            }
            ctrl.initialize(nhanVien, luongNhanVienTemp);

            dialog.setOnHidden(event -> {
                if(!ctrl.isSaved){
                    return;
                }
                LuongNhanVien updatedLuongNV = ctrl.luongNhanVien;
                if (updatedLuongNV==null) return;
                LuongNhanVien_Dao dao = new LuongNhanVien_Dao();
                String maLNV = dao.getNewMaLNV();
                for(LuongNhanVien lnv : listLuong){
                    if(lnv.getDenNgay()==null){
                        lnv.setDenNgay(Date.valueOf(LocalDate.now()));
                            break;
                    }
                }
                listLuong.add(new LuongNhanVien(maLNV, Date.valueOf(LocalDate.now().plusDays(1)), null, updatedLuongNV.getLuongCoBan(), updatedLuongNV.getPhuCap(), updatedLuongNV.getGhiChu(), nhanVien));
//              Cập nhật lại TableView
                ObservableList<LuongNhanVien> updatedDataLuong = FXCollections.observableArrayList(listLuong);
                tblLuongNV.setItems(updatedDataLuong);
                tblLuongNV.refresh();
            });

            dialog.initOwner(txtDiaChi.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thay đổi lương nhân viên");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnLuu(ActionEvent actionEvent) {
        // Lấy root hiện tại
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();

        // Tạo overlay làm mờ nền
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        ProgressIndicator progress = new ProgressIndicator();
        overlay.getChildren().add(progress);

        // Căn overlay phủ toàn màn hình
        AnchorPane.setTopAnchor(overlay, 0.0);
        AnchorPane.setRightAnchor(overlay, 0.0);
        AnchorPane.setBottomAnchor(overlay, 0.0);
        AnchorPane.setLeftAnchor(overlay, 0.0);

        // Thêm overlay vào AnchorPane
        root.getChildren().add(overlay);

        // Tạo luồng riêng để xử lý cập nhật (tránh lag UI)
        new Thread(() -> {
            try {
                // 👉 Code xử lý lâu (ví dụ: cập nhật CSDL)
                NhanVien nv = new NhanVien();
                nv.setMaNV(txtMaNV.getText());
                nv.setTenNV(txtTenNV.getText());
                nv.setSdt(txtSDT.getText());
                nv.setEmail(txtEmail.getText());
                nv.setNgaySinh(java.sql.Date.valueOf(txtNgaySinh.getValue()));
                nv.setGioiTinh("Nữ".equals(cbxGioiTinh.getValue().toString()));
                nv.setDiaChi(txtDiaChi.getText());

                String trangThaiStr = cbxTrangThai.getValue().toString();
                boolean isDangLam = trangThaiStr.equals("Đang làm việc");

                nv.setNgayVaoLam(nhanVien.getNgayVaoLam());
                nv.setNgayNghiLam(nhanVien.getNgayNghiLam());
                nv.setTaiKhoan(nhanVien.getTaiKhoan());
                nv.setMatKhau(nhanVien.getMatKhau());

                if (!nhanVien.isTrangThai() && isDangLam) {
                    nv.setNgayVaoLam(Date.valueOf(LocalDate.now()));
                    nv.setNgayNghiLam(null);
                } else if (nhanVien.isTrangThai() && !isDangLam) {
                    nv.setNgayNghiLam(Date.valueOf(LocalDate.now()));
                }

                nv.setTrangThai(isDangLam);

                nhanVien = nv;

                NhanVien_Dao nhanViendao = new NhanVien_Dao();
                nhanViendao.update(nv);

                LuongNhanVien_Dao luongNhanViendao = new LuongNhanVien_Dao();
                for (LuongNhanVien lnv : listLuong) {
                    if (luongNhanViendao.selectById(lnv.getMaLNV()) == null) {
                        luongNhanViendao.insert(lnv);
                    } else {
                        luongNhanViendao.update(lnv);
                    }
                }
                Platform.runLater(() -> {
                    root.getChildren().remove(overlay);
                    danhMucNhanVien_Ctrl.loadData();
                    dong();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> root.getChildren().remove(overlay));
            }
        }).start();
    }

    public void setParent(DanhMucNhanVien_Ctrl parent) {
        danhMucNhanVien_Ctrl = parent;
    }

    private void dong(){
        Stage stage = (Stage) txtMaNV.getScene().getWindow();
        stage.close();
    }


    public void btnXoa(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc chắn muốn xóa nhân viên này không?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return; // người dùng chọn Hủy
        }

        // Hiển thị overlay loading (tuỳ chọn)
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        ProgressIndicator progress = new ProgressIndicator();
        overlay.getChildren().add(progress);
        AnchorPane.setTopAnchor(overlay, 0.0);
        AnchorPane.setRightAnchor(overlay, 0.0);
        AnchorPane.setBottomAnchor(overlay, 0.0);
        AnchorPane.setLeftAnchor(overlay, 0.0);
        root.getChildren().add(overlay);

        // Xử lý trong thread riêng
        new Thread(() -> {
            try {
                NhanVien_Dao nhanVien_dao = new NhanVien_Dao();
                nhanVien.setTrangThaiXoa(true);
                boolean success = nhanVien_dao.update(nhanVien);

                Platform.runLater(() -> {
                    root.getChildren().remove(overlay); // gỡ overlay

                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText(null);
                        alert.setContentText("Xóa nhân viên thành công!");
                        alert.showAndWait();

                        danhMucNhanVien_Ctrl.loadData();
                        dong();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Xóa nhân viên thất bại!");
                        alert.showAndWait();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    root.getChildren().remove(overlay);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Đã xảy ra lỗi khi xóa nhân viên!");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    public void btnHuy(ActionEvent actionEvent) {
        nhanVien=null;
        dong();
    }

}
