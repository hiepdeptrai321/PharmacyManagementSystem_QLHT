module com.example.pharmacymanagementsystem_qlht {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires java.desktop;
    opens com.example.pharmacymanagementsystem_qlht.controller to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.controller;
    opens com.example.pharmacymanagementsystem_qlht to javafx.fxml;

}