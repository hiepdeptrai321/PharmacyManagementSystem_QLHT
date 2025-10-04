package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private NhanVien nhanVien;
    private Timestamp ngayLap;
    private KhachHang khachHang;
    private String trangThai;
    private List<ChiTietHoaDon> chiTietHD;

    public HoaDon() {
    }

    public HoaDon(String maHD, NhanVien maNV, Timestamp ngayLap, KhachHang maKH, String trangThai) {
        this.maHD = maHD;
        this.nhanVien = maNV;
        this.ngayLap = ngayLap;
        this.khachHang = maKH;
        this.trangThai = trangThai;
    }
    public double tongHD() {
        if (chiTietHD == null || chiTietHD.isEmpty()) {
            return 0.0;
        }
        return chiTietHD.stream()
                .mapToDouble(ChiTietHoaDon::tinhThanhTien)
                .sum();
    }

    // Thêm Setter/Getter cho ChiTietHoaDon
    public List<ChiTietHoaDon> getChiTietHD() {
        return chiTietHD;
    }

    public void setChiTietHD(List<ChiTietHoaDon> chiTietHD) {
        this.chiTietHD = chiTietHD;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public NhanVien getMaNV() {
        return nhanVien;
    }

    public void setMaNV(NhanVien maNV) {
        this.nhanVien = maNV;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public KhachHang getMaKH() {
        return khachHang;
    }

    public void setMaKH(KhachHang maKH) {
        this.khachHang = maKH;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", nhanVien=" + nhanVien +
                ", ngayLap=" + ngayLap +
                ", khachHang=" + khachHang +
                ", trangThai=" + trangThai +
                '}';
    }
}
