package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class ChiTietPhieuDoiHang {
    private Thuoc_SP_TheoLo loHang;
    private PhieuDoiHang phieuDoiHang;
    private Thuoc_SanPham thuoc;
    private int soLuong;
    private double donGia;
    private double giamGia;

    public ChiTietPhieuDoiHang() {
    }

    public ChiTietPhieuDoiHang(Thuoc_SP_TheoLo loHang, PhieuDoiHang phieuDoiHang, Thuoc_SanPham thuoc, int soLuong, double donGia, double giamGia) {
        this.loHang = loHang;
        this.phieuDoiHang = phieuDoiHang;
        this.thuoc = thuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.giamGia = giamGia;
    }

    public Thuoc_SP_TheoLo getLoHang() {
        return loHang;
    }

    public void setLoHang(Thuoc_SP_TheoLo loHang) {
        this.loHang = loHang;
    }

    public PhieuDoiHang getPhieuDoiHang() {
        return phieuDoiHang;
    }

    public void setPhieuDoiHang(PhieuDoiHang phieuDoiHang) {
        this.phieuDoiHang = phieuDoiHang;
    }

    public Thuoc_SanPham getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc_SanPham thuoc) {
        this.thuoc = thuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietPhieuDoiHang that = (ChiTietPhieuDoiHang) o;
        return Objects.equals(loHang, that.loHang) && Objects.equals(phieuDoiHang, that.phieuDoiHang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loHang, phieuDoiHang);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDoiHang{" +
                "loHang=" + loHang +
                ", phieuDoiHang=" + phieuDoiHang +
                ", thuoc=" + thuoc +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", giamGia=" + giamGia +
                '}';
    }
}
