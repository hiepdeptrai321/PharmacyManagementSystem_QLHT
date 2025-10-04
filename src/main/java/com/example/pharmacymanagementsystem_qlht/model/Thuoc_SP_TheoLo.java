package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Thuoc_SP_TheoLo {
    private String maLH;
    private int soLuongTon;
    private Timestamp nsx;
    private Timestamp hsd;
    private ChiTietPhieuNhap phieunhap;
    private Thuoc_SanPham thuoc;

    public Thuoc_SP_TheoLo(String maLH, int soLuongTon, Timestamp nsx, Timestamp hsd, ChiTietPhieuNhap phieunhap, Thuoc_SanPham thuoc) {
        this.maLH = maLH;
        this.soLuongTon = soLuongTon;
        this.nsx = nsx;
        this.hsd = hsd;
        this.phieunhap = phieunhap;
        this.thuoc = thuoc;
    }

    public String getMaLH() {
        return maLH;
    }

    public void setMaLH(String maLH) {
        this.maLH = maLH;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public Timestamp getNsx() {
        return nsx;
    }

    public void setNsx(Timestamp nsx) {
        this.nsx = nsx;
    }

    public Timestamp getHsd() {
        return hsd;
    }

    public void setHsd(Timestamp hsd) {
        this.hsd = hsd;
    }

    public ChiTietPhieuNhap getPhieunhap() {
        return phieunhap;
    }

    public void setPhieunhap(ChiTietPhieuNhap phieunhap) {
        this.phieunhap = phieunhap;
    }

    public Thuoc_SanPham getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc_SanPham thuoc) {
        this.thuoc = thuoc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Thuoc_SP_TheoLo that = (Thuoc_SP_TheoLo) o;
        return Objects.equals(maLH, that.maLH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maLH);
    }

    @Override
    public String toString() {
        return "Thuoc_SP_TheoLo{" +
                "maLH='" + maLH + '\'' +
                ", soLuongTon=" + soLuongTon +
                ", nsx=" + nsx +
                ", hsd=" + hsd +
                ", phieunhap=" + phieunhap +
                ", thuoc=" + thuoc +
                '}';
    }
}
