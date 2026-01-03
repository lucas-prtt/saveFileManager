package domain.Juegos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
@Setter
@Getter
@Entity
@NoArgsConstructor
public class Partida {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String titulo;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Checkpoint> checkpoints = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="juego_id")
    @Setter
    Juego juego;

    public Partida(String titulo) {
        this.titulo = titulo;
    }


    public Checkpoint crearCheckpoint(){
        return crearCheckpoint(null);
    };
    public Checkpoint crearCheckpoint(String nombre){
        Checkpoint chk = new Checkpoint(nombre, this);
        checkpoints.add(chk);
        return chk;
    }
    public void cargarCheckpoint(Checkpoint checkpoint){
    }

    public void eliminarCheckpointByIndex(int index){
        checkpoints.remove(index);
    }
    public void eliminarCheckpoint(Checkpoint chk){
        checkpoints.remove(chk);
    }
    @JsonIgnore
    public List<String> getAllCheckpointsId(){
        return checkpoints.stream().map(Checkpoint::getId).toList();
    }
    @JsonIgnore
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
    public void eliminarCheckpointById(String id){
        checkpoints.removeIf(checkpoint -> Objects.equals(checkpoint.getId(), id));
    }
}
