package ServerManagment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Getter
@Setter
@NoArgsConstructor
public class ServerConnection {
    String nombre; // Nomas para diferenciarlos
    String ip;
    Integer puerto;
    WebClient webClient;
    public ServerConnection(String ip, Integer puerto, String nombre){
        this.ip = ip;
        this.puerto = puerto;
        this.nombre = nombre;
        loadWebClient();
    }
    public void loadWebClient(){
        this.webClient = WebClient.create(getUri());
    }
    public String getUri(){
        return "http://"+ip+":" + puerto;
    }
    public Mono<String> sendGet(String path) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class);
    }
    @Override
    public String toString() {
        return "ServerConnection{" +
                "ip='" + ip + '\'' +
                ", puerto=" + puerto +
                ", uri='" + getUri() + '\'' +
                '}';
    }
}
