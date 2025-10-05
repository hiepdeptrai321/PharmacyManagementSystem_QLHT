package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.PhieuTraHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhieuTraHang_Dao implements DaoInterface<PhieuTraHang> {
    private final String INSERT_SQL = "INSERT INTO PhieuTraHang (maPT, maNV, maKH, ngayLap, lyDoTra, ghiChu, maHD) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuTraHang SET maNV=?, maKH=?, ngayLap=?, lyDoTra=?, ghiChu=?, maHD=? WHERE maPT=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM PhieuTraHang WHERE maPT=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuTraHang WHERE maPT=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuTraHang";

    @Override
    public void insert(PhieuTraHang e) {
        ConnectDB.update(INSERT_SQL, e.getMaPT(), e.getNhanVien().getMaNV(), e.getKhachHang().getMaKH(), e.getNgayLap(), e.getLyDoTra(), e.getGhiChu(), e.getHoaDon().getMaHD());
    }

    @Override
    public void update(PhieuTraHang e) {
        ConnectDB.update(UPDATE_SQL, e.getNhanVien().getMaNV(), e.getKhachHang().getMaKH(), e.getNgayLap(), e.getLyDoTra(), e.getGhiChu(), e.getHoaDon().getMaHD(), e.getMaPT());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys);
    }

    @Override
    public PhieuTraHang selectById(Object... keys) {
        List<PhieuTraHang> list = selectBySql(SELECT_BY_ID_SQL, keys);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<PhieuTraHang> selectBySql(String sql, Object... args) {
        List<PhieuTraHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                PhieuTraHang pt = new PhieuTraHang();
                pt.setMaPT(rs.getString("maPT"));
                pt.setNhanVien(new NhanVien_Dao().selectById(rs.getString("maNV")));
                pt.setKhachHang(new KhachHang_Dao().selectById(rs.getString("maKH")));
                pt.setNgayLap(rs.getTimestamp("ngayLap"));
                pt.setLyDoTra(rs.getString("lyDoTra"));
                pt.setGhiChu(rs.getString("ghiChu"));
                pt.setHoaDon(new HoaDon_Dao().selectById(rs.getString("maHD")));
                list.add(pt);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<PhieuTraHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}