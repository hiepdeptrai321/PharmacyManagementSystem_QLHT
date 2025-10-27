package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoatChat;
import com.example.pharmacymanagementsystem_qlht.model.HoatChat;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SuaXoaThuoc_Ctrl {
    public TextField txtTenThuoc;
    public ComboBox cbxLoaiHang;
    public ComboBox cbxViTri;
    public TextField txtHamLuong;
    public TextField txtHangSanXuat;
    public ComboBox cbxNhomDuocLy;
    public TextField txtNuocSanXuat;
    public TextField txtQuyCachDongGoi;
    public TextField txtSDK_GPNK;
    public TableView<ChiTietHoatChat> tblHoatChat;
    public TableColumn<ChiTietHoatChat,String> colMaHoatChat;
    public TableColumn<ChiTietHoatChat,String> colTenHoatChat;
    public TableColumn<ChiTietHoatChat,Float> colHamLuong;
    public TableColumn<ChiTietHoatChat,String> colXoa;
    public TextField txtDuongDung;
    public TextField txtDonViHamLuong;
    public ListView listViewHoatChat;
    public TextField txtTimKiemHoatChat;
    public TextField txtMaThuoc;
    public ImageView imgThuoc_SanPham;
    private ObservableList<HoatChat> allHoatChat;
    private List<ChiTietHoatChat> listChiTietHoatChat = new ArrayList<>();
    private Thuoc_SanPham thuocTempDeXemSoLuongTon;
    private Consumer<Thuoc_SanPham> onAdded;
    private Consumer<Thuoc_SanPham> onDeleted;
    private DanhMucThuoc_Ctrl danhMucThuoc_Ctrl;

    @FXML
    public void initialize(Thuoc_SanPham thuoc) {
        listChiTietHoatChat = new ChiTietHoatChat_Dao().selectAll();
        thuocTempDeXemSoLuongTon = thuoc;
        tblHoatChat.setEditable(true);
        colHamLuong.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colHamLuong.setOnEditCommit(event -> {
            ChiTietHoatChat hoatChatMoi = event.getRowValue();
            hoatChatMoi.setHamLuong(event.getNewValue());

            for (ChiTietHoatChat chtc : listChiTietHoatChat) {
                if (chtc.getThuoc() != null && hoatChatMoi.getThuoc() != null &&
                        chtc.getThuoc().getMaThuoc().equals(hoatChatMoi.getThuoc().getMaThuoc()) &&
                        chtc.getHoatChat().getMaHoatChat().equals(hoatChatMoi.getHoatChat().getMaHoatChat())) {

                    chtc.setHamLuong(hoatChatMoi.getHamLuong());
                    chtc.setThuoc(thuoc);
                    break;
                }
            }
        });
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
                if(tblHoatChat.getItems().stream().noneMatch(item -> item.getHoatChat().getMaHoatChat().equals(hoatChat.getMaHoatChat()))) {
                    ChiTietHoatChat chtc = new ChiTietHoatChat();
                    chtc.setHoatChat(hoatChat);
                    chtc.setThuoc(thuoc);
//                  Tạo dialog để nhập hàm lượng cho hoạt chất
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nhập hàm lượng");
                    dialog.setHeaderText("Vui lòng nhập hàm lượng cho hoạt chất: " + hoatChat.getTenHoatChat());
                    dialog.setContentText("Hàm lượng:");
                    dialog.showAndWait().ifPresent(hamLuong -> {
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

        cbxLoaiHang.getItems().addAll(new LoaiHang_Dao().getAllTenLH());
        cbxLoaiHang.getItems().addFirst("Chọn loại hàng");
        cbxViTri.getItems().addAll(new KeHang_Dao().getAllTenKe());
        cbxViTri.getItems().addFirst("Chọn vị trí");
        cbxNhomDuocLy.getItems().addAll(new NhomDuocLy_Dao().getAllTenNhomDuocLy());
        cbxNhomDuocLy.getItems().addFirst("Chọn nhóm dược lý");
        loadDuLieuThuoc(thuoc);
    }

    public void loadDuLieuThuoc(Thuoc_SanPham thuoc) {
        txtMaThuoc.setText(thuoc.getMaThuoc());
        txtTenThuoc.setText(thuoc.getTenThuoc());
        cbxLoaiHang.setValue(thuoc.getLoaiHang()!=null? thuoc.getLoaiHang().getTenLoaiHang() : cbxLoaiHang.getItems().get(0));
        cbxViTri.setValue(thuoc.getVitri()!=null? thuoc.getVitri().getTenKe() : cbxViTri.getItems().get(0));
        txtHamLuong.setText(String.valueOf(thuoc.getHamLuong()));
        txtHangSanXuat.setText(thuoc.getHangSX());
        txtDonViHamLuong.setText(thuoc.getDonViHamLuong());
        txtDuongDung.setText(thuoc.getDuongDung());
        if(thuoc.getNhomDuocLy() != null) {
            cbxNhomDuocLy.setValue(thuoc.getNhomDuocLy().getTenNDL());
        }else{
            cbxNhomDuocLy.getSelectionModel().selectFirst();
        }
        txtNuocSanXuat.setText(thuoc.getNuocSX());
        txtQuyCachDongGoi.setText(thuoc.getQuyCachDongGoi());
        txtSDK_GPNK.setText(thuoc.getSDK_GPNK());
        try {
            if (thuoc.getHinhAnh() == null) {
                imgThuoc_SanPham.setImage(
                        new Image(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/img/noimage.jpg").toExternalForm())
                );
            } else {
                imgThuoc_SanPham.setImage(new Image(new ByteArrayInputStream(thuoc.getHinhAnh())));
            }
        } catch (Exception e) {
            imgThuoc_SanPham.setImage(
                    new Image(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/img/noimage.jpg").toExternalForm())
            );
        }
        List<ChiTietHoatChat> listHoatChat = new ChiTietHoatChat_Dao().selectByMaThuoc(thuoc.getMaThuoc());
        ObservableList<ChiTietHoatChat> data = FXCollections.observableArrayList(listHoatChat);

        colMaHoatChat.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getHoatChat().getMaHoatChat()));
        colTenHoatChat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoatChat().getTenHoatChat()));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<ChiTietHoatChat,Float>("hamLuong"));
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
        tblHoatChat.setItems(data);
    }

//  Hiển thị danh sách hoạt chất
    public void listView() {
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
        if (newVal == null || newVal.isEmpty()) {
            Platform.runLater(() -> listViewHoatChat.setItems(allHoatChat));
            return;
        }

        String keyword = newVal.toLowerCase();
        ObservableList<HoatChat> danhSachHoatChatDaLoc = FXCollections.observableArrayList();

        for (HoatChat hoatChat : allHoatChat) {
            if (hoatChat.getMaHoatChat().toLowerCase().contains(keyword)
                    || hoatChat.getTenHoatChat().toLowerCase().contains(keyword)) {
                danhSachHoatChatDaLoc.add(hoatChat);
            }
        }

        Platform.runLater(() -> {
            listViewHoatChat.setItems(danhSachHoatChatDaLoc.isEmpty()
                    ? FXCollections.observableArrayList()
                    : danhSachHoatChatDaLoc);
        });
    }

    public void btnCapNhat(ActionEvent actionEvent) {
        // Lấy root hiện tại
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();

        // Tạo overlay làm mờ nền
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        ProgressIndicator progress = new ProgressIndicator();
        overlay.getChildren().add(progress);

        // Căn overlay phủ toàn màn hình
        AnchorPane.setTopAnchor(overlay, 0.0);
        AnchorPane.setRightAnchor(overlay, 0.0);
        AnchorPane.setBottomAnchor(overlay, 0.0);
        AnchorPane.setLeftAnchor(overlay, 0.0);

        // Thêm overlay vào AnchorPane
        root.getChildren().add(overlay);

        // Tạo luồng riêng để xử lý cập nhật (tránh lag UI)
        new Thread(() -> {
            try {
                // 👉 Code xử lý lâu (ví dụ: cập nhật CSDL)
                Thuoc_SanPham thuoc = new Thuoc_SanPham();
                thuoc.setMaThuoc(txtMaThuoc.getText());
                thuoc.setTenThuoc(txtTenThuoc.getText().trim());
                if(cbxLoaiHang.getSelectionModel().getSelectedIndex() == 0){
                    thuoc.setLoaiHang(null);
                }else{
                    thuoc.setLoaiHang(new LoaiHang_Dao().selectByTenLH(cbxLoaiHang.getSelectionModel().getSelectedItem().toString()));
                }
                if(cbxViTri.getSelectionModel().getSelectedIndex() == 0){
                    thuoc.setVitri(null);
                }else{
                    thuoc.setVitri(new KeHang_Dao().selectByTenKe(cbxViTri.getSelectionModel().getSelectedItem().toString()));
                }
                thuoc.setHamLuong(Float.parseFloat(txtHamLuong.getText().trim()));
                thuoc.setHangSX(txtHangSanXuat.getText().trim());
                thuoc.setDonViHamLuong(txtDonViHamLuong.getText().trim());
                thuoc.setDuongDung(txtDuongDung.getText().trim());
                if(cbxNhomDuocLy.getSelectionModel().getSelectedIndex() == 0){
                    thuoc.setNhomDuocLy(null);
                }else{
                    thuoc.setNhomDuocLy(new NhomDuocLy_Dao().selectByTenNhomDuocLy(cbxNhomDuocLy.getSelectionModel().getSelectedItem().toString()));
                }
                thuoc.setNuocSX(txtNuocSanXuat.getText().trim());
                thuoc.setQuyCachDongGoi(txtQuyCachDongGoi.getText().trim());
                thuoc.setSDK_GPNK(txtSDK_GPNK.getText().trim());
                // ... xử lý ảnh, listChiTietHoatChat, update, insert, v.v. ...
                Image image = imgThuoc_SanPham.getImage(); // lấy ảnh trong ImageView
                if (image != null) {
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bufferedImage, "png", baos); // định dạng ảnh png
                        baos.flush();
                        byte[] imageBytes = baos.toByteArray();    // chuyển sang byte[]
                        baos.close();
                        thuoc.setHinhAnh(imageBytes); // gán vào đối tượng
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Thuoc_SanPham_Dao thuoc_dao = new Thuoc_SanPham_Dao();
                thuoc_dao.update(thuoc);

                if(listChiTietHoatChat != null) {
                    ChiTietHoatChat_Dao chtc_dao = new ChiTietHoatChat_Dao();
                    List<ChiTietHoatChat> existingList = chtc_dao.selectByMaThuoc(thuoc.getMaThuoc());
                    Map<String, Float> existingMap = new HashMap<>();
                    for (ChiTietHoatChat c : existingList) {
                        existingMap.put(c.getHoatChat().getMaHoatChat(), c.getHamLuong());
                    }

                    for (ChiTietHoatChat chtc : listChiTietHoatChat) {
                        ChiTietHoatChat existing = chtc_dao.selectById(
                                chtc.getThuoc().getMaThuoc(),
                                chtc.getHoatChat().getMaHoatChat()
                        );

                        if (existing == null) {
                            chtc_dao.insert(chtc);
                        } else if (existing.getHamLuong() != chtc.getHamLuong()) {
                            chtc_dao.update(chtc);
                        }
                    }
                }
                // Sau khi xong, ẩn overlay (chạy lại trên JavaFX Thread)
                Platform.runLater(() -> {
                    root.getChildren().remove(overlay);
                    danhMucThuoc_Ctrl.refestTable();
                    dong();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> root.getChildren().remove(overlay));
            }
        }).start();
    }

    private void dong(){
        Stage stage = (Stage) txtMaThuoc.getScene().getWindow();
        stage.close();
    }

    public void btnHuy(ActionEvent actionEvent) {
        dong();
    }

    public void setParent(DanhMucThuoc_Ctrl parent) {
        danhMucThuoc_Ctrl = parent;
    }

    public void btnXoa(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xoá thuốc này?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                Thuoc_SanPham_Dao thuoc_dao = new Thuoc_SanPham_Dao();
                Thuoc_SP_TheoLo_Dao thuocSpTheoLoDao = new Thuoc_SP_TheoLo_Dao();
                if(thuocSpTheoLoDao.selectSoLuongTonByMaThuoc(thuocTempDeXemSoLuongTon.getMaThuoc())>0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Thuốc "+ thuocTempDeXemSoLuongTon.getTenThuoc() + " hiện đang có tồn kho, bạn có muốn xóa không");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            thuoc_dao.xoaThuoc_SanPham(thuocTempDeXemSoLuongTon.getMaThuoc());
                            danhMucThuoc_Ctrl.refestTable();
                            dong();
                        }
                    });
                }else{
                    thuoc_dao.xoaThuoc_SanPham(thuocTempDeXemSoLuongTon.getMaThuoc());
                    danhMucThuoc_Ctrl.refestTable();
                    dong();
                }
            }
        });
    }

    public void chonFile(ActionEvent actionEvent) {
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

    public boolean kiemTraHopLe(Thuoc_SanPham thuoc) {
        if(thuoc.getTenThuoc().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Tên thuốc không được để trống!");
            alert.showAndWait();
            return false;
        }else if(cbxLoaiHang.getSelectionModel().getSelectedIndex() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn loại hàng!");
            alert.showAndWait();
            return false;
        }else if(cbxViTri.getSelectionModel().getSelectedIndex() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn vị trí!");
            alert.showAndWait();
            return false;
        }else if(listChiTietHoatChat.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng thêm ít nhất một hoạt chất cho thuốc!");
            alert.showAndWait();
            return false;
        }else if(cbxNhomDuocLy.getSelectionModel().getSelectedIndex() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn nhóm dược lý!");
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
        }
        return true;
    }
}
