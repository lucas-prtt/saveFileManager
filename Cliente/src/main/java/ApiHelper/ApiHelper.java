package ApiHelper;

import Archivos.Archivo;
import Archivos.Directorio;
import Juegos.Juego;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ApiHelper {

    private static void cambiarPartidaActual(ApiRequestManager api,String juegoTitulo, String nombrePartidaActual){
        JuegoDTO patch = new JuegoDTO();
        patch.setTituloPartidaActual(nombrePartidaActual);
        api.patchearJuego(juegoTitulo, patch);
    }
    public static void cambiarPartidaActual(ApiRequestManager api, JuegoDTO juego, String nombrePartidaActual){
        cambiarPartidaActual(api, juego.getTitulo(), nombrePartidaActual);
        // Tambien actualiza el objeto Juego
        juego.setTituloPartidaActual(nombrePartidaActual);
    }
    private static void crearPartida(ApiRequestManager api, String tituloJuego, String nombrePartida){
        PartidaDTO partida = new PartidaDTO();
        partida.setTituloPartida(nombrePartida);
        partida.generateNewId();
        partida.setTituloJuego(tituloJuego);
        api.postearPartida(tituloJuego, partida);
    }
    public static void crearPartida(ApiRequestManager api, JuegoDTO juego, String nombrePartida){
        crearPartida(api, juego.getTitulo(), nombrePartida);
        // Tambien actualiza el objeto partida
        juego.getTituloPartidas().add(nombrePartida);
    }
    private static void crearCheckpoint(ApiRequestManager api, String tituloJuego, String nombrePartida, CheckpointDTO checkpoint, List<Archivo> archivos){
        api.postearCheckpoint(tituloJuego, nombrePartida, checkpoint);
        api.postearArchivos(tituloJuego, nombrePartida, checkpoint.getId(), archivos);
    }
    public static void crearCheckpoint(ApiRequestManager api, PartidaDTO partidaDTO, String nombreCheckpoint, List<Archivo> archivos){
        CheckpointDTO checkpoint = new CheckpointDTO();
        checkpoint.generateNewId();
        checkpoint.setDescripcion(nombreCheckpoint);
        checkpoint.setTituloPartida(partidaDTO.getTituloPartida());
        checkpoint.setTituloJuego(partidaDTO.getTituloJuego());
        checkpoint.setFechaDeCreacion(LocalDateTime.now());
        crearCheckpoint(api, partidaDTO.getTituloJuego(), partidaDTO.getTituloPartida(), checkpoint, archivos);
        partidaDTO.getCheckpointsIDs().add(checkpoint.getId());
    }
    private static void eliminarCheckpoint(ApiRequestManager api, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        api.eliminarCheckpoint(tituloJuego, tituloPartida, uuidCheckpoint);
    }
    public static void eliminarCheckpoint(ApiRequestManager api, PartidaDTO partida, String uuidCheckpoint){
        eliminarCheckpoint(api, partida.getTituloJuego(), partida.getTituloPartida(), uuidCheckpoint);
        partida.getCheckpointsIDs().removeIf(c-> Objects.equals(c, uuidCheckpoint));
    }
    private static void eliminarPartida(ApiRequestManager api, String tituloJuego, String tituloPartida){
        api.eliminarPartida(tituloJuego, tituloPartida);
    }
    public static void eliminarPartida(ApiRequestManager api, JuegoDTO juego, String tituloPartida){
        ApiHelper.eliminarPartida(api, juego.getTitulo(), tituloPartida);
        juego.getTituloPartidas().removeIf(titulo -> Objects.equals(titulo, tituloPartida));

    }
    public static void eliminarJuego(ApiRequestManager api, String tituloJuego){
        api.eliminarJuego(tituloJuego);
    }
    public static void eliminarPath(ApiRequestManager api, JuegoDTO juego, Directorio directorio){
        eliminarPath(api, juego.getTitulo(), directorio);
        juego.getSaveFilePaths().removeIf(sfp -> sfp.getPathPrincipal() == directorio.getPathPrincipal());
    }
    public static void eliminarPath(ApiRequestManager api, String tituloJuego, Directorio directorio){
        eliminarPath(api, tituloJuego, directorio.getPathPrincipal());
    }
    private static void eliminarPath(ApiRequestManager api, String tituloJuego, Path directorio){
        JuegoDTO patch = new JuegoDTO();
        patch.setSaveFilePaths(api.obtenerJuego(tituloJuego).getSaveFilePaths());
        patch.getSaveFilePaths().removeIf(sf -> sf.getPathPrincipal() == directorio);
        api.patchearJuego(tituloJuego, patch);
    }
}
