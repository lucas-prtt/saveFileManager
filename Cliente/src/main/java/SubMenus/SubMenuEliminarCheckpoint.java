package SubMenus;

import ApiClients.CheckpointClient;
import Juegos.Checkpoint;
import Juegos.Partida;
import ServerManagment.ServerManager;

import java.util.Scanner;

public class SubMenuEliminarCheckpoint {
    Partida partida;

    public SubMenuEliminarCheckpoint(Partida partida) {
        this.partida = partida;
    }

    public void abrirMenu() throws Exception {
        System.out.println("Checkpoints: ");
        int i = 1;
        for (Checkpoint checkpoint : partida.getCheckpoints()) {
            System.out.println(i + ". " + checkpoint.getStringReferencia());
            i++;
        }
        System.out.println("Elija el checkpoint a eliminar, o presione 0 para cancelar");
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1){
            return;
        }
        else if (indice >=0 && indice<partida.getCheckpoints().size()){
            if (indice == 0)
                return;
            Checkpoint chk = partida.getCheckpoints().get(indice);
            new CheckpointClient().eliminarCheckpoint(ServerManager.getInstance().getServidorLocal(), partida.getJuego().getTitulo(), partida.getTitulo(), chk.getId());
            partida.eliminarCheckpointByIndex(indice);
            return;
        }
        else throw new Exception("Opcion invalida para eliminar checkpoint");

    }
}
