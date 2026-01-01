package domain.Archivos.checkpoint;

import domain.Archivos.ObjectStore;
import jakarta.persistence.*;
import lombok.*;
import lprtt.ApplicationContext;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ArchivoFinal extends Archivo{
    @Column(nullable = false, length = 64)
    private String hash; //SHA 256
    private long size;


    @Override
    public List<ArchivoFinal> obtenerArchivosRecursivo() {
        return List.of(this);
    }

    @Override
    public String toString() {
        return " (Archivo " + nombre + " - " + hash.substring(0, 20) + ") ";
    }
}
