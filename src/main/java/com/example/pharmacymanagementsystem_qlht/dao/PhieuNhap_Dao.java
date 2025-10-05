package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhap_Dao implements DaoInterface<PhieuNhap>{
    private final String INSERT_SQL = "INSERT INTO PhieuNhap (maPN, maNCC, ngayNhap, trangThai, ghiChu, maNV) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE PhieuNhap SET maNCC = ?, maNV = ?, ngayNhap = ?, trangThai = ?, ghiChu = ? WHERE maPN = ?";
    private final String DELETE_SQL = "DELETE FROM PhieuNhap WHERE maPN = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM PhieuNhap";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM PhieuNhap WHERE maPN = ?";

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
                pn.setMaPN(rs.getString("maPN"));
                pn.setNhaCungCap(new NhaCungCap_Dao().selectById(rs.getString("maPN")));
                pn.setNgayNhap(rs.getTimestamp("ngayNhap"));
                pn.setTrangThai(rs.getBoolean("trangThai"));
                pn.setGhiChu(rs.getString("ghiChu"));
                pn.setNhanVien(new NhanVien_Dao().selectById(rs.getString("nhanVien")));
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
}
