package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.LuongNhanVien;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LuongNhanVien_Dao implements DaoInterface<LuongNhanVien> {

    private final String INSERT_SQL = "INSERT INTO LuongNhanVien VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE LuongNhanVien SET tuNgay=?, denNgay=?, luongCoBan=?, phuCap=?, ghiChu=? WHERE maLNV=?";
    private final String DELETE_BY_ID = "DELETE FROM LuongNhanVien WHERE maLNV = ?";
    private final String SELECT_BY_ID = "SELECT * FROM LuongNhanVien WHERE maLNV=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM LuongNhanVien";

    @Override
    public void insert(LuongNhanVien e) {
        ConnectDB.update(INSERT_SQL, e.getMaLNV(), e.getTuNgay(), e.getDenNgay(), e.getLuongCoBan(), e.getPhuCap(), e.getGhiChu(), e.getNhanVien().getMaNV());
    }

    @Override
    public void update(LuongNhanVien e) {
        ConnectDB.update(UPDATE_SQL, e.getTuNgay(), e.getDenNgay(), e.getLuongCoBan(), e.getPhuCap(), e.getGhiChu(), e.getMaLNV());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public LuongNhanVien selectById(Object... keys) {
        List<LuongNhanVien> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<LuongNhanVien> selectBySql(String sql, Object... args) {
        List<LuongNhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                LuongNhanVien lnv = new LuongNhanVien();
                lnv.setMaLNV(rs.getString("maLNV"));
                lnv.setTuNgay(rs.getDate("tuNgay"));
                lnv.setDenNgay(rs.getDate("denNgay"));
                lnv.setLuongCoBan(rs.getDouble("luongCoBan"));
                lnv.setPhuCap(rs.getDouble("phuCap"));
                lnv.setGhiChu(rs.getString("ghiChu"));
                lnv.setNhanVien(new NhanVien_Dao().selectById(rs.getString("MaNV")));
                list.add(lnv);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<LuongNhanVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}