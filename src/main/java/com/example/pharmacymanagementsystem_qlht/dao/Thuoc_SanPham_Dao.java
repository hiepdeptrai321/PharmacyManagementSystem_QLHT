package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.LoaiHang;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_SanPham_Dao implements DaoInterface<Thuoc_SanPham> {
    private final String INSERT_SQL = "INSERT INTO Thuoc_SanPham (TenThuoc, HamLuong, DonViHL, DuongDung, QuyCachDongGoi, SDK_GPNK, HangSX, NuocSX, MaNDL, MaLH, HinhAnh, ViTri) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Thuoc_SanPham SET TenThuoc=?, HamLuong=?, DonViHL=?, DuongDung=?, QuyCachDongGoi=?, SDK_GPNK=?, HangSX=?, NuocSX=?, HinhAnh=? WHERE MaThuoc=?";
    private final String DELETE_SQL = "DELETE FROM Thuoc_SanPham WHERE MaThuoc=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Thuoc_SanPham";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Thuoc_SanPham WHERE MaThuoc=?";

    @Override
    public void insert(Thuoc_SanPham e) {
        ConnectDB.update(INSERT_SQL, e.getTenThuoc(), e.getHamLuong(), e.getDonViHamLuong(), e.getDuongDung(), e.getQuyCachDongGoi(), e.getSDK_GPNK(), e.getHangSX(), e.getNuocSX(),e.getNhomDuocLy().getMaNDL(), e.getLoaiHang().getMaLoaiHang(), e.getHinhAnh(),e.getVitri().getMaKe());
    }

    @Override
    public void update(Thuoc_SanPham e) {
        ConnectDB.update(UPDATE_SQL, e.getTenThuoc(), e.getHamLuong(), e.getDonViHamLuong(), e.getDuongDung(), e.getQuyCachDongGoi(), e.getSDK_GPNK(), e.getHangSX(), e.getNuocSX(), e.getHinhAnh(), e.getMaThuoc());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_SQL, keys);
    }

    @Override
    public Thuoc_SanPham selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID_SQL, keys).get(0);
    }

    @Override
    public List<Thuoc_SanPham> selectBySql(String sql, Object... args) {
        List<Thuoc_SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                Thuoc_SanPham sp = new Thuoc_SanPham();
                sp.setTenThuoc(rs.getString("TenThuoc"));
                sp.setHamLuong(rs.getInt("HamLuong"));
                sp.setDonViHamLuong(rs.getString("DonViHL"));
                sp.setDuongDung(rs.getString("DuongDung"));
                sp.setQuyCachDongGoi(rs.getString("QuyCachDongGoi"));
                sp.setSDK_GPNK(rs.getString("SDK_GPNK"));
                sp.setHangSX(rs.getString("HangSX"));
                sp.setNuocSX(rs.getString("NuocSX"));
                sp.setNhomDuocLy(new NhomDuocLy_Dao().selectById(rs.getString("MaNDL")));
                sp.setLoaiHang(new LoaiHang_Dao().selectById(rs.getString("MaLH")));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setVitri(new KeHang_Dao().selectById(rs.getString("ViTri")));
                list.add(sp);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Thuoc_SanPham> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }
}