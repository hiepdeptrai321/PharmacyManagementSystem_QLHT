package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Timestamp;
import java.util.Objects;

public class LuongNhanVien {
    private String maLNV;
    private Timestamp tuNgay;
    private Timestamp denNgay;
    private double luongCoBan;
    private double phuCap;
    private String ghiChu;
    private NhanVien nhanVien;

    public LuongNhanVien() {
    }

    public LuongNhanVien(String maLNV, Timestamp tuNgay, Timestamp denNgay, double luongCoBan, double phuCap, String ghiChu, NhanVien nhanVien) {
        this.maLNV = maLNV;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.ghiChu = ghiChu;
        this.nhanVien = nhanVien;
    }

    public String getMaLNV() {
        return maLNV;
    }

    public void setMaLNV(String maLNV) {
        this.maLNV = maLNV;
    }

    public Timestamp getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(Timestamp tuNgay) {
        this.tuNgay = tuNgay;
    }

    public Timestamp getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(Timestamp denNgay) {
        this.denNgay = denNgay;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public double getPhuCap() {
        return phuCap;
    }

    public void setPhuCap(double phuCap) {
        this.phuCap = phuCap;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LuongNhanVien that = (LuongNhanVien) o;
        return Objects.equals(maLNV, that.maLNV);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maLNV);
    }

    @Override
    public String toString() {
        return "LuongNhanVien{" +
                "maLNV='" + maLNV + '\'' +
                ", tuNgay=" + tuNgay +
                ", denNgay=" + denNgay +
                ", luongCoBan=" + luongCoBan +
                ", phuCap=" + phuCap +
                ", ghiChu='" + ghiChu + '\'' +
                ", nhanVien=" + nhanVien +
                '}';
    }
}
