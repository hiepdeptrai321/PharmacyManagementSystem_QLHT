package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String email;
    private Date ngaySinh;
    private Boolean gioiTinh;
    private String diaChi;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String sdt, String email, Date ngaySinh, Boolean gioiTinh, String diaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKH, khachHang.maKH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKH);
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", sdt='" + sdt + '\'' +
                ", email='" + email + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh=" + gioiTinh +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
