package com.example.pharmacymanagementsystem_qlht.controller.CN_TimKiem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class TimKiemThuoc_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(TimKiemThuoc_Ctrl.class.getResource("/com/example/pharmacymanagementsystem_qlht/CN_TimKiem/TKThuoc/TKThuoc_GUI.fxml"));
            TimKiemThuoc_Ctrl timkiemThuocCtrl = new TimKiemThuoc_Ctrl();
            loader.setController(timkiemThuocCtrl);
            Pane root = loader.load();
            Scene scene = new Scene(root, 1200, 704);
            scene.getStylesheets().add(
                    getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/TimKiemThuoc.css").toExternalForm()
            );
            primaryStage.setTitle("Tim Kiem");
            primaryStage.setScene(scene);
            primaryStage.show();
    }
}
