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
import javafx.beans.property.SimpleStringProperty;
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

//  1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public static CuaSoChinh_QuanLy_Ctrl instance;
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
    public TableColumn<Thuoc_SP_TheoLo, java.sql.Date> colHSDHetHan;
    public TableView<Thuoc_SP_TheoLo> tblThuocSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaThuocSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colLoHangSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, java.sql.Date> colHSDSapHetHan;
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

//  2. KHAI BÁO BIẾN TOÀN CỤC
    private int viTri;
    private List<Thuoc_SP_TheoLo> listThuocHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangDaHetHan();
    private List<Thuoc_SP_TheoLo> listThuocSapHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangSapHetHan();

//  3. HÀM KHỞI TẠO
    public void initialize(){
        txtNguoiDung.setText("Người dùng: "+ DangNhap_Ctrl.user.getTenNV());
        setNgayGio(txtNgayThangNam);
        loadTableThuocHetHan();
        loadTableThuocSapHetHan();
        setThongKeLabelsAndData();
        setupGlobalShortcuts();
        pnlThongTin.setVisible(false);
    }

//  4. CÁC HÀM CHUNG
//  Hàm load giao diện con vào panel chung
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
        pnlChung.requestFocus();

    }

//  Hàm load dữ liệu vào bảng thuốc hết hạn
    public void loadTableThuocHetHan(){
        ObservableList<Thuoc_SP_TheoLo> data = tblThuocHetHan.getItems();
        data.clear();
        data.addAll(listThuocHetHan);

//      Set số lượng lô hàng hết hạn
        lbl_SoLuongHangHetHan.setText("Số lượng lô hàng hết hạn: " +listThuocHetHan.size());

//      Định nghĩa các cột trong bảng
        colMaThuocHetHan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThuoc().getMaThuoc()));
        colLoHangHetHan.setCellValueFactory(new PropertyValueFactory<>("maLH"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colHSDHetHan.setCellValueFactory(new PropertyValueFactory<>("hsd"));
        colHSDHetHan.setCellFactory(column -> new TableCell<Thuoc_SP_TheoLo, java.sql.Date>() {
            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item.toLocalDate()));
                }
            }
        });

//      Thiết lập dữ liệu cho bảng
        tblThuocHetHan.setItems(data);
    }

//  Hàm load dữ liệu vào bảng thuốc sắp hết hạns
    public void loadTableThuocSapHetHan(){
        ObservableList<Thuoc_SP_TheoLo> data = tblThuocSapHetHan.getItems();
        data.clear();
        data.addAll(listThuocSapHetHan);
//      Set số lượng lô hàng sắp hết hạn
        lbl_SoLuongHangSapHetHan.setText("Số lượng lô hàng sắp hết hạn: " +listThuocSapHetHan.size());

        colMaThuocSapHetHan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThuoc().getMaThuoc()));
        colLoHangSapHetHan.setCellValueFactory(new PropertyValueFactory<>("maLH"));

//      Định dạng ngày tháng cho cột hạn sử dụng
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colHSDSapHetHan.setCellValueFactory(new PropertyValueFactory<>("hsd"));
        colHSDSapHetHan.setCellFactory(column -> new TableCell<Thuoc_SP_TheoLo, java.sql.Date>() {
            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item.toLocalDate()));
                }
            }
        });

//      Thiết lập dữ liệu cho bảng
        tblThuocSapHetHan.setItems(data);
    }

