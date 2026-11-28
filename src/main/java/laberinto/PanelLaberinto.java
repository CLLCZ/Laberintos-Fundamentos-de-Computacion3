package laberinto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelLaberinto extends JPanel {
    private static final char PARED_H = '-', PARED_V = '|', ESQUINA = '+';
    private static final char VISITADO = '*', CAMINO = '#', ACTUAL = ':';
    private static final char ENTRADA = 'E', SALIDA = 'S';

    private static final Color C_PARED = Color.GRAY;
    private static final Color C_VISITADO = new Color(70, 150, 230);
    private static final Color C_CAMINO = new Color(112, 157, 122);
    private static final Color C_ACTUAL = Color.RED;
    private static final Color C_ENTRADA = Color.BLUE;
    private static final Color C_SALIDA = Color.MAGENTA;

    private final Font fuente = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private final int anchoCelda, altoCelda, base;

    private Laberinto laberinto;
    private List<Vertice> visitados = List.of();
    private int hasta = 0;
    private List<Vertice> camino;
    private Vertice actual;

    public PanelLaberinto() {
        FontMetrics fm = getFontMetrics(fuente);
        anchoCelda = fm.charWidth('M');
        altoCelda = fm.getHeight();
        base = fm.getAscent();
        setBackground(Color.WHITE);
    }

    public void setLaberinto(Laberinto l) {
        this.laberinto = l;
        limpiar();
        revalidate();
    }

    public void limpiar() {
        visitados = List.of();
        hasta = 0;
        camino = null;
        actual = null;
        repaint();
    }

    public void mostrar(List<Vertice> visitados, int hasta, List<Vertice> camino, Vertice actual) {
        this.visitados = visitados;
        this.hasta = hasta;
        this.camino = camino;
        this.actual = actual;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (laberinto == null) return new Dimension(300, 200);
        return new Dimension(laberinto.getColumnas() * anchoCelda, laberinto.getFilas() * altoCelda);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (laberinto == null) return;
        g.setFont(fuente);

        int filas = laberinto.getFilas(), cols = laberinto.getColumnas();
        boolean[][] vis = new boolean[filas][cols];
        for (int i = 0; i < hasta && i < visitados.size(); i++) {
            Vertice v = visitados.get(i);
            vis[v.getFila()][v.getColumna()] = true;
        }
        boolean[][] enCamino = new boolean[filas][cols];
        if (camino != null) {
            for (Vertice v : camino) enCamino[v.getFila()][v.getColumna()] = true;
        }

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                char ch;
                Color color;
                if (!laberinto.esTransitable(f, c)) {
                    ch = laberinto.getChar(f, c);
                    color = C_PARED;
                } else if (enCamino[f][c]) {
                    ch = CAMINO;
                    color = C_CAMINO;
                } else if (vis[f][c]) {
                    ch = VISITADO;
                    color = C_VISITADO;
                } else {
                    continue;
                }
                dibujar(g, ch, color, f, c);
            }
        }
        Vertice e = laberinto.getEntrada(), s = laberinto.getSalida();
        dibujar(g, ENTRADA, C_ENTRADA, e.getFila(), e.getColumna());
        dibujar(g, SALIDA, C_SALIDA, s.getFila(), s.getColumna());
        if (actual != null) dibujar(g, ACTUAL, C_ACTUAL, actual.getFila(), actual.getColumna());
    }

    private void dibujar(Graphics g, char ch, Color color, int f, int c) {
        g.setColor(color);
        g.drawString(String.valueOf(ch), c * anchoCelda, f * altoCelda + base);
    }
}