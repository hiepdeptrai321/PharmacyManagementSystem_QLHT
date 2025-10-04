package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_Dao implements DaoInterface<NhanVien> {
    private final String INSERT_SQL = "INSERT INTO NhanVien VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE NhanVien SET tenNV=?, sdt=?, email=?, ngaySinh=?, gioiTinh=?, diaChi=?, trangThai=?, taiKhoan=?, matKhau=? WHERE maNV=?";
    private final String DELETE_BY_ID = "DELETE FROM NhanVien WHERE maNV = ?";
    private final String SELECT_BY_ID = "SELECT * FROM NhanVien WHERE maNV=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM NhanVien";

    @Override
    public void insert(NhanVien e) {
        ConnectDB.update(INSERT_SQL,
                e.getMaNV(),
                e.getTenNV(),
                e.getSdt(),
                e.getEmail(),
                e.getNgaySinh(),
                e.getGioiTinh(),
                e.getDiaChi(),
                e.getTrangThai(),
                e.getTaiKhoan(),
                e.getMatKhau());
    }

    @Override
    public void update(NhanVien e) {
    ConnectDB.update(UPDATE_SQL,
            e.getTenNV(),
            e.getSdt(),
            e.getEmail(),
            e.getNgaySinh(),
            e.getGioiTinh(),
            e.getDiaChi(),
            e.getTrangThai(),
            e.getTaiKhoan(),
            e.getMatKhau(),
            e.getMaNV());
    }

    @Override
    public void deleteById(Object... keys) {
    ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public NhanVien selectById(Object... keys) {
        List<NhanVien> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setSdt(rs.getString("sdt"));
                nv.setEmail(rs.getString("email"));
                nv.setNgaySinh(rs.getTimestamp("ngaySinh"));
                nv.setGioiTinh(rs.getBoolean("gioiTinh"));
                nv.setDiaChi(rs.getString("diaChi"));
                nv.setTrangThai(rs.getBoolean("trangThai"));
                nv.setTaiKhoan(rs.getString("taiKhoan"));
                nv.setMatKhau(rs.getString("matKhau"));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<NhanVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}
