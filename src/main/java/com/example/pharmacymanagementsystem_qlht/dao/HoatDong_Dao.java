package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoatDong;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HoatDong_Dao implements DaoInterface<HoatDong> {

    private final String INSERT_SQL = "INSERT INTO HoatDong VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HoatDong SET loaiHD=?, bang=?, thoiGian=?, ghiChu=? WHERE maHD=?";
    private final String DELETE_BY_ID = "DELETE FROM HoatDong WHERE maHD = ?";
    private final String SELECT_BY_ID = "SELECT * FROM HoatDong WHERE maHD=?";
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
                hd.setMaHD(rs.getString("maHD"));
                hd.setLoaiHD(rs.getString("loaiHD"));
                hd.setBang(rs.getString("bang"));
                hd.setThoiGian(rs.getTimestamp("thoiGian"));
                hd.setGhiChu(rs.getString("ghiChu"));
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



