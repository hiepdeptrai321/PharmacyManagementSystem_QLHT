package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;

import java.util.List;

public class PhieuNhap_Dao implements DaoInterface<PhieuNhap>{
    private final String INSERT_SQL = "INSERT INTO PhieuNhap (maPN, maNCC, maNV, ngayNhap, tongTien, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuNhap SET maNCC = ?, maNV = ?, ngayNhap = ?, tongTien = ?, trangThai = ?, ghiChu = ? WHERE maPN = ?";
    private final String DELETE_SQL = "DELETE FROM PhieuNhap WHERE maPN = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuNhap";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuNhap WHERE maPN = ?";

    @Override
    public void insert(PhieuNhap e) {
        ConnectDB.update(INSERT_SQL, e.getMaPN(), e.getNhaCungCap().getMaNCC(), e.getNhanVien().getMaNV(), e.getNgayNhap(), e.getTrangThai(), e.getGhiChu());
    }

    @Override
    public void update(PhieuNhap e) {

    }

    @Override
    public void deleteById(Object... keys) {

    }

    @Override
    public PhieuNhap selectById(Object... keys) {
        return null;
    }

    @Override
    public List<PhieuNhap> selectBySql(String sql, Object... args) {
        return List.of();
    }

    @Override
    public List<PhieuNhap> selectAll() {
        return List.of();
    }
}
