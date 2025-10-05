package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDatHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatHang_Dao implements DaoInterface<PhieuDatHang> {
    private final String INSERT_SQL = "INSERT INTO PhieuDatHang (maPDat, ngayLap, soTienCoc, ghiChu, maKH) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuDatHang SET ngayLap=?, soTienCoc=?, ghiChu=?, maKH=? WHERE maPDat=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM PhieuDatHang WHERE maPDat=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuDatHang WHERE maPDat=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuDatHang";

    @Override
    public void insert(PhieuDatHang e) {
        ConnectDB.update(INSERT_SQL, e.getMaPDat(), e.getNgayLap(), e.getSoTienCoc(), e.getGhiChu(), e.getKhachHang().getMaKH());
    }

    @Override
    public void update(PhieuDatHang e) {
        ConnectDB.update(UPDATE_SQL, e.getNgayLap(), e.getSoTienCoc(), e.getGhiChu(), e.getKhachHang().getMaKH(), e.getMaPDat());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys);
    }

    @Override
    public PhieuDatHang selectById(Object... keys) {
        List<PhieuDatHang> list = selectBySql(SELECT_BY_ID_SQL, keys);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<PhieuDatHang> selectBySql(String sql, Object... args) {
        List<PhieuDatHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                PhieuDatHang pdh = new PhieuDatHang();
                pdh.setMaPDat(rs.getString("maPDat"));
                pdh.setNgayLap(rs.getTimestamp("ngayLap"));
                pdh.setSoTienCoc(rs.getDouble("soTienCoc"));
                pdh.setGhiChu(rs.getString("ghiChu"));
                pdh.setKhachHang(new KhachHang_Dao().selectById(rs.getString("maKH")));
                list.add(pdh);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<PhieuDatHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}
