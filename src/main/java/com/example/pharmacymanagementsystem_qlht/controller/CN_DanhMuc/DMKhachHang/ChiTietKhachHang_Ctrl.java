package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang;

import com.example.pharmacymanagementsystem_qlht.dao.KhachHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ChiTietKhachHang_Ctrl extends Application {

    @FXML
    private Button btnLuu;

    @FXML
    private Button btnHuy;

    @FXML
    private Button btnXoa;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtNgaySinh;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTenKH;

    @FXML
    private ComboBox<String> cboGioiTinh;

    @FXML
    private Label errTenKH, errDiaChi, errEmail, errSDT;

    private final KhachHang_Dao khachHangDao = new KhachHang_Dao();
    private KhachHang khachHang;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/SuaXoaKhachHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ChiTietKhachHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        cboGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        btnHuy.setOnAction(e -> HuyClick());
        btnXoa.setOnAction(e -> XoaClick());
        btnLuu.setOnAction(e -> LuuClick());
    }


    private boolean validateFields() {
        boolean isValid = true;

        // Reset lỗi
        errTenKH.setText("");
        errSDT.setText("");
        errEmail.setText("");
        errDiaChi.setText("");

        // Tên KH
        if (txtTenKH.getText().trim().isEmpty()) {
            errTenKH.setText("Tên khách hàng không được để trống.");
            isValid = false;
        }

        // Số điện thoại
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            errSDT.setText("Số điện thoại không được để trống.");
            isValid = false;
        } else if (!sdt.matches("\\d{10}")) {
            errSDT.setText("Số điện thoại không hợp lệ (10số).");
            isValid = false;
        }

        // Email
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            errEmail.setText("Email không được để trống.");
            isValid = false;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errEmail.setText("Email không hợp lệ.");
            isValid = false;
        }

        // Địa chỉ
        if (txtDiaChi.getText().trim().isEmpty()) {
            errDiaChi.setText("Địa chỉ không được để trống.");
            isValid = false;
        }

        return isValid;
    }


    @FXML
    private void LuuClick() {
        try {
            // Kiểm tra dữ liệu trước khi lưu
            if (!validateFields()) {
                return;
            }

            if (khachHang == null) {
                khachHang = new KhachHang();
            }

            khachHang.setTenKH(txtTenKH.getText().trim());
            khachHang.setDiaChi(txtDiaChi.getText().trim());
            khachHang.setEmail(txtEmail.getText().trim());
            khachHang.setSdt(txtSDT.getText().trim());
            khachHang.setNgaySinh(txtNgaySinh.getValue() != null ? txtNgaySinh.getValue() : LocalDate.now());
            khachHang.setGioiTinh("Nam".equals(cboGioiTinh.getValue()));
            khachHang.setTrangThai(true);

            boolean success;
            if (khachHang.getMaKH() == null || khachHang.getMaKH().trim().isEmpty()) {
                success = khachHangDao.insert(khachHang);
            } else {
                success = khachHangDao.update(khachHang);
            }

            if (success) {
                thongBao("Lưu thành công!");
                dongCuaSo();
            } else {
                thongBao("Lưu thất bại!");
            }
        } catch (Exception e) {
            thongBao("Lỗi khi lưu: " + e.getMessage());
        }
    }


    @FXML
    private void XoaClick() {
        if (khachHang == null || khachHang.getMaKH() == null) {
            thongBao("Chưa chọn khách hàng để xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc muốn xóa khách hàng này?");
        alert.setContentText("Khách hàng sẽ được ẩn (trạng thái = 0).");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean result = khachHangDao.deleteById(khachHang.getMaKH());
                if (result) {
                    thongBao("Đã xóa khách hàng!");
                    dongCuaSo();
                } else {
                    thongBao("Xóa thất bại!");
                }
            }
        });
    }


    @FXML
    private void HuyClick() {
        dongCuaSo();
    }


    public void hienThiThongTin(KhachHang kh) {
        if (kh != null) {
            khachHang = kh;
            txtTenKH.setText(kh.getTenKH());
            txtDiaChi.setText(kh.getDiaChi() != null ? kh.getDiaChi() : "");
            txtEmail.setText(kh.getEmail() != null ? kh.getEmail() : "");
            txtSDT.setText(kh.getSdt() != null ? kh.getSdt() : "");
            txtNgaySinh.setValue(kh.getNgaySinh());
            cboGioiTinh.setValue(Boolean.TRUE.equals(kh.getGioiTinh()) ? "Nam" : "Nữ");
        }
    }


    private void thongBao(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void dongCuaSo() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }
}
