package laberinto;

import java.util.ArrayList;
import java.util.List;

public class Laberinto {
    private static final int[][] DIREC = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // Arriba, derecha, abajo, izquierda
    private final char[][] celdas;
    private final int filas;
    private final int columnas;
    private final Vertices vertices;
    private final int idEntrada;
    private final int idSalida;

    public Laberinto(List<String> lineas, int[] coordsES) {
        this.filas = lineas.size();
        this.columnas = lineas.get(0).length();
        this.celdas = new char[filas][columnas];
        this.vertices = new Vertices();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char c = lineas.get(i).charAt(j);
                if (ValidadorLaberinto.esTransitable(c)) {
                    this.celdas[i][j] = ' ';
                    int id = idDe(i, j);
                    this.vertices.agregar(new Vertice(id, i, j));
                } else {
                    this.celdas[i][j] = c;
                }
            }
        }
        this.idEntrada = idDe(coordsES[0], coordsES[1]);
        this.idSalida  = idDe(coordsES[2], coordsES[3]);
    }

    public int idDe(int fila, int col) {
        return fila * columnas + col;
    }

    public static Laberinto desdeLineas(List<String> lineas) throws ArchivoInvalidoException {
        int[] coordenadaES = ValidadorLaberinto.validar(lineas);
        return new Laberinto(lineas, coordenadaES);
    }

    public List<Vertice> vecinos(Vertice v) {
        List<Vertice> res = new ArrayList<>();
        for (int[] d : DIREC) {
            int f = v.getFila() + d[0];
            int c = v.getColumna() + d[1];
            if (esTransitable(f, c)) {
                res.add(vertices.obtener(idDe(f, c)));
            }
        }
        return res;
    }

    public List<Arcos> generarArcos() {
        List<Arcos> arcos = new ArrayList<>();
        for (Vertice v : vertices.todosOrdenados()) {
            for (int[] d : new int[][]{{0, 1}, {1, 0}}) { // Solo derecha y abajo
                int f = v.getFila() + d[0];
                int c = v.getColumna() + d[1];
                if (esTransitable(f, c)) {
                    arcos.add(new Arcos(v, vertices.obtener(idDe(f, c)), 1));
                }
            }
        }
        return arcos;
    }

    public char[][] getMatriz() { return celdas; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public Vertice getEntrada() { return vertices.obtener(idEntrada); }
    public Vertice getSalida() { return vertices.obtener(idSalida); }
    public char getChar(int f, int c) { return celdas[f][c]; }

    public boolean esTransitable(int f, int c) {
        return f >= 0 && f < filas && c >= 0 && c < columnas && celdas[f][c] == ' ';
    }
}