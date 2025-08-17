package Archivos;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Carpeta extends Archivo{
    @OneToMany(cascade = CascadeType.ALL)
    List<Archivo> archivos = new ArrayList<>();

    public Carpeta(String nombre){
        this.nombre = nombre;
    }
    @Override
    public void escribirEn(Path path) {
        try {
            Files.createDirectories(path.resolve(nombre));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path ruta = path.resolve(nombre);
        archivos.forEach(a->a.escribirEn(ruta));
    }

    @Override
    void cargarArchivoDe(Path path) throws Exception {
        Path directorio = path.resolve(nombre);
        File[] subArchivos = directorio.toFile().listFiles();
        if (subArchivos == null)
        {
            throw new FileNotFoundException("No se encontró la carpeta \""+path.resolve(nombre)+"\"");
        }

        for (File file : subArchivos){
            Archivo arch = Archivo.FileToArchivo(file);
            arch.cargarArchivoDe(path.resolve(nombre));
            archivos.add(arch);
        }
    }
    @Override
    protected String treeAux(int nivel){
        String respuesta = " ".repeat(nivel);
        respuesta = respuesta + nombre + "\n";
        for(Archivo arch : archivos){
            respuesta = respuesta + " ".repeat(nivel) + arch.treeAux(nivel + 1);
        }
        return respuesta;
    }

}
