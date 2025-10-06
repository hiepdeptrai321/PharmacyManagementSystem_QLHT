package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoatChat;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoatChat_Dao implements DaoInterface<ChiTietHoatChat>{
    private final String INSERT_SQL = "INSERT INTO ChiTietHoatChat (maThuoc, maHoatChat, hamLuong) VALUES (?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietHoatChat SET hamLuong = ? WHERE maThuoc = ? AND maHoatChat = ?";
    private final String DELETE_BY_ID = "DELETE FROM ChiTietHoatChat WHERE maThuoc = ? AND maHoatChat = ?";
    private final String SELECT_BY_ID = "SELECT * FROM ChiTietHoatChat WHERE maThuoc = ? AND maHoatChat = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietHoatChat";

    @Override
    public void insert(ChiTietHoatChat e) {
        ConnectDB.update(INSERT_SQL, e.getThuoc().getMaThuoc(), e.getHoatChat().getMaHoatChat(), e.getHamLuong());
    }

    @Override
    public void update(ChiTietHoatChat e) {
        ConnectDB.update(UPDATE_SQL, e.getHamLuong(), e.getThuoc().getMaThuoc(), e.getHoatChat().getMaHoatChat());
    }

    @Override
    public void deleteById(Object... keys) {
        this.selectBySql(DELETE_BY_ID, keys[0], keys[1]);
    }

    @Override
    public ChiTietHoatChat selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID, keys[0], keys[1]).get(0);
    }

    @Override
    public List<ChiTietHoatChat> selectBySql(String sql, Object... args) {
        List<ChiTietHoatChat> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietHoatChat ct = new ChiTietHoatChat();
                ct.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getInt("maThuoc")));
                ct.setHoatChat(new HoatChat_Dao().selectById(rs.getString("maHoatChat")));
                ct.setHamLuong(rs.getFloat("hamLuong"));
                list.add(ct);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietHoatChat> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}
