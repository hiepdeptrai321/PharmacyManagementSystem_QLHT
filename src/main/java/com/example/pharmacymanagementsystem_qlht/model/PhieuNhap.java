package com.example.pharmacymanagementsystem_qlht.model;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietPhieuNhap_Dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PhieuNhap {
    private String maPN;
    private NhaCungCap nhaCungCap;
    private Date ngayNhap;
    private Boolean trangThai;
    private String ghiChu;
    private NhanVien nhanVien;
    private Double TongTien;

    public PhieuNhap() {

    }

    public PhieuNhap(String maPN, NhaCungCap nhaCungCap, Date ngayNhap, Boolean trangThai, String ghiChu, NhanVien nhanVien) {
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

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
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

    public double getTongTien() {
        List<ChiTietPhieuNhap> list = new ChiTietPhieuNhap_Dao().getChiTietPhieuNhapByMaPN(this.getMaPN());
        double tong = 0;
        for (ChiTietPhieuNhap ctpn : list) {
            tong += ctpn.getSoLuong() * ctpn.getGiaNhap() * (1 - ctpn.getChietKhau() / 100) * (1 + ctpn.getThue() / 100);
        }
        return tong;
    }
}
