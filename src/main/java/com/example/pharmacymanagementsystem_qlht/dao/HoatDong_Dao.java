package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoatDong;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HoatDong_Dao implements DaoInterface<HoatDong> {

    private final String INSERT_SQL = "INSERT INTO HoatDong(MaHDong, LoaiHD, ThoiGian, MaNV, BangDL, GhiChu) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HoatDong SET LoaiHD=?, ThoiGian=?, MaNV=?, BangDL=?,GhiChu=? WHERE maHD=?";
    private final String DELETE_BY_ID = "DELETE FROM HoatDong WHERE MaHDong = ?";
    private final String SELECT_BY_ID = "SELECT * FROM HoatDong WHERE MaHDong=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM HoatDong";

    @Override
    public void insert(HoatDong e) {
        ConnectDB.update(INSERT_SQL, e.getLoaiHD(), e.getBang(), e.getThoiGian(), e.getGhiChu(), e.getNhanVien().getMaNV(), e.getMaHD());
    }

    @Override
    public void update(HoatDong e) {
        ConnectDB.update(UPDATE_SQL, e.getLoaiHD(), e.getBang(), e.getThoiGian(), e.getGhiChu(), e.getMaHD());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public HoatDong selectById(Object... keys) {
        List<HoatDong> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HoatDong> selectBySql(String sql, Object... args) {
        List<HoatDong> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                HoatDong hd = new HoatDong();
                hd.setMaHD(rs.getString("MaHDong"));
                hd.setLoaiHD(rs.getString("LoaiHD"));
                hd.setBang(rs.getString("BangDL"));
                hd.setThoiGian(rs.getTimestamp("ThoiGian"));
                hd.setGhiChu(rs.getString("GhiChu"));
                hd.setNhanVien(new NhanVien_Dao().selectById(rs.getString("MaNV")));
                list.add(hd);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<HoatDong> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}



