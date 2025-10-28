package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThemThuoc_Ctrl {

    public Button btnHuy;
    public Button btnThem;
    public TextField txtTenThuoc;
    public ComboBox cbxLoaiHang;
    public ComboBox cbxViTri;
    public TextField txtMaThuoc;
    public ImageView imgThuoc_SanPham;
    public TextField txtDonViHamLuong;
    public TextField txtHamLuong;
    public TextField txtHangSanXuat;
    public ComboBox cbxNhomDuocLy;
    public TextField txtNuocSanXuat;
    public TextField txtQuyCachDongGoi;
    public TextField txtSDK_GPNK;
    public TextField txtDuongDung;
    public ComboBox cbDVTCB;
    public TableColumn<ChiTietHoatChat,String> colMaHoatChat;
    public TableColumn<ChiTietHoatChat,String> colTenHoatChat;
    public TableColumn<ChiTietHoatChat,String> colHamLuong;
    public TableColumn<ChiTietHoatChat,String> colXoa;
    public TextField txtTimKiemHoatChat;
    public ListView listViewHoatChat;
    public TableView<ChiTietHoatChat> tblHoatChat;
    private ObservableList<HoatChat> allHoatChat;
    private List<ChiTietHoatChat> listChiTietHoatChat = new ArrayList<>();
    private DanhMucThuoc_Ctrl parentController;
    private Thuoc_SanPham thuocThem = new Thuoc_SanPham();
    private boolean isThayDoi = false;

    public void initialize() {

//      Load các combobox mẫu
        cbxLoaiHang.getItems().add(0, "Chọn loại hàng");
        cbxViTri.getItems().add(0, "Chọn vị trí");
        cbxNhomDuocLy.getItems().add(0, "Chọn nhóm dược lý");
        cbxLoaiHang.getItems().addAll(new LoaiHang_Dao().getAllLoaiHang());
        cbxLoaiHang.getSelectionModel().selectFirst();
        cbxViTri.getItems().addAll(new KeHang_Dao().getAllTenKe());
        cbxViTri.getSelectionModel().selectFirst();
        cbxNhomDuocLy.getItems().addAll( new NhomDuocLy_Dao().getAllTenNhomDuocLy());
        cbxNhomDuocLy.getSelectionModel().selectFirst();
        List<DonViTinh> list = new DonViTinh_Dao().selectAll();
        for(DonViTinh donViTinh : list){
            cbDVTCB.getItems().add(donViTinh.getTenDonViTinh());
        }

//      Load bảng thuốc để khai báo bảng
        loadBangThuoc();

//      Tạo sự kiện cho listViewHoatChat
        listViewHoatChat.setVisible(false);
        listView();
        txtTimKiemHoatChat.textProperty().addListener((obs, oldVal, newVal) -> {
//          Nếu txtTimKiemHoatChat được nhập thì listViewHoatChat sẽ hiện còn nếu txtTimKiemHoatChat trống thì sẽ tắt
            if (newVal != null && !newVal.trim().isEmpty()) {
                listViewHoatChat.setVisible(true);
                locDanhSachHoatChat(newVal, oldVal);
            } else {
                listViewHoatChat.setVisible(false);
            }
        });

        listViewHoatChat.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                HoatChat hoatChat = ((HoatChat) newVal);
                txtTimKiemHoatChat.clear();
                listViewHoatChat.setVisible(false);
//              Kiểm tra nếu Hoạt chất đã có trong bảng chưa
                if (tblHoatChat.getItems().stream().noneMatch(item -> item.getHoatChat() != null && item.getHoatChat().getMaHoatChat().equals(hoatChat.getMaHoatChat()))) {
                    ChiTietHoatChat chtc = new ChiTietHoatChat();
                    chtc.setHoatChat(hoatChat);
//                  Tạo dialog để nhập hàm lượng cho hoạt chất
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nhập hàm lượng");
                    dialog.setHeaderText("Vui lòng nhập hàm lượng cho hoạt chất: " + hoatChat.getTenHoatChat());
                    dialog.setContentText("Hàm lượng:");
                    dialog.showAndWait().ifPresent(hamLuong -> {
                        if(!hamLuong.matches("\\d+(\\.\\d+)?")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setHeaderText(null);
                            alert.setContentText("Hàm lượng không hợp lệ! Vui lòng nhập số.");
                            alert.showAndWait();
                            return;
                        }
                        chtc.setHamLuong(Float.parseFloat(hamLuong));
//                      Thêm chiTietHoatChat vào list để lưu lại khi thêm thuốc sẽ được thêm vào chi tiết hoạt chất
                        listChiTietHoatChat.add(chtc);
                        tblHoatChat.getItems().add(chtc);
                    });
                    Platform.runLater(() -> {
                        listViewHoatChat.getSelectionModel().clearSelection();
                        listViewHoatChat.refresh();
                    });
                } else {
//                  Thông báo lỗi khi hoạt chất đã tồn tại trong bảng
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Hoạt chất đã tồn tại trong danh sách!");
                    alert.showAndWait();
//                  Clear listViewHoatChat để lần sau thêm 1 hoạt chất khác
                    Platform.runLater(() -> {
                        listViewHoatChat.getSelectionModel().clearSelection();
                        listViewHoatChat.refresh();
                    });
                }
            }
        });

