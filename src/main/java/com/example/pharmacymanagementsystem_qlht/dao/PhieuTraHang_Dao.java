package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.PhieuTraHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhieuTraHang_Dao implements DaoInterface<PhieuTraHang> {
    private final String INSERT_SQL = "INSERT INTO PhieuTraHang (MaPT, MaNV, MaKH, NgayLap, LyDoTra, GhiChu, MaHD) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuTraHang SET MaNV=?, MaKH=?, NgayLap=?, LyDoTra=?, GhiChu=?, MaHD=? WHERE MaPT=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM PhieuTraHang WHERE MaPT=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuTraHang WHERE MaPT=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuTraHang";

    @Override
    public boolean insert(PhieuTraHang e) {
        return ConnectDB.update(INSERT_SQL, e.getMaPT(), e.getNhanVien().getMaNV(), e.getKhachHang().getMaKH(), e.getNgayLap(), e.getLyDoTra(), e.getGhiChu(), e.getHoaDon().getMaHD())>0;
    }

    @Override
    public boolean update(PhieuTraHang e) {
        return ConnectDB.update(UPDATE_SQL, e.getNhanVien().getMaNV(), e.getKhachHang().getMaKH(), e.getNgayLap(), e.getLyDoTra(), e.getGhiChu(), e.getHoaDon().getMaHD(), e.getMaPT())>0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        return ConnectDB.update(DELETE_BY_ID_SQL, keys)>0;
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
                pt.setMaPT(rs.getString("MaPT"));
                pt.setNhanVien(new NhanVien_Dao().selectById(rs.getString("MaNV")));
                pt.setKhachHang(new KhachHang_Dao().selectById(rs.getString("MaKH")));
                pt.setNgayLap(rs.getTimestamp("NgayLap"));
                pt.setLyDoTra(rs.getString("LyDoTra"));
                pt.setGhiChu(rs.getString("GhiChu"));
                pt.setHoaDon(new HoaDon_Dao().selectById(rs.getString("MaHD")));
                list.add(pt);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public int countByHoaDon(String maHD) {
        String sql = "SELECT COUNT(*) FROM PhieuTraHang WHERE MaHD=?";
        try (ResultSet rs = ConnectDB.query(sql, maHD)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
    @Override
    public List<PhieuTraHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}