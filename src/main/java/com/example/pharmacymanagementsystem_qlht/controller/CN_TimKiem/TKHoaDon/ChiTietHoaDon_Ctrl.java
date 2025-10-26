package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietDonViTinh_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.ChiTietHoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.HoaDon_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.*;
import com.example.pharmacymanagementsystem_qlht.service.ApDungKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.service.DichVuKhuyenMai;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ChiTietHoaDon_Ctrl {
    @FXML private TableView<ChiTietHoaDon> tblChiTietHoaDon;
    @FXML private TableColumn<ChiTietHoaDon, Number> colNSTT;
    @FXML private TableColumn<ChiTietHoaDon, String> colNTen;
    @FXML private TableColumn<ChiTietHoaDon, Integer> colNSL;
    @FXML private TableColumn<ChiTietHoaDon, String> colNDonVi;
    @FXML private TableColumn<ChiTietHoaDon, Double> colNDonGia;
    @FXML private TableColumn<ChiTietHoaDon, Double> colNChietKhau;
    @FXML private TableColumn<ChiTietHoaDon, Double> colNThanhTien;
    @FXML private Label lblMaHoaDonValue;
    @FXML private Label lblNgayLapValue;
    @FXML private Label lblTenNhanVienValue;
    @FXML private Label lblTenKhachHangValue;
    @FXML private Label lblSDTKhachHangValue;
    @FXML private Label lblGhiChuValue;
    @FXML private Label lblTongTienHang;
    @FXML Label lblGiamTheoSP;
    @FXML Label lblGiamTheoHD;
    @FXML Label lblVAT;
    @FXML Label lblTongThanhToan;

    @FXML private Button btnDong;
    @FXML private Button btnInHoaDon;
    @FXML private Label lblLoaiHoaDon;
    @FXML private Label lblMaDonThuocTitle;
    @FXML private Label lblMaDonThuocValue;


    private HoaDon hoaDon;
    private final HoaDon_Dao hdDao = new HoaDon_Dao();
    private final ChiTietHoaDon_Dao cthdDao = new ChiTietHoaDon_Dao();
    private final ChiTietDonViTinh_Dao ctdvtDao = new ChiTietDonViTinh_Dao();
    private final Map<String, String> baseUnitCache = new HashMap<>();
    private final DichVuKhuyenMai kmService = new DichVuKhuyenMai();
    private final Thuoc_SanPham_Dao spDao = new Thuoc_SanPham_Dao();
    private final Map<String, String> tenSpCache = new HashMap<>();

    @FXML
    public void initialize() {
        if (btnDong != null) btnDong.setOnAction(e -> ((Stage) btnDong.getScene().getWindow()).close());
      //  if (btnInHoaDon != null) btnInHoaDon.setOnAction(e -> printInvoice());
    }


    public void setHoaDon(HoaDon hd) {
        this.hoaDon = hd;
        hienThiThongTin();
    }


    private void hienThiThongTin() {
        if (hoaDon == null) return;

        if (lblMaHoaDonValue != null) lblMaHoaDonValue.setText(safeStr(hoaDon.getMaHD()));
        if (lblNgayLapValue != null) {
            if (hoaDon.getNgayLap() != null) {
                var fmt = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                lblNgayLapValue.setText(fmt.format(hoaDon.getNgayLap()));
            } else lblNgayLapValue.setText("");
        }
        if (lblTenNhanVienValue != null)
            lblTenNhanVienValue.setText(hoaDon.getMaNV() != null ? safeStr(hoaDon.getMaNV().getTenNV()) : "");
        if (hoaDon.getMaKH() != null) {
            if (lblTenKhachHangValue != null) lblTenKhachHangValue.setText(safeStr(hoaDon.getMaKH().getTenKH()));
            if (lblSDTKhachHangValue != null) lblSDTKhachHangValue.setText(safeStr(hoaDon.getMaKH().getSdt()));
        } else {
            if (lblTenKhachHangValue != null) lblTenKhachHangValue.setText("Khách lẻ");
            if (lblSDTKhachHangValue != null) lblSDTKhachHangValue.setText("");
        }
        if (hoaDon.getLoaiHoaDon() != null) {
            boolean isETC = hoaDon.getLoaiHoaDon().equalsIgnoreCase("ETC");

            if (lblLoaiHoaDon != null) {
                lblLoaiHoaDon.setText(isETC ? "Hóa đơn Kê đơn (ETC)" : "Hóa đơn Không kê đơn (OTC)");
            }

            // Ẩn/Hiện trường Mã Đơn Thuốc
            if (lblMaDonThuocTitle != null) {
                lblMaDonThuocTitle.setVisible(isETC);
                lblMaDonThuocTitle.setManaged(isETC);
            }
            if (lblMaDonThuocValue != null) {
                lblMaDonThuocValue.setVisible(isETC);
                lblMaDonThuocValue.setManaged(isETC);
                if (isETC) {
                    lblMaDonThuocValue.setText(safeStr(hoaDon.getMaDonThuoc()));
                }
            }

        } else {
            // Dự phòng cho các hóa đơn cũ chưa có dữ liệu
            if (lblLoaiHoaDon != null) lblLoaiHoaDon.setText("Không kê đơn (OTC)");
            if (lblMaDonThuocTitle != null) lblMaDonThuocTitle.setVisible(false);
            if (lblMaDonThuocValue != null) lblMaDonThuocValue.setVisible(false);
        }
        if (lblGhiChuValue != null) lblGhiChuValue.setText("");

        List<ChiTietHoaDon> list = cthdDao.selectByMaHD(hoaDon.getMaHD());
        tblChiTietHoaDon.setItems(FXCollections.observableArrayList(list));

        if (colNSTT != null) {
            colNSTT.setCellValueFactory(cd ->
                    new ReadOnlyObjectWrapper<>(tblChiTietHoaDon.getItems().indexOf(cd.getValue()) + 1));
        }

        if (colNTen != null) {
            colNTen.setCellValueFactory(cel -> {
                ChiTietHoaDon row = cel.getValue();
                Thuoc_SP_TheoLo lo = row.getLoHang();
                String ten = (lo != null && lo.getThuoc() != null) ? safeStr(lo.getThuoc().getTenThuoc()) : "";
                String suffix = giftSuffix(row);
                return new SimpleStringProperty(ten + suffix);
            });
        }

        if (colNSL != null) {
            colNSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        }

        if (colNDonVi != null) {
            colNDonVi.setCellValueFactory(cel ->
                    new SimpleStringProperty(tenDonViCoBan(cel.getValue().getLoHang())));
        }

        if (colNDonGia != null) {
            colNDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
            colNDonGia.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatVNDTable(item == null ? 0 : item));
                    setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
                }
            });
        }

        if (colNChietKhau != null) {
            colNChietKhau.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
            colNChietKhau.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatVNDTable(item == null ? 0 : item));
                    setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
                }
            });
        }

        if (colNThanhTien != null) {
            colNThanhTien.setCellValueFactory(cel -> {
                ChiTietHoaDon r = cel.getValue();
                double tt = Math.max(0, r.getSoLuong() * r.getDonGia() - r.getGiamGia());
                return new ReadOnlyObjectWrapper<>(tt);
            });
            colNThanhTien.setCellFactory(tc -> new TableCell<>() {
                @Override protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatVNDTable(item == null ? 0 : item));
                    setStyle(empty ? "" : "-fx-alignment: CENTER-RIGHT;");
                }
            });
        }

        BigDecimal tongHang = BigDecimal.ZERO;
        BigDecimal giamTheoSp = BigDecimal.ZERO;
        for (ChiTietHoaDon r : list) {
            BigDecimal line = BigDecimal.valueOf(r.getDonGia()).multiply(BigDecimal.valueOf(r.getSoLuong()));
            tongHang = tongHang.add(line);
            giamTheoSp = giamTheoSp.add(BigDecimal.valueOf(Math.max(0, r.getGiamGia())));
        }

        BigDecimal baseTruocVAT = tongHang.subtract(giamTheoSp).max(BigDecimal.ZERO);
        BigDecimal giamTheoHoaDon = BigDecimal.ZERO;
        BigDecimal baseSauHD = baseTruocVAT.subtract(giamTheoHoaDon).max(BigDecimal.ZERO);
        BigDecimal vat = baseSauHD.multiply(new BigDecimal("0.05")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongThanhToan = baseSauHD.add(vat);

        if (lblGiamTheoSP != null)      lblGiamTheoSP.setText(formatVNDLabel(giamTheoSp));
        if (lblTongTienHang != null)    lblTongTienHang.setText(formatVNDLabel(baseTruocVAT));
        if (lblGiamTheoHD != null)      lblGiamTheoHD.setText(formatVNDLabel(giamTheoHoaDon));
        if (lblVAT != null)             lblVAT.setText(formatVNDLabel(vat));
        if (lblTongThanhToan != null)   lblTongThanhToan.setText(formatVNDLabel(tongThanhToan));


    }
    private String giftSuffix(ChiTietHoaDon row) {
        if (row == null || row.getLoHang() == null || row.getLoHang().getThuoc() == null) return "";
        Thuoc_SanPham sp = row.getLoHang().getThuoc();
        String maThuoc = sp.getMaThuoc();
        if (maThuoc == null || maThuoc.isBlank()) return "";

        int soLuong = Math.max(0, row.getSoLuong());
        BigDecimal donGia = BigDecimal.valueOf(Math.max(0, row.getDonGia()));
        LocalDate ngay = hoaDon != null && hoaDon.getNgayLap() != null
                ? hoaDon.getNgayLap().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : LocalDate.now();

        ApDungKhuyenMai ap = kmService.apDungChoSP(maThuoc, soLuong, donGia, ngay);
        if (ap == null || ap.getFreeItems().isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (String maTang : ap.getFreeItems().keySet()) {
            String name = getTenSP(maTang);
            if (name != null && !name.isBlank()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(name);
            }
        }
        return sb.length() > 0 ? " (có tặng kèm " + sb + ")" : "";
    }

    private String getTenSP(String ma) {
        if (ma == null) return null;
        if (tenSpCache.containsKey(ma)) return tenSpCache.get(ma);
        Thuoc_SanPham sp = spDao.selectById(ma);
        String ten = (sp != null && sp.getTenThuoc() != null) ? sp.getTenThuoc() : "";
        tenSpCache.put(ma, ten);
        return ten;
    }
    private static String formatVNDTable(double v) {
        DecimalFormat df = new DecimalFormat("#,##0");
        df.setGroupingUsed(true);
        return df.format(Math.max(0, Math.round(v))) + " đ";
    }

    private static String formatVNDLabel(BigDecimal v) {
        DecimalFormat df = new DecimalFormat("#,##0");
        df.setGroupingUsed(true);
        return df.format(v.max(BigDecimal.ZERO)) + " VND";
    }

    private static String formatVNDLabel(double v) {
        return formatVNDLabel(BigDecimal.valueOf(Math.max(0, Math.round(v))));
    }

    private static String safeStr(String s) { return s == null ? "" : s; }

    private String tenDonViCoBan(Thuoc_SP_TheoLo lo) {
        if (lo == null || lo.getThuoc() == null) return "";
        var sp = lo.getThuoc();
        String maThuoc = sp.getMaThuoc();
        if (maThuoc == null || maThuoc.isBlank()) return "";

        // 1
        if (baseUnitCache.containsKey(maThuoc)) return baseUnitCache.get(maThuoc);
        //2
        List<ChiTietDonViTinh> ds = (sp.getDsCTDVT() != null && !sp.getDsCTDVT().isEmpty())
                ? sp.getDsCTDVT()
                : ctdvtDao.selectByMaThuoc(maThuoc); // DAO fallback

        ChiTietDonViTinh base = null;
        if (ds != null && !ds.isEmpty()) {
            //3
            for (ChiTietDonViTinh ct : ds) {
                if (ct != null && ct.isDonViCoBan()) { base = ct; break; }
            }
            //4
            if (base == null) {
                ChiTietDonViTinh min = null;
                for (ChiTietDonViTinh ct : ds) {
                    if (ct == null) continue;
                    if (min == null) min = ct;
                    else if (ct.getHeSoQuyDoi() > 0 && ct.getHeSoQuyDoi() < min.getHeSoQuyDoi()) min = ct;
                }
                base = min;
            }
            //5
            if (base != null && base.getDvt() != null && base.getDvt().getTenDonViTinh() != null) {
                String ten = base.getDvt().getTenDonViTinh();
                baseUnitCache.put(maThuoc, ten);
                return ten;
            }
        }

        try {
            String ten = sp.getTenDVTCoBan();
            if (ten != null) {
                baseUnitCache.put(maThuoc, ten);
                return ten;
            }
        } catch (Exception ignore) {}

        baseUnitCache.put(maThuoc, "");
        return "";
    }

}
