package com.example.pharmacymanagementsystem_qlht.controller.CN_XuLy.LapHoaDon;

import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;


public class LapHoaDon_Ctrl extends Application {
    @FXML
    private Button btnTimKhachHang;
    @FXML
    private Button btnThemKH;
    @FXML
    private ChoiceBox<String> cbPhuongThucTT;
    @FXML
    private Pane paneTienMat;
    @FXML
    private TextField txtTimThuoc;

    // popup suggestions
    private final ContextMenu suggestionsPopup = new ContextMenu();
    private final PauseTransition pause = new PauseTransition(Duration.millis(250));
    private final Thuoc_SanPham_Dao thuocDao = new Thuoc_SanPham_Dao();


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_XuLy/LapHoaDon/LapHoaDon_GUI.fxml"));
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
        if (txtTimThuoc != null) {
            txtTimThuoc.textProperty().addListener((obs, oldText, newText) -> {
                pause.stop();
                pause.setOnFinished(e -> layDanhSachThuoc(newText));
                pause.playFromStart();
            });
            txtTimThuoc.focusedProperty().addListener((obs, was, isNow) -> {
                if (!isNow) suggestionsPopup.hide();
            });
        }
    }
    private void layDanhSachThuoc(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            Platform.runLater(suggestionsPopup::hide);
            return;
        }

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                return thuocDao.timTheoTen(keyword, 10);
            }
        };

        task.setOnSucceeded(evt -> {
            List<String> results = task.getValue();
            if (results == null || results.isEmpty()) {
                suggestionsPopup.hide();
                return;
            }
            suggestionsPopup.getItems().clear();
            for (String name : results) {
                MenuItem mi = new MenuItem(name);
                mi.setOnAction(ae -> {
                    txtTimThuoc.setText(name);
                    suggestionsPopup.hide();
                    xuLyChonThuoc(name);
                });
                suggestionsPopup.getItems().add(mi);
            }
            if (!suggestionsPopup.isShowing()) {
                suggestionsPopup.show(txtTimThuoc, Side.BOTTOM, 0, 0);
            }
        });

        task.setOnFailed(evt -> suggestionsPopup.hide());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    private void xuLyChonThuoc(String medicineName) {
        if (medicineName == null || medicineName.trim().isEmpty()) return;

        Task<List<?>> task = new Task<>() {
            @Override
            protected List<?> call() {
                return thuocDao.selectByTuKhoa(medicineName);
            }
        };

        task.setOnSucceeded(evt -> {
            List<?> list = task.getValue();
            Object sp = (list == null || list.isEmpty()) ? null : list.get(0);

            Platform.runLater(() -> {
                if (sp == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product not found: " + medicineName, ButtonType.OK);
                    if (txtTimThuoc != null && txtTimThuoc.getScene() != null) alert.initOwner(txtTimThuoc.getScene().getWindow());
                    alert.showAndWait();
                    return;
                }

                // store selected product for later use (e.g. when adding to invoice)
                if (txtTimThuoc != null) txtTimThuoc.setUserData(sp);

                // Try to call an addToInvoice method if it exists in this controller
                try {
                    java.lang.reflect.Method m = LapHoaDon_Ctrl.this.getClass().getDeclaredMethod("addToInvoice", sp.getClass());
                    m.setAccessible(true);
                    m.invoke(LapHoaDon_Ctrl.this, sp);
                } catch (NoSuchMethodException ex) {
                    // No addToInvoice method found: show confirmation and log selection
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Add selected product to invoice?\n" + sp.toString(), ButtonType.YES, ButtonType.NO);
                    if (txtTimThuoc != null && txtTimThuoc.getScene() != null) confirm.initOwner(txtTimThuoc.getScene().getWindow());
                    confirm.showAndWait().ifPresent(btn -> {
                        if (btn == ButtonType.YES) {
                            System.out.println("Selected product (no addToInvoice method): " + sp);
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        task.setOnFailed(evt -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to fetch product suggestions", ButtonType.OK);
                if (txtTimThuoc != null && txtTimThuoc.getScene() != null) alert.initOwner(txtTimThuoc.getScene().getWindow());
                alert.showAndWait();
            });
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
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
        // Link ma QR
        ImageView qrImage = new ImageView(new Image("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=DemoQRCode"));
        vbox.getChildren().addAll(label, qrImage);
        Scene scene = new Scene(vbox, 300, 300);
        qrStage.setScene(scene);
        qrStage.show();
    }

    @FXML
    private void xuLyTimKhachHang() {
        try {
            // Goi giap dien
            com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl ctrl = new com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKKhachHang.TimKiemKhachHang_Ctrl();
            Stage stage = new Stage();
            ctrl.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xuLyThemKH(ActionEvent actionEvent) {
        try {
            // Goi giap dien
            com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang.ThemKhachHang_Ctrl ctrl = new com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMKhachHang.ThemKhachHang_Ctrl();
            Stage stage = new Stage();
            ctrl.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
