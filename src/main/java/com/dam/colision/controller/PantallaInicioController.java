package com.dam.colision.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PantallaInicioController implements Initializable {
    @FXML
    void iniciarJuego(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PantallaInicioController.class.getResource("/com/dam/colision/tablero.fxml"));
        try {
            AnchorPane raiz = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Juego La Serpiente");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(raiz);
            stage.setScene(scene);
            TableroController controller = loader.getController();
            controller.setStage(stage);
            controller.setAplicacionPrincipal(this);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void instrucciones(ActionEvent event) {
        Alert informacionJuego = new Alert(Alert.AlertType.INFORMATION);
        informacionJuego.setTitle("Instrucciones para jugar a la serpiente");
        informacionJuego.setHeaderText("El objetivo del juego es controlar una serpiente que se mueve por un tablero y comer la mayor cantidad de comida posible sin chocar contra las paredes o su propio cuerpo.");
        informacionJuego.setContentText("Puedes controlar la dirección de la serpiente utilizando las teclas:\n" + "W: Mover hacia arriba\n" +
                "A: Mover hacia la izquierda\n" +
                "S: Mover hacia abajo\n" +
                "D: Mover hacia la derecha\n" + "o puede usar también\n" +
                "↑: Mover hacia arriba\n" +
                "←: Mover hacia la izquierda\n" +
                "↓: Mover hacia abajo\n" +
                "→: Mover hacia la derecha");
        informacionJuego.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
