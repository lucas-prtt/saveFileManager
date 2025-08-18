package Servicios;

import Juegos.Juego;
import Juegos.Partida;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;

import java.util.Optional;

public class PartidaService {

    private final PartidaRepository partidaRepository;

    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }
    public Partida obtenerPartidaDeJuegoPorTitulo(Juego juego, String partida) throws Exception {
        Optional<Partida> p = juego.getPartidaByTitulo(partida);
        if(p.isPresent()){
            return p.get();
        }
        else
            throw new Exception("No se encontro la partida");
    }
}
