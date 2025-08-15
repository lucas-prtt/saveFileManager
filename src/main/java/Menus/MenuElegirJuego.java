package Menus;

import Controllers.JuegosController;
import Juegos.Juego;

import java.util.Scanner;

public class MenuElegirJuego extends Menu{
    Juego juego;
    public MenuElegirJuego(){
        juego = new Juego("Nombre del juego");
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Titulo: " + juego.getTitulo());

        System.out.println("Paths de guardado: ");
        for(String path : juego.getSaveFilePaths()){
            System.out.println("> " + path);
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
                System.out.println("Ingrese el Path a agregar:");
                juego.addSaveFilePath(new Scanner(System.in).nextLine());
                break;
            case 3:
                if(juego.getSaveFilePaths().isEmpty())
                    throw  new Exception("Error: no hay paths guardados");
                int i = 0;
                System.out.println("Elija cual eliminar:");
                for(String path : juego.getSaveFilePaths()){
                    System.out.println(i+1 + ". " + path);
                    i++;
                }
                int eliminado = new Scanner(System.in).nextInt() - 1;
                if (eliminado > i || eliminado < 0)
                    throw new Exception("Error: path invalido");
                System.out.println("Path \" " + juego.getSaveFilePaths().get(eliminado)+ "\" eliminado");
                juego.removeSaveFilePath(eliminado);
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
