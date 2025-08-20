package ServerManagment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ServerConnection {
    String nombre; // Nomas para diferenciarlos
    String ip;
    Integer puerto;
    public ServerConnection(String ip, Integer puerto, String nombre){
        this.ip = ip;
        this.puerto = puerto;
        this.nombre = nombre;
    }
    public String getUri(){
        return "http://"+ip+":" + puerto;
    }
    @Override
    public String toString() {
        return "ServerConnection{" +
                "ip='" + ip + '\'' +
                ", puerto=" + puerto +
                ", uri='" + getUri() + '\'' +
                '}';
    }

    public WebClient getWebClient() {
        return WebClient.create(getUri());
    }
    public Boolean ipEquals(String ipComparada){
        return Objects.equals(ip, ipComparada);
    }
}
