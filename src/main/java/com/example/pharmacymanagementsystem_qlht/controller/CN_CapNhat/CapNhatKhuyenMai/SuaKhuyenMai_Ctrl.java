package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatKhuyenMai;

import com.example.pharmacymanagementsystem_qlht.dao.ChiTietKhuyenMai_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietKhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.KhuyenMai;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SP_TangKem;
import com.example.pharmacymanagementsystem_qlht.model.Thuoc_SanPham;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SuaKhuyenMai_Ctrl extends Application {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)
    public Button btnHuy;
    public TableView<ChiTietKhuyenMai> tbDSThuoc;
    public TableColumn<ChiTietKhuyenMai,String> colMaThuoc;
    public TableColumn<ChiTietKhuyenMai,String> colTenThuoc;
    public TableColumn<ChiTietKhuyenMai,Integer> colSLAP;
    public TableColumn<ChiTietKhuyenMai,Integer> colSLTD;
    @FXML private Accordion accTangKem;
    @FXML private TitledPane tpTangKem;
    @FXML private TableView<Thuoc_SP_TangKem> tbTangKem;
    @FXML private TableColumn<Thuoc_SP_TangKem, String> colMaQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, String> colTenQua;
    @FXML private TableColumn<Thuoc_SP_TangKem, Integer> colSLTang;
    @FXML private TableColumn<Thuoc_SP_TangKem, Void> colXoaQua;
    @FXML private TextField tfTimQua;
    @FXML private ListView<Thuoc_SanPham> listViewQua;
    private final ObservableList<Thuoc_SanPham> allThuocForGifts = FXCollections.observableArrayList();
    private FilteredList<Thuoc_SanPham> filteredThuocForGifts;
    private final ObservableList<Thuoc_SP_TangKem> giftItems = FXCollections.observableArrayList();
    @FXML
    private ListView<?> listViewThuoc;
    @FXML
    private TextField tfTimThuoc;

    @FXML
    private TextField tfTenKM;
    @FXML
    private ComboBox<String> cbLoaiKM;
    @FXML
    private TextField tfGiaTri;
    @FXML
    private DatePicker dpTuNgay;
    @FXML
    private DatePicker dpDenNgay;
    @FXML
    private TextField tfMoTa;

    // 2. KHỞI TẠO (INITIALIZE)

    @FXML
    public void initialize() {
        listViewThuoc.setVisible(false);
        listViewThuoc.setManaged(false);
        tfTimThuoc.focusedProperty().addListener((obs, oldVal, newVal) -> {
            listViewThuoc.setVisible(newVal);
            listViewThuoc.setManaged(newVal);
        });

        // make table editable
        tbDSThuoc.setEditable(true);
        colSLAP.setEditable(true);
        colSLTD.setEditable(true);

        // keep value factories (you also set them in loadDatatbCTKM)
        colSLAP.setCellValueFactory(new PropertyValueFactory<>("slApDung"));
        colSLTD.setCellValueFactory(new PropertyValueFactory<>("slToiDa"));

        // install spinner cells
        installSpinnerColumn(
                colSLAP, 0, 1_000_000, 1,
                ChiTietKhuyenMai::getSlApDung,
                ChiTietKhuyenMai::setSlApDung
        );
        installSpinnerColumn(
                colSLTD, 0, 1_000_000, 1,
                ChiTietKhuyenMai::getSlToiDa,
                ChiTietKhuyenMai::setSlToiDa
        );

        accTangKem.setVisible(true);
        accTangKem.setManaged(true);
        accTangKem.setExpandedPane(tpTangKem);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_CapNhat/CapNhatKhuyenMai/SuaKhuyenMai_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN

    public void loadData(KhuyenMai km) {
        if (km == null) return;
        tfTenKM.setText(km.getTenKM());
        cbLoaiKM.setValue(km.getLoaiKM().getMaLoai());
        tfGiaTri.setText(String.valueOf(km.getGiaTriKM()));
        dpTuNgay.setValue(km.getNgayBatDau().toLocalDate());
        dpDenNgay.setValue(km.getNgayKetThuc().toLocalDate());
        tfMoTa.setText(km.getMoTa());
        ChiTietKhuyenMai_Dao ctkm_dao = new ChiTietKhuyenMai_Dao();
    }

    public void loadDatatbCTKM(List<ChiTietKhuyenMai> dsCTKM){
        ObservableList<ChiTietKhuyenMai> listCTKM = FXCollections.observableArrayList(dsCTKM);
        colMaThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThuoc().getMaThuoc()));
        colTenThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThuoc().getTenThuoc()));
        colSLAP.setCellValueFactory(new PropertyValueFactory<>("slApDung"));
        colSLTD.setCellValueFactory(new PropertyValueFactory<>("slToiDa"));
        tbDSThuoc.setItems(listCTKM);
    }

    public void btnHuyClick(){
        Stage stage = (Stage) tfTimThuoc.getScene().getWindow();
        stage.close();
    }

    public void btnLuuClick(){
        // TODO
    }

    private void installSpinnerColumn(
            TableColumn<ChiTietKhuyenMai, Integer> column,
            int min, int max, int step,
            Function<ChiTietKhuyenMai, Integer> getter,
            BiConsumer<ChiTietKhuyenMai, Integer> setter
    ) {
        column.setCellFactory(tc -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>();

            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                spinner.setEditable(true);
                spinner.setMaxWidth(Double.MAX_VALUE);
                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min, step));

                // commit when value changes by arrows
                spinner.valueProperty().addListener((obs, oldV, newV) -> {
                    int row = getIndex();
                    if (row >= 0 && row < getTableView().getItems().size()) {
                        ChiTietKhuyenMai item = getTableView().getItems().get(row);
                        if (item != null && newV != null) {
                            setter.accept(item, newV);
                        }
                    }
                });

                // commit typed text on focus loss
                spinner.focusedProperty().addListener((obs, was, is) -> {
                    if (!is) {
                        try {
                            Integer typed = Integer.valueOf(spinner.getEditor().getText());
                            SpinnerValueFactory.IntegerSpinnerValueFactory vf = (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
                            if (typed < vf.getMin()) typed = vf.getMin();
                            if (typed > vf.getMax()) typed = vf.getMax();
                            vf.setValue(typed);
                        } catch (NumberFormatException ex) {
                            // reset to current value
                            spinner.getEditor().setText(String.valueOf(spinner.getValue()));
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                ChiTietKhuyenMai item = getTableView().getItems().get(getIndex());
                int current = 0;
                if (item != null) {
                    Integer v = getter.apply(item);
                    current = v == null ? 0 : v;
                }
                SpinnerValueFactory.IntegerSpinnerValueFactory vf = (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
                vf.setMin(min);
                vf.setMax(max);
                vf.setAmountToStepBy(step);
                vf.setValue(current);
                setGraphic(spinner);
            }
        });
    }

    // 4. XỬ LÝ NGHIỆP VỤ
}
