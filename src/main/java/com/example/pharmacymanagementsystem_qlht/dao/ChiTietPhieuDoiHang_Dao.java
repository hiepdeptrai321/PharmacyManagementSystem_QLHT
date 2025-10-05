package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuDoiHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDoiHang_Dao implements DaoInterface<ChiTietPhieuDoiHang> {
    private final String INSERT_SQL = "INSERT INTO ChiTietPhieuDoiHang (maLo, maPD, maThuoc, soLuong, donGia, giamGia) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietPhieuDoiHang SET soLuong=?, donGia=?, giamGia=? WHERE maLo=? AND maPD=? AND maThuoc=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietPhieuDoiHang WHERE maLo=? AND maPD=? AND maThuoc=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietPhieuDoiHang WHERE maLo=? AND maPD=? AND maThuoc=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietPhieuDoiHang";

    @Override
    public void insert(ChiTietPhieuDoiHang e) {
        ConnectDB.update(INSERT_SQL, e.getLoHang().getMaLo(), e.getPhieuDoiHang().getMaPD(), e.getThuoc().getMaThuoc(), e.getSoLuong(), e.getDonGia(), e.getGiamGia());
    }

    @Override
    public void update(ChiTietPhieuDoiHang e) {
        ConnectDB.update(UPDATE_SQL, e.getSoLuong(), e.getDonGia(), e.getGiamGia(), e.getLoHang().getMaLo(), e.getPhieuDoiHang().getMaPD(), e.getThuoc().getMaThuoc());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1], keys[2]);
    }

    @Override
    public ChiTietPhieuDoiHang selectById(Object... keys) {
        List<ChiTietPhieuDoiHang> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1], keys[2]);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ChiTietPhieuDoiHang> selectBySql(String sql, Object... args) {
        List<ChiTietPhieuDoiHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietPhieuDoiHang ct = new ChiTietPhieuDoiHang();
                ct.setLoHang(new Thuoc_SP_TheoLo_Dao().selectById(rs.getString("maLo")));
                ct.setPhieuDoiHang(new PhieuDoiHang_Dao().selectById(rs.getString("maPD")));
                ct.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getString("maThuoc")));
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setDonGia(rs.getDouble("donGia"));
                ct.setGiamGia(rs.getDouble("giamGia"));

                list.add(ct);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietPhieuDoiHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}
