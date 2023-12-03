package com.dam.colision.modelo;

public class CuerpoSnacke {
    private int x;
    private int y;

    public CuerpoSnacke() {
    }

    public CuerpoSnacke(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "CuerpoSnacke{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
