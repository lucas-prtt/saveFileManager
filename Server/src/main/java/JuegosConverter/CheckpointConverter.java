package JuegosConverter;

import Juegos.Checkpoint;
import Juegos.Partida;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.PartidaDTO;
import Servicios.JuegosService;
import Servicios.PartidaService;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CheckpointConverter {
    @Autowired
    PartidaService partidaService;

    public Checkpoint fromDto(CheckpointDTO dto){
        if (dto.getId() == null)
            return new Checkpoint(dto.getDescripcion(), partidaService.obtenerPartidaDeJuegoPorTitulo(dto.getTituloJuego(), dto.getTituloPartida()));
        else
            return new Checkpoint(dto.getDescripcion(), partidaService.obtenerPartidaDeJuegoPorTitulo(dto.getTituloJuego(), dto.getTituloPartida()), dto.getId());
    }
}

