package com.example.pharmacymanagementsystem_qlht.dao;

import com.example.pharmacymanagementsystem_qlht.connectDB.ConnectDB;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeSanPham;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThongKe_Dao {


    public List<ThongKeBanHang> getThongKeBanHang(String thoiGian) {
        List<ThongKeBanHang> list = new ArrayList<>();
        String sql; // Tên Stored Procedure

        // Chọn Stored Procedure dựa trên đầu vào
        switch (thoiGian) {
            case "Hôm nay":
                sql = "{call sp_ThongKeBanHang_HomNay}";
                break;
            case "Tuần này":
                sql = "{call sp_ThongKeBanHang_TuanNay}";
                break;
            case "Tháng này":
                sql = "{call sp_ThongKeBanHang_ThangNay}";
                break;
            case "Quý này":
                sql = "{call sp_ThongKeBanHang_QuyNay}";
                break;
            default:
                sql = "{call sp_ThongKeBanHang_HomNay}"; // Mặc định
        }

        try (ResultSet rs = ConnectDB.query(sql)) {
            while (rs.next()) {
                ThongKeBanHang tk = new ThongKeBanHang(
                        rs.getString("ThoiGian"),
                        rs.getInt("SoLuongHoaDon"),
                        rs.getDouble("TongGiaTri"),
                        rs.getDouble("GiamGia"),
                        rs.getInt("SoLuongDonTra"),
                        rs.getDouble("GiaTriDonTra"),
                        rs.getDouble("DoanhThu")
                );
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy Top 5 sản phẩm bán chạy theo thời gian
     * @param thoiGian "Hôm nay", "Tuần này", "Tháng này", "Quý này"
     * @return Danh sách đối tượng ThongKeSanPham
     */
    public List<ThongKeSanPham> getTop5SanPham(String thoiGian) {
        List<ThongKeSanPham> list = new ArrayList<>();
        String sql; // Tên Stored Procedure

        switch (thoiGian) {
            case "Hôm nay":
                sql = "{call sp_Top5SanPham_HomNay}";
                break;
            case "Tuần này":
                sql = "{call sp_Top5SanPham_TuanNay}";
                break;
            case "Tháng này":
                sql = "{call sp_Top5SanPham_ThangNay}";
                break;
            case "Quý này":
                sql = "{call sp_Top5SanPham_QuyNay}";
                break;
            default:
                sql = "{call sp_Top5SanPham_HomNay}";
        }

        try (ResultSet rs = ConnectDB.query(sql)) {
            while (rs.next()) {
                ThongKeSanPham sp = new ThongKeSanPham(
                        rs.getString("MaThuoc"),
                        rs.getString("TenThuoc"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("ThanhTien")
                );
                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}