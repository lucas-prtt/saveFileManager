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

}
