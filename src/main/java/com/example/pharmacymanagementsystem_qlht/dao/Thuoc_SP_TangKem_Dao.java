package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TangKem;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_SP_TangKem_Dao implements DaoInterface<Thuoc_SP_TangKem> {
    private final String INSERT_SQL = "INSERT INTO Thuoc_SP_TangKem (maThuocTangKem, maKM, soLuong) VALUES (?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Thuoc_SP_TangKem SET soLuong=? WHERE maThuocTangKem=? AND maKM=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM Thuoc_SP_TangKem WHERE maThuocTangKem=? AND maKM=?";
    private final String SELECT_BY_ID_SQL = "SELECT maThuocTangKem, maKM, soLuong FROM Thuoc_SP_TangKem WHERE maThuocTangKem=? AND maKM=?";
    private final String SELECT_ALL_SQL = "SELECT maThuocTangKem, maKM, soLuong FROM Thuoc_SP_TangKem";

    @Override
    public void insert(Thuoc_SP_TangKem e) {
        ConnectDB.update(INSERT_SQL, e.getThuocTangKem().getMaThuoc(), e.getKhuyenmai().getMaKM(), e.getSoLuong());
    }

    @Override
    public void update(Thuoc_SP_TangKem e) {
        ConnectDB.update(UPDATE_SQL, e.getSoLuong(), e.getThuocTangKem().getMaThuoc(), e.getKhuyenmai().getMaKM());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1]);
    }

    @Override
    public Thuoc_SP_TangKem selectById(Object... keys) {
        List<Thuoc_SP_TangKem> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1]);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Thuoc_SP_TangKem> selectBySql(String sql, Object... args) {
        List<Thuoc_SP_TangKem> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                Thuoc_SP_TangKem ct = new Thuoc_SP_TangKem();
                ct.setThuocTangKem(new Thuoc_SanPham_Dao().selectById(rs.getString("maThuocTangKem")));
                ct.setKhuyenmai(new KhuyenMai_Dao().selectById(rs.getString("maKM")));
                ct.setSoLuong(rs.getInt("soLuong"));
                list.add(ct);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Thuoc_SP_TangKem> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}

