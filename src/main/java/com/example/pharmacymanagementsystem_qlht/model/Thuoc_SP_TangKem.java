package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class Thuoc_SP_TangKem {
    private String maThuocTangKem;
    private KhuyenMai khuyenmai;
    private int soLuong;

    public Thuoc_SP_TangKem(String maThuocTangKem, KhuyenMai khuyenmai, int soLuong) {
        this.maThuocTangKem = maThuocTangKem;
        this.khuyenmai = khuyenmai;
        this.soLuong = soLuong;
    }

    public String getMaThuocTangKem() {
        return maThuocTangKem;
    }

    public void setMaThuocTangKem(String maThuocTangKem) {
        this.maThuocTangKem = maThuocTangKem;
    }

    public KhuyenMai getKhuyenmai() {
        return khuyenmai;
    }

    public void setKhuyenmai(KhuyenMai khuyenmai) {
        this.khuyenmai = khuyenmai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Thuoc_SP_TangKem that = (Thuoc_SP_TangKem) o;
        return Objects.equals(maThuocTangKem, that.maThuocTangKem) && Objects.equals(khuyenmai, that.khuyenmai);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThuocTangKem, khuyenmai);
    }

    @Override
    public String toString() {
        return "Thuoc_SP_TangKem{" +
                "maThuocTangKem='" + maThuocTangKem + '\'' +
                ", khuyenmai=" + khuyenmai +
                ", soLuong=" + soLuong +
                '}';
    }
}
