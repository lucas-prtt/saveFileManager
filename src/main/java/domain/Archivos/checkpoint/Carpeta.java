package domain.Archivos.checkpoint;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Carpeta extends Archivo{
    @OneToMany(cascade = CascadeType.ALL)
    List<Archivo> archivos = new ArrayList<>();
    public List<ArchivoFinal> obtenerArchivosRecursivo(){
        return archivos.stream().flatMap( a -> a.obtenerArchivosRecursivo().stream()).toList();
    }
    @Override
    public String toString() {
        return " (Carpeta " + nombre + " - " + archivos.size() + " elementos ) ";
    }
}
