package ApiHelper;

import Archivos.Archivo;
import Archivos.Directorio;
import Juegos.Juego;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import org.hibernate.annotations.Check;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class ApiHelper {

    public static void cambiarPartidaActual(ApiRequestManager api,String juegoTitulo, String nombrePartidaActual){
        JuegoDTO patch = new JuegoDTO();
        patch.setTituloPartidaActual(nombrePartidaActual);
        api.patchearJuego(juegoTitulo, patch);
    }
    public static void crearPartida(ApiRequestManager api, String tituloJuego, String nombrePartida){
        PartidaDTO partida = new PartidaDTO();
        partida.setTituloPartida(nombrePartida);
        partida.generateNewId();
        partida.setTituloJuego(tituloJuego);
        api.postearPartida(tituloJuego, partida);
    }
    public static void crearCheckpoint(ApiRequestManager api, String tituloJuego, String nombrePartida, CheckpointDTO checkpoint, List<Archivo> archivos){
        api.postearCheckpoint(tituloJuego, nombrePartida, checkpoint);
        api.postearArchivos(tituloJuego, nombrePartida, checkpoint.getId(), archivos);
    }
    public static void crearCheckpoint(ApiRequestManager api, String tituloJuego, String nombrePartida, String nombreCheckpoint, List<Archivo> archivos){
        CheckpointDTO checkpoint = new CheckpointDTO();
        checkpoint.generateNewId();
        checkpoint.setDescripcion(nombreCheckpoint);
        checkpoint.setTituloPartida(nombrePartida);
        checkpoint.setTituloJuego(tituloJuego);
        checkpoint.setFechaDeCreacion(LocalDateTime.now());
        crearCheckpoint(api, tituloJuego, nombrePartida, checkpoint, archivos);
    }
    public static void eliminarCheckpoint(ApiRequestManager api, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        api.eliminarCheckpoint(tituloJuego, tituloPartida, uuidCheckpoint);
    }
    public static void eliminarPartida(ApiRequestManager api, String tituloJuego, String tituloPartida){
        api.eliminarPartida(tituloJuego, tituloPartida);
    }
    public static void eliminarJuego(ApiRequestManager api, String tituloJuego){
        api.eliminarJuego(tituloJuego);
    }
    public static void eliminarPath(ApiRequestManager api, String tituloJuego, Directorio directorio){
        eliminarPath(api, tituloJuego, directorio.getPathPrincipal());
    }
    public static void eliminarPath(ApiRequestManager api, String tituloJuego, Path directorio){
        JuegoDTO patch = new JuegoDTO();
        patch.setSaveFilePaths(api.obtenerJuego(tituloJuego).getSaveFilePaths());
        patch.getSaveFilePaths().removeIf(sf -> sf.getPathPrincipal() == directorio);
        api.patchearJuego(tituloJuego, patch);
    }
}
