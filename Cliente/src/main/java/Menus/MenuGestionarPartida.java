package Menus;

import ApiHelper.ApiRequestManager;
import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerManager;
import SubMenus.SubMenuCargarCheckpoint;
import SubMenus.SubMenuCargarUltimoCheckpoint;
import SubMenus.SubMenuGuardarCheckpoint;
import SubMenus.SubMenuEliminarCheckpoint;

import java.util.List;

public class MenuGestionarPartida extends Menu{
    PartidaDTO partida;
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());
    public MenuGestionarPartida(PartidaDTO partida, JuegoDTO juego){
        this.partida = partida;
        this.juego = juego;
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Gestionando \n > Juego: " + juego.getTitulo() + "\n > Partida: " + partida.getTituloPartida());
        System.out.println("Elija una opcion:");
        System.out.println("1. Guardar checkpoint");
        System.out.println("2. Cargar ultimo checkpoint");
        System.out.println("3. Ver checkpoints");
        System.out.println("4. Cargar checkpoint");
        System.out.println("5. Eliminar checkpoints");
        System.out.println("6. Salir");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                new SubMenuGuardarCheckpoint(partida, juego).abrirMenu();
                break;
            case 2:
                new SubMenuCargarUltimoCheckpoint(partida, juego).abrirMenu();
                break;
            case 3:
                System.out.println("Checkpoints actuales: ");
                int i = 1;
                List<CheckpointDTO> listaCheckpoints= api.obtenerCheckpointsDTO(partida.getTituloJuego(), partida.getTituloPartida());
                for(CheckpointDTO checkpoint : listaCheckpoints){
                    System.out.println(i+". "+checkpoint);
                    i++;
                }
                break;
            case 4:
                new SubMenuCargarCheckpoint(partida, juego).abrirMenu();
                break;
            case 5:
                new SubMenuEliminarCheckpoint(partida).abrirMenu();
                break;
            case 6:
                return true;
        }
        return false;
    }
}
