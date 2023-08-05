module com.aptech.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires jbcrypt;


    opens com.aptech.project2.Controller to javafx.fxml;
    opens com.aptech.project2.Model to javafx.fxml;
    opens com.aptech.project2.DAO to javafx.fxml;
    exports com.aptech.project2.Controller;
    exports com.aptech.project2.Model;
    exports com.aptech.project2;
}