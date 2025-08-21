package ApiClients;

import Archivos.Archivo;
import Juegos.*;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
public class PartidaClient {

    // GET

    public static List<String> obtenerTitulosPartidas(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo+"/partidas").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {}).block();
    }
    public static PartidaDTO obtenerPartida(ServerConnection servidor, String tituloJuego, String tituloPartida){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().bodyToMono(PartidaDTO.class).block();
    }


    // POST


    public static PartidaDTO postearPartida(ServerConnection servidor, String tituloJuego, PartidaDTO partida) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas").bodyValue(partida).retrieve().bodyToMono(PartidaDTO.class).block();
    }


    // DELETE

    public static void eliminarPartida(ServerConnection servidor, String tituloJuego, String tituloPartida) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().toBodilessEntity().block();
    }


    // PATCH

    public static PartidaDTO patchearPartida(ServerConnection servidor, String tituloJuego, String tituloPartida, PartidaDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).bodyValue(patchDTO).retrieve().bodyToMono(PartidaDTO.class).block();
    }

}
