// java
package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.KhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SP_TangKem_Dao;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;

import java.util.List;

public class DanhMucKhuyenMai_Ctrl extends Application {

    @FXML public TableView<KhuyenMai> tbKM;
    public TextField tfTimKM;
    @FXML private Button btnthemKM;
    @FXML public TableColumn<KhuyenMai, String> colChiTiet;
    @FXML public TableColumn<KhuyenMai, String> colSTT;
    @FXML private TableColumn<KhuyenMai, String> colMaKM;
    @FXML private TableColumn<KhuyenMai, String> colTenKM;
    @FXML private TableColumn<KhuyenMai, String> colLoaiKM;
    @FXML private TableColumn<KhuyenMai, Float> colGiaTri;
    @FXML private TableColumn<KhuyenMai, java.sql.Date> colNBD;
    @FXML private TableColumn<KhuyenMai, java.sql.Date> colNKT;
    @FXML private TableColumn<KhuyenMai, java.sql.Date> colNgayTao;
    @FXML private Button btnLamMoi;

    private KhuyenMai_Dao khuyenMaiDao = new KhuyenMai_Dao();

    public void initialize() {
        tfTimKM.setOnAction(e -> timKhuyenMai());
        btnLamMoi.setOnAction(e-> LamMoi());
        Platform.runLater(()->{
            loadTable();
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/DanhMucKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loadTable() {
        List<KhuyenMai> list = khuyenMaiDao.selectAll();
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(list);

        colSTT.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(tbKM.getItems().indexOf(cellData.getValue()) + 1))
        );
        colMaKM.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        colTenKM.setCellValueFactory(new PropertyValueFactory<>("tenKM"));
        colTenKM.setCellFactory(col -> new TableCell<KhuyenMai, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
        colLoaiKM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLoaiKM().getMaLoai()));
        colNBD.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));
        colNKT.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));
        colNgayTao.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

        // Change to red "Xóa" button and hook delete handler
        colChiTiet.setCellFactory(col -> new TableCell<KhuyenMai, String>() {
            private final Button btn = new Button("Xóa");
            {
                btn.setOnAction(event -> {
                    KhuyenMai km = getTableView().getItems().get(getIndex());
                    btnXoaClick(km);
                });
                btn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                btn.getStyleClass().add("btn");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tbKM.setItems(data);
    }

    // Confirm and delete promotion (and related details/gifts if applicable)
    public void btnXoaClick(KhuyenMai km) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Xác nhận xóa khuyến mãi " + km.getMaKM() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Xác nhận xóa");
        confirm.showAndWait().ifPresent(res -> {
            if (res != ButtonType.YES) return;
            try {
                // \*Adjust these DAO calls to your actual API if method names differ\*
                new Thuoc_SP_TangKem_Dao().deleteByMaKM(km.getMaKM());
                new ChiTietKhuyenMai_Dao().deleteByMaKM(km.getMaKM());
                khuyenMaiDao.deleteByMaKM(km.getMaKM());

                loadTable();
                new Alert(Alert.AlertType.INFORMATION, "Đã xóa khuyến mãi.", ButtonType.OK).showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
            }
        });
    }

    public void btnThemKMClick() {
        try {
            Stage dialog = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMKhuyenMai/ThemKhuyenMai_GUI.fxml"));

            dialog.setOnHidden(e -> loadTable());

            dialog.initOwner(btnLamMoi.getScene().getWindow());
            dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Thêm khuyến mãi");
            dialog.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/logoNguyenBan.png")));
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void LamMoi() {
        tfTimKM.clear();
        loadTable();
    }

    public void timKhuyenMai() {
        String keyword = tfTimKM.getText().trim().toLowerCase();
        KhuyenMai_Dao km_dao = new KhuyenMai_Dao();
        List<KhuyenMai> dsKMLoc = km_dao.selectByTuKhoa(keyword);
        ObservableList<KhuyenMai> data = FXCollections.observableArrayList(dsKMLoc);
        tbKM.setItems(data);
    }
}
