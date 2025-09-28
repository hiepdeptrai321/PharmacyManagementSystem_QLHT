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

INSERT INTO Thuoc_SanPham
(MaThuoc, TenThuoc, HamLuong, DonViHL, DuongDung, QuyCachDongGoi, SDK_GPNK, HangSX, NuocSX, HinhAnh, MaLH, MaNDL)
VALUES
-- Thuốc tân dược
('TS001','Paracetamol 500mg',500,'mg','Uống','Hộp 10 vỉ x 10 viên','VN-2345-19','DHG Pharma','Việt Nam','t001.jpg','LH01','NDL016'),
('TS002','Amoxicillin 500mg',500,'mg','Uống','Hộp 2 vỉ x 10 viên','VN-2134-19','Traphaco','Việt Nam','t002.jpg','LH01','NDL017'),
('TS003','Cefuroxime 250mg',250,'mg','Uống','Hộp 2 vỉ x 10 viên','VN-3241-19','GSK','Anh','t003.jpg','LH01','NDL017'),
('TS004','Vitamin C 1000mg',1000,'mg','Uống','Hộp 10 ống','VN-1232-19','Bayer','Đức','t004.jpg','LH01','NDL030'),
('TS005','Ibuprofen 400mg',400,'mg','Uống','Hộp 1 vỉ x 10 viên','VN-5675-19','Mekophar','Việt Nam','t005.jpg','LH01','NDL014'),
('TS006','Aspirin 81mg',81,'mg','Uống','Hộp 3 vỉ x 10 viên','VN-8678-19','Sanofi','Pháp','t006.jpg','LH01','NDL014'),
('TS007','Loratadine 10mg',10,'mg','Uống','Hộp 1 vỉ x 10 viên','VN-4564-21','DHG Pharma','Việt Nam','t007.jpg','LH01','NDL009'),
('TS008','Omeprazole 20mg',20,'mg','Uống','Hộp 2 vỉ x 7 viên','VN-2344-21','Traphaco','Việt Nam','t008.jpg','LH01','NDL027'),
('TS009','Metformin 500mg',500,'mg','Uống','Hộp 3 vỉ x 10 viên','VN-4569-21','Mekophar','Việt Nam','t009.jpg','LH01','NDL022'),
('TS010','Atorvastatin 20mg',20,'mg','Uống','Hộp 2 vỉ x 10 viên','VN-8254-21','Bayer','Đức','t010.jpg','LH01','NDL010'),
('TS011','Paracetamol 650mg',650,'mg','Uống','Hộp 10 vỉ x 10 viên','VN-8542-21','GSK','Anh','t011.jpg','LH01','NDL016'),
('TS012','Amoxicillin 250mg',250,'mg','Uống','Hộp 1 vỉ x 10 viên','VN-6258-21','Sanofi','Pháp','t012.jpg','LH01','NDL017'),
('TS013','Cefuroxime 500mg',500,'mg','Uống','Hộp 2 vỉ x 10 viên','VN-8345-21','DHG Pharma','Việt Nam','t013.jpg','LH01','NDL017'),
('TS014','Vitamin C 500mg',500,'mg','Uống','Hộp 10 ống','VN-8351-21','Traphaco','Việt Nam','t014.jpg','LH01','NDL030'),
('TS015','Ibuprofen 200mg',200,'mg','Uống','Hộp 1 vỉ x 10 viên','VN-7242-21','Mekophar','Việt Nam','t015.jpg','LH01','NDL014'),
('TS016','Aspirin 500mg',500,'mg','Uống','Hộp 3 vỉ x 10 viên','VN-8462-22','Bayer','Đức','t016.jpg','LH01','NDL014'),
('TS017','Loratadine 5mg',5,'mg','Uống','Hộp 1 vỉ x 10 viên','VN-7834-22','GSK','Anh','t017.jpg','LH01','NDL009'),
('TS018','Omeprazole 40mg',40,'mg','Uống','Hộp 2 vỉ x 7 viên','VN-4264-22','Sanofi','Pháp','t018.jpg','LH01','NDL027'),
('TS019','Metformin 850mg',850,'mg','Uống','Hộp 3 vỉ x 10 viên','VN-7834-22','DHG Pharma','Việt Nam','t019.jpg','LH01','NDL022'),
('TS020','Atorvastatin 40mg',40,'mg','Uống','Hộp 2 vỉ x 10 viên','VN-6354-22','Traphaco','Việt Nam','t020.jpg','LH01','NDL010'),
-- Đông y
('TS226','Hoạt huyết dưỡng não',250,'mg','Uống','Hộp 3 vỉ x 10 viên','VD-0001-23','Traphaco','Việt Nam','ts301.jpg','LH03',null),
('TS227','Boganic',null,null,'Uống','Hộp 3 vỉ x 10 viên','VD-0002-23','Traphaco','Việt Nam','ts302.jpg','LH03',null),
('TS228','Ích mẫu',250,'mg','Uống','Hộp 2 vỉ x 10 viên','VD-0003-23','DHG Pharma','Việt Nam','ts303.jpg','LH03',null),
('TS229','Siro ho Bảo Thanh',10,'ml','Uống','Chai 125 ml','VD-0004-23','Nam Dược','Việt Nam','ts304.jpg','LH03',null),
('TS230','Viên ngậm Strepsils thảo dược',null,null,'Ngậm','Hộp 2 vỉ x 12 viên','VD-0005-23','Reckitt','Anh','ts305.jpg','LH03',null),
('TS231','Cao ích mẫu',250,'mg','Uống','Lọ 100 viên','VD-0006-23','Traphaco','Việt Nam','ts306.jpg','LH03',null),
('TS232','Sâm bổ chính khí',null,null,'Uống','Lọ 30 viên','VD-0007-23','Công ty Dược OPC','Việt Nam','ts307.jpg','LH03',null),
('TS233','Kim tiền thảo',null,null,'Uống','Hộp 3 vỉ x 10 viên','VD-0008-23','Mekophar','Việt Nam','ts308.jpg','LH03',null),
('TS234','Nhất nhất thống phong',null,null,'Uống','Hộp 3 vỉ x 10 viên','VD-0009-23','Dược Nhất Nhất','Việt Nam','ts309.jpg','LH03',null),
('TS235','Hoàng liên giải độc hoàn',null,null,'Uống','Lọ 60 viên','VD-0010-23','Trung Quốc Dược','Trung Quốc','ts310.jpg','LH03',null),
-- Thực phẩm chức năng
('TS336','Vitamin D3 1000IU',1000,'IU','Uống','Lọ 100 viên','TPCN-0001-23','Nature Made','Mỹ','ts201.jpg','LH04',null),
('TS337','Omega-3 Fish Oil 1000mg',1000,'mg','Uống','Lọ 120 viên','TPCN-0002-23','Blackmores','Úc','ts202.jpg','LH04',null),
('TS338','Calcium + Vitamin D',500,'mg','Uống','Lọ 60 viên','TPCN-0003-23','Traphaco','Việt Nam','ts203.jpg','LH04',null),
('TS339','Collagen Type II',40,'mg','Uống','Lọ 30 viên','TPCN-0004-23','Neocell','Mỹ','ts204.jpg','LH04',null),
('TS340','Probiotic 10 strains',10,'tỷ CFU','Uống','Hộp 30 gói','TPCN-0005-23','Yakult','Nhật Bản','ts205.jpg','LH04',null),
('TS341','Multivitamin Daily',1,'viên','Uống','Lọ 100 viên','TPCN-0006-23','Centrum','Mỹ','ts206.jpg','LH04',null),
('TS342','Sâm Ngọc Linh Extract',500,'mg','Uống','Lọ 30 viên','TPCN-0007-23','Sâm Ngọc Linh Quảng Nam','Việt Nam','ts207.jpg','LH04',null),
('TS343','Ginkgo Biloba 120mg',120,'mg','Uống','Lọ 60 viên','TPCN-0008-23','Pharmaton','Thụy Sĩ','ts208.jpg','LH04',null),
('TS344','Vitamin C + Zinc',1000,'mg','Uống','Lọ 20 viên sủi','TPCN-0009-23','DHG Pharma','Việt Nam','ts209.jpg','LH04',null),
('TS345','Glucosamine 1500mg',1500,'mg','Uống','Lọ 60 viên','TPCN-0010-23','Puritan''s Pride','Mỹ','ts210.jpg','LH04',null),
-- Dụng cụ y tế
('TS446','Nhiệt kế điện tử',null,null,'Đo','Hộp 1 cái','DM-0001-23','Omron','Nhật Bản','ts401.jpg','LH05',null),
('TS447','Máy đo huyết áp bắp tay',null,null,'Đo','Hộp 1 cái','DM-0002-23','Microlife','Thụy Sĩ','ts402.jpg','LH05',null),
('TS448','Máy đo đường huyết',null,null,'Đo','Hộp 1 cái + que thử','DM-0003-23','Accu-Chek','Đức','ts403.jpg','LH05',null),
('TS449','Ống nghe y tế',null,null,'Khám','Hộp 1 cái','DM-0004-23','3M Littmann','Mỹ','ts404.jpg','LH05',null),
('TS450','Khẩu trang y tế 3 lớp',null,null,'Đeo','Hộp 50 cái','DM-0005-23','Bảo Thạch','Việt Nam','ts405.jpg','LH05',null),
('TS451','Găng tay y tế',null,null,'Đeo','Hộp 100 cái','DM-0006-23','Top Glove','Malaysia','ts406.jpg','LH05',null),
('TS452','Bơm tiêm dùng một lần 5ml',null,null,'Tiêm','Hộp 100 cái','DM-0007-23','Vinahankook','Việt Nam','ts407.jpg','LH05',null),
('TS453','Kháng khuẩn rửa tay nhanh',null,null,'Sát khuẩn','Chai 500ml','DM-0008-23','Lifebuoy','Việt Nam','ts408.jpg','LH05',null),
('TS454','Máy xông khí dung',null,null,'Hít','Hộp 1 cái','DM-0009-23','Omron','Nhật Bản','ts409.jpg','LH05',null),
('TS455','Miếng dán nhiệt',null,null,'Dán','Hộp 10 miếng','DM-0010-23','Kobayashi','Nhật Bản','ts410.jpg','LH05',null),
-- Mỹ phẩm
('TS556','Kem chống nắng SPF50',50,'ml','Bôi','Tuýp 50ml','MP-0001-23','Anessa','Nhật Bản','ts501.jpg','LH05',null),
('TS557','Sữa rửa mặt tạo bọt',100,'ml','Rửa mặt','Tuýp 100ml','MP-0002-23','Hada Labo','Nhật Bản','ts502.jpg','LH05',null),
('TS558','Nước hoa hồng cân bằng da',150,'ml','Bôi','Chai 150ml','MP-0003-23','Innisfree','Hàn Quốc','ts503.jpg','LH05',null),
('TS559','Serum Vitamin C 15%',30,'ml','Bôi','Lọ 30ml','MP-0004-23','Vichy','Pháp','ts504.jpg','LH05',null),
('TS560','Kem dưỡng ẩm ban đêm',50,'ml','Bôi','Hũ 50ml','MP-0005-23','Laneige','Hàn Quốc','ts505.jpg','LH05',null),
('TS561','Son dưỡng môi có màu',null,null,'Bôi','Thỏi 3g','MP-0006-23','Maybelline','Mỹ','ts506.jpg','LH05',null),
('TS562','Dầu gội thảo dược',300,'ml','Gội đầu','Chai 300ml','MP-0007-23','Thái Dương','Việt Nam','ts507.jpg','LH05',null),
('TS563','Kem trị mụn',20,'g','Bôi','Tuýp 20g','MP-0008-23','La Roche-Posay','Pháp','ts508.jpg','LH05',null),
('TS564','Mặt nạ dưỡng da Green Tea',25,'ml','Đắp mặt','Hộp 10 miếng','MP-0009-23','The Face Shop','Hàn Quốc','ts509.jpg','LH05',null),
('TS565','Nước hoa nữ Eau de Parfum',50,'ml','Xịt','Chai 50ml','MP-0010-23','Chanel','Pháp','ts510.jpg','LH05',null);

