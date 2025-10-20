package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMThuoc;

import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietHoatChat;
import com.example.pharmacymanagementsystem_qlht.model.HoatChat;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public TableColumn<ChiTietHoatChat,String> colHamLuong;
    public TableColumn<ChiTietHoatChat,String> colXoa;
    public TextField txtDuongDung;
    public TextField txtDonViHamLuong;
    public ListView listViewHoatChat;
    public TextField txtTimKiemHoatChat;

    @FXML
    public void initialize(Thuoc_SanPham thuoc) {
        listViewHoatChat.setVisible(false);
        listView();
        txtTimKiemHoatChat.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                listViewHoatChat.setVisible(true);
                filterList(newVal, oldVal);
            } else {
                listViewHoatChat.setVisible(false);
            }
        });
        String maHoatChat = listViewHoatChat.getSelectionModel().getSelectedItem() != null ? ((HoatChat) listViewHoatChat.getSelectionModel().getSelectedItem()).getMaHoatChat() : null;
        listViewHoatChat.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                HoatChat hoatChat = ((HoatChat) newVal);
                txtTimKiemHoatChat.clear();
                listViewHoatChat.setVisible(false);
                if(tblHoatChat.getItems().stream().noneMatch(item -> item.getHoatChat().getMaHoatChat().equals(hoatChat.getMaHoatChat()))) {
                    ChiTietHoatChat chtc = new ChiTietHoatChat();
                    chtc.setThuoc(thuoc);
                    chtc.setHoatChat(hoatChat);
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nhập hàm lượng");
                    dialog.setHeaderText("Vui lòng nhập hàm lượng cho hoạt chất: " + hoatChat.getTenHoatChat());
                    dialog.setContentText("Hàm lượng:");
                    dialog.showAndWait().ifPresent(hamLuong -> {
                        chtc.setHamLuong(Float.parseFloat(hamLuong));
                        new ChiTietHoatChat_Dao().insert(chtc);
                        tblHoatChat.getItems().add(chtc);
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Hoạt chất đã tồn tại trong danh sách!");
                    alert.showAndWait();
                    alert.getButtonTypes().setAll(ButtonType.OK);
                }
            }
        });

        cbxLoaiHang.getItems().addAll(new LoaiHang_Dao().getAllTenLH());
        cbxViTri.getItems().addAll(new KeHang_Dao().getAllTenKe());
        cbxNhomDuocLy.getItems().addAll(new NhomDuocLy_Dao().getAllTenNhomDuocLy());
        loadDuLieuThuoc(thuoc);
    }

    public void loadDuLieuThuoc(Thuoc_SanPham thuoc) {
        txtTenThuoc.setText(thuoc.getTenThuoc());
        cbxLoaiHang.setValue(thuoc.getLoaiHang().getTenLoaiHang());
        cbxViTri.setValue(thuoc.getVitri().getMaKe());
        txtHamLuong.setText(String.valueOf(thuoc.getHamLuong()));
        txtHangSanXuat.setText(thuoc.getHangSX());
        txtDonViHamLuong.setText(thuoc.getDonViHamLuong());
        txtDuongDung.setText(thuoc.getDuongDung());
        cbxNhomDuocLy.setValue(thuoc.getNhomDuocLy().getTenNDL());
        txtNuocSanXuat.setText(thuoc.getNuocSX());
        txtQuyCachDongGoi.setText(thuoc.getQuyCachDongGoi());
        txtSDK_GPNK.setText(thuoc.getSDK_GPNK());

        List<ChiTietHoatChat> listHoatChat = new ChiTietHoatChat_Dao().selectByMaThuoc(thuoc.getMaThuoc());
        ObservableList<ChiTietHoatChat> data = FXCollections.observableArrayList(listHoatChat);

        colMaHoatChat.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getHoatChat().getMaHoatChat()));
        colTenHoatChat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHoatChat().getTenHoatChat()));
        colHamLuong.setCellValueFactory(new PropertyValueFactory<ChiTietHoatChat,String>("hamLuong"));
        colXoa.setCellFactory(celldata -> new TableCell<ChiTietHoatChat,String>(){
            private final Button btn = new Button("Xóa");
            {
                btn.setOnAction(event -> {
                    ChiTietHoatChat chtc = getTableView().getItems().get(getIndex());
                    new ChiTietHoatChat_Dao().deleteById(chtc.getThuoc().getMaThuoc(), chtc.getHoatChat().getMaHoatChat());
                    getTableView().getItems().remove(chtc);
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

    public void listView(){
        List<HoatChat> listHoatChat = new HoatChat_Dao().selectAll();
        ObservableList<HoatChat> dataHoatChat = FXCollections.observableArrayList(listHoatChat);
        listViewHoatChat.setItems(dataHoatChat);
        listViewHoatChat.setCellFactory(data -> new ListCell<HoatChat>() {
            @Override
            protected void updateItem(HoatChat item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getMaHoatChat() +" - "+  item.getTenHoatChat());
                }
            }
        });
    }

    private void filterList(String newVal, String oldVal) {
        if (newVal == null || newVal.isEmpty()) {
            listView();
            return;
        } else if (oldVal != null && newVal.length() < oldVal.length()) {
            listView();
        }

        ObservableList<HoatChat> filteredList = FXCollections.observableArrayList();
        for (Object items : listViewHoatChat.getItems()) {
            HoatChat hoatChat = (HoatChat) items;
            if (items.toString().toLowerCase().contains(newVal.toLowerCase())
                    || hoatChat.getMaHoatChat().toLowerCase().contains(newVal.toLowerCase())
                    || hoatChat.getTenHoatChat().toLowerCase().contains(newVal.toLowerCase())) {
                filteredList.add(hoatChat);
            }
        }
        listViewHoatChat.setItems(filteredList);
    }
}
