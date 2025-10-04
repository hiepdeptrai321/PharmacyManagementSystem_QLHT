package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_Dao implements DaoInterface<KhachHang> {

    private final String INSERT_SQL = "INSERT INTO KhachHang (MaKH, TenKH, SDT, Email, NgaySinh, GioiTinh, DiaChi) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE KhachHang SET TenKH=?, SDT=?, Email=?, NgaySinh=?, GioiTinh=?, DiaChi=? WHERE MaKH=?";
    private final String DELETE_BY_ID = "DELETE FROM KhachHang WHERE MaKH=?";
    private final String SELECT_BY_ID = "SELECT * FROM KhachHang WHERE MaKH=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM KhachHang";

    @Override
    public void insert(KhachHang e) {
        ConnectDB.update(INSERT_SQL, e.getMaKH(), e.getTenKH(), e.getSdt(), e.getEmail(), e.getNgaySinh(), e.getGioiTinh(), e.getDiaChi());
    }

    @Override
    public void update(KhachHang e) {
        ConnectDB.update(UPDATE_SQL, e.getTenKH(), e.getSdt(), e.getEmail(), e.getNgaySinh(), e.getGioiTinh(), e.getDiaChi(), e.getMaKH());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public KhachHang selectById(Object... keys) {
        List<KhachHang> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
    }
        return list.get(0);
    }

    @Override
    public List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setNgaySinh(rs.getDate("NgaySinh"));
                kh.setGioiTinh(rs.getBoolean("GioiTinh"));
                kh.setDiaChi(rs.getString("DiaChi"));
                list.add(kh);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<KhachHang> selectAll() {
        return  this.selectBySql(SELECT_ALL_SQL);
    }
}
