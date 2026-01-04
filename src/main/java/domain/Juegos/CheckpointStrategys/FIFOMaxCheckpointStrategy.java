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
public class FIFOMaxCheckpointStrategy extends CheckpointStrategy{
    Integer maxCheckpoints = 10;

    @Override
    public List<Checkpoint> checkpointsABorrar(List<Checkpoint> checkpoints) {
        return checkpoints.subList(checkpoints.size()-maxCheckpoints-1, checkpoints.size()-1);
    }
}
