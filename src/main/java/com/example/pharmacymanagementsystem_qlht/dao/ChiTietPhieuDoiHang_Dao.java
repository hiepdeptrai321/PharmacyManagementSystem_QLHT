package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuTraHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDoiHang_Dao implements DaoInterface<ChiTietPhieuDoiHang> {
    private final String INSERT_SQL = "INSERT INTO ChiTietPhieuDoiHang (MaLH, MaPD, MaThuoc, SoLuong, DonGia, GiamGia) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietPhieuDoiHang SET SoLuong=?, DonGia=?, GiamGia=? WHERE MaLH=? AND MaPD=? AND MaThuoc=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietPhieuDoiHang WHERE MaLH=? AND MaPD=? AND MaThuoc=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietPhieuDoiHang WHERE MaLH=? AND maPD=? AND MaThuoc=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietPhieuDoiHang";
    private final String SELECT_BY_MAPD_SQL = "SELECT * FROM ChiTietPhieuDoiHang WHERE MaPD = ?";

    @Override
    public boolean insert(ChiTietPhieuDoiHang e) {
        return ConnectDB.update(INSERT_SQL, e.getLoHang().getMaLH(), e.getPhieuDoiHang().getMaPD(), e.getThuoc().getMaThuoc(), e.getSoLuong(), e.getDonGia(), e.getGiamGia())>0;
    }

    @Override
    public boolean update(ChiTietPhieuDoiHang e) {
        return ConnectDB.update(UPDATE_SQL, e.getSoLuong(), e.getDonGia(), e.getGiamGia(), e.getLoHang().getMaLH(), e.getPhieuDoiHang().getMaPD(), e.getThuoc().getMaThuoc())>0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        return ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1], keys[2])>0;
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
                ct.setLoHang(new Thuoc_SP_TheoLo_Dao().selectById(rs.getString("MaLH")));
                ct.setPhieuDoiHang(new PhieuDoiHang_Dao().selectById(rs.getString("MaPD")));
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
    public List<ChiTietPhieuDoiHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    public List<ChiTietPhieuDoiHang> getChiTietPhieuDoiByMaPT(String maPD) {
        return this.selectBySql(SELECT_BY_MAPD_SQL, maPD);
    }
}
