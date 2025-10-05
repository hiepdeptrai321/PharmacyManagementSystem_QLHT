package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem.TKPhieuNhap;

import com.example.pharmacymanagementsystem_qlht.dao.PhieuNhap_Dao;
import com.example.pharmacymanagementsystem_qlht.model.PhieuNhap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TimKiemPhieuNhap extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKPhieuNhapHang/TKPhieuNhapHang_GUI.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            loadPhieuNhap();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadPhieuNhap(){
        List<PhieuNhap> list = new ArrayList<>();
        list = new PhieuNhap_Dao().selectAll();
        for (PhieuNhap pn : list){

        }
    }
}
