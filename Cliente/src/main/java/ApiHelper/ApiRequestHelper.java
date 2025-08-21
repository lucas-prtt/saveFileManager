package ApiHelper;

import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;

public class ApiRequestHelper {

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

}
