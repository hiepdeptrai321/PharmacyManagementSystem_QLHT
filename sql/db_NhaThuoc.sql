use master
CREATE DATABASE QuanLyNhaThuoc;
GO

USE QuanLyNhaThuoc;
GO

-- =========================
-- Bảng KhachHang
-- =========================
CREATE TABLE KhachHang (
    MaKH       VARCHAR(10) PRIMARY KEY,
    TenKH      VARCHAR(50) NOT NULL,
    SDT        VARCHAR(15) NOT NULL,
    Email      VARCHAR(50),
    NgaySinh   DATE,
    GioiTinh   VARCHAR(5) NOT NULL,
    DiaChi     VARCHAR(50),
    TrangThai  VARCHAR(10) NOT NULL
);

-- =========================
-- Bảng NhanVien
-- =========================
CREATE TABLE NhanVien (
    MaNV       VARCHAR(10) PRIMARY KEY,
    TenNV      VARCHAR(50) NOT NULL,
    SDT        VARCHAR(15) NOT NULL,
    Email      VARCHAR(50),
    NgaySinh   DATE NOT NULL,
    GioiTinh   VARCHAR(5) NOT NULL,
    DiaChi     VARCHAR(50),
    TrangThai  VARCHAR(10) NOT NULL,
    TaiKhoan   VARCHAR(50) NOT NULL,
    MatKhau    VARCHAR(50) NOT NULL
);

-- =========================
-- Bảng LuongNhanVien
-- =========================
CREATE TABLE LuongNhanVien (
    MaLNV      VARCHAR(10) PRIMARY KEY,
    TuNgay     DATE NOT NULL,
    DenNgay    DATE NOT NULL,
    LuongCoBan FLOAT NOT NULL,
    PhuCap     FLOAT NOT NULL,
    GhiChu     VARCHAR(255) NOT NULL,
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);

-- =========================
-- Bảng NhaCungCap
-- =========================
CREATE TABLE NhaCungCap (
    MaNCC      VARCHAR(10) PRIMARY KEY,
    TenNCC     VARCHAR(50) NOT NULL,
    DiaChi     VARCHAR(100),
    SDT        VARCHAR(20) NOT NULL,
    Email      VARCHAR(50),
	GPKD       VARCHAR(50),
    GhiChu     VARCHAR(255),
    TenCongTy  VARCHAR(50),
    MSThue     VARCHAR(20)
);

-- =========================
-- Bảng LoaiHang
-- =========================
CREATE TABLE LoaiHang (
    MaLH       VARCHAR(10) PRIMARY KEY,
    TenNH      VARCHAR(50),
    MoTa       VARCHAR(255)
);

-- =========================
-- Bảng NhomDuocLy
-- =========================
CREATE TABLE NhomDuocLy (
    MaNDL      VARCHAR(10) PRIMARY KEY,
    TenNDL     VARCHAR(50),
    MoTa       VARCHAR(255)
);

-- =========================
-- Bảng Thuoc_SanPham
-- =========================
CREATE TABLE Thuoc_SanPham (
    MaThuoc    VARCHAR(10) PRIMARY KEY,
    TenThuoc   VARCHAR(100),
    HamLuong   INT,
    DonViHL    VARCHAR(20),
    DuongDung  VARCHAR(20),
    QuyCachDongGoi VARCHAR(20),
    SDK_GPNK   VARCHAR(20),
    HangSX     VARCHAR(20),
    NuocSX     VARCHAR(20),
    HinhAnh    VARCHAR(50),
	MaLH       VARCHAR(10) FOREIGN KEY REFERENCES LoaiHang(MaLH),
    MaNDL      VARCHAR(10) FOREIGN KEY REFERENCES NhomDuocLy(MaNDL)
);


-- =========================
-- Bảng PhieuNhap
-- =========================
CREATE TABLE PhieuNhap (
    MaPN       VARCHAR(10) PRIMARY KEY,
    NgayNhap   DATE NOT NULL,
    TrangThai  VARCHAR(10) NOT NULL,
    GhiChu     VARCHAR(255),
    MaNCC      VARCHAR(10) FOREIGN KEY REFERENCES NhaCungCap(MaNCC),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);

-- =========================
-- Bảng ChiTietPhieuNhap
-- =========================
CREATE TABLE ChiTietPhieuNhap (
    MaPN       VARCHAR(10) FOREIGN KEY REFERENCES PhieuNhap(MaPN),
    MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
	MaLH       VARCHAR(10) NOT NULL,
    SoLuong    INT NOT NULL,
    GiaNhap    FLOAT NOT NULL,
    ChietKhau  FLOAT NOT NULL,
    Thue       FLOAT NOT NULL,
    PRIMARY KEY (MaPN, MaThuoc, MaLH)
);

-- =========================
-- Bảng Thuoc_SP_TheoLo
-- =========================
CREATE TABLE Thuoc_SP_TheoLo (
    MaPN    VARCHAR(10),
    MaThuoc VARCHAR(10),
    MaLH    VARCHAR(10),
    SoLuongTon INT,
    NSX DATE,
    HSD DATE,
    PRIMARY KEY (MaLH),
    FOREIGN KEY (MaPN, MaThuoc, MaLH) REFERENCES ChiTietPhieuNhap(MaPN, MaThuoc, MaLH)
);

-- =========================
-- Bảng HoaDon
-- =========================
CREATE TABLE HoaDon (
    MaHD       VARCHAR(10) PRIMARY KEY,
    TongHD     FLOAT NOT NULL,
    NgayLap    DATE NOT NULL,
    TrangThai  VARCHAR(10) NOT NULL,
	MaKH       VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);


