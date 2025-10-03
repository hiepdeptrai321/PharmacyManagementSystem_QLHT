package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class Thuoc_SanPham {
    private String maThuoc;
    private String tenThuoc;
    private int hamLuong;
    private String donViHamLuong;
    private String duongDung;
    private String quyCachDongGoi;
    private String SDK_GPNK;
    private String hangSX;
    private String nuocSX;
//    private String maNDL;
//    private String maLH;
    private String hinhAnh;
//    private String vitri;


    public Thuoc_SanPham(String maThuoc, String tenThuoc, int hamLuong, String donViHamLuong, String duongDung, String quyCachDongGoi, String SDK_GPNK, String hangSX, String nuocSX, String hinhAnh) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.hamLuong = hamLuong;
        this.donViHamLuong = donViHamLuong;
        this.duongDung = duongDung;
        this.quyCachDongGoi = quyCachDongGoi;
        this.SDK_GPNK = SDK_GPNK;
        this.hangSX = hangSX;
        this.nuocSX = nuocSX;
        this.hinhAnh = hinhAnh;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public int getHamLuong() {
        return hamLuong;
    }

    public void setHamLuong(int hamLuong) {
        this.hamLuong = hamLuong;
    }

    public String getDonViHamLuong() {
        return donViHamLuong;
    }

    public void setDonViHamLuong(String donViHamLuong) {
        this.donViHamLuong = donViHamLuong;
    }

    public String getDuongDung() {
        return duongDung;
    }

    public void setDuongDung(String duongDung) {
        this.duongDung = duongDung;
    }

    public String getQuyCachDongGoi() {
        return quyCachDongGoi;
    }

    public void setQuyCachDongGoi(String quyCachDongGoi) {
        this.quyCachDongGoi = quyCachDongGoi;
    }

    public String getSDK_GPNK() {
        return SDK_GPNK;
    }

    public void setSDK_GPNK(String SDK_GPNK) {
        this.SDK_GPNK = SDK_GPNK;
    }

    public String getHangSX() {
        return hangSX;
    }

    public void setHangSX(String hangSX) {
        this.hangSX = hangSX;
    }

    public String getNuocSX() {
        return nuocSX;
    }

    public void setNuocSX(String nuocSX) {
        this.nuocSX = nuocSX;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Thuoc_SanPham that = (Thuoc_SanPham) o;
        return Objects.equals(maThuoc, that.maThuoc);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maThuoc);
    }

    @Override
    public String toString() {
        return "Thuoc_SanPham{" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", hamLuong=" + hamLuong +
                ", donViHamLuong='" + donViHamLuong + '\'' +
                ", duongDung='" + duongDung + '\'' +
                ", quyCachDongGoi='" + quyCachDongGoi + '\'' +
                ", SDK_GPNK='" + SDK_GPNK + '\'' +
                ", hangSX='" + hangSX + '\'' +
                ", nuocSX='" + nuocSX + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                '}';
    }
}
