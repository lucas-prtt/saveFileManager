package Juegos;


import java.util.ArrayList;
import java.util.List;

public class Juego {
    String titulo;
    List<Partida> partidas;
    List<String> saveFilePaths;

    public Juego(String titulo){
        this.titulo = titulo;
        this.partidas = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>();
    }
    public String getTitulo() {
        return titulo;
    }
    public List<String> getTitulosPartidas() {
        return partidas.stream().map(Partida::getTitulo).toList();
    }
}
