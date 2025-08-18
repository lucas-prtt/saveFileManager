package Menus;

import Archivos.Directorio;
import Controllers.JuegosController;
import Juegos.Juego;
import SubMenus.SubMenuAgregarPath;
import SubMenus.SubMenuEliminarPath;

import java.util.Scanner;

public class MenuCargarJuego extends Menu{
    Juego juego;
    public MenuCargarJuego(){
        juego = new Juego("Nombre del juego");
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Titulo: " + juego.getTitulo());

        System.out.println("Paths de guardado: ");
        for(Directorio path : juego.getSaveFilePaths()){
            System.out.println("> " + path.getPathPrincipal());
        }
        if(juego.getSaveFilePaths().isEmpty())
            System.out.println("Ningun filepath guardado");

        System.out.println("\n\nElija una opcion:");
        System.out.println("1. Establecer titulo");
        System.out.println("2. Agregar path de archivos de guardado");
        System.out.println("3. Quitar path de archivos de guardado");
        System.out.println("4. Confirmar");
        System.out.println("5. Cancelar");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                System.out.println("Ingrese el titulo del juego:");
                juego.setTitulo(new Scanner(System.in).nextLine());
                break;
            case 2:
                new SubMenuAgregarPath(juego).abrirMenu();
                break;
            case 3:
                new SubMenuEliminarPath(juego).abrirMenu();
                break;
            case 4:
                JuegosController.getInstance().addJuego(juego);
                return true;
            case 5:
                return true;
        }
        return false;
    }
}
