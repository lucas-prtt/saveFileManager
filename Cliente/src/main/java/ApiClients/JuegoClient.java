package ApiClients;

import Archivos.Archivo;
import Juegos.*;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoClient {
    ServerManager serverManager;
    public JuegoClient(ServerManager serverManager){
        this.serverManager = serverManager;
    }

    // GET

    public List<String> obtenerTitulosJuegos(ServerConnection servidor){
        return servidor.getWebClient().get().uri("/api/juegos").retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>(){}).block();
    }
    public Juego obtenerJuego(ServerConnection servidor, String titulo){
        return servidor.getWebClient().get().uri("/api/juegos/" + titulo).retrieve().bodyToMono(Juego.class).block();
    }

    // POST

    public Juego postearJuego(ServerConnection servidor, Juego juego) {
        return servidor.getWebClient().post().uri("/api/juegos").bodyValue(juego).retrieve().bodyToMono(Juego.class).block();
    }

    // DELETE

    public void eliminarJuego(ServerConnection servidor, String tituloJuego) {
        servidor.getWebClient().delete().uri("/api/juegos/" + tituloJuego).retrieve().toBodilessEntity().block();
    }

    // PATCH


    public Juego patchearJuego(ServerConnection servidor, String tituloJuego, JuegoPatchDTO patchDTO) {
        return servidor.getWebClient().patch().uri("/api/juegos/" + tituloJuego).bodyValue(patchDTO).retrieve().bodyToMono(Juego.class).block();
    }


}
