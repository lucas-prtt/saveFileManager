package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointDTO {
    String id;
    String descripcion;
    LocalDateTime fechaDeCreacion;
    String tituloPartida;
    String tituloJuego;



    public void generateNewId(){
        id = UUID.randomUUID().toString();
    }
    @Override
    public String toString(){
        return fechaDeCreacion.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + " - " + fechaDeCreacion.toLocalTime().format(DateTimeFormatter.ISO_TIME) + " -  " + (descripcion != null ? descripcion : "");
    }
}
