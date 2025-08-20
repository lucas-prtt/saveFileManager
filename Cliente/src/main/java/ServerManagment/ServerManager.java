package ServerManagment;

import Exceptions.ServerNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServerManager {
    @Value("${server.port}")
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
    public ServerConnection getServidorLocal(){
        return getServidorByIp("localhost");
    }
    public ServerConnection getServidorByIp(String ip){
        final Optional<ServerConnection> serverConnection = servidoresRegistrados.stream().filter(serv -> serv.ipEquals(ip)).findFirst();
        if(serverConnection.isEmpty()) {
            throw new ServerNotFoundException("No se encontro el servidor");
        }
        return serverConnection.get();
    }
}
