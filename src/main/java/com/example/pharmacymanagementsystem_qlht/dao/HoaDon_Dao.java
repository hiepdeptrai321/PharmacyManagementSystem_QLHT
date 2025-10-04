package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;

import java.util.List;

public class HoaDon_Dao implements DaoInterface<HoaDon>{

    private final String INSERT_SQL = "INSERT INTO HoaDon values (?,?,?,?,?)";
    private final String UPDATE_SQL = "UPDATE HoaDon SET thoiGian=?, idNV=?, idKH=?, tongTien=? WHERE idHD=?";
    private final String DELETE_BY_ID = "DELETE from HoaDon WHERE idHD = ?";

    private final String SELECT_ALL_SQL
            = "SELECT HoaDon.idHD, HoaDon.thoiGian, HoaDon.idNV, HoaDon.idKH, HoaDon.tongTien, "
            + "NhanVien.hoTen AS tenNV, NhanVien.sdt AS sdtNV, NhanVien.gioiTinh AS gioiTinhNV, NhanVien.namSinh, NhanVien.ngayVaoLam, "
            + "KhachHang.hoTen AS tenKH, KhachHang.sdt AS sdtKH, KhachHang.gioiTinh AS gioiTinhKH, KhachHang.ngayThamGia "
            + "FROM HoaDon "
            + "INNER JOIN NhanVien ON HoaDon.idNV = NhanVien.idNV "
            + "INNER JOIN KhachHang ON HoaDon.idKH = KhachHang.idKH "
            + "ORDER BY HoaDon.thoiGian ";

    private final String SELECT_BY_ID
            = "SELECT HoaDon.idHD, HoaDon.thoiGian, HoaDon.idNV, HoaDon.idKH, HoaDon.tongTien, "
            + "NhanVien.hoTen AS tenNV, NhanVien.sdt AS sdtNV, NhanVien.gioiTinh AS gioiTinhNV, NhanVien.namSinh, NhanVien.ngayVaoLam, "
            + "KhachHang.hoTen AS tenKH, KhachHang.sdt AS sdtKH, KhachHang.gioiTinh AS gioiTinhKH, KhachHang.ngayThamGia "
            + "FROM HoaDon "
            + "INNER JOIN NhanVien ON HoaDon.idNV = NhanVien.idNV "
            + "INNER JOIN KhachHang ON HoaDon.idKH = KhachHang.idKH "
            + "WHERE idHD = ? "
            + "ORDER BY HoaDon.thoiGian ";
    
    @Override
    public void insert(HoaDon e) {

    }

    @Override
    public void update(HoaDon e) {

    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public HoaDon selectById(Object... keys) {
        return null;
    }

    @Override
    public List<HoaDon> selectBySql(String sql, Object... args) {
        return List.of();
    }

    @Override
    public List<HoaDon> selectAll() {
        return List.of();
    }
}

