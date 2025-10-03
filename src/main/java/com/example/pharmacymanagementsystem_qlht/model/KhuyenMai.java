package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class KhuyenMai {
    private String maKM;
    private LoaiKhuyenMai loaiKM;
    private String tenKM;
    private float giaTriKM;
    private String loaiGiaTri;
    private Timestamp ngayBatDau;
    private Timestamp ngayKetThuc;
    private String moTa;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, LoaiKhuyenMai loaiKM, String tenKM, float giaTriKM, String loaiGiaTri, Timestamp ngayBatDau, Timestamp ngayKetThuc, String moTa) {
        this.maKM = maKM;
        this.loaiKM = loaiKM;
        this.tenKM = tenKM;
        this.giaTriKM = giaTriKM;
        this.loaiGiaTri = loaiGiaTri;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.moTa = moTa;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public LoaiKhuyenMai getLoaiKM() {
        return loaiKM;
    }

    public void setLoaiKM(LoaiKhuyenMai loaiKM) {
        this.loaiKM = loaiKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public float getGiaTriKM() {
        return giaTriKM;
    }

    public void setGiaTriKM(float giaTriKM) {
        this.giaTriKM = giaTriKM;
    }

    public String getLoaiGiaTri() {
        return loaiGiaTri;
    }

    public void setLoaiGiaTri(String loaiGiaTri) {
        this.loaiGiaTri = loaiGiaTri;
    }

    public Timestamp getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Timestamp ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Timestamp getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Timestamp ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
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
        KhuyenMai khuyenMai = (KhuyenMai) o;
        return Objects.equals(maKM, khuyenMai.maKM);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKM);
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", loaiKM=" + loaiKM +
                ", tenKM='" + tenKM + '\'' +
                ", giaTriKM=" + giaTriKM +
                ", loaiGiaTri='" + loaiGiaTri + '\'' +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
