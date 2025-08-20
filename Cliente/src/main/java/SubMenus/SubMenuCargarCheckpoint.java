package SubMenus;

import ApiClients.CheckpointClient;
import ApiClients.JuegoClient;
import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import ServerManagment.ServerManager;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuCargarCheckpoint {
    Partida partida;
    Juego juego;

    public SubMenuCargarCheckpoint(Partida partida, Juego juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {
        Checkpoint chk;
        System.out.println("Partidas actuales: ");
        int i = 1;
        for(Checkpoint checkpoint : partida.getCheckpoints()){
            System.out.println(i+". "+checkpoint.getStringReferencia());
            i++;
        }

        System.out.println("Elija el checkpoint a cargar, o presione 0 para cancelar");
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1){
            return;
        }
        else if (indice >=0 && indice<partida.getCheckpoints().size()){
            System.out.println("Atención! Desea guardar el estado actual de la partida antes de cargar el checkpoint?" );
            System.out.println("1. Si\n2. No");
            int r = 0;
            while (r != 1 && r != 2) {
                r = new Scanner(System.in).nextInt();
            }
            if(r == 1){
                System.out.println("Ingrese el nombre del checkpoint (opcional)");
                String nombre = new Scanner(System.in).nextLine();
                if(Objects.equals(nombre, ""))
                   chk =  partida.crearCheckpoint();
                else {
                    chk = partida.crearCheckpoint(nombre);
                }
                new CheckpointClient().postearCheckpoint(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.getPartidaActual().getTitulo(), chk);
                juego.setPartidaActual(partida);
                new JuegoClient().patchearJuego(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.toPatchDTO());
            }
            partida.cargarCheckpoint(partida.getCheckpoints().get(indice));
            return;
        }
        else throw new Exception("Opcion invalida para eliminar checkpoint");
    }
}
