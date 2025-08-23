package Archivos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ArchivoFinal extends Archivo{
    @Lob
    @Column(name = "datos")//Se le puede poner length = cantidadDeBytesMaxima
    private byte[] datos;

    public ArchivoFinal(String nombre){
        this.nombre = nombre;
    }

    @Override
    public void escribirEn(Path path) {
        if(datos == null){
            throw new RuntimeException("Se quiso escribir un archivo (" + nombre + ") sin cargar los datos");
        }
        File archivo = path.resolve(nombre).toFile();
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            fos.write(datos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo: " + archivo.getAbsolutePath(), e);
        }
    }

    @Override
    public void cargarArchivoDe(Path path) throws FileNotFoundException {
        try {
            Path archivo = path.resolve(nombre);
            datos = Files.readAllBytes(archivo);
        } catch (IOException e) {
            throw new FileNotFoundException("No se pudo leer el archivo: " + path.resolve(nombre));
        }
    }


    @Override
    String treeAux(int nivel) {
        return " ".repeat(nivel)+nombre+"\n";
    }

    @Override
    public void borrar(Path path) throws Exception {
        if(Objects.equals(nombre, "") || nombre == null){
            throw new Exception("Se quiso borrar sin asignar el nombre al archivo");
        }
        if (!path.resolve(nombre).toFile().delete()) {
            throw new RuntimeException("No se pudo eliminar el archivo o carpeta: \"" + path.resolve(nombre) + "\"");
        }
    }

    @Override
    public String subTypeAsString() {
        return "Archivo";
    }

    public String getSHA512() {
        if(datos == null){
            throw new RuntimeException("Se quiso calcular el checksum de un archivo (" + nombre + ") sin cargar los datos");
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md.digest(datos);

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
    public String toString(){
        return "Archivo: " + nombre + "\tUbicacion: " + ubicacion.getPathPrincipal();
    }


}
