package ApiClients;

import Archivos.Archivo;
import Juegos.*;
import JuegosDtos.CheckpointDTO;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
public class CheckpointClient {


    // GET
    public List<CheckpointDTO> obtenerCheckpointsDTO(ServerConnection servidor, String tituloJuego, String tituloPartida){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").retrieve().bodyToMono(new ParameterizedTypeReference<List<CheckpointDTO>>() {}).block();
    }
    public CheckpointDTO obtenerCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint).retrieve().bodyToMono(CheckpointDTO.class).block();
    }
    public List<Archivo> obtenerArchivosCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint + "/archivos").retrieve().bodyToMono(new ParameterizedTypeReference<List<Archivo>>() {}).block();
    }

    // POST


    public CheckpointDTO postearCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, CheckpointDTO checkpoint) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").bodyValue(checkpoint).retrieve().bodyToMono(CheckpointDTO.class).block();
    }


    // DELETE


    public void eliminarCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint).retrieve().toBodilessEntity().block();
    }

}
