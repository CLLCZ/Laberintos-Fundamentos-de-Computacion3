package laberinto;

public class Laberinto {
    private final char[][] matriz;
    private final int filas;
    private final int columnas;
    private Vertice entrada;
    private Vertice salida;

    public Laberinto(char[][] matriz) {
        this.matriz = matriz;
        this.filas = matriz.length;
        this.columnas = matriz[0].length;
    }

    public char[][] getMatriz() { return matriz; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public Vertice getEntrada() { return entrada; }
    public Vertice getSalida() { return salida; }
    public char getChar(int f, int c) { return matriz[f][c]; }

    public boolean esTransitable(int f, int c) {
        throw new UnsupportedOperationException(" rol A");
    }
}