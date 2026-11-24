package laberinto;

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
        throw new UnsupportedOperationException(" rol C: bucle while con menú");
    }
}