package laberinto;

import java.util.Objects;

public class Vertice {
    private final int id;
    private final int fila;
    private final int columna;

    public Vertice(int id, int fila, int columna) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
    }

    public int getId() { return id; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return id == vertice.id; // dos vertices son iguales si son el mismo id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "V(" + fila + "," + columna + ")"; // para que sea fácil de leer en consola
    }
}