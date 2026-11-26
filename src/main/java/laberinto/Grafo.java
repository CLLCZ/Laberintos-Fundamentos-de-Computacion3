package laberinto;

import java.util.*;

public class Grafo {
    private final Laberinto laberinto;
    private final Vertices vertices;
    private final Vertice entrada;
    private final Vertice salida;

    // listas de ADYACENCIA
    private final Map<Integer, List<Arcos>> adyacencia;

    private ResultadoBusqueda ultimoResultado;

    public Grafo(Laberinto laberinto) {
        this.laberinto = laberinto;
        this.vertices = new Vertices();
        this.adyacencia = new HashMap<>();
        this.entrada = laberinto.getEntrada();
        this.salida = laberinto.getSalida();

        construirGrafo();
    }

    private void construirGrafo() {
        // 1. crear todos los vértices transitables
        for (int f = 0; f < laberinto.getFilas(); f++) {
            for (int c = 0; c < laberinto.getColumnas(); c++) {
                if (laberinto.esTransitable(f, c)) {
                    // se crea ID unico basado en la posicion
                    int id = f * laberinto.getColumnas() + c;
                    Vertice v = new Vertice(id, f, c);
                    vertices.agregar(v);
                    adyacencia.put(id, new ArrayList<>());
                }
            }
        }

        // 2. conectar las vertices y su orientación
        int[] df = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        for (Vertice v : vertices.getTabla().values()) {
            for (int i = 0; i < 4; i++) {
                int nf = v.getFila() + df[i];
                int nc = v.getColumna() + dc[i];

                if (laberinto.esTransitable(nf, nc)) {
                    int idVecino = nf * laberinto.getColumnas() + nc;
                    Vertice vecino = vertices.obtener(idVecino);

                    if (vecino != null) {
                        // grafo NO DIRIGIDO
                        adyacencia.get(v.getId()).add(new Arcos(v, vecino, 1));
                    }
                }
            }
        }
    }
        //DFS!
    public List<Vertice> DFS() {
        long inicio = System.nanoTime();
        List<Vertice> visitados = new ArrayList<>();
        Set<Integer> procesados = new HashSet<>();
        Map<Integer, Vertice> predecesores = new HashMap<>();
        Stack<Vertice> pila = new Stack<>();

        pila.push(entrada);

        while (!pila.isEmpty()) {
            Vertice actual = pila.pop();

            if (procesados.contains(actual.getId())) continue;

            procesados.add(actual.getId());
            visitados.add(actual);

            if (actual.equals(salida)) break;

            // Para que la pila saque en orden (Arriba, Derecha, Abajo, Izquierda),
            // debemos meterlos en orden inverso (Izquierda, Abajo, Derecha, Arriba).
            List<Arcos> arcos = adyacencia.get(actual.getId());
            for (int i = arcos.size() - 1; i >= 0; i--) {
                Vertice vecino = arcos.get(i).getDestino();
                if (!procesados.contains(vecino.getId())) {
                    predecesores.put(vecino.getId(), actual);
                    pila.push(vecino);
                }
            }
        }

        long fin = System.nanoTime();
        guardarResultado(visitados, predecesores, procesados.contains(salida.getId()), fin - inicio);
        return visitados;
    }
    // BFS
    public List<Vertice> BFS() {
        long inicio = System.nanoTime();
        List<Vertice> visitados = new ArrayList<>();
        Set<Integer> procesados = new HashSet<>();
        Set<Integer> enCola = new HashSet<>();
        Map<Integer, Vertice> predecesores = new HashMap<>();
        Queue<Vertice> cola = new LinkedList<>();

        cola.add(entrada);
        enCola.add(entrada.getId());

        while (!cola.isEmpty()) {
            Vertice actual = cola.poll();
            procesados.add(actual.getId());
            visitados.add(actual);

            if (actual.equals(salida)) break;

            // Exploración en orden: Arriba, Derecha, Abajo, Izquierda
            for (Arcos arco : adyacencia.get(actual.getId())) {
                Vertice vecino = arco.getDestino();
                if (!procesados.contains(vecino.getId()) && !enCola.contains(vecino.getId())) {
                    predecesores.put(vecino.getId(), actual);
                    enCola.add(vecino.getId());
                    cola.add(vecino);
                }
            }
        }

        long fin = System.nanoTime();
        guardarResultado(visitados, predecesores, procesados.contains(salida.getId()), fin - inicio);
        return visitados;
    }
    // DIJKSTRA
    public List<Vertice> caminoMasCorto() {
        long inicio = System.nanoTime();
        List<Vertice> visitados = new ArrayList<>();
        Set<Integer> procesados = new HashSet<>();
        Map<Integer, Vertice> predecesores = new HashMap<>();
        Map<Integer, Integer> distancias = new HashMap<>();

        // distancias en infinito
        for (Integer id : adyacencia.keySet()) {
            distancias.put(id, Integer.MAX_VALUE);
        }
        distancias.put(entrada.getId(), 0);

        // cola de prioridad ordenada por distancia y, en caso de empate, por ID (menor primero)
        PriorityQueue<Vertice> colaPrioridad = new PriorityQueue<>((v1, v2) -> {
            int comp = Integer.compare(distancias.get(v1.getId()), distancias.get(v2.getId()));
            if (comp == 0) return Integer.compare(v1.getId(), v2.getId());
            return comp;
        });

        colaPrioridad.add(entrada);

        while (!colaPrioridad.isEmpty()) {
            Vertice actual = colaPrioridad.poll();

            if (procesados.contains(actual.getId())) continue;

            procesados.add(actual.getId());
            visitados.add(actual);

            if (actual.equals(salida)) break;

            for (Arcos arco : adyacencia.get(actual.getId())) {
                Vertice vecino = arco.getDestino();
                if (!procesados.contains(vecino.getId())) {
                    int nuevaDist = distancias.get(actual.getId()) + arco.getCosto();
                    if (nuevaDist < distancias.get(vecino.getId())) {
                        distancias.put(vecino.getId(), nuevaDist);
                        predecesores.put(vecino.getId(), actual);
                        colaPrioridad.add(vecino);
                    }
                }
            }
        }

        long fin = System.nanoTime();
        guardarResultado(visitados, predecesores, procesados.contains(salida.getId()), fin - inicio);
        return visitados;
    }

    private void guardarResultado(List<Vertice> visitados, Map<Integer, Vertice> predecesores, boolean encontrado, long tiempo) {
        List<Vertice> camino = null;

        if (encontrado) {
            camino = new ArrayList<>();
            Vertice actual = salida;
            while (actual != null) {
                camino.add(0, actual); // insertar al inicio para reconstruir de entrada a salida
                actual = predecesores.get(actual.getId());
            }
        }

        this.ultimoResultado = new ResultadoBusqueda(visitados, camino, encontrado, tiempo);
    }

    // getters
    public ResultadoBusqueda getUltimoResultado() { return ultimoResultado; }
    public Vertices getVertices() { return vertices; }
    public Vertice getEntrada() { return entrada; }
    public Vertice getSalida() { return salida; }
}