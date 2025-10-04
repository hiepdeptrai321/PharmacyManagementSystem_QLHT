package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class LoaiHang {
    private String maLH;
    private String tenLH;
    private String moTa;

    public LoaiHang() {
    }

    public LoaiHang(String maLH, String tenLH, String moTa) {
        this.maLH = maLH;
        this.tenLH = tenLH;
        this.moTa = moTa;
    }

    public String getMaLH() {
        return maLH;
    }

    public void setMaLH(String maLH) {
        this.maLH = maLH;
    }

    public String getTenLH() {
        return tenLH;
    }

    public void setTenLH(String tenLH) {
        this.tenLH = tenLH;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoaiHang loaiHang = (LoaiHang) o;
        return Objects.equals(maLH, loaiHang.maLH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maLH);
    }

    @Override
    public String toString() {
        return "LoaiHang{" +
                "maLH='" + maLH + '\'' +
                ", tenLH='" + tenLH + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
