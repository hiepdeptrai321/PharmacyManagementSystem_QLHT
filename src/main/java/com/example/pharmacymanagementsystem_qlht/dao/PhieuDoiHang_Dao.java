package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.PhieuDoiHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhieuDoiHang_Dao implements DaoInterface<PhieuDoiHang> {
    private final String INSERT_SQL = "INSERT INTO PhieuDoiHang (maPD, maNV, maKH, ngayLap, lyDoDoi, ghiChu, maHD) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuDoiHang SET maNV=?, maKH=?, ngayLap=?, lyDoDoi=?, ghiChu=?, maHD=? WHERE maPD=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM PhieuDoiHang WHERE maPD=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuDoiHang WHERE maPD=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuDoiHang";

    @Override
    public void insert(PhieuDoiHang e) {
        ConnectDB.update(INSERT_SQL, e.getMaPD(), e.getNhanVien().getMaNV(), e.getKhachHang().getMaKH(), e.getNgayLap(), e.getLyDoDoi(), e.getGhiChu(), e.getHoaDon().getMaHD());
    }

    @Override
    public void update(PhieuDoiHang e) {
        ConnectDB.update(UPDATE_SQL, e.getNhanVien().getMaNV(), e.getKhachHang().getMaKH(), e.getNgayLap(), e.getLyDoDoi(), e.getGhiChu(), e.getHoaDon().getMaHD(), e.getMaPD());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys);
    }

    @Override
    public PhieuDoiHang selectById(Object... keys) {
        List<PhieuDoiHang> list = selectBySql(SELECT_BY_ID_SQL, keys);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<PhieuDoiHang> selectBySql(String sql, Object... args) {
        List<PhieuDoiHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                PhieuDoiHang pdh = new PhieuDoiHang();
                pdh.setMaPD(rs.getString("maPD"));
                pdh.setNhanVien(new NhanVien_Dao().selectById(rs.getString("maNV")));
                pdh.setKhachHang(new KhachHang_Dao().selectById(rs.getString("maKH")));
                pdh.setNgayLap(rs.getTimestamp("ngayLap"));
                pdh.setLyDoDoi(rs.getString("lyDoDoi"));
                pdh.setGhiChu(rs.getString("ghiChu"));
                pdh.setHoaDon(new HoaDon_Dao().selectById(rs.getString("maHD")));
                list.add(pdh);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<PhieuDoiHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}
