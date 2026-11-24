package laberinto;

import java.util.List;

public class Grafo {
    private final Vertices vertices;
    private final Vertice entrada;
    private final Vertice salida;
    private ResultadoBusqueda ultimoResultado;

    public Grafo(Laberinto laberinto) {
        throw new UnsupportedOperationException(" rol B: construir grafo");
    }

    public List<Vertice> DFS() { throw new UnsupportedOperationException(" rol B"); }
    public List<Vertice> BFS() { throw new UnsupportedOperationException(" rol B"); }
    public List<Vertice> caminoMasCorto() { throw new UnsupportedOperationException(" rol B"); }

    public ResultadoBusqueda getUltimoResultado() { return ultimoResultado; }
    public Vertices getVertices() { return vertices; }
    public Vertice getEntrada() { return entrada; }
    public Vertice getSalida() { return salida; }
}