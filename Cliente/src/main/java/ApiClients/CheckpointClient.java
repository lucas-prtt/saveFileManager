package ApiClients;

import Archivos.Archivo;
import Juegos.*;
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
    public Checkpoint obtenerCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint).retrieve().bodyToMono(Checkpoint.class).block();
    }
    public List<Archivo> obtenerArchivosCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint + "/archivos").retrieve().bodyToMono(new ParameterizedTypeReference<List<Archivo>>() {}).block();
    }

    // POST


    public Checkpoint postearCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, Checkpoint checkpoint) {
        return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").bodyValue(checkpoint).retrieve().bodyToMono(Checkpoint.class).block();
    }

    // DELETE


    public void eliminarCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint).retrieve().toBodilessEntity().block();
    }

}
