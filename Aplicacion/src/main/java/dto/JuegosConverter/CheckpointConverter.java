package dto.JuegosConverter;

import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import dto.CheckpointDTO;
import servicios.PartidaService;
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

