package laberinto;

import java.util.Scanner;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuConsola {
    private Laberinto laberintoActual;
    private Grafo grafoActual;
    private LectorLaberinto lector;
    private DespliegueLaberinto despliegue;
    private ReporteComparativo reporte;

    public MenuConsola() {
        this.lector = new LectorLaberinto();
        this.despliegue = new DespliegueLaberinto();
        this.reporte = new ReporteComparativo();
    }

    public void ejecutar() {
        Scanner sc = new Scanner(System.in);

        while (true){
            int opcion = -1;
            System.out.println("-----RESOLVER LABERINTOS CON GRAFOS-----");
            System.out.println("0. Cargar laberinto");
            System.out.println("1. Resolver con DFS");
            System.out.println("2. Resolver con BFS");
            System.out.println("3. Camino mas corto");
            System.out.println("4. Mostrar las tres soluciones");
            System.out.println("5. Salir");

            if (sc.hasNextInt()){
                int n = sc.nextInt();
                if (0 <= n && n <= 5){
                    opcion = n;
                } else {
                    System.out.println("Seleccione una opcion valida");
                }
            } else {
                System.out.println("Seleccione una opcion valida");
                sc.next();
                continue;
            }

            switch (opcion){

                default:
                    break;
            }
        }
    }


}