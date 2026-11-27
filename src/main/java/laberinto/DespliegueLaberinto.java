package laberinto;

import java.util.ArrayList;
import java.util.List;

public class DespliegueLaberinto {

    public static void despliegaLaberinto(List<Vertice> ordenVisita, Laberinto laberinto) throws InterruptedException {
        ArrayList<String> lineas = construirLineas(laberinto);
        imprimirLaberinto(lineas);
        for (Vertice visita : ordenVisita){
            Thread.sleep(100);
            avanzarLaberinto(visita, lineas);
            imprimirLaberinto(lineas);
        }
    }

    public void mostrarResultadoFinal(ResultadoBusqueda resultado) {
        throw new UnsupportedOperationException(" rol C: imprimir camino o aviso");
    }

    private static void imprimirLaberinto(ArrayList<String> lineas){
        System.out.println();
        for(String linea : lineas){
            System.out.println(linea);
        }
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

    public static void limpiar() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // ignorar
        }
    }
}
