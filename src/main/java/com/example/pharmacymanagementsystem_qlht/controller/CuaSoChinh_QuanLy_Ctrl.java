package com.example.pharmacymanagementsystem_qlht.controller;

import com.example.pharmacymanagementsystem_qlht.TienIch.VNDFormatter;
import com.example.pharmacymanagementsystem_qlht.dao.ThongKe_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SP_TheoLo_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CuaSoChinh_QuanLy_Ctrl{
    public Pane pnlChung;
    public Menu menuTimKiem;
    public Menu menuDanhMuc;
    public Menu menuCapNhat;
    public Menu menuThongKe;
    public Menu menuXuLy;
    public Label txtNguoiDung;
    public Label txtNgayThangNam;
    public TableView<Thuoc_SP_TheoLo> tblThuocHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaThuocHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colLoHangHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colHSDHetHan;
    public TableView<Thuoc_SP_TheoLo> tblThuocSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaThuocSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colLoHangSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colHSDSapHetHan;
    public Label lbl_SoLuongHangHetHan;
    public Label lbl_SoLuongHangSapHetHan;
    public LineChart chartDoanhThuThangNay;
    public Label lblDoanhThuThangTruoc;
    public Label lblDoanhThuThangNay;
    public Label lblHoaDonThangTruoc;
    public Label lblHoaDonThangNay;
    public Pane pnlThongTin;
    public Pane pnlNguoiDung;
    public TextField txtVaiTroNhanVien;
    public Label lblVaiTro;
    private int viTri;
    private List<Thuoc_SP_TheoLo> listThuocHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangDaHetHan();
    private List<Thuoc_SP_TheoLo> listThuocSapHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangSapHetHan();

    public void initialize(){
        txtNguoiDung.setText("Người dùng: "+ DangNhap_Ctrl.user.getTenNV());
        setNgayGio(txtNgayThangNam);
        loadTableThuocHetHan();
        loadTableThuocSapHetHan();
        setThongKeLabelsAndData();
        setupGlobalShortcuts();
        pnlThongTin.setVisible(false);
    }

    private void loadView(int menuIndex, String fxmlPath) {
        viTri = menuIndex;
        selectMenu(viTri);
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Không thể tải FXML: " + fxmlPath);
        }
        pnlChung.requestFocus();
        pnlThongTin.setVisible(false);

    }

    public void loadTableThuocHetHan(){
        ObservableList<Thuoc_SP_TheoLo> data = tblThuocHetHan.getItems();
        data.clear();
        data.addAll(listThuocHetHan);
        lbl_SoLuongHangHetHan.setText("Số lượng hàng hết hạn: " +listThuocHetHan.size());
        colMaThuocHetHan.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colLoHangHetHan.setCellValueFactory(new PropertyValueFactory<>("maLH"));
        colHSDSapHetHan.setCellValueFactory(new PropertyValueFactory<>("hsd"));
    }

    public void loadTableThuocSapHetHan(){
        ObservableList<Thuoc_SP_TheoLo> data = tblThuocSapHetHan.getItems();
        data.clear();
        data.addAll(listThuocSapHetHan);
        lbl_SoLuongHangSapHetHan.setText("Số lượng hàng sắp hết hạn: " +listThuocSapHetHan.size());
        colLoHangSapHetHan.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colLoHangSapHetHan.setCellValueFactory(new PropertyValueFactory<>("maLH"));
        colLoHangSapHetHan.setCellValueFactory(new PropertyValueFactory<>("hsd"));
    }

    private void setNgayGio(Label lblNgayGio) {
        Locale localeVN = new Locale("vi", "VN");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy HH:mm:ss", localeVN);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> {
                    LocalDateTime now = LocalDateTime.now();
                    lblNgayGio.setText(now.format(formatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setThongKeLabelsAndData() {
        // Safety: ensure FXML controls are injected
        if (lblHoaDonThangNay == null || lblHoaDonThangTruoc == null
                || lblDoanhThuThangNay == null || lblDoanhThuThangTruoc == null
                || chartDoanhThuThangNay == null) {
            return;
        }

        ThongKe_Dao tkDao = new ThongKe_Dao();
        LocalDate now = LocalDate.now();

        LocalDate startThis = now.withDayOfMonth(1);
        LocalDate endThis = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate startPrev = startThis.minusMonths(1);
        LocalDate endPrev = startThis.minusDays(1);

        // Get per-day (or per-period) entries from DAO for the ranges
        List<ThongKeBanHang> dataThis = tkDao.getThongKeBanHang_TuyChon(startThis, endThis);
        List<ThongKeBanHang> dataPrev = tkDao.getThongKeBanHang_TuyChon(startPrev, endPrev);

        // Aggregate totals
        int invoicesThis = dataThis.stream().mapToInt(ThongKeBanHang::getSoLuongHoaDon).sum();
        double revenueThis = dataThis.stream().mapToDouble(ThongKeBanHang::getDoanhThu).sum();

        int invoicesPrev = dataPrev.stream().mapToInt(ThongKeBanHang::getSoLuongHoaDon).sum();
        double revenuePrev = dataPrev.stream().mapToDouble(ThongKeBanHang::getDoanhThu).sum();

        // Format numbers (thousands separator)
        DecimalFormat df = new DecimalFormat("#,###");

        lblHoaDonThangNay.setText(invoicesThis+" Hóa đơn");
        lblHoaDonThangTruoc.setText(invoicesPrev + " Hóa đơn");
        VNDFormatter vndFormatter = new VNDFormatter();
        lblDoanhThuThangNay.setText(vndFormatter.format(revenueThis));
        lblDoanhThuThangTruoc.setText(vndFormatter.format(revenuePrev));

        // Fill line chart for current month
        chartDoanhThuThangNay.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");
        chartDoanhThuThangNay.setLegendVisible(false);
        for (ThongKeBanHang tk : dataThis) {
            String label = tk.getThoiGian() == null ? "" : tk.getThoiGian();
            series.getData().add(new XYChart.Data<>(label, tk.getDoanhThu()));
        }
        chartDoanhThuThangNay.getData().add(series);
        chartDoanhThuThangNay.setAnimated(false);
    }

    public void selectMenu(int viTriGiaoDien){
        switch (viTriGiaoDien){
            case 0:{
                menuTimKiem.setStyle("-fx-background-color: #1e90ff");
                menuDanhMuc.setStyle("-fx-background-color: #1e90ff");
                menuCapNhat.setStyle("-fx-background-color: #1e90ff");
                menuThongKe.setStyle("-fx-background-color: #1e90ff");
                menuXuLy.setStyle("-fx-background-color: #1e90ff");
                break;
            }
            case 1:{
                menuTimKiem.setStyle("-fx-background-color: #003cff");
                menuDanhMuc.setStyle("-fx-background-color: #1e90ff");
                menuCapNhat.setStyle("-fx-background-color: #1e90ff");
                menuThongKe.setStyle("-fx-background-color: #1e90ff");
                menuXuLy.setStyle("-fx-background-color: #1e90ff");
                break;
            }
            case 2:{
                menuTimKiem.setStyle("-fx-background-color: #1e90ff");
                menuDanhMuc.setStyle("-fx-background-color: #003cff");
                menuCapNhat.setStyle("-fx-background-color: #1e90ff");
                menuThongKe.setStyle("-fx-background-color: #1e90ff");
                menuXuLy.setStyle("-fx-background-color: #1e90ff");
                break;
            }
            case 3:{
                menuTimKiem.setStyle("-fx-background-color: #1e90ff");
                menuDanhMuc.setStyle("-fx-background-color: #1e90ff");
                menuCapNhat.setStyle("-fx-background-color: #003cff");
                menuThongKe.setStyle("-fx-background-color: #1e90ff");
                menuXuLy.setStyle("-fx-background-color: #1e90ff");
                break;
            }
            case 4:{
                menuTimKiem.setStyle("-fx-background-color: #1e90ff");
                menuDanhMuc.setStyle("-fx-background-color: #1e90ff");
                menuCapNhat.setStyle("-fx-background-color: #1e90ff");
                menuThongKe.setStyle("-fx-background-color: #003cff");
                menuXuLy.setStyle("-fx-background-color: #1e90ff");
                break;
            }
            case 5: {
                menuTimKiem.setStyle("-fx-background-color: #1e90ff");
                menuDanhMuc.setStyle("-fx-background-color: #1e90ff");
                menuCapNhat.setStyle("-fx-background-color: #1e90ff");
                menuThongKe.setStyle("-fx-background-color: #1e90ff");
                menuXuLy.setStyle("-fx-background-color: #003cff");
                break;
            }
        }
    }

    public void LapHoaDon(ActionEvent actionEvent) {
        viTri=5;
        selectMenu(viTri);
    }

    public void AnhChuyenTrangChu(MouseEvent mouseEvent) {
        loadView(0, "/com/example/pharmacymanagementsystem_qlht/TrangChu_GUI.fxml");

    }

//  1.Chức năng tìm kiếm
//  Tìm kiếm hóa đơn
    public void timKiemHoaDon(ActionEvent actionEvent) {

        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoaDon/TKHoaDon_GUI.fxml");
    }

//  Tìm kiếm phiếu nhập
    public void timKiemPhieuNhap(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuNhapHang/TKPhieuNhapHang_GUI.fxml");
    }

//  Tìm kiếm phiếu đổi hàng
    public void timKiemPhieuDoiHang(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDoiHang/TKPhieuDoiHang_GUI.fxml");
    }

//  Tìm kiếm phiếu trả hàng
    public void timKiemPhieuTraHang(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuTraHang/TKPhieuTraHang_GUI.fxml");
    }

//  Tìm kiếm phiếu đặt hàng
    public void timKiemPhieuDatHang(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDatHang/TKPhieuDatHang_GUI.fxml");
    }

//  Tìm kiếm nhà cung cấp
    public void timKiemNhaCungCap(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKNhaCungCap/TKNCC_GUI.fxml");
    }

//  Tìm kiếm thuốc
    public void timKiemThuoc(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml");
    }

//  Tìm kiếm khách hàng
    public void timKiemKhachHang(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml");
    }

    //  Danh mục hoạt động
    public void timKiemHoatDong(ActionEvent actionEvent) {
        loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoatDong/TKHoatDong_GUI.fxml");
    }

//  2.Chức năng danh mục
//  Danh mục thuốc
    public void danhMucThuoc(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/DanhMucThuoc_GUI.fxml");
    }

//  Danh mục nhân viên
    public void danhMucNhanVien(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/DanhMucNhanVien_GUI.fxml");
    }

//  Danh mục khách hàng
    public void danhMucKhachHang(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/DanhMucKhachHang_GUI.fxml");
    }

//  Danh mục kệ hàng
    public void danhMucKeHang(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/DanhMucKeHang_GUI.fxml");
    }

//  Danh mục nhà cung cấp
    public void danhMucNhaCungCap(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/DanhMucNhaCungCap_GUI.fxml");
    }

//  Danh mục khuyến mãi
    public void danhMucKhuyenMai(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/DanhMucKhuyenMai_GUI.fxml");
    }

    //  Danh mục nhóm dược lý
    public void danhMucNhomDuocLy(ActionEvent actionEvent) {
        loadView(2, "/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhomDuocLy/DanhMucNhomDuocLy.fxml");
    }

//  3.Chức năng cập nhật
//  Cập nhật giá bán
    public void CapNhatGiaBan(ActionEvent actionEvent) {
        loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/CapNhatGiaThuoc_GUI.fxml");
    }

//  Cập nhật tồn kho
    public void capNhatTonKho(ActionEvent actionEvent) {
        loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatSoLuong/CapNhatSoLuongThuoc_GUI.fxml");
    }

    public void capNhatKhuyenMai(ActionEvent actionEvent) {
        loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatKhuyenMai/CapNhatKhuyenMai_GUI.fxml");
    }

//  4.Chức năng thống kê và báo cáo
//  Thống kê doanh thu
    public void thongKeDoanhThu(ActionEvent actionEvent) {
        loadView(4, "/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml");
    }

    public void thongKeXuatNhap(ActionEvent actionEvent) {
        loadView(4, "/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeXNT_GUI.fxml");
    }

//  5.Chức năng xử lý
//  Lập hóa đơn
    public void lapHoaDon(ActionEvent actionEvent) {
        loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml");
    }

//  Lập phiếu đổi hàng
    public void lapPhieuDoiHang(ActionEvent actionEvent) {
        loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDoi/LapPhieuDoiHang_GUI.fxml");
    }

//  Lập phiếu trả hàng
    public void lapPhieuTraHang(ActionEvent actionEvent) {
        loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuTra/LapPhieuTraHang_GUI.fxml");
    }

//  Lập phiếu đặt hàng
    public void lapPhieuDatHang(ActionEvent actionEvent) {
        loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDat/LapPhieuDatHang_GUI.fxml");
    }

//  Lập phiếu nhập hàng
    public void nhapHang(ActionEvent actionEvent) {
        loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/LapPhieuNhapHang_GUI.fxml");
    }

    private void addShortcut(Scene scene, KeyCodeCombination keyCombination, Runnable action) {
        scene.getAccelerators().put(keyCombination, action);
    }
    private void setupGlobalShortcuts() {
        // Phải dùng Platform.runLater để đợi Scene được tạo
        Platform.runLater(() -> {
            Scene scene = pnlChung.getScene(); // Lấy scene từ pane chính
            if (scene == null) {
                System.err.println("Không thể lấy Scene để gán phím tắt.");
                return;
            }

            // --- ĐỊNH NGHĨA PHÍM TẮT TOÀN CỤC (F1-F8) ---

            // F1: Trang chủ
            addShortcut(scene, new KeyCodeCombination(KeyCode.F1), () ->
                    loadView(0, "/com/example/pharmacymanagementsystem_qlht/TrangChu_GUI.fxml")
            );

            // F2: Lập hóa đơn
            addShortcut(scene, new KeyCodeCombination(KeyCode.F2), () ->
                    loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml")
            );

            // F3: Đặt hàng (Lập phiếu đặt hàng)
            addShortcut(scene, new KeyCodeCombination(KeyCode.F3), () ->
                    loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDat/LapPhieuDatHang_GUI.fxml")
            );

            // F4: Nhập hàng (Lập phiếu nhập hàng)
            addShortcut(scene, new KeyCodeCombination(KeyCode.F4), () ->
                    loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/LapPhieuNhapHang_GUI.fxml")
            );

            // F5: Tìm khách hàng
            addShortcut(scene, new KeyCodeCombination(KeyCode.F5), () ->
                    loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml")
            );

            // F6: Tìm thuốc
            addShortcut(scene, new KeyCodeCombination(KeyCode.F6), () ->
                    loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml")
            );

            // F7: Cập nhật giá
            addShortcut(scene, new KeyCodeCombination(KeyCode.F7), () ->
                    loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/CapNhatGiaThuoc_GUI.fxml")
            );

            // F8: Cập nhật số lượng (Tồn kho)
            addShortcut(scene, new KeyCodeCombination(KeyCode.F8), () ->
                    loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatSoLuong/CapNhatSoLuongThuoc_GUI.fxml")
            );

        });
        pnlChung.requestFocus();

    }

    public void pnlNguoiDungClick(MouseEvent mouseEvent) {
        pnlThongTin.setVisible(!pnlThongTin.isVisible());
        lblVaiTro.setText(DangNhap_Ctrl.user.getVaiTro().toString());
    }


    public void btnDangXuatClick(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/DangNhap_GUI.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Đăng nhập hệ thống quản lý nhà thuốc");
            stage.show();
            // Đóng cửa sổ hiện tại
            pnlChung.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
