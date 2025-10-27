package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuDoiHang;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietPhieuTraHang;
import com.example.pharmacymanagementsystem_qlht.model.DonViTinh;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDoiHang_Dao implements DaoInterface<ChiTietPhieuDoiHang> {
    private final String INSERT_SQL = "INSERT INTO ChiTietPhieuDoiHang (MaLH, MaPD, MaThuoc, MaDVT, SoLuong, LyDoDoi) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietPhieuDoiHang SET SoLuong=? WHERE MaLH=? AND MaPD=? AND MaThuoc=? AND MaDVT=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM ChiTietPhieuDoiHang WHERE MaLH=? AND MaPD=? AND MaThuoc=? AND MaDVT=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietPhieuDoiHang WHERE MaLH=? AND MaPD=? AND MaThuoc=? AND MaDVT=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietPhieuDoiHang";
    private final String SELECT_BY_MAPD_SQL = "SELECT * FROM ChiTietPhieuDoiHang WHERE MaPD = ?";

    @Override
    public boolean insert(ChiTietPhieuDoiHang e) {
        String maDVT = null;
        if (e.getDvt() != null && e.getDvt().getMaDVT() != null) maDVT = e.getDvt().getMaDVT();
        return ConnectDB.update(INSERT_SQL,
                e.getLoHang().getMaLH(),
                e.getPhieuDoiHang().getMaPD(),
                e.getThuoc().getMaThuoc(),
                maDVT,
                e.getSoLuong(),
                e.getLyDoDoi()) > 0;
    }

    @Override
    public boolean update(ChiTietPhieuDoiHang e) {
        String maDVT = null;
        if (e.getDvt() != null && e.getDvt().getMaDVT() != null) maDVT = e.getDvt().getMaDVT();
        return ConnectDB.update(UPDATE_SQL,
                e.getSoLuong(),
                e.getLoHang().getMaLH(),
                e.getPhieuDoiHang().getMaPD(),
                e.getThuoc().getMaThuoc(),
                maDVT) > 0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        //return ConnectDB.update(DELETE_BY_ID_SQL, keys[0], keys[1], keys[2], keys[3])>0;
        return ConnectDB.update(DELETE_BY_ID_SQL, keys) > 0;
    }

    @Override
    public ChiTietPhieuDoiHang selectById(Object... keys) {
       // List<ChiTietPhieuDoiHang> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1], keys[2]);
        List<ChiTietPhieuDoiHang> list = selectBySql(SELECT_BY_ID_SQL, keys);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ChiTietPhieuDoiHang> selectBySql(String sql, Object... args) {
        List<ChiTietPhieuDoiHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietPhieuDoiHang ct = new ChiTietPhieuDoiHang();
                ct.setLoHang(new Thuoc_SP_TheoLo_Dao().selectById(rs.getString("MaLH")));
                ct.setPhieuDoiHang(new PhieuDoiHang_Dao().selectById(rs.getString("MaPD")));
                ct.setThuoc(new Thuoc_SanPham_Dao().selectById(rs.getString("MaThuoc")));
                try {
                    String maDVT = rs.getString("MaDVT");
                    DonViTinh dvt = null;

                    if (maDVT != null && !maDVT.isBlank()) {
                        try {
                            DonViTinh_Dao dvtDao = new DonViTinh_Dao();
                            dvt = dvtDao.selectById(maDVT);
                        } catch (Exception ignored) {}
                    }

                    ct.setDvt(dvt);
                } catch (Exception ignore) {
                    // Optional: log the exception for debugging
                }
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setLyDoDoi(safeGetString(rs, "LyDoDoi"));

                list.add(ct);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    private String safeGetString(ResultSet rs, String column) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                String label = md.getColumnLabel(i);
                String name = md.getColumnName(i);
                if (column.equalsIgnoreCase(label) || column.equalsIgnoreCase(name)) {
                    return rs.getString(column);
                }
            }
        } catch (Exception ignored) {}
        // fallback: try common alternate names
        try { return rs.getString("LyDo"); } catch (Exception ignored) {}
        return null;
    }

    @Override
    public List<ChiTietPhieuDoiHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    public List<ChiTietPhieuDoiHang> getChiTietPhieuDoiByMaPT(String maPD) {
        return this.selectBySql(SELECT_BY_MAPD_SQL, maPD);
    }
}
