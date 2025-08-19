package ServerManagment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ServerManager {
    @Value("${app.nombre}")
    private Integer puertoDefault;
    private final List<ServerConnection> servidoresRegistrados = new ArrayList<>();

    public void registrarServidor(String servidor, Integer puerto, String nombre) {
        servidoresRegistrados.add(new ServerConnection(servidor, puerto, nombre));
    }
    public List<ServerConnection> getServerConnections(){
        return servidoresRegistrados;
    }
    public void registrarLocalHost(){
        registrarServidor("localhost", puertoDefault, "Servidor local");
    }
}
