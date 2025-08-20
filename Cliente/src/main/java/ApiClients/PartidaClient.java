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

    public List<String> obtenerTitulosPartidas(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo+"/partidas").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {}).block();
    }
    public PartidaDTO obtenerPartida(ServerConnection servidor, String tituloJuego, String tituloPartida){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().bodyToMono(PartidaDTO.class).block();
    }
    public String obtenerPartidaActual(ServerConnection servidor, String tituloJuego){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidaActual").retrieve().bodyToMono(String.class).block();
    }


    // POST


    public PartidaDTO postearPartida(ServerConnection servidor, String tituloJuego, Partida partida) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas").bodyValue(partida).retrieve().bodyToMono(PartidaDTO.class).block();
    }


    // DELETE

    public void eliminarPartida(ServerConnection servidor, String tituloJuego, String tituloPartida) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().toBodilessEntity().block();
    }


    // PATCH

    public PartidaDTO patchearPartida(ServerConnection servidor, String tituloJuego, String tituloPartida, PartidaDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).bodyValue(patchDTO).retrieve().bodyToMono(PartidaDTO.class).block();
    }

}
