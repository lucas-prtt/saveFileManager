package ApiClients;

import Archivos.Archivo;
import Juegos.Checkpoint;
import Juegos.CheckpointDTO;
import Juegos.Juego;
import Juegos.Partida;
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


}
