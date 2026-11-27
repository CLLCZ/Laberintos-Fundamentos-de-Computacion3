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
                case 0:
                    cargarLaberinto();
                    break;
                default:
                    break;
            }
        }
    }

    private void cargarLaberinto(){

        Thread cargarArchivo = new Thread(() -> {
            try {
                String path = seleccionarArchivo();
                Laberinto laberinto = LectorLaberinto.cargar(path);
                laberintoActual = laberinto;
                grafoActual = new Grafo(laberinto);
                System.out.flush();
                System.out.println("\r\nLaberinto cargado!!!");
            } catch (ArchivoInvalidoException e) {
                System.out.flush();
                System.out.println("\r\n" + e.toString());
            }
        }, "cargar archivo");

        cargarArchivo.start();

        String[] mensajesCarga = {"Cargando laberinto.  ", "Cargando laberinto.. ", "Cargando laberinto..." };
        int contador = 0;
        while (cargarArchivo.isAlive()){
            System.out.print("\r" + mensajesCarga[contador]);
            contador = (++contador % 3);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println(e.toString());
            }

        }

    }

    private static String seleccionarArchivo() throws ArchivoInvalidoException{
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona un archivo .txt");

        FileNameExtensionFilter filtroTxt = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        chooser.setFileFilter(filtroTxt);

        chooser.setAcceptAllFileFilterUsed(false);

        int resultado = chooser.showOpenDialog(null);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            throw new ArchivoInvalidoException("No se selecciono ningun archivo valido.");
        }

        return chooser.getSelectedFile().getAbsolutePath();
    }
}