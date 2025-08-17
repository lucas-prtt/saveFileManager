package Juegos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CheckpointDTO {
    private final String descripcion;
    private final String ID;
    private final LocalDateTime fechaCreacion;
    public CheckpointDTO(String ID, LocalDateTime fechaCreacion, String descripcion){
        this.fechaCreacion = fechaCreacion;
        this.ID = ID;
        this.descripcion = descripcion;
    }
}
