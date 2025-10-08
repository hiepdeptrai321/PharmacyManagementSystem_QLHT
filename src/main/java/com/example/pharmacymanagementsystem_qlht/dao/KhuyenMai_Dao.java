package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_Dao implements DaoInterface<KhuyenMai> {
    private final String INSERT_SQL = "INSERT INTO KhuyenMai (MaLoai, TenKM, GiaTriKM, NgayBatDau, NgayKetThuc, MoTa) VALUES ( ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE KhuyenMai SET MaLoai=?, TenKM=?, GiaTriKM=?, NgayBatDau=?, NgayKetThuc=?, MoTa=? WHERE MaKM=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM KhuyenMai WHERE MaKM=?";
    private final String SELECT_BY_ID_SQL = "SELECT MaKM, MaLoai, TenKM, GiaTriKM, NgayBatDau, NgayKetThuc, MoTa FROM KhuyenMai WHERE MaKM = ?";
    private final String SELECT_ALL_SQL = "SELECT MaKM, MaLoai, TenKM, GiaTriKM, NgayBatDau, NgayKetThuc, MoTa FROM KhuyenMai";

    @Override
    public void insert(KhuyenMai e) {
        ConnectDB.update(INSERT_SQL, e.getLoaiKM(), e.getTenKM(), e.getGiaTriKM(), e.getNgayBatDau(), e.getNgayKetThuc(), e.getMoTa());
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
        return this.selectBySql(SELECT_BY_ID_SQL, keys).get(0);
    }

    @Override
    public List<KhuyenMai> selectBySql(String sql, Object... args) {
        List<KhuyenMai> khuyenMaiList = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai();
                khuyenMai.setMaKM(rs.getString("MaKM"));
                khuyenMai.setLoaiKM(new LoaiKhuyenMai_Dao().selectById(rs.getString("MaLoai")));
                khuyenMai.setTenKM(rs.getString("TenKM"));
                khuyenMai.setGiaTriKM(rs.getFloat("GiaTriKM"));
                khuyenMai.setNgayBatDau(rs.getDate("NgayBatDau"));
                khuyenMai.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                khuyenMai.setMoTa(rs.getString("MoTa"));
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

    public String generateNewMaKM() {
        String newMaKM = "KM001"; // Default value if no records exist
        String SELECT_TOP1_SQL = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY MaKM DESC";
        try {
            ResultSet rs = ConnectDB.query(SELECT_TOP1_SQL);
            if (rs.next()) {
                String lastMaKM = rs.getString("maKM");
                int stt = Integer.parseInt(lastMaKM.substring(2)); // Extract numeric part
                stt++; // Increment the numeric part
                newMaKM = String.format("KM%03d", stt); // Format with leading zeros
            }
            rs.getStatement().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newMaKM;
    }
}
