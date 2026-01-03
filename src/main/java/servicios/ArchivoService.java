package servicios;
import com.github.luben.zstd.Zstd;
import domain.Archivos.ObjectStore;
import domain.Archivos.checkpoint.*;
import domain.Archivos.juego.Directorio;
import domain.Exceptions.ArchivoYaExisteException;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        //System.out.println("cargarArchivo(" + archivo.getNombre() + ", " + path + ")");
        directorySecurity.validarRuta(path);
        if(archivo instanceof Carpeta carpeta){
            purgar(path.resolve(carpeta.getNombre()), carpeta.getArchivos());
            //System.out.println("!Files.exists(" + path.resolve(carpeta.getNombre()) + ")");
            if(!Files.exists(path.resolve(carpeta.getNombre()))){

                try {
                    //System.out.println("Files.createDirectories(" + path.resolve(carpeta.getNombre()) + ")");
                    Files.createDirectories(path.resolve(carpeta.getNombre()));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //System.out.println("carpeta.getArchivos.forEach(");
            //carpeta.getArchivos().forEach(a -> System.out.println("cargarArchivo(" + a + ", "  + path.resolve(carpeta.getNombre()) + ")"));
            //System.out.println(")");
            carpeta.getArchivos().forEach( archivoInterno -> cargarArchivo(archivoInterno, path.resolve(carpeta.getNombre())));
        }else if (archivo instanceof ArchivoFinal archivoFinal){
            try {

                //System.out.println("!ObjectStore.esArchivoIgual("+path.resolve(archivo.getNombre()).toFile().toString() + ", " +  archivo.getNombre() + ")" );
                if(!ObjectStore.esArchivoIgual(path.resolve(archivo.getNombre()).toFile(), archivo)){
                    //System.out.println("objectStore.loadArchivoFinal("+archivoFinal.getNombre() + ", " +  path + ")");
                    objectStore.loadArchivoFinal(archivoFinal, path);
                    System.out.println(" + Se agrego el archivo " + path.resolve(archivo.getNombre()).toAbsolutePath().normalize());
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
        File file = path.toFile();
        List<Archivo> resultado = new ArrayList<>();

            if (Files.isDirectory(file.toPath(), LinkOption.NOFOLLOW_LINKS)){ // Ignorar Junctions
                File[] files = path.toFile().listFiles();
                if (files == null) return List.of();
                files = Arrays.stream(files) // Ignorar Accesos directos
                        .filter(file2 -> !Files.isSymbolicLink(file2.toPath())).toArray(File[]::new);
                for (File file2 : files) {
                    if(Files.isDirectory(file2.toPath(), LinkOption.NOFOLLOW_LINKS)){
                        Carpeta carpeta = new Carpeta();
                        carpeta.setNombre(file2.getName());
                        carpeta.setArchivos(crearArchivos(file.toPath().resolve(file2.getName())));
                        resultado.add(carpeta);
                    }else{
                        resultado.add(crearArchivos(file2.toPath()).getFirst());
                    }
                }

            } else {

                ArchivoFinal archivoFinal = Tx.run(()->{
                    ArchivoFinal af = new ArchivoFinal();
                    af.setNombre(file.getName());
                    try {
                        objectStore.storeArchivoFinal(af, file.toPath()); //Setea hash, size y guarda los binarios asociados al hash
                        archivoRepository.save(af.getBinario()); // Registra el binario en la BD
                        // Si hay error al escribir el archivo en el FS, no se registra en la BD
                        // Suponemos que si se escribe en el FS, se podra escribir en la BD
                        // La transaccion se realiza a nivel archivo para evitar guardar muchos archivos en el FS que no queden en la BD
                    } catch (ArchivoYaExisteException e){
                        af.setBinario(archivoRepository.findByHash(e.getHash()).orElseThrow(() -> new RuntimeException("El fileSystem esta desincronizado con la base de datos de archivos")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return af;
                });

                resultado.add(archivoFinal);
            }

        return resultado;
    }
    public void eliminarArchivosHuerfanos(){
        List<Binario> binarios = archivoRepository.obtenerHuerfanos();
        for (Binario binario : binarios){
            try{
                System.out.println("Borrando binario " + binario.getHash() + " con " + binario.getUsos() + " usos");
                Tx.runVoid(()->{
                        objectStore.delete(binario.getHash());
                        archivoRepository.remove(binario);
                });
            }catch (Exception ex){
                System.out.println("No se pudo borrar el binario " + binario.getHash() + ": " + ex.getMessage());
            }
        }
    }

    public GrupoDeDatos merge(GrupoDeDatos grupoDeDatos) {
        return archivoRepository.merge(grupoDeDatos);
    }

    public Binario merge(Binario binario) {
        return archivoRepository.merge(binario);
    }

    public void eliminarDatosDeDirectorio(Directorio d) {
        archivoRepository.eliminarDatosDeDirectorio(d);
        eliminarArchivosHuerfanos();
    }

    public void exportarArchivoZip(Archivo archivo, String rutaBase, ZipOutputStream zos) throws IOException {

        if (archivo instanceof Carpeta carpeta) {
            String rutaCarpeta = rutaBase.isEmpty()
                    ? carpeta.getNombre()
                    : rutaBase + "/" + carpeta.getNombre();
            rutaCarpeta = rutaCarpeta.replace("\\", "/");
            zos.putNextEntry(new ZipEntry(rutaCarpeta + "/"));
            zos.closeEntry();
            for (Archivo hijo : carpeta.getArchivos()) {
                exportarArchivoZip(hijo, rutaCarpeta, zos);
            }
        } else if (archivo instanceof ArchivoFinal af) {
            String rutaArchivo = rutaBase.isEmpty()
                    ? af.getNombre()
                    : rutaBase + "/" + af.getNombre();
            rutaArchivo = rutaArchivo.replace("\\", "/");
            zos.putNextEntry(new ZipEntry(rutaArchivo));
            byte[] raw = objectStore.loadRaw(af.getBinario());
            zos.write(raw);
            zos.closeEntry();
        }
    }

}
