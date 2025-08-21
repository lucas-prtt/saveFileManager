package SubMenus;

import ApiClients.CheckpointClient;
import ApiClients.JuegoClient;
import ApiHelper.ApiHelper;
import ApiHelper.ApiRequestManager;
import FileManager.FileManager;
import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerManager;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuGuardarCheckpoint {
    PartidaDTO partida;
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());

    public SubMenuGuardarCheckpoint(PartidaDTO partida, JuegoDTO juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {
        Checkpoint chk;
        if (Objects.equals(partida.getTituloPartida(), juego.getTituloPartidaActual())) { // Si la partida es la actual
            System.out.println("Ingrese el nombre del checkpoint (opcional)");
            String nombre = new Scanner(System.in).nextLine();
            if(Objects.equals(nombre, ""))
                nombre = null;
            ApiHelper.crearCheckpoint(api, partida, nombre, FileManager.guardarArchivos(juego));
        }else{
            int r;
            if(juego.getTituloPartidaActual() != null){// Si hay partida actual que no coincide, confirma sobreescritura
                System.out.println("Atención! La partida seleccionada es <"+partida.getTituloPartida()+">, la partida actualmente cargada es <"+juego.getTituloPartidaActual() + ">\nEsta seguro que desea guardar la partida actual aquí?" );
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
                    nombre = null;
                System.out.println("La partida actual se ha actualizado a la seleccionada ("+partida.getTituloPartida()+")");
                ApiHelper.crearCheckpoint(api, partida, nombre, FileManager.guardarArchivos(juego));
                ApiHelper.cambiarPartidaActual(api, juego, partida.getTituloPartida());
            }
        }

    }
}
