package laberinto;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class MenuConsola {
    private Laberinto laberintoActual = null;
    private Grafo grafoActual = null;
    private Scanner sc;

    public void ejecutar() {
        sc = new Scanner(System.in);

        int opcion = -1;
        while (opcion != 6) {
            imprimirMenu();
            opcion = leerOpcion();
            System.out.println();

            switch (opcion) {
                case 1:
                    cargarLaberinto();
                    break;
                case 2:
                    resolver(Grafo.ALG_DFS);
                    break;
                case 3:
                    resolver(Grafo.ALG_BFS);
                    break;
                case 4:
                    resolver(Grafo.ALG_DIJKSTRA);
                    break;
                case 5:
                    mostrarTresSoluciones();
                    break;
                case 6:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
                    break;
            }
        }
    }

    private void imprimirMenu() {
        System.out.println("\n-----RESOLVER LABERINTOS CON GRAFOS-----");
        // 1-6 numeración
        System.out.println("1. Cargar laberinto");
        System.out.println("2. Resolver con DFS");
        System.out.println("3. Resolver con BFS");
        System.out.println("4. Resolver con Dijkstra (camino mas corto)");
        System.out.println("5. Mostrar las tres soluciones");
        System.out.println("6. Salir");
    }


    private int leerOpcion() {
        System.out.print("Elige una opción: ");
        String linea = leerLinea();
        if (linea == null) {
            // si se acaba la entrada se sale del menú
            System.out.println();
            return 6;
        }
        try {
            int n = Integer.parseInt(linea.trim());
            if (1 <= n && n <= 6) {
                return n;
            }
        } catch (NumberFormatException e) {
            // no es un número, se trata como opción no válida
        }
        return -1;
    }

    //devuelve null cuando ya no hay más entrada
    private String leerLinea() {
        try {
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    //la ruta se pide por consola
    private void cargarLaberinto() {
        System.out.print("Escribe la ruta del archivo del laberinto: ");
        String ruta = leerLinea();
        if (ruta == null) {
            return;
        }
        ruta = limpiarRuta(ruta);
        if (ruta.isEmpty()) {
            System.out.println("No escribiste ninguna ruta.");
            return;
        }

        try {
            Laberinto nuevo = LectorLaberinto.cargar(ruta);
            // el Grafo nuevo descarta los resultados anteriores; solo se reemplaza si la carga salió bien
            laberintoActual = nuevo;
            grafoActual = new Grafo(nuevo);
            System.out.println("Laberinto cargado: " + nuevo.getFilas() + " renglones x " + nuevo.getColumnas() + " columnas.");
        } catch (ArchivoInvalidoException e) {
            System.out.println("No se pudo cargar el laberinto: " + e.getMessage());
            if (laberintoActual != null) {
                System.out.println("Se conserva el laberinto cargado anteriormente.");
            }
        }
    }

    // quita comillas, convierte "\ " en espacio
    private String limpiarRuta(String ruta) {
        String limpia = ruta.trim();
        boolean entreComillas = limpia.length() >= 2
                && ((limpia.startsWith("\"") && limpia.endsWith("\""))
                || (limpia.startsWith("'") && limpia.endsWith("'")));
        if (entreComillas) {
            limpia = limpia.substring(1, limpia.length() - 1);
        }
        limpia = limpia.replace("\\ ", " ");
        if (limpia.startsWith("~")) {
            limpia = System.getProperty("user.home") + limpia.substring(1);
        }
        return limpia;
    }

    private boolean hayLaberinto() {
        if (grafoActual == null) {
            System.out.println("Primero carga un laberinto (opción 1).");
            return false;
        }
        return true;
    }

    private void resolver(String algoritmo) {
        if (!hayLaberinto()) {
            return;
        }
        ejecutarAlgoritmo(algoritmo);
        DespliegueLaberinto.despliegaLaberinto(grafoActual.getResultado(algoritmo), laberintoActual);
    }

    private void mostrarTresSoluciones() {
        if (!hayLaberinto()) {
            return;
        }
        String[] algoritmos = {Grafo.ALG_DFS, Grafo.ALG_BFS, Grafo.ALG_DIJKSTRA};
        for (String alg : algoritmos) {
            if (grafoActual.getResultado(alg) == null) {
                ejecutarAlgoritmo(alg);
            }
            System.out.println("-----" + alg + "-----");
            DespliegueLaberinto.mostrarResultadoFinal(grafoActual.getResultado(alg), laberintoActual);
        }
    }

    private void ejecutarAlgoritmo(String algoritmo) {
        if (algoritmo.equals(Grafo.ALG_DFS)) {
            grafoActual.DFS();
        } else if (algoritmo.equals(Grafo.ALG_BFS)) {
            grafoActual.BFS();
        } else {
            grafoActual.caminoMasCorto();
        }
    }
}
