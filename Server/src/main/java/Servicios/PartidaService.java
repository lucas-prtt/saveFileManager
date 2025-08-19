package Servicios;

import Exceptions.ResourceAlreadyExistsException;
import Exceptions.ResourceNotFoundException;
import Juegos.Juego;
import Juegos.Partida;
import Repositorios.PartidaRepository;

import java.util.Optional;

public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JuegosService juegosService;

    public PartidaService(PartidaRepository partidaRepository, JuegosService juegosService) {
        this.partidaRepository = partidaRepository;
        this.juegosService = juegosService;
    }
    public Partida obtenerPartidaDeJuegoPorTitulo(String juegotitulo, String partida) throws ResourceNotFoundException {
        Juego juego;
        juego = juegosService.obtenerJuegoPorTitulo(juegotitulo);
        Optional<Partida> p = juego.getPartidaByTitulo(partida);
        if(p.isPresent()){
            return p.get();
        }
        else
            throw new ResourceNotFoundException("No se encontro la partida");
    }
    public void guardarPartida(String juegotitulo, Partida partida) throws ResourceAlreadyExistsException, ResourceNotFoundException  {
        Juego juego = juegosService.obtenerJuegoPorTitulo(juegotitulo);
        if(juego.getTitulosPartidas().contains(juegotitulo))
            throw new ResourceAlreadyExistsException("Partida ya existe con el mismo titulo");
        juego.agregarPartida(partida);
        juegosService.actualizarJuego(juego);
    }
    public void actualizarPartida(Partida partida){
        partidaRepository.save(partida);
    }
    public void eliminarPartida(String tituloJuego, String tituloPartida) throws ResourceNotFoundException{
        Juego juego = juegosService.obtenerJuegoPorTitulo(tituloJuego);
        if(juego.getTitulosPartidas().contains(tituloPartida)){
            throw new ResourceNotFoundException("No se encontro la partida");
        }
        juego.eliminarPartidaByTitulo(tituloPartida);
        juegosService.actualizarJuego(juego);
    }
}
