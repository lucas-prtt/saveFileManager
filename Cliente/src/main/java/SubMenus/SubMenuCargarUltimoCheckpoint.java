package SubMenus;

import ApiHelper.ApiRequestManager;
import FileManager.FileManager;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SubMenuCargarUltimoCheckpoint {
    PartidaDTO partida;
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());

    public SubMenuCargarUltimoCheckpoint(PartidaDTO partida, JuegoDTO juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {
        CheckpointDTO newChk = new CheckpointDTO();
        List<CheckpointDTO> chkList = api.obtenerCheckpointsDTO(juego.getTitulo(), partida.getTituloPartida());
        System.out.println("Checkpoints actuales: ");
        int i = 1;
        for(CheckpointDTO checkpoint : chkList){
            System.out.println(i+". " + checkpoint);
            i++;
        }

        if (Objects.equals(partida.getTituloPartida(), juego.getTituloPartidaActual())) {
            System.out.println("Se ha cargado el ultimo checkpoint");

            FileManager.cargarArchivos(juego, api.obtenerArchivosCheckpoint(juego.getTitulo(), partida.getTituloPartida(), chkList.getLast().getId()));
        } else {
            System.out.println("La partida actual es <"+juego.getTituloPartidaActual()+">. Usted está en <"+partida.getTituloPartida()+">. Se guardará un checkpoint en la partida actual antes de cargar la nueva");
            System.out.println("Ingrese el nombre del checkpoint (opcional)");
            String nombre = new Scanner(System.in).nextLine();

            if(Objects.equals(nombre, ""))
                newChk.setDescripcion(null);
            else {
                newChk.setDescripcion(nombre);
            }
            newChk.setFechaDeCreacion(LocalDateTime.now());
            newChk.generateNewId();
            api.postearCheckpoint(juego.getTitulo(), juego.getTituloPartidaActual(), newChk);
            api.postearArchivos(juego.getTitulo(), partida.getTituloPartida(), newChk.getId(), FileManager.guardarArchivos(juego));

            FileManager.cargarArchivos(juego, api.obtenerArchivosCheckpoint(juego.getTitulo(), partida.getTituloPartida(), chkList.getLast().getId()));
            juego.setTituloPartidaActual(partida.getTituloPartida());
            api.patchearJuego(juego.getTitulo(), juego);
        }
    }
}
