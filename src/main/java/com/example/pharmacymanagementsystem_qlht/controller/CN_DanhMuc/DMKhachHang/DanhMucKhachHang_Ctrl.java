package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang;

import com.example.pharmacymanagementsystem_qlht.dao.KhachHang_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KeHang;
import com.example.pharmacymanagementsystem_qlht.model.KhachHang;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class DanhMucKhachHang_Ctrl extends Application {
    @FXML
    private Button btnTim;

    @FXML
    private Button btnthemKH;

    @FXML
    private TableColumn<KhachHang, String> cotChiTiet;

    @FXML
    private TableColumn<KhachHang, String> cotGioiTinh;

    @FXML
    private TableColumn<KhachHang, String> cotMaKH;

    @FXML
    private TableColumn<KhachHang, String> cotNgaySinh;

    @FXML
    private TableColumn<KhachHang, String> cotSDT;

    @FXML
    private TableColumn<KhachHang, String> cotSTT;

    @FXML
    private TableColumn<KhachHang, String> cotTenKH;

    @FXML
    private TableView<KhachHang> tbKhachHang;

    private KhachHang_Dao khachHangDao = new KhachHang_Dao();
    @FXML
    private TextField txtTim;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhachHang/DanhMucKhachHang_GUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/QuanLyThuoc.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        loadTable();
    }
    public void loadTable() {
        List<KhachHang> list = khachHangDao.selectAll();
        ObservableList<KhachHang> data = FXCollections.observableArrayList(list);
        cotSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbKhachHang.getItems().indexOf(cellData.getValue()) + 1))
        );
       // cotMaKe.setCellValueFactory(new PropertyValueFactory<>("maKe"));
        //cotTenKe.setCellValueFactory(new PropertyValueFactory<>("tenKe"));
       // colChiTiet.setCellFactory(col -> new TableCell<KeHang, String>() {
            //private final Button btn = new Button("Chi tiết");
            //{
             //   btn.setOnAction(event -> {
                  //  KeHang kh = getTableView().getItems().get(getIndex());
                   // btnChiTietClick(kh);
              //  });
              //  btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
             //   btn.getStyleClass().add("btn");
            //}
            //@Override
            //protected void updateItem(String item, boolean empty) {
             //   super.updateItem(item, empty);
             //   setGraphic(empty ? null : btn);
        //    }
       // });
       // tblKeHang.setItems(data);

    }

}
