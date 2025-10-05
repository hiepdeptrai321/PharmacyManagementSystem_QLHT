package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.LoaiKhuyenMai;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhuyenMai_Dao implements DaoInterface<LoaiKhuyenMai> {

    private final String SELECT_BY_ID_SQL = "SELECT maLoai, tenLoai, moTa FROM LoaiKhuyenMai WHERE maLoai = ?";
    @Override
    public void insert(LoaiKhuyenMai e) {
    }

    @Override
    public void update(LoaiKhuyenMai e) {
    }

    @Override
    public void deleteById(Object... keys) {
    }

    @Override
    public LoaiKhuyenMai selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID_SQL,keys).get(0);
    }

    @Override
    public List<LoaiKhuyenMai> selectBySql(String sql, Object... args) {
        List<LoaiKhuyenMai> loaiKhuyenMaiList = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql,args);
            while(rs.next()){
                LoaiKhuyenMai loaiKhuyenMai = new LoaiKhuyenMai();
                loaiKhuyenMai.setMaLoai(rs.getString("maLoai"));
                loaiKhuyenMai.setTenLoai(rs.getString("tenLoai"));
                loaiKhuyenMai.setMoTa(rs.getString("moTa"));
                loaiKhuyenMaiList.add(loaiKhuyenMai);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return loaiKhuyenMaiList;
    }

    @Override
    public List<LoaiKhuyenMai> selectAll() {
        return List.of();
    }
}
