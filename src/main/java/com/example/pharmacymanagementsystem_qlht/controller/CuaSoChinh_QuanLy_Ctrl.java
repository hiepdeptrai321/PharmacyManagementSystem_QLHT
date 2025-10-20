package com.example.pharmacymanagementsystem_qlht.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class CuaSoChinh_QuanLy_Ctrl extends Application {
    public Pane pnlChung;

    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CuaSoChinh_QuanLy_GUI.fxml"));
        primaryStage.setTitle("Hệ thống quản lý hiệu thuốc");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logo.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void LapHoaDon(ActionEvent actionEvent) {
    }

    public void AnhChuyenTrangChu(MouseEvent mouseEvent) {
        pnlChung.getChildren().clear();
        try{
            Pane pane = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/TrangChu_GUI.fxml"));
            pnlChung.getChildren().add(pane);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//  1.Chức năng tìm kiếm
//  Tìm kiếm hóa đơn
    public void timKiemHoaDon(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKHoaDon/TKHoaDon_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm phiếu nhập
    public void timKiemPhieuNhap(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuNhapHang/TKPhieuNhapHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm phiếu đổi hàng
    public void timKiemPhieuDoiHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDoiHang/TKPhieuDoiHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm phiếu trả hàng
    public void timKiemPhieuTraHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuTraHang/TKPhieuTraHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm phiếu đặt hàng
    public void timKiemPhieuDatHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuDatHang/TKPhieuDatHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm nhà cung cấp
    public void timKiemNhaCungCap(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKNhaCungCap/TKNCC_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm thuốc
    public void timKiemThuoc(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Tìm kiếm khách hàng
    public void timKiemKhachHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKKhachHang/TKKhachHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  2.Chức năng danh mục
//  Danh mục thuốc
    public void danhMucThuoc(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMThuoc/DanhMucThuoc_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Danh mục nhân viên
    public void danhMucNhanVien(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNhanVien/DanhMucNhanVien_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Danh mục khách hàng
    public void danhMucKhachHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/DanhMucKhachHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Danh mục kệ hàng
    public void danhMucKeHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKeHang/DanhMucKeHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Danh mục nhà cung cấp
    public void danhMucNhaCungCap(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/DanhMucNhaCungCap_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Danh mục hoạt động
    public void danhMucHoatDong(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMHoatDong/DanhMucHoatDong_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Danh mục khuyến mãi
    public void danhMucKhuyenMai(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/DanhMucKhuyenMai_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  3.Chức năng cập nhật
//  Cập nhật giá bán
    public void CapNhatGiaBan(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatGia/CapNhatGiaThuoc_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Cập nhật tồn kho
    public void capNhatTonKho(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatSoLuong/CapNhatSoLuongThuoc_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void capNhatKhuyenMai(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatKhuyenMai/CapNhatKhuyenMai_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  4.Chức năng thống kê và báo cáo
//  Thống kê doanh thu
    public void thongKeDoanhThu(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeBanHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void thongKeXuatNhap(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_ThongKe/ThongKeXNT_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  5.Chức năng xử lý
//  Lập hóa đơn
    public void lapHoaDon(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Lập phiếu đổi hàng
    public void lapPhieuDoiHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDoi/LapPhieuDoiHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Lập phiếu trả hàng
    public void lapPhieuTraHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuTra/LapPhieuTraHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Lập phiếu đặt hàng
    public void lapPhieuDatHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuDat/LapPhieuDatHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Lập phiếu nhập hàng
    public void nhapHang(ActionEvent actionEvent) {
        pnlChung.getChildren().clear();
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuNhapHang/LapPhieuNhapHang_GUI.fxml")));
            pnlChung.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
