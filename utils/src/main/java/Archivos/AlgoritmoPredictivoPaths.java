package Archivos;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.io.FileWriter;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AlgoritmoPredictivoPaths {
    public static Path predecir(List<Path> validos) throws Exception {
        List<Path> raicesBusqueda = List.of(
                Paths.get(System.getProperty("user.home")),
                Paths.get(System.getProperty("user.home"), "AppData", "Local"),
                Paths.get(System.getProperty("user.home"), "AppData", "LocalLow"),
                Paths.get(System.getProperty("user.home"), "AppData", "Roaming"),
                Paths.get(System.getProperty("user.home"), "Documents"),
                Paths.get(System.getProperty("user.home"), "Saved Games"),
                Paths.get(System.getProperty("user.home"), ".config"),
                Paths.get(System.getProperty("user.home"), ".local", "share"),
                Paths.get(System.getProperty("user.home"), "AppData", "Local", "Steam", "userdata"),
                Paths.get("C:\\Program Files (x86)\\Steam")
        );
        Path mejorCoincidencia = buscarPathSimilar(raicesBusqueda, validos);
        if (mejorCoincidencia != null) {
            return mejorCoincidencia;
        } else {
            throw new Exception("No se pudo encontrar un Path similar a ninguno de los validos");
        }
    }
    public static Path buscarPathSimilar(List<Path> raicesBusqueda, List<Path> pathValidos){
        List<String> sufijos = new ArrayList<>(pathValidos.stream()
                .map(path -> path.normalize().toString().replace('\\', '/'))
                .map(p -> p.replaceAll("^[a-zA-Z]:", "")) // quitar letra del disco si hay
                .map(p -> p.startsWith("/") ? p.substring(1) : p) // quitar / inicial
                .flatMap(p -> generarSufijos(Path.of(p)).stream())
                .toList()); // Obtiene lista con muchos posibles sufijos


        sufijos.sort((a, b) -> Integer.compare(
                contarNiveles(b),
                contarNiveles(a)
        ));// Los ordena de mas especificos a mas genericos

        final int[] mejorScore = {-1}; // Para comparar el mejor sufijo según cada raiz. Es lista porque se usa en otra clase

        final Path[] mejorMatch = {null}; // El mejor match hasta la fecha

        for (Path raiz : raicesBusqueda) {
            if (!Files.exists(raiz) || !Files.isDirectory(raiz)) continue;  // Si no existe la raiz, no tiene sentido seguir analizando. Se va a la raiz siguiente

            try {
                Files.walkFileTree(raiz, new SimpleFileVisitor<>() { // Explora cada raiz
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) { // Ejecuta walkFileTree desde la raiz, con esta clase a modo de visitor. Solo visita directorios
                        String dirPath = dir.toAbsolutePath().normalize().toString().replace('\\', '/'); // Normaliza la raiz. Se analiza de atras para adelante, por lo que no se saca C: o eso
                        for (String sufijo : sufijos) {
                            if (dirPath.endsWith(sufijo)) {
                                int score = sufijo.split("/").length;
                                    if (score > mejorScore[0]) {
                                        mejorScore[0] = score;
                                        mejorMatch[0] = dir; // Guardo dir, no dirpath
                                }
                                break;
                            }
                        }
                        return FileVisitResult.CONTINUE; // Visita subdirectorios (si los hay)
                    }
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                System.err.println("Error escaneando raíz: " + raiz + " -> " + e.getMessage());
            }
        }
        return mejorMatch[0]; // Devuelve el directorio mas adecuado, en formato Path
    }
    public static List<String> generarSufijos(Path pathValido) {
        List<String> sufijos = new ArrayList<>();
        List<String> acumulador = new ArrayList<>();
        List<String> partes = new ArrayList<>();

        for (Path p : pathValido) { // Itera sobre cada "palabra" del path (separada por /)
            partes.add(p.toString());
        }// Me queda una lista que diga, por ejemplo ["Users", "Admin", "Documents", "Games", "Bethesda", "Fallout New Vegas"]
        Collections.reverse(partes); // La doy vuelta, para que arranque con las partes más del final
        for (String parte : partes) {
            acumulador.addFirst(parte); //Se va añadiendo de atras para adelante ["Fallout New Vegas"] -> ["Bethesda", "Fallout New Vegas"]
            String sufijo = String.join("/", acumulador); // Forma el path, creando un objeto nuevo
            sufijos.add(sufijo);    // Añade el objeto nuevo a la lista de sufijos a chequear
        }
        return sufijos;// Genera algo tipo ["Fallout", "Bethesda/Fallout", "Games/Bethesda/Fallout", etc...]
        }
    private static int contarNiveles(String path) {
        return path.split("/").length;
    }
}