INSERT INTO PhieuNhap (MaPN, NgayNhap, TrangThai, GhiChu, MaNCC, MaNV)
VALUES
('PN001', '2025-09-01', 'Hoàn tất', 'Nhập thuốc giảm đau', 'NCC001', 'NV001'),
('PN002', '2025-09-02', 'Hoàn tất', 'Nhập kháng sinh', 'NCC002', 'NV001'),
('PN003', '2025-09-03', 'Hoàn tất', 'Nhập vitamin và khoáng chất', 'NCC003', 'NV001'),
('PN004', '2025-09-04', 'Hoàn tất', 'Nhập thuốc tim mạch', 'NCC004', 'NV001'),
('PN005', '2025-09-05', 'Hoàn tất', 'Nhập vaccine phòng bệnh', 'NCC005', 'NV001'),
('PN006', '2025-09-06', 'Hoàn tất', 'Nhập thực phẩm chức năng', 'NCC006', 'NV001'),
('PN007', '2025-09-07', 'Hoàn tất', 'Nhập thuốc đông y', 'NCC007', 'NV001'),
('PN008', '2025-09-08', 'Hoàn tất', 'Nhập dụng cụ y tế', 'NCC008', 'NV001'),
('PN009', '2025-09-09', 'Hoàn tất', 'Nhập mỹ phẩm chăm sóc da', 'NCC009', 'NV001'),
('PN010', '2025-09-10', 'Hoàn tất', 'Nhập hỗn hợp nhiều loại sản phẩm', 'NCC010', 'NV001');

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
