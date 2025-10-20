package com.example.pharmacymanagementsystem_qlht.model;

import java.util.ArrayList;
import java.util.List;

public class YeuCauHoaDon {

    // 1. Thông tin hóa đơn chính (Header)
    private HoaDon hoaDonChinh;

    // 2. Danh sách chi tiết các mặt hàng bán ra (Lines)
    private List<ChiTietHoaDon> danhSachChiTiet;

    // 3. Thông tin phụ (Nếu cần, ví dụ: Phương thức thanh toán)
    private String phuongThucThanhToan;

    // 4. (Tùy chọn) Danh sách sản phẩm tặng kèm sau khi áp dụng KM
    private List<Thuoc_SP_TangKem> danhSachTangKem;

    // Constructor mặc định
    public YeuCauHoaDon() {
        this.danhSachChiTiet = new ArrayList<>();
        this.danhSachTangKem = new ArrayList<>();
    }

    // Constructor đầy đủ
    public YeuCauHoaDon(HoaDon hoaDonChinh, List<ChiTietHoaDon> danhSachChiTiet, String phuongThucThanhToan) {
        this.hoaDonChinh = hoaDonChinh;
        this.danhSachChiTiet = danhSachChiTiet;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.danhSachTangKem = new ArrayList<>(); // Khởi tạo nếu chưa có
    }

    // --- Getters và Setters ---
    public HoaDon getHoaDonChinh() {
        return hoaDonChinh;
    }

    public void setHoaDonChinh(HoaDon hoaDonChinh) {
        this.hoaDonChinh = hoaDonChinh;
    }

    public List<ChiTietHoaDon> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public List<Thuoc_SP_TangKem> getDanhSachTangKem() {
        return danhSachTangKem;
    }

    public void setDanhSachTangKem(List<Thuoc_SP_TangKem> danhSachTangKem) {
        this.danhSachTangKem = danhSachTangKem;
    }

    // --- Phương thức tiện ích ---

    public double tinhTongTienTruocGiamGia() {
        if (danhSachChiTiet == null || danhSachChiTiet.isEmpty()) {
            return 0.0;
        }
        return danhSachChiTiet.stream()
                .mapToDouble(ChiTietHoaDon::tinhThanhTien)
                .sum();
    }
}