package servicios;
import domain.Archivos.ObjectStore;
import domain.Archivos.checkpoint.*;
import domain.Juegos.Juego;
import repositorios.ArchivoRepository;
import ui.MainController;
import utils.Tx;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;

public class ArchivoService {
    private final DirectorySecurity directorySecurity;
    private final ObjectStore objectStore;
    private final ArchivoRepository archivoRepository;

    public ArchivoService(DirectorySecurity directorySecurity, ObjectStore objectStore, ArchivoRepository archivoRepository) {
        this.directorySecurity = directorySecurity;
        this.objectStore = objectStore;
        this.archivoRepository = archivoRepository;
    }


    public  Map<GrupoDeDatos, Path> hallarPaths(List<GrupoDeDatos> grupoDeDatosList){
        System.out.println("Se cargaran los archivos en:");
        Map<GrupoDeDatos, Path> caminosEncontrados = new HashMap<>();

        for (GrupoDeDatos grupoDeDatos : grupoDeDatosList){
            try {
                Path path = grupoDeDatos.getDirectorio().getPathPrincipal(); // TODO: Predecir path de manera inteligente, lidiar con sinonimos
                System.out.println(path);
                caminosEncontrados.put(grupoDeDatos, path);
            }catch (Exception e){
            }
        }
        System.out.println("----------------------------------");
        return caminosEncontrados;
    }

    public  void cargarArchivos(Map<GrupoDeDatos, Path> archivosACargar){
        archivosACargar.forEach((datos, path) -> {
            directorySecurity.validarRuta(path);
            purgar(path, datos.getArchivos());
            datos.getArchivos().forEach(archivo -> cargarArchivo(archivo, path));
        });
    }

    public  void cargarArchivo(Archivo archivo, Path path){
        directorySecurity.validarRuta(path);
        if(archivo instanceof Carpeta carpeta){
            purgar(path.resolve(carpeta.getNombre()), carpeta.getArchivos());
            if(!Files.exists(path.resolve(carpeta.getNombre()))){
                try {
                    Files.createDirectories(path.resolve(carpeta.getNombre()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            carpeta.getArchivos().forEach( archivoInterno -> cargarArchivo(archivoInterno, path.resolve(carpeta.getNombre())));
        }else if (archivo instanceof ArchivoFinal archivoFinal){
            try {
                if(!ObjectStore.esArchivoIgual(path.resolve(archivo.getNombre()).toFile(), archivo)){
                    objectStore.loadArchivoFinal(archivoFinal, path);
                    System.out.println(" + Se agrego el archivo " + path.resolve(archivo.getNombre()).toAbsolutePath().normalize().toAbsolutePath().normalize() );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void purgar(Path carpeta, List<Archivo> archivos) {

        directorySecurity.validarRuta(carpeta);
        if(Files.isDirectory(carpeta)){
            try {
                Files.list(carpeta).map(Path::toFile).toList().forEach(file ->
                    {
                        if(archivos.stream().noneMatch(archivo -> ObjectStore.esArchivoIgual(file, archivo))){
                            directorySecurity.validarRuta(Path.of(file.getAbsolutePath()));
                            if(!file.delete()) {
                                throw new RuntimeException("No se pudo borrar " + file.toString());
                            }
                            System.out.println(" - Se elimino el archivo " +   file.getAbsolutePath());
                        }
                    }
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public  List<GrupoDeDatos> guardarArchivos(Juego juego) {
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

    private  List<Archivo> crearArchivos(Path path)
            throws FileNotFoundException {
        directorySecurity.validarRuta(path);

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

    public void eliminarArchivosHuerfanos(){
        Tx.runVoid(() -> {
            Set<String> archivosUsados = archivoRepository.obtenerHashArchivosUsados();
            System.out.println("Usados: ("+archivosUsados.size()+")");

            Set<String> archivosPersistidos = objectStore.obtenerHashesArchivosPersistidos();
            System.out.println("Persistidos: ("+archivosPersistidos.size()+")");
            Set<String> hashesHuerfanos = new HashSet<>(archivosPersistidos);
            hashesHuerfanos.removeAll(archivosUsados);
            System.out.println("Huerfanos: ("+hashesHuerfanos.size()+")");

            hashesHuerfanos.forEach(objectStore::delete);
        });
    }

}
