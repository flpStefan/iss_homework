module iss.bug_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens iss.bug_application to javafx.fxml;
    opens iss.bug_application.domain to javafx.fxml;
    opens iss.bug_application.controller to javafx.fxml;
    exports iss.bug_application;
    exports iss.bug_application.domain;
    exports iss.bug_application.controller;
}