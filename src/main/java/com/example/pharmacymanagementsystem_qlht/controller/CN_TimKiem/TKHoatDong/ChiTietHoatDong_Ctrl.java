package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKHoatDong;

import com.example.pharmacymanagementsystem_qlht.model.HoatDong;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ChiTietHoatDong_Ctrl implements Initializable {

    @FXML
    private TextField tfMaHD;
    @FXML private TextField tfLoaiHD;
    @FXML private TextField tfThoiGian;
    @FXML private TextField tfMaNV;
    @FXML private TextField tfTenNV;
    @FXML private TextField tfBang;
    @FXML private TextArea tfNoiDung;
    @FXML private Button btnHuy;

    private final SimpleDateFormat tsFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    private HoatDong currentHd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (btnHuy != null) {
            btnHuy.setOnAction(e -> {
                Window w = btnHuy.getScene() == null ? null : btnHuy.getScene().getWindow();
                if (w != null) w.hide();
            });
        }
    }

    // Allows external code to pass HoatDong; stores it and applies when UI is ready
    public void loadData(HoatDong hd) {
        this.currentHd = hd;
        if (tfMaHD == null) {
            // UI not initialized yet; data will be applied in start()
            return;
        }
        if (hd == null) return;

        tfMaHD.setText(nullSafe(hd.getMaHD()));
        tfLoaiHD.setText(nullSafe(hd.getLoaiHD()));

        Timestamp t = hd.getThoiGian();
        tfThoiGian.setText(t == null ? "" : tsFormat.format(t));

        tfBang.setText(nullSafe(hd.getBang()));
        tfNoiDung.setText(nullSafe(hd.getNoiDung()));

        Object nv = hd.getNhanVien();
        if (nv == null) {
            tfMaNV.setText("");
            tfTenNV.setText("");
            return;
        }

        String ma = tryInvokeStringMethod(nv, "getMaNV", "getMaNhanVien", "getId", "getMa");
        String ten = tryInvokeStringMethod(nv, "getHoTen", "getTen", "getName", "toString");
        if (ten == null || ten.isEmpty()) ten = nv.toString();

        tfMaNV.setText(nullSafe(ma));
        tfTenNV.setText(nullSafe(ten));
    }

    private String tryInvokeStringMethod(Object obj, String... methodCandidates) {
        for (String mName : methodCandidates) {
            try {
                Method m = obj.getClass().getMethod(mName);
                Object res = m.invoke(obj);
                if (res != null) return res.toString();
            } catch (NoSuchMethodException ignored) {
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

}