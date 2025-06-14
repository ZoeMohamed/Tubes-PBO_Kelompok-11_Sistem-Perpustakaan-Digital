module com.tubes {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;

    opens com.tubes to javafx.fxml;
    opens com.controller to javafx.fxml;
    opens com.Service to javafx.fxml;
    opens com.DAO to javafx.fxml;
    opens com.model to javafx.fxml;

    exports com.tubes;
    exports com.controller;
    exports com.Service;
    exports com.DAO;
    exports com.model;
}
