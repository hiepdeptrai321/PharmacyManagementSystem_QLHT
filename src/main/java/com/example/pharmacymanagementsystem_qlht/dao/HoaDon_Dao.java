package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoaDon;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_Dao implements DaoInterface<HoaDon>{

    private final String INSERT_SQL = "INSERT INTO HoaDon (MaHD, MaNV, NgayLap, MaKH, TrangThai) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HoaDon SET MaNV=?, NgayLap=?, MaKH=?, TrangThai=? WHERE MaHD=?";
    private final String DELETE_SQL = "DELETE FROM HoaDon WHERE MaHD=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM HoaDon";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM HoaDon WHERE MaHD=?";



    @Override
    public void insert(HoaDon e) {
        ConnectDB.update(INSERT_SQL, e.getMaHD(), e.getMaNV().getMaNV(), e.getNgayLap(), e.getMaKH().getMaKH(), e.getTrangThai());
    }

    @Override
    public void update(HoaDon e) {
        ConnectDB.update(UPDATE_SQL, e.getMaNV().getMaNV(), e.getNgayLap(), e.getMaKH().getMaKH(), e.getTrangThai(), e.getMaHD());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_SQL, keys);
    }

    @Override
    public HoaDon selectById(Object... keys) {
        List<HoaDon> list = selectBySql(SELECT_BY_ID_SQL, keys);
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    @Override
    public List<HoaDon> selectBySql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                NhanVien nv = new NhanVien_Dao().selectById(rs.getString("MaNV"));
                KhachHang kh = new KhachHang_Dao().selectById(rs.getString("MaKH"));
                hd.setMaNV(nv);
                hd.setMaKH(kh);
                hd.setNgayLap(rs.getTimestamp("NgayLap"));
                hd.setTrangThai(rs.getBoolean("TrangThai"));

                list.add(hd);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<HoaDon> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}

