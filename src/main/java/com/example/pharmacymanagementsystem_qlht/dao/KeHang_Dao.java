package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KeHang_Dao implements DaoInterface<KeHang> {

    private final String INSERT_SQL = "INSERT INTO KeHang VALUES (?, ?)";
    private final String UPDATE_SQL = "UPDATE KeHang SET tenKe=? WHERE maKe=?";
    private final String DELETE_BY_ID = "DELETE FROM KeHang WHERE maKe = ?";

    private final String SELECT_BY_ID = "SELECT * FROM KeHang WHERE MaKe=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM KeHang";

    @Override
    public void insert(KeHang e) {
        ConnectDB.update(INSERT_SQL, e.getMaKe(), e.getTenKe());
    }

    @Override
    public void update(KeHang e) {
        ConnectDB.update(UPDATE_SQL,e.getTenKe(),e.getMaKe());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public KeHang selectById(Object... keys) {
       List <KeHang> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List selectBySql(String sql, Object... args) {
        List <KeHang> list = new ArrayList<>();
                try {
                    ResultSet rs = ConnectDB.query(sql, args);
                    while (rs.next()){
                        KeHang keHang = new KeHang();
                        keHang.setMaKe(rs.getString("maKe"));
                        keHang.setTenKe(rs.getString("tenKe"));
                        list.add(keHang);
                    }
                    rs.getStatement().getConnection().close();
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
        return list;
    }

    @Override
    public List selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

}
