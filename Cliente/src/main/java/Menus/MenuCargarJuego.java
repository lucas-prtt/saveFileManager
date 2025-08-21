package Menus;

import ApiClients.JuegoClient;
import Archivos.Directorio;
import Juegos.Juego;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerConnection;
import ServerManagment.ServerManager;
import SubMenus.SubMenuAgregarPath;
import SubMenus.SubMenuEliminarPath;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuCargarJuego extends Menu{
    JuegoDTO juego;



    public MenuCargarJuego(){
        juego = new JuegoDTO();
        juego.setTitulo("Nombre del juego");
        juego.setSaveFilePaths(new ArrayList<>());
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Titulo: " + juego.getTitulo());

        System.out.println("Paths de guardado: ");
        if(juego.getSaveFilePaths().isEmpty())
            System.out.println("Ningun filepath guardado");
        else {
            for (Directorio path : juego.getSaveFilePaths()) {
                System.out.println("> " + path.getPathPrincipal());
            }
        }
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
                juego.agregarDirectorio(new Directorio(Path.of(new Scanner(System.in).nextLine())));
                break;
            case 3:
                if(juego.getSaveFilePaths().isEmpty())
                    throw new Exception("Error: no hay paths guardados");
                int i = 0;
                System.out.println("Elija cual eliminar. Presione 0 para cancelar");
                for(Directorio path : juego.getSaveFilePaths()){
                    System.out.println(i+1 + ". " + path.getPathPrincipal());
                    i++;
                }
                int eliminado = new Scanner(System.in).nextInt() - 1;
                if(eliminado == -1){
                    break;
                }
                if (eliminado > i || eliminado < 0){
                    System.out.println("Error, path invalido");
                    break;
                }
                System.out.println("Path \" " + juego.getSaveFilePaths().get(eliminado)+ "\" eliminado");
                juego.eliminarDirectorio(juego.getSaveFilePaths().get(i));
                break;
            case 4:
                    JuegoClient.postearJuego(ServerManager.getInstance().getServidorLocal(), juego);
                return true;
            case 5:
                return true;
        }
        return false;
    }
}
