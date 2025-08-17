package Juegos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Checkpoint {
    @Getter
    LocalDateTime fechaDeCreacion;
    @ManyToOne
            @JoinColumn
    Partida partida;
    @Id
    @Getter
    String id;
    @Getter
    String descripcion;
    public Checkpoint(){
        this.fechaDeCreacion = LocalDateTime.now();
    }
    public Checkpoint(String descripcion, Partida partida){
        this.descripcion = descripcion;
        this.fechaDeCreacion = LocalDateTime.now();
        this.partida = partida;
        this.id = UUID.randomUUID().toString();
    }

    public String getStringReferencia(){
        if(descripcion == null)
            return fechaDeCreacion.toString();
        else
            return fechaDeCreacion.toString() + " - " + descripcion;
    }
    public CheckpointDTO toDTO(){
        return new CheckpointDTO(id, fechaDeCreacion, descripcion);
    }
}
