package Archivos;

import jakarta.persistence.*;

import java.io.File;
import java.util.concurrent.ExecutionException;
@Entity
abstract class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nombre;
    abstract void escribirEn(String path);
    abstract void cargarArchivoDe(String path);
    public static Archivo FileToArchivo(File file){
        if (file.isFile())
            return new ArchivoFinal(file.getName());
        else if(file.isDirectory())
            return new Carpeta(file.getName());
        else{
            throw new RuntimeException("Problema al leer el archivo");
        }
    }
}
