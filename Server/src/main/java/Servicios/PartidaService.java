package Servicios;

import Juegos.Juego;
import Juegos.Partida;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;

import java.util.Optional;

public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JuegosService juegosService;

    public PartidaService(PartidaRepository partidaRepository, JuegosService juegosService) {
        this.partidaRepository = partidaRepository;
        this.juegosService = juegosService;
    }
    public Partida obtenerPartidaDeJuegoPorTitulo(String juegotitulo, String partida) throws Exception {
        Juego juego;
        juego = juegosService.obtenerJuegoPorTitulo(juegotitulo);
        Optional<Partida> p = juego.getPartidaByTitulo(partida);
        if(p.isPresent()){
            return p.get();
        }
        else
            throw new Exception("No se encontro la partida");
    }

}
