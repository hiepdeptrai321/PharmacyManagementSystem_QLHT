package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_Dao implements DaoInterface<NhanVien> {
    private final String INSERT_SQL = "INSERT INTO NhanVien(MaNV, TenNV, SDT,Email, NgaySinh, GioiTinh, DiaChi, TrangThai, TaiKhoan, MatKhau, VaiTro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE NhanVien SET TenNV=?, SDT=?, Email=?, NgaySinh=?, GioiTinh=?, DiaChi=?, TrangThai=?, TaiKhoan=?, MatKhau=? WHERE MaNV=?";
    private final String DELETE_BY_ID = "DELETE FROM NhanVien WHERE MaNV = ?";
    private final String SELECT_BY_ID = "SELECT * FROM NhanVien WHERE MaNV=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM NhanVien";
    private final String SELECT_BY_TAIKHOAN_MATKHAU = "SELECT * FROM NhanVien WHERE TaiKhoan=? AND MatKhau=?";

    @Override
    public boolean insert(NhanVien e) {
        return ConnectDB.update(INSERT_SQL,
                e.getMaNV(),
                e.getTenNV(),
                e.getSdt(),
                e.getEmail(),
                e.getNgaySinh(),
                e.getGioiTinh(),
                e.getDiaChi(),
                e.getTrangThai(),
                e.getTaiKhoan(),
                e.getMatKhau())>0;
    }

    @Override
    public boolean update(NhanVien e) {
        return ConnectDB.update(UPDATE_SQL,
            e.getTenNV(),
            e.getSdt(),
            e.getEmail(),
            e.getNgaySinh(),
            e.getGioiTinh(),
            e.getDiaChi(),
            e.getTrangThai(),
            e.getTaiKhoan(),
            e.getMatKhau(),
            e.getMaNV())>0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        return ConnectDB.update(DELETE_BY_ID, keys)>0;
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
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                nv.setTaiKhoan(rs.getString("TaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setVaiTro(rs.getString("VaiTro"));
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
    public boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan=? AND MatKhau=?";
        List<NhanVien> list = selectBySql(sql, username, password);
        return !list.isEmpty();
    }
    public NhanVien selectByTKVaMK(String taiKhoan, String matKhau) {
        List<NhanVien> list = selectBySql(SELECT_BY_TAIKHOAN_MATKHAU, taiKhoan, matKhau);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
