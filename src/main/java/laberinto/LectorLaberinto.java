package laberinto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorLaberinto {

    public static Laberinto cargar(String ruta) throws ArchivoInvalidoException {
        List<String> lineas = leerArchivo(ruta);
        return Laberinto.desdeLineas(lineas);
    }
    public static List<String> leerArchivo(String ruta) throws ArchivoInvalidoException {
        if (ruta == null) {
            throw new ArchivoInvalidoException("La ruta no puede ser nula.");
        }

        File archivo = new File(ruta.trim());

        if (!archivo.exists()) {
            throw new ArchivoInvalidoException("El archivo no existe: " + archivo.getAbsolutePath());
        }
        if (!archivo.isFile()) {
            throw new ArchivoInvalidoException("La ruta especificada no es un archivo: " + archivo.getAbsolutePath());
        }
        if (!archivo.canRead()) {
            throw new ArchivoInvalidoException("No hay permisos de lectura para el archivo: " + archivo.getAbsolutePath());
        }

        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea.replace("\r", ""));
            }
        }catch (IOException e) {
            throw new ArchivoInvalidoException("Error al leer el archivo: " + e.getMessage());
        }
        return lineas;
    }
}