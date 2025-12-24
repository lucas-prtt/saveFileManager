package domain.Juegos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dto.CheckpointDTO;
import dto.PartidaDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Entity
@NoArgsConstructor
public class Partida {

    String titulo;
    @Id
            @Setter
    String id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Checkpoint> checkpoints = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="juego_titulo")
            @Setter
    Juego juego;

    public Partida(String titulo, Juego juego) {
        this.titulo = titulo;
        generateNewId();
        this.juego = juego;
    }


    public Checkpoint crearCheckpoint(){
        return crearCheckpoint(null);
    };
    public Checkpoint crearCheckpoint(String nombre){
        Checkpoint chk = new Checkpoint(nombre, this);
        checkpoints.add(chk);
        return chk;

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
    @JsonIgnore
    public List<CheckpointDTO> getCheckpointsDTO(){
        return checkpoints.stream().map(Checkpoint::toCheckpointDTO).toList();
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
    public void patchWithDto(PartidaDTO patch){
        if (!patch.getTituloPartida().isEmpty())
            titulo = patch.getTituloPartida();
    }

    public PartidaDTO toPartidaDTO(){
        return new PartidaDTO(juego.getTitulo(), id, titulo, getAllCheckpointsId());
    }
}
