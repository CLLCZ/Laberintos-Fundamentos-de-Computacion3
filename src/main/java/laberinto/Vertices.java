package laberinto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class Vertices {
    private final Hashtable<Integer, Vertice> tabla;

    public Vertices() {
        this.tabla = new Hashtable<>();
    }

    public void agregar(Vertice v) {
        if (v == null) {
            throw new IllegalArgumentException("El vértice que se busca agregar no puede ser nulo.");
        }
        tabla.put(v.getId(), v);
    }

    public Vertice obtener(int id) {
        return tabla.get(id);
    }

    public Vertice obtenerxCoordenada(int fila, int columna, int ancho) {
        return obtener(fila * ancho + columna);
    }

    public boolean contiene(int id) {
        return tabla.containsKey(id);
    }

    public int tamano() {
        return tabla.size();
    }

    public List<Vertice> todosOrdenados() {
        List<Vertice> lista = new ArrayList<>(tabla.values());
        lista.sort((v1, v2) -> Integer.compare(v1.getId(), v2.getId()));
        return lista;
    }

    public Hashtable<Integer, Vertice> getTabla() { return tabla; }
}