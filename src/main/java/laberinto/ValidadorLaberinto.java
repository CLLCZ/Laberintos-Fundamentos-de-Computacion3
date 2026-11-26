package laberinto;

import java.util.ArrayList;
import java.util.List;

public class ValidadorLaberinto {
    private static final String CAR_VALIDOS = "+-| #ES";
//E = Entrada , S = Salida ;
    public static boolean esTransitable(char caract) {
        return caract == ' ' || caract == '#' || caract == 'E' || caract == 'S';
    }


    public static int[] validar(List<String> lineas) throws ArchivoInvalidoException {
        if (lineas.isEmpty()) throw new IllegalArgumentException("El archivo está vacío.");

        int ancho = lineas.get(0).length();
        for (int i = 0; i < lineas.size(); i++) {
            String l = lineas.get(i);
            if (l.length() == 0)
                throw new ArchivoInvalidoException("El renglón " + (i + 1) + " está vacío (mide 0).");
            if (l.length() != ancho)
                throw new ArchivoInvalidoException("El renglón " + (i + 1) + " mide " + l.length()
                        + " y se esperaban " + ancho + " columnas.");
            for (int j = 0; j < l.length(); j++) {
                char caract = l.charAt(j);
                if (caract == '\t')
                    throw new ArchivoInvalidoException("Tabulación en renglón " + (i + 1)
                            + ", columna " + (j + 1) + ": las tabulaciones no sustituyen espacios.");
                if (CAR_VALIDOS.indexOf(caract) < 0)
                    throw new ArchivoInvalidoException("Carácter inválido '" + caract + "' en renglón "
                            + (i + 1) + ", columna " + (j + 1) + ".");
            }
        }
        return localizarEntradaSalida(lineas);
    }

    private static int[] localizarEntradaSalida(List<String> lineas) throws ArchivoInvalidoException {
        int filas = lineas.size(), columnas = lineas.get(0).length();
        int numeroE = 0, numeroS = 0;
        int filaE = -1, columnaE = -1;
        int filaS = -1, columnaS = -1;

        for (int i = 0; i < filas; i++)
            for (int j = 0; j < columnas; j++) {
                char caract = lineas.get(i).charAt(j);
                if (caract == 'E') {
                    numeroE++;
                    filaE = i;
                    columnaE = j;
                }
                if (caract == 'S') {
                    numeroS++;
                    filaS = i;
                    columnaS = j;
                }
            }

        if (numeroE > 0 || numeroS > 0) {
            if (numeroE != 1 || numeroS != 1)
                throw new ArchivoInvalidoException("Con marcas explícitas debe haber exactamente una 'Entrada' y una 'Salida' (hay "
                        + numeroE + " 'Entradas' y " + numeroS + " 'Salidas').");
            return new int[] {filaE, columnaE, filaS, columnaS};
        }

        List<int[]> borde = new ArrayList<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if ((i == 0 || i == filas - 1 || j == 0 || j == columnas - 1) && esTransitable(lineas.get(i).charAt(j))) {
                    borde.add(new int[]{i, j});
                }
            }
        }

        if (borde.size() < 2) {
            throw new ArchivoInvalidoException("Sin marcas Entrada/Salida se necesitan al menos dos posiciones transitables en el borde (hay "
                    + borde.size() + ").");
        }
        return new int[]{borde.get(0)[0], borde.get(0)[1], borde.get(1)[0], borde.get(1)[1]};
    }

}