package JuegosDtos;

import Archivos.Directorio;
import Juegos.Juego;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JuegoDTO {
    private String titulo;
    private String tituloPartidaActual;
    private List<String> tituloPartidas;
    private List<Directorio> saveFilePaths;


    public void agregarDirectorio(Directorio directorio){
        saveFilePaths.add(directorio);
    }

    public void eliminarDirectorio(Directorio directorio){
        saveFilePaths.remove(directorio);
    }
}
