module com.dam.colision {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.dam.colision to javafx.fxml;
    exports com.dam.colision.controller;
    exports com.dam.colision.modelo;
    opens com.dam.colision.controller to javafx.fxml;
}