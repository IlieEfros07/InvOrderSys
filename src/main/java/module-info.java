module com.app.invordersys {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires java.sql;


    opens com.app.invordersys to javafx.fxml;
    opens com.app.invordersys.controllers to javafx.fxml;
    exports com.app.invordersys.controllers;
    exports com.app.invordersys;
}