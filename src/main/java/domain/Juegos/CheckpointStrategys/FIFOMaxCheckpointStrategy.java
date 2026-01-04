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

    public FIFOMaxCheckpointStrategy(Integer maxCheckpoints){
        this.maxCheckpoints = maxCheckpoints;
    }
    @Override
    public List<Checkpoint> checkpointsABorrar(List<Checkpoint> checkpoints) {
        return checkpoints.subList(0, checkpoints.size() <= maxCheckpoints ? 0 : checkpoints.size()-maxCheckpoints);    }
}
