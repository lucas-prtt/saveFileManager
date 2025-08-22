package ApiClients;

import Archivos.*;
import JuegosDtos.CheckpointDTO;
import ServerManagment.ServerConnection;

import Utils.SerializaConTipoForzado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;


    public class CheckpointClient {


        // GET
        public static List<CheckpointDTO> obtenerCheckpointsDTO(ServerConnection servidor, String tituloJuego, String tituloPartida){
            return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").retrieve().bodyToMono(new ParameterizedTypeReference<List<CheckpointDTO>>() {}).block();
        }
        public static CheckpointDTO obtenerCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
            return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint).retrieve().bodyToMono(CheckpointDTO.class).block();
        }
        public static List<Archivo> obtenerArchivosCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint){
            return servidor.getWebClient().get().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/"+ uuidCheckpoint + "/archivos").retrieve().bodyToMono(new ParameterizedTypeReference<List<Archivo>>() {}).block();
        }

        // POST


        public static CheckpointDTO postearCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, CheckpointDTO checkpoint) {
            return servidor.getWebClient().post().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints").bodyValue(checkpoint).retrieve().bodyToMono(CheckpointDTO.class).block();
        }


        public static Void postearArchivos(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint, List<Archivo> archivos) {
            //System.out.println(SerializaConTipoForzado.serializaAListaDeArchivos(archivos));

            return servidor.getWebClient()
                    .post()
                    .uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint + "/archivos")
                    .bodyValue(SerializaConTipoForzado.serializaAListaDeArchivos(archivos))
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }


        // DELETE


        public static void eliminarCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint) {
            servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint).retrieve().toBodilessEntity().block();
        }

    }
