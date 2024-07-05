module com.linkedin.clientapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires json;
    requires com.jfoenix;
    requires java.sql;
    requires fontawesomefx;

    opens com.linkedin.clientapp to javafx.fxml;
    exports com.linkedin.clientapp;
}