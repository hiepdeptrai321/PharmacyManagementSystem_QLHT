package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Thuoc_SP_TheoLo loHang;
    private int soLuong;
    private DonViTinh dvt;
    private double donGia;
    private double giamGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, Thuoc_SP_TheoLo loHang, int soLuong, DonViTinh dvt, double donGia, double giamGia) {
        this.hoaDon = hoaDon;
        this.loHang = loHang;
        this.soLuong = soLuong;
        this.dvt = dvt;
        this.donGia = donGia;
        this.giamGia = giamGia;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
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
    public DonViTinh getDvt() {
        return dvt;
    }

    public void setDvt(DonViTinh dvt) {
        this.dvt = dvt;
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

    public double tinhThanhTien() {
        double thanhTien = this.soLuong * this.donGia - this.giamGia;
        return thanhTien;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(hoaDon, that.hoaDon) &&
                Objects.equals(loHang, that.loHang) &&
                Objects.equals(dvt, that.dvt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, loHang, dvt);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "hoaDon=" + hoaDon +
                ", loHang=" + loHang +
                ", dvt=" + dvt +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", giamGia=" + giamGia +
                '}';
    }
}