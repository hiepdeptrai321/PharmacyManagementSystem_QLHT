package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.HoaDon;
import com.example.pharmacymanagementsystem_qlht.model.NhanVien;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_Dao implements DaoInterface<HoaDon>{

    private final String INSERT_SQL = "INSERT INTO HoaDon (MaHD, MaNV, NgayLap, MaKH, TrangThai) VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HoaDon SET MaNV=?, NgayLap=?, MaKH=?, TrangThai=? WHERE MaHD=?";
    private final String DELETE_BY_ID_SQL = "DELETE FROM HoaDon WHERE MaHD=?";
    private final String SELECT_BY_ID_SQL = "SELECT MaHD, MaNV, NgayLap, MaKH, TrangThai FROM HoaDon WHERE MaHD=?";
    private final String SELECT_ALL_SQL = "SELECT MaHD, MaNV, NgayLap, MaKH, TrangThai FROM HoaDon";
    private final String SELECT_BY_TUKHOA_SQL = "SELECT MaHD, MaNV, NgayLap, MaKH, TrangThai FROM HoaDon WHERE MaHD LIKE ?";

    @Override
    public void insert(HoaDon e) {
        ConnectDB.update(INSERT_SQL, e.getMaHD(), e.getMaNV().getMaNV(), e.getNgayLap(), e.getMaKH().getMaKH(), e.getTrangThai());
    }

    @Override
    public void update(HoaDon e) {
        ConnectDB.update(UPDATE_SQL, e.getMaNV().getMaNV(), e.getNgayLap(), e.getMaKH().getMaKH(), e.getTrangThai(), e.getMaHD());
    }

    @Override
    public void deleteById(Object... keys) {
        ConnectDB.update(DELETE_BY_ID_SQL, keys);
    }

    @Override
    public HoaDon selectById(Object... keys) {
        List<HoaDon> list = selectBySql(SELECT_BY_ID_SQL, keys);
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    @Override
    public List<HoaDon> selectBySql(String sql, Object... args) {
        List<HoaDon> hoaDonList = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setMaNV(new NhanVien_Dao().selectById(rs.getString("MaNV")));
                hd.setMaKH(new KhachHang_Dao().selectById(rs.getString("MaKH")));
                hd.setNgayLap(rs.getTimestamp("NgayLap"));
                hd.setTrangThai(rs.getBoolean("TrangThai"));
                hoaDonList.add(hd);
            }
            rs.getStatement().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hoaDonList;
    }

    @Override
    public List<HoaDon> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    public String generateNewMaHD() {
        String newMaHD = "HD001"; // Default value if no records exist
        String SELECT_TOP1_SQL = "SELECT TOP 1 MaHD FROM HoaDon ORDER BY MaHD DESC";
        try {
            ResultSet rs = ConnectDB.query(SELECT_TOP1_SQL);
            if (rs.next()) {
                String lastMaHD = rs.getString("MaHD");
                int stt = Integer.parseInt(lastMaHD.substring(2)); // Extract numeric part
                stt++; // Increment the numeric part
                newMaHD = String.format("HD%03d", stt); // Format with leading zeros
            }
            rs.getStatement().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newMaHD;
    }

    public List<HoaDon> selectByTuKhoa(String tuKhoa){
        return this.selectBySql(SELECT_BY_TUKHOA_SQL, "%" + tuKhoa + "%");
    }
}
