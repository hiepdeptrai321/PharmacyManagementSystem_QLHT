package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietKhuyenMai_Dao implements DaoInterface<ChiTietKhuyenMai> {
    private final String INSERT_SQL = "INSERT INTO ChiTietKhuyenMai (maThuoc, maKM, slApDung, slToiDa) VALUES (?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietKhuyenMai SET slApDung=?, slToiDa=? WHERE maThuoc=? AND maKM=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietKhuyenMai WHERE maThuoc=? AND maKM=?";
    private final String SELECT_BY_ID_SQL = "SELECT maThuoc, maKM, slApDung, slToiDa FROM ChiTietKhuyenMai WHERE maThuoc=? AND maKM=?";
    private final String SELECT_ALL_SQL = "SELECT maThuoc, maKM, slApDung, slToiDa FROM ChiTietKhuyenMai";

    @Override
    public void insert(ChiTietKhuyenMai e) {
        ConnectDB.update(INSERT_SQL, e.getThuoc().getMaThuoc(), e.getKhuyenMai().getMaKM(), e.getSlApDung(), e.getSlToiDa());
    }

    @Override
    public void update(ChiTietKhuyenMai e) {
        ConnectDB.update(UPDATE_SQL, e.getSlApDung(), e.getSlToiDa(), e.getThuoc().getMaThuoc(), e.getKhuyenMai().getMaKM());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1]);
    }

    @Override
    public ChiTietKhuyenMai selectById(Object... keys) {
        List<ChiTietKhuyenMai> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1]);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ChiTietKhuyenMai> selectBySql(String sql, Object... args) {
        List<ChiTietKhuyenMai> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietKhuyenMai ct = new ChiTietKhuyenMai();
                ct.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getString("maThuoc")));
                ct.setKhuyenMai(new KhuyenMai_Dao().selectById(rs.getString("maKM")));
                ct.setSlApDung(rs.getInt("slApDung"));
                ct.setSlToiDa(rs.getInt("slToiDa"));
                list.add(ct);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietKhuyenMai> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}