-- =========================
-- Bảng ChiTietHoaDon
-- =========================
CREATE TABLE ChiTietHoaDon (
	MaHD       VARCHAR(10) FOREIGN KEY REFERENCES HoaDon(MaHD),
    MaLH       VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SP_TheoLo(MaLH),
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL,
	PRIMARY KEY (MaHD, MaLH)
);

-- =========================
-- Bảng HoatDong
-- =========================
CREATE TABLE HoatDong (
    MaHDong    VARCHAR(10) PRIMARY KEY,
    LoaiHD     VARCHAR(20),
    ThoiGian   DATE NOT NULL,
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV),
    BangDL    VARCHAR(20),
	GhiChu     VARCHAR(255)
);


-- =========================
-- Bảng PhieuDoiHang
-- =========================
CREATE TABLE PhieuDoiHang (
    MaPD       VARCHAR(10) PRIMARY KEY,
    NgayLap    DATE NOT NULL,
    LyDoDoi    DATE NOT NULL,
    GhiChu     VARCHAR(255),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV),
    MaKH       VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH),
    MaHD       VARCHAR(10) FOREIGN KEY REFERENCES HoaDon(MaHD)
);

-- =========================
-- Bảng ChiTietPhieuDoiHang
-- =========================
CREATE TABLE ChiTietPhieuDoiHang (
    MaLH       VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SP_TheoLo(MaLH),
    MaPD       VARCHAR(10) FOREIGN KEY REFERENCES PhieuDoiHang(MaPD),
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL,
    PRIMARY KEY (MaLH, MaPD)
);

-- =========================
-- Bảng PhieuTraHang
-- =========================
CREATE TABLE PhieuTraHang (
    MaPT       INT PRIMARY KEY,
    NgayLap    DATE NOT NULL,
    LyDoTra    VARCHAR(20) NOT NULL,
    GhiChu     VARCHAR(255),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV),
    MaHD       VARCHAR(10) FOREIGN KEY REFERENCES HoaDon(MaHD),
    MaKH       VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH)
);

-- =========================
-- Bảng ChiTietPhieuTraHang
-- =========================
CREATE TABLE ChiTietPhieuTraHang (
    MaLH       VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SP_TheoLo(MaLH),
    MaPT       INT NOT NULL FOREIGN KEY REFERENCES PhieuTraHang(MaPT),
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL,
    PRIMARY KEY (MaLH, MaPT)
);

-- =========================
-- Bảng HoatChat
-- =========================
CREATE TABLE HoatChat (
    MaHoatChat VARCHAR(10) PRIMARY KEY,
    TenHoatChat VARCHAR(50) NOT NULL
);

-- =========================
-- Bảng ChiTietHoatChat
-- =========================
CREATE TABLE ChiTietHoatChat (
    MaHoatChat VARCHAR(10) FOREIGN KEY REFERENCES HoatChat(MaHoatChat),
    MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    HamLuong   FLOAT NOT NULL,
    PRIMARY KEY (MaHoatChat, MaThuoc)
);

-- =========================
-- Bảng DonViTinh
-- =========================
CREATE TABLE DonViTinh (
    MaDVT      VARCHAR(10) PRIMARY KEY,
    TenDonViTinh VARCHAR(50) NOT NULL,
    KiHieu     VARCHAR(10) NOT NULL
);

-- =========================
-- Bảng ChiTietDonViTinh
-- =========================
CREATE TABLE ChiTietDonViTinh (
    MaThuoc       VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    MaDVT      VARCHAR(10) FOREIGN KEY REFERENCES DonViTinh(MaDVT),
    HeSoQuyDoi INT NOT NULL,
    GiaNhap    FLOAT NOT NULL,
    GiaBan     FLOAT NOT NULL
	PRIMARY KEY(MaThuoc, MaDVT)
);
-- =========================
-- Bảng LoaiKhuyenMai
-- =========================
CREATE TABLE LoaiKhuyenMai (
    MaLoai     VARCHAR(10) PRIMARY KEY,
    TenLoai    VARCHAR(50),
    MoTa       VARCHAR(255)
);

-- =========================
-- Bảng KhuyenMai
-- =========================
CREATE TABLE KhuyenMai (
    MaKM       VARCHAR(10) PRIMARY KEY,
    TenKM      VARCHAR(50) NOT NULL,
    GiaTriKM   FLOAT NOT NULL,
    LoaiGiaTri  VARCHAR(10) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    MoTa       VARCHAR(255),
    MaLoai     VARCHAR(10) FOREIGN KEY REFERENCES LoaiKhuyenMai(MaLoai)
);

-- =========================
-- Bảng ChiTietKhuyenMai
-- =========================
CREATE TABLE ChiTietKhuyenMai (
    MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    MaKM       VARCHAR(10) FOREIGN KEY REFERENCES KhuyenMai(MaKM),
    SLApDung   INT NOT NULL,
    SLToiDa    INT NOT NULL,
    PRIMARY KEY (MaThuoc, MaKM)
);


-- =========================
-- Bảng Thuoc_SP_TangKem
-- =========================
CREATE TABLE Thuoc_SP_TangKem (
    MaKM       VARCHAR(10) FOREIGN KEY REFERENCES KhuyenMai(MaKM),
    MaThuocTangKem VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    SoLuong    INT NOT NULL,
    PRIMARY KEY (MaKM, MaThuocTangKem)
);
