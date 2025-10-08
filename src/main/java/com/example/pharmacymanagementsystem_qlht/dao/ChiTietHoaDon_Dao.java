package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoaDon;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_Dao implements DaoInterface<ChiTietHoaDon> {
    private final String INSERT_SQL = "INSERT INTO ChiTietHoaDon (maHD, maLo, soLuong, donGia, giamGia) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietHoaDon SET soLuong=?, donGia=?, giamGia=? WHERE maHD=? AND maLo=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietHoaDon WHERE maHD=? AND maLo=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietHoaDon WHERE maHD=? AND maLo=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietHoaDon";
    private final String SELECT_BY_MAHD_SQL = "SELECT * FROM ChiTietHoaDon WHERE maHD=?";

    @Override
    public void insert(ChiTietHoaDon e) {
        ConnectDB.update(INSERT_SQL, e.getHoaDon().getMaHD(), e.getLoHang().getMaLH(), e.getSoLuong(), e.getDonGia(), e.getGiamGia());
    }

    @Override
    public void update(ChiTietHoaDon e) {
        ConnectDB.update(UPDATE_SQL, e.getSoLuong(), e.getDonGia(), e.getGiamGia(), e.getHoaDon().getMaHD(), e.getLoHang().getMaLH());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1]);
    }

    @Override
    public ChiTietHoaDon selectById(Object... keys) {
        List<ChiTietHoaDon> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1]);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ChiTietHoaDon> selectByMaHD(String maHD) {
        return selectBySql(SELECT_BY_MAHD_SQL, maHD);
    }

    @Override
    public List<ChiTietHoaDon> selectBySql(String sql, Object... args) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHD(rs.getString("maHD"));
                cthd.setHoaDon(hoaDon);

                Thuoc_SP_TheoLo loHang = new Thuoc_SP_TheoLo();
                loHang.setMaLH(rs.getString("maLo"));
                cthd.setLoHang(loHang);

                cthd.setSoLuong(rs.getInt("soLuong"));
                cthd.setDonGia(rs.getDouble("donGia"));
                cthd.setGiamGia(rs.getDouble("giamGia"));

                list.add(cthd);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietHoaDon> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}
