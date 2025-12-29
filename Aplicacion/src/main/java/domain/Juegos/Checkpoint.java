package domain.Juegos;

import ch.qos.logback.core.pattern.SpacePadder;
import domain.Archivos.Archivo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    List<Archivo> archivos = new ArrayList<>();

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

    public VBox listRepresentation() {
        Label nombreLabel = new Label(
                nombre != null ? nombre : ""
        );
        nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");



        Label fechaLabel = new Label(
                fechaDeCreacion != null
                        ? fechaDeCreacion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : ""
        );
        fechaLabel.setStyle(" -fx-font-size: 14 px;");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox nombreHBox = new HBox(1, fechaLabel, espacio, nombreLabel);
        Label descripcionLabel = new Label(
                descripcion != null ? descripcion : "Sin descripción"
        );
        descripcionLabel.setWrapText(true);
        descripcionLabel.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 11 px;");
        VBox box = new VBox(2, nombreHBox, descripcionLabel);
        box.setPadding(new Insets(5));
        descripcionLabel.maxWidthProperty().bind(box.widthProperty().subtract(20));
        descripcionLabel.setWrapText(true);

        return box;
    }
}
