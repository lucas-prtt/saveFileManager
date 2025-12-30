package domain.Archivos;
import domain.Archivos.checkpoint.Archivo;
import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.Carpeta;
import domain.Archivos.checkpoint.GrupoDeDatos;
import domain.Juegos.Juego;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileManager {

    private static ObjectStore objectStore =
            new ObjectStore(Paths.get("data/objects"));


    public static Map<GrupoDeDatos, Path> hallarPaths(List<GrupoDeDatos> grupoDeDatosList){
        System.out.println("Se cargaran los archivos en:");
        Map<GrupoDeDatos, Path> caminosEncontrados = new HashMap<>();

        for (GrupoDeDatos grupoDeDatos : grupoDeDatosList){
            try {
                Path path = grupoDeDatos.getDirectorio().getPathPrincipal(); // TODO: Predecir path de manera inteligente, lidiar con sinonimos
                System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).bold().a(String.format("%-30s -> %s", grupoDeDatos.getDirectorio().getPathPrincipal(), path.toString())).reset());
                caminosEncontrados.put(grupoDeDatos, path);
            }catch (Exception e){
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).bold().a(String.format("%-30s -> %s", grupoDeDatos.getDirectorio().getPathPrincipal(), " No fue ubicado.")).reset());
            }
        }
        return caminosEncontrados;
    }

    public static void cargarArchivos(Map<GrupoDeDatos, Path> archivosACargar){
        archivosACargar.forEach((datos, path) -> {
            datos.getArchivos().forEach(archivo -> cargarArchivo(archivo, path));
        });
    }

    public static void cargarArchivo(Archivo archivo, Path path){

        if(archivo instanceof Carpeta carpeta){
            carpeta.getArchivos().forEach( archivoInterno -> cargarArchivo(archivoInterno, path.resolve(carpeta.getNombre())));
        }else if (archivo instanceof ArchivoFinal archivoFinal){
            try {
                objectStore.loadArchivoFinal(archivoFinal, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<GrupoDeDatos> guardarArchivos(Juego juego) {

        System.out.println("Leyendo archivos de:");
        juego.getSaveFilePaths()
                .forEach(d -> System.out.println(d.getPathPrincipal()));

        return juego.getSaveFilePaths().stream()
                .map(dir -> {
                    try {
                        return new GrupoDeDatos(crearArchivos(dir.getPathPrincipal()), dir);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e); // Todo: Ver que pasa con los archivos ya creados si tira error
                    }
                })
                .toList();
    }

    private static List<Archivo> crearArchivos(Path path)
            throws FileNotFoundException {

        File[] files = path.toFile().listFiles();
        if (files == null) return List.of();
        files = Arrays.stream(files) // Ignorar Accesos directos
                .filter(file -> !Files.isSymbolicLink(file.toPath())).toArray(File[]::new);

        List<Archivo> resultado = new ArrayList<>();

        for (File file : files) {

            if (Files.isDirectory(file.toPath(), LinkOption.NOFOLLOW_LINKS)){ // Ignorar Junctions
                Carpeta carpeta = new Carpeta();
                carpeta.setNombre(file.getName());

                carpeta.setArchivos(crearArchivos(
                        file.toPath()
                ));
                resultado.add(carpeta);

            } else {
                ArchivoFinal af = new ArchivoFinal();
                af.setNombre(file.getName());

                try {
                    objectStore.storeArchivoFinal(af, file.toPath()); //Setea hash, size y guarda los binarios asociados al hash
                } catch (IOException e) {
                    throw new RuntimeException(e); // TODO: Ver que pasa con los archivos ya creados si tira error
                }

                resultado.add(af);
            }
        }

        return resultado;
    }

}
