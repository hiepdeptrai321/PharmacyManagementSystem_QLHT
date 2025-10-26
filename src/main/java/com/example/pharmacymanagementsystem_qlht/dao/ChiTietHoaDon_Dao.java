package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoaDon;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_Dao implements  DaoInterface<ChiTietHoaDon> {
    private final String INSERT_SQL = "INSERT INTO ChiTietHoaDon (MaHD, MaLH, SoLuong, DonGia, GiamGia) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE ChiTietHoaDon SET SoLuong=?, DonGia=?, GiamGia=? WHERE MaHD=? AND MaLH=?";
    private final String DELETE_SQL = "DELETE FROM ChiTietHoaDon WHERE MaHD=? AND MaLH=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM ChiTietHoaDon";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietHoaDon WHERE MaHD=? AND MaLH=?";
    private final String SELECT_BY_MAHD_SQL = "SELECT * FROM ChiTietHoaDon WHERE MaHD=?";

    @Override
    public boolean insert(ChiTietHoaDon e) {
        return ConnectDB.update(INSERT_SQL, e.getHoaDon().getMaHD(), e.getLoHang().getMaLH(), e.getSoLuong(), e.getDonGia(), e.getGiamGia())>0;
    }
//    public boolean insert(ChiTietHoaDon cthd) {
//        String sql = "insert into ChiTietHoaDon(MaHD, MaLH, SoLuong, DonGia, GiamGia) values (?, ?, ?, ?, ?)";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setString(1, cthd.getHoaDon().getMaHD());
//            ps.setString(2, cthd.getLoHang().getMaLH());
//            ps.setInt(3, cthd.getSoLuong());
//            ps.setDouble(4, cthd.getDonGia());
//            ps.setDouble(5, cthd.getGiamGia());
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @Override
    public boolean update(ChiTietHoaDon e) {
        return ConnectDB.update(UPDATE_SQL, e.getSoLuong(), e.getDonGia(), e.getGiamGia(), e.getHoaDon().getMaHD(), e.getLoHang().getMaLH())>0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        return ConnectDB.update(DELETE_SQL, keys[0], keys[1])>0;
    }

    @Override
    public ChiTietHoaDon selectById(Object... keys) {
        List<ChiTietHoaDon> list = selectBySql(SELECT_BY_ID_SQL, keys[0], keys[1]);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ChiTietHoaDon> selectByMaHD(String maHD) {
        return selectBySql(SELECT_BY_MAHD_SQL, maHD);
    }

    @Override
    public List<ChiTietHoaDon> selectBySql(String sql, Object... args) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();

                // Populate HoaDon (minimal)
                HoaDon hoaDon = new HoaDon();
                String maHD = rs.getString("MaHD");
                if (maHD != null) {
                    hoaDon.setMaHD(maHD);
                    // try to load full HoaDon if DAO available
                    try {
                        HoaDon_Dao hdDao = new HoaDon_Dao();
                        HoaDon full = hdDao.selectById(maHD);
                        if (full != null) hoaDon = full;
                    } catch (Exception ignored) {}
                }
                cthd.setHoaDon(hoaDon);

                String maLH = rs.getString("MaLH");
                Thuoc_SP_TheoLo loHang = new Thuoc_SP_TheoLo();
                if (maLH != null) {
                    loHang.setMaLH(maLH);
                    try {
                        Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();
                        Thuoc_SP_TheoLo fullLo = loDao.selectById(maLH);
                        if (fullLo != null) loHang = fullLo;
                    } catch (Exception ignored) {}
                }
                cthd.setLoHang(loHang);

                cthd.setSoLuong(rs.getInt("SoLuong"));
                cthd.setDonGia(rs.getDouble("DonGia"));
                cthd.setGiamGia(rs.getDouble("GiamGia"));

                list.add(cthd);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ChiTietHoaDon> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
}
