package laberinto;

import java.util.List;

public class ResultadoBusqueda {
    private List<Vertice> visitados;
    private List<Vertice> camino;
    private boolean solucionEncontrada;
    private long tiempoNanos;

    public ResultadoBusqueda() {
        //  rol B
    }

    public List<Vertice> getVisitados() { return visitados; }
    public List<Vertice> getCamino() { return camino; }
    public boolean isSolucionEncontrada() { return solucionEncontrada; }
    public long getTiempoNanos() { return tiempoNanos; }
}