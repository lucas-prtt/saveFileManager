package domain.Archivos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Carpeta.class, name = "carpeta"),
        @JsonSubTypes.Type(value = ArchivoFinal.class, name = "archivofinal")
})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id; // Iria uuid?
    protected String nombre;
    @ManyToOne
    @JoinColumn(name = "directorio_id")
    protected Directorio ubicacion;
    public abstract void escribirEn(Path path);
    public abstract void cargarArchivoDe(Path path) throws FileNotFoundException;
    public String tree(){
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
    public abstract String subTypeAsString();
}
