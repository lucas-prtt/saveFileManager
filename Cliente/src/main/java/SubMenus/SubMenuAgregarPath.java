package SubMenus;

import ApiHelper.ApiRequestManager;
import Archivos.Directorio;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerManager;

import java.nio.file.Path;
import java.util.Scanner;

public class SubMenuAgregarPath {
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());
    public SubMenuAgregarPath(JuegoDTO juego){
        this.juego = juego;
    }
    public void abrirMenu() throws Exception {
        System.out.println("Ingrese el Path a agregar:");
        juego.agregarDirectorio(new Directorio(Path.of(new Scanner(System.in).nextLine())));
        api.patchearJuego(juego.getTitulo(), juego);
    }
}
