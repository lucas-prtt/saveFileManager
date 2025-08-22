package FileManager;

import Archivos.Archivo;
import Archivos.ArchivoFinal;
import Archivos.Carpeta;
import Archivos.Directorio;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import lombok.ToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileManager {

    public static void cargarArchivos(JuegoDTO juego, List<Archivo> archivos){
        System.out.println("Se escribirian los archivos en:");
        archivos.forEach(System.out::println);
        //TODO
        return;
    }

    public static List<Archivo> guardarArchivos(JuegoDTO juego){
    System.out.println("Leo los archivos de:");
    juego.getSaveFilePaths().forEach(directorio->System.out.println(directorio.getPathPrincipal()));
        return juego.getSaveFilePaths().stream().flatMap(dir -> {try {return crearArchivos(dir).stream();} catch (FileNotFoundException e) {throw new RuntimeException("No se pudo leer el directorio: " + dir.getPathPrincipal(), e);}}).toList();
    }
    private static List<Archivo> crearArchivos(Directorio directorio) throws FileNotFoundException {
        List<File> files = List.of(Objects.requireNonNull(directorio.findBestMatchingPath().toFile().listFiles()));
        if (files.isEmpty()){
            return new ArrayList<>();
        }
        List<Archivo> archs = new ArrayList<>();
        for(File file : files){
            Archivo arch = file.isDirectory() ? new Carpeta(file.getName()) : new ArchivoFinal(file.getName());
            arch.setUbicacion(directorio);
            arch.cargarArchivoDe(directorio.findBestMatchingPath());
            archs.add(arch);
        }
        return archs;
    }

}
