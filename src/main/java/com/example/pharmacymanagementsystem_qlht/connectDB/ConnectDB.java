package com.example.pharmacymanagementsystem_qlht.connectDB;

import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class ConnectDB {

    public static String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaThuoc;encrypt=true;trustServerCertificate=true;useUnicode=true;characterEncoding=UTF-8";
    public static String user = "sa";
    public static String password = "sapassword";

    public static PreparedStatement getStmt(String sql, Object... args) throws Exception {
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement stmt;
        if (DangNhap_Ctrl.user != null) {
            PreparedStatement ps = con.prepareStatement("SET CONTEXT_INFO ?");
            byte[] maNvBytes = new byte[128];
            byte[] actualBytes = DangNhap_Ctrl.user.getMaNV().getBytes(StandardCharsets.UTF_8);
            System.arraycopy(actualBytes, 0, maNvBytes, 0, actualBytes.length);
            ps.setBytes(1, maNvBytes);
            ps.execute();
            ps.close();
        }
        if (sql.trim().startsWith("{")) {
            stmt = con.prepareCall(sql);
        } else {
            stmt = con.prepareStatement(sql);
        }

        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt;
    }

    public static int update(String sql, Object... args) {
        try {
            PreparedStatement stmt = ConnectDB.getStmt(sql, args);
            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static ResultSet query(String sql, Object... args) throws Exception {
        PreparedStatement stmt = ConnectDB.getStmt(sql, args);
        return stmt.executeQuery();
    }

    public static String queryTaoMa(String sql) {
        String maGenerate = null;
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                maGenerate = rs.getString(1);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maGenerate;
    }



}
