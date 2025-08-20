package JuegosConverter;

import Juegos.Juego;
import JuegosDtos.JuegoDTO;
import org.springframework.stereotype.Service;

@Service
public class JuegoConverter {
    public static Juego fromDto(JuegoDTO dto){
        return new Juego(dto.getTitulo(), dto.getSaveFilePaths());
    }
}
