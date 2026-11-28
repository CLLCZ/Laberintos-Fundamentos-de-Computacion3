package laberinto;

import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuConsola {
    private Laberinto laberintoActual;
    private Grafo grafoActual = null;


    public void ejecutar() {
        Scanner sc = new Scanner(System.in);

        int opcion = -1;
        while (opcion != 6){
            opcion = -1;
            System.out.println("\n-----RESOLVER LABERINTOS CON GRAFOS-----");
            // numeración 1-6
            System.out.println("1. Cargar laberinto");
            System.out.println("2. Resolver con DFS");
            System.out.println("3. Resolver con BFS");
            System.out.println("4. Resolver con Dijkstra (camino mas corto)");
            System.out.println("5. Mostrar las tres soluciones");
            System.out.println("6. Salir");

            if (sc.hasNextInt()){
                int n = sc.nextInt();
                if (1 <= n && n <= 6){
                    opcion = n;
                } else {
                    System.out.println("Seleccione una opcion valida");
                }
            } else {
                System.out.println("Seleccione una opcion valida");
                sc.next();
                continue;
            }

            System.out.println();
            switch (opcion){
                case 1:
                    cargarLaberinto();
                    break;
                case 2:
                    if (grafoActual != null){
                        // se ejecuta el algoritmo deseado
                        ejecutarAlgoritmo(Grafo.ALG_DFS);
                        DespliegueLaberinto.despliegaLaberinto(grafoActual.getResultado(Grafo.ALG_DFS), laberintoActual);
                        break;
                    } else {
                        System.out.println("Debes de agregar un laberinto");
                    }
                    break;
                case 3:
                    if (grafoActual != null){
                        ejecutarAlgoritmo(Grafo.ALG_BFS);
                        DespliegueLaberinto.despliegaLaberinto(grafoActual.getResultado(Grafo.ALG_BFS), laberintoActual);
                        break;
                    } else {
                        System.out.println("Debes de agregar un laberinto");
                    }
                    break;
                case 4:
                    if (grafoActual != null){
                        ejecutarAlgoritmo(Grafo.ALG_DIJKSTRA);
                        DespliegueLaberinto.despliegaLaberinto(grafoActual.getResultado(Grafo.ALG_DIJKSTRA), laberintoActual);
                        break;
                    } else {
                        System.out.println("Debes de agregar un laberinto");
                    }
                    break;
                case 5:
                    if (grafoActual != null){
                        String[] algoritmos = {Grafo.ALG_BFS, Grafo.ALG_DFS, Grafo.ALG_DIJKSTRA};
                        for (String alg : algoritmos){
                            // Problema 3: solo se ejecutan los que falten, los demás se reutilizan
                            if (grafoActual.getResultado(alg) == null){
                                ejecutarAlgoritmo(alg);
                            }
                            System.out.println("-----"+ alg+ "-----");
                            DespliegueLaberinto.mostrarResultadoFinal(grafoActual.getResultado(alg), laberintoActual);
                        }
                    } else {
                        System.out.println("Debes de agregar un laberinto");
                    }
                    break;
                case 6:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    break;
            }


        }
    }

    private void ejecutarAlgoritmo(String algoritmo){
        if (algoritmo.equals(Grafo.ALG_DFS)){
            grafoActual.DFS();
        } else if (algoritmo.equals(Grafo.ALG_BFS)){
            grafoActual.BFS();
        } else {
            grafoActual.caminoMasCorto();
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
                // Mostrar motivo y no sologit "laberinto.ArchivoInvalidoException: ...")
                System.out.println("\r\nNo se pudo cargar el laberinto: " + e.getMessage());
            }
        }, "cargar archivo");

        cargarArchivo.start();

        String[] mensajesCarga = {"Selecciona tu laberinto.  ", "Selecciona tu laberinto.. ", "Selecciona tu laberinto..." };
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