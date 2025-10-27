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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TrangChu_Ctrl {
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
    private int viTri;
    private List<Thuoc_SP_TheoLo> listThuocHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangDaHetHan();
    private List<Thuoc_SP_TheoLo> listThuocSapHetHan  = new Thuoc_SP_TheoLo_Dao().selectHangSapHetHan();

    public void initialize(){
        loadTableThuocHetHan();
        loadTableThuocSapHetHan();
        setThongKeLabelsAndData();
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
}
