package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCap_Dao implements DaoInterface<NhaCungCap>{
    private final String INSERT_SQL = "INSERT INTO NhaCungCap (tenNCC, diaChi, SDT, email, GPKD, ghiChu, tenCongTy, MSThue) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE NhaCungCap SET tenNCC = ?, diaChi = ?, SDT = ?, email = ?, GPKD = ?, ghiChu = ?, tenCongTy = ?, MSThue = ? WHERE maNCC = ?";
    private final String DELETE_SQL = "DELETE FROM NhaCungCap WHERE MaNCC = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM NhaCungCap";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM NhaCungCap WHERE MaNCC = ?";
    private final String SELECT_TOP1_MANCC = "SELECT TOP 1 MaNCC FROM NhaCungCap ORDER BY MaNCC DESC";

    @Override
    public void insert(NhaCungCap e) {
        ConnectDB.update(INSERT_SQL, e);
    }

    @Override
    public void update(NhaCungCap e) {
        ConnectDB.update(UPDATE_SQL, e);
    }

    @Override
    public void deleteById(Object... keys) {
        this.selectBySql(DELETE_SQL, keys);
    }

    @Override
    public NhaCungCap selectById(Object... keys) {
        return this.selectBySql(SELECT_BY_ID_SQL, keys).get(0);
    }

    @Override
    public List<NhaCungCap> selectBySql(String sql, Object... args) {
        List<NhaCungCap> list = new ArrayList<NhaCungCap>();
        try {
            ResultSet rs = ConnectDB.query(sql, args);
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSDT(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));
                ncc.setGPKD(rs.getString("GPKD"));
                ncc.setGhiChu(rs.getString("GhiChu"));
                ncc.setTenCongTy(rs.getString("TenCongTy"));
                ncc.setMSThue(rs.getString("MSThue"));
                list.add(ncc);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<NhaCungCap> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    public String generatekeyNhaCungCap() {
        String key = null;
        try {
            ResultSet rs = ConnectDB.query(SELECT_TOP1_MANCC);
            String lastKey = rs.getString("maPN");
            if (lastKey != null) {
                int numericPart = Integer.parseInt(lastKey.substring(2));
                numericPart++;
                key = String.format("NCC%03d", numericPart);
            } else {
                key = "NCC001";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return key;
    }
}
