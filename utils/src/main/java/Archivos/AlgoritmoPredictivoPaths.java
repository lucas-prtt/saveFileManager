package Archivos;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AlgoritmoPredictivoPaths {
    static List<Path> raicesBusquedaDefault = List.of(
            Paths.get(System.getProperty("user.home"), "Documents"),
            Paths.get(System.getProperty("user.home"), "AppData", "Local"),
            Paths.get(System.getProperty("user.home"), "AppData", "LocalLow"),
            Paths.get(System.getProperty("user.home"), "AppData", "Roaming"),
            Paths.get(System.getProperty("user.home"), "Saved Games"),
            Paths.get(System.getProperty("user.home"), ".config"),
            Paths.get(System.getProperty("user.home"), ".local", "share")
            //,Paths.get("C:\\Program Files (x86)\\Steam")
    );
    static List<Path> directoriosProhibidos = new ArrayList<>();
    static List<Path> raicesBusqueda = new ArrayList<>(raicesBusquedaDefault);
    public static void addDirectoriosProhibido(Path path){
        directoriosProhibidos.add(path);
    }
    public static void quitarDeDirectoriosProhibidos(Path path){
        directoriosProhibidos.remove(path);
    }
    public static void addRaiz(Path path){
        raicesBusqueda.add(path);
    }
    public static void quitarRaiz(Path path){
        raicesBusqueda.remove(path);
    }


    public static Path predecir(List<Path> validos) throws Exception {

        Path mejorCoincidencia = buscarPathSimilar(raicesBusqueda, validos);
        if (mejorCoincidencia != null) {
            return mejorCoincidencia;
        } else {
            throw new Exception("No se pudo encontrar un Path similar a ninguno de los validos");
        }
    }
    public static Path buscarPathSimilar(List<Path> raicesBusqueda, List<Path> pathValidos){
        List<Path> sufijos;


        sufijos = new ArrayList<>(pathValidos.stream().flatMap(p -> generarSufijos(p).stream()).toList()); // Obtiene lista con muchos posibles sufijos
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
                        if(directoriosProhibidos.contains(dir)) return FileVisitResult.SKIP_SUBTREE;
                        for (Path sufijo: sufijos) {
                            if (dir.endsWith(sufijo)) {
                                int score = contarNiveles(sufijo);
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
    public static List<Path> generarSufijos(Path pathValido) {
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
        return sufijos.stream().map(Path::of).toList();// Genera algo tipo ["Fallout", "Bethesda/Fallout", "Games/Bethesda/Fallout", etc...]
        }
    private static int contarNiveles(Path path) {
        return path.getNameCount();
    }
}
