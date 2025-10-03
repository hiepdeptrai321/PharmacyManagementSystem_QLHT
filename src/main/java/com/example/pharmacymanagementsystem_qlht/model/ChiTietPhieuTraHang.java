package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class ChiTietPhieuTraHang {
    private Thuoc_SP_TheoLo loHang;
    private PhieuTraHang phieuTraHang;
    private  Thuoc_SanPham thuoc;
    private int soLuong;
    private double donGia;
    private double giamGia;
    public ChiTietPhieuTraHang() {
    }

    public ChiTietPhieuTraHang(Thuoc_SP_TheoLo loHang, PhieuTraHang phieuTraHang, Thuoc_SanPham thuoc, int soLuong, double donGia, double giamGia) {
        this.loHang = loHang;
        this.phieuTraHang = phieuTraHang;
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

    public PhieuTraHang getPhieuTraHang() {
        return phieuTraHang;
    }

    public void setPhieuTraHang(PhieuTraHang phieuTraHang) {
        this.phieuTraHang = phieuTraHang;
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
        ChiTietPhieuTraHang that = (ChiTietPhieuTraHang) o;
        return Objects.equals(loHang, that.loHang) && Objects.equals(phieuTraHang, that.phieuTraHang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loHang, phieuTraHang);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuTraHang{" +
                "loHang=" + loHang +
                ", phieuTraHang=" + phieuTraHang +
                ", thuoc=" + thuoc +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", giamGia=" + giamGia +
                '}';
    }
}
