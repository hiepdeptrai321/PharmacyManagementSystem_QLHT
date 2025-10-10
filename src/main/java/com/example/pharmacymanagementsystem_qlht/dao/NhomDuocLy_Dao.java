package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.NhomDuocLy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhomDuocLy_Dao implements DaoInterface<NhomDuocLy> {

    private final String INSERT_SQL = "INSERT INTO NhomDuocLy(MaNDL, TenNDL, Mota) VALUES (?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE NhomDuocLy SET TenNDL=?, MoTa=? WHERE MaNDL=?";
    private final String DELETE_BY_ID = "DELETE FROM NhomDuocLy WHERE MaNDL = ?";
    private final String SELECT_BY_ID = "SELECT * FROM NhomDuocLy WHERE MaNDL=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM NhomDuocLy";

    @Override
    public void insert(NhomDuocLy e) {
        ConnectDB.update(INSERT_SQL, e.getMaNDL(), e.getTenNDL(), e.getMoTa());
    }

    @Override
    public void update(NhomDuocLy e) {
    ConnectDB.update(UPDATE_SQL, e.getTenNDL(), e.getMoTa(), e.getMaNDL());
    }

    @Override
    public void deleteById(Object... keys) {
    ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public NhomDuocLy selectById(Object... keys) {
        List<NhomDuocLy> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhomDuocLy> selectBySql(String sql, Object... args) {
        List<NhomDuocLy> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                NhomDuocLy ndl = new NhomDuocLy();
                ndl.setMaNDL(rs.getString("MaNDL"));
                ndl.setTenNDL(rs.getString("TenNDL"));
                ndl.setMoTa(rs.getString("MoTa"));
                list.add(ndl);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    @Override
    public List<NhomDuocLy> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}
