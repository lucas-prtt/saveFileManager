package domain.Juegos.CheckpointStrategys;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.Carpeta;
import domain.Juegos.Checkpoint;
import jakarta.persistence.*;

import java.util.List;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RandomChanceCheckpointStrategy.class, name = "rand"),
        @JsonSubTypes.Type(value = FIFOMaxCheckpointStrategy.class, name = "fifo"),
        @JsonSubTypes.Type(value = SaveAllCheckpointsStrategy.class, name = "safe")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class CheckpointStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract List<Checkpoint> checkpointsABorrar(List<Checkpoint> checkpoints);
}
