package ApiClients;

import ServerManagment.ServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuegosClient {
    ServerManager serverManager;
    public JuegosClient(ServerManager serverManager){
        this.serverManager = serverManager;
    }

}
