package ApiHelper;

import Archivos.Directorio;
import Juegos.Juego;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;

import java.nio.file.Path;

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
