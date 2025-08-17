package Juegos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@Entity
@NoArgsConstructor
public class Partida {
    String titulo;
    @Id
    String Id;
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
    List<Checkpoint> checkpoints;
    @ManyToOne
    @JoinColumn
    Juego juego;

    public Partida(String titulo, Juego juego) {
        this.titulo = titulo;
        this.checkpoints = new ArrayList<>();
        this.Id = UUID.randomUUID().toString();
        this.juego = juego;
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
    public void agregarCheckpoint(Checkpoint checkpoint){
        checkpoints.add(checkpoint);
        return;
    }
}
