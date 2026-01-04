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

    @Override
    public String nombre() {
        return "Sin rotación";
    }

    @Override
    public String descripcion() {
        return "No se eliminan nunca los checkpoints. Requiere eliminación manual para no acumular espacio indefinidamente";
    }
}
