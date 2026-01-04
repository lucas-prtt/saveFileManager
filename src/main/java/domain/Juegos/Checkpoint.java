package domain.Juegos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import domain.Archivos.checkpoint.GrupoDeDatos;
import jakarta.persistence.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Checkpoint {
    @Getter
    LocalDateTime fechaDeCreacion;
    @ManyToOne
    @JoinColumn
    @Setter
    @Getter
    @JsonIgnore
    Partida partida;
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Getter
    @Setter
    @Column(length = 16383)
    String descripcion;
    @Getter
    @Setter
            @Column(length = 255)
    String nombre;
    @Getter @Setter @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<GrupoDeDatos> archivos = new ArrayList<>();

    public Checkpoint(){
        this.fechaDeCreacion = LocalDateTime.now();
    }
    public Checkpoint(String nombre, Partida partida){
        this.nombre = nombre;
        this.fechaDeCreacion = LocalDateTime.now();
        this.partida = partida;
    }
    public Checkpoint(String nombre, String descripcion, Partida partida){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaDeCreacion = LocalDateTime.now();
        this.partida = partida;
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
            return fechaDeCreacion.toString() + " - " + nombre;
    }

    public String toString(){
        return String.format(" (Checkpoint) { fechaDeCreacion:%s, id: %s } ", fechaDeCreacion, id);
    }

    public String nombreLabelText(){
        return nombre != null ? nombre : "";
    }
    public String fechaLabelText(){
        return fechaDeCreacion != null ? fechaDeCreacion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
    }
    public String descripcionLabelText(){
        return descripcion != null ? descripcion : "Sin descripción";
    }

}
