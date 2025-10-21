package com.example.pharmacymanagementsystem_qlht.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectDB {

    static String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaThuoc;encrypt=true;trustServerCertificate=true;useUnicode=true;characterEncoding=UTF-8";
    static String user = "sa";
    static String password = "sapassword";

    public static PreparedStatement getStmt(String sql, Object... args) throws Exception {
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement stmt;
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
