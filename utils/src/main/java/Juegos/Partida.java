package Juegos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.*;

@Getter
@Entity
@NoArgsConstructor
public class Partida {
    String titulo;
    @Id
            @Setter
    String id;
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Checkpoint> checkpoints = new ArrayList<>();
    @ManyToOne
    @JoinColumn
            @Setter
            @JsonIgnore
    Juego juego;

    public Partida(String titulo, Juego juego) {
        this.titulo = titulo;
        generateNewId();
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

    public void eliminarCheckpointByIndex(int index){
        checkpoints.remove(index);
    }
    public void eliminarCheckpoint(Checkpoint chk){
        checkpoints.remove(chk);
    }
    public List<CheckpointDTO> getCheckpointsDTO(){
        return checkpoints.stream().map(Checkpoint::toDTO).toList();
    }
    public List<String> getAllCheckpointsId(){
        return checkpoints.stream().map(Checkpoint::getId).toList();
    }
    public Optional<Checkpoint> getCheckpointById(String id){
        return checkpoints.stream().filter(chk ->{return Objects.equals(chk.getId(), id);
        }).findFirst();
    }
    public void agregarCheckpoint(Checkpoint checkpoint){
        checkpoints.add(checkpoint);
        return;
    }
    public void generateNewId(){
        id = UUID.randomUUID().toString();
    }
}
