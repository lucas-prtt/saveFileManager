package domain.Archivos.checkpoint;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ArchivoFinal extends Archivo{
    @Column(nullable = false, length = 64)
    private String hash; //SHA 256
    private long size;
}
