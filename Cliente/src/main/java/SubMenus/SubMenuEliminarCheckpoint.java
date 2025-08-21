package SubMenus;

import ApiHelper.ApiHelper;
import ApiHelper.ApiRequestManager;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerManager;

import java.util.List;
import java.util.Scanner;

public class SubMenuEliminarCheckpoint {
    PartidaDTO partida;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());

    public SubMenuEliminarCheckpoint(PartidaDTO partida) {
        this.partida = partida;
    }

    public void abrirMenu() throws Exception {
        System.out.println("Checkpoints: ");
        int i = 1;
        List<CheckpointDTO> checkpoints = api.obtenerCheckpointsDTO(partida.getTituloJuego(), partida.getTituloPartida());
        for (CheckpointDTO checkpoint : checkpoints) {
            System.out.println(i + ". " + checkpoint);
            i++;
        }
        System.out.println("Elija el checkpoint a eliminar, o presione 0 para cancelar");
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1){
            return;
        }
        else if (indice >=0 && indice<checkpoints.size()){
            CheckpointDTO chk = checkpoints.get(indice);
            ApiHelper.eliminarCheckpoint(api, partida.getTituloJuego(), partida.getTituloJuego(), chk.getId());
            partida.getCheckpointsIDs().remove(chk.getId());
            return;
        }
        else throw new Exception("Opcion invalida para eliminar checkpoint");

    }
}
