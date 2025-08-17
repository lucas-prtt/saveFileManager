package Archivos;

import jakarta.persistence.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArchivoFinal extends Archivo{
    @Lob
    @Column(name = "datos")//Se le puede poner length = cantidadDeBytesMaxima
    private byte[] datos;

    public ArchivoFinal(String nombre){
        this.nombre = nombre;
    }
    @Override
    public void escribirEn(String path) {
        File archivo = new File(path+"/"+nombre);
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            fos.write(datos);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo: " + archivo.getAbsolutePath(), e);
        }
    }

    @Override
    void cargarArchivoDe(String path) {
        try {
            datos = Files.readAllBytes(Paths.get(path+"/"+nombre));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo:" + path + "/" + nombre);
        }
    }
}
