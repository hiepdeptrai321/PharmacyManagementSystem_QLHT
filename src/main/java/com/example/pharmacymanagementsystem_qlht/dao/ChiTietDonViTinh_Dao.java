package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietDonViTinh;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDonViTinh_Dao implements DaoInterface<ChiTietDonViTinh> {
    private final String INSERT_SQL = "INSERT INTO ChiTietDonViTinh (maThuoc, maDVT, heSoQuyDoi, giaNhap, giaBan) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietDonViTinh SET heSoQuyDoi=?, giaNhap=?, giaBan=? WHERE maThuoc=? AND maDVT=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietDonViTinh WHERE maThuoc=? AND maDVT=?";
    private final String SELECT_BY_ID_SQL = "SELECT maThuoc, maDVT, heSoQuyDoi, giaNhap, giaBan FROM ChiTietDonViTinh WHERE maThuoc=? AND maDVT=?";
    private final String SELECT_ALL_SQL = "SELECT maThuoc, maDVT, heSoQuyDoi, giaNhap, giaBan FROM ChiTietDonViTinh";
    @Override
    public void insert(ChiTietDonViTinh e) {
        ConnectDB.update(INSERT_SQL, e.getThuoc().getMaThuoc(), e.getDvt().getMaDVT(), e.getHeSoQuyDoi(), e.getGiaNhap(), e.getGiaBan());
    }

    @Override
    public void update(ChiTietDonViTinh e) {
        ConnectDB.update(UPDATE_SQL, e.getHeSoQuyDoi(), e.getGiaNhap(), e.getGiaBan(), e.getThuoc().getMaThuoc(), e.getDvt().getMaDVT());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1]);
    }

    @Override
    public ChiTietDonViTinh selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1]).get(0);
    }

    @Override
    public List<ChiTietDonViTinh> selectBySql(String sql, Object... args) {
        List<ChiTietDonViTinh> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietDonViTinh ct = new ChiTietDonViTinh();
                ct.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getString("maThuoc")));
                ct.setDvt(new DonViTinh_Dao().selectById(rs.getString("maDVT")));
                ct.setHeSoQuyDoi(rs.getInt("heSoQuyDoi"));
                ct.setGiaNhap(rs.getDouble("giaNhap"));
                ct.setGiaBan(rs.getDouble("giaBan"));
                list.add(ct);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietDonViTinh> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

}
