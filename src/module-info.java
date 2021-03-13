module GradingLogUI {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens controllers;
    opens model;
    opens database;

    //
}