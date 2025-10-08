package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.LoaiHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LoaiHang_Dao implements DaoInterface<LoaiHang>{
    private final String INSERT_SQL = "INSERT INTO LoaiHang(MaLoaiHang, TenLH, MoTa) VALUES (?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE LoaiHang SET TenLH=?, MoTa=? WHERE MaLoaiHang=?";
    private final String DELETE_BY_ID = "DELETE FROM LoaiHang WHERE MaLoaiHang = ?";
    private final String SELECT_BY_ID = "SELECT * FROM LoaiHang WHERE MaLH=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM LoaiHang";

    @Override
    public void insert(LoaiHang e) {
        ConnectDB.update(INSERT_SQL, e.getMaLoaiHang(), e.getTenLoaiHang(), e.getMoTa());
    }

    @Override
    public void update(LoaiHang e) {
        ConnectDB.update(UPDATE_SQL, e.getTenLoaiHang(), e.getMoTa(), e.getMaLoaiHang());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public LoaiHang selectById(Object... keys) {
        List<LoaiHang> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<LoaiHang> selectBySql(String sql, Object... args) {
        List<LoaiHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                LoaiHang lh = new LoaiHang();
                lh.setMaLoaiHang(rs.getString("MaLoaiHang"));
                lh.setTenLoaiHang(rs.getString("TenLH"));
                lh.setMoTa(rs.getString("MoTa"));
                list.add(lh);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<LoaiHang> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}


