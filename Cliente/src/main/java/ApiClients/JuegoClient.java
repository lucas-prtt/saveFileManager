package ApiClients;

import Archivos.Archivo;
import Juegos.*;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerConnection;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public class JuegoClient {

    // GET

    public static List<String> obtenerTitulosJuegos(ServerConnection servidor){
        return servidor.getWebClient().get().uri("/api/juegos").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>(){}).block();
    }
    public static JuegoDTO obtenerJuego(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo).retrieve().bodyToMono(JuegoDTO.class).block();
    }

    // POST

    public static JuegoDTO postearJuego(ServerConnection servidor, JuegoDTO juego) {
        return servidor.getWebClient().post().uri("/api/juegos").bodyValue(juego).retrieve().bodyToMono(JuegoDTO.class).block();
    }

    // DELETE

    public static void eliminarJuego(ServerConnection servidor, String tituloJuego) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego).retrieve().toBodilessEntity().block();
    }

    // PATCH


    public static JuegoDTO patchearJuego(ServerConnection servidor, String tituloJuego, JuegoDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego).bodyValue(patchDTO).retrieve().bodyToMono(JuegoDTO.class).block();
    }



}
