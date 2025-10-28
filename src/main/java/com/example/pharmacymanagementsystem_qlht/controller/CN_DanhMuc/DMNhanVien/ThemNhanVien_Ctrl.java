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

public class ThemNhanVien_Ctrl{
    public TextField txtTenNV;
    public TextField txtSDT;
    public TextField txtEmail;
    public ComboBox cbxGioiTinh;
    public TextField txtDiaChi;
    public DatePicker txtNgaySinh;
    private NhanVien nhanVien;
    public DanhMucNhanVien_Ctrl danhMucNhanVien_Ctrl;

    public void initialize() {
        cbxGioiTinh.getItems().addAll("Chọn giới tính","Nam", "Nữ");
        cbxGioiTinh.getSelectionModel().selectFirst();
        nhanVien = new NhanVien();
    }

    public void btnThemTaiKhoan(ActionEvent actionEvent) {
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
            dialog.setTitle("Thêm tài khoản nhân viên");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnThem(ActionEvent actionEvent) {
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

        if(!KiemTraHopLe()){
            Platform.runLater(() -> root.getChildren().remove(overlay));
            return;
        }

        // Tạo luồng riêng để xử lý cập nhật (tránh lag UI)
        new Thread(() -> {
            try {
                // 👉 Code xử lý lâu (ví dụ: cập nhật CSDL)
                NhanVien nv = new NhanVien();
                nv.setTenNV(txtTenNV.getText());
                nv.setSdt(txtSDT.getText());
                nv.setEmail(txtEmail.getText());
                nv.setNgaySinh(java.sql.Date.valueOf(txtNgaySinh.getValue()));
                nv.setGioiTinh("Nữ".equals(cbxGioiTinh.getValue().toString()));
                nv.setDiaChi(txtDiaChi.getText());
                nv.setTrangThai(true);
                nv.setNgayVaoLam(Date.valueOf(LocalDate.now()));
                nv.setNgayNghiLam(null);
                nv.setTaiKhoan(nhanVien.getTaiKhoan());
                nv.setMatKhau(nhanVien.getMatKhau());

                NhanVien_Dao nhanViendao = new NhanVien_Dao();
                nhanViendao.insertNhanVienProc(nv);

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
        Stage stage = (Stage) txtTenNV.getScene().getWindow();
        stage.close();
    }

    private boolean KiemTraHopLe() {
        if(txtTenNV.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Tên nhân viên không được để trống!");
            alert.showAndWait();
            return false;
        }else if(txtSDT.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Số điện thoại không được để trống!");
            alert.showAndWait();
            return false;
        }else if(txtNgaySinh.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Ngày sinh không được để trống!");
            alert.showAndWait();
            return false;
        }else if(cbxGioiTinh.getSelectionModel().getSelectedIndex() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn giới tính!");
            alert.showAndWait();
            return false;
        }else if(nhanVien.getTaiKhoan() == null||nhanVien.getTaiKhoan().isEmpty()||nhanVien.getMatKhau() == null||nhanVien.getMatKhau().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng thêm tài khoản cho nhân viên!");
            alert.showAndWait();
            return false;
        }else if(!txtSDT.getText().matches("0\\d{9}")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số.");
            alert.showAndWait();
            return false;
        }else if(!txtEmail.getText().isEmpty() && !txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Email không hợp lệ!");
            alert.showAndWait();
            return false;
        }else {
            return true;
        }
    }
}
