module com.cgvsu {
    requires javafx.controls;
    requires javafx.fxml;
    requires vecmath;
    requires java.desktop;


    opens com.cgvsu to javafx.fxml;
    exports com.cgvsu;
    exports com.cgvsu.log;
    opens com.cgvsu.log to javafx.fxml;
    exports com.cgvsu.controllers;
    opens com.cgvsu.controllers to javafx.fxml;
}