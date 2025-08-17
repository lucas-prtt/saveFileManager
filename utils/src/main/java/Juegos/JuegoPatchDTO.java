package Juegos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class JuegoPatchDTO {
    String titulo;
    List<String> saveFilePaths;
    String partidaActual;
}
