package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_SanPham_Dao implements DaoInterface<Thuoc_SanPham> {
    private final String INSERT_SQL = "INSERT INTO Thuoc_SanPham (MaThuoc,TenThuoc, HamLuong, DonViHL, DuongDung, QuyCachDongGoi, SDK_GPNK, HangSX, NuocSX, MaNDL, MaLoaiHang, HinhAnh, ViTri) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Thuoc_SanPham SET TenThuoc=?, HamLuong=?, DonViHL=?, DuongDung=?, QuyCachDongGoi=?, SDK_GPNK=?, HangSX=?, NuocSX=?, MaNDL=?, MaLoaiHang=?, HinhAnh=?, ViTri=? WHERE MaThuoc=?";
    private final String DELETE_SQL = "DELETE FROM Thuoc_SanPham WHERE MaThuoc=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Thuoc_SanPham";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Thuoc_SanPham WHERE MaThuoc=?";
    private final String SELECT_BY_TUKHOA_SQL = "SELECT * FROM Thuoc_SanPham WHERE TenThuoc LIKE ? OR MaThuoc LIKE ?";
    private final String SELECT_THUOC_SANPHAM_DONVICOBAN_SQL =
            "SELECT * FROM Thuoc_SanPham ts " +
                    "JOIN ChiTietDonViTinh ctdvt ON ts.MaThuoc = ctdvt.MaThuoc " +
                    "WHERE ctdvt.DonViCoBan = 1";

    private final String SELECT_THUOC_SANPHAM_DONVICOBAN_BYTUKHOA_SQL =
            "SELECT * FROM Thuoc_SanPham ts " +
            "JOIN ChiTietDonViTinh ctdvt ON ts.MaThuoc = ctdvt.MaThuoc " +
            "WHERE ctdvt.DonViCoBan = 1 AND (ts.TenThuoc LIKE ? OR ts.MaThuoc LIKE ?)";

    private final String SELECT_TENDVT_BYMA_SQL = "SELECT TenDonViTinh FROM ChiTietDonViTinh ctdvt JOIN DonViTinh dvt ON ctdvt.MaDVT = dvt.MaDVT WHERE MaThuoc = ? AND DonViCoBan = 1";
    private final String SELECT_TOP1_MATHUOC = "SELECT TOP 1 MaThuoc FROM Thuoc_SanPham ORDER BY MaThuoc DESC";
    @Override
    public boolean insert(Thuoc_SanPham e) {
        return ConnectDB.update(INSERT_SQL,e.getMaThuoc(), e.getTenThuoc(), e.getHamLuong(), e.getDonViHamLuong(), e.getDuongDung(), e.getQuyCachDongGoi(), e.getSDK_GPNK(), e.getHangSX(), e.getNuocSX(),e.getNhomDuocLy().getMaNDL(), e.getLoaiHang().getMaLoaiHang(), e.getHinhAnh(),e.getVitri().getMaKe())>0;
    }

    @Override
    public boolean update(Thuoc_SanPham thuoc) {
        return ConnectDB.update(UPDATE_SQL, thuoc.getTenThuoc(), thuoc.getHamLuong(), thuoc.getDonViHamLuong(), thuoc.getDuongDung(), thuoc.getQuyCachDongGoi(), thuoc.getSDK_GPNK(), thuoc.getHangSX(), thuoc.getNuocSX(), thuoc.getNhomDuocLy().getMaNDL(), thuoc.getLoaiHang().getMaLoaiHang(), thuoc.getHinhAnh(), thuoc.getVitri().getMaKe(), thuoc.getMaThuoc()) > 0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        return ConnectDB.update(DELETE_SQL, keys)>0;
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
                sp.setMaThuoc(rs.getString("MaThuoc"));
                sp.setTenThuoc(rs.getString("TenThuoc"));
                sp.setHamLuong(rs.getInt("HamLuong"));
                sp.setDonViHamLuong(rs.getString("DonViHL"));
                sp.setDuongDung(rs.getString("DuongDung"));
                sp.setQuyCachDongGoi(rs.getString("QuyCachDongGoi"));
                sp.setSDK_GPNK(rs.getString("SDK_GPNK"));
                sp.setHangSX(rs.getString("HangSX"));
                sp.setNuocSX(rs.getString("NuocSX"));
                sp.setNhomDuocLy(new NhomDuocLy_Dao().selectById(rs.getString("MaNDL")));
                sp.setLoaiHang(new LoaiHang_Dao().selectById(rs.getString("MaLoaiHang")));
                sp.setHinhAnh(rs.getBytes("HinhAnh"));
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

    public List<String> getAllLoaiHang() {
        String sql = "SELECT TenLH FROM LoaiHang";
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql);
            while (rs.next()) {
                list.add(rs.getString("TenLH"));
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<String> getAllXuatXu() {
        String sql = "SELECT DISTINCT NuocSX FROM Thuoc_SanPham";
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql);
            while (rs.next()) {
                list.add(rs.getString("NuocSX"));
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<Thuoc_SanPham> selectByTuKhoa(String tuKhoa) {
        return this.selectBySql(SELECT_BY_TUKHOA_SQL, "%" + tuKhoa + "%", "%" + tuKhoa + "%");
    }

    // Only join ChiTietDonViTinh (unit info)
    public List<Thuoc_SanPham> selectAllSLTheoDonViCoBan_ChiTietDVT() {
        List<Thuoc_SanPham> list = this.selectBySql(SELECT_THUOC_SANPHAM_DONVICOBAN_SQL);
        ChiTietDonViTinh_Dao ctdvtDao = new ChiTietDonViTinh_Dao();
        for (Thuoc_SanPham sp : list) {
            List<ChiTietDonViTinh> dsCTDVT = ctdvtDao.selectByMaThuoc(sp.getMaThuoc());
            sp.setDsCTDVT(dsCTDVT);
        }
        return list;
    }

    // By keyword, only join ChiTietDonViTinh
    public List<Thuoc_SanPham> selectSLTheoDonViCoBanByTuKhoa_ChiTietDVT(String tuKhoa) {
        List<Thuoc_SanPham> list = this.selectBySql(SELECT_THUOC_SANPHAM_DONVICOBAN_BYTUKHOA_SQL, "%" + tuKhoa + "%", "%" + tuKhoa + "%");
        ChiTietDonViTinh_Dao ctdvtDao = new ChiTietDonViTinh_Dao();
        for (Thuoc_SanPham sp : list) {
            List<ChiTietDonViTinh> dsCTDVT = ctdvtDao.selectByMaThuoc(sp.getMaThuoc());
            sp.setDsCTDVT(dsCTDVT);
        }
        return list;
    }

    public String getTenDVTByMaThuoc(String maThuoc) {
        String tenDVT = null;
        try {
            ResultSet rs = ConnectDB.query(SELECT_TENDVT_BYMA_SQL, maThuoc);
            if (rs.next()) {
                tenDVT = rs.getString("TenDonViTinh");
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tenDVT;
    }
    public List<String> timTheoTen(String keyword, int limit) {
        if (keyword == null) keyword = "";
        keyword = keyword.trim();
        if (keyword.isEmpty() || limit <= 0) return new ArrayList<>();

        String sql = "SELECT TenThuoc " +
                "FROM Thuoc_SanPham " +
                "WHERE TenThuoc LIKE ? OR MaThuoc LIKE ? " +
                "ORDER BY TenThuoc " +
                "OFFSET 0 ROWS FETCH NEXT " + limit + " ROWS ONLY";

        List<String> names = new ArrayList<>();
        try (ResultSet rs = ConnectDB.query(sql, "%" + keyword + "%", "%" + keyword + "%")) {
            while (rs.next()) {
                names.add(rs.getString("TenThuoc"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return names;
    }



    public List<String> layDanhSachThuocTheoKe(String maKe) {
        List<String> danhSach = new ArrayList<>();
        String sql = "SELECT TenThuoc FROM Thuoc_SanPham WHERE ViTri = ?";
        try {
            ResultSet rs = ConnectDB.query(sql, maKe);
            while (rs.next()) {
                danhSach.add(rs.getString("TenThuoc"));
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public String getTenLoaiHangByMaThuoc(String maThuoc) {
        String tenLoaiHang = null;
        String sql = "SELECT lh.TenLH FROM Thuoc_SanPham ts JOIN LoaiHang lh ON ts.MaLoaiHang = lh.MaLoaiHang WHERE ts.MaThuoc = ?";
        try {
            ResultSet rs = ConnectDB.query(sql, maThuoc);
            if (rs.next()) {
                tenLoaiHang = rs.getString("TenLH");
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tenLoaiHang;
    }
    public List<String> layDanhSachThuocTheoNDL(String maKe) {
        List<String> danhSach = new ArrayList<>();
        String sql = "SELECT TenThuoc FROM Thuoc_SanPham WHERE MaNDL = ?";
        try {
            ResultSet rs = ConnectDB.query(sql, maKe);
            while (rs.next()) {
                danhSach.add(rs.getString("TenThuoc"));
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}