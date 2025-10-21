package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;

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
    public TableColumn<ChiTietHoatChat,String> colMaHoatChat;
    public TableColumn<ChiTietHoatChat,String> colTenHoatChat;
    public TableColumn<ChiTietHoatChat,String> colHamLuong;
    public TableColumn<ChiTietHoatChat,String> colXoa;
    public TextField txtTimKiemHoatChat;
    public ListView listViewHoatChat;
    public TableView<ChiTietHoatChat> tblHoatChat;
    private ObservableList<HoatChat> allHoatChat;
    private List<ChiTietHoatChat> listChiTietHoatChat = new ArrayList<>();

    public void initialize() {
        cbxLoaiHang.getItems().addAll(new LoaiHang_Dao().getAllLoaiHang());
        cbxLoaiHang.getItems().addFirst("Chọn loại hàng");
        cbxLoaiHang.getSelectionModel().selectFirst();
        cbxViTri.getItems().addAll(new KeHang_Dao().getAllTenKe());
        cbxViTri.getItems().addFirst("Chọn vị trí");
        cbxViTri.getSelectionModel().selectFirst();
        cbxNhomDuocLy.getItems().addAll( new NhomDuocLy_Dao().getAllTenNhomDuocLy());
        cbxNhomDuocLy.getItems().addFirst("Chọn nhóm dược lý");
        cbxNhomDuocLy.getSelectionModel().selectFirst();

        loadBangThuoc();

        listChiTietHoatChat = new ChiTietHoatChat_Dao().selectAll();

//        tblHoatChat.setEditable(true);
//        colHamLuong.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
//        colHamLuong.setOnEditCommit(event -> {
//            ChiTietHoatChat hoatChatMoi = event.getRowValue();
//            hoatChatMoi.setHamLuong(event.getNewValue());
//
//            for(ChiTietHoatChat chtc : listChiTietHoatChat) {
//                if(chtc.getThuoc().getMaThuoc().equals(hoatChatMoi.getThuoc().getMaThuoc()) && chtc.getHoatChat().getMaHoatChat().equals(hoatChatMoi.getHoatChat().getMaHoatChat())) {
//                    chtc.setHamLuong(hoatChatMoi.getHamLuong());
//                    break;
//                }
//            }
//        });

        listViewHoatChat.setVisible(false);
        listView();
        txtTimKiemHoatChat.textProperty().addListener((obs, oldVal, newVal) -> {
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
                if(tblHoatChat.getItems().stream().noneMatch(item ->
                        item.getHoatChat().getMaHoatChat().equals(hoatChat.getMaHoatChat()))) {
                    ChiTietHoatChat chtc = new ChiTietHoatChat();
                    chtc.setHoatChat(hoatChat);
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nhập hàm lượng");
                    dialog.setHeaderText("Vui lòng nhập hàm lượng cho hoạt chất: " + hoatChat.getTenHoatChat());
                    dialog.setContentText("Hàm lượng:");
                    dialog.showAndWait().ifPresent(hamLuong -> {
                        chtc.setHamLuong(Float.parseFloat(hamLuong));
                        listChiTietHoatChat.add(chtc);
                        tblHoatChat.getItems().add(chtc);
                    });
                    Platform.runLater(() -> {
                        listViewHoatChat.getSelectionModel().clearSelection();
                        listViewHoatChat.refresh();
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Hoạt chất đã tồn tại trong danh sách!");
                    alert.showAndWait();
                    Platform.runLater(() -> {
                        listViewHoatChat.getSelectionModel().clearSelection();
                        listViewHoatChat.refresh();
                    });
                }
            }
        });

        colMaHoatChat.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getHoatChat().getMaHoatChat()
                ));

        colTenHoatChat.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getHoatChat().getTenHoatChat()
                ));

        colHamLuong.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getHamLuong())
                ));
    }

    public void loadBangThuoc(){
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

    public void btnHuyClick() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    public void chonFile(ActionEvent actionEvent) {
    }

    public void btnThemThuoc(ActionEvent actionEvent) {
        Thuoc_SanPham thuoc = new Thuoc_SanPham();
        thuoc.setMaThuoc(txtMaThuoc.getText());
        thuoc.setTenThuoc(txtTenThuoc.getText());
        thuoc.setHamLuong(Float.parseFloat(txtHamLuong.getText()));
        thuoc.setDonViHamLuong(txtDonViHamLuong.getText());
        thuoc.setHangSX(txtHangSanXuat.getText());
        thuoc.setNhomDuocLy(new NhomDuocLy_Dao().getNhomDuocLyByTen(cbxNhomDuocLy.getSelectionModel().getSelectedItem().toString()));
        thuoc.setNuocSX(txtNuocSanXuat.getText());
        thuoc.setQuyCachDongGoi(txtQuyCachDongGoi.getText());
        thuoc.setSDK_GPNK(txtSDK_GPNK.getText());
        thuoc.setDuongDung(txtDuongDung.getText());
        thuoc.setLoaiHang(new LoaiHang_Dao().getLoaiHangByTen(cbxLoaiHang.getSelectionModel().getSelectedItem().toString()));
        thuoc.setVitri(new KeHang_Dao().selectByTenKe(cbxViTri.getSelectionModel().getSelectedItem().toString()));
        Image image = imgThuoc_SanPham.getImage();
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
    }

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
}
