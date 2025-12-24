package dto.JuegosConverter;

import domain.Juegos.Juego;
import dto.JuegoDTO;
import org.springframework.stereotype.Service;

@Service
public class JuegoConverter {
    public Juego fromDto(JuegoDTO dto){
        return new Juego(dto.getTitulo(), dto.getSaveFilePaths());
    }
}
