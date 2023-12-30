module com.example.oussama {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.oussama to javafx.fxml;
    exports com.example.oussama;
}