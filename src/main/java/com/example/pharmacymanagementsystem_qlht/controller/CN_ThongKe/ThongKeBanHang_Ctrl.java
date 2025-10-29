package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

// Imports cho việc xuất file
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Imports cho Excel (Apache POI)
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Imports cho PDF (iText 7)
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;
import java.text.DecimalFormat;
import javafx.scene.control.TableCell;
import com.example.pharmacymanagementsystem_qlht.dao.ThongKe_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeSanPham;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.time.LocalDate;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.chart.CategoryAxis;
import java.net.URL;
import java.util.ResourceBundle;

public class ThongKeBanHang_Ctrl extends Application implements Initializable {

    // --- 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML) ---
    @FXML
    private Button btnBang;
    @FXML
    private Button btnBieuDo;
    @FXML
    private Button btnXuat;
    @FXML
    private ComboBox<String> cboThoiGian;
    @FXML
    private ComboBox<String> cboXuatfile;
    @FXML
    private BarChart<String, Number> chartDoanhThu;
    @FXML
    private ToggleGroup date;

    @FXML
    private Label lblTu;

    @FXML
    private DatePicker dateTu;

    @FXML
    private Label lblDen;

    @FXML
    private DatePicker dateDen;

    @FXML
    private TableView<ThongKeBanHang> tableDoanhThu;
    @FXML
    private TableColumn<ThongKeBanHang, String> cotTG;
    @FXML
    private TableColumn<ThongKeBanHang, Integer> cotSLHoaDon;
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotTongGT;
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotGG;
    @FXML
    private TableColumn<ThongKeBanHang, Integer> cotDT; // Số lượng đơn trả
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotGTDonTra;
    @FXML
    private TableColumn<ThongKeBanHang, Double> cotDoanhThu;
    @FXML
    private CategoryAxis xAxis;


    @FXML
    private TableView<ThongKeSanPham> tableTopSanPham;
    @FXML
    private TableColumn<ThongKeSanPham, String> cotMaThuoc;
    @FXML
    private TableColumn<ThongKeSanPham, String> cotTenThuoc;
    @FXML
    private TableColumn<ThongKeSanPham, Integer> cotSL;
    @FXML
    private TableColumn<ThongKeSanPham, Double> cotThanhTien;

    private ThongKe_Dao tkDao = new ThongKe_Dao();
    private ObservableList<ThongKeBanHang> listThongKe;
    private ObservableList<ThongKeSanPham> listTopSanPham;


    // --- 2. KHỞI TẠO (INITIALIZE) ---
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnXuat.setOnAction(e-> xuatFile(e));
        chartDoanhThu.managedProperty().bind(chartDoanhThu.visibleProperty());
        tableDoanhThu.managedProperty().bind(tableDoanhThu.visibleProperty());
        DecimalFormat formatter = new DecimalFormat("#,##0");
        cboThoiGian.getItems().addAll("Hôm nay", "Tuần này", "Tháng này", "Năm Nay", "Tùy chọn");
        cboXuatfile.getItems().addAll("Excel", "PDF");


