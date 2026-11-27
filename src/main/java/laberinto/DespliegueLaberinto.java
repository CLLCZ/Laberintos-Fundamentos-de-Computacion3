package laberinto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DespliegueLaberinto {

    private static final char VISITADO = '.';
    private static final char CAMINO = '*';
    private static final char ENTRADA = 'E';
    private static final char SALIDA = 'S';
    private static final char ACTUAL = '@';
    private static final int PAUSA_POR_DEFECTO_MS = 100;

    // animación con la pausa por defecto
    public static void despliegaLaberinto(ResultadoBusqueda resultado, Laberinto laberinto) {
        despliegaLaberinto(resultado, laberinto, PAUSA_POR_DEFECTO_MS, null);
    }

    // pausaMs > 0: pausa entre pasos , pausaMs == 0: avanza con Enter
    public static void despliegaLaberinto(ResultadoBusqueda resultado, Laberinto laberinto, int pausaMs, Scanner teclado) {
        if (resultado == null) {
            System.out.println("Este algoritmo todavía no se ha ejecutado.");
            return;
        }
        imprimirLeyenda();
        mostrarAnimacion(resultado, laberinto, pausaMs, teclado);
        imprimirEstadisticas(resultado);
    }

    // sin animación: laberinto con la exploración y el camino final, y los datos del recorrido
    public static void mostrarResultadoFinal(ResultadoBusqueda resultado, Laberinto laberinto) {
        if (resultado == null) {
            System.out.println("Este algoritmo todavía no se ha ejecutado.");
            return;
        }
        imprimirLeyenda();
        imprimirCuadro(construirLineas(laberinto, resultado.getVisitados(), resultado.getCamino(), null),
                estadoFinal(resultado), false);
        imprimirEstadisticas(resultado);
    }

    public static void imprimirLeyenda() {
        System.out.println("Leyenda: E=entrada  S=salida  .=visitado  @=actual  *=camino final  +,-,|=paredes  espacio=libre");
    }

    private static void mostrarAnimacion(ResultadoBusqueda resultado, Laberinto laberinto, int pausaMs, Scanner teclado) {
        List<Vertice> ordenVisita = resultado.getVisitados();
        boolean enLugar = pausaMs > 0;                 // con Enter los cuadros se imprimen uno debajo de otro
        int altura = laberinto.getFilas() + 1;         // renglones del laberinto + línea de estado
        System.out.println("Recorriendo laberinto");

        for (int i = 0; i < ordenVisita.size(); i++) {
            Vertice actual = ordenVisita.get(i);
            if (enLugar && i > 0) {
                System.out.print("\033[" + altura + "A\r");
            }
            String estado = "Paso " + (i + 1) + " de " + ordenVisita.size() + "  actual: " + actual;
            imprimirCuadro(construirLineas(laberinto, ordenVisita.subList(0, i + 1), null, actual), estado, enLugar);
            esperar(pausaMs, teclado);
        }

        // último cuadro: se distingue el camino final de lo explorado (o el aviso si no hay solución)
        if (enLugar) {
            System.out.print("\033[" + altura + "A\r");
        }
        imprimirCuadro(construirLineas(laberinto, ordenVisita, resultado.getCamino(), null), estadoFinal(resultado), enLugar);
    }

    private static String estadoFinal(ResultadoBusqueda resultado) {
        if (resultado.isSolucionEncontrada()) {
            return "Camino final marcado con *";
        }
        return "Sin solución: se muestra solo la exploración";
    }

    private static void esperar(int pausaMs, Scanner teclado) {
        if (pausaMs > 0) {
            try {
                Thread.sleep(pausaMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else if (teclado != null) {
            System.out.print("(Enter para avanzar) ");
            teclado.nextLine();
        }
    }

    // camino puede ser null (sin solución) y actual puede ser null (cuadro final)
    private static ArrayList<String> construirLineas(Laberinto laberinto, List<Vertice> visitados, List<Vertice> camino, Vertice actual) {
        char[][] base = laberinto.getMatriz();
        char[][] pantalla = new char[base.length][];
        for (int i = 0; i < base.length; i++) {
            pantalla[i] = base[i].clone();            // copia para no modificar el laberinto
        }

        for (Vertice v : visitados) {
            pantalla[v.getFila()][v.getColumna()] = VISITADO;
        }
        if (camino != null) {
            for (Vertice v : camino) {
                pantalla[v.getFila()][v.getColumna()] = CAMINO;
            }
        }
        Vertice entrada = laberinto.getEntrada();
        Vertice salida = laberinto.getSalida();
        pantalla[entrada.getFila()][entrada.getColumna()] = ENTRADA;
        pantalla[salida.getFila()][salida.getColumna()] = SALIDA;
        if (actual != null) {
            pantalla[actual.getFila()][actual.getColumna()] = ACTUAL;
        }

        ArrayList<String> lineas = new ArrayList<>();
        for (char[] fila : pantalla) {
            lineas.add(new String(fila));
        }
        return lineas;
    }

    private static void imprimirCuadro(ArrayList<String> lineas, String estado, boolean enLugar) {
        for (String linea : lineas) {
            System.out.println(linea);
        }
        System.out.println(estado + (enLugar ? "\033[K" : ""));
    }

    private static void imprimirEstadisticas(ResultadoBusqueda resultado) {
        System.out.println("Vértices visitados: " + resultado.getVisitados().size());
        if (resultado.isSolucionEncontrada()) {
            System.out.println("Camino encontrado: " + (resultado.getCamino().size() - 1) + " movimientos");
        } else {
            System.out.println("No existe camino entre la entrada y la salida.");
        }
        System.out.println("Tiempo de búsqueda: " + String.format("%.3f", resultado.getTiempoNanos() / 1_000_000.0) + " ms");
    }
}