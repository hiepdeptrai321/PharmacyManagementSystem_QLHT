package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoatChat;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HoatChat_Dao implements DaoInterface<HoatChat> {
    private final String INSERT_SQL = "INSERT INTO HoatChat (TenHoatChat) VALUES (?)";
    private final String UPDATE_SQL = "UPDATE HoatChat SET tenHoatChat = ? WHERE maHoatChat = ?";
    private final String DELETE_SQL = "DELETE FROM HoatChat WHERE maHoatChat = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM HoatChat";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM HoatChat WHERE maHoatChat = ?";

    @Override
    public void insert(HoatChat e) {
        ConnectDB.update(INSERT_SQL, e.getTenHoatChat());
    }

    @Override
    public void update(HoatChat e) {
        ConnectDB.update(UPDATE_SQL, e.getMaHoatChat(), e.getTenHoatChat());
    }

    @Override
    public void deleteById(Object... keys) {
        this.selectBySql(DELETE_SQL, keys);
    }

    @Override
    public HoatChat selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID_SQL, keys).get(0);
    }

    @Override
    public List<HoatChat> selectBySql(String sql, Object... args) {
        List<HoatChat> list = new ArrayList<HoatChat>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                HoatChat hc = new HoatChat();
                hc.setTenHoatChat(rs.getString("tenHoatChat"));
                list.add(hc);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<HoatChat> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}
