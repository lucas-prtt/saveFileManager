package Controllers;

import Juegos.Juego;

import java.util.ArrayList;
import java.util.List;

public class JuegosController {
    List<Juego> juegos;
    static JuegosController instance;

    private JuegosController(){
        juegos = new ArrayList<Juego>();
    }

    public static JuegosController getInstance(){
        if (instance == null)
            instance = new JuegosController();
        return instance;
    }

    public List<Juego> getJuegos() {
        return juegos;
    }

    public void addJuego(Juego juego){
        juegos.add(juego);
    }
    public List<String> getTitulos(){
        return juegos.stream().map(Juego::getTitulo).toList();
    }
}
