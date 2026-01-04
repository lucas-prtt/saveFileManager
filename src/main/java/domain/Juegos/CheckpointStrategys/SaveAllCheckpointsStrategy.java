package domain.Juegos.CheckpointStrategys;

import domain.Juegos.Checkpoint;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@Entity
public class SaveAllCheckpointsStrategy extends CheckpointStrategy{

    @Override
    public List<Checkpoint> checkpointsABorrar(List<Checkpoint> checkpoints) {
        return List.of();
    }
}
