package ApiClients;

import Archivos.*;
import Juegos.*;
import JuegosDtos.CheckpointDTO;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

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
        }public static CheckpointDTO postearArchivos(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint, List<Archivo> archivos) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT); // más legible
                String json = mapper.writeValueAsString(archivos);
                System.out.println("JSON que se va a enviar:");
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return servidor.getWebClient()
                    .post()
                    .uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint + "/archivos")
                    .bodyValue(archivos)
                    .retrieve()
                    .bodyToMono(CheckpointDTO.class)
                    .block();
        }


        // DELETE


        public static void eliminarCheckpoint(ServerConnection servidor, String tituloJuego, String tituloPartida, String uuidCheckpoint) {
            servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego + "/partidas/" + tituloPartida + "/checkpoints/" + uuidCheckpoint).retrieve().toBodilessEntity().block();
        }

    }