//      Thêm mã thuốc
        Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();
        txtMaThuoc.setText(thuocDao.generatekeyThuocSanPham());
    }

//  Khai báo bảng thuốc
    public void loadBangThuoc(){
//      Thêm button cho colXoa
        colMaHoatChat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoatChat().getMaHoatChat()));
        colTenHoatChat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoatChat().getTenHoatChat()));
        colHamLuong.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getHamLuong())));
        colXoa.setCellFactory(celldata -> new TableCell<ChiTietHoatChat,String>(){
            private final Button btn = new Button("Xóa");
            {
                btn.setOnAction(event -> {
                    ChiTietHoatChat chtc = getTableView().getItems().get(getIndex());
                    new ChiTietHoatChat_Dao().deleteById(chtc.getThuoc().getMaThuoc(), chtc.getHoatChat().getMaHoatChat());
                    getTableView().getItems().remove(chtc);
                    listChiTietHoatChat.removeIf(item ->
                            item.getThuoc().getMaThuoc().equals(chtc.getThuoc().getMaThuoc()) &&
                                    item.getHoatChat().getMaHoatChat().equals(chtc.getHoatChat().getMaHoatChat()));

                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

//  Action button hủy
    public void btnHuyClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

//  Action button chonFile
    public void chonFile(ActionEvent actionEvent) {
//      thêm díalog để chọn file ảnh
        Stage stage = (Stage) txtMaThuoc.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh thuốc");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imgThuoc_SanPham.setImage(image);
        }
    }

//  Action button ThemThuoc
    public void btnThemThuoc(ActionEvent actionEvent) {
        if(kiemTraHopLe()) {
            thuocThem.setMaThuoc(txtMaThuoc.getText());
            thuocThem.setTenThuoc(txtTenThuoc.getText());
            thuocThem.setHamLuong(Float.parseFloat(txtHamLuong.getText()));
            thuocThem.setDonViHamLuong(txtDonViHamLuong.getText());
            thuocThem.setHangSX(txtHangSanXuat.getText());
            if(cbxNhomDuocLy.getSelectionModel().getSelectedIndex() > 0){
                thuocThem.setNhomDuocLy(new NhomDuocLy_Dao().selectByTenNhomDuocLy(cbxNhomDuocLy.getSelectionModel().getSelectedItem().toString()));
            }else{
                thuocThem.setNhomDuocLy(null);
            }
            thuocThem.setNuocSX(txtNuocSanXuat.getText());
            thuocThem.setQuyCachDongGoi(txtQuyCachDongGoi.getText());
            thuocThem.setSDK_GPNK(txtSDK_GPNK.getText());
            thuocThem.setDuongDung(txtDuongDung.getText());
            if (cbxLoaiHang.getSelectionModel().getSelectedIndex() > 0){
                thuocThem.setLoaiHang(new LoaiHang_Dao().selectByTenLoaiHang(cbxLoaiHang.getSelectionModel().getSelectedItem().toString()));
            }else{
                thuocThem.setLoaiHang(null);
            }
            if(cbxViTri.getSelectionModel().getSelectedIndex() > 0){
                thuocThem.setVitri(new KeHang_Dao().selectByTenKe(cbxViTri.getSelectionModel().getSelectedItem().toString()));
            }else{
                thuocThem.setVitri(null);
            }


//          Get image
            Image image = imgThuoc_SanPham.getImage();
            if (image != null) {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
//                  Định dạng ảnh png
                    ImageIO.write(bufferedImage, "png", baos);
                    baos.flush();

//                  Chuyển sang byte[]
                    byte[] imageBytes = baos.toByteArray();
                    baos.close();

//                  Gán vào đối tượng
                    thuocThem.setHinhAnh(imageBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//          Lấy root hiện tại
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();
            AnchorPane root = (AnchorPane) scene.getRoot();

//          Tạo overlay làm mờ nền
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
            ProgressIndicator progress = new ProgressIndicator();
            overlay.getChildren().add(progress);

//          Căn overlay phủ toàn màn hình
            AnchorPane.setTopAnchor(overlay, 0.0);
            AnchorPane.setRightAnchor(overlay, 0.0);
            AnchorPane.setBottomAnchor(overlay, 0.0);
            AnchorPane.setLeftAnchor(overlay, 0.0);

//          Thêm overlay vào AnchorPane
            root.getChildren().add(overlay);

//          Tạo luồng riêng để xử lý cập nhật (tránh lag UI)
            new Thread(() -> {
                Thuoc_SanPham_Dao thuoc_dao = new Thuoc_SanPham_Dao();
//              Thêm thuốc
                if (thuoc_dao.insert(thuocThem)) {
//              Thêm chi tiết hoạt chất
                    for (ChiTietHoatChat chtc : listChiTietHoatChat) {
                        chtc.setThuoc(thuocThem);
                        new ChiTietHoatChat_Dao().insert(chtc);
                    }
                }
                //Thêm một dòng của thuốc mới vào bảng Chi tiết đơn vị tính với đơn vị cơ bản
                if(cbDVTCB.getSelectionModel().getSelectedItem() != null){
                    DonViTinh dvt = new DonViTinh_Dao().selectByTenDVT(cbDVTCB.getSelectionModel().getSelectedItem().toString());
                    ChiTietDonViTinh ctdvt = new ChiTietDonViTinh();
                    ctdvt.setThuoc(thuocThem);
                    ctdvt.setDvt(dvt);
                    ctdvt.setHeSoQuyDoi(1.0);
                    ctdvt.setGiaNhap(0.0);
                    ctdvt.setGiaBan(0.0);
                    ctdvt.setDonViCoBan(true);
                    List<ChiTietDonViTinh> listCTDVT = new ArrayList<>();
                    listCTDVT.add(ctdvt);
                    new ChiTietDonViTinh_Dao().insert(ctdvt);
                }

                if(parentController != null) {
                    Platform.runLater(() -> parentController.refestTable());
                }
                // Quay lại luồng giao diện để loại bỏ overlay
                Platform.runLater(() -> {
                    root.getChildren().remove(overlay);
                    isThayDoi = true;
                    stage.close();
                });
            }).start();
        }
    }

    public void listView() {
//      Tạo listHoatChat và thêm các hoạt chất vào list
        List<HoatChat> listHoatChat = new HoatChat_Dao().selectAll();
        allHoatChat = FXCollections.observableArrayList(listHoatChat);
        listViewHoatChat.setItems(allHoatChat);
        listViewHoatChat.setCellFactory(data -> new ListCell<HoatChat>() {
            @Override
            protected void updateItem(HoatChat item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getMaHoatChat() + " - " + item.getTenHoatChat());
                }
            }
        });
    }

//  Lọc danh sách hoạt chất
    private void locDanhSachHoatChat(String newVal, String oldVal) {

//      Nếu Hoạt chất = null thì listViewHoatChat sẽ đc fill full
        if (newVal == null || newVal.isEmpty()) {
            Platform.runLater(() -> listViewHoatChat.setItems(allHoatChat));
            return;
        }

        String keyword = newVal.toLowerCase();
        ObservableList<HoatChat> danhSachHoatChatDaLoc = FXCollections.observableArrayList();

//      HoatChat trong txtTimKiem phù hợp thì sẽ hiện lên listviewHoatChat
        for (HoatChat hoatChat : allHoatChat) {
            if (hoatChat.getMaHoatChat().toLowerCase().contains(keyword)
                    || hoatChat.getTenHoatChat().toLowerCase().contains(keyword)) {
                danhSachHoatChatDaLoc.add(hoatChat);
            }
        }

//      setItems cho listviewHoatChat
        Platform.runLater(() -> {
            listViewHoatChat.setItems(danhSachHoatChatDaLoc.isEmpty()
                    ? FXCollections.observableArrayList()
                    : danhSachHoatChatDaLoc);
        });
    }

    public boolean kiemTraHopLe() {
        if(txtTenThuoc.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Tên thuốc không được để trống!");
            alert.showAndWait();
            return false;
        }else if(txtHamLuong.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Hàm lượng không được để trống!");
            alert.showAndWait();
            return false;
        }else if(!txtHamLuong.getText().matches("\\d+(\\.\\d+)?")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Hàm lượng không hợp lệ! Vui lòng nhập số.");
            alert.showAndWait();
            return false;
        }else if(txtDonViHamLuong.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Đơn vị hàm lượng không được để trống!");
            alert.showAndWait();
            return false;
        }else if(txtDuongDung.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Đường dùng không được để trống!");
            alert.showAndWait();
            return false;
        }else if(txtSDK_GPNK.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("SĐK/GPNK không được để trống!");
            alert.showAndWait();
            return false;
        } else if (txtHamLuong.getText().equals("/d+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Hàm lượng không hợp lệ! Vui lòng nhập số.");
            alert.showAndWait();
            return false;
        } else if (cbDVTCB.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn đơn vị tính cơ bản!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void setParent(DanhMucThuoc_Ctrl parent) {
        parentController = parent;
    }

    public Thuoc_SanPham getThuocThem() {
        return thuocThem;
    }
}
