package Juegos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
public class Partida {
    @Id
    String titulo;
    List<Checkpoint> checkpoints;

    public Partida(String titulo) {
        this.titulo = titulo;
        this.checkpoints = new ArrayList<>();
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
}
