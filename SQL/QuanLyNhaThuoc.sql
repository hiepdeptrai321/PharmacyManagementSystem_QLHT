use master
CREATE DATABASE QuanLyNhaThuoc;
GO

USE QuanLyNhaThuoc;
GO

--Link thư mục hình ảnh thuốc 
DECLARE @path NVARCHAR(255) = N'C:\Users\hiepdeptrai\Desktop\hk1_2025-2026\QLHT\SQL\imgThuoc\';

-- =========================
-- Bảng KhachHang
-- =========================
CREATE TABLE KhachHang (
    MaKH       VARCHAR(10) PRIMARY KEY,
    TenKH      NVARCHAR(50) NOT NULL,
    SDT        VARCHAR(15) NOT NULL,
    Email      VARCHAR(50),
    NgaySinh   DATE,
    GioiTinh   BIT NOT NULL,
    DiaChi     NVARCHAR(50),
    TrangThai  BIT NOT NULL
);
-- =========================
-- Bảng NhanVien
-- =========================
CREATE TABLE NhanVien (
    MaNV       VARCHAR(10) PRIMARY KEY,
    TenNV      NVARCHAR(50) NOT NULL,
    SDT        VARCHAR(15) NOT NULL,
    Email      VARCHAR(50),
    NgaySinh   DATE NOT NULL,
    GioiTinh   NVARCHAR(5) NOT NULL,
    DiaChi     NVARCHAR(50),
	VaiTro	   NVARCHAR(20) NOT NULL,
    TrangThai  BIT NOT NULL,
    TaiKhoan   VARCHAR(50) NOT NULL,
    MatKhau    VARCHAR(50) NOT NULL,
	NgayVaoLam Date NOT NULL,
	NgayKetThuc Date,
	TrangThaiXoa BIT NOT NULL

);
-- =========================
-- Bảng LuongNhanVien
-- =========================
CREATE TABLE LuongNhanVien (
    MaLNV      VARCHAR(10) PRIMARY KEY,
    TuNgay     DATE NOT NULL,
    DenNgay    DATE ,
    LuongCoBan FLOAT NOT NULL,
    PhuCap     FLOAT NOT NULL,
    GhiChu     NVARCHAR(255) NOT NULL,
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);

-- =========================
-- Bảng NhaCungCap
-- =========================
CREATE TABLE NhaCungCap (
    MaNCC      VARCHAR(10) PRIMARY KEY,
    TenNCC     NVARCHAR(50) NOT NULL,
    DiaChi     NVARCHAR(100),
    SDT        VARCHAR(20) NOT NULL,
    Email      VARCHAR(50),
	GPKD       VARCHAR(50),
    GhiChu     NVARCHAR(255),
    TenCongTy  NVARCHAR(50),
    MSThue     VARCHAR(20)
);

-- =========================
-- Bảng LoaiHang
-- =========================
CREATE TABLE LoaiHang (
    MaLoaiHang       VARCHAR(10) PRIMARY KEY,
    TenLH      NVARCHAR(50),
    MoTa       NVARCHAR(255)
);

-- =========================
-- Bảng NhomDuocLy
-- =========================
CREATE TABLE NhomDuocLy (
    MaNDL      VARCHAR(10) PRIMARY KEY,
    TenNDL     NVARCHAR(50),
    MoTa       NVARCHAR(255)
);

-- =========================
-- Bảng KeHang
-- =========================
CREATE TABLE KeHang (
    MaKe      VARCHAR(10) PRIMARY KEY,
    TenKe     NVARCHAR(50),
    MoTa       NVARCHAR(255)
);

-- =========================
-- Bảng Thuoc_SanPham
-- =========================
CREATE TABLE Thuoc_SanPham (
    MaThuoc    VARCHAR(10) PRIMARY KEY,
    TenThuoc   NVARCHAR(100),
    HamLuong   INT,
    DonViHL    VARCHAR(20),
    DuongDung  NVARCHAR(20),
    QuyCachDongGoi NVARCHAR(20),
    SDK_GPNK   VARCHAR(20),
    HangSX     NVARCHAR(30),
    NuocSX     NVARCHAR(20),
    HinhAnh    VARBINARY(MAX) NULL,
	MaLoaiHang VARCHAR(10) FOREIGN KEY REFERENCES LoaiHang(MaLoaiHang),
    MaNDL      VARCHAR(10) FOREIGN KEY REFERENCES NhomDuocLy(MaNDL),
	ViTri	   VARCHAR(10) FOREIGN KEY REFERENCES KeHang(MaKe)
);


-- =========================
-- Bảng PhieuNhap
-- =========================
CREATE TABLE PhieuNhap (
    MaPN       VARCHAR(10) PRIMARY KEY,
    NgayNhap   DATE NOT NULL,
    TrangThai  BIT,
    GhiChu     NVARCHAR(255),
    MaNCC      VARCHAR(10) FOREIGN KEY REFERENCES NhaCungCap(MaNCC),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);

-- =========================
-- Bảng ChiTietPhieuNhap
-- =========================
CREATE TABLE ChiTietPhieuNhap (
    MaPN       VARCHAR(10) FOREIGN KEY REFERENCES PhieuNhap(MaPN),
    MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
	MaLH       VARCHAR(10) ,
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
    FOREIGN KEY (MaPN, MaThuoc,MaLH) REFERENCES ChiTietPhieuNhap(MaPN, MaThuoc,MaLH)
);

-- =========================
-- Bảng HoaDon
-- =========================
CREATE TABLE HoaDon (
    MaHD       VARCHAR(10) PRIMARY KEY,
    NgayLap    DATETIME NOT NULL,
    TrangThai  NVARCHAR(10) NOT NULL,
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
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHDong AS ('HD' + RIGHT('0000' + CAST(ID AS VARCHAR(4)), 4)) PERSISTED,
    LoaiHD NVARCHAR(20),
    ThoiGian DATETIME DEFAULT GETDATE(),
    BangDL NVARCHAR(50),
	MaNV VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV),
    NoiDung NVARCHAR(MAX)
);

-- =========================
-- Bảng PhieuDatHang
-- =========================
CREATE TABLE PhieuDatHang (
    MaPDat     VARCHAR(10) PRIMARY KEY,
    NgayLap    DATE NOT NULL,
    SoTienCoc  FLOAT,
    GhiChu     NVARCHAR(255),
    MaKH       VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);

-- =========================
-- Bảng ChiTietPhieuDatHang
-- =========================
CREATE TABLE ChiTietPhieuDatHang (
    MaPDat     VARCHAR(10) FOREIGN KEY REFERENCES PhieuDatHang(MaPDat),
    MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL DEFAULT 0,
    PRIMARY KEY (MaPDat, MaThuoc)
);


-- =========================
-- Bảng PhieuDoiHang
-- =========================
CREATE TABLE PhieuDoiHang (
    MaPD       VARCHAR(10) PRIMARY KEY,
    NgayLap    DATE NOT NULL,
    LyDoDoi    NVARCHAR(255) NOT NULL,
    GhiChu     NVARCHAR(255),
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
	MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL,
    PRIMARY KEY (MaLH, MaPD,MaThuoc)
);

-- =========================
-- Bảng PhieuTraHang
-- =========================
CREATE TABLE PhieuTraHang (
    MaPT       VARCHAR(10) PRIMARY KEY,
    NgayLap    DATE NOT NULL,
    LyDoTra    NVARCHAR(20) NOT NULL,
    GhiChu     NVARCHAR(255),
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV),
    MaHD       VARCHAR(10) FOREIGN KEY REFERENCES HoaDon(MaHD),
    MaKH       VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH)
);

-- =========================
-- Bảng ChiTietPhieuTraHang
-- =========================
CREATE TABLE ChiTietPhieuTraHang (
    MaLH       VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SP_TheoLo(MaLH),
    MaPT       VARCHAR(10) NOT NULL FOREIGN KEY REFERENCES PhieuTraHang(MaPT),
	MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL,
    PRIMARY KEY (MaLH, MaPT,MaThuoc)
);

-- =========================
-- Bảng HoatChat
-- =========================
CREATE TABLE HoatChat (
    MaHoatChat VARCHAR(10) PRIMARY KEY,
    TenHoatChat NVARCHAR(50) NOT NULL
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
    TenDonViTinh NVARCHAR(50) NOT NULL,
    KiHieu     NVARCHAR(10) NOT NULL
);

-- =========================
-- Bảng ChiTietDonViTinh
-- =========================
CREATE TABLE ChiTietDonViTinh (
    MaThuoc       VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    MaDVT      VARCHAR(10) FOREIGN KEY REFERENCES DonViTinh(MaDVT),
    HeSoQuyDoi INT NOT NULL,
    GiaNhap    FLOAT NOT NULL,
    GiaBan     FLOAT NOT NULL,
	DonViCoBan BIT NOT NULL DEFAULT 0,
	PRIMARY KEY(MaThuoc, MaDVT)
);
-- =========================
-- Bảng LoaiKhuyenMai
-- =========================
CREATE TABLE LoaiKhuyenMai (
    MaLoai     VARCHAR(10) PRIMARY KEY,
    TenLoai    NVARCHAR(50),
    MoTa       NVARCHAR(255)
);

