module com.example.chatr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.example.chatr to javafx.fxml;
    exports com.example.chatr;
    exports com.example.chatr.controllers;
    opens com.example.chatr.controllers to javafx.fxml;
}