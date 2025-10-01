module com.example.pharmacymanagementsystem_qlht {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires java.desktop;
    opens com.example.pharmacymanagementsystem_qlht.controller to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller;
    opens com.example.pharmacymanagementsystem_qlht to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapHoaDon;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapHoaDon to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuDoi;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuDoi to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuTra;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapPhieuTra to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_NhapHang;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_NhapHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.ThongKe;
    opens com.example.pharmacymanagementsystem_qlht.controller.ThongKe to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLThuoc;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLThuoc to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLHoatDong;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLHoatDong to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKeHang;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKeHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhachHang;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhachHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLKhuyenMai to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhaCungCap;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhaCungCap to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhanVien;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_QuanLy.QLNhanVien to javafx.fxml;

}