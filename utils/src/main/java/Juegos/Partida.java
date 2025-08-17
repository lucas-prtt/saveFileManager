package Juegos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.*;

@Getter
@Entity
public class Partida {
    String titulo;
    @Id
    String Id;
    List<Checkpoint> checkpoints;

    public Partida(String titulo) {
        this.titulo = titulo;
        this.checkpoints = new ArrayList<>();
        this.Id = UUID.randomUUID().toString();
    }


    public void crearCheckpoint(){
        crearCheckpoint(null);
    };
    public void crearCheckpoint(String nombre){
        checkpoints.add(new Checkpoint(nombre, this ));
        //TODO
    }
    public void cargarUltimoCheckpoint(){
        cargarCheckpoint(checkpoints.getLast());
    }
    public void cargarCheckpoint(Checkpoint checkpoint){
        //TODO
    }

    public void eliminarCheckpoint(int index){
        checkpoints.remove(index);
    }
    public List<CheckpointDTO> getCheckpointsDTO(){
        return checkpoints.stream().map(Checkpoint::toDTO).toList();
    }
    public Optional<Checkpoint> getCheckpointById(String id){
        return checkpoints.stream().filter(chk ->{return Objects.equals(chk.getId(), id);
        }).findFirst();
    }
}
