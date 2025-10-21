package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_Dao implements DaoInterface<KhachHang> {

    private final String INSERT_SQL = "INSERT INTO KhachHang (MaKH, TenKH, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE KhachHang SET TenKH=?, SDT=?, Email=?, NgaySinh=?, GioiTinh=?, DiaChi=? WHERE MaKH=?";
    private final String DELETE_BY_ID = "DELETE FROM KhachHang WHERE MaKH=?";
    private final String SELECT_BY_ID = "SELECT * FROM KhachHang WHERE MaKH=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM KhachHang";

    @Override
    public boolean insert(KhachHang e) {
        // ensure default status when inserting
        String tt = e.getTrangThai() == null ? "Hoạt động" : e.getTrangThai();
        // ensure MaKH exists; generate if missing
        if (e.getMaKH() == null || e.getMaKH().trim().isEmpty()) {
            e.setMaKH(generateNewMaKH());
        }
        final int MAX_RETRY = 5;
        int attempt = 0;
        while (attempt < MAX_RETRY) {
            try {
                int rows = ConnectDB.update(INSERT_SQL, e.getMaKH(), e.getTenKH(), e.getSdt(), e.getEmail(), e.getNgaySinh(), e.getGioiTinh(), e.getDiaChi(), tt);
                return rows > 0;
            } catch (RuntimeException ex) {
                attempt++;
                // On duplicate key/PK violation, regenerate MaKH and retry; otherwise rethrow after max attempts
                if (attempt >= MAX_RETRY) {
                    throw ex;
                }
                // generate a new MaKH and retry
                e.setMaKH(generateNewMaKH());
            }
        }
        return false; // unreachable but required by compiler
    }

    @Override
    public boolean update(KhachHang e) {
        return ConnectDB.update(UPDATE_SQL, e.getTenKH(), e.getSdt(), e.getEmail(), e.getNgaySinh(), e.getGioiTinh(), e.getDiaChi(), e.getMaKH())>0;
    }

    @Override
    public boolean deleteById(Object... keys) {
        return ConnectDB.update(DELETE_BY_ID, keys)>0;
    }

    @Override
    public KhachHang selectById(Object... keys) {
        List<KhachHang> list = selectBySql(SELECT_BY_ID, keys);
        if (list.isEmpty()) {
            return null;
    }
        return list.get(0);
    }

    @Override
    public List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setNgaySinh(rs.getDate("NgaySinh"));
                kh.setGioiTinh(rs.getString("GioiTinh"));
                kh.setDiaChi(rs.getString("DiaChi"));
                // map TrangThai from DB
                kh.setTrangThai(rs.getString("TrangThai"));
                list.add(kh);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Refresh customer status based on last purchase:
     * - If customer has a purchase within last 1 year -> 'Kích hoạt'
     * - Else if customer has purchases but last is older than 1 year -> 'Ngưng'
     * - Else (no purchases) -> keep existing TrangThai (do not change)
     */
    public void refreshTrangThai() {
        String sql = "UPDATE KhachHang SET TrangThai = CASE " +
                "WHEN EXISTS (SELECT 1 FROM HoaDon WHERE HoaDon.MaKH = KhachHang.MaKH AND NgayLap >= DATEADD(year, -1, GETDATE())) THEN 'Hoạt động' " +
                "WHEN EXISTS (SELECT 1 FROM HoaDon WHERE HoaDon.MaKH = KhachHang.MaKH) AND NOT EXISTS (SELECT 1 FROM HoaDon WHERE HoaDon.MaKH = KhachHang.MaKH AND NgayLap >= DATEADD(year, -1, GETDATE())) THEN 'Không hoạt động' " +
                "ELSE TrangThai END";
        ConnectDB.update(sql);
    }

    @Override
    public List<KhachHang> selectAll() {
        return  this.selectBySql(SELECT_ALL_SQL);
    }

    /**
     * Generate a new MaKH by taking the last MaKH in descending order and incrementing the numeric suffix.
     * Format: KH%03d (e.g., KH011 -> KH012). If no existing rows, returns KH001.
     */
    public String generateNewMaKH() {
        // Preferred: use SQL Server SEQUENCE if available (atomic and safe under concurrency)
        try {
            String seqSql = "SELECT NEXT VALUE FOR seq_MaKH";
            java.sql.ResultSet rsSeq = ConnectDB.query(seqSql);
            if (rsSeq.next()) {
                long seqVal = rsSeq.getLong(1);
                rsSeq.getStatement().getConnection().close();
                return String.format("KH%03d", seqVal);
            }
            rsSeq.getStatement().getConnection().close();
        } catch (Exception ignore) {
            // Sequence may not exist; fall back to previous method
        }

        // Fallback: read highest MaKH and increment numeric suffix (works when MaKH formatted as KH###)
        String newMa = "KH001";
        // Order by numeric suffix of MaKH (substring after 'KH'), safe for values like KH009, KH010, KH100
        String sql = "SELECT TOP 1 MaKH FROM KhachHang ORDER BY TRY_CONVERT(INT, SUBSTRING(MaKH, 3, 100)) DESC";
        try {
            java.sql.ResultSet rs = ConnectDB.query(sql);
            if (rs.next()) {
                String last = rs.getString("MaKH");
                String digits = last == null ? "" : last.replaceAll("\\D", "");
                if (digits.isEmpty()) {
                    newMa = "KH001";
                } else {
                    int num = Integer.parseInt(digits);
                    num++;
                    newMa = String.format("KH%03d", num);
                }
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return newMa;
    }
}