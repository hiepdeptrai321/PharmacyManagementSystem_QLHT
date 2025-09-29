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

-- =========================
-- Bảng NhomDuocLy
-- =========================
CREATE TABLE NhomDuocLy (
    MaNDL      VARCHAR(10) PRIMARY KEY,
    TenNDL     NVARCHAR(50),
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


INSERT INTO NhanVien (MaNV, TenNV, SDT, Email, NgaySinh, GioiTinh, DiaChi, TrangThai, TaiKhoan, MatKhau) VALUES
('NV001', N'Đàm Thái An', '0912345678', 'thaian@gmail.com', '2005-01-01', N'Nam', N'Củ Chi', N'Hoạt động', 'thaian', '123'),
('NV002', N'Hoàng Phước Thành Công', '0363636363', 'thanhcong@gmail.com', '2005-02-02', N'Nữ', N'Huế', N'Hoạt động', 'thanhcong', '123'),
('NV003', N'Đỗ Phú Hiệp', '0181818181', 'phuhiep@gmail.com', '2003-03-03', N'Nam', N'An Giang', N'Hoạt động', 'phuhiep', '123'),
('NV004', N'Nguyễn Nhựt Hảo', '0636363636', 'nhuthao@gmail.com', '2005-05-31', N'Nam', N'Đồng Tháp', N'Không hoạt động', 'nhuthao', '123');

INSERT INTO LuongNhanVien (MaLNV, TuNgay, DenNgay, LuongCoBan, PhuCap, GhiChu, MaNV) VALUES
('LNV001', '2025-01-01', '2025-01-31', 8000000, 500000, N'Lương tháng 1', 'NV001'),
('LNV002', '2025-01-01', '2025-01-31', 7500000, 400000, N'Lương tháng 1', 'NV002'),
('LNV003', '2025-01-01', '2025-01-31', 9000000, 600000, N'Lương tháng 1', 'NV003'),
('LNV004', '2025-01-01', '2025-01-31', 7000000, 350000, N'Lương tháng 1', 'NV004');

INSERT INTO LoaiHang (MaLH, TenLH, MoTa) VALUES
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
-- Data DonViTinh



-- =========================
-- Data LoaiKhuyenMai



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



-- =========================
-- Data ChiTietDonViTinh


-- =========================
-- Data KhuyenMai


-- =========================
-- Data ChiTietKhuyenMai


-- =========================
-- Data Thuoc_SP_Tang_Kem

--------Hóa đơn
INSERT INTO HoaDon (MaHD, TongHD, NgayLap, TrangThai, MaKH, MaNV)
VALUES
('HD001', 34000, '2025-09-11 08:30:00', N'Hoàn tất', 'KH001', 'NV001'), -- (10*1500 + 10*1900) - Giảm 10% cho 15000 = 34000
('HD002', 3600, '2025-09-11 09:15:00', N'Hoàn tất', 'KH002', 'NV002'),
('HD003', 5000, '2025-09-11 10:45:00', N'Hoàn tất', NULL, 'NV003'),
('HD004', 34000, '2025-09-12 14:00:00', N'Hoàn tất', 'KH003', 'NV001'),
('HD005', 150000, '2025-09-12 16:30:00', N'Hoàn tất', NULL, 'NV002'),
('HD006', 148000, '2025-09-13 11:00:00', N'Hoàn tất', 'KH004', 'NV003'),
('HD007', 6000, '2025-09-13 15:20:00', N'Hoàn tất', NULL, 'NV001'),
('HD008', 600000, '2025-09-14 09:40:00', N'Hoàn tất', 'KH005', 'NV002'),
('HD009', 370000, '2025-09-14 13:00:00', N'Hoàn tất', 'KH006', 'NV003'),
('HD010', 14000, '2025-09-15 17:00:00', N'Hoàn tất', NULL, 'NV001');

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

INSERT INTO PhieuDoiHang (MaPD, NgayLap, LyDoDoi, GhiChu, MaNV, MaKH, MaHD)
VALUES
('PD001', '2025-09-12 10:00:00', N'Đổi sang loại khác', N'Khách vãng lai, đổi Ibuprofen sang Aspirin', 'NV002', NULL, 'HD003'),
('PD002', '2025-09-14 14:30:00', N'Khách muốn mua loại lớn hơn', N'Đổi Vitamin D3 sang Probiotic, có bù thêm tiền', 'NV003', NULL, 'HD005'),
('PD003', '2025-09-16 09:00:00', N'Sản phẩm không phù hợp', N'Đổi Amoxicillin lấy Paracetamol', 'NV001', 'KH001', 'HD001');

INSERT INTO ChiTietPhieuDoiHang (MaLH, MaPD, SoLuong, DonGia, GiamGia)
VALUES
-- PD001: Đổi Ibuprofen (LH00003) lấy Aspirin (LH00004)
('LH00003', 'PD001', -1, 2500, 0),
('LH00004', 'PD001', 2, 3000, 0),

-- PD002: Đổi Vitamin D3 (LH00011) lấy Probiotic (LH00012)
('LH00011', 'PD002', -1, 150000, 0),
('LH00012', 'PD002', 1, 320000, 0),

-- PD003: Đổi Amoxicillin (LH00002) lấy Paracetamol (LH00001)
('LH00002', 'PD003', -10, 1900, 0),
('LH00001', 'PD003', 10, 1500, 0);

INSERT INTO PhieuTraHang (MaPT, NgayLap, LyDoTra, GhiChu, MaNV, MaHD, MaKH)
VALUES
('PT001', '2025-09-13 10:30:00', N'Dư thừa', N'Trả lại Hoạt huyết dưỡng não và Cao ích mẫu', 'NV001', 'HD004', 'KH003'),
('PT002', '2025-09-15 11:45:00', N'Không phù hợp', N'Trả lại Kem chống nắng', 'NV002', 'HD008', 'KH005'),
('PT003', '2025-09-16 15:00:00', N'Mua nhầm', N'Trả lại Găng tay y tế', 'NV003', 'HD006', 'KH004');

INSERT INTO ChiTietPhieuTraHang (MaLH, MaPT, SoLuong, DonGia, GiamGia)
VALUES
-- PT001: Trả Hoạt huyết dưỡng não (LH00007) và Cao ích mẫu (LH00009)
('LH00007', 'PT001', 1, 9500, 0),
('LH00009', 'PT001', 1, 15000, 0),

-- PT002: Trả Kem chống nắng SPF50 (LH00015)
('LH00015', 'PT002', 1, 250000, 0),

-- PT003: Trả Găng tay y tế (LH00014)
('LH00014', 'PT003', 1, 1800, 0);