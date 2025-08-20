package JuegosConverter;

import Juegos.Partida;
import JuegosDtos.PartidaDTO;
import Servicios.JuegosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PartidaConverter {
    @Autowired
    JuegosService juegosService;

    public Partida fromDto(PartidaDTO dto){
        return new Partida(dto.getTituloPartida(), juegosService.obtenerJuegoPorTitulo(dto.getTituloJuego()));
    }
}
