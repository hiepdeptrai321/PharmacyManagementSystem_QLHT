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
    private NhomDuocLy nhomDuocLy;
    private LoaiHang loaiHang;
    private String hinhAnh;
    private KeHang vitri;

    public Thuoc_SanPham(){
    }

    public Thuoc_SanPham(String maThuoc, String tenThuoc, int hamLuong, String donViHamLuong, String duongDung, String quyCachDongGoi, String SDK_GPNK, String hangSX, String nuocSX, NhomDuocLy nhomDuocLy, LoaiHang loaiHang, String hinhAnh, KeHang vitri) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.hamLuong = hamLuong;
        this.donViHamLuong = donViHamLuong;
        this.duongDung = duongDung;
        this.quyCachDongGoi = quyCachDongGoi;
        this.SDK_GPNK = SDK_GPNK;
        this.hangSX = hangSX;
        this.nuocSX = nuocSX;
        this.nhomDuocLy = nhomDuocLy;
        this.loaiHang = loaiHang;
        this.hinhAnh = hinhAnh;
        this.vitri = vitri;
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

    public NhomDuocLy getNhomDuocLy() {
        return nhomDuocLy;
    }

    public void setNhomDuocLy(NhomDuocLy nhomDuocLy) {
        this.nhomDuocLy = nhomDuocLy;
    }

    public LoaiHang getLoaiHang() {
        return loaiHang;
    }

    public void setLoaiHang(LoaiHang loaiHang) {
        this.loaiHang = loaiHang;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public KeHang getVitri() {
        return vitri;
    }

    public void setVitri(KeHang vitri) {
        this.vitri = vitri;
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
                ", nhomDuocLy=" + nhomDuocLy +
                ", loaiHang=" + loaiHang +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", vitri=" + vitri +
                '}';
    }

    public String getHamLuongDonVi(){
        if(donViHamLuong == null || donViHamLuong.isEmpty()){
            return String.valueOf(hamLuong);
        }
        return hamLuong + " " + donViHamLuong;
    }
}
