package ApiClients;

import Archivos.Archivo;
import Juegos.*;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class JuegosClient {
    ServerManager serverManager;
    public JuegosClient(ServerManager serverManager){
        this.serverManager = serverManager;
    }

    // GET

    public List<String> obtenerTitulosJuegos(ServerConnection servidor){
        return servidor.getWebClient().get().uri("/api/juegos").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>(){}).block();
    }
    public Juego obtenerJuego(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo).retrieve().bodyToMono(Juego.class).block();
    }
    public List<String> obtenerTitulosPartidas(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo+"/partidas").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {}).block();
    }
    public Partida obtenerPartida(ServerConnection servidor, String tituloJuego, String tituloPartida){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().bodyToMono(Partida.class).block();
    }
    public List<CheckpointDTO> obtenerCheckpointsDTO(ServerConnection servidor, String tituloJuego, String tituloPartida){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").retrieve().bodyToMono(new ParameterizedTypeReference<List<CheckpointDTO>>() {}).block();
    }
    public Checkpoint obtenerCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint).retrieve().bodyToMono(Checkpoint.class).block();
    }
    public List<Archivo> obtenerArchivosCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint + "/archivos").retrieve().bodyToMono(new ParameterizedTypeReference<List<Archivo>>() {}).block();
    }

    // POST

    public Juego postearJuego(ServerConnection servidor, Juego juego) {
        return servidor.getWebClient().post().uri("/api/juegos").bodyValue(juego).retrieve().bodyToMono(Juego.class).block();
    }

    public Partida postearPartida(ServerConnection servidor, String tituloJuego, Partida partida) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas").bodyValue(partida).retrieve().bodyToMono(Partida.class).block();
    }

    public Checkpoint postearCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, Checkpoint checkpoint) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").bodyValue(checkpoint).retrieve().bodyToMono(Checkpoint.class).block();
    }

    // DELETE

    public void eliminarJuego(ServerConnection servidor, String tituloJuego) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego).retrieve().toBodilessEntity().block();
    }

    public void eliminarPartida(ServerConnection servidor, String tituloJuego, String tituloPartida) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).retrieve().toBodilessEntity().block();
    }

    public void eliminarCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint).retrieve().toBodilessEntity().block();
    }

    // PATCH


    public Juego patchearJuego(ServerConnection servidor, String tituloJuego, JuegoPatchDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego).bodyValue(patchDTO).retrieve().bodyToMono(Juego.class).block();
    }

    public Partida patchearPartida(ServerConnection servidor, String tituloJuego, String tituloPartida, PartidaPatchDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida).bodyValue(patchDTO).retrieve().bodyToMono(Partida.class).block();
    }


}
