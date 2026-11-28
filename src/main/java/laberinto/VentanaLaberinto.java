package laberinto;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VentanaLaberinto extends JFrame {
    private Laberinto laberintoActual;
    private Grafo grafoActual;

    private final PanelLaberinto panel = new PanelLaberinto();
    private final JLabel estado = new JLabel("Carga un laberinto para comenzar.");
    private final JSpinner pausa = new JSpinner(new SpinnerNumberModel(100, 1, 2000, 10));
    private final JButton btnDfs = new JButton("DFS");
    private final JButton btnBfs = new JButton("BFS");
    private final JButton btnDij = new JButton("Dijkstra");
    private final JButton btnTres = new JButton("Mostrar todas");
    private Timer timer;

    public VentanaLaberinto() {
        super("Resolver laberintos con grafos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnCargar = new JButton("Cargar laberinto");
        JButton btnSalir = new JButton("Salir");

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(btnCargar);
        barra.add(btnDfs);
        barra.add(btnBfs);
        barra.add(btnDij);
        barra.add(btnTres);
        barra.add(new JLabel("Pausa (ms):"));
        barra.add(pausa);
        barra.add(btnSalir);

        JPanel sur = new JPanel(new GridLayout(2, 1));
        sur.add(new JLabel("  E = Entrada  S = Salida  Celeste = Visitado  Verde = Camino final"));
        sur.add(estado);

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        btnCargar.addActionListener(e -> cargar());
        btnDfs.addActionListener(e -> resolver(Grafo.ALG_DFS));
        btnBfs.addActionListener(e -> resolver(Grafo.ALG_BFS));
        btnDij.addActionListener(e -> resolver(Grafo.ALG_DIJKSTRA));
        btnTres.addActionListener(e -> mostrarTodos());
        btnSalir.addActionListener(e -> System.exit(0));

        habilitarAlgoritmos(false);
        setSize(700, 700);
        setLocationRelativeTo(null);
    }

    private void habilitarAlgoritmos(boolean on) {
        btnDfs.setEnabled(on);
        btnBfs.setEnabled(on);
        btnDij.setEnabled(on);
        btnTres.setEnabled(on);
    }

    private void cargar() {
        JFileChooser chooser = new JFileChooser(new File("."));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            Laberinto nuevo = LectorLaberinto.cargar(chooser.getSelectedFile().getAbsolutePath());
            detenerAnimacion();
            laberintoActual = nuevo;
            grafoActual = new Grafo(nuevo);
            panel.setLaberinto(nuevo);
            habilitarAlgoritmos(true);
            estado.setText("Laberinto cargado: " + nuevo.getFilas() + " renglones x " + nuevo.getColumnas() + " columnas.");
        } catch (ArchivoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo cargar el laberinto", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void resolver(String alg) {
        detenerAnimacion();
        ejecutar(alg);
        ResultadoBusqueda r = grafoActual.getResultado(alg);
        List<Vertice> orden = r.getVisitados();

        int[] paso = {0};
        timer = new Timer((Integer) pausa.getValue(), null);
        timer.addActionListener(e -> {
            if (paso[0] < orden.size()) {
                paso[0]++;
                panel.mostrar(orden, paso[0], null, orden.get(paso[0] - 1));
                estado.setText(alg + ": paso " + paso[0] + " de " + orden.size());
            } else {
                timer.stop();
                panel.mostrar(orden, orden.size(), r.getCamino(), null);
                estado.setText(resumen(alg, r));
                if (!r.isSolucionEncontrada()) {
                    JOptionPane.showMessageDialog(this, "No existe camino entre la entrada y la salida.");
                }
            }
        });
        timer.start();
    }

    private void ejecutar(String alg) {
        switch (alg) {
            case Grafo.ALG_DFS -> grafoActual.DFS();
            case Grafo.ALG_BFS -> grafoActual.BFS();
            default -> grafoActual.caminoMasCorto();
        }
    }

    private void detenerAnimacion() {
        if (timer != null && timer.isRunning()) timer.stop();
    }

    private String resumen(String alg, ResultadoBusqueda r) {
        String ms = String.format("%.3f ms", r.getTiempoNanos() / 1_000_000.0);
        if (!r.isSolucionEncontrada())
            return alg + ": sin solución. Visitados: " + r.getVisitados().size() + ". Tiempo: " + ms;
        return alg + ": visitados " + r.getVisitados().size()
                + ", longitud " + (r.getCamino().size() - 1) + " movimientos, tiempo " + ms;
    }

    private void mostrarTodos() {
        detenerAnimacion();
        JTabbedPane tabs = new JTabbedPane();
        for (String alg : new String[]{Grafo.ALG_DFS, Grafo.ALG_BFS, Grafo.ALG_DIJKSTRA}) {
            if (grafoActual.getResultado(alg) == null) ejecutar(alg);
            ResultadoBusqueda r = grafoActual.getResultado(alg);

            PanelLaberinto p = new PanelLaberinto();
            p.setLaberinto(laberintoActual);
            p.mostrar(r.getVisitados(), r.getVisitados().size(), r.getCamino(), null);

            JPanel contenedor = new JPanel(new BorderLayout());
            contenedor.add(new JScrollPane(p), BorderLayout.CENTER);
            contenedor.add(new JLabel(resumen(alg, r)), BorderLayout.SOUTH);
            tabs.addTab(alg, contenedor);
        }
        JDialog d = new JDialog(this, "Las tres soluciones", true);
        d.add(tabs);
        d.setSize(900, 650);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }
}
