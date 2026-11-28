package laberinto;

import java.util.ArrayList;
import java.util.List;

public class DespliegueLaberinto {

    public static void despliegaLaberinto(ResultadoBusqueda resultado, Laberinto laberinto) {
        List<Vertice> ordenVisita = resultado.getVisitados();
        List<Vertice> camino = resultado.getCamino();
        long tiempoNanos = resultado.getTiempoNanos();
        ArrayList<String> lineas = construirLineas(laberinto);
        boolean isSolucion = resultado.isSolucionEncontrada();

        mostrarAnimacion(lineas, ordenVisita);
        mostrarCaminoEncontrado(lineas, camino, tiempoNanos, isSolucion);

    }

    public static void mostrarResultadoFinal(ResultadoBusqueda resultado, Laberinto laberinto) {
        List<Vertice> camino = resultado.getCamino();
        ArrayList<String> lineas = construirLineas(laberinto);
        long tiempoNanos = resultado.getTiempoNanos();
        boolean isSolucion = resultado.isSolucionEncontrada();

        mostrarCaminoEncontrado(lineas, camino, tiempoNanos, isSolucion);
    }

    private static void imprimirLaberinto(ArrayList<String> lineas){
        for(String linea : lineas){
            System.out.println(linea);
        }
    }

    private static void mostrarAnimacion(ArrayList<String> laberinto, List<Vertice> ordenVisita) {
        ArrayList<String> lineas = new ArrayList<>(laberinto);
        System.out.println("Recorriendo laberinto...");
        imprimirLaberinto(lineas);
        for (Vertice visita : ordenVisita){
            System.out.print("\033["+ lineas.size() + "A\r");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e){
                System.out.println(e.toString());
            }

            avanzarLaberinto(visita, lineas);
            imprimirLaberinto(lineas);
        }
        System.out.print("\033["+ (lineas.size() + 1) + "A\r");
    }

    private static ArrayList<String> construirLineas(Laberinto laberinto){
        ArrayList<String> lineas = new ArrayList<>();
        char[][] matrizLaberinto = laberinto.getMatriz();

        for(char[] fila : matrizLaberinto){
            StringBuilder linea = new StringBuilder();
            for (char columna : fila){
                linea.append(columna);
            }
            lineas.add(linea.toString());
        }

        return lineas;
    }

    private static void avanzarLaberinto(Vertice proximo, ArrayList<String> laberinto){
        StringBuilder linea = new StringBuilder(laberinto.get(proximo.getFila()));
        linea.setCharAt(proximo.getColumna(), '#');
        laberinto.set(proximo.getFila(), linea.toString());
    }

    private static void mostrarCaminoEncontrado(ArrayList<String> laberinto, List<Vertice> solucion, long tiempoNanos, boolean isSolucion){
        ArrayList<String> lineas = new ArrayList<>(laberinto);
        for(Vertice vertice : solucion){
            avanzarLaberinto(vertice, lineas);
        }
        System.out.println(isSolucion?("Camino encontrado en: " + (tiempoNanos/1000000) + "ms  ") : "No hay solucion");
        imprimirLaberinto(lineas);
    }
}
