package com.example.pharmacymanagementsystem_qlht.controller.CN_DanhMuc.DMNhaCungCap;

import com.example.pharmacymanagementsystem_qlht.dao.NhaCungCap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.NhaCungCap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ThemNhaCungCap_Ctrl extends Application {

    public TextField txtTenNCC;
    public TextField txtDiaChi;
    public TextField txtSDT;
    public TextField txtEmail;
    public TextField txtGPKD_SDK;
    public TextField txtTenCongTy;
    public TextField txtMaSoThue;
    public TextArea txtGhiChu;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_DanhMuc/DMNCC/ThemNhaCungCap_GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void btnHuy(MouseEvent mouseEvent) {
        dong();
    }

    public void btnThemNCC(MouseEvent mouseEvent) {
        NhaCungCap ncc = new NhaCungCap();
        ncc.setTenNCC(txtTenNCC.getText());
        ncc.setDiaChi(txtDiaChi.getText());
        ncc.setSDT(txtSDT.getText());
        ncc.setEmail(txtEmail.getText());
        ncc.setGPKD(txtGPKD_SDK.getText());
        ncc.setTenCongTy(txtTenCongTy.getText());
        ncc.setMSThue(txtMaSoThue.getText());
        ncc.setGhiChu(txtGhiChu.getText());
        if(kiemTraHopLe(ncc)){
            NhaCungCap_Dao ncc_dao = new NhaCungCap_Dao();
            if(ncc_dao.insert(ncc)){
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Thành công");
                DialogPane dialogPane = new DialogPane();
                dialogPane.setContentText("Thêm nhà cung cấp thành công!");
                dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
                dialog.setDialogPane(dialogPane);
                dialog.showAndWait();
            }else{
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Lỗi");
                DialogPane dialogPane = new DialogPane();
                dialogPane.setContentText("Thêm nhà cung cấp thất bại!");
                dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
                dialog.setDialogPane(dialogPane);
                dialog.showAndWait();
            }
        }
        dong();
    }

    private void dong(){
        Stage stage = (Stage) txtTenNCC.getScene().getWindow();
        stage.close();
    }

    private boolean kiemTraHopLe(NhaCungCap ncc){
        if(ncc.getTenNCC().isEmpty()){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContentText("Tên nhà cung cấp không được để trống!");
            dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            return false;
        }else if(ncc.getDiaChi().isEmpty()){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContentText("Địa chỉ không được để trống!");
            dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            return false;
        }else if(ncc.getSDT().isEmpty()){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContentText("Số điện thoại không được để trống!");
            dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            return false;
        }
        if(!ncc.getSDT().matches("0\\d{9}")){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContentText("Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số.");
            dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            return false;
        }else if(!ncc.getEmail().isEmpty() && !ncc.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContentText("Email không hợp lệ!");
            dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            return false;
        }else if(!ncc.getMSThue().isEmpty() && !ncc.getMSThue().matches("\\d{10}")){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Lỗi");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContentText("Mã số thuế không hợp lệ! Mã số thuế phải gồm 10 chữ số.");
            dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            return false;
        }else{
            return true;
        }
    }
}
