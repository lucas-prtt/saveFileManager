package SubMenus;

import Juegos.Juego;

import java.util.Scanner;

public class SubMenuAgregarPath {
    Juego juego;
    public SubMenuAgregarPath(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu() throws Exception {
        System.out.println("Ingrese el Path a agregar:");
        juego.addSaveFilePath(new Scanner(System.in).nextLine());
    }
}
