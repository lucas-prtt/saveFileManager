package domain.Archivos.checkpoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;
    protected String nombre;
}
