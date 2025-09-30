module com.example.pharmacymanagementsystem_qlht {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.pharmacymanagementsystem_qlht to javafx.fxml;
    exports com.example.pharmacymanagementsystem_qlht.MyApp.controller;
}