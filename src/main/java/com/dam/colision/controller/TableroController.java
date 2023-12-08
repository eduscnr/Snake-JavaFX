package com.dam.colision.controller;

import com.dam.colision.modelo.Comida;
import com.dam.colision.modelo.CuadradoSerpiente;
import com.dam.colision.modelo.CuerpoSnacke;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableroController implements Initializable {
    private Comida comida;
    @FXML
    private Label lblDireccionActual;
    private ArrayList<CuerpoSnacke> snake = new ArrayList<>();
    @FXML
    private GridPane tableView;
    @FXML
    private Label lbStatus;
    private int puntos = 0;
    private final int DIMENSION = 26;
    private String direccionActual = "DERECHA";
    private boolean gameOver = false;
    private AnimationTimer gameLoop;
    private long lastUpdateTime = 0;
    private long timeBetweenUpdates = 300_000_000;
    private Stage stage;
    private PantallaInicioController aplicacionPrincipal;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(windowEvent -> {
            // Detener la animación y cerrar la aplicación
            gameLoop.stop();
            stage.close();
        });
    }

    public void setAplicacionPrincipal(PantallaInicioController aplicacionPrincipal) {
        this.aplicacionPrincipal = aplicacionPrincipal;
    }

    public void dibujarSerpiente() {
        tableView.getChildren().removeIf(node -> node instanceof Rectangle);

        for (int i = 0; i < snake.size(); i++) {
            CuerpoSnacke cuerpo = snake.get(i);
            int x = cuerpo.getX();
            int y = cuerpo.getY();

            // Usar un cuadrado en lugar de un rectángulo
            CuadradoSerpiente cell = new CuadradoSerpiente(tableView.getWidth() / DIMENSION);

            // Asignar un color diferente a la cabeza de la serpiente
            if (i == 0) {
                cell.setFill(Color.BLUE); // Cambia el color a tu elección
            } else {
                cell.setFill(Color.GREEN);
            }

            tableView.add(cell, x, y);
        }
    }

    public void dibujarComida() {
        tableView.getChildren().removeIf(node -> node instanceof ImageView);

        int x = comida.getPosX();
        int y = comida.getPosY();

        // Carga la imagen de la manzana
        Image manzanaImage = new Image(getClass().getResource("/com/dam/colision/icons8-manzana-64.png").toExternalForm());

        // Crea un ImageView con la imagen de la manzana
        ImageView manzanaImageView = new ImageView(manzanaImage);

        // Ajusta el tamaño del ImageView según sea necesario
        double escala = 1.2;
        manzanaImageView.setFitWidth(Math.min(tableView.getWidth(), tableView.getHeight()) / DIMENSION * escala);
        manzanaImageView.setFitHeight(Math.min(tableView.getWidth(), tableView.getHeight()) / DIMENSION * escala);

        tableView.add(manzanaImageView, x, y);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbStatus.setText(String.valueOf(puntos));
        int stat_x = 3;
        int stat_y = 3;
        snake.add(new CuerpoSnacke(stat_x, stat_y));
        comida = generarNuevaComida();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdateTime >= timeBetweenUpdates) {
                    moveSnake();
                    lastUpdateTime = now;
                }
            }
        };
        tableView.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            clickBotones(code);
        });
        tableView.setFocusTraversable(true);
        tableView.requestFocus();
        gameLoop.start();
    }

    @FXML
    void reiniciarJuego(ActionEvent event) {
        resetGame();
    }



    public void moveSnake() {
        // Obtener la cabeza de la serpiente
        CuerpoSnacke head = snake.get(0);

        // Determinar la nueva posición de la cabeza en función de la dirección actual
        int newHeadX = head.getX();
        int newHeadY = head.getY();

        switch (direccionActual) {
            case "DERECHA":
                newHeadX = head.getX() + 1;
                break;
            case "IZQUIERDA":
                newHeadX = head.getX() - 1;
                break;
            case "ARRIBA":
                newHeadY = head.getY() - 1;
                break;
            case "ABAJO":
                newHeadY = head.getY() + 1;
                break;
        }
        lblDireccionActual.setText(direccionActual);
        // Verificar si la nueva posición está dentro de los límites del tablero
        if (isValidPosition(newHeadX, newHeadY)) {
            // Verificar si la nueva posición coincide con la posición de la cola
            for (int i = 1; i < snake.size(); i++) {
                CuerpoSnacke cuerpo = snake.get(i);
                if (newHeadX == cuerpo.getX() && newHeadY == cuerpo.getY()) {
                    // La cabeza ha chocado con la cola, juego over
                    Alert fin = new Alert(Alert.AlertType.WARNING);
                    fin.setTitle("Game Over");
                    fin.setContentText("Game Over - La cabeza chocó con la cola");
                    gameOver = true;
                    gameLoop.stop();
                    fin.show();
                    return;
                }
            }

            // Crear un nuevo cuerpo en la nueva posición
            CuerpoSnacke newHead = new CuerpoSnacke(newHeadX, newHeadY);

            // Agregar la nueva cabeza al frente de la serpiente
            snake.add(0, newHead);

            // Verificar si la cabeza de la serpiente alcanza la posición de la comida
            if (newHeadX == comida.getPosX() && newHeadY ==comida.getPosY()) {
                // La serpiente ha alcanzado la comida, genera una nueva posición para la comida
                puntos++;
                lbStatus.setText(String.valueOf(puntos));
                comida = generarNuevaComida();
            } else {
                // Eliminar la cola de la serpiente
                if (snake.size() > 1) {
                    snake.remove(snake.size() - 1);
                    //System.out.println(tail);
                }
            }

            // Actualizar el tablero
            // Llamar a dibujarSerpiente y dibujarComida para actualizar la interfaz de usuario
            dibujarSerpiente();
            dibujarComida();

        } else {
            // La nueva posición está fuera de los límites, juego over o manejar como desees
            Alert fin = new Alert(Alert.AlertType.WARNING);
            fin.setTitle("Game Over");
            fin.setContentText("Game Over - La cabeza salió de los límites");
            gameOver = true;
            gameLoop.stop();
            fin.show();
        }
    }

    public void clickBotones(KeyCode code) {
        if (!gameOver) {
            CuerpoSnacke cabeza = snake.get(0);
            int nuevaPosX = cabeza.getX();
            int nuevaPosY = cabeza.getY();

            // Guardar la dirección actual antes de cambiarla
            String direccionAnterior = direccionActual;

            switch (code) {
                case UP:
                case W:
                    if (!getDireccionActual().equalsIgnoreCase("ABAJO") && !getDireccionActual().equalsIgnoreCase("ARRIBA")) {
                        setDireccionActual("ARRIBA");
                        nuevaPosY = cabeza.getY() - 1;
                    }
                    break;
                case DOWN:
                case S:
                    if (!getDireccionActual().equalsIgnoreCase("ARRIBA") && !getDireccionActual().equalsIgnoreCase("ABAJO")) {
                        setDireccionActual("ABAJO");
                        nuevaPosY = cabeza.getY() + 1;
                    }
                    break;
                case LEFT:
                case A:
                    if (!getDireccionActual().equalsIgnoreCase("DERECHA") && !getDireccionActual().equalsIgnoreCase("IZQUIERDA")) {
                        setDireccionActual("IZQUIERDA");
                        nuevaPosX = cabeza.getX() - 1;
                    }
                    break;
                case RIGHT:
                case D:
                    if (!getDireccionActual().equalsIgnoreCase("IZQUIERDA") && !getDireccionActual().equalsIgnoreCase("DERECHA")) {
                        setDireccionActual("DERECHA");
                        nuevaPosX = cabeza.getX() + 1;
                    }
                    break;
            }

            // Solo agregar nueva cabeza y eliminar cola si la posición es diferente
            if (!getDireccionActual().equalsIgnoreCase(direccionAnterior) && isValidPosition(nuevaPosX, nuevaPosY)) {
                CuerpoSnacke nuevaCabeza = new CuerpoSnacke(nuevaPosX, nuevaPosY);
                snake.add(0, nuevaCabeza);
                if (nuevaPosX == comida.getPosX() && nuevaPosY == comida.getPosY()) {
                    // La serpiente ha alcanzado la comida, genera una nueva posición para la comida
                    comida = generarNuevaComida();
                    puntos++;
                    lbStatus.setText(String.valueOf(puntos));
                } else {
                    // Solo eliminar la cola si no ha alcanzado la comida
                    if (snake.size() > 1) {
                        snake.remove(snake.size() - 1);
                    }
                }

                // Actualizar el tablero
                dibujarSerpiente();
                dibujarComida();
            }
        }
    }
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < DIMENSION && y >= 0 && y < DIMENSION;
    }
    private Comida generarNuevaComida() {
        int comidaX, comidaY;

        do {
            // Generar nuevas coordenadas hasta encontrar una posición no ocupada por la serpiente
            comidaX = (int) (Math.random() * DIMENSION);
            comidaY = (int) (Math.random() * DIMENSION);
        } while (posicionOcupadaPorSerpiente(comidaX, comidaY));

        return new Comida(comidaX, comidaY);
    }
    private boolean posicionOcupadaPorSerpiente(int x, int y) {
        for (CuerpoSnacke cuerpo : snake) {
            if (cuerpo.getX() == x && cuerpo.getY() == y) {
                return true;
            }
        }
        return false;
    }
    public String getDireccionActual() {
        return direccionActual;
    }

    public void setDireccionActual(String direccionActual) {
        this.direccionActual = direccionActual;
    }
    public void resetGame() {
        // Limpiar la serpiente y la comida
        snake.clear();
        comida = generarNuevaComida();
        lbStatus.setText(String.valueOf(0));
        puntos = 0;

        // Inicializar la serpiente con una nueva posición
        int stat_x = 3;
        int stat_y = 3;
        snake.add(new CuerpoSnacke(stat_x, stat_y));

        // Reiniciar dirección y estado del juego
        direccionActual = "DERECHA";
        gameOver = false;
        tableView.setFocusTraversable(true);
        tableView.requestFocus();

        // Detener y reiniciar la animación
        gameLoop.stop();
        gameLoop.start();
    }
}
