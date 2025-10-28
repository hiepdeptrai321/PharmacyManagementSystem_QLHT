package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapPhieuTra;

import com.example.pharmacymanagementsystem_qlht.controller.DangNhap_Ctrl;
import com.example.pharmacymanagementsystem_qlht.dao.*;
import com.example.pharmacymanagementsystem_qlht.model.*;
import com.example.pharmacymanagementsystem_qlht.service.TraHangItem;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.*;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;

public class LapPhieuTraHang_Ctrl extends Application {
    @FXML private TextField txtTimHoaDon;
    @FXML private Button btnTimHoaDon;
    @FXML private TableView<ChiTietHoaDon> tblSanPhamHoaDon;
    @FXML private  TableColumn<ChiTietHoaDon, Number> colSTTGoc;
    @FXML private TableColumn<ChiTietHoaDon, String> colTenSPGoc;
    @FXML private TableColumn<ChiTietHoaDon, Number> colSLGoc;
    @FXML private TableColumn<ChiTietHoaDon, Double> colDonGiaGoc;
    @FXML private TableColumn<ChiTietHoaDon, Double> colGiamGiaGoc;
    @FXML private TableColumn<ChiTietHoaDon, Double> colThanhTienGoc;


    @FXML private TextField lblMaHDGoc;
    @FXML private DatePicker dpNgayLapPhieu;
    @FXML private Label lblTongTienGoc;
    @FXML private Label lblTongTienTraLai;
    @FXML private Label lblVAT;
    @FXML private Label lblSoTienTraLai;
    @FXML private TextArea txtGhiChu;



    @FXML private TableView<TraHangItem> tblChiTietTraHang;
    @FXML private TableColumn<TraHangItem, Number> colSTTTra;
    @FXML private TableColumn<TraHangItem, String> colTenSPTra;
    @FXML private TableColumn<TraHangItem, Number> colSLTra;
    @FXML private TableColumn<TraHangItem, String> colLyDo;
    @FXML private TableColumn<TraHangItem, Double> colThanhTienTra;
    @FXML private TableColumn<TraHangItem, Void> colBo;

    private final ObservableList<ChiTietHoaDon> dsGoc = FXCollections.observableArrayList();
    private final ObservableList<TraHangItem> dsTra = FXCollections.observableArrayList();
    private final Map<String, TraHangItem> traByKey = new HashMap<>();

    // DAO
    private final HoaDon_Dao hoaDonDao = new HoaDon_Dao();
    private final ChiTietHoaDon_Dao cthdDao = new ChiTietHoaDon_Dao();
    private final Thuoc_SP_TheoLo_Dao loDao = new Thuoc_SP_TheoLo_Dao();
    private final PhieuTraHang_Dao phieuTraDao = new PhieuTraHang_Dao();
    private final ChiTietPhieuTraHang_Dao ctptDao = new ChiTietPhieuTraHang_Dao();
    @Override
    public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapPhieuTra/LapPhieuTraHang_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        dpNgayLapPhieu.setValue(LocalDate.now());
    }

    // ========== Nghiệp vụ ==========

    public void xuLyTimHDGoc(ActionEvent actionEvent) {
    }

    public void xuLyTraHangVaIn(ActionEvent actionEvent) {
    }

    public void xuLyTraHang(ActionEvent actionEvent) {
    }

    private void thongBaoTuyChinh(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(header);
        alert.setHeaderText(header);
        alert.setContentText(message);
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongBaoAlert.css"
        ).toExternalForm());
        Stage stage = (Stage) pane.getScene().getWindow();
        switch (type) {
            case WARNING:
                pane.getStyleClass().add("warning-alert");
                stage.getIcons().add(new Image(
                        getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/iconcanhbao.jpg")));
                break;
            case INFORMATION:
                pane.getStyleClass().add("info-alert");
                break;
            case ERROR:
                pane.getStyleClass().add("error-alert");
                break;
        }

        stage.setWidth(550);
        stage.setHeight(260);

        alert.showAndWait();
    }
}