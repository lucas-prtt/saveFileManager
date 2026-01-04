package domain.Archivos.checkpoint;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ArchivoFinal extends Archivo{
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    Binario binario;


    @Override
    public List<ArchivoFinal> obtenerArchivosRecursivo() {
        return List.of(this);
    }

    @Override
    public String toString() {
        return " (Archivo " + nombre + " - " + binario + ") ";
    }
    @PreRemove
    private void beforeDelete() {
        binario.reducirUso();
    }

}
