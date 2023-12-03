package com.dam.colision.modelo;

public class Comida {
    private int posX;
    private int posY;

    public Comida(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public String toString() {
        return "Comida{" +
                "posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}
