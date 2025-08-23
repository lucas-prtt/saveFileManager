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
import java.nio.file.Path;
import java.util.*;

public class FileManager {

    public static void cargarArchivos(JuegoDTO juego, List<Archivo> archivos){
        System.out.println("Se cargan los archivos en:");
        List<AbstractMap.SimpleEntry<Path, Archivo>> caminosEncontrados = new ArrayList<>();

        for (Archivo f : archivos){
            try {
                Path pathEncontrado = f.getUbicacion().findMostLikelyPath();
                System.out.println(f.getNombre() + " se cargara en " + pathEncontrado.toString());
                caminosEncontrados.add(new AbstractMap.SimpleEntry<>(pathEncontrado, f));
            }catch (Exception e){
                System.out.println(f.getUbicacion().getPathPrincipal() + "no fue ubicado. No se puede cargar el archivo " + f.getNombre());
            }
        }

        System.out.print("¿Deseás continuar? \n 1. Si\n2. No\n ");
        while (true) {
            try {
                int respuesta = new Scanner(System.in).nextInt();
                if (respuesta == 2) {
                    System.out.println("Carga de archivos cancelada");
                    return;
                } else if (respuesta == 1) {
                    System.out.println("Continuando");
                    break;
                }
            }catch (Exception _){}
        }

        for(AbstractMap.SimpleEntry<Path, Archivo> dupla : caminosEncontrados){
        dupla.getValue().escribirEn(dupla.getKey());}
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
