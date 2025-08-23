package Archivos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Carpeta extends Archivo{
    @OneToMany(cascade = CascadeType.ALL) // TODO: Que sea una relacion ManyToMany, donde solo se persistan los archivos con checksum distinto a los de la BD
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
    public void cargarArchivoDe(Path path) throws FileNotFoundException {
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
    @Override
    public void borrar(Path path) throws Exception {
        if (Objects.equals(nombre, "") || nombre == null) {
            throw new Exception("Se quiso borrar sin asignar el nombre a la carpeta");
        }

        Path carpeta = path.resolve(nombre);

        borrarRecursivo(carpeta);
    }

    @Override
    public String subTypeAsString() {
        return "Carpeta";
    }

    private void borrarRecursivo(Path path) {
        File archivo = path.toFile();

        if (archivo.isDirectory()) {
            File[] contenidos = archivo.listFiles();
            if (contenidos != null) {
                for (File f : contenidos) {
                    borrarRecursivo(f.toPath());
                }
            }
        }

        if (!archivo.delete()) {
            throw new RuntimeException("No se pudo eliminar: " + path.toAbsolutePath());
        }
    }
    @Override
    public String toString(){
        return "Carpeta: " + nombre + "\tUbicacion: " + ubicacion.getPathPrincipal();
    }
}