//  Hàm thiết lập đồng hồ ngày giờ
    private void setNgayGio(Label lblNgayGio) {
        Locale localeVN = new Locale("vi", "VN");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy HH:mm:ss", localeVN);

//      Tạo và chạy Timeline để cập nhật thời gian mỗi giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> {
                    LocalDateTime now = LocalDateTime.now();
                    lblNgayGio.setText(now.format(formatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );

//      set timeline để chạy vô hạn
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

//  Hàm thiết lập nhãn và dữ liệu thống kê
    private void setThongKeLabelsAndData() {

//      Kiểm tra null để tránh lỗi
        if (lblHoaDonThangNay == null || lblHoaDonThangTruoc == null
                || lblDoanhThuThangNay == null || lblDoanhThuThangTruoc == null
                || chartDoanhThuThangNay == null) {
            return;
        }
//      Lấy dữ liệu thống kê từ DAO
        ThongKe_Dao tkDao = new ThongKe_Dao();
        LocalDate now = LocalDate.now();

//      Xác định phạm vi ngày cho tháng hiện tại và tháng trước
        LocalDate startThis = now.withDayOfMonth(1);
        LocalDate endThis = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate startPrev = startThis.minusMonths(1);
        LocalDate endPrev = startThis.minusDays(1);

//      và các mục nhập theo ngày (hoặc theo kỳ) từ DAO cho các phạm vi
        List<ThongKeBanHang> dataThis = tkDao.getThongKeBanHang_TuyChon(startThis, endThis);
        List<ThongKeBanHang> dataPrev = tkDao.getThongKeBanHang_TuyChon(startPrev, endPrev);

//      Tính tổng số hóa đơn và doanh thu cho cả hai tháng
        int invoicesThis = dataThis.stream().mapToInt(ThongKeBanHang::getSoLuongHoaDon).sum();
        double revenueThis = dataThis.stream().mapToDouble(ThongKeBanHang::getDoanhThu).sum();

        int invoicesPrev = dataPrev.stream().mapToInt(ThongKeBanHang::getSoLuongHoaDon).sum();
        double revenuePrev = dataPrev.stream().mapToDouble(ThongKeBanHang::getDoanhThu).sum();

//      Định dạng và hiển thị trên nhãn
        DecimalFormat df = new DecimalFormat("#,###");

        lblHoaDonThangNay.setText(invoicesThis+" Hóa đơn");
        lblHoaDonThangTruoc.setText(invoicesPrev + " Hóa đơn");
        VNDFormatter vndFormatter = new VNDFormatter();
        lblDoanhThuThangNay.setText(vndFormatter.format(revenueThis));
        lblDoanhThuThangTruoc.setText(vndFormatter.format(revenuePrev));

//      Thiết lập dữ liệu cho biểu đồ doanh thu tháng này
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

//  Hàm chọn menu để thay đổi màu thanh menu khi chọn
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

//  5. CÁC HÀM XỬ LÝ SỰ KIỆN FXML
    public void AnhChuyenTrangChu(MouseEvent mouseEvent) {
        loadView(0, "/com/example/pharmacymanagementsystem_qlht/TrangChu_GUI.fxml");
    }

//  5.1.Chức năng tìm kiếm
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

//  5.2.Chức năng danh mục
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

//  5.3.Chức năng cập nhật
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

//  5.4.Chức năng thống kê và báo cáo
//  Thống kê doanh thu
    public void thongKeDoanhThu(ActionEvent actionEvent) {
        loadView(4, "/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml");
    }

    public void thongKeXuatNhap(ActionEvent actionEvent) {
        loadView(4, "/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeXNT_GUI.fxml");
    }

//  5.5.Chức năng xử lý
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

//  Hàm thiết lập phím tắt toàn cục
    private void setupGlobalShortcuts() {
        Platform.runLater(() -> {
            Scene scene = pnlChung.getScene();
            if (scene == null) {
                System.err.println("Không thể lấy Scene để gán phím tắt.");
                return;
            }

            // F1: Trang chủ
            addShortcut(scene, new KeyCodeCombination(KeyCode.F1), () -> loadView(0, "/com/example/pharmacymanagementsystem_qlht/TrangChu_GUI.fxml"));

            // F2: Lập hóa đơn
            addShortcut(scene, new KeyCodeCombination(KeyCode.F2), () -> loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml"));

            // F3: Đặt hàng (Lập phiếu đặt hàng)
            addShortcut(scene, new KeyCodeCombination(KeyCode.F3), () -> loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDat/LapPhieuDatHang_GUI.fxml"));

            // F4: Nhập hàng (Lập phiếu nhập hàng)
            addShortcut(scene, new KeyCodeCombination(KeyCode.F4), () -> loadView(5, "/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/LapPhieuNhapHang_GUI.fxml"));

            // F5: Tìm khách hàng
            addShortcut(scene, new KeyCodeCombination(KeyCode.F5), () -> loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml"));

            // F6: Tìm thuốc
            addShortcut(scene, new KeyCodeCombination(KeyCode.F6), () -> loadView(1, "/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml"));

            // F7: Cập nhật giá
            addShortcut(scene, new KeyCodeCombination(KeyCode.F7), () -> loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/CapNhatGiaThuoc_GUI.fxml"));

            // F8: Cập nhật số lượng (Tồn kho)
            addShortcut(scene, new KeyCodeCombination(KeyCode.F8), () -> loadView(3, "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatSoLuong/CapNhatSoLuongThuoc_GUI.fxml"));

        });
        pnlChung.requestFocus();

    }

//  Hàm xử lý sự kiện khi click vào panel người dùng
    public void pnlNguoiDungClick(MouseEvent mouseEvent) {
        pnlThongTin.setVisible(!pnlThongTin.isVisible());
        lblVaiTro.setText(DangNhap_Ctrl.user.getVaiTro().toString());
    }

//  Hàm xử lý sự kiện đăng xuất
    public void btnDangXuatClick(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/DangNhap_GUI.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Đăng nhập hệ thống quản lý nhà thuốc");
            stage.show();
            pnlChung.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
