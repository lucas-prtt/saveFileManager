package SubMenus;

import ApiClients.JuegoClient;
import Juegos.Juego;
import ServerManagment.ServerManager;

import java.nio.file.Path;
import java.util.Scanner;

public class SubMenuAgregarPath {
    Juego juego;
    public SubMenuAgregarPath(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu() throws Exception {
        System.out.println("Ingrese el Path a agregar:");
        juego.addSaveFilePath(Path.of(new Scanner(System.in).nextLine()));
        new JuegoClient().patchearJuego(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.toDTO());
    }
}
