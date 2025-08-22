package ServerManagment;

import Archivos.ArchivoFinal;
import Archivos.Carpeta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.json.Jackson2SmileDecoder;
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
    public WebClient getWebClientFullSubtypeParsing() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(
                new NamedType(Carpeta.class, "carpeta"),
                new NamedType(ArchivoFinal.class, "archivofinal")
        );
        mapper.activateDefaultTypingAsProperty(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, "tipo");
        return WebClient.builder().codecs(config -> {
            config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
            config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
                }).baseUrl(getUri()).build();
    }
    public Boolean ipEquals(String ipComparada){
        return Objects.equals(ip, ipComparada);
    }
}
