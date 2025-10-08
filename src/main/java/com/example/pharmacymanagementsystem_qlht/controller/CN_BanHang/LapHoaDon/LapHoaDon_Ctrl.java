package com.example.pharmacymanagementsystem_qlht.controller.CN_BanHang.LapHoaDon;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


public class LapHoaDon_Ctrl extends Application {
    @FXML
    private Button btnTimKhachHang;

    @FXML
    private ChoiceBox<String> cbPhuongThucTT;
    @FXML
    private Pane paneTienMat;

//    @FXML
//    private ToggleButton myToggleButton;

//    @FXML
//    public void initialize(URL location, ResourceBundle resources) {
//        myToggleButton.getStyleClass().add("toggle-switch");
//
//    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_BanHang/LapHoaDon/LapHoaDon_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        if (cbPhuongThucTT != null) {
            cbPhuongThucTT.getItems().clear();
            cbPhuongThucTT.getItems().addAll("Phương thức thanh toán", "Tiền mặt", "Chuyển khoản");
            cbPhuongThucTT.setValue("Phương thức thanh toán");
            updateTienMatFieldsVisibility("Phương thức thanh toán");
            cbPhuongThucTT.setOnShowing(event -> {
                cbPhuongThucTT.getItems().remove("Phương thức thanh toán");
            });
            cbPhuongThucTT.setOnHiding(event -> {
                if (!cbPhuongThucTT.getItems().contains("Phương thức thanh toán")) {
                    cbPhuongThucTT.getItems().add(0, "Phương thức thanh toán");
                }
            });
            cbPhuongThucTT.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                updateTienMatFieldsVisibility(newVal);
                if ("Chuyển khoản".equals(newVal)) {
                    hienThiQR();
                }
            });
        }
    }

    private void updateTienMatFieldsVisibility(String value) {
        if (paneTienMat != null) {
            paneTienMat.setVisible("Tiền mặt".equals(value));
        }
    }

    private void hienThiQR() {
        Stage qrStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label label = new Label("Quét mã QR dưới đây để thanh toán");
        // Sử dụng một mã QR online bất kỳ
        ImageView qrImage = new ImageView(new Image("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=DemoQRCode"));
        vbox.getChildren().addAll(label, qrImage);
        Scene scene = new Scene(vbox, 300, 300);
        qrStage.setScene(scene);
        qrStage.show();
    }

    @FXML
    private void handleTimKhachHang() {
        try {
            // Gọi giao diện tìm kiếm khách hàng
            com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl ctrl = new com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl();
            Stage stage = new Stage();
            ctrl.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}