-- =========================
-- Bảng KhuyenMai
-- =========================
CREATE TABLE KhuyenMai (
    MaKM       VARCHAR(10) PRIMARY KEY,
    TenKM      NVARCHAR(50) NOT NULL,
    GiaTriKM   FLOAT,
    LoaiGiaTri  VARCHAR(10),
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    MoTa       NVARCHAR(255),
	NgayTao	   DATETIME NOT NULL DEFAULT GETDATE(),
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











INSERT INTO KhachHang (MaKH, TenKH, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai) VALUES
('KH001', N'Nguyễn Văn An', '0905123456', 'an.nguyen@gmail.com', '1990-05-12', 1, N'Hà Nội', 1),
('KH002', N'Lê Thị Hoa', '0905789456', 'hoa.le@gmail.com', '1995-08-21', 0, N'Hải Phòng', 1),
('KH003', N'Trần Văn Bình', '0912457896', 'binh.tran@gmail.com', '1988-11-03', 1, N'TP HCM', 1),
('KH004', N'Phạm Thị Mai', '0932458976', 'mai.pham@gmail.com', '1992-02-15', 0, N'Đà Nẵng', 1),
('KH005', N'Hoàng Văn Nam', '0987654321', 'nam.hoang@gmail.com', '1985-12-20', 1, N'Cần Thơ', 1),
('KH006', N'Vũ Thị Lan', '0978456123', 'lan.vu@gmail.com', '1998-09-09', 0, N'Hải Dương', 1),
('KH007', N'Đặng Văn Hùng', '0934567890', 'hung.dang@gmail.com', '1993-07-01', 1, N'Bắc Ninh', 1),
('KH008', N'Bùi Thị Thảo', '0967451230', 'thao.bui@gmail.com', '1996-01-22', 0, N'Quảng Ninh', 1),
('KH009', N'Ngô Văn Tuấn', '0945789632', 'tuan.ngo@gmail.com', '1991-04-17', 1, N'Thái Bình', 1),
('KH010', N'Đỗ Thị Hạnh', '0923456789', 'hanh.do@gmail.com', '1994-03-05', 0, N'Ninh Bình', 1),
('KH011', N'Nguyễn Nhựt Hảo', '0825902972', 'hao.dep.dzai3105@gmail.com', '2005-05-31', 1, N'Đồng Tháp', 0);




INSERT INTO NhanVien (MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai, TaiKhoan, MatKhau, NgayVaoLam, NgayKetThuc,TrangThaiXoa, VaiTro) VALUES
('NV001', N'Đàm Thái An', '0912345678', 'thaian@gmail.com', '2005-01-01', N'Nam', N'Củ Chi', 1, 'thaian', '123', '2025-1-12', null,0,N'Quản lý'),
('NV002', N'Hoàng Phước Thành Công', '0363636363', 'thanhcong@gmail.com', '2005-02-02', N'Nữ', N'Huế', 1, 'thanhcong', '123', '2025-1-12', null,0,N'Quản lý'),
('NV003', N'Đỗ Phú Hiệp', '0181818181', 'phuhiep@gmail.com', '2003-03-03', N'Nam', N'An Giang', 1, 'phuhiep', '123', '2025-1-12', null,0,N'Nhân viên'),
('NV004', N'Nguyễn Nhựt Hảo', '0636363636', 'nhuthao@gmail.com', '2005-05-31', N'Nam', N'Đồng Tháp',1, 'nhuthao', '123', '2025-1-12', null,0,N'Nhân viên');

INSERT INTO LuongNhanVien (MaLNV, TuNgay, DenNgay, LuongCoBan, PhuCap, GhiChu, MaNV) VALUES
('LNV001', '2025-01-01', null, 8000000, 500000, N'Lương tháng 1', 'NV001'),
('LNV002', '2025-01-01', null, 7500000, 400000, N'Lương tháng 1', 'NV002'),
('LNV003', '2025-01-01', null, 9000000, 600000, N'Lương tháng 1', 'NV003'),
('LNV004', '2025-01-01', null, 7000000, 350000, N'Lương tháng 1', 'NV004');

INSERT INTO LoaiHang (MaLoaiHang, TenLH, MoTa) VALUES
('LH01', N'Thuốc Tây', N'Thuốc kê đơn, thuốc không kê đơn, thuốc điều trị bệnh lý thông thường...'),
('LH02', N'Vaccine', N'Chế phẩm sinh học giúp tạo miễn dịch, phòng ngừa các bệnh truyền nhiễm...'),
('LH03', N'Đông Y', N'Thuốc y học cổ truyền, thảo dược, cao, trà, thuốc sắc...'),
('LH04', N'Thực Phẩm Chức Năng', N'Vitamin, khoáng chất, sản phẩm tăng sức đề kháng, hỗ trợ miễn dịch...'),
('LH05', N'Dụng Cụ Y Tế', N'Nhiệt kế, máy đo huyết áp, bông băng, khẩu trang y tế...'),
('LH06', N'Mỹ Phẩm', N'Sữa rửa mặt, kem dưỡng da, dầu gội, sản phẩm chăm sóc da và tóc...');

-- 1. Tác dụng lên hệ thần kinh trung ương
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL001', N'An thần, gây ngủ', N'Thuốc an thần, hỗ trợ giấc ngủ'),
('NDL002', N'Giảm đau', N'Thuốc giảm cảm giác đau'),
('NDL003', N'Gây mê', N'Thuốc gây mê toàn thân hoặc cục bộ'),
('NDL004', N'Chống co giật', N'Thuốc phòng và điều trị động kinh'),
('NDL005', N'Chống trầm cảm, hưng trí', N'Điều trị rối loạn tâm thần');

-- 2. Tác dụng lên hệ thần kinh thực vật
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL006', N'Kích thích giao cảm (sympathomimetic)', N'Thuốc tăng hoạt động giao cảm'),
('NDL007', N'Ức chế giao cảm (sympatholytic)', N'Thuốc giảm hoạt động giao cảm'),
('NDL008', N'Kích thích phó giao cảm (parasympathomimetic)', N'Thuốc tăng hoạt động phó giao cảm'),
('NDL009', N'Ức chế phó giao cảm (parasympatholytic)', N'Thuốc giảm hoạt động phó giao cảm');

-- 3. Tác dụng lên hệ tim mạch – huyết áp
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL010', N'Thuốc chống tăng huyết áp', N'Điều trị cao huyết áp'),
('NDL011', N'Thuốc trợ tim, chống suy tim', N'Hỗ trợ chức năng tim'),
('NDL012', N'Thuốc chống rối loạn nhịp tim', N'Ổn định nhịp tim'),
('NDL013', N'Thuốc lợi tiểu', N'Tăng đào thải nước và muối');

-- 4. Tác dụng chống viêm – giảm đau – hạ sốt
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL014', N'NSAIDs', N'Thuốc kháng viêm không steroid'),
('NDL015', N'Corticoid', N'Thuốc kháng viêm steroid'),
('NDL016', N'Thuốc giảm đau gây nghiện và không gây nghiện', N'Điều trị đau mức độ nặng và nhẹ');

-- 5. Tác dụng kháng vi sinh vật, kháng ký sinh trùng
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL017', N'Kháng sinh', N'Diệt hoặc kìm khuẩn'),
('NDL018', N'Thuốc kháng virus', N'Ức chế hoặc tiêu diệt virus'),
('NDL019', N'Thuốc chống nấm', N'Điều trị nấm'),
('NDL020', N'Thuốc diệt ký sinh trùng, chống sốt rét', N'Điều trị giun sán, ký sinh trùng');

-- 6. Tác dụng kháng ung thư
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL021', N'Thuốc chống tăng sinh tế bào', N'Điều trị ung thư');

-- 7. Tác dụng điều hòa nội tiết
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL022', N'Hormone thay thế', N'Insulin, thyroxin, estrogen, testosterone...'),
('NDL023', N'Thuốc kháng hormone', N'Kháng estrogen, kháng androgen, kháng giáp...');

-- 8. Tác dụng trên hô hấp
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL024', N'Giãn phế quản', N'Mở rộng đường thở'),
('NDL025', N'Ức chế ho', N'Giảm phản xạ ho'),
('NDL026', N'Long đờm', N'Hỗ trợ tống đờm ra khỏi đường hô hấp');

-- 9. Tác dụng trên hệ tiêu hóa
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL027', N'Thuốc chống loét dạ dày – tá tràng', N'Ức chế bơm proton, kháng H2'),
('NDL028', N'Thuốc nhuận tràng, cầm tiêu chảy', N'Hỗ trợ điều hòa tiêu hóa'),
('NDL029', N'Thuốc chống nôn', N'Ngăn ngừa và điều trị buồn nôn');

-- 10. Tác dụng bổ trợ
INSERT INTO NhomDuocLy (MaNDL, TenNDL, MoTa) VALUES
('NDL030', N'Vitamin, khoáng chất', N'Bổ sung dưỡng chất'),
('NDL031', N'Thuốc tăng sức đề kháng', N'Hỗ trợ miễn dịch'),
('NDL032', N'Chế phẩm sinh học, vaccine', N'Phòng và hỗ trợ điều trị bệnh');

-- Thêm các đơn vị tính mới với kí hiệu viết tắt ngắn gọn
INSERT INTO DonViTinh (MaDVT, TenDonViTinh, KiHieu) VALUES
('DVT01', N'Viên', N'v'),
('DVT02', N'Vỉ', N'vỉ'),
('DVT03', N'Hộp', N'h'),
('DVT04', N'Chai', N'c'),
('DVT05', N'Lọ', N'lọ'),
('DVT06', N'Tuýp', N't'),
('DVT07', N'Gói', N'gói'),
('DVT08', N'Ống', N'ống'),
('DVT09', N'Thùng', N'th'),
('DVT10', N'Cái', N'cái'),
('DVT11', N'Cuộn', N'cuộn'),
('DVT12', N'Bịch', N'bịch'),
('DVT13', N'Lốc', N'lốc')

INSERT INTO HoatChat (MaHoatChat, TenHoatChat) VALUES
('HC001','Paracetamol'),
('HC002','Ibuprofen'),
('HC003','Diclofenac'),
('HC004','Aspirin'),
('HC005','Naproxen'),
('HC006','Amoxicillin'),
('HC007','Clavulanic acid'),
('HC008','Cefuroxime'),
('HC009','Cephalexin'),
('HC010','Cefixime'),
('HC011','Ciprofloxacin'),
('HC012','Levofloxacin'),
('HC013','Azithromycin'),
('HC014','Clarithromycin'),
('HC015','Doxycycline'),
('HC016','Metronidazole'),
('HC017','Omeprazole'),
('HC018','Esomeprazole'),
('HC019','Pantoprazole'),
('HC020','Ranitidine'),
('HC021','Amlodipine'),
('HC022','Losartan'),
('HC023','Valsartan'),
('HC024','Lisinopril'),
('HC025','Enalapril'),
('HC026','Atenolol'),
('HC027','Metoprolol'),
('HC028','Furosemide'),
('HC029','Spironolactone'),
('HC030','Hydrochlorothiazide'),
('HC031','Atorvastatin'),
('HC032','Simvastatin'),
('HC033','Rosuvastatin'),
('HC034','Metformin'),
('HC035','Glibenclamide'),
('HC036','Gliclazide'),
('HC037','Insulin (Regular)'),
('HC038','Insulin Glargine'),
('HC039','Prednisone'),
('HC040','Dexamethasone'),
('HC041','Betamethasone'),
('HC042','Loratadine'),
('HC043','Cetirizine'),
('HC044','Chlorpheniramine'),
('HC045','Salbutamol'),
('HC046','Budesonide'),
('HC047','Montelukast'),
('HC048','Warfarin'),
('HC049','Clopidogrel'),
('HC050','Heparin'),
('HC051','Vitamin C'),
('HC052','Vitamin D3'),
('HC053','Omega-3 Fatty Acids'),
('HC054','Calcium'),
('HC055','Collagen Type II'),
('HC056','Probiotic (Multi-strain)'),
('HC057','Multivitamins'),
('HC058','Saponin (Sâm Ngọc Linh)'),
('HC059','Ginkgo Biloba Extract'),
('HC060','Zinc'),
('HC061','Glucosamine Sulfate'),
('HC062','BCG Vaccine'),
('HC063','Polio Vaccine (OPV)'),
('HC064','Hepatitis B Surface Antigen'),
('HC065','Diphtheria Toxoid'),
('HC066','Tetanus Toxoid'),
('HC067','Pertussis Antigen'),
('HC068','Measles Virus Antigen'),
('HC069','Mumps Virus Antigen'),
('HC070','Rubella Virus Antigen');




INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, SDT, Email, GPKD, GhiChu, TenCongTy, MSThue)
VALUES
-- Công ty thuốc tân dược
('NCC001', N'Công ty Dược Hậu Giang', N'Cần Thơ, Việt Nam', '02923888888', 'HauGiang@dhgpharma.com.vn', 'GPKD-001', N'Chuyên cung cấp thuốc tân dược', N'DHG Pharma', '18001001'),
('NCC002', N'Công ty Vắc xin và Sinh phẩm số 1', N'Hà Nội, Việt Nam', '02438683333', 'misto@vabiotech.com.vn', 'GPKD-002', N'Nhà cung cấp vaccine', N'VABIOTECH', '01001002'),
('NCC003', N'Công ty Traphaco', N'Hà Nội, Việt Nam', '02436888333', 'Traphaco@traphaco.com.vn', 'GPKD-003', N'Thực phẩm chức năng & đông dược', N'Traphaco', '01001003'),
('NCC004', N'Công ty Domesco', N'Đồng Tháp, Việt Nam', '02773888888', 'Domesco@domesco.com.vn', 'GPKD-004', N'Chuyên cung cấp thuốc kê đơn', N'Domesco', '14001004'),
-- Công ty đông y
('NCC005', N'Công ty CP Dược phẩm OPC', N'TP.HCM, Việt Nam', '02838999999', 'opc@opcpharma.com', 'GPKD-005', N'Thuốc đông y & TPCN', N'OPC Pharma', '03001005'),
('NCC006', N'Công ty CP Y tế Mediplantex', N'Hà Nội, Việt Nam', '02438556677', 'Mediphantex@mediplantex.com.vn', 'GPKD-006', N'Dược liệu và đông y', N'Mediplantex', '01001006'),
-- Công ty vaccine
('NCC007', N'Công ty CP Dược phẩm Imexpharm', N'Đồng Tháp, Việt Nam', '02773889999', 'imexpharm@imexpharm.com', 'GPKD-007', N'Thuốc generic và TPCN', N'Imexpharm', '14001007'),
('NCC008', N'Công ty CP Dược phẩm Bidiphar', N'Bình Định, Việt Nam', '02563886666', 'Bidiphar@bidiphar.com.vn', 'GPKD-008', N'Thuốc tiêm và vaccine', N'Bidiphar', '41001008'),
-- Công ty chuyên dụng cụ y tế
('NCC009', N'Công ty CP Trang thiết bị Y tế Vinamed', N'Hà Nội, Việt Nam', '02438223344', 'Vinamed@vinamed.vn', 'GPKD-009', N'Cung cấp dụng cụ y tế', N'Vinamed', '01001009'),
-- Công ty chuyên mỹ phẩm
('NCC010', N'Công ty TNHH Mỹ phẩm Sài Gòn', N'TP.HCM, Việt Nam', '02837778888', 'saigoncosmetics@saigoncosmetics.com.vn', 'GPKD-010', N'Mỹ phẩm chăm sóc da', N'Saigon Cosmetics', '03001010');

