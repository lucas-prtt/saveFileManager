package SubMenus;

import ApiClients.ApiRequestManager;
import ApiClients.CheckpointClient;
import ApiClients.JuegoClient;
import Archivos.Archivo;
import FileManager.FileManager;
import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerManager;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SubMenuCargarCheckpoint {
    PartidaDTO partida;
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());
    public SubMenuCargarCheckpoint(PartidaDTO partida, JuegoDTO juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {
        System.out.println("Partidas actuales: ");
        int i = 1;
        List<CheckpointDTO> chkList = api.obtenerCheckpointsDTO(juego.getTitulo(), partida.getTituloPartida());
        CheckpointDTO newchk = new CheckpointDTO();
        for(CheckpointDTO checkpoint : chkList){
            System.out.println(i+". "+ checkpoint);
            i++;
        }

        System.out.println("Elija el checkpoint a cargar, o presione 0 para cancelar");
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1){
            return;
        }
        else if (indice >=0 && indice<chkList.size()){
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
                   newchk.setDescripcion(null);
                else {
                    newchk.setDescripcion(nombre);
                }
                newchk.setFechaDeCreacion(LocalDateTime.now());
                newchk.generateNewId();
                api.postearCheckpoint(juego.getTitulo(), juego.getTituloPartidaActual(), newchk);

                // TODO: Postear archivos
                JuegoDTO juegoPatch = new JuegoDTO();
                juegoPatch.setTituloPartidaActual(partida.getTituloPartida());
                api.patchearJuego(juego.getTitulo(), juegoPatch);
            }
            List<Archivo> archivos = api.obtenerArchivosCheckpoint(juego.getTitulo(), partida.getTituloPartida(), chkList.get(indice).getId());
            FileManager.cargarArchivos(juego, archivos);
            return;
        }
        else throw new Exception("Opcion invalida para eliminar checkpoint");
    }
}
