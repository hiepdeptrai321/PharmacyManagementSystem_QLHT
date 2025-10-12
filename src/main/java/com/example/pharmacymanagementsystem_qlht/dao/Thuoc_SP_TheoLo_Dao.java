package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuNhap;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_SP_TheoLo_Dao implements DaoInterface<Thuoc_SP_TheoLo> {

    private final String INSERT_SQL = "INSERT INTO Thuoc_SP_TheoLo(MaLH, MaPN, MaThuoc,SoLuongTon, NSX, HSD) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Thuoc_SP_TheoLo SET SoLuongTon=?, NSX=?, HSD=?, MaPN=?, MaThuoc=? WHERE MaLH=?";
    private final String DELETE_BY_ID = "DELETE FROM Thuoc_SP_TheoLo WHERE MaLH = ?";
    private final String SELECT_BY_ID = "SELECT * FROM Thuoc_SP_TheoLo WHERE MaLH=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Thuoc_SP_TheoLo";
    private final String SELECT_SLTON_BY_MATHUOC = "SELECT SUM(SoLuongTon) AS TongSoLuongTon FROM Thuoc_SP_TheoLo WHERE MaThuoc = ?";
    private final String SELECT_BY_TUKHOA_SQL =
                "SELECT t.*, ctdvt.* FROM Thuoc_SP_TheoLo t " +
                "JOIN Thuoc_SanPham sp ON t.MaThuoc = sp.MaThuoc " +
                "WHERE LOWER(t.MaThuoc) LIKE ? OR LOWER(sp.TenThuoc) LIKE ?";

    @Override
    public void insert(Thuoc_SP_TheoLo e) {
        ConnectDB.update(INSERT_SQL, e.getMaLH(), e.getSoLuongTon(), e.getNsx(), e.getHsd(),
                e.getPhieuNhap() != null ? e.getPhieuNhap().getPhieuNhap().getMaPN() : null,
                e.getThuoc() != null ? e.getThuoc().getMaThuoc() : null);
    }

    @Override
    public void update(Thuoc_SP_TheoLo e) {
        ConnectDB.update(UPDATE_SQL, e.getSoLuongTon(), e.getNsx(), e.getHsd(),
                e.getPhieuNhap() != null ? e.getPhieuNhap().getPhieuNhap().getMaPN() : null,
                e.getThuoc() != null ? e.getThuoc().getMaThuoc() : null,
                e.getMaLH());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID, keys);
    }

    @Override
    public Thuoc_SP_TheoLo selectById(Object... keys) {
        List<Thuoc_SP_TheoLo> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Thuoc_SP_TheoLo> selectBySql(String sql, Object... args) {
        List<Thuoc_SP_TheoLo> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                Thuoc_SP_TheoLo t = new Thuoc_SP_TheoLo();
                t.setMaLH(rs.getString("MaLH"));
                t.setSoLuongTon(rs.getInt("SoLuongTon"));
                t.setNsx(rs.getDate("NSX"));
                t.setHsd(rs.getDate("HSD"));
                t.setPhieuNhap(new ChiTietPhieuNhap_Dao().selectById(rs.getString("MaPN"),rs.getString("MaThuoc"),rs.getString("MaLH")));
                t.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getString("MaThuoc")));
                list.add(t);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Thuoc_SP_TheoLo> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    public int selectSoLuongTonByMaThuoc(String maThuoc) {
        int soLuongTon = 0;
        try {
            ResultSet rs = ConnectDB.query(SELECT_SLTON_BY_MATHUOC, maThuoc);
            if (rs.next()) {
                soLuongTon = rs.getInt("TongSoLuongTon");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return soLuongTon;
    }

    public List<Thuoc_SP_TheoLo> selectByTuKhoa(String tuKhoa) {
        return this.selectBySql(SELECT_BY_TUKHOA_SQL, "%" + tuKhoa.toLowerCase() + "%", "%" + tuKhoa.toLowerCase() + "%");
    }

}
