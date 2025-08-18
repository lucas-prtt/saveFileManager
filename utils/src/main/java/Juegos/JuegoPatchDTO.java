package Juegos;

import Archivos.Directorio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class JuegoPatchDTO {
    String titulo;
    List<Directorio> saveFilePaths;
    String partidaActual;
}
