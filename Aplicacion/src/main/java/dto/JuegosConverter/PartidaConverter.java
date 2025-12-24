package dto.JuegosConverter;

import domain.Juegos.Partida;
import dto.PartidaDTO;
import servicios.JuegosService;
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
