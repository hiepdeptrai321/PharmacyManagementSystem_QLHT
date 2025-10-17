package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuNhap;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuTraHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuTraHang_Dao implements DaoInterface<ChiTietPhieuTraHang> {
    private final String INSERT_SQL = "INSERT INTO ChiTietPhieuTraHang (MaLH, MaPT, MaThuoc, SoLuong, DonGia, GiamGia) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietPhieuTraHang SET SoLuong=?, DonGia=?, GiamGia=? WHERE MaLH=? AND MaPT=? AND MaThuoc=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietPhieuTraHang WHERE MaLH=? AND MaPT=? AND MaThuoc=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietPhieuTraHang WHERE MaLH=? AND MaPT=? AND MaThuoc=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietPhieuTraHang";
    private final String SELECT_BY_MAPT_SQL = "SELECT * FROM ChiTietPhieuTraHang WHERE MaPT = ?";

    @Override
    public void insert(ChiTietPhieuTraHang e) {
        ConnectDB.update(INSERT_SQL, e.getLoHang().getMaLH(), e.getPhieuTraHang().getMaPT(), e.getThuoc().getMaThuoc(), e.getSoLuong(), e.getDonGia(), e.getGiamGia());
    }

    @Override
    public void update(ChiTietPhieuTraHang e) {
        ConnectDB.update(UPDATE_SQL, e.getSoLuong(), e.getDonGia(), e.getGiamGia(), e.getLoHang().getMaLH(), e.getPhieuTraHang().getMaPT(), e.getThuoc().getMaThuoc());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1], keys[2]);
    }

    @Override
    public ChiTietPhieuTraHang selectById(Object... keys) {
        List<ChiTietPhieuTraHang> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1], keys[2]);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ChiTietPhieuTraHang> selectBySql(String sql, Object... args) {
        List<ChiTietPhieuTraHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietPhieuTraHang ct = new ChiTietPhieuTraHang();
                ct.setLoHang(new Thuoc_SP_TheoLo_Dao().selectById(rs.getString("MaLH")));
                ct.setPhieuTraHang(new PhieuTraHang_Dao().selectById(rs.getString("MaPT")));
                ct.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getString("MaThuoc")));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setGiamGia(rs.getDouble("GiamGia"));
                list.add(ct);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietPhieuTraHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    public List<ChiTietPhieuTraHang> getChiTietPhieuTraByMaPT(String maPT) {
        return this.selectBySql(SELECT_BY_MAPT_SQL, maPT);
    }
}