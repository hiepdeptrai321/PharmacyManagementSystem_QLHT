package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_Dao implements DaoInterface<KhuyenMai>{
    private final String INSERT_SQL = "INSERT INTO KhuyenMai (maKM, loaiKM, tenKM, giaTriKM, ngayBatDau, ngayKetThuc, moTa) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE KhuyenMai SET loaiKM=?, tenKM=?, giaTriKM=?, ngayBatDau=?, ngayKetThuc=?, moTa=? WHERE maKM=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM KhuyenMai WHERE maKM=?";
    private final String SELECT_BY_ID_SQL = "SELECT maKM, loaiKM, tenKM, giaTriKM, ngayBatDau, ngayKetThuc, moTa FROM LoaiKhuyenMai WHERE maLoaiKM = ?";
    private final String SELECT_ALL_SQL = "SELECT maKM, loaiKM, tenKM, giaTriKM, ngayBatDau, ngayKetThuc, moTa FROM LoaiKhuyenMai";
    @Override
    public void insert(KhuyenMai e) {
        ConnectDB.update(INSERT_SQL, e.getMaKM(), e.getLoaiKM(), e.getTenKM(), e.getGiaTriKM(), e.getNgayBatDau(), e.getNgayKetThuc(), e.getMoTa());
    }

    @Override
    public void update(KhuyenMai e) {
        ConnectDB.update(UPDATE_SQL, e.getLoaiKM(), e.getTenKM(), e.getGiaTriKM(), e.getNgayBatDau(), e.getNgayKetThuc(), e.getMoTa(), e.getMaKM());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys);
    }

    @Override
    public KhuyenMai selectById(Object... keys) {
        return this.selectById(SELECT_BY_ID_SQL, keys);
    }

    @Override
    public List<KhuyenMai> selectBySql(String sql, Object... args) {
        List<KhuyenMai> khuyenMaiList = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql,args);
            while(rs.next()){
                KhuyenMai khuyenMai = new KhuyenMai();
                khuyenMai.setMaKM(rs.getString("maKM"));
                khuyenMai.setLoaiKM(new LoaiKhuyenMai_Dao().selectById(rs.getString("loaiKM")));
                khuyenMai.setTenKM(rs.getString("tenKM"));
                khuyenMai.setGiaTriKM(rs.getFloat("giaTriKM"));
                khuyenMai.setNgayBatDau(rs.getDate("ngayBatDau"));
                khuyenMai.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                khuyenMai.setMoTa(rs.getString("moTa"));
                khuyenMaiList.add(khuyenMai);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return khuyenMaiList;
    }

    @Override
    public List<KhuyenMai> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}
