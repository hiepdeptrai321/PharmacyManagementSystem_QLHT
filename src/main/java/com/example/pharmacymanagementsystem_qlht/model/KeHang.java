package com.example.pharmacymanagementsystem_qlht.model;

import java.util.Objects;

public class KeHang {
    private String maKe;
    private String tenKe;

    public KeHang() {
    }

    public KeHang(String maKe, String tenKe) {
        this.maKe = maKe;
        this.tenKe = tenKe;
    }

    public String getMaKe() {
        return maKe;
    }

    public void setMaKe(String maKe) {
        this.maKe = maKe;
    }

    public String getTenKe() {
        return tenKe;
    }

    public void setTenKe(String tenKe) {
        this.tenKe = tenKe;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KeHang keHang = (KeHang) o;
        return Objects.equals(maKe, keHang.maKe);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKe);
    }

    @Override
    public String toString() {
        return "keHang{" +
                "maKe='" + maKe + '\'' +
                ", tenKe='" + tenKe + '\'' +
                '}';
    }
}
