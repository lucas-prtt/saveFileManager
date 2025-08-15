package Menus;

import java.util.Scanner;

public class MenuPrincipal extends Menu{

    @Override
     protected void mostrarTextoOpciones (){
        System.out.println("Elija una opción: ");
        System.out.println("1. Agregar juego");
        System.out.println("2. Ver juegos cargados");
        System.out.println("3. Salir");
    }
    @Override
    protected boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                break;
            case 2:
                break;
            case 3:
                return true;
            default:
                throw new Exception("Error: Opcion Invalida");
        }
        return false;
    }
}
