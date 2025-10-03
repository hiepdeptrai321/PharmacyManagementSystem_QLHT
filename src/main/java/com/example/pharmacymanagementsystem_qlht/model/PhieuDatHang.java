package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Timestamp;
import java.util.Objects;

public class PhieuDatHang {
    private String maPDat;
    private Timestamp ngayLap;
    private int soTienCoc;
    private String ghiChu;
    private KhachHang khachHang;

    public PhieuDatHang(String maPDat, Timestamp ngayLap, int soTienCoc, String ghiChu, KhachHang khachHang) {
        this.maPDat = maPDat;
        this.ngayLap = ngayLap;
        this.soTienCoc = soTienCoc;
        this.ghiChu = ghiChu;
        this.khachHang = khachHang;
    }

    public String getMaPDat() {
        return maPDat;
    }

    public void setMaPDat(String maPDat) {
        this.maPDat = maPDat;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public int getSoTienCoc() {
        return soTienCoc;
    }

    public void setSoTienCoc(int soTienCoc) {
        this.soTienCoc = soTienCoc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhieuDatHang that = (PhieuDatHang) o;
        return Objects.equals(maPDat, that.maPDat);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maPDat);
    }

    @Override
    public String toString() {
        return "PhieuDatHang{" +
                "maPDat='" + maPDat + '\'' +
                ", ngayLap=" + ngayLap +
                ", soTienCoc=" + soTienCoc +
                ", ghiChu='" + ghiChu + '\'' +
                ", khachHang=" + khachHang +
                '}';
    }
}