        cotTG.setCellValueFactory(new PropertyValueFactory<>("thoiGian"));
        cotSLHoaDon.setCellValueFactory(new PropertyValueFactory<>("soLuongHoaDon"));
        cotTongGT.setCellValueFactory(new PropertyValueFactory<>("tongGiaTri"));
        cotTongGT.setCellFactory(col -> new TableCell<ThongKeBanHang, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); // Không hiển thị gì nếu ô rỗng
                } else {
                    // Dùng formatter để định dạng số và hiển thị
                    setText(formatter.format(item));
                }
            }
        });
        cotGG.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
        cotGG.setCellFactory(col -> new TableCell<ThongKeBanHang, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        cotDT.setCellValueFactory(new PropertyValueFactory<>("soLuongDonTra"));
        cotGTDonTra.setCellValueFactory(new PropertyValueFactory<>("giaTriDonTra"));
        cotGTDonTra.setCellFactory(col -> new TableCell<ThongKeBanHang, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        cotDoanhThu.setCellValueFactory(new PropertyValueFactory<>("doanhThu"));
        cotDoanhThu.setCellFactory(col -> new TableCell<ThongKeBanHang, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });


        cotMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        cotTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        cotSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        cotThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        cotThanhTien.setCellFactory(col -> new TableCell<ThongKeSanPham, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        lblTu.setVisible(false);
        dateTu.setVisible(false);
        lblDen.setVisible(false);
        dateDen.setVisible(false);

        lblTu.managedProperty().bind(lblTu.visibleProperty());
        dateTu.managedProperty().bind(dateTu.visibleProperty());
        lblDen.managedProperty().bind(lblDen.visibleProperty());
        dateDen.managedProperty().bind(dateDen.visibleProperty());


        cboThoiGian.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isCustom = newValue.equals("Tùy chọn");

                lblTu.setVisible(isCustom);
                dateTu.setVisible(isCustom);
                lblDen.setVisible(isCustom);
                dateDen.setVisible(isCustom);

                if (isCustom) {
                    attemptAutoLoadTuyChon();
                } else {
                    loadData(newValue);
                }
            }
        });


        dateTu.valueProperty().addListener((options, oldValue, newValue) -> {
            attemptAutoLoadTuyChon();
        });


        dateDen.valueProperty().addListener((options, oldValue, newValue) -> {
            attemptAutoLoadTuyChon();
        });


        cboThoiGian.setValue("Hôm nay");
        chartDoanhThu.setAnimated(false);
    }


    private void loadData(String thoiGian) {
        // Lấy dữ liệu
        listTopSanPham = FXCollections.observableArrayList(tkDao.getTop5SanPham(thoiGian));
        listThongKe = FXCollections.observableArrayList(tkDao.getThongKeBanHang(thoiGian));

        tableTopSanPham.setItems(listTopSanPham);
        tableDoanhThu.setItems(listThongKe);

        // ⚠️ Bắt buộc: reset cả dữ liệu và category trước
        chartDoanhThu.getData().clear();
        if (xAxis != null) {
            xAxis.getCategories().clear();
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        // Danh mục mới
        ObservableList<String> categories = FXCollections.observableArrayList();
        for (ThongKeBanHang tk : listThongKe) {
            String label = tk.getThoiGian();
            categories.add(label);
            series.getData().add(new XYChart.Data<>(label, tk.getDoanhThu()));
        }

        chartDoanhThu.getData().add(series);

        // ⚡ Đặt category sau khi đã add series để đồng bộ
        xAxis.setCategories(categories);

        chartDoanhThu.setVisible(false);
        tableDoanhThu.setVisible(true);

        // Xoay nhãn
        xAxis.setTickLabelRotation(-20);
    }



    private void attemptAutoLoadTuyChon() {
        // 1. Chỉ thực thi nếu ComboBox đang là "Tùy chọn"
        String selectedTime = cboThoiGian.getValue();
        if (selectedTime == null || !selectedTime.equals("Tùy chọn")) {
            return;
        }

        LocalDate tuNgay = dateTu.getValue();
        LocalDate denNgay = dateDen.getValue();

        if (tuNgay == null || denNgay == null) {
            return;
        }
        if (tuNgay.isAfter(denNgay)) {
            System.out.println("Ngày bắt đầu không thể sau ngày kết thúc");
            tableDoanhThu.getItems().clear();
            tableTopSanPham.getItems().clear();
            chartDoanhThu.getData().clear();
            return;
        }
        loadDataTuyChon(tuNgay, denNgay);
    }


    private void loadDataTuyChon(LocalDate tuNgay, LocalDate denNgay) {
        listTopSanPham = FXCollections.observableArrayList(tkDao.getTop5SanPham_TuyChon(tuNgay, denNgay));
        listThongKe = FXCollections.observableArrayList(tkDao.getThongKeBanHang_TuyChon(tuNgay, denNgay));

        tableTopSanPham.setItems(listTopSanPham);
        tableDoanhThu.setItems(listThongKe);

        chartDoanhThu.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu (Tùy chọn)");

        ObservableList<String> categories = FXCollections.observableArrayList();
        for (ThongKeBanHang tk : listThongKe) {
            String tg = tk.getThoiGian() == null ? "" : tk.getThoiGian();
            categories.add(tg);
            series.getData().add(new XYChart.Data<>(tg, tk.getDoanhThu()));
        }

        if (xAxis != null) xAxis.setCategories(categories);
        chartDoanhThu.getData().add(series);

        chartDoanhThu.setVisible(false);
        tableDoanhThu.setVisible(true);
    }


    @FXML
    private void xuatFile(ActionEvent event) {
        String selectedFormat = cboXuatfile.getValue();
        if (selectedFormat == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn định dạng", "Vui lòng chọn định dạng file (Excel hoặc PDF) để xuất.");
            return;
        }

        if (listThongKe == null || listTopSanPham == null || listThongKe.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Không có dữ liệu", "Không có dữ liệu thống kê để xuất.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file thống kê");
        fileChooser.setInitialFileName("BaoCao_" + LocalDate.now());

        if (selectedFormat.equals("Excel")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
            File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow());
            if (file != null) {
                try {
                    xuatExcel(file);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất file Excel thành công!");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi xuất file Excel: " + e.getMessage());
                }
            }
        } else if (selectedFormat.equals("PDF")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
            File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow());
            if (file != null) {
                try {
                    xuatPDF(file);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất file PDF thành công!");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi xuất file PDF: " + e.getMessage());
                }
            }
        }
    }

    private void xuatExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheetDT = workbook.createSheet("Thong ke Doanh thu");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            String[] headersDT = {
                    cotTG.getText(), cotSLHoaDon.getText(), cotTongGT.getText(),
                    cotGG.getText(), cotDT.getText(), cotGTDonTra.getText(), cotDoanhThu.getText()
            };

            Row headerRowDT = sheetDT.createRow(0);
            for (int i = 0; i < headersDT.length; i++) {
                Cell cell = headerRowDT.createCell(i);
                cell.setCellValue(headersDT[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowNumDT = 1;
            for (ThongKeBanHang tk : listThongKe) {
                Row row = sheetDT.createRow(rowNumDT++);
                row.createCell(0).setCellValue(tk.getThoiGian());
                row.createCell(1).setCellValue(tk.getSoLuongHoaDon());
                row.createCell(2).setCellValue(tk.getTongGiaTri());
                row.createCell(3).setCellValue(tk.getGiamGia());
                row.createCell(4).setCellValue(tk.getSoLuongDonTra());
                row.createCell(5).setCellValue(tk.getGiaTriDonTra());
                row.createCell(6).setCellValue(tk.getDoanhThu());
            }

            for (int i = 0; i < headersDT.length; i++) {
                sheetDT.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    public static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";

    private void xuatPDF(File file) throws IOException {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont font;
        try {
            font = PdfFontFactory.createFont(FONT_PATH );
        } catch (IOException e) {
            System.err.println("Không tìm thấy font tại: " + FONT_PATH + ". Sử dụng font mặc định.");
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        }
        document.setFont(font);


        document.add(new Paragraph("BÁO CÁO THỐNG KÊ DOANH THU")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("I. Thống kê Doanh thu")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15));

        float[] columnWidthsDT = {2, 1, 1, 1, 1, 1, 1}; // Tỷ lệ độ rộng các cột
        Table tableDT = new Table(UnitValue.createPercentArray(columnWidthsDT));
        tableDT.setWidth(UnitValue.createPercentValue(100)); // Rộng 100%

        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotTG.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotSLHoaDon.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotTongGT.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotGG.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotDT.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotGTDonTra.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(cotDoanhThu.getText()).setBold()));


        for (ThongKeBanHang tk : listThongKe) {
            tableDT.addCell(tk.getThoiGian());
            tableDT.addCell(String.valueOf(tk.getSoLuongHoaDon()));
            tableDT.addCell(String.valueOf(tk.getTongGiaTri()));
            tableDT.addCell(String.valueOf(tk.getGiamGia()));
            tableDT.addCell(String.valueOf(tk.getSoLuongDonTra()));
            tableDT.addCell(String.valueOf(tk.getGiaTriDonTra()));
            tableDT.addCell(String.valueOf(tk.getDoanhThu()));
        }
        document.add(tableDT);
        document.close();
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- 3. XỬ LÝ SỰ KIỆN GIAO DIỆN ---
    @FXML
    private void hienThiBieuDo(ActionEvent event) {
        chartDoanhThu.setVisible(true);
        tableDoanhThu.setVisible(false);
    }

    @FXML
    private void hienThiBang(ActionEvent event) {
        chartDoanhThu.setVisible(false);
        tableDoanhThu.setVisible(true);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}