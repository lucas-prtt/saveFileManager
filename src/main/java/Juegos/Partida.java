package Juegos;

import java.util.ArrayList;
import java.util.List;

public class Partida {
    String titulo;
    List<Checkpoint> checkpoints;

    public Partida(String titulo) {
        this.titulo = titulo;
        this.checkpoints = new ArrayList<>();
    }


    public String getTitulo(){
        return titulo;
    }
    public void crearCheckpoint(){
        crearCheckpoint(null);
    };
    public void crearCheckpoint(String nombre){
        //TODO
    }
    public void cargarUltimoCheckpoint(){
        cargarCheckpoint(checkpoints.getLast());
    }
    public void cargarCheckpoint(Checkpoint checkpoint){
        //TODO
    }
    public List<Checkpoint> getCheckpoints(){
        return checkpoints;
    }
    public void eliminarCheckpoint(int index){
        checkpoints.remove(index);
    }
}
