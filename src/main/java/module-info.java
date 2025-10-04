module com.example.pharmacymanagementsystem_qlht {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLThuoc;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLHoatDong;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKeHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhachHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhaCungCap;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhanVien;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapHoaDon;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuDatHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuDoi;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuTra;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_NhapHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;
    requires javafx.base;
    requires java.sql;


    opens com.example.pharmacymanagementsystem_qlht to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLThuoc to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.CN_NhapHang to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_NhapHang to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapHoaDon to javafx.fxml;
}