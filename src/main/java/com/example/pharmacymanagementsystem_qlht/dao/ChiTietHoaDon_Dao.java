package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoaDon;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_Dao implements  DaoInterface<ChiTietHoaDon> {
    private final String INSERT_SQL = "INSERT INTO ChiTietHoaDon (MaHD, MaLH, SoLuong, DonGia, GiamGia) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietHoaDon SET SoLuong=?, DonGia=?, GiamGia=? WHERE MaHD=? AND MaLo=?";
    private final String DELETE_SQL = "DELETE FROM ChiTietHoaDon WHERE MaHD=? AND MaLo=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietHoaDon";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietHoaDon WHERE MaHD=? AND MaLo=?";

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
        ConnectDB.update(DELETE_SQL, keys[0], keys[1]);
    }

    @Override
    public ChiTietHoaDon selectById(Object... keys) {
        List<ChiTietHoaDon> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1]);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ChiTietHoaDon> selectByMaHD(String maHD) {
        return selectBySql(SELECT_BY_ID_SQL, maHD);
    }

    @Override
    public List<ChiTietHoaDon> selectBySql(String sql, Object... args) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHD(rs.getString("MaHD"));
                cthd.setHoaDon(hoaDon);

                Thuoc_SP_TheoLo loHang = new Thuoc_SP_TheoLo();
                loHang.setMaLH(rs.getString("MaLo"));
                cthd.setLoHang(loHang);

                cthd.setSoLuong(rs.getInt("SoLuong"));
                cthd.setDonGia(rs.getDouble("DonGia"));
                cthd.setGiamGia(rs.getDouble("GiamGia"));

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
