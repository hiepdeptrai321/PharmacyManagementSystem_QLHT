package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class ChiTietPhieuNhap {
    private PhieuNhap phieuNhap;
    private Thuoc_SanPham Thuoc;
    private Thuoc_SP_TheoLo loHang;
    private int soLuong;
    private double giaNhap;
    private float chietKhau;
    private float thue;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(PhieuNhap phieuNhap, Thuoc_SanPham Thuoc, Thuoc_SP_TheoLo loHang, int soLuong, double giaNhap, float chietKhau, float thue) {
        this.phieuNhap = phieuNhap;
        this.Thuoc = Thuoc;
        this.loHang = loHang;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.chietKhau = chietKhau;
        this.thue = thue;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public Thuoc_SanPham getThuoc() {
        return Thuoc;
    }

    public void setThuoc(Thuoc_SanPham Thuoc) {
        this.Thuoc = Thuoc;
    }

    public Thuoc_SP_TheoLo getLoHang() {
        return loHang;
    }

    public void setLoHang(Thuoc_SP_TheoLo loHang) {
        this.loHang = loHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public float getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(float chietKhau) {
        this.chietKhau = chietKhau;
    }

    public float getThue() {
        return thue;
    }

    public void setThue(float thue) {
        this.thue = thue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietPhieuNhap that = (ChiTietPhieuNhap) o;
        return Objects.equals(phieuNhap, that.phieuNhap) && Objects.equals(Thuoc, that.Thuoc) && Objects.equals(loHang, that.loHang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phieuNhap, Thuoc, loHang);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "phieuNhap=" + phieuNhap +
                ", maThuoc=" + Thuoc +
                ", loHang=" + loHang +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                ", chietKhau=" + chietKhau +
                ", thue=" + thue +
                '}';
    }
}
