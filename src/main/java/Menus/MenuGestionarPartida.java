package Menus;

import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;
import SubMenus.SubMenuEliminarCheckpoint;

import java.util.Objects;
import java.util.Scanner;

public class MenuGestionarPartida extends Menu{
    Partida partida;
    Juego juego;
    public MenuGestionarPartida(Partida partida, Juego juego){
        this.partida = partida;
        this.juego = juego;
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Gestionando \n > Juego: " + juego.getTitulo() + "\n > Partida: " + partida.getTitulo());
        System.out.println("Elija una opcion:");
        System.out.println("1. Guardar checkpoint");
        System.out.println("2. Cargar ultimo checkpoint");
        System.out.println("3. Ver checkpoints");
        System.out.println("4. Eliminar checkpoints");
        System.out.println("5. Salir");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                if (partida == juego.getPartidaActual()) {
                    System.out.println("Ingrese el nombre del checkpoint (opcional)");
                    String nombre = new Scanner(System.in).nextLine();
                    if(Objects.equals(nombre, ""))
                        partida.crearCheckpoint();
                    else
                        partida.crearCheckpoint(nombre);
                }else{
                    System.out.println("Atencion! La partida seleccionada es <"+partida.getTitulo()+">, la partida actualmente cargada es <"+juego.getPartidaActual().getTitulo()+">\nEsta seguro que desea guardar la partida actual aquí?" );
                    System.out.println("1. Si\n2. No");
                    int r = 0;
                    while (r != 1 && r != 2) {
                        r = new Scanner(System.in).nextInt();
                    }
                    if(r == 1){
                        System.out.println("Ingrese el nombre del checkpoint (opcional)");
                        String nombre = new Scanner(System.in).nextLine();
                        if(Objects.equals(nombre, ""))
                            partida.crearCheckpoint();
                        else
                            partida.crearCheckpoint(nombre);
                        juego.setPartidaActual(partida);
                        System.out.println("La partida actual se ha actualizado a la seleccionada ("+partida.getTitulo()+")");
                    }
                    else{
                        break;
                    }
                }
                break;
            case 2:
                if (partida == juego.getPartidaActual()) {
                    System.out.println("Se ha cargado el ultimo checkpoint");
                    partida.cargarUltimoCheckpoint();
                } else {
                    System.out.println("La partida actual es <"+juego.getPartidaActual().getTitulo()+">. Se ha guardado un checkpoint automaticamente antes de cargar la nueva partida");
                    juego.getPartidaActual().crearCheckpoint();
                    partida.cargarUltimoCheckpoint();
                }
                break;
            case 3:
                System.out.println("Partidas actuales: ");
                int i = 1;
                for(Checkpoint checkpoint : partida.getCheckpoints()){
                    System.out.println(i+". "+checkpoint.getStringReferencia());
                    i++;
                }
                break;
            case 4:
                new SubMenuEliminarCheckpoint(partida).abrirMenu();
                break;
            case 5:
                return true;
        }
        return false;
    }
}
