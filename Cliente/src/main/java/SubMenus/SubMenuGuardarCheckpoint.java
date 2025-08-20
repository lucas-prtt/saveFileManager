package SubMenus;

import ApiClients.CheckpointClient;
import ApiClients.JuegoClient;
import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import ServerManagment.ServerManager;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuGuardarCheckpoint {
    Partida partida;
    Juego juego;

    public SubMenuGuardarCheckpoint(Partida partida, Juego juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {
        Checkpoint chk;
        if (partida == juego.getPartidaActual()) { // Si la partida es la actual
            System.out.println("Ingrese el nombre del checkpoint (opcional)");
            String nombre = new Scanner(System.in).nextLine();
            if(Objects.equals(nombre, ""))
                chk = partida.crearCheckpoint();
            else
                chk = partida.crearCheckpoint(nombre);
            new CheckpointClient().postearCheckpoint(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), partida.getTitulo(), chk);
        }else{
            int r;
            if(juego.getPartidaActual() != null){// Si hay partida actual que no coincide, confirma sobreescritura
                System.out.println("Atención! La partida seleccionada es <"+partida.getTitulo()+">, la partida actualmente cargada es <"+juego.getPartidaActual().getTitulo()+">\nEsta seguro que desea guardar la partida actual aquí?" );
                System.out.println("1. Si\n2. No");
                r = 0;
                while (r != 1 && r != 2) {
                    r = new Scanner(System.in).nextInt();
                }
            }
            else{   // Si no hay partida actual, guarda de una
                r = 1;
            }
            if(r == 1){
                System.out.println("Ingrese el nombre del checkpoint (opcional)");
                String nombre = new Scanner(System.in).nextLine();
                if(Objects.equals(nombre, ""))
                    chk = partida.crearCheckpoint();
                else
                    chk = partida.crearCheckpoint(nombre);
                juego.setPartidaActual(partida);
                System.out.println("La partida actual se ha actualizado a la seleccionada ("+partida.getTitulo()+")");
                new JuegoClient().patchearJuego(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.toPatchDTO());
                new CheckpointClient().postearCheckpoint(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), partida.getTitulo(), chk);
            }
        }

    }
}
