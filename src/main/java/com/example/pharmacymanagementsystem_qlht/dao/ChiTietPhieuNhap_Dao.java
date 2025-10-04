package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuNhap;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhap_Dao implements DaoInterface<ChiTietPhieuNhap> {
    private final String INSERT_SQL = "INSERT INTO ChiTietPhieuNhap (MaPN, MaThuoc, MaLH, SoLuong, GiaNhap, ChietKhau, Thue) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietPhieuNhap SET SoLuong = ?, GiaNhap = ?, ChietKhau = ?, Thue = ? WHERE MaPN = ? AND MaThuoc = ? AND MaLH = ?";
    private final String DELETE_SQL = "DELETE FROM ChiTietPhieuNhap WHERE MaPN = ? AND MaThuoc = ? AND MaLH = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietPhieuNhap";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN = ? AND MaThuoc = ? AND MaLH = ?";

    @Override
    public void insert(ChiTietPhieuNhap e) {
        ConnectDB.update(INSERT_SQL, e.getPhieuNhap().getMaPN(), e.getThuoc().getMaThuoc(), e.getLoHang().getMaLH(), e.getSoLuong(), e.getGiaNhap(),e.getChietKhau(),e.getThue());
    }

    @Override
    public void update(ChiTietPhieuNhap e) {

    }

    @Override
    public void deleteById(Object... keys) {

    }

    @Override
    public ChiTietPhieuNhap selectById(Object... keys) {
        return null;
    }

    @Override
    public List<ChiTietPhieuNhap> selectBySql(String sql, Object... args) {
        return List.of();
    }

    @Override
    public List<ChiTietPhieuNhap> selectAll() {
        return List.of();
    }
}
