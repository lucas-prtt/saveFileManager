package ServerManagment;

import Exceptions.ServerNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServerManager {
    @Value("${server.port}")
    private static Integer puertoDefault;
    private final List<ServerConnection> servidoresRegistrados = new ArrayList<>();
    @Getter
    private final static ServerManager instance = new ServerManager();
    //Singleton
    private ServerManager(){
        registrarLocalHost();
    }


    public void registrarLocalHost(){
        registrarServidor("localhost", "Servidor local");
    }
    public void registrarLocalHost(Integer puerto){
        registrarServidor("localhost", "Servidor local", puerto);
    }
    public void registrarServidor(String servidor, String nombre, Integer puerto) {
        servidoresRegistrados.add(new ServerConnection(servidor, puerto, nombre));
    }
    public void registrarServidor(String servidor, String nombre) {
        servidoresRegistrados.add(new ServerConnection(servidor, puertoDefault, nombre));
    }
    public List<ServerConnection> getServerConnections(){
        return servidoresRegistrados;
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
