package domain.Archivos;

import com.github.luben.zstd.Zstd;
import domain.Archivos.checkpoint.Archivo;
import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.Carpeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Objects;

public class ObjectStore {
    // Lugar donde se guardan los binarios
    private final Path objectRoot;

    public ObjectStore(Path objectRoot) {
        this.objectRoot = objectRoot;
    }

    public void storeArchivoFinal(ArchivoFinal archivo, Path path) throws IOException {

        byte[] raw = Files.readAllBytes(path);

        String hash = sha256(raw);

        archivo.setHash(hash);
        archivo.setSize(raw.length);

        Path objPath = pathFromHash(hash);

        if (Files.exists(objPath)) {
            return;
        }

        byte[] compressed = Zstd.compress(raw, 3);

        Files.createDirectories(objPath.getParent());

        Files.write(objPath, compressed, StandardOpenOption.CREATE_NEW);
    }
    /**
     *  Carga el archivo final en la ubicacion especificada
     *  El Path de destino no incluye el nombre del archivo. Este se asume del nombre en ArchivoFinal
     */

    public void loadArchivoFinal(ArchivoFinal archivo, Path destino) throws IOException {

        Path objPath = pathFromHash(archivo.getHash());
        Path destinoFinal = destino.resolve(archivo.getNombre());

        if (!Files.exists(objPath)) {
            throw new FileNotFoundException("Objeto no encontrado: " + archivo.getHash());
        }

        byte[] compressed = Files.readAllBytes(objPath);

        byte[] raw = Zstd.decompress(compressed, (int) archivo.getSize());

        Files.createDirectories(destinoFinal.getParent());

        Files.write(destinoFinal, raw, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Path pathFromHash(String hash) {
        String dir = hash.substring(0, 2);
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
    public static boolean esArchivoIgual(File file, Archivo archivo){
        if(archivo instanceof Carpeta carpeta){
            return carpeta.getNombre().equals(file.getName());
        }
        if(archivo instanceof ArchivoFinal archivoFinal)
            try {
                if (!file.getName().equals(archivoFinal.getNombre())) {
                    return false;
                }
                if (file.length() != archivoFinal.getSize()) {
                    return false;
                }
                byte[] data = Files.readAllBytes(file.toPath());
                return Objects.equals(archivoFinal.getHash(), sha256(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return true;
    }
}