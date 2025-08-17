package Archivos;

import jakarta.persistence.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ArchivoFinal extends Archivo{
    @Lob
    @Column(name = "datos")//Se le puede poner length = cantidadDeBytesMaxima
    private byte[] datos;

    public ArchivoFinal(String nombre){
        this.nombre = nombre;
    }
    @Override
    public void escribirEn(Path path) {
        File archivo = path.resolve(nombre).toFile();
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            fos.write(datos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo: " + archivo.getAbsolutePath(), e);
        }
    }

    @Override
    void cargarArchivoDe(Path path) {
        try {
            Path archivo = path.resolve(nombre);
            datos = Files.readAllBytes(archivo);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo: " + path.resolve(nombre), e);
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
}
