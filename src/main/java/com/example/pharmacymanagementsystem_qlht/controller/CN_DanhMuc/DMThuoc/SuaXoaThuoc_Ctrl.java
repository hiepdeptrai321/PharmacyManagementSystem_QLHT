package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoatChat;
import com.example.pharmacymanagementsystem_qlht.model.HoatChat;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    public void initialize(Thuoc_SanPham thuoc) {
        listChiTietHoatChat = new ChiTietHoatChat_Dao().selectAll();

        tblHoatChat.setEditable(true);
        colHamLuong.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colHamLuong.setOnEditCommit(event -> {
            ChiTietHoatChat hoatChatMoi = event.getRowValue();
            hoatChatMoi.setHamLuong(event.getNewValue());

            for(ChiTietHoatChat chtc : listChiTietHoatChat) {
                if(chtc.getThuoc().getMaThuoc().equals(hoatChatMoi.getThuoc().getMaThuoc()) && chtc.getHoatChat().getMaHoatChat().equals(hoatChatMoi.getHoatChat().getMaHoatChat())) {
                    chtc.setHamLuong(hoatChatMoi.getHamLuong());
                    break;
                }
            }
        });
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
                    chtc.setThuoc(thuoc);
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

        cbxLoaiHang.getItems().addAll(new LoaiHang_Dao().getAllTenLH());
        cbxViTri.getItems().addAll(new KeHang_Dao().getAllTenKe());
        cbxNhomDuocLy.getItems().addAll(new NhomDuocLy_Dao().getAllTenNhomDuocLy());
        loadDuLieuThuoc(thuoc);
    }

    public void loadDuLieuThuoc(Thuoc_SanPham thuoc) {
        txtMaThuoc.setText(thuoc.getMaThuoc());
        txtTenThuoc.setText(thuoc.getTenThuoc());
        cbxLoaiHang.setValue(thuoc.getLoaiHang().getTenLoaiHang());
        cbxViTri.setValue(thuoc.getVitri().getTenKe());
        txtHamLuong.setText(String.valueOf(thuoc.getHamLuong()));
        txtHangSanXuat.setText(thuoc.getHangSX());
        txtDonViHamLuong.setText(thuoc.getDonViHamLuong());
        txtDuongDung.setText(thuoc.getDuongDung());
        cbxNhomDuocLy.setValue(thuoc.getNhomDuocLy().getTenNDL());
        txtNuocSanXuat.setText(thuoc.getNuocSX());
        txtQuyCachDongGoi.setText(thuoc.getQuyCachDongGoi());
        txtSDK_GPNK.setText(thuoc.getSDK_GPNK());
        imgThuoc_SanPham.setImage(new Image(new ByteArrayInputStream(thuoc.getHinhAnh())));

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
        Thuoc_SanPham thuoc = new Thuoc_SanPham();
        thuoc.setMaThuoc(txtMaThuoc.getText());
        thuoc.setTenThuoc(txtTenThuoc.getText().trim());
        thuoc.setLoaiHang(new LoaiHang_Dao().selectByTenLH(cbxLoaiHang.getSelectionModel().getSelectedItem().toString()));
        thuoc.setVitri(new KeHang_Dao().selectByTenKe(cbxViTri.getSelectionModel().getSelectedItem().toString()));
        thuoc.setHamLuong(Float.parseFloat(txtHamLuong.getText().trim()));
        thuoc.setHangSX(txtHangSanXuat.getText().trim());
        thuoc.setDonViHamLuong(txtDonViHamLuong.getText().trim());
        thuoc.setDuongDung(txtDuongDung.getText().trim());
        thuoc.setNhomDuocLy(new NhomDuocLy_Dao().selectByTenNhomDuocLy(cbxNhomDuocLy.getSelectionModel().getSelectedItem().toString()));
        thuoc.setNuocSX(txtNuocSanXuat.getText().trim());
        thuoc.setQuyCachDongGoi(txtQuyCachDongGoi.getText().trim());
        thuoc.setSDK_GPNK(txtSDK_GPNK.getText().trim());
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
        dong();

        Thuoc_SanPham_Dao thuoc_dao = new Thuoc_SanPham_Dao();
        thuoc_dao.update(thuoc);

        if(listChiTietHoatChat != null) {
            ChiTietHoatChat_Dao chtc_dao = new ChiTietHoatChat_Dao();
            for (ChiTietHoatChat chtc : listChiTietHoatChat) {
                if(chtc_dao.selectById(chtc.getThuoc().getMaThuoc(), chtc.getHoatChat().getMaHoatChat()) == null) {
                    chtc_dao.insert(chtc);
                } else if (chtc.getHamLuong() != chtc_dao.selectById(chtc.getThuoc().getMaThuoc(), chtc.getHoatChat().getMaHoatChat()).getHamLuong()) {
                    chtc_dao.update(chtc);
                }
            }
        }
    }

    private void dong(){
        Stage stage = (Stage) txtMaThuoc.getScene().getWindow();
        stage.close();
    }

    public void btnHuy(ActionEvent actionEvent) {
        dong();
    }
}
