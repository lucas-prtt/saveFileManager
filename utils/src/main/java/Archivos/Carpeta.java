package Archivos;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.File;
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
    public void escribirEn(String path) {
        archivos.forEach(a->a.escribirEn(path+"/"+nombre));
    }

    @Override
    void cargarArchivoDe(String path) {
        File directorio = new File(path+"/"+nombre);
        File[] subArchivos = directorio.listFiles();
        if (subArchivos == null) return;
        for (File file : subArchivos){
            Archivo arch = Archivo.FileToArchivo(file);
            arch.cargarArchivoDe(path+"/"+nombre);
            archivos.add(arch);
        }
    }
}
