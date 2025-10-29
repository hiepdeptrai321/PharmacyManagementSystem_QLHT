package com.example.pharmacymanagementsystem_qlht.controller;

import com.example.pharmacymanagementsystem_qlht.TienIch.VNDFormatter;
import com.example.pharmacymanagementsystem_qlht.dao.ThongKe_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SP_TheoLo_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrangChu_Ctrl {

//  1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public TableView<Thuoc_SP_TheoLo> tblThuocHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaThuocHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colLoHangHetHan;
    public TableColumn<Thuoc_SP_TheoLo, Date> colHSDHetHan;
    public TableView<Thuoc_SP_TheoLo> tblThuocSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaThuocSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, String> colLoHangSapHetHan;
    public TableColumn<Thuoc_SP_TheoLo, Date> colHSDSapHetHan;
    public Label lbl_SoLuongHangHetHan;
    public Label lbl_SoLuongHangSapHetHan;
    public LineChart chartDoanhThuThangNay;
    public Label lblDoanhThuThangTruoc;
    public Label lblDoanhThuThangNay;
    public Label lblHoaDonThangTruoc;
    public Label lblHoaDonThangNay;
    private List<Thuoc_SP_TheoLo> listThuocHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangDaHetHan();
    private List<Thuoc_SP_TheoLo> listThuocSapHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangSapHetHan();

//  2. HÀM KHỞI TẠO
    public void initialize(){
        loadTableThuocHetHan();
        loadTableThuocSapHetHan();
        setThongKeLabelsAndData();
    }

//  3. CÁC HÀM XỬ LÝ SỰ KIỆN, HÀM HỖ TRỢ KHÁC
//  3.1 Load dữ liệu vào bảng thuốc hết hạn
    public void loadTableThuocHetHan(){
        ObservableList<Thuoc_SP_TheoLo> data = tblThuocHetHan.getItems();
        data.clear();
        data.addAll(listThuocHetHan);
        lbl_SoLuongHangHetHan.setText("Số lượng hàng hết hạn: " +listThuocHetHan.size());
        colMaThuocHetHan.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        colLoHangHetHan.setCellValueFactory(new PropertyValueFactory<>("maLH"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colHSDHetHan.setCellValueFactory(new PropertyValueFactory<>("hsd"));
        colHSDHetHan.setCellFactory(column -> new TableCell<Thuoc_SP_TheoLo, Date>() {
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
    }
//  3.2 Load dữ liệu vào bảng thuốc sắp hết hạn
    public void loadTableThuocSapHetHan(){
        ObservableList<Thuoc_SP_TheoLo> data = tblThuocSapHetHan.getItems();
        data.clear();
        data.addAll(listThuocSapHetHan);
        lbl_SoLuongHangSapHetHan.setText("Số lượng hàng sắp hết hạn: " +listThuocSapHetHan.size());
        colMaThuocSapHetHan.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getThuoc().getMaThuoc()));
        colLoHangSapHetHan.setCellValueFactory(new PropertyValueFactory<>("maLH"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colHSDSapHetHan.setCellValueFactory(new PropertyValueFactory<>("hsd"));
        colHSDSapHetHan.setCellFactory(column -> new TableCell<Thuoc_SP_TheoLo, Date>() {
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
    }

//  3.3 Thiết lập các nhãn thống kê và biểu đồ
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
}
