package domain.Juegos;

import domain.Archivos.Archivo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Checkpoint {
    @Getter
    LocalDateTime fechaDeCreacion;
    @ManyToOne
    @JoinColumn
    @Setter
    @JsonIgnore
    Partida partida;
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Getter
    String descripcion;
    @Getter @Setter @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Archivo> archivos = new ArrayList<>();

    public Checkpoint(){
        this.fechaDeCreacion = LocalDateTime.now();
    }
    public Checkpoint(String descripcion, Partida partida){
        this.descripcion = descripcion;
        this.fechaDeCreacion = LocalDateTime.now();
        this.partida = partida;
        generateNewId();
    }
    public Checkpoint(String descripcion, Partida partida, String id){
        this.descripcion = descripcion;
        this.fechaDeCreacion = LocalDateTime.now();
        this.partida = partida;
        this.id = id;
    }


    public String getStringReferencia(){
        if(descripcion == null)
            return fechaDeCreacion.toString();
        else
            return fechaDeCreacion.toString() + " - " + descripcion;
    }
    public void generateNewId(){
        id = UUID.randomUUID().toString();
    }
}
