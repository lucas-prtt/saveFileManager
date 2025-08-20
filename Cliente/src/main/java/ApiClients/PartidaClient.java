package ApiClients;

import Archivos.Archivo;
import Juegos.*;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public class PartidaClient {

    ServerManager serverManager;
    public PartidaClient(ServerManager serverManager){
        this.serverManager = serverManager;
    }

    // GET

    public List<String> obtenerTitulosPartidas(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo+"/partidas").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {}).block();
    }
    public Partida obtenerPartida(ServerConnection servidor, String tituloJuego, String tituloPartida){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().bodyToMono(Partida.class).block();
    }

    // POST


    public Partida postearPartida(ServerConnection servidor, String tituloJuego, Partida partida) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas").bodyValue(partida).retrieve().bodyToMono(Partida.class).block();
    }


    // DELETE

    public void eliminarPartida(ServerConnection servidor, String tituloJuego, String tituloPartida) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().toBodilessEntity().block();
    }


    // PATCH

    public Partida patchearPartida(ServerConnection servidor, String tituloJuego, String tituloPartida, PartidaPatchDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).bodyValue(patchDTO).retrieve().bodyToMono(Partida.class).block();
    }

}
