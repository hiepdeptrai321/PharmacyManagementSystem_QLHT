--use master
--CREATE DATABASE QuanLyNhaThuoc;
--GO

--USE QuanLyNhaThuoc;
--GO

-- =========================
-- Bảng KhachHang
-- =========================
CREATE TABLE KhachHang (
    MaKH       VARCHAR(10) PRIMARY KEY,
    TenKH      NVARCHAR(50) NOT NULL,
    SDT        VARCHAR(15) NOT NULL,
    Email      VARCHAR(50),
    NgaySinh   DATE,
    GioiTinh   NVARCHAR(5) NOT NULL,
    DiaChi     NVARCHAR(50),
    TrangThai  NVARCHAR(10) NOT NULL
);
INSERT INTO KhachHang (MaKH, TenKH, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai) VALUES
('KH001', N'Nguyễn Văn An', '0905123456', 'an.nguyen@gmail.com', '1990-05-12', N'Nam', N'Hà Nội', N'Hoạt động'),
('KH002', N'Lê Thị Hoa', '0905789456', 'hoa.le@gmail.com', '1995-08-21', N'Nữ', N'Hải Phòng', N'Hoạt động'),
('KH003', N'Trần Văn Bình', '0912457896', 'binh.tran@gmail.com', '1988-11-03', N'Nam', N'TP HCM', N'Hoạt động'),
('KH004', N'Phạm Thị Mai', '0932458976', 'mai.pham@gmail.com', '1992-02-15', N'Nữ', N'Đà Nẵng', N'Hoạt động'),
('KH005', N'Hoàng Văn Nam', '0987654321', 'nam.hoang@gmail.com', '1985-12-20', N'Nam', N'Cần Thơ', N'Hoạt động'),
('KH006', N'Vũ Thị Lan', '0978456123', 'lan.vu@gmail.com', '1998-09-09', N'Nữ', N'Hải Dương', N'Hoạt động'),
('KH007', N'Đặng Văn Hùng', '0934567890', 'hung.dang@gmail.com', '1993-07-01', N'Nam', N'Bắc Ninh', N'Hoạt động'),
('KH008', N'Bùi Thị Thảo', '0967451230', 'thao.bui@gmail.com', '1996-01-22', N'Nữ', N'Quảng Ninh', N'Hoạt động'),
('KH009', N'Ngô Văn Tuấn', '0945789632', 'tuan.ngo@gmail.com', '1991-04-17', N'Nam', N'Thái Bình', N'Hoạt động'),
('KH010', N'Đỗ Thị Hạnh', '0923456789', 'hanh.do@gmail.com', '1994-03-05', N'Nữ', N'Ninh Bình', N'Hoạt động'),
('KH011', N'Nguyễn Nhựt Hảo', '0825902972', 'hao.dep.dzai3105@gmail.com', '2005-05-31', N'Nam', N'Đồng Tháp', N'Hoạt động');
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
    TrangThai  NVARCHAR(30) NOT NULL,
    TaiKhoan   VARCHAR(50) NOT NULL,
    MatKhau    VARCHAR(50) NOT NULL
);
INSERT INTO NhanVien (MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai, TaiKhoan, MatKhau) VALUES
('NV001', N'Đàm Thái An', '0912345678', 'thaian@gmail.com', '2005-01-01', N'Nam', N'Củ Chi', N'Hoạt động', 'thaian', '123'),
('NV002', N'Hoàng Phước Thành Công', '0363636363', 'thanhcong@gmail.com', '2005-02-02', N'Nữ', N'Huế', N'Hoạt động', 'thanhcong', '123'),
('NV003', N'Đỗ Phú Hiệp', '0181818181', 'phuhiep@gmail.com', '2003-03-03', N'Nam', N'An Giang', N'Hoạt động', 'phuhiep', '123'),
('NV004', N'Nguyễn Nhựt Hảo', '0636363636', 'nhuthao@gmail.com', '2005-05-31', N'Nam', N'Đồng Tháp', N'Không hoạt động', 'nhuthao', '123');
-- =========================
-- Bảng LuongNhanVien
-- =========================
CREATE TABLE LuongNhanVien (
    MaLNV      VARCHAR(10) PRIMARY KEY,
    TuNgay     DATE NOT NULL,
    DenNgay    DATE NOT NULL,
    LuongCoBan FLOAT NOT NULL,
    PhuCap     FLOAT NOT NULL,
    GhiChu     NVARCHAR(255) NOT NULL,
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV)
);
INSERT INTO LuongNhanVien (MaLNV, TuNgay, DenNgay, LuongCoBan, PhuCap, GhiChu, MaNV) VALUES
('LNV001', '2025-01-01', '2025-01-31', 8000000, 500000, N'Lương tháng 1', 'NV001'),
('LNV002', '2025-01-01', '2025-01-31', 7500000, 400000, N'Lương tháng 1', 'NV002'),
('LNV003', '2025-01-01', '2025-01-31', 9000000, 600000, N'Lương tháng 1', 'NV003'),
('LNV004', '2025-01-01', '2025-01-31', 7000000, 350000, N'Lương tháng 1', 'NV004');
-- =========================
-- Bảng NhaCungCap
-- =========================
CREATE TABLE NhaCungCap (
    MaNCC      VARCHAR(10) PRIMARY KEY,
    TenNCC     VARCHAR(50) NOT NULL,
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
    MaLH       VARCHAR(10) PRIMARY KEY,
    TenLH      NVARCHAR(50),
    MoTa       NVARCHAR(255)
);
INSERT INTO LoaiHang (MaLH, TenLH, MoTa) VALUES
('LH01', N'Thuốc Tây', N'Thuốc kê đơn, thuốc không kê đơn, thuốc điều trị bệnh lý thông thường...'),
('LH02', N'Vaccine', N'Chế phẩm sinh học giúp tạo miễn dịch, phòng ngừa các bệnh truyền nhiễm...'),
('LH03', N'Đông Y', N'Thuốc y học cổ truyền, thảo dược, cao, trà, thuốc sắc...'),
('LH04', N'Thực Phẩm Chức Năng', N'Vitamin, khoáng chất, sản phẩm tăng sức đề kháng, hỗ trợ miễn dịch...'),
('LH05', N'Dụng Cụ Y Tế', N'Nhiệt kế, máy đo huyết áp, bông băng, khẩu trang y tế...'),
('LH06', N'Mỹ Phẩm', N'Sữa rửa mặt, kem dưỡng da, dầu gội, sản phẩm chăm sóc da và tóc...');
-- =========================
-- Bảng NhomDuocLy
-- =========================
CREATE TABLE NhomDuocLy (
    MaNDL      VARCHAR(10) PRIMARY KEY,
    TenNDL     NVARCHAR(50),
    MoTa       NVARCHAR(255)
);
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
    HangSX     NVARCHAR(20),
    NuocSX     NVARCHAR(20),
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
    TrangThai  NVARCHAR(10) NOT NULL,
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
    MaHDong    VARCHAR(10) PRIMARY KEY,
    LoaiHD     VARCHAR(20),
    ThoiGian   DATE NOT NULL,
    MaNV       VARCHAR(10) FOREIGN KEY REFERENCES NhanVien(MaNV),
    BangDL	   VARCHAR(20),
	GhiChu     NVARCHAR(255)
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
    SoLuong    INT NOT NULL,
    DonGia     FLOAT NOT NULL,
    GiamGia    FLOAT NOT NULL,
    PRIMARY KEY (MaLH, MaPD)
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
    TenHoatChat NVARCHAR(50) NOT NULL
);
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
-- =========================
-- Bảng ChiTietHoatChat
-- =========================
CREATE TABLE ChiTietHoatChat (
    MaHoatChat VARCHAR(10) FOREIGN KEY REFERENCES HoatChat(MaHoatChat),
    MaThuoc    VARCHAR(10) FOREIGN KEY REFERENCES Thuoc_SanPham(MaThuoc),
    HamLuong   FLOAT NOT NULL,
    PRIMARY KEY (MaHoatChat, MaThuoc)
);
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
    TenLoai    NVARCHAR(50),
    MoTa       NVARCHAR(255)
);

-- =========================
-- Bảng KhuyenMai
-- =========================
CREATE TABLE KhuyenMai (
    MaKM       VARCHAR(10) PRIMARY KEY,
    TenKM      NVARCHAR(50) NOT NULL,
    GiaTriKM   FLOAT NOT NULL,
    LoaiGiaTri  VARCHAR(10) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    MoTa       NVARCHAR(255),
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
