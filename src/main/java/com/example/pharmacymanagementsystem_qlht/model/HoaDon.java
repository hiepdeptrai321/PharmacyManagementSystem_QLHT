package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Timestamp;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private NhanVien nhanVien;
    private double tongHD;
    private Timestamp ngayLap;
    private KhachHang khanhHang;
    private Boolean trangThai;

    public HoaDon() {
    }

    public HoaDon(String maHD, NhanVien maNV, double tongHD, Timestamp ngayLap, KhachHang maKH, Boolean trangThai) {
        this.maHD = maHD;
        this.nhanVien = maNV;
        this.tongHD = tongHD;
        this.ngayLap = ngayLap;
        this.khanhHang = maKH;
        this.trangThai = trangThai;
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

    public double getTongHD() {
        return tongHD;
    }

    public void setTongHD(double tongHD) {
        this.tongHD = tongHD;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public KhachHang getMaKH() {
        return khanhHang;
    }

    public void setMaKH(KhachHang maKH) {
        this.khanhHang = maKH;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
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
                ", tongHD=" + tongHD +
                ", ngayLap=" + ngayLap +
                ", khanhHang=" + khanhHang +
                ", trangThai=" + trangThai +
                '}';
    }
}