INSERT INTO KeHang (MaKe, TenKe)
VALUES
('KE001', N'Kệ thuốc cảm sốt'),
('KE002', N'Kệ vitamin và thực phẩm chức năng'),
('KE003', N'Kệ thuốc kháng sinh'),
('KE004', N'Kệ thuốc giảm đau'),
('KE005', N'Kệ mỹ phẩm và chăm sóc da');


-- Insert các thuốc có hình ảnh
DECLARE @sql NVARCHAR(MAX);

-- Thêm từng sản phẩm
SET @sql = N'
INSERT INTO Thuoc_SanPham
(MaThuoc, TenThuoc, HamLuong, DonViHL, DuongDung, QuyCachDongGoi, SDK_GPNK, HangSX, NuocSX, HinhAnh, MaLoaiHang, MaNDL, ViTri)
VALUES
(''TS001'', N''Paracetamol 500mg'', 500, ''mg'', N''Uống'', N''Hộp 10 vỉ x 10 viên'',
 ''VN-2345-19'', ''DHG Pharma'', N''Việt Nam'',
 (SELECT * FROM OPENROWSET(BULK N''' + @path + 'TS001.jpg'', SINGLE_BLOB) AS img),
 ''LH01'', ''NDL016'', ''KE001''),

(''TS002'', N''Amoxicillin 500mg'', 500, ''mg'', N''Uống'', N''Hộp 2 vỉ x 10 viên'',
 ''VN-2134-19'', ''Traphaco'', N''Việt Nam'',
 (SELECT * FROM OPENROWSET(BULK N''' + @path + 'TS002.jpg'', SINGLE_BLOB) AS img),
 ''LH01'', ''NDL017'', ''KE001''),

(''TS003'', N''Cefuroxime 250mg'', 250, ''mg'', N''Uống'', N''Hộp 2 vỉ x 10 viên'',
 ''VN-3241-19'', ''GSK'', N''Anh'',
 (SELECT * FROM OPENROWSET(BULK N''' + @path + 'TS003.jpg'', SINGLE_BLOB) AS img),
 ''LH01'', ''NDL017'', ''KE001''),

(''TS004'', N''Vitamin C 1000mg'', 1000, ''mg'', N''Uống'', N''Hộp 10 ống'',
 ''VN-1232-19'', ''Bayer'', N''Đức'',
 (SELECT * FROM OPENROWSET(BULK N''' + @path + 'TS004.jpg'', SINGLE_BLOB) AS img),
 ''LH01'', ''NDL030'', ''KE001''),

(''TS005'', N''Ibuprofen 400mg'', 400, ''mg'', N''Uống'', N''Hộp 1 vỉ x 10 viên'',
 ''VN-5675-19'', ''Mekophar'', N''Việt Nam'',
 (SELECT * FROM OPENROWSET(BULK N''' + @path + 'TS005.jpg'', SINGLE_BLOB) AS img),
 ''LH01'', ''NDL014'', ''KE001'');
';

-- Thực thi câu lệnh động
EXEC sp_executesql @sql;

INSERT INTO Thuoc_SanPham
(MaThuoc, TenThuoc, HamLuong, DonViHL, DuongDung, QuyCachDongGoi, SDK_GPNK, HangSX, NuocSX, MaLoaiHang, MaNDL,ViTri)
VALUES
-- Thuốc tân dược
('TS006',N'Aspirin 81mg',81,'mg',N'Uống',N'Hộp 3 vỉ x 10 viên','VN-8678-19','Sanofi',N'Pháp','LH01','NDL014','KE001'),
('TS007',N'Loratadine 10mg',10,'mg',N'Uống',N'Hộp 1 vỉ x 10 viên','VN-4564-21','DHG Pharma',N'Việt Nam','LH01','NDL009','KE001'),
('TS008',N'Omeprazole 20mg',20,'mg',N'Uống',N'Hộp 2 vỉ x 7 viên','VN-2344-21','Traphaco',N'Việt Nam','LH01','NDL027','KE001'),
('TS009',N'Metformin 500mg',500,'mg',N'Uống',N'Hộp 3 vỉ x 10 viên','VN-4569-21','Mekophar',N'Việt Nam','LH01','NDL022','KE001'),
('TS010',N'Atorvastatin 20mg',20,'mg',N'Uống',N'Hộp 2 vỉ x 10 viên','VN-8254-21','Bayer',N'Đức','LH01','NDL010','KE001'),
('TS011',N'Paracetamol 650mg',650,'mg',N'Uống',N'Hộp 10 vỉ x 10 viên','VN-8542-21','GSK','Anh','LH01','NDL016','KE001'),
('TS012',N'Amoxicillin 250mg',250,'mg',N'Uống',N'Hộp 1 vỉ x 10 viên','VN-6258-21','Sanofi',N'Pháp','LH01','NDL017','KE001'),
('TS013',N'Cefuroxime 500mg',500,'mg',N'Uống',N'Hộp 2 vỉ x 10 viên','VN-8345-21','DHG Pharma',N'Việt Nam','LH01','NDL017','KE001'),
('TS014',N'Vitamin C 500mg',500,'mg',N'Uống',N'Hộp 10 ống','VN-8351-21','Traphaco',N'Việt Nam','LH01','NDL030','KE001'),
('TS015',N'Ibuprofen 200mg',200,'mg',N'Uống',N'Hộp 1 vỉ x 10 viên','VN-7242-21','Mekophar',N'Việt Nam','LH01','NDL014','KE001'),
('TS016',N'Aspirin 500mg',500,'mg',N'Uống',N'Hộp 3 vỉ x 10 viên','VN-8462-22','Bayer',N'Đức','LH01','NDL014','KE001'),
('TS017',N'Loratadine 5mg',5,'mg',N'Uống',N'Hộp 1 vỉ x 10 viên','VN-7834-22','GSK',N'Anh','LH01','NDL009','KE001'),
('TS018',N'Omeprazole 40mg',40,'mg',N'Uống',N'Hộp 2 vỉ x 7 viên','VN-4264-22','Sanofi',N'Pháp','LH01','NDL027','KE001'),
('TS019',N'Metformin 850mg',850,'mg',N'Uống',N'Hộp 3 vỉ x 10 viên','VN-7834-22','DHG Pharma',N'Việt Nam','LH01','NDL022','KE001'),
('TS020',N'Atorvastatin 40mg',40,'mg',N'Uống',N'Hộp 2 vỉ x 10 viên','VN-6354-22','Traphaco',N'Việt Nam','LH01','NDL010','KE001'),
-- Đông y
('TS226',N'Hoạt huyết dưỡng não',250,'mg',N'Uống',N'Hộp 3 vỉ x 10 viên','VD-0001-23','Traphaco',N'Việt Nam','LH03',null,'KE001'),
('TS227',N'Boganic',null,null,N'Uống',N'Hộp 3 vỉ x 10 viên','VD-0002-23','Traphaco',N'Việt Nam','LH03',null,'KE001'),
('TS228',N'Ích mẫu',250,'mg',N'Uống',N'Hộp 2 vỉ x 10 viên','VD-0003-23','DHG Pharma',N'Việt Nam','LH03',null,'KE001'),
('TS229',N'Siro ho Bảo Thanh',10,'ml',N'Uống',N'Chai 125 ml','VD-0004-23',N'Nam Dược',N'Việt Nam','LH03',null,'KE001'),
('TS230',N'Viên ngậm Strepsils thảo dược',null,null,N'Ngậm',N'Hộp 2 vỉ x 12 viên','VD-0005-23','Reckitt',N'Anh','LH03',null,'KE001'),
('TS231',N'Cao ích mẫu',250,'mg',N'Uống',N'Lọ 100 viên','VD-0006-23','Traphaco',N'Việt Nam','LH03',null,'KE001'),
('TS232',N'Sâm bổ chính khí',null,null,N'Uống',N'Lọ 30 viên','VD-0007-23',N'Công ty Dược OPC',N'Việt Nam','LH03',null,'KE001'),
('TS233',N'Kim tiền thảo',null,null,N'Uống',N'Hộp 3 vỉ x 10 viên','VD-0008-23',N'Mekophar',N'Việt Nam','LH03',null,'KE001'),
('TS234',N'Nhất nhất thống phong',null,null,N'Uống',N'Hộp 3 vỉ x 10 viên','VD-0009-23',N'Dược Nhất Nhất',N'Việt Nam','LH03',null,'KE001'),
('TS235',N'Hoàng liên giải độc hoàn',null,null,N'Uống',N'Lọ 60 viên','VD-0010-23',N'Trung Quốc Dược',N'Trung Quốc','LH03',null,'KE001'),
-- Thực phẩm chức năng
('TS336',N'Vitamin D3 1000IU',1000,'IU',N'Uống',N'Lọ 100 viên','TPCN-0001-23','Nature Made',N'Mỹ','LH04',null,'KE001'),
('TS337',N'Omega-3 Fish Oil 1000mg',1000,'mg',N'Uống',N'Lọ 120 viên','TPCN-0002-23','Blackmores',N'Úc','LH04',null,'KE001'),
('TS338',N'Calcium + Vitamin D',500,'mg',N'Uống',N'Lọ 60 viên','TPCN-0003-23','Traphaco',N'Việt Nam','LH04',null,'KE001'),
('TS339',N'Collagen Type II',40,'mg',N'Uống',N'Lọ 30 viên','TPCN-0004-23','Neocell',N'Mỹ','LH04',null,'KE001'),
('TS340',N'Probiotic 10 strains',10,'tỷ CFU',N'Uống',N'Hộp 30 gói','TPCN-0005-23','Yakult',N'Nhật Bản','LH04',null,'KE001'),
('TS341',N'Multivitamin Daily',1,'viên',N'Uống',N'Lọ 100 viên','TPCN-0006-23','Centrum',N'Mỹ','LH04',null,'KE001'),
('TS342',N'Sâm Ngọc Linh Extract',500,'mg',N'Uống',N'Lọ 30 viên','TPCN-0007-23',N'Sâm Ngọc Linh Quảng Nam',N'Việt Nam','LH04',null,'KE001'),
('TS343',N'Ginkgo Biloba 120mg',120,'mg',N'Uống',N'Lọ 60 viên','TPCN-0008-23','Pharmaton',N'Thụy Sĩ','LH04',null,'KE001'),
('TS344',N'Vitamin C + Zinc',1000,'mg',N'Uống',N'Lọ 20 viên sủi','TPCN-0009-23','DHG Pharma',N'Việt Nam','LH04',null,'KE001'),
('TS345',N'Glucosamine 1500mg',1500,'mg',N'Uống',N'Lọ 60 viên','TPCN-0010-23','Puritan''s Pride',N'Mỹ','LH04',null,'KE001'),
-- Dụng cụ y tế
('TS446',N'Nhiệt kế điện tử',null,null,N'Đo',N'Hộp 1 cái','DM-0001-23','Omron',N'Nhật Bản','LH05',null,'KE001'),
('TS447',N'Máy đo huyết áp bắp tay',null,null,N'Đo',N'Hộp 1 cái','DM-0002-23','Microlife',N'Thụy Sĩ','LH05',null,'KE001'),
('TS448',N'Máy đo đường huyết',null,null,N'Đo',N'Hộp 1 cái + que thử','DM-0003-23','Accu-Chek',N'Đức','LH05',null,'KE001'),
('TS449',N'Ống nghe y tế',null,null,N'Khám',N'Hộp 1 cái','DM-0004-23','3M Littmann',N'Mỹ','LH05',null,'KE001'),
('TS450',N'Khẩu trang y tế 3 lớp',null,null,N'Đeo',N'Hộp 50 cái','DM-0005-23',N'Bảo Thạch',N'Việt Nam','LH05',null,'KE001'),
('TS451',N'Găng tay y tế',null,null,N'Đeo',N'Hộp 100 cái','DM-0006-23','Top Glove','Malaysia','LH05',null,'KE001'),
('TS452',N'Bơm tiêm dùng một lần 5ml',null,null,N'Tiêm',N'Hộp 100 cái','DM-0007-23','Vinahankook',N'Việt Nam','LH05',null,'KE001'),
('TS453',N'Kháng khuẩn rửa tay nhanh',null,null,N'Sát khuẩn',N'Chai 500ml','DM-0008-23','Lifebuoy',N'Việt Nam','LH05',null,'KE001'),
('TS454',N'Máy xông khí dung',null,null,N'Hít',N'Hộp 1 cái','DM-0009-23','Omron',N'Nhật Bản','LH05',null,'KE001'),
('TS455',N'Miếng dán nhiệt',null,null,N'Dán',N'Hộp 10 miếng','DM-0010-23','Kobayashi',N'Nhật Bản','LH05',null,'KE001'),
-- Mỹ phẩm
('TS556',N'Kem chống nắng SPF50',50,'ml',N'Bôi',N'Tuýp 50ml','MP-0001-23','Anessa',N'Nhật Bản','LH05',null,'KE001'),
('TS557',N'Sữa rửa mặt tạo bọt',100,'ml',N'Rửa mặt',N'Tuýp 100ml','MP-0002-23','Hada Labo',N'Nhật Bản','LH05',null,'KE001'),
('TS558',N'Nước hoa hồng cân bằng da',150,'ml',N'Bôi',N'Chai 150ml','MP-0003-23','Innisfree',N'Hàn Quốc','LH05',null,'KE001'),
('TS559',N'Serum Vitamin C 15%',30,'ml',N'Bôi',N'Lọ 30ml','MP-0004-23','Vichy',N'Pháp','LH05',null,'KE001'),
('TS560',N'Kem dưỡng ẩm ban đêm',50,'ml',N'Bôi',N'Hũ 50ml','MP-0005-23','Laneige',N'Hàn Quốc','LH05',null,'KE001'),
('TS561',N'Son dưỡng môi có màu',null,null,N'Bôi',N'Thỏi 3g','MP-0006-23','Maybelline',N'Mỹ','LH05',null,'KE001'),
('TS562',N'Dầu gội thảo dược',300,'ml',N'Gội đầu',N'Chai 300ml','MP-0007-23',N'Thái Dương',N'Việt Nam','LH05',null,'KE001'),
('TS563',N'Kem trị mụn',20,'g',N'Bôi',N'Tuýp 20g','MP-0008-23','La Roche-Posay',N'Pháp','LH05',null,'KE001'),
('TS564',N'Mặt nạ dưỡng da Green Tea',25,'ml',N'Đắp mặt',N'Hộp 10 miếng','MP-0009-23','The Face Shop',N'Hàn Quốc','LH05',null,'KE001'),
('TS565',N'Nước hoa nữ Eau de Parfum',50,'ml',N'Xịt',N'Chai 50ml','MP-0010-23','Chanel',N'Pháp','LH05',null,'KE001');


INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS001', 'DVT01', 1, 800, 1000, 1),      -- Viên
('TS001', 'DVT02', 10, 7800, 9800, 0),     -- Vỉ
('TS001', 'DVT03', 100, 75000, 95000, 0);  -- Hộp

-- Thuốc: Amoxicillin 500mg (TS002) - Hộp 2 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS002', 'DVT01', 1, 1200, 1500, 1),     -- Viên
('TS002', 'DVT02', 10, 11800, 14500, 0),    -- Vỉ
('TS002', 'DVT03', 20, 23000, 28000, 0);   -- Hộp

-- Thuốc: Cefuroxime 250mg (TS003) - Hộp 2 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS003', 'DVT01', 1, 2000, 2500, 1),     -- Viên
('TS003', 'DVT02', 10, 19500, 24000, 0),    -- Vỉ
('TS003', 'DVT03', 20, 38000, 47000, 0);   -- Hộp

-- Thuốc: Vitamin C 1000mg (TS004) - Hộp 10 ống
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS004', 'DVT08', 1, 2500, 3200, 1),      -- Ống
('TS004', 'DVT03', 10, 24000, 30000, 0);   -- Hộp

-- Thuốc: Ibuprofen 400mg (TS005) - Hộp 1 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS005', 'DVT01', 1, 900, 1200, 1),       -- Viên
('TS005', 'DVT03', 10, 8500, 11000, 0);    -- Hộp

-- Thuốc: Aspirin 81mg (TS006) - Hộp 3 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS006', 'DVT01', 1, 500, 700, 1),        -- Viên
('TS006', 'DVT02', 10, 4800, 6800, 0),      -- Vỉ
('TS006', 'DVT03', 30, 14000, 19500, 0);   -- Hộp

-- Thuốc: Loratadine 10mg (TS007) - Hộp 1 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS007', 'DVT01', 1, 1500, 2000, 1),     -- Viên
('TS007', 'DVT03', 10, 14500, 19000, 0);   -- Hộp

-- Thuốc: Omeprazole 20mg (TS008) - Hộp 2 vỉ x 7 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS008', 'DVT01', 1, 1800, 2300, 1),     -- Viên
('TS008', 'DVT02', 7, 12000, 15500, 0),     -- Vỉ
('TS008', 'DVT03', 14, 23500, 30000, 0);   -- Hộp

-- Thuốc: Metformin 500mg (TS009) - Hộp 3 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS009', 'DVT01', 1, 700, 900, 1),       -- Viên
('TS009', 'DVT02', 10, 6800, 8800, 0),      -- Vỉ
('TS009', 'DVT03', 30, 20000, 26000, 0);  -- Hộp

-- Thuốc: Atorvastatin 20mg (TS010) - Hộp 2 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS010', 'DVT01', 1, 3000, 3800, 1),     -- Viên
('TS010', 'DVT02', 10, 29000, 37000, 0),    -- Vỉ
('TS010', 'DVT03', 20, 57000, 72000, 0);   -- Hộp

-- Thuốc: Hoạt huyết dưỡng não (TS226) - Hộp 3 vỉ x 10 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS226', 'DVT01', 1, 900, 1100, 1),      -- Viên
('TS226', 'DVT02', 10, 8800, 10800, 0),     -- Vỉ
('TS226', 'DVT03', 30, 26000, 32000, 0);   -- Hộp

-- Thuốc: Siro ho Bảo Thanh (TS229) - Chai 125 ml
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS229', 'DVT04', 1, 35000, 42000, 1),    -- Chai
('TS229', 'DVT09', 20, 680000, 800000, 0); -- Thùng (Giả định 1 thùng 20 chai)

-- Thuốc: Viên ngậm Strepsils thảo dược (TS230) - Hộp 2 vỉ x 12 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS230', 'DVT01', 1, 2000, 2500, 1),     -- Viên ngậm
('TS230', 'DVT02', 12, 23000, 29000, 0),    -- Vỉ
('TS230', 'DVT03', 24, 45000, 56000, 0);   -- Hộp

-- TPCN: Vitamin D3 1000IU (TS336) - Lọ 100 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS336', 'DVT01', 1, 1500, 2000, 1),     -- Viên
('TS336', 'DVT05', 100, 145000, 190000, 0); -- Lọ

-- TPCN: Omega-3 Fish Oil 1000mg (TS337) - Lọ 120 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS337', 'DVT01', 1, 2500, 3200, 1),     -- Viên
('TS337', 'DVT05', 120, 290000, 370000, 0);-- Lọ

-- TPCN: Probiotic 10 strains (TS340) - Hộp 30 gói
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS340', 'DVT07', 1, 4000, 5000, 1),      -- Gói
('TS340', 'DVT03', 30, 115000, 145000, 0); -- Hộp

-- TPCN: Vitamin C + Zinc (TS344) - Lọ 20 viên sủi
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS344', 'DVT01', 1, 3500, 4500, 1),     -- Viên sủi
('TS344', 'DVT05', 20, 68000, 85000, 0);   -- Lọ

-- TPCN: Glucosamine 1500mg (TS345) - Lọ 60 viên
INSERT INTO ChiTietDonViTinh (MaThuoc, MaDVT, HeSoQuyDoi, GiaNhap, GiaBan, DonViCoBan) VALUES
('TS345', 'DVT01', 1, 4000, 5500, 1),     -- Viên
('TS345', 'DVT05', 60, 235000, 310000, 0);-- Lọ
GO



INSERT INTO ChiTietHoatChat (MaHoatChat, MaThuoc, HamLuong) VALUES
-- Thuốc tây
('HC001','TS001',500),   
('HC006','TS002',500),   
('HC008','TS003',250),   
('HC051','TS004',1000),  
('HC002','TS005',400),   
('HC004','TS006',81),    
('HC042','TS007',10),    
('HC017','TS008',20),   
('HC034','TS009',500),   
('HC031','TS010',20),   
('HC001','TS011',650),  
('HC006','TS012',250),   
('HC008','TS013',500),   
('HC051','TS014',500),   
('HC002','TS015',200),   
('HC004','TS016',500),  
('HC042','TS017',5),     
('HC017','TS018',40),    
('HC034','TS019',850),   
('HC031','TS020',40); 

INSERT INTO PhieuNhap (MaPN, NgayNhap, TrangThai, GhiChu, MaNCC, MaNV)
VALUES
('PN001', '2025-09-01', 1 , N'Nhập thuốc giảm đau', 'NCC001', 'NV001'),
('PN002', '2025-09-02', 1, N'Nhập kháng sinh', 'NCC002', 'NV001'),
('PN003', '2025-09-03', 1, N'Nhập vitamin và khoáng chất', 'NCC003', 'NV001'),
('PN004', '2025-09-04', 1, N'Nhập thuốc tim mạch', 'NCC004', 'NV001'),
('PN005', '2025-09-05', 1, N'Nhập vaccine phòng bệnh', 'NCC005', 'NV001'),
('PN006', '2025-09-06', 1, N'Nhập thực phẩm chức năng', 'NCC006', 'NV001'),
('PN007', '2025-09-07', 1, N'Nhập thuốc đông y', 'NCC007', 'NV001'),
('PN008', '2025-09-08', 1, N'Nhập dụng cụ y tế', 'NCC008', 'NV001'),
('PN009', '2025-09-09', 1, N'Nhập mỹ phẩm chăm sóc da', 'NCC009', 'NV001'),
('PN010', '2025-09-10', 1, N'Nhập hỗn hợp nhiều loại sản phẩm', 'NCC010', 'NV001');

INSERT INTO ChiTietPhieuNhap (MaPN, MaThuoc, MaLH, SoLuong, GiaNhap, ChietKhau, Thue)
VALUES
-- PN001: Thuốc tây
('PN001','TS001','LH00001',100,1200,0.05,0.08),
('PN001','TS002','LH00002',80,1500,0.02,0.08),
('PN002','TS005','LH00003',50,1800,0.00,0.08),
('PN002','TS006','LH00004',60,2000,0.01,0.08),
('PN003','TS004','LH00005',120,900,0.00,0.05),
('PN003','TS010','LH00006',70,2500,0.03,0.08),
-- PN006: Đông y
('PN006','TS226','LH00007',90,7500,0.01,0.05),
('PN006','TS229','LH00008',60,18000,0.00,0.05),
('PN007','TS231','LH00009',100,12000,0.02,0.05),
('PN007','TS234','LH00010',80,22000,0.00,0.05),
-- PN008: Thực phẩm chức năng
('PN008','TS336','LH00011',100,120000,0.02,0.05),
('PN008','TS340','LH00012',80,250000,0.03,0.05),
-- PN009: Dụng cụ y tế
('PN009','TS446','LH00013',40,95000,0.02,0.08),
('PN009','TS451','LH00014',200,1200,0.00,0.08),
-- PN010: Mỹ phẩm
('PN010','TS556','LH00015',70,180000,0.05,0.05),
('PN010','TS560','LH00016',50,250000,0.03,0.05);

INSERT INTO Thuoc_SP_TheoLo (MaLH, MaPN, MaThuoc, SoLuongTon, NSX, HSD)
VALUES
-- LH01: thuốc tây
('LH00001','PN001','TS001',100,'2025-01-01','2027-01-01'),
('LH00002','PN001','TS002',80,'2025-02-01','2027-02-01'),
('LH00003','PN002','TS005',50,'2025-03-01','2026-03-01'),
('LH00004','PN002','TS006',60,'2025-03-15','2026-03-15'),
('LH00005','PN003','TS004',120,'2025-04-01','2026-04-01'),
('LH00006','PN003','TS010',70,'2025-05-01','2027-05-01'),

-- LH02: đông y
('LH00007','PN006','TS226',90,'2025-01-10','2028-01-10'),
('LH00008','PN006','TS229',60,'2025-02-20','2028-02-20'),
('LH00009','PN007','TS231',100,'2025-03-05','2028-03-05'),
('LH00010','PN007','TS234',80,'2025-04-10','2028-04-10'),

-- LH03: thực phẩm chức năng
('LH00011','PN008','TS336',100,'2025-01-25','2026-07-25'),
('LH00012','PN008','TS340',80,'2025-02-15','2026-08-15'),

-- LH04: dụng cụ y tế
('LH00013','PN009','TS446',40,'2025-01-01','2030-01-01'),
('LH00014','PN009','TS451',200,'2025-03-01','2030-03-01'),

-- LH05: mỹ phẩm
('LH00015','PN010','TS556',70,'2025-02-10','2027-02-10'),
('LH00016','PN010','TS560',50,'2025-03-20','2027-03-20');



-- Thêm dữ liệu vào bảng LoaiKhuyenMai
INSERT INTO LoaiKhuyenMai (MaLoai, TenLoai, MoTa)
VALUES
('LKM001', N'Tặng kèm sản phẩm', N'Khi khách hàng mua sản phẩm nhất định sẽ được tặng kèm thêm sản phẩm khác'),
('LKM002', N'Giảm giá trực tiếp', N'Giảm trực tiếp một số tiền nhất định trên tổng hóa đơn hoặc sản phẩm'),
('LKM003', N'Giảm giá phần trăm', N'Khách hàng được giảm theo tỷ lệ phần trăm trên giá trị sản phẩm hoặc hóa đơn');


-- Khuyến mãi cho 10 sản phẩm
INSERT INTO KhuyenMai (MaKM, TenKM, GiaTriKM, LoaiGiaTri, NgayBatDau, NgayKetThuc, MoTa, MaLoai)
VALUES
('KM011', N'Paracetamol giảm 10%', 10, '%', '2025-10-01', '2025-10-31', N'Giảm 10% cho Paracetamol 500mg', 'LKM003'),
('KM012', N'Amoxicillin giảm 20k', 20000, 'VND', '2025-10-05', '2025-10-25', N'Giảm 20.000đ khi mua Amoxicillin 500mg', 'LKM002'),
('KM013', N'Cefuroxime giảm 15%', 15, '%', '2025-10-01', '2025-10-20', N'Giảm 15% cho Cefuroxime 250mg', 'LKM003'),
('KM014', N'Vitamin C tặng Ibu + Para', NULL, NULL, '2025-10-01', '2025-10-31', N'Mua Vitamin C 1000mg tặng Ibuprofen 400mg và Paracetamol 500mg', 'LKM001'),
('KM015', N'Ibuprofen giảm 10k', 10000, 'VND', '2025-10-10', '2025-11-10', N'Giảm 10.000đ cho Ibuprofen 400mg', 'LKM002'),
('KM016', N'Ginkgo giảm 12%', 12, '%', '2025-10-01', '2025-10-31', N'Giảm 12% cho Ginkgo Biloba 120mg', 'LKM003'),
('KM017', N'C + Zinc tặng Ginkgo + Gluco', NULL, NULL, '2025-10-05', '2025-11-05', N'Mua Vitamin C + Zinc tặng Ginkgo Biloba 120mg và Glucosamine 1500mg', 'LKM001'),
('KM018', N'Glucosamine giảm 50k', 50000, 'VND', '2025-10-01', '2025-11-15', N'Giảm 50.000đ cho Glucosamine 1500mg', 'LKM002'),
('KM019', N'Nhiệt kế giảm 5%', 5, '%', '2025-10-01', '2025-12-01', N'Giảm 5% cho Nhiệt kế điện tử', 'LKM003'),
('KM020', N'Máy đo HA giảm 100k', 100000, 'VND', '2025-10-01', '2025-12-31', N'Giảm 100.000đ cho Máy đo huyết áp bắp tay', 'LKM002');


INSERT INTO ChiTietKhuyenMai (MaThuoc, MaKM, SLApDung, SLToiDa)
VALUES
('TS001', 'KM011', 1, 50),  -- Paracetamol giảm %
('TS002', 'KM012', 1, 50),  -- Amoxicillin giảm tiền
('TS007', 'KM012', 1, 50),  -- Amoxicillin giảm tiền
('TS015', 'KM012', 1, 50),  -- Amoxicillin giảm tiền
('TS003', 'KM013', 1, 50),  -- Cefuroxime giảm %
('TS005', 'KM013', 1, 50),  -- 
('TS004', 'KM014', 2, 20),  -- Vitamin C mua 2 tặng 1 (áp dụng tối đa 20 lần / hóa đơn)
('TS005', 'KM015', 1, 50),  -- Ibuprofen giảm tiền
('TS343', 'KM016', 1, 50),  -- Ginkgo giảm %
('TS344', 'KM017', 2, 15),  -- Vitamin C + Zinc mua 2 tặng 1 (áp dụng tối đa 15 lần)
('TS345', 'KM018', 1, 50),  -- Glucosamine giảm tiền
('TS446', 'KM019', 1, 30),  -- Nhiệt kế điện tử giảm %
('TS447', 'KM020', 1, 30);  -- Máy đo huyết áp giảm tiền


INSERT INTO Thuoc_SP_TangKem (MaKM, MaThuocTangKem, SoLuong)
VALUES
('KM014', 'TS005', 1),   -- Tặng 1 Ibuprofen 400mg
('KM014', 'TS001', 1),   -- Tặng 1 Paracetamol 500mg
('KM017', 'TS343', 1),   -- Tặng 1 Ginkgo Biloba 120mg
('KM017', 'TS345', 1);   -- Tặng 1 Glucosamine 1500mg



--------Hóa đơn
INSERT INTO HoaDon (MaHD, NgayLap, TrangThai, MaKH, MaNV)
VALUES
('HD001', '2025-09-11 08:30:00', N'Hoàn tất', 'KH001', 'NV001'),
('HD002', '2025-09-11 09:15:00', N'Hoàn tất', 'KH002', 'NV002'),
('HD003', '2025-09-11 10:45:00', N'Hoàn tất', NULL, 'NV003'),
('HD004', '2025-09-12 14:00:00', N'Hoàn tất', 'KH003', 'NV001'),
('HD005', '2025-09-12 16:30:00', N'Hoàn tất', NULL, 'NV002'),
('HD006', '2025-09-13 11:00:00', N'Hoàn tất', 'KH004', 'NV003'),
('HD007', '2025-09-13 15:20:00', N'Hoàn tất', NULL, 'NV001'),
('HD008', '2025-09-14 09:40:00', N'Hoàn tất', 'KH005', 'NV002'),
('HD009', '2025-09-14 13:00:00', N'Hoàn tất', 'KH006', 'NV003'),
('HD010', '2025-09-15 17:00:00', N'Hoàn tất', NULL, 'NV001');

INSERT INTO ChiTietHoaDon (MaHD, MaLH, SoLuong, DonGia, GiamGia)
VALUES
-- HD001
('HD001', 'LH00001', 10, 1500, 150), -- Paracetamol
('HD001', 'LH00002', 10, 1900, 0), -- Amoxicillin

-- HD002: Vitamin C 1000mg (3 chai)
('HD002', 'LH00005', 3, 1200, 0),

-- HD003: Ibuprofen 400mg (2 hộp)
('HD003', 'LH00003', 2, 2500, 0),

-- HD004: Hoạt huyết dưỡng não (2 hộp) và Cao ích mẫu (1 hộp)
('HD004', 'LH00007', 2, 9500, 0),
('HD004', 'LH00009', 1, 15000, 0),

-- HD005: Vitamin D3 1000IU (1 hộp)
('HD005', 'LH00011', 1, 150000, 0),

-- HD006: Nhiệt kế điện tử (1 cái) và Găng tay y tế (10 hộp)
('HD006', 'LH00013', 1, 130000, 0),
('HD006', 'LH00014', 10, 1800, 0),

-- HD007: Aspirin 81mg (2 hộp)
('HD007', 'LH00004', 2, 3000, 0),

-- HD008: Kem chống nắng (1 tuýp) và Kem dưỡng ẩm (1 hộp)
('HD008', 'LH00015', 1, 250000, 0),
('HD008', 'LH00016', 1, 350000, 0),

-- HD009: Siro ho Bảo Thanh (2 chai) và Probiotic 10 strains (1 hộp)
('HD009', 'LH00008', 2, 25000, 0),
('HD009', 'LH00012', 1, 320000, 0),

-- HD010: Atorvastatin 20mg (4 hộp)
('HD010', 'LH00006', 4, 3500, 0);



-- Dữ liệu mẫu cho PhieuDatHang
INSERT INTO PhieuDatHang (MaPDat, NgayLap, SoTienCoc, GhiChu, MaKH, MaNV)
VALUES
('PDH001', '2025-10-01', 50000, N'Khách đặt hàng mới', 'KH001', 'NV001'),
('PDH002', '2025-10-02', 100000, N'Đặt hàng lại lô thuốc cũ', 'KH002', 'NV002'),
('PDH003', '2025-10-03', 0, N'Khách đặt hàng gấp', 'KH003', 'NV003');
GO

-- Dữ liệu mẫu cho ChiTietPhieuDatHang
INSERT INTO ChiTietPhieuDatHang (MaPDat, MaThuoc, SoLuong, DonGia, GiamGia)
VALUES
('PDH001', 'TS001', 5, 12000, 0.05),
('PDH001', 'TS002', 10, 8000, 0),
('PDH002', 'TS003', 3, 15000, 0.1);



-- Dữ liệu mẫu cho PhieuDoiHang (Không thay đổi)
INSERT INTO PhieuDoiHang (MaPD, NgayLap, LyDoDoi, GhiChu, MaNV, MaKH, MaHD)
VALUES
('PD001', '2025-09-12', N'Đổi sang loại khác', N'Khách vãng lai, đổi Ibuprofen sang Aspirin', 'NV002', NULL, 'HD003'),
('PD002', '2025-09-14', N'Khách muốn mua loại lớn hơn', N'Đổi Vitamin D3 sang Probiotic, có bù thêm tiền', 'NV003', NULL, 'HD005'),
('PD003', '2025-09-16', N'Sản phẩm không phù hợp', N'Đổi Amoxicillin lấy Paracetamol', 'NV001', 'KH001', 'HD001');

INSERT INTO ChiTietPhieuDoiHang (MaLH, MaPD, MaThuoc, SoLuong, DonGia, GiamGia)
VALUES
-- PD001: Đổi Ibuprofen (LH00003/TS005) lấy Aspirin (LH00004/TS006)
('LH00003', 'PD001', 'TS005', -1, 2500, 0), -- Trả Ibuprofen
('LH00004', 'PD001', 'TS006', 2, 3000, 0),  -- Lấy Aspirin

-- PD002: Đổi Vitamin D3 (LH00011/TS336) lấy Probiotic (LH00012/TS340)
('LH00011', 'PD002', 'TS336', -1, 150000, 0), -- Trả Vitamin D3
('LH00012', 'PD002', 'TS340', 1, 320000, 0),  -- Lấy Probiotic

-- PD003: Đổi Amoxicillin (LH00002/TS002) lấy Paracetamol (LH00001/TS001)
('LH00002', 'PD003', 'TS002', -10, 1900, 0), -- Trả Amoxicillin
('LH00001', 'PD003', 'TS001', 10, 1500, 0);  -- Lấy Paracetamol

-- Dữ liệu mẫu cho PhieuTraHang (Sửa định dạng ngày tháng)
INSERT INTO PhieuTraHang (MaPT, NgayLap, LyDoTra, GhiChu, MaNV, MaHD, MaKH)
VALUES
('PT001', '2025-09-13', N'Dư thừa', N'Trả lại Hoạt huyết dưỡng não và Cao ích mẫu', 'NV001', 'HD004', 'KH003'),
('PT002', '2025-09-15', N'Không phù hợp', N'Trả lại Kem chống nắng', 'NV002', 'HD008', 'KH005'),
('PT003', '2025-09-16', N'Mua nhầm', N'Trả lại Găng tay y tế', 'NV003', 'HD006', 'KH004');

INSERT INTO ChiTietPhieuTraHang (MaLH, MaPT, MaThuoc, SoLuong, DonGia, GiamGia)
VALUES
-- PT001: Trả Hoạt huyết dưỡng não (LH00007/TS226) và Cao ích mẫu (LH00009/TS231) từ HD004
('LH00007', 'PT001', 'TS226', 1, 9500, 0),  -- Trả Hoạt huyết dưỡng não
('LH00009', 'PT001', 'TS231', 1, 15000, 0), -- Trả Cao ích mẫu

-- PT002: Trả Kem chống nắng (LH00015/TS556) từ HD008
('LH00015', 'PT002', 'TS556', 1, 250000, 0), -- Trả Kem chống nắng

-- PT003: Trả Găng tay y tế (LH00014/TS451) từ HD006
('LH00014', 'PT003', 'TS451', 1, 1800, 0);   -- Trả Găng tay y tế










--=======================================================================================================================
--=======================================================================================================================
--TRIGGER GHI LOG THÊM XÓA SỬA TRONG DANH MỤC THUỐC---------------------------------------------------------------------------------------------------------------
GO
CREATE OR ALTER TRIGGER trg_ThuocSanPham_Audit
ON Thuoc_SanPham
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @LoaiHD NVARCHAR(20);
    DECLARE @BangDL NVARCHAR(50) = N'Thuốc_Sản_Phẩm';
    DECLARE @NoiDung NVARCHAR(MAX) = N'';
	-- 👇 Lấy thông tin nhân viên từ CONTEXT_INFO()
	DECLARE @context VARBINARY(128) = CONTEXT_INFO();
	DECLARE @MaNV VARCHAR(10) = RTRIM(REPLACE(CAST(@context AS VARCHAR(128)), CHAR(0), ''));

    -- 🔹 1. Xác định loại hoạt động
    IF EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted)
        SET @LoaiHD = N'Cập nhật';
    ELSE IF EXISTS (SELECT 1 FROM inserted)
        SET @LoaiHD = N'Thêm mới';
    ELSE
        SET @LoaiHD = N'Xóa';

    -- 🔹 2. Tạo nội dung mô tả chi tiết thay đổi
    IF @LoaiHD = N'Thêm mới'
    BEGIN
        SELECT @NoiDung = STRING_AGG(
            CONCAT(
                N'Thêm thuốc mới: [',
                N'Mã thuốc = ', MaThuoc,
                N', Tên thuốc = ', TenThuoc,
                N', Hàm lượng = ', HamLuong,
                N', Đơn vị hàm lượng = ', DonViHL,
                N', Hãng sản xuất = ', HangSX,
                N', Nước sản xuất = ', NuocSX,
                N']'
            ), N'; '
        )
        FROM inserted;
    END
    ELSE IF @LoaiHD = N'Cập nhật'
    BEGIN
        SELECT @NoiDung = STRING_AGG(
            CONCAT(
                N'Cập nhật thuốc [', i.MaThuoc, N']: ',
                N'Tên thuốc: "', d.TenThuoc, N'" → "', i.TenThuoc, N'", ',
                N'Hàm lượng: ', d.HamLuong, N' → ', i.HamLuong, N', ',
                N'Đơn vị hàm lượng: "', d.DonViHL, N'" → "', i.DonViHL, N'", ',
                N'Hãng SX: "', d.HangSX, N'" → "', i.HangSX, N'", ',
                N'Nước SX: "', d.NuocSX, N'" → "', i.NuocSX, N'"'
            ), N'; '
        )
        FROM inserted i
        JOIN deleted d ON i.MaThuoc = d.MaThuoc;
    END
    ELSE IF @LoaiHD = N'Xóa'
    BEGIN
        SELECT @NoiDung = STRING_AGG(
            CONCAT(
                N'Xóa thuốc: [',
                N'Mã thuốc = ', MaThuoc,
                N', Tên thuốc = ', TenThuoc,
                N', Hàm lượng = ', HamLuong,
                N', Đơn vị hàm lượng = ', DonViHL,
                N', Hãng SX = ', HangSX,
                N', Nước SX = ', NuocSX,
                N']'
            ), N'; '
        )
        FROM deleted;
    END

    -- 🔹 3. Ghi log vào bảng HoatDong
    INSERT INTO HoatDong (LoaiHD, BangDL, NoiDung, MaNV)
    VALUES (@LoaiHD, @BangDL, @NoiDung, @MaNV);
END;
GO

-- TRIGGER GHI LOG CHỈNH SỬA HOẠT CHẤT TRONG DANH MỤC THUỐC
GO
CREATE OR ALTER TRIGGER trg_ChiTietHoatChat_Audit
ON ChiTietHoatChat
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaThuoc VARCHAR(10);
    DECLARE @NoiDung NVARCHAR(MAX) = N'';
    DECLARE @LoaiHD NVARCHAR(50);
    -- 👇 Lấy thông tin nhân viên từ CONTEXT_INFO()
	DECLARE @context VARBINARY(128) = CONTEXT_INFO();
	DECLARE @MaNV VARCHAR(10) = RTRIM(REPLACE(CAST(@context AS VARCHAR(128)), CHAR(0), ''));

    SELECT TOP 1 @MaThuoc = MaThuoc FROM inserted;
    IF @MaThuoc IS NULL
        SELECT TOP 1 @MaThuoc = MaThuoc FROM deleted;

    IF EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted)
        SET @LoaiHD = N'Cập nhật hoạt chất';
    ELSE IF EXISTS (SELECT 1 FROM inserted)
        SET @LoaiHD = N'Thêm hoạt chất';
    ELSE
        SET @LoaiHD = N'Xóa hoạt chất';

    SELECT @NoiDung = STRING_AGG(CONCAT(hc.TenHoatChat, ' (', ct.HamLuong, ')'), ', ')
    FROM ChiTietHoatChat ct
    JOIN HoatChat hc ON ct.MaHoatChat = hc.MaHoatChat
    WHERE ct.MaThuoc = @MaThuoc;

    IF @NoiDung IS NULL OR @NoiDung = ''
        SET @NoiDung = N'(Không còn hoạt chất)';

    DECLARE @ID INT;
    SELECT TOP 1 @ID = ID
    FROM HoatDong
    WHERE BangDL = 'Thuoc_SanPham'
      AND NoiDung LIKE '%MaThuoc=' + @MaThuoc + '%'
    ORDER BY ID DESC;

    IF @ID IS NOT NULL
    BEGIN
        DECLARE @NoiDungCu NVARCHAR(MAX);
        SELECT @NoiDungCu = NoiDung FROM HoatDong WHERE ID = @ID;

        IF @NoiDungCu LIKE N'%hoạt chất=['
        BEGIN
            UPDATE HoatDong
            SET NoiDung =
                STUFF(NoiDung,
                      CHARINDEX(N'hoạt chất=[', NoiDung),
                      LEN(NoiDung),
                      N'hoạt chất=[' + @NoiDung + N']')
            WHERE ID = @ID;
        END
        ELSE
        BEGIN
            UPDATE HoatDong
            SET NoiDung = NoiDung +
                N'; ' +
                CASE
                    WHEN @LoaiHD = N'Cập nhật hoạt chất' THEN N'Cập nhật hoạt chất=[' + @NoiDung + N']'
                    WHEN @LoaiHD = N'Thêm hoạt chất' THEN N'Thêm hoạt chất=[' + @NoiDung + N']'
                    WHEN @LoaiHD = N'Xóa hoạt chất' THEN N'Xóa hoạt chất=[' + @NoiDung + N']'
                    ELSE N'Hoạt động không xác định'
                END
            WHERE ID = @ID;
        END
    END
    ELSE
    BEGIN
        INSERT INTO HoatDong (LoaiHD, BangDL, NoiDung, MaNV)
        VALUES (
            @LoaiHD,
            'Thuoc_SanPham',
            N'[MaThuoc=' + @MaThuoc + N'] ' +
            CASE
                WHEN @LoaiHD = N'Cập nhật hoạt chất' THEN N'Cập nhật hoạt chất=[' + @NoiDung + N']'
                WHEN @LoaiHD = N'Thêm hoạt chất' THEN N'Thêm hoạt chất=[' + @NoiDung + N']'
                WHEN @LoaiHD = N'Xóa hoạt chất' THEN N'Xóa hoạt chất=[' + @NoiDung + N']'
                ELSE N'Hoạt động không xác định'
            END,
            @MaNV
        );
    END
END;
GO



--TRIGGER GHI LOG CHỈNH SỬA SỐ LƯỢNG TRONG CẬP NHẬT SỐ LƯỢNG---------------------------------------------------------------------------------------------------------------
GO
CREATE OR ALTER TRIGGER trg_ThuocSPTheoLo_Audit
ON Thuoc_SP_TheoLo
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @LoaiHD NVARCHAR(20);
    DECLARE @BangDL NVARCHAR(50) = N'Thuoc_SP_TheoLo';
    DECLARE @NoiDung NVARCHAR(MAX) = N'';

    -- 👇 Lấy thông tin nhân viên từ CONTEXT_INFO()
	DECLARE @context VARBINARY(128) = CONTEXT_INFO();
	DECLARE @MaNV VARCHAR(10) = RTRIM(REPLACE(CAST(@context AS VARCHAR(128)), CHAR(0), ''));

    IF EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted)
        SET @LoaiHD = N'Cập nhật';
    ELSE IF EXISTS (SELECT 1 FROM inserted)
        SET @LoaiHD = N'Thêm mới';
    ELSE
        SET @LoaiHD = N'Xóa';

    -- 🔹 2. Ghi nội dung mô tả (tập trung vào SoLuongTon)
    IF @LoaiHD = N'Thêm mới'
    BEGIN
        SELECT @NoiDung = STRING_AGG(
            CONCAT(
                N'Thêm lô: [Mã lô hàng=', MaLH,
                N', Mã thuốc=', MaThuoc,
                N', Mã phiếu nhập=', MaPN,
                N', Số lượng tồn=', SoLuongTon,
                N', Ngày sản xuất=', FORMAT(NSX, 'dd/MM/yyyy'),
                N', Hạn sử dụng=', FORMAT(HSD, 'dd/MM/yyyy'), N']'
            ), N'; '
        )
        FROM inserted;
    END
    ELSE IF @LoaiHD = N'Cập nhật'
    BEGIN
        SELECT @NoiDung = STRING_AGG(
            CONCAT(
                N'Cập nhật lô: [Mã lô hàng=', i.MaLH,
                N', Mã thuốc=', i.MaThuoc,
                N'] (Số lượng tồn: ', d.SoLuongTon, N' → ', i.SoLuongTon, N')'
            ), N'; '
        )
        FROM inserted i
        JOIN deleted d ON i.MaLH = d.MaLH
        WHERE ISNULL(i.SoLuongTon, 0) <> ISNULL(d.SoLuongTon, 0);
    END
    ELSE IF @LoaiHD = N'Xóa'
    BEGIN
        SELECT @NoiDung = STRING_AGG(
            CONCAT(
                N'Xóa lô: [Mã lô hàng=', MaLH,
                N', Mã thuốc=', MaThuoc,
                N', Số lượng tồn=', SoLuongTon, N']'
            ), N'; '
        )
        FROM deleted;
    END

    -- 🔹 3. Ghi vào bảng HoatDong (chỉ ghi khi có nội dung thực sự)
    IF (@NoiDung IS NOT NULL AND @NoiDung <> N'')
    BEGIN
        INSERT INTO HoatDong (LoaiHD, BangDL, NoiDung, MaNV)
        VALUES (@LoaiHD, @BangDL, @NoiDung, @MaNV);
    END
END;
GO



-- TRIGGER GHI LOG CHỈNH SỬA GÍA TRONG CẬP NHẬT GIÁ
CREATE OR ALTER TRIGGER trg_ChiTietDonViTinh_Audit
ON ChiTietDonViTinh
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaThuoc VARCHAR(10);
    DECLARE @NoiDung NVARCHAR(MAX) = N'';
    DECLARE @LoaiHD NVARCHAR(50);
	-- 👇 Lấy thông tin nhân viên từ CONTEXT_INFO()
	DECLARE @context VARBINARY(128) = CONTEXT_INFO();
	DECLARE @MaNV VARCHAR(10) = RTRIM(REPLACE(CAST(@context AS VARCHAR(128)), CHAR(0), ''));

    -- 🔹 Xác định thuốc bị thay đổi
    SELECT TOP 1 @MaThuoc = MaThuoc FROM inserted;
    IF @MaThuoc IS NULL
        SELECT TOP 1 @MaThuoc = MaThuoc FROM deleted;

    -- 🔹 Xác định loại hành động
    IF EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted)
        SET @LoaiHD = N'Cập nhật đơn vị tính';
    ELSE IF EXISTS (SELECT 1 FROM inserted)
        SET @LoaiHD = N'Thêm đơn vị tính';
    ELSE
        SET @LoaiHD = N'Xóa đơn vị tính';

    -- 🔹 Tạo mô tả chi tiết
    SELECT @NoiDung = STRING_AGG(
        CONCAT(
            dvt.TenDonViTinh,
            N' (Hệ số quy đổi: ', c.HeSoQuyDoi,
            N', Giá nhập: ', FORMAT(c.GiaNhap, 'N0'),
            N', Giá bán: ', FORMAT(c.GiaBan, 'N0'),
            CASE WHEN c.DonViCoBan = 1 THEN N', *Đơn vị cơ bản*' ELSE N'' END,
            N')'
        ), N'; '
    )
    FROM ChiTietDonViTinh c
    JOIN DonViTinh dvt ON c.MaDVT = dvt.MaDVT
    WHERE c.MaThuoc = @MaThuoc;

    IF @NoiDung IS NULL OR @NoiDung = ''
        SET @NoiDung = N'(Không còn đơn vị tính)';

    -- 🔹 Tìm hoạt động gần nhất của thuốc này
    DECLARE @ID INT;
    SELECT TOP 1 @ID = ID
    FROM HoatDong
    WHERE BangDL = 'Thuoc_SanPham'
      AND NoiDung LIKE '%MaThuoc=' + @MaThuoc + '%'
    ORDER BY ID DESC;

    IF @ID IS NOT NULL
    BEGIN
        DECLARE @NoiDungCu NVARCHAR(MAX);
        SELECT @NoiDungCu = NoiDung FROM HoatDong WHERE ID = @ID;

        IF @NoiDungCu LIKE N'%đơn vị tính = %'
        BEGIN
            UPDATE HoatDong
            SET NoiDung =
                STUFF(NoiDung,
                      CHARINDEX(N'đơn vị tính = ', NoiDung),
                      LEN(NoiDung),
                      N'đơn vị tính = [' + @NoiDung + N']')
            WHERE ID = @ID;
        END
        ELSE
        BEGIN
            UPDATE HoatDong
            SET NoiDung = NoiDung +
                N'; ' +
                CASE
                    WHEN @LoaiHD = N'Cập nhật đơn vị tính' THEN N'Cập nhật đơn vị tính = [' + @NoiDung + N']'
                    WHEN @LoaiHD = N'Thêm đơn vị tính' THEN N'Thêm đơn vị tính = [' + @NoiDung + N']'
                    WHEN @LoaiHD = N'Xóa đơn vị tính' THEN N'Xóa đơn vị tính = [' + @NoiDung + N']'
                    ELSE N'Hoạt động không xác định'
                END
            WHERE ID = @ID;
        END
    END
    ELSE
    BEGIN
        INSERT INTO HoatDong (LoaiHD, BangDL, NoiDung, MaNV)
        VALUES (
            @LoaiHD,
            'Thuoc_SanPham',
            N'[MaThuoc=' + @MaThuoc + N'] ' +
            CASE
                WHEN @LoaiHD = N'Cập nhật đơn vị tính' THEN N'Cập nhật đơn vị tính = [' + @NoiDung + N']'
                WHEN @LoaiHD = N'Thêm đơn vị tính' THEN N'Thêm đơn vị tính = [' + @NoiDung + N']'
                WHEN @LoaiHD = N'Xóa đơn vị tính' THEN N'Xóa đơn vị tính = [' + @NoiDung + N']'
                ELSE N'Hoạt động không xác định'
            END,
            @MaNV
        );
    END
END;
GO

CREATE PROCEDURE sp_InsertNhanVien
    @HoTen NVARCHAR(50),
    @SDT VARCHAR(15),
    @Email VARCHAR(100),
    @NamSinh DATE,
	@GioiTinh BIT,
    @DiaChi NVARCHAR(100),
	@TrangThai BIT,
    @NgayVaoLam DATE,
    @MaTK VARCHAR(30),
	@MatKhau VARCHAR(30)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @NewMaNV VARCHAR(10);
    DECLARE @MaxMaNV VARCHAR(10);
    DECLARE @NumPart INT;

    -- Lấy mã cao nhất hiện có (ví dụ NV012)
    SELECT @MaxMaNV = MAX(MaNV)
    FROM NhanVien;

    IF @MaxMaNV IS NULL
        SET @NewMaNV = 'NV001';
    ELSE
    BEGIN
        -- Cắt phần số, +1 và định dạng lại
        SET @NumPart = CAST(SUBSTRING(@MaxMaNV, 3, LEN(@MaxMaNV)) AS INT) + 1;
        SET @NewMaNV = 'NV' + RIGHT('000' + CAST(@NumPart AS VARCHAR(3)), 3);
    END

    -- Thêm nhân viên mới
    INSERT INTO NhanVien(MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai, TaiKhoan, MatKhau, NgayVaoLam, NgayKetThuc)
    VALUES(@NewMaNV, @HoTen, @SDT, @Email, @NamSinh,@GioiTinh, @DiaChi, @TrangThai,@MaTK,@MatKhau,@NgayVaoLam,null);

    -- Xuất mã nhân viên mới
    SELECT @NewMaNV AS MaNhanVienMoi;
END;


