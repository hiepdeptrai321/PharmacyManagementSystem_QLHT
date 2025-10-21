package com.example.pharmacymanagementsystem_qlht.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class KhuyenMai {
    private String maKM;
    private LoaiKhuyenMai loaiKM;
    private String tenKM;
    private float giaTriKM;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String moTa;
    private Timestamp ngayTao;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, LoaiKhuyenMai loaiKM, String tenKM, float giaTriKM, Date ngayBatDau, Date ngayKetThuc, String moTa) {
        this.maKM = maKM;
        this.loaiKM = loaiKM;
        this.tenKM = tenKM;
        this.giaTriKM = giaTriKM;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.moTa = moTa;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {this.maKM = maKM; }

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

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
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
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
