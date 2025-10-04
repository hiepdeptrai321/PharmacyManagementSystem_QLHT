package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_Dao implements DaoInterface<HoaDon>{

    private final String INSERT_SQL = "INSERT INTO HoaDon (MaHD, NgayLap, TrangThai, MaKH, MaNV) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HoaDon SET  NgayLap=?, TrangThai=?, MaKH=?, MaNV=? WHERE MaHD=?";
    private final String DELETE_BY_ID = "DELETE FROM HoaDon WHERE MaHD = ?";

    private final String SELECT_ALL_SQL =
            "SELECT HoaDon.MaHD, HoaDon.NgayLap, HoaDon.TrangThai, HoaDon.MaKH, HoaDon.MaNV, "
                    + "NhanVien.TenNV AS tenNV, NhanVien.SDT AS sdtNV, NhanVien.GioiTinh AS gioiTinhNV, NhanVien.NgaySinh, NhanVien.NgayVaoLam, "
                    + "KhachHang.TenKH AS tenKH, KhachHang.SDT AS sdtKH, KhachHang.GioiTinh AS gioiTinhKH, KhachHang.NgaySinh AS ngaySinh "
                    + "FROM HoaDon "
                    + "INNER JOIN NhanVien ON HoaDon.MaNV = NhanVien.MaNV "
                    + "LEFT JOIN KhachHang ON HoaDon.MaKH = KhachHang.MaKH";

    private final String SELECT_BY_ID =
            "SELECT HoaDon.MaHD, HoaDon.NgayLap, HoaDon.TrangThai, HoaDon.MaKH, HoaDon.MaNV, "
                    + "NhanVien.TenNV AS tenNV, NhanVien.SDT AS sdtNV, NhanVien.GioiTinh AS gioiTinhNV, NhanVien.NgaySinh, NhanVien.NgayVaoLam, "
                    + "KhachHang.TenKH AS tenKH, KhachHang.SDT AS sdtKH, KhachHang.GioiTinh AS gioiTinhKH, KhachHang.NgaySinh AS ngaySinh "
                    + "FROM HoaDon "
                    + "INNER JOIN NhanVien ON HoaDon.MaNV = NhanVien.MaNV "
                    + "INNER JOIN KhachHang ON HoaDon.MaKH = KhachHang.MaKH "
                    + "WHERE HoaDon.MaHD = ? "
                    + "ORDER BY HoaDon.NgayLap";


    @Override
    public void insert(HoaDon e) {
    }

    @Override
    public void update(HoaDon e) {
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys[0]);
    }

    @Override
    public HoaDon selectById(Object... keys) {
        List<HoaDon> list = selectBySql(SELECT_BY_ID, keys[0]);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<HoaDon> selectBySql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try (ResultSet rs = ConnectDB.query(sql, args)) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setNgayLap(rs.getTimestamp("NgayLap"));
                hd.setTrangThai(rs.getString("TrangThai"));

                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));


                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSdt(rs.getString("sdtKH"));
                kh.setGioiTinh(rs.getBoolean("gioiTinhKH"));
                kh.setNgaySinh(rs.getDate("NgaySinh"));

                list.add(hd);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<HoaDon> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}

