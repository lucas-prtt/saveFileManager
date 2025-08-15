package Menus;

import java.util.Scanner;

public class MenuPrincipal{


    public void abrirMenu(){
        boolean exit = false;
        int opcion = 0;
        while (!exit){
            try{
                opcion = elegirOpcionMenuPrincipal();
                exit = ejecutarOperacionMenuPrincipal(opcion);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return;
    }
    private int elegirOpcionMenuPrincipal() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Elija una opción: ");
        System.out.println("1. Agregar juego");
        System.out.println("2. Ver juegos cargados");
        System.out.println("3. Salir");
        int r = scanner.nextInt();
        verificarValidez(r);
        return r;
    }
    private boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                break;
            case 2:
                break;
            case 3:
                return true;
            default:
                throw new Exception("Error: Opcion Invalida - Case fallado");
        }
        return false;
    }
    private void verificarValidez(int opcion) throws Exception {
        if(opcion>3 || opcion<1)
            throw new Exception("Error: Opcion invalida");
    }
}
