package laberinto;

import java.util.Hashtable;

public class Vertices {
    private final Hashtable<Integer, Vertice> tabla;

    public Vertices() {
        this.tabla = new Hashtable<>();
    }

    public void agregar(Vertice v) { throw new UnsupportedOperationException(" rol A"); }
    public Vertice obtener(int id) { throw new UnsupportedOperationException(" rol A"); }
    public boolean contiene(int id) { throw new UnsupportedOperationException(" rol A"); }
    public int tamano() { throw new UnsupportedOperationException(" rol A"); }
    public Hashtable<Integer, Vertice> getTabla() { return tabla; }
}