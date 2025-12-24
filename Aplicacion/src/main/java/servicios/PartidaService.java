package servicios;

import domain.Exceptions.ResourceAlreadyExistsException;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import dto.JuegosConverter.PartidaConverter;
import dto.PartidaDTO;
import repositorios.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JuegosService juegosService;
    private final PartidaConverter partidaConverter;

    public PartidaService(PartidaRepository partidaRepository, JuegosService juegosService, PartidaConverter partidaConverter) {
        this.partidaRepository = partidaRepository;
        this.juegosService = juegosService;
        this.partidaConverter = partidaConverter;
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
    public void guardarPartida(String juegotitulo, PartidaDTO partidaDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException  {
        Juego juego = juegosService.obtenerJuegoPorTitulo(juegotitulo);
        if(juego.getTitulosPartidas().contains(juegotitulo)) {
            throw new ResourceAlreadyExistsException("Partida ya existe con el mismo titulo");
        }
        juego.agregarPartida(partidaConverter.fromDto(partidaDTO));
        juegosService.actualizarJuego(juego);
    }
    public void actualizarPartida(Partida partida){
        partidaRepository.save(partida);
    }
    public void eliminarPartida(String tituloJuego, String tituloPartida) throws ResourceNotFoundException{
        Juego juego = juegosService.obtenerJuegoPorTitulo(tituloJuego);
        if(!juego.getTitulosPartidas().contains(tituloPartida)){
            throw new ResourceNotFoundException("No se encontro la partida");
        }
        juego.eliminarPartidaByTitulo(tituloPartida);
        juegosService.actualizarJuego(juego);
    }
    public void patchPartida(String tituloJuego, String tituloPartida, PartidaDTO patchDTO) throws ResourceNotFoundException{
        Partida partida = obtenerPartidaDeJuegoPorTitulo(tituloJuego, tituloPartida);
        partida.patchWithDto(patchDTO);
        actualizarPartida(partida);
    }
}
