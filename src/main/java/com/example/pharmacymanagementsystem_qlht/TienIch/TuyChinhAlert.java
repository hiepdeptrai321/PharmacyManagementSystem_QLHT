package com.example.pharmacymanagementsystem_qlht.TienIch;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TuyChinhAlert {
    public static void hien(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);

        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(
                TuyChinhAlert.class.getResource(
                        "/com/example/pharmacymanagementsystem_qlht/css/ThongBaoAlert.css"
                ).toExternalForm()
        );

        Stage stage = (Stage) pane.getScene().getWindow();

        switch (type) {
            case WARNING:
                pane.getStyleClass().add("warning-alert");
                stage.getIcons().add(new Image(
                        TuyChinhAlert.class.getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/iconcanhbao.jpg")));
                break;

            case INFORMATION:
                pane.getStyleClass().add("info-alert");
//                stage.getIcons().add(new Image(
//                        TuyChinhAlert.class.getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/)));
                break;

            case ERROR:
                pane.getStyleClass().add("error-alert");
//                stage.getIcons().add(new Image(
//                        TuyChinhAlert.class.getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/")));
                break;
        }

        stage.setWidth(550);
        stage.setHeight(260);
        alert.showAndWait();
    }


}
