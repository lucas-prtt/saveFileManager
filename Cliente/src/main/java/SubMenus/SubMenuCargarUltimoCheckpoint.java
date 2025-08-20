package SubMenus;

import ApiClients.CheckpointClient;
import ApiClients.JuegoClient;
import ApiClients.PartidaClient;
import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuCargarUltimoCheckpoint {
    Partida partida;
    Juego juego;

    public SubMenuCargarUltimoCheckpoint(Partida partida, Juego juego) {
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

        if (partida == juego.getPartidaActual()) {
            System.out.println("Se ha cargado el ultimo checkpoint");
            partida.cargarUltimoCheckpoint();
        } else {
            System.out.println("La partida actual es <"+juego.getPartidaActual().getTitulo()+">. Usted está en <"+partida.getTitulo()+">. Se guardará un checkpoint en la partida actual antes de cargar la nueva");
            System.out.println("Ingrese el nombre del checkpoint (opcional)");
            String nombre = new Scanner(System.in).nextLine();
            if(Objects.equals(nombre, ""))
                chk = juego.getPartidaActual().crearCheckpoint();
            else
                chk = juego.getPartidaActual().crearCheckpoint(nombre);
            partida.cargarUltimoCheckpoint();
            new CheckpointClient().postearCheckpoint(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.getPartidaActual().getTitulo(), chk);
            juego.setPartidaActual(partida);
            new JuegoClient().patchearJuego(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.toDTO());
        }
    }
}
