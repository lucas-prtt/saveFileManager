package Archivos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
@Entity
public abstract class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    protected String id; // Iria uuid?
    protected String nombre;
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "directorio_id")
    protected Directorio ubicacion;
    abstract void escribirEn(Path path);
    abstract void cargarArchivoDe(Path path) throws Exception;
    String tree(){
        return treeAux(0);
    };
    abstract String treeAux(int nivel);
    public static Archivo FileToArchivo(File file){
        if (file.isFile())
            return new ArchivoFinal(file.getName());
        else if(file.isDirectory())
            return new Carpeta(file.getName());
        else{
            throw new RuntimeException("Problema al leer el archivo");
        }
    }
    abstract void borrar(Path path) throws Exception;
}
