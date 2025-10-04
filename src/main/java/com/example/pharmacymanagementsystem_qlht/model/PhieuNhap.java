package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Timestamp;
import java.util.Objects;

public class PhieuNhap {
    private String maPN;
    private NhaCungCap nhaCungCap;
    private Timestamp ngayNhap;
    private Boolean trangThai;
    private String ghiChu;
    private NhanVien nhanVien;

    public PhieuNhap() {

    }

    public PhieuNhap(String maPN, NhaCungCap nhaCungCap, Timestamp ngayNhap, Boolean trangThai, String ghiChu, NhanVien nhanVien) {
        this.maPN = maPN;
        this.nhaCungCap = nhaCungCap;
        this.ngayNhap = ngayNhap;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.nhanVien = nhanVien;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public Timestamp getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Timestamp ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
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
        PhieuNhap phieuNhap = (PhieuNhap) o;
        return Objects.equals(maPN, phieuNhap.maPN);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maPN);
    }

    @Override
    public String toString() {
        return "PhieuNhap{" +
                "maPN='" + maPN + '\'' +
                ", nhaCungCap=" + nhaCungCap +
                ", ngayNhap=" + ngayNhap +
                ", trangThai=" + trangThai +
                ", ghiChu='" + ghiChu + '\'' +
                ", nhanVien=" + nhanVien +
                '}';
    }
}
