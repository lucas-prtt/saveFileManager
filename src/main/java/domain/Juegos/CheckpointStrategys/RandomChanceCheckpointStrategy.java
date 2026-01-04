package domain.Juegos.CheckpointStrategys;

import domain.Juegos.Checkpoint;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
@Setter
@Getter
@NoArgsConstructor
@Entity
public class RandomChanceCheckpointStrategy extends CheckpointStrategy{
    Integer maxTailSize = 100; //Maximo de checkpoints a tener en cualquier momento
    Integer allCheckpointsTailSize = 10; // Cantidad de checkpoints que no se borran
    Integer maxDeletionAttemptsPerExecution = 5;
    Random random = new Random();

    @Override
    public List<Checkpoint> checkpointsABorrar(List<Checkpoint> checkpoints) {
        Set<Checkpoint> aBorrar = new HashSet<>();

        int total = checkpoints.size();


        int startIndex = allCheckpointsTailSize;
        int exceso = total - maxTailSize;
        int deletionAttempts = 0;
        while (exceso>0 || deletionAttempts < maxDeletionAttemptsPerExecution){
            int deleteNumber = deleteNumber();
            deletionAttempts++;
            if(checkpoints.size() >= deleteNumber) {
                continue;
            }
            aBorrar.add(checkpoints.get(deleteNumber()));
            exceso--;
        }

        return aBorrar.stream().toList();
    }
    private int deleteNumber(){
        return (int) (allCheckpointsTailSize + Math.abs(random.nextGaussian(0, (double) maxTailSize /2)) % (maxTailSize-allCheckpointsTailSize));
        // Da un numero para usar para borrar entre la cantidad minima que debe estar en tod o momento y el max
    }
}