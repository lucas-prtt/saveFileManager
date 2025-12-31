package servicios;

import domain.Archivos.juego.Directorio;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import org.hibernate.Hibernate;
import repositorios.JuegoRepository;

import java.util.*;

public class JuegosService {
    private final JuegoRepository juegoRepository;
    private final PartidaService partidaService;
    public JuegosService(JuegoRepository juegoRepository, PartidaService partidaService) {
        this.juegoRepository = juegoRepository;
        this.partidaService = partidaService;
    }
    public Directorio obtenerDirectorioPorJuegoEId(String idJuego, String idDirectorio) throws NoSuchElementException, ResourceNotFoundException {
        Optional<Juego> juego = juegoRepository.findById(idJuego);
        if(juego.isEmpty()){
            throw new ResourceNotFoundException("No se encontro el directorio");
        }
        return juego.get().getSaveFilePaths().stream().filter(dir -> Objects.equals(dir.getId(), idDirectorio)).findFirst().orElseThrow();
    }
    public Juego obtenerJuegoPorId(String uuid) throws ResourceNotFoundException {
        return juegoRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));
    }
    //TODO: Implementar paginacion
    public List<Juego> getJuegos () throws ResourceNotFoundException {
        return juegoRepository.findAll();
    }

    public void guardarJuego(Juego juego){
        juegoRepository.save(juego);
    }

    public void eliminarJuego(String juegoId) throws ResourceNotFoundException{
        obtenerJuegoPorId(juegoId);
        juegoRepository.deleteById(juegoId);
    }
    public void eliminarJuego(Juego juego) throws ResourceNotFoundException{
        juegoRepository.deleteById(juego.getId());
    }
    public List<Partida> getPartidas(Juego juego) {
        List<Partida> partidas = juegoRepository.findById(juego.getId()).map(Juego::getPartidas).orElse(new ArrayList<>());
        Hibernate.initialize(partidas);
        return partidas;
    }
    public void agregarPartida(Juego juego, Partida partida) {
        Juego juegoBD = obtenerJuegoPorId(juego.getId());
        juegoBD.agregarPartida(partida);
    }
}

