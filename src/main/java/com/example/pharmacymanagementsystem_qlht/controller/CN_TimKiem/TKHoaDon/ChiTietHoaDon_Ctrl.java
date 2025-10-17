package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;

import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoaDon;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChiTietHoaDon_Ctrl {
    @FXML
    private TableView<ChiTietHoaDon> tblChiTietHoaDon;
    @FXML
    private TableColumn<ChiTietHoaDon, Number> colNSTT;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colNTen;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> colNSL;
    @FXML
    private TableColumn<ChiTietHoaDon, String> colNDonVi;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colNDonGia;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colNChietKhau;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> colNThanhTien;
    @FXML
    private Label lblMaHoaDonValue;
    @FXML
    private Label lblNgayLapValue;
    @FXML
    private Label lblTenNhanVienValue;
    @FXML
    private Label lblTenKhachHangValue;
    @FXML
    private Label lblSDTKhachHangValue;
    @FXML
    private Label lblGhiChuValue;
    @FXML
    private Label lblTongTienHangValue;
    @FXML
    private Label lblChietKhauHDValue;
    @FXML
    private Label lblThueVATValue;
    @FXML
    private Label lblThanhToanValue;
    @FXML
    private Label lblPTTTValue;
    @FXML
    private Label lblTienKhachDuaValue;
    @FXML
    private Label lblTienThuaValue;
    @FXML
    private Button btnDong;
    @FXML
    private Button btnInHoaDon;

    private HoaDon hoaDon;



    private void taiChiTietHoaDon(List<ChiTietHoaDon> chiTietList) {
    }

    @FXML
    public void initialize() {
        btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
        //btnInHoaDon.setOnAction(e -> inHoaDon());
    }

//    private void inHoaDon() {
//        if (hoaDon == null) return;
//        try {
//            String fileName = "HoaDon_" + hoaDon.getMaHD() + ".pdf";
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(fileName));
//            document.open();
//            document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG"));
//            document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHD()));
//            document.add(new Paragraph("Ngày lập: " + (hoaDon.getNgayLap() != null ? hoaDon.getNgayLap().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "")));
//            document.add(new Paragraph("Khách hàng: " + (hoaDon.getMaKH() != null ? hoaDon.getMaKH().getTenKH() : "")));
//            document.add(new Paragraph("SĐT: " + (hoaDon.getMaKH() != null ? hoaDon.getMaKH().getSdt() : "")));
//            document.add(new Paragraph("Nhân viên: " + (hoaDon.getMaNV() != null ? hoaDon.getMaNV().getTenNV() : "")));
//            document.add(new Paragraph(" "));
//            PdfPTable table = new PdfPTable(5);
//            table.addCell("STT");
//            table.addCell("Tên sản phẩm");
//            table.addCell("Số lượng");
//            table.addCell("Đơn giá");
//            table.addCell("Thành tiền");
//            int stt = 1;
//            for (var cthd : hoaDon.getChiTietHD()) {
//                table.addCell(String.valueOf(stt++));
//                String tenThuoc = cthd.getLoHang() != null && cthd.getLoHang().getThuoc() != null ? cthd.getLoHang().getThuoc().getTenThuoc() : "";
//                table.addCell(tenThuoc);
//                table.addCell(String.valueOf(cthd.getSoLuong()));
//                table.addCell(String.valueOf(cthd.getDonGia()));
//                table.addCell(String.valueOf(cthd.getSoLuong() * cthd.getDonGia()));
//            }
//            document.add(table);
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("Tổng thanh toán: " + hoaDon.tongHD() + " VNĐ"));
//            document.close();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("In hóa đơn");
//            alert.setHeaderText(null);
//            alert.setContentText("Đã xuất file PDF: " + fileName);
//            alert.showAndWait();
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Lỗi in hóa đơn");
//            alert.setHeaderText(null);
//            alert.setContentText("Không thể xuất file PDF hóa đơn!");
//            alert.showAndWait();
//        }
//    }


}
