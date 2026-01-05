package domain.Archivos;

import com.github.luben.zstd.Zstd;
import domain.Archivos.checkpoint.Archivo;
import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.Binario;
import domain.Archivos.checkpoint.Carpeta;
import domain.Exceptions.ArchivoYaExisteException;
import domain.Exceptions.BinarioNoEncontradoException;
import servicios.DirectorySecurity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectStore {
    // Lugar donde se guardan los binarios
    private final Path objectRoot;
    private final DirectorySecurity directorySecurity;
    public ObjectStore(Path objectRoot, DirectorySecurity directorySecurity) {
        this.objectRoot = objectRoot;
        this.directorySecurity = directorySecurity;
    }

    public void delete(String hash){
        Path path = pathFromHash(hash);
        path.toFile().delete();
    }

    public void storeArchivoFinal(ArchivoFinal archivo, Path path) throws IOException, ArchivoYaExisteException {

        byte[] raw = Files.readAllBytes(path);

        String hash = sha256(raw);
        Path objPath = pathFromHash(hash);
        if (Files.exists(objPath)) {
            System.out.println("Ya existia " + path.resolve(archivo.getNombre()).toAbsolutePath().normalize() + " -> " + hash);
            throw new ArchivoYaExisteException(hash);
        }
        System.out.println("Se persistio " + path.resolve(archivo.getNombre()).toAbsolutePath().normalize() + " -> " + hash);
        archivo.setBinario(new Binario(hash, raw.length));

        byte[] compressed = Zstd.compress(raw, 3);

        Files.createDirectories(objPath.getParent());

        Files.write(objPath, compressed, StandardOpenOption.CREATE_NEW);

    }
    /**
     *  Carga el archivo final en la ubicacion especificada
     *  El Path de destino no incluye el nombre del archivo. Este se asume del nombre en ArchivoFinal
     */

    public void loadArchivoFinal(ArchivoFinal archivo, Path destino) throws IOException {

        Path objPath = pathFromHash(archivo.getBinario().getHash());
        Path destinoFinal = destino.resolve(archivo.getNombre());
        directorySecurity.validarRuta(destino);
        if (!Files.exists(objPath)) {
            throw new BinarioNoEncontradoException(archivo);
        }

        byte[] compressed = Files.readAllBytes(objPath);

        byte[] raw = Zstd.decompress(compressed, (int) archivo.getBinario().getSize());

        Files.createDirectories(destinoFinal.getParent());

        Files.write(destinoFinal, raw, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    }

    private Path pathFromHash(String hash) {
        String dir = hash.substring(0, 1);
        return objectRoot.resolve(dir).resolve(hash + ".zst");
    }
    private static String sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String sha256(Path path) throws IOException {
        byte[] raw = Files.readAllBytes(path);
        return sha256(raw);
    }
    public static boolean esArchivoIgual(File file, Archivo archivo){

        /* try {
            System.out.println("Comparando " + file.getName() + "( " +(file.isDirectory() ? "Carpeta" :  sha256(file.toPath())) + " ) vs " + archivo.getNombre() + " ( " + (archivo instanceof ArchivoFinal archivoFinal? archivoFinal.getHash() : "Carpeta") + " ) ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        if(!file.exists())
            return false;
        if(archivo instanceof Carpeta carpeta){
            return carpeta.getNombre().equals(file.getName());
        }
        if(archivo instanceof ArchivoFinal archivoFinal)
            try {
                if (!file.getName().equals(archivoFinal.getNombre())) {
                    return false;
                }
                if (file.length() != archivoFinal.getBinario().getSize()) {
                    return false;
                }
                byte[] data = Files.readAllBytes(file.toPath());
                return Objects.equals(archivoFinal.getBinario().getHash(), sha256(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return true;
    }

    public Set<String> obtenerHashesArchivosPersistidos() {
        File carpetaObjetos = objectRoot.toFile();
        File[] subCarpetas = carpetaObjetos.listFiles(File::isDirectory);
        if (subCarpetas == null) {
            return Set.of();
        }

        return Arrays.stream(subCarpetas).flatMap(subCarpeta ->
                {File[] archivos = subCarpeta.listFiles(File::isFile);
                    return archivos == null
                            ? Stream.empty()
                            : Arrays.stream(archivos);
                })
                .map(File::getName).map(fileName -> fileName.replaceFirst("\\.zst$", ""))
                .collect(Collectors.toSet());
    }

    public byte[] loadRaw(Binario binario) throws IOException {


        Path objPath = pathFromHash(binario.getHash());
        if (!Files.exists(objPath)) {
            throw new BinarioNoEncontradoException(binario);
        }

        byte[] compressed = Files.readAllBytes(objPath);

        byte[] raw = Zstd.decompress(compressed, (int) binario.getSize());
        return raw;
    }
}