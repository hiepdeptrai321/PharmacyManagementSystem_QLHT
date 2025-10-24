package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatSoLuong;

import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SP_TheoLo_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TheoLo;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class CapNhatSoLuongThuoc_Ctrl extends Application {
    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public TextField tfTimThuoc;
    public Button btnTimThuoc;
    public TableView<Thuoc_SP_TheoLo> tbThuoc;
    public TableColumn<Thuoc_SP_TheoLo, String> colSTT;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaThuoc;
    public TableColumn<Thuoc_SP_TheoLo, String> colTenThuoc;
    public TableColumn<Thuoc_SP_TheoLo, String> colDVT;
    public TableColumn<Thuoc_SP_TheoLo, Integer> colSLTon;
    public TableColumn<Thuoc_SP_TheoLo, String> colMaLo;
    public TableColumn<Thuoc_SP_TheoLo, String> colChiTiet;

    // 2. KHỞI TẠO (INITIALIZE)
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatSoLuong/CapNhatSoLuongThuoc_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        loadTable();
        tfTimThuoc.setOnAction(e-> timThuoc());
    }
    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN
    public void loadTable() {
        Thuoc_SP_TheoLo_Dao dao = new Thuoc_SP_TheoLo_Dao();
        List<Thuoc_SP_TheoLo> list = dao.selectAll();
        ObservableList<Thuoc_SP_TheoLo> data = FXCollections.observableArrayList(list);

        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbThuoc.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaThuoc.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getThuoc().getMaThuoc())
        );
        colTenThuoc.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getThuoc().getTenThuoc())
        );
        colDVT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(new Thuoc_SanPham_Dao().getTenDVTByMaThuoc(cellData.getValue().getThuoc().getMaThuoc())))
        );
        colSLTon.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        colMaLo.setCellValueFactory(new PropertyValueFactory<>("maLH"));

        colChiTiet.setCellFactory(col -> new TableCell<Thuoc_SP_TheoLo, String>() {
            private final Button btn = new Button("Sửa");
            {
                btn.setOnAction(event -> {
                    Thuoc_SP_TheoLo thuocLo = getTableView().getItems().get(getIndex());
                    showSuaSoLuongThuoc(thuocLo);
                });
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tbThuoc.setItems(data);
    }

    // 4. XỬ LÝ NGHIỆP VỤ
    public void timThuoc() {
        String keyword = tfTimThuoc.getText().trim().toLowerCase();
        Thuoc_SP_TheoLo_Dao dao = new Thuoc_SP_TheoLo_Dao();
        List<Thuoc_SP_TheoLo> list;
        if (keyword.isEmpty()) {
            list = dao.selectAll();
        } else {
            list = dao.selectByTuKhoa(keyword);
        }
        ObservableList<Thuoc_SP_TheoLo> data = FXCollections.observableArrayList(list);
        tbThuoc.setItems(data);
    }

    private void showSuaSoLuongThuoc(Thuoc_SP_TheoLo thuocLo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatSoLuong/SuaSoLuongThuoc_GUI.fxml"));
            Parent root = loader.load();
            SuaSoLuongThuoc_Ctrl controller = loader.getController();
            controller.setThuoc(thuocLo);
            Stage stage = new Stage();
            stage.setTitle("Cập nhật số lượng thuốc");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            tbThuoc.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
