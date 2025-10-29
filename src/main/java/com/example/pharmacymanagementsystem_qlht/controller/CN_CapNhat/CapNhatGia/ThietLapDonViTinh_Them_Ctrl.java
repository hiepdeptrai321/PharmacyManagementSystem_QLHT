package com.example.pharmacymanagementsystem_qlht.controller.CN_CapNhat.CapNhatGia;

import com.example.pharmacymanagementsystem_qlht.dao.DonViTinh_Dao;
import com.example.pharmacymanagementsystem_qlht.dao.Thuoc_SanPham_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ChiTietDonViTinh;
import com.example.pharmacymanagementsystem_qlht.model.DonViTinh;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class ThietLapDonViTinh_Them_Ctrl {

    // 1. KHAI BÁO THÀNH PHẦN GIAO DIỆN (FXML)

    public CheckBox checkDVCB;
    public TextField tfHeSo;
    public TextField tfGiaBan;
    public TextField tfGiaNhap;
    public Button btnThemDVT;
    public ComboBox cbDVT;
    public Button btnThem, btnHuy;
    private String maThuoc;
    private Consumer<ChiTietDonViTinh> onAdded;
    ChiTietDonViTinh ctdvt = new ChiTietDonViTinh();

    // 2. KHỞI TẠO (INITIALIZE)

    public void initialize(){
        loadCbDVT();
        cbDVCBCheck();
    }

    // 3. XỬ LÝ SỰ KIỆN GIAO DIỆN

    public void setCtdvt(ChiTietDonViTinh ctdvt) {
        this.ctdvt = ctdvt;
        fillFormFromModel();
    }

    public void loadCbDVT(){
        DonViTinh_Dao donViTinh_dao = new DonViTinh_Dao();
        List<DonViTinh> list = donViTinh_dao.selectAll();
        for(DonViTinh donViTinh : list){
            cbDVT.getItems().add(donViTinh.getTenDonViTinh());
        }
    }

    public void btnThemDVTClick(MouseEvent mouseEvent) {
    }
    public void btnHuyClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    public void btnThemClick(MouseEvent mouseEvent) {
        if (!kiemTraData()) return;

        DonViTinh_Dao donViTinh_dao = new DonViTinh_Dao();
        DonViTinh dvt = donViTinh_dao.selectByTenDVT(cbDVT.getSelectionModel().getSelectedItem().toString());

        Float heSo = parseNumberVN(tfHeSo.getText().trim());
        Float giaNhap = parseNumberVN(tfGiaNhap.getText().trim());
        Float giaBan = parseNumberVN(tfGiaBan.getText().trim());

        ctdvt.setDvt(dvt);
        ctdvt.setHeSoQuyDoi(heSo);
        ctdvt.setGiaNhap(giaNhap);
        ctdvt.setGiaBan(giaBan);
        ctdvt.setDonViCoBan(checkDVCB.isSelected());
        ctdvt.setThuoc(new Thuoc_SanPham_Dao().selectById(maThuoc));

        //Thông báo cho SuaGiaThuoc để thêm vào bảng
        if (onAdded != null) onAdded.accept(ctdvt);

        Stage stage = (Stage) btnThem.getScene().getWindow();
        stage.close();

    }


    //2 tham số cho luồng thêm mới
    public void setContext(String maThuoc, Consumer<ChiTietDonViTinh> onAdded) {
        this.maThuoc = maThuoc;
        this.onAdded = onAdded;
    }


    private void fillFormFromModel() {
        if (ctdvt == null) return;
        if (cbDVT != null && ctdvt.getDvt() != null) {
            cbDVT.getSelectionModel().select(ctdvt.getDvt().getTenDonViTinh());
        }
        if (tfHeSo != null) tfHeSo.setText(String.valueOf(ctdvt.getHeSoQuyDoi()));
        if (tfGiaNhap != null) tfGiaNhap.setText(String.valueOf(ctdvt.getGiaNhap()));
        if (tfGiaBan != null) tfGiaBan.setText(String.valueOf(ctdvt.getGiaBan()));
        if (checkDVCB != null) checkDVCB.setSelected(ctdvt.isDonViCoBan());
        cbDVCBCheck();
    }

    // --- Helpers ---

    private void clearErrorStyles() {
        resetStyle(cbDVT);
        resetStyle(tfGiaNhap);
        resetStyle(tfGiaBan);
        resetStyle(tfHeSo);
    }

    private void addErrorStyle(Control control) {
        // Giữ style hiện tại và thêm viền đỏ
        String base = control.getStyle() == null ? "" : control.getStyle();
        control.setStyle(base + "; -fx-border-color: #d32f2f; -fx-border-width: 1; -fx-border-radius: 4;");
    }

    private void resetStyle(Control control) {
        control.setStyle("");
    }

    // Use Float for validation to keep everything in float domain
    public boolean kiemTraData() {
        clearErrorStyles();
        StringBuilder sb = new StringBuilder();

        if (cbDVT.getSelectionModel().getSelectedIndex() < 0) {
            sb.append("Vui lòng chọn đơn vị tính.\n");
            addErrorStyle(cbDVT);
        }

        // Giá nhập
        String giaNhapText = tfGiaNhap.getText() == null ? "" : tfGiaNhap.getText().trim();
        Float giaNhap = null;
        if (giaNhapText.isEmpty()) {
            sb.append("Giá nhập không được để trống.\n");
            addErrorStyle(tfGiaNhap);
        } else {
            giaNhap = parseNumberVN(giaNhapText);
            if (giaNhap == null) {
                sb.append("Giá nhập phải là số hợp lệ.\n");
                addErrorStyle(tfGiaNhap);
            } else if (giaNhap.floatValue() < 0f) {
                sb.append("Giá nhập phải >= 0.\n");
                addErrorStyle(tfGiaNhap);
            }
        }

        // Giá bán
        String giaBanText = tfGiaBan.getText() == null ? "" : tfGiaBan.getText().trim();
        Float giaBan = null;
        if (giaBanText.isEmpty()) {
            sb.append("Giá bán không được để trống.\n");
            addErrorStyle(tfGiaBan);
        } else {
            giaBan = parseNumberVN(giaBanText);
            if (giaBan == null) {
                sb.append("Giá bán phải là số hợp lệ.\n");
                addErrorStyle(tfGiaBan);
            } else if (giaBan.floatValue() < 0f) {
                sb.append("Giá bán phải >= 0.\n");
                addErrorStyle(tfGiaBan);
            }
        }

        // So sánh giá bán với giá nhập
        if (giaNhap != null && giaBan != null && giaBan.floatValue() < giaNhap.floatValue()) {
            sb.append("Giá bán phải lớn hơn hoặc bằng giá nhập.\n");
            addErrorStyle(tfGiaBan);
        }

        // Hệ số quy đổi
        String heSoText = tfHeSo.getText() == null ? "" : tfHeSo.getText().trim();
        Float heSo = null;
        if (heSoText.isEmpty()) {
            sb.append("Hệ số quy đổi không được để trống.\n");
            addErrorStyle(tfHeSo);
        } else {
            heSo = parseNumberVN(heSoText);
            if (heSo == null) {
                sb.append("Hệ số quy đổi phải là số hợp lệ.\n");
                addErrorStyle(tfHeSo);
            } else if (heSo.floatValue() <= 0f) {
                sb.append("Hệ số quy đổi phải > 0.\n");
                addErrorStyle(tfHeSo);
            }
        }

        // Nếu là đơn vị cơ bản thì hệ số = 1
        if (checkDVCB.isSelected() && heSo != null && Math.abs(heSo.floatValue() - 1f) > 1e-6f) {
            sb.append("Đơn vị cơ bản phải có hệ số quy đổi = 1.\n");
            addErrorStyle(tfHeSo);
        }

        if (sb.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dữ liệu không hợp lệ");
            alert.setHeaderText("Vui lòng kiểm tra lại các trường sau:");
            alert.setContentText(sb.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    // Parse to Float instead of Double
    private Float parseNumberVN(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;

        // remove internal spaces
        s = s.replaceAll("\\s+", "");

        // handle fraction forms "num/den" or "num:den"
        if (s.contains("/") || s.contains(":")) {
            String delim = s.contains("/") ? "/" : ":";
            String[] parts = s.split(java.util.regex.Pattern.quote(delim), 2);
            if (parts.length != 2) return null;
            String a = parts[0].replace(",", ".").trim();
            String b = parts[1].replace(",", ".").trim();
            try {
                double num = Double.parseDouble(a);
                double den = Double.parseDouble(b);
                if (den == 0.0) return null;
                return (float) (num / den);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        // fallback: handle thousand separators and decimal comma
        if (s.contains(",") && s.contains(".")) {
            s = s.replace(".", "").replace(",", ".");
        } else {
            s = s.replace(",", ".");
        }
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void cbDVCBCheck(){
        if(checkDVCB.isSelected()){
            tfHeSo.setText("1");
            tfHeSo.setDisable(true);
        } else {
            tfHeSo.setDisable(false);
        }
    }
}
