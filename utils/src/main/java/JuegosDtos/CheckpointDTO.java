package JuegosDtos;

import Juegos.Checkpoint;
import Juegos.Partida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
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
}
