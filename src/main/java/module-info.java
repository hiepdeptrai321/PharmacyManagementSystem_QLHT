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
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoatDong;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKeHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhuyenMai;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhanVien;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapHoaDon;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDatHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuDoi;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuTra;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuNhapHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;
    requires javafx.base;
    requires java.sql;


    opens com.example.pharmacymanagementsystem_qlht to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuNhapHang to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhuyenMai to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKThuoc to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoaDon to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapHoaDon to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.model to javafx.base;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuDatHang;
    exports com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKNhaCungCap to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuNhap to javafx.fxml;
    opens com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;
}