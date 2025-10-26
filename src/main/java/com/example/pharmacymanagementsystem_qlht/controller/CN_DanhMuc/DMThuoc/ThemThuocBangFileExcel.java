package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ThemThuocBangFileExcel {

    public Label lblThongTinFile;
    public ImageView btnXoa;
    public Button btnLuu;
    private DanhMucThuoc_Ctrl danhMucThuocCtrl;

    public List<Thuoc_SanPham> danhSachThuoc = new ArrayList<>();

//  ==================================================================Khởi tạo
    public void initialize() {
        lblThongTinFile.setText("Kéo & thả file Excel vào đây");
        btnXoa.setVisible(false);
    }
//  ==================================================================hàm xử lý
    public void setDanhMucThuocCtrl(DanhMucThuoc_Ctrl ctrl){
        this.danhMucThuocCtrl = ctrl;
    }
//  Thả file dữ liệu
    public void thaFileDuLieu(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;

        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
                    try {
                        lblThongTinFile.setText("File đã tải: "+file.getName());
                        btnXoa.setVisible(true);
                        try (FileInputStream fis = new FileInputStream(file)) {
                            lblThongTinFile.setText("File đã chọn: " + file.getName());
                            btnXoa.setVisible(true);
                            importExcel(file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng chọn file Excel hợp lệ (.xlsx hoặc .xls)");
                    alert.showAndWait();
                }
            }
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

//  Kéo file dữ liệu
    public void onDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

//  Chọn file dữ liệu
    public void btnChonTapTinClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file Excel");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                lblThongTinFile.setText("File đã chọn: " + file.getName());
                btnXoa.setVisible(true);
                importExcel(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//  Xóa file đã chọn
    public void btnXoaClick(MouseEvent mouseEvent) {
        lblThongTinFile.setText("Kéo & thả file Excel vào đây");
        btnXoa.setVisible(false);
        danhSachThuoc.clear();
    }

//  Nhập dữ liệu từ file Excel
    public void importExcel(File excelFile) {
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ dòng tiêu đề

                Thuoc_SanPham sp = new Thuoc_SanPham();
                sp.setTenThuoc(getString(row.getCell(0)));
                sp.setHamLuong((int) getNumeric(row.getCell(1)));
                sp.setDonViHamLuong(getString(row.getCell(2)));
                sp.setDuongDung(getString(row.getCell(3)));
                sp.setQuyCachDongGoi(getString(row.getCell(4)));
                sp.setSDK_GPNK(getString(row.getCell(5)));
                sp.setHangSX(getString(row.getCell(6)));
                sp.setNuocSX(getString(row.getCell(7)));

                // Lấy các entity liên kết
                String maNDL = getString(row.getCell(8));
                String maLoaiHang = getString(row.getCell(9));
                String viTri = getString(row.getCell(10));

                sp.setNhomDuocLy(new NhomDuocLy_Dao().selectById(maNDL));
                sp.setLoaiHang(new LoaiHang_Dao().selectById(maLoaiHang));
                sp.setVitri(new KeHang_Dao().selectById(viTri));

                danhSachThuoc.add(sp);
                count++;
            }

            System.out.println("✅ Đã thêm " + count + " thuốc từ Excel!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Hàm hỗ trợ đọc dữ liệu từ ô Excel
    private String getString(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

//  Lấy giá trị số từ ô Excel
    private double getNumeric(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try {
            return Double.parseDouble(cell.getStringCellValue());
        } catch (Exception e) {
            return 0;
        }
    }

//  Lưu dữ liệu vào database
    public void btnLuuClick(ActionEvent actionEvent) {
        Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
        for(Thuoc_SanPham thuoc : danhSachThuoc){
            thuocDao.insertThuocProc(thuoc);
        }
        danhMucThuocCtrl.loadTable();
    }

//  Tải file mẫu
    public void btnTaiTapTinMauClick(ActionEvent actionEvent) {
        try (InputStream inputStream = getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/file_Excel/MauThemThuoc.xlsx")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Không tìm thấy file mẫu trong resources!");
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file mẫu");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            fileChooser.setInitialFileName("MauThemThuoc.xlsx");

            File destFile = fileChooser.showSaveDialog(null);
            if (destFile != null) {
                Files.copy(inputStream, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể tải file mẫu: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
