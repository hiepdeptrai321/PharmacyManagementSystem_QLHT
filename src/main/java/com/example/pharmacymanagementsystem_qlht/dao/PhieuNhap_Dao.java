package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhap_Dao implements DaoInterface<PhieuNhap>{
    private final String INSERT_SQL = "INSERT INTO PhieuNhap (MaPN, MaNCC, NgayNhap, TrangThai, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuNhap SET MaNCC = ?, MaNV = ?, NgayNhap = ?, TrangThai = ?, GhiChu = ? WHERE MaPN = ?";
    private final String DELETE_SQL = "DELETE FROM PhieuNhap WHERE MaPN = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuNhap";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuNhap WHERE MaPN = ?";
    private final String SELECT_TOP1_MAPN = "SELECT TOP 1 MaPN FROM PhieuNhap ORDER BY MaPN DESC";

    @Override
    public void insert(PhieuNhap e) {
        ConnectDB.update(INSERT_SQL, e.getMaPN(), e.getNhaCungCap().getMaNCC(), e.getNhanVien().getMaNV(), e.getNgayNhap(), e.getTrangThai(), e.getGhiChu());
    }

    @Override
    public void update(PhieuNhap e) {
        ConnectDB.update(UPDATE_SQL, e.getNhaCungCap().getMaNCC(), e.getNhanVien().getMaNV(), e.getNgayNhap(), e.getTrangThai(), e.getGhiChu(), e.getMaPN());
    }

    @Override
    public void deleteById(Object... keys) {
        this.selectBySql(DELETE_SQL, keys);
    }

    @Override
    public PhieuNhap selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID_SQL, keys).get(0);
    }

    @Override
    public List<PhieuNhap> selectBySql(String sql, Object... args) {
        List<PhieuNhap> phieuNhapList = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPN(rs.getString("MaPN"));
                pn.setNgayNhap(rs.getTimestamp("NgayNhap"));
                pn.setTrangThai(rs.getBoolean("TrangThai"));
                pn.setGhiChu(rs.getString("GhiChu"));
                pn.setNhaCungCap(new NhaCungCap_Dao().selectById(rs.getString("MaNCC")));
                pn.setNhanVien(new NhanVien_Dao().selectById(rs.getString("MaNV")));
                phieuNhapList.add(pn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return phieuNhapList;
    }

    @Override
    public List<PhieuNhap> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    public String generatekeyPhieuNhap() {
        String key = null;
        try {
            ResultSet rs = ConnectDB.query(SELECT_TOP1_MAPN);
            String lastKey = rs.getString("maPN");
            if (lastKey != null) {
                int numericPart = Integer.parseInt(lastKey.substring(2));
                numericPart++;
                key = String.format("PN%03d", numericPart);
            } else {
                key = "PN001";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return key;
    }
}
